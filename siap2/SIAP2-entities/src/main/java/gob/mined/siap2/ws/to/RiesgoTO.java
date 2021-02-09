/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.ws.to;

import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.LineaEstrategica;
import gob.mined.siap2.entities.data.impl.POARiesgo;
import gob.mined.siap2.entities.data.impl.ProgramaInstitucional;
import gob.mined.siap2.entities.data.impl.ProgramaPresupuestario;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;

/**
 *
 * @author Sofis Solutions
 */
public class RiesgoTO {
    private String nombreProyecto;
    private AnioFiscal anioFiscal;
    private LineaEstrategica lineaEstrategica;
    private ProgramaInstitucional programaInstitucional;
    private ProgramaPresupuestario programaPresupuestario;
    private POARiesgo riesgo; 
    private UnidadTecnica unidadTecnica;

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

    public ProgramaInstitucional getProgramaInstitucional() {
        return programaInstitucional;
    }

    public void setProgramaInstitucional(ProgramaInstitucional programaInstitucional) {
        this.programaInstitucional = programaInstitucional;
    }

    public ProgramaPresupuestario getProgramaPresupuestario() {
        return programaPresupuestario;
    }

    public void setProgramaPresupuestario(ProgramaPresupuestario programaPresupuestario) {
        this.programaPresupuestario = programaPresupuestario;
    }

    public POARiesgo getRiesgo() {
        return riesgo;
    }

    public void setRiesgo(POARiesgo riesgo) {
        this.riesgo = riesgo;
    }

    public String getNombreProyecto() {
        return nombreProyecto;
    }

    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }

    public UnidadTecnica getUnidadTecnica() {
        return unidadTecnica;
    }

    public void setUnidadTecnica(UnidadTecnica unidadTecnica) {
        this.unidadTecnica = unidadTecnica;
    }
    
}
