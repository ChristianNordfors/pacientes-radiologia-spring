package com.bolsadeideas.springboot.app.models.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="turnos")
public class Turno implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nombre;

	private String apellido;
	
	private String observacion;
	
	private String urgente;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Dia dia;
	
	
	
	public Long getId() {
		return id;
	}




	public void setId(Long id) {
		this.id = id;
	}
	
	


	public String getNombre() {
		return nombre;
	}




	public void setNombre(String nombre) {
		this.nombre = nombre;
	}




	public String getApellido() {
		return apellido;
	}




	public void setApellido(String apellido) {
		this.apellido = apellido;
	}




	public String getObservacion() {
		return observacion;
	}




	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}







	public String getUrgente() {
		return urgente;
	}




	public void setUrgente(String urgente) {
		this.urgente = urgente;
	}




	public Dia getDia() {
		return dia;
	}




	public void setDia(Dia dia) {
		this.dia = dia;
	}




	private static final long serialVersionUID = 1L;

}
