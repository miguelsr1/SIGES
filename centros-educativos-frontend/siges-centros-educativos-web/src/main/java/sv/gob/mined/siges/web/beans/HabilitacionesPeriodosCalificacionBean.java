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
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgHabilitacionPeriodoCalificacion;
import sv.gob.mined.siges.web.dto.SgRangoFecha;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.enumerados.EnumEstadoHabilitacionPeriodoCalificacion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroHabilitacionPeriodoCalificacion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.lazymodels.LazyHabilitacionPeriodoCalificacionDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.HabilitacionPeriodoCalificacionRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class HabilitacionesPeriodosCalificacionBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(HabilitacionesPeriodosCalificacionBean.class.getName());

    @Inject
    private HabilitacionPeriodoCalificacionRestClient restClient;

    @Inject
    private CatalogosRestClient catalogoRestClient;

    @Inject
    private SedeRestClient sedeRestClient;

    @Inject
    private SessionBean sessionBean;

    private FiltroHabilitacionPeriodoCalificacion filtro = new FiltroHabilitacionPeriodoCalificacion();
    private SgHabilitacionPeriodoCalificacion entidadEnEdicion = new SgHabilitacionPeriodoCalificacion();
    private List<RevHistorico> historialHabilitacionPeriodoCalificacion = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyHabilitacionPeriodoCalificacionDataModel habilitacionPeriodoCalificacionLazyModel;
    private SofisComboG<SgDepartamento> comboDepartamento;
    private SofisComboG<EnumEstadoHabilitacionPeriodoCalificacion> comboEstado;
    private SgSede sedeActividadSeleccionadaBusqueda;
    private Long estudianteNie;
    private List<SgRangoFecha> rangoFechas;
    private Boolean soloLectura;

    public HabilitacionesPeriodosCalificacionBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            buscar();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void buscar() {
        try {
            filtro.setIncluirCampos(new String[]{"hpcMatriculaFk.matSeccion.secServicioEducativo.sduSede.sedPk",
                "hpcMatriculaFk.matSeccion.secServicioEducativo.sduSede.sedTipo",
                "hpcMatriculaFk.matSeccion.secServicioEducativo.sduSede.sedCodigo",
                "hpcMatriculaFk.matSeccion.secServicioEducativo.sduSede.sedNombre",
                "hpcMatriculaFk.matSeccion.secServicioEducativo.sduSede.sedTelefono",
                "hpcMatriculaFk.matSeccion.secServicioEducativo.sduSede.sedDireccion.dirDepartamento.depNombre",
                "hpcMatriculaFk.matSeccion.secServicioEducativo.sduSede.sedDireccion.dirMunicipio.munNombre",
                "hpcFechaSolicitud",
                "hpcEstado",
                "hpcUltModUsuario",
                "hpcUltModFecha",
                "hpcObservacion",
                "hpcEstudianteFk.estPk",
                "hpcEstudianteFk.estVersion",
                "hpcEstudianteFk.estPersona.perPk",
                "hpcEstudianteFk.estPersona.perVersion",
                "hpcEstudianteFk.estPersona.perNie",
                "hpcEstudianteFk.estPersona.perPrimerNombre",
                "hpcEstudianteFk.estPersona.perSegundoNombre",
                "hpcEstudianteFk.estPersona.perPrimerApellido",
                "hpcEstudianteFk.estPersona.perSegundoApellido",
                "hpcObservacionAprobacionRechazo",
                "hpcVersion"
            });
            filtro.setHpcDepartamentoFk(comboDepartamento.getSelectedT() != null ? comboDepartamento.getSelectedT().getDepPk() : null);
            filtro.setHpcEstado(comboEstado.getSelectedT());
            filtro.setHpcSedeFk(sedeActividadSeleccionadaBusqueda != null ? sedeActividadSeleccionadaBusqueda.getSedPk() : null);
            filtro.setFirst(new Long(0));
            totalResultados = restClient.buscarTotal(filtro);
            habilitacionPeriodoCalificacionLazyModel = new LazyHabilitacionPeriodoCalificacionDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});
            fc.setOrderBy(new String[]{"depNombre"});
            fc.setIncluirCampos(new String[]{"depNombre", "depVersion"});
            List<SgDepartamento> listaDepartamentos = catalogoRestClient.buscarDepartamento(fc);
            comboDepartamento = new SofisComboG<>(listaDepartamentos, "depNombre");
            comboDepartamento.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            List<EnumEstadoHabilitacionPeriodoCalificacion> estados = new ArrayList(Arrays.asList(EnumEstadoHabilitacionPeriodoCalificacion.values()));
            comboEstado = new SofisComboG(estados, "text");
            comboEstado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
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
            return sedeRestClient.buscar(fil);
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ce), ce);
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }



    private void limpiarCombos() {
        comboDepartamento.setSelected(-1);
        comboEstado.setSelected(-1);
    }

    public void limpiar() {
        filtro = new FiltroHabilitacionPeriodoCalificacion();
        sedeActividadSeleccionadaBusqueda = null;
        limpiarCombos();
        buscar();
    }

    public void agregar() {
        soloLectura=Boolean.FALSE;
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgHabilitacionPeriodoCalificacion();
        rangoFechas = new ArrayList();
        estudianteNie=null;
    }

    public void actualizar(SgHabilitacionPeriodoCalificacion var) {
        try {
            soloLectura=Boolean.FALSE;
            JSFUtils.limpiarMensajesError();
            limpiarCombos();
            entidadEnEdicion = restClient.obtenerPorId(var.getHpcPk());
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    
    public void prepararParaEliminar(SgHabilitacionPeriodoCalificacion var) {      
           entidadEnEdicion = (SgHabilitacionPeriodoCalificacion) SerializationUtils.clone(var);

    }
    
    public String tituloDialog(){
        if(BooleanUtils.isFalse(soloLectura) && EnumEstadoHabilitacionPeriodoCalificacion.PENDIENTE.equals(entidadEnEdicion.getHpcEstado())){
            return Etiquetas.getValue("evaluacionSolicitudHabilitacion");
        }
        return Etiquetas.getValue("habilitacionPeriodoCalificacion");
    }
    
    public void ver(SgHabilitacionPeriodoCalificacion var) {
        try {
            soloLectura=Boolean.TRUE;
            JSFUtils.limpiarMensajesError();
            entidadEnEdicion = restClient.obtenerPorId(var.getHpcPk());
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardar() {
        try {
           
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscar();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void aprobar() {
        try {
            entidadEnEdicion = restClient.aprobar(entidadEnEdicion);
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.APROBADO_CORRECTO), "");
            buscar();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void rechazar() {
        try {
            entidadEnEdicion = restClient.rechazar(entidadEnEdicion);
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.RECHAZADO_CORRECTO), "");
            buscar();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }


    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getHpcPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            buscar();
            entidadEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void historial(Long id) {
        try {
            historialHabilitacionPeriodoCalificacion = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroHabilitacionPeriodoCalificacion getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroHabilitacionPeriodoCalificacion filtro) {
        this.filtro = filtro;
    }

    public SgHabilitacionPeriodoCalificacion getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgHabilitacionPeriodoCalificacion entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<RevHistorico> getHistorialHabilitacionPeriodoCalificacion() {
        return historialHabilitacionPeriodoCalificacion;
    }

    public void setHistorialHabilitacionPeriodoCalificacion(List<RevHistorico> historialHabilitacionPeriodoCalificacion) {
        this.historialHabilitacionPeriodoCalificacion = historialHabilitacionPeriodoCalificacion;
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

    public LazyHabilitacionPeriodoCalificacionDataModel getHabilitacionPeriodoCalificacionLazyModel() {
        return habilitacionPeriodoCalificacionLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyHabilitacionPeriodoCalificacionDataModel habilitacionPeriodoCalificacionLazyModel) {
        this.habilitacionPeriodoCalificacionLazyModel = habilitacionPeriodoCalificacionLazyModel;
    }

    public SofisComboG<SgDepartamento> getComboDepartamento() {
        return comboDepartamento;
    }

    public void setComboDepartamento(SofisComboG<SgDepartamento> comboDepartamento) {
        this.comboDepartamento = comboDepartamento;
    }

    public SgSede getSedeActividadSeleccionadaBusqueda() {
        return sedeActividadSeleccionadaBusqueda;
    }

    public void setSedeActividadSeleccionadaBusqueda(SgSede sedeActividadSeleccionadaBusqueda) {
        this.sedeActividadSeleccionadaBusqueda = sedeActividadSeleccionadaBusqueda;
    }

    public SofisComboG<EnumEstadoHabilitacionPeriodoCalificacion> getComboEstado() {
        return comboEstado;
    }

    public void setComboEstado(SofisComboG<EnumEstadoHabilitacionPeriodoCalificacion> comboEstado) {
        this.comboEstado = comboEstado;
    }

    public Long getEstudianteNie() {
        return estudianteNie;
    }

    public void setEstudianteNie(Long estudianteNie) {
        this.estudianteNie = estudianteNie;
    }

    public List<SgRangoFecha> getRangoFechas() {
        return rangoFechas;
    }

    public void setRangoFechas(List<SgRangoFecha> rangoFechas) {
        this.rangoFechas = rangoFechas;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

}
