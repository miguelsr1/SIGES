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
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgArchivo;
import sv.gob.mined.siges.web.dto.SgOrganismoAdministracionEscolar;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.SgSolDeshabilitarPerJur;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.catalogo.SgMunicipio;
import sv.gob.mined.siges.web.enumerados.EnumEstadoSolicitudOAE;
import sv.gob.mined.siges.web.enumerados.TipoSede;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSolicitudesOAE;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroMunicipio;
import sv.gob.mined.siges.web.lazymodels.LazySolicitudesOAEDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.DeshabilitarPersonaJuridicaRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class SolicitudOAEBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(SolicitudOAEBean.class.getName());

    @Inject
    private DeshabilitarPersonaJuridicaRestClient restClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private SedeRestClient restSede;

    @Inject
    private CatalogosRestClient catalogoClient;

    @Inject
    private HandleArchivoBean handleArchivoBean;

    private FiltroSolicitudesOAE filtro = new FiltroSolicitudesOAE();
    private SgSolDeshabilitarPerJur entidadEnEdicion = new SgSolDeshabilitarPerJur();
    private List<RevHistorico> historialSolicitudes = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazySolicitudesOAEDataModel solLazyModel;

    private SgSede sedeSeleccionada;
    private SofisComboG<TipoSede> comboTiposSede = new SofisComboG<>();
    private SofisComboG<EnumEstadoSolicitudOAE> comboEstado = new SofisComboG<>();
    private SofisComboG<SgDepartamento> comboDepartamentos = new SofisComboG<>();
    private SofisComboG<SgMunicipio> comboMunicipios = new SofisComboG<>();
    private String tamanioImportArchivo = "1048576"; //1MB por defecto
    private String tipoImportArchivo = "/(\\.|\\/)(pdf)$/";
    private SelectItem[] acciones;
    private Boolean resultado = true;

    public SolicitudOAEBean() {
    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            cargarCombos();
            agregar();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.BUSCAR_SOLICITUDES_OAE)) {
            JSFUtils.redirectToIndex();
        }
    }

    public void buscar() {
        try {
            if (sedeSeleccionada != null) {
                filtro.setSedeFk(sedeSeleccionada.getSedPk());
            }
            if (comboTiposSede.getSelectedT() != null && comboTiposSede.getSelected() != -1) {
                filtro.setTipoSede(comboTiposSede.getSelectedT());
            }
            if (comboEstado.getSelectedT() != null && comboEstado.getSelected() != -1) {
                filtro.setEstado(comboEstado.getSelectedT());
            }
            if (comboDepartamentos.getSelectedT() != null && comboDepartamentos.getSelected() != -1) {
                filtro.setDepartamento(comboDepartamentos.getSelectedT().getDepPk());
            }
            if (comboMunicipios.getSelectedT() != null && comboMunicipios.getSelected() != -1) {
                filtro.setMunicipio(comboMunicipios.getSelectedT().getMunPk());
            }
            filtro.setIncluirCampos(new String[]{"dpjPk",    
                    "dpjMotivo",    
                    "dpjEstado",    
                    "dpjActa.achPk", 
                    "dpjActa.achVersion",    
                    "dpjActa.achNombre",  
                    "dpjActa.achDescripcion",  
                    "dpjActa.achContentType",  
                    "dpjActa.achTmpPath",  
                    "dpjActa.achExt",  
                    "dpjNumeroAcuerdo",    
                    "dpjFechaAcuerdo", 
                    "dpjAcuerdo.achPk", 
                    "dpjAcuerdo.achVersion",   
                    "dpjOaeFk.oaePk",
                    "dpjOaeFk.oaeVersion",
                    "dpjOaeFk.oaeFechaCierre",
                    "dpjOaeFk.oaeSede.sedPk",
                    "dpjOaeFk.oaeSede.sedTipo",
                    "dpjOaeFk.oaeSede.sedVersion",
                    "dpjOaeFk.oaeSede.sedCodigo",
                    "dpjOaeFk.oaeSede.sedNombre",   
                    "dpjUltModFecha",
                    "dpjUltModUsuario",
                    "dpjVersion"});
            totalResultados = restClient.buscarTotal(filtro);
            solLazyModel = new LazySolicitudesOAEDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        try {
            List<TipoSede> tipoSede = new ArrayList(Arrays.asList(TipoSede.values()));
            comboTiposSede = new SofisComboG(tipoSede, "text");
            comboTiposSede.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboTiposSede.ordenar();

            List<EnumEstadoSolicitudOAE> tipoEnum = new ArrayList(Arrays.asList(EnumEstadoSolicitudOAE.values()));
            comboEstado = new SofisComboG(tipoEnum, "text");
            comboEstado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboEstado.ordenar();

            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});
            fc.setOrderBy(new String[]{"depNombre"});
            fc.setIncluirCampos(new String[]{"depNombre", "depVersion"});
            List<SgDepartamento> listaDepartamentos = catalogoClient.buscarDepartamento(fc);
            comboDepartamentos = new SofisComboG<>(listaDepartamentos, "depNombre");
            comboDepartamentos.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboMunicipios = new SofisComboG<>();
            comboMunicipios.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            acciones = new SelectItem[]{
                new SelectItem(Boolean.TRUE, "Aprobar"),
                new SelectItem(Boolean.FALSE, "Rechazar")
            };
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {

    }

    public void limpiar() {
        comboTiposSede.setSelected(-1);
        comboEstado.setSelected(-1);
        comboDepartamentos.setSelected(-1);
        comboMunicipios = new SofisComboG<>();
        comboMunicipios.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        filtro = new FiltroSolicitudesOAE();
        sedeSeleccionada = null;
        buscar();
    }

    public void agregar() {
        limpiarCombos();
        entidadEnEdicion = new SgSolDeshabilitarPerJur();
        entidadEnEdicion.setDpjOaeFk(new SgOrganismoAdministracionEscolar());
    }

    public void actualizar(SgSolDeshabilitarPerJur var) {
        try {
            limpiarCombos();
            entidadEnEdicion = restClient.obtenerPorId(var.getDpjPk());
            if (entidadEnEdicion.getDpjAcuerdo() != null) {
                resultado = true;
            } else {
                resultado = false;
            }
        } catch (BusinessException be) {
            entidadEnEdicion.setDpjEstado(EnumEstadoSolicitudOAE.ENVIADA);
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            entidadEnEdicion.setDpjEstado(EnumEstadoSolicitudOAE.ENVIADA);
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardar() {
        try {
            if (!resultado) {
                entidadEnEdicion.setDpjAcuerdo(null);
                entidadEnEdicion.setDpjNumeroAcuerdo(null);
                entidadEnEdicion.setDpjFechaAcuerdo(null);
            }
           
            entidadEnEdicion.setDpjAprobar(resultado);
            entidadEnEdicion.setDpjEstado(EnumEstadoSolicitudOAE.FINALIZADA);
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscar();
        } catch (BusinessException be) {
            entidadEnEdicion.setDpjEstado(EnumEstadoSolicitudOAE.ENVIADA);
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            entidadEnEdicion.setDpjEstado(EnumEstadoSolicitudOAE.ENVIADA);
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Sube acta de OAE
     *
     * @param event
     */
    public void subirAcuerdoOae(FileUploadEvent event) {
        try {
            entidadEnEdicion.setDpjAcuerdo(new SgArchivo());
            handleArchivoBean.subirArchivoTmp(event.getFile(), entidadEnEdicion.getDpjAcuerdo());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP_2, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public List<SgSede> completeSede(String query) {
        try {
            FiltroSedes fil = new FiltroSedes();
            fil.setSedCodigoONombre(query);
            fil.setMaxResults(11L);
            fil.setSedHabilitado(Boolean.TRUE);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedCodigo", "sedNombre", "sedTipo", "sedVersion"});
            return restSede.buscar(fil);
        } catch (Exception ex) {
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void deseleccionarSede(AjaxBehaviorEvent event) {
        this.sedeSeleccionada = null;
//        filtro.setOaeSedeFk(null);
    }

    public void seleccionarDepartamento() {
        try {
            comboMunicipios = new SofisComboG();
            comboMunicipios.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            if (comboDepartamentos.getSelectedT() != null) {
                FiltroMunicipio filtro = new FiltroMunicipio();
                filtro.setOrderBy(new String[]{"munNombre"});
                filtro.setAscending(new boolean[]{true});
                filtro.setMunDepartamentoFk(comboDepartamentos.getSelectedT().getDepPk());
                filtro.setIncluirCampos(new String[]{"munNombre", "munVersion"});
                List<SgMunicipio> municipios = catalogoClient.buscarMunicipio(filtro);
                comboMunicipios = new SofisComboG(municipios, "munNombre");
                comboMunicipios.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void historial(Long id) {
        try {
            historialSolicitudes = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroSolicitudesOAE getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroSolicitudesOAE filtro) {
        this.filtro = filtro;
    }

    public SgSolDeshabilitarPerJur getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgSolDeshabilitarPerJur entidadEnEdicion) {
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

    public SgSede getSedeSeleccionada() {
        return sedeSeleccionada;
    }

    public void setSedeSeleccionada(SgSede sedeSeleccionada) {
        this.sedeSeleccionada = sedeSeleccionada;
    }

    public SofisComboG<TipoSede> getComboTiposSede() {
        return comboTiposSede;
    }

    public void setComboTiposSede(SofisComboG<TipoSede> comboTiposSede) {
        this.comboTiposSede = comboTiposSede;
    }

    public SofisComboG<EnumEstadoSolicitudOAE> getComboEstado() {
        return comboEstado;
    }

    public void setComboEstado(SofisComboG<EnumEstadoSolicitudOAE> comboEstado) {
        this.comboEstado = comboEstado;
    }

    public SofisComboG<SgDepartamento> getComboDepartamentos() {
        return comboDepartamentos;
    }

    public void setComboDepartamentos(SofisComboG<SgDepartamento> comboDepartamentos) {
        this.comboDepartamentos = comboDepartamentos;
    }

    public SofisComboG<SgMunicipio> getComboMunicipios() {
        return comboMunicipios;
    }

    public void setComboMunicipios(SofisComboG<SgMunicipio> comboMunicipios) {
        this.comboMunicipios = comboMunicipios;
    }

    public List<RevHistorico> getHistorialSolicitudes() {
        return historialSolicitudes;
    }

    public void setHistorialSolicitudes(List<RevHistorico> historialSolicitudes) {
        this.historialSolicitudes = historialSolicitudes;
    }

    public LazySolicitudesOAEDataModel getSolLazyModel() {
        return solLazyModel;
    }

    public void setSolLazyModel(LazySolicitudesOAEDataModel solLazyModel) {
        this.solLazyModel = solLazyModel;
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

    public SelectItem[] getAcciones() {
        return acciones;
    }

    public void setAcciones(SelectItem[] acciones) {
        this.acciones = acciones;
    }

    public Boolean getResultado() {
        return resultado;
    }

    public void setResultado(Boolean resultado) {
        this.resultado = resultado;
    }

   
}
