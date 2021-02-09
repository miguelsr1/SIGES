/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgArchivo;
import sv.gob.mined.siges.web.dto.SgDocumentos;
import sv.gob.mined.siges.web.dto.SgPresupuestoEscolar;
import sv.gob.mined.siges.web.dto.SgRecibos;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.SgTransferenciaACed;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.siap2.SsCategoriaPresupuestoEscolar;
import sv.gob.mined.siges.web.dto.siap2.SsGesPresEs;
import sv.gob.mined.siges.web.enumerados.EnumEstadoDocumento;
import sv.gob.mined.siges.web.enumerados.EnumTransferenciaEstado;
import sv.gob.mined.siges.web.enumerados.TipoDocumento;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroGesPresEs;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPresupuestoEscolar;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroTransferenciaACed;
import sv.gob.mined.siges.web.lazymodels.LazyTransferenciaACedDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.ArchivoRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.CategoriaPresupuestoEscolarRestClient;
import sv.gob.mined.siges.web.restclient.GesPresEsRestClient;
import sv.gob.mined.siges.web.restclient.PresupuestoEscolarRestCliente;
import sv.gob.mined.siges.web.restclient.RecibosRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.restclient.TransferenciaACedRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 * Bean correspondiente a la gestión de las transferencias a los centros
 * educativos-
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class TransferenciaACedBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(TransferenciaACedBean.class.getName());

    @Inject
    private PresupuestoEscolarRestCliente peaClient;

    @Inject
    private TransferenciaACedRestClient restClient;

    @Inject
    private HandleArchivoBean handleArchivoBean;

    @Inject
    private RecibosRestClient recibosClient;

    @Inject
    private ArchivoRestClient archivosClient;

    @Inject
    private CatalogosRestClient restCatalogo;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private CategoriaPresupuestoEscolarRestClient componenteClient;

    @Inject
    private GesPresEsRestClient subComponenteClient;

    @Inject
    private SedeRestClient sedeRestClient;

    private Integer paginado = 10;
    private Long totalResultados;
    private String headerGenerarRecibo;
    private String tamanioImportArchivo = "1048576"; //1MB por defecto
    private String tipoImportArchivo = "/(\\.|\\/)(pdf)$/";
    LocalDate currentDate = LocalDate.now();

    private FiltroTransferenciaACed filtro = new FiltroTransferenciaACed();
    private List<SgTransferenciaACed> historialTransferenciaACed = new ArrayList();
    private LazyTransferenciaACedDataModel transferenciaACedLazyModel;

    private SofisComboG<EnumTransferenciaEstado> comboEstado = new SofisComboG<>();
    private SofisComboG<SgDepartamento> comboDepartamento = new SofisComboG<>();
    private SofisComboG<SsCategoriaPresupuestoEscolar> comboComponente = new SofisComboG<>();
    private SofisComboG<SsGesPresEs> comboSubComponente = new SofisComboG<>();

    private List<SelectItem> estadoReciboItems = new ArrayList<>();
    private List<EnumTransferenciaEstado> estados = new ArrayList();

    private SgSede sedeSeleccionadaFiltro;
    private SgTransferenciaACed entidadEnEdicion = new SgTransferenciaACed();
    private SgRecibos reciboTransferencia = new SgRecibos();

    public TransferenciaACedBean() {
    }

    /**
     * Inicializa el objeto después de crearse.
     */
    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            buscar();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Busca los datos que satisfacen el filtro.
     */
    public void buscar() {
        try {
            filtro.setFirst(new Long(0));
            filtro.setIncluirCampos(new String[]{"tacPk",
                "tacCedFk.sedPk",
                "tacCedFk.sedCodigo",
                "tacCedFk.sedNombre",
                "tacCedFk.sedTipo",
                "tacCedFk.sedVersion",
                "tacTransferenciaFk.tcId",
                "tacTransferenciaFk.componente.cpeId",
                "tacTransferenciaFk.componente.cpeNombre",
                "tacTransferenciaFk.componente.cpeVersion",
                "tacTransferenciaFk.subComponente.gesId",
                "tacTransferenciaFk.subComponente.gesNombre",
                "tacTransferenciaFk.subComponente.gesVersion",
                "tacTransferenciaFk.anioFiscal.aniPk",
                "tacTransferenciaFk.anioFiscal.aniAnio",
                "tacTransferenciaFk.anioFiscal.aniVersion",
                "tacTransferenciaFk.unidadPresupuestaria.cuNombre",
                "tacTransferenciaFk.unidadPresupuestaria.cuVersion",
                "tacTransferenciaFk.lineaPresupuestaria.cuNombre",
                "tacTransferenciaFk.lineaPresupuestaria.cuVersion",
                "tacTransferenciaFk.tcFuenteRecursosFk.nombre",
                "tacTransferenciaFk.tcFuenteRecursosFk.version",
                "tacTransferenciaFk.tcFuenteRecursosFk.fuenteFinanciamiento.nombre",
                "tacTransferenciaFk.tcFuenteRecursosFk.fuenteFinanciamiento.version",
                "tacTransferenciaFk.tcPorcentaje",
                "tacTransferenciaFk.tcVersion",
                "tacTransferenciaDireccionDepFk.tddDireccionDepFk.dedDepartamentoFk.depCodigo",
                "tacTransferenciaDireccionDepFk.tddDireccionDepFk.dedDepartamentoFk.depNombre",
                "tacTransferenciaDireccionDepFk.tddDireccionDepFk.dedDepartamentoFk.depVersion",
                "recibo.recPk",
                "recibo.recVersion",
                "tacMontoAutorizado",
                "tacMontoSolicitado",
                "tacEstado",
                "tacVersion"});

            if (comboDepartamento.getSelectedT() != null) {
                filtro.setDepartamento(comboDepartamento.getSelectedT());
            }

            if (comboEstado.getSelectedT() != null) {
                filtro.setTacEstado(comboEstado.getSelectedT());
            }

            if (comboComponente.getSelectedT() != null) {
                filtro.setComponente(comboComponente.getSelectedT().getCpeId());
            }

            if (comboSubComponente.getSelectedT() != null) {
                filtro.setSubComponente(comboSubComponente.getSelectedT().getGesId());
            }

            if (sedeSeleccionadaFiltro != null) {
                filtro.setTacSedePk(sedeSeleccionadaFiltro.getSedPk());
            }

            totalResultados = restClient.buscarTotal(filtro);
            transferenciaACedLazyModel = new LazyTransferenciaACedDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Carga los combos de la busqueda
     */
    public void cargarCombos() {
        try {
            estados = new ArrayList(Arrays.asList(EnumTransferenciaEstado.values()));
            comboEstado = new SofisComboG(estados, "text");
            comboEstado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});
            fc.setOrderBy(new String[]{"depNombre"});
            fc.setIncluirCampos(new String[]{"depNombre", "depVersion"});
            List<SgDepartamento> departamentos = restCatalogo.buscarDepartamento(fc);
            comboDepartamento = new SofisComboG(departamentos, "depNombre");
            comboDepartamento.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            FiltroCodiguera filtroComponente = new FiltroCodiguera();
            filtroComponente.setIncluirCampos(new String[]{"cpeVersion", "cpeNombre", "cpeCodigo"});
            comboComponente = new SofisComboG<>(componenteClient.buscar(filtroComponente), "cpeNombre");
            comboComponente.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboSubComponente.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Carga el combo de subcomponente
     */
    public void cargarSubComponente() {
        try {
            if (comboComponente.getSelectedT() != null) {
                FiltroGesPresEs filtroSubComponente = new FiltroGesPresEs();
                filtroSubComponente.setOrderBy(new String[]{"gesNombre"});
                filtroSubComponente.setIncluirCampos(new String[]{"gesVersion", "gesNombre", "gesCod"});
                filtroSubComponente.setCpeId(comboComponente.getSelectedT().getCpeId());
                comboSubComponente = new SofisComboG<>(subComponenteClient.buscarSubcomponente(filtroSubComponente), "gesNombre");
                comboSubComponente.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }
    }

    /**
     * Carga el combo de Sede para creación y edición de registros.
     *
     * @param query String: Palabra de filtro digitada en el combo
     * @return List<SgSede>
     */
    public List<SgSede> completeSede(String query) {
        try {
            FiltroSedes fil = new FiltroSedes();
            fil.setSedCodigoONombre(query);
            fil.setSedHabilitado(Boolean.TRUE);
            fil.setMaxResults(11L);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedCodigo", "sedNombre", "sedTipo", "sedVersion"});
            return sedeRestClient.buscar(fil);
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ce), ce);
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    /**
     * Limpia los combos de la búsqueda
     */
    private void limpiarCombos() {
        estados = new ArrayList();
        comboEstado = new SofisComboG();
        comboDepartamento = new SofisComboG();
        comboComponente = new SofisComboG();
        comboSubComponente = new SofisComboG();
        sedeSeleccionadaFiltro = null;
    }

    /**
     * Limpia los filtros seleccionados y busca de nuevo
     */
    public void limpiar() {
        filtro = new FiltroTransferenciaACed();
        limpiarCombos();
        cargarCombos();
        buscar();
    }

    /**
     * Crea un nuevo objeto de transferencia a ced
     */
    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgTransferenciaACed();
    }

    /**
     * Carga los datos de un transferencia a ced para poder ser editados.
     *
     * @param var
     */
    public void actualizar(SgTransferenciaACed var) {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgTransferenciaACed) SerializationUtils.clone(var);
    }

    /**
     * Muestra el formulario para crear o actualizar un recibo
     *
     * @param var
     */
    public void generarRecibo(SgTransferenciaACed var) {
        try {
            entidadEnEdicion = (SgTransferenciaACed) SerializationUtils.clone(var);
            FiltroPresupuestoEscolar filtro = new FiltroPresupuestoEscolar();
            filtro.setAnioFiscal(entidadEnEdicion.getTacTransferenciaFk().getAnioFiscal().getAniAnio());
            filtro.setIdSede(entidadEnEdicion.getTacCedFk().getSedPk());
            filtro.setOrderBy(new String[]{"pesPk"});

            List<SgPresupuestoEscolar> listPea = peaClient.buscar(filtro);

            if (!listPea.isEmpty()) {
                if (listPea.get(0).getPesDocumentos() != null && !listPea.get(0).getPesDocumentos().isEmpty()) {
                    if (validarDocumentos(listPea.get(0).getPesDocumentos())) {
                        if (entidadEnEdicion != null) {
                            if (entidadEnEdicion.getRecibo() != null) {
                                SgRecibos recibo = recibosClient.obtenerPorId(entidadEnEdicion.getRecibo().getRecPk());
                                if (recibo != null) {
                                    reciboTransferencia = recibo;
                                    headerGenerarRecibo = StringUtils.replace(Etiquetas.getValue("edicionReciboTransferencia"), "[x]", entidadEnEdicion.getTacPk().toString());
                                } else {
                                    JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_RECIBO_OBTENER), "");
                                }
                            } else {
                                reciboTransferencia = new SgRecibos();
                                reciboTransferencia.setRecTransferencia(entidadEnEdicion);
                                headerGenerarRecibo = StringUtils.replace(Etiquetas.getValue("generarReciboTransferencia"), "[x]", entidadEnEdicion.getTacPk().toString());
                            }
                            if (estadoReciboItems.isEmpty()) {
                                Arrays.asList(EnumEstadoDocumento.values()).forEach(e -> {
                                    estadoReciboItems.add(new SelectItem(e, e.getText()));
                                });
                            }

                            PrimeFaces.current().executeScript("PF('itemDialog').show()");
                        }
                    } else {
                        JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_RECIBO_ESTADO_DOC), "");
                    }
                } else {
                    JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_RECIBO_SIN_DOC), "");
                }
            } else {
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_RECIBO_SIN_PEA), "");
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Crea o actualiza un registro del recibo de la transferencia
     */
    public void guardarRecibo() {
        try {
            reciboTransferencia.setRecMonto(entidadEnEdicion.getTacMontoAutorizado());
            recibosClient.guardar(reciboTransferencia);
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscar();
        } catch (BusinessException be) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), be), be);
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Almancena el archivo del recibo
     *
     * @param event
     */
    public void subirArchivo(FileUploadEvent event) {
        try {
            if (reciboTransferencia.getRecArchivo() == null) {
                reciboTransferencia.setRecArchivo(new SgArchivo());
            }

            handleArchivoBean.subirArchivoTmp(event.getFile(), reciboTransferencia.getRecArchivo());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Eliminar el archivo del recibo
     *
     * @param archivoId
     * @throws Exception
     */
    public void eliminarArchivo(Long archivoId) throws Exception {
        try {
            archivosClient.eliminar(archivoId);
        } catch (BusinessException be) {
            LOGGER.log(Level.SEVERE, be.getMessage(), be);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     * Crea o actualiza un registro de transferencia a ced.
     */
    public void guardar() {
        try {
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscar();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Elimina un registro de transferencia a ced.
     */
    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getTacPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            buscar();
            entidadEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Muestra el historial del registro.
     *
     * @param id
     */
    public void historial(Long id) {
        try {
            historialTransferenciaACed = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Valida el estado de los documentos de un pea
     *
     * @param
     */
    public Boolean validarDocumentos(List<SgDocumentos> documentos) {
        Boolean validado = Boolean.FALSE;
        EnumEstadoDocumento enumResult = EnumEstadoDocumento.FIRMADO;
        TipoDocumento ccf = TipoDocumento.CONGELACION;
        TipoDocumento con = TipoDocumento.CONVENIO;
        try {
            SgDocumentos doc1 = documentos.stream().filter(d -> d.getDocTipoDocumento().equals(ccf)).findFirst().orElse(null);
            SgDocumentos doc2 = documentos.stream().filter(d -> d.getDocTipoDocumento().equals(con)).findFirst().orElse(null);
            if (doc1 != null && doc2 != null) {
                if (doc1.getDocEstadoDocumento().equals(enumResult) && doc2.getDocEstadoDocumento().equals(enumResult)) {
                    validado = Boolean.TRUE;
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return validado;
    }

    /**
     * Getter - Setter.
     */
    // <editor-fold defaultstate="collapsed" desc="Getter's-Setter's">

    public FiltroTransferenciaACed getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroTransferenciaACed filtro) {
        this.filtro = filtro;
    }

    public SgTransferenciaACed getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgTransferenciaACed entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgTransferenciaACed> getHistorialTransferenciaACed() {
        return historialTransferenciaACed;
    }

    public void setHistorialTransferenciaACed(List<SgTransferenciaACed> historialTransferenciaACed) {
        this.historialTransferenciaACed = historialTransferenciaACed;
    }

    public Integer getPaginado() {
        return paginado;
    }

    public void setPaginado(Integer paginado) {
        this.paginado = paginado;
    }

    public Long getTotalResultados() {
        return totalResultados;
    }

    public void setTotalResultados(Long totalResultados) {
        this.totalResultados = totalResultados;
    }

    public LazyTransferenciaACedDataModel getTransferenciaACedLazyModel() {
        return transferenciaACedLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyTransferenciaACedDataModel transferenciaACedLazyModel) {
        this.transferenciaACedLazyModel = transferenciaACedLazyModel;
    }

    public SofisComboG<EnumTransferenciaEstado> getComboEstado() {
        return comboEstado;
    }

    public void setComboEstado(SofisComboG<EnumTransferenciaEstado> comboEstado) {
        this.comboEstado = comboEstado;
    }

    public SofisComboG<SgDepartamento> getComboDepartamento() {
        return comboDepartamento;
    }

    public void setComboDepartamento(SofisComboG<SgDepartamento> comboDepartamento) {
        this.comboDepartamento = comboDepartamento;
    }

    public SgRecibos getReciboTransferencia() {
        return reciboTransferencia;
    }

    public void setReciboTransferencia(SgRecibos reciboTransferencia) {
        this.reciboTransferencia = reciboTransferencia;
    }

    public String getHeaderGenerarRecibo() {
        return headerGenerarRecibo;
    }

    public void setHeaderGenerarRecibo(String headerGenerarRecibo) {
        this.headerGenerarRecibo = headerGenerarRecibo;
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

    public LocalDate getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(LocalDate currentDate) {
        this.currentDate = currentDate;
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

    public List<SelectItem> getEstadoReciboItems() {
        return estadoReciboItems;
    }

    public void setEstadoReciboItems(List<SelectItem> estadoReciboItems) {
        this.estadoReciboItems = estadoReciboItems;
    }

    public SgSede getSedeSeleccionadaFiltro() {
        return sedeSeleccionadaFiltro;
    }

    public void setSedeSeleccionadaFiltro(SgSede sedeSeleccionadaFiltro) {
        this.sedeSeleccionadaFiltro = sedeSeleccionadaFiltro;
    }

    // </editor-fold>
}
