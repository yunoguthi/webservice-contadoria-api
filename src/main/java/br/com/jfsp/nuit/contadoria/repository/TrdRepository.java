package br.com.jfsp.nuit.contadoria.repository;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.jfsp.nuit.contadoria.models.Trd;

@Repository
public interface TrdRepository extends JpaRepository<Trd, Long> {
	
	Optional<Trd> findByData(Date data);

	Boolean existsByData(Date data);
		
	@Query("select max(data) from Trd")
	Date findMaxData();

}
