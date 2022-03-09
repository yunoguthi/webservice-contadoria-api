package br.jus.jfsp.nuit.contadoria.repository;

import br.jus.jfsp.nuit.contadoria.models.TetoContribuicao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Optional;

@Repository
public interface TetoContribuicaoRepository extends JpaRepository<TetoContribuicao, Long> {
	
	Optional<TetoContribuicao> findByData(Calendar data);
	Iterable<TetoContribuicao> findAllByDataLessThanEqualAndDataGreaterThanEqual(Calendar data1, Calendar data2);

	Boolean existsByData(Calendar data);
	
	@Query("select max(data) from TetoContribuicao")
	Calendar findMaxData();

	@Query("SELECT i FROM TetoContribuicao i WHERE CAST(i.valor AS text) LIKE CONCAT('%',:like,'%') OR CAST(i.data AS text) LIKE CONCAT('%',:like,'%')")
	Page<TetoContribuicao> findLikePage(Pageable pageable, @Param("like") String like);

//	@Query("update TetoContribuicao set data = :data set moeda = :moeda")
//	void update(@Param("data") Calendar data, @Param("moeda") EMoeda moeda);

	Iterable<TetoContribuicao> findByMoedaIsNull();

}
