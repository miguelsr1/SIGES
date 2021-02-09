/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.entities.data.impl.ActividadPOProyecto;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.ClasificadorFuncional;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.data.impl.Proyecto;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import gob.mined.siges.entities.data.impl.SgTipoOrganismoAdministrativo;
import java.util.List;

/**
 * Esta clase implementa los métodos genéricos par ala gestión de catálogos
 *
 * @author Sofis Solutions
 */
@JPADAO
public class GeneralCodigueralDAO extends EclipselinkJpaDAOImp {

    /**
     * Este método devuelve todos los objetos de tipo c y que están habilitados,
     * ordenados por nombre.
     *
     * @param c es la clase
     * @return
     */
    public List getGeneralCodiguera(Class c) {
        return entityManager.createQuery("SELECT c"
            + " FROM " + c.getSimpleName() + " c"
            + " WHERE c.habilitado=TRUE "
            + " ORDER BY c.orden, c.nombre")
            .getResultList();
    }

    /**
     * Este método devuelve todas las unidades técnicas que son direcciones.
     *
     * @return
     */
    public List<UnidadTecnica> getUTDirecciones() {
        return entityManager.createQuery("SELECT u"
            + " FROM UnidadTecnica u"
            + " WHERE u.direccion=TRUE "
            + " ORDER BY u.nombre")
            .getResultList();
    }

    /**
     * Este método devuelve todos los clasificadores funcionales del gasto
     * habilitados.
     *
     * @return
     */
    public List<ClasificadorFuncional> getClasificadoresAsignables() {
        return entityManager.createQuery("SELECT c"
            + " FROM ClasificadorFuncional c"
            + " WHERE c.habilitado=TRUE "
            + " AND c.asignable=TRUE "
            + " ORDER BY c.codigo")
            .getResultList();
    }

    /**
     * Este método permite obtener todos los años fiscales habilitados para
     * planificación.
     *
     * @return
     */
    public List<AnioFiscal> getAniosFiscalesPlanificacion() {
        return entityManager.createQuery("SELECT a"
            + " FROM AnioFiscal a"
            + " WHERE a.habilitadoPlanificacion=TRUE "
            + " ORDER BY a.anio")
            .getResultList();
    }
    /**
     * Este método permite obtener todos los años fiscales habilitados para
     * planificación.
     *
     * @return
     */
    public List<AnioFiscal> getAniosFiscalesFormulacion() {
        return entityManager.createQuery("SELECT a"
            + " FROM AnioFiscal a"
            + " WHERE a.formulacionCe=TRUE "
            + " ORDER BY a.anio")
            .getResultList();
    }
    /**
     * Este método permite obtener el año fiscales habilitados para
     * formulacion.
     *
     * @return
     */
    public AnioFiscal getUltimoAnioFiscalFormulacion() {
        List<AnioFiscal> lista = entityManager.createQuery("SELECT a"
            + " FROM AnioFiscal a"
            + " WHERE a.formulacionCe=TRUE "
            + " ORDER BY a.anio desc").setMaxResults(1)
            .getResultList();
        if(lista != null && !lista.isEmpty() && lista.size() > 0) {
            return lista.get(0);
        }
        return null;
    }
    /**
     * Este método permite obtener todos los años fiscales habilitados para
     * planificación.
     *
     * @return
     */
    public List<SgTipoOrganismoAdministrativo> getTiposOrganismosAdministrativos() {
        return entityManager.createQuery("SELECT to"
            + " FROM SgTipoOrganismoAdministrativo to"
            + " WHERE to.toaHabilitado=TRUE "
            + " ORDER BY to.toaCodigo asc")
            .getResultList();
    }

    /**
     * Este método permite obtener todos los PO de un proyecto.
     *
     * @param query
     * @return
     */
    public List<ActividadPOProyecto> getActividadPOProyecto(String query) {
        return entityManager.createQuery("SELECT c"
            + " FROM ActividadPOProyecto c"
            + " WHERE c.habilitado=TRUE "
            + " AND UPPER(c.nombre) LIKE UPPER(:query)"
            + " ORDER BY c.orden, c.nombre")
            .setParameter("query", "%" + query + "%")
            .getResultList();
    }

    /**
     * Este método permite obtener todos los proyectos que tienen en el nombre
     * el query indicado. El resultado está ordenado por nombre.
     *
     * @param query
     * @return
     */
    public List<Proyecto> getProyectos(String query) {
        return entityManager.createQuery("SELECT p"
            + " FROM Proyecto p"
            + " WHERE p.nombre LIKE :query"
            + " ORDER BY p.nombre")
            .setParameter("query", "%" + query + "%")
            .getResultList();
    }

    /**
     * Este método permite obtener todos los insumos no UACI de un POA
     *
     * @param id
     * @return
     */
    public List<POInsumos> getInsumosNOUACIporCodigo(Integer id) {
        return entityManager.createQuery("SELECT pi "
             + " FROM POInsumos pi  "
             + " WHERE CAST ( pi.id AS VARCHAR2(255) ) LIKE :idStr "
             + " AND pi.noUACI = TRUE "
             + " AND pi.poa.lineaTrabajo is NULL"
             + " ORDER BY pi.id")
             .setParameter("idStr", "%" + id+ "%")
             .getResultList();

    }
    
    /**
     * Este método permite obtener todos los insumos no UACI de un POA
     * que tengan al menos una fuente certificada
     *
     * @param id
     * @return
     */
    public List<POInsumos> getInsumosNOUACICertificadosPorCodigo(Integer id) {
        return entityManager.createQuery("SELECT pi "
             + " FROM POInsumos pi  "
             + " WHERE CAST ( pi.id AS VARCHAR2(255) ) LIKE :idStr "
             + " AND pi.noUACI = TRUE "
             + " AND pi.poa.lineaTrabajo is NULL "                
             + " AND EXISTS (SELECT 1 FROM pi.montosFuentes montoFuente WHERE montoFuente.certificadoDisponibilidadPresupuestariaAprobada = TRUE )"
             + " ORDER BY pi.id")
             .setParameter("idStr", "%" + id+ "%")
             .getResultList();

    }


}
