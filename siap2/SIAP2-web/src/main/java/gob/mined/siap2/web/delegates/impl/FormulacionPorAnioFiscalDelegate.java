/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.delegates.impl;

import gob.mined.siap2.business.ejbs.impl.FormulacionPorAnioFiscalBean;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import java.io.Serializable;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Esta clase implementa un delegate de la Formulación por año fiscal
 * @author Sofis Solutions
 */
@Named
public class FormulacionPorAnioFiscalDelegate implements Serializable {
    @Inject
    private FormulacionPorAnioFiscalBean bean;

    /**
     * Este método crea o actualiza para un año fiscal
     * @param formulacion 
     */
    public void crearActualizarFormulacion(AnioFiscal formulacion) {
        bean.crearActualizarFormulacion(formulacion);
    }

    /**
     * Este método elimina una formulación para un id dado.
     * @param id 
     */
    public void eliminarFormulacion(Integer id) {
        bean.eliminarFormulacion(id);
    }
    
  
   
}
