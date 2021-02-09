/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.entities.data.impl.Impuesto;
import gob.mined.siap2.entities.data.impl.QuedanEmitido;
import gob.mined.siap2.entities.enums.EstadoActa;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;

/**
 * Esta clase implementa los métodos de acceso a datos vinculados a Impuestos.
 *
 * @author Sofis Solutions
 */
@JPADAO
public class ImpuestoDAO extends EclipselinkJpaDAOImp<Impuesto, Integer> {

    /**
     * Este método permite obtener los quedan de un proyecto en un rango de
     * fechas y para un impuesto dado.
     *
     * @param idProyecto identificación del proyecto
     * @param fechaDesde rango de fechas
     * @param fechaHasta
     * @param idImpuesto identificación del impuesto
     * @return lista de quedan emitidos que satisfacen los criterios anteriores.
     */
    public List<QuedanEmitido> getQuedanEnProyecto(Integer idProyecto, Date fechaDesde, Date fechaHasta, Integer idImpuesto) {
        String sql = "SELECT acta.quedan "
                + " FROM ActaContrato acta "
                + " WHERE acta.estado = :estadoAprobada "
                + " AND acta.quedanEmitido = :quedanEmitido ";

        if (fechaDesde != null) {
            sql = sql + " AND acta.fechaGeneracion >= :fechaDesde ";
        }
        if (fechaHasta != null) {
            sql = sql + " AND acta.fechaGeneracion <= :fechaHasta ";
        }
        if (idImpuesto != null) {
            sql = sql + " AND EXISTS ( SELECT 1 FROM acta.quedan.valoresImpuesto valoresImpuesto WHERE valoresImpuesto.impuesto.id = :idImpuesto ) ";
        }
        if (idProyecto != null) {
            sql = sql + " AND EXISTS "
                    + " ( "
                    + " SELECT 1 "
                    + " FROM  acta.contratoOC.items item "
                    + " join item.relItemInsumos relInsumo "
                    + " WHERE relInsumo.insumo.poInsumo.poa.id IN "
                    + " ( "
                    + " SELECT poaProyecto.id  "
                    + " FROM POAProyecto poaProyecto"
                    + " WHERE poaProyecto.proyecto.id = :idProyecto "
                    + " )"
                    + " ) ";
        }
        sql = sql + " ORDER BY acta.fechaGeneracion";

        Query q = entityManager.createQuery(sql);
        q.setParameter("estadoAprobada", EstadoActa.APROBADA);
        if (idProyecto != null) {
            q.setParameter("idProyecto", idProyecto);
        }
        if (fechaDesde != null) {
            q.setParameter("fechaDesde", fechaDesde);
        }
        if (fechaHasta != null) {
            q.setParameter("fechaHasta", fechaHasta);
        }
        if (idImpuesto != null) {
            q.setParameter("idImpuesto", idImpuesto);
        }
        q.setParameter("quedanEmitido", Boolean.TRUE);

        return q.getResultList();
    }

    /**
     * Este método permite obtener todos los quedan emitidos en un rango de
     * fechas que tienen retenciones para un impuesto dado.
     *
     * @param fechaDesde
     * @param fechaHasta
     * @param idImpuesto
     * @return lista de quedan emitidos.
     */
    public List<QuedanEmitido> getQuedansPorImpuesto(Date fechaDesde, Date fechaHasta, Integer idImpuesto) {

        return entityManager.createQuery("SELECT acta.quedan "
                + " FROM ActaContrato acta "
                + " WHERE acta.estado = :estadoAprobada "
                + " AND acta.quedanEmitido = :quedanEmitido "
                + " AND acta.fechaGeneracion >= :fechaDesde "
                + " AND acta.fechaGeneracion <= :fechaHasta "
                + " AND EXISTS ( SELECT 1 FROM acta.quedan.valoresImpuesto valoresImpuesto WHERE valoresImpuesto.impuesto.id = :idImpuesto ) ")
                .setParameter("estadoAprobada", EstadoActa.APROBADA)
                .setParameter("fechaDesde", fechaDesde)
                .setParameter("fechaHasta", fechaHasta)
                .setParameter("idImpuesto", idImpuesto)
                .setParameter("quedanEmitido", Boolean.TRUE)
                .getResultList();
    }

    /**
     * Este método permite obtener todos los quedan emitidos para un proveedor
     * dado
     *
     * @param fechaDesde
     * @param fechaHasta
     * @param idProveedor
     * @return
     */
    public List<QuedanEmitido> getQuedansPorProveedor(Date fechaDesde, Date fechaHasta, Integer idProveedor) {

        return entityManager.createQuery("SELECT acta.quedan "
                + " FROM ActaContrato acta "
                + " WHERE acta.estado = :estadoAprobada "
                + " AND acta.quedanEmitido = :quedanEmitido "
                + " AND acta.contratoOC.procesoAdquisicionProveedor.proveedor.id = :idProveedor "
                + " AND acta.fechaGeneracion >= :fechaDesde "
                + " AND acta.fechaGeneracion <= :fechaHasta ")
                .setParameter("estadoAprobada", EstadoActa.APROBADA)
                .setParameter("fechaDesde", fechaDesde)
                .setParameter("fechaHasta", fechaHasta)
                .setParameter("idProveedor", idProveedor)
                .setParameter("quedanEmitido", Boolean.TRUE)
                .getResultList();
    }

    /**
     * Retorna los impuestos iniciales que aparecen en un nuevo contrato
     *
     * @return
     */
    public List<Impuesto> getImpuestosIniciales() {
        return entityManager.createQuery("SELECT c"
                + " FROM Impuesto c"
                + " WHERE c.habilitado= TRUE "
                + " AND c.porDefectoEnContrato = TRUE "
                + " ORDER BY c.orden, c.nombre")
                .getResultList();
    }
}
