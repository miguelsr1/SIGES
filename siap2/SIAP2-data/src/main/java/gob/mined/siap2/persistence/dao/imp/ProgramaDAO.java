/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.entities.data.impl.Programa;
import gob.mined.siap2.entities.data.impl.ProgramaInstitucional;
import gob.mined.siap2.entities.data.impl.ProgramaPresupuestario;
import gob.mined.siap2.entities.data.impl.Proyecto;
import gob.mined.siap2.entities.enums.EstadoComun;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Query;
import org.apache.commons.lang3.StringUtils;

/**
 * Esta clase incluye los métodos de acceso a datos para programas.
 *
 * @author Sofis Solutions
 */
@JPADAO
public class ProgramaDAO extends EclipselinkJpaDAOImp {

    /**
     * Este método devuelve todos los programas institucionales activos
     * (vigentes)
     *
     * @return lista de programas institucionales.
     */
    public List<ProgramaInstitucional> getProgramasInstitucioanalesActivos() {
        return entityManager.createQuery("SELECT p "
                + " FROM ProgramaInstitucional p"
                + " WHERE p.programaInstitucional IS NULL "
                + " AND p.estado=:estado "
                + " ORDER BY p.nombre ")
                .setParameter("estado", EstadoComun.VIGENTE)
                .getResultList();
    }

    /**
     * Este método devuelve todos los programas institucionales activos
     * (vigentes)
     *
     * @return lista de programas institucionales.
     */
    public List<ProgramaInstitucional> getProgramasInstitucioanalesActivosByQuery(String query) {
        if(query== null) {
            query = "";
        }
        return entityManager.createQuery("SELECT p "
                + " FROM ProgramaInstitucional p"
                + " WHERE p.programaInstitucional IS NULL "
                + " AND p.estado=:estado and p.nombreBusqueda like (:query)"
                + " ORDER BY p.nombre ")
                .setParameter("estado", EstadoComun.VIGENTE)
                 .setParameter("query", "%"+query.trim().toLowerCase()+"%")
                .getResultList();
    }
    
    /**
     * Este método devuelve la lista de los proyectos asociados a un programa
     * presupuestario
     *
     * @param idProgramaPresupuestario id del programa presupuestario.
     * @return lista de proyectos
     */
    public List<Proyecto> getProyectosEnPrograma(Integer idProgramaPresupuestario) {
        return entityManager.createQuery("SELECT p "
                + " FROM Proyecto p"
                + " WHERE p.programaPresupuestario.id = :idProgramaPresupuestario "
                + " ORDER BY p.nombre ")
                .setParameter("idProgramaPresupuestario", idProgramaPresupuestario)
                .getResultList();
    }

    /**
     * Este método devuelve una lista con todos los programas presupuestarios
     * vigentes.
     *
     * @return
     */
    public List<ProgramaPresupuestario> getProgramasPresupuestarios() {
        return entityManager.createQuery("SELECT p "
                + " FROM ProgramaPresupuestario p"
                + " WHERE p.programaPresupuestario IS NULL "
                + " AND p.estado=:estado "
                + " ORDER BY p.nombre ")
                .setParameter("estado", EstadoComun.VIGENTE)
                .getResultList();
    }

    /**
     * Este método devuelve una lista de todos los subprogramas presupuestarios
     * de un programa dado.
     *
     * @param idPadre id del programa
     * @return lista de los subprogramas del programa dado.
     */
    public List<ProgramaPresupuestario> getSubProgramasPresupuestarios(Integer idPadre) {
        String query = "SELECT p "
                + " FROM ProgramaPresupuestario p"
                + " WHERE p.programaPresupuestario IS NOT NULL ";

        if (idPadre != null) {
            query = query + " AND p.programaPresupuestario.id=:idPadre ";
        }
        query = query + " AND p.estado=:estado "
                + " ORDER BY p.nombre ";

        Query q = entityManager.createQuery(query);
        q.setParameter("estado", EstadoComun.VIGENTE);
        if (idPadre != null) {
            q.setParameter("idPadre", idPadre);
        }
        return q.getResultList();
    }

    /**
     * Este método devuelve la lista de todos los programas presupuestarios de
     * una planificación estratégica dada.
     *
     * @param idPlanificacionEstrategica id de la planificación estratégica.
     * @return lista de programas presupuestarios.
     */
    public List<ProgramaPresupuestario> getProgramasPresupuestariosParaCargarTechos(Integer idPlanificacionEstrategica) {
        String query = "SELECT p FROM ProgramaPresupuestario p WHERE p.programaPresupuestario IS NULL";
        if (idPlanificacionEstrategica != null) {
            query = query + " AND p.planificacionEstrategica.id =:plaId";
        }
        query = query + " ORDER BY p.nombre ";
        Query q = entityManager.createQuery(query);
        if (idPlanificacionEstrategica != null) {
            q.setParameter("plaId", idPlanificacionEstrategica);
        }
        return q.getResultList();
    }

    /**
     * Este método devuelve la lista de programas presupuestarios asociados a un
     * programa institucional dado.
     *
     * @param idProgramaInstitucional id del programa institucional
     * @return lista de programas presupuestarios.
     */
    public List<ProgramaPresupuestario> getProgramasPresupuestariosConInstitucional(Integer idProgramaInstitucional) {
        return entityManager.createQuery("SELECT p "
                + " FROM ProgramaPresupuestario p, p.programasInstitucionales i"
                + " WHERE i.id =:idProgramaInstitucional "
                + " ORDER BY p.nombre ")
                .setParameter("idProgramaInstitucional", idProgramaInstitucional)
                .getResultList();
    }

    /**
     * Este método permite obtener todos los PO de un proyecto.
     *
     * @param query
     * @return
     */
    public List<Programa> getProgramaByNombre(String query) {
        List<Programa> listado = new LinkedList<>();
        if(query != null && StringUtils.isNotBlank(query)) {
            listado = entityManager.createQuery("SELECT p"
            + " FROM Programa p"
            + " WHERE p.nombreBusqueda LIKE :query"
            + " ORDER BY p.nombre ASC")
            .setParameter("query", "%" + query.toLowerCase() + "%")
            .getResultList();
        }
        return listado;
    }
}
