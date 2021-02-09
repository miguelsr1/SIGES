/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.SerializationUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgCiclo;
import sv.gob.mined.siges.web.dto.SgModalidad;
import sv.gob.mined.siges.web.dto.SgNivel;
import sv.gob.mined.siges.web.dto.SgOrganizacionCurricular;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CicloRestClient;
import sv.gob.mined.siges.web.restclient.ModalidadRestClient;
import sv.gob.mined.siges.web.restclient.NivelRestClient;
import sv.gob.mined.siges.web.restclient.OrganizacionCurricularRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.SgArchivo;
import sv.gob.mined.siges.web.dto.catalogo.SgPlantilla;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroPlantilla;
import sv.gob.mined.siges.web.restclient.PlantillaRestClient;
import sv.gob.mined.siges.web.utilidades.FileUploadUtils;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class OrganizacionCurricularBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(OrganizacionCurricularBean.class.getName());

    @Inject
    @Param(name = "id")
    private Long orgCurrId;

    @Inject
    @Param(name = "rev")
    private Long orgCurrRev;

    @Inject
    @Param(name = "edit")
    private Boolean editable;

    @Inject
    @ConfigProperty(name = "files.tmp.path")
    private String tmpBasePath;

    @Inject
    private OrganizacionCurricularRestClient restClient;

    @Inject
    private NivelRestClient restNivel;

    @Inject
    private CicloRestClient restCiclo;

    @Inject
    private ModalidadRestClient restModalidad;

    @Inject
    private PlantillaRestClient plantillaClient;

    @Inject
    private ApplicationBean appBean;

    @Inject
    private SessionBean sessionBean;

    private Boolean soloLectura = Boolean.FALSE;
    private FiltroCodiguera filtro = new FiltroCodiguera();
    private SgOrganizacionCurricular entidadEnEdicion;
    private SgNivel nivelEnEdicion = new SgNivel();
    private SgCiclo cicloEnEdicion = new SgCiclo();
    private SgModalidad modalidadEnEdicion = new SgModalidad();
    private SgPlantilla plantillaEnEdicion = new SgPlantilla();

    public OrganizacionCurricularBean() {
    }

    @PostConstruct
    public void init() {
        try {
            if (orgCurrId != null && orgCurrId > 0) {
                if (orgCurrRev != null && orgCurrRev > 0) {
                    this.actualizar(restClient.obtenerEnRevision(orgCurrId, orgCurrRev));
                    this.soloLectura = Boolean.TRUE;
                } else {
                    this.actualizar(restClient.obtenerPorId(orgCurrId));
                    soloLectura = editable != null ? !editable : soloLectura;
                }
            } else {
                this.agregar();
            }
            validarAcceso();
        } catch (HttpClientException ce) {
            JSFUtils.redirectNotFound();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }
    }

    public void validarAcceso() {
        if (soloLectura) {
            if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.BUSCAR_ORGANIZACION_CURRICULAR)) {
                JSFUtils.redirectToIndex();
            }
        } else {
            if (entidadEnEdicion.getOcuPk() == null) {
                if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.CREAR_ORGANIZACION_CURRICULAR)) {
                    JSFUtils.redirectToIndex();
                }
            } else {
                if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.ACTUALIZAR_ORGANIZACION_CURRICULAR)) {
                    JSFUtils.redirectToIndex();
                }
            }
        }
    }

    public void cargarCombos() {

    }

    public void agregar() {
        entidadEnEdicion = new SgOrganizacionCurricular();
        JSFUtils.limpiarMensajesError();
    }

    public void actualizar(SgOrganizacionCurricular ocu) {
        try {
            entidadEnEdicion = ocu;
            Collections.sort(ocu.getOcuNiveles(), (SgNivel a1, SgNivel a2) -> (a1.getNivOrden() != null ? a1.getNivOrden() : 0) - (a2.getNivOrden() != null ? a2.getNivOrden() : 0));
            for (SgNivel niv : entidadEnEdicion.getOcuNiveles()) {
                Collections.sort(niv.getNivCiclos(), (SgCiclo a1, SgCiclo a2) -> (a1.getCicOrden() != null ? a1.getCicOrden() : 0) - (a2.getCicOrden() != null ? a2.getCicOrden() : 0));
                for (SgCiclo cic : niv.getNivCiclos()) {
                    Collections.sort(cic.getCicModalidades(), (SgModalidad a1, SgModalidad a2) -> (a1.getModOrden() != null ? a1.getModOrden() : 0) - (a2.getModOrden() != null ? a2.getModOrden() : 0));
                }
            }
            JSFUtils.limpiarMensajesError();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void editarNivel(SgNivel var) {
        try {
            nivelEnEdicion = restNivel.obtenerPorId(var.getNivPk());
            JSFUtils.limpiarMensajesError();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void agregarNivel() {
        try {
            nivelEnEdicion = new SgNivel();
            JSFUtils.limpiarMensajesError();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardarNivel() {
        try {
            nivelEnEdicion.setNivOrganizacionCurricular(entidadEnEdicion);
            nivelEnEdicion = restNivel.guardar(nivelEnEdicion);
            this.actualizar(restClient.obtenerPorId(entidadEnEdicion.getOcuPk()));
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void editarCiclo(SgNivel niv, SgCiclo var) {
        try {
            nivelEnEdicion = restNivel.obtenerPorId(niv.getNivPk());
            cicloEnEdicion = restCiclo.obtenerPorId(var.getCicPk());
            JSFUtils.limpiarMensajesError();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void agregarCiclo(SgNivel niv) {
        try {
            nivelEnEdicion = niv;
            cicloEnEdicion = new SgCiclo();
            JSFUtils.limpiarMensajesError();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardarCiclo() {
        try {
            cicloEnEdicion.setCicNivel(nivelEnEdicion);
            cicloEnEdicion = restCiclo.guardar(cicloEnEdicion);
            this.actualizar(restClient.obtenerPorId(entidadEnEdicion.getOcuPk()));
            PrimeFaces.current().executeScript("PF('itemDialogCiclo').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP_2, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP_2, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void editarModalidad(SgCiclo cic, SgModalidad mod) {
        try {
            cicloEnEdicion = restCiclo.obtenerPorId(cic.getCicPk());
            modalidadEnEdicion = restModalidad.obtenerPorIdLazy(mod.getModPk());
            JSFUtils.limpiarMensajesError();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void agregarModalidad(SgCiclo cic) {
        try {
            cicloEnEdicion = cic;
            modalidadEnEdicion = new SgModalidad();
            JSFUtils.limpiarMensajesError();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardarModalidad() {
        try {
            modalidadEnEdicion.setModCiclo(cicloEnEdicion);
            modalidadEnEdicion = restModalidad.guardar(modalidadEnEdicion);
            this.actualizar(restClient.obtenerPorId(entidadEnEdicion.getOcuPk()));
            cicloEnEdicion = new SgCiclo();
            modalidadEnEdicion = new SgModalidad();
            PrimeFaces.current().executeScript("PF('itemDialogMod').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP_3, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP_3, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void editarPlantilla(SgPlantilla var) {
        JSFUtils.limpiarMensajesError();
        plantillaEnEdicion = (SgPlantilla) SerializationUtils.clone(var);
    }

    public void agregarPlantilla() {
        JSFUtils.limpiarMensajesError();
        plantillaEnEdicion = new SgPlantilla();
    }

    public void guardarPlantilla() {
        try {
            plantillaEnEdicion.setPlaOrganizacionCurricular(entidadEnEdicion);
            plantillaEnEdicion = plantillaClient.guardar(plantillaEnEdicion);
            if (entidadEnEdicion.getOcuPlantillas() == null) {
                entidadEnEdicion.setOcuPlantillas(new ArrayList<>());
            }
            if (entidadEnEdicion.getOcuPlantillas().contains(plantillaEnEdicion)) {
                entidadEnEdicion.getOcuPlantillas().remove(plantillaEnEdicion);
            }
            entidadEnEdicion.getOcuPlantillas().add(plantillaEnEdicion);
            PrimeFaces.current().executeScript("PF('itemDialogPlantilla').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP_4, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP_4, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void handleFileUploadPlantilla(FileUploadEvent event) {
        if (plantillaEnEdicion.getPlaArchivo() == null) {
            plantillaEnEdicion.setPlaArchivo(new SgArchivo());
        }
        FileUploadUtils.handleFileUpload(event.getFile(), plantillaEnEdicion.getPlaArchivo(), tmpBasePath);
    }

    public void eliminarArchivoPlantilla() {
        this.plantillaEnEdicion.setPlaArchivo(null);
    }

    public void eliminarPlantilla() {
        try {
            plantillaClient.eliminar(plantillaEnEdicion.getPlaPk());
            if (entidadEnEdicion.getOcuPlantillas().contains(plantillaEnEdicion)) {
                entidadEnEdicion.getOcuPlantillas().remove(plantillaEnEdicion);
            }
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            plantillaEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public List<String> completeCodigosPlantilla(String query) {
        try {
            FiltroPlantilla fil = new FiltroPlantilla();
            fil.setNombre(query);
            fil.setHabilitado(Boolean.TRUE);
            fil.setHabilitadaReemplazoOrg(Boolean.TRUE);
            fil.setOrderBy(new String[]{"plaCodigo"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"plaCodigo"});
            return plantillaClient.buscarPlantillas(fil).stream().map(p -> p.getPlaCodigo()).collect(Collectors.toList());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }
    
    public void guardar() {
        try {
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            this.actualizar(restClient.obtenerPorId(entidadEnEdicion.getOcuPk()));
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getOcuPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            entidadEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public String getTituloPagina() {
        if (this.orgCurrId == null) {
            return Etiquetas.getValue("nuevoOrganizacionCurricular");
        } else if (this.orgCurrRev != null) {
            return Etiquetas.getValue("historialOrganizacionCurricular") + " " + entidadEnEdicion.getOcuNombre() + " (" + entidadEnEdicion.getOcuUltModUsuario() + (entidadEnEdicion.getOcuUltModFecha() != null ? (" " + this.appBean.getDateTimeFormater().format(entidadEnEdicion.getOcuUltModFecha())) : "") + ")";
        } else if (this.soloLectura) {
            return Etiquetas.getValue("verSede") + " " + entidadEnEdicion.getOcuNombre();
        } else {
            return Etiquetas.getValue("edicionOrganizacionCurricular") + " " + entidadEnEdicion.getOcuNombre();
        }
    }

    public SgModalidad getModalidadEnEdicion() {
        return modalidadEnEdicion;
    }

    public void setModalidadEnEdicion(SgModalidad modalidadEnEdicion) {
        this.modalidadEnEdicion = modalidadEnEdicion;
    }

    public Long getOrgCurrId() {
        return orgCurrId;
    }

    public void setOrgCurrId(Long orgCurrId) {
        this.orgCurrId = orgCurrId;
    }

    public FiltroCodiguera getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCodiguera filtro) {
        this.filtro = filtro;
    }

    public SgOrganizacionCurricular getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgOrganizacionCurricular entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public SgNivel getNivelEnEdicion() {
        return nivelEnEdicion;
    }

    public void setNivelEnEdicion(SgNivel nivelEnEdicion) {
        this.nivelEnEdicion = nivelEnEdicion;
    }

    public SgCiclo getCicloEnEdicion() {
        return cicloEnEdicion;
    }

    public void setCicloEnEdicion(SgCiclo cicloEnEdicion) {
        this.cicloEnEdicion = cicloEnEdicion;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public SgPlantilla getPlantillaEnEdicion() {
        return plantillaEnEdicion;
    }

    public void setPlantillaEnEdicion(SgPlantilla plantillaEnEdicion) {
        this.plantillaEnEdicion = plantillaEnEdicion;
    }

    public Long getOrgCurrRev() {
        return orgCurrRev;
    }
    
    

}
