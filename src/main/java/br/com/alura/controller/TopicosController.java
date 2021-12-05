package br.com.alura.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.controller.dto.AtualizacaoTopicoForm;
import br.com.alura.controller.dto.DetalhesTopicoDto;
import br.com.alura.controller.dto.TopicoDTO;
import br.com.alura.controller.form.TopicoForm;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.repository.CursoRepository;
import br.com.alura.repository.TopicoRepository;

//ao usar a annotation RestController em lugar
//da Controller, dispensa o uso da @ResponseBody nos métodos usados pelos
//endpoints, pois o spring já entende que o retorno deve ser parte do body da response,
//em lugar da página de destino.
@RestController
//para não ter que repetir a URL em cada método, esta
//anotação sobe para a classe do controlador
@RequestMapping(value="/topicos")
public class TopicosController {
	
	@Autowired
	private TopicoRepository topicoRepository;
	
	@Autowired
	private CursoRepository cursoRepository;
	
	//usar classes de domínio para respostas não é uma boa prática,
	//pois o spring (usando o Jackson por baixo) converte todos os atributos
	//para JSON, e nem sempre queremos/podemos retornar
	//todos os atributos. Em seu lugar, devemos usar DTOs (Data Transfer Objects
	
	//quando passamos parâmetros num método anotado com @GetMapping o spring considera como 
	//obrigatorio, mas podemos usar a anotação @RequestParam explicitamente, que possui a opção 
	//required = true or false
	@GetMapping  //substitui @RequestMapping(method=RequestMethod.GET)
	//O pageable pode ser colocado diretamente com parâmetro do método do endpoint,
	//desde que a annotation @EnableSpringDataWebSupport seja usada na classe principal da aplicação
	//informa ao spring que desejamos armazenar cache para retorno deste método 
	@Cacheable(value = "listaDeTopicos")
	public Page<TopicoDTO> lista(@RequestParam(required = false) String nomeCurso, 
			//podemos definir os valores padrao que serao usados se nao vierem na url com @PageableDefault
			@PageableDefault(sort="id", direction = Direction.DESC, page=0, size=10) Pageable paginacao){
		
		//o spring facilita a paginação do JPA com a interface Pageable
		//também outras opcoes no metodo PageRequest.of, para ordenacao
		//Pageable paginacao = PageRequest.of(pagina, qtd, Direction.ASC, ordenacao);
		
		if(nomeCurso == null) {
			//A classe Page contém a lista encapsulada, e outras informações
			Page<Topico> topicos = topicoRepository.findAll(paginacao);
			return TopicoDTO.convert(topicos);	
		} else {
			Page<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso, paginacao);
			return TopicoDTO.convert(topicos);
		}
		
	}
	
	@RequestMapping("/topicosComQuery")
	public List<TopicoDTO> listaComQueryExplicita(String nomeCurso){
		//Testando chamada de método do repository que usa query explícita
		List<Topico> topicos2 = topicoRepository.consultarPorNomeDoCurso(nomeCurso);
		return TopicoDTO.convertToList(topicos2);
	}
	
	@PostMapping //substitui @RequestMapping(method=RequestMethod.POST)
	//o professor achou melhor criar um dto específico para post, TopicoForm
	//foi adicionado o parâmetro UriComponentsBuilder para facilitar a montagem da URI de retorno
	//a annotation RequestBody indica que os dados vêm no corpo do request
	//
	//validação: a validação poderia se dar com if elses aqui mesmo, mas iria poluir... em lugar disso,
	//podemos usar as validações do Bean Validator do próprio java, que o Spring se integra,
	//colocando anotações na própria classe dto.
	@Transactional
	public ResponseEntity<TopicoDTO> cadastrar(@RequestBody @Valid TopicoForm topicoForm, UriComponentsBuilder uriBuilder) {
		Topico topico = topicoForm.converter(cursoRepository);
		topicoRepository.save(topico);
		
		//Monta a URI do recurso criado para devolver
		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
		//devolve status 201 (Created), a URI do recurso criado com o próprio recurso criado.
		return ResponseEntity.created(uri).body(new TopicoDTO(topico));
	}
	
	@GetMapping("/{id}")
	//Para fazer com que o Spring entenda que é uma variável de path, e não parametro get,
	//anotar com @PathVariable
	public ResponseEntity<DetalhesTopicoDto> detalhar(@PathVariable long id) {
		//trocando getById por findById, para o caso de não existir o registro.
		//Uso da classe container do Java 8 Optional.
		Optional<Topico> topico = topicoRepository.findById(id);
		if(topico.isPresent()) {
			return ResponseEntity.ok(new DetalhesTopicoDto(topico.get()));
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PutMapping("/{id}")
	//esta annotation faz com que após a conslusão os dados sejam persistidos.
	@Transactional
	public ResponseEntity<TopicoDTO> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm form) {
		Optional<Topico> optional = topicoRepository.findById(id);
		if(optional.isPresent()) {
			Topico topico = form.atualizar(id, topicoRepository);
			return ResponseEntity.ok(new TopicoDTO(topico));
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> remover(@PathVariable Long id){
		Optional<Topico> topico = topicoRepository.findById(id);
		if(topico.isPresent()) {
			topicoRepository.deleteById(id);
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
