package com.bolsadeideas.springboot.app.controllers;

//import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.List;
//import java.nio.file.Path;
//import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.springboot.app.models.entity.Paciente;
import com.bolsadeideas.springboot.app.models.entity.Estudio;
import com.bolsadeideas.springboot.app.models.entity.Practica;
import com.bolsadeideas.springboot.app.models.entity.Radiografia;
import com.bolsadeideas.springboot.app.models.service.IPacienteService;
import com.bolsadeideas.springboot.app.models.service.IUploadFileService;

@Controller
@SessionAttributes({ "paciente", "estudio" })
public class PacienteControlador {

	protected final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	// @Qualifier("pacienteDaoJPA")
	private IPacienteService pacienteService;

	@Autowired
	private IUploadFileService uploadFileService;

	@Secured({ "ROLE_USER", "ROLE_ADMIN" })
	@GetMapping(value = "/uploads/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String filename) {

		Resource recurso = null;

		try {
			recurso = uploadFileService.load(filename);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
				.body(recurso);

	}

	@Secured("ROLE_USER")
	@GetMapping(value = "/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

		// fetch join(lo mismo que en factura) mejor manera de hacerlo cuando hay muchas
		// relaciones
		Paciente paciente = pacienteService.fetchByIdWithEstudios(id); // pacienteService.findOne(id);
		if (paciente == null) {
			flash.addFlashAttribute("error", "El paciente no existe en la base de datos");
			return "redirect:/listar";
		}

		model.put("paciente", paciente);
		model.put("titulo", "Ficha de paciente: " + paciente.getNombre() + ' ' + paciente.getApellido());

		// ESTUDIO
		// -----------------------------------//-----------------------------------//-----------------------------------//-----------------------------------
		Estudio estudio = new Estudio();

		estudio.setPaciente(paciente);
		
		List<Radiografia> radiografias = pacienteService.findAllRadiografias();

		model.put("estudio", estudio);
		model.put("radiografias", radiografias);

		model.put("tituloModal", "Nuevo estudio");

		// -----------------------------------//-----------------------------------//-----------------------------------//-----------------------------------

		return "ver";
	}

	// @GetMapping
	@Secured("ROLE_USER")
	@RequestMapping(value = { "/listar", "/" }, method = RequestMethod.GET)
	public String listar(Model model, Authentication authentication, HttpServletRequest request) {

		if (authentication != null) {
			logger.info("Hola usuario autenticado, tu username es: ".concat(authentication.getName()));
		}

//		Pageable pageRequest = PageRequest.of(page, 8);

		// Forma estática
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null) {
			logger.info("Utilizando forma estática SecurityContextHolder.getContext(), el username es: "
					.concat(auth.getName()));
		}

		// Otra alternativa para verificar ROLE relacionada con el hasRole mas abajo
		if (hasRole("ROLE_ADMIN")) {
			logger.info("Hola ".concat(auth.getName()).concat(" tienes acceso!"));
		} else {
			logger.info("Hola ".concat(auth.getName()).concat(" NO tienes acceso!"));
		}

		// Otra alternativa para verificar ROLE
		// Usa el metodo isGranted haciendo lo mismo que el metodo creado hasRole
		SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request,
				"ROLE_");

		if (securityContext.isUserInRole("ADMIN")) {
			logger.info("Forma usando SecurityContextHolderAwareRequestWrapper: Hola ".concat(auth.getName())
					.concat(" tienes acceso!"));
		} else {
			logger.info("Forma usando SecurityContextHolderAwareRequestWrapper: Hola ".concat(auth.getName())
					.concat(" NO tienes acceso!"));
		}

		// Otra forma de validar el rol solamente con el objeto request de forma nativa
		// si o si hay que escribir el rol completo ROLE_ etc
		if (request.isUserInRole("ROLE_ADMIN")) {
			logger.info("Forma usando HttpServletRequest: Hola ".concat(auth.getName()).concat(" tienes acceso!"));
		} else {
			logger.info("Forma usando HttpServletRequest: Hola ".concat(auth.getName()).concat(" NO tienes acceso!"));
		}

		List<Paciente> pacientes = pacienteService.findAll();

//		PageRender<Paciente> pageRender = new PageRender<>("/listar", pacientes);

		model.addAttribute("titulo", "Listado de pacientes");
		model.addAttribute("pacientes", pacientes);
//		model.addAttribute("page", pageRender);
		return "listar";
	}


	@Secured("ROLE_ADMIN")
	@GetMapping("/form")
	public String crear(Map<String, Object> model) {

		// Está mapeado al formulario y a la tabla. Es bidireccional
		Paciente paciente = new Paciente();
		model.put("paciente", paciente);
		model.put("titulo", "Formulario de paciente");
		model.put("botonEnviar", "Crear paciente");
		return "form";
	}

	// @PreAuthorize("hasRole('ROLE_ADMIN')")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER', 'ROLE_OTRO')")
	// @Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

		Paciente paciente = null;

		if (id > 0) {
			paciente = pacienteService.findOne(id);
			if (paciente == null) {
				flash.addFlashAttribute("error", "El ID del paciente no existe en la base de datos");
				return "redirect:/listar";
			}
		} else {
			flash.addFlashAttribute("error", "El ID del paciente no puede ser cero");
			return "redirect:/listar";
		}
		model.put("paciente", paciente);
		model.put("titulo", "Editar paciente");
		model.put("botonEnviar", "Actualizar paciente");
		return "form";
	}


	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String guardar(@Valid Paciente paciente, BindingResult result, Model model,
			@RequestParam("file") MultipartFile foto, RedirectAttributes flash, SessionStatus status) {
		if (result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de paciente");
			String botonTexto = (paciente.getId() != null) ? "Editar Usuario" : "Crear Usuario";
			model.addAttribute("botonEnviar", botonTexto);
			return "form";
		}
		// Eliminar foto vieja al momento de editar
		if (!foto.isEmpty()) {
			if (paciente.getId() != null && paciente.getId() > 0 && paciente.getFoto() != null
					&& paciente.getFoto().length() > 0) {

				uploadFileService.delete(paciente.getFoto());
			}
			// Mover imagen
			String uniqueFilename = null;
			try {
				uniqueFilename = uploadFileService.copy(foto);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// flash.addFlashAttribute("info", "Ha subido correctamente '" + uniqueFilename
			// + "'");

			paciente.setFoto(uniqueFilename);


		}
		if (paciente.getId() == null) {
			flash.addFlashAttribute("successPaciente", "Paciente creado con éxito. Agregar nuevo paciente.");
		} else {
			flash.addFlashAttribute("success", "Paciente actualizado con éxito");
		}

		try{
			 
		    pacienteService.save(paciente);
		} catch (DataIntegrityViolationException dke) {
			if(paciente.getId()!=null) {
				model.addAttribute("error", "Ya se encuentra registrado un paciente con ese DNI.");
			    model.addAttribute("titulo", "Editar paciente");
				model.addAttribute("botonEnviar", "Actualizar paciente");
			    return "form";
			} else {
				model.addAttribute("error", "Ya se encuentra registrado un paciente con ese DNI.");
			    model.addAttribute("titulo", "Formulario de paciente");
				model.addAttribute("botonEnviar", "Crear paciente");
			    return "form";
			}
		}
		status.setComplete();

		return "redirect:/ver/" + paciente.getId();
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {

		if (id > 0) {
			Paciente paciente = pacienteService.findOne(id);
			pacienteService.delete(id);
			flash.addFlashAttribute("success", "Paciente " + paciente.getId() + " - " + paciente.getNombre() + " "
					+ paciente.getApellido() + " eliminado con éxito");
			

			// como retorna boolean si es true manda mensaje
			if (uploadFileService.delete(paciente.getFoto())) {
				flash.addFlashAttribute("info", "Foto " + paciente.getFoto() + " eliminada con éxito!");
			}
		}
//		}
		return "redirect:/listar";
	}

	// Forma programatica para validar rol
	private boolean hasRole(String role) {

		SecurityContext context = SecurityContextHolder.getContext();

		if (context == null) {
			return false;
		}

		Authentication auth = context.getAuthentication();

		if (auth == null) {
			return false;
		}

		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();


		return authorities.contains(new SimpleGrantedAuthority(role));

	}

	// ESTUDIO GUARDAR ---------------------------------------------------

	@PostMapping("/crear")
	public String crear(@Valid Estudio estudio, BindingResult result, Map<String, Object> model,
			@RequestParam(value="radiografia", required = false) String[] radio,
			RedirectAttributes flash, SessionStatus status) {

		if (result.hasErrors()) {
			flash.addFlashAttribute("error", "La fecha del estudio es obligatoria.");
			return "redirect:/ver/" + estudio.getPaciente().getId();
		}
		
		

		if (estudio.getId() != null) {
//			List<Radiografia> radiografiasLimpiar = pacienteService.find();
			
			if(radio != null) {
				
//				List<Practica> practicas = estudio.getPracticas();
				
				estudio.getPracticas().clear();;				
				
				
				
				for(int i=0; i < radio.length; i++) {
					
//					Long radioId = Long.valueOf(radio[i]);
					
					
					Radiografia radiografia = pacienteService.findRadiografiaById(Long.valueOf(radio[i]));
					
						Practica practica = new Practica();
						practica.setRadiografia(radiografia);
						estudio.addPractica(practica);
					
				}
			}

			flash.addFlashAttribute("success", "Estudio actualizado con éxito.");
			
			pacienteService.saveEstudio(estudio);

			status.setComplete();

			return "redirect:/estudio/ver/" + estudio.getId();

		} else {
			
			if(radio != null) {		
				for(int i=0; i < radio.length; i++) {
//					Long radioId = Long.valueOf(radio[i]);
					Radiografia radiografia = pacienteService.findRadiografiaById(Long.valueOf(radio[i]));
					Practica practica = new Practica();
					practica.setRadiografia(radiografia);
					estudio.addPractica(practica);
				}
			}
			
			flash.addFlashAttribute("success", "Estudio creado con éxito.");
			
			pacienteService.saveEstudio(estudio);

			status.setComplete();

			return "redirect:/ver/" + estudio.getPaciente().getId();	
			
		}	
		
		
	}

}
