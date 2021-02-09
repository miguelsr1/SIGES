/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
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
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPlaza;
import sv.gob.mined.siges.web.lazymodels.LazyPlazaDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.PlazaRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.SgPlaza;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.catalogo.SgMunicipio;
import sv.gob.mined.siges.web.enumerados.EnumEstadoPlaza;
import sv.gob.mined.siges.web.enumerados.EnumSituacionPlaza;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroMunicipio;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class PlazasBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(PlazasBean.class.getName());

    @Inject
    private PlazaRestClient restClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private CatalogosRestClient restCatalogo;

    @Inject
    private SedeRestClient restSede;

    private SgPlaza entidadEnEdicion = new SgPlaza();
    private FiltroPlaza filtro = new FiltroPlaza();
    private LazyPlazaDataModel plazaLazyModel;
    private List<RevHistorico> historialPlaza = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private List<EnumEstadoPlaza> listEstadoPlaza;
    private List<EnumSituacionPlaza> listSituacionPlaza;
    private List<SelectItem> comboEstado;
    private List<SelectItem> comboSituacion;
    private SofisComboG<SgDepartamento> comboDepartamentos;
    private SofisComboG<SgMunicipio> comboMunicipio;
    private SgSede sedeSeleccionadaFiltro;
    private SgSede sedeSeleccionada;
    private Long solicitudPlazaHistorial;
    private Boolean verHistorico = Boolean.FALSE;

    public PlazasBean() {
    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            cargarCombos();
            buscar();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_PLAZA)) {
            JSFUtils.redirectToIndex();
        }
    }

    public String buscar() {
        try {
            filtro.setIncluirCampos(new String[]{"plaSedeFk.sedCodigo",
                "plaSedeFk.sedNombre",
                "plaSedeFk.sedTelefono",
                "plaSedeFk.sedDireccion.dirDepartamento.depNombre",
                "plaSedeFk.sedTipo",
                "plaNombre",
                "plaIdPuesto",
                "plaPartida",
                "plaSubpartida",
                "plaEstado",
                "plaSituacion",
                "plaAnioPartida"});
            filtro.setDepartamento(comboDepartamentos.getSelectedT() != null ? comboDepartamentos.getSelectedT().getDepPk() : null);
            filtro.setMunicipio(comboMunicipio.getSelectedT() != null ? comboMunicipio.getSelectedT().getMunPk() : null);
            filtro.setSedeFk(sedeSeleccionadaFiltro != null ? sedeSeleccionadaFiltro.getSedPk() : null);
            totalResultados = restClient.buscarTotal(filtro);
            plazaLazyModel = new LazyPlazaDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public List<SgSede> completeSede(String query) {
        try {
            FiltroSedes fil = new FiltroSedes();
//            if (securityOperation != null) {
//                fil.setSecurityOperation(securityOperation);
//            }

            fil.setSedCodigoONombre(query);
            fil.setSedHabilitado(Boolean.TRUE);
            fil.setMaxResults(11L);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedCodigo", "sedNombre", "sedTipo", "sedVersion", "sedTipoCalendario.tcePk"});
            return restSede.buscar(fil);
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ce), ce);
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void cargarCombos() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});

            fc.setOrderBy(new String[]{"depNombre"});
            fc.setIncluirCampos(new String[]{"depNombre", "depVersion"});
            List<SgDepartamento> listaDepartamentos = restCatalogo.buscarDepartamento(fc);
            comboDepartamentos = new SofisComboG<>(listaDepartamentos, "depNombre");
            comboDepartamentos.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboMunicipio = new SofisComboG();
            comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            listEstadoPlaza = new ArrayList(Arrays.asList(EnumEstadoPlaza.values()));
            listSituacionPlaza = new ArrayList(Arrays.asList(EnumSituacionPlaza.values()));

            comboEstado = new ArrayList(Arrays.asList(EnumEstadoPlaza.values()));
            comboSituacion = new ArrayList(Arrays.asList(EnumSituacionPlaza.values()));
        } catch (Exception ex) {
            Logger.getLogger(PlazasBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void departamentoElegido() {
        try {
            if (comboDepartamentos.getSelectedT() != null) {
                FiltroMunicipio fm = new FiltroMunicipio();
                fm.setOrderBy(new String[]{"munNombre"});
                fm.setAscending(new boolean[]{true});
                fm.setIncluirCampos(new String[]{"munNombre", "munVersion"});
                fm.setMunDepartamentoFk(comboDepartamentos.getSelectedT().getDepPk());

                List<SgMunicipio> listMunicipio = restCatalogo.buscarMunicipio(fm);
                comboMunicipio = new SofisComboG(listMunicipio, "munNombre");
                comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                comboMunicipio.ordenar();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {
        cargarCombos();
    }

    public String limpiar() {
        sedeSeleccionadaFiltro = null;
        filtro = new FiltroPlaza();
        comboDepartamentos.setSelected(-1);
        comboMunicipio = new SofisComboG();
        comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        limpiarCombos();
        buscar();
        return null;
    }

    public String historial(Long id) {
        try {

            historialPlaza = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void buscarRevEntity(RevHistorico revHist) {
        try {
            verHistorico = Boolean.TRUE;
            entidadEnEdicion = restClient.obtenerEnRevision(revHist.getObjPk(), revHist.getRevEntity().getRev());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public void agregar() {
         verHistorico = Boolean.FALSE;
        sedeSeleccionada = null;
        limpiarCombos();
        JSFUtils.limpiarMensajesError();
        entidadEnEdicion = new SgPlaza();
    }
    
    public void seleccionarSede(){
        entidadEnEdicion.setPlaSedeFk(sedeSeleccionada);
    }

    public void guardar() {
        try {
           // entidadEnEdicion.setPlaSedeFk(sedeSeleccionada);
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscar();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void actualizar(SgPlaza var) {
        try {
            verHistorico = Boolean.FALSE;
          //  limpiarCombos();
            entidadEnEdicion = restClient.obtenerPorId(var.getPlaPk());
            sedeSeleccionada = var.getPlaSedeFk();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getPlaPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            buscar();
            entidadEnEdicion = new SgPlaza();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    //<editor-fold defaultstate="collapsed" desc="GET & SET">
    public List<SelectItem> getComboEstado() {
        return comboEstado;
    }

    public void setComboEstado(List<SelectItem> comboEstado) {
        this.comboEstado = comboEstado;
    }

    public List<SelectItem> getComboSituacion() {
        return comboSituacion;
    }

    public void setComboSituacion(List<SelectItem> comboSituacion) {
        this.comboSituacion = comboSituacion;
    }

    public List<EnumEstadoPlaza> getListEstadoPlaza() {
        return listEstadoPlaza;
    }

    public void setListEstadoPlaza(List<EnumEstadoPlaza> listEstadoPlaza) {
        this.listEstadoPlaza = listEstadoPlaza;
    }

    public List<EnumSituacionPlaza> getListSituacionPlaza() {
        return listSituacionPlaza;
    }

    public void setListSituacionPlaza(List<EnumSituacionPlaza> listSituacionPlaza) {
        this.listSituacionPlaza = listSituacionPlaza;
    }

    public SgPlaza getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgPlaza entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public FiltroPlaza getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroPlaza filtro) {
        this.filtro = filtro;
    }

    public LazyPlazaDataModel getPlazaLazyModel() {
        return plazaLazyModel;
    }

    public void setPlazaLazyModel(LazyPlazaDataModel plazaLazyModel) {
        this.plazaLazyModel = plazaLazyModel;
    }

    public List<RevHistorico> getHistorialPlaza() {
        return historialPlaza;
    }

    public void setHistorialPlaza(List<RevHistorico> historialPlaza) {
        this.historialPlaza = historialPlaza;
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

    //</editor-fold>
    public SofisComboG<SgDepartamento> getComboDepartamentos() {
        return comboDepartamentos;
    }

    public void setComboDepartamentos(SofisComboG<SgDepartamento> comboDepartamentos) {
        this.comboDepartamentos = comboDepartamentos;
    }

    public SofisComboG<SgMunicipio> getComboMunicipio() {
        return comboMunicipio;
    }

    public void setComboMunicipio(SofisComboG<SgMunicipio> comboMunicipio) {
        this.comboMunicipio = comboMunicipio;
    }

    public SgSede getSedeSeleccionadaFiltro() {
        return sedeSeleccionadaFiltro;
    }

    public void setSedeSeleccionadaFiltro(SgSede sedeSeleccionadaFiltro) {
        this.sedeSeleccionadaFiltro = sedeSeleccionadaFiltro;
    }

    public SgSede getSedeSeleccionada() {
        return sedeSeleccionada;
    }

    public void setSedeSeleccionada(SgSede sedeSeleccionada) {
        this.sedeSeleccionada = sedeSeleccionada;
    }

    public Long getSolicitudPlazaHistorial() {
        return solicitudPlazaHistorial;
    }

    public void setSolicitudPlazaHistorial(Long solicitudPlazaHistorial) {
        this.solicitudPlazaHistorial = solicitudPlazaHistorial;
    }

    public Boolean getVerHistorico() {
        return verHistorico;
    }

    public void setVerHistorico(Boolean verHistorico) {
        this.verHistorico = verHistorico;
    }

}
