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
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgBancos;
import sv.gob.mined.siges.web.dto.SgCuentasBancariasDD;
import sv.gob.mined.siges.web.dto.SgDireccionDepartamental;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCuentasBancariasDD;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDireccionDepartamental;
import sv.gob.mined.siges.web.lazymodels.LazyCuentasBancariasDDDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.BancosRestClient;
import sv.gob.mined.siges.web.restclient.CuentasBancariasDDRestClient;
import sv.gob.mined.siges.web.restclient.DireccionDepartamentalRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 * Bean para la gestión de las cuentas bancarias de direcciones departamentales
 * Departamentales.
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class CuentasBancariasDDBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(CuentasBancariasDDBean.class.getName());

    @Inject
    private CuentasBancariasDDRestClient restClient;

    @Inject
    private BancosRestClient bancosClient;

    @Inject
    private DireccionDepartamentalRestClient dirClient;

    private FiltroCodiguera filtro = new FiltroCodiguera();
    private FiltroCuentasBancariasDD filtroDD = new FiltroCuentasBancariasDD();
    private FiltroDireccionDepartamental filtroD = new FiltroDireccionDepartamental();
    private SgCuentasBancariasDD entidadEnEdicion = new SgCuentasBancariasDD();
    private List<RevHistorico> historialCuentasBancariasDD = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyCuentasBancariasDDDataModel cuentasBancariasDDLazyModel;
    private SofisComboG<SgBancos> comboBancos = new SofisComboG<>();
    private SofisComboG<SgDireccionDepartamental> comboDireccionDep = new SofisComboG<>();
    private SofisComboG<SgDireccionDepartamental> filtroDireccionDep = new SofisComboG<>();
    private boolean historial = false;
    private String titulo = "";

    /**
     * Constructor de la clase.
     */
    public CuentasBancariasDDBean() {
    }

    /**
     * Método para incializar filtros, combos y búsqueda de cuentas bancarias de
     * direcciones departamentales.
     */
    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            cargarCombosForm();
            buscar();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Busca los registros de cuentas bancarias de direcciones departamentales
     * según los filtros utilizados.
     */
    public void buscar() {
        try {
            filtroDD.setFirst(new Long(0));
            if (filtroDireccionDep.getSelectedT() != null) {
                filtroDD.setCbdDirDepFk(filtroDireccionDep.getSelectedT().getDedPk());
            }
            totalResultados = restClient.buscarTotal(filtroDD);
            cuentasBancariasDDLazyModel = new LazyCuentasBancariasDDDataModel(restClient, filtroDD, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Carga los combos de filtros.
     */
    public void cargarCombos() {
        try {
            FiltroDireccionDepartamental filtroD = new FiltroDireccionDepartamental();
            filtroD.setIncluirCampos(new String[]{"dedVersion", "dedNombre"});
            filtroD.setOrderBy(new String[]{"dedNombre"});
            filtroD.setAscending(new boolean[]{true});
            filtroD.setDedHabilitado(Boolean.TRUE);
            filtroDireccionDep = new SofisComboG<>(dirClient.buscar(filtroD), "dedNombre");
            filtroDireccionDep.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch (HttpServerException ex) {
            Logger.getLogger(CuentasBancariasDDBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (HttpClientException ex) {
            Logger.getLogger(CuentasBancariasDDBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (HttpServerUnavailableException ex) {
            Logger.getLogger(CuentasBancariasDDBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BusinessException ex) {
            Logger.getLogger(CuentasBancariasDDBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Carga los combos necesarios para la creación y edición de registros.
     */
    public void cargarCombosForm() {
        try {
            filtro = new FiltroCodiguera();
            filtro.setIncluirCampos(new String[]{"bncPk", "bncCodigo", "bncNombre", "bncVersion"});
            filtro.setOrderBy(new String[]{"bncCodigo"});
            filtro.setAscending(new boolean[]{true});
            filtro.setHabilitado(Boolean.TRUE);
            comboBancos = new SofisComboG<>(bancosClient.buscar(filtro), "codigoNombre");
            comboBancos.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            FiltroDireccionDepartamental filtroD = new FiltroDireccionDepartamental();
            filtroD.setIncluirCampos(new String[]{"dedVersion", "dedNombre"});
            filtroD.setOrderBy(new String[]{"dedNombre"});
            filtroD.setAscending(new boolean[]{true});
            filtroD.setDedHabilitado(Boolean.TRUE);
            comboDireccionDep = new SofisComboG<>(dirClient.buscar(filtroD), "dedNombre");
            comboDireccionDep.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch (HttpServerException ex) {
            Logger.getLogger(CuentasBancariasDDBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (HttpClientException ex) {
            Logger.getLogger(CuentasBancariasDDBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (HttpServerUnavailableException ex) {
            Logger.getLogger(CuentasBancariasDDBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BusinessException ex) {
            Logger.getLogger(CuentasBancariasDDBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Limpia los combos usados para filtrar y crear.
     */
    private void limpiarCombosFiltros() {
        filtroDireccionDep = new SofisComboG<>();
        cargarCombos();
    }

    /**
     * Limpia los combos usados para filtrar y crear.
     */
    private void limpiarCombos() {
        comboBancos = new SofisComboG<>();
        comboDireccionDep = new SofisComboG<>();
        cargarCombosForm();
    }

    /**
     * Limpia los filtros.
     */
    public void limpiar() {
        filtro = new FiltroCodiguera();
        filtroDD = new FiltroCuentasBancariasDD();
        filtroD = new FiltroDireccionDepartamental();
        limpiarCombosFiltros();
        buscar();
    }

    /**
     * Limpia los filtros, además de crear un nuevo objeto de cuenta bancaria.
     */
    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgCuentasBancariasDD();
        titulo = new String("Cuentas bancarias dirección departamental");
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
            if (entidadEnEdicion.getCbdBancoFk() != null) {
                comboBancos.setSelectedT(entidadEnEdicion.getCbdBancoFk());
            }
            if (entidadEnEdicion.getCbdDirDepFk() != null) {
                comboDireccionDep.setSelectedT(entidadEnEdicion.getCbdDirDepFk());
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
     * @param var SgCuentasBancariasDD: Elemento del grid seleccionado de
     * cuentas bancarias de direcciones departamentales.
     */
    public void actualizar(SgCuentasBancariasDD var) {
        historial = false;
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgCuentasBancariasDD) SerializationUtils.clone(var);
        if (entidadEnEdicion.getCbdBancoFk() != null) {
            comboBancos.setSelectedT(entidadEnEdicion.getCbdBancoFk());
        }
        if (entidadEnEdicion.getCbdDirDepFk() != null) {
            comboDireccionDep.setSelectedT(entidadEnEdicion.getCbdDirDepFk());
        }
        titulo = "Edición cuentas bancarias dirección departamental";
    }

    /**
     * Crea o actualiza un registro de cuentas bancarias de direcciones
     * departamentales.
     */
    public void guardar() {
        try {
            entidadEnEdicion.setCbdBancoFk(comboBancos.getSelectedT());
            entidadEnEdicion.setCbdDirDepFk(comboDireccionDep.getSelectedT());
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
     * Elimina un registro de cuentas bancarias de direcciones departamentales.
     */
    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getCbdPk());
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
     * @param id Long: id del registro del cual se quiere obtener el historial
     */
    public void historial(Long id) {
        try {
            historialCuentasBancariasDD = restClient.obtenerHistorialPorId(id);
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

    public SgCuentasBancariasDD getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgCuentasBancariasDD entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<RevHistorico> getHistorialCuentasBancariasDD() {
        return historialCuentasBancariasDD;
    }

    public void setHistorialCuentasBancariasDD(List<RevHistorico> historialCuentasBancariasDD) {
        this.historialCuentasBancariasDD = historialCuentasBancariasDD;
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

    public LazyCuentasBancariasDDDataModel getCuentasBancariasDDLazyModel() {
        return cuentasBancariasDDLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyCuentasBancariasDDDataModel cuentasBancariasDDLazyModel) {
        this.cuentasBancariasDDLazyModel = cuentasBancariasDDLazyModel;
    }

    public SofisComboG<SgBancos> getComboBancos() {
        return comboBancos;
    }

    public void setComboBancos(SofisComboG<SgBancos> comboBancos) {
        this.comboBancos = comboBancos;
    }

    public FiltroCuentasBancariasDD getFiltroDD() {
        return filtroDD;
    }

    public void setFiltroDD(FiltroCuentasBancariasDD filtroDD) {
        this.filtroDD = filtroDD;
    }

    public FiltroDireccionDepartamental getFiltroD() {
        return filtroD;
    }

    public void setFiltroD(FiltroDireccionDepartamental filtroD) {
        this.filtroD = filtroD;
    }

    public SofisComboG<SgDireccionDepartamental> getComboDireccionDep() {
        return comboDireccionDep;
    }

    public void setComboDireccionDep(SofisComboG<SgDireccionDepartamental> comboDireccionDep) {
        this.comboDireccionDep = comboDireccionDep;
    }

    public SofisComboG<SgDireccionDepartamental> getFiltroDireccionDep() {
        return filtroDireccionDep;
    }

    public void setFiltroDireccionDep(SofisComboG<SgDireccionDepartamental> filtroDireccionDep) {
        this.filtroDireccionDep = filtroDireccionDep;
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

    // </editor-fold>
}
