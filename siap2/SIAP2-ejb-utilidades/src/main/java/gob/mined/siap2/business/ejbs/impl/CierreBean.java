/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.ejbs.impl;

import gob.mined.siap2.business.interceptors.log.LoggedInterceptor;
import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.TechnicalException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

/**
 *
 * @author Sofis Solutions
 */
@Stateless(name = "CierreBean")
@LocalBean
@Interceptors({LoggedInterceptor.class})
public class CierreBean {
    
    @Inject
    @JPADAO
    private GeneralDAO generalDAO;

    private static final Logger logger = Logger.getLogger(CierreBean.class.getName());

    /**
     * Este método permite cerrar una año fiscal
     * @param idAnioFiscal 
     */
    public void cerrarAnioFiscal(Integer idAnioFiscal) {
        try {
            AnioFiscal anio = (AnioFiscal) generalDAO.find(AnioFiscal.class, idAnioFiscal);
            anio.setCerrado(true);
            anio.setHabilitadoEjecucion(false);
            anio.setHabilitadoPlanificacion(false);            
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException ge = new TechnicalException();
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

}
