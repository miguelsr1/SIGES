/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgAnioLectivo;
import sv.gob.mined.siges.web.dto.SgRequerimientoFondo;
import sv.gob.mined.siges.web.dto.SgTransferenciaDireccionDep;
import sv.gob.mined.siges.web.dto.SgTransferenciaGDep;
import sv.gob.mined.siges.web.dto.siap2.SsCategoriaPresupuestoEscolar;
import sv.gob.mined.siges.web.dto.siap2.SsCuenta;
import sv.gob.mined.siges.web.dto.siap2.SsFuenteFinanciamiento;
import sv.gob.mined.siges.web.dto.siap2.SsFuenteRecursos;
import sv.gob.mined.siges.web.dto.siap2.SsGesPresEs;
import sv.gob.mined.siges.web.enumerados.EnumEstadoSolicitudesTransferencia;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAnioLectivo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCuenta;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroFuenteRecursos;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroGesPresEs;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRequerimientosFondo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroTransferenciaGDep;
import sv.gob.mined.siges.web.lazymodels.LazyRequerimientoFondoDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AnioLectivoRestClient;
import sv.gob.mined.siges.web.restclient.CategoriaPresupuestoEscolarRestClient;
import sv.gob.mined.siges.web.restclient.CuentaRestClient;
import sv.gob.mined.siges.web.restclient.FuenteFinanciamientoRestClient;
import sv.gob.mined.siges.web.restclient.FuenteRecursosRestClient;
import sv.gob.mined.siges.web.restclient.GesPresEsRestClient;
import sv.gob.mined.siges.web.restclient.RequerimientoFondoRestClient;
import sv.gob.mined.siges.web.restclient.TransferenciaDireccionDepRestClient;
import sv.gob.mined.siges.web.restclient.TransferenciaGDepRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 * Bean para la gestión de solicitudes transferencia.
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class SolicitudesTransferenciaBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(SolicitudesTransferenciaBean.class.getName());

    @Inject
    private RequerimientoFondoRestClient restClient;
    @Inject
    private SessionBean sessionBean;
    @Inject
    private AnioLectivoRestClient anioLectRestClient;

    @Inject
    private CuentaRestClient cuentaRestClient;

    @Inject
    private TransferenciaGDepRestClient restTransferenciaGDep;

    @Inject
    private FuenteFinanciamientoRestClient fuenteFinClient;

    @Inject
    private FuenteRecursosRestClient fuenteRecClient;

    @Inject
    private CategoriaPresupuestoEscolarRestClient componenteClient;

    @Inject
    private GesPresEsRestClient subComponenteClient;

    @Inject
    private TransferenciaDireccionDepRestClient transferenciaDireccionDepRestClient;

    @Inject
    @Param(name = "idTgd")
    private Long transferenciaGDepId;

    private Integer paginado = 10;
    private Long totalResultados;
    private Integer selectedTab;
    private String tituloPagina;
    private Boolean soloLectura = Boolean.FALSE;

    private SgRequerimientoFondo entidadEnEdicion = new SgRequerimientoFondo();
    private SgTransferenciaDireccionDep transferenciaDireccionDep = new SgTransferenciaDireccionDep();
    private SgTransferenciaGDep transferenciaGDep = new SgTransferenciaGDep();
    private FiltroRequerimientosFondo filtro = new FiltroRequerimientosFondo();
    private LazyRequerimientoFondoDataModel solicitudTransferenciaDataModel;

    private List<RevHistorico> historialRequerimientoFondo = new ArrayList();
    private List<SgRequerimientoFondo> listaDesembolsosSeleccionados = new ArrayList();

    private SofisComboG<SsCategoriaPresupuestoEscolar> comboComponente = new SofisComboG<>();
    private SofisComboG<SsGesPresEs> comboSubComponente = new SofisComboG<>();
    private SofisComboG<SgAnioLectivo> anioLectivoCombo = new SofisComboG<>();
    private SofisComboG<SsCuenta> comboUnidadPresupuestaria = new SofisComboG<>();
    private SofisComboG<SsCuenta> comboLineaPresupuestaria = new SofisComboG<>();
    private SofisComboG<SsFuenteFinanciamiento> comboFuenteFin = new SofisComboG<>();
    private SofisComboG<SsFuenteRecursos> comboFuenteRec = new SofisComboG<>();
    private SofisComboG<EnumEstadoSolicitudesTransferencia> comboEstados = new SofisComboG<>();

    private InputStream file;
    private String nombreFile;
    private Boolean uploading = Boolean.FALSE;
    private Integer filasCantMax = 500;
    private String tamanioImportArchivo = "1048576"; //1MB por defecto
    private String tipoImportArchivo = "/(\\.|\\/)(xlsx)$/";//xlsx por defecto

    public SolicitudesTransferenciaBean() {
    }

    /**
     * Inicializa el objeto después de crearse.
     */
    @PostConstruct
    public void init() {
        try {
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

            buscar();
            cargarCombos();
            setTituloPagina();

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Carga los combos de filtro
     */
    public void cargarCombos() {
        try {
            FiltroAnioLectivo fal = new FiltroAnioLectivo();
            fal.setOrderBy(new String[]{"aleAnio"});
            fal.setAscending(new boolean[]{false});
            fal.setIncluirCampos(new String[]{"alePk", "aleAnio", "aleVersion"});
            List<SgAnioLectivo> listAnio = anioLectRestClient.buscar(fal);
            anioLectivoCombo = new SofisComboG(listAnio, "aleAnio");
            anioLectivoCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            FiltroCodiguera filtroComponente = new FiltroCodiguera();
            filtroComponente.setIncluirCampos(new String[]{"cpeVersion", "cpeNombre", "cpeCodigo"});
            filtroComponente.setOrderBy(new String[]{"cpeNombre"});
            comboComponente = new SofisComboG<>(componenteClient.buscar(filtroComponente), "cpeNombre");
            comboComponente.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            cargarEstados();

            cargarComboUnidadPresupuestaria();
            cargarComboFuenteFin();
            setDefaultValueCombo();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Carga los combos de estados
     */
    private void cargarEstados() {
        try {
            comboEstados = new SofisComboG<>(Arrays.asList(EnumEstadoSolicitudesTransferencia.values()), "text");
            comboEstados.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Se carga el combo de subcomponente.
     */
    public void cargarSubComponente() {
        try {
            if (comboComponente.getSelectedT() != null) {
                FiltroGesPresEs filtroSubCo = new FiltroGesPresEs();
                filtroSubCo.setCpeId(comboComponente.getSelectedT().getCpeId());
                filtroSubCo.setIncluirCampos(new String[]{"gesVersion", "gesNombre", "gesCod"});
                filtroSubCo.setOrderBy(new String[]{"gesNombre"});
                comboSubComponente = new SofisComboG<>(subComponenteClient.buscarSubcomponente(filtroSubCo), "gesNombre");
                comboSubComponente.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }
    }

    /**
     * Se carga el combo de unidad presupuestaria (para realizar búsquedas).
     */
    private void cargarComboUnidadPresupuestaria() {
        try {
            FiltroCuenta unp = new FiltroCuenta();
            unp.setCuHabilitado(Boolean.TRUE);
            unp.setOrderBy(new String[]{"cuNombre"});
            unp.setIncluirCampos(new String[]{"cuId", "cuNombre", "cuVersion"});
            unp.setAscending(new boolean[]{true});
            List<SsCuenta> listUnidad = cuentaRestClient.buscarUnidadPresupuestaria(unp);
            comboUnidadPresupuestaria = new SofisComboG(listUnidad, "cuNombre");
            comboUnidadPresupuestaria.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (Exception ex) {
            Logger.getLogger(PresupuestoEscolarBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Se carga el combo de línea presupuestaria (para realizar búsquedas).
     */
    public void cargarComboLineaPresupuestaria() {
        try {
            if (comboUnidadPresupuestaria.getSelectedT() != null) {
                FiltroCuenta lin = new FiltroCuenta();
                lin.setCuCuentaPadre(comboUnidadPresupuestaria.getSelectedT());
                lin.setCuHabilitado(Boolean.TRUE);
                lin.setOrderBy(new String[]{"cuNombre"});
                lin.setIncluirCampos(new String[]{"cuId", "cuNombre", "cuVersion"});
                lin.setAscending(new boolean[]{true});
                List<SsCuenta> listUnidad = cuentaRestClient.buscar(lin);
                comboLineaPresupuestaria = new SofisComboG(listUnidad, "cuNombre");
                comboLineaPresupuestaria.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }

        } catch (Exception ex) {
            Logger.getLogger(PresupuestoEscolarBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Se carga el combo de unidad presupuestaria (para realizar búsquedas).
     */
    private void cargarComboFuenteFin() {
        try {
            FiltroCodiguera filtro = new FiltroCodiguera();
            filtro.setHabilitado(Boolean.TRUE);
            filtro.setOrderBy(new String[]{"nombre"});
            filtro.setIncluirCampos(new String[]{"id", "nombre", "version"});
            filtro.setAscending(new boolean[]{true});
            List<SsFuenteFinanciamiento> listFF = fuenteFinClient.buscar(filtro);
            comboFuenteFin = new SofisComboG(listFF, "nombre");
            comboFuenteFin.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (Exception ex) {
            Logger.getLogger(PresupuestoEscolarBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Se carga el combo de línea presupuestaria (para realizar búsquedas).
     */
    public void cargarComboFuenteRec() {
        try {
            if (comboFuenteFin.getSelectedT() != null) {
                FiltroFuenteRecursos filtro = new FiltroFuenteRecursos();
                filtro.setFinId(comboFuenteFin.getSelectedT().getId());
                filtro.setOrderBy(new String[]{"nombre"});
                filtro.setIncluirCampos(new String[]{"id", "nombre", "version"});
                filtro.setAscending(new boolean[]{true});
                List<SsFuenteRecursos> listRec = fuenteRecClient.buscar(filtro);
                comboFuenteRec = new SofisComboG(listRec, "nombre");
                comboFuenteRec.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }

        } catch (Exception ex) {
            Logger.getLogger(PresupuestoEscolarBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Se asigna el primer valor por defecto de un combo.
     */
    private void setDefaultValueCombo() {
        comboSubComponente.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboLineaPresupuestaria.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboFuenteRec.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
    }

    /**
     * Crea un nuevo objeto
     */
    public void agregar() {
        entidadEnEdicion = new SgRequerimientoFondo();
    }

    /**
     * Busca los datos que satisfacen el filtro.
     */
    public void buscar() {
        try {
            filtro.setFirst(new Long(0));

            if (transferenciaGDep.getTgdPk() != null) {
                filtro.setTransferenciaGDep(transferenciaGDep.getTgdPk());
            }

            if (comboFuenteFin.getSelectedT() != null) {
                filtro.setFuenteFinId(comboFuenteFin.getSelectedT().getId());
            }

            if (comboFuenteRec.getSelectedT() != null) {
                filtro.setFuenteRecId(comboFuenteRec.getSelectedT().getId());
            }

            if (comboUnidadPresupuestaria.getSelectedT() != null) {
                filtro.setUnidadId(comboUnidadPresupuestaria.getSelectedT().getCuId());
            }

            if (comboLineaPresupuestaria.getSelectedT() != null) {
                filtro.setLineaId(comboLineaPresupuestaria.getSelectedT().getCuId());
            }

            if (comboComponente.getSelectedT() != null) {
                filtro.setComponenteId(comboComponente.getSelectedT().getCpeId());
            }

            if (comboSubComponente.getSelectedT() != null) {
                filtro.setSubComponenteId(comboSubComponente.getSelectedT().getGesId());
            }

            if (anioLectivoCombo.getSelectedT() != null) {
                filtro.setAnioFiscal(anioLectivoCombo.getSelectedT().getAleAnio());
            }

            if (comboEstados.getSelectedT() != null) {
                filtro.setEstado(comboEstados.getSelectedT());
            }

            filtro.setIncluirCampos(new String[]{"strPk",
                "strTransferenciaGDepFk.tgdPk",
                "strTransferenciaGDepFk.tgdTransferenciaFk.traSubcomponente.gesCategoriaComponente.cpeId",
                "strTransferenciaGDepFk.tgdTransferenciaFk.traSubcomponente.gesCategoriaComponente.cpeNombre",
                "strTransferenciaGDepFk.tgdTransferenciaFk.traSubcomponente.gesCategoriaComponente.cpeVersion",
                "strTransferenciaGDepFk.tgdTransferenciaFk.traSubcomponente.gesId",
                "strTransferenciaGDepFk.tgdTransferenciaFk.traSubcomponente.gesNombre",
                "strTransferenciaGDepFk.tgdTransferenciaFk.traSubcomponente.gesVersion",
                "strTransferenciaGDepFk.tgdTransferenciaFk.traAnioFiscal.aniAnio",
                "strTransferenciaGDepFk.tgdTransferenciaFk.traAnioFiscal.aniVersion",
                "strTransferenciaGDepFk.tgdTransferenciaFk.traLineaPresupuestaria.cuNombre",
                "strTransferenciaGDepFk.tgdTransferenciaFk.traLineaPresupuestaria.cuVersion",
                "strTransferenciaGDepFk.tgdTransferenciaFk.traLineaPresupuestaria.cuCuentaPadre.cuNombre",
                "strTransferenciaGDepFk.tgdTransferenciaFk.traLineaPresupuestaria.cuCuentaPadre.cuVersion",
                "strTransferenciaGDepFk.tgdVersion",
                "strCuentaBancDdFk.cbdPk",
                "strCuentaBancDdFk.cbdVersion",
                "strEstado",
                "strSacGOES",
                "strSacUFI",
                "strImporteTotal",
                "strCompromisoPresupuestario",
                "strVersion"
            });
            filtro.setOrderBy(new String[]{"strPk"});
            filtro.setAscending(new boolean[]{false});
            totalResultados = restClient.buscarTotal(filtro);
            //LOGGER.log(Level.SEVERE,"TOTAL DESEMBOLSOS" + totalResultados.toString());
            solicitudTransferenciaDataModel = new LazyRequerimientoFondoDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Carga los datos de un solicitud de transferencia para poder ser editados.
     *
     * @param req
     */
    public void actualizar(SgRequerimientoFondo req) {
        try {
            entidadEnEdicion = restClient.obtenerPorId(req.getStrPk());

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Carga el titulo de la página..
     */
    public void setTituloPagina() {

        if (transferenciaGDep.getTgdPk() != null) {
            tituloPagina = Etiquetas.getValue("requerimientoFondosTransferencia").concat(" ").concat(transferenciaGDep.getTgdPk().toString());
        }
        if (this.transferenciaDireccionDep.getTddPk() != null) {
            tituloPagina = Etiquetas.getValue("requerimientoFondosTransferencia").concat(" ").concat(transferenciaDireccionDep.getTddPk().toString());
        } else if (this.transferenciaDireccionDep.getTddPk() == null) {
            tituloPagina = Etiquetas.getValue("requerimientoFondos");
        } else if (this.soloLectura) {
            tituloPagina = Etiquetas.getValue("verSolicitudTransferencia");
        } else {
            tituloPagina = Etiquetas.getValue("edicionSolicitudTransferencia");
        }
    }

    /**
     * Muestra el historial del registro.
     *
     * @param id
     */
    public void historial(Long id) {
        try {
            historialRequerimientoFondo = restClient.obtenerHistorialPorId(id);

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Limpia los filtros seleccionados y busca de nuevo
     */
    public void limpiar() {
        filtro = new FiltroRequerimientosFondo();
        limpiarCombos();
        buscar();
    }

    /**
     * Limpia los combos usados para filtrar y crear.
     */
    public void limpiarCombos() {
        comboFuenteFin.setSelected(-1);
        comboFuenteRec = new SofisComboG();
        comboUnidadPresupuestaria.setSelected(-1);
        comboLineaPresupuestaria = new SofisComboG();
        comboComponente.setSelected(-1);
        comboSubComponente = new SofisComboG();
        anioLectivoCombo.setSelected(-1);
        comboEstados = new SofisComboG<>();
        cargarEstados();
        setDefaultValueCombo();
    }

    /**
     * Elimina un registro.
     */
    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getStrPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            buscar();
            entidadEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Crea o actualiza un registro de solicitud de transferencia.
     */
    public void guardar() {
        try {
            restClient.guardar(entidadEnEdicion);
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscar();
        } catch (BusinessException be) {
            LOGGER.log(Level.SEVERE, be.getMessage(), be);
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Inicialización de las variables para subida de planilla
     */
    public void nuevoImport() {
        nombreFile = null;
        file = null;
        uploading = Boolean.FALSE;
    }

    /**
     * Subida de archivo de planilla
     */
    public void handleFileUpload(FileUploadEvent event) {
        try {
            nombreFile = event.getFile().getFileName();
            file = event.getFile().getInputstream();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Muestra recurso Loading
     */
    public void renderLoading() {
        if (file != null) {
            if (uploading) {
                uploading = Boolean.FALSE;
                procesarArchivo();
            } else {
                uploading = Boolean.TRUE;
            }
        }
    }

    /**
     * Procesa el archivo cargado.
     */
    public void procesarArchivo() {
        org.apache.poi.ss.usermodel.Workbook myWorkBook = null;
        try {
            if (file != null) {
                myWorkBook = WorkbookFactory.create(file);
                org.apache.poi.ss.usermodel.Sheet mySheet = myWorkBook.getSheetAt(0);

                Integer cantRows = 0;
                Integer rowsSave = 0;

                // Get iterator to all the rows in current sheet
                Iterator<Row> rowIterator = mySheet.iterator();
                List<String[]> archivo = new ArrayList<>();

                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();

                    if (row.getRowNum() > 0) {
                        Cell cell;
                        Long reqId = null;

                        //Se extraen los demas datos
                        String[] datos = new String[4];

                        //No REQUERIMIENTO
                        cell = row.getCell(0, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if (cell != null && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            reqId = Math.round(cell.getNumericCellValue());
                        } else {
                            file = null;
                            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_ID_REQ_IMPORT_VACIO), "");
                            return;
                        }

                        datos[0] = String.valueOf(reqId);

                        //COMPROMISO PRESUPUESTARIO
                        cell = row.getCell(1,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if (cell != null) {
                            if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
                                datos[1] = Double.toString(cell.getNumericCellValue());
                            }
                            else if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                                datos[1] = cell.getStringCellValue();
                            }
                        } else {
                            file = null;
                            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_COMPROMISO_IMPORT_VACIO), "");
                            return;
                        }

                        //SAC UFI
                        cell = row.getCell(2,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if (cell != null) {
                            if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
                                datos[2] = Double.toString(cell.getNumericCellValue());
                            }
                            else if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                                datos[2] = cell.getStringCellValue();
                            }
                        } else {
                            file = null;
                            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_SAC_UFI_IMPORT_VACIO), "");
                            return;
                        }

                        //SAC GOES
                        cell = row.getCell(3,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if (cell != null) {
                            if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
                                datos[3] = Double.toString(cell.getNumericCellValue());
                            }
                            else if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                                datos[3] = cell.getStringCellValue();
                            }
                        } else {
                            file = null;
                            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_SAC_GOES_IMPORT_VACIO), "");
                            return;
                        }

                        archivo.add(datos);
                        cantRows++;

                    }
                }

                if (cantRows > filasCantMax) {
                    file = null;
                    JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, "La cantidad de filas del archivo no puede ser mayor a " + filasCantMax + ".", "");
                    return;
                }
                
                FiltroRequerimientosFondo filtro = new FiltroRequerimientosFondo();
                filtro.setIncluirCampos(new String[]{"strPk",
                    "strTransferenciaGDepFk.tgdPk",
                    "strTransferenciaGDepFk.tgdVersion",
                    "strCuentaBancDdFk.cbdPk",
                    "strCuentaBancDdFk.cbdVersion",
                    "strEstado",
                    "strSacGOES",
                    "strSacUFI",
                    "strImporteTotal",
                    "strVersion"
                });
                filtro.setEstado(EnumEstadoSolicitudesTransferencia.RECIBIDA_PRESUPUESTO);
                filtro.setOrderBy(new String[]{"strPk"});
                filtro.setAscending(new boolean[]{false});
                List<SgRequerimientoFondo> listReqs = restClient.buscar(filtro);
                
                for (String[] s : archivo) {
                    for (SgRequerimientoFondo req : listReqs) {
                        if (req.getStrPk().equals(Long.parseLong(s[0]))) {
                            req.setStrSacUFI(s[2]);
                            req.setStrSacGOES(s[3]);
                            req.setStrEstado(EnumEstadoSolicitudesTransferencia.TRAMITADO_ANTE_HACIENDA);
                            restClient.guardar(req);
                            rowsSave++;
                        }
                    }
                }

                if (rowsSave > 0) {
                    buscar();
                    PrimeFaces.current().executeScript("PF('itemDialog').hide()");
                    JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, StringUtils.replace(Mensajes.obtenerMensaje(Mensajes.IMPORT_REQ_SAC_CORRECTA), "[x]", rowsSave.toString()), "");
                } else {
                    PrimeFaces.current().executeScript("PF('itemDialog').hide()");
                    JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_IMPORT_REQ_SAC), "");
                }

            }
        } catch (BusinessException be) {
            file = null;
            JSFUtils.agregarMensajesDetalle(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            file = null;
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        } finally {
            if (myWorkBook != null) {
                try {
                    myWorkBook.close();
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
                }
            }
        }
    }

    /**
     * Elimina el archivo cargado.
     */
    public void eliminarFile() {
        file = null;
        nombreFile = null;
    }

    /**
     * Muestra los datos de auditoría.
     *
     * @param estId Long: Id del registro
     * @param estRev Long: Revisión del registro
     */
    public void mostrarDatos(Long estId, Long estRev) {
        try {
            limpiarCombos();
            if (estId != null && estId > 0) {
                if (estRev != null && estRev > 0) {
                    entidadEnEdicion = restClient.obtenerEnRevision(estId, estRev);
                } else {
                    entidadEnEdicion = restClient.obtenerPorId(estId);
                }
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="getters y setters">
    public void setEntidadEnEdicion(SgRequerimientoFondo entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public SgRequerimientoFondo getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public FiltroRequerimientosFondo getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroRequerimientosFondo filtro) {
        this.filtro = filtro;
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

    public List<RevHistorico> getHistorialRequerimientoFondo() {
        return historialRequerimientoFondo;
    }

    public void setHistorialRequerimientoFondo(List<RevHistorico> historialRequerimientoFondo) {
        this.historialRequerimientoFondo = historialRequerimientoFondo;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public Integer getSelectedTab() {
        return selectedTab;
    }

    public void setSelectedTab(Integer selectedTab) {
        this.selectedTab = selectedTab;
    }

    public LazyRequerimientoFondoDataModel getSolicitudTransferenciaDataModel() {
        return solicitudTransferenciaDataModel;
    }

    public void setSolicitudTransferenciaDataModel(LazyRequerimientoFondoDataModel solicitudTransferenciaDataModel) {
        this.solicitudTransferenciaDataModel = solicitudTransferenciaDataModel;
    }

    public SofisComboG<SgAnioLectivo> getAnioLectivoCombo() {
        return anioLectivoCombo;
    }

    public void setAnioLectivoCombo(SofisComboG<SgAnioLectivo> anioLectivoCombo) {
        this.anioLectivoCombo = anioLectivoCombo;
    }

    public List<SgRequerimientoFondo> getListaDesembolsosSeleccionados() {
        return listaDesembolsosSeleccionados;
    }

    public void setListaDesembolsosSeleccionados(List<SgRequerimientoFondo> listaDesembolsosSeleccionados) {
        this.listaDesembolsosSeleccionados = listaDesembolsosSeleccionados;
    }

    public SgTransferenciaDireccionDep getTransferenciaDireccionDep() {
        return transferenciaDireccionDep;
    }

    public void setTransferenciaDireccionDep(SgTransferenciaDireccionDep transferenciaDireccionDep) {
        this.transferenciaDireccionDep = transferenciaDireccionDep;
    }

    public String getTituloPagina() {
        return tituloPagina;
    }

    public SofisComboG<SsCuenta> getComboUnidadPresupuestaria() {
        return comboUnidadPresupuestaria;
    }

    public void setComboUnidadPresupuestaria(SofisComboG<SsCuenta> comboUnidadPresupuestaria) {
        this.comboUnidadPresupuestaria = comboUnidadPresupuestaria;
    }

    public SofisComboG<SsCuenta> getComboLineaPresupuestaria() {
        return comboLineaPresupuestaria;
    }

    public void setComboLineaPresupuestaria(SofisComboG<SsCuenta> comboLineaPresupuestaria) {
        this.comboLineaPresupuestaria = comboLineaPresupuestaria;
    }

    public SofisComboG<SsFuenteFinanciamiento> getComboFuenteFin() {
        return comboFuenteFin;
    }

    public void setComboFuenteFin(SofisComboG<SsFuenteFinanciamiento> comboFuenteFin) {
        this.comboFuenteFin = comboFuenteFin;
    }

    public SofisComboG<SsFuenteRecursos> getComboFuenteRec() {
        return comboFuenteRec;
    }

    public void setComboFuenteRec(SofisComboG<SsFuenteRecursos> comboFuenteRec) {
        this.comboFuenteRec = comboFuenteRec;
    }

    public SofisComboG<SsCategoriaPresupuestoEscolar> getComboComponente() {
        return comboComponente;
    }

    public void setComboComponente(SofisComboG<SsCategoriaPresupuestoEscolar> comboComponente) {
        this.comboComponente = comboComponente;
    }

    public SofisComboG<SsGesPresEs> getComboSubComponente() {
        return comboSubComponente;
    }

    public void setComboSubComponente(SofisComboG<SsGesPresEs> comboSubComponente) {
        this.comboSubComponente = comboSubComponente;
    }

    public Boolean getUploading() {
        return uploading;
    }

    public void setUploading(Boolean uploading) {
        this.uploading = uploading;
    }

    public String getTamanioImportArchivo() {
        return tamanioImportArchivo;
    }

    public void setTamanioImportArchivo(String tamanioImportArchivo) {
        this.tamanioImportArchivo = tamanioImportArchivo;
    }

    public String getTipoImportArchivo() {
        return tipoImportArchivo;
    }

    public void setTipoImportArchivo(String tipoImportArchivo) {
        this.tipoImportArchivo = tipoImportArchivo;
    }

    public InputStream getFile() {
        return file;
    }

    public void setFile(InputStream file) {
        this.file = file;
    }

    public String getNombreFile() {
        return nombreFile;
    }

    public void setNombreFile(String nombreFile) {
        this.nombreFile = nombreFile;
    }

    public SgTransferenciaGDep getTransferenciaGDep() {
        return transferenciaGDep;
    }

    public void setTransferenciaGDep(SgTransferenciaGDep transferenciaGDep) {
        this.transferenciaGDep = transferenciaGDep;
    }

    public Long getTransferenciaGDepId() {
        return transferenciaGDepId;
    }

    public void setTransferenciaGDepId(Long transferenciaGDepId) {
        this.transferenciaGDepId = transferenciaGDepId;
    }

    public SofisComboG<EnumEstadoSolicitudesTransferencia> getComboEstados() {
        return comboEstados;
    }

    public void setComboEstados(SofisComboG<EnumEstadoSolicitudesTransferencia> comboEstados) {
        this.comboEstados = comboEstados;
    }

    // </editor-fold>
}
