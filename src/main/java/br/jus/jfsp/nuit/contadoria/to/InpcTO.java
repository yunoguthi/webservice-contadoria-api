package br.jus.jfsp.nuit.contadoria.to;

import br.jus.jfsp.nuit.contadoria.aspect.ApplyHateoas;
import br.jus.jfsp.nuit.contadoria.controllers.BtnMensalController;
import br.jus.jfsp.nuit.contadoria.controllers.InpcController;
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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class InpcTO
        extends BaseTO<InpcTO>
        implements ApplyHateoas<InpcTO>, Serializable {

   // private Long id;
    private String ano;
    private String mes;
    //private Calendar data;
    //private String observacao;
    private String dataStr;
    //private Double valor;
    //private Double numeroIndice;
    private Float variacaoMensal;
    private Date ultimaAtualizacao;
   // private Long version;

    public InpcTO apply() throws RecordNotFoundException {
        add(linkTo(methodOn(InpcController.class).read(id)).withSelfRel());
        return this;
    }

}
