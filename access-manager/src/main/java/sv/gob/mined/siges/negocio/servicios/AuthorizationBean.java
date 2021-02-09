/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;

@Stateless
public class AuthorizationBean {

    @PersistenceContext
    private EntityManager em;


    
    /**
     * Devuelve los codigos de las operaciones del usuario con el codigo
     * indicado
     *
     * @param codigo String
     * @return Lista de String
     * @throws GeneralException
     */
    public List<String> obtenerOperacionesPorUsuarioCodigo(String codigo, List<Long> categoriasOperacionPk) throws GeneralException {
        if (codigo != null) {
            try {

                String query = "Select operacion_codigo from " + Constantes.SCHEMA_SEGURIDAD + ".permisos where usuario_codigo = :cod";

                if (categoriasOperacionPk != null && !categoriasOperacionPk.isEmpty()) {
                    query = query + " and operacion_categoria IN (:catoperacion)";
                }

                Query q = em.createNativeQuery(query);

                q.setParameter("cod", codigo);

                if (categoriasOperacionPk != null && !categoriasOperacionPk.isEmpty()) {
                    q.setParameter("catoperacion", categoriasOperacionPk);
                }

                return q.getResultList();
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return null;
    }

    
}
