/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.ejbs.impl;

import gob.mined.siap2.business.datatype.DataReportePolizaConcentracion;
import gob.mined.siap2.business.datatype.DataDistribuccionProgramacionPagosContrato;
import gob.mined.siap2.business.datatype.DataFilaGlobalReportePEP;
import gob.mined.siap2.business.datatype.DataFilaReportePEP;
import gob.mined.siap2.business.datatype.DataFlujoCajaAnioParaReporte;
import gob.mined.siap2.business.datatype.DataMetasPOA;
import gob.mined.siap2.business.datatype.DataPOI;
import gob.mined.siap2.business.datatype.DataReporteAC;
import gob.mined.siap2.business.datatype.DataReporteANP;
import gob.mined.siap2.business.datatype.DataReporteANPActividades;
import gob.mined.siap2.business.datatype.DataReporteANPClasificadorFuncional;
import gob.mined.siap2.business.datatype.DataReporteANPMontos;
import gob.mined.siap2.business.datatype.DataReporteAccionCentraLineas;
import gob.mined.siap2.business.datatype.DataReporteAccionCentral;
import gob.mined.siap2.business.datatype.DataReporteActaDeRecepcion;
import gob.mined.siap2.business.datatype.DataReporteActaDeRecepcionItem;
import gob.mined.siap2.business.datatype.DataReporteAsignacionNoProgramable;
import gob.mined.siap2.business.datatype.DataReporteAsignacionNoProgramableLineas;
import gob.mined.siap2.business.datatype.DataReporteAsignacionNoProgramableSubActividades;
import gob.mined.siap2.business.datatype.DataReporteComprobanteRecepcionExpedientePago;
import gob.mined.siap2.business.datatype.DataReporteCompromisoPresupuestario;
import gob.mined.siap2.business.datatype.DataReporteCompromisoPresupuestarioInsumo;
import gob.mined.siap2.business.datatype.DataReporteCompromisoPresupuestarioProgPagos;
import gob.mined.siap2.business.datatype.DataReporteCompromisoPresupuestarioProgPagosItem;
import gob.mined.siap2.business.datatype.DataReporteDisponibilidadPresupuestaria;
import gob.mined.siap2.business.datatype.DataReporteDisponibilidadPresupuestariaProceso;
import gob.mined.siap2.business.datatype.DataReportePAC;
import gob.mined.siap2.business.datatype.DataReportePACInsumo;
import gob.mined.siap2.business.datatype.DataReportePEP;
import gob.mined.siap2.business.datatype.DataReportePEPGlobal;
import gob.mined.siap2.business.datatype.DataReportePlanOperativoAnual;
import gob.mined.siap2.business.datatype.DataReportePolizaDetalleAplicacionEnPOAyPEP;
import gob.mined.siap2.business.datatype.DataReportePolizaDetalleFactura;
import gob.mined.siap2.business.datatype.DataReporteProvedoor;
import gob.mined.siap2.business.datatype.DataReporteProyecto;
import gob.mined.siap2.business.datatype.DataReporteProyectoEstructura;
import gob.mined.siap2.business.datatype.DataReporteProyectoEstructuraMontoFuente;
import gob.mined.siap2.business.datatype.DataReporteProyectoFinanciacion;
import gob.mined.siap2.business.datatype.DataReporteProyectoIndicador;
import gob.mined.siap2.business.datatype.DataReporteQUEDAN;
import gob.mined.siap2.business.datatype.DataReporteQUEDANCategorias;
import gob.mined.siap2.business.datatype.DataReporteQUEDANFuentes;
import gob.mined.siap2.business.datatype.DataReporteQUEDANImpuestos;
import gob.mined.siap2.business.datatype.DataReporteQUEDANRetenciones;
import gob.mined.siap2.business.datatype.DataReporteQuedanFactura;
import gob.mined.siap2.business.datatype.DataReporteReprogramacion;
import gob.mined.siap2.business.datatype.DataReporteReprogramacionInsumos;
import gob.mined.siap2.business.datatype.DataReporteReservaFondos;
import gob.mined.siap2.business.datatype.DataReporteRetencionDeImpuestosPorProyecto;
import gob.mined.siap2.business.datatype.DataReporteRetencionImpuestoPorProveedor;
import gob.mined.siap2.business.datatype.DataReporteRetencionImpuestoPorProveedorItem;
import gob.mined.siap2.business.datatype.DataReporteRetencionImpuestoPorProveedorItemTemporal;
import gob.mined.siap2.business.datatype.DataReporteTemplate;
import gob.mined.siap2.business.datatype.DataReporteinsumoReservaFondos;
import gob.mined.siap2.business.datatype.DataRetencionImpuesoPorProveedor;
import gob.mined.siap2.business.datatype.DataRiesgo;
import gob.mined.siap2.business.datatype.DataSeguimientoProyAdm;
import gob.mined.siap2.business.datatype.DataTablaReporteRetencionDeImpuestosPorProyecto;
import gob.mined.siap2.business.datatype.DistribucionMontoAdjudicado;
import gob.mined.siap2.business.datatype.DistribucionProgramacionPagosInsumos;
import gob.mined.siap2.business.datatype.DistribucionProgramacionPagosItem;
import gob.mined.siap2.business.datatype.DistribucionProgramacionPagosMes;
import gob.mined.siap2.business.ejbs.ConfiguracionBean;
import gob.mined.siap2.business.ejbs.DatosUsuario;
import gob.mined.siap2.business.ejbs.SecuenciaBean;
import gob.mined.siap2.business.ejbs.UsuarioBean;
import gob.mined.siap2.business.utils.ContratoUtils;
import gob.mined.siap2.business.utils.DataActa;
import gob.mined.siap2.business.utils.DataFichaContrato;
import gob.mined.siap2.business.utils.DataInsumo;
import gob.mined.siap2.business.utils.DataInsumo2;
import gob.mined.siap2.business.utils.DataItem;
import gob.mined.siap2.business.utils.DataOrdenInicio;
import gob.mined.siap2.business.utils.DataProveedorItems;
import gob.mined.siap2.business.utils.DataQuedanEmitido;
import gob.mined.siap2.business.utils.DataReportesContratoOC;
import gob.mined.siap2.business.utils.DateUtils;
import gob.mined.siap2.business.utils.GanttUtils;
import gob.mined.siap2.business.utils.InsumoUtils;
import gob.mined.siap2.business.utils.NumeroALetra;
import gob.mined.siap2.business.utils.ReporteConverter;
import gob.mined.siap2.business.utils.ReportesUtils;
import gob.mined.siap2.business.utils.TipoSeguimientoUtils;
import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesConfiguracion;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.Configuracion;
import gob.mined.siap2.entities.data.SsUsuario;
import gob.mined.siap2.entities.data.impl.AccionCentral;
import gob.mined.siap2.entities.data.impl.ActaContrato;
import gob.mined.siap2.entities.data.impl.ActividadAsignacionNP;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.Archivo;
import gob.mined.siap2.entities.data.impl.AsignacionNoProgramable;
import gob.mined.siap2.entities.data.impl.CertificadoDisponibilidadPresupuestaria;
import gob.mined.siap2.entities.data.impl.CompromisoPresupuestario;
import gob.mined.siap2.entities.data.impl.CompromisoPresupuestarioModificativa;
import gob.mined.siap2.entities.data.impl.CompromisoPresupuestarioProceso;
import gob.mined.siap2.entities.data.impl.ContratoOC;
import gob.mined.siap2.entities.data.impl.Factura;
import gob.mined.siap2.entities.data.impl.FacturaPolizaConcentracion;
import gob.mined.siap2.entities.data.impl.FuenteRecursos;
import gob.mined.siap2.entities.data.impl.Gantt;
import gob.mined.siap2.entities.data.impl.GanttTask;
import gob.mined.siap2.entities.data.impl.GeneralPOA;
import gob.mined.siap2.entities.data.impl.Impuesto;
import gob.mined.siap2.entities.data.impl.LineaEstrategica;
import gob.mined.siap2.entities.data.impl.ObjetoEspecificoGasto;
import gob.mined.siap2.entities.data.impl.PAC;
import gob.mined.siap2.entities.data.impl.POA;
import gob.mined.siap2.entities.data.impl.POAConActividades;
import gob.mined.siap2.entities.data.impl.POAMetaAnual;
import gob.mined.siap2.entities.data.impl.POAProyecto;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.data.impl.POMontoFuenteInsumo;
import gob.mined.siap2.entities.data.impl.PagoInsumo;
import gob.mined.siap2.entities.data.impl.PolizaDeConcentracion;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicion;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionInsumo;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionItem;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionLote;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionProveedor;
import gob.mined.siap2.entities.data.impl.ProgramacionFinancieraAccionCentral;
import gob.mined.siap2.entities.data.impl.ProgramacionFinancieraAsignacionNoProgramable;
import gob.mined.siap2.entities.data.impl.ProgramacionTrimestral;
import gob.mined.siap2.entities.data.impl.Proveedor;
import gob.mined.siap2.entities.data.impl.Proyecto;
import gob.mined.siap2.entities.data.impl.ProyectoAporte;
import gob.mined.siap2.entities.data.impl.ProyectoComponente;
import gob.mined.siap2.entities.data.impl.ProyectoMacroActividad;
import gob.mined.siap2.entities.data.impl.QuedanEmitido;
import gob.mined.siap2.entities.data.impl.RelActaContratoCategoriaConvenio;
import gob.mined.siap2.entities.data.impl.RelActaItem;
import gob.mined.siap2.entities.data.impl.RelacionProAdqItemInsumo;
import gob.mined.siap2.entities.data.impl.Reprogramacion;
import gob.mined.siap2.entities.data.impl.ReprogramacionDetalle;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.entities.data.impl.ValoresImpuestoQuedan;
import gob.mined.siap2.entities.enums.EstadoActa;
import gob.mined.siap2.entities.enums.EstadoCertificadoDispPresupuestaria;
import gob.mined.siap2.entities.enums.TipoActaContrato;
import gob.mined.siap2.entities.enums.TipoCompromisoPresupuestario;
import gob.mined.siap2.entities.enums.TipoContrato;
import gob.mined.siap2.entities.enums.TipoFactura;
import gob.mined.siap2.entities.enums.TipoImpuesto;
import gob.mined.siap2.entities.enums.TipoMontoPorAnio;
import gob.mined.siap2.entities.enums.TipoPOA;
import gob.mined.siap2.entities.enums.TipoSeguimiento;
import gob.mined.siap2.entities.enums.TipoTarea;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import gob.mined.siap2.persistence.dao.imp.ImpuestoDAO;
import gob.mined.siap2.persistence.dao.imp.PACDAO;
import gob.mined.siap2.persistence.dao.imp.POADAO;
import gob.mined.siap2.persistence.dao.imp.ProcesoAdquisicionDAO;
import gob.mined.siap2.utils.generalutils.DatesUtils;
import gob.mined.siap2.utils.generalutils.NumberUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.ws.to.RiesgoTO;
import gob.mined.siap2.ws.to.RiesgoTOPOA;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Stateless(name = "ReporteBean")
@LocalBean
public class ReporteBean {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
    private static final String formatoFechaConHora = "dd/MM/yyyy HH:mm";
    private static final String contentTypeAplicationPDF = "application/pdf";
    private static final String errorAlObtenerLogoReporte = "Error tratando de obtener el logo del reporte.";

    @Inject
    @JPADAO
    private GeneralDAO generalDAO;
    @Inject
    @JPADAO
    private ProcesoAdquisicionDAO pAdqDAO;
    
    @Inject
    private ProgramacionTrimestralBean ptBean;
    
    @Inject
    @JPADAO
    private PACDAO pacdao;
    @Inject
    private ArchivoBean archivoBean;
    @Inject
    @JPADAO
    private POADAO poaDAO;
    @Inject
    @JPADAO
    private ImpuestoDAO impuestoDAO;
    @Inject
    private POABean utBean;
    @Inject
    private GeneralBean generalBean;
    @Inject
    private DatosUsuario datosUsuario;
    @Inject
    private UsuarioBean usuarioBean;
    @Inject
    private ConfiguracionBean configuracionBean;
    @Inject
    private SecuenciaBean secuenciaBean;
    @Inject
    private InsumoBean insumoBean;

    /**
     * Este método genera el reporte de certificado de disponibilidad
     * presupuestaria
     *
     * @param poa
     * @param idPOMontoFuenteInsumo
     * @return
     */
    public byte[] generarCertificadoDisponibilidadPOInsumo(Integer idPOMontoFuenteInsumo) {
        try {

            POMontoFuenteInsumo monto = (POMontoFuenteInsumo) generalDAO.find(POMontoFuenteInsumo.class, idPOMontoFuenteInsumo);

            DataReporteDisponibilidadPresupuestaria data = this.cargarDataReporteDisponibilidadPresupuestariaParaPOMontoFuenteInsumo(monto, null);
            Configuracion tituloMinisterioReporte = configuracionBean.obtenerCnfPorCodigo(ConstantesConfiguracion.TITULO_MINISTERIO_REPORTES);
            DateFormat df = new SimpleDateFormat(formatoFechaConHora);
            String fechaHoraImpresionReporte = df.format(new Date());

            String areaReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_AREA_CERT_DISP_PRESUP_INSUMO);
            String tituloReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_TITULO_CERT_DISP_PRESUP_INSUMO);

            data.setTituloMinisterio(tituloMinisterioReporte.getCnfValor());
            data.setTituloAreaMinisterio(areaReporte);
            data.setFechaHoraImpresionReporte(fechaHoraImpresionReporte);
            data.setTituloNombreReporte(tituloReporte);
            data.setUsuarioImprimeReporte(datosUsuario.getCodigoUsuario());
            Configuracion monedaMontos = configuracionBean.obtenerCnfPorCodigo(ConstantesConfiguracion.ACLARACION_MONEDA_MONTOS_REPORTES);
            data.setAclaracionMonedaMontos(monedaMontos.getCnfValor());

            ClassLoader classLoader = getClass().getClassLoader();
            URL jasperFileNameURL = classLoader.getResource("reportes/certificadoDisponibilidadPresupuestaria2.jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFileNameURL);

            Map parcol = new HashMap();
            addLogoToMap(parcol);

            return ReportesUtils.generarBytesPDF(data, parcol, jasperReport);
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método genera un anexo de documento de orden de compra
     *
     * @param contratoId
     * @return
     * @throws GeneralException
     */
    public byte[] generarDocumentoCompra(Integer contratoId) throws GeneralException {
        try {
            ContratoOC contrato = (ContratoOC) generalDAO.findById(ContratoOC.class, contratoId);

            List<DataInsumo> datainsumos = new ArrayList<>();
            BigDecimal totalInsumo = BigDecimal.ZERO;
            for (ProcesoAdquisicionItem item : contrato.getItems()) {
                //La lista de insumos del Item se obtiene de la tabla de relación entre insumos e item de los procesos de adquisición
                for (RelacionProAdqItemInsumo relInsumo : item.getRelItemInsumos()) {
                    ProcesoAdquisicionInsumo insumo = relInsumo.getInsumo();
                    DataInsumo dataInsumo = new DataInsumo();
                    dataInsumo.setInsumoId(insumo.getInsumo().getId());
                    dataInsumo.setCantidad(insumo.getCantidadAdjudicada());
                    dataInsumo.setCodigoUACI(String.valueOf(insumo.getInsumo().getObjetoEspecificoGasto().getClasificador()));
                    dataInsumo.setDescripcionInsumo("");
                    if (insumo.getPoInsumo() != null && insumo.getPoInsumo().getObservacion() != null) {
                        dataInsumo.setDescripcionInsumo(insumo.getPoInsumo().getObservacion());
                    }
                    dataInsumo.setNombreUT(insumo.getUnidadTecnica().getNombre());
                    BigDecimal monteExterno = BigDecimal.ZERO;
                    BigDecimal financiacionGOES = BigDecimal.ZERO;
                    dataInsumo.setFinanciacionGOES(NumberUtils.nomberToString(BigDecimal.ZERO));
                    for (POMontoFuenteInsumo fuenteMonto : insumo.getPoInsumo().getMontosFuentes()) {
                        if (InsumoUtils.esMontoDeGOES(fuenteMonto)) {
                            dataInsumo.setFinanciacionGOES(NumberUtils.nomberToString(fuenteMonto.getMonto()));
                            financiacionGOES = fuenteMonto.getMonto();
                        } else {
                            monteExterno = monteExterno.add(fuenteMonto.getMonto());
                        }

                    }
                    dataInsumo.setFinanciacionExt(NumberUtils.nomberToString(monteExterno));

                    BigDecimal total = null;
                    if (dataInsumo.getFinanciacionGOES() != null) {
                        total = monteExterno.add(financiacionGOES);
                    } else {
                        total = monteExterno;
                    }

                    Integer idPoInsumo = insumo.getPoInsumo().getId();
                    String codigoProgramaACANP = insumoBean.getCodigoProgramaACoANPDePoInsumo(idPoInsumo)[0] != null ? insumoBean.getCodigoProgramaACoANPDePoInsumo(idPoInsumo)[0] : "";
                    String codigoSubprograma = insumoBean.getCodigoSubprogramaDePoInsumo(idPoInsumo)[0] != null ? insumoBean.getCodigoSubprogramaDePoInsumo(idPoInsumo)[0] : "";
                    String codigoProyecto = insumoBean.getCodigoProyectoDePoInsumo(idPoInsumo)[0] != null ? insumoBean.getCodigoProyectoDePoInsumo(idPoInsumo)[0] : "";

                    dataInsumo.setCodigoProgramaACANP(codigoProgramaACANP);
                    dataInsumo.setCodigoSubprograma(codigoSubprograma);
                    dataInsumo.setCodigoProyecto(codigoProyecto);

                    dataInsumo.setTotal(NumberUtils.nomberToString(total));
                    totalInsumo = totalInsumo.add(total);
                    datainsumos.add(dataInsumo);
                }
            }

            String areaReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_AREA_ANEXO_DOCUMENTO_COMPRA);
            String tituloReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_TITULO_ANEXO_DOCUMENTO_COMPRA);

            DataReportesContratoOC dataReportAnexoDocCompra = (DataReportesContratoOC) this.inicializarReporteTemplate(tituloReporte, areaReporte, DataReportesContratoOC.class);
            dataReportAnexoDocCompra.setInsumos(datainsumos);
            ProcesoAdquisicionItem item = contrato.getItems().get(0);
            ProcesoAdquisicionProveedor adquisicionProveedor = item.getProveedorOfertaAdjudicadaId().getProcesoAdquisicionProveedor();

            String nombrePrograma = "";
            if (item.getProcesoAdquisicion() != null && !item.getProcesoAdquisicion().getInsumos().isEmpty()) {
                GeneralPOA poa = item.getProcesoAdquisicion().getInsumos().get(0).getPoInsumo().getPoa();
                if (poa.getTipo() == TipoPOA.POA_PROYECTO) {
                    POAProyecto poap = (POAProyecto) poa;
                    if (poap.getProyecto().getProgramaPresupuestario() != null) {
                        nombrePrograma = poap.getProyecto().getProgramaPresupuestario().getNombre();
                    }
                }
            }
            dataReportAnexoDocCompra.setNombrePrograma(nombrePrograma);
            dataReportAnexoDocCompra.setNombreProveedor(adquisicionProveedor.getProveedor().getNombreComercial());
            dataReportAnexoDocCompra.setProveedorNIT(adquisicionProveedor.getProveedor().getNitOferente());

            BigDecimal compraTotal = totalInsumo;

            dataReportAnexoDocCompra.setTotalCompra(NumberUtils.nomberToString(compraTotal));
            ClassLoader classLoader = getClass().getClassLoader();
            URL jasperFileNameURL = classLoader.getResource("reportes/AnexoDocumentoCompra.jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFileNameURL);

            Date fechaActual = new Date();
            int anio = fechaActual.getYear();

            dataReportAnexoDocCompra.setCifradoPresupuestario(anio + "3100-3-0000-22-0-61501");
            Map parcol = new HashMap();
            addLogoToMap(parcol);

            return ReportesUtils.generarBytesPDF(dataReportAnexoDocCompra, parcol, jasperReport);

        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    private String obtenerFuente(ContratoOC contrato) {
        Set<String> fuentes = new LinkedHashSet<>();

        for (ProcesoAdquisicionItem item : contrato.getItems()) {
            //La lista de insumos del Item se obtiene de la tabla de relación entre insumos e item de los procesos de adquisición
            for (RelacionProAdqItemInsumo rel : item.getRelItemInsumos()) {
                //se agregan las fuentes
                for (POMontoFuenteInsumo insumoF : rel.getInsumo().getPoInsumo().getMontosFuentes()) {
                    if (insumoF.getCertificadoDisponibilidadPresupuestariaAprobada() != null && insumoF.getCertificadoDisponibilidadPresupuestariaAprobada().booleanValue() == true) {
                        fuentes.add(insumoF.getFuente().getFuenteRecursos().getNombre());
                        insumoF.getMonto();//Monto que pusieron en el POA para ese fuente
                        insumoF.getCertificado();//Monto que le certificaron para la fuente (menor al anterior)

                    }
                }
                rel.getMontoTotalAdjudicado();//Costo del insumo real (TOTAL DE LA TABLA INSUMOS)
                rel.getCantidadAdjudicada();//Cantidad adjudicada para el insumo (Es lo que se termina comprando)
                rel.getInsumo().getUnidadTecnica().getNombre();//UT de la tabla de insumos
                rel.getInsumo().getInsumo().getCodigo();//Código del insumo del catálogo de insumos.
                rel.getInsumo().getInsumo().getObjetoEspecificoGasto().getClasificador();//Objeto específico de gasto
                rel.getInsumo().getObservacion();//Descripción del insumo del proceso de adquisición.

            }

        }

        return TextUtils.join(", ", fuentes);

    }

    private String obtenerGruposPacs(ContratoOC contrato) {

        Set<String> gruposPacs = new LinkedHashSet<>();
        for (ProcesoAdquisicionItem item : contrato.getItems()) {
            //La lista de insumos del Item se obtiene de la tabla de relación entre insumos e item de los procesos de adquisición
            for (RelacionProAdqItemInsumo rel : item.getRelItemInsumos()) {
                //se agregan las fuentes
                gruposPacs.add(rel.getInsumo().getPoInsumo().getPacGrupo().getId().toString());
            }
        }
        return TextUtils.join(", ", gruposPacs);

    }

    private String obtenerDescripcionActa(ActaContrato acta) {
        String respuesta = "";
        if (acta.getTipo().equals(TipoActaContrato.RECEPCION)) {
            for (RelActaItem actaItem : acta.getRelActaItem()) {
                if (respuesta.length() > 0) {
                    respuesta = respuesta + ", ";
                }
                respuesta = respuesta + actaItem.getDescripcion();
            }
        } else {
            respuesta = acta.getObservaciones();
        }
        return respuesta;
    }

    /**
     * Este método genera un documento de orden de compra
     *
     * @param contratoId
     * @return
     * @throws GeneralException
     */
    public byte[] generarDocumentoOrdenCompra(Integer contratoId) throws GeneralException {
        try {
            ContratoOC contrato = (ContratoOC) generalDAO.findById(ContratoOC.class, contratoId);
            List<DataItem> dataItems = new ArrayList<>();
            ProcesoAdquisicionProveedor adquisicionProveedor = null;
            BigDecimal totalPrecio = BigDecimal.ZERO;

            Set<String> fuentes = new LinkedHashSet<>();

            for (ProcesoAdquisicionItem item : contrato.getItems()) {
                DataItem dataItem = new DataItem();
                //La lista de insumos del Item se obtiene de la tabla de relación entre insumos e item de los procesos de adquisición
                BigDecimal precioUnitario = item.getProveedorOfertaAdjudicadaId().getPrecioUnitOferta();
                Integer cantidadItem = 0;
                BigDecimal montoTotal = BigDecimal.ZERO;
                for (RelacionProAdqItemInsumo rel : item.getRelItemInsumos()) {
                    cantidadItem += rel.getCantidadAdjudicada();
                    montoTotal = montoTotal.add(rel.getMontoTotalAdjudicado());

                    //se agregan las fuentes
                    for (POMontoFuenteInsumo insumoF : rel.getInsumo().getPoInsumo().getMontosFuentes()) {
                        if (insumoF.getCertificadoDisponibilidadPresupuestariaAprobada() != null && insumoF.getCertificadoDisponibilidadPresupuestariaAprobada().booleanValue() == true) {
                            fuentes.add(insumoF.getFuente().getFuenteRecursos().getNombre());
                        }
                    }

                }

                dataItem.setPrecio(ReportesUtils.getNumber(precioUnitario));
                dataItem.setTotal(ReportesUtils.getNumber(montoTotal));
                dataItem.setCantidad(cantidadItem);
                dataItem.setNroItem(item.getNroItem());

                dataItem.setDescripcion(item.getNombre());

                dataItems.add(dataItem);
                adquisicionProveedor = item.getProveedorOfertaAdjudicadaId().getProcesoAdquisicionProveedor();
                totalPrecio = totalPrecio.add(montoTotal);
            }

            String titualrFuentes = TextUtils.join(", ", fuentes);

            String areaReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_AREA_DOCUMENTO_ORDEN_COMPRA);
            String tituloReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_TITULO_DOCUMENTO_ORDEN_COMPRA);

            DataReportesContratoOC dataReportesContratoOC = (DataReportesContratoOC) this.inicializarReporteTemplate(tituloReporte, areaReporte, DataReportesContratoOC.class);
            dataReportesContratoOC.setFuentes(titualrFuentes);
            SimpleDateFormat dfEmision = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new Locale("es"));
            dataReportesContratoOC.setFechaEmisionStr(dfEmision.format(new Date()));
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            if (contrato.getFechaFin() != null) {
                dataReportesContratoOC.setFechaFin(df.format(contrato.getFechaFin()));
            } else {
                dataReportesContratoOC.setFechaFin("");
            }
            if (contrato.getFechaInicio() != null) {
                dataReportesContratoOC.setFechaInicio(df.format(contrato.getFechaInicio()));
            } else {
                dataReportesContratoOC.setFechaInicio("");
            }

            setRepresentanteOrdenDeCompra(dataReportesContratoOC);

            dataReportesContratoOC.setItems(dataItems);

            dataReportesContratoOC.setNombreProveedor(adquisicionProveedor.getProveedor().getNombreComercial());
            if (adquisicionProveedor.getProveedor().getNitOferente() != null) {
                dataReportesContratoOC.setProveedorNIT(adquisicionProveedor.getProveedor().getNitOferente());
            } else {
                dataReportesContratoOC.setProveedorNIT("");
            }

            dataReportesContratoOC.setNroContrato("No.: " + contratoId);
            if (contrato.getObservaciones() != null) {
                dataReportesContratoOC.setObservaciones(contrato.getObservaciones());
            } else {
                dataReportesContratoOC.setObservaciones("");
            }

            String plazoDias = "0 días";
            if (contrato.getPlazoEntrega() != null && contrato.getPlazoEntrega().intValue() > 0) {
                plazoDias = contrato.getPlazoEntrega() + " días";
            }
            dataReportesContratoOC.setPlazoDias(plazoDias);
            dataReportesContratoOC.setSubTotal(ReportesUtils.getNumber(contrato.getMontoAdjudicado()));
            dataReportesContratoOC.setTotal(ReportesUtils.getNumber(contrato.getMontoAdjudicado()));
            NumeroALetra numeroALetra = new NumeroALetra();
            String totalLetras = numeroALetra.convertirMoneda(contrato.getMontoAdjudicado().toString(), true);
            dataReportesContratoOC.setTotalLetras(totalLetras);

            //si es factura o recibo
            boolean hayFactura = false;
            boolean hayrecibo = false;
            Iterator<Impuesto> iter = contrato.getImpuestos().iterator();
            while (!hayFactura && iter.hasNext()) {
                Impuesto i = iter.next();
                if (i.getTipoFactura() == TipoFactura.FACTURA) {
                    hayFactura = true;
                } else if (i.getTipoFactura() == TipoFactura.RECIBO) {
                    hayrecibo = true;
                }
            }
            if (hayFactura) {
                dataReportesContratoOC.setTextoFinal("Para efectos de cobro presentar Orden de Compra original, Acta de Recepción emitida por el MINED y copia de Factura de consumidor final a nombre de : MINISTERIO DE EDUCACIÓN /FONDO GENERAL");
            } else if (hayrecibo) {
                dataReportesContratoOC.setTextoFinal("Para efectos de cobro presentar Orden de Compra original, Acta de Recepción emitida por el MINED y RECIBO a nombre de : MINISTERIO DE EDUCACIÓN /FONDO GENERAL");
            }

            ClassLoader classLoader = getClass().getClassLoader();
            URL jasperFileNameURL = classLoader.getResource("reportes/ordenDeCompra.jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFileNameURL);

            Map parcol = new HashMap();
            addLogoToMap(parcol);

            byte[] pdfbytes = ReportesUtils.generarBytesPDF(dataReportesContratoOC, parcol, jasperReport);

            return pdfbytes;

        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }

    }

    /**
     * Este método genera las invitaciones a proveedores para un proceso de
     * adquisición
     *
     * @param procesoId
     * @param observaciones
     * @return
     * @throws GeneralException
     */
    public byte[] generarInvitacionesProveedores(Integer procesoId, String observaciones) throws GeneralException {
        try {

            ProcesoAdquisicion proceso = (ProcesoAdquisicion) generalDAO.findById(ProcesoAdquisicion.class, procesoId);

            proceso.setObservaciones(observaciones);

            String autorizadoMined = getAutorizadoMINED();

            String textoInvitacion = getTextoInvitacionProveedor();

            List<ProcesoAdquisicionItem> items = pAdqDAO.obtenerItemsDelProceso(procesoId);
            List<DataItem> dataItems = new ArrayList<>();

            Set<String> fuentes = new LinkedHashSet<>();

            for (ProcesoAdquisicionItem item : items) {
                DataItem dataItem = new DataItem();
                Integer cantidad = 0;
                //La lista de insumos del Item se obtiene de la tabla de relación entre insumos e item de los procesos de adquisición
                for (RelacionProAdqItemInsumo rel : item.getRelItemInsumos()) {
                    ProcesoAdquisicionInsumo insumo = rel.getInsumo();
                    cantidad += insumo.getRelacionItemInsumoPorItem(item).getCantidad();
                    //se agregan las fuentes
                    for (POMontoFuenteInsumo insumoF : insumo.getPoInsumo().getMontosFuentes()) {
                        if (insumoF.getCertificadoDisponibilidadPresupuestariaAprobada() != null && insumoF.getCertificadoDisponibilidadPresupuestariaAprobada().booleanValue() == true) {
                            fuentes.add(insumoF.getFuente().getFuenteRecursos().getNombre());
                        }
                    }

                }

                dataItem.setCantidad(cantidad);
                dataItem.setDescripcion(item.getNombre());
                ProcesoAdquisicionLote lote = item.getLote();
                if (lote != null) {
                    dataItem.setLote(lote.getNombre());
                } else {
                    dataItem.setLote("");
                }
                dataItem.setNroItem(item.getNroItem());
                dataItems.add(dataItem);

            }

            String fechaLimiteRecepcion = "";
            String horaLimiteRecepción = "";

            GanttTask mayorTareaRecepcionOfertas = GanttUtils.obtenerTareaConMayorFechaFinPorTipoTarea(proceso.getGantt(), TipoTarea.RECEPCION_DE_OFERTAS);

            if (mayorTareaRecepcionOfertas != null && mayorTareaRecepcionOfertas.getEnd() != null) {
                fechaLimiteRecepcion = DatesUtils.dateToString(new Date(mayorTareaRecepcionOfertas.getEnd()));
                if (!TextUtils.isEmpty(mayorTareaRecepcionOfertas.getHoraFin())) {
                    horaLimiteRecepción = mayorTareaRecepcionOfertas.getHoraFin();
                }
            }

            String titualrFuentes = TextUtils.join(", ", fuentes);

            Integer nroProceso = proceso.getSecuenciaNumero();
            Integer contadorInvitacion = 1;

            String numeroProcesoAdq = "";
            if (proceso.getMetodoAdquisicion() != null) {
                numeroProcesoAdq = proceso.getMetodoAdquisicion().getNombre();
            }
            numeroProcesoAdq += "/" + proceso.getSecuenciaAnio() + "/" + proceso.getSecuenciaNumero();

            Boolean existenLotes = (proceso.getLotes() != null && !proceso.getLotes().isEmpty());

            List<DataProveedorItems> dataProveedoresItems = new ArrayList<>();
            for (ProcesoAdquisicionProveedor adquisiconProveedor : proceso.getProveedores()) {
                if (adquisiconProveedor.getInvitado()) {
                    String areaReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_AREA_INVITACION_PROVEEDRES);
                    String tituloReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_TITULO_INVITACION_PROVEEDRES);
                    DataProveedorItems dataProveedorItems = (DataProveedorItems) this.inicializarReporteTemplate(tituloReporte, areaReporte, DataProveedorItems.class);
                    dataProveedorItems.setFuentes(titualrFuentes);
                    dataProveedorItems.setProveedorNombre(adquisiconProveedor.getProveedor().getNombreComercial());
                    dataProveedorItems.setProveedorDir(ReportesUtils.getText(adquisiconProveedor.getProveedor().getDireccionFactura()));
                    dataProveedorItems.setProveedorFax(ReportesUtils.getText(adquisiconProveedor.getProveedor().getFax()));
                    dataProveedorItems.setProveedorTel(ReportesUtils.getText(adquisiconProveedor.getProveedor().getTelefonoRepresentante()));
                    dataProveedorItems.setProveedorMail(ReportesUtils.getText(adquisiconProveedor.getProveedor().getMail()));
                    dataProveedorItems.setTextoCabezal(textoInvitacion);
                    dataProveedorItems.setItems(dataItems);
                    dataProveedorItems.setAutorizadoMINED(autorizadoMined);
                    dataProveedorItems.setUsuarioProceso(proceso.getResponsable().getUsuPrimerNombre() + " " + proceso.getResponsable().getUsuPrimerApellido());
                    dataProveedorItems.setTelUsuarioProceso(ReportesUtils.getText(proceso.getResponsable().getUsuTelefono()));
                    dataProveedorItems.setNroInvitacion("INVITACIÓN No. " + nroProceso + " - " + contadorInvitacion);
                    dataProveedorItems.setObservaciones(ReportesUtils.getText(proceso.getObservaciones()));
                    dataProveedorItems.setFechaInvitacion(DatesUtils.dateToString(new Date()));
                    dataProveedorItems.setFechaLimiteRecepcion(fechaLimiteRecepcion);
                    dataProveedorItems.setHoraLimiteRecepción(horaLimiteRecepción);
                    dataProveedorItems.setNumeroProcesoAdq(numeroProcesoAdq);
                    dataProveedorItems.setExistenLotes(existenLotes);
                    dataProveedoresItems.add(dataProveedorItems);
                    contadorInvitacion++;
                }

            }

            ClassLoader classLoader = getClass().getClassLoader();
            URL jasperFileNameURL = classLoader.getResource("reportes/invitacionesProveedoresV2.jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFileNameURL);

            Map parcol = new HashMap();
            addLogoToMap(parcol);

            return ReportesUtils.generarBytesPDF(dataProveedoresItems, parcol, jasperReport);

        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }

    }

    /**
     * Este método genera el documento de reserva de fondos para un proceso de
     * adquisición
     *
     * @param procesoId
     * @return
     * @throws GeneralException
     */
    public byte[] generarDocumentoReservaDeFondos(Integer procesoId) throws GeneralException {
        try {
            Map<Integer, DataReporteProvedoor> mapaProveedores = new LinkedHashMap();

            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

            String areaReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_AREA_RESERVA_FONDOS);
            String tituloReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_TITULO_RESERVA_FONDOS);
            DataReporteReservaFondos dataReporte = (DataReporteReservaFondos) this.inicializarReporteTemplate(tituloReporte, areaReporte, DataReporteReservaFondos.class);
            dataReporte.setFecha("Fecha: " + df.format(new Date()));
            dataReporte.setProveedores(new LinkedList());
            dataReporte.setTotalAportesExterno(ReportesUtils.getBigDecimal(BigDecimal.ZERO));
            dataReporte.setTotalAportesGoes(ReportesUtils.getBigDecimal(BigDecimal.ZERO));
            dataReporte.setTotalProveedores(ReportesUtils.getBigDecimal(BigDecimal.ZERO));

            List<ProcesoAdquisicionItem> itemsProceso = pAdqDAO.obtenerItemsDelProcesoAdjudicados(procesoId);
            for (ProcesoAdquisicionItem itemAdj : itemsProceso) {
                if (itemAdj.getProveedorOfertaAdjudicadaId() != null && itemAdj.getProveedorOfertaAdjudicadaId().getProcesoAdquisicionProveedor() != null) {
                    Proveedor proveedor = itemAdj.getProveedorOfertaAdjudicadaId().getProcesoAdquisicionProveedor().getProveedor();
                    //se busca si ya no esta aniadido el proveedor
                    DataReporteProvedoor reporteProveedor = mapaProveedores.get(proveedor.getId());
                    if (reporteProveedor == null) {
                        reporteProveedor = new DataReporteProvedoor();
                        reporteProveedor.setInsumos(new LinkedList());
                        reporteProveedor.setNombrDelProveedor(proveedor.getNombreComercial());

                        dataReporte.getProveedores().add(reporteProveedor);
                        mapaProveedores.put(proveedor.getId(), reporteProveedor);
                    }
                    //La lista de insumos del Item se obtiene de la tabla de relación entre insumos e item de los procesos de adquisición
                    List<ProcesoAdquisicionInsumo> insumosDelItemAdj = itemAdj.getInsumosTemporalesDelItem();
                    for (ProcesoAdquisicionInsumo insumo : insumosDelItemAdj) {
                        DataReporteinsumoReservaFondos dataInsumo = new DataReporteinsumoReservaFondos();
                        dataInsumo.setIdInsumo(insumo.getInsumo().getId().toString());
                        dataInsumo.setIdItem(itemAdj.getId().toString());
                        dataInsumo.setNombreDelInsumo(insumo.getInsumo().getNombre());
                        dataInsumo.setDescripcionDelInsumo(ReportesUtils.getText(insumo.getObservacion()));
                        dataInsumo.setOeg(String.valueOf(insumo.getInsumo().getObjetoEspecificoGasto().getClasificador()));

                        dataInsumo.setPorctjBCO(ReportesUtils.getBigDecimal(BigDecimal.ZERO));
                        dataInsumo.setPorctjGOES(ReportesUtils.getBigDecimal(BigDecimal.ZERO));
                        dataInsumo.setAporteExterno(ReportesUtils.getBigDecimal(BigDecimal.ZERO));
                        dataInsumo.setAporteGOES(ReportesUtils.getBigDecimal(BigDecimal.ZERO));

                        BigDecimal montoTotalAdjudicado = insumo.getRelacionItemInsumoPorItem(itemAdj).getMontoTotalAdjudicado();
                        dataInsumo.setTotal(ReportesUtils.getBigDecimal(montoTotalAdjudicado));
                        String iva = "";
                        dataInsumo.setIva(iva);

                        reporteProveedor.getInsumos().add(dataInsumo);
                        dataReporte.setTotalAportesExterno(dataReporte.getTotalAportesExterno().add(dataInsumo.getAporteExterno()));
                        dataReporte.setTotalAportesGoes(dataReporte.getTotalAportesGoes().add(dataInsumo.getAporteGOES()));
                        dataReporte.setTotalProveedores(dataReporte.getTotalProveedores().add(dataInsumo.getTotal()));
                    }
                }

            }
            ClassLoader classLoader = getClass().getClassLoader();
            URL jasperFileNameURL = classLoader.getResource("reportes/reserva-de-fondos-nuevo.jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFileNameURL);

            Map parcol = new HashMap();
            addLogoToMap(parcol);
            parcol.put("SUBREPORT_DIR", "reportes/");

            return ReportesUtils.generarBytesPDF(dataReporte, parcol, jasperReport);

        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month - 1];
    }

    /**
     * Este método genera el reporte pac y lo guarda en un archivo
     *
     * @param idPAC
     * @return
     * @throws GeneralException
     */
    public Archivo generarReportePAC(Integer idPAC) throws GeneralException {
        try {
            PAC pac = (PAC) generalDAO.findById(PAC.class, idPAC);
            pac.initInsumos();

            String areaReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_AREA_PAC);
            String tituloReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_TITULO_PAC);
            DataReportePAC dataReportePAC = (DataReportePAC) this.inicializarReporteTemplate(tituloReporte, areaReporte, DataReportePAC.class);
            dataReportePAC.setInsumos(new LinkedList());

            dataReportePAC.setEntreFechas("DEL " + ReportesUtils.dateToString(pac.getFechaInicio()) + " AL " + ReportesUtils.dateToString(pac.getFechaFin()));
            dataReportePAC.setNombrePAC(pac.getNombre());
            dataReportePAC.setFechaActual(ReportesUtils.dateToStringCompleto(new Date()));
            dataReportePAC.setCifradoPresupuestario("cifrado??");
            dataReportePAC.setCodigoSegunUACI("Código según UACI: " + "codigoSegunUaci??");
            BigDecimal totalCompra = ReportesUtils.getBigDecimal(BigDecimal.ZERO);
            for (POInsumos insumo : pac.getInsumos()) {
                DataReportePACInsumo dataReportePACInsumo = procesarItemEnReportePac(insumo);
                totalCompra = totalCompra.add(insumo.getMontoTotal());
                dataReportePAC.getInsumos().add(dataReportePACInsumo);
            }
            dataReportePAC.setTotalCompra(totalCompra);

            Map parcol = new HashMap();
            addLogoToMap(parcol);

            ClassLoader classLoader = getClass().getClassLoader();
            URL jasperFileNameURL = classLoader.getResource("reportes/ReportePAC.jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFileNameURL);

            Archivo a = archivoBean.crearArchivo();
            a.setContentType(contentTypeAplicationPDF);
            String nombreOriginal = "ReportePAC_" + pac.getId() + ".pdf";
            a.setNombreOriginal(nombreOriginal);

            ReportesUtils.generarPDF(dataReportePAC, parcol, jasperReport, archivoBean.getFile(a));

            return (Archivo) generalDAO.update(a);

        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Método que se encarga de generar un reporte pac global para todo los pac
     * en el año
     *
     * @param anio
     * @return
     * @throws GeneralException
     */
    public byte[] generarReportePACGlobal(Integer anio) throws GeneralException {
        try {
            String areaReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_AREA_PAC_GLOBAL);
            String tituloReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_TITULO_PAC_GLOBAL);
            DataReportePAC dataReportePAC = (DataReportePAC) this.inicializarReporteTemplate(tituloReporte, areaReporte, DataReportePAC.class);
            dataReportePAC.setInsumos(new LinkedList());
            dataReportePAC.setNombrePAC("PAC GLOBAL");
            dataReportePAC.setFechaActual(ReportesUtils.dateToStringCompleto(new Date()));

            Date fechaDesde = null;
            Date fechaHasta = null;

            BigDecimal totalCompra = ReportesUtils.getBigDecimal(BigDecimal.ZERO);

            List<PAC> l = pacdao.getPACEnAnio(anio);
            for (PAC pac : l) {
                pac.initInsumos();

                if (fechaDesde == null) {
                    fechaDesde = pac.getFechaInicio();
                } else if (pac.getFechaInicio() != null && fechaDesde.compareTo(pac.getFechaInicio()) > 0) {
                    fechaDesde = pac.getFechaInicio();
                } else {
                    fechaDesde = new Date();
                }

                if (fechaHasta == null) {
                    fechaHasta = pac.getFechaFin();
                } else if (pac.getFechaFin() != null && fechaHasta.compareTo(pac.getFechaFin()) < 0) {
                    fechaHasta = pac.getFechaFin();
                } else {
                    fechaHasta = null;
                }

                for (POInsumos insumo : pac.getInsumos()) {
                    DataReportePACInsumo dataReportePACInsumo = procesarItemEnReportePac(insumo);
                    totalCompra = totalCompra.add(insumo.getMontoTotal());
                    dataReportePAC.getInsumos().add(dataReportePACInsumo);
                }
            }

            dataReportePAC.setEntreFechas("DEL " + ReportesUtils.dateToString(fechaDesde) + " AL " + ReportesUtils.dateToString(fechaHasta));
            dataReportePAC.setTotalCompra(totalCompra);

            Map parcol = new HashMap();
            addLogoToMap(parcol);

            ClassLoader classLoader = getClass().getClassLoader();
            URL jasperFileNameURL = classLoader.getResource("reportes/ReportePAC.jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFileNameURL);

            return ReportesUtils.generarBytesPDF(dataReportePAC, parcol, jasperReport);

        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    private DataReportePACInsumo procesarItemEnReportePac(POInsumos insumo) {
        DataReportePACInsumo dataReportePACInsumo = new DataReportePACInsumo();
        dataReportePACInsumo.setNroProceso(ReportesUtils.getIdProceso(insumo));
        dataReportePACInsumo.setNombreInsumo(insumo.getId() + " " + insumo.getInsumo().getNombre());
        String nombreMetodoAdq = "";
        if (insumo.getPacGrupo() != null && insumo.getPacGrupo().getMetodoAdquisicion() != null) {
            nombreMetodoAdq = insumo.getPacGrupo().getMetodoAdquisicion().getNombre();
        }
        dataReportePACInsumo.setNombreMetodoAdquisicion(nombreMetodoAdq);
        dataReportePACInsumo.setMontoPresupuestado(ReportesUtils.getBigDecimal(insumo.getMontoTotal()));

        Gantt gantt = null;
        if (insumo.getPacGrupo() != null) {
            gantt = insumo.getPacGrupo().getGantt();
        }
        dataReportePACInsumo.setFechaRecepcionDeTDRoET(ReportesUtils.dateToString(ReportesUtils.getFechaTareaGantt(gantt, TipoTarea.RECEPCION_DE_TDR_O_ESPECIFICACIONES_TECNICAS)));
        dataReportePACInsumo.setFechaRevisionDeTDRoET(ReportesUtils.dateToString(ReportesUtils.getFechaTareaGantt(gantt, TipoTarea.REVISION_DE_TDR_O_ET)));
        dataReportePACInsumo.setFechaInvitacionOPublicacion(ReportesUtils.dateToString(ReportesUtils.getFechaTareaGantt(gantt, TipoTarea.INVITACION_O_PUBLICACION)));
        dataReportePACInsumo.setFechaRecepcionDeOfertas(ReportesUtils.dateToString(ReportesUtils.getFechaTareaGantt(gantt, TipoTarea.RECEPCION_DE_OFERTAS)));
        dataReportePACInsumo.setFechaEvaluacion(ReportesUtils.dateToString(ReportesUtils.getFechaTareaGantt(gantt, TipoTarea.EVALUACION)));
        dataReportePACInsumo.setFechaAdjudicacion(ReportesUtils.dateToString(ReportesUtils.getFechaTareaGantt(gantt, TipoTarea.ADJUDICACION)));
        dataReportePACInsumo.setFechaContratacion(ReportesUtils.dateToString(ReportesUtils.getFechaTareaGantt(gantt, TipoTarea.CONTRATACION)));
        dataReportePACInsumo.setFechaContratado(ReportesUtils.dateToString(ReportesUtils.getFechaTareaGantt(gantt, TipoTarea.CONTRATADO)));
        dataReportePACInsumo.setFechaOrdenDeInicio(ReportesUtils.dateToString(ReportesUtils.getFechaTareaGantt(gantt, TipoTarea.ORDEN_DE_INICIO)));

        return dataReportePACInsumo;
    }

    /**
     * Este método genera el reporte PEP para un pac y lo guarda en un archivo
     *
     * @param idPAC
     * @return
     * @throws GeneralException
     * @deprecated
     */
    @Deprecated
    public Archivo generarReportePEP(Integer idPAC) throws GeneralException {
        try {
            PAC pac = (PAC) generalDAO.findById(PAC.class, idPAC);
            pac.initInsumos();

            DataReportePEP dataReportePEP = new DataReportePEP();
            dataReportePEP.setFilas(new LinkedList());
            dataReportePEP.setFecha(ReportesUtils.dateToStringCompleto(new Date()));
            dataReportePEP.setAnio("Ejercicio Año fiscal: " + pac.getAnio());

            DataFilaReportePEP rubro = null;
            DataFilaReportePEP cuenta = null;
            DataFilaReportePEP oeg = null;

            POInsumos insumoAnterior = null;

            Map parcol = new HashMap();
            addLogoToMap(parcol);

            ClassLoader classLoader = getClass().getClassLoader();
            URL jasperFileNameURL = classLoader.getResource("reportes/reportePEP.jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFileNameURL);

            Archivo a = archivoBean.crearArchivo();
            a.setContentType("application/pdf");
            a.setNombreOriginal("ReportePEP.pdf");

            ReportesUtils.generarPDF(dataReportePEP, parcol, jasperReport, archivoBean.getFile(a));

            a = (Archivo) generalDAO.update(a);

            return a;

        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método genera el reporte PEP de todo el Sistema para un año
     *
     * @return
     * @throws GeneralException
     */
    public byte[] generarReportePEPGlobal(Integer idAnioFiscal) throws GeneralException {
        try {
            Set<ObjetoEspecificoGasto> oegs = new HashSet();
            oegs.addAll(poaDAO.getOEGsParaPEPEnConMontoPorAnio(idAnioFiscal));
            oegs.addAll(poaDAO.getOEGsParaPEPEnProyectos(idAnioFiscal));

            //se agrupan los OEG
            HashMap<ObjetoEspecificoGasto, Set<ObjetoEspecificoGasto>> mapOEG = new HashMap();
            for (ObjetoEspecificoGasto oeg : oegs) {
                List<Integer> usados = new LinkedList();

                //se queda con el nivel 0 del OEG del insumo
                ObjetoEspecificoGasto nivel0 = oeg;
                usados.add(nivel0.getClasificador());

                while (nivel0.getPadre() != null && !usados.contains(nivel0.getPadre().getClasificador())) {
                    nivel0 = nivel0.getPadre();
                    usados.add(nivel0.getClasificador());
                }

                if (!mapOEG.containsKey(nivel0)) {
                    Set<ObjetoEspecificoGasto> list = new HashSet<>();
                    list.add(oeg);
                    mapOEG.put(nivel0, list);
                } else {
                    mapOEG.get(nivel0).add(oeg);
                }
            }

            AnioFiscal anio = (AnioFiscal) generalDAO.findById(AnioFiscal.class, idAnioFiscal);

            String areaReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_AREA_PEP_GLOBAL);
            String tituloReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_TITULO_PEP_GLOBAL);
            tituloReporte += " " + anio.getAnio().toString();
            DataReportePEPGlobal dataReportePEP = (DataReportePEPGlobal) this.inicializarReporteTemplate(tituloReporte, areaReporte, DataReportePEPGlobal.class);
            dataReportePEP.setFecha(ReportesUtils.dateToStringCompleto(new Date()));
            dataReportePEP.setAnio(anio.getAnio().toString());
            dataReportePEP.setFilas(new LinkedList<DataFilaGlobalReportePEP>());

            DataFilaGlobalReportePEP totalGlobal = new DataFilaGlobalReportePEP();
            totalGlobal.setNombre("Total ");
            totalGlobal.setColorFila(2);

            //se recorren los niveles de og y se va totalizando
            for (ObjetoEspecificoGasto nivel0 : mapOEG.keySet()) {
                Set<ObjetoEspecificoGasto> oegsEnNivel = mapOEG.get(nivel0);
                DataFilaGlobalReportePEP totalNivel0 = new DataFilaGlobalReportePEP();
                totalNivel0.setNombre("Total " + nivel0.getClasificador().toString());
                totalNivel0.setColorFila(1);

                //al nivel 0 se le suma todos insumos que son solo de ese nivel
                List<Object[]> fcmEnNivel0 = (poaDAO.getFCMEnOEGParaPEP(anio.getId(), anio.getAnio(), nivel0.getClasificador()));

                totalNivel0.sumarFCM(fcmEnNivel0);
                totalGlobal.sumarFCM(fcmEnNivel0);

                //para cada nivel de oeg suma el fcm para todos los insumos
                for (ObjetoEspecificoGasto oeg : oegsEnNivel) {
                    DataFilaGlobalReportePEP totalOEG = new DataFilaGlobalReportePEP();
                    totalOEG.setNombre(oeg.getClasificador().toString());
                    totalOEG.setColorFila(0);

                    List<Object[]> fcmEnNivel = (poaDAO.getFCMEnOEGParaPEP(anio.getId(), anio.getAnio(), oeg.getClasificador()));

                    totalOEG.sumarFCM(fcmEnNivel);
                    totalNivel0.sumarFCM(fcmEnNivel);
                    totalGlobal.sumarFCM(fcmEnNivel);

                    dataReportePEP.getFilas().add(totalOEG);
                }
                dataReportePEP.getFilas().add(totalNivel0);

            }
            dataReportePEP.getFilas().add(totalGlobal);

            Map parcol = new HashMap();
            addLogoToMap(parcol);

            ClassLoader classLoader = getClass().getClassLoader();
            URL jasperFileNameURL = classLoader.getResource("reportes/reportePEPGlobal.jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFileNameURL);

            return ReportesUtils.generarBytesPDF(dataReportePEP, parcol, jasperReport);

        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método genera el reporte PEP de todo el Sistema para un año
     *
     * @return
     * @throws GeneralException
     */
    public byte[] generarReportePEPGlobal(Integer idAnioFiscal, Integer idProyecto) throws GeneralException {
        try {
            Proyecto proy = generalBean.getProyecto(idProyecto);
            Set<ObjetoEspecificoGasto> oegs = new HashSet();
            oegs.addAll(poaDAO.getOEGsParaPEPEnConMontoPorAnio(idAnioFiscal, idProyecto));
            oegs.addAll(poaDAO.getOEGsParaPEPEnProyectos(idAnioFiscal, idProyecto));

            //se agrupan los OEG
            HashMap<ObjetoEspecificoGasto, Set<ObjetoEspecificoGasto>> mapOEG = new HashMap();
            for (ObjetoEspecificoGasto oeg : oegs) {
                List<Integer> usados = new LinkedList();

                //se queda con el nivel 0 del OEG del insumo
                ObjetoEspecificoGasto nivel0 = oeg;
                usados.add(nivel0.getClasificador());

                while (nivel0.getPadre() != null && !usados.contains(nivel0.getPadre().getClasificador())) {
                    nivel0 = nivel0.getPadre();
                    usados.add(nivel0.getClasificador());
                }

                if (!mapOEG.containsKey(nivel0)) {
                    Set<ObjetoEspecificoGasto> list = new HashSet<>();
                    list.add(oeg);
                    mapOEG.put(nivel0, list);
                } else {
                    mapOEG.get(nivel0).add(oeg);
                }
            }

            AnioFiscal anio = (AnioFiscal) generalDAO.findById(AnioFiscal.class, idAnioFiscal);

            String areaReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_AREA_PEP_GLOBAL);
            String tituloReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_TITULO_PEP_POA);
            if(proy != null) {
                tituloReporte = tituloReporte.trim() + " " + proy.getNombre().trim();
            }
            
            //tituloReporte += " " + anio.getAnio().toString();
            DataReportePEPGlobal dataReportePEP = (DataReportePEPGlobal) this.inicializarReporteTemplate(tituloReporte, areaReporte, DataReportePEPGlobal.class);
            dataReportePEP.setFecha(ReportesUtils.dateToStringCompleto(new Date()));
            dataReportePEP.setAnio(anio.getAnio().toString());
            dataReportePEP.setFilas(new LinkedList<DataFilaGlobalReportePEP>());

            DataFilaGlobalReportePEP totalGlobal = new DataFilaGlobalReportePEP();
            totalGlobal.setNombre("Total ");
            totalGlobal.setColorFila(2);

            //se recorren los niveles de og y se va totalizando
            for (ObjetoEspecificoGasto nivel0 : mapOEG.keySet()) {
                Set<ObjetoEspecificoGasto> oegsEnNivel = mapOEG.get(nivel0);
                DataFilaGlobalReportePEP totalNivel0 = new DataFilaGlobalReportePEP();
                totalNivel0.setNombre("Total " + nivel0.getClasificador().toString());
                totalNivel0.setColorFila(1);

                //al nivel 0 se le suma todos insumos que son solo de ese nivel
                List<Object[]> fcmEnNivel0 = (poaDAO.getFCMEnOEGParaPEP(anio.getId(), anio.getAnio(), nivel0.getClasificador()));

                totalNivel0.sumarFCM(fcmEnNivel0);
                totalGlobal.sumarFCM(fcmEnNivel0);

                //para cada nivel de oeg suma el fcm para todos los insumos
                for (ObjetoEspecificoGasto oeg : oegsEnNivel) {
                    DataFilaGlobalReportePEP totalOEG = new DataFilaGlobalReportePEP();
                    totalOEG.setNombre(oeg.getClasificador().toString());
                    totalOEG.setColorFila(0);

                    List<Object[]> fcmEnNivel = (poaDAO.getFCMEnOEGParaPEP(anio.getId(), anio.getAnio(), oeg.getClasificador()));

                    totalOEG.sumarFCM(fcmEnNivel);
                    totalNivel0.sumarFCM(fcmEnNivel);
                    totalGlobal.sumarFCM(fcmEnNivel);

                    dataReportePEP.getFilas().add(totalOEG);
                }
                dataReportePEP.getFilas().add(totalNivel0);

            }
            dataReportePEP.getFilas().add(totalGlobal);

            Map parcol = new HashMap();
            addLogoToMap(parcol);

            ClassLoader classLoader = getClass().getClassLoader();
            URL jasperFileNameURL = classLoader.getResource("reportes/reportePEPGlobal.jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFileNameURL);

            return ReportesUtils.generarBytesPDF(dataReportePEP, parcol, jasperReport);

        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    /**
     * Este método genera un acta de recepción
     *
     * @param pagoId
     * @return
     * @throws GeneralException
     */
    public byte[] generarActaDeRecepcion(Integer pagoId) throws GeneralException {
        try {
            ActaContrato pago = (ActaContrato) generalDAO.findById(ActaContrato.class, pagoId);
            Proveedor proveedor = pago.getContratoOC().getItems().get(0).getProveedorOfertaAdjudicadaId().getProcesoAdquisicionProveedor().getProveedor();

            String areaReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_AREA_ACTA_RECEPCION);
            String tituloReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_TITULO_ACTA_RECEPCION);

            tituloReporte += " " + pago.getContratoOC().getId() + "-" + pago.getNroActa();
            DataReporteActaDeRecepcion reporte = (DataReporteActaDeRecepcion) this.inicializarReporteTemplate(tituloReporte, areaReporte, DataReporteActaDeRecepcion.class);
            Set<String> fuentes = new LinkedHashSet<>();
            for (PagoInsumo pagoInsumo : pago.getPagosInsumo()) {
                RelacionProAdqItemInsumo rel = pagoInsumo.getRelacionItemInsumo();
                //se agregan las fuentes
                for (POMontoFuenteInsumo insumoF : rel.getInsumo().getPoInsumo().getMontosFuentes()) {
                    if (insumoF.getCertificadoDisponibilidadPresupuestariaAprobada() != null && insumoF.getCertificadoDisponibilidadPresupuestariaAprobada().booleanValue() == true) {
                        fuentes.add(insumoF.getFuente().getFuenteRecursos().getNombre());
                    }
                }

            }
            String titualrFuentes = TextUtils.join(", ", fuentes);

            reporte.setFuentes(titualrFuentes);
            reporte.setFechaDeRecepcion(ReportesUtils.dateToString(pago.getFechaGeneracion()));
            reporte.setHoraDeRecepcion(ReportesUtils.convertToHora(pago.getFechaGeneracion()));
            reporte.setNitProveedor(ReportesUtils.getText(proveedor.getNitOferente()));
            reporte.setFechaDeContrato(ReportesUtils.dateToString(pago.getContratoOC().getFechaEmision()));
            reporte.setLugarDeRecepcion(pago.getLugarRecepcion());
            reporte.setProveedor(proveedor.getNombreComercial());
            reporte.setEstadoActa(EstadoActa.traducirEstado(pago.getEstado()));
            reporte.setNroContrato(pago.getContratoOC().getId() + "-" + pago.getNroActa());

            reporte.setItems(new LinkedList());
            BigDecimal total = BigDecimal.ZERO;

            for (RelActaItem relActaItem : pago.getRelActaItem()) {
                BigDecimal pagadoItem = relActaItem.getImporte();
                if (pagadoItem != null) {
                    DataReporteActaDeRecepcionItem dataPagoItem = new DataReporteActaDeRecepcionItem();
                    dataPagoItem.setDescripcion(relActaItem.getDescripcion());
                    dataPagoItem.setMonto("US$ " + NumberUtils.nomberToString(pagadoItem));
                    BigDecimal montoUnitItem = relActaItem.getItem().getRelItemInsumos().get(0).getMontoUnitAdjudicado();
                    dataPagoItem.setPrecioUnit("US$ " + NumberUtils.nomberToString(montoUnitItem));
                    dataPagoItem.setCantidad(relActaItem.getCantRecibida().toString());
                    total = total.add(pagadoItem);
                    reporte.getItems().add(dataPagoItem);
                }
            }

            reporte.setTotal("US$ " + NumberUtils.nomberToString(total));
            NumeroALetra numeroALetra = new NumeroALetra();
            String totalLetras = numeroALetra.convertirMoneda(total.toString(), false);
            reporte.setTotalEnLetras(totalLetras);

            reporte.setNombreAdminContrato(ReportesUtils.getNombreUsuario(pago.getContratoOC().getAdministrador()) + "\n"
                    + "ADMINISTRADOR(A) DE CONTRATO" + "\n"
                    + "GERENCIAS DE TECNOLOGIAS EDUCATIVAS");
            ClassLoader classLoader = getClass().getClassLoader();
            URL jasperFileNameURL = classLoader.getResource("reportes/ActaDeRecepcion.jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFileNameURL);

            Map parcol = new HashMap();
            addLogoToMap(parcol);

            return ReportesUtils.generarBytesPDF(reporte, parcol, jasperReport);

        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }

    }

    /**
     * Este método genera un acta de anticipo para un pago de un contrato
     *
     * @param pagoId
     * @return
     * @throws GeneralException
     */
    public byte[] generarActaDeAnticipo(Integer pagoId) throws GeneralException {
        try {
            ActaContrato acta = (ActaContrato) generalDAO.findById(ActaContrato.class, pagoId);
            Proveedor proveedor = acta.getContratoOC().getItems().get(0).getProveedorOfertaAdjudicadaId().getProcesoAdquisicionProveedor().getProveedor();

            String areaReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_AREA_ACTA_ANTICIPO);
            String tituloReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_TITULO_ACTA_ANTICIPO);

            tituloReporte += " " + acta.getContratoOC().getId() + "-" + acta.getNroActa();
            DataReporteActaDeRecepcion reporte = (DataReporteActaDeRecepcion) this.inicializarReporteTemplate(tituloReporte, areaReporte, DataReporteActaDeRecepcion.class);

            String codigoProyecto = "";
            String nombreProyecto = "";

            ContratoOC contrato = acta.getContratoOC();
            ProcesoAdquisicionItem item = contrato.getItems().get(0);
            POInsumos poInsumo = item.getRelItemInsumos().get(0).getInsumo().getPoInsumo();
            GeneralPOA generalPOA = poInsumo.getPoa();
            if (generalPOA.getTipo().equals(TipoPOA.POA_PROYECTO)) {
                POAProyecto poaProy = (POAProyecto) generalPOA;
                nombreProyecto = poaProy.getProyecto().getNombre();
                codigoProyecto = poaProy.getProyecto().getCodigoSIIP();
            } else if (generalPOA.getTipo().equals(TipoPOA.POA_CON_ACTIVIDADES)) {
                POAConActividades poaCA = (POAConActividades) generalPOA;
                nombreProyecto = poaCA.getConMontoPorAnio().getNombre();
            }

            reporte.setNombreProyecto(nombreProyecto.toUpperCase());

            Set<String> fuentes = new LinkedHashSet<>();
            for (RelacionProAdqItemInsumo rel : item.getRelItemInsumos()) {
                //se agregan las fuentes
                for (POMontoFuenteInsumo insumoF : rel.getInsumo().getPoInsumo().getMontosFuentes()) {
                    if (insumoF.getCertificadoDisponibilidadPresupuestariaAprobada() != null && insumoF.getCertificadoDisponibilidadPresupuestariaAprobada().booleanValue() == true) {
                        fuentes.add(insumoF.getFuente().getFuenteRecursos().getNombre());
                    }
                }

            }
            String titualrFuentes = TextUtils.join(", ", fuentes);
            if (codigoProyecto != null && !codigoProyecto.isEmpty()) {
                titualrFuentes += " / " + codigoProyecto;
            }
            reporte.setFuentes(titualrFuentes.toUpperCase());

            reporte.setNroActa(acta.getContratoOC().getId() + "-" + acta.getNroActa());
            reporte.setEstadoActa(EstadoActa.traducirEstado(acta.getEstado()));

            reporte.setNroContrato(acta.getContratoOC().getId() + "-" + acta.getNroActa());
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            reporte.setFechaEmisionStr(df.format(acta.getFechaGeneracion()));
            reporte.setProveedor(proveedor.getNombreComercial());
            reporte.setNitProveedor(ReportesUtils.getText(proveedor.getNitOferente()));

            String montoContrato = "US$ " + NumberUtils.nomberToString(contrato.getMontoAdjudicado());
            reporte.setMontoContrato(montoContrato);

            String procesoCompra = contrato.getProcesoAdquisicion().getSecuenciaAnio() + "/" + contrato.getProcesoAdquisicion().getSecuenciaNumero();
            reporte.setProcesoCompra(procesoCompra);

            reporte.setItems(new LinkedList());
            DataReporteActaDeRecepcionItem dataPagoItem = new DataReporteActaDeRecepcionItem();
            String descripcion = acta.getObservaciones() != null ? acta.getObservaciones() : "";
            dataPagoItem.setDescripcion(descripcion);
            String montoTotal = "US$ " + NumberUtils.nomberToString(acta.getMontoRecibido());
            dataPagoItem.setMonto(montoTotal);//El monto es el total porque siempre va a haber uno solo
            reporte.getItems().add(dataPagoItem);

            String total = NumberUtils.nomberToString(acta.getMontoRecibido());
            reporte.setTotal("US$ " + total);
            NumeroALetra numeroALetra = new NumeroALetra();
            String totalLetras = numeroALetra.convertirMoneda(total.toString(), false);
            reporte.setTotalEnLetras(totalLetras);

            ClassLoader classLoader = getClass().getClassLoader();
            URL jasperFileNameURL = classLoader.getResource("reportes/ActaDeAnticipo.jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFileNameURL);

            Map parcol = new HashMap();
            addLogoToMap(parcol);

            return ReportesUtils.generarBytesPDF(reporte, parcol, jasperReport);

        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }

    }

    /**
     * Este método genera un acta de devolución para un acta de un contrato
     *
     * @param pagoId
     * @return
     * @throws GeneralException
     */
    public byte[] generarActaDeDevolucion(Integer pagoId) throws GeneralException {
        try {
            ActaContrato acta = (ActaContrato) generalDAO.findById(ActaContrato.class, pagoId);
            Proveedor proveedor = acta.getContratoOC().getItems().get(0).getProveedorOfertaAdjudicadaId().getProcesoAdquisicionProveedor().getProveedor();

            String areaReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_AREA_ACTA_DEVOLUCION);
            String tituloReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_TITULO_ACTA_DEVOLUCION);

            tituloReporte += " " + acta.getContratoOC().getId() + "-" + acta.getNroActa();
            DataReporteActaDeRecepcion reporte = (DataReporteActaDeRecepcion) this.inicializarReporteTemplate(tituloReporte, areaReporte, DataReporteActaDeRecepcion.class);

            String codigoProyecto = "";
            String nombreProyecto = "";

            ContratoOC contrato = acta.getContratoOC();
            ProcesoAdquisicionItem item = contrato.getItems().get(0);
            POInsumos poInsumo = item.getRelItemInsumos().get(0).getInsumo().getPoInsumo();
            GeneralPOA generalPOA = poInsumo.getPoa();
            if (generalPOA.getTipo().equals(TipoPOA.POA_PROYECTO)) {
                POAProyecto poaProy = (POAProyecto) generalPOA;
                nombreProyecto = poaProy.getProyecto().getNombre();
                codigoProyecto = poaProy.getProyecto().getCodigoSIIP();
            } else if (generalPOA.getTipo().equals(TipoPOA.POA_CON_ACTIVIDADES)) {
                POAConActividades poaCA = (POAConActividades) generalPOA;
                nombreProyecto = poaCA.getConMontoPorAnio().getNombre();
            }

            reporte.setNombreProyecto(nombreProyecto.toUpperCase());

            Set<String> fuentes = new LinkedHashSet<>();
            for (RelacionProAdqItemInsumo rel : item.getRelItemInsumos()) {
                //se agregan las fuentes
                for (POMontoFuenteInsumo insumoF : rel.getInsumo().getPoInsumo().getMontosFuentes()) {
                    if (insumoF.getCertificadoDisponibilidadPresupuestariaAprobada() != null && insumoF.getCertificadoDisponibilidadPresupuestariaAprobada().booleanValue() == true) {
                        fuentes.add(insumoF.getFuente().getFuenteRecursos().getNombre());
                    }
                }

            }
            String titualrFuentes = TextUtils.join(", ", fuentes);
            if (codigoProyecto != null && !codigoProyecto.isEmpty()) {
                titualrFuentes += " / " + codigoProyecto;
            }
            reporte.setFuentes(titualrFuentes.toUpperCase());

            reporte.setNroActa(acta.getContratoOC().getId() + "-" + acta.getNroActa());
            reporte.setEstadoActa(EstadoActa.traducirEstado(acta.getEstado()));

            SimpleDateFormat formateador = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new Locale("es"));
            String fecha = "San Salvador, " + formateador.format(new Date());
            reporte.setFecha(fecha);

            reporte.setNroContrato(acta.getContratoOC().getId() + "-" + acta.getNroActa());
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            reporte.setFechaEmisionStr(df.format(acta.getFechaGeneracion()));
            reporte.setProveedor(proveedor.getNombreComercial());
            reporte.setNitProveedor(ReportesUtils.getText(proveedor.getNitOferente()));

            String montoContrato = "US$ " + NumberUtils.nomberToString(contrato.getMontoAdjudicado());
            reporte.setMontoContrato(montoContrato);

            String procesoCompra = contrato.getProcesoAdquisicion().getSecuenciaAnio() + "/" + contrato.getProcesoAdquisicion().getSecuenciaNumero();
            reporte.setProcesoCompra(procesoCompra);

            reporte.setItems(new LinkedList());
            DataReporteActaDeRecepcionItem dataPagoItem = new DataReporteActaDeRecepcionItem();
            String descripcion = acta.getObservaciones() != null ? acta.getObservaciones() : "";
            dataPagoItem.setDescripcion(descripcion);
            String montoTotal = "US$ " + NumberUtils.nomberToString(acta.getMontoRecibido());
            dataPagoItem.setMonto(montoTotal);//El monto es el total porque siempre va a haber uno solo
            reporte.getItems().add(dataPagoItem);

            String total = NumberUtils.nomberToString(acta.getMontoRecibido());
            reporte.setTotal("US$ " + total);
            NumeroALetra numeroALetra = new NumeroALetra();
            String totalLetras = numeroALetra.convertirMoneda(total.toString(), false);
            totalLetras = totalLetras != null ? totalLetras : "";
            reporte.setTotalEnLetras(totalLetras);

            ClassLoader classLoader = getClass().getClassLoader();
            URL jasperFileNameURL = classLoader.getResource("reportes/ActaDeDevolucion.jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFileNameURL);

            Map parcol = new HashMap();
            addLogoToMap(parcol);

            return ReportesUtils.generarBytesPDF(reporte, parcol, jasperReport);

        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }

    }

    /**
     * Este método genera el reporte QUEDAN para un acta de contrato
     *
     * @param pago
     * @return
     * @throws GeneralException
     */
    public Archivo generarQUEDAN(ActaContrato pago) throws GeneralException {
        try {
            Proveedor proveedor = pago.getContratoOC().getProcesoAdquisicionProveedor().getProveedor();

            String areaReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_AREA_QUEDAN);
            String tituloReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_TITULO_QUEDAN);

            DataReporteQUEDAN reporte = (DataReporteQUEDAN) this.inicializarReporteTemplate(tituloReporte, areaReporte, DataReporteQUEDAN.class);

            if (pago.getNumeroComprobanteRecepcionPago() == null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_PARA_DESCARGAR_REPORTE_ES_NECESARIO_GENERAR_NUMERO_COMPROBANTE_RECEPCION_PAGO);
                throw b;
            }
            String nroComprobante = pago.getNumeroComprobanteRecepcionPago() != null ? pago.getNumeroComprobanteRecepcionPago().getId().toString() : "";
            reporte.setNumeroComprobanteRecepcionPago(nroComprobante);
            String nombreProveedor = "QUEDA en poder de esta Oficina, para trámite de pago, documento de propiedad de: " + proveedor.getNombreComercial();
            reporte.setNombreProveedor(nombreProveedor);
            reporte.setNitProveedor(ReportesUtils.getText(proveedor.getNitOferente()));
            reporte.setNroTelefonoProveedor(ReportesUtils.getText(proveedor.getTelefonoRepresentante()));
            reporte.setAgente("MINED");
            String nroContrato = "";
            if (pago.getContratoOC() != null && pago.getContratoOC().getNroContrato() != null) {
                nroContrato = ReportesUtils.getText(pago.getContratoOC().getNroContrato().toString());
            }
            reporte.setNroContratoOC(nroContrato);

            Date fechaActual = new Date();
            reporte.setFechaDeEmision(ReportesUtils.dateToString(fechaActual));
            Date fechaDeposito = DatesUtils.sumarRestarDiasFecha(fechaActual, 60);
            reporte.setFechaProgramadaDeDeposito(ReportesUtils.dateToString(fechaDeposito));

            String descripcion = "";
            if (pago.getTipo() == TipoActaContrato.RECEPCION) {
                for (PagoInsumo pi : pago.getPagosInsumo()) {
                    String nuevaDescripcion = ReportesUtils.getText(pi.getDescripcion());
                    descripcion = descripcion + nuevaDescripcion + "\n";
                }
            } else {
                descripcion = ReportesUtils.getText(pago.getObservaciones());
            }
            reporte.setDescripcionItems(ReportesUtils.getText(descripcion));

            //se cargan las facturas
            BigDecimal totalFacturas = BigDecimal.ZERO;
            reporte.setFacturas(new LinkedList<DataReporteQuedanFactura>());
            for (Factura factura : pago.getFacturas()) {
                DataReporteQuedanFactura df = new DataReporteQuedanFactura();
                String numFactura = factura.getNumero() != null ? factura.getNumero().toString() : "";
                df.setNumeroFactura(numFactura);
                String total = factura.getImporte() != null ? ReportesUtils.getNumber(factura.getImporte()) : "";
                df.setTotal(total);

                if (factura.getImporte() != null) {
                    totalFacturas = totalFacturas.add(factura.getImporte());
                }
                reporte.getFacturas().add(df);
            }
            DataReporteQuedanFactura totalF = new DataReporteQuedanFactura();
            totalF.setNumeroFactura("Total");
            totalF.setTotal(ReportesUtils.getNumber(totalFacturas));
            reporte.getFacturas().add(totalF);

            //se cargan los impuestos
            reporte.setImpuestos(new LinkedList<DataReporteQUEDANImpuestos>());

            DataReporteQUEDANImpuestos totalSinImpuestos = new DataReporteQUEDANImpuestos();
            totalSinImpuestos.setNombre("Total sin impuestos");
            totalSinImpuestos.setValor(ReportesUtils.getNumber(pago.getQuedan().getMontoSinImpuestos()));
            reporte.getImpuestos().add(totalSinImpuestos);

            for (ValoresImpuestoQuedan valorImpuesto : pago.getQuedan().getValoresImpuesto()) {
                DataReporteQUEDANImpuestos iter = new DataReporteQUEDANImpuestos();
                String nombre = valorImpuesto.getImpuesto().getNombre();
                if (valorImpuesto.getTipoImpuestoEnCodiguera() == TipoImpuesto.PORCENTAJE) {
                    nombre = nombre + "  (%" + ReportesUtils.getNumber(valorImpuesto.getValorImpuestoEnCodiguera()) + ")";
                } else {
                    nombre = nombre + "  (TF)";
                }

                iter.setNombre(nombre);
                iter.setValor(ReportesUtils.getNumber(valorImpuesto.getValorImpuestoEnPAgo()));
                reporte.getImpuestos().add(iter);
            }

            DataReporteQUEDANImpuestos totalconImpuestos = new DataReporteQUEDANImpuestos();
            totalconImpuestos.setNombre("Total");
            totalconImpuestos.setValor(ReportesUtils.getNumber(pago.getQuedan().getMontoquedan()));
            reporte.getImpuestos().add(totalconImpuestos);

            //se cargan las retenciones
            reporte.setRetenciones(new LinkedList<DataReporteQUEDANRetenciones>());

            DataReporteQUEDANRetenciones totalSinImpuestosR = new DataReporteQUEDANRetenciones();
            totalSinImpuestosR.setNombre("Total sin impuestos");
            totalSinImpuestosR.setValor(ReportesUtils.getNumber(pago.getQuedan().getMontoSinImpuestos()));
            reporte.getRetenciones().add(totalSinImpuestosR);

            for (ValoresImpuestoQuedan valorImpuesto : pago.getQuedan().getValoresImpuesto()) {
                DataReporteQUEDANRetenciones iter = new DataReporteQUEDANRetenciones();
                String nombre = "Retención " + valorImpuesto.getImpuesto().getNombre();
                nombre = nombre + "  (%" + ReportesUtils.getNumber(valorImpuesto.getValorRetencionEnPago()) + ")";

                iter.setNombre(nombre);
                iter.setValor(ReportesUtils.getNumber(valorImpuesto.getValorRetencionEnPago()));
                reporte.getRetenciones().add(iter);
            }

            //se cargan las fuentes de recursos
            reporte.setFuentes(new LinkedList());
            reporte.setCategorias(new LinkedList());

            HashSet<String> fuentesSet = new LinkedHashSet();

            if (pago.getContratoOC() != null) {
                for (ProcesoAdquisicionItem item : pago.getContratoOC().getItems()) {
                    for (RelacionProAdqItemInsumo rel : item.getRelItemInsumos()) {
                        for (POMontoFuenteInsumo fuente : rel.getInsumo().getPoInsumo().getMontosFuentes()) {
                            if (fuente.getFuente().getFuenteRecursos() != null) {
                                String nombreFuente = ReportesUtils.getText(fuente.getFuente().getFuenteRecursos().getNombre());
                                fuentesSet.add(nombreFuente);
                            }
                        }
                    }
                }
            }

            for (String nombreFuente : fuentesSet) {
                DataReporteQUEDANFuentes test1 = new DataReporteQUEDANFuentes();
                test1.setNombreFuente(nombreFuente);
                reporte.getFuentes().add(test1);
            }

            for (RelActaContratoCategoriaConvenio relActaCategoria : pago.getRelacionesActaCategoria()) {
                DataReporteQUEDANCategorias test2 = new DataReporteQUEDANCategorias();
                test2.setCategoria(relActaCategoria.getCategoriaConvenio().getNombre());
                test2.setGoes(NumberUtils.nomberToString(relActaCategoria.getMontoGoes()));
                test2.setPrestamo(NumberUtils.nomberToString(relActaCategoria.getMontoNoGoes()));
                BigDecimal montoTotal = relActaCategoria.getMontoGoes().add(relActaCategoria.getMontoNoGoes());
                test2.setTotal(NumberUtils.nomberToString(montoTotal));
                reporte.getCategorias().add(test2);
            }

            ClassLoader classLoader = getClass().getClassLoader();
            URL jasperFileNameURL = classLoader.getResource("reportes/quedan2.jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFileNameURL);

            Map parcol = new HashMap();
            addLogoToMap(parcol);

            Archivo a = archivoBean.crearArchivo();
            a.setContentType("application/pdf");
            String nombreOriginal = "QUEDAN_" + nroComprobante + ".pdf";
            a.setNombreOriginal(nombreOriginal);

            ReportesUtils.generarPDF(reporte, parcol, jasperReport, archivoBean.getFile(a));

            a = (Archivo) generalDAO.update(a);
            return a;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }

    }

    /**
     * Este método es el encargado de generar el reporte de asignación no
     * programable
     *
     * @param asignacionNoProgramableId
     * @return
     * @throws GeneralException
     */
    public byte[] generarReporteAsignacionNoProgramable(Integer asignacionNoProgramableId) throws GeneralException {
        try {
            AsignacionNoProgramable obj = (AsignacionNoProgramable) generalDAO.findById(AsignacionNoProgramable.class, asignacionNoProgramableId);

            DataReporteAsignacionNoProgramable data = new DataReporteAsignacionNoProgramable();
            data.setLineas(new LinkedList());
            data.setSubActividadeses(new LinkedList());

            DataReporteAsignacionNoProgramableLineas lineaItem = null;
            DataReporteAsignacionNoProgramableSubActividades subActividadItem = null;

            if (obj.getNombre() != null) {
                data.setNombre(obj.getNombre());
            }
            if (obj.getClasificadorFuncional() != null) {
                data.setCalificador(obj.getClasificadorFuncional().getNombre());
            }
            if (obj.getDescripcion() != null) {
                data.setDescripcion(obj.getDescripcion());
            }
            if (obj.getPlanificacionEstrategica() != null) {
                data.setPlanificacion(obj.getPlanificacionEstrategica().getNombre());
            }

            if (obj.getClasificadorFuncional() != null) {
                data.setCalificador(obj.getClasificadorFuncional().getNombre());
            }

            if (obj.getUnidadTecnica() != null) {
                data.setUnidadTecnicaResponsable(obj.getUnidadTecnica().getNombre());
            }

            if (obj.getLineasEstrategicas() != null) {
                for (LineaEstrategica col : obj.getLineasEstrategicas()) {
                    lineaItem = new DataReporteAsignacionNoProgramableLineas();
                    lineaItem.setNombre(col.getNombre());
                    data.getLineas().add(lineaItem);
                }
            }

            if (obj.getActividadesNP() != null) {
                for (ActividadAsignacionNP col : obj.getActividadesNP()) {
                    subActividadItem = new DataReporteAsignacionNoProgramableSubActividades();
                    subActividadItem.setNombre(col.getNombre());
                    data.getSubActividadeses().add(subActividadItem);
                }
            }

            if (datosUsuario != null && datosUsuario.getCodigoUsuario() != null) {
                data.setUsuario(datosUsuario.getCodigoUsuario());
            }

            data.setFechaActual(ReportesUtils.dateToString(new Date()));

            Map parcol = new HashMap();
            addLogoToMap(parcol);

            ClassLoader classLoader = getClass().getClassLoader();
            URL jasperFileNameURL = classLoader.getResource("reportes/ReporteAsignacionNoProgramable.jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFileNameURL);

            return ReportesUtils.generarBytesPDF(data, parcol, jasperReport);

        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método es el encargado de generar el reporte de acción central
     *
     * @param accionCentralId
     * @return
     * @throws GeneralException
     */
    public byte[] generarReporteAccionCentral(Integer accionCentralId) throws GeneralException {
        try {
            AccionCentral obj = (AccionCentral) generalDAO.findById(AccionCentral.class, accionCentralId);

            DataReporteAccionCentral data = new DataReporteAccionCentral();
            data.setLineas(new LinkedList());

            DataReporteAccionCentraLineas lineaItem = null;

            if (obj.getNombre() != null) {
                data.setNombre(obj.getNombre());
            }
            if (obj.getClasificadorFuncional() != null) {
                data.setCalificador(obj.getClasificadorFuncional().getNombre());
            }
            if (obj.getDescripcion() != null) {
                data.setDescripcion(obj.getDescripcion());
            }
            if (obj.getPlanificacionEstrategica() != null) {
                data.setPlanificacion(obj.getPlanificacionEstrategica().getNombre());
            }

            if (obj.getClasificadorFuncional() != null) {
                data.setCalificador(obj.getClasificadorFuncional().getNombre());
            }

            if (obj.getUnidadTecnica() != null) {
                data.setUnidadTecnicaResponsable(obj.getUnidadTecnica().getNombre());
            }

            if (obj.getLineasEstrategicas() != null) {
                for (LineaEstrategica col : obj.getLineasEstrategicas()) {
                    lineaItem = new DataReporteAccionCentraLineas();
                    lineaItem.setNombre(col.getNombre());
                    data.getLineas().add(lineaItem);
                }
            }

            if (datosUsuario != null && datosUsuario.getCodigoUsuario() != null) {
                data.setUsuario(datosUsuario.getCodigoUsuario());
            }

            data.setFechaActual(ReportesUtils.dateToString(new Date()));

            Map parcol = new HashMap();
            addLogoToMap(parcol);

            ClassLoader classLoader = getClass().getClassLoader();
            URL jasperFileNameURL = classLoader.getResource("reportes/ReporteAccionCentral.jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFileNameURL);

            return ReportesUtils.generarBytesPDF(data, parcol, jasperReport);

        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método es el encargado de generar el reporte global de asignaciones
     * no programables
     *
     * @return
     * @throws GeneralException
     */
    public byte[] generarReporteGralAsignacionNoProgramable() throws GeneralException {
        try {
            List<AsignacionNoProgramable> listObj = generalDAO.findAll(AsignacionNoProgramable.class);

            String areaReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_AREA_ANP_GRAL);
            String tituloReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_TITULO_ANP_GRAL);

            DataReporteANP data = (DataReporteANP) this.inicializarReporteTemplate(tituloReporte, areaReporte, DataReporteANP.class);
            data.setActividades(new LinkedList());
            data.setClasificadores(new LinkedList());
            data.setMontos(new LinkedList());

            DataReporteANPActividades actividad = null;
            DataReporteANPClasificadorFuncional clasificador = null;
            DataReporteANPMontos montos = null;

            //obtiene anio desde ya nio hasta   
            Integer cantAnios = 5;
            Configuracion confVentana = configuracionBean.obtenerCnfPorCodigo(ConstantesConfiguracion.CANTIDAD_AMOS_PLANIFICACION);
            if (confVentana != null && !TextUtils.isEmpty(confVentana.getCnfValor())) {
                cantAnios = Integer.valueOf(confVentana.getCnfValor());
            }

            Integer anioDesde = DatesUtils.getYearOfDate(new Date());
            Configuracion confAnio = configuracionBean.obtenerCnfPorCodigo(ConstantesConfiguracion.ANIO_TECHO_CATEGORIA_PRESUPUESTAL);
            if (confAnio != null && !TextUtils.isEmpty(confAnio.getCnfValor())) {
                anioDesde = Integer.valueOf(confAnio.getCnfValor());
            }
            Integer anioHasta = anioDesde + cantAnios - 1;
            List<ProgramacionFinancieraAsignacionNoProgramable> programacionActual = generalBean.getProgramacionFinancieraAsigNP(anioDesde, anioHasta);

            BigDecimal totalAnio0 = BigDecimal.ZERO;
            BigDecimal totalAnio1 = BigDecimal.ZERO;
            BigDecimal totalAnio2 = BigDecimal.ZERO;
            BigDecimal totalAnio3 = BigDecimal.ZERO;
            BigDecimal totalAnio4 = BigDecimal.ZERO;

            for (AsignacionNoProgramable asic : listObj) {
                actividad = new DataReporteANPActividades();
                actividad.setNombre(asic.getNombre());
                if (asic.getUnidadTecnica() != null) {
                    actividad.setResponsable(asic.getUnidadTecnica().getNombre());
                    if (asic.getUnidadTecnica().getUniUsuario() != null && asic.getUnidadTecnica().getUniUsuario().getUsuCorreoElectronico() != null) {
                        actividad.setCorreo(asic.getUnidadTecnica().getUniUsuario().getUsuCorreoElectronico());
                    }
                }
                data.getActividades().add(actividad);

                clasificador = new DataReporteANPClasificadorFuncional();
                clasificador.setNombreActividad(asic.getNombre());
                if (asic.getClasificadorFuncional() != null) {
                    clasificador.setClase(asic.getClasificadorFuncional().getCodigo() + " " + asic.getClasificadorFuncional().getCodigo());
                    if (asic.getClasificadorFuncional().getPadre() != null) {
                        clasificador.setGrupo(asic.getClasificadorFuncional().getPadre().getCodigo() + " " + asic.getClasificadorFuncional().getCodigo());
                        if (asic.getClasificadorFuncional().getPadre().getPadre() != null) {
                            clasificador.setDivision(asic.getClasificadorFuncional().getPadre().getPadre().getCodigo() + " " + asic.getClasificadorFuncional().getCodigo());
                        }
                    }
                }
                data.getClasificadores().add(clasificador);

                //se cargan los montos
                montos = new DataReporteANPMontos();
                montos.setNombreActividad(asic.getNombre());
                for (int anio = anioDesde; anio <= anioHasta; anio++) {
                    Iterator<ProgramacionFinancieraAsignacionNoProgramable> iter = programacionActual.iterator();
                    BigDecimal valor = null;
                    while (iter.hasNext() && valor == null) {
                        ProgramacionFinancieraAsignacionNoProgramable iterMonto = iter.next();
                        if (iterMonto.getAnio().intValue() == anio && iterMonto.getAsignacionNoProgramable().equals(asic)) {
                            valor = (iterMonto.getMonto());
                        }
                    }
                    if (valor == null) {
                        valor = BigDecimal.ZERO;
                    }

                    int pos = anio - anioDesde;
                    if (pos == 0) {
                        montos.setMeta(NumberUtils.nomberToString(valor));
                        totalAnio0 = totalAnio0.add(valor);
                    } else if (pos == 1) {
                        montos.setAnio1(NumberUtils.nomberToString(valor));
                        totalAnio1 = totalAnio1.add(valor);
                    } else if (pos == 2) {
                        montos.setAnio2(NumberUtils.nomberToString(valor));
                        totalAnio2 = totalAnio2.add(valor);
                    } else if (pos == 3) {
                        montos.setAnio3(NumberUtils.nomberToString(valor));
                        totalAnio3 = totalAnio3.add(valor);
                    } else if (pos == 4) {
                        montos.setAnio4(NumberUtils.nomberToString(valor));
                        totalAnio4 = totalAnio4.add(valor);
                    }
                }
                data.getMontos().add(montos);
            }

            //se setean los titulos de los totales
            for (int anio = anioDesde; anio <= anioHasta; anio++) {
                int pos = anio - anioDesde;
                if (pos == 0) {
                    data.setMetaH("Meta t");
                    data.setTotalMeta(NumberUtils.nomberToString(totalAnio0));
                } else if (pos == 1) {
                    data.setAnio1H(String.valueOf(anio));
                    data.setTotalAnio1(NumberUtils.nomberToString(totalAnio1));
                } else if (pos == 2) {
                    data.setAnio2H(String.valueOf(anio));
                    data.setTotalAnio2(NumberUtils.nomberToString(totalAnio2));
                } else if (pos == 3) {
                    data.setAnio3H(String.valueOf(anio));
                    data.setTotalAnio3(NumberUtils.nomberToString(totalAnio3));
                } else if (pos == 4) {
                    data.setAnio4H(String.valueOf(anio));
                    data.setTotalAnio4(NumberUtils.nomberToString(totalAnio4));
                }
            }

            if (datosUsuario != null && datosUsuario.getCodigoUsuario() != null) {
                data.setUsuario(datosUsuario.getCodigoUsuario());
            }
            data.setFecha(ReportesUtils.dateToString(new Date()));

            Map parcol = new HashMap();
            addLogoToMap(parcol);

            ClassLoader classLoader = getClass().getClassLoader();
            URL jasperFileNameURL = classLoader.getResource("reportes/ReporteGeneralAsignacionNoProgramable.jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFileNameURL);

            return ReportesUtils.generarBytesPDF(data, parcol, jasperReport);

        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método es el encargado de generar el reoprte global de acciones
     * centrales
     *
     * @return
     * @throws GeneralException
     */
    public byte[] generarReporteGralAccionCentral() throws GeneralException {
        try {
            List<AccionCentral> listObj = generalDAO.findAll(AccionCentral.class);

            String areaReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_AREA_AC_GRAL);
            String tituloReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_TITULO_AC_GRAL);

            DataReporteAC data = (DataReporteAC) this.inicializarReporteTemplate(tituloReporte, areaReporte, DataReporteAC.class);
            data.setActividades(new LinkedList());
            data.setClasificadores(new LinkedList());
            data.setMontos(new LinkedList());

            DataReporteANPActividades actividad = null;
            DataReporteANPClasificadorFuncional clasificador = null;
            DataReporteANPMontos montos = null;

            for (AccionCentral asic : listObj) {
                actividad = new DataReporteANPActividades();
                actividad.setNombre(asic.getNombre());
                if (asic.getUnidadTecnica() != null) {
                    actividad.setResponsable(asic.getUnidadTecnica().getNombre());
                    if (asic.getUnidadTecnica().getUniUsuario() != null && asic.getUnidadTecnica().getUniUsuario().getUsuCorreoElectronico() != null) {
                        actividad.setCorreo(asic.getUnidadTecnica().getUniUsuario().getUsuCorreoElectronico());
                    }
                }
                data.getActividades().add(actividad);

                clasificador = new DataReporteANPClasificadorFuncional();
                clasificador.setNombreActividad(asic.getNombre());
                if (asic.getClasificadorFuncional() != null) {
                    clasificador.setClase(asic.getClasificadorFuncional().getCodigo() + " " + asic.getClasificadorFuncional().getCodigo());
                    if (asic.getClasificadorFuncional().getPadre() != null) {
                        clasificador.setGrupo(asic.getClasificadorFuncional().getPadre().getCodigo() + " " + asic.getClasificadorFuncional().getCodigo());
                        if (asic.getClasificadorFuncional().getPadre().getPadre() != null) {
                            clasificador.setDivision(asic.getClasificadorFuncional().getPadre().getPadre().getCodigo() + " " + asic.getClasificadorFuncional().getCodigo());
                        }
                    }
                }
                data.getClasificadores().add(clasificador);

            }

            //obtiene anio desde ya nio hasta   
            Integer cantAnios = 5;
            Configuracion confVentana = configuracionBean.obtenerCnfPorCodigo(ConstantesConfiguracion.CANTIDAD_AMOS_PLANIFICACION);
            if (confVentana != null && !TextUtils.isEmpty(confVentana.getCnfValor())) {
                cantAnios = Integer.valueOf(confVentana.getCnfValor());
            }

            Integer anioDesde = DatesUtils.getYearOfDate(new Date());
            Configuracion confAnio = configuracionBean.obtenerCnfPorCodigo(ConstantesConfiguracion.ANIO_TECHO_CATEGORIA_PRESUPUESTAL);
            if (confAnio != null && !TextUtils.isEmpty(confAnio.getCnfValor())) {
                anioDesde = Integer.valueOf(confAnio.getCnfValor());
            }
            Integer anioHasta = anioDesde + cantAnios - 1;
            List<ProgramacionFinancieraAccionCentral> programacionActual = generalBean.getProgramacionFinancieraAccionCentral(anioDesde, anioHasta);

            BigDecimal totalAnio0 = BigDecimal.ZERO;
            BigDecimal totalAnio1 = BigDecimal.ZERO;
            BigDecimal totalAnio2 = BigDecimal.ZERO;
            BigDecimal totalAnio3 = BigDecimal.ZERO;
            BigDecimal totalAnio4 = BigDecimal.ZERO;

            List<UnidadTecnica> direcciones = generalBean.getUnidadesTecnicasDireccion();

            for (UnidadTecnica asic : direcciones) {

                //se cargan los montos
                montos = new DataReporteANPMontos();
                montos.setNombreActividad(asic.getNombre());
                for (int anio = anioDesde; anio <= anioHasta; anio++) {
                    Iterator<ProgramacionFinancieraAccionCentral> iter = programacionActual.iterator();
                    BigDecimal valor = null;
                    while (iter.hasNext() && valor == null) {
                        ProgramacionFinancieraAccionCentral iterMonto = iter.next();
                        if (iterMonto.getAnio().intValue() == anio && iterMonto.getUnidadTecnica().equals(asic)) {
                            valor = (iterMonto.getMonto());
                        }
                    }
                    if (valor == null) {
                        valor = BigDecimal.ZERO;
                    }

                    int pos = anio - anioDesde;
                    if (pos == 0) {
                        montos.setMeta(NumberUtils.nomberToString(valor));
                        totalAnio0 = totalAnio0.add(valor);
                    } else if (pos == 1) {
                        montos.setAnio1(NumberUtils.nomberToString(valor));
                        totalAnio1 = totalAnio1.add(valor);
                    } else if (pos == 2) {
                        montos.setAnio2(NumberUtils.nomberToString(valor));
                        totalAnio2 = totalAnio2.add(valor);
                    } else if (pos == 3) {
                        montos.setAnio3(NumberUtils.nomberToString(valor));
                        totalAnio3 = totalAnio3.add(valor);
                    } else if (pos == 4) {
                        montos.setAnio4(NumberUtils.nomberToString(valor));
                        totalAnio4 = totalAnio4.add(valor);
                    }
                }
                data.getMontos().add(montos);
            }

            //se setean los titulos de los totales
            for (int anio = anioDesde; anio <= anioHasta; anio++) {
                int pos = anio - anioDesde;
                if (pos == 0) {
                    data.setMetaH("Meta t");
                    data.setTotalMeta(NumberUtils.nomberToString(totalAnio0));
                } else if (pos == 1) {
                    data.setAnio1H(String.valueOf(anio));
                    data.setTotalAnio1(NumberUtils.nomberToString(totalAnio1));
                } else if (pos == 2) {
                    data.setAnio2H(String.valueOf(anio));
                    data.setTotalAnio2(NumberUtils.nomberToString(totalAnio2));
                } else if (pos == 3) {
                    data.setAnio3H(String.valueOf(anio));
                    data.setTotalAnio3(NumberUtils.nomberToString(totalAnio3));
                } else if (pos == 4) {
                    data.setAnio4H(String.valueOf(anio));
                    data.setTotalAnio4(NumberUtils.nomberToString(totalAnio4));
                }
            }

            Map parcol = new HashMap();
            addLogoToMap(parcol);

            ClassLoader classLoader = getClass().getClassLoader();
            URL jasperFileNameURL = classLoader.getResource("reportes/ReporteGeneralAccionCentral.jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFileNameURL);

            return ReportesUtils.generarBytesPDF(data, parcol, jasperReport);

        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método genera el reporte para un proyecto
     *
     * @param idProyecto
     * @return
     * @throws GeneralException
     */
    public byte[] generarReporteProyecto(Integer idProyecto) throws GeneralException {
        try {
            Proyecto proyecto = (Proyecto) generalDAO.findById(Proyecto.class, idProyecto);

            String areaReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_AREA_PROYECTO);
            String tituloReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_TITULO_PROYECTO);

            DataReporteProyecto data = (DataReporteProyecto) this.inicializarReporteTemplate(tituloReporte, areaReporte, DataReporteProyecto.class);
            data.setNombre(proyecto.getNombre());
            data.setTipo(proyecto.getTipoProyecto().getNombre());
            if (proyecto.getProgramaInstitucional() != null) {
                data.setProgramaInstitucional(proyecto.getProgramaInstitucional().getNombre());
            }
            if (proyecto.getProgramaPresupuestario() != null) {
                data.setSubprogramaPresupuestario(proyecto.getProgramaPresupuestario().getNombre());
                if (proyecto.getProgramaPresupuestario().getProgramaPresupuestario() != null) {
                    data.setProgramaPresupuestario(proyecto.getProgramaPresupuestario().getProgramaPresupuestario().getNombre());
                }
            }
            data.setCodigoSiip(ReportesUtils.getText(proyecto.getCodigoSIIP()));
            data.setMontoGlobal(ReportesUtils.getNumber(proyecto.getMontoGlobal()));
            data.setObjetivo(ReportesUtils.getText(proyecto.getObjetivo()));
            data.setDescripcion(ReportesUtils.getText(proyecto.getDescripcion()));
            data.setPep(ReportesUtils.getSiNo(proyecto.getPep()));
            data.setFechaInicio(ReportesUtils.dateToString(proyecto.getInicio()));
            data.setFechaHasta(ReportesUtils.dateToString(proyecto.getFin()));
            data.setCorrespondePOG(ReportesUtils.getSiNo(proyecto.getTienePOG()));

            data.setFinanciacion(new LinkedList<DataReporteProyectoFinanciacion>());
            for (ProyectoAporte fuente : proyecto.getAportesProyecto()) {
                DataReporteProyectoFinanciacion dataFuente = new DataReporteProyectoFinanciacion();
                if (fuente.getFuenteRecursos() != null) {
                    dataFuente.setFuenteRecursos(fuente.getFuenteRecursos().getCodigo() + " | " + fuente.getFuenteRecursos().getNombre());
                }
                if (fuente.getCategoriaConvenio() != null) {
                    dataFuente.setCategoria(fuente.getCategoriaConvenio().getNombre());
                }
                dataFuente.setMonto(ReportesUtils.getNumber(fuente.getMonto()));

                data.getFinanciacion().add(dataFuente);
            }

            data.setIndicadores(new LinkedList<DataReporteProyectoIndicador>());

            data.setComponentes(new LinkedList<DataReporteProyectoEstructura>());
            data.setMontosComponentes(new LinkedList<DataReporteProyectoEstructuraMontoFuente>());
            List<ProyectoComponente> componentes = ReporteConverter.ordenarProyectoEstructuraPorOrden(proyecto.getProyectoComponentes());
            for (ProyectoComponente componente : componentes) {
                if (componente.getComponentePadre() == null) {
                    ReporteConverter.addEstrucutra(null, componente, data.getComponentes());
                    ReporteConverter.generarMontoEstructura(null, componente, data.getMontosComponentes());
                    List<ProyectoComponente> hijos = ReporteConverter.ordenarProyectoEstructuraPorOrden(componente.getComponenteHijos());
                    for (ProyectoComponente componenteHijo : hijos) {
                        ReporteConverter.addEstrucutra(componente.getNumero(), componenteHijo, data.getComponentes());
                        ReporteConverter.generarMontoEstructura(componente.getNumero(), componenteHijo, data.getMontosComponentes());
                    }
                }
            }

            data.setMacroactividades(new LinkedList<DataReporteProyectoEstructura>());
            data.setMontosMacroActividades(new LinkedList<DataReporteProyectoEstructuraMontoFuente>());
            List<ProyectoMacroActividad> macroActividades = ReporteConverter.ordenarProyectoEstructuraPorOrden(proyecto.getProyectoMacroactividad());
            for (ProyectoMacroActividad macroActividad : macroActividades) {
                ReporteConverter.addEstrucutra(null, macroActividad, data.getMacroactividades());
                ReporteConverter.generarMontoEstructura(null, macroActividad, data.getMontosMacroActividades());
            }

            ClassLoader classLoader = getClass().getClassLoader();
            URL jasperFileNameURL = classLoader.getResource("reportes/reporteProyecto.jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFileNameURL);

            Map parcol = new HashMap();
            addLogoToMap(parcol);

            return ReportesUtils.generarBytesPDF(data, parcol, jasperReport);
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * *
     * Este método genera el reporte de orden de inicio
     *
     * @param contrato
     * @return
     */
    public byte[] generarOrdenInicio(ContratoOC contrato) {
        try {

            SimpleDateFormat formateador = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new Locale("es"));

            String ordenCompraOContrato = contrato.getTipo().equals(TipoContrato.CONTRATO) ? "al Contrato" : "a la Orden de Compra";
            String nroOrdenCompra = contrato.getNroContrato().toString();
            String metodoAdq = contrato.getProcesoAdquisicion().getMetodoAdquisicion().getNombre();
            String numProceso = contrato.getProcesoAdquisicion().getSecuenciaAnio() + "/" + contrato.getProcesoAdquisicion().getSecuenciaNumero();
            String nombreProceso = contrato.getProcesoAdquisicion().getNombre();
            String fechaInicio = formateador.format(contrato.getFechaInicio());
            String plazo = contrato.getPlazoEntrega().toString();
            String fechaFin = formateador.format(contrato.getFechaFin());
            String telAdminContrato = contrato.getAdministrador().getUsuTelefono() != null ? contrato.getAdministrador().getUsuTelefono() : "";
            String prefijoAdminContrato = contrato.getAdministrador().getUsuPrefijo();
            String adminContrato = contrato.getAdministrador().getUsuPrimerNombre() != null ? contrato.getAdministrador().getUsuPrimerNombre() : "";
            adminContrato += contrato.getAdministrador().getUsuSegundoNombre() != null ? " " + contrato.getAdministrador().getUsuSegundoNombre() : "";
            adminContrato += contrato.getAdministrador().getUsuPrimerApellido() != null ? " " + contrato.getAdministrador().getUsuPrimerApellido() : "";
            adminContrato += contrato.getAdministrador().getUsuSegundoApellido() != null ? " " + contrato.getAdministrador().getUsuSegundoApellido() : "";
            String cargoAdminContrato = contrato.getAdministrador().getUsuCargo() != null ? contrato.getAdministrador().getUsuCargo() : "";
            String delContratoODeLaOrdenCompra = contrato.getTipo().equals(TipoContrato.CONTRATO) ? "del contrato" : "de la Orden de Compra";

            String contenido = configuracionBean.obtenerCnfPorCodigo(ConstantesConfiguracion.CONTENIDO_REPORTE_ORDEN_INICIO).getCnfValor();
            Map map = new HashMap();
            map.put("ordenCompraOContrato", ordenCompraOContrato);
            map.put("nroOrdenCompra", nroOrdenCompra);
            map.put("metodoAdq", metodoAdq);
            map.put("numProceso", numProceso);
            map.put("nombreProcesoAdq", nombreProceso.toUpperCase());
            map.put("fechaInicio", fechaInicio);
            map.put("plazo", plazo);
            map.put("fechaFin", fechaFin);
            map.put("telAdminContrato", telAdminContrato);
            map.put("prefijoAdminContrato", prefijoAdminContrato);
            map.put("adminContrato", adminContrato);
            map.put("cargoAdminContrato", cargoAdminContrato);
            map.put("delContratoODeLaOrdenCompra", delContratoODeLaOrdenCompra);
            contenido = TextUtils.replaceTokens(contenido, map);

            String fechaEmision = contrato.getFechaEmision() != null ? formateador.format(contrato.getFechaEmision()) : "";
            String nombreRepresentanteProv = contrato.getProcesoAdquisicionProveedor().getProveedor().getNombreRepresentante() != null ? contrato.getProcesoAdquisicionProveedor().getProveedor().getNombreRepresentante() : "";
            nombreRepresentanteProv += contrato.getProcesoAdquisicionProveedor().getProveedor().getApellidosRepresentante() != null ? " " + contrato.getProcesoAdquisicionProveedor().getProveedor().getApellidosRepresentante() : "";
            String empresaProveedor = contrato.getProcesoAdquisicionProveedor().getProveedor().getRazonSocial() != null ? contrato.getProcesoAdquisicionProveedor().getProveedor().getRazonSocial() : "";
            String firmante = ReportesUtils.getNombreFirmante(contrato.getFirmanteOrdenInicio());
            String cargoFirmante = contrato.getFirmanteOrdenInicio().getUsuCargo() != null ? contrato.getFirmanteOrdenInicio().getUsuCargo() : "";
            String unidadTecnicaFirmante = "";
            List<UnidadTecnica> listaUT = usuarioBean.getUTDeUsuarioConOperacion(contrato.getFirmanteOrdenInicio().getUsuId(), ConstantesEstandares.OPERACION_FIRMAR_CONTRATO_OC);
            if (listaUT != null) {
                if (listaUT != null && !listaUT.isEmpty()) {
                    unidadTecnicaFirmante = listaUT.get(0).getNombre();
                }
            }

            String areaReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_AREA_ORDEN_INICIO);
            String tituloReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_TITULO_ORDEN_INICIO);

            DataOrdenInicio dataOrdenInicio = (DataOrdenInicio) this.inicializarReporteTemplate(tituloReporte, areaReporte, DataOrdenInicio.class);
            dataOrdenInicio.setFechaEmision("San Salvador, " + fechaEmision);
            dataOrdenInicio.setNombreProveedor(nombreRepresentanteProv.toUpperCase());
            dataOrdenInicio.setEmpresaProveedor(empresaProveedor.toUpperCase());
            dataOrdenInicio.setFirmante(firmante);
            dataOrdenInicio.setCargoFirmante(cargoFirmante.toUpperCase());
            dataOrdenInicio.setContenido(contenido);
            dataOrdenInicio.setUTFirmante(unidadTecnicaFirmante.toUpperCase());

            ClassLoader classLoader = getClass().getClassLoader();
            URL jasperFileNameURL = classLoader.getResource("reportes/OrdenInicio.jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFileNameURL);

            Map parcol = new HashMap();
            addLogoToMap(parcol);

            return ReportesUtils.generarBytesPDF(dataOrdenInicio, parcol, jasperReport);

        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método genera el comprobante de recepción de expedientes de pago
     *
     * @param pago
     * @param usuario
     * @return
     * @throws GeneralException
     */
    public Archivo generarComprobanteDeRecepcionDeExpedienteDePago(ActaContrato pago, SsUsuario usuario) throws GeneralException {
        try {

            if (pago.getNumeroComprobanteRecepcionPago() == null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_PARA_DESCARGAR_REPORTE_ES_NECESARIO_GENERAR_NUMERO_COMPROBANTE_RECEPCION_PAGO);
                throw b;
            }
            Proveedor proveedor = pago.getContratoOC().getProcesoAdquisicionProveedor().getProveedor();

            String areaReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_AREA_COMPROBANTE_REC_EXP_PAGO);
            String tituloReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_TITULO_COMPROBANTE_REC_EXP_PAGO);

            DataReporteComprobanteRecepcionExpedientePago dataReporte = (DataReporteComprobanteRecepcionExpedientePago) this.inicializarReporteTemplate(tituloReporte, areaReporte, DataReporteComprobanteRecepcionExpedientePago.class);

            GeneralPOA poa = null;
            if (pago.getContratoOC() != null) {
                ContratoOC contrato = pago.getContratoOC();
                if (contrato.getItems() != null && !contrato.getItems().isEmpty()) {
                    ProcesoAdquisicionItem item = pago.getContratoOC().getItems().get(0);
                    if (item.getRelItemInsumos() != null && !item.getRelItemInsumos().isEmpty()) {
                        RelacionProAdqItemInsumo relacion = item.getRelItemInsumos().get(0);
                        if (relacion.getInsumo() != null) {
                            ProcesoAdquisicionInsumo insumo = relacion.getInsumo();
                            if (insumo.getPoInsumo() != null && insumo.getPoInsumo().getPoa() != null) {
                                poa = insumo.getPoInsumo().getPoa();
                            }
                        }
                    }
                }
            }
            String nombreProyecto = "";
            if (poa != null) {
                if (poa.getTipo() == TipoPOA.POA_PROYECTO) {
                    POAProyecto poaP = (POAProyecto) poa;
                    nombreProyecto = poaP.getProyecto().getNombre();
                }
                if (poa.getTipo() == TipoPOA.POA_CON_ACTIVIDADES) {
                    POAConActividades poActividades = (POAConActividades) poa;
                    if (poActividades.getConMontoPorAnio().getTipo() == TipoMontoPorAnio.ACCION_CENTRAL) {
                        AccionCentral accionCentra = (AccionCentral) poActividades.getConMontoPorAnio();
                        nombreProyecto = accionCentra.getNombre();
                    }
                    if (poActividades.getConMontoPorAnio().getTipo() == TipoMontoPorAnio.ASIGN_NO_PROGRAMABLE) {
                        AsignacionNoProgramable asigNP = (AsignacionNoProgramable) poActividades.getConMontoPorAnio();
                        nombreProyecto = asigNP.getNombre();
                    }
                }
            }

            dataReporte.setNombreProyecto(nombreProyecto);
            dataReporte.setQuedanNO(pago.getNumeroComprobanteRecepcionPago().getId().toString());
            dataReporte.setNITProveedor(proveedor.getNitOferente());
            dataReporte.setNombreProveedor(proveedor.getNombreComercial());
            dataReporte.setMonto(ReportesUtils.getNumber(pago.getMontoRecibido()));
            dataReporte.setTelefono(proveedor.getTelefonoRepresentante());
            dataReporte.setMail(proveedor.getMail());

            dataReporte.setFechaEntrega(ReportesUtils.dateToString(pago.getFechaGeneracion()));
            dataReporte.setNombreFirmaTecnico(usuario.getUsuPrimerNombre() + " " + usuario.getUsuPrimerApellido());
            dataReporte.setTelefonoFirmanteTecnico(ReportesUtils.getText(usuario.getUsuTelefono()));

            List<String> numerosFacturas = new LinkedList<>();
            for (Factura f : pago.getFacturas()) {
                if (f.getTipo() != null && f.getTipo().equals(TipoFactura.FACTURA)) {
                    numerosFacturas.add("Factura No. " + f.getNumero());
                } else {
                    numerosFacturas.add("Recibo No. " + f.getNumero());
                }
            }

            dataReporte.setDocumentoDePago(TextUtils.join(", ", numerosFacturas));
            Configuracion notaPiePag = configuracionBean.obtenerCnfPorCodigo(ConstantesConfiguracion.REPORTE_QUEDAN_NOTA_PIE_PAGINA);
            dataReporte.setNotaPiePagina(notaPiePag.getCnfValor());

            Map parcol = new HashMap();
            addLogoToMap(parcol);

            ClassLoader classLoader = getClass().getClassLoader();
            URL jasperFileNameURL = classLoader.getResource("reportes/comprobante_recepcion_expediente_pago.jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFileNameURL);

            Archivo a = archivoBean.crearArchivo();
            a.setContentType("application/pdf");
            String nombreOriginal = "ComprobanteDeRecepcionDeExpedienteDePago_" + pago.getNumeroComprobanteRecepcionPago().getId().toString() + ".pdf";
            a.setNombreOriginal(nombreOriginal);

            ReportesUtils.generarPDF(dataReporte, parcol, jasperReport, archivoBean.getFile(a));

            a = (Archivo) generalDAO.update(a);
            return a;

        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método genera el reporte de retención de impuestos por proyecto
     *
     * @param idProyecto
     * @param fechaDesde
     * @param fechahasta
     * @param idImpuesto
     * @return
     * @throws GeneralException
     */
    public byte[] generarRetencionDeImpuestosPorProyecto(Integer idProyecto, Date fechaDesde, Date fechahasta, Integer idImpuesto) throws GeneralException {
        try {
            String nombreProyecto = "Todos";
            if (idProyecto != null) {
                Proyecto proyecto = (Proyecto) generalDAO.find(Proyecto.class, idProyecto);
                nombreProyecto = proyecto.getNombre();
            }
            Impuesto impuesto = (Impuesto) generalDAO.find(Impuesto.class, idImpuesto);
            Configuracion nitDelMinisterio = configuracionBean.obtenerCnfPorCodigo(ConstantesConfiguracion.NIT_DEL_MINISTERIO);

            String areaReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_AREA_RETENCION_IMPUESTO_POR_PROYECTO);
            String tituloReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_TITULO_RETENCION_IMPUESTO_POR_PROYECTO);

            DataReporteRetencionDeImpuestosPorProyecto dataReporte = (DataReporteRetencionDeImpuestosPorProyecto) this.inicializarReporteTemplate(tituloReporte, areaReporte, DataReporteRetencionDeImpuestosPorProyecto.class);
            dataReporte.setProyecto(nombreProyecto);
            dataReporte.setDesde(ReportesUtils.dateToString(fechaDesde));
            dataReporte.setHasta(ReportesUtils.dateToString(fechahasta));
            dataReporte.setImpuesto(impuesto.getNombre());
            dataReporte.setNitDelMinisterio(nitDelMinisterio.getCnfValor());

            Set<String> fuentes = new LinkedHashSet<>();
            List<QuedanEmitido> quedans = impuestoDAO.getQuedanEnProyecto(idProyecto, fechaDesde, fechahasta, idImpuesto);
            for (QuedanEmitido quedan : quedans) {
                for (ProcesoAdquisicionItem item : quedan.getActa().getContratoOC().getItems()) {
                    for (RelacionProAdqItemInsumo rel : item.getRelItemInsumos()) {
                        for (POMontoFuenteInsumo insumoF : rel.getInsumo().getPoInsumo().getMontosFuentes()) {
                            if (insumoF.getCertificadoDisponibilidadPresupuestariaAprobada() != null && insumoF.getCertificadoDisponibilidadPresupuestariaAprobada().booleanValue() == true) {
                                fuentes.add(insumoF.getFuente().getFuenteRecursos().getNombre());
                            }
                        }

                    }
                }
            }
            String titualrFuentes = TextUtils.join(", ", fuentes);
            dataReporte.setFuentes(titualrFuentes);

            BigDecimal globalRetencion = BigDecimal.ZERO;
            BigDecimal globalRetenido = BigDecimal.ZERO;
            BigDecimal globalTotal = BigDecimal.ZERO;

            dataReporte.setPagos(new LinkedList<DataTablaReporteRetencionDeImpuestosPorProyecto>());

            Configuracion calidadEnQueActua = configuracionBean.obtenerCnfPorCodigo(ConstantesConfiguracion.RETENCION_IMPUESTO_PROY_CALIDAD_ACTUA);
            Configuracion modalidad = configuracionBean.obtenerCnfPorCodigo(ConstantesConfiguracion.RETENCION_IMPUESTO_PROY_MODALIDAD);
            Configuracion codigoDocmento = configuracionBean.obtenerCnfPorCodigo(ConstantesConfiguracion.RETENCION_IMPUESTO_PROY_CODIGO_DOC);

            for (QuedanEmitido quedan : quedans) {
                Proveedor proveedor = quedan.getActa().getContratoOC().getProcesoAdquisicionProveedor().getProveedor();

                DataTablaReporteRetencionDeImpuestosPorProyecto item = new DataTablaReporteRetencionDeImpuestosPorProyecto();
                item.setNumero(ReportesUtils.getNumber(quedan.getActa().getNroActa()));
                item.setApellidos(proveedor.getApellidosRepresentante());
                item.setNombres(proveedor.getNombreRepresentante());
                item.setQuedan(ReportesUtils.getNumber(quedan.getNumeroComprobanteRecepcionPago().getId()));
                item.setNit(proveedor.getNitOferente());
                item.setRazonSocialODenominacion(ReportesUtils.getRazonSocialONombre(proveedor));

                BigDecimal totalRetenido = BigDecimal.ZERO;
                BigDecimal toalImpuesto = BigDecimal.ZERO;
                for (ValoresImpuestoQuedan val : quedan.getValoresImpuesto()) {
                    if (val.getImpuesto().equals(impuesto)) {
                        if (val.getValorRetencionEnPago() != null) {
                            totalRetenido = totalRetenido.add(val.getValorRetencionEnPago());
                        }
                        if (val.getValorImpuestoEnPAgo() != null) {
                            toalImpuesto = toalImpuesto.add(val.getValorImpuestoEnPAgo());
                        }
                    }

                }

                item.setIngresosSujetosDeRetencion(ReportesUtils.getNumber(quedan.getMontoSinImpuestos()));
                item.setImpuestoRetenido(ReportesUtils.getNumber(totalRetenido));
                item.setTotal(ReportesUtils.getNumber(toalImpuesto));

                globalRetencion = globalRetencion.add(quedan.getMontoSinImpuestos());
                globalRetenido = globalRetenido.add(totalRetenido);
                globalTotal = globalTotal.add(toalImpuesto);

                List<String> facturas = new LinkedList<>();
                for (Factura f : quedan.getActa().getFacturas()) {
                    String tipo = f.getTipo() == TipoFactura.FACTURA ? "F" : "R";
                    tipo = tipo + " " + f.getNumero();
                    facturas.add(tipo);
                }
                item.setNumeroDeFactura(TextUtils.join(", ", facturas));

                item.setCalidadEnQueActua(calidadEnQueActua.getCnfValor());
                item.setModalidad(modalidad.getCnfValor());
                item.setCodigoDocmento(codigoDocmento.getCnfValor());

                String numeroDocumento = "???";
                item.setNumeroDocumento(numeroDocumento);

                dataReporte.getPagos().add(item);
            }

            dataReporte.setTotalIngresosSujetosDeRetencion(ReportesUtils.getNumber(globalRetencion));
            dataReporte.setTotalImpuestoRetenido(ReportesUtils.getNumber(globalRetenido));
            dataReporte.setTotalTotal(ReportesUtils.getNumber(globalTotal));

            Map parcol = new HashMap();
            addLogoToMap(parcol);

            ClassLoader classLoader = getClass().getClassLoader();
            URL jasperFileNameURL = classLoader.getResource("reportes/retencionDeImpuestoPorProyecto.jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFileNameURL);

            return ReportesUtils.generarBytesPDF(dataReporte, parcol, jasperReport);

        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método genera el reporte de retención de impuestos por proveedor
     *
     * @param anio
     * @param idImpuesto
     * @return
     * @throws GeneralException
     */
    public byte[] generarRetencionDeImpuestosPorProveedor(Integer anio, Integer mes, Integer idImpuesto) throws GeneralException {
        try {
            Impuesto impuesto = (Impuesto) generalDAO.find(Impuesto.class, idImpuesto);
            Date fechaDesde = null;
            Date fechahasta = null;
            if (mes == null) {
                fechaDesde = DatesUtils.getStartOfYear(anio);
                fechahasta = DatesUtils.getEndOfYear(anio);
            } else {
                mes = mes - 1;
                fechaDesde = DatesUtils.getStartOfMonth(anio, mes);
                fechahasta = DatesUtils.getEndOfMonth(anio, mes);
            }

            String areaReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_AREA_RETENCION_IMPUESTO_POR_PROVEEDOR);
            String tituloReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_TITULO_RETENCION_IMPUESTO_POR_PROVEEDOR);

            DataReporteRetencionImpuestoPorProveedor dataReporte = (DataReporteRetencionImpuestoPorProveedor) this.inicializarReporteTemplate(tituloReporte, areaReporte, DataReporteRetencionImpuestoPorProveedor.class);

            dataReporte.setAnio(anio.toString());
            String nombreMes = null;
            if (mes != null) {
                SimpleDateFormat formateador = new SimpleDateFormat("MMMM", new Locale("es"));
                nombreMes = formateador.format(fechaDesde);
            }
            dataReporte.setMes(nombreMes);
            dataReporte.setImpuesto(impuesto.getNombre());

            BigDecimal globalRetencion = BigDecimal.ZERO;
            BigDecimal globalRetenido = BigDecimal.ZERO;
            BigDecimal globalTotal = BigDecimal.ZERO;

            //primero se agrupan los quedan por proveedor y se van cargando en un mapa
            List<QuedanEmitido> quedans = impuestoDAO.getQuedansPorImpuesto(fechaDesde, fechahasta, idImpuesto);
            Map<Integer, DataReporteRetencionImpuestoPorProveedorItemTemporal> map = new LinkedHashMap();
            for (QuedanEmitido quedan : quedans) {

                Proveedor proveedor = quedan.getActa().getContratoOC().getProcesoAdquisicionProveedor().getProveedor();
                DataReporteRetencionImpuestoPorProveedorItemTemporal item = map.get(proveedor.getId());
                if (item == null) {
                    item = new DataReporteRetencionImpuestoPorProveedorItemTemporal();
                    item.setProveedor(proveedor);
                    item.setImpuestoRetenidoAnual(BigDecimal.ZERO);
                    item.setIngresoSujetoDeRetencionAnual(BigDecimal.ZERO);
                    map.put(proveedor.getId(), item);
                }

                BigDecimal totalRetenido = BigDecimal.ZERO;
                BigDecimal toalImpuesto = BigDecimal.ZERO;
                for (ValoresImpuestoQuedan val : quedan.getValoresImpuesto()) {
                    if (val.getImpuesto().equals(impuesto)) {
                        if (val.getValorRetencionEnPago() != null) {
                            totalRetenido = totalRetenido.add(val.getValorRetencionEnPago());
                        }
                        if (val.getValorImpuestoEnPAgo() != null) {
                            toalImpuesto = toalImpuesto.add(val.getValorImpuestoEnPAgo());
                        }
                    }

                }

                item.setIngresoSujetoDeRetencionAnual(item.getIngresoSujetoDeRetencionAnual().add(quedan.getMontoSinImpuestos()));
                item.setImpuestoRetenidoAnual(item.getImpuestoRetenidoAnual().add(totalRetenido));

                globalRetencion = globalRetencion.add(quedan.getMontoSinImpuestos());
                globalRetenido = globalRetenido.add(totalRetenido);
                globalTotal = globalTotal.add(toalImpuesto);
            }

            dataReporte.setTotalIngresosSujetosDeRetencionAnual(ReportesUtils.getNumber(globalRetencion));
            dataReporte.setTotalImpuestoRetenidoAnual(ReportesUtils.getNumber(globalRetenido));

            //se agregan los valores al reporte
            dataReporte.setProveedores(new LinkedList());
            Integer numero = 1;

            for (Map.Entry<Integer, DataReporteRetencionImpuestoPorProveedorItemTemporal> entry : map.entrySet()) {
                DataReporteRetencionImpuestoPorProveedorItem itemReporte = new DataReporteRetencionImpuestoPorProveedorItem();
                itemReporte.setNumero(numero.toString());
                itemReporte.setNombreProveedor(ReportesUtils.getRazonSocialONombre(entry.getValue().getProveedor()));
                itemReporte.setNit(entry.getValue().getProveedor().getNitOferente());

                itemReporte.setImpuestoRetenidoAnual(ReportesUtils.getNumber(entry.getValue().getImpuestoRetenidoAnual()));
                itemReporte.setIngresoSujetoDeRetencionAnual(ReportesUtils.getNumber(entry.getValue().getIngresoSujetoDeRetencionAnual()));

                dataReporte.getProveedores().add(itemReporte);
                numero++;
            }

            Map parcol = new HashMap();
            addLogoToMap(parcol);

            ClassLoader classLoader = getClass().getClassLoader();
            URL jasperFileNameURL = classLoader.getResource("reportes/retencionImpuesoPorProveedor.jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFileNameURL);

            return ReportesUtils.generarBytesPDF(dataReporte, parcol, jasperReport);

        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método genera la constanca de retención de impuestos
     *
     * @param anio
     * @param idProveedor
     * @param idUsuario
     * @return
     * @throws GeneralException
     */
    public byte[] generarConstanciaRetencionDeImpuestos(Integer anio, Integer idProveedor, Integer idUsuario) throws GeneralException {
        try {

            Configuracion nitDelMinisterio = configuracionBean.obtenerCnfPorCodigo(ConstantesConfiguracion.NIT_DEL_MINISTERIO);
            Proveedor proveedor = (Proveedor) generalDAO.find(Proveedor.class, idProveedor);
            String paisProveedor = proveedor.getPais() != null ? proveedor.getPais() : "";
            String codigoDeIngreso = "";
            if (proveedor.getPersonaJuridica() != null && proveedor.getPersonaJuridica()) {
                codigoDeIngreso = ConstantesConfiguracion.CODIGO_INGRESO_PROVEEDOR_ES_PERSONA_JURIDICA;
            } else {
                codigoDeIngreso = ConstantesConfiguracion.CODIGO_INGRESO_PROVEEDOR_ES_PERSONA_FISICA;
            }

            //toma fecha desde como primer dia del año
            Date fechaDesde = DatesUtils.getStartOfYear(anio);
            Date fechahasta = DatesUtils.getEndOfYear(anio);

            BigDecimal montoSujetoDeRetencion = BigDecimal.ZERO;
            BigDecimal montoRetenido = BigDecimal.ZERO;
            //primero se agrupan los quedan por proveedor y se van cargando en un mapa
            List<QuedanEmitido> quedans = impuestoDAO.getQuedansPorProveedor(fechaDesde, fechahasta, idProveedor);
            for (QuedanEmitido quedan : quedans) {
                for (ValoresImpuestoQuedan val : quedan.getValoresImpuesto()) {
                    if (val.getValorRetencionEnPago() != null) {
                        montoRetenido = montoRetenido.add(val.getValorRetencionEnPago());
                    }
                }
                montoSujetoDeRetencion = montoSujetoDeRetencion.add(quedan.getMontoSinImpuestos());
            }

            String contenido = configuracionBean.obtenerCnfPorCodigo(ConstantesConfiguracion.CONTENIDO_CONSTANCIA_ANUAL_RETENCION_POR_PROVEEDOR).getCnfValor();
            Map<String, String> propiedades = new LinkedHashMap();
            propiedades.put("codigoDeIngreso", codigoDeIngreso);
            propiedades.put("nitMinisterio", nitDelMinisterio.getCnfValor());
            propiedades.put("nacionalidadProveedor", paisProveedor);
            propiedades.put("nitProveedor", proveedor.getNitOferente());
            propiedades.put("proveedor", ReportesUtils.getRazonSocialONombre(proveedor));
            propiedades.put("anio", anio.toString());
            propiedades.put("montoSujetoDeRetencion", ReportesUtils.getNumber(montoSujetoDeRetencion));
            propiedades.put("montoRetenido", ReportesUtils.getNumber(montoRetenido));
            propiedades.put("fechaActualEnDias", DateUtils.dateToWords2(new Date()));
            contenido = TextUtils.replaceTokens(contenido, propiedades);

            SsUsuario usuario = (SsUsuario) generalDAO.find(SsUsuario.class, idUsuario);
            String firmante = ReportesUtils.getNombreFirmante(usuario);

            String areaReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_AREA_CONST_ANUAL_RETENCION_IMPUESTO_POR_PROVEEDOR);
            String tituloReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_TITULO_CONST_ANUAL_RETENCION_IMPUESTO_POR_PROVEEDOR);

            DataRetencionImpuesoPorProveedor dataReporte = (DataRetencionImpuesoPorProveedor) this.inicializarReporteTemplate(tituloReporte, areaReporte, DataRetencionImpuesoPorProveedor.class);
            dataReporte.setContenido(contenido);
            dataReporte.setFirmante(firmante);
            dataReporte.setCargoFirmante(ReportesUtils.getString(usuario.getUsuCargo()));

            Map parcol = new HashMap();
            addLogoToMap(parcol);

            ClassLoader classLoader = getClass().getClassLoader();
            URL jasperFileNameURL = classLoader.getResource("reportes/ConstanciaAnualDeRetencionPorProveedor.jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFileNameURL);

            return ReportesUtils.generarBytesPDF(dataReporte, parcol, jasperReport);

        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método genera una ficha del contrato
     *
     * @param contratoId
     * @return
     */
    public byte[] generarFichaContrato(Integer contratoId) {
        try {
            ContratoOC contrato = (ContratoOC) generalDAO.findById(ContratoOC.class, contratoId);

            String areaReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_AREA_FICHA_CONTRATO);
            String tituloReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_TITULO_FICHA_CONTRATO);

            DataFichaContrato data = (DataFichaContrato) this.inicializarReporteTemplate(tituloReporte, areaReporte, DataFichaContrato.class);

            String simboloMoneda = "US$";
            String denominacionMoneda = "Dólares";
            Configuracion confDenominacionMoneda = configuracionBean.obtenerCnfPorCodigo(ConstantesConfiguracion.DENOMINACION_MONEDA);
            Configuracion confSimboloMoneda = configuracionBean.obtenerCnfPorCodigo(ConstantesConfiguracion.SIMBOLO_MONEDA);
            if (confDenominacionMoneda != null) {
                denominacionMoneda = confDenominacionMoneda.getCnfValor();
            }
            if (confSimboloMoneda != null) {
                simboloMoneda = confSimboloMoneda.getCnfValor();
            }
            data.setLeyendaPunto7("7. DATOS RELATIVOS A LA RESERVA: (" + denominacionMoneda + " " + simboloMoneda + ")");
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            data.setFechaReporte(format.format(new Date()));

            data.setRazonSocial(contrato.getProcesoAdquisicionProveedor().getProveedor().getRazonSocial());
            data.setNit(contrato.getProcesoAdquisicionProveedor().getProveedor().getNitOferente());
            data.setFuentes(obtenerFuente(contrato));
            data.setNroContrato(contrato.getNroContrato().toString());
            data.setTipoDocumento(contrato.getTipo().toString());
            data.setVigenciaDesde(format.format(contrato.getFechaInicio()));
            data.setVigenciaHasta(format.format(contrato.getFechaFin()));
            data.setMontoTotal(ReportesUtils.getNumber(contrato.getMontoAdjudicado()));
            data.setMontoConCambios(ReportesUtils.getNumber(contrato.getMontoAdjudicado()));
            data.setMontoModificado("0.00");
            data.setMontoConCambios(ReportesUtils.getNumber(contrato.getMontoAdjudicado()));
            data.setPacs(obtenerGruposPacs(contrato));
            data.setNroAdquisicion(contrato.getProcesoAdquisicion().getSecuenciaAnio() + " - " + contrato.getProcesoAdquisicion().getSecuenciaNumero());
            data.setMetodoAdquisicion(contrato.getProcesoAdquisicion().getMetodoAdquisicion().getNombre());
            data.setAdministradorContrato(contrato.getAdministrador().getUsuPrimerNombre() + " " + contrato.getAdministrador().getUsuPrimerApellido());

            BigDecimal montoTotalTotal = new BigDecimal("0");
            BigDecimal montoTotalGoes = BigDecimal.ZERO;//Montos provenientes de GOES
            BigDecimal montoTotalOtro = BigDecimal.ZERO;//Montos provenientes de las otras fuentes de recurso

            for (ProcesoAdquisicionItem item : contrato.getItems()) {
                //La lista de insumos del Item se obtiene de la tabla de relación entre insumos e item de los procesos de adquisición
                for (RelacionProAdqItemInsumo rel : item.getRelItemInsumos()) {
                    rel.getMontoTotalAdjudicado();//Costo del insumo real (TOTAL DE LA TABLA INSUMOS)
                    rel.getCantidadAdjudicada();//Cantidad adjudicada para el insumo (Es lo que se termina comprando)
                    rel.getInsumo().getUnidadTecnica().getNombre();//UT de la tabla de insumos
                    rel.getInsumo().getInsumo().getCodigo();//Código del insumo del catálogo de insumos.
                    rel.getInsumo().getInsumo().getObjetoEspecificoGasto().getClasificador();//Objeto específico de gasto
                    rel.getInsumo().getObservacion();//Descripción del insumo del proceso de adquisición.
                    DataInsumo2 dataInsumo = new DataInsumo2();
                    dataInsumo.setCantidad(rel.getCantidadAdjudicada().toString());
                    dataInsumo.setCodigoUaci(rel.getInsumo().getInsumo().getCodigo());
                    String descripcionInsumo = "";
                    if (rel.getInsumo() != null && rel.getInsumo().getPoInsumo() != null && rel.getInsumo().getPoInsumo().getObservacion() != null) {
                        descripcionInsumo = rel.getInsumo().getPoInsumo().getObservacion();
                    }
                    dataInsumo.setDescripcionInsumo(descripcionInsumo);
                    dataInsumo.setNombreUt(rel.getInsumo().getUnidadTecnica().getNombre());

                    BigDecimal montoGoesPoInumo = BigDecimal.ZERO;//Montos provenientes de GOES
                    BigDecimal montoOtroPoInumo = BigDecimal.ZERO;
                    CompromisoPresupuestario compromiso = null;
                    if(contrato.getProcesoAdquisicion().getCompromisosPresupuestarios()!=null && !contrato.getProcesoAdquisicion().getCompromisosPresupuestarios().isEmpty()){
                        compromiso = contrato.getProcesoAdquisicion().getCompromisosPresupuestarios().get(0);
                    }
                    List<DataDistribuccionProgramacionPagosContrato> res = ContratoUtils.getDistribuccionFinanciera(compromiso);
                    for (DataDistribuccionProgramacionPagosContrato distContrato : res) {
                        for (DistribucionProgramacionPagosMes distMes : distContrato.getDistribuccionMeses()) {
                            for (DistribucionProgramacionPagosItem distItem : distMes.getDistribuccionItems()) {
                                for (DistribucionProgramacionPagosInsumos distInsumo : distItem.getDistribuccionInsumos()) {
                                    if (distInsumo.getInsumo().getId().equals(rel.getId())) {
                                        for (DistribucionMontoAdjudicado distFuente : distInsumo.getDistribuccion()) {
                                            POMontoFuenteInsumo fuenteMonto = distFuente.getFuente();
                                            if (InsumoUtils.esMontoDeGOES(fuenteMonto)) {
                                                if (fuenteMonto.getReservaFondos() != null) {
                                                    montoGoesPoInumo = montoGoesPoInumo.add(distFuente.getMonto());
                                                }
                                            } else {
                                                if (fuenteMonto.getReservaFondos() != null) {
                                                    montoOtroPoInumo = montoOtroPoInumo.add(distFuente.getMonto());
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    dataInsumo.setGoes(ReportesUtils.getNumber(montoGoesPoInumo));
                    dataInsumo.setOtro(ReportesUtils.getNumber(montoOtroPoInumo));

                    montoTotalGoes = montoTotalGoes.add(montoGoesPoInumo);
                    montoTotalOtro = montoTotalOtro.add(montoOtroPoInumo);

                    dataInsumo.setMontoTotalGoes(ReportesUtils.getNumber(montoTotalGoes));
                    dataInsumo.setMontoTotalOtro(ReportesUtils.getNumber(montoTotalOtro));

                    dataInsumo.setTotal(ReportesUtils.getNumber(new BigDecimal("0.00").add(rel.getMontoTotalAdjudicado())));
                    montoTotalTotal = montoTotalTotal.add(rel.getMontoTotalAdjudicado());
                    dataInsumo.setMontoTotalTotal(ReportesUtils.getNumber(montoTotalTotal));
                    data.getInsumos().add(dataInsumo);
                }

            }

            data.setMontoGoes(NumberUtils.nomberToString(montoTotalGoes));
            data.setMontoOtro(NumberUtils.nomberToString(montoTotalOtro));

            BigDecimal montoTotalActasAnticipoYDevolucion = new BigDecimal("0");
            BigDecimal montoTotalActasRecepcion = new BigDecimal("0");
            BigDecimal montoTotalQuedan = new BigDecimal("0");
            for (ActaContrato acta : contrato.getPagos()) {
                if (acta.getEstado().equals(EstadoActa.APROBADA)) {
                    DataActa dataActa = new DataActa();
                    dataActa.setDescripcion(obtenerDescripcionActa(acta));
                    dataActa.setFechaEmision(format.format(acta.getFechaGeneracion()));
                    dataActa.setMonto(ReportesUtils.getNumber(acta.getMontoRecibido()));
                    dataActa.setNroActa(acta.getNroActa().toString());
                    dataActa.setTipoActa(acta.getTipo().toString());
                    if (acta.getTipo().equals(TipoActaContrato.RECEPCION)) {
                        montoTotalActasRecepcion = montoTotalActasRecepcion.add(acta.getMontoRecibido());
                        dataActa.setMontoTotal(ReportesUtils.getNumber(montoTotalActasRecepcion));
                        data.getActasRecepcion().add(dataActa);
                    } else {
                        montoTotalActasAnticipoYDevolucion = montoTotalActasAnticipoYDevolucion.add(acta.getMontoRecibido());
                        dataActa.setMontoTotal(ReportesUtils.getNumber(montoTotalActasAnticipoYDevolucion));
                        data.getActasAnticipoYDevolucion().add(dataActa);
                    }

                    if (acta.getQuedanEmitido() != null && acta.getQuedanEmitido()) {
                        DataQuedanEmitido dataQuedan = new DataQuedanEmitido();
                        dataQuedan.setFechaEmision(format.format(acta.getQuedan().getFechaGeneracion()));
                        dataQuedan.setMontoTotal(ReportesUtils.getNumber(acta.getQuedan().getMontoquedan()));
                        dataQuedan.setNroActa(acta.getNroActa().toString());
                        dataQuedan.setNroComprobante(acta.getQuedan().getNumeroComprobanteRecepcionPago().getId().toString());
                        montoTotalQuedan = montoTotalQuedan.add(acta.getQuedan().getMontoquedan());
                        dataQuedan.setTotalTotal(ReportesUtils.getNumber(montoTotalQuedan));
                        data.getQuedan().add(dataQuedan);
                    }
                }
            }

            data.setExistenActasAnticipoYDevolucion(data.getActasAnticipoYDevolucion() != null && !data.getActasAnticipoYDevolucion().isEmpty());
            data.setExistenActasRecepcion(data.getActasRecepcion() != null && !data.getActasRecepcion().isEmpty());
            data.setExistenQuedanEmitidos(data.getQuedan() != null && !data.getQuedan().isEmpty());

            ClassLoader classLoader = getClass().getClassLoader();
            URL jasperFileNameURL = classLoader.getResource("reportes/FichaContrato.jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFileNameURL);

            Map parcol = new HashMap();
            addLogoToMap(parcol);

            return ReportesUtils.generarBytesPDF(data, parcol, jasperReport);

        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método genera el reporte de compromiso presupuestario de un proceso
     * o una modificativa
     *
     * @param idCompromiso
     * @return
     */
    public byte[] generarCompromisoPresupuestario(Integer idCompromiso) {
        try {
            CompromisoPresupuestario compromiso = (CompromisoPresupuestario) generalDAO.findById(CompromisoPresupuestario.class, idCompromiso);

            String areaReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_AREA_COMPROMISO_PRESUPUESTARIO);
            String tituloReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_TITULO_COMPROMISO_PRESUPUESTARIO);

            DataReporteCompromisoPresupuestario reporte = (DataReporteCompromisoPresupuestario) this.inicializarReporteTemplate(tituloReporte, areaReporte, DataReporteCompromisoPresupuestario.class);

            Set<String> conveniosProyecto = new HashSet<>();
            Set<String> organismosProyecto = new HashSet<>();

            reporte.setConcepto("?????????");
            reporte.setNoCompromiso(compromiso.getId().toString());
            reporte.setConvenio("??????????");
            reporte.setFechaEmision(ReportesUtils.dateToString(compromiso.getFechaEmision()));
            reporte.setOrganismo("??????????");
            reporte.setEstado(compromiso.getEstado().toString());

            reporte.setInsumos(new LinkedList());

            List<ContratoOC> contratosEnCompromiso = new LinkedList();
            if (compromiso.getTipo() == TipoCompromisoPresupuestario.PROCESO) {
                CompromisoPresupuestarioProceso compromisoProceso = (CompromisoPresupuestarioProceso) compromiso;

                CompromisoPresupuestarioProceso compromisoPresupuestarioProceso = (CompromisoPresupuestarioProceso) compromiso;
                contratosEnCompromiso.addAll(compromisoPresupuestarioProceso.getProcesoAdquisicion().getContratos());
                reporte.setNroProceso(compromisoProceso.getProcesoAdquisicion().getSecuenciaAnio() + " - " + compromisoProceso.getProcesoAdquisicion().getSecuenciaNumero());
                reporte.setTipo("Proceso");
                reporte.setEjercicioPresupuestario(compromisoProceso.getProcesoAdquisicion().getSecuenciaAnio() + "");
            } else {
                CompromisoPresupuestarioModificativa compromisoModificativa = (CompromisoPresupuestarioModificativa) compromiso;
                CompromisoPresupuestarioModificativa compromisoPresupuestarioModificativa = (CompromisoPresupuestarioModificativa) compromiso;
                contratosEnCompromiso.add(compromisoPresupuestarioModificativa.getModificativaContrato().getContratoOC());
                reporte.setTipo("Modificativa");
                reporte.setNroContrato(compromisoModificativa.getModificativaContrato().getContratoOC().getNroAnio() + " - " + compromisoModificativa.getModificativaContrato().getContratoOC().getNroContrato());
                reporte.setEjercicioPresupuestario(compromisoModificativa.getModificativaContrato().getContratoOC().getProcesoAdquisicion().getSecuenciaAnio() + "");
            }

            BigDecimal total = BigDecimal.ZERO;

            List<DataFlujoCajaAnioParaReporte> listaDataFCA = new LinkedList<>();

            List<DataDistribuccionProgramacionPagosContrato> listaDataDistProgPagosCont = ContratoUtils.getDistribuccionFinanciera(compromiso);
            for (DataDistribuccionProgramacionPagosContrato dataDistProgPagosContrato : listaDataDistProgPagosCont) {

                ContratoOC contrato = dataDistProgPagosContrato.getContrato();
                String noContrat = contrato.getNroContrato() + "";
                String nitProveedor = contrato.getProcesoAdquisicionProveedor().getProveedor().getNitOferente();

                for (DistribucionProgramacionPagosMes distProgPagosMes : dataDistProgPagosContrato.getDistribuccionMeses()) {

                    for (DistribucionProgramacionPagosItem distProgPagosItem : distProgPagosMes.getDistribuccionItems()) {

                        for (DistribucionProgramacionPagosInsumos distProgPagosInsumo : distProgPagosItem.getDistribuccionInsumos()) {

                            BigDecimal montoInsumo = BigDecimal.ZERO;
                            Set<String> codigosCategorias = new HashSet<>();

                            for (DistribucionMontoAdjudicado distMontoAdjudicado : distProgPagosInsumo.getDistribuccion()) {

                                if (distMontoAdjudicado.getMonto() != null && distMontoAdjudicado.getMonto().compareTo(BigDecimal.ZERO) != 0) {
                                    montoInsumo = montoInsumo.add(distMontoAdjudicado.getMonto());

                                    total = total.add(distMontoAdjudicado.getMonto());
                                    FuenteRecursos fr = distMontoAdjudicado.getFuente().getFuente().getFuenteRecursos();
                                    organismosProyecto.add(fr.getNombre());
                                    if (distMontoAdjudicado.getFuente().getFuente().getCategoriaConvenio() != null
                                            && distMontoAdjudicado.getFuente().getFuente().getCategoriaConvenio().getCodigo() != null) {
                                        codigosCategorias.add(distMontoAdjudicado.getFuente().getFuente().getCategoriaConvenio().getCodigo());
                                    }

                                }

                            }

                            DataReporteCompromisoPresupuestarioInsumo iter = new DataReporteCompromisoPresupuestarioInsumo();

                            ProcesoAdquisicionInsumo insumo = distProgPagosInsumo.getInsumo().getInsumo();

                            String oeg = insumo.getInsumo().getObjetoEspecificoGasto() != null ? insumo.getInsumo().getObjetoEspecificoGasto().getClasificador() + "" : "";
                            String codInsumo = insumo.getInsumo().getCodigo();
                            String montoMes = NumberUtils.nomberToString(montoInsumo);

                            iter.setNoContrato(noContrat);
                            iter.setProveedor(nitProveedor);
                            iter.setOeg(oeg);
                            iter.setLineaPresupuestaria("?????");
                            iter.setMes(distProgPagosMes.getAnio() + "-" + distProgPagosMes.getMes());
                            iter.setMonto(montoMes);
                            iter.setCategoria(TextUtils.join(", ", codigosCategorias));
                            iter.setInsumo(codInsumo);
                            iter.setPoInsumo(distProgPagosInsumo.getInsumo().getInsumo().getPoInsumo().getId() + "");

                            reporte.getInsumos().add(iter);

                            if (distProgPagosInsumo.getInsumo().getInsumo().getPoInsumo().getPoa().getTipo() == TipoPOA.POA_PROYECTO) {
                                POAProyecto poa = (POAProyecto) distProgPagosInsumo.getInsumo().getInsumo().getPoInsumo().getPoa();
                                if (poa.getProyecto() != null && poa.getProyecto().getConvenio() != null
                                        && poa.getProyecto().getConvenio().getNombre() != null) {
                                    conveniosProyecto.add(poa.getProyecto().getConvenio().getNombre());
                                }
                            }

                            DataFlujoCajaAnioParaReporte dataFCA = null;
                            boolean encontroAnio = false;
                            Iterator<DataFlujoCajaAnioParaReporte> itDataFCA = listaDataFCA.iterator();
                            while (itDataFCA.hasNext() && !encontroAnio) {
                                DataFlujoCajaAnioParaReporte unDataFCA = itDataFCA.next();
                                String anio = unDataFCA.getAnio();
                                if (distProgPagosMes.getAnio().toString().equals(anio)) {
                                    encontroAnio = true;
                                    dataFCA = unDataFCA;
                                }
                            }
                            if (!encontroAnio) {
                                dataFCA = new DataFlujoCajaAnioParaReporte();
                                dataFCA.setAnio(distProgPagosMes.getAnio().toString());

                                dataFCA.setMontoMes1(NumberUtils.nomberToString(BigDecimal.ZERO));
                                dataFCA.setMontoMes2(NumberUtils.nomberToString(BigDecimal.ZERO));
                                dataFCA.setMontoMes3(NumberUtils.nomberToString(BigDecimal.ZERO));
                                dataFCA.setMontoMes4(NumberUtils.nomberToString(BigDecimal.ZERO));
                                dataFCA.setMontoMes5(NumberUtils.nomberToString(BigDecimal.ZERO));
                                dataFCA.setMontoMes6(NumberUtils.nomberToString(BigDecimal.ZERO));
                                dataFCA.setMontoMes7(NumberUtils.nomberToString(BigDecimal.ZERO));
                                dataFCA.setMontoMes8(NumberUtils.nomberToString(BigDecimal.ZERO));
                                dataFCA.setMontoMes9(NumberUtils.nomberToString(BigDecimal.ZERO));
                                dataFCA.setMontoMes10(NumberUtils.nomberToString(BigDecimal.ZERO));
                                dataFCA.setMontoMes11(NumberUtils.nomberToString(BigDecimal.ZERO));
                                dataFCA.setMontoMes12(NumberUtils.nomberToString(BigDecimal.ZERO));

                                listaDataFCA.add(dataFCA);
                            }

                            switch (distProgPagosMes.getMes()) {
                                case 1:
                                    if (dataFCA.getMontoMes1BD() != null) {
                                        dataFCA.setMontoMes1BD(dataFCA.getMontoMes1BD().add(montoInsumo));
                                    } else {
                                        dataFCA.setMontoMes1BD(montoInsumo);
                                    }
                                    dataFCA.setMontoMes1(NumberUtils.nomberToString(dataFCA.getMontoMes1BD()));
                                    break;
                                case 2:
                                    if (dataFCA.getMontoMes2BD() != null) {
                                        dataFCA.setMontoMes2BD(dataFCA.getMontoMes2BD().add(montoInsumo));
                                    } else {
                                        dataFCA.setMontoMes2BD(montoInsumo);
                                    }
                                    dataFCA.setMontoMes2(NumberUtils.nomberToString(dataFCA.getMontoMes2BD()));
                                    break;
                                case 3:
                                    if (dataFCA.getMontoMes3BD() != null) {
                                        dataFCA.setMontoMes3BD(dataFCA.getMontoMes3BD().add(montoInsumo));
                                    } else {
                                        dataFCA.setMontoMes3BD(montoInsumo);
                                    }
                                    dataFCA.setMontoMes3(NumberUtils.nomberToString(dataFCA.getMontoMes3BD()));
                                    break;
                                case 4:
                                    if (dataFCA.getMontoMes4BD() != null) {
                                        dataFCA.setMontoMes4BD(dataFCA.getMontoMes4BD().add(montoInsumo));
                                    } else {
                                        dataFCA.setMontoMes4BD(montoInsumo);
                                    }
                                    dataFCA.setMontoMes4(NumberUtils.nomberToString(dataFCA.getMontoMes4BD()));
                                    break;
                                case 5:
                                    if (dataFCA.getMontoMes5BD() != null) {
                                        dataFCA.setMontoMes5BD(dataFCA.getMontoMes5BD().add(montoInsumo));
                                    } else {
                                        dataFCA.setMontoMes5BD(montoInsumo);
                                    }
                                    dataFCA.setMontoMes5(NumberUtils.nomberToString(dataFCA.getMontoMes5BD()));
                                    break;
                                case 6:
                                    if (dataFCA.getMontoMes6BD() != null) {
                                        dataFCA.setMontoMes6BD(dataFCA.getMontoMes6BD().add(montoInsumo));
                                    } else {
                                        dataFCA.setMontoMes6BD(montoInsumo);
                                    }
                                    dataFCA.setMontoMes6(NumberUtils.nomberToString(dataFCA.getMontoMes6BD()));
                                    break;
                                case 7:
                                    if (dataFCA.getMontoMes7BD() != null) {
                                        dataFCA.setMontoMes7BD(dataFCA.getMontoMes7BD().add(montoInsumo));
                                    } else {
                                        dataFCA.setMontoMes7BD(montoInsumo);
                                    }
                                    dataFCA.setMontoMes7(NumberUtils.nomberToString(dataFCA.getMontoMes7BD()));
                                    break;
                                case 8:
                                    if (dataFCA.getMontoMes8BD() != null) {
                                        dataFCA.setMontoMes8BD(dataFCA.getMontoMes8BD().add(montoInsumo));
                                    } else {
                                        dataFCA.setMontoMes8BD(montoInsumo);
                                    }
                                    dataFCA.setMontoMes8(NumberUtils.nomberToString(dataFCA.getMontoMes8BD()));
                                    break;
                                case 9:
                                    if (dataFCA.getMontoMes9BD() != null) {
                                        dataFCA.setMontoMes9BD(dataFCA.getMontoMes9BD().add(montoInsumo));
                                    } else {
                                        dataFCA.setMontoMes9BD(montoInsumo);
                                    }
                                    dataFCA.setMontoMes9(NumberUtils.nomberToString(dataFCA.getMontoMes9BD()));
                                    break;
                                case 10:
                                    if (dataFCA.getMontoMes10BD() != null) {
                                        dataFCA.setMontoMes10BD(dataFCA.getMontoMes10BD().add(montoInsumo));
                                    } else {
                                        dataFCA.setMontoMes10BD(montoInsumo);
                                    }
                                    dataFCA.setMontoMes10(NumberUtils.nomberToString(dataFCA.getMontoMes10BD()));
                                    break;
                                case 11:
                                    if (dataFCA.getMontoMes11BD() != null) {
                                        dataFCA.setMontoMes11BD(dataFCA.getMontoMes11BD().add(montoInsumo));
                                    } else {
                                        dataFCA.setMontoMes11BD(montoInsumo);
                                    }
                                    dataFCA.setMontoMes11(NumberUtils.nomberToString(dataFCA.getMontoMes11BD()));
                                    break;
                                case 12:
                                    if (dataFCA.getMontoMes12BD() != null) {
                                        dataFCA.setMontoMes12BD(dataFCA.getMontoMes12BD().add(montoInsumo));
                                    } else {
                                        dataFCA.setMontoMes12BD(montoInsumo);
                                    }
                                    dataFCA.setMontoMes12(NumberUtils.nomberToString(dataFCA.getMontoMes12BD()));
                                    break;
                                default:
                                    break;
                            }

                        }
                    }
                }
            }

            reporte.setConvenio(TextUtils.join(", ", conveniosProyecto));
            reporte.setOrganismo(TextUtils.join(", ", organismosProyecto));

            reporte.setTotal(NumberUtils.nomberToString(total));

            Collections.sort(listaDataFCA, new Comparator<DataFlujoCajaAnioParaReporte>() {
                @Override
                public int compare(DataFlujoCajaAnioParaReporte o1, DataFlujoCajaAnioParaReporte o2) {
                    return o1.getAnio().compareTo(o2.getAnio());
                }
            });
            reporte.setAniosProgramacionFinanciera(listaDataFCA);

            ClassLoader classLoader = getClass().getClassLoader();
            URL jasperFileNameURL = classLoader.getResource("reportes/compromisoPresupuestario.jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFileNameURL);

            Map parcol = new HashMap();
            addLogoToMap(parcol);

            return ReportesUtils.generarBytesPDF(reporte, parcol, jasperReport);

        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    private void addLogoToMap(Map parcol) {
        try {
            Configuracion confLogo = configuracionBean.obtenerCnfPorCodigo(ConstantesConfiguracion.REPORTE_RUTA_LOGO);
            File rutaLogo = new File(confLogo.getCnfValor());
            if (rutaLogo.exists()) {
                parcol.put("LOGO_PATH", "file:///" + confLogo.getCnfValor().trim());
            }
        } catch (Exception e) {
            logger.getLogger(this.getClass().getName()).log(Level.SEVERE, errorAlObtenerLogoReporte + e.getMessage(), e);
        }
    }

    /**
     * Este método genera el reporte de reprogramación
     *
     * @param reprogramacionId
     * @return
     */
    public byte[] generarReporteReprogramacion(Integer reprogramacionId) {
        try {
            Reprogramacion reprogramacion = (Reprogramacion) generalDAO.find(Reprogramacion.class,
                    reprogramacionId);

            String areaReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_AREA_REPROGRAMACION);
            String tituloReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_TITULO_COMPROMISO_REPROGRAMACION);

            DataReporteReprogramacion reporte = (DataReporteReprogramacion) this.inicializarReporteTemplate(tituloReporte, areaReporte, DataReporteReprogramacion.class
            );
            reporte.setTipoPOA(reprogramacion.getTipoReprogramacion().getTexto());
            reporte.setNombre("----");

            List<String> uts = new LinkedList();
            List<String> nombres = new LinkedList();
            for (ReprogramacionDetalle det : reprogramacion.getRep_detalle_lista()) {
                uts.add(det.getPoa().getUnidadTecnica().getNombre());
                if (det.getPoa().getTipo() == TipoPOA.POA_PROYECTO) {
                    POAProyecto poaP = (POAProyecto) det.getPoa();
                    nombres.add(poaP.getProyecto().getNombre());
                }

                if (det.getPoa().getTipo() == TipoPOA.POA_CON_ACTIVIDADES) {
                    POAConActividades poActividades = (POAConActividades) det.getPoa();
                    nombres.add(poActividades.getConMontoPorAnio().getNombre());
                }
            }
            reporte.setUnidadEjecutora(TextUtils.join(", ", uts));
            reporte.setNombre(TextUtils.join(", ", nombres));

            SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
            String fecha = formateador.format(reprogramacion.getUltMod());
            reporte.setFechaReprogramacion(fecha);
            reporte.setNumeroReprogramacion(reprogramacion.getId().toString());
            reporte.setEstado(reprogramacion.getEstado().getTexto());

            reporte.setListaInsumos(new LinkedList<DataReporteReprogramacionInsumos>());

            BigDecimal montoTotal = BigDecimal.ZERO;
            for (ReprogramacionDetalle detalle : reprogramacion.getRep_detalle_lista()) {
                DataReporteReprogramacionInsumos dataInsumo = new DataReporteReprogramacionInsumos();
                String nombreActividad = "---";
                if (detalle.getPoaActividad() != null) {
                    nombreActividad = detalle.getPoaActividad().getNombre();
                } else if (detalle.getNuevaActividad() != null) {
                    nombreActividad = detalle.getActividadNuevaAccionCentral();
                }
                dataInsumo.setActividad(nombreActividad);
                String descripcionInsumo = "----";
                if (detalle.getPoaInsumo() != null && detalle.getPoaInsumo().getInsumo() != null) {
                    descripcionInsumo = detalle.getPoaInsumo().getInsumo().getDescripcion() != null ? detalle.getPoaInsumo().getInsumo().getDescripcion() : "----";
                    dataInsumo.setOEG(detalle.getPoaInsumo().getInsumo().getObjetoEspecificoGasto().getClasificador().toString());
                } else if (detalle.getInsumoNuevo() != null) {
                    descripcionInsumo = detalle.getInsumoNuevo().getDescripcion() != null ? detalle.getInsumoNuevo().getDescripcion() : "--";
                    dataInsumo.setOEG("----");
                    if (detalle.getInsumoNuevo().getObjetoEspecificoGasto() != null) {
                        dataInsumo.setOEG(detalle.getInsumoNuevo().getObjetoEspecificoGasto().getDescripcion());
                    }
                }
                dataInsumo.setDescripcion(descripcionInsumo);
                dataInsumo.setFuenteRecurso("");
                dataInsumo.setMonto(NumberUtils.nomberToString(detalle.getDiferencia()));
                if (detalle.getPoaInsumo() != null) {
                    dataInsumo.setInsumo(detalle.getPoaInsumo().getId().toString());
                }
                if (detalle.getInsumoNuevoTotal() != null) {
                    montoTotal = montoTotal.add(detalle.getInsumoNuevoTotal());
                }
                reporte.getListaInsumos().add(dataInsumo);
            }
            reporte.setTotal(NumberUtils.nomberToString(montoTotal));

            ClassLoader classLoader = getClass().getClassLoader();
            URL jasperFileNameURL = classLoader.getResource("reportes/Reprogramacion.jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFileNameURL);

            Map parcol = new HashMap();
            addLogoToMap(parcol);

            return ReportesUtils.generarBytesPDF(reporte, parcol, jasperReport);

        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }

    }

    public byte[] generarReportePlanOperativoAnual(Integer poaId) {
        try{
            List<DataReportePlanOperativoAnual> listaData = new ArrayList<>();
            POA poa = (POA) generalDAO.find(POA.class,poaId);
            String responsable = "";
            String cargo = "";
            if(poa.getUnidadTecnica() != null && poa.getUnidadTecnica().getUniUsuario() != null ) {
                responsable = poa.getUnidadTecnica().getUniUsuario().getNombresApellidosCompletons();
                cargo = poa.getUnidadTecnica().getUniUsuario().getUsuCargo();
            }
            
            Integer trimestreHabilitado = 0;
            if(poa.getAnio() != null && poa.getAnio().getAnio() > 0) {
                ProgramacionTrimestral prog = ptBean.obtenerPorAnioFiscal(poa.getAnio().getId());
                if(prog != null) {
                    LocalDateTime dateActual = LocalDateTime.now().withHour(12).withMinute(0).withSecond(0);
                    LocalDateTime dateDesde = null;
                    LocalDateTime dateHasta = null;
                    if(prog.getFechaDesdePrimerTrimestre() != null && prog.getFechaHastaPrimerTrimestre() != null) {
                        dateDesde = LocalDateTime.ofInstant(prog.getFechaDesdePrimerTrimestre().toInstant(),ZoneId.systemDefault()).withHour(0).withMinute(0).withSecond(0);
                        dateHasta = LocalDateTime.ofInstant(prog.getFechaHastaPrimerTrimestre().toInstant(),ZoneId.systemDefault()).withHour(23).withMinute(59).withSecond(59);
                        
                        if(dateActual.isAfter(dateDesde) && dateActual.isBefore(dateHasta)) {
                            trimestreHabilitado = 1;
                        }
                    }
                    
                    if(prog.getFechaDesdeSegundoTrimestre() != null && prog.getFechaHastaSegundoTrimestre()!= null) {
                        
                        dateDesde = LocalDateTime.ofInstant(prog.getFechaDesdeSegundoTrimestre().toInstant(),ZoneId.systemDefault()).withHour(0).withMinute(0).withSecond(0);
                        dateHasta = LocalDateTime.ofInstant(prog.getFechaHastaSegundoTrimestre().toInstant(),ZoneId.systemDefault()).withHour(23).withMinute(59).withSecond(59);
                        
                        if(dateActual.isAfter(dateDesde) && dateActual.isBefore(dateHasta)) {
                            trimestreHabilitado = 2;
                        }
                    }
                    
                    if(prog.getFechaDesdeTercerTrimestre() != null && prog.getFechaHastaTercerTrimestre() != null) {
                        dateDesde = LocalDateTime.ofInstant(prog.getFechaDesdeTercerTrimestre().toInstant(),ZoneId.systemDefault()).withHour(0).withMinute(0).withSecond(0);
                        dateHasta = LocalDateTime.ofInstant(prog.getFechaHastaTercerTrimestre().toInstant(),ZoneId.systemDefault()).withHour(23).withMinute(59).withSecond(59);
                        
                        if(dateActual.isAfter(dateDesde) && dateActual.isBefore(dateHasta)) {
                            trimestreHabilitado = 3;
                        }
                    }
                    
                    if(prog.getFechaDesdeCuartoTrimestre() != null && prog.getFechaHastaCuartoTrimestre() != null) {
                        dateDesde = LocalDateTime.ofInstant(prog.getFechaDesdeCuartoTrimestre().toInstant(),ZoneId.systemDefault()).withHour(0).withMinute(0).withSecond(0);
                        dateHasta = LocalDateTime.ofInstant(prog.getFechaHastaCuartoTrimestre().toInstant(),ZoneId.systemDefault()).withHour(23).withMinute(59).withSecond(59);
                        
                        if(dateActual.isAfter(dateDesde) && dateActual.isBefore(dateHasta)) {
                            trimestreHabilitado = 4;
                        }
                    }
                }
            }
            
            
            //String areaReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_AREA_REPROGRAMACION);
            String tituloReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_TITULO_METAS_POA);

          
            String trimestre = trimestreHabilitado == null || trimestreHabilitado.compareTo(0)==0 ? "" : trimestreHabilitado.toString();
            
            String objetivo = poa.getProgramaInstitucional() != null && poa.getProgramaInstitucional().getPlanificacionEstrategica() != null 
                    && poa.getProgramaInstitucional().getPlanificacionEstrategica().getObjetivo() != null ? poa.getProgramaInstitucional().getPlanificacionEstrategica().getObjetivo().trim() : "";
 
            
            if(poa.getMetasAnuales() != null && !poa.getMetasAnuales().isEmpty()) {
                for(POAMetaAnual meta : poa.getMetasAnuales()) {
                    DataReportePlanOperativoAnual reporte = (DataReportePlanOperativoAnual) this.inicializarReporteTemplate("", "", DataReportePlanOperativoAnual.class);
                    reporte.setDireccion(meta.getPoa().getUnidadTecnica().getNombre().trim());
                    reporte.setAnio(meta.getPoa().getAnio().getAnio().toString());
                    reporte.setMeta(meta.getMetaAnual());
                    reporte.setAvancesLimitantes(meta.getAlcanceLimitaciones());
                    reporte.setTituloReporte(tituloReporte);
                    reporte.setObjetivo(objetivo);
                    reporte.setResponsable(responsable);
                    reporte.setCargoResponsable(cargo);
                    reporte.setTrimestre(trimestre);
                    reporte.setMedioVerificacion(meta.getMedioVerificacion());
                    reporte.setT1(meta.getProgramaPrimerTrimestre().toString());
                    reporte.setT1Real(meta.getProgramaPrimerTrimestreReal().toString());
                    
                    reporte.setT2(meta.getProgramaSegundoTrimestre().toString());
                    reporte.setT2Real(meta.getProgramaSegundoTrimestreReal().toString());
                    
                    reporte.setT3(meta.getProgramaTercerTrimestre().toString());
                    reporte.setT3Real(meta.getProgramaTercerTrimestreReal().toString());
                    
                    reporte.setT4(meta.getProgramaCuartoTrimestre().toString());
                    reporte.setT4Real(meta.getProgramaCuartoTrimestreReal().toString());
                    
                    reporte.setTotal(meta.getTotal().toString());
                    reporte.setTotalReal(meta.getTotalReal().toString());
                    listaData.add(reporte);
                }
            }
            
            ClassLoader classLoader = getClass().getClassLoader();
            URL jasperFileNameURL = classLoader.getResource("reportes/MetasPOA.jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFileNameURL);

            Map parcol = new HashMap();
            addLogoToMap(parcol);

            return ReportesUtils.generarBytesPDF(listaData, parcol, jasperReport);
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    /**
     * Obtiene los datos genéricos de los reportes utilizados en el cabezal y el
     * pie.
     *
     * @param tituloReporte: Título que se setea especificamente para cada
     * reporte
     * @param clase
     * @return
     */
    public DataReporteTemplate inicializarReporteTemplate(String tituloReporte, String areaMinisterio, Class clase) {
        try {
            Configuracion tituloMinisterioReporte = configuracionBean.obtenerCnfPorCodigo(ConstantesConfiguracion.TITULO_MINISTERIO_REPORTES);
            DateFormat df = new SimpleDateFormat(formatoFechaConHora);
            String fechaHoraImpresionReporte = df.format(new Date());
            Configuracion monedaMontos = configuracionBean.obtenerCnfPorCodigo(ConstantesConfiguracion.ACLARACION_MONEDA_MONTOS_REPORTES);

            DataReporteTemplate template = (DataReporteTemplate) clase.newInstance();

            template.setTituloMinisterio(tituloMinisterioReporte.getCnfValor());
            template.setTituloAreaMinisterio(areaMinisterio);
            template.setFechaHoraImpresionReporte(fechaHoraImpresionReporte);
            template.setTituloNombreReporte(tituloReporte);
            template.setUsuarioImprimeReporte(datosUsuario.getCodigoUsuario());
            template.setAclaracionMonedaMontos(monedaMontos.getCnfValor());

            return template;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Carga el TO utilizado en reportes de disponibilidad presupuestaria, con
     * los datos de un Monto fuente
     *
     * @param poa
     * @param monto
     * @return
     */
    private DataReporteDisponibilidadPresupuestaria cargarDataReporteDisponibilidadPresupuestariaParaPOMontoFuenteInsumo(POMontoFuenteInsumo monto, CertificadoDisponibilidadPresupuestaria cert) {
        try {
            POInsumos insumo = monto.getInsumo();
            DataReporteDisponibilidadPresupuestaria data = new DataReporteDisponibilidadPresupuestaria();
            data.setDescripcionContratacion(ReportesUtils.getText(insumo.getObservacion()));
            GeneralPOA poa = monto.getInsumo().getPoa();
            data.setNombreUT(poa.getUnidadTecnica().getNombre());
            if (monto.getCertificado() != null) {
                data.setPresupuesto(ReportesUtils.getNumber(monto.getCertificado()));
            } else {
                data.setPresupuesto(ReportesUtils.getNumber(BigDecimal.ZERO));
            }
            data.setRecurso(insumo.getInsumo().getNombre());
            if (insumo.getInsumo().getObjetoEspecificoGasto() != null) {
                data.setOeg(insumo.getInsumo().getObjetoEspecificoGasto().getClasificador().toString());
            }
            data.setNombreFuente(monto.getFuente().getFuenteRecursos().getNombre());

            String nombreSubProgramaPresupuestario = "";
            String nombreProgramaPresupuestario = "";
            if (poa.getTipo() == TipoPOA.POA_PROYECTO) {
                POAProyecto poaP = (POAProyecto) poa;
                data.setProyecto(poaP.getProyecto().getNombre());
                data.setPoa(poaP.getProyecto().getNombre());
                if (poaP.getProyecto().getProgramaPresupuestario() != null) {
                    if (poaP.getProyecto().getProgramaPresupuestario().getNombre() != null) {
                        nombreSubProgramaPresupuestario = poaP.getProyecto().getProgramaPresupuestario().getNombre();
                    }
                    if (poaP.getProyecto().getProgramaPresupuestario().getProgramaPresupuestario() != null
                            && poaP.getProyecto().getProgramaPresupuestario().getProgramaPresupuestario().getNombre() != null) {
                        nombreProgramaPresupuestario = poaP.getProyecto().getProgramaPresupuestario().getProgramaPresupuestario().getNombre();
                    }
                }
            }
            data.setSubPrograma(nombreSubProgramaPresupuestario);
            data.setPrograma(nombreProgramaPresupuestario);

            if (poa.getTipo() == TipoPOA.POA_CON_ACTIVIDADES) {
                POAConActividades poActividades = (POAConActividades) poa;
                if (poActividades.getConMontoPorAnio().getTipo() == TipoMontoPorAnio.ACCION_CENTRAL) {
                    AccionCentral accionCentra = (AccionCentral) poActividades.getConMontoPorAnio();
                    data.setActividadAccionCentral(accionCentra.getNombre());
                    data.setPoa(accionCentra.getNombre());
                }
                if (poActividades.getConMontoPorAnio().getTipo() == TipoMontoPorAnio.ASIGN_NO_PROGRAMABLE) {
                    AsignacionNoProgramable asigNP = (AsignacionNoProgramable) poActividades.getConMontoPorAnio();
                    data.setActividadAsignacionNoProgramable(asigNP.getNombre());
                    data.setPoa(asigNP.getNombre());
                }
            }

            if (cert != null) {
                String numeroCert = cert.getNumero() != null ? cert.getNumero().toString() : "";
                String estadoCert = cert.getEstado() != null ? EstadoCertificadoDispPresupuestaria.traducirEstado(cert.getEstado()) : "";

                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String fechaEmisionCert = cert.getFecha() != null ? df.format(cert.getFecha()) : "";
                String fechaAprobacionCert = cert.getFechaCertificado() != null ? df.format(cert.getFechaCertificado()) : null;

                data.setNumero(numeroCert);
                data.setEstado(estadoCert);
                data.setFechaEmision(fechaEmisionCert);
                data.setFechaAprobacion(fechaAprobacionCert);
            }

            return data;

        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Genera reporte para seguimiento mensual de proyectos administrativos
     *
     * @param registros
     * @return
     */
    public byte[] generarSeguimientoProyectoAdministrativoMensual(List<HashMap> registros) {
        try {
            List<DataSeguimientoProyAdm> listaData = new ArrayList<>();
            String seguimiento = "";
            String unidadTecnica = "";
            String anio = "";
            for (int i = 0; i < registros.size(); i++) {
                HashMap fila = registros.get(i);
                if (i == 0) {
                    seguimiento = (String) fila.get("col1");
                }
                if (i == 2) {
                    unidadTecnica = (String) fila.get("col1");
                }
                if (i == 3) {
                    anio = (String) fila.get("col1");

                }
                if (i >= 7) {

                    String areaReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_AREA_SEGUIMIENTO_PROY_ADMINISTRATIVO_MENSUAL);
                    String tituloReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_TITULO_SEGUIMIENTO_PROY_ADMINISTRATIVO_MENSUAL);

                    DataSeguimientoProyAdm data = (DataSeguimientoProyAdm) this.inicializarReporteTemplate(tituloReporte, areaReporte, DataSeguimientoProyAdm.class
                    );
                    data.setSeguimiento(seguimiento);
                    data.setUnidadTecnica(unidadTecnica);
                    data.setAnio(anio);
                    data.setIndicador((String) fila.get("col1"));
                    data.setPm1((String) fila.get("col3"));
                    data.setPm2((String) fila.get("col4"));
                    data.setPm3((String) fila.get("col5"));
                    data.setPm4((String) fila.get("col6"));
                    data.setPm5((String) fila.get("col7"));
                    data.setPm6((String) fila.get("col8"));
                    data.setPm7((String) fila.get("col9"));
                    data.setPm8((String) fila.get("col10"));
                    data.setPm9((String) fila.get("col11"));
                    data.setPm10((String) fila.get("col12"));
                    data.setPm11((String) fila.get("col13"));
                    data.setPm12((String) fila.get("col14"));
                    data.setPtotal((String) fila.get("col15"));
                    data.setEm1((String) fila.get("col16"));
                    data.setEm2((String) fila.get("col17"));
                    data.setEm3((String) fila.get("col18"));
                    data.setEm4((String) fila.get("col19"));
                    data.setEm5((String) fila.get("col20"));
                    data.setEm6((String) fila.get("col21"));
                    data.setEm7((String) fila.get("col22"));
                    data.setEm8((String) fila.get("col23"));
                    data.setEm9((String) fila.get("col24"));
                    data.setEm10((String) fila.get("col25"));
                    data.setEm11((String) fila.get("col26"));
                    data.setEm12((String) fila.get("col27"));
                    data.setEtotal((String) fila.get("col28"));
                    data.setAlcance((String) fila.get("colAlcance"));
                    data.setMedio((String) fila.get("colMedio"));
                    listaData.add(data);
                }
            }

            ClassLoader classLoader = getClass().getClassLoader();
            URL jasperFileNameURL = classLoader.getResource("reportes/SeguimientoProyAdm.jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFileNameURL);

            Map parcol = new HashMap();
            addLogoToMap(parcol);

            return ReportesUtils.generarBytesPDF(listaData, parcol, jasperReport);

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Genera reporte para seguimiento trimestral de proyectos administrativos.
     *
     * @param registros
     * @return
     */
    public byte[] generarSeguimientoProyectoAdministrativoTrimestral(List<HashMap> registros) {
        try {
            List<DataSeguimientoProyAdm> listaData = new ArrayList<>();
            String seguimiento = "";
            String unidadTecnica = "";
            String anio = "";
            for (int i = 0; i < registros.size(); i++) {
                HashMap fila = registros.get(i);
                if (i == 0) {
                    seguimiento = (String) fila.get("col1");
                }
                if (i == 2) {
                    unidadTecnica = (String) fila.get("col1");
                }
                if (i == 3) {
                    anio = (String) fila.get("col1");

                }
                if (i >= 7) {

                    String areaReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_AREA_SEGUIMIENTO_PROY_ADMINISTRATIVO_TRIMESTRAL);
                    String tituloReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_TITULO_SEGUIMIENTO_PROY_ADMINISTRATIVO_TRIMESTRAL);

                    DataSeguimientoProyAdm data = (DataSeguimientoProyAdm) this.inicializarReporteTemplate(tituloReporte, areaReporte, DataSeguimientoProyAdm.class);
                    data.setSeguimiento(seguimiento);
                    data.setUnidadTecnica(unidadTecnica);
                    data.setAnio(anio);
                    data.setIndicador((String) fila.get("col1"));
                    data.setProyecto((String) fila.get("col2"));
                    data.setPm1((String) fila.get("col3"));
                    data.setPm2((String) fila.get("col4"));
                    data.setPm3((String) fila.get("col5"));
                    data.setPm4((String) fila.get("col6"));
                    data.setPtotal((String) fila.get("col7"));
                    data.setEm1((String) fila.get("col8"));
                    data.setEm2((String) fila.get("col9"));
                    data.setEm3((String) fila.get("col10"));
                    data.setEm4((String) fila.get("col11"));
                    data.setEtotal((String) fila.get("col12"));
                    data.setAlcance((String) fila.get("colAlcance"));
                    data.setMedio((String) fila.get("colMedio"));
                    listaData.add(data);
                }
            }

            ClassLoader classLoader = getClass().getClassLoader();
            URL jasperFileNameURL = classLoader.getResource("reportes/SeguimientoProyAdmTrim.jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFileNameURL);

            Map parcol = new HashMap();
            addLogoToMap(parcol);

            return ReportesUtils.generarBytesPDF(listaData, parcol, jasperReport);

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método genera el reporte POI
     *
     * @param registros
     * @param planificacion
     * @param anio
     * @param tipoPrograma
     * @return
     */
    public byte[] generarReportePOI(List<HashMap> registros, String planificacion, String anio, String tipoPrograma) {
        try {
            List<DataPOI> listaData = new ArrayList<>();
            String tipoProyecto = "";

            for (int i = 0; i < registros.size(); i++) {
                HashMap fila = registros.get(i);
                if (i == 0) {
                    tipoProyecto = (String) fila.get("col1");

                }
                if (i >= 4) {

                    String areaReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_AREA_POI);
                    String tituloReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_TITULO_POI);
                    DataPOI data = (DataPOI) this.inicializarReporteTemplate(tituloReporte, areaReporte, DataPOI.class);
                    data.setPlanificacion(planificacion);
                    data.setAnio(anio);
                    data.setTipoProyecto(tipoProyecto);
                    data.setPrograma((String) fila.get("col1"));
                    data.setProyecto((String) fila.get("col2"));
                    data.setUnidadTecnica((String) fila.get("col3"));
                    data.setIndicador((String) fila.get("col4"));
                    data.setPm1((String) fila.get("col5"));
                    data.setPm2((String) fila.get("col6"));
                    data.setPm3((String) fila.get("col7"));
                    data.setPm4((String) fila.get("col8"));
                    data.setPtotal((String) fila.get("col9"));
                    data.setMonto((String) fila.get("col10"));
                    listaData.add(data);
                }
            }

            ClassLoader classLoader = getClass().getClassLoader();
            URL jasperFileNameURL = classLoader.getResource("reportes/POIProyectoAdministrativo.jasper");
            if (tipoPrograma.equals("P")) {//Presupuestario
                jasperFileNameURL = classLoader.getResource("reportes/POIProyectoAdministrativoPP.jasper");
            }
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFileNameURL);

            Map parcol = new HashMap();
            addLogoToMap(parcol);

            return ReportesUtils.generarBytesPDF(listaData, parcol, jasperReport);

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método genera el reporte de matriz de riesgo
     *
     * @param listaRiesgos
     * @return
     */
    public byte[] generarReporteMatrizRiesgo(List<RiesgoTO> listaRiesgos) {
        try {
            List<DataRiesgo> listaData = new ArrayList<>();

            for (RiesgoTO riesgoTO : listaRiesgos) {
                String areaReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_AREA_MATRIZ_RIESGO);
                String tituloReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_TITULO_MATRIZ_RIESGO);
                DataRiesgo data = (DataRiesgo) this.inicializarReporteTemplate(tituloReporte, areaReporte, DataRiesgo.class);
                data.setProyecto(riesgoTO.getNombreProyecto());
                data.setUnidadTecnica(riesgoTO.getUnidadTecnica().getNombre());
                data.setRiesgo(riesgoTO.getRiesgo().getRiesgo());
                data.setOrigen(riesgoTO.getRiesgo().getOrigen().getLabel());
                data.setValoracionRiesgo(riesgoTO.getRiesgo().getValoracionDelRiesgo().getLabel());
                data.setMitigacion(riesgoTO.getRiesgo().getAccionesDeMitigacion());
                data.setContingencia(riesgoTO.getRiesgo().getAccionesDeContingencia());
                data.setRenponsable(riesgoTO.getRiesgo().getNombreFuncionarioResponsable());
                listaData.add(data);
            }

            ClassLoader classLoader = getClass().getClassLoader();
            URL jasperFileNameURL = classLoader.getResource("reportes/MatrizRiesgos.jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFileNameURL);

            Map parcol = new HashMap();
            addLogoToMap(parcol);

            return ReportesUtils.generarBytesPDF(listaData, parcol, jasperReport);

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método genera el reporte de matriz de riesgo
     *
     * @param listaRiesgos
     * @return
     */
    public byte[] generarReporteMatrizRiesgoPOA(List<RiesgoTOPOA> listaRiesgos) {
        try {
            List<DataRiesgo> listaData = new ArrayList<>();

            for (RiesgoTOPOA riesgoTO : listaRiesgos) {
                String areaReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_AREA_MATRIZ_RIESGO);
                String tituloReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_TITULO_MATRIZ_RIESGO);
                DataRiesgo data = (DataRiesgo) this.inicializarReporteTemplate(tituloReporte, areaReporte, DataRiesgo.class);
                data.setProyecto(riesgoTO.getNombre());
                data.setUnidadTecnica(riesgoTO.getUnidadTecnica().getNombre());
                data.setRiesgo(riesgoTO.getRiesgo().getRiesgo());
                data.setOrigen(riesgoTO.getRiesgo().getOrigen().getLabel());
                data.setValoracionRiesgo(riesgoTO.getRiesgo().getValoracionDelRiesgo().getLabel());
                data.setMitigacion(riesgoTO.getRiesgo().getAccionesDeMitigacion());
                data.setContingencia(riesgoTO.getRiesgo().getAccionesDeContingencia());
                data.setRenponsable(riesgoTO.getRiesgo().getNombreFuncionarioResponsable());
                listaData.add(data);
            }

            ClassLoader classLoader = getClass().getClassLoader();
            URL jasperFileNameURL = classLoader.getResource("reportes/MatrizRiesgos.jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFileNameURL);

            Map parcol = new HashMap();
            addLogoToMap(parcol);

            return ReportesUtils.generarBytesPDF(listaData, parcol, jasperReport);

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    /**
     * Genera un reporte de compromiso presupuestario a partir de la
     * programación de pagos
     *
     * @param compromiso
     * @param data
     * @return
     * @throws GeneralException
     */
    public Archivo generarReporteCompromisoAPartirProgramaiconPagos(CompromisoPresupuestario compromiso, List<DataDistribuccionProgramacionPagosContrato> data) throws GeneralException {
        try {
            String areaReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_AREA_COMPROMISO_PARA_PROGRAMACION_PAGOS);
            String tituloReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_TITULO_COMPROMISO_PARA_PROGRAMACION_PAGOS);

            DataReporteCompromisoPresupuestarioProgPagos dataReportePEP = (DataReporteCompromisoPresupuestarioProgPagos) this.inicializarReporteTemplate(tituloReporte, areaReporte, DataReporteCompromisoPresupuestarioProgPagos.class);
            dataReportePEP.setFecha(ReportesUtils.dateToStringCompleto(new Date()));
            dataReportePEP.setFilas(new LinkedList());

            Set<String> codigosFuentesEnReporte = new LinkedHashSet<>();

            for (DataDistribuccionProgramacionPagosContrato distContrato : data) {

                DataReporteCompromisoPresupuestarioProgPagosItem itemContrato = new DataReporteCompromisoPresupuestarioProgPagosItem();
                itemContrato.setContrato(distContrato.getContrato().getNroContrato() + "");
                setfuentes(itemContrato, distContrato.getTotales(), codigosFuentesEnReporte);
                itemContrato.setTipoFila(0);
                dataReportePEP.getFilas().add(itemContrato);

                for (DistribucionProgramacionPagosMes distMes : distContrato.getDistribuccionMeses()) {
                    DataReporteCompromisoPresupuestarioProgPagosItem itemMes = new DataReporteCompromisoPresupuestarioProgPagosItem();
                    itemMes.setMes(distMes.getAnio() + " - " + TipoSeguimientoUtils.getListName(TipoSeguimiento.MENSUAL).get(distMes.getMes() - 1));
                    itemMes.setTipoFila(1);
                    setfuentes(itemMes, distMes.getTotales(), codigosFuentesEnReporte);
                    dataReportePEP.getFilas().add(itemMes);

                    for (DistribucionProgramacionPagosItem distItem : distMes.getDistribuccionItems()) {
                        DataReporteCompromisoPresupuestarioProgPagosItem itemItem = new DataReporteCompromisoPresupuestarioProgPagosItem();
                        itemItem.setItem(distItem.getItem().getNombre());
                        itemItem.setTipoFila(2);
                        setfuentes(itemItem, distItem.getTotales(), codigosFuentesEnReporte);
                        dataReportePEP.getFilas().add(itemItem);

                        for (DistribucionProgramacionPagosInsumos distInsumo : distItem.getDistribuccionInsumos()) {
                            DataReporteCompromisoPresupuestarioProgPagosItem itemInsumo = new DataReporteCompromisoPresupuestarioProgPagosItem();
                            itemInsumo.setInsumo(distInsumo.getInsumo().getInsumo().getInsumo().getNombre());
                            itemInsumo.setTipoFila(3);
                            itemInsumo.setUt(distInsumo.getInsumo().getInsumo().getPoInsumo().getUnidadTecnica().getNombre());
                            if (distInsumo.getInsumo().getInsumo().getInsumo().getObjetoEspecificoGasto() != null) {
                                itemInsumo.setOeg(distInsumo.getInsumo().getInsumo().getInsumo().getObjetoEspecificoGasto().getClasificador() + "");
                            }

                            String nombrePOA = "";
                            GeneralPOA poa = distInsumo.getInsumo().getInsumo().getPoInsumo().getPoa();
                            if (poa.getTipo() == TipoPOA.POA_PROYECTO) {
                                POAProyecto pOAProyecto = (POAProyecto) poa;
                                nombrePOA = "Proyecto";
                                itemInsumo.setId(pOAProyecto.getProyecto().getId() + "");

                            } else {
                                POAConActividades pOAConActividades = (POAConActividades) poa;
                                if (pOAConActividades.getConMontoPorAnio().getTipo() == TipoMontoPorAnio.ACCION_CENTRAL) {
                                    nombrePOA = "Acción Central";
                                } else {
                                    nombrePOA = "Asignación No Programable";
                                }
                                itemInsumo.setId(pOAConActividades.getConMontoPorAnio().getId() + "");
                            }
                            itemInsumo.setTipoPOA(nombrePOA);

                            setfuentes(itemInsumo, distInsumo.getDistribuccion(), codigosFuentesEnReporte);
                            dataReportePEP.getFilas().add(itemInsumo);

                        }

                    }

                }
            }

            //Para setear evitar mostrar null en el reporte, cuando un contrato no contiene determinada fuente
            for (DataReporteCompromisoPresupuestarioProgPagosItem fila : dataReportePEP.getFilas()) {
                Set<String> codigosFuentesNoContenidosEnReporte = new LinkedHashSet<String>();
                for (String codigoFuenteEnReporte : codigosFuentesEnReporte) {
                    if (!fila.getMontosFuentes().containsKey(codigoFuenteEnReporte)) {
                        codigosFuentesNoContenidosEnReporte.add(codigoFuenteEnReporte);
                    }
                }
                for (String codigoFuentesNoContenido : codigosFuentesNoContenidosEnReporte) {
                    fila.getMontosFuentes().put(codigoFuentesNoContenido, NumberUtils.nomberToString(BigDecimal.ZERO));
                }
            }

            Map parcol = new HashMap();
            addLogoToMap(parcol);

            //se carga el template a modificar en un string
            String jrxmlContent = cargarTemplateReporteReservaPresupuestaria();

            //se midifica el string para agregar las fuentes dinamicas a la tabla
            final String INDICADOR_START_TEMPLATE = "<!--START-TEMPLATE-FUENTE-->";
            final String INDICADOR_END_TEMPLATE = "<!--END-TEMPLATE-FUENTE-->";

            String templateColumnaFuente = StringUtils.substringBetween(jrxmlContent, INDICADOR_START_TEMPLATE, INDICADOR_END_TEMPLATE);

            double tamanioDisponible = 802.0 - (70 + 60 + 60 + 77 + 73 + 50 + 60 + 40);
            int tamanioCadaFila = (int) Math.floor(tamanioDisponible / codigosFuentesEnReporte.size());

            String nuevoContenido = "";
            for (String codigoFuente : codigosFuentesEnReporte) {
                nuevoContenido = nuevoContenido + "\n"
                        + templateColumnaFuente
                                .replace("CODIGO-FUENTE", codigoFuente)
                                .replace("TAMANIO-CELDA", (new Integer(tamanioCadaFila)).toString());
            }

            InputStream is = null;
            if (jrxmlContent != null) {
                jrxmlContent = jrxmlContent.replace(INDICADOR_START_TEMPLATE + templateColumnaFuente + INDICADOR_END_TEMPLATE, nuevoContenido);
                is = new ByteArrayInputStream(jrxmlContent.getBytes());
            }
            //se complia el reporte a partir del string

            JasperReport jasperReport = JasperCompileManager.compileReport(is);

            Archivo a = archivoBean.crearArchivo();
            a.setContentType("application/pdf");
            String nombreOriginal = "ReservaDeFondos_" + compromiso.getId() + ".pdf";
            a.setNombreOriginal(nombreOriginal);

            ReportesUtils.generarPDF(dataReportePEP, parcol, jasperReport, archivoBean.getFile(a));

            return (Archivo) generalDAO.update(a);

        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Carga el template a modificar en un string
     *
     * @return
     */
    private String cargarTemplateReporteReservaPresupuestaria() throws IOException {
        String jrxmlContent = null;
        Scanner scanner = null;
        InputStream archivoIS = null;
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            archivoIS = classLoader.getResource("reportes/reporteReservaPresupuestaria.jrxml").openStream();
            scanner = new Scanner(archivoIS);
            jrxmlContent = scanner.useDelimiter("\\A").next();
            return jrxmlContent;
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (scanner != null) {
                scanner.close();
            }
            if (archivoIS != null) {
                archivoIS.close();
            }
        }
    }

    /**
     * Asocia los valores de las fuentes en el reporte de reserva presupuestaria
     *
     * @param reporte
     * @param list
     * @param codigosFuentesGlobales
     */
    private void setfuentes(DataReporteCompromisoPresupuestarioProgPagosItem reporte, List<DistribucionMontoAdjudicado> list, Set<String> codigosFuentesGlobales) {
        reporte.setMontosFuentes(new LinkedHashMap<String, String>());

        for (DistribucionMontoAdjudicado item : list) {
            String nombre = "";
            if (item.getFuente().getFuente().getCategoriaConvenio() != null) {
                nombre = nombre + item.getFuente().getFuente().getCategoriaConvenio().getCodigo() + " ";
            }
            if (item.getFuente().getFuente().getFuenteRecursos() != null) {
                nombre = nombre + item.getFuente().getFuente().getFuenteRecursos().getCodigo();
            }

            reporte.getMontosFuentes().put(nombre, ReportesUtils.getNumber(item.getMonto()));
            codigosFuentesGlobales.add(nombre);

        }
    }

    /**
     * Genera el reporte de certificado de disponibilidad de determinados montos
     * para determinado proceso
     *
     * @param montosSeleccionadosParaReporte
     * @return
     */
    public Archivo generarArchivoCertificadoDisponibilidadProceso(CertificadoDisponibilidadPresupuestaria cert) {
        try {

            String areaReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_AREA_CERTIFICADO_DISPONIBILIDAD_PROCESO);
            String tituloReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_TITULO_CERTIFICADO_DISPONIBILIDAD_PROCESO);

            DataReporteDisponibilidadPresupuestariaProceso dataProceso = (DataReporteDisponibilidadPresupuestariaProceso) this.inicializarReporteTemplate(tituloReporte, areaReporte, DataReporteDisponibilidadPresupuestariaProceso.class);
            dataProceso.setListaDisponibilidadPresupuestariaMontosFuenteInsumo(new LinkedList<DataReporteDisponibilidadPresupuestaria>());
            BigDecimal totalPresupuesto = BigDecimal.ZERO;

            for (POMontoFuenteInsumo mfi : cert.getFuentes()) {
                DataReporteDisponibilidadPresupuestaria dataMonto = this.cargarDataReporteDisponibilidadPresupuestariaParaPOMontoFuenteInsumo(mfi, cert);
                dataProceso.getListaDisponibilidadPresupuestariaMontosFuenteInsumo().add(dataMonto);
                if (mfi.getCertificado() != null) {
                    totalPresupuesto = totalPresupuesto.add(mfi.getCertificado());
                }
            }
            dataProceso.setTotalPresupuesto(ReportesUtils.getNumber(totalPresupuesto));

            String numeroCert = cert.getNumero() != null ? cert.getNumero().toString() : "";
            String estadoCert = cert.getEstado() != null ? EstadoCertificadoDispPresupuestaria.traducirEstado(cert.getEstado()) : "";
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String fechaEmisionCert = cert.getFecha() != null ? df.format(cert.getFecha()) : "";
            String fechaAprobacionCert = cert.getFechaCertificado() != null ? df.format(cert.getFechaCertificado()) : null;

            dataProceso.setNumero(numeroCert);
            dataProceso.setEstado(estadoCert);
            dataProceso.setFechaEmision(fechaEmisionCert);
            dataProceso.setFechaAprobacion(fechaAprobacionCert);

            ClassLoader classLoader = getClass().getClassLoader();
            URL jasperFileNameURL = classLoader.getResource("reportes/certificadoDisponibilidadPresupuestariaProceso.jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFileNameURL);

            Map parcol = new HashMap();
            addLogoToMap(parcol);

            Archivo a = archivoBean.crearArchivo();
            a.setContentType("application/pdf");
            String nombreOriginal = "CertificadoDisponibilidadPresupuestaria.pdf";
            a.setNombreOriginal(nombreOriginal);

            ReportesUtils.generarPDF(dataProceso, parcol, jasperReport, archivoBean.getFile(a));

            a = (Archivo) generalDAO.update(a);
            return a;

        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }

    }

    /**
     * Este método genera el reporte de certificado de disponibilidad
     * presupuestaria y lo devuelve como un archivo
     *
     * @param poa
     * @param idPOMontoFuenteInsumo
     * @return
     */
    public Archivo generarArchivoCertificadoDisponibilidadPOInsumo(CertificadoDisponibilidadPresupuestaria cert) {
        try {

            POMontoFuenteInsumo monto = (POMontoFuenteInsumo) cert.getFuentes().get(0);

            DataReporteDisponibilidadPresupuestaria data = this.cargarDataReporteDisponibilidadPresupuestariaParaPOMontoFuenteInsumo(monto, cert);
            Configuracion tituloMinisterioReporte = configuracionBean.obtenerCnfPorCodigo(ConstantesConfiguracion.TITULO_MINISTERIO_REPORTES);
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String fechaHoraImpresionReporte = df.format(new Date());

            String areaReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_AREA_CERT_DISP_PRESUP_INSUMO);
            String tituloReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_TITULO_CERT_DISP_PRESUP_INSUMO);

            data.setTituloMinisterio(tituloMinisterioReporte.getCnfValor());
            data.setTituloAreaMinisterio(areaReporte);
            data.setFechaHoraImpresionReporte(fechaHoraImpresionReporte);
            data.setTituloNombreReporte(tituloReporte);
            data.setUsuarioImprimeReporte(datosUsuario.getCodigoUsuario());
            Configuracion monedaMontos = configuracionBean.obtenerCnfPorCodigo(ConstantesConfiguracion.ACLARACION_MONEDA_MONTOS_REPORTES);
            data.setAclaracionMonedaMontos(monedaMontos.getCnfValor());

            ClassLoader classLoader = getClass().getClassLoader();
            URL jasperFileNameURL = classLoader.getResource("reportes/certificadoDisponibilidadPresupuestaria2.jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFileNameURL);

            Map parcol = new HashMap();
            addLogoToMap(parcol);

            Archivo a = archivoBean.crearArchivo();
            a.setContentType("application/pdf");
            String nombreOriginal = "CertificadoDisponibilidadPresupuestaria.pdf";
            a.setNombreOriginal(nombreOriginal);

            ReportesUtils.generarPDF(data, parcol, jasperReport, archivoBean.getFile(a));

            a = (Archivo) generalDAO.update(a);
            return a;

        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Devuelve el valor almacenado en un registro de la tabla Configuración de
     * la BD. Si el valor es null devuelve una cadena vacía
     *
     * @param constanteConfiguracion
     * @return
     */
    public String obtenerTextoDeReporteDesdeConfiguracion(String constanteConfiguracion) {
        Configuracion conf = configuracionBean.obtenerCnfPorCodigo(constanteConfiguracion);
        String texto = conf.getCnfValor() != null ? conf.getCnfValor() : "";
        return texto;
    }

    /**
     * Genera el reporte pdf de una póliza de concentración
     *
     * @param polizaId
     * @return
     */
    public byte[] generarPolizaConcentracion(Integer polizaId) {
        try {
            PolizaDeConcentracion poliza = (PolizaDeConcentracion) generalDAO.findById(PolizaDeConcentracion.class, polizaId);

            String areaReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_AREA_POLIZA_CONCENTRACION);
            String tituloReporte = obtenerTextoDeReporteDesdeConfiguracion(ConstantesConfiguracion.REPORTE_TITULO_POLIZA_CONCENTRACION);

            DataReportePolizaConcentracion reporte = (DataReportePolizaConcentracion) this.inicializarReporteTemplate(tituloReporte, areaReporte, DataReportePolizaConcentracion.class);

            String nroPoliza = poliza.getNumeroPoliza() != null ? poliza.getNumeroPoliza().toString() : "";
            String institucion = configuracionBean.obtenerCnfPorCodigo(ConstantesConfiguracion.TITULO_MINISTERIO_REPORTES).getCnfValor();
            String unidadEjecutora = poliza.getFuente().getInsumo().getPoa().getUnidadTecnica().getNombre();
            String codigoPresup = "???";
            String estadoReserva = "???";
            String ejercicioFiscal = poliza.getFuente().getInsumo().getPoa().getAnioFiscal().getAnio().toString();
            String nombreSuminis = poliza.getProveedorNombre() != null ? poliza.getProveedorNombre() : "";
            String nit = poliza.getProveedorNIT() != null ? poliza.getProveedorNIT() : "";
            String montoPoliza = NumberUtils.nomberToString(poliza.getMonto());
            String nroCuentaBanc = poliza.getProveedorNroCuentaBancaria() != null ? poliza.getProveedorNroCuentaBancaria() : "";
            String fechaEmision = "";
            if (poliza.getFechaEmision() != null) {
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                fechaEmision = df.format(poliza.getFechaEmision());
            }

            reporte.setNroPoliza(nroPoliza);
            reporte.setInstitución(institucion);
            reporte.setUnidadEjecutora(unidadEjecutora);
            reporte.setCodigoPrespuestario(codigoPresup);
            reporte.setEstadoReserva(estadoReserva);
            reporte.setEjercicioFiscal(ejercicioFiscal);
            reporte.setNombreSuministrante(nombreSuminis);
            reporte.setNit(nit);
            reporte.setMonto(montoPoliza);
            reporte.setNroCuentaBancaria(nroCuentaBanc);
            reporte.setFechaEmision(fechaEmision);

            reporte.setListaDetallesAplicacionesEnPOAyPEP(new LinkedList());
            String codigoRecurso = poliza.getFuente().getInsumo().getInsumo().getCodigo();
            String oeg = poliza.getFuente().getInsumo().getInsumo().getObjetoEspecificoGasto().getClasificador().toString();
            String mes = "";
            String fteFinanciamiento = "";
            String valorAplicado = "";
            if (poliza.getPagoFuenteFCM() != null) {
                mes = poliza.getPagoFuenteFCM().getFlujoCajaMensual().getMes().toString();
                fteFinanciamiento = poliza.getPagoFuenteFCM().getMontoFuenteInsumo().getFuente().getFuenteRecursos().getNombre();
                valorAplicado = NumberUtils.nomberToString(poliza.getPagoFuenteFCM().getMontoCertificado());
            }

            DataReportePolizaDetalleAplicacionEnPOAyPEP dataDetalleAplicacion = new DataReportePolizaDetalleAplicacionEnPOAyPEP();
            dataDetalleAplicacion.setCodigoRecurso(codigoRecurso);
            dataDetalleAplicacion.setOeg(oeg);
            dataDetalleAplicacion.setMes(mes);
            dataDetalleAplicacion.setFuenteFinanciamiento(fteFinanciamiento);
            dataDetalleAplicacion.setValorAplicado(valorAplicado);
            reporte.getListaDetallesAplicacionesEnPOAyPEP().add(dataDetalleAplicacion);

            reporte.setListaDetallesFactura(new LinkedList());
            for (FacturaPolizaConcentracion factura : poliza.getFacturas()) {
                String numeroFactura = factura.getId().toString();
                String numeroDocumento = factura.getNumero().toString();
                String tipoDoc = factura.getTipo().getTexto();
                String descripcion = factura.getObservacion() != null ? factura.getObservacion() : "";
                String valor = NumberUtils.nomberToString(factura.getImporte());

                DataReportePolizaDetalleFactura dataDetalleFactura = new DataReportePolizaDetalleFactura();
                dataDetalleFactura.setNumeroFactura(numeroFactura);
                dataDetalleFactura.setNumeroDocumento(numeroDocumento);
                dataDetalleFactura.setDescripcion(descripcion);
                dataDetalleFactura.setTipoDocumento(tipoDoc);
                dataDetalleFactura.setValor(valor);
                reporte.getListaDetallesFactura().add(dataDetalleFactura);
            }

            ClassLoader classLoader = getClass().getClassLoader();
            URL jasperFileNameURL = classLoader.getResource("reportes/PolizaDeConcentracion.jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFileNameURL);

            Map parcol = new HashMap();
            addLogoToMap(parcol);

            return ReportesUtils.generarBytesPDF(reporte, parcol, jasperReport);

        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Setea en el reporte de Orden de compra el representante
     *
     * @param dataReportesContratoOC
     */
    private void setRepresentanteOrdenDeCompra(DataReportesContratoOC dataReportesContratoOC) {
        try {
            Configuracion confFirma = configuracionBean.obtenerCnfPorCodigo(ConstantesConfiguracion.REPRESENTANTE_MINED_ORDEN_COMPRA);
            if (confFirma != null && confFirma.getCnfValor() != null && !confFirma.getCnfValor().isEmpty()) {
                dataReportesContratoOC.setFirmaRepresentanteMINED(confFirma.getCnfValor().trim());
            } else {
                dataReportesContratoOC.setFirmaRepresentanteMINED("");
            }
        } catch (GeneralException ge) {
            dataReportesContratoOC.setFirmaRepresentanteMINED("");
        }
    }

    /**
     * Devuelve un texto fijo para el reporte de invitación al proveedor
     *
     * @return
     */
    private String getAutorizadoMINED() {
        String autorizadoMined = "";
        try {
            Configuracion confFirmaAutorizadaMINED = configuracionBean.obtenerCnfPorCodigo(ConstantesConfiguracion.AUTORIZADO_MINED_INVITACION_PROVEEDOR);
            if (confFirmaAutorizadaMINED != null && confFirmaAutorizadaMINED.getCnfValor() != null && !confFirmaAutorizadaMINED.getCnfValor().isEmpty()) {
                autorizadoMined = confFirmaAutorizadaMINED.getCnfValor().trim();

            }
            return autorizadoMined;
        } catch (GeneralException ge) {
            return "";
        }
    }

    /**
     * Devuelve un texto fijo para el reporte de invitación al proveedor
     *
     * @return
     */
    private String getTextoInvitacionProveedor() {
        String textoInvitacion = "";
        try {
            Configuracion conftextoInvitacion = configuracionBean.obtenerCnfPorCodigo(ConstantesConfiguracion.TEXTO_INVITACIONES_PROVEEDOR);
            if (conftextoInvitacion != null && conftextoInvitacion.getCnfValor() != null && !conftextoInvitacion.getCnfValor().isEmpty()) {
                textoInvitacion = conftextoInvitacion.getCnfValor().trim();
            }
            return textoInvitacion;
        } catch (GeneralException ge) {
            return "";
        }
    }
}
