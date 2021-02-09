/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.data.daos;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.entities.data.impl.Indicador;
import gob.mined.siap2.entities.enums.EstadoComun;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase gestiona en forma genérica las codigueras/catálogos del sistema.
 * @author Sofis Solutions
 * @param <T>
 */
@JPADAO
public class CodigueraDAO<T>  extends EclipselinkJpaDAOImp<T, Integer> implements Serializable {
    
     /**
      * Método genérico que permite obtener los objetos habilitados de un determinado tipo
      * @param c clase de los objetos
      * @param campoHabilitado nombre del campo habilitado
      * @param campoDescripcion nombre del campo nombre o descripción
      * @return
      * @throws DAOGeneralException 
      */   
     public List<T> obtenerHabilitados(Class c, String campoHabilitado,String campoDescripcion) throws DAOGeneralException {
        List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();
        MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                MatchCriteriaTO.types.EQUALS, campoHabilitado,
                Boolean.TRUE);
        criterios.add(criterio);

        CriteriaTO condicion = criterios.get(0);
        String[] orderBy = {campoDescripcion};
        boolean[] asc = {true};
        return  findEntityByCriteria(c, condicion, orderBy, asc, 0L, 1000L);
    }
     
     
     
     /**
      * Este método permite obtener los indicadores de tipo producto que están habilitados.
      * @return 
      */
    public List<Indicador> getProductosHabilitados() {
        return entityManager.createQuery("SELECT p"
                + " FROM Indicador p"
                + " WHERE p.estado = :estado "
                + " AND p.tipo.esProducto = TRUE"
                + " ORDER BY p.nombre")
                .setParameter("estado", EstadoComun.VIGENTE)
                .getResultList();
    }
    
    
         
    /**
     * Este método permite obtener las fuentes de recurso habilitadas, de una fuente de financiamiento.
     * @param idfuenteFinanciamiento
     * @return 
     */
    public List<Indicador> getFuenteRecursosHabilitados(Integer idfuenteFinanciamiento) {
        return entityManager.createQuery("SELECT f"
                + " FROM FuenteRecursos f"
                + " WHERE f.habilitado = :habilitado "
                + " AND f.fuenteFinanciamiento.id = :idfuenteFinanciamiento"
                + " ORDER BY f.orden,  f.nombre")
                .setParameter("habilitado", Boolean.TRUE)
                .setParameter("idfuenteFinanciamiento", idfuenteFinanciamiento)
                .getResultList();
    }
}
