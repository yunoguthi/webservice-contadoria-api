package br.jus.jfsp.nuit.contadoria.repository;

import br.jus.jfsp.nuit.contadoria.models.Ipca15;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Optional;

@Repository
public interface Ipca15Repository extends JpaRepository<Ipca15, Long> {
	
	Optional<Ipca15> findByData(String data);
	Iterable<Ipca15> findAllByDataLessThanEqualAndDataGreaterThanEqual(Calendar data1, Calendar data2);
	Boolean existsByData(String data);
		
	@Query("select max(data) from Ipca15")
	String findMaxData();

}
