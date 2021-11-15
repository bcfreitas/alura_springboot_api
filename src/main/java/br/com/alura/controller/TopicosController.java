package br.com.alura.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

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
	@GetMapping  //substitui @RequestMapping(method=RequestMethod.GET)
	public List<TopicoDTO> lista(String nomeCurso){
		if(nomeCurso == null) {
			List<Topico> topicos = topicoRepository.findAll();
			return TopicoDTO.convert(topicos);	
		} else {
			List<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso);
			return TopicoDTO.convert(topicos);
		}
		
	}
	
	@RequestMapping("/topicosComQuery")
	public List<TopicoDTO> listaComQueryExplicita(String nomeCurso){
		//Testando chamada de método do repository que usa query explícita
		List<Topico> topicos2 = topicoRepository.consultarPorNomeDoCurso(nomeCurso);
		return TopicoDTO.convert(topicos2);
	}
	
	@PostMapping //substitui @RequestMapping(method=RequestMethod.POST)
	//o professor achou melhor criar um dto específico para post, TopicoForm
	//foi adicionado o parâmetro UriComponentsBuilder para facilitar a montagem da URI de retorno
	//a annotation RequestBody indica que os dados vêm no corpo do request
	public ResponseEntity<TopicoDTO> cadastrar(@RequestBody TopicoForm topicoForm, UriComponentsBuilder uriBuilder) {
		Topico topico = topicoForm.converter(cursoRepository);
		topicoRepository.save(topico);
		
		//Monta a URI do recurso criado para devolver
		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
		//devolve status 201 (Created), a URI do recurso criado com o próprio recurso criado.
		return ResponseEntity.created(uri).body(new TopicoDTO(topico));
	}
}
