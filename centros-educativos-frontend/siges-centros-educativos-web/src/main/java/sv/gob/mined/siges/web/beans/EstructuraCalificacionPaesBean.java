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
import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesPaginas;
import sv.gob.mined.siges.web.dto.SgEstructuraCalificacionPaes;
import sv.gob.mined.siges.web.dto.SgEstudiante;
import sv.gob.mined.siges.web.enumerados.EnumEstadoArchivoCalificacionPAES;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEstructuraCalificacionPaes;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEstudiante;
import sv.gob.mined.siges.web.lazymodels.LazyEstructuraCalificacionPaesDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.EstructuraCalificacionPaesRestClient;
import sv.gob.mined.siges.web.restclient.EstudianteRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class EstructuraCalificacionPaesBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(EstructuraCalificacionPaesBean.class.getName());

    @Inject
    private EstructuraCalificacionPaesRestClient restClient;

    @Inject
    private EstudianteRestClient estudianteRestClient;

    @Inject
    private ApplicationBean appBean;

    private FiltroEstructuraCalificacionPaes filtro = new FiltroEstructuraCalificacionPaes();
    private SgEstructuraCalificacionPaes entidadEnEdicion = new SgEstructuraCalificacionPaes();
    private List<SgEstructuraCalificacionPaes> historialEstructuraCalificacionPaes = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private List<SgEstructuraCalificacionPaes> historial = new ArrayList();
    private LazyEstructuraCalificacionPaesDataModel estructuraCalificacionPaesLazyModel;
    private SofisComboG<EnumEstadoArchivoCalificacionPAES> estadoArchivoCalPaes;

    public EstructuraCalificacionPaesBean() {
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
            filtro.setFirst(new Long(0));
            filtro.setIncluirCampos(new String[]{"ecpPk", "ecpNie", "ecpEstado", "ecpCalificacion", "ecpCodigoCpe", "ecpResultado", "ecpUltModFecha", "ecpUltModUsuario",
                "ecpVersion", "ecpArchivoCalificacionPaesFk.acpPk",
                "ecpArchivoCalificacionPaesFk.acpEstadoArchivo",
                "ecpArchivoCalificacionPaesFk.acpResultado",
                "ecpArchivoCalificacionPaesFk.acpVersion",
                "ecpArchivoCalificacionPaesFk.acpAnio",
                "ecpPersonaFk.perPk",
                "ecpPersonaFk.perVersion",
                "ecpPersonaFk.perPrimerApellido",
                "ecpPersonaFk.perSegundoApellido",
                "ecpPersonaFk.perPrimerNombre",
                "ecpPersonaFk.perSegundoNombre",
                "ecpArchivoCalificacionPaesFk.acpArchivo.achPk",
                "ecpArchivoCalificacionPaesFk.acpArchivo.achVersion",
                "ecpArchivoCalificacionPaesFk.acpArchivo.achNombre",
                "ecpArchivoCalificacionPaesFk.acpArchivo.achDescripcion",
                "ecpArchivoCalificacionPaesFk.acpArchivo.achTmpPath",
                "ecpArchivoCalificacionPaesFk.acpArchivo.achContentType",
                "ecpArchivoCalificacionPaesFk.acpArchivo.achExt",
                "ecpArchivoCalificacionPaesFk.acpArchivo.achVersion"});

            filtro.setEstadoPaes(estadoArchivoCalPaes.getSelectedT());
            totalResultados = restClient.buscarTotal(filtro);
            estructuraCalificacionPaesLazyModel = new LazyEstructuraCalificacionPaesDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        List<EnumEstadoArchivoCalificacionPAES> funcionesRedondeo = new ArrayList(Arrays.asList(EnumEstadoArchivoCalificacionPAES.values()));
        estadoArchivoCalPaes = new SofisComboG(funcionesRedondeo, "text");
        estadoArchivoCalPaes.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
    }

    private void limpiarCombos() {
        estadoArchivoCalPaes.setSelected(-1);
    }

    public void limpiar() {
        limpiarCombos();
        filtro = new FiltroEstructuraCalificacionPaes();
        buscar();
    }

    public void agregar() {
        JSFUtils.limpiarMensajesError();
        entidadEnEdicion = new SgEstructuraCalificacionPaes();
    }

    public void actualizar(SgEstructuraCalificacionPaes var) {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgEstructuraCalificacionPaes) SerializationUtils.clone(var);
    }

    public String redirigirAReporte(SgEstructuraCalificacionPaes est) {
        try {
            if (!StringUtils.isBlank(est.getEcpNie())) {
                FiltroEstudiante fil = new FiltroEstudiante();
                fil.setNie(Long.parseLong(est.getEcpNie()));
                fil.setIncluirCampos(new String[]{"estUltimaMatricula.matSeccion.secPk"});
                List<SgEstudiante> estudiantes = estudianteRestClient.buscar(fil);
                
                if (!estudiantes.isEmpty()){
                    SgEstudiante e = estudiantes.get(0);
                    JSFUtils.redirectToPage(appBean.getReportGeneratorUrl() + ConstantesPaginas.REPORTE_CALIFICACIONES_ESTUDIANTES 
                            + "?seccionId=" + e.getEstUltimaMatricula().getMatSeccion().getSecPk() + "&estudianteId="+e.getEstPk());
                }
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void guardar() {
        try {
            if (entidadEnEdicion.getEcpPk() == null) {
                if (!StringUtils.isBlank(entidadEnEdicion.getEcpNie())) {
                    FiltroEstudiante fe = new FiltroEstudiante();
                    try {
                        fe.setNie(Long.parseLong(entidadEnEdicion.getEcpNie()));
                    } catch (NumberFormatException ex) {
                        JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, "NIE incorrecto", "");
                        return;
                    }
                    fe.setMaxResults(1L);
                    fe.setIncluirCampos(new String[]{"estPersona.perNie"});
                    Long cantidad = estudianteRestClient.buscarTotal(fe);
                    if (cantidad.compareTo(0L) == 0) {
                        JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, "NIE incorrecto", "");
                        return;
                    }
                }
            } else {
                entidadEnEdicion.getEcpArchivoCalificacionPaesFk().setAcpEstadoArchivo(EnumEstadoArchivoCalificacionPAES.SIN_PROCESAR);
            }

            entidadEnEdicion.setEcpEstado(EnumEstadoArchivoCalificacionPAES.SIN_PROCESAR);
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

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getEcpPk());
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

    public String cargarHistorial(Long id) {
        try {
            historial = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public List<SgEstructuraCalificacionPaes> getHistorial() {
        return historial;
    }

    public void setHistorial(List<SgEstructuraCalificacionPaes> historial) {
        this.historial = historial;
    }

    public FiltroEstructuraCalificacionPaes getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroEstructuraCalificacionPaes filtro) {
        this.filtro = filtro;
    }

    public SgEstructuraCalificacionPaes getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgEstructuraCalificacionPaes entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgEstructuraCalificacionPaes> getHistorialEstructuraCalificacionPaes() {
        return historialEstructuraCalificacionPaes;
    }

    public void setHistorialEstructuraCalificacionPaes(List<SgEstructuraCalificacionPaes> historialEstructuraCalificacionPaes) {
        this.historialEstructuraCalificacionPaes = historialEstructuraCalificacionPaes;
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

    public LazyEstructuraCalificacionPaesDataModel getEstructuraCalificacionPaesLazyModel() {
        return estructuraCalificacionPaesLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyEstructuraCalificacionPaesDataModel estructuraCalificacionPaesLazyModel) {
        this.estructuraCalificacionPaesLazyModel = estructuraCalificacionPaesLazyModel;
    }

    public SofisComboG<EnumEstadoArchivoCalificacionPAES> getEstadoArchivoCalPaes() {
        return estadoArchivoCalPaes;
    }

    public void setEstadoArchivoCalPaes(SofisComboG<EnumEstadoArchivoCalificacionPAES> estadoArchivoCalPaes) {
        this.estadoArchivoCalPaes = estadoArchivoCalPaes;
    }

}
