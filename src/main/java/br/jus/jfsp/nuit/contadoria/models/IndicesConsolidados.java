package br.jus.jfsp.nuit.contadoria.models;

import br.jus.jfsp.nuit.contadoria.util.ManipulaData;
import lombok.*;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

@Entity
@Data
//@Getter
//@Setter
@Table(name = "indices_consolidados", indexes = @Index(
		name="idx_indices_consolidados", unique=true, columnList = "ano,mes"
))
//@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
@Audited
@AuditTable(value = "indices_consolidados_audit")
@EntityListeners(AuditingEntityListener.class)
public class IndicesConsolidados {

	@PrePersist
	public void onPrePersist() {

	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	protected Date createdDate;

	@LastModifiedDate
	@Temporal(TemporalType.TIMESTAMP)
	protected Date modifiedDate;

	@CreatedBy
	public String createdBy;

	@LastModifiedBy
	protected String modifiedBy;

	@Temporal(TemporalType.DATE)
	protected Calendar data;

	protected Double valor;

	private String ano;

	private String mes;

	private Boolean dataBase;
	private Double salarioMinimo;
	private Double salarioMinimoReferencia;
	private Double tetoContribuicao;
	private Double tetoBeneficio;
	private Double integral;
	private Double proporcional;
	private Double multiplicadorMoeda;
	private Double ajusteMoeda;
	@Column(precision = 40, scale = 20)
	private BigDecimal indiceAtualizado;
	@Column(precision = 40, scale = 20)
	private BigDecimal indiceAcumulado;
	@Column(precision = 40, scale = 20)
	private BigDecimal indiceRes134;
	@Column(precision = 40, scale = 20)
	private BigDecimal indiceRes134Acumulado;
	@Column(precision = 40, scale = 20)
	private BigDecimal indiceSalarios;
	@Column(precision = 40, scale = 20)
	private BigDecimal indiceSalariosAcumulado;
	private Float ipca;
	private Double ipcaE;
	@Column(precision = 40, scale = 20)
	private BigDecimal indiceCondenatorias;
	@Column(precision = 40, scale = 20)
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

		DecimalFormat df = new DecimalFormat("#.###################");
		String salarioMinimoStr = salarioMinimo!=null?salarioMinimo.doubleValue() + "":"0";

		String salarioMinimoReferenciaStr = salarioMinimoReferencia!=null?salarioMinimoReferencia.doubleValue() + "":"0";
		String tetoContribuicaoStr = tetoContribuicao!=null?tetoContribuicao.doubleValue() + "":"0";
		String tetoBeneficioStr = tetoBeneficio!=null?tetoBeneficio.doubleValue() + "":"0";
		String integralStr = integral!=null?integral.doubleValue() + "":"0";
		String proporcionalStr = proporcional!=null?proporcional.doubleValue() + "":"0";
		String multiplicadorMoedaStr = 		multiplicadorMoeda!=null?df.format(multiplicadorMoeda) + "":"0";
		String ajusteMoedaStr = 	ajusteMoeda!=null?df.format(ajusteMoeda) + "":"0";
		String indiceAtualizadoStr = 		indiceAtualizado!=null?indiceAtualizado.doubleValue() + "":"0";
		String indiceAcumuladoStr = indiceAcumulado!=null?df.format(indiceAcumulado) + "":"0";
		String indiceRes134Str = indiceRes134!=null?df.format(indiceRes134) + "":"0";
		String indiceRes134AcumuladoStr = 		indiceRes134Acumulado!=null?df.format(indiceRes134Acumulado) + "":"0";
		String indiceSalariosStr = 	indiceSalarios!=null?df.format(indiceSalarios) + "":"0";
		String indiceSalariosAcumuladoStr = indiceSalariosAcumulado!=null?df.format(indiceSalariosAcumulado) + "":"0";
		String ipcaStr = ipca!=null?ipca.doubleValue() + "":"0";
		String ipcaEStr = ipcaE!=null?ipcaE.doubleValue() + "":"0";
		String indiceCondenatoriasStr = 	indiceCondenatorias!=null?df.format(indiceCondenatorias) + "":"0";
		String indiceCondenatoriasAcumuladoStr = 		indiceCondenatoriasAcumulado!=null?df.format(indiceCondenatoriasAcumulado) + "":"0";
		String selicStr = 		selic!=null?selic.doubleValue() + "":"0";
		String jurosStr = 		juros!=null?juros.doubleValue() + "":"0";
		String jurosAltStr = jurosAlt!=null?df.format(jurosAlt) + "":"0";

		String retorno =
				"01/" + mesStr  + "/" + ManipulaData.getAno(ManipulaData.toDate(data)) + ";" +
						dataBase + ";" +
						salarioMinimoStr + ";" +
						salarioMinimoReferenciaStr + ";" +
						tetoContribuicaoStr + ";" +
						tetoBeneficioStr+ ";" +
						integralStr+ ";" +
						proporcionalStr+ ";" +
						multiplicadorMoedaStr+ ";" +
						ajusteMoedaStr+ ";" +
						indiceAtualizadoStr+ ";" +
						indiceAcumuladoStr+ ";" +
						indiceRes134Str+ ";" +
						indiceRes134AcumuladoStr+ ";" +
						indiceSalariosStr+ ";" +
						indiceSalariosAcumuladoStr+ ";" +
						ipcaStr+ ";" +
						ipcaEStr+ ";" +
						indiceCondenatoriasStr+ ";" +
						indiceCondenatoriasAcumuladoStr+ ";" +
						selicStr+ ";" +
						jurosStr+ ";" +
						jurosAltStr
				;

		return
				retorno.replaceAll("null", "0").replace(",", ".").trim();
	}

//	@Override
//	public boolean equals(Object o) {
//		System.out.println("equals");
//		if (!(o instanceof IndicesConsolidados)) {
//			return false;
//		}
//		IndicesConsolidados indicesConsolidados = (IndicesConsolidados) o;
//		if (this.getMes().equals(indicesConsolidados.getMes()) &&
//				this.getAno().equals(indicesConsolidados.getAno())) {
//			return true;
//		}
//		return false;
//	}



}
