package br.jus.jfsp.nuit.contadoria.repository;

import br.jus.jfsp.nuit.contadoria.models.Ipca;
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
public interface IpcaRepository extends JpaRepository<Ipca, Long> {
	
	Optional<Ipca> findByData(Calendar data);
	Optional<Ipca> findByDataStr(String dataStr);

	Iterable<Ipca> findAllByDataLessThanEqualAndDataGreaterThanEqual(Calendar data1, Calendar data2);
	Boolean existsByData(Calendar data);
	Boolean existsByDataStr(String dataStr);

	@Query("select max(dataStr) from Ipca")
	String findMaxDataStr();

	@Query("SELECT i FROM Ipca i WHERE CAST(i.valor AS text) LIKE CONCAT('%',:like,'%') OR CAST(i.data AS text) LIKE CONCAT('%',:like,'%') OR CAST(i.variacaoMensal AS text) LIKE CONCAT('%',:like,'%')")
	Page<Ipca> findLikePage(Pageable pageable, @Param("like") String like);


}
