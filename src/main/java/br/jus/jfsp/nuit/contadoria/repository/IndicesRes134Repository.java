package br.jus.jfsp.nuit.contadoria.repository;

import br.jus.jfsp.nuit.contadoria.models.IndicesRes134;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Calendar;
import java.util.Optional;

public interface IndicesRes134Repository extends PagingAndSortingRepository<IndicesRes134, Long> {

	Optional<IndicesRes134> findByData(Calendar data);
	Iterable<IndicesRes134> findAllByDataLessThanEqualAndDataGreaterThanEqual(Calendar data1, Calendar data2);
	Boolean existsByData(Calendar data);
	
	@Query("select max(data) from IndicesRes134")
	Calendar findMaxData();

	@Query("SELECT b FROM IndicesRes134 b WHERE CAST(b.valor AS text) LIKE CONCAT('%',:like,'%') OR CAST(b.data AS text) LIKE CONCAT('%',:like,'%')")
	Page<IndicesRes134> findLikePage(Pageable pageable, @Param("like") String like);

}
