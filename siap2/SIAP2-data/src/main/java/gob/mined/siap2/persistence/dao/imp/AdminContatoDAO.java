/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.entities.data.impl.AccionCentral;
import gob.mined.siap2.entities.data.impl.ActaContrato;
import gob.mined.siap2.entities.data.impl.ContratoOC;
import gob.mined.siap2.entities.data.impl.PagoInsumo;
import gob.mined.siap2.entities.enums.EstadoActa;
import gob.mined.siap2.entities.enums.EstadoContrato;
import gob.mined.siap2.entities.enums.TipoActaContrato;
import gob.mined.siap2.entities.enums.TipoContrato;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import gob.mined.siap2.utils.generalutils.TextUtils;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;

/**
 * Esta clase incluye los métodos de acceso a datos para la Administración de
 * Contrato
 *
 * @author Sofis Solutions
 */
@JPADAO
public class AdminContatoDAO extends EclipselinkJpaDAOImp<AccionCentral, Integer> {

    /**
     * Este método devuelve el contrato con la acción central con el id
     * indicado.
     *
     * @param primaryKey
     * @return
     */
    public AccionCentral find(Integer primaryKey) {
        return entityManager.find(AccionCentral.class, primaryKey);
    }

    /**
     * Devuelve el mayor número de acta para un contrato dado.
     *
     * @param idContrato
     * @return
     */
    public List<Integer> getMaxPagoId(Integer idContrato) {
        return entityManager.createQuery("SELECT MAX(pago.nroActa)"
                + " FROM ContratoOC contrato, contrato.pagos pago"
                + " WHERE contrato.id =:idContrato ")
                .setParameter("idContrato", idContrato)
                .getResultList();
    }

    /**
     * Devuelve el mayor número de solicitud de pago para un contrato dado.
     *
     * @param idContrato
     * @return
     */
    public List<Integer> getMaxNumSolicitud(Integer idContrato) {
        return entityManager.createQuery("SELECT MAX(pago.numeroSolicitudPago)"
                + " FROM ContratoOC contrato, contrato.pagos pago"
                + " WHERE contrato.id =:idContrato ")
                .setParameter("idContrato", idContrato)
                .getResultList();
    }

    /**
     * Devuelve la lista de insumos a los que se le realizó un pago.
     *
     * @param idPOInsumo
     * @return
     */
    public List<PagoInsumo> getPAgosFinalizadosPorInsumo(Integer idPOInsumo) {
        return entityManager.createQuery("SELECT pagoInsumos "
                + " FROM PagoInsumo pagoInsumos "
                + " WHERE pagoInsumos.relacionItemInsumo.insumo.poInsumo.id = :idPOInsumo "
                + " AND pagoInsumos.contrato.quedanEmitido = :quedanEmitido")
                .setParameter("idPOInsumo", idPOInsumo)
                .setParameter("quedanEmitido", Boolean.TRUE)
                .getResultList();
    }

    public List<ActaContrato> getActasParaComprobanteRetencionImpuestosPorProv(Integer idProveedor, Integer idImpuesto, Date fechaDesde, Date fechaHasta) {
        return entityManager.createQuery("SELECT acta "
                + " FROM ActaContrato acta, IN (acta.contratoOC.impuestos) impuesto"
                + " WHERE acta.contratoOC.procesoAdquisicionProveedor.proveedor.id = :idProveedor "
                + " AND impuesto.id = :idImpuesto "
                + " AND acta.fechaGeneracion BETWEEN :fechaDesde AND :fechaHasta")
                .setParameter("idProveedor", idProveedor)
                .setParameter("idImpuesto", idImpuesto)
                .setParameter("fechaDesde", fechaDesde)
                .setParameter("fechaHasta", fechaHasta)
                .getResultList();
    }

    /**
     * Devuelve el mayor número de modificativa para un contrato dado.
     *
     * @param idContrato
     * @return
     */
    public List<Integer> getMaxModificativaId(Integer idContrato) {
        return entityManager.createQuery("SELECT MAX(modif.numero)"
                + " FROM ContratoOC contrato, contrato.modificativas modif"
                + " WHERE contrato.id =:idContrato ")
                .setParameter("idContrato", idContrato)
                .getResultList();
    }

    /**
     * Devuelve la lista de insumos a los que se le realizó un pago.
     *
     * @param idPOInsumo
     * @return
     */
    public List<PagoInsumo> getPagosEnActasAprobadasPorInsumo(Integer idPOInsumo) {
        return entityManager.createQuery("SELECT pagoInsumos "
                + " FROM PagoInsumo pagoInsumos "
                + " WHERE pagoInsumos.relacionItemInsumo.insumo.poInsumo.id = :idPOInsumo "
                + " AND pagoInsumos.contrato.estado = :estadoActa"
                + " AND pagoInsumos.contrato.tipo = :tipoActa")
                .setParameter("idPOInsumo", idPOInsumo)
                .setParameter("estadoActa", EstadoActa.APROBADA)
                .setParameter("tipoActa", TipoActaContrato.RECEPCION)
                .getResultList();
    }

    public List<ContratoOC> getConsultaContratosOC(Integer nroContratoOC, TipoContrato tipoContratoOC, String nombreProceso, Date fechaDesde, Date fechaHasta, String proveedor, EstadoContrato estadoContratoOC, Integer nroInsumo, EstadoContrato estadoContrato, String usuarioCodigo, Integer firstResult, Integer maxResults, String[] orderBy, boolean[] ascending) {
        Query query = createConsultaContratosOCQuery("SELECT contrato ", nroContratoOC, tipoContratoOC, nombreProceso, fechaDesde, fechaHasta, proveedor, estadoContratoOC, nroInsumo, estadoContrato, usuarioCodigo, orderBy, ascending);

        return query.setFirstResult(firstResult)
                .setMaxResults(maxResults)
                .getResultList();
    }

    public long countConsultaContratosOC(Integer nroContratoOC, TipoContrato tipoContratoOC, String nombreProceso, Date fechaDesde, Date fechaHasta, String proveedor, EstadoContrato estadoContratoOC, Integer nroInsumo, EstadoContrato estadoContrato, String usuarioCodigo) {
        Query query = createConsultaContratosOCQuery("SELECT count(contrato) ", nroContratoOC, tipoContratoOC, nombreProceso, fechaDesde, fechaHasta, proveedor, estadoContratoOC, nroInsumo, estadoContrato, usuarioCodigo, null, null);

        return (long) query.getSingleResult();
    }

    private Query createConsultaContratosOCQuery(String select, Integer nroContratoOC, TipoContrato tipoContratoOC, String nombreProceso, Date fechaDesde, Date fechaHasta, String proveedor, EstadoContrato estadoContratoOC, Integer nroInsumo, EstadoContrato estadoContrato, String usuarioCodigo, String[] orderBy, boolean[] ascending) {
        String query = select;// ? "SELECT count(poInsumo) " : "SELECT poInsumo ";

        query = query + " FROM ContratoOC contrato "
                + " WHERE contrato.estado != :estadoContrato ";

        if (usuarioCodigo != null) {
            query = query + " AND contrato.administrador.usuCod = :usuarioCodigo ";
        }

        if (nroContratoOC != null) {
            query = query + " AND contrato.nroContrato = :nroContratoOC ";
        }

        if (tipoContratoOC != null) {
            query = query + " AND contrato.tipo = :tipoContratoOC ";
        }

        if (!TextUtils.isEmpty(nombreProceso)) {
            query = query + " AND contrato.procesoAdquisicion.nombre LIKE :nombreProceso ";
        }

        if (fechaDesde != null) {
            query = query + " AND contrato.fechaInicio = :fechaDesde ";
        }

        if (fechaHasta != null) {
            query = query + " AND contrato.fechaFin = :fechaHasta ";
        }

        if (!TextUtils.isEmpty(proveedor)) {
            query = query + " AND contrato.procesoAdquisicionProveedor.proveedor.nombreComercial LIKE :proveedor ";
        }

        if (estadoContratoOC != null) {
            query = query + " AND contrato.estado = :estadoContratoOC ";
        }

        if (nroInsumo != null) {
            query = query + " AND EXISTS (SELECT 1 FROM RelacionProAdqItemInsumo relItemInsumo where relItemInsumo.insumo.poInsumo.id = :nroInsumo AND relItemInsumo.item.contrato.id = contrato.id)";
        }

        Query jpaQuery = entityManager.createQuery(query);

        jpaQuery = jpaQuery.setParameter("estadoContrato", estadoContrato);

        if (usuarioCodigo != null) {
            jpaQuery = jpaQuery.setParameter("usuarioCodigo", usuarioCodigo);
        }

        if (nroContratoOC != null) {
            jpaQuery = jpaQuery.setParameter("nroContratoOC", nroContratoOC);
        }
        if (tipoContratoOC != null) {
            jpaQuery = jpaQuery.setParameter("tipoContratoOC", tipoContratoOC);
        }
        if (!TextUtils.isEmpty(nombreProceso)) {
            jpaQuery = jpaQuery.setParameter("nombreProceso", "%" + nombreProceso + "%");
        }
        if (fechaDesde != null) {
            jpaQuery = jpaQuery.setParameter("fechaDesde", fechaDesde);
        }
        if (fechaHasta != null) {
            jpaQuery = jpaQuery.setParameter("fechaHasta", fechaHasta);
        }
        if (!TextUtils.isEmpty(proveedor)) {
            jpaQuery = jpaQuery.setParameter("proveedor", "%" + proveedor + "%");
        }
        if (estadoContratoOC != null) {
            jpaQuery = jpaQuery.setParameter("estadoContratoOC", estadoContratoOC);
        }
        if (nroInsumo != null) {
            jpaQuery = jpaQuery.setParameter("nroInsumo", nroInsumo);
        }

        return jpaQuery;
    }
}
