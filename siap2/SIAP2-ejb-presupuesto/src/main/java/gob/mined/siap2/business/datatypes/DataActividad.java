/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatypes;

import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.POActividadBase;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.entities.enums.TipoPOAPAC;

/**
 *
 * @author Sofis Solutions
 */
public class DataActividad {
    String nombrePOA;
    String nombreActividad;
    UnidadTecnica ut;
    AnioFiscal anioFiscal;
    TipoPOAPAC tipoPOA;
    POActividadBase actividad;

    public String getNombrePOA() {
        return nombrePOA;
    }

    public void setNombrePOA(String nombrePOA) {
        this.nombrePOA = nombrePOA;
    }

    public String getNombreActividad() {
        return nombreActividad;
    }

    public void setNombreActividad(String nombreActividad) {
        this.nombreActividad = nombreActividad;
    }
    
    public UnidadTecnica getUt() {
        return ut;
    }

    public void setUt(UnidadTecnica ut) {
        this.ut = ut;
    }

    public AnioFiscal getAnioFiscal() {
        return anioFiscal;
    }

    public void setAnioFiscal(AnioFiscal anioFiscal) {
        this.anioFiscal = anioFiscal;
    }

    public TipoPOAPAC getTipoPOA() {
        return tipoPOA;
    }

    public void setTipoPOA(TipoPOAPAC tipoPOA) {
        this.tipoPOA = tipoPOA;
    }

    public POActividadBase getActividad() {
        return actividad;
    }

    public void setActividad(POActividadBase actividad) {
        this.actividad = actividad;
    }



}
