package com.bolsadeideas.springboot.app.models.service;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IUploadFileService {
	
	// implementacion url resource que se guarda en la respueta responseentity en el metodo handler del controlador
	public Resource load(String filename) throws MalformedURLException;
	
	// copia la imagen original al nuevo directorio y la renombra para obtenerla en el controlador
	public String copy(MultipartFile file) throws IOException;
	
	// retoran boolean para saner si enviar el flash
	public boolean delete(String filename);
	
	// Relacionado con el init
	public void deleteAll();
	
	// Crear directorio para imagenes automaticamente
	public void init() throws IOException;

}
