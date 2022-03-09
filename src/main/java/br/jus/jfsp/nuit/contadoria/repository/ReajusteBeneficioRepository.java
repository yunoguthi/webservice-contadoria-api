package br.jus.jfsp.nuit.contadoria.repository;

import br.jus.jfsp.nuit.contadoria.models.ReajusteBeneficio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Calendar;
import java.util.Optional;

public interface ReajusteBeneficioRepository extends PagingAndSortingRepository<ReajusteBeneficio, Long> {

	Optional<ReajusteBeneficio> findByData(Calendar data);
	Iterable<ReajusteBeneficio> findAllByDataLessThanEqualAndDataGreaterThanEqual(Calendar data1, Calendar data2);
	Boolean existsByData(Calendar data);
	
	@Query("select max(data) from ReajusteBeneficio")
	Calendar findMaxData();

	@Query("SELECT b FROM ReajusteBeneficio b WHERE CAST(b.valor AS text) LIKE CONCAT('%',:like,'%') OR CAST(b.data AS text) LIKE CONCAT('%',:like,'%')")
	Page<ReajusteBeneficio> findLikePage(Pageable pageable, @Param("like") String like);

}
