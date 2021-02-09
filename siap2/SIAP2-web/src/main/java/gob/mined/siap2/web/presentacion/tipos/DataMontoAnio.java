/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.presentacion.tipos;

import gob.mined.siap2.entities.data.impl.AsignacionNoProgramable;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import java.util.List;

/**
 *
 * @author Sofis Solutions
 */
public class DataMontoAnio {
    UnidadTecnica ut;
    AsignacionNoProgramable asignacionNoProgramable;
    List montoAnios;

    public UnidadTecnica getUt() {
        return ut;
    }

    public void setUt(UnidadTecnica ut) {
        this.ut = ut;
    }

    public List getMontoAnios() {
        return montoAnios;
    }

    public void setMontoAnios(List montoAnios) {
        this.montoAnios = montoAnios;
    }

    public AsignacionNoProgramable getAsignacionNoProgramable() {
        return asignacionNoProgramable;
    }

    public void setAsignacionNoProgramable(AsignacionNoProgramable asignacionNoProgramable) {
        this.asignacionNoProgramable = asignacionNoProgramable;
    }
    
    
    
    
}
