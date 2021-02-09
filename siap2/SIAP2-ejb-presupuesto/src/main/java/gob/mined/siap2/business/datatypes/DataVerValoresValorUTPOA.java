/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatypes;

import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.POA;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import java.util.List;

/**
 *
 * @author Sofis Solutions
 */
public class DataVerValoresValorUTPOA {
    AnioFiscal anioFiscal;
    UnidadTecnica ut;
    POA poa;
    List<DataVerValoresValorPOA> valores;
    Integer totalValor ;
    Integer totalMeta;

    
    public AnioFiscal getAnioFiscal() {
        return anioFiscal;
    }

    public void setAnioFiscal(AnioFiscal anioFiscal) {
        this.anioFiscal = anioFiscal;
    }

    public UnidadTecnica getUt() {
        return ut;
    }

    public void setUt(UnidadTecnica ut) {
        this.ut = ut;
    }

    public POA getPoa() {
        return poa;
    }

    public void setPoa(POA poa) {
        this.poa = poa;
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
    
    
}
