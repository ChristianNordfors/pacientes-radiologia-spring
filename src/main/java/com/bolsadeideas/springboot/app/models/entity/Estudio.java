package com.bolsadeideas.springboot.app.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
//import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="estudios")
public class Estudio implements Serializable{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String observacion;
	
	@NotNull
	@Temporal(TemporalType.DATE)
	@Column(name="created_at")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date createdAt;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private Paciente paciente;
	
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name="estudio_id")
	private List<Practica> practicas;
	
	
	public Estudio() {
		this.practicas = new ArrayList<Practica>();

	}
	


	public Long getId() {
		return id;
	}





	public void setId(Long id) {
		this.id = id;
	}





	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	public List<Practica> getPracticas() {
		return practicas;
	}

	public void setPracticas(List<Practica> practicas) {
		this.practicas = practicas;
	}
	
	
	public void addPractica(Practica practica) {
		this.practicas.add(practica);
	}
	

	

	private static final long serialVersionUID = 1L;
	
	
}
