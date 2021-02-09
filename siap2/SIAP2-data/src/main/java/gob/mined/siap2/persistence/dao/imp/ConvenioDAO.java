/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.entities.data.impl.Convenio;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import java.util.List;

/**
 * Esta clase implementa los métodos de acceso a datos vinculados a Convenio
 * @author Sofis Solutions
 */
@JPADAO
public class ConvenioDAO extends EclipselinkJpaDAOImp<Convenio, Integer> {

    /**
     * Devuelve el Convenio con el id indicado.
     * @param primaryKey
     * @return 
     */
    public Convenio find(Integer primaryKey) {
        return entityManager.find(Convenio.class, primaryKey);
    }

    /**
     * Este método devuelve el convenio que tiene el código indicado en query.
     * @param query código del convenio
     * @return lista de convenios con el código indicado.
     */
    public List<Convenio> getConveniosHabilitadosPorCodigo(String query) {
        return entityManager.createQuery("SELECT c"
                + " FROM Convenio c"
                + " WHERE UPPER(c.codigo) LIKE (:query) "
                + " AND c.habilitado=TRUE "
                + " ORDER BY c.nombre")
                .setParameter("query", "%"+query+"%")
                .getResultList();
    }

  
}
