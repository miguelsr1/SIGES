/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.time.LocalDate;
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
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgMatricula;
import sv.gob.mined.siges.web.dto.SgReglaEquivalenciaDetalle;
import sv.gob.mined.siges.web.dto.SgSeccion;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgMotivoRetiro;
import sv.gob.mined.siges.web.enumerados.EnumMatriculaEstado;
import sv.gob.mined.siges.web.enumerados.EnumSeccionEstado;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMatricula;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSeccion;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroMotivoRetiro;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.EstudianteRestClient;
import sv.gob.mined.siges.web.restclient.MatriculaRestClient;
import sv.gob.mined.siges.web.restclient.ReglaEquivalenciaDetalleRestClient;
import sv.gob.mined.siges.web.restclient.SeccionRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class CambioSeccionBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(CambioSeccionBean.class.getName());

    @Inject
    private SeccionRestClient restSeccion;

    @Inject
    private EstudianteRestClient restEstudiante;

    @Inject
    private MatriculaRestClient restMatricula;

    @Inject
    private CatalogosRestClient restCatalogo;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private SeleccionarSeccionBean seleccionarSeccionBean;

    @Inject
    private SeleccionarSeccionBean2 seleccionarSeccionBean2;

    @Inject
    private ReglaEquivalenciaDetalleRestClient equivalenciaDetalleClient;

    private String formatoSeleccionado = "csv";
    private Integer paginado = 10;
    private SgSeccion seccionSeleccionada;
    private SofisComboG<SgSeccion>[] combosSecciones;
    private LocalDate[] fechaIngresoSecciones;
    private SgMotivoRetiro motivoRetiro;
    private Boolean admiteOpciones = false;

    private List<SgMatricula> listaMatriculas;

    private SgSeccion[] seccionesSeleccionadas;
    private SgSeccion seccionDestinoSeleccionada;
    private Integer estudianteIndexSeleccionado;
    private List<SgReglaEquivalenciaDetalle> equivalenciasPermitidas = new ArrayList<>();
    private SgSede sedeSeleccionada;

    public CambioSeccionBean() {
    }

    @PostConstruct
    public void init() {
        try {

            FiltroMotivoRetiro fmotivo = new FiltroMotivoRetiro();
            fmotivo.setCambioSeccion(Boolean.TRUE);
            List<SgMotivoRetiro> listMotivo = restCatalogo.buscarMotivoRetiro(fmotivo);
            if (listMotivo.size() == 1) {
                motivoRetiro = listMotivo.get(0);
            } else {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_CAMBIO_SECCION_VACIO), "");
            }

            cargarCombos();
            validarAcceso();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_CAMBIO_JORNADA)) {
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
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarEstudiantes(SgSeccion seccion) {
        try {
            seccionSeleccionada = seccion;
            sedeSeleccionada=null;
            if (seccionSeleccionada != null) {
                //Buscar 
                sedeSeleccionada=seccionSeleccionada.getSecServicioEducativo().getSduSede();
                
                FiltroMatricula filtroMatricula = new FiltroMatricula();
                filtroMatricula.setSecPk(seccionSeleccionada.getSecPk());

                filtroMatricula.setSecGradoFk(this.seleccionarSeccionBean.getComboGrado().getSelectedT().getGraPk());
                filtroMatricula.setMatAnio(this.seleccionarSeccionBean.getComboAnioLectivo().getSelectedT().getAleAnio());
                filtroMatricula.setMatEstado(EnumMatriculaEstado.ABIERTO);
                filtroMatricula.setSecOpcionFk((this.seleccionarSeccionBean.getComboOpcion() != null && this.seleccionarSeccionBean.getComboOpcion().getSelectedT() != null) ? this.seleccionarSeccionBean.getComboOpcion().getSelectedT().getOpcPk() : null);
                filtroMatricula.setSecProgramaEducativoFk((this.seleccionarSeccionBean.getComboProgramaEducativo() != null && this.seleccionarSeccionBean.getComboProgramaEducativo().getSelectedT() != null) ? this.seleccionarSeccionBean.getComboProgramaEducativo().getSelectedT().getPedPk() : null);

                filtroMatricula.setOrderBy(new String[]{"matEstudiante.estPersona.perNombreBusqueda"});
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
                combosSecciones = new SofisComboG[listaMatriculas.size()];
                seccionesSeleccionadas = new SgSeccion[listaMatriculas.size()];
                fechaIngresoSecciones = new LocalDate[listaMatriculas.size()];

                Arrays.fill(fechaIngresoSecciones, restCatalogo.obtenerFechaIngresoMatriculasPorDefecto(
                        this.seccionSeleccionada.getSecAnioLectivo().getAleAnio(), this.seccionSeleccionada.getSecServicioEducativo().getSduSede().getSedTipoCalendario()));

                FiltroSeccion fSeccion = new FiltroSeccion();
                fSeccion.setSecSedeFk(this.seleccionarSeccionBean.getSedeSeleccionada().getSedPk());
                fSeccion.setSecGradoFk(this.seleccionarSeccionBean.getComboGrado().getSelectedT().getGraPk());
                fSeccion.setSecAnioLectivoFk(this.seleccionarSeccionBean.getComboAnioLectivo().getSelectedT() != null ? this.seleccionarSeccionBean.getComboAnioLectivo().getSelectedT().getAlePk() : null);
                fSeccion.setSecProgramaEducativoFk(this.seleccionarSeccionBean.getComboProgramaEducativo().getSelectedT() != null ? this.seleccionarSeccionBean.getComboProgramaEducativo().getSelectedT().getPedPk() : null);
                fSeccion.setSecOpcionFk(this.seleccionarSeccionBean.getComboOpcion().getSelectedT() != null ? this.seleccionarSeccionBean.getComboOpcion().getSelectedT().getOpcPk() : null);
                fSeccion.setSecEstado(EnumSeccionEstado.ABIERTA);
                fSeccion.setSecModalidadEducativaPk(this.seleccionarSeccionBean.getComboModalidad().getSelectedT() != null ? this.seleccionarSeccionBean.getComboModalidad().getSelectedT().getModPk() : null);
                fSeccion.setSecSubModalidadAtencionFk(this.seleccionarSeccionBean.getComboSubModalidadAtencion() != null && this.seleccionarSeccionBean.getComboSubModalidadAtencion().getSelectedT() != null ? this.seleccionarSeccionBean.getComboSubModalidadAtencion().getSelectedT().getSmoPk() : null);
                fSeccion.setOrderBy(new String[]{"secCodigo"});
                fSeccion.setAscending(new boolean[]{true});
                List<SgSeccion> secciones = restSeccion.buscar(fSeccion);

                for (int i = 0; i < listaMatriculas.size(); i++) {
                    combosSecciones[i] = new SofisComboG(secciones, "nombreSeccion");
                    combosSecciones[i].addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

                    if (listaMatriculas.get(i).getMatSeccion() != null) {
                        combosSecciones[i].setSelectedT(listaMatriculas.get(i).getMatSeccion());
                    }
                }
            }

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminarSeccionEstudianteIndex(Integer index) {
        this.seccionDestinoSeleccionada = null;
        this.seccionesSeleccionadas[index] = null;
        seleccionarSeccionBean2.limpiarCombos();
    }

    public void seleccionarEstudianteIndex(Integer index) {
        try{
            this.estudianteIndexSeleccionado = index;
            this.seccionDestinoSeleccionada = null;
            this.seleccionarSeccionBean2.setEntidadEnEdicion(null);
            this.seleccionarSeccionBean2.setSoloLectura(Boolean.FALSE);
            this.seleccionarSeccionBean2.limpiarCombos();
            if (this.seccionesSeleccionadas[index] != null) {
                this.seccionDestinoSeleccionada = this.seccionesSeleccionadas[index];
                this.seleccionarSeccionBean2.setSoloLectura(Boolean.TRUE);
                this.seleccionarSeccionBean2.setEntidadEnEdicion(this.seccionDestinoSeleccionada);
            }else{
                this.seleccionarSeccionBean2.setSedeEnEdicion(sedeSeleccionada);
                this.seleccionarSeccionBean2.cargarSedePorDefecto();
            }
         } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarSeccion(SgSeccion sec) {
        try {
            this.seccionDestinoSeleccionada = sec;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public SgSeccion obtenerNuevaSeccionIndiceEstudiante(Integer index) {
        if (this.seccionesSeleccionadas[index] != null) {
            return this.seccionesSeleccionadas[index];
        } else {
            return combosSecciones[index].getSelectedT();
        }
    }

    public void aceptarSeccion() {
        try {

            if (seccionDestinoSeleccionada != null) {
                
                if (!seccionDestinoSeleccionada.getSecAnioLectivo().equals(seccionSeleccionada.getSecAnioLectivo())){
                    JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP_2, Mensajes.obtenerMensaje(Mensajes.ERROR_ANIO_SECCIONES_DIFIERE), "");
                    return;
                }
                
                equivalenciasPermitidas = equivalenciaDetalleClient.buscarEquivalenciasPermitidas(seccionSeleccionada.getSecServicioEducativo().getSduGrado(),
                        seccionSeleccionada.getSecPlanEstudio(),
                        seccionSeleccionada.getSecServicioEducativo().getSduOpcion(),
                        seccionSeleccionada.getSecServicioEducativo().getSduProgramaEducativo()
                );
                
                Boolean evaluacion = equivalenciasPermitidas.stream().filter(
                        o -> o.getRedGrado().getGraPk().equals(seccionDestinoSeleccionada.getSecServicioEducativo().getSduGrado().getGraPk())
                            && (seccionDestinoSeleccionada.getSecServicioEducativo().getSduProgramaEducativo()!=null?seccionDestinoSeleccionada.getSecServicioEducativo().getSduProgramaEducativo().getPedPk().equals(o.getRedProgramaEducativo().getPedPk()):Boolean.TRUE)
                            && (seccionDestinoSeleccionada.getSecServicioEducativo().getSduOpcion()!=null?seccionDestinoSeleccionada.getSecServicioEducativo().getSduOpcion().getOpcPk().equals(o.getRedOpcion().getOpcPk()):Boolean.TRUE)
                ).findFirst().isPresent();
                if (!evaluacion) {
                    JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP_2, Mensajes.obtenerMensaje(Mensajes.ERROR_SECCIONES_NO_EQUIVALENTES), "");
                    return;
                }
            }
            this.seccionesSeleccionadas[estudianteIndexSeleccionado] = seccionDestinoSeleccionada;
            PrimeFaces.current().executeScript("PF('itemDetailSeleccionarSeccion').hide()");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP_2, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP_2, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardar() {
        try {

            int cantidadModificar = 0;
            int indice = 0;

            for (int i = 0; i < listaMatriculas.size(); i++) {

                if (this.obtenerNuevaSeccionIndiceEstudiante(i) == null) {
                    JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_ESTUDIANTE_SIN_SECCION), "");
                    return;
                }
                if (!this.obtenerNuevaSeccionIndiceEstudiante(i).equals(listaMatriculas.get(i).getMatSeccion())) {
                    cantidadModificar++;
                }
            }

            SgMatricula entidadEnEdicion;
            SgMatricula entidadModificada;
            SgMatricula[][] listaMat = new SgMatricula[cantidadModificar][2];
            for (int i = 0; i < listaMatriculas.size(); i++) {
                entidadModificada = listaMatriculas.get(i);

                if (!entidadModificada.getMatSeccion().equals(this.obtenerNuevaSeccionIndiceEstudiante(i))) {

                    LocalDate fechaIngreso = fechaIngresoSecciones[i];
                    if (fechaIngreso == null) {
                        JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_FECHA_INGRESO_VACIO), "");
                        return;
                    }

                    entidadModificada.setMatEstado(EnumMatriculaEstado.CERRADO);
                    entidadModificada.setMatRetirada(Boolean.TRUE);
                    entidadModificada.setMatFechaHasta(fechaIngreso.minusDays(1));
                    entidadModificada.setMatMotivoRetiro(motivoRetiro);

                    entidadEnEdicion = new SgMatricula();
                    entidadEnEdicion.setMatEstudiante(entidadModificada.getMatEstudiante());
                    entidadEnEdicion.setMatEstado(EnumMatriculaEstado.ABIERTO);
                    entidadEnEdicion.setMatValidacionAcademica(entidadModificada.getMatValidacionAcademica());
                    entidadEnEdicion.setMatSeccion(this.obtenerNuevaSeccionIndiceEstudiante(i));
                    entidadEnEdicion.setMatFechaIngreso(fechaIngreso);
                    entidadEnEdicion.setMatRepetidor(entidadModificada.getMatRepetidor());

                    entidadEnEdicion.setMatProvisional(entidadModificada.getMatProvisional());
                    if (entidadModificada.getMatProvisional()) {
                        entidadModificada.setMatProvisional(Boolean.FALSE);
                        entidadEnEdicion.setMatObservacioneProvisional(entidadModificada.getMatObservacioneProvisional());
                        entidadModificada.setMatObservacioneProvisional(null);
                    }

                    listaMat[indice][0] = entidadModificada;
                    listaMat[indice][1] = entidadEnEdicion;
                    indice++;
                }

            }
            restMatricula.cambiarSeccion(listaMat);
            this.cargarEstudiantes(seccionSeleccionada);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
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

    public SgSeccion getSeccionSeleccionada() {
        return seccionSeleccionada;
    }

    public void setSeccionSeleccionada(SgSeccion seccionSeleccionada) {
        this.seccionSeleccionada = seccionSeleccionada;
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

    public SofisComboG<SgSeccion>[] getCombosSecciones() {
        return combosSecciones;
    }

    public void setCombosSecciones(SofisComboG<SgSeccion>[] combosSecciones) {
        this.combosSecciones = combosSecciones;
    }

    public MatriculaRestClient getRestMatricula() {
        return restMatricula;
    }

    public void setRestMatricula(MatriculaRestClient restMatricula) {
        this.restMatricula = restMatricula;
    }

    public SgMotivoRetiro getMotivoRetiro() {
        return motivoRetiro;
    }

    public void setMotivoRetiro(SgMotivoRetiro motivoRetiro) {
        this.motivoRetiro = motivoRetiro;
    }

    public Boolean getAdmiteOpciones() {
        return admiteOpciones;
    }

    public void setAdmiteOpciones(Boolean admiteOpciones) {
        this.admiteOpciones = admiteOpciones;
    }

    public LocalDate[] getFechaIngresoSecciones() {
        return fechaIngresoSecciones;
    }

    public void setFechaIngresoSecciones(LocalDate[] fechaIngresoSecciones) {
        this.fechaIngresoSecciones = fechaIngresoSecciones;
    }

    public SeleccionarSeccionBean getSeleccionarSeccionBean() {
        return seleccionarSeccionBean;
    }

    public void setSeleccionarSeccionBean(SeleccionarSeccionBean seleccionarSeccionBean) {
        this.seleccionarSeccionBean = seleccionarSeccionBean;
    }

    public SgSeccion[] getSeccionesSeleccionadas() {
        return seccionesSeleccionadas;
    }

    public void setSeccionesSeleccionadas(SgSeccion[] seccionesSeleccionadas) {
        this.seccionesSeleccionadas = seccionesSeleccionadas;
    }

    public SgSeccion getSeccionDestinoSeleccionada() {
        return seccionDestinoSeleccionada;
    }

    public void setSeccionDestinoSeleccionada(SgSeccion seccionDestinoSeleccionada) {
        this.seccionDestinoSeleccionada = seccionDestinoSeleccionada;
    }
     public SgSede getSedeSeleccionada() {
        return sedeSeleccionada;
    }

    public void setSedeSeleccionada(SgSede sedeSeleccionada) {
        this.sedeSeleccionada = sedeSeleccionada;
    }

    //</editor-fold>

   
}
