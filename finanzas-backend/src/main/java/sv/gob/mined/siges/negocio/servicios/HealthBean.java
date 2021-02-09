/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.eclipse.microprofile.opentracing.Traced;

/**
 * Testing de conexòn a base de datos
 *
 * @author Sofis Solutions
 */
@Stateless
@Traced
public class HealthBean {

    @PersistenceContext
    private EntityManager em;

    /**
     * Devuelve true si la conexión con la base de datos funciona correctamente,
     * false en caso contrario.
     *
     * @return Boolean
     */
    public Boolean checkDatabaseConnection() {
        try {
            em.createNativeQuery("SELECT count(*)").getSingleResult();
            return Boolean.TRUE;
        } catch (Exception ex) {
            return Boolean.FALSE;
        }
    }

}
