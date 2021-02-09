/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgAnioLectivo;
import sv.gob.mined.siges.web.dto.SgMatricula;
import sv.gob.mined.siges.web.dto.SgSeccion;
import sv.gob.mined.siges.web.enumerados.EnumAnioLectivoEstado;
import sv.gob.mined.siges.web.enumerados.EnumMatriculaEstado;
import sv.gob.mined.siges.web.enumerados.EnumSeccionEstado;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAnioLectivo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMatricula;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSeccion;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AnioLectivoRestClient;
import sv.gob.mined.siges.web.restclient.EstudianteRestClient;
import sv.gob.mined.siges.web.restclient.MatriculaRestClient;
import sv.gob.mined.siges.web.restclient.SeccionRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;
import java.util.Comparator;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAsistencia;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCalificacionEstudiante;
import sv.gob.mined.siges.web.restclient.AsistenciaRestClient;
import sv.gob.mined.siges.web.restclient.CalificacionEstudianteRestClient;

@Named
@ViewScoped
public class CambioMatriculaBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(CambioSeccionBean.class.getName());

    @Inject
    private AnioLectivoRestClient restAnio;

    @Inject
    private SeccionRestClient restSeccion;

    @Inject
    private EstudianteRestClient restEstudiante;

    @Inject
    private MatriculaRestClient restMatricula;

    @Inject
    private AsistenciaRestClient restAsistencia;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private SeleccionarSeccionBean seccionBean;

    @Inject
    private CalificacionEstudianteRestClient restCalificacionEstudiante;

    private String formatoSeleccionado = "csv";
    private Integer paginado = 10;
    private SofisComboG<SgAnioLectivo> comboAnioDestino;
    private SofisComboG<SgSeccion> comboSeccionDestino;
    private SgSeccion seccionEnEdicion;
    private List<SgMatricula> listaMatriculas;
    private List<SgMatricula> listaCambios;
    private LocalDate fechaCambio;

    public CambioMatriculaBean() {
    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            cargarCombos();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.CAMBIAR_SECCION_MASIVO)) {
            JSFUtils.redirectToIndex();
        }
    }

    public void cargarCombos() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});
            fc.setOrderBy(new String[]{"minNombre"});

            listaMatriculas = new ArrayList<>();

            comboAnioDestino = new SofisComboG();
            comboAnioDestino.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboSeccionDestino = new SofisComboG();
            comboSeccionDestino.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarEstudiantes() {
        try {

            if (seccionEnEdicion != null) {

                //Buscar 
                FiltroMatricula filtroMatricula = new FiltroMatricula();
                filtroMatricula.setSecPk(seccionEnEdicion.getSecPk());
                filtroMatricula.setSecGradoFk(this.seccionBean.getComboGrado().getSelectedT().getGraPk());
                filtroMatricula.setMatAnio(this.seccionBean.getComboAnioLectivo().getSelectedT().getAleAnio());
                filtroMatricula.setMatEstado(EnumMatriculaEstado.ABIERTO);
                filtroMatricula.setSecOpcionFk((this.seccionBean.getComboOpcion() != null && this.seccionBean.getComboOpcion().getSelectedT() != null) ? this.seccionBean.getComboOpcion().getSelectedT().getOpcPk() : null);
                filtroMatricula.setSecProgramaEducativoFk((this.seccionBean.getComboProgramaEducativo() != null && this.seccionBean.getComboProgramaEducativo().getSelectedT() != null) ? this.seccionBean.getComboProgramaEducativo().getSelectedT().getPedPk() : null);

                filtroMatricula.setOrderBy(new String[]{"matEstudiante.estPersona.perPrimerApellido"});
                filtroMatricula.setAscending(new boolean[]{true});
                filtroMatricula.setIncluirCampos(new String[]{"matEstado", "matFechaHasta", "matFechaRegistro", "matFechaIngreso", "matCodigoConstancia",
                    "matObservaciones", "matObservacioneProvisional", "matProvisional", "matAnulada", "matMotivoRetiro.mrePk",
                    "matMotivoRetiro.mreVersion", "matRepetidor",
                    "matRetirada", "matCreacionUsuario", "matVersion", "matObsAnuRetiro", "matValidacionAcademica",
                    "matValidacionAcademicaFecha", "matValidacionAcademicaUsuario", "matPromocionGrado",
                    "matSeccion.secPk", "matSeccion.secNombre", "matSeccion.secVersion",
                    "matEstudiante.estPk", "matEstudiante.estVersion",
                    "matEstudiante.estPersona.perPk", "matEstudiante.estPersona.perVersion",
                    "matEstudiante.estPersona.perPrimerNombre", "matEstudiante.estPersona.perSegundoNombre", "matEstudiante.estPersona.perTercerNombre",
                    "matEstudiante.estPersona.perPrimerApellido", "matEstudiante.estPersona.perSegundoApellido", "matEstudiante.estPersona.perTercerApellido",
                    "matEstudiante.estPersona.perNie", "matEstudiante.estPersona.perSexo.sexPk", "matEstudiante.estPersona.perSexo.sexNombre", "matEstudiante.estPersona.perFechaNacimiento", "matEstudiante.estPersona.perNombreBusqueda"});

                listaMatriculas = restMatricula.buscar(filtroMatricula);
            }

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void validar() {
        if (listaCambios.isEmpty()) {
            JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_LISTA_ESTUDIANTES_VACIO), "");
        } else if (comboSeccionDestino != null && comboSeccionDestino.getSelectedT() != null) {
            if (fechaCambio != null) {
                PrimeFaces.current().executeScript("PF('confirmDialog').show()");
            } else {
                JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_FECHA_VACIO), "");
            }
        } else {
            JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_SECCION_DESTINO_VACIO), "");
        }

    }

    public void guardar() {
        try {
            SgMatricula entidadModificada;
            Integer indice = 0;
            SgMatricula[] listaMat = new SgMatricula[listaCambios.size()];
            for (int i = 0; i < listaCambios.size(); i++) {

                entidadModificada = listaCambios.get(i);
                if (!entidadModificada.getMatSeccion().equals(comboSeccionDestino.getSelectedT())) {
                    entidadModificada.setMatSeccion(comboSeccionDestino.getSelectedT());
                    entidadModificada.setMatFechaIngreso(fechaCambio);
                    listaMat[indice] = entidadModificada;
                    indice++;
                }

            }
            restMatricula.cambioMasivoSeccion(listaMat);

            this.cargarEstudiantes();
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            listaCambios = new ArrayList<>();

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarSeccion(SgSeccion sec) {
        try {

            seccionEnEdicion = sec;
            listaMatriculas = new ArrayList<>();
            listaCambios = new ArrayList<>();
            comboAnioDestino = new SofisComboG<>();
            comboAnioDestino.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboSeccionDestino = new SofisComboG();
            comboSeccionDestino.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            if (seccionEnEdicion != null) {
                
                
                //Buscar si hay calificaciones pendientes para no dejar hacer el cambio
                FiltroCalificacionEstudiante fCE = new FiltroCalificacionEstudiante();
                fCE.setSeccionPk(seccionEnEdicion.getSecPk());
                if (restCalificacionEstudiante.buscarTotal(fCE) > 0) {
                    JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_CALIFICACIONES_REALIZADAS), "");
                    return;
                }

                FiltroAsistencia fAsis = new FiltroAsistencia();
                fAsis.setAsiSeccion(seccionEnEdicion.getSecPk());
                if (restAsistencia.buscarTotal(fAsis) > 0) {
                    JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_ASISTENCIAS_REALIZADAS), "");
                    return;
                }

                FiltroAnioLectivo fanio = new FiltroAnioLectivo();
                fanio.setAleGradoFk(this.seccionBean.getComboGrado().getSelectedT().getGraPk());
                fanio.setAleSedeFk(this.seccionBean.getSedeSeleccionada().getSedPk());
                fanio.setAleEstado(EnumAnioLectivoEstado.ABIERTO);
                fanio.setAleSeccionEstado(EnumSeccionEstado.ABIERTA);
                List<SgAnioLectivo> anios = restAnio.buscar(fanio);
                anios.sort(Comparator.comparing(SgAnioLectivo::getAleAnio).reversed());
                anios.remove(this.seccionBean.getComboAnioLectivo().getSelectedT());
                comboAnioDestino = new SofisComboG(anios, "aleAnio");
                comboAnioDestino.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                if (comboAnioDestino != null) {
                    comboAnioDestino.setSelected(-1);
                    seleccionarAnioDestino();
                }
                cargarEstudiantes();

            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarAnioDestino() {
        try {
            comboSeccionDestino = new SofisComboG();
            comboSeccionDestino.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            if (comboAnioDestino.getSelectedT() != null) {
                FiltroSeccion fSeccion = new FiltroSeccion();
                fSeccion.setSecSedeFk(this.seccionBean.getSedeSeleccionada().getSedPk());
                fSeccion.setSecGradoFk(this.seccionBean.getComboGrado().getSelectedT().getGraPk());
                fSeccion.setSecAnioLectivoFk(comboAnioDestino.getSelectedT().getAlePk());
                fSeccion.setSecEstado(EnumSeccionEstado.ABIERTA);
                fSeccion.setSecOpcionFk((this.seccionBean.getComboOpcion() != null && this.seccionBean.getComboOpcion().getSelectedT() != null) ? this.seccionBean.getComboOpcion().getSelectedT().getOpcPk() : null);
                fSeccion.setSecProgramaEducativoFk((this.seccionBean.getComboProgramaEducativo() != null && this.seccionBean.getComboProgramaEducativo().getSelectedT() != null) ? this.seccionBean.getComboProgramaEducativo().getSelectedT().getPedPk() : null);
                fSeccion.setIncluirCampos(new String[]{"secNombre", "secVersion", "secJornadaLaboral"});
                List<SgSeccion> seccion = restSeccion.buscar(fSeccion);
                seccion.remove(seccionEnEdicion);
                comboSeccionDestino = new SofisComboG(seccion, "nombreSeccion");
                comboSeccionDestino.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                comboSeccionDestino.ordenar();
                if (seccion.size() == 1) {
                    comboSeccionDestino.setSelectedT(seccion.get(0));
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    //<editor-fold defaultstate="collapsed" desc="getter and setter">
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

    public SeccionRestClient getRestSeccion() {
        return restSeccion;
    }

    public void setRestSeccion(SeccionRestClient restSeccion) {
        this.restSeccion = restSeccion;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public EstudianteRestClient getRestEstudiante() {
        return restEstudiante;
    }

    public void setRestEstudiante(EstudianteRestClient restEstudiante) {
        this.restEstudiante = restEstudiante;
    }

    public List<SgMatricula> getListaMatriculas() {
        return listaMatriculas;
    }

    public void setListaMatriculas(List<SgMatricula> listaMatriculas) {
        this.listaMatriculas = listaMatriculas;
    }

    public SofisComboG<SgAnioLectivo> getComboAnioDestino() {
        return comboAnioDestino;
    }

    public void setComboAnioDestino(SofisComboG<SgAnioLectivo> comboAnioDestino) {
        this.comboAnioDestino = comboAnioDestino;
    }

    public SofisComboG<SgSeccion> getComboSeccionDestino() {
        return comboSeccionDestino;
    }

    public void setComboSeccionDestino(SofisComboG<SgSeccion> comboSeccionDestino) {
        this.comboSeccionDestino = comboSeccionDestino;
    }

    public AnioLectivoRestClient getRestAnio() {
        return restAnio;
    }

    public void setRestAnio(AnioLectivoRestClient restAnio) {
        this.restAnio = restAnio;
    }

    public MatriculaRestClient getRestMatricula() {
        return restMatricula;
    }

    public void setRestMatricula(MatriculaRestClient restMatricula) {
        this.restMatricula = restMatricula;
    }

    public SgSeccion getSeccionEnEdicion() {
        return seccionEnEdicion;
    }

    public void setSeccionEnEdicion(SgSeccion seccionEnEdicion) {
        this.seccionEnEdicion = seccionEnEdicion;
    }

    public List<SgMatricula> getListaCambios() {
        return listaCambios;
    }

    public void setListaCambios(List<SgMatricula> listaCambios) {
        this.listaCambios = listaCambios;
    }

    public LocalDate getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(LocalDate fechaCambio) {
        this.fechaCambio = fechaCambio;
    }

    //</editor-fold>
}
