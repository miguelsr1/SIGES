/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.mined.siap2.business.datatypes;

import gob.mined.siap2.entities.data.impl.Indicador;
import gob.mined.siap2.entities.data.impl.POAMetaAnual;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;

/**
 *
 * @author bruno
 */
public class DataMetaIndicadorPOA {
    private UnidadTecnica ut;
    private POAMetaAnual metaIndicador;
    private Indicador indicador;

    public UnidadTecnica getUt() {
        return ut;
    }

    public void setUt(UnidadTecnica ut) {
        this.ut = ut;
    }

    public POAMetaAnual getMetaIndicador() {
        return metaIndicador;
    }

    public void setMetaIndicador(POAMetaAnual metaIndicador) {
        this.metaIndicador = metaIndicador;
    }

    public Indicador getIndicador() {
        return indicador;
    }

    public void setIndicador(Indicador indicador) {
        this.indicador = indicador;
    }
    
    
}
