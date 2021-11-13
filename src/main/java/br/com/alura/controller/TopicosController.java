package br.com.alura.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.controller.dto.TopicoDTO;
import br.com.alura.forum.modelo.Curso;
import br.com.alura.forum.modelo.Topico;

//ao usar a annotation RestController em lugar
//da Controller, dispensa o uso da @ResponseBody nos métodos usados pelos
//endpoints, pois o spring já entende que o retorno deve ser parte do body da response,
//em lugar da página de destino.
@RestController
public class TopicosController {
	
	@RequestMapping("/topicos")
	//usar classes de domínio para respostas não é uma boa prática,
	//pois o spring (usando o Jackson por baixo) converte todos os atributos
	//para JSON, e nem sempre queremos/podemos retornar
	//todos os atributos. Em seu lugar, devemos usar DTOs (Data Transfer Objects
	public List<TopicoDTO> lista(){

		//mock de data source
		Topico topico = new Topico("duvida", "duvida com spring", new Curso("Spring", "Programação"));
		
		List<Topico> topicos = Arrays.asList(topico, topico, topico);
		
		return TopicoDTO.convert(topicos);
	}
}
