package br.com.jfsp.nuit.contadoria.repository;

import java.util.Calendar;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.jfsp.nuit.contadoria.models.TrMensal;

@Repository
public interface TrMensalRepository extends JpaRepository<TrMensal, Long> {
	
	Optional<TrMensal> findByData(Calendar data);
	
	Boolean existsByData(Calendar data);
	
	@Query("select max(data) from TrMensal")
	Calendar findMaxData();

	
}
