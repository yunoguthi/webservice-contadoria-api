package br.com.jfsp.nuit.contadoria.repository;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import br.com.jfsp.nuit.contadoria.models.BtnMensal;

public interface BtnMensalRepository extends CrudRepository<BtnMensal, Long> {

	Optional<BtnMensal> findByData(Calendar data);
	Iterable<BtnMensal> findAllByDataLessThanEqualAndDataGreaterThanEqual(Calendar data1, Calendar data2);
	Boolean existsByData(Calendar data);
	
	@Query("select max(data) from BtnMensal")
	Calendar findMaxData();
	
}
