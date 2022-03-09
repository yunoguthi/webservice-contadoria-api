package br.jus.jfsp.nuit.contadoria.repository;

import br.jus.jfsp.nuit.contadoria.models.EMoeda;
import br.jus.jfsp.nuit.contadoria.models.IpcaE;
import br.jus.jfsp.nuit.contadoria.models.SalarioMinimo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Optional;

@Repository
public interface SalarioMinimoRepository extends JpaRepository<SalarioMinimo, Long> {
	
	Optional<SalarioMinimo> findByData(Calendar data);
	Iterable<SalarioMinimo> findAllByDataLessThanEqualAndDataGreaterThanEqual(Calendar data1, Calendar data2);

	Boolean existsByData(Calendar data);
	
	@Query("select max(data) from SalarioMinimo")
	Calendar findMaxData();

	@Query("SELECT i FROM SalarioMinimo i WHERE CAST(i.valor AS text) LIKE CONCAT('%',:like,'%') OR CAST(i.data AS text) LIKE CONCAT('%',:like,'%')")
	Page<SalarioMinimo> findLikePage(Pageable pageable, @Param("like") String like);

//	@Query("update SalarioMinimo set data = :data set moeda = :moeda")
//	void update(@Param("data") Calendar data, @Param("moeda") EMoeda moeda);

	Iterable<SalarioMinimo> findByMoedaIsNull();

}
