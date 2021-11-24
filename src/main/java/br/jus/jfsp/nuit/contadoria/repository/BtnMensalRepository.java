package br.jus.jfsp.nuit.contadoria.repository;

import br.jus.jfsp.nuit.contadoria.models.BtnMensal;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Calendar;
import java.util.Optional;

public interface BtnMensalRepository extends CrudRepository<BtnMensal, Long> {

	Optional<BtnMensal> findByData(Calendar data);
	Iterable<BtnMensal> findAllByDataLessThanEqualAndDataGreaterThanEqual(Calendar data1, Calendar data2);
	Boolean existsByData(Calendar data);
	
	@Query("select max(data) from BtnMensal")
	Calendar findMaxData();
	
}
