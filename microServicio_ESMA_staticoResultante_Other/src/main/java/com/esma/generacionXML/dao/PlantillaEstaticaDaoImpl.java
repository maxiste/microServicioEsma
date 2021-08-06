//package com.esma.generacionXML.dao;
//
//import java.io.File;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.function.Predicate;
//import java.util.stream.Collectors;
//
//import org.apache.commons.collections.map.HashedMap;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Repository;
////import org.springframework.stereotype.Service;
//
////import com.esma.generacionXML.dao._PlantillaRepository;
////import com.esma.generacionXML.modelo.Plantilla_esma;
//import com.esma.generacionXML.modelo.tmp_xml;
//
//import net.sf.jasperreports.engine.JRException;
//import net.sf.jasperreports.engine.JasperCompileManager;
//import net.sf.jasperreports.engine.JasperExportManager;
//import net.sf.jasperreports.engine.JasperFillManager;
//import net.sf.jasperreports.engine.JasperPrint;
//import net.sf.jasperreports.engine.JasperReport;
//import net.sf.jasperreports.engine.export.JRCsvExporter;
//import net.sf.jasperreports.engine.export.JRCsvMetadataExporter;
//import net.sf.jasperreports.export.SimpleCsvExporterConfiguration;
//import net.sf.jasperreports.export.SimpleCsvMetadataExporterConfiguration;
//import net.sf.jasperreports.export.SimpleExporterInput;
//import net.sf.jasperreports.export.SimpleWriterExporterOutput;
//import net.sf.jasperreports.view.JasperViewer;
//
//
//@Repository
//public class PlantillaEstaticaDaoImpl implements PlantillaEstaticaDao {
//
//
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
//
//	@Override
//	public List<tmp_xml> listaEstructura() {
//		return  tplantillas
//			.stream()
//			.collect(Collectors.toList());
//	}
//
//	@Override
//	public List<tmp_xml> buscarPorfondo(String fondo, String cod_plantilla, String fInicial, String fFinal) {
//		Predicate<tmp_xml> ffondo=ffon->ffon.getFondo().equalsIgnoreCase(fondo);
//		Predicate<tmp_xml> ffesma=ffes->ffes.getFormat_e().equalsIgnoreCase(cod_plantilla);
//		
//		Predicate<tmp_xml> ffIncial=fi->fi.getpInicial().equals(fInicial);
//		//covertimnos con la funcion  fecha
//		Predicate<tmp_xml> ffFin=ff->ff.getpFin().equals(fFinal);
//		
//		return 
//				tplantillas
//				.stream()
//				//Agregamos las fechas de busquedas 
//				.filter((ffondo).and(ffesma).and(ffIncial).and(ffFin))
//				.collect(Collectors.toList());
//				
//
//	}
//	
//	//Validacion por Consola que tome lo que se pide
//	//errores con las convwersion de fecha que hay que validar
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
//	private static Date parseFecha(String fecha) {
//		SimpleDateFormat formatoD= new SimpleDateFormat("dd-MM-yyyy");
//		Date fechaNormal=null;
//		try {
//			fechaNormal=formatoD.parse(fecha);
//			
//		} catch (ParseException ex) {
//			System.out.println(ex);
//		}
//		return fechaNormal;
//	}
//
//
//	@Override
//	public List<tmp_xml> buscarfondo(tmp_xml modelotm) {
//		
//		return null;
//	}
//
//	@Override
//	public void generacionArchivoXML(String fondo, String cod_plantilla, String fInicial, String fFinal) {
//		System.setProperty("java.awt.headless", "false");
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
//		
//	}
//	
//	
////-----------------------------------Pruebas --------------------------//	
////	
////	@Override
////	public List<Plantilla_esma> buscarPorfondo(String fondo) {
////		return tplantillas
////				.stream()
////				.filter(fon->fon.getCod_fondo().equalsIgnoreCase(fondo))
////				.collect(Collectors.toList());
////		
////				//.toList();
////		// TODO Auto-generated method stub
////		//return null;
////		
////	}
//
//
////	@Override
////	public List<Plantilla_esma> listaEstructura() {
////		return  tplantillas
////			.stream()
////			.collect(Collectors.toList());
////	}
//
//	////
//////@Autowired
//////_PlantillaRepository plantillaRep;
////
////@Override
////public List<Plantilla_esma> buscarPorfondo(String fondo) {
////	return plantillaRep.findBycod_fondo(fondo);
////		
////}
//}
