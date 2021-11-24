package br.jus.jfsp.nuit.contadoria.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.io.Serializable;

@Getter
@Setter
@MappedSuperclass
@SuppressWarnings("serial")
public class BaseEntity implements Serializable {

    @Version
    protected Long version;
}
