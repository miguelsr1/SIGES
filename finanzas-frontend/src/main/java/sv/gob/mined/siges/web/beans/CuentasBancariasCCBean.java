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
import sv.gob.mined.siges.web.dto.SgCajaChica;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCajaChica;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.lazymodels.LazyCajasChicasDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.BancosRestClient;
import sv.gob.mined.siges.web.restclient.CajaChicaRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 * Bean para la gestión de las cuentas bancarias de caja chica.
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class CuentasBancariasCCBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(CuentasBancariasBean.class.getName());

    @Inject
    private CajaChicaRestClient restClient;

    @Inject
    private SedeRestClient sedeRestClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private BancosRestClient bancosClient;

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

    /**
     * Constructor de la clase.
     */
    public CuentasBancariasCCBean() {
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
            if (sedeSeleccionadaFiltro != null) {
                filtroBanco.setBccSedeFk(sedeSeleccionadaFiltro.getSedPk());
            }
            filtroBanco.setOrderBy(new String[]{"bccNumeroCuenta"});
            filtroBanco.setAscending(new boolean[]{false});
            filtroBanco.setIncluirCampos(new String[]{
                "bccPk",
                "bccNumeroCuenta",
                "bccTitular",
                "bccComentario",
                "bccSedeFk.sedPk",
                "bccSedeFk.sedCodigo",
                "bccSedeFk.sedNombre",
                "bccSedeFk.sedTipo",
                "bccSedeFk.sedVersion",
                "bccUltModFecha",
                "bccUltModUsuario",
                "bccHabilitado",
                "bccVersion"

            });
            totalResultados = restClient.buscarTotal(filtroBanco);
            cuentasBancariasLazyModel = new LazyCajasChicasDataModel(restClient, filtroBanco, totalResultados, paginado);

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Carga los combos necesarios para la creación y edición de registros,
     * también combos de filtros.
     */
    public void cargarCombos() {

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
     * Limpia los combos usados para filtrar y crear.
     */
    private void limpiarCombos() {

    }

    /**
     * Limpia los filtros.
     */
    public void limpiar() {
        filtro = new FiltroCodiguera();
        filtroBanco = new FiltroCajaChica();
        sedeSeleccionadaFiltro = null;
        buscar();

    }

    /**
     * Limpia los filtros, además de crear un nuevo objeto de caja chica.
     */
    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgCajaChica();
        setTitulo();
    }

    /**
     * Carga los datos de una cuenta para poder ser editados.
     *
     * @param var SgCajaChica: Elemento del grid seleccionado de caja chica.
     */
    public void actualizar(SgCajaChica var) {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgCajaChica) SerializationUtils.clone(var);
        sedeSeleccionada = entidadEnEdicion.getBccSedeFk();
        setTitulo();
    }

    /**
     * Crea o actualiza un registro de caja chica.
     */
    public void guardar() {
        try {
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

    // </editor-fold>
}
