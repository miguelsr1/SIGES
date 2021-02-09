/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.auditoria;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.persistence.Id;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.utils.ReflectionUtils;

@Interceptor
public class AuditInterceptor {
    
    private static final Logger LOGGER = Logger.getLogger(AuditInterceptor.class.getName());
    
    @Inject
    private AuditoriaBean auditoriaBean;
    
    @Inject
    @Claim("ip")
    private String callerIp;
    
    @Inject
    private JsonWebToken callerJWT;
    
    @AroundInvoke
    public Object intercept(InvocationContext ctx) throws Exception {
        try {           
            Object result = ctx.proceed();
            auditoriaBean.guardarConsumo(callerJWT != null ? callerJWT.getName() : null, callerIp, ctx.getTarget().getClass().getName(), ctx.getMethod().getName(), obtenerIdObjeto(ctx), null);
            return result;
        } catch (Exception ex) {
            auditoriaBean.guardarExcepcion(callerJWT != null ? callerJWT.getName() : null, callerIp, ctx.getTarget().getClass().getName(), ctx.getMethod().getName(), obtenerIdObjeto(ctx), ex.getMessage());
            throw ex;
        }
        
    }
    
    private Long obtenerIdObjeto(InvocationContext ctx) throws Exception {
        
        Object[] parameters = ctx.getParameters();
        Long entidadId = null;
        
        try {
            
            if (parameters.length > 0) {
                
                Object o = parameters[0];
                
                if (o instanceof Long) {
                    entidadId = (Long) o;
                } else if (o instanceof Integer) {
                    entidadId = new Long((Integer) o);
                } else {
                    Field f = ReflectionUtils.obtenerCampoAnotado(o.getClass(), Id.class);
                    if (f != null) {
                        entidadId = (Long) f.get(o);
                    }
                }
            }
            
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
        
        return entidadId;
        
    }
}
