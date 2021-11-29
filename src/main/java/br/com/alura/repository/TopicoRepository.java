package br.com.alura.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.alura.forum.modelo.Topico;

//Em lugar de usar o padrao DAO com EntityManager, o spring nos fornece
//a interface JpaRepository, que contem diversos métodos prontos para acesso
//aos dados
//Os tipos informados no generics são o objeto persistido e o tipo de dado do id
public interface TopicoRepository extends JpaRepository<Topico, Long>{
	
	//Caso precisemos usar filtros, o Sring Data JPA também facilita!
	//nem precisa implementar a query, basta seguir o padrao
	//findByEntidadeAtributo[0]..Atributo[n]
	//que ele vai montar a query com os joins automagicamente!!
	public Page<Topico> findByCursoNome(String nomeCurso, Pageable paginacao);
	
	//Obs. Caso exista um atributo CursoNome na classe Topico, o Spring iria priorizar ele!
	//mas para indicar que queremos pegar o nome da entidade Curso, bastaria usar assim
	// findByCurso_Nome (underline)

	//Contudo, caso desejemos personalizar a query, ainda podemos
	//usar a seguinte forma:
	@Query("Select t from Topico t where t.curso.nome = :nomeCurso")
	public List<Topico> consultarPorNomeDoCurso(@Param("nomeCurso") String nomeCurso);
}
