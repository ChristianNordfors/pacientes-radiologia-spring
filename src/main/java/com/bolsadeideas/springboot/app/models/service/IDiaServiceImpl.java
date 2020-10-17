package com.bolsadeideas.springboot.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.app.models.dao.IDiaDao;
import com.bolsadeideas.springboot.app.models.dao.ITurnoDao;
import com.bolsadeideas.springboot.app.models.entity.Dia;
import com.bolsadeideas.springboot.app.models.entity.Turno;

@Service
public class IDiaServiceImpl implements IDiaService {
	
	@Autowired
	private IDiaDao diaDao;
	
	@Autowired
	private ITurnoDao turnoDao;

	@Override
	@Transactional(readOnly=true)
	public List<Dia> findAll() {
		return (List<Dia>) diaDao.findAll();
	}

	@Override
	@Transactional
	public void save(Dia dia) {
		diaDao.save(dia);
	}

	@Override
	@Transactional(readOnly=true)
	public Dia findOne(Long id) {
		return diaDao.findById(id).orElse(null);
	}

	@Override
	@Transactional(readOnly=true)
	public Dia fetchByIdWithTurnos(Long id) {
		return diaDao.fetchByIdWithTurnos(id);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		diaDao.deleteById(id);
	}

	@Override
	@Transactional
	public void saveTurno(Turno turno) {
		turnoDao.save(turno);
		
	}

	@Override
	public Turno findTurnoById(Long id) {
		return turnoDao.findById(id).orElse(null);
	}

	@Override
	public void deleteTurno(Long id) {
		turnoDao.deleteById(id);
	}

	@Override
	public List<Turno> findAllTurnos() {
		return (List<Turno>) turnoDao.findAll();
	}

//	@Override
//	public void saveTurnoById(Long id) {
//		turnoDao.saveTurnoById(id);
//	}


}
