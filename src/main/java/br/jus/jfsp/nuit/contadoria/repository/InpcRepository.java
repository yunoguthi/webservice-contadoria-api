package br.jus.jfsp.nuit.contadoria.repository;

import br.jus.jfsp.nuit.contadoria.models.BtnMensal;
import br.jus.jfsp.nuit.contadoria.models.Inpc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Optional;

@Repository
public interface InpcRepository extends PagingAndSortingRepository<Inpc, Long> {
	
	Optional<Inpc> findByData(Calendar data);
	Optional<Inpc> findByDataStr(String dataStr);
	Iterable<Inpc> findAllByDataLessThanEqualAndDataGreaterThanEqual(Calendar data1, Calendar data2);

	@Query("select max(dataStr) from Inpc")
	String findMaxDataStr();

	@Query("select max(data) from Inpc")
	Calendar findMaxData();

	@Query("SELECT i FROM Inpc i WHERE i.ano LIKE CONCAT('%',:like,'%') OR i.mes LIKE CONCAT('%',:like,'%') OR i.data LIKE CONCAT('%',:like,'%') OR CAST(i.valor AS text) LIKE CONCAT('%',:like,'%') OR CAST(i.variacaoMensal AS text) LIKE CONCAT('%',:like,'%')")
	Page<Inpc> findLikePage(Pageable pageable, @Param("like") String like);

}
