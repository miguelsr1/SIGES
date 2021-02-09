/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.entities.data.impl.MetaIndicador;
import gob.mined.siap2.entities.data.impl.MetaIndicadorProducto;
import gob.mined.siap2.entities.data.impl.MetaIndicadorProyecto;
import gob.mined.siap2.entities.data.impl.POAConActividades;
import gob.mined.siap2.entities.data.impl.POAProyecto;
import gob.mined.siap2.entities.data.impl.PeriodoSeguimientoIndicadores;
import gob.mined.siap2.entities.data.impl.Programa;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import java.util.Date;
import java.util.List;
import javax.persistence.TemporalType;

/**
 * Esta clase incluye los métodos de acceso a datos para los VAlores de indicadores.
 * @author Sofis Solutions
 */
@JPADAO
public class ValorDeIndicadorDAO extends EclipselinkJpaDAOImp {


    /**
     * Este método devuelve la lista de indicadores en períodos de seguimiento.
     * @param fechaActual
     * @param idAnioFiscal
     * @param habilitadoMensual
     * @param posicionHabilitadaMensual
     * @param habilitadoTrimestral
     * @param posicionHabilitadaTrimestral
     * @param habilitadoCuatrimestral
     * @param posicionHabilitadaCuatrimestral
     * @param habilitadoSemestral
     * @param posicionHabilitadaSemestral
     * @param habilitadoAnual
     * @param aplicaProyectosDeInversion
     * @param aplicaProyectosAdministrarivos
     * @return 
     */
    public List<PeriodoSeguimientoIndicadores> getPeriodosHabilitados(Date fechaActual,
            Integer idAnioFiscal,
            boolean habilitadoMensual,
            int posicionHabilitadaMensual,
            boolean habilitadoTrimestral,
            int posicionHabilitadaTrimestral,
            boolean habilitadoCuatrimestral,
            int posicionHabilitadaCuatrimestral,
            boolean habilitadoSemestral,
            int posicionHabilitadaSemestral,
            boolean habilitadoAnual,
            boolean aplicaProyectosDeInversion,
            boolean aplicaProyectosAdministrarivos) {
        return entityManager.createQuery("SELECT p "
                + " FROM PeriodoSeguimientoIndicadores p "
                + " WHERE (p.desde <= :fechaActual AND  :fechaActual <= p.hasta ) "
                + " AND p.anioFiscal.id = :idAnioFiscal "
                + " AND ((FALSE = :habilitadoMensual ) OR (TRUE = :habilitadoMensual AND TRUE = p.habilitadoMensual AND p.posicionHabilitadaMensual = :posicionHabilitadaMensual ) )"
                + " AND ((FALSE = :habilitadoTrimestral ) OR (TRUE =:habilitadoTrimestral AND TRUE =p.habilitadoTrimestral AND p.posicionHabilitadaTrimestral = :posicionHabilitadaTrimestral ) )"
                + " AND ((FALSE = :habilitadoCuatrimestral ) OR (TRUE =:habilitadoCuatrimestral AND TRUE =p.habilitadoCuatrimestral AND p.posicionHabilitadaCuatrimestral = :posicionHabilitadaCuatrimestral )) "
                + " AND ((FALSE = :habilitadoSemestral ) OR (TRUE =:habilitadoSemestral AND TRUE =p.habilitadoSemestral AND p.posicionHabilitadaSemestral = :posicionHabilitadaSemestral )) "
                + " AND ((FALSE = :habilitadoAnual ) OR (TRUE =:habilitadoAnual AND TRUE =p.habilitadoAnual ) )"
                + " AND ((FALSE = :aplicaProyectosDeInversion ) OR (TRUE =:aplicaProyectosDeInversion AND TRUE =p.aplicaProyectosDeInversion ) ) "
                + " AND ((FALSE = :aplicaProyectosAdministrarivos ) OR (TRUE =:aplicaProyectosAdministrarivos AND TRUE =p.aplicaProyectosAdministrarivos ) )"
        )
                .setParameter("fechaActual", fechaActual, TemporalType.DATE)
                .setParameter("idAnioFiscal", idAnioFiscal)
                .setParameter("habilitadoMensual", habilitadoMensual)
                .setParameter("posicionHabilitadaMensual", posicionHabilitadaMensual)
                .setParameter("habilitadoTrimestral", habilitadoTrimestral)
                .setParameter("posicionHabilitadaTrimestral", posicionHabilitadaTrimestral)
                .setParameter("habilitadoCuatrimestral", habilitadoCuatrimestral)
                .setParameter("posicionHabilitadaCuatrimestral", posicionHabilitadaCuatrimestral)
                .setParameter("habilitadoSemestral", habilitadoSemestral)
                .setParameter("posicionHabilitadaSemestral", posicionHabilitadaSemestral)
                .setParameter("habilitadoAnual", habilitadoAnual)
                .setParameter("aplicaProyectosDeInversion", aplicaProyectosDeInversion)
                .setParameter("aplicaProyectosAdministrarivos", aplicaProyectosAdministrarivos)
                .getResultList();
    }

    public List<PeriodoSeguimientoIndicadores> getPeriodosTrimestraleHabilitadosPorAnioFiscal(Integer idAnioFiscal) {
        return entityManager.createQuery("SELECT p "
                + " FROM PeriodoSeguimientoIndicadores p "
                + " WHERE p.anioFiscal.id = :idAnioFiscal AND p.habilitadoTrimestral=true"
        )
                .setParameter("idAnioFiscal", idAnioFiscal)
                .getResultList();
    }
    
    /**
     * Este método devuelve la lista de metas de indicadores para un indicador de producto dado.
     * @param idProyectoEstProducto id del producto
     * @param idAnioFiscal id del año
     * @return  lista de metas para un producto.
     */
    public List<MetaIndicadorProducto> getMetaIndicadorProducto(Integer idProyectoEstProducto, Integer idAnioFiscal) {
        return entityManager.createQuery("SELECT meta"
                + " FROM MetaIndicadorProducto meta"
                + " WHERE meta.proyectoEstProducto.id =:idProyectoEstProducto "
                + " AND meta.anioFiscal.id =:idAnioFiscal")
                .setParameter("idProyectoEstProducto", idProyectoEstProducto)
                .setParameter("idAnioFiscal", idAnioFiscal)
                .getResultList();
    }

    /**
     * Este método devuelve una lista de metas de indicadores de un proyecto.
     * @param idIndicador id del indicador
     * @param idAnioFiscal id del año fiscal
     * @param idProyecto id del proyecto
     * @return lista de metas de indicador.
     */
    public List<MetaIndicadorProyecto> getMetaIndicadorProyecto(Integer idIndicador, Integer idAnioFiscal, Integer idProyecto) {
        return entityManager.createQuery("SELECT meta"
                + " FROM MetaIndicadorProyecto meta"
                + " WHERE meta.indicador.id =:idIndicador "
                + " AND meta.anioFiscal.id =:idAnioFiscal"
                + " AND meta.proyecto.id =:idProyecto")
                .setParameter("idIndicador", idIndicador)
                .setParameter("idAnioFiscal", idAnioFiscal)
                .setParameter("idProyecto", idProyecto)
                .getResultList();
    }

    /**
     * Este método devuelve los POA de proyecto para un año fiscal dado y una unidad técnica dada
     * @param unidadesTecnicasDelUsuario lista de unidades técnicas
     * @param idAnioFiscal id del año fiscal.
     * @return 
     */
    public List<POAProyecto> getPOASTrabajoProyectoParaFinaliarActividades(List<Integer> unidadesTecnicasDelUsuario, Integer idAnioFiscal) {
        return entityManager.createQuery("SELECT poa FROM POAProyecto poa "
                 + " WHERE poa.anioFiscal.id = :idAnioFiscal "
                + " AND poa.unidadTecnica.id IN :unidadesTecnicasDelUsuario"
                + " AND poa.lineaTrabajo IS NULL")
                .setParameter("idAnioFiscal", idAnioFiscal)
                .setParameter("unidadesTecnicasDelUsuario", unidadesTecnicasDelUsuario)
                .getResultList();
    }

    /**
     * Este método devuelve los POA de actividad (AC y ANP) para unidades técnicas y año fiscal.
     * @param unidadesTecnicasDelUsuario conjunto de unidades técnicas
     * @param idAnioFiscal id del año fiscal.
     * @return lista de POA de AC y ANP.
     */
    public List<POAConActividades> getPOASTrabajoPAraFinalizarActividades(List<Integer> unidadesTecnicasDelUsuario, Integer idAnioFiscal) {
        return entityManager.createQuery("SELECT poa FROM POAConActividades poa "
                + " WHERE poa.anioFiscal.id = :idAnioFiscal "
                + " AND poa.unidadTecnica.id IN :unidadesTecnicasDelUsuario"
                + " AND poa.lineaTrabajo IS NULL")
                .setParameter("idAnioFiscal", idAnioFiscal)
                .setParameter("unidadesTecnicasDelUsuario", unidadesTecnicasDelUsuario)
                .getResultList();
    }

    /**
     * Este método devuelve las metas de indicadores en un año dado.
     * @param idanioFiscal id del año fiscal.
     * @return lista de metas de indicadores.
     */
    public List<MetaIndicador> getIndicadoresEnAnios(Integer idanioFiscal) {

        return entityManager.createQuery("SELECT meta"
                + " FROM MetaIndicador meta"
                + " WHERE meta.anioFiscal.id =:idanioFiscal ")
                .setParameter("idanioFiscal", idanioFiscal)
                .getResultList();
    }

    /**
     * Este método devuelve la lista de metas de indicadores de producto para un proyecto en un año dado
     * @param idAnioFiscal id del año fiscal.
     * @param idProyecto id del proyecto
     * @return lista de metas de indicadores
     */
    public List<MetaIndicadorProducto> getMetaIndicadoresProductoEnAnios(Integer idAnioFiscal, Integer idProyecto) {
        return entityManager.createQuery("SELECT meta"
                + " FROM MetaIndicadorProducto meta"
                + " WHERE meta.anioFiscal.id =:idanioFiscal and meta.proyectoEstProducto.proyectoEstructura.proyecto.id =:idProyecto")
                .setParameter("idanioFiscal", idAnioFiscal)
                .setParameter("idProyecto", idProyecto)
                .getResultList();

    }

    /**
     * Este método devuelve una lista de meta de indicadores para un año y unidad técnica
     * @param idAnioFiscal id del año fiscal
     * @param idUT id de la unidad técnica.
     * @return 
     */
    public List<MetaIndicadorProducto> getMetaIndicadoresProductoEnAniosPorUT(Integer idAnioFiscal, Integer idUT) {

        return entityManager.createQuery("SELECT meta"
                + " FROM MetaIndicadorProducto meta"
                + " WHERE meta.anioFiscal.id =:idanioFiscal and meta.proyectoEstProducto.unidadTecnica.id =:idUT")
                .setParameter("idanioFiscal", idAnioFiscal)
                .setParameter("idUT", idUT)
                .getResultList();

    }

    /**
     * Este método devuelve la lista de programas que tienen asociado un indicador dado.
     * @param idIndicador
     * @return 
     */
    public List<Programa> getProgramasConIndicador(Integer idIndicador) {
        return entityManager.createQuery("SELECT p "
                + " FROM Programa p "
                + " WHERE EXISTS (SELECT 1 FROM  ProgramaIndicador pi  WHERE pi.indicador.id =:idIndicador AND pi.programa.id = p.id )")
                .setParameter("idIndicador", idIndicador)
                .getResultList();
    }

}
