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
import org.apache.commons.lang3.SerializationUtils;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgAnioLectivo;
import sv.gob.mined.siges.web.enumerados.EnumAnioLectivoEstado;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAnioLectivo;
import sv.gob.mined.siges.web.lazymodels.LazyAnioLectivoDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AnioLectivoRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class AniosLectivosBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(AniosLectivosBean.class.getName());

    @Inject
    private AnioLectivoRestClient restClient;

    @Inject
    private SessionBean sessionBean;

    
    private SgAnioLectivo entidadEnEdicion = new SgAnioLectivo();
    private FiltroAnioLectivo filtro = new FiltroAnioLectivo();
    private LazyAnioLectivoDataModel anioLectivoLazyModel;
    private List<RevHistorico> historialAnioLectivo = new ArrayList();
    private String formatoSeleccionado = "csv";
    private Integer paginado = 10;
    private Long totalResultados;
    private SofisComboG<EnumAnioLectivoEstado> comboEstado;
    private SofisComboG<EnumAnioLectivoEstado> comboFiltroEstado;
    

    public AniosLectivosBean() {
    }
    
    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            cargarCombos();
            buscar();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

    }
    
    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_ANIO_LECTIVO)) {
            JSFUtils.redirectToIndex();
        }
    }

    public String buscar() {
        try {
          
            totalResultados = restClient.buscarTotal(filtro);
            filtro.setAleEstado(comboFiltroEstado.getSelectedT());
            anioLectivoLazyModel = new LazyAnioLectivoDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void cargarCombos() {

        List<EnumAnioLectivoEstado> estados = new ArrayList(Arrays.asList(EnumAnioLectivoEstado.values()));
        comboEstado = new SofisComboG(estados, "text");
        comboEstado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboFiltroEstado = new SofisComboG(estados, "text");
        comboFiltroEstado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
    }

    private void limpiarCombos() {
        comboEstado.setSelected(-1);
        comboFiltroEstado.setSelected(-1);
    }

    public String limpiar() {
        comboFiltroEstado.setSelected(-1);
        filtro = new FiltroAnioLectivo();
        buscar();
        return null;
    }

    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgAnioLectivo();
    }

    public void actualizar(SgAnioLectivo var) {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        comboEstado.setSelectedT(var.getAleEstado());
        entidadEnEdicion = (SgAnioLectivo) SerializationUtils.clone(var);
    }

    public void guardar() {
        try {
            entidadEnEdicion.setAleEstado(this.comboEstado.getSelectedT() != null ? comboEstado.getSelectedT() : null);
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscar();
        } catch (BusinessException be) {
            LOGGER.log(Level.SEVERE, be.getMessage(), be);
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getAlePk());
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

    public String historial(Long id) {
        try {
            historialAnioLectivo = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }
public String getTituloPagina() {
        if(this.entidadEnEdicion.getAlePk()!=null){
           // if (this.soloLectura) {
                return Etiquetas.getValue("visualizarAnioLectivo")+ " " +entidadEnEdicion.getAleDescripcion();
           // } else {
              //  return Etiquetas.getValue("edicionEstudiante")+ " " + entidadEnEdicion.getEstPersona().getPerPrimerNombre()+ " " + entidadEnEdicion.getEstPersona().getPerPrimerApellido();
            //}
        }else{
            return "";
        }
    }
    
    public AnioLectivoRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(AnioLectivoRestClient restClient) {
        this.restClient = restClient;
    }

    public SgAnioLectivo getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgAnioLectivo entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public FiltroAnioLectivo getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroAnioLectivo filtro) {
        this.filtro = filtro;
    }

    public LazyAnioLectivoDataModel getAnioLectivoLazyModel() {
        return anioLectivoLazyModel;
    }

    public void setAnioLectivoLazyModel(LazyAnioLectivoDataModel anioLectivoLazyModel) {
        this.anioLectivoLazyModel = anioLectivoLazyModel;
    }

    public List<RevHistorico> getHistorialAnioLectivo() {
        return historialAnioLectivo;
    }

    public String getFormatoSeleccionado() {
        return formatoSeleccionado;
    }

    public void setFormatoSeleccionado(String formatoSeleccionado) {
        this.formatoSeleccionado = formatoSeleccionado;
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

    public SofisComboG<EnumAnioLectivoEstado> getComboEstado() {
        return comboEstado;
    }

    public void setComboEstado(SofisComboG<EnumAnioLectivoEstado> comboEstado) {
        this.comboEstado = comboEstado;
    }

    public SofisComboG<EnumAnioLectivoEstado> getComboFiltroEstado() {
        return comboFiltroEstado;
    }

    public void setComboFiltroEstado(SofisComboG<EnumAnioLectivoEstado> comboFiltroEstado) {
        this.comboFiltroEstado = comboFiltroEstado;
    }
}
