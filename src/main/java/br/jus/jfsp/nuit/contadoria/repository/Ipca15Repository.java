package br.jus.jfsp.nuit.contadoria.repository;

import br.jus.jfsp.nuit.contadoria.models.BtnMensal;
import br.jus.jfsp.nuit.contadoria.models.Ipca15;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Optional;

@Repository
public interface Ipca15Repository extends JpaRepository<Ipca15, Long> {
	
	Optional<Ipca15> findByData(String data);
	Iterable<Ipca15> findAllByDataLessThanEqualAndDataGreaterThanEqual(Calendar data1, Calendar data2);
	Boolean existsByData(String data);
		
	@Query("select max(data) from Ipca15")
	Calendar findMaxData();

	@Query("SELECT i FROM Ipca15 i WHERE CAST(i.valor AS text) LIKE CONCAT('%',:like,'%') OR CAST(i.data AS text) LIKE CONCAT('%',:like,'%') OR CAST(i.variacaoMensal AS text) LIKE CONCAT('%',:like,'%')")
	Page<Ipca15> findLikePage(Pageable pageable, @Param("like") String like);

}
