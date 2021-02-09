/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.SerializationUtils;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgCompromisoPresupuestario;
import sv.gob.mined.siges.web.dto.SgCuentasBancariasDD;
import sv.gob.mined.siges.web.dto.SgDireccionDepartamental;
import sv.gob.mined.siges.web.dto.SgDocumentos;
import sv.gob.mined.siges.web.dto.SgReqFondoCed;
import sv.gob.mined.siges.web.dto.SgRequerimientoFondo;
import sv.gob.mined.siges.web.dto.SgTransferenciaACed;
import sv.gob.mined.siges.web.dto.SgTransferenciaGDep;
import sv.gob.mined.siges.web.dto.TransferenciaCedAgrup;
import sv.gob.mined.siges.web.enumerados.EnumEstadoDocumento;
import sv.gob.mined.siges.web.enumerados.EnumEstadoSolicitudesTransferencia;
import sv.gob.mined.siges.web.enumerados.TipoDocumento;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCuentasBancariasDD;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDireccionDepartamental;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroReqFondoCed;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRequerimientosFondo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroTransferenciaACed;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroTransferenciaGDep;
import sv.gob.mined.siges.web.lazymodels.LazyReqFondoCedDataModel;
import sv.gob.mined.siges.web.lazymodels.LazyTransferenciaACedDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CompromisoPresupuestarioRestClient;
import sv.gob.mined.siges.web.restclient.CuentasBancariasDDRestClient;
import sv.gob.mined.siges.web.restclient.DireccionDepartamentalRestClient;
import sv.gob.mined.siges.web.restclient.ReqFondoCedRestClient;
import sv.gob.mined.siges.web.restclient.RequerimientoFondoRestClient;
import sv.gob.mined.siges.web.restclient.TransferenciaACedRestClient;
import sv.gob.mined.siges.web.restclient.TransferenciaGDepRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 * Bean para la gestión de requerimiento de fonde.
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class RequerimientoFondoBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(RequerimientoFondoBean.class.getName());

    @Inject
    private RequerimientoFondoRestClient restClient;

    @Inject
    private ReqFondoCedRestClient restReqFondoCed;

    @Inject
    private DireccionDepartamentalRestClient restDireccDep;

    @Inject
    private TransferenciaGDepRestClient restTransferenciaGDep;

    @Inject
    private TransferenciaACedRestClient transferenciaACedRestClient;

    @Inject
    private CuentasBancariasDDRestClient cuentasBancariasDDRestClient;

    @Inject
    private ReqFondoCedRestClient reqFondoCedRestClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private CompromisoPresupuestarioRestClient compromisoRestClient;

    @Inject
    @Param(name = "id")
    private Long solicitudTransferenciaId;

    @Inject
    @Param(name = "transPk")
    private Long transferenciaGDepId;

    private Locale locale;
    private SgRequerimientoFondo entidadEnEdicion = new SgRequerimientoFondo();
    private SgReqFondoCed reqFondoCedEnEdicion = new SgReqFondoCed();
    private SgTransferenciaGDep transferenciaGDep = new SgTransferenciaGDep();
    private SgDireccionDepartamental direccionDepartamental = new SgDireccionDepartamental();
    private SgCompromisoPresupuestario compromisoPresupuestarioEnEdicion = new SgCompromisoPresupuestario();
    private String tituloPagina = "";
    private Long totalResultados;
    private Long totalResultadosReq = 0L;
    private Integer paginado = 10;
    private Boolean soloLectura = Boolean.FALSE;
    private Boolean busquedaRealizada = Boolean.FALSE;
    private List<RevHistorico> historialPresupuestoEscolar = new ArrayList();

    private LazyTransferenciaACedDataModel transferenciaCedDataModel;
    private LazyReqFondoCedDataModel reqFondoCedDataModel;
    private FiltroTransferenciaACed filtro = new FiltroTransferenciaACed();
    private FiltroReqFondoCed filtroReqFondoCed = new FiltroReqFondoCed();
    private FiltroCuentasBancariasDD filtroCuentasBacDD;

    private SofisComboG<SgCuentasBancariasDD> cuentasBancariasDD = new SofisComboG();
    private List<TransferenciaCedAgrup> tablaTransACed = new ArrayList<>();
    private List<SgTransferenciaACed> removeTransACed = new ArrayList<>();
    private List<SgTransferenciaACed> listTraCed = new ArrayList();
    private List<SelectItem> estadosReq = new ArrayList<>();
    private List<BigDecimal> montoAutorizados = new ArrayList();
    private List<SgReqFondoCed> resultado = new ArrayList();
    private List<SgReqFondoCed> nuevoResultado = new ArrayList();
    private List<SgCompromisoPresupuestario> compromisos = new ArrayList<>();
    private Boolean guardadoReqs;

    @Inject
    @Param(name = "historial")
    private Boolean hist;

    public RequerimientoFondoBean() {
    }

    /**
     * Inicializa el objeto después de crearse.
     */
    @PostConstruct
    public void init() {
        try {
            locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
            if (transferenciaGDepId != null && transferenciaGDepId > 0) {
                FiltroTransferenciaGDep filtroTrans = new FiltroTransferenciaGDep();
                filtroTrans.setIncluirCampos(new String[]{
                    "tgdPk", "tgdDepartamentoFk.depPk", "tgdDepartamentoFk.depVersion", "tgdVersion"
                });
                filtroTrans.setTgdPk(transferenciaGDepId);
                List<SgTransferenciaGDep> list = restTransferenciaGDep.buscar(filtroTrans);
                if (!list.isEmpty()) {
                    transferenciaGDep = list.get(0);
                }
            }

            if (solicitudTransferenciaId != null && solicitudTransferenciaId > 0) {
                actualizar(solicitudTransferenciaId);
            } else {
                agregar();
            }
            cargarTransferenciaDD();
            cargarCombos();
            buscar();

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Crea un nuevo objeto requerimiento fondo de ced
     *
     * @param transferenciaDireccionDep
     */
    public void agregar() {
        entidadEnEdicion = new SgRequerimientoFondo();
        entidadEnEdicion.setStrTransferenciaGDepFk(transferenciaGDep);
        entidadEnEdicion.setStrEstado(EnumEstadoSolicitudesTransferencia.EN_PROCESO);
    }

    /**
     * Carga los datos de una solicitud de transferencia para poder ser
     * editados.
     */
    private void actualizar(Long sol) {
        try {
            FiltroRequerimientosFondo filtroSol = new FiltroRequerimientosFondo();
            filtroSol.setStrPk(sol);
            filtroSol.setIncluirCampos(new String[]{"strPk",
                "strTransferenciaGDepFk.tgdPk",
                "strTransferenciaGDepFk.tgdVersion",
                "strTransferenciaGDepFk.tgdTransferenciaFk.traId",
                "strTransferenciaGDepFk.tgdTransferenciaFk.traVersion",
                "strTransferenciaGDepFk.tgdDepartamentoFk.depPk",
                "strTransferenciaGDepFk.tgdDepartamentoFk.depNombre",
                "strTransferenciaGDepFk.tgdDepartamentoFk.depVersion",
                "strCuentaBancDdFk.cbdPk",
                "strCuentaBancDdFk.cbdNumeroCuenta",
                "strCuentaBancDdFk.cbdTitular",
                "strCuentaBancDdFk.cbdVersion",
                "strSacGOES",
                "strSacUFI",
                "strImporteTotal",
                "strCompromisoPresupuestario",
                "strEstado",
                "strVersion"});
            filtroSol.setOrderBy(new String[]{"strPk"});
            filtroSol.setAscending(new boolean[]{false});
            List<SgRequerimientoFondo> listSol = restClient.buscar(filtroSol);
            if (!listSol.isEmpty()) {
                entidadEnEdicion = listSol.get(0);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Obtener Pagaduría
     *
     * @throws BusinessException
     */
    private void cargarTransferenciaDD() throws BusinessException {
        try {
            FiltroDireccionDepartamental filtroDD = new FiltroDireccionDepartamental();
            if (entidadEnEdicion.getStrTransferenciaGDepFk() != null && entidadEnEdicion.getStrTransferenciaGDepFk().getTgdDepartamentoFk() != null) {
                filtroDD.setDepartamentoPk(entidadEnEdicion.getStrTransferenciaGDepFk().getTgdDepartamentoFk().getDepPk());
            }
            filtroDD.setIncluirCampos(new String[]{"dedPk", "dedNombre", "dedVersion", "dedDepartamentoFk.depPk", "dedDepartamentoFk.depCodigo", "dedDepartamentoFk.depVersion"});
            filtroDD.setOrderBy(new String[]{"dedPk"});
            filtroDD.setAscending(new boolean[]{false});
            List<SgDireccionDepartamental> listdd = restDireccDep.buscar(filtroDD);
            if (!listdd.isEmpty()) {
                direccionDepartamental = listdd.get(0);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Carga los combos para agregar o editar
     *
     * @throws BusinessException
     */
    private void cargarCombos() throws BusinessException {
        try {
            if (cuentasBancariasDD.isEmpty()) {
                filtroCuentasBacDD = new FiltroCuentasBancariasDD();
                filtroCuentasBacDD.setCbdHabilitado(Boolean.TRUE);
                if (direccionDepartamental != null) {
                    filtroCuentasBacDD.setCbdDirDepFk(direccionDepartamental.getDedPk());
                    filtroCuentasBacDD.setIncluirCampos(new String[]{"cbdPk",
                        "cbdNumeroCuenta",
                        "cbdTitular",
                        "cbdNumeroCuenta",
                        "cbdVersion"});
                    filtroCuentasBacDD.setOrderBy(new String[]{"cbdPk"});
                    filtroCuentasBacDD.setAscending(new boolean[]{false});
                    List<SgCuentasBancariasDD> listCuentasBacDD = cuentasBancariasDDRestClient.buscar(filtroCuentasBacDD);
                    cuentasBancariasDD = new SofisComboG(listCuentasBacDD, "numeroCuentaTitular");
                }
                cuentasBancariasDD.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
            if (entidadEnEdicion.getStrPk() != null && entidadEnEdicion.getStrPk() > 0) {
                cuentasBancariasDD.setSelectedT(entidadEnEdicion.getStrCuentaBancDdFk());
                if (estadosReq.isEmpty()) {
                    Arrays.asList(EnumEstadoSolicitudesTransferencia.values()).forEach(e -> {
                        estadosReq.add(new SelectItem(e, e.getText()));
                    });
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Obtener estado documento,
     *
     * @throws BusinessException
     */
    public EnumEstadoDocumento getEstadoDocumento(TipoDocumento tipoDoc, SgTransferenciaACed tac) {
        EnumEstadoDocumento enumResult = EnumEstadoDocumento.PENDIENTE;
        try {
            if (tac != null) {
                if (tac.getTacCedFk().getSedPresupuestos() != null && !tac.getTacCedFk().getSedPresupuestos().isEmpty()) {
                    if (tac.getTacCedFk().getSedPresupuestos().get(0).getPesDocumentos() != null && !tac.getTacCedFk().getSedPresupuestos().get(0).getPesDocumentos().isEmpty()) {
                        SgDocumentos doc = tac.getTacCedFk().getSedPresupuestos().get(0).getPesDocumentos().stream().filter(d -> d.getDocTipoDocumento().equals(tipoDoc)).findFirst().orElse(null);
                        if (doc != null) {
                            enumResult = doc.getDocEstadoDocumento();
                        }
                    }
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return enumResult;
    }

    /**
     * Busca el detalle de requerimiento de fondo
     */
    public void buscar() {
        
        try {
            nuevoResultado = new ArrayList();
            resultado = new ArrayList();
            if (entidadEnEdicion.getStrPk() != null) {

                filtroReqFondoCed.setFirst(new Long(0));
                filtroReqFondoCed.setOrderBy(new String[]{"rfcPk"});
                filtroReqFondoCed.setIncluirCampos(new String[]{
                    "rfcPk", "rfcMonto", "rfcTransferenciaCedFk.tacCedFk.sedPk",
                    "rfcTransferenciaCedFk.tacCedFk.sedCodigo", "rfcTransferenciaCedFk.tacCedFk.sedNombre", "rfcTransferenciaCedFk.tacCedFk.sedTipo",
                    "rfcTransferenciaCedFk.tacCedFk.sedVersion",
                    "rfcTransferenciaCedFk.tacTransferenciaFk.tcId",
                    "rfcTransferenciaCedFk.tacTransferenciaFk.componente.cpeId",
                    "rfcTransferenciaCedFk.tacTransferenciaFk.componente.cpeNombre",
                    "rfcTransferenciaCedFk.tacTransferenciaFk.componente.cpeVersion",
                    "rfcTransferenciaCedFk.tacTransferenciaFk.subComponente.gesId",
                    "rfcTransferenciaCedFk.tacTransferenciaFk.subComponente.gesNombre",
                    "rfcTransferenciaCedFk.tacTransferenciaFk.subComponente.gesVersion",
                    "rfcTransferenciaCedFk.tacTransferenciaFk.anioFiscal.aniPk",
                    "rfcTransferenciaCedFk.tacTransferenciaFk.anioFiscal.aniAnio",
                    "rfcTransferenciaCedFk.tacTransferenciaFk.anioFiscal.aniVersion",
                    "rfcTransferenciaCedFk.tacTransferenciaFk.unidadPresupuestaria.cuNombre",
                    "rfcTransferenciaCedFk.tacTransferenciaFk.unidadPresupuestaria.cuVersion",
                    "rfcTransferenciaCedFk.tacTransferenciaFk.lineaPresupuestaria.cuNombre",
                    "rfcTransferenciaCedFk.tacTransferenciaFk.lineaPresupuestaria.cuVersion",
                    "rfcTransferenciaCedFk.tacTransferenciaFk.tcFuenteRecursosFk.nombre",
                    "rfcTransferenciaCedFk.tacTransferenciaFk.tcFuenteRecursosFk.version",
                    "rfcTransferenciaCedFk.tacTransferenciaFk.tcFuenteRecursosFk.fuenteFinanciamiento.nombre",
                    "rfcTransferenciaCedFk.tacTransferenciaFk.tcFuenteRecursosFk.fuenteFinanciamiento.version",
                    "rfcTransferenciaCedFk.tacTransferenciaFk.tcPorcentaje", "rfcVersion"});
                filtroReqFondoCed.setAscending(new boolean[]{false});
                filtroReqFondoCed.setRfcStrFk(entidadEnEdicion.getStrPk());
                totalResultadosReq = restReqFondoCed.buscarTotal(filtroReqFondoCed);
                resultado = restReqFondoCed.buscar(filtroReqFondoCed);
                reqFondoCedDataModel = new LazyReqFondoCedDataModel(restReqFondoCed, filtroReqFondoCed, totalResultadosReq, paginado);

                nuevoResultado
                        = resultado.stream()
                                .filter(distinctByKey(x -> x.getRfcTransferenciaCedFk().getTacTransferenciaFk().getTcFuenteRecursosFk().getNombre()))
                                .collect(Collectors.toList());

                nuevoResultado.forEach(x -> {
                    SgCompromisoPresupuestario entidad = new SgCompromisoPresupuestario();
                    entidad.setCprFuenteRecursosFk(x.getRfcTransferenciaCedFk().getTacTransferenciaFk().getTcFuenteRecursosFk().getId());
                    entidad.setCprRequerimientoFondoFk(entidadEnEdicion);
                    entidad.setNombreRecurso(x.getRfcTransferenciaCedFk().getTacTransferenciaFk().getTcFuenteRecursosFk().getNombre());
                    compromisos.add(entidad);
                });

            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Búsqueda de diferentes elementos en una lista
     */
    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    /**
     * Busca las transferencias a ced agrupadas por sede
     */
    public void obtenerTransferenciasCedAgrupadas() {
        try {
            Long idTransferencia = 0L;
            if (entidadEnEdicion != null) {
                if (entidadEnEdicion.getStrPk() != null && entidadEnEdicion.getStrTransferenciaGDepFk() != null) {
                    idTransferencia = entidadEnEdicion.getStrTransferenciaGDepFk().getTgdTransferenciaFk().getTraId();
                } else if (transferenciaGDep != null) {
                    idTransferencia = transferenciaGDep.getTgdTransferenciaFk().getTraId();
                }
                if (direccionDepartamental != null) {
                    tablaTransACed = transferenciaACedRestClient.obtenerTransferenciasAgrupadas(idTransferencia, direccionDepartamental.getDedDepartamentoFk().getDepPk());
                }
                if (!tablaTransACed.isEmpty()) {
                    totalResultados = new Long(tablaTransACed.size());
                }
                busquedaRealizada = Boolean.TRUE;
            } else {
                busquedaRealizada = Boolean.FALSE;
                //Modificar el mensaje de error
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    /**
     * Busca las transferencias a ced
    */
    private void obtenerTransferenciasCed() throws Exception {
        try {
            filtro = new FiltroTransferenciaACed();
            filtro.setFirst(new Long(0));
            filtro.setOrderBy(new String[]{"tacPk"});
            filtro.setIncluirCampos(new String[]{"tacPk",
                "tacCedFk.sedPk",
                "tacCedFk.sedCodigo",
                "tacCedFk.sedTipo",
                "tacCedFk.sedVersion",
                "tacMontoAutorizado",
                "tacReqFondoCed.rfcPk",
                "tacReqFondoCed.rfcVersion",
                "tacVersion"});
            filtro.setAscending(new boolean[]{false});
            filtro.setSsTransferencia(entidadEnEdicion.getStrTransferenciaGDepFk().getTgdTransferenciaFk());
            listTraCed = transferenciaACedRestClient.buscar(filtro);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    
    /**
     * Valida la selección de transferencias a ced 
     */
    public boolean disabledCheck(TransferenciaCedAgrup tac) {
        boolean result = false;
        try {
            if (tac != null) {
                LocalDate localDate = LocalDate.now();
                if (!tac.getHabilitado().equals(Boolean.TRUE)) {
                    result = true;
                }

                if (tac.getConvenio().equals(EnumEstadoDocumento.PENDIENTE.name()) || tac.getCcf().equals(EnumEstadoDocumento.PENDIENTE.name())) {
                    result = true;
                }

                if (tac.getOeaMiembrosVigente() == null) {
                    result = true;
                } else if (tac.getOeaMiembrosVigente() == false) {
                    result = true;
                }

                if (tac.getOeaFechaVencimiento() == null) {
                    result = true;
                } else if (tac.getOeaFechaVencimiento().isBefore(localDate)) {
                    result = true;
                }

                if (tac.getRecibo() == null) {
                    result = true;
                } else if (tac.getRecibo() == false) {
                    result = true;
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }
        return result;
    }

    /**
     * Muestra el historial del registro.
     *
     * @param id
     */
    public void historial(Long id) {
        try {
            historialPresupuestoEscolar = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Limpia los filtros seleccionados y busca de nuevo
     */
    public void limpiar() {
        busquedaRealizada = Boolean.FALSE;
    }

    /**
     * Obtiene el objeto a eliminar
     */
    public void preEliminar(SgReqFondoCed ced) {
        reqFondoCedEnEdicion = (SgReqFondoCed) SerializationUtils.clone(ced);
    }

    /**
     * Elimina un registro de requerimiento fondo de ced
     */
    public void eliminar() {
        try {
            if (reqFondoCedEnEdicion != null && reqFondoCedEnEdicion.getRfcPk() != null) {
                restReqFondoCed.eliminar(reqFondoCedEnEdicion.getRfcPk());
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
                buscar();
                guardar();
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Crea o actualiza un registro.
     */
    public void guardar() {
        try {
            if (cuentasBancariasDD.getSelectedT() != null) {
                entidadEnEdicion.setStrCuentaBancDdFk(cuentasBancariasDD.getSelectedT());
            }
            
            if (!resultado.isEmpty()) {
                entidadEnEdicion.setStrImporteTotal(resultado.stream().filter(r->r.getRfcMonto()!=null).map(r->r.getRfcMonto()).reduce(BigDecimal.ZERO, BigDecimal::add));
            }
            else{
                entidadEnEdicion.setStrImporteTotal(BigDecimal.ZERO);
            }

            if (nuevoResultado != null && !nuevoResultado.isEmpty()) {
                if (!compromisos.isEmpty()) {
                    guardarCompromisoPresupuestario();
                }
            }

            entidadEnEdicion = restClient.guardar(entidadEnEdicion);

            actualizar(entidadEnEdicion.getStrPk());
            cargarCombos();
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            LOGGER.log(Level.SEVERE, be.getMessage(), be);
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Cambiar estado requerimiento de fondo
     */
    public void cambiarEstado(EnumEstadoSolicitudesTransferencia estado) {
        try {
            if(totalResultadosReq==0L){
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
                return;
            }
            entidadEnEdicion.setStrEstado(estado);
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);

            actualizar(entidadEnEdicion.getStrPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            LOGGER.log(Level.SEVERE, be.getMessage(), be);
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Crea o actualiza un Compromiso Presupuestario
     */
    public void guardarCompromisoPresupuestario() {
        try {
            for (SgCompromisoPresupuestario compromiso : compromisos) {
                compromisoRestClient.guardar(compromiso);
            }
            //JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            LOGGER.log(Level.SEVERE, be.getMessage(), be);
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Crea o actualiza un requerimiento fondo de ced
     */
    public void guardarReqFondosCed() {
        try {
            guardadoReqs = Boolean.FALSE;
            if (entidadEnEdicion != null && entidadEnEdicion.getStrPk() != null && entidadEnEdicion.getStrPk() > 0) {
                if (!tablaTransACed.isEmpty()) {

                    if (tablaTransACed.stream().filter(tac -> tac.getSeleccionado() != null).filter(tac -> tac.getSeleccionado().equals(Boolean.TRUE)).count() > 0) {
                        obtenerTransferenciasCed();
                        if (!listTraCed.isEmpty()) {
                            tablaTransACed.stream().filter(tac -> tac.getSeleccionado() != null).filter(tac -> tac.getSeleccionado().equals(Boolean.TRUE))
                                    .forEach(tac -> {
                                        listTraCed.stream().filter(tced -> tced.getTacReqFondoCed() == null)
                                                .filter(tced -> tced.getTacCedFk().getSedCodigo().equals(tac.getCodSede()))
                                                .forEach(tced -> {
                                                    SgReqFondoCed reqFondoCed = new SgReqFondoCed();
                                                    reqFondoCed.setRfcSolTransferenciaFk(entidadEnEdicion);
                                                    reqFondoCed.setRfcTransferenciaCedFk(tced);
                                                    reqFondoCed.setRfcHabilitado(Boolean.TRUE);
                                                    reqFondoCed.setRfcMonto(tced.getTacMontoAutorizado());
                                                    try {
                                                        reqFondoCedRestClient.guardar(reqFondoCed);
                                                        guardadoReqs = Boolean.TRUE;

                                                    } catch (BusinessException ex) {
                                                        Logger.getLogger(RequerimientoFondoBean.class
                                                                .getName()).log(Level.SEVERE, null, ex);
                                                    } catch (Exception ex) {
                                                        LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
                                                    }
                                                });
                                    });
                        } else {
                            JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_DET_REQ_CED_GUARDAR), "");
                        }
                    } else {
                        JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_REQS_CED_LIST_GUARDAR), "");
                    }
                }
                if (guardadoReqs) {
                    buscar();
                    guardar();
                    PrimeFaces.current().executeScript("PF('itemDialog').hide()");
                } else {
                    JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_DET_REQ_CED_GUARDAR), "");
                }
            } else {
                JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_REQS_CED_GUARDAR), "");
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Getters and setters">
    public SgRequerimientoFondo getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public LazyTransferenciaACedDataModel getTransferenciaCedDataModel() {
        return transferenciaCedDataModel;
    }

    public void setTransferenciaCedDataModel(LazyTransferenciaACedDataModel transferenciaCedDataModel) {
        this.transferenciaCedDataModel = transferenciaCedDataModel;
    }

    public Long getSolicitudTransferenciaId() {
        return solicitudTransferenciaId;
    }

    public SgCompromisoPresupuestario getCompromisoPresupuestarioEnEdicion() {
        return compromisoPresupuestarioEnEdicion;
    }

    public void setCompromisoPresupuestarioEnEdicion(SgCompromisoPresupuestario compromisoPresupuestarioEnEdicion) {
        this.compromisoPresupuestarioEnEdicion = compromisoPresupuestarioEnEdicion;
    }

    public void setSolicitudTransferenciaId(Long solicitudTransferenciaId) {
        this.solicitudTransferenciaId = solicitudTransferenciaId;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Long getTotalResultados() {
        return totalResultados;
    }

    public void setTotalResultados(Long totalResultados) {
        this.totalResultados = totalResultados;
    }

    public Integer getPaginado() {
        return paginado;
    }

    public void setPaginado(Integer paginado) {
        this.paginado = paginado;
    }

    public List<RevHistorico> getHistorialPresupuestoEscolar() {
        return historialPresupuestoEscolar;
    }

    public void setHistorialPresupuestoEscolar(List<RevHistorico> historialPresupuestoEscolar) {
        this.historialPresupuestoEscolar = historialPresupuestoEscolar;
    }

    public Boolean getBusquedaRealizada() {
        return busquedaRealizada;
    }

    public void setBusquedaRealizada(Boolean busquedaRealizada) {
        this.busquedaRealizada = busquedaRealizada;
    }

    public String getTituloPagina() {
        if (this.solicitudTransferenciaId == null) {
            return Etiquetas.getValue("nuevoReqFondo");
        } else if (this.getSoloLectura()) {
            return Etiquetas.getValue("verSolicitudTransferencia");
        } else {
            return Etiquetas.getValue("edicionReqFondo");
        }
    }

    public void setTituloPagina(String tituloPagina) {
        this.tituloPagina = tituloPagina;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public SofisComboG<SgCuentasBancariasDD> getCuentasBancariasDD() {
        return cuentasBancariasDD;
    }

    public void setCuentasBancariasDD(SofisComboG<SgCuentasBancariasDD> cuentasBancariasDD) {
        this.cuentasBancariasDD = cuentasBancariasDD;
    }

    public List<TransferenciaCedAgrup> getTablaTransACed() {
        return tablaTransACed;
    }

    public void setTablaTransACed(List<TransferenciaCedAgrup> tablaTransACed) {
        this.tablaTransACed = tablaTransACed;
    }

    public Boolean getGuardadoReqs() {
        return guardadoReqs;
    }

    public void setGuardadoReqs(Boolean guardadoReqs) {
        this.guardadoReqs = guardadoReqs;
    }

    public Long getTransferenciaGDepId() {
        return transferenciaGDepId;
    }

    public void setTransferenciaGDepId(Long transferenciaGDepId) {
        this.transferenciaGDepId = transferenciaGDepId;
    }

    public List<SelectItem> getEstadosReq() {
        return estadosReq;
    }

    public void setEstadosReq(List<SelectItem> estadosReq) {
        this.estadosReq = estadosReq;
    }

    public SgTransferenciaGDep getTransferenciaGDep() {
        return transferenciaGDep;
    }

    public void setTransferenciaGDep(SgTransferenciaGDep transferenciaGDep) {
        this.transferenciaGDep = transferenciaGDep;
    }

    public LazyReqFondoCedDataModel getReqFondoCedDataModel() {
        return reqFondoCedDataModel;
    }

    public void setReqFondoCedDataModel(LazyReqFondoCedDataModel reqFondoCedDataModel) {
        this.reqFondoCedDataModel = reqFondoCedDataModel;
    }

    public Long getTotalResultadosReq() {
        return totalResultadosReq;
    }

    public void setTotalResultadosReq(Long totalResultadosReq) {
        this.totalResultadosReq = totalResultadosReq;
    }

    public SgDireccionDepartamental getDireccionDepartamental() {
        return direccionDepartamental;
    }

    public void setDireccionDepartamental(SgDireccionDepartamental direccionDepartamental) {
        this.direccionDepartamental = direccionDepartamental;
    }

    public List<SgReqFondoCed> getNuevoResultado() {
        return nuevoResultado;
    }

    public void setNuevoResultado(List<SgReqFondoCed> nuevoResultado) {
        this.nuevoResultado = nuevoResultado;
    }

    public Boolean getHist() {
        return hist;
    }

    public void setHist(Boolean hist) {
        this.hist = hist;
    }

    public List<SgTransferenciaACed> getListTraCed() {
        return listTraCed;
    }

    public void setListTraCed(List<SgTransferenciaACed> listTraCed) {
        this.listTraCed = listTraCed;
    }

    public List<SgCompromisoPresupuestario> getCompromisos() {
        return compromisos;
    }

    public void setCompromisos(List<SgCompromisoPresupuestario> compromisos) {
        this.compromisos = compromisos;
    }

    // </editor-fold>
}
