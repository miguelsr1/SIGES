/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.mined.siap2.business.datatypes;

import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.Indicador;
import java.util.List;

/**
 *
 * @author bruno
 */
public class DataVerValoresIndicadoresPOA {
    AnioFiscal anioFiscal;
    Indicador indicador;

    List<DataVerValoresValorUTPOA> desgloce;

    List<DataVerValoresValorPOA> valores;
    Integer totalValor ;
    Integer totalMeta;

    String alcanceYLimitante;    
    String medioVerificacion;
    
    public AnioFiscal getAnioFiscal() {
        return anioFiscal;
    }

    public void setAnioFiscal(AnioFiscal anioFiscal) {
        this.anioFiscal = anioFiscal;
    }

    public Indicador getIndicador() {
        return indicador;
    }

    public void setIndicador(Indicador indicador) {
        this.indicador = indicador;
    }

    public List<DataVerValoresValorUTPOA> getDesgloce() {
        return desgloce;
    }

    public void setDesgloce(List<DataVerValoresValorUTPOA> desgloce) {
        this.desgloce = desgloce;
    }

    public List<DataVerValoresValorPOA> getValores() {
        return valores;
    }

    public void setValores(List<DataVerValoresValorPOA> valores) {
        this.valores = valores;
    }

    public Integer getTotalValor() {
        return totalValor;
    }

    public void setTotalValor(Integer totalValor) {
        this.totalValor = totalValor;
    }

    public Integer getTotalMeta() {
        return totalMeta;
    }

    public void setTotalMeta(Integer totalMeta) {
        this.totalMeta = totalMeta;
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
