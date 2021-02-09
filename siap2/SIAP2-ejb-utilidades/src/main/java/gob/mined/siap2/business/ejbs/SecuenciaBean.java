/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.ejbs;

import gob.mined.siap2.business.interceptors.log.LoggedInterceptor;
import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.impl.Secuencia;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

/**
 * Session Bean que incluye los métodos para la gestión de los datos de configuración.
 * @author Sofis Solutions
 */
@Stateless(name = "SecuenciaBean")
@LocalBean
@Interceptors({LoggedInterceptor.class})
public class SecuenciaBean {
 
    @Inject @JPADAO
    private GeneralDAO generalDAO;
    
    private static final Logger logger =  Logger.getLogger(SecuenciaBean.class.getName());


    /**
     * Devuelve la secuencia que coincide con el código pasado por parámetro
     *
     * @param codigo
     * @return
     * @throws GeneralException
     */
    public Secuencia obtenerSecPorCodigo(String codigo) throws GeneralException {
        logger.log(Level.INFO, "obtenerSecPorCodigo");
        try {
            List<Secuencia> resultado = generalDAO.findByOneProperty(Secuencia.class, "codigo", codigo);
            if (resultado.size() == 1) {
                return resultado.get(0);
            } else if (resultado.isEmpty()) {
                return null;
            } else {
                BusinessException be = new BusinessException();
                be.addError(ConstantesErrores.ERROR_DEMASIADOS_RESULTADOS);
                throw be;
            }
        } catch (DAOGeneralException ex) {
            logger.log(Level.SEVERE, ex.getMessage() , ex);
            TechnicalException te = new TechnicalException();
            te.addError(ex.getMessage());
            throw te;
        }
    }
    
    

  
}
