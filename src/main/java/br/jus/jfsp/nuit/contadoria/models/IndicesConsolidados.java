package br.jus.jfsp.nuit.contadoria.models;

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
	private Calendar dataBase;
	private Double integral;
	private Double proporcional;
	private Double multiplicadorMoeda;
	private Double ajusteMoeda;
	private Double indiceAtualizado;
	private Double indiceAcumulado;
	private Double indiceRes134;
	private Double indiceRes134Acumulado;
	private Double indiceSalarios;
	private Double indiceSalariosAcumulado;
	private Double ipca;
	private Double ipcaE;
	private Double indiceCondenatorias;
	private Double indiceCondenatoriasAcumulado;
	private Double selic;
	private Double juros;
	private Double jurosAlt;

}
