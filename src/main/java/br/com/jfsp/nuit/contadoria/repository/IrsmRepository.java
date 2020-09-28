package br.com.jfsp.nuit.contadoria.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.jfsp.nuit.contadoria.models.Irsm;

@Repository
public interface IrsmRepository extends JpaRepository<Irsm, Long> {
	
	Optional<Irsm> findByData(String data);

	Boolean existsByData(String data);
		
	@Query("select max(data) from Irsm")
	String findMaxData();

}
