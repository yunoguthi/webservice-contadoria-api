package br.jus.jfsp.nuit.contadoria.models;

import br.jus.jfsp.nuit.contadoria.util.ManipulaData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Calendar;

@Entity
@Table(name = "indices_consolidados", indexes = @Index(
		name="idx_indices_consolidados", unique=true, columnList = "data"
))
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Audited
@AuditTable(value = "indices_consolidados_audit")
@EntityListeners(AuditingEntityListener.class)
public class IndicesConsolidados extends BaseEntity {

	@PrePersist
	public void onPrePersist() {

	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private Double salarioMinimo;
	private Double salarioMinimoReferencia;
	private Double tetoContribuicao;
	private Double tetoBeneficio;
	private Double integral;
	private Double proporcional;
	private Double multiplicadorMoeda;
	private Double ajusteMoeda;
	private BigDecimal indiceAtualizado;
	private BigDecimal indiceAcumulado;
	private BigDecimal indiceRes134;
	private BigDecimal indiceRes134Acumulado;
	private BigDecimal indiceSalarios;
	private BigDecimal indiceSalariosAcumulado;
	private Double ipca;
	private Double ipcaE;
	private BigDecimal indiceCondenatorias;
	private BigDecimal indiceCondenatoriasAcumulado;
	private Double selic;
	private Double juros;
	private Double jurosAlt;

	@Override
	public String toString() {
//		return "IndicesConsolidados{" +
//				"data=" + data +
//				", salarioMinimo=" + salarioMinimo +
//				", salarioMinimoReferencia=" + salarioMinimoReferencia +
//				", tetoContribuicao=" + tetoContribuicao +
//				", tetoBeneficio=" + tetoBeneficio +
//				", integral=" + integral +
//				", proporcional=" + proporcional +
//				", multiplicadorMoeda=" + multiplicadorMoeda +
//				", ajusteMoeda=" + ajusteMoeda +
//				", indiceAtualizado=" + indiceAtualizado +
//				", indiceAcumulado=" + indiceAcumulado +
//				", indiceRes134=" + indiceRes134 +
//				", indiceRes134Acumulado=" + indiceRes134Acumulado +
//				", indiceSalarios=" + indiceSalarios +
//				", indiceSalariosAcumulado=" + indiceSalariosAcumulado +
//				", ipca=" + ipca +
//				", ipcaE=" + ipcaE +
//				", indiceCondenatorias=" + indiceCondenatorias +
//				", indiceCondenatoriasAcumulado=" + indiceCondenatoriasAcumulado +
//				", selic=" + selic +
//				", juros=" + juros +
//				", jurosAlt=" + jurosAlt +
//				'}';

		int mes = ManipulaData.getMes(ManipulaData.toDate(data)) + 1;

		String mesStr = mes<10 ? "0"+mes : mes+"";

		return
				("01/" + mesStr  + "/" + ManipulaData.getAno(ManipulaData.toDate(data)) + ";" +
				salarioMinimo+ ";" +
				salarioMinimoReferencia + ";" +
				tetoContribuicao + ";" +
				tetoBeneficio + ";" +
				integral + ";" +
				proporcional + ";" +
				multiplicadorMoeda + ";" +
				ajusteMoeda + ";" +
				indiceAtualizado + ";" +
				indiceAcumulado + ";" +
				indiceRes134 + ";" +
				indiceRes134Acumulado + ";" +
				indiceSalarios + ";" +
				indiceSalariosAcumulado + ";" +
				ipca + ";" +
				ipcaE + ";" +
				indiceCondenatorias + ";" +
				indiceCondenatoriasAcumulado + ";" +
				selic + ";" +
				juros + ";" +
				jurosAlt).replaceAll("null", "-").replace(".", ",");
	}
}
