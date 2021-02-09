/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatypes;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author sofis
 */
public class LineaMetaIndicadorTO implements Serializable{
    
    private String programa;
    private String proyecto;
    private String indicador;
    private BigDecimal meta;
    private BigDecimal actual;
    private BigDecimal finToleranciaVerde;
    private BigDecimal finToleranciaAmarillo;
    
    
    public String getKey(){
        return programa+proyecto+indicador;
    }

    public String getPrograma() {
        return programa;
    }

    public void setPrograma(String programa) {
        this.programa = programa;
    }

    public String getProyecto() {
        return proyecto;
    }

    public void setProyecto(String proyecto) {
        this.proyecto = proyecto;
    }

    public String getIndicador() {
        return indicador;
    }

    public void setIndicador(String indicador) {
        this.indicador = indicador;
    }

    public BigDecimal getMeta() {
        return meta;
    }

    public void setMeta(BigDecimal meta) {
        this.meta = meta;
    }

    public BigDecimal getActual() {
        return actual;
    }

    public void setActual(BigDecimal actual) {
        this.actual = actual;
    }
    
    public int compareTo(LineaMetaIndicadorTO o){
        return getKey().compareTo(o.getKey());
    } 

    public BigDecimal getFinToleranciaVerde() {
        return finToleranciaVerde;
    }

    public void setFinToleranciaVerde(BigDecimal finToleranciaVerde) {
        this.finToleranciaVerde = finToleranciaVerde;
    }

    public BigDecimal getFinToleranciaAmarillo() {
        return finToleranciaAmarillo;
    }

    public void setFinToleranciaAmarillo(BigDecimal finToleranciaAmarillo) {
        this.finToleranciaAmarillo = finToleranciaAmarillo;
    }
    
    
    
    
    
}
