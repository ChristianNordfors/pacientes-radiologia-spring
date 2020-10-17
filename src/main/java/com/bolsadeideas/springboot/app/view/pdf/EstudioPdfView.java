package com.bolsadeideas.springboot.app.view.pdf;

import java.awt.Color;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.bolsadeideas.springboot.app.models.entity.Estudio;
import com.bolsadeideas.springboot.app.models.entity.Practica;
import com.lowagie.text.Document;
//import com.lowagie.text.Element;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Component("estudio/ver")
// AbstractPdfView hereda de AbstractView que hereda de View
public class EstudioPdfView extends AbstractPdfView{

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Estudio estudio = (Estudio) model.get("estudio");
		
		PdfPTable tabla = new PdfPTable(1);
		tabla.setSpacingAfter(20);
		
		PdfPCell cell = null;
		
		cell = new PdfPCell(new Phrase("Datos del paciente"));
		cell.setBackgroundColor(new Color(184,218,255));
		cell.setPadding(12f);
		tabla.addCell(cell);
		
		PdfPCell cellNombre = null;
		cellNombre = new PdfPCell(new Phrase("Nombre y apellido: " + estudio.getPaciente().getNombre() + " " + estudio.getPaciente().getApellido()));
		cellNombre.setPadding(6f);
		PdfPCell cellFecha = null;
		cellFecha = new PdfPCell(new Phrase("Fecha: " + estudio.getPaciente().getCreatedAt().toString()));
		cellFecha.setPadding(6f);
		PdfPCell cellDni = null;
		cellDni = new PdfPCell(new Phrase("DNI: " + estudio.getPaciente().getDni().toString()));
		cellDni.setPadding(6f);

		
		tabla.addCell(cellNombre);
		tabla.addCell(cellFecha);
		if(estudio.getPaciente().getDni() != null) {
			tabla.addCell(cellDni);
		}
//		if(estudio.getPaciente().getTelefono() != null) {
//			tabla.addCell("Teléfono: " + estudio.getPaciente().getTelefono().toString());
//		}
//		if(estudio.getPaciente().getDomicilio() != null) {
//			tabla.addCell("Domicilio: " + estudio.getPaciente().getDomicilio());
//		}
//		if(estudio.getPaciente().getEmail() != null) {
//			tabla.addCell("Email: " + estudio.getPaciente().getEmail());
//		}
		
		
		
		PdfPTable tabla2 = new PdfPTable(1);
		tabla2.setSpacingAfter(20);
		
		
		cell = new PdfPCell(new Phrase("Datos del estudio"));
//		cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		cell.setBackgroundColor(new Color(195,230,203));
		cell.setPadding(12f);
		tabla2.addCell(cell);
		
		PdfPCell cellFechaEstudio = null;
		cellFechaEstudio = new PdfPCell(new Phrase("Fecha: " + estudio.getCreatedAt()));
		cellFechaEstudio.setPadding(6f);
		tabla2.addCell(cellFechaEstudio);

		if(estudio.getObservacion() != null) {
		PdfPCell cellObservacion = null;
		cellObservacion = new PdfPCell(new Phrase("Observaciónes: " + estudio.getObservacion()));
		cellObservacion.setPadding(6f);
		tabla2.addCell(cellObservacion);
		}
		
		
		document.add(tabla);
		document.add(tabla2);
	
		
		PdfPTable tabla3 = new PdfPTable(1);
		tabla3.setSpacingAfter(20);
		cell = new PdfPCell(new Phrase("Radiografías"));
		cell.setBackgroundColor(new Color(250,218,94));
		cell.setPadding(12f);
		tabla3.addCell(cell);
		
		for(Practica practica: estudio.getPracticas()) {
			cell = new PdfPCell(new Phrase(practica.getRadiografia().getNombre()));
			cell.setPadding(6f);
			tabla3.addCell(cell);
		}
		
		document.add(tabla3);
		
//		PdfPTable tabla3 = new PdfPTable(4);
//		tabla3.setWidths(new float[] {3.5f, 1, 1, 1});
//		tabla3.addCell("Producto");
//		tabla3.addCell("Precio");
//		tabla3.addCell("Cantidad");
//		tabla3.addCell("Total");
//		
//		for(Practica practica: estudio.getPracticas()) {
//			tabla3.addCell(practica.getRadiografia().getNombre());
//			tabla3.addCell(practica.getRadiografia().get);
			
//			cell = new PdfPCell(new Phrase(item.getCantidad().toString()));
//			cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
//			tabla3.addCell(cell);
//			tabla3.addCell(item.calcularImporte().toString());
		}
		


}
