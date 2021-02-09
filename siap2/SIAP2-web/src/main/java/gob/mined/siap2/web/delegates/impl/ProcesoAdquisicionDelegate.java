/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.delegates.impl;

import gob.mined.siap2.business.ProcesoAdquisicionBean;
import gob.mined.siap2.entities.data.impl.Archivo;
import gob.mined.siap2.entities.data.impl.CompromisoPresupuestario;
import gob.mined.siap2.entities.data.impl.ContratoOC;
import gob.mined.siap2.entities.data.impl.PAC;
import gob.mined.siap2.entities.data.impl.PACGrupo;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicion;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionInsumo;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionItem;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionItemProvOferta;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionLote;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionProveedor;
import gob.mined.siap2.entities.data.impl.Proveedor;
import gob.mined.siap2.entities.data.impl.RelacionProAdqItemInsumo;
import gob.mined.siap2.entities.data.impl.TDR;
import gob.mined.siap2.entities.enums.EstadoItem;
import gob.mined.siap2.entities.enums.EstadoProcesoAdq;
import gob.mined.siap2.entities.enums.PasosProcesoAdquisicion;
import gob.mined.siap2.entities.enums.TipoGeneracionContrato;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.ws.to.DataFile;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Sofis Solutions
 */
@Named
public class ProcesoAdquisicionDelegate implements Serializable {

    @Inject

    private ProcesoAdquisicionBean bean;

    public List<PAC> obtenerPacsGruposAgregarInsumos(Integer pacId, String nombrePac, Integer rubro, String codigoIns, String nombreIns, Integer unidadTecId, Integer idPacGrupo, String nombreGrupo) {
        return bean.obtenerPacsParaAgregarInsumos(pacId, nombrePac, rubro, codigoIns, nombreIns, unidadTecId, idPacGrupo, nombreGrupo);

    }

    public List<PACGrupo> obtenerPacsGruposConInsumosParaProcesosDeAdquisicionPorPacId(Integer pacId, Integer rubro, String codigoIns, String nombreIns, Integer unidadTecId, Integer pacGrupoId, String nombreGrupo) {
        return bean.obtenerPacsGruposConInsumosParaProcesosDeAdquisicionPorPacId(pacId, rubro, codigoIns, nombreIns, unidadTecId, pacGrupoId, nombreGrupo);
    }

    public ContratoOC generarDistribucionPagosAutomaticaProceso(Integer idContrato, Integer anioEmpieza, Integer mesInicial, Integer cantidadMeses) {
        return bean.generarDistribucionPagosAutomaticaProceso(idContrato, anioEmpieza, mesInicial, cantidadMeses);
    }

    public List<POInsumos> obtenerPoInsumosParaProcesosDeAdquisicionPorPacGrupoId(Integer pacGrupoId, Integer rubro, String codigoIns, String nombreIns, Integer unidadTecId) {
        return bean.obtenerPoInsumosParaProcesosDeAdquisicionPorPacGrupoId(pacGrupoId, rubro, codigoIns, nombreIns, unidadTecId);
    }

    public ProcesoAdquisicion guardarProceso(ProcesoAdquisicion proceso, Integer idMetodo, Date fechaInicio, String ganttJson) {
        return bean.guardarProceso(proceso, idMetodo, fechaInicio, ganttJson);
    }

    public void guardarMetodoAdquisicion(Integer idProceso, Integer idMetodo, Date fechaInicio, String ganttJson) {
        bean.guardarMetodoAdquisicion(idProceso, idMetodo, fechaInicio, ganttJson);
    }

    public ProcesoAdquisicion cargarProceso(Integer id) {
        return bean.cargarProceso(id);
    }

    public ContratoOC guardarProgramacionDePagosProceso(ContratoOC contrato) {
        return bean.guardarProgramacionDePagosProceso(contrato);
    }

    public ProcesoAdquisicion cambiarEstadoProceso(int idProceso, PasosProcesoAdquisicion nuevoEstado, String ganttNuevo) throws GeneralException {
        return bean.cambiarEstadoProceso(idProceso, nuevoEstado, ganttNuevo);
    }

    public Map obtenerPacsYGruposDeInsumosdeProcesoAdquisicion(Integer proId) {
        return bean.obtenerPacsYGruposDeInsumosdeProcesoAdquisicion(proId);
    }

    public void eliminarInsumoProceso(int idProceso, int idInsumo) {
        bean.eliminarInsumoProceso(idProceso, idInsumo);
    }

    public Boolean agregarInsumosProceso(ProcesoAdquisicion procAdq, Set<POInsumos> insumosAAgregar, boolean chequearMontos) {
        return bean.agregarInsumosProceso(procAdq, insumosAAgregar, chequearMontos);
    }

    public List<ProcesoAdquisicionItem> obtenerItemsSinLoteProcesoAdquisicion(Integer proId) throws GeneralException {
        return bean.obtenerItemsSinLoteProcesoAdquisicion(proId);
    }

    public List<ProcesoAdquisicionLote> obtenerLotesProcesoAdquisicion(Integer proId) throws GeneralException {
        return bean.obtenerLotesProcesoAdquisicion(proId);
    }

    public ProcesoAdquisicion guardarLoteItem(ProcesoAdquisicion porceso, List<ProcesoAdquisicionItem> items, List<ProcesoAdquisicionLote> lotes, List<ProcesoAdquisicionItem> itemsAEliminar, List<ProcesoAdquisicionLote> lotesAEliminar, List<RelacionProAdqItemInsumo> relacionesItemsInsumosEliminadas) throws GeneralException {
        return bean.guardarLoteItem(porceso, items, lotes, itemsAEliminar, lotesAEliminar, relacionesItemsInsumosEliminadas);
    }

    public List<Proveedor> obtenerProveedoresNoAsignadosAlproceso(Integer proId) {
        return bean.obtenerProveedoresNoAsignadosAlproceso(proId);
    }

    public void guardarProveedores(ProcesoAdquisicion proceso, List<ProcesoAdquisicionProveedor> proveedores, List<ProcesoAdquisicionProveedor> eliminarProveedores) throws GeneralException {
        bean.guardarProveedores(proceso, proveedores, eliminarProveedores);
    }

    public List<Proveedor> obtenerProveedoresDelProceso(Integer proId) throws GeneralException {
        return bean.obtenerProveedoresDelProceso(proId);
    }

    public ProcesoAdquisicionItemProvOferta agregarEditarOfertaItem(ProcesoAdquisicionItemProvOferta oferta, Integer idItem) throws GeneralException {
        return bean.agregarEditarOfertaItem(oferta, idItem);
    }

    public void eliminarOfertaItem(ProcesoAdquisicionItemProvOferta eliminarOferta) throws GeneralException {
        bean.eliminarOfertaItem(eliminarOferta);

    }

    public List<ProcesoAdquisicionItemProvOferta> obtenerOfertasDelItem(Integer proItemId) throws GeneralException {
        return bean.obtenerOfertasDelItem(proItemId);
    }

    public ProcesoAdquisicionProveedor obtenerProcesoAdquisicionProveedorPorProveedorIdyProcesoId(Integer proveedorId, Integer procesoId) throws GeneralException {
        return bean.obtenerProcesoAdquisicionProveedorPorProveedorIdyProcesoId(proveedorId, procesoId);
    }

    public List<ProcesoAdquisicionProveedor> obtenerProveedoresAdquisicionDelProceso(Integer proId) throws GeneralException {
        return bean.obtenerProveedoresAdquisicionDelProceso(proId);
    }

    public ProcesoAdquisicion asignarItem(ProcesoAdquisicionItem item, ProcesoAdquisicionItemProvOferta selectOferta, EstadoItem estadoItem, Integer idProceso) throws GeneralException {
        return bean.asignarItem(item, selectOferta, estadoItem, idProceso);
    }

    public ProcesoAdquisicionItem deshacerEstadoItem(Integer idItem) throws GeneralException {
        return bean.deshacerEstadoItem(idItem);
    }

    public void reservarNumerosContrato(Integer proId, Integer cantidadNros) throws GeneralException {
        bean.reservarNumerosContrato(proId, cantidadNros);
    }

    public List<ProcesoAdquisicionItem> obtenerTodosLosItemsDelProceso(Integer proId) {
        return bean.obtenerTodosLosItemsDelProceso(proId);
    }

    public List<ProcesoAdquisicionProveedor> obtenerProveedoresGanadoresItemProceso(Integer proId) {
        return bean.obtenerProveedoresGanadoresItemProceso(proId);
    }

    public List<ContratoOC> obtenerContratos(Integer proId) throws GeneralException {
        return bean.obtenerContratos(proId);
    }

    public Map obtenerTodosLosItemsDelProcesoConOfertaGanadoraSinContratoAsigando(Integer proId) {
        return bean.obtenerTodosLosItemsDelProcesoConOfertaGanadoraSinContratoAsigando(proId);
    }

    public ProcesoAdquisicion generarActualizarContratos(Integer idProcesoAdquisicion, TipoGeneracionContrato tipoGeneracion) {
        return bean.generarActualizarContratos(idProcesoAdquisicion, tipoGeneracion);
    }

    public ContratoOC actualizarContrato(ContratoOC contrato) {
        return bean.actualizarContrato(contrato);
    }

    public List<ProcesoAdquisicionItem> obtenerItemsDelProcesoAdjudicados(Integer proId) {
        return bean.obtenerItemsDelProcesoAdjudicados(proId);
    }

    public void generarContratos(Integer proId) {
        bean.generarContratos(proId);
    }

    public TDR getTDRProceso(Integer idProceso) throws GeneralException {
        return bean.getTDRProceso(idProceso);
    }

    public ProcesoAdquisicion saveTDRProceso(Integer idProceso, TDR tdr) throws GeneralException {
        return bean.saveTDRProceso(idProceso, tdr);
    }

    public TDR getTDRItem(Integer idItem) throws GeneralException {
        return bean.getTDRItem(idItem);
    }

    public void saveTDRItem(Integer idItem, TDR tdr) throws GeneralException {
        bean.saveTDRItem(idItem, tdr);
    }

    public ProcesoAdquisicionItem obtenerItemDelInsumo(Integer idInsumo) {
        return bean.obtenerItemDelInsumo(idInsumo);
    }

    public void pausarProceso(Integer procesoAdquisicionId) {
        bean.pausarProceso(procesoAdquisicionId);
    }

    public Integer obtenerUltimoNroItemProceso(Integer procesoAdquisicionId) {
        return bean.obtenerUltimoNroItemProceso(procesoAdquisicionId);
    }

    public void eliminarContrato(Integer contratoId) {
        bean.eliminarContrato(contratoId);
    }

    public void editarInsumosProceso(RelacionProAdqItemInsumo relacioneInsumoItem) throws GeneralException {
        bean.editarInsumosProceso(relacioneInsumoItem);
    }

    public List<ProcesoAdquisicionInsumo> obtenerInsumosDelItem(Integer idItem) throws GeneralException {
        return bean.obtenerInsumosDelItem(idItem);
    }

    public void notificarUsuariosUTSobreTDR(Integer idProcesoAdquisicion) {
        bean.notificarUsuariosUTSobreTDR(idProcesoAdquisicion);
    }

    public void guardarInsumos(List<ProcesoAdquisicionInsumo> insumos, Map<Integer, Boolean> mapPoInsumosRecepcionTDR) {
        bean.guardarInsumos(insumos, mapPoInsumosRecepcionTDR);
    }

    public POInsumos actualizarCertificadosDeDisponibilidadPresupuestaria(Integer idPOInsumo, List<Archivo> archivos, List<DataFile> aAgregar) {
        return bean.actualizarCertificadosDeDisponibilidadPresupuestaria(idPOInsumo, archivos, aAgregar);
    }

    public List<ProcesoAdquisicionInsumo> obtenerInsumosDelProceso(Integer idProceso) {
        return bean.obtenerInsumosDelProceso(idProceso);
    }

    public POInsumos obtenerPoInsumoById(Integer idPoInsumo) {
        return bean.obtenerPoInsumoById(idPoInsumo);
    }

    public void verificarConsistenciaProceso(Integer idProceso) {
        bean.verificarConsistenciaProcesoParaEstadoRecepcionTDR(idProceso);
    }

    public ProcesoAdquisicion pausarProceso(Integer idProceso, EstadoProcesoAdq estadoProceso) {
        return bean.pausarProceso(idProceso, estadoProceso);
    }

    public ProcesoAdquisicion declararProcesoDesiertoOSinEfecto(Integer idProceso, EstadoProcesoAdq estado) {
        return bean.declararProcesoDesiertoOSinEfecto(idProceso, estado);
    }

    public ProcesoAdquisicionItem pausarItem(Integer idItem) {
        return bean.pausarItem(idItem);
    }

    public ProcesoAdquisicionItemProvOferta adjudicarItemSinCompromisoPresupuestario(ProcesoAdquisicionItemProvOferta oferta) {
        return bean.adjudicarItemSinCompromisoPresupuestario(oferta);
    }

    public String getColorSegunFechaPlanificada(ProcesoAdquisicion procesoAdq) {
        return bean.getColorSegunFechaPlanificada(procesoAdq);
    }

    public Integer obtenerTiempoPlanificadoParaEstadoActual(Integer idProceso) {
        return bean.obtenerTiempoPlanificadoParaEstadoActual(idProceso);
    }

    public Integer obtenerTiempoRealParaEstadoActual(Integer idProceso) {
        return bean.obtenerTiempoRealParaEstadoActual(idProceso);
    }

    public Date obtenerFechaInicioPlanificada(Integer idProceso) {
        return bean.obtenerFechaInicioPlanificada(idProceso);
    }

    public Date obtenerFechaFinPlanificadaParaPasoActual(Integer idProceso) {
        return bean.obtenerFechaFinPlanificadaParaPasoActual(idProceso);
    }

    public Date obtenerFechaInicioReal(Integer idProceso) {
        return bean.obtenerFechaInicioReal(idProceso);
    }

    public Integer obtenerTiempoAtraso(Integer idProceso) {
        return bean.obtenerTiempoAtraso(idProceso);
    }

    public Integer obtenerTiempoAdelanto(Integer idProceso) {
        return bean.obtenerTiempoAdelanto(idProceso);
    }

    public void solicitarCompromisoPresupuestario(Integer proId) {
        bean.solicitarCompromisoPresupuestario(proId);
    }

    public CompromisoPresupuestario emitirCompromisoPresupuestario(Integer compromidoId) {
        return bean.emitirCompromisoPresupuestario(compromidoId);
    }

    public CompromisoPresupuestario validarCompromisoPresupuestario(Integer compromidoId, String codigoSAFI) {
        return bean.validarCompromisoPresupuestario(compromidoId, codigoSAFI);
    }

    public String obtenerUltimos5PreciosInsumoAdjudicado(Integer idInsumo) {
        return bean.obtenerUltimos5PreciosInsumoAdjudicado(idInsumo);
    }

    public void verificarGeneracionInvitaciones(Integer idProceso) {
        bean.verificarGeneracionInvitaciones(idProceso);
    }

}
