package gob.mined.siap2.business.datatype;

import java.util.List;



/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */

public class DistribucionProgramacionPagosMes {
    private Integer mes;
    private Integer anio;
    
    private List<DistribucionMontoAdjudicado> totales;
    private List<DistribucionProgramacionPagosItem> distribuccionItems;

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public List<DistribucionMontoAdjudicado> getTotales() {
        return totales;
    }

    public void setTotales(List<DistribucionMontoAdjudicado> totales) {
        this.totales = totales;
    }

    public List<DistribucionProgramacionPagosItem> getDistribuccionItems() {
        return distribuccionItems;
    }

    public void setDistribuccionItems(List<DistribucionProgramacionPagosItem> distribuccionItems) {
        this.distribuccionItems = distribuccionItems;
    }
    
    
    
    
    
    
    
}
