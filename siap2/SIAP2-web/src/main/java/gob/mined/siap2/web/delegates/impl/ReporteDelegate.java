/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.delegates.impl;

import gob.mined.siap2.business.ejbs.impl.ReporteBean;
import gob.mined.siap2.entities.data.impl.ContratoOC;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.ws.to.RiesgoTO;
import gob.mined.siap2.ws.to.RiesgoTOPOA;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Sofis Solutions
 */
@Named
public class ReporteDelegate implements Serializable {

    @Inject
    private ReporteBean bean;

    public byte[] generarCertificadoDisponibilidadPOInsumo(Integer idPOMontoFuenteInsumo) {
        return bean.generarCertificadoDisponibilidadPOInsumo(idPOMontoFuenteInsumo);
    }

    public byte[] generarDocumentoCompra(Integer contratoId) throws GeneralException {
        return bean.generarDocumentoCompra(contratoId);
    }

    public byte[] generarDocumentoOrdenCompra(Integer contratoId) throws GeneralException {

        return bean.generarDocumentoOrdenCompra(contratoId);
    }

    public byte[] generarInvitacionesProveedores(Integer procesoId, String observaciones) throws GeneralException {
        return bean.generarInvitacionesProveedores(procesoId, observaciones);
    }

    public byte[] generarDocumentoReservaDeFondos(Integer procesoId) throws GeneralException {
        return bean.generarDocumentoReservaDeFondos(procesoId);
    }

    public byte[] generarActaDeRecepcion(Integer pagoId) throws GeneralException {
        return bean.generarActaDeRecepcion(pagoId);
    }

    public byte[] generarActaDeAnticipo(Integer pagoId) throws GeneralException {
        return bean.generarActaDeAnticipo(pagoId);
    }

    public byte[] generarActaDeDevolucion(Integer pagoId) throws GeneralException {
        return bean.generarActaDeDevolucion(pagoId);
    }

    public byte[] generarReporteAsignacionNoProgramable(Integer asignacionNoProgramableId) throws GeneralException {
        return bean.generarReporteAsignacionNoProgramable(asignacionNoProgramableId);
    }

    public byte[] generarReporteAccionCentral(Integer accionCentralId) throws GeneralException {
        return bean.generarReporteAccionCentral(accionCentralId);
    }

    public byte[] generarReporteGralAsignacionNoProgramable() throws GeneralException {
        return bean.generarReporteGralAsignacionNoProgramable();
    }

    public byte[] generarReporteGralAccionCentral() throws GeneralException {
        return bean.generarReporteGralAccionCentral();
    }

    public byte[] generarReporteProyecto(Integer idProyecto) throws GeneralException {
        return bean.generarReporteProyecto(idProyecto);
    }

    public byte[] generarReportePEPGlobal(Integer idAnioFiscal) throws GeneralException {
        return bean.generarReportePEPGlobal(idAnioFiscal);
    }
    public byte[] generarReportePEPGlobal(Integer idAnioFiscal, Integer idProyecto) throws GeneralException {
        return bean.generarReportePEPGlobal(idAnioFiscal, idProyecto);
    }
    public byte[] generarOrdenInicio(ContratoOC contrato) {
        return bean.generarOrdenInicio(contrato);
    }

    public byte[] generarRetencionDeImpuestosPorProyecto(Integer idProyecto, Date fechaDesde, Date fechahasta, Integer idImpuesto) throws GeneralException {
        return bean.generarRetencionDeImpuestosPorProyecto(idProyecto, fechaDesde, fechahasta, idImpuesto);
    }

    public byte[] generarReportePACGlobal(Integer anio) throws GeneralException {
        return bean.generarReportePACGlobal(anio);
    }

    public byte[] generarRetencionDeImpuestosPorProveedor(Integer anio, Integer mes, Integer idImpuesto) throws GeneralException {
        return bean.generarRetencionDeImpuestosPorProveedor(anio, mes, idImpuesto);
    }

    public byte[] generarConstanciaRetencionDeImpuestos(Integer anio, Integer idProveedor, Integer idUsuario) throws GeneralException {
        return bean.generarConstanciaRetencionDeImpuestos(anio, idProveedor, idUsuario);
    }

    public byte[] generarFichaContrato(Integer contratoId) throws GeneralException {
        return bean.generarFichaContrato(contratoId);
    }
    
    public byte[] generarCompromisoPresupuestario(Integer idCompromiso) {
        return bean.generarCompromisoPresupuestario(idCompromiso);
    }

    public byte[] generarReporteReprogramacion(Integer reprogramacionId) {
        return bean.generarReporteReprogramacion(reprogramacionId);
    }    

    public byte[] generarReportePlanOperativoAnual(Integer poaId) {
        return bean.generarReportePlanOperativoAnual(poaId);
    } 
    
    public byte[] generarSeguimientoProyectoAdministrativoMensual(List<HashMap> registros) {
        return bean.generarSeguimientoProyectoAdministrativoMensual(registros);
    }
    
    public byte[] generarSeguimientoProyectoAdministrativoTrimestral(List<HashMap> registros) {
        return bean.generarSeguimientoProyectoAdministrativoTrimestral(registros);
    }
    
    public byte[] generarReportePOI(List<HashMap> registros, String planificacion, String anio, String tipoPrograma){
        return bean.generarReportePOI(registros, planificacion, anio, tipoPrograma);
    }
    
    public byte[] generarReporteMatrizRiesgo(List<RiesgoTO> listaRiesgos){
        return bean.generarReporteMatrizRiesgo(listaRiesgos);
    }
    public byte[] generarReporteMatrizRiesgoPOA(List<RiesgoTOPOA> listaRiesgos){
        return bean.generarReporteMatrizRiesgoPOA(listaRiesgos);
    }

    /**
     * Genera el reporte pdf de una poliza de concentración
     * @param polizaId
     * @return 
     */
    public byte[] generarPolizaConcentracion(Integer polizaId) {
        return bean.generarPolizaConcentracion(polizaId);
    }

}
