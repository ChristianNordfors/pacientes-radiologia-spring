package com.bolsadeideas.springboot.app.models.dao;

import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bolsadeideas.springboot.app.models.entity.Paciente;

// No se anota con @Repository ni  @Component pero se inyecta en Impl porque hereda de CrudRepository
// Por debajo ya es un componente Spring. No es necesario anotarlo
public interface IPacienteDao extends PagingAndSortingRepository<Paciente, Long>{
	
	
	// query de estudios
	@Query("select p from Paciente p left join fetch p.estudios e where p.id=?1")
	public Paciente fetchByIdWithEstudios(Long id);
	
	// PagingAndSortingRepository hereda de CrudRepository
	
	// Los metodos vienen implementados ya CRUD
}
