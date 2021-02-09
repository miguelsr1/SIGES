/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.mined.siap2.ws.to;

import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.LineaEstrategica;
import gob.mined.siap2.entities.data.impl.POARiesgoPOA;
import gob.mined.siap2.entities.data.impl.Programa;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;

/**
 *
 * @author bruno
 */
public class RiesgoTOPOA {
    private String nombre;
    private AnioFiscal anioFiscal;
    private LineaEstrategica lineaEstrategica;
    private Programa programaInstitucional;
    private POARiesgoPOA riesgo; 
    private UnidadTecnica unidadTecnica;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public AnioFiscal getAnioFiscal() {
        return anioFiscal;
    }

    public void setAnioFiscal(AnioFiscal anioFiscal) {
        this.anioFiscal = anioFiscal;
    }

    public LineaEstrategica getLineaEstrategica() {
        return lineaEstrategica;
    }

    public void setLineaEstrategica(LineaEstrategica lineaEstrategica) {
        this.lineaEstrategica = lineaEstrategica;
    }

    public Programa getProgramaInstitucional() {
        return programaInstitucional;
    }

    public void setProgramaInstitucional(Programa programaInstitucional) {
        this.programaInstitucional = programaInstitucional;
    }

    public POARiesgoPOA getRiesgo() {
        return riesgo;
    }

    public void setRiesgo(POARiesgoPOA riesgo) {
        this.riesgo = riesgo;
    }

    public UnidadTecnica getUnidadTecnica() {
        return unidadTecnica;
    }

    public void setUnidadTecnica(UnidadTecnica unidadTecnica) {
        this.unidadTecnica = unidadTecnica;
    }
    
    
}
