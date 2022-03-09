package br.jus.jfsp.nuit.contadoria.to;

import br.jus.jfsp.nuit.contadoria.aspect.ApplyHateoas;
import br.jus.jfsp.nuit.contadoria.controllers.BtnMensalController;
import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

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
public class BtnMensalTO
        extends BaseTO<BtnMensalTO>
        implements ApplyHateoas<BtnMensalTO>, Serializable {

    //private Long id;
    //private Double valor;
    //private Calendar data;
    //private String observacao;
    //private Calendar dataModificacao;
    //private Long version;

    public BtnMensalTO apply() throws RecordNotFoundException {
        add(linkTo(methodOn(BtnMensalController.class).read(id)).withSelfRel());
        return this;
    }

}
