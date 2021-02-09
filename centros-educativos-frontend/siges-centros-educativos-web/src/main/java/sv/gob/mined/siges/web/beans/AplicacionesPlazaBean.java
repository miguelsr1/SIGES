/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.cdi.Param;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.SolicitudPlazaRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.SgSolicitudPlaza;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAplicacionPlaza;
import sv.gob.mined.siges.web.lazymodels.LazyAplicacionPlazaDataModel;
import sv.gob.mined.siges.web.restclient.AplicacionPlazaRestClient;

@Named
@ViewScoped
public class AplicacionesPlazaBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(AplicacionesPlazaBean.class.getName());

    @Inject
    private SolicitudPlazaRestClient restClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private AplicacionPlazaRestClient restAplicaciones;
    
    @Inject
    @Param(name = "id")
    private Long plazaId;
    

    private SgSolicitudPlaza entidadEnEdicion;
    private FiltroAplicacionPlaza filtro = new FiltroAplicacionPlaza();
    private LazyAplicacionPlazaDataModel aplicacionesLazyModel;
    private Integer paginado = 10;
    private Long totalResultados;

    public AplicacionesPlazaBean() {
    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            cargarCombos();
            
            if (plazaId != null && plazaId > 0) {
                entidadEnEdicion = restClient.obtenerPorId(plazaId);
            }
            buscar();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.VER_POSTULACIONES)) {
            JSFUtils.redirectToIndex();
        }
    }


    public String buscar() {
        try {
            if(entidadEnEdicion!=null){

                filtro.setAplPlaza(entidadEnEdicion.getSplPk());
                filtro.setIncluirCampos(new String[]{
                    "aplPersonal.pseVersion",
                    "aplPersonal.psePersona.perPrimerNombre",
                    "aplPersonal.psePersona.perSegundoNombre",
                    "aplPersonal.psePersona.perTercerNombre",
                    "aplPersonal.psePersona.perPrimerApellido",
                    "aplPersonal.psePersona.perSegundoApellido",
                    "aplPersonal.psePersona.perTercerApellido",
                    "aplPersonal.psePersona.perDui",
                    "aplPersonal.psePersona.perNip",
                    "aplPersonal.psePk",
                    "aplRevPersonalCuandoAplica",
                    "aplFechaAplico",
                    "aplVersion"});
                totalResultados = restAplicaciones.buscarTotal(filtro);
                aplicacionesLazyModel = new LazyAplicacionPlazaDataModel(restAplicaciones, filtro, totalResultados, paginado);
            }

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        }catch (Exception ex) {
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
        } catch (Exception ex) {
            Logger.getLogger(AplicacionesPlazaBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void limpiarCombos() {
        cargarCombos();
    }

    public String limpiar() {
        filtro = new FiltroAplicacionPlaza();
        limpiarCombos();
        buscar();
        return null;
    }
    
    
    

   //<editor-fold defaultstate="collapsed" desc="GET & SET">
    
    public SgSolicitudPlaza getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgSolicitudPlaza entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public FiltroAplicacionPlaza getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroAplicacionPlaza filtro) {
        this.filtro = filtro;
    }

    public LazyAplicacionPlazaDataModel getAplicacionesLazyModel() {
        return aplicacionesLazyModel;
    }
    public void setAplicacionesLazyModel(LazyAplicacionPlazaDataModel aplicacionesLazyModel) {
        this.aplicacionesLazyModel = aplicacionesLazyModel;
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


}
