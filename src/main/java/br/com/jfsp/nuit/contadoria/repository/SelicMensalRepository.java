package br.com.jfsp.nuit.contadoria.repository;

import java.util.Calendar;
import java.util.Optional;

import br.com.jfsp.nuit.contadoria.models.Inpc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.jfsp.nuit.contadoria.models.SelicMensal;

@Repository
public interface SelicMensalRepository extends JpaRepository<SelicMensal, Long>{

	Optional<SelicMensal> findByData(Calendar data);
	Iterable<SelicMensal> findAllByDataLessThanEqualAndDataGreaterThanEqual(Calendar data1, Calendar data2);

	Boolean existsByData(Calendar data);
	
	@Query("select max(data) from SelicMensal")
	Calendar findMaxData();
	
}
