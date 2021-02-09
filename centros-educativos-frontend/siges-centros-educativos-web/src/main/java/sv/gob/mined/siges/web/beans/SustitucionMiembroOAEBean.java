/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.SerializationUtils;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgPersonaOrganismoAdministracion;
import sv.gob.mined.siges.web.dto.SgRelOAEIdentificacionOAE;
import sv.gob.mined.siges.web.dto.SgRelSustitucionMiembroOAE;
import sv.gob.mined.siges.web.dto.SgSustitucionMiembroOAE;
import sv.gob.mined.siges.web.dto.catalogo.SgIdentificacionOAE;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelOAEIdentificacionOAE;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.DeshabilitarPersonaJuridicaRestClient;
import sv.gob.mined.siges.web.restclient.PersonaOrganismoAdministracionRestClient;
import sv.gob.mined.siges.web.restclient.PersonaRestClient;
import sv.gob.mined.siges.web.restclient.RelOAEIdentificacionOAERestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.restclient.SustitucionMiembroOAERestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class SustitucionMiembroOAEBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(SustitucionMiembroOAEBean.class.getName());

    @Inject
    @Param(name = "id")
    private Long orgId;

    @Inject
    @Param(name = "rev")
    private Long rev;

    @Inject
    @Param(name = "edit")
    private Boolean editable;

    @Inject
    private SustitucionMiembroOAERestClient restClient;

    @Inject
    private SedeRestClient restSede;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private PersonaOrganismoAdministracionRestClient personaOrganismoRestClient;

    @Inject
    private CatalogosRestClient catalogosRestClient;

    @Inject
    private PersonaRestClient restPersona;

    @Inject
    private HandleArchivoBean handleArchivoBean;

    @Inject
    private DeshabilitarPersonaJuridicaRestClient deshabilitarPerJurCLient;

    @Inject
    private RelOAEIdentificacionOAERestClient RelOAEIdentificacionOAERestClient;

    @Inject
    private SustitucionMiembroOAERestClient sustitucionMiembroOAERestClient;

    private SgSustitucionMiembroOAE entidadEnEdicion = new SgSustitucionMiembroOAE();

    private List<SgRelSustitucionMiembroOAE> listRelSustitucionOAE = new ArrayList<>();

    private Set<SgIdentificacionOAE> listIdentificacionOAE;
    private List<SgRelOAEIdentificacionOAE> listRelOAEIdentificacionOAE;
    private List<SgPersonaOrganismoAdministracion> listPersonasAsustituir;
    private List<SgPersonaOrganismoAdministracion> listPersonasSustituyen;
    private SgPersonaOrganismoAdministracion personaEnEdicion;
    
    private SelectItem[] acciones;
    private Integer resultado;

    public SustitucionMiembroOAEBean() {
    }

    @PostConstruct
    public void init() {
        try {
            buscarIdentificacionOAE();
            if (orgId != null && orgId > 0) {
                this.actualizar(restClient.obtenerPorId(orgId));
            }
            prepararIdentificacionOAE();
            acciones = new SelectItem[]{
                new SelectItem(1, "Aprobar"),
                new SelectItem(2, "Rechazar")
            };
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void buscarIdentificacionOAE() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            listIdentificacionOAE = new HashSet<SgIdentificacionOAE>(catalogosRestClient.buscarIdentificacionOAE(fc));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void prepararIdentificacionOAE() {
        try {
            Set<SgIdentificacionOAE> elementosSinAgregar = new HashSet<SgIdentificacionOAE>(listIdentificacionOAE);
            listRelOAEIdentificacionOAE = new ArrayList();
            if (entidadEnEdicion.getSmoOaeFk().getOaePk() != null) {
                FiltroRelOAEIdentificacionOAE filtroRel = new FiltroRelOAEIdentificacionOAE();
                filtroRel.setIdentificacionHabilitada(Boolean.TRUE);
                filtroRel.setRioOrganismoAdministracionEscolarFk(entidadEnEdicion.getSmoOaeFk().getOaePk());
                listRelOAEIdentificacionOAE = new ArrayList(RelOAEIdentificacionOAERestClient.buscar(filtroRel));
                Set<SgIdentificacionOAE> elementos = new HashSet<SgIdentificacionOAE>(listRelOAEIdentificacionOAE.stream().map(c -> c.getRioIdentificacionOAEfk()).collect(Collectors.toList()));
                elementosSinAgregar.removeAll(elementos);
            }
            for (SgIdentificacionOAE id : elementosSinAgregar) {
                SgRelOAEIdentificacionOAE rel = new SgRelOAEIdentificacionOAE();
                rel.setRioIdentificacionOAEfk(id);
                listRelOAEIdentificacionOAE.add(rel);
            }
            Collections.sort(listRelOAEIdentificacionOAE, (o1, o2) -> o1.getRioIdentificacionOAEfk().getIoaPk().compareTo(o2.getRioIdentificacionOAEfk().getIoaPk()));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void actualizar(SgSustitucionMiembroOAE var) {
        listPersonasAsustituir = new ArrayList();
        listPersonasSustituyen = new ArrayList();
        JSFUtils.limpiarMensajesError();
        entidadEnEdicion = (SgSustitucionMiembroOAE) SerializationUtils.clone(var);
        if (entidadEnEdicion != null) {
            listPersonasAsustituir = entidadEnEdicion.getSmoRelSustitucionMiembroOAE().stream().map(c -> c.getRsmMiembroaSustituirFk()).collect(Collectors.toList());
        }
    }
    
    public void nuevaEvaluacion() {
        resultado = null;
    }

    public void evaluar() {
        if (resultado == null) {
            JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_ACCION_VACIO), "");
        } else {
            switch (resultado) {
                case 1:
                    aceptar();
                    break;
                case 2:
                    rechazar();
                    break;
            }
        }
    }
    
    public void aceptar(){
        try{ 
            if(entidadEnEdicion!=null){
                List<SgRelSustitucionMiembroOAE> list=entidadEnEdicion.getSmoRelSustitucionMiembroOAE();
                entidadEnEdicion=restClient.aprobar(entidadEnEdicion);
                entidadEnEdicion.setSmoRelSustitucionMiembroOAE(list);
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
                
                PrimeFaces.current().executeScript("PF('confirmDialogEnviar').hide()");
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void rechazar(){
        try{ 
            if(entidadEnEdicion!=null){
                List<SgRelSustitucionMiembroOAE> list=entidadEnEdicion.getSmoRelSustitucionMiembroOAE();
                entidadEnEdicion=restClient.rechazar(entidadEnEdicion);
                entidadEnEdicion.setSmoRelSustitucionMiembroOAE(list);
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
                
                PrimeFaces.current().executeScript("PF('confirmDialogEnviar').hide()");
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void actualizarPersona(SgPersonaOrganismoAdministracion var) {
        try {
            JSFUtils.limpiarMensajesError();
            personaEnEdicion = personaOrganismoRestClient.obtenerPorId(var.getPoaPk());
        
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public SgSustitucionMiembroOAE getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgSustitucionMiembroOAE entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgRelSustitucionMiembroOAE> getListRelSustitucionOAE() {
        return listRelSustitucionOAE;
    }

    public void setListRelSustitucionOAE(List<SgRelSustitucionMiembroOAE> listRelSustitucionOAE) {
        this.listRelSustitucionOAE = listRelSustitucionOAE;
    }

    public Set<SgIdentificacionOAE> getListIdentificacionOAE() {
        return listIdentificacionOAE;
    }

    public void setListIdentificacionOAE(Set<SgIdentificacionOAE> listIdentificacionOAE) {
        this.listIdentificacionOAE = listIdentificacionOAE;
    }

    public List<SgRelOAEIdentificacionOAE> getListRelOAEIdentificacionOAE() {
        return listRelOAEIdentificacionOAE;
    }

    public void setListRelOAEIdentificacionOAE(List<SgRelOAEIdentificacionOAE> listRelOAEIdentificacionOAE) {
        this.listRelOAEIdentificacionOAE = listRelOAEIdentificacionOAE;
    }

    public List<SgPersonaOrganismoAdministracion> getListPersonasAsustituir() {
        return listPersonasAsustituir;
    }

    public void setListPersonasAsustituir(List<SgPersonaOrganismoAdministracion> listPersonasAsustituir) {
        this.listPersonasAsustituir = listPersonasAsustituir;
    }

    public List<SgPersonaOrganismoAdministracion> getListPersonasSustituyen() {
        return listPersonasSustituyen;
    }

    public void setListPersonasSustituyen(List<SgPersonaOrganismoAdministracion> listPersonasSustituyen) {
        this.listPersonasSustituyen = listPersonasSustituyen;
    }

    public SelectItem[] getAcciones() {
        return acciones;
    }

    public void setAcciones(SelectItem[] acciones) {
        this.acciones = acciones;
    }

    public Integer getResultado() {
        return resultado;
    }

    public void setResultado(Integer resultado) {
        this.resultado = resultado;
    }

    public SgPersonaOrganismoAdministracion getPersonaEnEdicion() {
        return personaEnEdicion;
    }

    public void setPersonaEnEdicion(SgPersonaOrganismoAdministracion personaEnEdicion) {
        this.personaEnEdicion = personaEnEdicion;
    }

}
