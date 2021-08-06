package com.esma.generacionXML.controller;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.esma.generacionXML.modelo.Plantilla_esma;
import com.esma.generacionXML.modelo.tmp_xml;
import com.esma.generacionXML.service.PlantillaService;

import net.sf.jasperreports.engine.JRException;



@CrossOrigin(origins = "http://localhost:4200")

@RequestMapping("/esma")
@RestController
public class PlantillaController {

	@Autowired
	PlantillaService sPlantilla;
	
//Petciones desde Angular
	//enviar en el cuerpo 
	// Generando metodo nuevo

	/**
	 * 
	 * @param mEsma
	 * @return devuelve el Get con cuerpo desde el forta en Angular
	 * @throws FileNotFoundException para cacptura las excepciones Propias
	 * @throws JRException
	 * Cambios
	 * Funcional al "hrs 1204 - 05082021"
	 * //method = RequestMethod.POST
	   //consumes = MediaType.APPLICATION_JSON_VALUE // se quita del front el parametro header 
	 */
	
	//@RequestMapping
	@RequestMapping (value="plantillas/",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE) 
	public List<Plantilla_esma> busquedaPlantilla(@RequestParam(required=false) String fondo,@RequestParam(required=false) String plantilla_esma){
		System.out.println("Este es el parametro del Front --> "+fondo);
		
		System.out.println("Este es el parametro tipo Plantillat --> "+plantilla_esma);
		SimpleDateFormat fechaFormato=new SimpleDateFormat("yyyy-MM-dd");
		try {
			sPlantilla.exportacionesReportes(fondo);
			return sPlantilla.buscarPorfondos(fondo, plantilla_esma);
			//return new ResponseEntity<>(sPlantilla.buscarPorfondos(fondo, tplantilla),HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return null; //validar que devolver en error
		}
	}
	
/**
 * 
 * @param mEsma
 * @return devuelve el Post de lo que se trae del Front que esta en Angular
 * @throws FileNotFoundException para cacptura las excepciones Propias
 * @throws JRException
 * Cambios
 * Funcional al "hrs 1204 - 05082021"
 * //method = RequestMethod.POST
   //consumes = MediaType.APPLICATION_JSON_VALUE // se quita del front el parametro header 
 */
//	@PostMapping	
//	@RequestMapping(value="plantilla", method = RequestMethod.POST )
//	public ResponseEntity<?> lista(@RequestBody Plantilla_esma mEsma) throws FileNotFoundException, JRException{
//		SimpleDateFormat fechaFormato=new SimpleDateFormat("dd-MM-yyyy"); //en dado caso que capturemos la fecha del front
//		
//		System.out.println("/nInformacion que viene desde Front Angular "+mEsma);
//		try {
//			sPlantilla.exportacionesReportes("html");
//			//return  (ResponseEntity<?>) sPlantilla.listaEstructura();
//			return 
//					new ResponseEntity<>(sPlantilla.listaEstructuras(),HttpStatus.OK); //devuelve la informacion al front
//		} catch (Exception e) {
//			
//			return new ResponseEntity<>(null,HttpStatus.EXPECTATION_FAILED);
//		}
//		
//	}


	

}
