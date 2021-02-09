package gob.mined.siap2.business.datatype;

import gob.mined.siap2.entities.data.impl.RelacionProAdqItemInsumo;
import java.util.List;


/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */


public class DistribucionProgramacionPagosInsumos {
    private RelacionProAdqItemInsumo insumo;
    private List<DistribucionMontoAdjudicado> distribuccion;

    public RelacionProAdqItemInsumo getInsumo() {
        return insumo;
    }

    public void setInsumo(RelacionProAdqItemInsumo insumo) {
        this.insumo = insumo;
    }

    public List<DistribucionMontoAdjudicado> getDistribuccion() {
        return distribuccion;
    }

    public void setDistribuccion(List<DistribucionMontoAdjudicado> distribuccion) {
        this.distribuccion = distribuccion;
    }
    
    
    
    
}
