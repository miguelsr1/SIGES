/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.data.daos;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import org.apache.commons.lang3.StringUtils;

/**
 * Esta clase incluye los métodos para la gestión de las unidades técnicas.
 *
 * @author Sofis Solutions
 */
@JPADAO
public class UnidadTecnicaDAO extends EclipselinkJpaDAOImp implements Serializable {

    /**
     * Este método permite lockear una entidad. Aplicar en los casos de mutua
     * exclusión.
     *
     * @param entity
     * @param id
     */
    public void lock(Class entity, int id) {
        Object lock = entityManager.find(entity, id);
        entityManager.lock(lock, LockModeType.PESSIMISTIC_WRITE);
    }

    /**
     * Devuelve el entityManager
     *
     * @return
     */
    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    /**
     * Este método devuelve el objeto de una determina clase que tiene una clave
     * primaria indicada.
     *
     * @param c clase
     * @param primaryKey clave primaria
     * @return unidad técnica con la clave primaria indicada
     */
    public UnidadTecnica find(Class c, Integer primaryKey) {
        return (UnidadTecnica) entityManager.find(c, primaryKey);
    }

    /**
     * Este método permite obtener las unidades técnicas padre de la unidad
     * técnica.
     *
     * @param id
     * @return
     * @throws DAOGeneralException
     */
    public List<UnidadTecnica> getPadresByUnidadTecnicaId(Integer id) throws DAOGeneralException {
        try {
            Query q = entityManager.createQuery("SELECT u FROM UnidadTecnica u WHERE u.id <> :id").setParameter("id", id);
            return q.getResultList();
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }

    }

    /**
     * Este método permite obtener el conjunto de unidades técnicas que tienen
     * query como substring del nombre.
     *
     * @param query texto incluido en el nombre.
     * @return
     */
    public List<UnidadTecnica> getUTBynombre(String query) {
        return entityManager.createQuery("SELECT u "
                + " FROM UnidadTecnica u "
                + " WHERE u.nombreBusqueda LIKE :query"
                + " ORDER BY u.nombre")
                .setParameter("query", "%" + query + "%")
                .getResultList();

    }

    /**
     * Si existe alguna UT que tenga determinado código retorna true, si no
     * retorna false
     *
     * @param idUT
     * @param codigoUT
     * @return
     */
    public Boolean existeUTByCodigo(Integer idUT, String codigoUT) {
        List result = entityManager.createQuery("SELECT u "
                + " FROM UnidadTecnica u "
                + " WHERE u.codigo = :codigoUT "
                + " AND u.id !=  :idUT")
                .setParameter("idUT", idUT)
                .setParameter("codigoUT", codigoUT)
                .getResultList();
        return !result.isEmpty();
    }

}
