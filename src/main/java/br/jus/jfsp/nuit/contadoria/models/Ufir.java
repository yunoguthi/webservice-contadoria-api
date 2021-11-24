package br.jus.jfsp.nuit.contadoria.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Calendar;

@Entity
public class Ufir {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	
	private double valor;
	
	@Temporal(TemporalType.DATE)
	private Calendar data;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public Calendar getData() {
		return data;
	}

	public void setData(Calendar data) {
		this.data = data;
	}
	


}
