/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.interceptors.log;

import gob.mined.siap2.business.ejbs.DatosUsuario;
import gob.mined.siap2.business.interceptors.annotations.SofisLog;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import java.io.Serializable;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
/**
 *
 * @author Sofis Solutions
 */


@SofisLog 
public class LoggedInterceptor implements Serializable{
    
    @Resource
  private javax.ejb.SessionContext ctx;
    @Inject
    private DatosUsuario datUsuario;
    
    
     private static final Logger logger =
            Logger.getLogger(ConstantesEstandares.LOGGER);
    
     public LoggedInterceptor() {
    }

    @AroundInvoke
    public Object logMethodEntry(InvocationContext invocationContext)
            throws Exception {
     
        return invocationContext.proceed();
    }
}
