/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.mined.siap2.business.datatypes;

import gob.mined.siap2.entities.data.impl.Categoria;
import java.util.List;

/**
 *
 * @author bruno
 */
public class DataVerIndicadorPOA {   
    
    private Categoria tipoIndicador;
    private List<DataVerValoresIndicadoresPOA> indicadores;

    public Categoria getTipoIndicador() {
        return tipoIndicador;
    }

    public void setTipoIndicador(Categoria tipoIndicador) {
        this.tipoIndicador = tipoIndicador;
    }

    public List<DataVerValoresIndicadoresPOA> getIndicadores() {
        return indicadores;
    }

    public void setIndicadores(List<DataVerValoresIndicadoresPOA> indicadores) {
        this.indicadores = indicadores;
    }

}
