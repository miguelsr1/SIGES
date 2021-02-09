/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.entities.data.impl.MetodoAdquisicion;
import gob.mined.siap2.entities.data.impl.PAC;
import gob.mined.siap2.entities.enums.EstadoPAC;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Query;

/**
 * Esta clase incluye los métodos de acceso a datos para métodos de adquisición.
 * @author Sofis Solutions
 */
@JPADAO
public class MetodoAdquisicionDAO extends EclipselinkJpaDAOImp<MetodoAdquisicion, Integer> {

    /**
     * Este método devuelve los nombres de los PAC según un método de adquisición
     * @param metId id del método del adquisición
     * @return
     * @throws DAOGeneralException 
     */
    public List<String> obtenerPacsNombresAsociadosPorMetId(Integer metId) throws DAOGeneralException {

        Query q = entityManager.createQuery("Select DISTINCT p.pac.nombre from PACGrupo p where p.metodoAdquisicion.id =:metodoId");
        q.setParameter("metodoId", metId);
        return (List<String>)q.getResultList();

    }
    
    /**
     * Este método permite obtener los nombres de los PAC que tienen un grupo sin cerrar.
     * @param metId
     * @return
     * @throws DAOGeneralException 
     */
    public List<String> obtenerPacsNombresNoFinalizadoAsociadosPorMetId(Integer metId) throws DAOGeneralException {
       
            Query q = entityManager.createQuery("Select DISTINCT p.pac.nombre from PACGrupo p where p.pac.estado !=:estado and p.metodoAdquisicion.id =:metodoId");
            q.setParameter("metodoId", metId);
            q.setParameter("estado", EstadoPAC.CERRADO);
            List<String> resultados = (List<String>)q.getResultList();
           
            return (List<String>)q.getResultList();
    }
    
    /**
     * Este método permite obtener todos los PAC que tienen un método de adquisición en un determinado año.
     * @param anio
     * @param metId
     * @return
     * @throws DAOGeneralException 
     */
    public List<PAC> obtenerPacsAnioRangoAsociadosPorMetIdNoCerrado(Integer anio,Integer metId) throws DAOGeneralException {
       
            Query q = entityManager.createQuery("Select DISTINCT p.pac from PACGrupo p where p.pac.anio=:anio and p.pac.estado !=:estado and p.metodoAdquisicion.id =:metodoId");
            q.setParameter("metodoId", metId);
            q.setParameter("estado", EstadoPAC.CERRADO);
            q.setParameter("anio", anio);
            
            return (List<PAC>)q.getResultList();
    }
    
    
    /**
     * Este método permite obtener los métodos de adquisición en un determinado rango en un determinado año.
     * @param monto monto incluido en el rango del método de adquisición
     * @param anio en le que buscar
     * @return lista de métodos de adquisición
     */    
    public List<MetodoAdquisicion> getMetodosQueCumplanRango(BigDecimal monto, Integer anio){
        return entityManager.createQuery("SELECT m "
                + " FROM MetodoAdquisicion m, MetodoAdquisicionRango rangos"
                + " WHERE rangos.metodo.id = m.id "
                + " AND rangos.anio = "
                    + " ( SELECT MAX(iter.anio) FROM MetodoAdquisicionRango iter"
                    + " WHERE iter.metodo.id = m.id  "
                    + " AND iter.anio <= :anio ) "
                + " AND :monto >= rangos.montoMin "
                + " AND :monto <= rangos.montoMax "
                + " AND m.habilitado = true "
                + " ORDER BY m.nombre")
                .setParameter("monto", monto)
                .setParameter("anio", anio)
                .getResultList();
    }
    
    
    
    
}
