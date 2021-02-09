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
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgAnioLectivo;
import sv.gob.mined.siges.web.dto.SgCalificacionDiplomado;
import sv.gob.mined.siges.web.dto.SgCalificacionDiplomadoEstudiante;
import sv.gob.mined.siges.web.dto.SgDiplomado;
import sv.gob.mined.siges.web.dto.SgDiplomadosEstudiante;
import sv.gob.mined.siges.web.dto.SgEstudiante;
import sv.gob.mined.siges.web.dto.SgModuloDiplomado;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgCalificacion;
import sv.gob.mined.siges.web.dto.catalogo.SgEscalaCalificacion;
import sv.gob.mined.siges.web.enumerados.EnumAnioLectivoEstado;
import sv.gob.mined.siges.web.enumerados.EnumTiposCalificacionDiplomado;
import sv.gob.mined.siges.web.enumerados.TipoEscalaCalificacion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAnioLectivo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCalificacionDiplomado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCalificacionDiplomadoEstudiante;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDiplomado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDiplomadosEstudiante;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroModuloDiplomado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroCalificacionCatalogo;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AnioLectivoRestClient;
import sv.gob.mined.siges.web.restclient.CalificacionDiplomadoEstudianteRestClient;
import sv.gob.mined.siges.web.restclient.CalificacionDiplomadoRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
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
public class CalificacionDiplomadoBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(CalificacionDiplomadoBean.class.getName());

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

    private SofisComboG<SgDiplomado> comboDiplomado;
    private SofisComboG<SgModuloDiplomado> comboModuloDiplomado;
    private SgSede sedeSeleccionada;
    private SofisComboG<SgAnioLectivo> comboAnioLectivo;
    private List<SelectItem> comboPeriodos;
    private Integer comboPeriodoSeleccionado;

    private SgModuloDiplomado moduloDiplomadoEnEdicion;
    private SgAnioLectivo anioLectivoEnEdicion;
    private Boolean soloLectura = Boolean.FALSE;
    private Boolean cabezalSoloLectura = Boolean.FALSE;
    private List<SgEstudiante> estudianteList;
    private List<SgCalificacionDiplomadoEstudiante> calificacionDiplomadoEstList;
    private SgCalificacionDiplomado calificacionDiplomado;
    private Boolean escalaNumerica;
    private SofisComboG<SgCalificacion>[] comboCalificacion;
    private SgEscalaCalificacion escalaCalificacion;

    @Inject
    @Param(name = "id")
    private Long calificacionId;

    @Inject
    @Param(name = "edit")
    private Boolean editable;

    public CalificacionDiplomadoBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            if (calificacionId != null && calificacionId > 0) {
                this.actualizar(calificacionDiplomadoRestClient.obtenerPorId(calificacionId));
                soloLectura = editable != null ? !editable : soloLectura;
                cabezalSoloLectura = Boolean.TRUE;
            } else {
                this.agregar();
                cabezalSoloLectura = Boolean.FALSE;

            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void cargarCombos() {
        try {

            sedeSeleccionada = null;
            comboDiplomado = new SofisComboG();
            comboDiplomado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboAnioLectivo = new SofisComboG();
            comboAnioLectivo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboModuloDiplomado = new SofisComboG();
            comboModuloDiplomado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboPeriodoSeleccionado = 0;
            comboPeriodos = new ArrayList();
            comboPeriodos.add(new SelectItem(0, Etiquetas.getValue("comboEmptyItem")));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
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
            fil.setIncluirCampos(new String[]{"sedCodigo", "sedNombre", "sedTipo", "sedVersion", "sedTipoCalendario.tcePk"});
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
            escalaCalificacion = null;
            estudianteList = new ArrayList();
            calificacionDiplomadoEstList = new ArrayList();
            calificacionDiplomado = new SgCalificacionDiplomado();
            comboAnioLectivo = new SofisComboG();
            comboAnioLectivo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboDiplomado = new SofisComboG();
            comboDiplomado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboModuloDiplomado = new SofisComboG();
            comboModuloDiplomado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboPeriodoSeleccionado = 0;
            comboPeriodos = new ArrayList();
            comboPeriodos.add(new SelectItem(0, Etiquetas.getValue("comboEmptyItem")));
            if (sedeSeleccionada != null) {
                FiltroAnioLectivo fal = new FiltroAnioLectivo();
                fal.setAleEstado(EnumAnioLectivoEstado.ABIERTO);
                fal.setIncluirCampos(new String[]{"alePk", "aleAnio", "aleVersion"});
                fal.setAscending(new boolean[]{true});
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
            escalaCalificacion = null;
            estudianteList = new ArrayList();
            calificacionDiplomadoEstList = new ArrayList();
            calificacionDiplomado = new SgCalificacionDiplomado();
            comboDiplomado = new SofisComboG();
            comboDiplomado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboModuloDiplomado = new SofisComboG();
            comboModuloDiplomado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboPeriodoSeleccionado = 0;
            comboPeriodos = new ArrayList();
            comboPeriodos.add(new SelectItem(0, Etiquetas.getValue("comboEmptyItem")));
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
        try {
            escalaCalificacion = null;
            estudianteList = new ArrayList();
            calificacionDiplomadoEstList = new ArrayList();
            calificacionDiplomado = new SgCalificacionDiplomado();
            comboModuloDiplomado = new SofisComboG();
            comboModuloDiplomado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboPeriodoSeleccionado = 0;
            comboPeriodos = new ArrayList();
            comboPeriodos.add(new SelectItem(0, Etiquetas.getValue("comboEmptyItem")));
            if (comboDiplomado.getSelectedT() != null) {
                FiltroModuloDiplomado fil = new FiltroModuloDiplomado();
                fil.setDiplomadoFk(comboDiplomado.getSelectedT().getDipPk());
                fil.setOrderBy(new String[]{"mdiNombre"});
                fil.setAscending(new boolean[]{true});
                List<SgModuloDiplomado> modDiplomados = restModuloDiplomado.buscar(fil);
                comboModuloDiplomado = new SofisComboG(modDiplomados, "mdiNombre");
                comboModuloDiplomado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                if (modDiplomados.size() == 1) {
                    comboModuloDiplomado.setSelectedT(modDiplomados.get(0));
                    seleccionarModuloDiplomado();
                }
            }
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ce), ce);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarModuloDiplomado() {
        escalaCalificacion = null;
        estudianteList = new ArrayList();
        calificacionDiplomadoEstList = new ArrayList();
        calificacionDiplomado = new SgCalificacionDiplomado();
        comboPeriodos = new ArrayList();
        comboPeriodos.add(new SelectItem(0, Etiquetas.getValue("comboEmptyItem")));
        comboPeriodoSeleccionado = 0;
        if (comboModuloDiplomado.getSelectedT() != null) {
            if (comboModuloDiplomado.getSelectedT().getMdiEscalaCalificacion() == null) {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, "El módulo del diplomado no tiene escala de calificación.", "");
                return;
            }

            if (comboModuloDiplomado.getSelectedT().getMdiPeriodosCalificacion() != null) {
                for (int i = 1; i <= comboModuloDiplomado.getSelectedT().getMdiPeriodosCalificacion(); i++) {
                    comboPeriodos.add(new SelectItem(i, "Período" + " " + i));
                }
            } else {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, "El módulo diplomado no tiene cantidad de períodos.", "");
                return;
            }
        }

    }

    public void seleccionarPeriodo() {
        try {
            estudianteList = new ArrayList();
            calificacionDiplomadoEstList = new ArrayList();
            calificacionDiplomado = new SgCalificacionDiplomado();
            if (comboPeriodoSeleccionado != null && comboPeriodoSeleccionado > 0 && comboModuloDiplomado.getSelectedT() != null
                    && sedeSeleccionada != null
                    && comboAnioLectivo.getSelectedT() != null) {

                FiltroCalificacionDiplomado fcd = new FiltroCalificacionDiplomado();
                fcd.setCadTipoCalificacion(EnumTiposCalificacionDiplomado.ORD);
                fcd.setCalAnioLectivoFk(comboAnioLectivo.getSelectedT().getAlePk());
                fcd.setCalModuloDiplomadoFk(comboModuloDiplomado.getSelectedT().getMdiPk());
                fcd.setCalNumeroPeriodo(comboPeriodoSeleccionado);
                fcd.setCalSedeFk(sedeSeleccionada.getSedPk());
                fcd.setIncluirCalificacionEstudiante(Boolean.TRUE);
                List<SgCalificacionDiplomado> listCalDp = calificacionDiplomadoRestClient.buscar(fcd);

                if (listCalDp.isEmpty()) {
                    //Se crea cabezal de calificacion diplomado y se setean los datos 
                    calificacionDiplomadoEstList = new ArrayList();
                    calificacionDiplomado = new SgCalificacionDiplomado();
                    calificacionDiplomado.setCadAnioLectivoFk(comboAnioLectivo.getSelectedT());
                    calificacionDiplomado.setCadModuloDiplomado(comboModuloDiplomado.getSelectedT());
                    calificacionDiplomado.setCadNumeroPeriodo(comboPeriodoSeleccionado);
                    calificacionDiplomado.setCadSedeFk(sedeSeleccionada);
                    calificacionDiplomado.setCadTipoCalificacion(EnumTiposCalificacionDiplomado.ORD);
                } else {
                    calificacionDiplomado = listCalDp.get(0);
                    calificacionDiplomadoEstList = calificacionDiplomado.getCadCalificacionDiplomadoEstudiantes();
                }
                estudianteList = buscarEstudiantes();
                if (estudianteList.isEmpty()) {
                    JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, "Lista de estudiantes es vacía para los datos seleccionados", "");
                    return;
                }
                cargarTabla();
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarTabla() {
        try {
            escalaCalificacion = null;
            if (!estudianteList.isEmpty()) {
                List<SgCalificacionDiplomadoEstudiante> calDipEstAux = new ArrayList();
                Collections.sort(estudianteList,(o1,o2)->o1.getEstPersona().getPerNombreCompleto().compareTo(o2.getEstPersona().getPerNombreCompleto()));
                for (SgEstudiante est : estudianteList) {

                    List<SgCalificacionDiplomadoEstudiante> calDeEst = calificacionDiplomadoEstList.stream().filter(c -> c.getCdeEstudianteFk().getEstPk().equals(est.getEstPk())).collect(Collectors.toList());

                    if (calDeEst.isEmpty()) {
                        SgCalificacionDiplomadoEstudiante calNueva = new SgCalificacionDiplomadoEstudiante();
                        calNueva.setCdeEstudianteFk(est);
                        calDipEstAux.add(calNueva);
                    } else {
                        calDipEstAux.add(calDeEst.get(0));
                    }
                }
                calificacionDiplomadoEstList = calDipEstAux;
                calificacionDiplomado.setCadCalificacionDiplomadoEstudiantes(calificacionDiplomadoEstList);

                if (calificacionDiplomado.getCadModuloDiplomado().getMdiEscalaCalificacion() != null) {
                    escalaCalificacion = calificacionDiplomado.getCadModuloDiplomado().getMdiEscalaCalificacion();
                    if (calificacionDiplomado.getCadModuloDiplomado().getMdiEscalaCalificacion().getEcaTipoEscala().equals(TipoEscalaCalificacion.NUMERICA)) {
                        escalaNumerica = Boolean.TRUE;
                    } else {
                        escalaNumerica = Boolean.FALSE;
                        FiltroCalificacionCatalogo fcal = new FiltroCalificacionCatalogo();
                        fcal.setCalEscalaCalificacionPk(calificacionDiplomado.getCadModuloDiplomado().getMdiEscalaCalificacion().getEcaPk());
                        fcal.setOrderBy(new String[]{"calValor"});
                        List<SgCalificacion> calificaciones = catalogoRestClient.buscarCalificacion(fcal);

                        comboCalificacion = new SofisComboG[estudianteList.size()];
                        for (int i = 0; i < estudianteList.size(); i++) {
                            comboCalificacion[i] = new SofisComboG(calificaciones, "calValor");
                            comboCalificacion[i].addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

                        }
                        for (SgCalificacionDiplomadoEstudiante cal : calificacionDiplomado.getCadCalificacionDiplomadoEstudiantes()) {
                            if (cal.getCdeCalificacionConceptualFk()!=null) {
                                comboCalificacion[estudianteList.indexOf(cal.getCdeEstudianteFk())].setSelectedT(cal.getCdeCalificacionConceptualFk());
                            }
                        }

                    }
                } else {
                    JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, "El módulo del diplomado no tiene escala de calificación.", "");
                }
            }
            PrimeFaces.current().executeScript("PF('blockerExord').hide()");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public List<SgEstudiante> buscarEstudiantes() {
        try {
            estudianteList = new ArrayList();

            if (calificacionDiplomado.getCadModuloDiplomado().getMdiDiplomado() != null && calificacionDiplomado.getCadAnioLectivoFk() != null && calificacionDiplomado.getCadSedeFk() != null) {
                FiltroDiplomadosEstudiante fde = new FiltroDiplomadosEstudiante();
                fde.setAnioLectivoId(calificacionDiplomado.getCadAnioLectivoFk().getAlePk());
                fde.setSedePk(calificacionDiplomado.getCadSedeFk().getSedPk());
                fde.setDiplomadoId(calificacionDiplomado.getCadModuloDiplomado().getMdiDiplomado().getDipPk());
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
                    "depEstudiante.estPersona.perPk",});
                List<SgDiplomadosEstudiante> dipEst = diplomadoEstudianteRestClient.buscar(fde);
                return dipEst.stream().map(c -> c.getDepEstudiante()).collect(Collectors.toList());

            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return new ArrayList();
    }

    public void calificacionSelected(SgCalificacionDiplomadoEstudiante cal, Integer rowIndex) {
        cal.setCdeCalificacionConceptualFk(this.comboCalificacion[rowIndex].getSelectedT());
    }

    public void agregar() {
        estudianteList = new ArrayList();
        calificacionDiplomadoEstList = new ArrayList();
        calificacionDiplomado = new SgCalificacionDiplomado();
    }

    public void actualizar(SgCalificacionDiplomado calDiplomado) {
        try {
            estudianteList = new ArrayList();
            calificacionDiplomadoEstList = new ArrayList();
            calificacionDiplomado = new SgCalificacionDiplomado();
            if (calDiplomado.getCadPk() == null) {
                agregar();
            } else {
                FiltroCalificacionDiplomadoEstudiante fcde = new FiltroCalificacionDiplomadoEstudiante();
                fcde.setCdeCalificacionDiplomadoFk(calDiplomado.getCadPk());
                calificacionDiplomadoEstList = calificacionDiplomadoEstudianteRestClient.buscar(fcde);
                calificacionDiplomado = calDiplomado;
                estudianteList = buscarEstudiantes();
                cargarTabla();
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

            calificacionDiplomado = calificacionDiplomadoRestClient.guardar(calificacionDiplomado);
            actualizar(calificacionDiplomadoRestClient.obtenerPorId(calificacionDiplomado.getCadPk()));
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public SofisComboG<SgDiplomado> getComboDiplomado() {
        return comboDiplomado;
    }

    public void setComboDiplomado(SofisComboG<SgDiplomado> comboDiplomado) {
        this.comboDiplomado = comboDiplomado;
    }

    public SofisComboG<SgModuloDiplomado> getComboModuloDiplomado() {
        return comboModuloDiplomado;
    }

    public void setComboModuloDiplomado(SofisComboG<SgModuloDiplomado> comboModuloDiplomado) {
        this.comboModuloDiplomado = comboModuloDiplomado;
    }

    public SgSede getSedeSeleccionada() {
        return sedeSeleccionada;
    }

    public void setSedeSeleccionada(SgSede sedeSeleccionada) {
        this.sedeSeleccionada = sedeSeleccionada;
    }

    public SofisComboG<SgAnioLectivo> getComboAnioLectivo() {
        return comboAnioLectivo;
    }

    public void setComboAnioLectivo(SofisComboG<SgAnioLectivo> comboAnioLectivo) {
        this.comboAnioLectivo = comboAnioLectivo;
    }

    public List<SelectItem> getComboPeriodos() {
        return comboPeriodos;
    }

    public void setComboPeriodos(List<SelectItem> comboPeriodos) {
        this.comboPeriodos = comboPeriodos;
    }

    public Integer getComboPeriodoSeleccionado() {
        return comboPeriodoSeleccionado;
    }

    public void setComboPeriodoSeleccionado(Integer comboPeriodoSeleccionado) {
        this.comboPeriodoSeleccionado = comboPeriodoSeleccionado;
    }

    public SgModuloDiplomado getModuloDiplomadoEnEdicion() {
        return moduloDiplomadoEnEdicion;
    }

    public void setModuloDiplomadoEnEdicion(SgModuloDiplomado moduloDiplomadoEnEdicion) {
        this.moduloDiplomadoEnEdicion = moduloDiplomadoEnEdicion;
    }

    public SgAnioLectivo getAnioLectivoEnEdicion() {
        return anioLectivoEnEdicion;
    }

    public void setAnioLectivoEnEdicion(SgAnioLectivo anioLectivoEnEdicion) {
        this.anioLectivoEnEdicion = anioLectivoEnEdicion;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public List<SgEstudiante> getEstudianteList() {
        return estudianteList;
    }

    public void setEstudianteList(List<SgEstudiante> estudianteList) {
        this.estudianteList = estudianteList;
    }

    public List<SgCalificacionDiplomadoEstudiante> getCalificacionDiplomadoEstList() {
        return calificacionDiplomadoEstList;
    }

    public void setCalificacionDiplomadoEstList(List<SgCalificacionDiplomadoEstudiante> calificacionDiplomadoEstList) {
        this.calificacionDiplomadoEstList = calificacionDiplomadoEstList;
    }

    public SgCalificacionDiplomado getCalificaciocionDiplomado() {
        return calificacionDiplomado;
    }

    public void setCalificaciocionDiplomado(SgCalificacionDiplomado calificacionDiplomado) {
        this.calificacionDiplomado = calificacionDiplomado;
    }

    public SgCalificacionDiplomado getCalificacionDiplomado() {
        return calificacionDiplomado;
    }

    public void setCalificacionDiplomado(SgCalificacionDiplomado calificacionDiplomado) {
        this.calificacionDiplomado = calificacionDiplomado;
    }

    public Boolean getEscalaNumerica() {
        return escalaNumerica;
    }

    public void setEscalaNumerica(Boolean escalaNumerica) {
        this.escalaNumerica = escalaNumerica;
    }

    public SofisComboG<SgCalificacion>[] getComboCalificacion() {
        return comboCalificacion;
    }

    public void setComboCalificacion(SofisComboG<SgCalificacion>[] comboCalificacion) {
        this.comboCalificacion = comboCalificacion;
    }

    public SgEscalaCalificacion getEscalaCalificacion() {
        return escalaCalificacion;
    }

    public void setEscalaCalificacion(SgEscalaCalificacion escalaCalificacion) {
        this.escalaCalificacion = escalaCalificacion;
    }

    public Long getCalificacionId() {
        return calificacionId;
    }

    public void setCalificacionId(Long calificacionId) {
        this.calificacionId = calificacionId;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public Boolean getCabezalSoloLectura() {
        return cabezalSoloLectura;
    }

    public void setCabezalSoloLectura(Boolean cabezalSoloLectura) {
        this.cabezalSoloLectura = cabezalSoloLectura;
    }

}
