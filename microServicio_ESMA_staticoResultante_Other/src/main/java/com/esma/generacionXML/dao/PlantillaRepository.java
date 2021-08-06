package com.esma.generacionXML.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.esma.generacionXML.modelo.Plantilla_esma;



public interface PlantillaRepository extends JpaRepository<Plantilla_esma, Integer> {

	//List<Plantilla_esma> findBycod_fondo_cod_plantilla_fInicio(String fondo, String cod_plantilla, String fInicial, String fFinal);
	//List<Plantilla_esma> findByCod_fondoAndPlantilla_esma(String fondo, String cod_plantilla);
}
