package br.jus.jfsp.nuit.contadoria.repository;

import br.jus.jfsp.nuit.contadoria.models.Ipca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Optional;

@Repository
public interface IpcaRepository extends JpaRepository<Ipca, Long> {
	
	Optional<Ipca> findByData(String data);
	Iterable<Ipca> findAllByDataLessThanEqualAndDataGreaterThanEqual(Calendar data1, Calendar data2);
	Boolean existsByData(String data);
		
	@Query("select max(data) from Ipca")
	String findMaxData();

}
