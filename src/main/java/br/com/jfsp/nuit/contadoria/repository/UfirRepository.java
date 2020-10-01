package br.com.jfsp.nuit.contadoria.repository;

import java.util.Calendar;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.jfsp.nuit.contadoria.models.Ufir;
import br.com.jfsp.nuit.contadoria.models.Urv;

@Repository
public interface UfirRepository extends JpaRepository<Ufir, Long>{

	Optional<Ufir> findByData(Calendar data);
	
	Boolean existsByData(Calendar data);
	
	@Query("select max(data) from Ufir")
	Calendar findMaxData();

}
