package br.jus.jfsp.nuit.contadoria.to;

import br.jus.jfsp.nuit.contadoria.aspect.ApplyHateoas;
import br.jus.jfsp.nuit.contadoria.controllers.TetoBeneficioController;
import br.jus.jfsp.nuit.contadoria.controllers.TetoContribuicaoController;
import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.EMoeda;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

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
public class TetoBeneficioTO
        extends BaseTO<TetoBeneficioTO>
        implements ApplyHateoas<TetoBeneficioTO>, Serializable {

    private EMoeda moeda;

    public TetoBeneficioTO apply() throws RecordNotFoundException {
        add(linkTo(methodOn(TetoBeneficioController.class).read(id)).withSelfRel());
        return this;
    }

}
