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
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgArchivo;
import sv.gob.mined.siges.web.dto.SgCalificacionesHistoricasEstudiante;
import sv.gob.mined.siges.web.dto.SgEstudiante;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCalificacionesHistoricasEstudiante;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CalificacionesHistoricasEstudianteRestClient;
import sv.gob.mined.siges.web.restclient.EstudianteRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class CalificacionesHistoricasEstudianteBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(CalificacionesHistoricasEstudianteBean.class.getName());

    @Inject
    private CalificacionesHistoricasEstudianteRestClient restClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private EstudianteRestClient estudianteRestClient;
    
    @Inject
    private HandleArchivoBean handleArchivoBean;

    private FiltroCalificacionesHistoricasEstudiante filtro = new FiltroCalificacionesHistoricasEstudiante();
    private SgCalificacionesHistoricasEstudiante entidadEnEdicion = new SgCalificacionesHistoricasEstudiante();
    private Integer paginado = 10;
    private Long totalResultados;
    private Long estudianteNie;
    private List<SgCalificacionesHistoricasEstudiante> listCalificacionHistorica;
    private List<SelectItem> comboAnios;
    private Integer anioSeleccionado;
    private List<SelectItem> comboComponente;
    private String componenteSeleccionado;
    private List<SgCalificacionesHistoricasEstudiante> listCalificacionModificar;
    private SgEstudiante estudianteEnEdicion;

    public CalificacionesHistoricasEstudianteBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarCombos();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public Boolean buscarCalificaciones() {
        try {
            if (estudianteNie != null) {
                    filtro.setEstNie(estudianteNie);
                    filtro.setIncluirCampos(new String[]{"cheMatriculaExternaId",
                        "cheAnioMatricula",
                        "cheObservacion",
                        "cheFechaRealizado",
                        "cheProcesoDeCreacion",
                        "cheEstudianteFk.estPk",
                        "cheEstudianteFk.estVersion",
                        "cheEstudianteFk.estPersona.perPk",
                        "cheEstudianteFk.estPersona.perVersion",
                        "cheEstudianteFk.estPersona.perPrimerNombre",
                        "cheEstudianteFk.estPersona.perSegundoNombre",
                        "cheEstudianteFk.estPersona.perPrimerApellido",
                        "cheEstudianteFk.estPersona.perSegundoApellido",
                        "cheEstudianteFk.estPersona.perNie",
                        "chePersonaFk.perPk",
                        "chePersonaFk.perVersion",
                        "cheEstudianteNie",
                        "cheSedeFk.sedPk",
                        "cheSedeFk.sedVersion",
                        "cheSedeFk.sedCodigo",
                        "cheSedeFk.sedNombre",
                        "cheSedeFk.sedTipo",
                        "cheSedeFk.sedTelefono",
                        "cheSedeExternaCodigo",
                        "cheSedeExternaNombre",
                        "cheServicioEducativoExternoDescripcion",
                        "cheServicioEducativoExternoEtiquetaImpresion",
                        "chePlanEstudioExternoId",
                        "chePlanEstudioExternoDescripcion",
                        "cheComponente",
                        "chePI",
                        "cheNFI",
                        "cheNPAESI",
                        "cheRIX",
                        "cheRIR",
                        "cheRIRE",
                        "cheNF",
                        "cheNPAES",
                        "chePIModif",
                        "chePI_r",
                        "chePIR",
                        "cheUltModFecha",
                        "cheUltModUsuario",
                        "cheObservacionEdicion",
                        "cheVersion", "cheEvaluacionExternaId",
                        "cheServicioEducativoExternoId"});
                    filtro.setOrderBy(new String[]{"cheAnioMatricula"});
                    filtro.setAscending(new boolean[]{true});
                    listCalificacionHistorica = restClient.buscar(filtro);
                    return Boolean.TRUE;
                
            } else {
                    JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_TEMPLATE, "Debe ingresar NIE.", "");
                    return Boolean.FALSE;
                }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return Boolean.TRUE;
    }
    
    public void handlePartidaFileUpload(FileUploadEvent event) {
        if (entidadEnEdicion.getCheArchivoEdicion()== null) {
            entidadEnEdicion.setCheArchivoEdicion(new SgArchivo());
        }
        handleArchivoBean.subirArchivoTmp(event.getFile(), entidadEnEdicion.getCheArchivoEdicion());
    }

    public String getUrlReporte() {
        String url = null;
        if (estudianteEnEdicion != null) {
            url = "?estudianteId=" + estudianteEnEdicion.getEstPk();
            if (anioSeleccionado != null) {
                url += "&anio=" + anioSeleccionado;
            }
        }
        return url;
    }

    public void buscarEstudiantePorNIE() {
        try {
            estudianteEnEdicion = null;
            comboAnios = new ArrayList();
            comboAnios.add(new SelectItem(null, Etiquetas.getValue("comboEmptyItem")));
            anioSeleccionado = null;
            comboComponente = new ArrayList();
            comboComponente.add(new SelectItem(null, Etiquetas.getValue("comboEmptyItem")));
            componenteSeleccionado = null;
            listCalificacionHistorica = new ArrayList();
            listCalificacionModificar = new ArrayList();
            Boolean ret = buscarCalificaciones();

            if (BooleanUtils.isTrue(ret)) {
                if (!listCalificacionHistorica.isEmpty()) {
                    List<Integer> anios = listCalificacionHistorica.stream().map(c -> c.getCheAnioMatricula()).distinct().collect(Collectors.toList());
                    Collections.sort(anios);
                    for (Integer anio : anios) {
                        comboAnios.add(new SelectItem(anio));
                    }

                    estudianteEnEdicion = listCalificacionHistorica.get(0).getCheEstudianteFk();
                    listCalificacionModificar = new ArrayList(listCalificacionHistorica);
                    for (SgCalificacionesHistoricasEstudiante cal : listCalificacionModificar) {
                        if (!StringUtils.isNumeric(cal.getCheNF())) {
                            cal.setCheNF(null);
                        }
                    }
                } else {
                    if (estudianteNie != null) {
                        JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_TEMPLATE, "No se encontraron calificaciones para el estudiante seleccionado", "");
                    }
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarAnio() {
        try {
            comboComponente = new ArrayList();
            comboComponente.add(new SelectItem(null, Etiquetas.getValue("comboEmptyItem")));
            componenteSeleccionado = null;
            if (anioSeleccionado != null) {
                List<String> componentes = listCalificacionHistorica.stream().filter(c -> c.getCheAnioMatricula().equals(anioSeleccionado)).map(c -> c.getCheComponente()).distinct().collect(Collectors.toList());
                Collections.sort(componentes);
                for (String comp : componentes) {
                    comboComponente.add(new SelectItem(comp));
                }
                listCalificacionModificar = new ArrayList(listCalificacionHistorica.stream().filter(c -> c.getCheAnioMatricula().equals(anioSeleccionado)).collect(Collectors.toList()));
                for (SgCalificacionesHistoricasEstudiante cal : listCalificacionModificar) {
                    if (!StringUtils.isNumeric(cal.getCheNF())) {
                        cal.setCheNF(null);
                    }
                }
            } else {
                listCalificacionModificar = new ArrayList(listCalificacionHistorica);
                for (SgCalificacionesHistoricasEstudiante cal : listCalificacionModificar) {
                    if (!StringUtils.isNumeric(cal.getCheNF())) {
                        cal.setCheNF(null);
                    }
                }
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarComponente() {
        if (componenteSeleccionado != null) {
            listCalificacionModificar = new ArrayList(listCalificacionHistorica.stream().filter(c -> c.getCheAnioMatricula().equals(anioSeleccionado) && c.getCheComponente().equals(componenteSeleccionado)).collect(Collectors.toList()));
            for (SgCalificacionesHistoricasEstudiante cal : listCalificacionModificar) {
                if (!StringUtils.isNumeric(cal.getCheNF())) {
                    cal.setCheNF(null);
                }
            }
        } else {
            seleccionarAnio();
        }

    }

    public void cargarCombos() {
        estudianteEnEdicion = null;
        comboAnios = new ArrayList();
        comboAnios.add(new SelectItem(null, Etiquetas.getValue("comboEmptyItem")));
        anioSeleccionado = null;
        comboComponente = new ArrayList();
        comboComponente.add(new SelectItem(null, Etiquetas.getValue("comboEmptyItem")));
        componenteSeleccionado = null;
        listCalificacionHistorica = new ArrayList();
        listCalificacionModificar = new ArrayList();
    }

    private void limpiarCombos() {

    }

    public void limpiar() {
        filtro = new FiltroCalificacionesHistoricasEstudiante();
    }

    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgCalificacionesHistoricasEstudiante();
    }

    public void actualizar(SgCalificacionesHistoricasEstudiante var) {
        try{
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion =restClient.obtenerPorId(var.getChePk());
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void guardar() {
        try {
            if(StringUtils.isBlank(entidadEnEdicion.getCheObservacionEdicion())){
                if(StringUtils.isBlank(entidadEnEdicion.getCheNF())){
                    JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, "Debe completar la nota final.", "");
                }
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, "Debe completar la observación.", "");
                return;
            }
                     
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscarCalificaciones();
            seleccionarComponente();
            entidadEnEdicion =null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }


    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getChePk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");

            entidadEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroCalificacionesHistoricasEstudiante getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCalificacionesHistoricasEstudiante filtro) {
        this.filtro = filtro;
    }

    public SgCalificacionesHistoricasEstudiante getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgCalificacionesHistoricasEstudiante entidadEnEdicion) {
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

    public Long getEstudianteNie() {
        return estudianteNie;
    }

    public void setEstudianteNie(Long estudianteNie) {
        this.estudianteNie = estudianteNie;
    }

    public List<SgCalificacionesHistoricasEstudiante> getListCalificacionHistorica() {
        return listCalificacionHistorica;
    }

    public void setListCalificacionHistorica(List<SgCalificacionesHistoricasEstudiante> listCalificacionHistorica) {
        this.listCalificacionHistorica = listCalificacionHistorica;
    }

    public List<SelectItem> getComboAnios() {
        return comboAnios;
    }

    public void setComboAnios(List<SelectItem> comboAnios) {
        this.comboAnios = comboAnios;
    }

    public Integer getAnioSeleccionado() {
        return anioSeleccionado;
    }

    public void setAnioSeleccionado(Integer anioSeleccionado) {
        this.anioSeleccionado = anioSeleccionado;
    }

    public List<SelectItem> getComboComponente() {
        return comboComponente;
    }

    public void setComboComponente(List<SelectItem> comboComponente) {
        this.comboComponente = comboComponente;
    }

    public String getComponenteSeleccionado() {
        return componenteSeleccionado;
    }

    public void setComponenteSeleccionado(String componenteSeleccionado) {
        this.componenteSeleccionado = componenteSeleccionado;
    }

    public List<SgCalificacionesHistoricasEstudiante> getListCalificacionModificar() {
        return listCalificacionModificar;
    }

    public void setListCalificacionModificar(List<SgCalificacionesHistoricasEstudiante> listCalificacionModificar) {
        this.listCalificacionModificar = listCalificacionModificar;
    }

    public SgEstudiante getEstudianteEnEdicion() {
        return estudianteEnEdicion;
    }

    public void setEstudianteEnEdicion(SgEstudiante estudianteEnEdicion) {
        this.estudianteEnEdicion = estudianteEnEdicion;
    }

}
