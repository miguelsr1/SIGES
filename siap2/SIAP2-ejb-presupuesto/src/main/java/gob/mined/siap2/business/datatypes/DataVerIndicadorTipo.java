/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatypes;

import gob.mined.siap2.entities.data.impl.Categoria;
import java.util.List;

/**
 *
 * @author Sofis Solutions
 */
public class DataVerIndicadorTipo {
    
    private Categoria tipoIndicador;
    private List<DataVerValoresIndicadores> indicadores;

    public Categoria getTipoIndicador() {
        return tipoIndicador;
    }

    public void setTipoIndicador(Categoria tipoIndicador) {
        this.tipoIndicador = tipoIndicador;
    }

    public List<DataVerValoresIndicadores> getIndicadores() {
        return indicadores;
    }

    public void setIndicadores(List<DataVerValoresIndicadores> indicadores) {
        this.indicadores = indicadores;
    }

    
    
    
}
