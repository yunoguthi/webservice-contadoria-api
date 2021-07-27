package br.com.jfsp.nuit.contadoria.repository;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import br.com.jfsp.nuit.contadoria.models.Inpc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.jfsp.nuit.contadoria.models.Trd;

@Repository
public interface TrdRepository extends JpaRepository<Trd, Long> {
	
	Optional<Trd> findByData(Calendar data);
	Iterable<Trd> findAllByDataLessThanEqualAndDataGreaterThanEqual(Calendar data1, Calendar data2);

	Boolean existsByData(Calendar data);
		
	@Query("select max(data) from Trd")
	Calendar findMaxData();

}
