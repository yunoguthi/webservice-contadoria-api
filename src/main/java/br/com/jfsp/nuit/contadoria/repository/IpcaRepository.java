package br.com.jfsp.nuit.contadoria.repository;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.jfsp.nuit.contadoria.models.Ipca;

@Repository
public interface IpcaRepository extends JpaRepository<Ipca, Long> {
	
	Optional<Ipca> findByData(String data);

	Boolean existsByData(String data);
		
	@Query("select max(data) from Ipca")
	String findMaxData();

}
