/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
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
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgAnioLectivo;
import sv.gob.mined.siges.web.dto.SgCajaChica;
import sv.gob.mined.siges.web.dto.SgCuentasBancarias;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.siap2.SsCategoriaPresupuestoEscolar;
import sv.gob.mined.siges.web.dto.siap2.SsGesPresEs;
import sv.gob.mined.siges.web.enumerados.EnumAnioLectivoEstado;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAnioLectivo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCajaChica;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCuentasBancarias;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroGesPresEs;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.lazymodels.LazyCajasChicasDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AnioLectivoRestClient;
import sv.gob.mined.siges.web.restclient.BancosRestClient;
import sv.gob.mined.siges.web.restclient.CajaChicaRestClient;
import sv.gob.mined.siges.web.restclient.CategoriaPresupuestoEscolarRestClient;
import sv.gob.mined.siges.web.restclient.CuentasBancariasRestClient;
import sv.gob.mined.siges.web.restclient.GesPresEsRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 * Bean para la gestión de las cuentas bancarias de caja chica.
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class CajaChicaBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(CuentasBancariasBean.class.getName());

    @Inject
    private CajaChicaRestClient restClient;

    @Inject
    private SedeRestClient sedeRestClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private BancosRestClient bancosClient;
    
    @Inject
    private CategoriaPresupuestoEscolarRestClient componenteClient;
    
    @Inject
    private GesPresEsRestClient subComponenteClient;
    
    @Inject
    private AnioLectivoRestClient anioLectivoRestClient;
    
    @Inject
    private CuentasBancariasRestClient cuentaBancariaClient;

    private FiltroCodiguera filtro = new FiltroCodiguera();
    private FiltroCajaChica filtroBanco = new FiltroCajaChica();
    private SgCajaChica entidadEnEdicion = new SgCajaChica();
    private List<SgCajaChica> historialCuentasBancarias = new ArrayList();
    private Integer paginado = 10;
    private String titulo = new String("");
    private Long totalResultados;
    private LazyCajasChicasDataModel cuentasBancariasLazyModel;
    private SgSede sedeSeleccionada;
    private SgSede sedeSeleccionadaFiltro;
    private Boolean panelComponente = Boolean.FALSE;
    private String origenFondo = StringUtils.EMPTY;
    private SofisComboG<SsCategoriaPresupuestoEscolar> comboComponente = new SofisComboG<>();
    private SofisComboG<SsGesPresEs> comboSubComponente = new SofisComboG<>();
    private SofisComboG<SgAnioLectivo> comboAnioLectivo = new SofisComboG<>();
    private SofisComboG<SsCategoriaPresupuestoEscolar> comboComponenteF = new SofisComboG<>();
    private SofisComboG<SsGesPresEs> comboSubComponenteF = new SofisComboG<>();

    /**
     * Constructor de la clase.
     */
    public CajaChicaBean() {
    }

    /**
     * Método para incializar filtros, combos y búsqueda de caja chica.
     */
    @PostConstruct
    public void init() {
        try {
            filtro = new FiltroCodiguera();
            cargarCombos();
            buscar();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Busca los registros de caja chica según los filtros utilizados.
     */
    public void buscar() {
        try {
            filtroBanco.setFirst(new Long(0));
            filtroBanco.setIncluirCampos(new String[]{
            "bccPk","bccNumeroCuenta","bccTitular","bccComentario",
            "bccSedeFk.sedPk","bccSedeFk.sedCodigo","bccSedeFk.sedNombre","bccSedeFk.sedVersion","bccSedeFk.sedTipo",
            "bccSubcomponenteFk.gesId","bccSubcomponenteFk.gesCod","bccSubcomponenteFk.gesNombre","bccSubcomponenteFk.gesVersion",
            "bccSubcomponenteFk.gesCategoriaComponente.cpeId","bccSubcomponenteFk.gesCategoriaComponente.cpeNombre","bccSubcomponenteFk.gesCategoriaComponente.cpeVersion",
            "bccAnioFk.alePk","bccAnioFk.aleAnio","bccAnioFk.aleVersion","bccOtroIngreso","bccHabilitado","bccVersion","bccUltModFecha","bccUltModUsuario"
            });
            if (sedeSeleccionadaFiltro != null) {
                filtroBanco.setBccSedeFk(sedeSeleccionadaFiltro.getSedPk());
            }
            
           if(comboSubComponenteF.getSelectedT()!=null){
               filtroBanco.setSubComponenteFk(comboSubComponenteF.getSelectedT().getGesId());
           }
            
            totalResultados = restClient.buscarTotal(filtroBanco);
            cuentasBancariasLazyModel = new LazyCajasChicasDataModel(restClient, filtroBanco, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Carga los combos de filtros
     */
    public void cargarCombos() {
        try {
            FiltroCodiguera filtroComponente = new FiltroCodiguera();
            filtroComponente.setIncluirCampos(new String[]{"cpeVersion", "cpeNombre", "cpeCodigo"});
            comboComponenteF = new SofisComboG<>(componenteClient.buscar(filtroComponente), "cpeNombre");
            comboComponenteF.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboSubComponenteF.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    /**
     * Carga los combos de agregar.
     */
    public void cargarCombosAgregar() {
        try {
            comboAnioLectivo = new SofisComboG<>(cargarAnios(), "aleAnio");
            comboAnioLectivo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    /**
     * Carga el combo de años electivos
     *
     * @return List<SgAnioElectivo>
     */
    public List<SgAnioLectivo> cargarAnios() {
        try {
            FiltroAnioLectivo filtroAnioLectivo = new FiltroAnioLectivo();
            filtro.setIncluirCampos(new String[]{"alePk", "aleAnio", "aleVersion"});
            filtroAnioLectivo.setAleEstado(EnumAnioLectivoEstado.ABIERTO);
            filtroAnioLectivo.setOrderBy(new String[]{"aleAnio"});
            filtroAnioLectivo.setAscending(new boolean[]{false});
            List<SgAnioLectivo> listAniosLectivos = anioLectivoRestClient.buscar(filtroAnioLectivo);
            return listAniosLectivos;
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
     * Se obtiene el origen de fondos de la caja chica
     *
     */
    public void obtenerOrigenFondo() {
        try {
            if(sedeSeleccionada!=null && comboSubComponente.getSelectedT()!=null && comboSubComponente.getSelectedT().getTipoCuentaBancaria()!=null){
                FiltroCuentasBancarias filtro = new FiltroCuentasBancarias();
                filtro.setCbcSedeFk(sedeSeleccionada.getSedPk());
                filtro.setCbcCuentaTipoCuenta(comboSubComponente.getSelectedT().getTipoCuentaBancaria().getTcbPk());
                filtro.setCbcHabilitado(Boolean.TRUE);
                filtro.setIncluirCampos(new String[]{"cbcPk", "cbcVersion", "cbcNumeroCuenta","cbcBancoFk.bncPk","cbcBancoFk.bncNombre","cbcBancoFk.bncVersion"});
                List<SgCuentasBancarias> listCuenta = cuentaBancariaClient.buscar(filtro);
                if(listCuenta!=null && !listCuenta.isEmpty()){
                    SgCuentasBancarias banc = listCuenta.get(0);
                    if(banc!=null){
                        entidadEnEdicion.setBccCuentaBancFk(banc);
                        origenFondo = banc.getCbcNumeroCuenta().concat(" - ").concat(banc.getCbcBancoFk().getBncNombre());
                    }
                }
            }
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ce), ce);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }
    }
    
    
    /**
     * Otro Ingreso
     */
    public void esOtroIngreso() {
        if(entidadEnEdicion.getBccOtroIngreso().equals(Boolean.TRUE)){
            panelComponente = Boolean.FALSE;
        }
        else{
            cargarComponentes();
            panelComponente = Boolean.TRUE;
            if(entidadEnEdicion.getBccPk()!=null && entidadEnEdicion.getBccSubcomponenteFk()!=null){
                comboComponente.setSelectedT(entidadEnEdicion.getBccSubcomponenteFk().getGesCategoriaComponente());
                cargarSubComponentes();
                comboSubComponente.setSelectedT(entidadEnEdicion.getBccSubcomponenteFk());
            }
        }
    }
    
    /**
     * Obtiene el número de cuenta 
     */
    public void obtenerNumeroCuenta() {
        String numCuenta = new String();
        if(comboAnioLectivo.getSelectedT()!=null){
            if(sedeSeleccionada!=null){
                numCuenta = comboAnioLectivo.getSelectedT().getAleAnio().toString().concat(" - ").concat(sedeSeleccionada.getSedCodigo());
                entidadEnEdicion.setBccNumeroCuenta(numCuenta);
                obtenerOrigenFondo();
            }
        }
    }
    
    /**
     * Carga el combo de componente
     */
    public void cargarComponentes() {
         try {
            FiltroCodiguera filtroComponente = new FiltroCodiguera();
            filtroComponente.setIncluirCampos(new String[]{"cpeVersion", "cpeNombre", "cpeCodigo"});
            comboComponente = new SofisComboG<>(componenteClient.buscar(filtroComponente), "cpeNombre");
            comboComponente.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboSubComponente.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ce), ce);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }
    }
    
    /**
     * Carga el combo de subcomponente para filtros
     */
    public void cargarSubComponentesF() {
         try {
            if (comboComponenteF.getSelectedT() != null) {
                comboSubComponenteF = new SofisComboG();
                FiltroGesPresEs filtroSubComponente = new FiltroGesPresEs();
                filtroSubComponente.setOrderBy(new String[]{"gesNombre"});
                filtroSubComponente.setIncluirCampos(new String[]{"gesVersion", "gesNombre", "gesCod","gesId"});
                filtroSubComponente.setCpeId(comboComponenteF.getSelectedT().getCpeId());
                comboSubComponenteF = new SofisComboG<>(subComponenteClient.buscarSubcomponente(filtroSubComponente), "gesNombre");
                comboSubComponenteF.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ce), ce);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }
    }
    
    /**
     * Carga el combo de subcomponente
     */
    public void cargarSubComponentes() {
         try {
            if (comboComponente.getSelectedT() != null) {
                origenFondo = StringUtils.EMPTY;
                comboSubComponente = new SofisComboG();
                FiltroGesPresEs filtroSubComponente = new FiltroGesPresEs();
                filtroSubComponente.setOrderBy(new String[]{"gesNombre"});
                filtroSubComponente.setIncluirCampos(new String[]{"gesVersion", "gesNombre", "gesCod","gesId","tipoCuentaBancaria.tcbPk","tipoCuentaBancaria.tcbVersion"});
                filtroSubComponente.setCpeId(comboComponente.getSelectedT().getCpeId());
                comboSubComponente = new SofisComboG<>(subComponenteClient.buscarSubcomponente(filtroSubComponente), "gesNombre");
                comboSubComponente.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ce), ce);
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
     * Carga el combo de Sede para el filtro de pantalla.
     *
     * @param query String: Palabra de filtro digitada en el combo
     * @return List<SgSede>
     */
    public List<SgSede> completeSedeFiltro(String query) {
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
     * Limpia los combos usados para filtrar
     */
    private void limpiarCombos() {
        sedeSeleccionadaFiltro=null;
        comboComponenteF.setSelected(-1);
        comboSubComponenteF.setSelected(-1);
    }
    
    /**
     * Limpia los combos usados para filtrar y crear.
     */
    private void limpiarCombosAgregar() {
        comboAnioLectivo.setSelected(-1);
        comboComponente.setSelected(-1);
        comboSubComponente.setSelected(-1);
    }

    /**
     * Limpia los filtros.
     */
    public void limpiar() {
        filtro = new FiltroCodiguera();
        filtroBanco = new FiltroCajaChica();
        limpiarCombos();
        buscar();

    }

    /**
     * Limpia los filtros, además de crear un nuevo objeto de caja chica.
     */
    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombosAgregar();
        entidadEnEdicion = new SgCajaChica();
        sedeSeleccionada = null;
        entidadEnEdicion.setBccOtroIngreso(Boolean.TRUE);
        panelComponente = Boolean.FALSE;
        origenFondo = StringUtils.EMPTY;
        setTitulo();
        cargarCombosAgregar();
    }

    /**
     * Carga los datos de una cuenta para poder ser editados.
     *
     * @param var SgCajaChica: Elemento del grid seleccionado de caja chica.
     */
    public void actualizar(SgCajaChica var) {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        cargarCombosAgregar();
        origenFondo = StringUtils.EMPTY;
        entidadEnEdicion = (SgCajaChica) SerializationUtils.clone(var);
        sedeSeleccionada = entidadEnEdicion.getBccSedeFk();
        if(entidadEnEdicion.getBccAnioFk()!=null){
            comboAnioLectivo.setSelectedT(entidadEnEdicion.getBccAnioFk());
        }
        
        if(entidadEnEdicion.getBccOtroIngreso()!=null){
            esOtroIngreso();
        }
        else{
            entidadEnEdicion.setBccOtroIngreso(Boolean.TRUE);
            panelComponente = Boolean.FALSE;
        }
        obtenerOrigenFondo();
        setTitulo();
    }

    /**
     * Crea o actualiza un registro de caja chica.
     */
    public void guardar() {
        try {
            if(entidadEnEdicion.getBccOtroIngreso().equals(Boolean.FALSE)){
                if(comboComponente.getSelectedT()!=null && comboSubComponente.getSelectedT()!=null){
                    entidadEnEdicion.setBccSubcomponenteFk(comboSubComponente.getSelectedT());
                }
            }
            else{
                entidadEnEdicion.setBccSubcomponenteFk(null);
                entidadEnEdicion.setBccCuentaBancFk(null);
            }
            
            entidadEnEdicion.setBccAnioFk(comboAnioLectivo.getSelectedT());
            entidadEnEdicion.setBccSedeFk(sedeSeleccionada);
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
     * Elimina un registro de caja chica.
     */
    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getBccPk());
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
     * @param id Long
     */
    public void historial(Long id) {
        try {
            historialCuentasBancarias = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Getter - Setter.
     */
    // <editor-fold defaultstate="collapsed" desc="Getter's-Setter's">
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo() {
        if (entidadEnEdicion.getBccPk() != null) {
            titulo = Etiquetas.getValue("edicionCuentasBancariasCC");
        } else {
            titulo = Etiquetas.getValue("nuevoCuentasBancariasCC");
        }
    }

    public FiltroCodiguera getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCodiguera filtro) {
        this.filtro = filtro;
    }

    public SgCajaChica getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgCajaChica entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgCajaChica> getHistorialCuentasBancarias() {
        return historialCuentasBancarias;
    }

    public void setHistorialCuentasBancarias(List<SgCajaChica> historialCuentasBancarias) {
        this.historialCuentasBancarias = historialCuentasBancarias;
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

    public LazyCajasChicasDataModel getCuentasBancariasLazyModel() {
        return cuentasBancariasLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyCajasChicasDataModel cuentasBancariasLazyModel) {
        this.cuentasBancariasLazyModel = cuentasBancariasLazyModel;
    }

    public FiltroCajaChica getFiltroBanco() {
        return filtroBanco;
    }

    public void setFiltroBanco(FiltroCajaChica filtroBanco) {
        this.filtroBanco = filtroBanco;
    }

    public SgSede getSedeSeleccionada() {
        return sedeSeleccionada;
    }

    public void setSedeSeleccionada(SgSede sedeSeleccionada) {
        this.sedeSeleccionada = sedeSeleccionada;
    }

    public SgSede getSedeSeleccionadaFiltro() {
        return sedeSeleccionadaFiltro;
    }

    public void setSedeSeleccionadaFiltro(SgSede sedeSeleccionadaFiltro) {
        this.sedeSeleccionadaFiltro = sedeSeleccionadaFiltro;
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
    
    public Boolean getPanelComponente() {
        return panelComponente;
    }

    public void setPanelComponente(Boolean panelComponente) {
        this.panelComponente = panelComponente;
    }
    
    public SofisComboG<SgAnioLectivo> getComboAnioLectivo() {
        return comboAnioLectivo;
    }

    public void setComboAnioLectivo(SofisComboG<SgAnioLectivo> comboAnioLectivo) {
        this.comboAnioLectivo = comboAnioLectivo;
    }
    
    
    public String getOrigenFondo() {
        return origenFondo;
    }

    public void setOrigenFondo(String origenFondo) {
        this.origenFondo = origenFondo;
    }
    
    
    public SofisComboG<SsCategoriaPresupuestoEscolar> getComboComponenteF() {
        return comboComponenteF;
    }

    public void setComboComponenteF(SofisComboG<SsCategoriaPresupuestoEscolar> comboComponenteF) {
        this.comboComponenteF = comboComponenteF;
    }

    public SofisComboG<SsGesPresEs> getComboSubComponenteF() {
        return comboSubComponenteF;
    }

    public void setComboSubComponenteF(SofisComboG<SsGesPresEs> comboSubComponenteF) {
        this.comboSubComponenteF = comboSubComponenteF;
    }
    
    
    // </editor-fold>  
}
