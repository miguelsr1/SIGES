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
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgSolicitudTraslado;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.SgServicioEducativo;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.catalogo.SgMotivoTraslado;
import sv.gob.mined.siges.web.dto.catalogo.SgMunicipio;
import sv.gob.mined.siges.web.enumerados.EnumEstadoSolicitudTraslado;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroNombreCompleto;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSolicitudTraslado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroMunicipio;
import sv.gob.mined.siges.web.lazymodels.LazySolicitudTrasladoDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.SolicitudTrasladoRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class SolicitudesTrasladoBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(SolicitudesTrasladoBean.class.getName());

    @Inject
    private SessionBean sessionBean;

    @Inject
    private SolicitudTrasladoRestClient restClient;

    @Inject
    private CatalogosRestClient catalogoRestClient;

    @Inject
    private SedeRestClient sedeRestClient;

    private FiltroSolicitudTraslado filtro = new FiltroSolicitudTraslado();
    private SgSolicitudTraslado entidadEnEdicion;
    private List<RevHistorico> historialSolicitudTraslado = new ArrayList();
    private List<SgSolicitudTraslado> listSolicitudTraslado = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazySolicitudTrasladoDataModel solicitudTrasladoLazyModel;
    private SofisComboG<EnumEstadoSolicitudTraslado> comboEstado;
    private SofisComboG<SgMotivoTraslado> comboMotivos;
    private SofisComboG<SgDepartamento> comboDepartamento;
    private SofisComboG<SgMunicipio> comboMunicipio;
    private FiltroNombreCompleto filtroNombreCompleto = new FiltroNombreCompleto();
    private SgServicioEducativo equivalente;
    private String txtFiltroAvanzado = Etiquetas.getValue("busquedaAvanzadaM");
    private Boolean panelAvanzado = Boolean.FALSE;
    private SgSede sedeSeleccionada;

    public SolicitudesTrasladoBean() {
    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            cargarCombos();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }
    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_SOLICITUD_TRASLADO)) {
            JSFUtils.redirectToIndex();
        }

    }

    public void buscar() {
        try {
            filtro.setDepartamento(comboDepartamento.getSelectedT() != null ? comboDepartamento.getSelectedT().getDepPk() : null);
            filtro.setMunicipio(comboMunicipio.getSelectedT() != null ? comboMunicipio.getSelectedT().getMunPk() : null);
            filtro.setCentroEducativo(sedeSeleccionada != null ? sedeSeleccionada.getSedPk() : null);
            filtro.setBuscarMismaSede(Boolean.TRUE);
            filtro.setSotEstado(comboEstado.getSelectedT() != null ? comboEstado.getSelectedT() : null);
            filtro.setSotMotivoTraslado(comboMotivos.getSelectedT() != null ? comboMotivos.getSelectedT().getMotPk() : null);
            filtro.setIncluirCampos(new String[]{"sotEstudiante.estPersona.perNie",
                "sotSedeSolicitante.sedCodigo",
                "sotSedeSolicitante.sedNombre",
                "sotSedeSolicitante.sedPk",
                "sotSedeSolicitante.sedTipo",
                "sotEstudiante.estPersona.perPrimerNombre",
                "sotEstudiante.estPersona.perSegundoNombre",
                "sotEstudiante.estPersona.perPrimerApellido",
                "sotEstudiante.estPersona.perSegundoApellido",
                "sotFechaSolicitud", "sotMotivoTraslado.motNombre",
                "sotEstado"});

            totalResultados = restClient.buscarTotal(filtro);
            solicitudTrasladoLazyModel = new LazySolicitudTrasladoDataModel(restClient, filtro, totalResultados, paginado);

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setAscending(new boolean[]{true});

            fc.setIncluirCampos(new String[]{"depNombre", "depVersion"});
            fc.setOrderBy(new String[]{"depNombre"});
            comboDepartamento = new SofisComboG(catalogoRestClient.buscarDepartamento(fc), "depNombre");
            comboDepartamento.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboMunicipio = new SofisComboG();
            comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            fc.setIncluirCampos(new String[]{"motNombre", "motVersion"});
            fc.setOrderBy(new String[]{"motNombre"});
            comboMotivos = new SofisComboG(catalogoRestClient.buscarMotivoTraslado(fc), "motNombre");
            comboMotivos.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            List<EnumEstadoSolicitudTraslado> estados = new ArrayList(Arrays.asList(EnumEstadoSolicitudTraslado.values()));
            estados.remove(EnumEstadoSolicitudTraslado.PENDIENTE_RESOLUCION);
            comboEstado = new SofisComboG(estados, "text");
            comboEstado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardar() {

    }

    private void limpiarCombos() {
        comboEstado.setSelected(-1);
        comboMotivos.setSelected(-1);
        comboDepartamento.setSelected(-1);
        comboMunicipio = new SofisComboG();
        comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        sedeSeleccionada = null;
    }

    public void limpiar() {
        filtro = new FiltroSolicitudTraslado();
        filtroNombreCompleto = new FiltroNombreCompleto();
        limpiarCombos();
        totalResultados = null;
        solicitudTrasladoLazyModel = null;
    }

    public void filtroNombreCompletoSeleccionar(FiltroNombreCompleto filtroNombre) {
        if (filtroNombre != null) {
            filtro.setPerPrimerNombre(filtroNombre.getPerPrimerNombre());
            filtro.setPerSegundoNombre(filtroNombre.getPerSegundoNombre());
            filtro.setPerTerceroNombre(filtroNombre.getPerTercerNombre());
            filtro.setPerPrimerApellido(filtroNombre.getPerPrimerApellido());
            filtro.setPerSegundoApellido(filtroNombre.getPerSegundoApellido());
            filtro.setPerTercerApellido(filtroNombre.getPerTercerApellido());
            if (!StringUtils.isBlank(filtroNombre.getNombreCompleto())) {
                filtro.setPerNombreCompleto(null);
            }
        }
        PrimeFaces.current().ajax().update("form:pnlSearch");
    }

    public void seleccionarDepartamento() {
        try {
            comboMunicipio = new SofisComboG();
            comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            if (comboDepartamento.getSelectedT() != null) {
                FiltroMunicipio filtroM = new FiltroMunicipio();
                filtroM.setOrderBy(new String[]{"munNombre"});
                filtroM.setIncluirCampos(new String[]{"munNombre", "munVersion"});
                filtroM.setAscending(new boolean[]{true});
                filtroM.setMunDepartamentoFk(comboDepartamento.getSelectedT().getDepPk());
                comboMunicipio = new SofisComboG(catalogoRestClient.buscarMunicipio(filtroM), "munNombre");
                comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public List<SgSede> completeSede(String query) {
        try {
            FiltroSedes fil = new FiltroSedes();
            fil.setSedCodigoONombre(query);
            fil.setSecurityOperation(ConstantesOperaciones.BUSCAR_SOLICITUD_TRASLADO);
            fil.setMaxResults(11L);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedNombre","sedCodigo", "sedVersion", "sedTipo"});
            return sedeRestClient.buscar(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void actualizar(SgSolicitudTraslado var) {
        entidadEnEdicion=var;
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getSotPk());
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

    public void historial(Long id) {
        try {
            historialSolicitudTraslado = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void verPanelAvanzado() {
        if (panelAvanzado) {
            panelAvanzado = false;
            txtFiltroAvanzado = Etiquetas.getValue("busquedaAvanzadaM");
        } else {
            txtFiltroAvanzado = Etiquetas.getValue("busquedaAvanzadaL");
            panelAvanzado = true;
        }
    }

    public Boolean getPanelAvanzado() {
        return panelAvanzado;
    }

    public void setPanelAvanzado(Boolean panelAvanzado) {
        this.panelAvanzado = panelAvanzado;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public SolicitudTrasladoRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(SolicitudTrasladoRestClient restClient) {
        this.restClient = restClient;
    }

    public SedeRestClient getSedeRestClient() {
        return sedeRestClient;
    }

    public void setSedeRestClient(SedeRestClient sedeRestClient) {
        this.sedeRestClient = sedeRestClient;
    }

    public CatalogosRestClient getCatalogoRestClient() {
        return catalogoRestClient;
    }

    public void setCatalogoRestClient(CatalogosRestClient catalogoRestClient) {
        this.catalogoRestClient = catalogoRestClient;
    }

    public FiltroSolicitudTraslado getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroSolicitudTraslado filtro) {
        this.filtro = filtro;
    }

    public SgSolicitudTraslado getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgSolicitudTraslado entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<RevHistorico> getHistorialSolicitudTraslado() {
        return historialSolicitudTraslado;
    }

    public void setHistorialSolicitudTraslado(List<RevHistorico> historialSolicitudTraslado) {
        this.historialSolicitudTraslado = historialSolicitudTraslado;
    }

    public List<SgSolicitudTraslado> getListSolicitudTraslado() {
        return listSolicitudTraslado;
    }

    public void setListSolicitudTraslado(List<SgSolicitudTraslado> listSolicitudTraslado) {
        this.listSolicitudTraslado = listSolicitudTraslado;
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

    public LazySolicitudTrasladoDataModel getSolicitudTrasladoLazyModel() {
        return solicitudTrasladoLazyModel;
    }

    public void setSolicitudTrasladoLazyModel(LazySolicitudTrasladoDataModel solicitudTrasladoLazyModel) {
        this.solicitudTrasladoLazyModel = solicitudTrasladoLazyModel;
    }

    public SgSede getSedeSeleccionada() {
        return sedeSeleccionada;
    }

    public void setSedeSeleccionada(SgSede sedeSeleccionada) {
        this.sedeSeleccionada = sedeSeleccionada;
    }

    public SofisComboG<EnumEstadoSolicitudTraslado> getComboEstado() {
        return comboEstado;
    }

    public void setComboEstado(SofisComboG<EnumEstadoSolicitudTraslado> comboEstado) {
        this.comboEstado = comboEstado;
    }

    public SofisComboG<SgMotivoTraslado> getComboMotivos() {
        return comboMotivos;
    }

    public void setComboMotivos(SofisComboG<SgMotivoTraslado> comboMotivos) {
        this.comboMotivos = comboMotivos;
    }

    public SofisComboG<SgDepartamento> getComboDepartamento() {
        return comboDepartamento;
    }

    public void setComboDepartamento(SofisComboG<SgDepartamento> comboDepartamento) {
        this.comboDepartamento = comboDepartamento;
    }

    public SofisComboG<SgMunicipio> getComboMunicipio() {
        return comboMunicipio;
    }

    public void setComboMunicipio(SofisComboG<SgMunicipio> comboMunicipio) {
        this.comboMunicipio = comboMunicipio;
    }

    public SgServicioEducativo getEquivalente() {
        return equivalente;
    }

    public void setEquivalente(SgServicioEducativo equivalente) {
        this.equivalente = equivalente;
    }

    public String getTxtFiltroAvanzado() {
        return txtFiltroAvanzado;
    }

    public void setTxtFiltroAvanzado(String txtFiltroAvanzado) {
        this.txtFiltroAvanzado = txtFiltroAvanzado;
    }

    public FiltroNombreCompleto getFiltroNombreCompleto() {
        return filtroNombreCompleto;
    }

    public void setFiltroNombreCompleto(FiltroNombreCompleto filtroNombreCompleto) {
        this.filtroNombreCompleto = filtroNombreCompleto;
    }

}
