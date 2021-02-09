/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.datatypes;

import gob.mined.siap2.entities.data.impl.GeneralPOA;
import gob.mined.siap2.entities.data.impl.POActividadBase;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.data.impl.POLinea;
import java.math.BigDecimal;
import java.util.Objects;

/**
 *
 * @author rgonzalez
 */
public class DataPOTablaLinea {
    public final static String TIPO_LINEA = "TIPO_LINEA" ;
    public final static String TIPO_ACTIVIDAD = "TIPO_ACTIVIDAD" ;
    public final static String TIPO_INSUMO = "TIPO_INSUMO" ;
    
    
    
    String tipo;
    Integer numeroLinea;
    String componenteMacroActividad;
    String producto;
    String actividad;
    String insumo;
    BigDecimal montoTotal;
    
    
    
    GeneralPOA poa;
    POLinea poLinea;
    POActividadBase poActividad;
    POInsumos poIsumo;

    
    
    
    public boolean soyLinea(){
        return Objects.equals(tipo, TIPO_LINEA);
    }
    
    public boolean soyActividad(){
        return Objects.equals(tipo, TIPO_ACTIVIDAD);
    }
    
    public boolean soyInsumo(){
        return Objects.equals(tipo, TIPO_INSUMO);
    }
    
    
    public String getCssClass(){
        if (soyActividad()){
            return "row-actividad";
        }else if (soyInsumo()){
            return "row-insumo";
        }
        return "";
    }
    
    
    public Integer getNumeroLinea() {
        return numeroLinea;
    }

    public void setNumeroLinea(Integer numeroLinea) {
        this.numeroLinea = numeroLinea;
    }

    
   

    public String getComponenteMacroActividad() {
        return componenteMacroActividad;
    }

    public void setComponenteMacroActividad(String componenteMacroActividad) {
        this.componenteMacroActividad = componenteMacroActividad;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public String getInsumo() {
        return insumo;
    }

    public void setInsumo(String insumo) {
        this.insumo = insumo;
    }

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }

    public GeneralPOA getPoa() {
        return poa;
    }

    public void setPoa(GeneralPOA poa) {
        this.poa = poa;
    }

    public POLinea getPoLinea() {
        return poLinea;
    }

    public void setPoLinea(POLinea poLinea) {
        this.poLinea = poLinea;
    }

    public DataPOTablaLinea() {
    }

    
    

    public POActividadBase getPoActividad() {
        return poActividad;
    }

    public void setPoActividad(POActividadBase poActividad) {
        this.poActividad = poActividad;
    }

    public POInsumos getPoIsumo() {
        return poIsumo;
    }

    public void setPoIsumo(POInsumos poIsumo) {
        this.poIsumo = poIsumo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    
    
    
}
