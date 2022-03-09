package br.jus.jfsp.nuit.contadoria.repository;

import br.jus.jfsp.nuit.contadoria.models.IndicesCond;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Calendar;
import java.util.Optional;

public interface IndicesCondRepository extends PagingAndSortingRepository<IndicesCond, Long> {

	Optional<IndicesCond> findByData(Calendar data);
	Iterable<IndicesCond> findAllByDataLessThanEqualAndDataGreaterThanEqual(Calendar data1, Calendar data2);
	Boolean existsByData(Calendar data);
	
	@Query("select max(data) from IndicesCond")
	Calendar findMaxData();

	@Query("SELECT b FROM IndicesCond b WHERE CAST(b.valor AS text) LIKE CONCAT('%',:like,'%') OR CAST(b.data AS text) LIKE CONCAT('%',:like,'%')")
	Page<IndicesCond> findLikePage(Pageable pageable, @Param("like") String like);

}
