package br.com.alura.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.alura.forum.modelo.Topico;

//Em lugar de usar o padrao DAO com EntityManager, o spring nos fornece
//a interface JpaRepository, que contem diversos métodos prontos para acesso
//aos dados
//Os tipos informados no generics são o objeto persistido e o tipo de dado do id
public interface TopicoRepository extends JpaRepository<Topico, Long>{
	
}
