package br.com.alura.controller.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import br.com.alura.forum.modelo.Topico;

//Uma classe DTO - Data Transfer Object 
//serve basicamente para trasmitir dados
//selecionados entre camadas da aplicação,
//expondo apenas os dados desejados.
public class TopicoDTO {
	private Long id;
	private String titulo;
	private String mensagem;
	private LocalDateTime dataCriacao = LocalDateTime.now();

	public TopicoDTO(Topico topico) {
		this.id = topico.getId();
		this.titulo = topico.getTitulo();
		this.mensagem = topico.getMensagem();
		this.dataCriacao = topico.getDataCriacao();
	}
	
	public Long getId() {
		return id;
	}
	public String getTitulo() {
		return titulo;
	}
	public String getMensagem() {
		return mensagem;
	}
	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}
	
	public static Page<TopicoDTO> convert(Page<Topico> topicos) {
		
		//para converter para o DTO, a classe page conta com o método Map, e aqui usamos 
		//o operador method reference do java 8.
		return topicos.map(TopicoDTO::new);	
	}
	
	public static List<TopicoDTO> convertToList(List<Topico> topicos) {
		//Uso do método stream do java 8: evita criação de um for para alimentar a nova lista
		//o metodo stream fornece uma stream com a coleção topicos como fonte
		//o metodo map retorna uma outra stream com o resultado da aplicação da função passada como argumento, no caso está chamando o construtor que recebe Topico como argumento
		//o método collect aplica para cada resultado da stream resultante a função passada como argument, restornando neste caso uma lista de TopicoDTO.
		return topicos.stream().map(TopicoDTO::new).collect(Collectors.toList());
	}
}
