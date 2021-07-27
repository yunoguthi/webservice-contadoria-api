package br.com.jfsp.nuit.contadoria.repository;

import java.util.Calendar;

import java.util.Optional;

import br.com.jfsp.nuit.contadoria.models.Inpc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.jfsp.nuit.contadoria.models.IpcaE;

@Repository
public interface IpcaERepository extends JpaRepository<IpcaE, Long>{

	Optional<IpcaE> findByData(Calendar data);
	Iterable<IpcaE> findAllByDataLessThanEqualAndDataGreaterThanEqual(Calendar data1, Calendar data2);
	Boolean existsByData(Calendar data);
	
	@Query("select max(data) from IpcaE")
	Calendar findMaxData();

}
