/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.ejbs;

import gob.mined.siap2.entities.constantes.Constantes;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Esta clase incluye métodos para la obtención de numeradores para el log de auditoría.
 * @author Sofis Solutions.
 */
@Stateless
@LocalBean
public class NumeradoresBean {

    @PersistenceContext(unitName = "generico-PU")
   EntityManager em;

    /**
     * Este método permite obtener un numero único para registrar operaciones en el log de auditoría.
     * @return 
     */
    public Long obtenerNumeroOperacion() {
        Query query = em.createNativeQuery("select nextval('"+Constantes.SCHEMA_SIAP2+".operaciones_log_seq')");
        return (Long) query.getSingleResult();
    }
}
