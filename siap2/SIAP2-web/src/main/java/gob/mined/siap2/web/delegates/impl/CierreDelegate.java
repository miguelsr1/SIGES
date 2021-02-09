/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.delegates.impl;

import gob.mined.siap2.business.ejbs.impl.CierreBean;
import java.io.Serializable;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Sofis Solutions
 */
@Named
public class CierreDelegate implements Serializable {

    @Inject
    private CierreBean bean;

    /**
     * Este método permite cerrar una año fiscal
     * @param idAnioFiscal 
     */
    public void cerrarAnioFiscal(Integer idAnioFiscal) {
        bean.cerrarAnioFiscal(idAnioFiscal);
    }
}
