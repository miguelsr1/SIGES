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
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgBancos;
import sv.gob.mined.siges.web.dto.SgCuentasBancarias;
import sv.gob.mined.siges.web.dto.SgRelCuentaTipoCuenta;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoCuentaBancaria;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCuentasBancarias;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelCuentaTipoCuenta;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.lazymodels.LazyCuentasBancariasDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.BancosRestClient;
import sv.gob.mined.siges.web.restclient.CategoriaPresupuestoEscolarRestClient;
import sv.gob.mined.siges.web.restclient.CuentasBancariasRestClient;
import sv.gob.mined.siges.web.restclient.DepartamentoRestClient;
import sv.gob.mined.siges.web.restclient.RelCuentaTipoCuentaRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.restclient.TipoCuentasBancariaRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 * Bean para la gestión de las cuentas bancarias (genérico)
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class CuentasBancariasBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(CuentasBancariasBean.class.getName());

    @Inject
    private CuentasBancariasRestClient restClient;

    @Inject
    private SedeRestClient sedeRestClient;

    @Inject
    private DepartamentoRestClient departamentoRestClient;
    
    @Inject
    private CategoriaPresupuestoEscolarRestClient componenteClient;
    
    @Inject
    private RelCuentaTipoCuentaRestClient relCTipoCuentaRestClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private BancosRestClient bancosClient;
    
    
    @Inject
    private TipoCuentasBancariaRestClient tipoCuentaBaClient;

    private FiltroCodiguera filtro = new FiltroCodiguera();
    private FiltroCuentasBancarias filtroBanco = new FiltroCuentasBancarias();
    private SgCuentasBancarias entidadEnEdicion = new SgCuentasBancarias();
    private List<RevHistorico> historialCuentasBancarias = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyCuentasBancariasDataModel cuentasBancariasLazyModel;
    private SofisComboG<SgBancos> comboBancos = new SofisComboG<>();
    private SgSede sedeSeleccionada;
    private SgSede sedeSeleccionadaFiltro;
    private List<SgTipoCuentaBancaria> tiposSeleccionadas = new ArrayList();
    private boolean historial = false;
    private String titulo = "";
    private SgDepartamento departamentoSeleccionadoFiltro;
    private SofisComboG<SgDepartamento> comboDepartamentos = new SofisComboG<>();
    private SofisComboG<SgTipoCuentaBancaria> comboTipoCuenta = new SofisComboG<>();

    /**
     * Constructor de la clase.
     */
    public CuentasBancariasBean() {
    }

    /**
     * Método para incializar filtros, combos y búsqueda.
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
     * Busca las cuentas bancarias según los filtros utilizados.
     */
    public void buscar() {
        try {
            filtroBanco.setFirst(new Long(0));
            
            
            if (sedeSeleccionadaFiltro != null) {
                filtroBanco.setCbcSedeFk(sedeSeleccionadaFiltro.getSedPk());
            }
            
            if (comboDepartamentos.getSelectedT() != null) {

                filtroBanco.setDepartamentoFk(comboDepartamentos.getSelectedT().getDepPk());
            }
            
            if(comboTipoCuenta.getSelectedT()!=null){
                filtroBanco.setCbcCuentaTipoCuenta(comboTipoCuenta.getSelectedT().getTcbPk());
            }
            
            filtroBanco.setIncluirCampos(new String[]{"cbcPk",
                "cbcNumeroCuenta",
                "cbcSedeFk.sedPk",
                "cbcSedeFk.sedCodigo",
                "cbcSedeFk.sedNombre",
                "cbcSedeFk.sedTipo",
                "cbcSedeFk.sedDireccion.dirDepartamento.depPk",
                "cbcSedeFk.sedDireccion.dirDepartamento.depCodigo",
                "cbcSedeFk.sedDireccion.dirDepartamento.depNombre",
                "cbcSedeFk.sedDireccion.dirDepartamento.depHabilitado",
                "cbcSedeFk.sedDireccion.dirDepartamento.depVersion",
                "cbcSedeFk.sedNombreBusqueda",
                "cbcSedeFk.sedVersion",
                "cbcComentario",
                "cbcHabilitado",
                "cbcOtroIngreso",
                "cbcTitular",
                "cbcBancoFk.bncPk",
                "cbcBancoFk.bncNombre",
                "cbcBancoFk.bncVersion",
                "cbcVersion"
            });
            filtroBanco.setOrderBy(new String[]{"cbcPk"});
            filtroBanco.setAscending(new boolean[]{false});
            totalResultados = restClient.buscarTotal(filtroBanco);
            cuentasBancariasLazyModel = new LazyCuentasBancariasDataModel(restClient, filtroBanco, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Carga el combo de bancos.
     */
    public void cargarCombos() {
        try {
            filtro = new FiltroCodiguera();
            filtro.setIncluirCampos(new String[]{"bncPk", "bncCodigo", "bncNombre", "bncVersion"});
            filtro.setOrderBy(new String[]{"bncCodigo"});
            filtro.setAscending(new boolean[]{true});
            comboBancos = new SofisComboG<>(bancosClient.buscar(filtro), "codigoNombre");
            comboBancos.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            FiltroCodiguera fil = new FiltroCodiguera();
            fil.setIncluirCampos(new String[]{"depPk", "depCodigo", "depNombre", "depVersion"});
            fil.setMaxResults(16L);
            fil.setOrderBy(new String[]{"depCodigo"});
            fil.setAscending(new boolean[]{true});
            comboDepartamentos = new SofisComboG<>(departamentoRestClient.buscar(fil), "depCodigoNombre");
            comboDepartamentos.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            comboTipoCuenta = new SofisComboG<>(completeTipoCuentas(null), "tcbNombre");
            comboTipoCuenta.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (HttpServerException ex) {
            Logger.getLogger(CuentasBancariasBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (HttpClientException ex) {
            Logger.getLogger(CuentasBancariasBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (HttpServerUnavailableException ex) {
            Logger.getLogger(CuentasBancariasBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BusinessException ex) {
            Logger.getLogger(CuentasBancariasBean.class.getName()).log(Level.SEVERE, null, ex);
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
     * Carga el combo de tipo de cuenta.
     *
     * @param query String: Palabra de filtro digitada en el combo
     * @return List<SgTipoCuentaBancaria>
     */
    public List<SgTipoCuentaBancaria> completeTipoCuentas(String query) {
        try {
            tiposSeleccionadas = new ArrayList();
            FiltroCodiguera filTcb = new FiltroCodiguera();
            filTcb.setNombre(query);
            filTcb.setHabilitado(Boolean.TRUE);
            filTcb.setMaxResults(11L);
            filTcb.setOrderBy(new String[]{"tcbNombre"});
            filTcb.setAscending(new boolean[]{false});
            filTcb.setIncluirCampos(new String[]{
                "tcbCodigo",
                "tcbNombre",
                "tcbNombreBusqueda",
                "tcbHabilitado",
                "tcbVersion"});
            return tipoCuentaBaClient.buscar(filTcb);
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
            fil.setIncluirCampos(new String[]{
                "sedCodigo",
                "sedNombre",
                "sedDireccion.dirDepartamento.depPk",
                "sedDireccion.dirDepartamento.depCodigo",
                "sedDireccion.dirDepartamento.depNombre",
                "sedDireccion.dirDepartamento.depHabilitado",
                "sedDireccion.dirDepartamento.depVersion",
                "sedTipo",
                "sedVersion"});
            fil.setSedCodigoONombre(query);
            if (comboDepartamentos.getSelectedT() != null) {
                fil.setSedDepartamentoId(comboDepartamentos.getSelectedT().getDepPk());
            }
            fil.setSedHabilitado(Boolean.TRUE);
            fil.setMaxResults(11L);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});

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
     * Carga el combo de Departamento para el filtro de pantalla.
     *
     * @param query String: Palabra de filtro digitada en el combo
     * @return List<SgDepartamento>
     */
    public List<SgDepartamento> completeDepartamentoFiltro(String query) {
        try {
            FiltroCodiguera fil = new FiltroCodiguera();
            fil.setCodigoONombre(query);
            fil.setMaxResults(16L);
            fil.setOrderBy(new String[]{"depCodigo"});
            fil.setAscending(new boolean[]{true});
            fil.setIncluirCampos(new String[]{"depPk", "depCodigo", "depNombre", "depVersion"});
            return departamentoRestClient.buscar(fil);
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
     * Obtiene los tipos de cuenta de una cuenta bancaria
     *
     * @return List<SgRelCuentaTipoCuenta>
     */
    public List<SgRelCuentaTipoCuenta> obtenerTipoCuentas() {
        try {
            FiltroRelCuentaTipoCuenta fil = new FiltroRelCuentaTipoCuenta();
            fil.setCuentaBancPk(entidadEnEdicion.getCbcPk());
            fil.setIncluirCampos(new String[]{"relPk", "cbcTipoCuentaBacFk.tcbPk", "cbcTipoCuentaBacFk.tcbNombre", "cbcTipoCuentaBacFk.tcbVersion", "relVersion"});
            return relCTipoCuentaRestClient.buscar(fil);
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
     * Limpia los combos usados para filtrar y crear.
     */
    private void limpiarCombos() {
        tiposSeleccionadas = new ArrayList<>();
        comboBancos = new SofisComboG<>();
        comboDepartamentos = new SofisComboG<>();
        sedeSeleccionadaFiltro = null;
        departamentoSeleccionadoFiltro = null;
        sedeSeleccionada = null;
        cargarCombos();
    }

    /**
     * Limpia los filtros.
     */
    public void limpiar() {
        filtro = new FiltroCodiguera();
        filtroBanco = new FiltroCuentasBancarias();
        comboDepartamentos = new SofisComboG<>();
        sedeSeleccionadaFiltro = null;
        departamentoSeleccionadoFiltro = null;
        cargarCombos();
        buscar();
    }

    /**
     * Limpia los filtros, además de crear un nuevo objeto de cuenta bancaria.
     */
    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgCuentasBancarias();
        titulo = new String("Cuentas Bancarias");
        entidadEnEdicion.setCbcOtroIngreso(Boolean.TRUE);
    }

    /**
     * Muestra los datos de auditoría.
     *
     * @param estId Long: Id del registro
     * @param estRev Long: Revisión del registro
     */
    public void mostrarDatos(Long estId, Long estRev) {
        try {
            tiposSeleccionadas = new ArrayList<>();
            limpiarCombos();
            if (estId != null && estId > 0) {
                if (estRev != null && estRev > 0) {
                    entidadEnEdicion = restClient.obtenerEnRevision(estId, estRev);
                } else {
                    entidadEnEdicion = restClient.obtenerPorId(estId);
                }
            }
            sedeSeleccionada = entidadEnEdicion.getCbcSedeFk();
            if (entidadEnEdicion.getCbcBancoFk() != null) {
                comboBancos.setSelectedT(entidadEnEdicion.getCbcBancoFk());
            }
            List<SgRelCuentaTipoCuenta> listTipos = obtenerTipoCuentas();
        
            if (listTipos != null && !listTipos.isEmpty()) {
                listTipos.forEach(rel -> {
                    tiposSeleccionadas.add(rel.getCbcTipoCuentaBacFk());
                });
            }
            historial = true;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Carga los datos de una cuenta para poder ser editados.
     *
     * @param var SgCuentasBancarias: Elemento del grid seleccionado de cuenta
     * bancaria.
     */
    public void actualizar(SgCuentasBancarias var) {
        historial = false;
        tiposSeleccionadas = new ArrayList<>();
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgCuentasBancarias) SerializationUtils.clone(var);
        sedeSeleccionada = entidadEnEdicion.getCbcSedeFk();
        
        if(entidadEnEdicion.getCbcOtroIngreso()==null){
            entidadEnEdicion.setCbcOtroIngreso(Boolean.TRUE);
        }
        
        if (entidadEnEdicion.getCbcBancoFk() != null) {
            comboBancos.setSelectedT(entidadEnEdicion.getCbcBancoFk());
        }
        
        List<SgRelCuentaTipoCuenta> listTipos = obtenerTipoCuentas();
        
        if (listTipos != null && !listTipos.isEmpty()) {
            listTipos.forEach(rel -> {
                tiposSeleccionadas.add(rel.getCbcTipoCuentaBacFk());
            });
        }
        titulo = "Edición cuentas bancarias";
    }

    /**
     * Crea o actualiza un registro de cuenta bancaria.
     */
    public void guardar() {
        try {
            List<SgRelCuentaTipoCuenta> relaciones = new ArrayList();

            entidadEnEdicion.setCbcSedeFk(sedeSeleccionada);
            entidadEnEdicion.setCbcBancoFk(comboBancos.getSelectedT());

            if (tiposSeleccionadas != null && !tiposSeleccionadas.isEmpty()) {
                tiposSeleccionadas.forEach(r -> {
                    SgRelCuentaTipoCuenta rel = new SgRelCuentaTipoCuenta();
                    rel.setCbcTipoCuentaBacFk(r);
                    relaciones.add(rel);
                });
            }
            entidadEnEdicion.setCbcCuentaTipoCuenta(relaciones);
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
     * Elimina un registro de cuenta bancaria.
     */
    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getCbcPk());
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
    public FiltroCodiguera getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCodiguera filtro) {
        this.filtro = filtro;
    }

    public SgCuentasBancarias getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgCuentasBancarias entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<RevHistorico> getHistorialCuentasBancarias() {
        return historialCuentasBancarias;
    }

    public void setHistorialCuentasBancarias(List<RevHistorico> historialCuentasBancarias) {
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

    public LazyCuentasBancariasDataModel getCuentasBancariasLazyModel() {
        return cuentasBancariasLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyCuentasBancariasDataModel cuentasBancariasLazyModel) {
        this.cuentasBancariasLazyModel = cuentasBancariasLazyModel;
    }

    public FiltroCuentasBancarias getFiltroBanco() {
        return filtroBanco;
    }

    public void setFiltroBanco(FiltroCuentasBancarias filtroBanco) {
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

    public SofisComboG<SgBancos> getComboBancos() {
        return comboBancos;
    }

    public void setComboBancos(SofisComboG<SgBancos> comboBancos) {
        this.comboBancos = comboBancos;
    }

    public List<SgTipoCuentaBancaria> getTiposSeleccionadas() {
        return tiposSeleccionadas;
    }

    public void setTiposSeleccionadas(List<SgTipoCuentaBancaria> tiposSeleccionadas) {
        this.tiposSeleccionadas = tiposSeleccionadas;
    }

    

    public boolean isHistorial() {
        return historial;
    }

    public void setHistorial(boolean historial) {
        this.historial = historial;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public SgDepartamento getDepartamentoSeleccionadoFiltro() {
        return departamentoSeleccionadoFiltro;
    }

    public void setDepartamentoSeleccionadoFiltro(SgDepartamento departamentoSeleccionadoFiltro) {
        this.departamentoSeleccionadoFiltro = departamentoSeleccionadoFiltro;
    }

    public SofisComboG<SgDepartamento> getComboDepartamentos() {
        return comboDepartamentos;
    }

    public void setComboDepartamentos(SofisComboG<SgDepartamento> comboDepartamentos) {
        this.comboDepartamentos = comboDepartamentos;
    }
    
    public SofisComboG<SgTipoCuentaBancaria> getComboTipoCuenta() {
        return comboTipoCuenta;
    }

    public void setComboTipoCuenta(SofisComboG<SgTipoCuentaBancaria> comboTipoCuenta) {
        this.comboTipoCuenta = comboTipoCuenta;
    }
    
    // </editor-fold>
    
}
