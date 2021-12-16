package br.jus.jfsp.nuit.contadoria.repository;

import br.jus.jfsp.nuit.contadoria.models.IpcaE;
import br.jus.jfsp.nuit.contadoria.models.Irsm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Optional;

@Repository
public interface IrsmRepository extends JpaRepository<Irsm, Long> {
	
	Optional<Irsm> findByData(String data);
	Iterable<Irsm> findAllByDataLessThanEqualAndDataGreaterThanEqual(Calendar data1, Calendar data2);
	Boolean existsByData(String data);
		
	@Query("select max(dataStr) from Irsm")
	String findMaxDataStr();

	@Query("SELECT i FROM Irsm i WHERE CAST(i.valor AS text) LIKE CONCAT('%',:like,'%') OR CAST(i.data AS text) LIKE CONCAT('%',:like,'%') OR CAST(i.variacaoMensal AS text) LIKE CONCAT('%',:like,'%')")
	Page<Irsm> findLikePage(Pageable pageable, @Param("like") String like);


}
