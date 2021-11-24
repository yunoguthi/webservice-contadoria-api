package br.jus.jfsp.nuit.contadoria.repository;

import br.jus.jfsp.nuit.contadoria.models.Irsm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Optional;

@Repository
public interface IrsmRepository extends JpaRepository<Irsm, Long> {
	
	Optional<Irsm> findByData(String data);
	Iterable<Irsm> findAllByDataLessThanEqualAndDataGreaterThanEqual(Calendar data1, Calendar data2);
	Boolean existsByData(String data);
		
	@Query("select max(data) from Irsm")
	String findMaxData();

}
