package br.jus.jfsp.nuit.contadoria.repository;

import br.jus.jfsp.nuit.contadoria.models.Trd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Optional;

@Repository
public interface TrdRepository extends JpaRepository<Trd, Long> {
	
	Optional<Trd> findByData(Calendar data);
	Iterable<Trd> findAllByDataLessThanEqualAndDataGreaterThanEqual(Calendar data1, Calendar data2);

	Boolean existsByData(Calendar data);
		
	@Query("select max(data) from Trd")
	Calendar findMaxData();

}
