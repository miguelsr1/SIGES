/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.SerializationUtils;
import org.omnifaces.cdi.Param;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgAnioLectivo;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AnioLectivoRestClient;
import sv.gob.mined.siges.web.restclient.CierreAnioLectivoRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author bruno
 */
@Named
@ViewScoped
public class AnioLectivoBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(AnioLectivoBean.class.getName());

    @Inject
    private AnioLectivoRestClient restClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private  SedeRestClient sedeRestClient;
    
    @Inject
    @Param(name = "id")
    private Long aleId;

    @Inject
    @Param(name = "rev")
    private Long aleRev;

    @Inject
    @Param(name = "edit")
    private Boolean editable;
    
    @Inject
    private CierreAnioLectivoRestClient cierreAnioLectivoRestClient;
    
    private SgAnioLectivo entidadEnEdicion;
    private Boolean soloLectura = Boolean.FALSE;

    
    @PostConstruct
    public void init() {
        try {
            if (aleId != null && aleId > 0) {
                if (aleRev != null && aleRev > 0) {
                    this.actualizar(restClient.obtenerEnRevision(aleId, aleRev));
                    this.soloLectura = Boolean.TRUE;
                } else {
                    this.actualizar(restClient.obtenerPorId(aleId));
                    soloLectura = editable != null ? !editable : soloLectura;
                }
            } else {
                JSFUtils.redirectToIndex();
            }
            validarAcceso();
            //cargarCombos();
        } catch (HttpClientException ce) {
            JSFUtils.redirectNotFound();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }

    }
    
    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_ANIO_LECTIVO)) {
            JSFUtils.redirectToIndex();
        }
    }

    public void actualizar(SgAnioLectivo var) {
        JSFUtils.limpiarMensajesError();
        entidadEnEdicion = (SgAnioLectivo) SerializationUtils.clone(var);
    }
    public String getTituloPagina() {
        if(this.entidadEnEdicion.getAlePk()!=null){
            if (this.soloLectura) {
                return Etiquetas.getValue("visualizarAnioLectivo")+ " " +entidadEnEdicion.getAleDescripcion();
            } else {
                return Etiquetas.getValue("edicionAnioLectivo")+ " " + entidadEnEdicion.getAleDescripcion();
            }
        }else{
            return "";
        }
    }
        
    public List<SgSede> completeSede(String query) {
        try {
            FiltroSedes fil = new FiltroSedes();
            fil.setSecurityOperation(ConstantesOperaciones.BUSCAR_SEDES);
            fil.setSedCodigoONombre(query);
            fil.setMaxResults(11L);
            fil.setSedHabilitado(Boolean.TRUE);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedCodigo", "sedNombre", "sedTipo", "sedVersion"});
            return sedeRestClient.buscar(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }
    public SgAnioLectivo getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgAnioLectivo entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }
}
