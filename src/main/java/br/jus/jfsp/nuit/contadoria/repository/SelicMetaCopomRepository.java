package br.jus.jfsp.nuit.contadoria.repository;

import br.jus.jfsp.nuit.contadoria.models.SelicMetaCopom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Optional;

@Repository
public interface SelicMetaCopomRepository extends JpaRepository<SelicMetaCopom, Long>{
	
	Optional<SelicMetaCopom> findByData(Calendar data);
	Iterable<SelicMetaCopom> findAllByDataLessThanEqualAndDataGreaterThanEqual(Calendar data1, Calendar data2);

	Boolean existsByData(Calendar data);
	
	@Query("select max(data) from SelicMetaCopom")
	Calendar findMaxData();

}
