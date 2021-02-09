/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.entities.data.impl.AccionCentral;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import java.math.BigDecimal;
import javax.persistence.Query;

/**
 * Esta clase implementa las operaciones de acceso a datos vinculadas a Acción Central
 * @author Sofis Solutions
 */
@JPADAO
public class AccionCentralDAO extends EclipselinkJpaDAOImp<AccionCentral, Integer> {

    public AccionCentral find(Integer primaryKey) {
        return entityManager.find(AccionCentral.class, primaryKey);
    }
 
    /**
     * Este método calcula los montos de una planificación dada para una unidad técnica
     * @param idPlanificacion de la planificación
     * @param anio año a calcular
     * @param idUnidadTecnica unidad técnica
     * @param idAccionCentralExcluir acción central a excluir
     * @return suma de los montos de la unidad técnica en el año indicado de esa planificación.
     */
    public BigDecimal sumarMontosUTPlanificacion(Integer idPlanificacion, Integer anio, Integer idUnidadTecnica, Integer idAccionCentralExcluir) {
        String sql = "SELECT SUM(monto.monto) "
                + " FROM AccionCentral a, a.montosUT monto"
                + " WHERE a.planificacionEstrategica.id =:idPlanificacion "
                + " AND monto.anioFiscal.anio=:anio"
                + " AND monto.unidadTecnica.id =:idUnidadTecnica";
        if (idAccionCentralExcluir != null) {
            sql = sql + " AND a.id != :idAccionCentralExcluir";
        }
        Query q = entityManager.createQuery(sql)
                .setParameter("idPlanificacion", idPlanificacion)
                .setParameter("anio", anio)
                .setParameter("idUnidadTecnica", idUnidadTecnica);

        if (idAccionCentralExcluir != null) {
            q.setParameter("idAccionCentralExcluir", idAccionCentralExcluir);
        }
        return (BigDecimal) q.getSingleResult();
    }

  

}
