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
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.cdi.Param;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesPaginas;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgCalificacionEstudiante;
import sv.gob.mined.siges.web.dto.SgComponentePlanGrado;
import sv.gob.mined.siges.web.dto.SgHabilitacionPeriodoCalificacion;
import sv.gob.mined.siges.web.dto.SgMatricula;
import sv.gob.mined.siges.web.dto.SgPeriodoCalificacion;
import sv.gob.mined.siges.web.dto.SgPeriodosOrdExord;
import sv.gob.mined.siges.web.dto.SgRangoFecha;
import sv.gob.mined.siges.web.dto.SgRelPeriodosHabilitacionPeriodo;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgCalificacion;
import sv.gob.mined.siges.web.dto.catalogo.SgConfiguracion;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.enumerados.EnumCalendarioEscolar;
import sv.gob.mined.siges.web.enumerados.EnumEstadoHabilitacionPeriodoCalificacion;
import sv.gob.mined.siges.web.enumerados.EnumTiposPeriodosCalificaciones;
import sv.gob.mined.siges.web.enumerados.TipoEscalaCalificacion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCalificacionEstudiante;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroHabilitacionPeriodoCalificacion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEstudiante;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroCalificacionCatalogo;
import sv.gob.mined.siges.web.lazymodels.LazyHabilitacionPeriodoCalificacionDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CalificacionEstudianteRestClient;
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
public class HabilitacionPeriodoCalificacionBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(HabilitacionPeriodoCalificacionBean.class.getName());

    @Inject
    private HabilitacionPeriodoCalificacionRestClient restClient;

    @Inject
    private CatalogosRestClient catalogoRestClient;

    @Inject
    private SedeRestClient sedeRestClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private CalificacionEstudianteRestClient calificacionEstudianteRestClient;

    @Inject
    @Param(name = "id")
    private Long habilitacionId;

    @Inject
    @Param(name = "edit")
    private Boolean editable;

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
    private SofisComboG<SgPeriodoCalificacion> comboPeriodoCalificacion;
    private SofisComboG<SgRangoFecha> comboRangoFecha;
    private SofisComboG<EnumCalendarioEscolar> comboCalendarioEscolar;
    private List<EnumTiposPeriodosCalificaciones> periodoOrdinarioList;
    private EnumTiposPeriodosCalificaciones tipoPeriodoSeleccionado;
    private List<EnumCalendarioEscolar> listPeriodoExtraordinario;
    private List<SgMatricula> listMatriculas;
    private SgMatricula matriculaSeleccionada;
    private List<SgComponentePlanGrado> componentePlanGradoList;
    private SofisComboG<SgComponentePlanGrado> comboComponentePlanGrado;
    private List<SelectItem> comboPeriodosExord;
    private Integer periodoSeleccionadoExord;
    private String calificacionNumerica;
    private SgCalificacion calificacionConceptual;
    private List<SgCalificacion> calificacionConceptualList;
    private SofisComboG<SgCalificacion> comboCalificacionConceptual;
    private String calificacionActualValor;    
    private String mensajeMatricula;
    private String mensajeCalificacion;

    public HabilitacionPeriodoCalificacionBean() {
    }

    @PostConstruct
    public void init() {
        try {
            if (habilitacionId != null && habilitacionId > 0) {
                actualizar(new SgHabilitacionPeriodoCalificacion(habilitacionId, 0));
                soloLectura = editable != null ? !editable : soloLectura;
            } else {
                agregar();
            }
            cargarCombos();
            buscarMensajes();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void buscarMensajes() {
        try {
            mensajeMatricula=null;
            mensajeCalificacion=null;
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setCodigo(Constantes.SOLICITUD_CAMBIO_CALIFICACION_MAT);
            List<SgConfiguracion> conf = catalogoRestClient.buscarConfiguracion(fc);
            if(conf!=null && !conf.isEmpty() && !StringUtils.isBlank(conf.get(0).getConValor())){
                mensajeMatricula=conf.get(0).getConValor();
            }
            
            fc.setCodigo(Constantes.SOLICITUD_CAMBIO_CALIFICACION_CAL);
            conf = catalogoRestClient.buscarConfiguracion(fc);
            
            if(conf!=null && !conf.isEmpty() && !StringUtils.isBlank(conf.get(0).getConValor())){
                mensajeCalificacion=conf.get(0).getConValor();
            }
            
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        List<EnumTiposPeriodosCalificaciones> tipoPeriodoCalList = new ArrayList();
        tipoPeriodoCalList.add(EnumTiposPeriodosCalificaciones.ORD);
        tipoPeriodoCalList.add(EnumTiposPeriodosCalificaciones.EXORD);
        periodoOrdinarioList = new ArrayList(tipoPeriodoCalList);
    }

    public void buscarEstudiante() {
        try {
            entidadEnEdicion.setHpcMatriculaFk(null);
            entidadEnEdicion.setHpcEstudianteFk(null);
            comboPeriodoCalificacion = new SofisComboG();
            comboPeriodoCalificacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboCalendarioEscolar = new SofisComboG();
            comboCalendarioEscolar.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboPeriodosExord = new ArrayList();
            comboPeriodosExord.add(new SelectItem(0, Etiquetas.getValue("comboEmptyItem")));
            comboRangoFecha = new SofisComboG();
            comboRangoFecha.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboCalificacionConceptual = new SofisComboG();
            comboCalificacionConceptual.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            matriculaSeleccionada = null;
            rangoFechas = null;
            listPeriodoExtraordinario = null;
            componentePlanGradoList = null;
            tipoPeriodoSeleccionado = null;
            listMatriculas = new ArrayList();
            calificacionNumerica = null;
            calificacionConceptual = null;
            periodoSeleccionadoExord = 0;
            comboPeriodosExord = new ArrayList();
            comboPeriodosExord.add(new SelectItem(0, Etiquetas.getValue("comboEmptyItem")));
            calificacionConceptualList = null;
            entidadEnEdicion.setRelPeriodosHabilitacionPeriodo(new ArrayList());
            if (estudianteNie != null) {
                FiltroEstudiante fe = new FiltroEstudiante();
                fe.setNie(estudianteNie);
                listMatriculas = restClient.buscarEstudiante(fe);
                if (listMatriculas != null && !listMatriculas.isEmpty()) {
                    entidadEnEdicion.setHpcEstudianteFk(listMatriculas.get(0).getMatEstudiante());
                }
            } else {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, "Debe completar NIE", "");
            }
            entidadEnEdicion.setRelPeriodosHabilitacionPeriodo(new ArrayList());
            limpiarCombos();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarMatricula(SgMatricula matricula) {
        matriculaSeleccionada = null;
        entidadEnEdicion.setHpcMatriculaFk(null);
        rangoFechas = null;
        listPeriodoExtraordinario = null;
        componentePlanGradoList = null;
        tipoPeriodoSeleccionado = EnumTiposPeriodosCalificaciones.ORD;
        comboPeriodoCalificacion = new SofisComboG();
        comboPeriodoCalificacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboCalendarioEscolar = new SofisComboG();
        comboCalendarioEscolar.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboRangoFecha = new SofisComboG();
        comboRangoFecha.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboComponentePlanGrado = new SofisComboG();
        comboComponentePlanGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboCalificacionConceptual = new SofisComboG();
        comboCalificacionConceptual.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        calificacionNumerica = null;
        calificacionConceptual = null;
        calificacionConceptualList = null;
        entidadEnEdicion.setRelPeriodosHabilitacionPeriodo(new ArrayList());
        try {
            if (matricula != null) {
                matriculaSeleccionada = matricula;
                entidadEnEdicion.setHpcMatriculaFk(matriculaSeleccionada);
                SgPeriodosOrdExord periodos = restClient.buscarPeriodos(matricula);
                rangoFechas = periodos.getRangoFechas();
                listPeriodoExtraordinario = periodos.getPeriodosExtraordinarios();
                componentePlanGradoList = periodos.getComponentesPlanGrado();

                if (!componentePlanGradoList.isEmpty()) {
                    comboComponentePlanGrado = new SofisComboG(componentePlanGradoList, "cpgNombrePublicable");
                    comboComponentePlanGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                    comboComponentePlanGrado.ordenar();
                }
            }

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public Boolean esMatriculaSeleccionada(SgMatricula mat) {
        if (matriculaSeleccionada != null) {
            return mat.equals(matriculaSeleccionada);
        }
        return Boolean.FALSE;
    }

    private void limpiarCombos() {
        comboPeriodoCalificacion = new SofisComboG();
        comboPeriodoCalificacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboCalendarioEscolar = new SofisComboG();
        comboCalendarioEscolar.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboRangoFecha = new SofisComboG();
        comboRangoFecha.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboComponentePlanGrado = new SofisComboG();
        comboComponentePlanGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
    }

    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgHabilitacionPeriodoCalificacion();
        rangoFechas = new ArrayList();
        listPeriodoExtraordinario = new ArrayList();
        estudianteNie = null;
    }

    public void actualizar(SgHabilitacionPeriodoCalificacion var) {
        try {
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

    public String tituloDialog() {
        if (BooleanUtils.isFalse(soloLectura) && EnumEstadoHabilitacionPeriodoCalificacion.PENDIENTE.equals(entidadEnEdicion.getHpcEstado())) {
            return Etiquetas.getValue("hevaluarCambiosCalificacion");
        }
        return Etiquetas.getValue("hsolicitarCambioCalificacion");
    }

    public void guardar() {
        try {

            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            actualizar(entidadEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            JSFUtils.redirectToPage(ConstantesPaginas.HABILITACIONES_PERIODOS_CALIFICACION);
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void aprobar() {
        try {
            entidadEnEdicion = restClient.aprobar(entidadEnEdicion);
            actualizar(entidadEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.APROBADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void rechazar() {
        try {
            entidadEnEdicion = restClient.rechazar(entidadEnEdicion);
            actualizar(entidadEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.RECHAZADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarComponentePlanGrado() {
        try {
            comboPeriodoCalificacion = new SofisComboG();
            comboPeriodoCalificacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboCalendarioEscolar = new SofisComboG();
            comboCalendarioEscolar.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            periodoSeleccionadoExord = 0;
            comboPeriodosExord = new ArrayList();
            comboPeriodosExord.add(new SelectItem(0, Etiquetas.getValue("comboEmptyItem")));
            comboRangoFecha = new SofisComboG();
            comboRangoFecha.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboCalificacionConceptual = new SofisComboG();
            comboCalificacionConceptual.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            tipoPeriodoSeleccionado = EnumTiposPeriodosCalificaciones.ORD;
            calificacionNumerica = null;
            calificacionConceptual = null;
            calificacionConceptualList = null;
            calificacionActualValor = null;
            if (comboComponentePlanGrado.getSelectedT() != null) {
                if (TipoEscalaCalificacion.CONCEPTUAL.equals(comboComponentePlanGrado.getSelectedT().getCpgEscalaCalificacion().getEcaTipoEscala())) {
                    FiltroCalificacionCatalogo fcal = new FiltroCalificacionCatalogo();
                    fcal.setCalEscalaCalificacionPk(comboComponentePlanGrado.getSelectedT().getCpgEscalaCalificacion().getEcaPk());
                    fcal.setOrderBy(new String[]{"calValor"});
                    calificacionConceptualList = catalogoRestClient.buscarCalificacion(fcal);
                    comboCalificacionConceptual = new SofisComboG(calificacionConceptualList, "calValor");
                    comboCalificacionConceptual.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

                }
            }
            seleccionarTipoPeriodo();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public Boolean esEscalaNumerica() {
        if (comboComponentePlanGrado.getSelectedT() != null) {
            return TipoEscalaCalificacion.NUMERICA.equals(comboComponentePlanGrado.getSelectedT().getCpgEscalaCalificacion().getEcaTipoEscala());
        } else {
            return Boolean.TRUE;
        }
    }

    public void seleccionarTipoPeriodo() {
        calificacionActualValor = null;
        comboPeriodoCalificacion = new SofisComboG();
        comboPeriodoCalificacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboCalendarioEscolar = new SofisComboG();
        comboCalendarioEscolar.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        periodoSeleccionadoExord = 0;
        comboPeriodosExord = new ArrayList();
        comboPeriodosExord.add(new SelectItem(0, Etiquetas.getValue("comboEmptyItem")));
        comboRangoFecha = new SofisComboG();
        comboRangoFecha.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        calificacionNumerica = null;
        calificacionConceptual = null;
        if (comboComponentePlanGrado.getSelectedT() != null) {
            switch (tipoPeriodoSeleccionado) {
                case ORD:
                    List<SgPeriodoCalificacion> listPeriodoCalif = rangoFechas.stream().filter(c -> c.getRfePeriodoCalificacion().getPcaNumero().equals(comboComponentePlanGrado.getSelectedT().getCpgPeriodosCalificacion())).map(c -> c.getRfePeriodoCalificacion()).distinct().collect(Collectors.toList());
                    comboPeriodoCalificacion = new SofisComboG(listPeriodoCalif, "pcaNombre");
                    comboPeriodoCalificacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                    break;
                case EXORD:
                    List<EnumCalendarioEscolar> listCal = new ArrayList();

                    for (EnumCalendarioEscolar esc : listPeriodoExtraordinario) {
                        if (esc.equals(EnumCalendarioEscolar.PREC) && comboComponentePlanGrado.getSelectedT().getCpgCantidadPrimeraPrueba() > 0) {
                            listCal.add(EnumCalendarioEscolar.PREC);
                        }
                        if (esc.equals(EnumCalendarioEscolar.PRECPS) && comboComponentePlanGrado.getSelectedT().getCpgCantidadPrimeraSuficiencia() > 0) {
                            listCal.add(EnumCalendarioEscolar.PRECPS);
                        }
                        if (esc.equals(EnumCalendarioEscolar.SREC) && comboComponentePlanGrado.getSelectedT().getCpgCantidadSegundaPrueba() > 0) {
                            listCal.add(EnumCalendarioEscolar.SREC);
                        }
                        if (esc.equals(EnumCalendarioEscolar.SRECPS) && comboComponentePlanGrado.getSelectedT().getCpgCantidadSegundaSuficiencia() > 0) {
                            listCal.add(EnumCalendarioEscolar.SRECPS);
                        }
                    }

                    comboCalendarioEscolar = new SofisComboG(listCal, "text");
                    comboCalendarioEscolar.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                    break;
            }
        }
    }

    public void seleccionarPeriodoCalificacion() {

        calificacionActualValor = null;
        comboRangoFecha = new SofisComboG();
        comboRangoFecha.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboCalendarioEscolar = new SofisComboG();
        comboCalendarioEscolar.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        periodoSeleccionadoExord = 0;
        comboPeriodosExord = new ArrayList();
        comboPeriodosExord.add(new SelectItem(0, Etiquetas.getValue("comboEmptyItem")));
        calificacionNumerica = null;
        calificacionConceptual = null;
        if (comboPeriodoCalificacion.getSelectedT() != null) {
            List<SgRangoFecha> rangos = rangoFechas.stream().filter(c -> c.getRfePeriodoCalificacion().equals(comboPeriodoCalificacion.getSelectedT())).collect(Collectors.toList());
            comboRangoFecha = new SofisComboG(rangos, "rfeCodigoRango");
            comboRangoFecha.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboRangoFecha.ordenar();
        }
    }

    public void seleccionarRangoFecha() {
        calificacionActualValor = null;
        calificacionNumerica = null;
        calificacionConceptual = null;
    }

    public void cargarRangoFechaCalendarioEscolar() {
        calificacionActualValor = null;
        periodoSeleccionadoExord = 0;
        comboPeriodosExord = new ArrayList();
        comboPeriodosExord.add(new SelectItem(0, Etiquetas.getValue("comboEmptyItem")));
        calificacionNumerica = null;
        calificacionConceptual = null;
        Integer cantidadPrueba = 0;
        if (comboCalendarioEscolar.getSelectedT() != null) {
            switch (comboCalendarioEscolar.getSelectedT()) {
                case PREC:
                    cantidadPrueba = comboComponentePlanGrado.getSelectedT().getCpgCantidadPrimeraPrueba();
                    break;
                case PRECPS:
                    cantidadPrueba = comboComponentePlanGrado.getSelectedT().getCpgCantidadPrimeraSuficiencia();
                    break;
                case SREC:
                    cantidadPrueba = comboComponentePlanGrado.getSelectedT().getCpgCantidadSegundaPrueba();
                    break;
                case SRECPS:
                    cantidadPrueba = comboComponentePlanGrado.getSelectedT().getCpgCantidadSegundaSuficiencia();
                    break;
                default:
                    cantidadPrueba = 0;
                    break;
            }
            comboPeriodosExord = new ArrayList();
            comboPeriodosExord.add(new SelectItem(0, Etiquetas.getValue("comboEmptyItem")));
            for (int i = 1; i <= cantidadPrueba; i++) {
                comboPeriodosExord.add(new SelectItem(i, Etiquetas.getValue("calificacion") + " " + i));
            }
        }

    }

    public void agregarPeriodo() {
        if (tipoPeriodoSeleccionado != null && comboComponentePlanGrado.getSelectedT() != null && (comboCalificacionConceptual.getSelectedT() != null || calificacionNumerica != null)) {
            switch (tipoPeriodoSeleccionado) {
                case ORD:
                    if (comboRangoFecha.getSelectedT() != null) {
                        List<SgRelPeriodosHabilitacionPeriodo> list = entidadEnEdicion.getRelPeriodosHabilitacionPeriodo().stream().filter(c -> c.getRphTipoPeriodoCalificacion().equals(tipoPeriodoSeleccionado) && c.getRphRangoFechaFk().equals(comboRangoFecha.getSelectedT()) && c.getRphComponentePlanGradoFk().equals(comboComponentePlanGrado.getSelectedT())).collect(Collectors.toList());
                        if (list.isEmpty()) {
                            SgRelPeriodosHabilitacionPeriodo rel = new SgRelPeriodosHabilitacionPeriodo();
                            rel.setRphRangoFechaFk(comboRangoFecha.getSelectedT());
                            rel.setRphTipoPeriodoCalificacion(tipoPeriodoSeleccionado);
                            rel.setRphComponentePlanGradoFk(comboComponentePlanGrado.getSelectedT());
                            rel.setRphCalificacionNumerica(calificacionNumerica);
                            rel.setRphCalificacionConceptual(comboCalificacionConceptual.getSelectedT());
                            rel.setRphCalificacionActualValor(calificacionActualValor);
                            entidadEnEdicion.getRelPeriodosHabilitacionPeriodo().add(rel);
                        }
                    } else {
                        JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, "Debe seleccionar rango fecha.", "");
                    }
                    break;
                case EXORD:
                    if (comboCalendarioEscolar.getSelectedT() != null && periodoSeleccionadoExord != null) {
                        List<SgRelPeriodosHabilitacionPeriodo> list = entidadEnEdicion.getRelPeriodosHabilitacionPeriodo().stream().filter(c -> c.getRphTipoPeriodoCalificacion().equals(tipoPeriodoSeleccionado) && c.getRphTipoCalendarioEscolar().equals(comboCalendarioEscolar.getSelectedT()) && c.getRphComponentePlanGradoFk().equals(comboComponentePlanGrado.getSelectedT()) && c.getRphNumeroExtraordinario().equals(periodoSeleccionadoExord)).collect(Collectors.toList());
                        if (list.isEmpty()) {
                            SgRelPeriodosHabilitacionPeriodo rel = new SgRelPeriodosHabilitacionPeriodo();
                            rel.setRphTipoCalendarioEscolar(comboCalendarioEscolar.getSelectedT());
                            rel.setRphTipoPeriodoCalificacion(tipoPeriodoSeleccionado);
                            rel.setRphCalificacionNumerica(calificacionNumerica);
                            rel.setRphCalificacionConceptual(comboCalificacionConceptual.getSelectedT());
                            rel.setRphComponentePlanGradoFk(comboComponentePlanGrado.getSelectedT());
                            rel.setRphNumeroExtraordinario(periodoSeleccionadoExord);
                            rel.setRphCalificacionActualValor(calificacionActualValor);
                            entidadEnEdicion.getRelPeriodosHabilitacionPeriodo().add(rel);
                        }
                    } else {
                        JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, "Debe seleccionar período.", "");
                    }
                    break;
            }
        } else {
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, "Debe seleccionar componente plan estudio, período y calificación.", "");
        }
    }

    public void eliminarPeriodo(SgRelPeriodosHabilitacionPeriodo rel) {
        if (entidadEnEdicion.getRelPeriodosHabilitacionPeriodo() != null) {
            entidadEnEdicion.getRelPeriodosHabilitacionPeriodo().remove(rel);
        }
    }

    public void cargarCalificacionActual() {
        try {
            calificacionActualValor = null;
            if (tipoPeriodoSeleccionado != null && comboComponentePlanGrado.getSelectedT() != null && matriculaSeleccionada != null) {

                FiltroCalificacionEstudiante fce = new FiltroCalificacionEstudiante();
                fce.setEstudiantePk(matriculaSeleccionada.getMatEstudiante().getEstPk());
                fce.setAnioLectivoPk(matriculaSeleccionada.getMatSeccion().getSecAnioLectivo().getAlePk());
                fce.setCaeComponentePlanEstudio(comboComponentePlanGrado.getSelectedT().getCpgComponentePlanEstudio().getCpePk());
                fce.setCaeTipoPeriodo(matriculaSeleccionada.getMatSeccion().getSecTipoPeriodo());
                fce.setCaeNumeroPeriodo(matriculaSeleccionada.getMatSeccion().getSecNumeroPeriodo());
                fce.setCaeGradoFk(matriculaSeleccionada.getMatSeccion().getSecServicioEducativo().getSduGrado().getGraPk());
                fce.setIncluirCampos(new String[]{"caeCalificacionNumericaEstudiante", "caeCalificacionConceptualEstudiante.calValor",
                    "caeCalificacionConceptualEstudiante.calPk", "caeCalificacionConceptualEstudiante.calVersion"});
                switch (tipoPeriodoSeleccionado) {
                    case ORD:
                        if (comboRangoFecha.getSelectedT() != null) {
                            fce.setCaeTipoPeriodoCalificacion(tipoPeriodoSeleccionado);
                            fce.setCaeRangoFechaPk(comboRangoFecha.getSelectedT().getRfePk());
                            List<SgCalificacionEstudiante> cal = calificacionEstudianteRestClient.buscar(fce);
                            if (cal != null && !cal.isEmpty()) {
                                calificacionActualValor = cal.get(0).getCaeCalificacionValor();
                            } else {
                                calificacionActualValor = "-";
                            }
                        }
                        break;
                    case EXORD:
                        if (comboCalendarioEscolar.getSelectedT() != null && periodoSeleccionadoExord != null) {
                            fce.setCaeTipoPeriodoCalificacion(tipoPeriodoSeleccionado);
                            fce.setCaeTipoCalendarioEscolar(comboCalendarioEscolar.getSelectedT());
                            fce.setCaeNumero(periodoSeleccionadoExord);
                            List<SgCalificacionEstudiante> cal = calificacionEstudianteRestClient.buscar(fce);
                            if (cal != null && !cal.isEmpty()) {
                                calificacionActualValor = cal.get(0).getCaeCalificacionValor();
                            } else {
                                calificacionActualValor = "-";
                            }
                        }
                        break;

                }
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
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

    public SofisComboG<SgPeriodoCalificacion> getComboPeriodoCalificacion() {
        return comboPeriodoCalificacion;
    }

    public void setComboPeriodoCalificacion(SofisComboG<SgPeriodoCalificacion> comboPeriodoCalificacion) {
        this.comboPeriodoCalificacion = comboPeriodoCalificacion;
    }

    public SofisComboG<SgRangoFecha> getComboRangoFecha() {
        return comboRangoFecha;
    }

    public void setComboRangoFecha(SofisComboG<SgRangoFecha> comboRangoFecha) {
        this.comboRangoFecha = comboRangoFecha;
    }

    public SofisComboG<EnumCalendarioEscolar> getComboCalendarioEscolar() {
        return comboCalendarioEscolar;
    }

    public void setComboCalendarioEscolar(SofisComboG<EnumCalendarioEscolar> comboCalendarioEscolar) {
        this.comboCalendarioEscolar = comboCalendarioEscolar;
    }

    public List<EnumTiposPeriodosCalificaciones> getPeriodoOrdinarioList() {
        return periodoOrdinarioList;
    }

    public void setPeriodoOrdinarioList(List<EnumTiposPeriodosCalificaciones> periodoOrdinarioList) {
        this.periodoOrdinarioList = periodoOrdinarioList;
    }

    public EnumTiposPeriodosCalificaciones getTipoPeriodoSeleccionado() {
        return tipoPeriodoSeleccionado;
    }

    public void setTipoPeriodoSeleccionado(EnumTiposPeriodosCalificaciones tipoPeriodoSeleccionado) {
        this.tipoPeriodoSeleccionado = tipoPeriodoSeleccionado;
    }

    public List<EnumCalendarioEscolar> getListPeriodoExtraordinario() {
        return listPeriodoExtraordinario;
    }

    public void setListPeriodoExtraordinario(List<EnumCalendarioEscolar> listPeriodoExtraordinario) {
        this.listPeriodoExtraordinario = listPeriodoExtraordinario;
    }

    public List<SgMatricula> getListMatriculas() {
        return listMatriculas;
    }

    public void setListMatriculas(List<SgMatricula> listMatriculas) {
        this.listMatriculas = listMatriculas;
    }

    public SgMatricula getMatriculaSeleccionada() {
        return matriculaSeleccionada;
    }

    public void setMatriculaSeleccionada(SgMatricula matriculaSeleccionada) {
        this.matriculaSeleccionada = matriculaSeleccionada;
    }

    public SofisComboG<SgComponentePlanGrado> getComboComponentePlanGrado() {
        return comboComponentePlanGrado;
    }

    public void setComboComponentePlanGrado(SofisComboG<SgComponentePlanGrado> comboComponentePlanGrado) {
        this.comboComponentePlanGrado = comboComponentePlanGrado;
    }

    public List<SelectItem> getComboPeriodosExord() {
        return comboPeriodosExord;
    }

    public void setComboPeriodosExord(List<SelectItem> comboPeriodosExord) {
        this.comboPeriodosExord = comboPeriodosExord;
    }

    public Integer getComboPeriodoSeleccionadoExord() {
        return periodoSeleccionadoExord;
    }

    public void setComboPeriodoSeleccionadoExord(Integer comboPeriodoSeleccionadoExord) {
        this.periodoSeleccionadoExord = comboPeriodoSeleccionadoExord;
    }

    public String getCalificacionNumerica() {
        return calificacionNumerica;
    }

    public void setCalificacionNumerica(String calificacionNumerica) {
        this.calificacionNumerica = calificacionNumerica;
    }

    public SgCalificacion getCalificacionConceptual() {
        return calificacionConceptual;
    }

    public void setCalificacionConceptual(SgCalificacion calificacionConceptual) {
        this.calificacionConceptual = calificacionConceptual;
    }

    public List<SgCalificacion> getCalificacionConceptualList() {
        return calificacionConceptualList;
    }

    public void setCalificacionConceptualList(List<SgCalificacion> calificacionConceptualList) {
        this.calificacionConceptualList = calificacionConceptualList;
    }

    public List<SgComponentePlanGrado> getComponentePlanGradoList() {
        return componentePlanGradoList;
    }

    public void setComponentePlanGradoList(List<SgComponentePlanGrado> componentePlanGradoList) {
        this.componentePlanGradoList = componentePlanGradoList;
    }

    public SofisComboG<SgCalificacion> getComboCalificacionConceptual() {
        return comboCalificacionConceptual;
    }

    public void setComboCalificacionConceptual(SofisComboG<SgCalificacion> comboCalificacionConceptual) {
        this.comboCalificacionConceptual = comboCalificacionConceptual;
    }

    public String getCalificacionActualValor() {
        return calificacionActualValor;
    }

    public void setCalificacionActualValor(String calificacionActualValor) {
        this.calificacionActualValor = calificacionActualValor;
    }

    public String getMensajeMatricula() {
        return mensajeMatricula;
    }

    public void setMensajeMatricula(String mensajeMatricula) {
        this.mensajeMatricula = mensajeMatricula;
    }

    public String getMensajeCalificacion() {
        return mensajeCalificacion;
    }

    public void setMensajeCalificacion(String mensajeCalificacion) {
        this.mensajeCalificacion = mensajeCalificacion;
    }

}
