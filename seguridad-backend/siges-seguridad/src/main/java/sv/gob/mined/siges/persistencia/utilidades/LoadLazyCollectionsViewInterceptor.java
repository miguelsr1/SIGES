/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.utilidades;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import sv.gob.mined.siges.persistencia.annotations.AtributoInicializarColeccion;

@Interceptor
public class LoadLazyCollectionsViewInterceptor {

    private static final Logger LOGGER = Logger.getLogger(LoadLazyCollectionsViewInterceptor.class.getName());

    @AroundInvoke
    public Object intercept(InvocationContext ctx) throws Exception {
        try {
            Object result = ctx.proceed();
            LoadLazyCollectionsViewInterceptor.initializeLazyCollections(result);
            return result;
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static void initializeLazyCollections(Object o) {
        try {
            if (o != null) {
                for (Field field : o.getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    if (field.isAnnotationPresent(AtributoInicializarColeccion.class)) {
                        Object value = field.get(o);
                        if (Collection.class.isAssignableFrom(field.getType())) {
                            for (Object co : Collection.class.cast(value)) {
                                LoadLazyCollectionsViewInterceptor.initializeLazyCollections(co);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

}
