package com.bolsadeideas.springboot.app.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
//import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
//import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.SessionAttributes;

@Entity
@Table(name = "pacientes")
@SessionAttributes("paciente")
public class Paciente implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	@Size(min=4, max=12)
	private String nombre;
	
	@NotEmpty
	private String apellido;
	
	@Column(nullable=false, unique=true)
	@NotNull
	@Min(value=111111, message="El número de documento ingresado no es válido")
	@Max(value=99999999, message="El número ingresado de documento no es válido")
	private Long dni;
	
	
	@Email
	private String email;
	
	@NotNull
	@Column(name = "created_at")
	// Con @Temporal se especifica el formato fecha
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date createdAt;
	
	private Long telefono;
	
	private String domicilio;
	
	
	public Paciente() {

		estudios = new ArrayList<Estudio>();
		
	}
	
	@OneToMany(mappedBy="paciente", fetch=FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval = true)
	private List<Estudio> estudios;

	
	private String foto;
	
	// De forma automatica se va a llamar a este metodo justo antes de llamar el persist
	// Es decir, justo antes de insertar el registro en la bd
//	@PrePersist
//	public void prePersist() {
//		createdAt = new Date();
//	}

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
	
	public Long getDni() {
		return dni;
	}

	public void setDni(Long dni) {
		this.dni = dni;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	

	public Long getTelefono() {
		return telefono;
	}

	public void setTelefono(Long telefono) {
		this.telefono = telefono;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}
	
	
	public List<Estudio> getEstudios() {
		return estudios;
	}

	public void setEstudios(List<Estudio> estudios) {
		this.estudios = estudios;
	}
	
	public void addEstudio(Estudio estudio) {
		estudios.add(estudio);
	}


	@Override
	public String toString() {
		return nombre + " " + apellido;
	}

	
}
