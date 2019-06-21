package it.polito.tdp.newufosightings.model;

import java.time.LocalDateTime;

public class Evento implements Comparable<Evento>{

	//sostanzialmente un evento è caratterizzato da:
	
	public enum TipoEvento{
		
		AVVISTAMENTO,
		CESSATA_ALLERTA
	}
	
	private LocalDateTime data; 
	private String idState;
	private TipoEvento tipo;
	private int liv;
	
	

	public Evento(LocalDateTime data, String idState, TipoEvento tipo) {
		
		this.data = data;
		this.idState = idState;
		this.tipo = tipo;
		this.liv = 0;
	}
	
	
	public Evento(LocalDateTime data, String idState, TipoEvento tipo, int liv) {
		
		this.data = data;
		this.idState = idState;
		this.tipo = tipo;
		this.liv = liv;
	}
	

	public int getLiv() {
		return liv;
	}


	public void setLiv(int liv) {
		this.liv = liv;
	}


	public LocalDateTime getData() {
		return data;
	}

	public String getIdState() {
		return idState;
	}

	public TipoEvento getTipo() {
		return tipo;
	}

	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((idState == null) ? 0 : idState.hashCode());
		result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Evento other = (Evento) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (idState == null) {
			if (other.idState != null)
				return false;
		} else if (!idState.equals(other.idState))
			return false;
		if (tipo != other.tipo)
			return false;
		return true;
	}

	@Override
	public int compareTo(Evento o) {
		// TODO Auto-generated method stub
		return this.data.compareTo(o.data);
	}
}
