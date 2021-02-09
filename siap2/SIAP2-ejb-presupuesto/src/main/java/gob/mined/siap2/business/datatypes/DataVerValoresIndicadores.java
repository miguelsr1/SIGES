/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatypes;

import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.Indicador;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
public class DataVerValoresIndicadores {

    AnioFiscal anioFiscal;
    Indicador indicador;

    List<DataVerValoresValorUT> desgloce;

    List<DataVerValoresValor> valores;
    BigDecimal totalValor ;
    BigDecimal totalMeta;

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

    public List<DataVerValoresValorUT> getDesgloce() {
        return desgloce;
    }

    public void setDesgloce(List<DataVerValoresValorUT> desgloce) {
        this.desgloce = desgloce;
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

   
    

    public List<DataVerValoresValor> getValores() {
        return valores;
    }

    public void setValores(List<DataVerValoresValor> valores) {
        this.valores = valores;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.anioFiscal);
        hash = 29 * hash + Objects.hashCode(this.indicador);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DataVerValoresIndicadores other = (DataVerValoresIndicadores) obj;
        if (!Objects.equals(this.anioFiscal, other.anioFiscal)) {
            return false;
        }
        if (!Objects.equals(this.indicador, other.indicador)) {
            return false;
        }
        return true;
    }

}
