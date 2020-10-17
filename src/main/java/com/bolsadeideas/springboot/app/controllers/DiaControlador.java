package com.bolsadeideas.springboot.app.controllers;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.bolsadeideas.springboot.app.models.entity.Dia;
import com.bolsadeideas.springboot.app.models.entity.Turno;
import com.bolsadeideas.springboot.app.models.service.IDiaService;

@Controller
@SessionAttributes({ "dia", "turno" })
public class DiaControlador {

	@Autowired
	private IDiaService diaService;

	@GetMapping("/turnos")
	public String listar(Map<String, Object> model, RedirectAttributes flash) {

		List<Dia> dias = diaService.findAll();

		model.put("titulo", "Listado de días");
		model.put("dias", dias);

		model.put("localDateTime", LocalDateTime.now());

		Dia dia = new Dia();
		model.put("dia", dia);

		model.put("boton", "Agregar");

		List<Turno> turnos = diaService.findAllTurnos();

		model.put("turnos", turnos);

		return "dias";
	}

	@GetMapping("/ver-dia/{id}")
	public String ver(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {

		Dia dia = diaService.fetchByIdWithTurnos(id);

		if (dia == null) {
			flash.addFlashAttribute("error", "El día no se encuentra en la basde de datos.");
			return "redirect:/turnos";
		}

		model.addAttribute("dia", dia);
		model.addAttribute("titulo", "Turnos del día");

		// TURNOS ----------------
		Turno turno = new Turno();

		turno.setDia(dia);

		model.addAttribute("turno", turno);

//		Turno turnoTabla = diaService.findTurnoById(id);
//		
//		model.addAttribute("turnoTabla", turnoTabla);
		// ---------------------------------------

		return "turno/ver";
	}

	@PostMapping("/turnos")
	public String guardar(@Valid Dia dia, BindingResult result, Map<String, Object> model, RedirectAttributes flash,
			SessionStatus status) {

		if (result.hasErrors()) {
			flash.addFlashAttribute("error", "Error en algún campo.");
			return "redirect:/turnos";
		}

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		// String mensajeDia = (dia.getId() == null) ? "Día " +
		// formatter.format(dia.getFecha()) + " agregado." : "Día " +
		// formatter.format(dia.getFecha()) + " actualizado.";

		if (dia.getId() == null) {
			flash.addFlashAttribute("success", "Día " + formatter.format(dia.getFecha()) + " agregado.");
			diaService.save(dia);
			status.setComplete();

			return "redirect:/turnos";
		} else {
			flash.addFlashAttribute("success", "Día " + formatter.format(dia.getFecha()) + " actualizado.");
			diaService.save(dia);
			status.setComplete();
			return "redirect:/ver-dia/" + dia.getId();
		}

	}

	@GetMapping("/eliminar-dia/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {

		if (id > 0) {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			Dia dia = diaService.findOne(id);
			diaService.delete(id);
			flash.addFlashAttribute("success", "El día ".concat(" ").concat(formatter.format(dia.getFecha()))
					.concat(" ").concat("fue eliminado."));
		}

		return "redirect:/turnos";
	}

	@PostMapping("/crear-turno")
	public String crear(Turno turno, Map<String, Object> model, RedirectAttributes flash, SessionStatus status) {

		if (turno.getId() != null) {

			flash.addFlashAttribute("success", "Turno actualizado.");

			diaService.saveTurno(turno);

			status.setComplete();

			return "redirect:/ver-dia/" + turno.getDia().getId();

		} else {

			flash.addFlashAttribute("success", "Turno agregado.");

			diaService.saveTurno(turno);

			status.setComplete();

			return "redirect:/ver-dia/" + turno.getDia().getId();

		}
	}

	@RequestMapping(value = "/modificar-turno/{id}", method = { RequestMethod.POST })
	public String modificarTurno(@PathVariable(value = "id") Long id, Model model,
			@RequestParam(value = "nombre", required = false) String nombre,
			@RequestParam(value = "apellido", required = false) String apellido,
			@RequestParam(value = "observacion", required = false) String observacion,
			@RequestParam(value = "urgente", required = false) String urgente,

			RedirectAttributes flash, SessionStatus status) {

		model.addAttribute("numero", id);

		Turno turno = diaService.findTurnoById(id);

		model.addAttribute("turno", turno);

		if (turno.getId() != null) {

//			if (turno.getNombre() == nombre && turno.getApellido() == apellido && turno.getObservacion() == observacion && turno.getUrgente() == urgente) {
//				
//				flash.addFlashAttribute("info", "No se ha modificado el turno");
//				
//				return "redirect:/ver-dia/" + turno.getDia().getId();
//			}

//			turno.setNombre(null);
//			turno.setApellido(null);
//			turno.setObservacion(null);

			if (urgente != null && turno.getUrgente() != urgente) {
				turno.setUrgente(urgente);
			}

			if (turno.getNombre() != nombre) {
				turno.setNombre(nombre);
			}

			if (turno.getApellido() != apellido) {
				turno.setApellido(apellido);
			}

			if (turno.getObservacion() != observacion) {
				turno.setObservacion(observacion);
			}

			diaService.saveTurno(turno);

			status.setComplete();

			flash.addFlashAttribute("success",
					"Turno de " + turno.getNombre() + ' ' + turno.getApellido() + " modificado.");

			return "redirect:/ver-dia/" + turno.getDia().getId();

		}

		flash.addFlashAttribute("error", "El turno no existe en la base de datos.");

		return "redirect:/ver-dia/" + turno.getDia().getId();
	}

	@GetMapping("/eliminar-turno/{id}")
	public String eliminarTurno(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {

		Turno turno = diaService.findTurnoById(id);
		if (turno != null) {
			diaService.deleteTurno(id);
			flash.addFlashAttribute("success", "Turno eliminado.");
			return "redirect:/ver-dia/" + turno.getDia().getId();
		}

		flash.addFlashAttribute("error", "El turno no se encuentra en la base de datos.");

		return "redirect:/listar";
	}
}
