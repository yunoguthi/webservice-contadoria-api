package br.jus.jfsp.nuit.contadoria.to;

import br.jus.jfsp.nuit.contadoria.aspect.ApplyHateoas;
import br.jus.jfsp.nuit.contadoria.controllers.IndicesConsolidadosController;
import br.jus.jfsp.nuit.contadoria.controllers.TrMensalController;
import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Calendar;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
@Setter
@ToString
@EqualsAndHashCode
//@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class IndicesConsolidadosTO
        extends BaseTO<IndicesConsolidadosTO>
        implements ApplyHateoas<IndicesConsolidadosTO>, Serializable {

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

    public IndicesConsolidadosTO apply() throws RecordNotFoundException {
        add(linkTo(methodOn(IndicesConsolidadosController.class).read(id)).withSelfRel());
        return this;
    }

}
