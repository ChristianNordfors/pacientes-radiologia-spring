package com.bolsadeideas.springboot.app.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.app.models.entity.Turno;

public interface ITurnoDao extends CrudRepository<Turno, Long> {

//	@Query("update Turno t where t.id=?1 ")
//	public Turno saveTurnoById(Long id);
}
