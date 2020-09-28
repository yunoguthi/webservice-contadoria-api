package br.com.jfsp.nuit.contadoria.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.jfsp.nuit.contadoria.models.Inpc;

@Repository
public interface InpcRepository extends JpaRepository<Inpc, Long> {
	
	Optional<Inpc> findByData(String data);

	Boolean existsByData(String data);
		
	@Query("select max(data) from Inpc")
	String findMaxData();

}
