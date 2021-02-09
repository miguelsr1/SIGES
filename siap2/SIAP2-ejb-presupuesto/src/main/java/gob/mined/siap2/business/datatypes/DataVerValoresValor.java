/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatypes;

import java.math.BigDecimal;

/**
 *
 * @author Sofis Solutions
 */
public class DataVerValoresValor {
    BigDecimal valor;
    BigDecimal meta;
    
    private String alcanceYLimitante;    
    private String medioVerificacion;
    

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getMeta() {
        return meta;
    }

    public void setMeta(BigDecimal meta) {
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
