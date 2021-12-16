package br.jus.jfsp.nuit.contadoria.to;

import br.jus.jfsp.nuit.contadoria.aspect.ApplyHateoas;
import br.jus.jfsp.nuit.contadoria.controllers.SelicMetaCopomController;
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
public class SelicMetaCopomTO
        extends BaseTO<SelicMetaCopomTO>
        implements ApplyHateoas<SelicMetaCopomTO>, Serializable {

    private Long id;
    private Double valor;
    private Calendar data;
    private Long version;
    private String createdBy;

    public SelicMetaCopomTO apply() throws RecordNotFoundException {
        add(linkTo(methodOn(SelicMetaCopomController.class).read(id)).withSelfRel());
        return this;
    }

}
