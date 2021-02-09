/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatype;

import java.io.Serializable;

/**
 *
 * @author Sofis Solutions
 */
public class DataRiesgo extends DataReporteTemplate implements Serializable {

    private String proyecto;
    private String unidadTecnica;
    private String riesgo;
    private String origen;
    private String valoracionRiesgo;
    private String mitigacion;
    private String contingencia;
    private String renponsable;

    public String getProyecto() {
        return proyecto;
    }

    public void setProyecto(String proyecto) {
        this.proyecto = proyecto;
    }

    public String getUnidadTecnica() {
        return unidadTecnica;
    }

    public void setUnidadTecnica(String unidadTecnica) {
        this.unidadTecnica = unidadTecnica;
    }

    public String getRiesgo() {
        return riesgo;
    }

    public void setRiesgo(String riesgo) {
        this.riesgo = riesgo;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getValoracionRiesgo() {
        return valoracionRiesgo;
    }

    public void setValoracionRiesgo(String valoracionRiesgo) {
        this.valoracionRiesgo = valoracionRiesgo;
    }

    public String getMitigacion() {
        return mitigacion;
    }

    public void setMitigacion(String mitigacion) {
        this.mitigacion = mitigacion;
    }

    public String getContingencia() {
        return contingencia;
    }

    public void setContingencia(String contingencia) {
        this.contingencia = contingencia;
    }

    public String getRenponsable() {
        return renponsable;
    }

    public void setRenponsable(String renponsable) {
        this.renponsable = renponsable;
    }

}
