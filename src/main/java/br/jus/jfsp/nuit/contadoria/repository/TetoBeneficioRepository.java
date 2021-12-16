package br.jus.jfsp.nuit.contadoria.repository;

import br.jus.jfsp.nuit.contadoria.models.TetoBeneficio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Optional;

@Repository
public interface TetoBeneficioRepository extends JpaRepository<TetoBeneficio, Long> {
	
	Optional<TetoBeneficio> findByData(Calendar data);
	Iterable<TetoBeneficio> findAllByDataLessThanEqualAndDataGreaterThanEqual(Calendar data1, Calendar data2);

	Boolean existsByData(Calendar data);
	
	@Query("select max(data) from TetoBeneficio")
	Calendar findMaxData();

	@Query("SELECT i FROM TetoBeneficio i WHERE CAST(i.valor AS text) LIKE CONCAT('%',:like,'%') OR CAST(i.data AS text) LIKE CONCAT('%',:like,'%')")
	Page<TetoBeneficio> findLikePage(Pageable pageable, @Param("like") String like);

//	@Query("update TetoBeneficio set data = :data set moeda = :moeda")
//	void update(@Param("data") Calendar data, @Param("moeda") EMoeda moeda);

	Iterable<TetoBeneficio> findByMoedaIsNull();

}
