package br.jus.jfsp.nuit.contadoria.to;

import br.jus.jfsp.nuit.contadoria.aspect.ApplyHateoas;
import br.jus.jfsp.nuit.contadoria.controllers.AjusteMoedaController;
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
public class AjusteMoedaTO
        extends BaseTO<AjusteMoedaTO>
        implements ApplyHateoas<AjusteMoedaTO>, Serializable {

    private String descricao;

    public AjusteMoedaTO apply() throws RecordNotFoundException {
        add(linkTo(methodOn(AjusteMoedaController.class).read(id)).withSelfRel());
        return this;
    }

}
