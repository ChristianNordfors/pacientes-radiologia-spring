package com.bolsadeideas.springboot.app.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.app.models.entity.Estudio;

public interface IEstudioDao extends CrudRepository<Estudio, Long> {
		   
	@Query("select e from Estudio e join fetch e.paciente p join fetch e.practicas pr join fetch pr.radiografia where e.id=?1")
	public Estudio fetchByIdWithPacienteWithPracticaWithRadiografia(Long id);

}
