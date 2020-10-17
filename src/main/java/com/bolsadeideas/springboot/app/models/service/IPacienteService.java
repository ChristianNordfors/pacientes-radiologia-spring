package com.bolsadeideas.springboot.app.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bolsadeideas.springboot.app.models.entity.Paciente;
import com.bolsadeideas.springboot.app.models.entity.Estudio;
import com.bolsadeideas.springboot.app.models.entity.Radiografia;

public interface IPacienteService {

	public List<Paciente> findAll();
	
	public Page<Paciente> findAll(Pageable pageable);

	public void save(Paciente paciente);

	public Paciente findOne(Long id);

	public void delete(Long id);
	

	
	// Estudio
	public Estudio findEstudioById(Long id);
	
	public void deleteEstudio(Long id);
	
	public Estudio fetchByIdWithPacienteWithPracticaWithRadiografia(Long id);
	
	public Paciente fetchByIdWithEstudios(Long id);
	
	public void saveEstudio(Estudio estudio);
	
	public Radiografia findRadiografiaById(Long id);
	
	public List<Radiografia> findAllRadiografias();
	
	
}
