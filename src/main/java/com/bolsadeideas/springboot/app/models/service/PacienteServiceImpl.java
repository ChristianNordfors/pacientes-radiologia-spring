package com.bolsadeideas.springboot.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.app.models.dao.IPacienteDao;
import com.bolsadeideas.springboot.app.models.dao.IEstudioDao;
import com.bolsadeideas.springboot.app.models.dao.IRadiografiaDao;
import com.bolsadeideas.springboot.app.models.entity.Paciente;
import com.bolsadeideas.springboot.app.models.entity.Estudio;
import com.bolsadeideas.springboot.app.models.entity.Radiografia;

@Service
public class PacienteServiceImpl implements IPacienteService{
	
	@Autowired
	private IPacienteDao pacienteDao;
	
	@Autowired
	private IEstudioDao estudioDao;
	
	@Autowired
	private IRadiografiaDao radiografiaDao;

	@Override
	@Transactional(readOnly=true)
	public List<Paciente> findAll() {
		return (List<Paciente>) pacienteDao.findAll();
	}

	@Override
	@Transactional
	public void save(Paciente paciente) {
		pacienteDao.save(paciente);
	}

	@Override
	@Transactional(readOnly = true)
	public Paciente findOne(Long id) {
		return pacienteDao.findById(id).orElse(null);
	}
	
	@Override
	@Transactional
	public void delete(Long id) {
		pacienteDao.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Paciente> findAll(Pageable pageable) {
		return pacienteDao.findAll(pageable);
	}


	
	
	// ESTUDIOS ---------------------------------------------------------------------------------------

	@Override
	@Transactional(readOnly=true)
	public Estudio findEstudioById(Long id) {
		return estudioDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void deleteEstudio(Long id) {
		estudioDao.deleteById(id);
	}

	@Override
	@Transactional(readOnly=true)
	public Estudio fetchByIdWithPacienteWithPracticaWithRadiografia(Long id) {
		return estudioDao.fetchByIdWithPacienteWithPracticaWithRadiografia(id);
	}

	@Override
	@Transactional(readOnly=true)
	public Paciente fetchByIdWithEstudios(Long id) {
		return pacienteDao.fetchByIdWithEstudios(id);
	}

	@Override
	@Transactional
	public void saveEstudio(Estudio estudio) {
		estudioDao.save(estudio);
	}

	@Override
	@Transactional(readOnly=true)
	public Radiografia findRadiografiaById(Long id) {
		return radiografiaDao.findById(id).orElse(null);
	}

	@Override
	@Transactional(readOnly=true)
	public List<Radiografia> findAllRadiografias() {
		return (List<Radiografia>) radiografiaDao.findAll();
	}


}
