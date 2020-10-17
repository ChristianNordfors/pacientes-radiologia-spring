package com.bolsadeideas.springboot.app.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.app.models.entity.Dia;

public interface IDiaDao extends CrudRepository<Dia, Long> {

	@Query("select d from Dia d left join fetch d.turnos t where d.id=?1 ")
	public Dia fetchByIdWithTurnos(Long id);
	
	
}
