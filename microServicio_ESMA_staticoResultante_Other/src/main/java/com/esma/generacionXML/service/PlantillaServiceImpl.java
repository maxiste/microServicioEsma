package com.esma.generacionXML.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.esma.generacionXML.dao.PlantillaRepository;
//import com.esma.generacionXML.dao._PlantillaRepository;
import com.esma.generacionXML.modelo.Plantilla_esma;
import com.esma.generacionXML.modelo.tmp_xml;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRCsvMetadataExporter;
import net.sf.jasperreports.export.SimpleCsvMetadataExporterConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;



@Service
public class PlantillaServiceImpl implements PlantillaService {

	@Value("${pathSalida}")
	String pathSalida;
	
	@Value("${plantBase5}")
	String plantBase5;
	
	@Value("${plantBase6}")
	String plantBase6;
	
	File reporteCreado=null;
	String pTitulo=null;
	
	@Autowired
	PlantillaRepository pRepository;
	

	@Override
	public List<Plantilla_esma> buscarPorfondos(String fondo, String cod_plantilla) {
		Predicate<Plantilla_esma> ffondo=ffon->ffon.getCod_fondo().equalsIgnoreCase(fondo);
		Predicate<Plantilla_esma> ffesma=ffes->ffes.getPlantilla_esma().equalsIgnoreCase(cod_plantilla);
	
		return 
				pRepository.findAll() //pudiera utilizar otro metodo filtrado del jpa
				.stream()
				//Agregamos las fechas de busquedas luego
				.filter((ffondo).and(ffesma))
				//.filter((ffondo).and(ffesma).and(fechaI).and(fechaF))
				.collect(Collectors.toList());
		
	}
	
	@Override
	public String exportacionesReportes(String fondo/*, Plantilla_esma plantilla*/) throws FileNotFoundException, JRException {
		Predicate<Plantilla_esma> ffondo=ffon->ffon.getCod_fondo().equalsIgnoreCase(fondo);
		//validar si incorpora el metodos de busqueda de datos directa buscrPorfondos()
		List<Plantilla_esma> miplantilla=pRepository.findAll()
				.stream()
				//Agregamos las fechas de busquedas luego el resto de losfiltrados
				//.and(ffesma))
				.filter(ffondo) 
				.collect(Collectors.toList()); //prueba de todas las plantillas
		
		//parametro Recibido
		String splantilla=fondo;
		
	
		//Aqui le pasaremos los parametros de filtrado de la peticion
		//File reporteCreado = null;
		
		//File reportado=ResourceUtils.getFile("classpath:Plantilla5_FondoCoches.jrxml");
		
		//Leemos  nuestro Reporte //Cambiamos el tipo de psaramtros
		if (fondo.equalsIgnoreCase("AUT")) {
			pTitulo="Fondo de Coches Plantillas tipo Esma 5";
			reporteCreado=ResourceUtils.getFile(plantBase5);
			System.out.println("Lugar del plantilla --> "+reporteCreado);
		}
		
		if (fondo.equalsIgnoreCase("CRM")) { 
			pTitulo="Fondo de Consumo Plantillas tipo Esma 6";
			reporteCreado=ResourceUtils.getFile(plantBase6);
			System.out.println("Lugar del plantilla --> "+reporteCreado);
		}
		
		//Compilamos el Reporte 
		
		JasperReport jasperC=JasperCompileManager.compileReport(reporteCreado.getAbsolutePath());
		
		JRBeanCollectionDataSource dataSources= new JRBeanCollectionDataSource(miplantilla); //conectamos al modelos
		
		//pasamos los parametros al Reporte
		
		Map<String, Object> parametros=new HashMap<>();
		parametros.put("miTitulo", pTitulo);
		
		JasperPrint jasperPrintS=JasperFillManager.fillReport(jasperC, parametros, dataSources);
		
		//parametros recibido desde la petcion, de ser necesario un +
		JasperExportManager.exportReportToHtmlFile(jasperPrintS, pathSalida+"\\reportePlantilla.html");
		
		if (fondo.equalsIgnoreCase("html")) {
			JasperExportManager.exportReportToHtmlFile(jasperPrintS, pathSalida+"\\reportePlantill6.html");
			//(pathSalida+"\\reportePlantilla6");
		}
		if (fondo.equalsIgnoreCase("xml")) { 
			JasperExportManager.exportReportToXmlFile(jasperPrintS, pathSalida+"\\reportePlantill6.xml", false); //sin Cabcera
		}
		if (fondo.equalsIgnoreCase("csv")) {
			
			//Salida CSV, proceso y metodo adicional
			 JRCsvMetadataExporter exporterCSV = new JRCsvMetadataExporter();
			 SimpleCsvMetadataExporterConfiguration configuration = new SimpleCsvMetadataExporterConfiguration();
				
			 
			 exporterCSV.setExporterInput(new SimpleExporterInput(jasperPrintS));
			 exporterCSV.setExporterOutput(new SimpleWriterExporterOutput(new File(pathSalida+"\\reportePlantill6.csv")));
			 
			 exporterCSV.setConfiguration(configuration);
			//JasperExportManager.exportReportToHtmlFile(jasperPrintS, pathSalida+"\\reportePalntill6.csv");
			}
		
		System.out.println("Hemos generado desde El Front de Angular con el ttulo del reporte Correspondiente--> " +pTitulo);
		return "El Reprte se ha Generado en la Siguiente Ruta --> "+pathSalida;
		
	}
	
	private static Date parseFecha(String fecha) {
		SimpleDateFormat formatoD= new SimpleDateFormat("dd-MM-yyyy");
		Date fechaNormal=null;
		try {
			fechaNormal=formatoD.parse(fecha);
			
		} catch (ParseException ex) {
			System.out.println(ex);
		}
		return fechaNormal;
	}
//	
//	@Override
//	public List<Plantilla_esma> listaEstructuras() {
//		return  pRepository.findAll()
//				.stream()
//				.collect(Collectors.toList());
//	}
//	
//	@Override
//	public String listaEstructura() {
//		return "Lista de Parametros";
//	}
//	
//	
//	
//	//Busqueda completa con parametros
//	@Override
//	public List<Plantilla_esma> buscarPorfondo(String fondo, String cod_plantilla, Date fechaI, Date fechaF) {
//		Predicate<Plantilla_esma> ffondo=ffon->ffon.getCod_fondo().equalsIgnoreCase(fondo);
//		Predicate<Plantilla_esma> ffesma=ffes->ffes.getPlantilla_esma().equalsIgnoreCase(cod_plantilla);
//	
//		//Predicate<String> ffIncial=fi->fi.getpInicial().equals(fInicial);
//		//covertimnos con la funcion  fecha
//		//Predicate<tmp_xml> ffFin=ff->ff.getpFin().equals(fFinal);
//		
//		return 
//				pRepository.findAll() //pudiera utilizar otro metodo filtrado del jpa
//				.stream()
//				//Agregamos las fechas de busquedas luego
//				.filter((ffondo).and(ffesma))
//				//.filter((ffondo).and(ffesma).and(fechaI).and(fechaF))
//				.collect(Collectors.toList());
//		
//	}
//	
//
//	
//	//////*****Otros Metoso Estadicos Qeu estaban en ElDAO*************/////////////
//
//	List<tmp_xml> tplantillas=new ArrayList<tmp_xml>(List.of(
//			new tmp_xml("pl5","AUT","2545454","2545454","900154545","900154545","ES",parseFecha("01-01-2021"),parseFecha("31-01-2021")),
//			new tmp_xml("pl5","AUT","2545455","2545455","900154578","900154578","ES",parseFecha("01-01-2021"),parseFecha("31-01-2021")),
//			new tmp_xml("pl5","AUT","2545534","2545534","900154580","900154580","ES",parseFecha("01-01-2021"),parseFecha("31-01-2021")),
//			new tmp_xml("pl5","AUT","2545145","2545145","900154599","900154599","ES",parseFecha("01-01-2021"),parseFecha("31-01-2021")),
//			new tmp_xml("pl6","CRM","2545859","2545859","900154500","900154500","ES",parseFecha("01-01-2021"),parseFecha("31-01-2021")),
//			new tmp_xml("pl6","AUT","2545222","2545222","900154577","900154577","ES",parseFecha("01-03-2021"),parseFecha("31-03-2021")),
//			new tmp_xml("pl5","AUT","2545454","2545454","900154597","900154597","ES",parseFecha("01-01-2021"),parseFecha("31-01-2021")),
//			new tmp_xml("pl5","AUT","2545458","2545458","900154520","900154520","ES",parseFecha("01-02-2021"),parseFecha("28-02-2021"))));
//
//	
//	@Override
//	public void generacionArchivoXML(String fondo, String cod_plantilla, String fInicial, String fFinal) {
//		System.setProperty("java.awt.headless", "false"); //cuando se manejba por pantalla
//		
//		String rp_fuente="src/main/resources/plantilla5.jrxml";
//		
//		String rp_parXML="src/main/resources/informes/miplantill5.xml";
//		
//		String rp_parCSV="src/main/resources/informes/miplantill5.csv";
//		
//		//opcional
//		String rp_parPDF="src/main/resources/informes/miplantill5.pdf";
//		
//		//parametros al Reporte hasMap
//			Map<String, Object> paramtroReporte= new HashMap<String, Object>();
//			paramtroReporte.put("titulo", "Fondo de Coches PLantillas tipo Esma 5");
//			paramtroReporte.put("autor", "ML");
//			
//			paramtroReporte.put("fondo", fondo);
//			paramtroReporte.put("tplantilla", cod_plantilla);
//			paramtroReporte.put("fechaI", fInicial);
//			paramtroReporte.put("fechaF", fFinal);
//			paramtroReporte.put("fecha", (new java.util.Date()).toString());
//		
//			//procesamiento Reporte
//			JasperReport miJasperCompilado = null;
//			try {
//				
//				miJasperCompilado = JasperCompileManager.compileReport(rp_fuente);
//				System.out.println("Hemos compilado el Reporte ");
//			} catch (JRException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			//la conexion a la BD, sin utilizacion de la notacion de spring,  tomamos una prueba de conexion
//			Connection miconexionP=null;
//			try {
//				miconexionP =  DriverManager.getConnection("jdbc:mysql://localhost:3306/generacion", "root", "Almighty1**");
//			} catch (SQLException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			//String miconexionP="";
//			
//			//Incopramos los datos leidos de la bd con los parametros
//				//rellenamos el reporte
//				//
//			//JasperPrint salidaInforme=JasperFillManager.fillReport(miJasperCompilado, paramtroReporte,miconexionP);
//			JasperPrint salidaInforme = null;
//			try {
//				salidaInforme = JasperFillManager.fillReport(miJasperCompilado, paramtroReporte, miconexionP);
//				//salidaInforme = JasperFillManager.fillReport(miJasperCompilado, paramtroReporte,miconexionP);
//				System.out.println("Le hemos psos los parametros al reporte ");
//			} catch (JRException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			 //Salida XML
//			 try {
//				JasperExportManager.exportReportToXmlFile(salidaInforme, rp_parXML,true);
//				System.out.println("Guardando el xml");
//				
//			} catch (JRException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} 
//			
//			 //Salida CSV, proceso y metodo adicional
//			 JRCsvMetadataExporter exporterCSV = new JRCsvMetadataExporter();
//			 SimpleCsvMetadataExporterConfiguration configuration = new SimpleCsvMetadataExporterConfiguration();
//				
//			 
//			 exporterCSV.setExporterInput(new SimpleExporterInput(salidaInforme));
//			 exporterCSV.setExporterOutput(new SimpleWriterExporterOutput(new File(rp_parCSV)));
//			 
//			 exporterCSV.setConfiguration(configuration);
//			 try {
//				exporterCSV.exportReport();
//			} catch (JRException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			 
//			 //Salida PDF
//			 try {
//				JasperExportManager.exportReportToPdfFile(salidaInforme,rp_parPDF);
//			} catch (JRException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//	
//			//vista por pantalla necesita una interfaz lanzadora para esto
//				//JasperViewer.viewReport(salidaInforme);
//				System.out.println("muestra el viewer,cuando se si posee interfaz");
//				//proceso de exportacion que se hace para cada formato
//		
//	}
//
//	@Override
//	public void listado(String fondo, String cod_plantilla, String fInicial, String fFinal) {
//		Predicate<tmp_xml> ffondo=ffon->ffon.getFondo().equalsIgnoreCase(fondo);
//		Predicate<tmp_xml> ffesma=ffes->ffes.getFormat_e().equalsIgnoreCase(cod_plantilla);
//		
//		Predicate<tmp_xml> filt_fIncial=fi->fi.getpInicial()==parseFecha(fInicial);
//		Predicate<tmp_xml> filt_fFin=ff->ff.getpFin()==parseFecha(fFinal);
//		
//		System.out.println("Listado de acuerdo a la listado de paramatros\n");
//		tplantillas
//		.stream()
//		.filter((ffondo).and(ffesma).and(filt_fIncial).and(filt_fFin))
//		.forEach(System.out::println);
//		
//	}
//
//	@Override
//	public List<Plantilla_esma> buscarfondo(Plantilla_esma plantilla_esma) {
//		// TODO Auto-generated method stub
//		return null;
//	}
	

	

}
