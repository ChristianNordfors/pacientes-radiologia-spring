package com.bolsadeideas.springboot.app.models.service;

import java.util.List;

import com.bolsadeideas.springboot.app.models.entity.Dia;
import com.bolsadeideas.springboot.app.models.entity.Turno;

public interface IDiaService {
	
	public List<Dia> findAll();
	
	public void save(Dia dia);
	
	public Dia findOne(Long id);
	
	public Dia fetchByIdWithTurnos(Long Id);
	
	public void delete(Long id);
	
	public void saveTurno(Turno turno);
	
	public Turno findTurnoById(Long id);
	
	public void deleteTurno(Long id);
	
	// public void saveTurnoById(Long id);
	
	public List<Turno> findAllTurnos();

}
