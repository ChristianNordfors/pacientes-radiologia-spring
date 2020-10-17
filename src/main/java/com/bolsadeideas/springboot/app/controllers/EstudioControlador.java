package com.bolsadeideas.springboot.app.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.springboot.app.models.entity.Estudio;
//import com.bolsadeideas.springboot.app.models.entity.Practica;
import com.bolsadeideas.springboot.app.models.entity.Radiografia;
import com.bolsadeideas.springboot.app.models.service.IPacienteService;

@Controller
@RequestMapping("/estudio")
@SessionAttributes("estudio")
public class EstudioControlador {
	
	@Autowired
	private IPacienteService pacienteService;
	
	@GetMapping("/ver/{id}")
	public String ver(@PathVariable(value="id") Long id,
			Model model,
			RedirectAttributes flash) {
		
		// fetch join es la mejor manera y mas recomendada cuando se trabaja con un objeto entity con muchas relaciones. Esto es sin Lazy load
		Estudio estudio = pacienteService.fetchByIdWithPacienteWithPracticaWithRadiografia(id); /*pacienteService.findFacturaById(id); esta seria la otra forma SIN join fetch    fetchByIdWithPacienteWithPracticaWithRadiografia(id)*/
		
		
		
		List<Radiografia> radiografias = pacienteService.findAllRadiografias();
		
		if(estudio==null) {
			flash.addFlashAttribute("error", "El estudio no existe en la base de datos.");
			return "redirect:/listar";
		}
		

		
		model.addAttribute("radiografias", radiografias);
		
		model.addAttribute("estudio", estudio);
		model.addAttribute("titulo", "Estudio: ".concat(estudio.getPaciente().getNombre().concat(" ").concat(estudio.getPaciente().getApellido())));
		
		return "estudio/ver";
	}
	
	

	
	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable(value="id") Long id, RedirectAttributes flash) {
		
		Estudio estudio = pacienteService.findEstudioById(id);
		
		if(estudio != null) {
			pacienteService.deleteEstudio(id);
			flash.addFlashAttribute("success", "Estudio eliminado con éxito.");
			return "redirect:/ver/" + estudio.getPaciente().getId();
		}
		flash.addFlashAttribute("error", "La factura no existe en la base de datos.");
		return "redirect:/listar";
	}
	
	
	@RequestMapping(value = "/estudio/ver/{id}")
	public String modificar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

		Estudio estudio = pacienteService.findEstudioById(id);

		model.put("estudio", estudio);
		
		
		return "redirect:/estudio/ver/" + estudio.getId();
	}
	
	
	
}

