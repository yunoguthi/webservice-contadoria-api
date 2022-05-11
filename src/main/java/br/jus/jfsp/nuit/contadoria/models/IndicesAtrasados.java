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
import java.math.BigDecimal;
import java.util.Calendar;

@Entity
@Table(name = "indices_atrasados", indexes = @Index(
		name="idx_indices_atrasados", unique=true, columnList = "data"
))
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Audited
@AuditTable(value = "indices_atrasados_audit")
@EntityListeners(AuditingEntityListener.class)
public class IndicesAtrasados extends BaseIndice {

	@PrePersist
	public void onPrePersist() {

	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "gen_indices_atrasados")
	private Long id;

//	public IndicesAtrasados(BigDecimal indice, String descricao, Calendar data) {
//		super();
//		this.data = data;
//		this.descricao = descricao;
//		this.indice = indice;
//	}

}
