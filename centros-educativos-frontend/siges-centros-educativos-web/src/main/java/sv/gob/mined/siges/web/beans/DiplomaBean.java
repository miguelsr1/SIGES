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
import org.apache.commons.lang3.SerializationUtils;
import org.omnifaces.cdi.Param;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgAnioLectivo;
import sv.gob.mined.siges.web.dto.SgDiploma;
import sv.gob.mined.siges.web.dto.SgDiplomaEstudiante;
import sv.gob.mined.siges.web.dto.SgDiplomado;
import sv.gob.mined.siges.web.dto.SgDiplomadosEstudiante;
import sv.gob.mined.siges.web.dto.SgEstudiante;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.enumerados.EnumAnioLectivoEstado;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAnioLectivo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDiploma;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDiplomado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDiplomadosEstudiante;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AnioLectivoRestClient;
import sv.gob.mined.siges.web.restclient.CalificacionDiplomadoEstudianteRestClient;
import sv.gob.mined.siges.web.restclient.CalificacionDiplomadoRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.DiplomaRestClient;
import sv.gob.mined.siges.web.restclient.DiplomadoRestClient;
import sv.gob.mined.siges.web.restclient.DiplomadosEstudianteRestClient;
import sv.gob.mined.siges.web.restclient.EstudianteRestClient;
import sv.gob.mined.siges.web.restclient.ModuloDiplomadoRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class DiplomaBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(DiplomaBean.class.getName());

    @Inject
    private DiplomaRestClient restClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private DiplomadoRestClient restDiplomado;

    @Inject
    private ModuloDiplomadoRestClient restModuloDiplomado;

    @Inject
    private SedeRestClient restSedeRestClient;

    @Inject
    private AnioLectivoRestClient anioLectivoRestClient;

    @Inject
    private EstudianteRestClient estudianteRestClient;

    @Inject
    private DiplomadosEstudianteRestClient diplomadoEstudianteRestClient;

    @Inject
    private CalificacionDiplomadoEstudianteRestClient calificacionDiplomadoEstudianteRestClient;

    @Inject
    private CalificacionDiplomadoRestClient calificacionDiplomadoRestClient;

    @Inject
    private CatalogosRestClient catalogoRestClient;

    private FiltroDiploma filtro = new FiltroDiploma();
    private SgDiploma entidadEnEdicion = new SgDiploma();
    private SofisComboG<SgDiplomado> comboDiplomado;
    private SgSede sedeSeleccionada;
    private SofisComboG<SgAnioLectivo> comboAnioLectivo;

    private SgAnioLectivo anioLectivoEnEdicion;
    private Boolean soloLectura = Boolean.FALSE;
    private Boolean cabezalSoloLectura = Boolean.FALSE;
    private List<SgEstudiante> estudianteList;
    private Boolean[] checkList;
    private List<SgEstudiante> estudiantesSeleccionados;

    @Inject
    @Param(name = "id")
    private Long diplomaId;

    @Inject
    @Param(name = "edit")
    private Boolean editable;

    public DiplomaBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            cargarSedePorDefecto();
            if (diplomaId != null && diplomaId > 0) {
                this.actualizar(restClient.obtenerPorId(diplomaId));
                soloLectura = editable != null ? !editable : soloLectura;
            } else {
                this.agregar();

            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void cargarCombos() {
        sedeSeleccionada = null;
        comboDiplomado = new SofisComboG();
        comboDiplomado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboAnioLectivo = new SofisComboG();
        comboAnioLectivo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
    }

    public void cargarSedePorDefecto() throws Exception {
        if (sessionBean.getSedeDefecto() != null) {
            sedeSeleccionada = sessionBean.getSedeDefecto();
            seleccionarSede();
        }
    }

    public List<SgSede> completeSede(String query) {
        try {
            FiltroSedes fil = new FiltroSedes();
//            if (securityOperation != null) {
//                fil.setSecurityOperation(securityOperation);
//            }
            fil.setSedCodigoONombre(query);
            fil.setSedHabilitado(Boolean.TRUE);
            fil.setMaxResults(11L);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedCodigo", "sedNombre", "sedTipo", "sedVersion", "sedTipoCalendario.tcePk",
            "sedSedeAdscritaDe.sedPk","sedSedeAdscritaDe.sedVersion","sedSedeAdscritaDe.sedTipo"});
            return restSedeRestClient.buscar(fil);
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ce), ce);
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void seleccionarSede() {
        try {
            checkList = null;
            entidadEnEdicion = null;
            estudianteList = new ArrayList();
            comboAnioLectivo = new SofisComboG();
            comboAnioLectivo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboDiplomado = new SofisComboG();
            comboDiplomado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            estudiantesSeleccionados=new ArrayList();
            if (sedeSeleccionada != null) {
                
                FiltroAnioLectivo fal = new FiltroAnioLectivo();
                fal.setAleEstado(EnumAnioLectivoEstado.ABIERTO);
                fal.setIncluirCampos(new String[]{"alePk", "aleAnio", "aleVersion"});
                fal.setAscending(new boolean[]{false});
                fal.setOrderBy(new String[]{"aleAnio"});
                List<SgAnioLectivo> anios = anioLectivoRestClient.buscar(fal);
                comboAnioLectivo = new SofisComboG(anios, "aleAnio");
                comboAnioLectivo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                if (anios.size() == 1) {
                    comboAnioLectivo.setSelectedT(anios.get(0));
                    seleccionarAnioLectivo();
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarAnioLectivo() {
        try {
            checkList = null;
            entidadEnEdicion = null;
            estudianteList = new ArrayList();
            comboDiplomado = new SofisComboG();
            comboDiplomado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            estudiantesSeleccionados=new ArrayList();
            if (comboAnioLectivo.getSelectedT() != null && sedeSeleccionada != null) {
                FiltroDiplomado fil = new FiltroDiplomado();
                fil.setAnioLectivoPk(comboAnioLectivo.getSelectedT().getAlePk());
                fil.setSedePk(sedeSeleccionada.getSedPk());
                fil.setIncluirCampos(new String[]{"dipNombre", "dipVersion"});
                List<SgDiplomado> diplomados = restDiplomado.buscar(fil);
                comboDiplomado = new SofisComboG(diplomados, "dipNombre");
                comboDiplomado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                if (diplomados.size() == 1) {
                    comboDiplomado.setSelectedT(diplomados.get(0));
                    seleccionarDiplomado();
                }
            }
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ce), ce);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarDiplomado() {    
            checkList = null;
            entidadEnEdicion = null;
            estudianteList = new ArrayList();
            estudiantesSeleccionados=new ArrayList();
            buscarDiploma();
   
    }

    public void buscarDiploma() {
        try {
            
            if (comboDiplomado.getSelectedT() != null && comboAnioLectivo.getSelectedT() != null && sedeSeleccionada != null) {
                FiltroDiploma fd = new FiltroDiploma();
                fd.setAnioLectivoId(comboAnioLectivo.getSelectedT().getAlePk());
                fd.setDiplomadoFk(comboDiplomado.getSelectedT().getDipPk());
                fd.setSedePk(sedeSeleccionada.getSedPk());
                fd.setIncluirDiplomadoEstudiante(Boolean.TRUE);
                List<SgDiploma> diplomas = restClient.buscar(fd);
                if (diplomas.isEmpty()) {
                    entidadEnEdicion = new SgDiploma();
                    entidadEnEdicion.setDilAnioLectivoFk(comboAnioLectivo.getSelectedT());
                    entidadEnEdicion.setDilDiplomadoFk(comboDiplomado.getSelectedT());
                    entidadEnEdicion.setDilSedeFk(sedeSeleccionada);
                    estudianteList = buscarEstudiantes();
                    estudiantesSeleccionados=new ArrayList();
                } else {
                    entidadEnEdicion = diplomas.get(0);
                    estudianteList = entidadEnEdicion.getDiplomaEstudiantes().stream().map(c -> c.getDieEstudianteFk()).collect(Collectors.toList());
                }
            }
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ce), ce);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public List<SgEstudiante> buscarEstudiantes() {
        try {
            if (comboDiplomado.getSelectedT() != null && comboAnioLectivo.getSelectedT() != null && sedeSeleccionada != null) {
                FiltroDiplomadosEstudiante fde = new FiltroDiplomadosEstudiante();
                fde.setAnioLectivoId(comboAnioLectivo.getSelectedT().getAlePk());
                fde.setSedePk(sedeSeleccionada.getSedPk());
                fde.setDiplomadoId(comboDiplomado.getSelectedT().getDipPk());
                fde.setOrderBy(new String[]{"depEstudiante.estPersona.perPrimerApellido", "depEstudiante.estPersona.perSegundoApellido",
                    "depEstudiante.estPersona.perPrimerNombre"});
                fde.setAscending(new boolean[]{true, true, true});
                fde.setIncluirCampos(new String[]{"depEstudiante.estVersion",
                    "depEstudiante.estPk",
                    "depEstudiante.estPersona.perVersion",
                    "depEstudiante.estPersona.perPrimerNombre",
                    "depEstudiante.estPersona.perSegundoNombre",
                    "depEstudiante.estPersona.perPrimerApellido",
                    "depEstudiante.estPersona.perSegundoApellido",
                    "depEstudiante.estPersona.perNie",
                    "depEstudiante.estPersona.perPk"});
                List<SgDiplomadosEstudiante> dipEst = diplomadoEstudianteRestClient.buscar(fde);
                return dipEst.stream().map(c -> c.getDepEstudiante()).collect(Collectors.toList());
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return new ArrayList();
    }


    private void limpiarCombos() {

    }

    public void limpiar() {
        filtro = new FiltroDiploma();
    }

    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgDiploma();
    }

    public void actualizar(SgDiploma var) {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgDiploma) SerializationUtils.clone(var);
        estudianteList = entidadEnEdicion.getDiplomaEstudiantes().stream().map(c -> c.getDieEstudianteFk()).collect(Collectors.toList());
    }

    public void guardar() {
        try {
            List<SgDiplomaEstudiante> est = new ArrayList();
            if(!estudiantesSeleccionados.isEmpty()){
                for(SgEstudiante estudiante:estudiantesSeleccionados){
                    SgDiplomaEstudiante estImp = new SgDiplomaEstudiante();
                    estImp.setDieEstudianteFk(estudiante);
                    estImp.setDieConfirmado(Boolean.TRUE);
                    est.add(estImp);
                }
            }

            entidadEnEdicion.setDiplomaEstudiantes(est);
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            buscarDiploma();
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getDilPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");

            entidadEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroDiploma getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroDiploma filtro) {
        this.filtro = filtro;
    }

    public SgDiploma getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgDiploma entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public SofisComboG<SgDiplomado> getComboDiplomado() {
        return comboDiplomado;
    }

    public void setComboDiplomado(SofisComboG<SgDiplomado> comboDiplomado) {
        this.comboDiplomado = comboDiplomado;
    }

    public SofisComboG<SgAnioLectivo> getComboAnioLectivo() {
        return comboAnioLectivo;
    }

    public void setComboAnioLectivo(SofisComboG<SgAnioLectivo> comboAnioLectivo) {
        this.comboAnioLectivo = comboAnioLectivo;
    }

    public SgAnioLectivo getAnioLectivoEnEdicion() {
        return anioLectivoEnEdicion;
    }

    public void setAnioLectivoEnEdicion(SgAnioLectivo anioLectivoEnEdicion) {
        this.anioLectivoEnEdicion = anioLectivoEnEdicion;
    }

    public List<SgEstudiante> getEstudianteList() {
        return estudianteList;
    }

    public void setEstudianteList(List<SgEstudiante> estudianteList) {
        this.estudianteList = estudianteList;
    }

    public Boolean[] getCheckList() {
        return checkList;
    }

    public void setCheckList(Boolean[] checkList) {
        this.checkList = checkList;
    }

    public Long getDiplomaId() {
        return diplomaId;
    }

    public void setDiplomaId(Long diplomaId) {
        this.diplomaId = diplomaId;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
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

    public List<SgEstudiante> getEstudiantesSeleccionados() {
        return estudiantesSeleccionados;
    }

    public void setEstudiantesSeleccionados(List<SgEstudiante> estudiantesSeleccionados) {
        this.estudiantesSeleccionados = estudiantesSeleccionados;
    }

}
