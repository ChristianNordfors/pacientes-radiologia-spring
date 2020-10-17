package com.bolsadeideas.springboot.app.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {
	
	@GetMapping("/login")
	public String login(@RequestParam(value="error", required=false) String error,
						@RequestParam(value="logout", required=false) String logout,
 			Model model, Principal principal, RedirectAttributes flash) {
		
		// Si principal es distinto de null es que ya habia iniciado sesion entonces evitamos que vuelva a mostrar la pagina de login
		if(principal != null) {
			flash.addFlashAttribute("info", "El usuario se encuentra logueado.");
			return "redirect:/";
		}
		
		if(error != null) {
			model.addAttribute("error", "Usuario o contraseña incorrectos.");
		}
		
		if(logout!=null) {
			model.addAttribute("success", "La sesión se ha cerrado correctamente.");
		}
		
		return "login";
	}
	
}
