package gob.mined.siap2.business.datatype;

import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionItem;
import java.util.List;

 
/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
public class DistribucionProgramacionPagosItem {
    private ProcesoAdquisicionItem item;
    private List<DistribucionMontoAdjudicado> totales;
    private List<DistribucionProgramacionPagosInsumos> distribuccionInsumos;

    public ProcesoAdquisicionItem getItem() {
        return item;
    }

    public void setItem(ProcesoAdquisicionItem item) {
        this.item = item;
    }

    public List<DistribucionMontoAdjudicado> getTotales() {
        return totales;
    }

    public void setTotales(List<DistribucionMontoAdjudicado> totales) {
        this.totales = totales;
    }

    public List<DistribucionProgramacionPagosInsumos> getDistribuccionInsumos() {
        return distribuccionInsumos;
    }

    public void setDistribuccionInsumos(List<DistribucionProgramacionPagosInsumos> distribuccionInsumos) {
        this.distribuccionInsumos = distribuccionInsumos;
    }
    
    
    
}
