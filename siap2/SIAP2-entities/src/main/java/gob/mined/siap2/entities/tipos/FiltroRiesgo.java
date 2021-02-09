/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.tipos;

import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.LineaEstrategica;
import gob.mined.siap2.entities.data.impl.ProgramaInstitucional;
import gob.mined.siap2.entities.data.impl.ProgramaPresupuestario;
import java.io.Serializable;

/**
 *
 * @author Sofis Solutions
 */
public class FiltroRiesgo implements Serializable {
    private  AnioFiscal anioFiscal;
    private ProgramaInstitucional programaInstitucional;
    private ProgramaPresupuestario programaPresupuestario;
    private LineaEstrategica lineaEstrategica;

    public AnioFiscal getAnioFiscal() {
        return anioFiscal;
    }

    public void setAnioFiscal(AnioFiscal anioFiscal) {
        this.anioFiscal = anioFiscal;
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

    public LineaEstrategica getLineaEstrategica() {
        return lineaEstrategica;
    }

    public void setLineaEstrategica(LineaEstrategica lineaEstrategica) {
        this.lineaEstrategica = lineaEstrategica;
    }
    
    
    
}
