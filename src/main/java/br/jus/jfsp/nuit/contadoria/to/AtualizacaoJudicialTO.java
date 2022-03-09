package br.jus.jfsp.nuit.contadoria.to;

import br.jus.jfsp.nuit.contadoria.aspect.ApplyHateoas;
import br.jus.jfsp.nuit.contadoria.controllers.AtualizacaoJudicialController;
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
public class AtualizacaoJudicialTO
        extends BaseTO<AtualizacaoJudicialTO>
        implements ApplyHateoas<AtualizacaoJudicialTO>, Serializable {

    private Double percentual;
    private EMoeda moeda;
    private String descricao;

    public AtualizacaoJudicialTO apply() throws RecordNotFoundException {
        add(linkTo(methodOn(AtualizacaoJudicialController.class).read(id)).withSelfRel());
        return this;
    }

}
