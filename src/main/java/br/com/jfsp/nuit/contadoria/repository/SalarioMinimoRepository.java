package br.com.jfsp.nuit.contadoria.repository;

import java.util.Calendar;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.jfsp.nuit.contadoria.models.SalarioMinimo;

@Repository
public interface SalarioMinimoRepository extends JpaRepository<SalarioMinimo, Long> {
	
	Optional<SalarioMinimo> findByData(Calendar data);
	
	Boolean existsByData(Calendar data);
	
	@Query("select max(data) from SalarioMinimo")
	Calendar findMaxData();

}
