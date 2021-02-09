/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.utilidades;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor
public class LoadLazyCollectionsViewInterceptor {

    @AroundInvoke
    public Object intercept(InvocationContext ctx) throws Exception {
        try {
            Object result = ctx.proceed();
            InitializeObjectUtils.initializeLazyCollectionsRecursive(result);
            return result;
        } catch (Exception ex) {
            throw ex;
        }
    } 

}
