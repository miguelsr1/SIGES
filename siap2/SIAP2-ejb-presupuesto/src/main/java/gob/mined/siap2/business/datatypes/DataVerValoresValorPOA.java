/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatypes;

/**
 *
 * @author Sofis Solutions
 */
public class DataVerValoresValorPOA {
    private Integer valor;
    private Integer meta;
    private String alcanceYLimitante;    
    private String medioVerificacion;
    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }

    public Integer getMeta() {
        return meta;
    }

    public void setMeta(Integer meta) {
        this.meta = meta;
    }

    public String getAlcanceYLimitante() {
        return alcanceYLimitante;
    }

    public void setAlcanceYLimitante(String alcanceYLimitante) {
        this.alcanceYLimitante = alcanceYLimitante;
    }

    public String getMedioVerificacion() {
        return medioVerificacion;
    }

    public void setMedioVerificacion(String medioVerificacion) {
        this.medioVerificacion = medioVerificacion;
    }
    
    
}
