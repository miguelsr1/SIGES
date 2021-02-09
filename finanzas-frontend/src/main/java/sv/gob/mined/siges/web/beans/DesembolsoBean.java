/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
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
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.DesembolsoDDE;
import sv.gob.mined.siges.web.dto.DesembolsoMovimiento;
import sv.gob.mined.siges.web.dto.RequerimientoPorRecurso;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgDesembolso;
import sv.gob.mined.siges.web.dto.SgDetalleDesembolso;
import sv.gob.mined.siges.web.dto.SgReqFondoCed;
import sv.gob.mined.siges.web.dto.SgRequerimientoFondo;
import sv.gob.mined.siges.web.enumerados.EnumDesembolsoEstado;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDetalleDesembolso;
import sv.gob.mined.siges.web.lazymodels.LazyDesembolsoDataModel;
import sv.gob.mined.siges.web.lazymodels.LazyDetalleDesembolsosDataModel;
import sv.gob.mined.siges.web.lazymodels.LazyRequerimientoFondoDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AnioLectivoRestClient;
import sv.gob.mined.siges.web.restclient.DesembolsoRestClient;
import sv.gob.mined.siges.web.restclient.DetalleDesembolsosRestClient;
import sv.gob.mined.siges.web.restclient.MovimientoDireccionDepartamentalRestClient;
import sv.gob.mined.siges.web.restclient.RequerimientoFondoRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
/**
 * Bean para la gestión de los desembolsos
 */
public class DesembolsoBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(DesembolsoBean.class.getName());

    @Inject
    private DesembolsoRestClient restClient;

    @Inject
    private RequerimientoFondoRestClient restReqFondo;

    @Inject
    private DetalleDesembolsosRestClient restDetDesembolso;

    @Inject
    private MovimientoDireccionDepartamentalRestClient restMovDireccionDep;

    @Inject
    private SessionBean sessionBean;
    @Inject
    private AnioLectivoRestClient anioLectRestClient;

    @Inject
    @Param(name = "id")
    private Long desembolsoId;

    private SgDesembolso entidadEnEdicion = new SgDesembolso();
    private SgDetalleDesembolso detalleDesEnEdicion = new SgDetalleDesembolso();
    private FiltroDetalleDesembolso filtro = new FiltroDetalleDesembolso();

    private Long totalResultados;
    private Long totalResultadosReq;
    private Long totalResultadosDet;
    private Integer paginado = 25;
    private Integer paginadoDet = 10;
    private Boolean soloLectura = Boolean.FALSE;
    private Boolean desembolsosGuardados = Boolean.FALSE;
    private BigDecimal totalDesGuardados = BigDecimal.ZERO;
    private BigDecimal totalImporte = BigDecimal.ZERO;
    private String tituloPagina = new String("");
    private LazyDetalleDesembolsosDataModel detalleDesembolsosLazyModel;
    private LazyDesembolsoDataModel desembolsoDataModel;
    private LazyRequerimientoFondoDataModel requerimientoFondoDataModel;

    private SofisComboG<EnumDesembolsoEstado> estadoDesCombo = new SofisComboG<>();
    private List<SgRequerimientoFondo> listaReqSeleccionados;
    private List<SgDetalleDesembolso> listDetDesembolsos = new ArrayList();
    private List<RevHistorico> historialMovimientos = new ArrayList();
    private List<RequerimientoPorRecurso> listReqsPorRecurso = new ArrayList();
    private List<RequerimientoPorRecurso> tablaReqsPorRecurso = new ArrayList();
    private List<SgDetalleDesembolso> listReqsRecurso = new ArrayList();
    private List<SgReqFondoCed> reqsFondoCed = new ArrayList();

    private Integer selectedTab;

    private InputStream file;
    private String nombreFile;
    private Boolean uploading = Boolean.FALSE;
    private Boolean saving = Boolean.FALSE;
    private Integer filasCantMax = 2000;
    private String tamanioImportArchivo = "1048576"; //1MB por defecto
    private String tipoImportArchivo = "/(\\.|\\/)(xlsx)$/";//xlsx por defecto
    private LocalDateTime fechaMovs = LocalDateTime.now();

    public DesembolsoBean() {
    }

    /**
     * Inicializa el objeto después de crearse.
     */
    @PostConstruct
    public void init() {
        try {
            if (desembolsoId != null && desembolsoId > 0) {
                SgDesembolso des = restClient.obtenerPorId(desembolsoId);
                if (des != null) {
                    actualizar(des);
                    cargarCombos();
                    buscar();
                }
            } else {
                agregar();
            }
            getTituloPagina();

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Carga los combos de filtro
     */
    public void cargarCombos() {
        try {
            estadoDesCombo = new SofisComboG<>(Arrays.asList(EnumDesembolsoEstado.values()), "text");
            estadoDesCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            if (entidadEnEdicion.getDsbEstado() != null) {
                estadoDesCombo.setSelectedT(entidadEnEdicion.getDsbEstado());
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Crea un nuevo objeto de desembolso
     */
    public void agregar() {

        entidadEnEdicion = new SgDesembolso();
        entidadEnEdicion.setDsbEstado(EnumDesembolsoEstado.EN_PROCESO);
        entidadEnEdicion.setDsbPorcentaje(0D);
    }

    /**
     * Busca el listado de los requerimientos agrupados por recurso.
     */
    public void buscarReqsPorRecurso() {
        try {
            listReqsPorRecurso = new ArrayList();
            listReqsPorRecurso = restReqFondo.obtenerReqsPorRecurso();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Busca los datos que satisfacen el filtro.
     */
    public void buscar() {
        try {
            desembolsosGuardados = Boolean.TRUE;
            filtro.setFirst(new Long(0));
            filtro.setOrderBy(new String[]{"dedPk"});
            filtro.setIncluirCampos(new String[]{
                "dedPk", "dedReqFondoFk.strPk",
                "dedReqFondoFk.strCuentaBancDdFk.cbdPk",
                "dedReqFondoFk.strCuentaBancDdFk.cbdNumeroCuenta",
                "dedReqFondoFk.strCuentaBancDdFk.cbdVersion",
                "dedReqFondoFk.strSacGOES",
                "dedReqFondoFk.strSacUFI",
                "dedReqFondoFk.strCompromisoPresupuestario",
                "dedFuenteRecursosFk.nombre",
                "dedFuenteRecursosFk.codigo",
                "dedFuenteRecursosFk.fuenteFinanciamiento.nombre",
                "dedPorcentaje",
                "dedMonto",
                "dedVersion"});
            filtro.setAscending(new boolean[]{false});
            filtro.setDedDesembolsoFk(entidadEnEdicion.getDsbPk());
            totalResultadosDet = restDetDesembolso.buscarTotal(filtro);
            listDetDesembolsos = restDetDesembolso.buscar(filtro);
            //detalleDesembolsosLazyModel = new LazyDetalleDesembolsosDataModel(restDetDesembolso, filtro, totalResultadosDet, paginadoDet);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Suma total de los desembolsos guardados
     */
    public BigDecimal totalDesembolsos() {
        try {

            totalDesGuardados = listDetDesembolsos.stream().map(d -> d.getDedMonto()).reduce(BigDecimal.ZERO, BigDecimal::add);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }
        return totalDesGuardados;
    }

    /**
     * Carga los datos de un desembolso para poder ser editados.
     *
     * @param req
     */
    public void actualizar(SgDesembolso req) {
        try {
            entidadEnEdicion = req;

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Actualiza los saldos de las cuentas bancarias.
     */
    public void actualizarCuentasBancarias() {
        try {
            DesembolsoMovimiento des = new DesembolsoMovimiento();
            des.setFechaMov(fechaMovs);
            entidadEnEdicion = restClient.crearMovimientos(des);

            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.DESEMBOLSO_MOVIMIENTO_GUARDAR), "");
            PrimeFaces.current().executeScript("PF('generarMovsDialog').hide()");
        } catch (BusinessException be) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), be), be);
            PrimeFaces.current().executeScript("PF('generarMovsDialog').hide()");
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            PrimeFaces.current().executeScript("PF('generarMovsDialog').hide()");
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Obtiene el título de la página correspondiente al bean
     *
     * @return
     */
    public String getTituloPagina() {
        if (this.desembolsoId == null) {
            return Etiquetas.getValue("nuevoDesembolso");
        } else if (this.soloLectura) {
            return Etiquetas.getValue("verDesembolso");
        } else {
            return Etiquetas.getValue("edicionDesembolso");
        }
    }

    /**
     * Muestra el historial del registro.
     *
     * @param id
     */
    public void historial(Long id) {
        try {
            historialMovimientos = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Limpia los filtros seleccionados y busca de nuevo
     */
    public void limpiar() {
        filtro = new FiltroDetalleDesembolso();
        buscar();
    }

    /**
     * Inicializa el objeto a eliminar
     *
     * @param des SgDetalleDesembolso
     */
    public void preEliminar(SgDetalleDesembolso des) {
        try {
            detalleDesEnEdicion = (SgDetalleDesembolso) SerializationUtils.clone(des);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Elimina un registro de detalle desembolso.
     */
    public void eliminar() {
        try {
            restDetDesembolso.eliminar(detalleDesEnEdicion.getDedPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            buscar();
            detalleDesEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Crea o actualiza un registro de desembolso.
     */
    public void guardarConDetalle() {
        try {
            if (!tablaReqsPorRecurso.isEmpty()) {
                DesembolsoDDE desDDE = new DesembolsoDDE();
                desDDE.setListReqsRecurso(tablaReqsPorRecurso);
                desDDE.setFechaMov(fechaMovs);
                entidadEnEdicion = restClient.guardarConDetalle(desDDE);

                buscar();
                tablaReqsPorRecurso = new ArrayList();
                PrimeFaces.current().executeScript("PF('generarMovsDialog').hide()");
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            } else {
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_LISTA_DET_DESEMBOLSO), "");
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Actualiza el estado de un registro de desembolso.
     */
    public void cambiarEstadoDes() {
        try {
            entidadEnEdicion.setDsbEstado(EnumDesembolsoEstado.AUTORIZADO);
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
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
     * Render en subida archivo de planilla
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
     * Procesa el archivo de planilla
     */
    public void procesarArchivo() {
        org.apache.poi.ss.usermodel.Workbook myWorkBook = null;
        DecimalFormat df2 = new DecimalFormat("#.##");
        tablaReqsPorRecurso = new ArrayList();
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
                        Double porcentaje = null;
                        Double porcentajeDiv = null;
                        BigDecimal monto= BigDecimal.ZERO;

                        //Se extraen los demas datos
                        String[] datos = new String[5];

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

                        //CODIGO DE FUENTE DE RECURSOS
                        cell = row.getCell(1, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if (cell != null && StringUtils.isNotBlank(cell.getStringCellValue())) {
                            datos[1] = cell.getStringCellValue();
                        } else {
                            file = null;
                            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_NIE_IMPORT_VACIO), "");
                            return;
                        }

                        //PORCENTAJE
                        cell = row.getCell(2, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if (cell != null && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            porcentaje = cell.getNumericCellValue();
                            porcentajeDiv = cell.getNumericCellValue() / 100;
                        } else {
                            file = null;
                            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_COMPROMISO_IMPORT_VACIO), "");
                            return;
                        }
                        datos[2] = porcentaje.toString();
                        datos[3] = df2.format(porcentajeDiv);
                        
                        //IMPORTE
                        cell = row.getCell(3, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if (cell != null && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            monto = BigDecimal.valueOf(cell.getNumericCellValue());
                        } else {
                            file = null;
                            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_COMPROMISO_IMPORT_VACIO), "");
                            return;
                        }
                        datos[4] = monto.toString();
                        
                        
                        if (!porcentaje.equals(0D)) {
                            archivo.add(datos);
                            cantRows++;
                        }

                    }
                }

                if (cantRows > filasCantMax) {
                    file = null;
                    JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, "La cantidad de filas del archivo no puede ser mayor a " + filasCantMax + ".", "");
                    return;
                }

                buscarReqsPorRecurso();
                if (listReqsPorRecurso.isEmpty()) {
                    file = null;
                    JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, "No se ha podido procesar el archivo", "");
                    return;
                }

                for (String[] s : archivo) {
                    for (RequerimientoPorRecurso req : listReqsPorRecurso) {
                        if (req.getReqPk().equals(Long.parseLong(s[0])) && req.getRecurso().equals(s[1])) {
                            req.setPorcentaje(Double.valueOf(s[2]));
                            req.setMontoDesembolso(new BigDecimal(s[4]));
                            tablaReqsPorRecurso.add(req);
                            rowsSave++;
                        }
                    }
                }
                if(!tablaReqsPorRecurso.isEmpty()){
                    totalResultadosReq = new Long(tablaReqsPorRecurso.size());
                    totalImporte = tablaReqsPorRecurso.stream().map(d -> d.getMontoDesembolso()).reduce(BigDecimal.ZERO, BigDecimal::add);
                }
            }
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
     * Elimina los archivo de planilla
     */
    public void eliminarFile() {
        file = null;
        nombreFile = null;
    }

    /**
     * Inicialización de las variables para generación de movimientos
     */
    public void nuevosMovs() {
        saving = Boolean.FALSE;
    }

    /**
     * Render en la generación de movimientos a cuenta bancaria
     */
    public void renderLoadingGenerarMovs() {
        if (saving) {
            saving = Boolean.FALSE;
            guardarConDetalle();
        } else {
            saving = Boolean.TRUE;
        }
    }

    // <editor-fold defaultstate="collapsed" desc="getters y setters">
    public void setEntidadEnEdicion(SgDesembolso entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public SgDesembolso getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public FiltroDetalleDesembolso getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroDetalleDesembolso filtro) {
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

    public List<RevHistorico> getHistorialPresupuestoEscolar() {
        return historialMovimientos;
    }

    public void setHistorialPresupuestoEscolar(List<RevHistorico> historialMovimientos) {
        this.historialMovimientos = historialMovimientos;
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

    public LazyDesembolsoDataModel getDesembolsoDataModel() {
        return desembolsoDataModel;
    }

    public void setDesembolsoDataModel(LazyDesembolsoDataModel desembolsoDataModel) {
        this.desembolsoDataModel = desembolsoDataModel;
    }

    public SofisComboG<EnumDesembolsoEstado> getEstadoDesCombo() {
        return estadoDesCombo;
    }

    public void setEstadoDesCombo(SofisComboG<EnumDesembolsoEstado> estadoDesCombo) {
        this.estadoDesCombo = estadoDesCombo;
    }

    public List<SgRequerimientoFondo> getListaReqSeleccionados() {
        return listaReqSeleccionados;
    }

    public void setListaReqSeleccionados(List<SgRequerimientoFondo> listaReqSeleccionados) {
        this.listaReqSeleccionados = listaReqSeleccionados;
    }

    public Long getTotalResultadosReq() {
        return totalResultadosReq;
    }

    public void setTotalResultadosReq(Long totalResultadosReq) {
        this.totalResultadosReq = totalResultadosReq;
    }

    public LazyRequerimientoFondoDataModel getRequerimientoFondoDataModel() {
        return requerimientoFondoDataModel;
    }

    public void setRequerimientoFondoDataModel(LazyRequerimientoFondoDataModel requerimientoFondoDataModel) {
        this.requerimientoFondoDataModel = requerimientoFondoDataModel;
    }

    public List<SgDetalleDesembolso> getListDetDesembolsos() {
        return listDetDesembolsos;
    }

    public void setListDetDesembolsos(List<SgDetalleDesembolso> listDetDesembolsos) {
        this.listDetDesembolsos = listDetDesembolsos;
    }

    public List<SgDetalleDesembolso> getListReqsRecurso() {
        return listReqsRecurso;
    }

    public void setListReqsRecurso(List<SgDetalleDesembolso> listReqsRecurso) {
        this.listReqsRecurso = listReqsRecurso;
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

    public List<RequerimientoPorRecurso> getTablaReqsPorRecurso() {
        return tablaReqsPorRecurso;
    }

    public void setTablaReqsPorRecurso(List<RequerimientoPorRecurso> tablaReqsPorRecurso) {
        this.tablaReqsPorRecurso = tablaReqsPorRecurso;
    }

    public Long getTotalResultadosDet() {
        return totalResultadosDet;
    }

    public void setTotalResultadosDet(Long totalResultadosDet) {
        this.totalResultadosDet = totalResultadosDet;
    }

    public Integer getPaginadoDet() {
        return paginadoDet;
    }

    public void setPaginadoDet(Integer paginadoDet) {
        this.paginadoDet = paginadoDet;
    }

    public LazyDetalleDesembolsosDataModel getDetalleDesembolsosLazyModel() {
        return detalleDesembolsosLazyModel;
    }

    public void setDetalleDesembolsosLazyModel(LazyDetalleDesembolsosDataModel detalleDesembolsosLazyModel) {
        this.detalleDesembolsosLazyModel = detalleDesembolsosLazyModel;
    }

    public Boolean getDesembolsosGuardados() {
        return desembolsosGuardados;
    }

    public void setDesembolsosGuardados(Boolean desembolsosGuardados) {
        this.desembolsosGuardados = desembolsosGuardados;
    }

    public BigDecimal getTotalDesGuardados() {
        return totalDesGuardados;
    }

    public void setTotalDesGuardados(BigDecimal totalDesGuardados) {
        this.totalDesGuardados = totalDesGuardados;
    }

    public BigDecimal getTotalImporte() {
        return totalImporte;
    }

    public void setTotalImporte(BigDecimal totalImporte) {
        this.totalImporte = totalImporte;
    }

    public Boolean getSaving() {
        return saving;
    }

    public void setSaving(Boolean saving) {
        this.saving = saving;
    }

    public LocalDateTime getFechaMovs() {
        return fechaMovs;
    }

    public void setFechaMovs(LocalDateTime fechaMovs) {
        this.fechaMovs = fechaMovs;
    }

    // </editor-fold>
}
