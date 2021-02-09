/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatypes;

import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.Proyecto;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Sofis Solutions
 */
public class DataVerValoresValorUT {
    AnioFiscal anioFiscal;
    UnidadTecnica ut;
    Proyecto proyecto;
    
    List<DataVerValoresValor> valores;
    BigDecimal totalValor ;
    BigDecimal totalMeta;

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

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public List<DataVerValoresValor> getValores() {
        return valores;
    }

    public void setValores(List<DataVerValoresValor> valores) {
        this.valores = valores;
    }

    public BigDecimal getTotalValor() {
        return totalValor;
    }

    public void setTotalValor(BigDecimal totalValor) {
        this.totalValor = totalValor;
    }

    public BigDecimal getTotalMeta() {
        return totalMeta;
    }

    public void setTotalMeta(BigDecimal totalMeta) {
        this.totalMeta = totalMeta;
    }
    
    
}
