package br.com.alura.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.controller.dto.TopicoDTO;
import br.com.alura.forum.modelo.Curso;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.repository.TopicoRepository;

//ao usar a annotation RestController em lugar
//da Controller, dispensa o uso da @ResponseBody nos métodos usados pelos
//endpoints, pois o spring já entende que o retorno deve ser parte do body da response,
//em lugar da página de destino.
@RestController
public class TopicosController {
	
	@Autowired
	private TopicoRepository topicoRepository;
	
	@RequestMapping("/topicos")
	//usar classes de domínio para respostas não é uma boa prática,
	//pois o spring (usando o Jackson por baixo) converte todos os atributos
	//para JSON, e nem sempre queremos/podemos retornar
	//todos os atributos. Em seu lugar, devemos usar DTOs (Data Transfer Objects
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
}
