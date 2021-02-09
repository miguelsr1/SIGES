/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import org.apache.commons.lang3.SerializationUtils;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgDiplomado;
import sv.gob.mined.siges.web.dto.SgModuloDiplomado;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.lazymodels.LazyDiplomadoDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.DiplomadoRestClient;
import sv.gob.mined.siges.web.restclient.ModuloDiplomadoRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.SgCalificacionDiplomado;
import sv.gob.mined.siges.web.dto.catalogo.SgEscalaCalificacion;
import sv.gob.mined.siges.web.enumerados.EnumCalculoNotaInstitucional;
import sv.gob.mined.siges.web.enumerados.EnumFuncionRedondeo;
import sv.gob.mined.siges.web.enumerados.TipoEscalaCalificacion;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCalificacionDiplomado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEscalaCalificacion;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.restclient.CalificacionDiplomadoRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class DiplomadoBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(DiplomadoBean.class.getName());

    @Inject
    private DiplomadoRestClient restClient;

    @Inject
    private ModuloDiplomadoRestClient modDiprestClient;

    @Inject
    private SessionBean sessionBean;
    @Inject
    private CatalogosRestClient catalogoRestClient;
    
    @Inject
    private CalificacionDiplomadoRestClient calificacionDiplomadorestClient;

    private Boolean soloLectura = false;
    private FiltroCodiguera filtro = new FiltroCodiguera();
    private SgDiplomado entidadEnEdicion = new SgDiplomado();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyDiplomadoDataModel diplomadoLazyModel;
    private SgModuloDiplomado moduloDiplomadoEnEdicion;
    private SofisComboG<SgEscalaCalificacion> comboEscalaCalificacion;
    private SofisComboG<EnumCalculoNotaInstitucional> comboCalculoNotaInstitucional;
    private Boolean esEscalaNumerica = Boolean.FALSE;
    private SofisComboG<EnumFuncionRedondeo> comboFuncionRedondeo;

    @Inject
    @Param(name = "id")
    private Long diplomadoId;

    public DiplomadoBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            if (diplomadoId != null && diplomadoId > 0) {
                entidadEnEdicion = restClient.obtenerPorId(diplomadoId);
                if (entidadEnEdicion.getDipModulosDiplomado() != null) {
                    totalResultados = Long.valueOf(entidadEnEdicion.getDipModulosDiplomado().size());
                } else {
                    totalResultados = Long.valueOf(0);
                }
            } else {
                agregar();
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
            if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.BUSCAR_DIPLOMADO)) {
                JSFUtils.redirectToIndex();
            }
        } else {
            if (entidadEnEdicion.getDipPk() == null) {
                if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.CREAR_DIPLOMADO)) {
                    JSFUtils.redirectToIndex();
                }
            } else {
                if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.ACTUALIZAR_DIPLOMADO)) {
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

    public void buscar() {
        try {
            entidadEnEdicion = restClient.obtenerPorId(entidadEnEdicion.getDipPk());

            if(entidadEnEdicion.getDipModulosDiplomado()!=null && !entidadEnEdicion.getDipModulosDiplomado().isEmpty()){
                Collections.sort(entidadEnEdicion.getDipModulosDiplomado(),(o1,o2)->o1.getMdiNombre().compareTo(o2.getMdiNombre()));
            }
            
            totalResultados = entidadEnEdicion.getDipModulosDiplomado() != null ? Long.valueOf(entidadEnEdicion.getDipModulosDiplomado().size()) : 0L;

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        try {
            FiltroEscalaCalificacion fec = new FiltroEscalaCalificacion();
            fec.setEcaHabilitado(Boolean.TRUE);
            List<SgEscalaCalificacion> listaSectorEconomico = catalogoRestClient.buscarEscalaCalificacion(fec);
            comboEscalaCalificacion = new SofisComboG(listaSectorEconomico, "ecaNombre");
            comboEscalaCalificacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboEscalaCalificacion.ordenar();

            comboCalculoNotaInstitucional = new SofisComboG();
            comboCalculoNotaInstitucional.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            List<EnumFuncionRedondeo> funcionesRedondeo = new ArrayList(Arrays.asList(EnumFuncionRedondeo.values()));
            comboFuncionRedondeo = new SofisComboG(funcionesRedondeo, "text");
            comboFuncionRedondeo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void eleccionEscalaCalificacion() {
        comboCalculoNotaInstitucional = new SofisComboG();
        comboCalculoNotaInstitucional.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboFuncionRedondeo.setSelected(-1);
        esEscalaNumerica = Boolean.FALSE;
        if (comboEscalaCalificacion.getSelectedT() != null) {
            List<EnumCalculoNotaInstitucional> list = new ArrayList();
            list.add(EnumCalculoNotaInstitucional.MAY);
            list.add(EnumCalculoNotaInstitucional.ULT);
            if (comboEscalaCalificacion.getSelectedT().getEcaTipoEscala().equals(TipoEscalaCalificacion.NUMERICA)) {
                list.add(EnumCalculoNotaInstitucional.PROM);
                list.add(EnumCalculoNotaInstitucional.MED);
                esEscalaNumerica = Boolean.TRUE;
            }
            moduloDiplomadoEnEdicion.setMdiPrecision(null);
            comboCalculoNotaInstitucional = new SofisComboG(list, "text");
            comboCalculoNotaInstitucional.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        }

    }

    private void limpiarCombos() {
        this.comboEscalaCalificacion.setSelected(-1);
        comboCalculoNotaInstitucional.setSelected(-1);
        comboFuncionRedondeo.setSelected(-1);
    }

    public void limpiar() {
        filtro = new FiltroCodiguera();
        buscar();
    }

    public void agregar() {
        limpiarCombos();
        entidadEnEdicion = new SgDiplomado();
        JSFUtils.limpiarMensajesError();
    }

    public void agregarModuloDiplomado() {
        limpiarCombos();
        moduloDiplomadoEnEdicion = new SgModuloDiplomado();
        JSFUtils.limpiarMensajesError();
    }

    public void actualizar(Long id) {
        try {
            entidadEnEdicion = this.restClient.obtenerPorId(id);
            limpiarCombos();
            JSFUtils.limpiarMensajesError();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void actualizarModuloDiplomado(SgModuloDiplomado var) {
        try {
            moduloDiplomadoEnEdicion = (SgModuloDiplomado) SerializationUtils.clone(var);
            limpiarCombos();
             if (moduloDiplomadoEnEdicion.getMdiEscalaCalificacion() != null) {
                comboEscalaCalificacion.setSelectedT(moduloDiplomadoEnEdicion.getMdiEscalaCalificacion());
                esEscalaNumerica = TipoEscalaCalificacion.NUMERICA.equals(moduloDiplomadoEnEdicion.getMdiEscalaCalificacion().getEcaTipoEscala());
            }
             
            Integer precision = moduloDiplomadoEnEdicion.getMdiPrecision();
            eleccionEscalaCalificacion();
            moduloDiplomadoEnEdicion.setMdiPrecision(precision);
            if (moduloDiplomadoEnEdicion.getMdiCalculoNotaInstitucional() != null) {
                comboCalculoNotaInstitucional.setSelectedT(moduloDiplomadoEnEdicion.getMdiCalculoNotaInstitucional());
                comboFuncionRedondeo.setSelectedT(moduloDiplomadoEnEdicion.getMdiFuncionRedondeo());
            }
            JSFUtils.limpiarMensajesError();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void prepararParaEliminar(SgModuloDiplomado var) {
        try {
            limpiarCombos();
            moduloDiplomadoEnEdicion = (SgModuloDiplomado) SerializationUtils.clone(var);
            JSFUtils.limpiarMensajesError();

            FiltroCalificacionDiplomado fcd = new FiltroCalificacionDiplomado();
            fcd.setCalModuloDiplomadoFk(var.getMdiPk());
            fcd.setIncluirCampos(new String[]{"cadPk"});
            fcd.setMaxResults(1L);
            List<SgCalificacionDiplomado> calDip = calificacionDiplomadorestClient.buscar(fcd);
            
            if(calDip!=null && !calDip.isEmpty()){
                JSFUtils.agregarWarn("eliminarMsg", "ADVERTENCIA: El módulo diplomado que desea eliminar tiene calificaciones asociadas, de continuar las mismas serán eliminadas.", "");
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardar() {
        try {
            
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            totalResultados = Long.valueOf(0);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardarModuloDiplomado() {
        try {
            if (moduloDiplomadoEnEdicion != null) {
                moduloDiplomadoEnEdicion.setMdiEscalaCalificacion(comboEscalaCalificacion.getSelectedT());
                moduloDiplomadoEnEdicion.setMdiCalculoNotaInstitucional(comboCalculoNotaInstitucional.getSelectedT());
                moduloDiplomadoEnEdicion.setMdiFuncionRedondeo(EnumCalculoNotaInstitucional.PROM.equals(comboCalculoNotaInstitucional.getSelectedT()) ? comboFuncionRedondeo.getSelectedT() : null);
                moduloDiplomadoEnEdicion.setMdiDiplomado(entidadEnEdicion);
                moduloDiplomadoEnEdicion = modDiprestClient.guardar(moduloDiplomadoEnEdicion);
                entidadEnEdicion.getDipModulosDiplomado().add(moduloDiplomadoEnEdicion);

            }

            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscar();
            moduloDiplomadoEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminar() {
        try {
           
            modDiprestClient.eliminar(moduloDiplomadoEnEdicion.getMdiPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            buscar();
            moduloDiplomadoEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public String getNombrePagina() {
        if (entidadEnEdicion.getDipPk() == null) {
            return "Nuevo Diplomado";
        } else {
            return "Edición Diplomado";
        }

    }

    public FiltroCodiguera getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCodiguera filtro) {
        this.filtro = filtro;
    }

    public SgDiplomado getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgDiplomado entidadEnEdicion) {
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

    public LazyDiplomadoDataModel getDiplomadoLazyModel() {
        return diplomadoLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyDiplomadoDataModel diplomadoLazyModel) {
        this.diplomadoLazyModel = diplomadoLazyModel;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public Long getDiplomadoId() {
        return diplomadoId;
    }

    public void setDiplomadoId(Long diplomadoId) {
        this.diplomadoId = diplomadoId;
    }

    public SgModuloDiplomado getModuloDiplomadoEnEdicion() {
        return moduloDiplomadoEnEdicion;
    }

    public void setModuloDiplomadoEnEdicion(SgModuloDiplomado moduloDiplomadoEnEdicion) {
        this.moduloDiplomadoEnEdicion = moduloDiplomadoEnEdicion;
    }

    public SofisComboG<SgEscalaCalificacion> getComboEscalaCalificacion() {
        return comboEscalaCalificacion;
    }

    public void setComboEscalaCalificacion(SofisComboG<SgEscalaCalificacion> comboEscalaCalificacion) {
        this.comboEscalaCalificacion = comboEscalaCalificacion;
    }

    public SofisComboG<EnumCalculoNotaInstitucional> getComboCalculoNotaInstitucional() {
        return comboCalculoNotaInstitucional;
    }

    public void setComboCalculoNotaInstitucional(SofisComboG<EnumCalculoNotaInstitucional> comboCalculoNotaInstitucional) {
        this.comboCalculoNotaInstitucional = comboCalculoNotaInstitucional;
    }

    public Boolean getEsEscalaNumerica() {
        return esEscalaNumerica;
    }

    public void setEsEscalaNumerica(Boolean esEscalaNumerica) {
        this.esEscalaNumerica = esEscalaNumerica;
    }

    public SofisComboG<EnumFuncionRedondeo> getComboFuncionRedondeo() {
        return comboFuncionRedondeo;
    }

    public void setComboFuncionRedondeo(SofisComboG<EnumFuncionRedondeo> comboFuncionRedondeo) {
        this.comboFuncionRedondeo = comboFuncionRedondeo;
    }

}
