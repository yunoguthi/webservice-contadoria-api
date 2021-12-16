package br.jus.jfsp.nuit.contadoria.repository;

import br.jus.jfsp.nuit.contadoria.models.Ipca15;
import br.jus.jfsp.nuit.contadoria.models.IpcaE;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Optional;

@Repository
public interface IpcaERepository extends JpaRepository<IpcaE, Long>{

	Optional<IpcaE> findByData(Calendar data);
	Iterable<IpcaE> findAllByDataLessThanEqualAndDataGreaterThanEqual(Calendar data1, Calendar data2);
	Boolean existsByData(Calendar data);
	
	@Query("select max(data) from IpcaE")
	Calendar findMaxData();

	@Query("SELECT i FROM IpcaE i WHERE CAST(i.valor AS text) LIKE CONCAT('%',:like,'%') OR CAST(i.data AS text) LIKE CONCAT('%',:like,'%')")
	Page<IpcaE> findLikePage(Pageable pageable, @Param("like") String like);

}
