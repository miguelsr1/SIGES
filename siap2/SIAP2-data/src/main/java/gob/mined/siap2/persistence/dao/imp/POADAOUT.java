/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.entities.data.impl.POA;
import gob.mined.siap2.entities.data.impl.POARiesgoPOA;
import gob.mined.siap2.entities.tipos.FiltroRiesgo;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import gob.mined.siap2.ws.to.RiesgoTOPOA;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import org.apache.commons.lang3.StringUtils;

/**
 * Esta clase incluye los métodos de acceso a datos de POA
 * @author Sofis Solutions
 */
@JPADAO
public class POADAOUT extends EclipselinkJpaDAOImp implements Serializable {

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
    public POA find(Class c, Integer primaryKey) {
        return (POA) entityManager.find(c, primaryKey);
    }

    /**
     * Este método devuelve los proyectos que tienen valores de indicadores
     *
     * @param idAnioFiscal id del año fiscal.
     * @return lista de proyectos
     */
    public List<POA> getPOAParaValoresIndicadores(Integer idAnioFiscal) {
        return entityManager.createQuery("SELECT poa FROM POA poa WHERE poa.anio.id = :idAnioFiscal order by  poa.nombre asc")
            .setParameter("idAnioFiscal", idAnioFiscal)
            .getResultList();
    }
    
     /**
     * Este método devuelve los proyectos que tienen valores de indicadores
     *
     * @param idAnioFiscal id del año fiscal.
     * @return lista de proyectos
     */
    public List<POA> getPOAParaValoresIndicadoresAnioAndPrograma(Integer idAnioFiscal, Integer programaId) {
        return entityManager.createQuery("SELECT poa FROM POA poa WHERE poa.anio.id = :idAnioFiscal and poa.programaInstitucional.id = :idPrograma order by poa.nombre asc")
            .setParameter("idAnioFiscal", idAnioFiscal)
            .setParameter("idPrograma", programaId)
            .getResultList();
    }
    
    /**
     * Este método permite obtener los riesgos para un filtro dado.
     * @param filtro
     * @return 
     */
    public Collection<RiesgoTOPOA> obtenerRiesgosPorFiltro(FiltroRiesgo filtro) {
        Collection<RiesgoTOPOA> respuesta = new ArrayList();
        String consulta = "select poa  FROM POA poa  ";
        String where = "";
        boolean conAnd = false;
        if (filtro != null) {
            if (filtro.getAnioFiscal() != null) {
                if (conAnd) {
                    where = where + " and ";
                }
                where = where + " ( poa.anio.id = :idAnioFiscal )";
                conAnd = true;
            }
            
            if (filtro.getProgramaInstitucional() != null) {
                if (conAnd) {
                    where = where + " and ";
                }
                where = where + " ( poa.programaInstitucional.id = :idProgramaInstitucional)";
                conAnd = true;
            }
        }
        if (!StringUtils.isEmpty(where)) {
            consulta = consulta + " where " + where;
        }
         
        Query query = entityManager.createQuery(consulta);
        if (filtro != null) {
            if (filtro.getAnioFiscal() != null) {
                query.setParameter("idAnioFiscal", filtro.getAnioFiscal().getId());
            }
            if (filtro.getProgramaInstitucional() != null) {
                query.setParameter("idProgramaInstitucional", filtro.getProgramaInstitucional().getId());
            }
        }
        List<POA> resultado = query.getResultList();
        for (POA poa : resultado) {
            for (POARiesgoPOA rie : poa.getRiesgos()) {

                RiesgoTOPOA riesgo = new RiesgoTOPOA();
                riesgo.setNombre(poa.getNombre());
                riesgo.setAnioFiscal(poa.getAnio());
                riesgo.setProgramaInstitucional(poa.getProgramaInstitucional());
                riesgo.setRiesgo(rie);
                riesgo.setUnidadTecnica(poa.getUnidadTecnica());
                respuesta.add(riesgo);
            }
        }
        return respuesta;
    }
}