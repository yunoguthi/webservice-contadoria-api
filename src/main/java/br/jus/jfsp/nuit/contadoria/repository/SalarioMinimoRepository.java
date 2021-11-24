package br.jus.jfsp.nuit.contadoria.repository;

import br.jus.jfsp.nuit.contadoria.models.SalarioMinimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

}
