package br.com.jfsp.nuit.contadoria.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.jfsp.nuit.contadoria.models.Ipca15;

@Repository
public interface Ipca15Repository extends JpaRepository<Ipca15, Long> {
	
	Optional<Ipca15> findByData(String data);

	Boolean existsByData(String data);
		
	@Query("select max(data) from Ipca15")
	String findMaxData();

}
