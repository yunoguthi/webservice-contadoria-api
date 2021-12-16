package br.jus.jfsp.nuit.contadoria.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
@SuppressWarnings("serial")
public class BaseEntity implements Serializable {

    @Version
    protected Long version;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createdDate;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    protected Date modifiedDate;

    @CreatedBy
    public String createdBy;

    @LastModifiedBy
    protected String modifiedBy;

    @Temporal(TemporalType.DATE)
    private Calendar data;

    private Double valor;

    private String observacao;

    private String fonte;


}
