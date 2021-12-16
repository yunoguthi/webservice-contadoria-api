package br.jus.jfsp.nuit.contadoria.to;

import br.jus.jfsp.nuit.contadoria.aspect.ApplyHateoas;
import br.jus.jfsp.nuit.contadoria.controllers.ReajusteBeneficioController;
import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReajusteBeneficioTO
        extends BaseTO<ReajusteBeneficioTO>
        implements ApplyHateoas<ReajusteBeneficioTO>, Serializable {

    private Calendar dataBase;

    private Double integral;

    private Double proporcional;

    public ReajusteBeneficioTO apply() throws RecordNotFoundException {
        add(linkTo(methodOn(ReajusteBeneficioController.class).read(id)).withSelfRel());
        return this;
    }

}
