/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.utilidades;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import org.eclipse.microprofile.jwt.JsonWebToken;

/**
 * Método de gestión de los JWT
 *
 * @author USUARIO
 */
public class Lookup {

    /**
     * Devuelve el JWT
     *
     * @return
     * @throws Exception
     */
    public static JsonWebToken obtenerJWT() throws Exception {
        BeanManager beanManager = (BeanManager) new InitialContext().lookup("java:comp/BeanManager");
        Bean<JsonWebToken> bean = (Bean<JsonWebToken>) beanManager.getBeans(JsonWebToken.class).iterator().next();
        CreationalContext<JsonWebToken> ctx = beanManager.createCreationalContext(bean);
        return (JsonWebToken) beanManager.getReference(bean, JsonWebToken.class, ctx);
    }

}
