/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.delegates.impl;

import gob.mined.siap2.business.ContratoBean;
import gob.mined.siap2.entities.data.impl.ActaContrato;
import gob.mined.siap2.entities.data.impl.ComprobanteDeRecepcionDeExpedienteDePago;
import gob.mined.siap2.entities.data.impl.CompromisoPresupuestario;
import gob.mined.siap2.entities.data.impl.ContratoOC;
import gob.mined.siap2.entities.data.impl.Factura;
import gob.mined.siap2.entities.data.impl.FacturaActaContratoOC;
import gob.mined.siap2.entities.data.impl.ModificativaContrato;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.data.impl.QuedanEmitido;
import gob.mined.siap2.entities.data.impl.RelacionProAdqItemInsumo;
import gob.mined.siap2.entities.enums.EstadoContrato;
import gob.mined.siap2.entities.enums.TipoContrato;
import gob.mined.siap2.exceptions.GeneralException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Esta clase implementa un delegate
 *
 * @author Sofis Solutions
 */
@Named
public class ContratoDelegate implements Serializable {

    @Inject
    private ContratoBean bean;

    /**
     * Este método obtiene un contrato por id
     *
     * @param id
     * @return
     */
    public ContratoOC cargarContrato(Integer id) {
        return bean.cargarContrato(id);
    }

    /**
     * Este método guarda un contrato.
     *
     * @param contrato
     * @return
     */
    public ContratoOC guardarContraro(ContratoOC contrato) {
        return bean.guardarContraro(contrato);
    }

    /**
     * Este método guarda un acta de un contrato.
     *
     * @param pago
     * @param idContrato
     * @return
     */
    public ActaContrato guardarPago(ActaContrato pago, Integer idContrato) {
        return bean.guardarPago(pago, idContrato);
    }

    /**
     * Este método elimina un pago de un contrato.
     *
     * @param idPago
     */
    public void eliminarPago(Integer idPago) {
        bean.eliminarPago(idPago);
    }

    /**
     * Este método permite cerrar un contrato
     *
     * @param id
     */
    public void cerrarContrato(Integer id) {
        bean.cerrarContrato(id);
    }

    /**
     * Este método obtiene un acta de contrato a partir de su id.
     *
     * @param id
     * @return
     */
    public ActaContrato cargarPago(Integer id) {
        return bean.cargarPago(id);
    }

    public ContratoOC saveArchivoOrdenInicio(ContratoOC contrato) {
        return bean.saveArchivoOrdenInicio(contrato);
    }

    /**
     * Este método valida los impuestos de un contrato.
     *
     * @param contrato
     * @return
     */
    public ContratoOC validarImpuestosContrato(ContratoOC contrato) {
        return bean.validarImpuestosContrato(contrato);
    }

    /**
     * Este método permite crear un número de acta para un acta.
     *
     * @param idActaPago
     */
    public void crearNumeroActaPago(Integer idActaPago) {
        bean.crearNumeroActaPago(idActaPago);
    }

    public void notificarProcesoAdquisicionAsignadoAOrdenDeInicio(ContratoOC contrato) {
        bean.notificarProcesoAdquisicionAsignadoAOrdenDeInicio(contrato);
    }

    /**
     * Este método permite guardar una factura asociada a un pago.
     *
     * @param idActaPago
     * @param factura
     * @return
     */
    public Factura guardarFactura(Integer idActaPago, FacturaActaContratoOC factura) {
        return bean.guardarFactura(idActaPago, factura);
    }

    /**
     * Este método permite eliminar una factura según id.
     *
     * @param idFactura
     */
    public void eliminarFactura(Integer idFactura) {
        bean.eliminarFactura(idFactura);
    }

    /**
     * Este método permite aprobar un acta.
     *
     * @param idActa
     */
    public void aprobarActa(Integer idActa) {
        bean.aprobarActa(idActa);
    }

    /**
     * Este método permite revertir un acta.
     *
     * @param idActa
     */
    public void revertirActa(Integer idActa) {
        bean.revertirActa(idActa);
    }

    /**
     * Este método permite generan el quedan de un acta.
     *
     * @param idActa
     * @return
     */
    public QuedanEmitido generarQuedan(Integer idActa) {
        return bean.generarQuedan(idActa);
    }

    /**
     * método que se encarga de eliminar un quedan emitido
     *
     *
     * @param idActa
     * @return
     */
    public void eliminarQuedan(Integer idActa) {
        bean.eliminarQuedan(idActa);
    }

    /**
     * Este método permite generar un comprobante de recepción de expediente de
     * pago de un acta.
     *
     * @param idActa
     * @return
     */
    public ComprobanteDeRecepcionDeExpedienteDePago generarComprobanteRecepcionExpedienteDepago(Integer idActa) {
        return bean.generarComprobanteRecepcionExpedienteDepago(idActa);
    }

    /**
     * elimina el comprobante de recepción de expediente de pago
     *
     *
     * @param idActa
     * @return
     */
    public void eliminarComprobanteRecepcionExpedienteDepago(Integer idActa) {
        bean.eliminarComprobanteRecepcionExpedienteDepago(idActa);
    }

    /**
     * Este método determina si un contrato se puede cerrar
     *
     * @param contrato
     * @return
     */
    public Boolean sePuedeCerrarContrato(ContratoOC contrato) {
        return bean.sePuedeCerrarContrato(contrato);
    }

    /**
     * Este método permite obtener el monto pagado por insumo
     *
     * @param idPOInsumo
     * @return
     */
    public BigDecimal getMontoPagadoPorInsumo(Integer idPOInsumo) {
        return bean.getMontoPagadoPorInsumo(idPOInsumo);
    }

    /**
     * Este método permite abrir un contrato
     *
     * @param idContratoOC
     */
    public void abrirContratoOC(Integer idContratoOC) {
        bean.abrirContratoOC(idContratoOC);
    }

    /**
     * Este método permite rescindir un contrato.
     *
     * @param contrato
     * @return
     */
    public ContratoOC rescindirContratoOC(ContratoOC contrato) {
        return bean.rescindirContratoOC(contrato);
    }

    /**
     * Valida que la programación de pagos de un contrato sea correcta y la
     * guarda.
     *
     * @param contrato
     * @return
     */
    public ContratoOC guardarProgramacionDePagosDeContrato(ContratoOC contrato) {
        return bean.guardarProgramacionDePagosDeContrato(contrato);
    }

    /**
     * Devuelve el monto total de las actas (aprobadas y con quedan emitido) que
     * aparecen tras el filtro de la consulta de comprobante de retención de
     * impuestos
     *
     * @param proveedor
     * @param idImpuesto
     * @param fechaDesde
     * @param fechaHasta
     * @return
     */
    public BigDecimal getMontoTotalParaComprobanteRetencionImpuestosPorProv(Integer idProveedor, Integer idImpuesto, Date fechaDesde, Date fechaHasta) {
        return bean.getMontoTotalParaComprobanteRetencionImpuestosPorProv(idProveedor, idImpuesto, fechaDesde, fechaHasta);
    }

    /**
     * Habilita la extensión de plazo del contrato y guarda la nueva fecha de
     * fin (en caso que se haya modificado)
     *
     * @param idContrato
     * @param fechaFinParaExtender
     * @return
     */
    public void guardarExtensionPlazoContrato(Integer idContrato, Date fechaFinParaExtender) {
        bean.guardarExtensionPlazoContrato(idContrato, fechaFinParaExtender);
    }

    /**
     * Deshabilita la posibilidad de extenderle el plazo a un contrato
     *
     * @param idContrato
     */
    public void deshabilitarExtenderPlazoContrato(Integer idContrato) {
        bean.deshabilitarExtenderPlazoContrato(idContrato);
    }

    /**
     * Devuelve la lista de los POInsumos que no están agregados en un proceso
     * de adquisición, están en un PAC cerrado, tienen asociado un insumo que
     * está contenido en la lista pasada por parámetro y no están asociado a
     * ninguna modificativa
     *
     * @param listaIdInsumosPermitidos
     * @return
     */
    public List<POInsumos> getPoInsumosDisponiblesParaModificativasContratoOC(Integer idContrato, ModificativaContrato tempModificativa) {
        return bean.getPoInsumosDisponiblesParaModificativasContratoOC(idContrato, tempModificativa);
    }

    /**
     * Devuelve la lista de los POInsumos que no están agregados en un proceso
     * de adquisición, están en un PAC cerrado, tienen asociado un insumo que
     * está contenido en la lista pasada por parámetro y no están asociado a
     * ninguna modificativa
     *
     * @param listaIdInsumosPermitidos
     * @return
     */
    public ModificativaContrato cargarModificativaContratoOC(Integer idModificativa) {
        return bean.cargarModificativaContratoOC(idModificativa);
    }

    /**
     * Guarda una modificativa en la base de datos
     *
     * @param tempModificativa
     */
    public void guardarModificativa(ModificativaContrato tempModificativa, Integer idContrato, List<POInsumos> poInsumosAEliminarDeModificativa) {
        bean.guardarModificativa(tempModificativa, idContrato, poInsumosAEliminarDeModificativa);
    }

    /**
     * Crea un compromiso presupuestario y lo asocia a una modificativa
     *
     * @param proId
     */
    public ModificativaContrato solicitarCompromisoPresupuestarioParaModificativa(Integer idModificaiva) {
        return bean.solicitarCompromisoPresupuestarioParaModificativa(idModificaiva);
    }

    /**
     * Aprueba una modificativa, cambiándole el estado, actualizando el monto,
     * insumos y programación de pagos del contrato
     *
     * @param idModificativa
     */
    public void aprobarModificativa(ModificativaContrato tempModificativa, List<POInsumos> poInsumosAEliminarDeModificativa) {
        bean.aprobarModificativa(tempModificativa, poInsumosAEliminarDeModificativa);
    }

    /**
     * Calcula el monto total de una modificativa en base al monto certificado
     * de los insumos que tiene asociado
     *
     * @param idContrato
     * @return
     */
    public BigDecimal calcularMontoTotalModificativa(Integer idModificativa) {
        return bean.calcularMontoTotalModificativa(idModificativa);
    }

    /**
     * Este método se encarga de generar la reserva de fondos para un compromiso
     * presupuestario Calcula la distribución por fuentes de la reserva y la
     * guarda
     *
     * @param idCompromiso
     * @return
     * @throws GeneralException
     */
    public CompromisoPresupuestario generarReservaDeFondos(Integer idCompromiso) throws GeneralException {
        return bean.generarReservaDeFondos(idCompromiso);
    }

    /**
     * Devuelve los insumos asociados al proceso o la modificativa del
     * compromiso presupuestario seleccionado
     *
     * @param idCompromisoPresupuestario
     * @return
     */
    public List<POInsumos> getPoInsumosCompromisoPresupuestario(Integer idCompromisoPresupuestario) {
        return bean.getPoInsumosCompromisoPresupuestario(idCompromisoPresupuestario);
    }

    /**
     * Valida el monto a descomprometer de cada insumo y actualiza el monto
     * comprometido
     */
    public void confirmarDescomprometerInsumos(List<POInsumos> listaPoInsumosADescomprometer) {
        bean.confirmarDescomprometerInsumos(listaPoInsumosADescomprometer);
    }

    /**
     * Filtra la consulta de Contratos / OC
     *
     * @param nroContratoOC
     * @param tipoContratoOC
     * @param nombreProceso
     * @param fechaDesde
     * @param fechaHasta
     * @param proveedor
     * @param estadoContratoOC
     * @param nroInsumo
     * @param estadoContrato
     * @param usuarioCodigo
     * @param firstResult
     * @param maxResults
     * @param orderBy
     * @param ascending
     * @return
     */
    public List<ContratoOC> getConsultaContratosOC(Integer nroContratoOC, TipoContrato tipoContratoOC, String nombreProceso, Date fechaDesde, Date fechaHasta, String proveedor, EstadoContrato estadoContratoOC, Integer nroInsumo, EstadoContrato estadoContrato, String usuarioCodigo, Integer firstResult, Integer maxResults, String[] orderBy, boolean[] ascending) {
        return bean.getConsultaContratosOC(nroContratoOC, tipoContratoOC, nombreProceso, fechaDesde, fechaHasta, proveedor, estadoContratoOC, nroInsumo, estadoContrato, usuarioCodigo, firstResult, maxResults, orderBy, ascending);
    }

    /**
     * Devuelve la cantidad de contratos que va a mostrar la consulta según los
     * filtros que hayan sido aplicados
     *
     * @param nroContratoOC
     * @param tipoContratoOC
     * @param nombreProceso
     * @param fechaDesde
     * @param fechaHasta
     * @param proveedor
     * @param estadoContratoOC
     * @param nroInsumo
     * @param estadoContrato
     * @param usuarioCodigo
     * @return
     */
    public long countConsultaContratosOC(Integer nroContratoOC, TipoContrato tipoContratoOC, String nombreProceso, Date fechaDesde, Date fechaHasta, String proveedor, EstadoContrato estadoContratoOC, Integer nroInsumo, EstadoContrato estadoContrato, String usuarioCodigo) {
        return bean.countConsultaContratosOC(nroContratoOC, tipoContratoOC, nombreProceso, fechaDesde, fechaHasta, proveedor, estadoContratoOC, nroInsumo, estadoContrato, usuarioCodigo);
    }

    /**
     * Comprueba si el usuario logueado es el administrador de determinado contrato
     * @param contrato
     * @param codigoUsuarioLogueado 
     * @return 
     */
    public Boolean usuarioEsAdministradorDelContrato(ContratoOC contrato, String codigoUsuarioLogueado) {
        return bean.usuarioEsAdministradorDelContrato(contrato, codigoUsuarioLogueado);
    }

    /**
     * Guarda la duración del insumo en el contrato
     * @param relacionItemInsumoEnEdicion 
     */
    public RelacionProAdqItemInsumo guardarDuracionInsumoEnContrato(RelacionProAdqItemInsumo relacionItemInsumoEnEdicion) {
        return bean.guardarDuracionInsumoEnContrato(relacionItemInsumoEnEdicion);
    }
    
    
}
