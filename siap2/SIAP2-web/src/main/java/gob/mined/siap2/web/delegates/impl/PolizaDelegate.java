/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.delegates.impl;

import gob.mined.siap2.business.PolizaBean;
import gob.mined.siap2.entities.data.impl.FacturaPolizaConcentracion;
import gob.mined.siap2.entities.data.impl.PolizaDeConcentracion;
import gob.mined.siap2.exceptions.GeneralException;
import java.io.Serializable;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Sofis Solutions
 */
@Named
public class PolizaDelegate implements Serializable {

    @Inject
    private PolizaBean bean;

    /**
     * Devuelve una Póliza de concentración por el id
     * @param id
     * @return
     * @throws GeneralException 
     */
    public PolizaDeConcentracion getPoliza(Integer id) throws GeneralException {
        return bean.getPoliza(id);
    }

    /**
     * Guarda una Póliza de concentración
     * @param poliza
     * @return
     * @throws GeneralException 
     */
    public PolizaDeConcentracion guardarPoliza(PolizaDeConcentracion poliza, Integer idPOMontoFuenteFCM) throws GeneralException {
        return bean.guardarPoliza(poliza, idPOMontoFuenteFCM);
    }

    /**
     * Guarda una factura sin asociar a la Póliza de Concentración 
     * @param tempFactura
     * @return 
     */
    public FacturaPolizaConcentracion guardarFactura(FacturaPolizaConcentracion factura) {
        return bean.guardarFactura(factura);
    }
    
}
