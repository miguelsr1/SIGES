/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgCalificacionesHistoricasEstudiante;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCalificacionesHistoricasEstudiante;
import sv.gob.mined.siges.web.lazymodels.LazyCalificacionesHistoricasEstudianteDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CalificacionesHistoricasEstudianteRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgAnioLectivo;
import sv.gob.mined.siges.web.dto.SgArchivo;
import sv.gob.mined.siges.web.dto.SgEstudiante;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgPlantilla;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAnioLectivo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEstudiante;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroNombreCompleto;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.restclient.AnioLectivoRestClient;
import sv.gob.mined.siges.web.restclient.EstudianteRestClient;
import sv.gob.mined.siges.web.restclient.PlantillaRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.FileUploadUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class CalificacionesHistoricasBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(CalificacionesHistoricasBean.class.getName());

    @Inject
    @ConfigProperty(name = "files.tmp.path")
    private String tmpBasePath;
    
    @Inject
    private CalificacionesHistoricasEstudianteRestClient restClient;

    @Inject
    private SedeRestClient sedeRestClient;
    @Inject
    private AnioLectivoRestClient anioLectRestClient;
    
    @Inject
    private EstudianteRestClient estudianteRestClient;
    
    @Inject
    private PlantillaRestClient plantillaRestClient;

    private FiltroCalificacionesHistoricasEstudiante filtro = new FiltroCalificacionesHistoricasEstudiante();
    private SgCalificacionesHistoricasEstudiante entidadEnEdicion = new SgCalificacionesHistoricasEstudiante();
    private List<RevHistorico> historialCalificacionesHistoricasEstudiante = new ArrayList();
    private Integer paginado = 100;
    private Long totalResultados=3000000L;
    private LazyCalificacionesHistoricasEstudianteDataModel calificacionesHistoricasEstudianteLazyModel;
    private FiltroNombreCompleto filtroNombreCompleto = new FiltroNombreCompleto();
    private Boolean sedeSiges = Boolean.FALSE;
    private Boolean sedeSigesInsert;
    private SgSede sedeSeleccionada;
    private Boolean soloLectura;
    private SofisComboG<SgAnioLectivo> anioLectivoCombo;
    private SofisComboG<SgAnioLectivo> anioLectivoComboInsert;
    private List<SgAnioLectivo> anioList;
    private Long nieEstudianteEditar;
    private SgEstudiante estudianteEditar;
    private Boolean edicionEstudiante;
    private Boolean preConfirmacionEdicionEstudiante;
    private SgArchivo importarCalificacion;
    private SgPlantilla plantillaImportacion;
    
    private String tamanioImportArchivo = "1048576";

    public CalificacionesHistoricasBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            obtenerPlantilla();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

        public void obtenerPlantilla() {
        try {        
                    plantillaImportacion = plantillaRestClient.obtenerPorCodigo(Constantes.PLANTILLA_IMPORTACION_CALIFICACIONES_HISTORICO);

        } catch (Exception ex) {
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void buscar() {
        try {
            filtro.setFirst(new Long(0));
            filtro.setAnio(anioLectivoCombo.getSelectedT() != null ? anioLectivoCombo.getSelectedT().getAleAnio() : null);
            filtro.setSedeSIGES(sedeSeleccionada != null ? sedeSeleccionada.getSedPk() : null);
            
            filtro.setIncluirCampos(new String[]{"chePk"});
            filtro.setMaxResults(1000L);
            List<SgCalificacionesHistoricasEstudiante> l=restClient.buscar(filtro);
            
            if(l.isEmpty()){
                calificacionesHistoricasEstudianteLazyModel =null;
                   JSFUtils.agregarWarn("eliminarMsg", "No hay resultados para los filtros indicados", "");
                return;
            }else{
                if(l.size()<1000){
                    Integer cantidad=l.size();
                    totalResultados=Long.valueOf(cantidad);
                }else{
                    totalResultados=3000000L;
                }
            }
        
            filtro.setMaxResults(null);
            filtro.setIncluirCampos(new String[]{"cheMatriculaExternaId",
                "cheAnioMatricula",
                "chePersonaFk.perPk",
                "cheEstudianteFk.estPk",
                "chePersonaFk.perVersion",
                "chePersonaFk.perPrimerNombre",
                "chePersonaFk.perSegundoNombre",
                "chePersonaFk.perPrimerApellido",
                "chePersonaFk.perSegundoApellido",
                "chePersonaFk.perNie",
                "cheSedeFk.sedPk",
                "cheSedeFk.sedVersion",
                "cheSedeFk.sedCodigo",
                "cheSedeFk.sedNombre",
                "cheSedeFk.sedTipo",
                "cheSedeExternaCodigo",
                "cheSedeExternaNombre",
                "cheServicioEducativoExternoEtiquetaImpresion",
                "chePlanEstudioExternoDescripcion",
                "cheNF",
                "cheComponente",
                "cheUltModFecha",
                "cheUltModUsuario",
                "cheVersion",
                "cheEvaluacionExternaId"});
            filtro.setOrderBy(new String[]{"cheAnioMatricula"});
            filtro.setAscending(new boolean[]{true});
            
            
            

           // totalResultados = restClient.buscarTotal(filtro);
            calificacionesHistoricasEstudianteLazyModel = new LazyCalificacionesHistoricasEstudianteDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void prepararBusquedaEstudiante(){
        estudianteEditar=null;
        nieEstudianteEditar=null;
        edicionEstudiante=Boolean.TRUE;
        entidadEnEdicion.setModificarCalificacionesEstudiante(Boolean.FALSE);
        preConfirmacionEdicionEstudiante=Boolean.FALSE;
        
    }
    
    public void buscarEstudiante(){
        try{
            
            preConfirmacionEdicionEstudiante=Boolean.FALSE;
            estudianteEditar=null;
            if(nieEstudianteEditar!=null){
                FiltroEstudiante fe=new FiltroEstudiante();
                fe.setNie(nieEstudianteEditar);
                fe.setIncluirCampos(new String[]{"estPk","estVersion","estPersona.perPk","estPersona.perNie",
                    "estPersona.perSexo.sexNombre",
                    "estPersona.perFechaNacimiento",
                    "estPersona.perPrimerNombre",
                    "estPersona.perPrimerApellido",
                    "estPersona.perSegundoNombre",
                    "estPersona.perSegundoApellido",
                    "estPersona.perVersion"});
                List<SgEstudiante> listEstudiante=estudianteRestClient.buscar(fe);
                
                if(listEstudiante!=null && !listEstudiante.isEmpty()){
                    estudianteEditar=listEstudiante.get(0);
                }else{
                    JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, "No existen estudiante para el NIE seleccionado", "");
                }
            }else{
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, "Debe completar NIE", "");
            }
        
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void preConfirmacionEstudiante(){
        preConfirmacionEdicionEstudiante=Boolean.TRUE;
    }
    
    public void confirmarEdicionEstudiante(){
        try{
            if(entidadEnEdicion.getChePk()!=null && estudianteEditar!=null){
                entidadEnEdicion.setNuevoEstudiante(estudianteEditar);
                restClient.editarEstudiante(entidadEnEdicion);
                PrimeFaces.current().executeScript("PF('itemDialog').hide()");
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
                buscar();
            }
            
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }


    public void sedeSigesSeleccionada() {
        filtro.setSedeExternaCodigo(null);
        filtro.setSedeExternaNombre(null);
        sedeSeleccionada = null;
    }

    public void sedeSigesSeleccionadaInsert() {
        entidadEnEdicion.setCheSedeFk(null);
        entidadEnEdicion.setCheSedeExternaCodigo(null);
        entidadEnEdicion.setCheSedeExternaNombre(null);
        sedeSeleccionada = null;
    }

    public List<SgSede> completeSede(String query) {
        try {
            FiltroSedes fil = new FiltroSedes();
            fil.setSecurityOperation(ConstantesOperaciones.BUSCAR_SEDES);
            fil.setSedCodigoONombre(query);
            fil.setMaxResults(11L);
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

    public void cargarCombos() {
        try {
            FiltroAnioLectivo fal = new FiltroAnioLectivo();
            fal.setOrderBy(new String[]{"aleAnio"});
            fal.setAscending(new boolean[]{false});
            List<SgAnioLectivo> listAnio = anioLectRestClient.buscar(fal);
            listAnio.removeIf(c -> c.getAleAnio() > 2019);
            anioList=listAnio;
            anioLectivoCombo = new SofisComboG(anioList, "aleAnio");
            anioLectivoCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            anioLectivoComboInsert = new SofisComboG(anioList, "aleAnio");
            anioLectivoComboInsert.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {

    }

    public void limpiar() {
        filtroNombreCompleto = new FiltroNombreCompleto();
        sedeSeleccionada = null;
        filtro = new FiltroCalificacionesHistoricasEstudiante();
        anioLectivoCombo = new SofisComboG(anioList, "aleAnio");
        anioLectivoCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        anioLectivoCombo.setSelected(-1);
        calificacionesHistoricasEstudianteLazyModel =null;
    }

    public void agregar() {
        sedeSigesInsert = Boolean.FALSE;
        soloLectura = Boolean.FALSE;
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgCalificacionesHistoricasEstudiante();
    }

    public void actualizar(SgCalificacionesHistoricasEstudiante var, Boolean soloLectura) {
        try {
            
            edicionEstudiante=Boolean.FALSE;
            sedeSigesInsert = Boolean.FALSE;
            this.soloLectura = soloLectura;
            JSFUtils.limpiarMensajesError();
            limpiarCombos();
            entidadEnEdicion = restClient.obtenerPorId(var.getChePk());
            if (entidadEnEdicion.getCheSedeFk() != null) {
                sedeSigesInsert = Boolean.TRUE;
            }
            if (!StringUtils.isNumeric(entidadEnEdicion.getCheNF())) {
                entidadEnEdicion.setCheNF(null);
            }
            anioLectivoComboInsert.setSelected(-1);
            if(entidadEnEdicion.getCheAnioMatricula()!=null){
                List<SgAnioLectivo> list= anioLectivoComboInsert.getAllTs();
                List<SgAnioLectivo> lis=list.stream().filter(c->entidadEnEdicion.getCheAnioMatricula().equals(c.getAleAnio())).collect(Collectors.toList());
                if(!lis.isEmpty()){
                    anioLectivoComboInsert.setSelectedT(lis.get(0));
                }
            }
           
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardar() {
        try {
            entidadEnEdicion.setCheAnioMatricula(anioLectivoComboInsert.getSelectedT()!=null?anioLectivoComboInsert.getSelectedT().getAleAnio():null);            
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
            restClient.eliminar(entidadEnEdicion.getChePk());
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
            historialCalificacionesHistoricasEstudiante = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void prepararImportacionCalificaciones(){
        importarCalificacion=null;
    }
    
     public void handleFileUpload(FileUploadEvent event) {      
        importarCalificacion=new SgArchivo();
        FileUploadUtils.handleFileUpload(event.getFile(), importarCalificacion, tmpBasePath);
    }
     
    public void calificarConArchivo(){
        try{
            restClient.calificarConArchivo(importarCalificacion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");        
            PrimeFaces.current().executeScript("PF('itemDialogArchivo').hide()");
        } catch (BusinessException be) {
            PrimeFaces.current().executeScript("PF('itemDialogArchivo').hide()");
            JSFUtils.agregarMensajesError(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            PrimeFaces.current().executeScript("PF('itemDialogArchivo').hide()");
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

    public List<RevHistorico> getHistorialCalificacionesHistoricasEstudiante() {
        return historialCalificacionesHistoricasEstudiante;
    }

    public void setHistorialCalificacionesHistoricasEstudiante(List<RevHistorico> historialCalificacionesHistoricasEstudiante) {
        this.historialCalificacionesHistoricasEstudiante = historialCalificacionesHistoricasEstudiante;
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

    public LazyCalificacionesHistoricasEstudianteDataModel getCalificacionesHistoricasEstudianteLazyModel() {
        return calificacionesHistoricasEstudianteLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyCalificacionesHistoricasEstudianteDataModel calificacionesHistoricasEstudianteLazyModel) {
        this.calificacionesHistoricasEstudianteLazyModel = calificacionesHistoricasEstudianteLazyModel;
    }

    public FiltroNombreCompleto getFiltroNombreCompleto() {
        return filtroNombreCompleto;
    }

    public void setFiltroNombreCompleto(FiltroNombreCompleto filtroNombreCompleto) {
        this.filtroNombreCompleto = filtroNombreCompleto;
    }

    public Boolean getSedeSiges() {
        return sedeSiges;
    }

    public void setSedeSiges(Boolean sedeSiges) {
        this.sedeSiges = sedeSiges;
    }

    public SgSede getSedeSeleccionada() {
        return sedeSeleccionada;
    }

    public void setSedeSeleccionada(SgSede sedeSeleccionada) {
        this.sedeSeleccionada = sedeSeleccionada;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public Boolean getSedeSigesInsert() {
        return sedeSigesInsert;
    }

    public void setSedeSigesInsert(Boolean sedeSigesInsert) {
        this.sedeSigesInsert = sedeSigesInsert;
    }

    public SofisComboG<SgAnioLectivo> getAnioLectivoCombo() {
        return anioLectivoCombo;
    }

    public void setAnioLectivoCombo(SofisComboG<SgAnioLectivo> anioLectivoCombo) {
        this.anioLectivoCombo = anioLectivoCombo;
    }

    public SofisComboG<SgAnioLectivo> getAnioLectivoComboInsert() {
        return anioLectivoComboInsert;
    }

    public void setAnioLectivoComboInsert(SofisComboG<SgAnioLectivo> anioLectivoComboInsert) {
        this.anioLectivoComboInsert = anioLectivoComboInsert;
    }

    public Long getNieEstudianteEditar() {
        return nieEstudianteEditar;
    }

    public void setNieEstudianteEditar(Long nieEstudianteEditar) {
        this.nieEstudianteEditar = nieEstudianteEditar;
    }

    public SgEstudiante getEstudianteEditar() {
        return estudianteEditar;
    }

    public void setEstudianteEditar(SgEstudiante estudianteEditar) {
        this.estudianteEditar = estudianteEditar;
    }

    public Boolean getEdicionEstudiante() {
        return edicionEstudiante;
    }

    public void setEdicionEstudiante(Boolean edicionEstudiante) {
        this.edicionEstudiante = edicionEstudiante;
    }

    public Boolean getPreConfirmacionEdicionEstudiante() {
        return preConfirmacionEdicionEstudiante;
    }

    public void setPreConfirmacionEdicionEstudiante(Boolean preConfirmacionEdicionEstudiante) {
        this.preConfirmacionEdicionEstudiante = preConfirmacionEdicionEstudiante;
    }

    public SgArchivo getImportarCalificacion() {
        return importarCalificacion;
    }

    public void setImportarCalificacion(SgArchivo importarCalificacion) {
        this.importarCalificacion = importarCalificacion;
    }

    public String getTamanioImportArchivo() {
        return tamanioImportArchivo;
    }

    public void setTamanioImportArchivo(String tamanioImportArchivo) {
        this.tamanioImportArchivo = tamanioImportArchivo;
    }

    public SgPlantilla getPlantillaImportacion() {
        return plantillaImportacion;
    }

    public void setPlantillaImportacion(SgPlantilla plantillaImportacion) {
        this.plantillaImportacion = plantillaImportacion;
    }

}
