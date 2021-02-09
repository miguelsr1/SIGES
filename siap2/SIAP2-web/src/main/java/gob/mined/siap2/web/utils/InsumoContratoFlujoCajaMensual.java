/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.utils;

import gob.mined.siap2.entities.data.impl.POFlujoCajaMenusal;
import java.util.List;

/**
 *
 * @author Sofis Solutions
 */
public class InsumoContratoFlujoCajaMensual {
    private String tipo;
    private String insumo;
    private Integer anio;
    private List<POFlujoCajaMenusal> flujoCajaMenusalInsumo;

    public InsumoContratoFlujoCajaMensual(String tipo, String insumo, List<POFlujoCajaMenusal> flujoCajaMenusalInsumo) {
        this.tipo = tipo;
        this.insumo = insumo;
        this.flujoCajaMenusalInsumo = flujoCajaMenusalInsumo;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }
    
    
    public InsumoContratoFlujoCajaMensual() {
    }
   
    
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getInsumo() {
        return insumo;
    }

    public void setInsumo(String insumo) {
        this.insumo = insumo;
    }

    public List<POFlujoCajaMenusal> getFlujoCajaMenusalInsumo() {
        return flujoCajaMenusalInsumo;
    }

    public void setFlujoCajaMenusalInsumo(List<POFlujoCajaMenusal> flujoCajaMenusalInsumo) {
        this.flujoCajaMenusalInsumo = flujoCajaMenusalInsumo;
    }
    
}
