package com.esma.generacionXML.service;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.esma.generacionXML.modelo.Plantilla_esma;
//import com.esma.generacionXML.modelo.Plantilla_esma;
import com.esma.generacionXML.modelo.tmp_xml;

import net.sf.jasperreports.engine.JRException;

@Service
public interface PlantillaService {
	
	public String exportacionesReportes(String reportFormato /*, Plantilla_esma plantilla*/) throws FileNotFoundException, JRException;

	List<Plantilla_esma> buscarPorfondos(String fondo, String cod_plantilla);
	
//	//otros Metodos que venian del DAO
//	List<Plantilla_esma> buscarPorfondo(String fondo, String cod_plantilla, Date fechaI, Date fechaF);
//	
//	String listaEstructura();
//	
//	List<Plantilla_esma> listaEstructuras();
//	
//	void generacionArchivoXML(String fondo, String cod_plantilla, String fInicial, String fFinal);
//	
//	//para pruebas
//	//muestra cpor consola
//	void listado(String fondo, String cod_plantilla, String fInicial, String fFinal);
//
//	List<Plantilla_esma> buscarfondo(Plantilla_esma plantilla_esma); //en dado prueba para utilizar el con http POST
//	
}
