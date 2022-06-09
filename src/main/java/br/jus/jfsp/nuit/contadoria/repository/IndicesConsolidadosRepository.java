package br.jus.jfsp.nuit.contadoria.repository;

import br.jus.jfsp.nuit.contadoria.models.IndicesConsolidados;
import br.jus.jfsp.nuit.contadoria.models.Inpc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Optional;

@Repository
public interface IndicesConsolidadosRepository extends PagingAndSortingRepository<IndicesConsolidados, Long> {
	
	Optional<IndicesConsolidados> findByData(Calendar data);
	Iterable<IndicesConsolidados> findAllByDataLessThanEqualAndDataGreaterThanEqual(Calendar data1, Calendar data2);
	Boolean existsByData(Calendar data);

	Optional<IndicesConsolidados> findByMesAndAno(String mes, String ano);

	Boolean existsByMesAndAno(String mes, String ano);


	@Query("select max(data) from IndicesConsolidados")
	Calendar findMaxData();

	@Query("SELECT i FROM IndicesConsolidados i WHERE i.data LIKE CONCAT('%',:like,'%') OR CAST(i.valor AS text) LIKE CONCAT('%',:like,'%')")
	Page<IndicesConsolidados> findLikePage(Pageable pageable, @Param("like") String like);

}
