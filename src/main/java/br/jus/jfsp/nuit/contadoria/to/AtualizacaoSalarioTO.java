package br.jus.jfsp.nuit.contadoria.to;

import br.jus.jfsp.nuit.contadoria.aspect.ApplyHateoas;
import br.jus.jfsp.nuit.contadoria.controllers.AtualizacaoSalarioController;
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
public class AtualizacaoSalarioTO
        extends BaseTO<AtualizacaoSalarioTO>
        implements ApplyHateoas<AtualizacaoSalarioTO>, Serializable {

    private Double percentual;
    private EMoeda moeda;
    private String descricao;

    public AtualizacaoSalarioTO apply() throws RecordNotFoundException {
        add(linkTo(methodOn(AtualizacaoSalarioController.class).read(id)).withSelfRel());
        return this;
    }

}
