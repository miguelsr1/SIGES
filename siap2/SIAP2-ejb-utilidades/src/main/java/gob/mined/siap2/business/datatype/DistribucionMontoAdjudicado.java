package gob.mined.siap2.business.datatype;

import gob.mined.siap2.entities.data.impl.OrigenDistribuccionMontoInsumo;
import gob.mined.siap2.entities.data.impl.POMontoFuenteInsumo;
import java.math.BigDecimal;


/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */

public class DistribucionMontoAdjudicado {
    
    private BigDecimal monto;
    private POMontoFuenteInsumo fuente;

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public POMontoFuenteInsumo getFuente() {
        return fuente;
    }

    public void setFuente(POMontoFuenteInsumo fuente) {
        this.fuente = fuente;
    }
    
    
}
