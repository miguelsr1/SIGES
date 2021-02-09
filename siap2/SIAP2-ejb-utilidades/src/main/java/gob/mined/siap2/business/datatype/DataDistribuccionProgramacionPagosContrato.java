package gob.mined.siap2.business.datatype;

import gob.mined.siap2.entities.data.impl.ContratoOC;
import java.util.List;



/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
public class DataDistribuccionProgramacionPagosContrato {
    private ContratoOC contrato;
    private List<DistribucionMontoAdjudicado> totales;
    private List<DistribucionProgramacionPagosMes> distribuccionMeses;

    public ContratoOC getContrato() {
        return contrato;
    }

    public void setContrato(ContratoOC contrato) {
        this.contrato = contrato;
    }

    public List<DistribucionMontoAdjudicado> getTotales() {
        return totales;
    }

    public void setTotales(List<DistribucionMontoAdjudicado> totales) {
        this.totales = totales;
    }

    public List<DistribucionProgramacionPagosMes> getDistribuccionMeses() {
        return distribuccionMeses;
    }

    public void setDistribuccionMeses(List<DistribucionProgramacionPagosMes> distribuccionMeses) {
        this.distribuccionMeses = distribuccionMeses;
    }
    
    
    
}
