package br.jus.jfsp.nuit.contadoria.repository;

import br.jus.jfsp.nuit.contadoria.models.AjusteMoeda;
import br.jus.jfsp.nuit.contadoria.models.IndicesAtrasados;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Calendar;
import java.util.Optional;

public interface IndicesAtrasadosRepository extends PagingAndSortingRepository<IndicesAtrasados, Long> {

	Optional<IndicesAtrasados> findByData(Calendar data);
	Iterable<IndicesAtrasados> findAllByDataLessThanEqualAndDataGreaterThanEqual(Calendar data1, Calendar data2);
	Boolean existsByData(Calendar data);
	
	@Query("select max(data) from IndicesAtrasados")
	Calendar findMaxData();

	@Query("SELECT b FROM IndicesAtrasados b WHERE CAST(b.valor AS text) LIKE CONCAT('%',:like,'%') OR CAST(b.data AS text) LIKE CONCAT('%',:like,'%')")
	Page<IndicesAtrasados> findLikePage(Pageable pageable, @Param("like") String like);

}
