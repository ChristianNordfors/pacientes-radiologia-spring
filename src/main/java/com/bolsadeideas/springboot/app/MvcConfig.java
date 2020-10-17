package com.bolsadeideas.springboot.app;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer{
	
//	private final Logger log = LoggerFactory.getLogger(getClass());
//	@Override
//	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		WebMvcConfigurer.super.addResourceHandlers(registry);
		
		// Se usa doble ** para mapear al nombre de la imagen y que se pueda ver
		// en la plantilla
		// ** Corresponden a un nombre variable de la imagen
		
		// Se modifica para que tambien sea absoluto. Ahora es mas dinamico y no tan literal como "file:/C:/Temp/uploads/"
		// toUri() agrega esquema "file:
		
		/*
		String resourcePath = Paths.get("uploads").toAbsolutePath().toUri().toString();		
		 
		log.info(resourcePath);
		registry.addResourceHandler("/uploads/**")
		.addResourceLocations(resourcePath);
		*/
		
		// Anterior
//		.addResourceLocations("file:/C:/Temp/uploads/");
//	}
	
	// viewController o controlador parametrizable
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/error_403").setViewName("error_403");
	}

}
