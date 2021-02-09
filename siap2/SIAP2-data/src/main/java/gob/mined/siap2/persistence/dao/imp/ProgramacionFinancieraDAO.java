/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.entities.data.impl.ProgramacionFinancieraAccionCentral;
import gob.mined.siap2.entities.data.impl.ProgramacionFinancieraAsignacionNoProgramable;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import java.util.List;

/**
 * Esta clase implementa los métodos de acceso a datos para Programación financiera
 * @author Sofis Solutions
 */
@JPADAO
public class ProgramacionFinancieraDAO extends EclipselinkJpaDAOImp<ProgramacionFinancieraAccionCentral, Integer> {

   

    /**
     * Este método devuelve la programación financiera de acción central para un rango de años.
     * @param minAnio año inferior
     * @param maxAnio año superior
     * @return lista de programaciones financieras.
     */
    public List<ProgramacionFinancieraAccionCentral> getProgFinancieraAccionCentral(Integer minAnio, Integer maxAnio) {
        return entityManager.createQuery("SELECT p "
                + " FROM ProgramacionFinancieraAccionCentral p"
                + " WHERE p.anio>= :minAnio"
                + " AND p.anio<= :maxAnio"
                + " ORDER BY p.anio, p.unidadTecnica.nombre")
                .setParameter("minAnio", minAnio)
                .setParameter("maxAnio", maxAnio)
                .getResultList();
    }

    /**
     * Este método devuelve la programación financiera de asignación no programable para un rango de años.
     * @param minAnio año inferior
     * @param maxAnio año superior
     * @return lista de programaciones financieras.
     */
    public List<ProgramacionFinancieraAsignacionNoProgramable> getProgFinancieraAsigNP(Integer minAnio, Integer maxAnio) {
        return entityManager.createQuery("SELECT p "
                + " FROM ProgramacionFinancieraAsignacionNoProgramable p"
                + " WHERE p.anio>= :minAnio"
                + " AND p.anio<= :maxAnio"
                + " ORDER BY p.anio, p.asignacionNoProgramable.nombre")
                .setParameter("minAnio", minAnio)
                .setParameter("maxAnio", maxAnio)
                .getResultList();
    }

}
