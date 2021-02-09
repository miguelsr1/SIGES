/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.datatypes;

import gob.mined.siap2.entities.data.impl.ComboValorSeguimiento;
import gob.mined.siap2.entities.data.impl.POIndicadorLinea;

/**
 *
 * @author Sofis Solutions
 */
public class DataComboValorSeguimiento {
    Integer anio;
    String indicador;
    ComboValorSeguimiento comboValorSeguimiento;
    POIndicadorLinea lineaIndicador;
    Integer index;

    public Integer getAnio() {
        return anio;
    }

    public POIndicadorLinea getLineaIndicador() {
        return lineaIndicador;
    }

    public void setLineaIndicador(POIndicadorLinea lineaIndicador) {
        this.lineaIndicador = lineaIndicador;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public String getIndicador() {
        return indicador;
    }

    public void setIndicador(String indicador) {
        this.indicador = indicador;
    }

    public ComboValorSeguimiento getComboValorSeguimiento() {
        return comboValorSeguimiento;
    }

    public void setComboValorSeguimiento(ComboValorSeguimiento comboValorSeguimiento) {
        this.comboValorSeguimiento = comboValorSeguimiento;
    }
    
    
    
}
