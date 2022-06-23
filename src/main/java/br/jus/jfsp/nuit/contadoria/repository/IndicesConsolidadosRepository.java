package br.jus.jfsp.nuit.contadoria.repository;

import br.jus.jfsp.nuit.contadoria.models.IndicesConsolidados;
import br.jus.jfsp.nuit.contadoria.models.Inpc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Optional;

@Repository
public interface IndicesConsolidadosRepository extends PagingAndSortingRepository<IndicesConsolidados, Long> {
	
	Optional<IndicesConsolidados> findByData(Calendar data);
	Iterable<IndicesConsolidados> findAllByDataLessThanEqualAndDataGreaterThanEqual(Calendar data1, Calendar data2);
	Boolean existsByData(Calendar data);

	Optional<IndicesConsolidados> findByMesAndAno(String mes, String ano);

	@Query("select case when count(i)> 0 then true else false end from IndicesConsolidados i where i.ano=:ano and i.mes=:mes")
	Boolean existsByMesAndAno(@Param("mes") String mes, @Param("ano") String ano);


	@Query("select max(data) from IndicesConsolidados")
	Calendar findMaxData();

	@Query("SELECT i FROM IndicesConsolidados i WHERE i.data LIKE CONCAT('%',:like,'%') OR CAST(i.valor AS text) LIKE CONCAT('%',:like,'%')")
	Page<IndicesConsolidados> findLikePage(Pageable pageable, @Param("like") String like);

	@Modifying
	@Query("UPDATE IndicesConsolidados i set i.salarioMinimo=:salarioMinimo WHERE i.ano=:ano and i.mes=:mes")
	Integer updateSalarioMinimo(@Param("mes") String mes, @Param("ano") String ano, @Param("salarioMinimo") Double salarioMinimo);


	@Modifying(clearAutomatically = true)
	@Query("update IndicesConsolidados i set i.salarioMinimo = :salarioMinimo WHERE i.ano=:ano and i.mes=:mes")
	void setSalarioMinimo(@Param("mes") String mes, @Param("ano") String ano, @Param("salarioMinimo") Double salarioMinimo);


	@Modifying(clearAutomatically = true)
	@Query("update IndicesConsolidados i set i.salarioMinimo = :salarioMinimo WHERE i.id=:id")
	void setSalarioMinimo(@Param("id") Long id, @Param("salarioMinimo") Double salarioMinimo);

	@Modifying
	@Query("update IndicesConsolidados i set i.salarioMinimo=1 where id=1")
	void mudaTudo();



}
