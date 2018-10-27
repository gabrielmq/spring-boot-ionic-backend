package br.com.cursomc.sbinc.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.cursomc.sbinc.domain.Cidade;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT cidade FROM Cidade cidade "
			+ "WHERE cidade.estado.id = :estadoId "
			+ "ORDER BY cidade.nome")
	List<Cidade> findCidades(@Param("estadoId") Integer estadoId);
}
