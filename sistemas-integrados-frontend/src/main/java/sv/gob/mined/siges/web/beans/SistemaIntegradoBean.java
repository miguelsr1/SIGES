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
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import org.primefaces.event.TabChangeEvent;
import sv.gob.mined.siges.client.excepciones.HttpClientException;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgSistemaIntegrado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.SistemaIntegradoRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.SgDocumentoSistema;
import sv.gob.mined.siges.web.dto.SgSistemaSede;
import sv.gob.mined.siges.web.dto.centros_educativos.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgDireccion;
import sv.gob.mined.siges.web.dto.catalogo.SgSiPromotor;
import sv.gob.mined.siges.web.enumerados.EnumCategoriaDocumento;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDocumentoSistema;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSistemaIntegrado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSistemaSede;
import sv.gob.mined.siges.web.lazymodels.LazySistemaSedeDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.restclient.SistemaSedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class SistemaIntegradoBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(SistemaIntegradoBean.class.getName());

    @Inject
    private SistemaIntegradoRestClient restClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private SedeRestClient sedeClient;

    @Inject
    private SistemaSedeRestClient sistemaSedeClient;

    @Inject
    private DocumentosSistemaBean documentosSistemaBean;

    @Inject
    private CatalogosRestClient restCatalogo;

    @Inject
    @Param(name = "id")
    private Long sistemaIntegradoId;

    @Inject
    @Param(name = "edit")
    private Boolean editable;

    @Inject
    private ApplicationBean applicationBean;

    @Inject
    private ActividadesSistemaBean actividadesSistemaBean;

    private Boolean soloLectura = Boolean.FALSE;
    private FiltroCodiguera filtro = new FiltroCodiguera();
    private SgSistemaIntegrado entidadEnEdicion = new SgSistemaIntegrado();
    private Integer paginado = 10;
    private Long totalResultados = 0L;
    private SgSede sedeSeleccionada;
    private SgSede sedeEnEdicion;
    private String tabActiveId = "general";
    private List<SgDocumentoSistema> listaGobernanza;
    private List<SgDocumentoSistema> listaOAE;
    private List<SgDocumentoSistema> listaOtros;
    private FiltroDocumentoSistema fGobernanza = new FiltroDocumentoSistema();
    private FiltroDocumentoSistema fOAE = new FiltroDocumentoSistema();
    private FiltroDocumentoSistema fOtros = new FiltroDocumentoSistema();
    private String tabSedesSelected = null;
    private String urlMapa;
    private LazySistemaSedeDataModel lazySedes;
    private FiltroSistemaSede fSS = new FiltroSistemaSede();
    private SofisComboG<SgSiPromotor> comboPromotor;

    public SistemaIntegradoBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            if (sistemaIntegradoId != null && sistemaIntegradoId > 0) {
                entidadEnEdicion = restClient.obtenerPorId(sistemaIntegradoId);
                comboPromotor.setSelectedT(entidadEnEdicion.getSinPromotor());
                soloLectura = editable != null ? !editable : soloLectura;
                buscarSedes();
            } else {
                agregar();
            }
            validarAcceso();
        } catch (HttpClientException ce) {
            JSFUtils.redirectNotFound();
        } catch (Exception ex) {
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public void buscarSedes() {
        try {
            fSS.setSistema(entidadEnEdicion.getSinPk());

            totalResultados = sistemaSedeClient.buscarTotal(fSS);
            lazySedes = new LazySistemaSedeDataModel(sistemaSedeClient, fSS, totalResultados, paginado);

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void validarAcceso() {

        if (soloLectura) {
            if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.VER_SISTEMA_INTEGRADO)) {
                JSFUtils.redirectToIndex();
            }
        } else {
            if (entidadEnEdicion.getSinPk() == null) {
                if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.CREAR_SISTEMA_INTEGRADO)) {
                    JSFUtils.redirectToIndex();
                }
            } else {
                if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.ACTUALIZAR_SISTEMA_INTEGRADO)) {
                    JSFUtils.redirectToIndex();
                }
            }
        }
    }

    public void redirectToIndex() throws Exception {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.redirect(externalContext.getRequestContextPath());
    }

    public void changeTab(TabChangeEvent event) {
        this.tabActiveId = event.getTab().getId();
        switch (tabActiveId) {
            case "gobernanza":
                documentosSistemaBean.setCategoriaDocumento(EnumCategoriaDocumento.GOBERNANZA);
                documentosSistemaBean.listasPreCargadas(listaGobernanza);
                documentosSistemaBean.setFiltro(fGobernanza);
                break;
            case "oae":
                documentosSistemaBean.setCategoriaDocumento(EnumCategoriaDocumento.OAE);
                documentosSistemaBean.listasPreCargadas(listaOAE);
                documentosSistemaBean.setFiltro(fOAE);
                break;
            case "otros":
                documentosSistemaBean.setCategoriaDocumento(EnumCategoriaDocumento.OTROS);
                documentosSistemaBean.listasPreCargadas(listaOtros);
                documentosSistemaBean.setFiltro(fOtros);
                break;
            case "actividades":
                // reseteo bean en caso de re ingresar al bean con nuevas actividades
                FacesContext.getCurrentInstance().getViewRoot().getViewMap().remove("actividadesSistemaBean"); 
                break;
        }
        PrimeFaces.current().executeScript("PF('tabsBlocker').hide()");
    }

    public void actualizarLista(List<SgDocumentoSistema> lista, FiltroDocumentoSistema fil) {
        switch (tabActiveId) {
            case "gobernanza":
                listaGobernanza = lista;
                fGobernanza = fil;
                break;
            case "oae":
                listaOAE = lista;
                fOAE = fil;
                break;
            case "otros":
                listaOtros = lista;
                fOtros = fil;
                break;
        }
    }

    public void cargarCombos() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});

            fc.setOrderBy(new String[]{"proNombre"});
            fc.setIncluirCampos(new String[]{"proNombre", "proVersion"});
            List<SgSiPromotor> listaPromotores = restCatalogo.buscarPromotor(fc);
            comboPromotor = new SofisComboG<>(listaPromotores, "proNombre");
            comboPromotor.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {

    }

    public void limpiar() {
        filtro = new FiltroCodiguera();
    }

    public void agregar() {
        limpiarCombos();
        JSFUtils.limpiarMensajesError();
        entidadEnEdicion = new SgSistemaIntegrado();
    }

    public void guardar() {
        try {
            entidadEnEdicion.setSinPromotor(comboPromotor != null ? comboPromotor.getSelectedT() : null);
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void nuevaSede() {
        sedeSeleccionada = null;
    }

    public void verDetalle(Long sedPk) {
        try {
            sedeEnEdicion = sistemaSedeClient.obtenerPorId(sedPk);
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarSede(SgSede sede) {
        try {
            sedeEnEdicion = sede;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void agregarSede() {
        try {
            //Validar que la sede no este asociada a otra sistema integrado
            if (sedeSeleccionada != null) {
                FiltroSistemaIntegrado fsis = new FiltroSistemaIntegrado();
                fsis.setSede(sedeSeleccionada.getSedPk());
                fsis.setHabilitado(Boolean.TRUE);
                fsis.setIncluirCampos(new String[]{"sinVersion"});
                Long preRegistro = restClient.buscarTotal(fsis);

                if (preRegistro > 0) {
                    JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_SEDE_EN_SISTEMA_INTEGRADO), "");
                } else {
                    SgSistemaSede sise = new SgSistemaSede();
                    sise.setSedPk(sedeSeleccionada);
                    sise.setSinPk(entidadEnEdicion);

                    sistemaSedeClient.guardar(sise);

                    buscarSedes();
                    sedeSeleccionada = null;

                    //Actualizar el mapa
                    tabSedesSelected = null;
                    actualizarMapa();
                    JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
                }
                PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            } else {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_SEDE_VACIA), "");
            }

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminarSede() {
        try {
            sistemaSedeClient.eliminar(entidadEnEdicion.getSinPk(), sedeEnEdicion.getSedPk());

            sedeEnEdicion = null;
            buscarSedes();

            //Actualizar el mapa
            tabSedesSelected = null;
            actualizarMapa();
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void actualizarMapa() {
        urlMapa = null;
        if (tabSedesSelected != null && tabSedesSelected.equals("0")) {
            mostrarMapa();
            PrimeFaces.current().ajax().update("tabMapSedes");
        }
    }

    public void eliminar() {
        try {
            restClient.guardar(entidadEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public String getNombrePagina() {
        if (entidadEnEdicion.getSinPk() == null) {
            return Etiquetas.getValue("agregarSistemaIntegrado");
        } else if (soloLectura) {
            return Etiquetas.getValue("verSistemaIntegrado");
        } else {
            return Etiquetas.getValue("editarSistemaIntegrado");
        }

    }

    public List<SgSede> completeSede(String query) {
        try {
            FiltroSedes fil = new FiltroSedes();
            fil.setSedCodigoONombre(query);
            fil.setSedHabilitado(Boolean.TRUE);
            fil.setMaxResults(11L);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedCodigo", "sedNombre", "sedTipo", "sedVersion"});
            fil.setIncluirAdscritas(Boolean.FALSE);
            return sedeClient.buscar(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            return new ArrayList<>();
        }
    }

    public void tabSelected() {
        try {
            PrimeFaces.current().executeScript("PF('tabsSedesBlocker').hide()");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public String getUrlMapa() {
        if (tabSedesSelected != null && tabSedesSelected.equals("0") && urlMapa == null) {
            mostrarMapa();
        }
        return urlMapa;
    }

    public void mostrarMapa() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();

        StringBuilder url = new StringBuilder();
        url.append(scheme).append("://").append(serverName);
        if (serverPort != 80 && serverPort != 443) {
            url.append(":").append(serverPort);
        }
        url.append(contextPath).append("/mapa/mapaMultiple.html?");

        urlMapa = url.toString();
        String latitud = "";
        String longitud = "";
        String codigos = "";
        String nombres = "";
        String tipos = "";
        String departamentos = "";
        String municipios = "";
        String ids = "";
        List<SgSede> sedes = lazySedes.getWrappedData();

        for (SgSede sed : sedes) {
            SgDireccion dir = sed.getSedDireccion();
            if (dir != null) {
                if (dir.getDirLatitud() != null && dir.getDirLongitud() != null) {
                    latitud += dir.getDirLatitud() + "|";
                    longitud += dir.getDirLongitud() + "|";
                    ids += sed.getSedPk() + "|";
                    codigos += sed.getSedCodigo() + "|";
                    nombres += sed.getSedNombre() + "|";
                    tipos += sed.getSedTipo().getText() + "|";
                    departamentos += sed.getSedDireccion().getDirDepartamento().getDepNombre() + "|";
                    municipios += sed.getSedDireccion().getDirMunicipio().getMunNombre() + "|";
                }
            }
        }
        urlMapa = urlMapa + "lat=" + latitud + "&lon=" + longitud + "&cods=" + codigos + "&noms=" + nombres
                + "&tipos=" + tipos + "&dptos=" + departamentos + "&muns=" + municipios + "&ids=" + ids + "&burl=" + this.applicationBean.getPortalUrl();
    }

    public FiltroCodiguera getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCodiguera filtro) {
        this.filtro = filtro;
    }

    public SgSistemaIntegrado getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgSistemaIntegrado entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
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

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public Long getSistemaIntegradoId() {
        return sistemaIntegradoId;
    }

    public void setSistemaIntegradoId(Long sistemaIntegradoId) {
        this.sistemaIntegradoId = sistemaIntegradoId;
    }

    public SgSede getSedeSeleccionada() {
        return sedeSeleccionada;
    }

    public void setSedeSeleccionada(SgSede sedeSeleccionada) {
        this.sedeSeleccionada = sedeSeleccionada;
    }

    public String getTabActiveId() {
        return tabActiveId;
    }

    public void setTabActiveId(String tabActiveId) {
        this.tabActiveId = tabActiveId;
    }

    public String getTabSedesSelected() {
        return tabSedesSelected;
    }

    public void setTabSedesSelected(String tabSedesSelected) {
        this.tabSedesSelected = tabSedesSelected;
    }

    public LazySistemaSedeDataModel getLazySedes() {
        return lazySedes;
    }

    public void setLazySedes(LazySistemaSedeDataModel lazySedes) {
        this.lazySedes = lazySedes;
    }

    public SgSede getSedeEnEdicion() {
        return sedeEnEdicion;
    }

    public void setSedeEnEdicion(SgSede sedeEnEdicion) {
        this.sedeEnEdicion = sedeEnEdicion;
    }

    public SofisComboG<SgSiPromotor> getComboPromotor() {
        return comboPromotor;
    }

    public void setComboPromotor(SofisComboG<SgSiPromotor> comboPromotor) {
        this.comboPromotor = comboPromotor;
    }

}
