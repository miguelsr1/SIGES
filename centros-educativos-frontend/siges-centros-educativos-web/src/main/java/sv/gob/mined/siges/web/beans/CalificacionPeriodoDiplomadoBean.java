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
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgAnioLectivo;
import sv.gob.mined.siges.web.dto.SgCalificacionDiplomadoAux;
import sv.gob.mined.siges.web.dto.SgCalificacionDiplomadoEstudiante;
import sv.gob.mined.siges.web.dto.SgDiplomado;
import sv.gob.mined.siges.web.dto.SgDiplomadosEstudiante;
import sv.gob.mined.siges.web.dto.SgEstudiante;
import sv.gob.mined.siges.web.dto.SgModuloDiplomado;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.enumerados.EnumAnioLectivoEstado;
import sv.gob.mined.siges.web.enumerados.EnumTiposCalificacionDiplomado;
import sv.gob.mined.siges.web.enumerados.TipoEscalaCalificacion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAnioLectivo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCalificacionDiplomadoEstudiante;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDiplomado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDiplomadosEstudiante;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroModuloDiplomado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
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
public class CalificacionPeriodoDiplomadoBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(CalificacionPeriodoDiplomadoBean.class.getName());

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

    private SgModuloDiplomado moduloDiplomadoEnEdicion;
    private SgAnioLectivo anioLectivoEnEdicion;
    private Boolean soloLectura = Boolean.FALSE;
    private Boolean cabezalSoloLectura = Boolean.FALSE;
    private List<SgEstudiante> estudianteList;
    private List<SgCalificacionDiplomadoEstudiante> calificacionDiplomadoEstList;
    private List<Integer> periodos;
    private Boolean existeEstudianteSinCalificacion;

    @Inject
    @Param(name = "id")
    private Long calificacionId;

    @Inject
    @Param(name = "edit")
    private Boolean editable;

    public CalificacionPeriodoDiplomadoBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            validarAcceso();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_CALIFICACION_PERIODO_DIPLOMADO)) {
            LOGGER.log(Level.INFO, sessionBean.getUser().getName() + " - Redirigiendo a inicio. " + ConstantesOperaciones.MENU_CALIFICACION_PERIODO_DIPLOMADO);
            JSFUtils.redirectToIndex();
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
            periodos = new ArrayList();
            estudianteList = new ArrayList();
            calificacionDiplomadoEstList = new ArrayList();
            comboAnioLectivo = new SofisComboG();
            comboAnioLectivo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboDiplomado = new SofisComboG();
            comboDiplomado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboModuloDiplomado = new SofisComboG();
            comboModuloDiplomado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            existeEstudianteSinCalificacion=Boolean.FALSE;
            if (sedeSeleccionada != null) {
                FiltroAnioLectivo fal = new FiltroAnioLectivo();
                fal.setAleEstado(EnumAnioLectivoEstado.ABIERTO);
                fal.setAleSedeFk(sedeSeleccionada.getSedPk());
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
            periodos = new ArrayList();
            estudianteList = new ArrayList();
            calificacionDiplomadoEstList = new ArrayList();
            comboDiplomado = new SofisComboG();
            comboDiplomado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboModuloDiplomado = new SofisComboG();
            comboModuloDiplomado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            existeEstudianteSinCalificacion=Boolean.FALSE;
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
            periodos = new ArrayList();
            estudianteList = new ArrayList();
            calificacionDiplomadoEstList = new ArrayList();
            comboModuloDiplomado = new SofisComboG();
            comboModuloDiplomado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            existeEstudianteSinCalificacion=Boolean.FALSE;
            if (comboDiplomado.getSelectedT() != null) {
                FiltroModuloDiplomado fil = new FiltroModuloDiplomado();
                fil.setDiplomadoFk(comboDiplomado.getSelectedT().getDipPk());

                List<SgModuloDiplomado> modDiplomados = restModuloDiplomado.buscar(fil);
                comboModuloDiplomado = new SofisComboG(modDiplomados, "mdiNombre");
                comboModuloDiplomado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                if (modDiplomados.size() == 1) {
                    comboModuloDiplomado.setSelectedT(modDiplomados.get(0));
                    buscarCalificaciones();
                }
            }
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ce), ce);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void buscarCalificaciones() {
        try {
            periodos = new ArrayList();
            estudianteList = new ArrayList();
            calificacionDiplomadoEstList = new ArrayList();
            existeEstudianteSinCalificacion=Boolean.FALSE;
            if (comboModuloDiplomado.getSelectedT() != null && comboAnioLectivo.getSelectedT() != null && sedeSeleccionada != null && comboDiplomado.getSelectedT() != null) {

                if (comboModuloDiplomado.getSelectedT().getMdiPeriodosCalificacion() == null) {
                    JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, "El módulo seleccionado no tiene todos los períodos calificados.", "");
                    return;
                }
                if (comboModuloDiplomado.getSelectedT().getMdiEscalaCalificacion() == null) {
                    JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, "El módulo del diplomado no tiene escala de calificación.", "");
                    return;
                }

                FiltroDiplomadosEstudiante fde = new FiltroDiplomadosEstudiante();
                fde.setAnioLectivoId(comboAnioLectivo.getSelectedT().getAlePk());
                fde.setSedePk(sedeSeleccionada.getSedPk());
                fde.setDiplomadoId(comboDiplomado.getSelectedT().getDipPk());
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
                estudianteList = dipEst.stream().map(c -> c.getDepEstudiante()).collect(Collectors.toList());

                if (!estudianteList.isEmpty()) {
                    FiltroCalificacionDiplomadoEstudiante fcde = new FiltroCalificacionDiplomadoEstudiante();
                    fcde.setAnioLectivoFk(comboAnioLectivo.getSelectedT().getAlePk());
                    fcde.setModuloDiplomadoFk(comboModuloDiplomado.getSelectedT().getMdiPk());
                    fcde.setSedeFk(sedeSeleccionada.getSedPk());
                    List<EnumTiposCalificacionDiplomado> tipoCalificacion = new ArrayList();
                    tipoCalificacion.add(EnumTiposCalificacionDiplomado.ORD);
                    tipoCalificacion.add(EnumTiposCalificacionDiplomado.NOTIN);
                    fcde.setTipoCalificacionDiplomadoList(tipoCalificacion);
                    calificacionDiplomadoEstList = calificacionDiplomadoEstudianteRestClient.buscar(fcde);
                    
                    Integer cantidad=estudianteList.size();
                    cantidad *=comboModuloDiplomado.getSelectedT().getMdiPeriodosCalificacion();
                    List<SgCalificacionDiplomadoEstudiante> calOrd = calificacionDiplomadoEstList.stream().filter(c -> c.getCdeCalificacionDiplomadoFk().getCadTipoCalificacion().equals(EnumTiposCalificacionDiplomado.ORD)
                    ).collect(Collectors.toList());
                    if(calOrd.size() < cantidad){
                        existeEstudianteSinCalificacion=Boolean.TRUE;
                    }
                }

                for (Integer i = 1; i <= comboModuloDiplomado.getSelectedT().getMdiPeriodosCalificacion(); i++) {
                    periodos.add(i);
                }
                Collections.sort(periodos);
            }
            PrimeFaces.current().executeScript("PF('blocker').hide()");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        PrimeFaces.current().executeScript("PF('blocker').hide()");
    }

    public String buscarCalificacionPeriodoOrdinario(SgEstudiante est, Integer periodo) {

        if (calificacionDiplomadoEstList != null && !calificacionDiplomadoEstList.isEmpty()) {

            Integer precision = null;
            if (comboModuloDiplomado.getSelectedT().getMdiEscalaCalificacion() != null && comboModuloDiplomado.getSelectedT().getMdiEscalaCalificacion().getEcaPrecision() != null) {
                precision = comboModuloDiplomado.getSelectedT().getMdiEscalaCalificacion().getEcaPrecision();
            } else {
                precision = comboModuloDiplomado.getSelectedT().getMdiPrecision();
            }

            List<SgCalificacionDiplomadoEstudiante> calDipEst = calificacionDiplomadoEstList.stream().filter(c -> c.getCdeCalificacionDiplomadoFk().getCadTipoCalificacion().equals(EnumTiposCalificacionDiplomado.ORD)
                    && c.getCdeEstudianteFk().getEstPk().equals(est.getEstPk()) && c.getCdeCalificacionDiplomadoFk().getCadNumeroPeriodo().equals(periodo)
            ).collect(Collectors.toList());
            if (!calDipEst.isEmpty()) {
                String resultado = null;
                if (calDipEst.get(0).getCdeCalificacionNumerica() != null) {
                    resultado = calDipEst.get(0).getCaeCalificacionNumerica(precision);
                }
                if (calDipEst.get(0).getCdeCalificacionConceptualFk() != null) {
                    resultado = calDipEst.get(0).getCdeCalificacionConceptualFk().getCalValor();
                }
              
                return resultado;

            }
        }
        return null;
    }

    public String buscarCalificacionNotaIntitucional(SgEstudiante est) {

        if (calificacionDiplomadoEstList != null && !calificacionDiplomadoEstList.isEmpty()) {

            Integer precision = null;
            if (comboModuloDiplomado.getSelectedT().getMdiEscalaCalificacion() != null && comboModuloDiplomado.getSelectedT().getMdiEscalaCalificacion().getEcaPrecision() != null) {
                precision = comboModuloDiplomado.getSelectedT().getMdiEscalaCalificacion().getEcaPrecision();
            } else {
                precision = comboModuloDiplomado.getSelectedT().getMdiPrecision();
            }

            List<SgCalificacionDiplomadoEstudiante> calDipEst = calificacionDiplomadoEstList.stream().filter(c -> c.getCdeCalificacionDiplomadoFk().getCadTipoCalificacion().equals(EnumTiposCalificacionDiplomado.NOTIN)
                    && c.getCdeEstudianteFk().getEstPk().equals(est.getEstPk())
            ).collect(Collectors.toList());

            if (!calDipEst.isEmpty()) {
                String resultado = null;
                if (calDipEst.get(0).getCdeCalificacionNumerica() != null) {
                    resultado = calDipEst.get(0).getCaeCalificacionNumerica(precision);
                }
                if (calDipEst.get(0).getCdeCalificacionConceptualFk() != null) {
                    resultado = calDipEst.get(0).getCdeCalificacionConceptualFk().getCalValor();
                }
                return resultado;

            }
        }
        return null;
    }

    public void calcularNotaIntitucional() {
        try {
            SgCalificacionDiplomadoAux calDipAux = new SgCalificacionDiplomadoAux();
            calDipAux.setAnioLectivoFk(comboAnioLectivo.getSelectedT() != null ? comboAnioLectivo.getSelectedT().getAlePk() : null);
            calDipAux.setDiplomadoFk(comboDiplomado.getSelectedT() != null ? comboDiplomado.getSelectedT().getDipPk() : null);
            calDipAux.setModuloDiplomadoFk(comboModuloDiplomado.getSelectedT() != null ? comboModuloDiplomado.getSelectedT().getMdiPk() : null);
            calDipAux.setSedeFk(sedeSeleccionada != null ? sedeSeleccionada.getSedPk() : null);

            calificacionDiplomadoRestClient.calcularNotaInstitucional(calDipAux);
            buscarCalificaciones();
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public String colorRow(SgEstudiante est) {       

        List<SgCalificacionDiplomadoEstudiante> calEstList = calificacionDiplomadoEstList.stream().filter(c -> c.getCdeEstudianteFk().getEstPk().equals(est.getEstPk()) && c.getCdeCalificacionDiplomadoFk().getCadTipoCalificacion().equals(EnumTiposCalificacionDiplomado.NOTIN)).collect(Collectors.toList());

        if (!calEstList.isEmpty()) {
            SgCalificacionDiplomadoEstudiante calEst = calEstList.get(0);
            if (TipoEscalaCalificacion.NUMERICA.equals(calEst.getCdeCalificacionDiplomadoFk().getCadModuloDiplomado().getMdiEscalaCalificacion().getEcaTipoEscala())) {
                Double res = Double.parseDouble(calEst.getCdeCalificacionNumerica());
                if (calEst.getCdeCalificacionDiplomadoFk().getCadModuloDiplomado().getMdiEscalaCalificacion().getEcaMinimoAprobacion() != null) {
                    if (Double.compare(res, calEst.getCdeCalificacionDiplomadoFk().getCadModuloDiplomado().getMdiEscalaCalificacion().getEcaMinimoAprobacion()) >= 0) {
                        return "aprobado";
                    } else {
                        return "reprobado";
                    }
                }
            }
            if (TipoEscalaCalificacion.CONCEPTUAL.equals(calEst.getCdeCalificacionDiplomadoFk().getCadModuloDiplomado().getMdiEscalaCalificacion().getEcaTipoEscala())) {
                if (calEst.getCdeCalificacionConceptualFk().getCalOrden() != null && calEst.getCdeCalificacionDiplomadoFk().getCadModuloDiplomado().getMdiEscalaCalificacion().getEcaValorMinimo() != null && calEst.getCdeCalificacionDiplomadoFk().getCadModuloDiplomado().getMdiEscalaCalificacion().getEcaValorMinimo().getCalOrden() != null) {
                    if (calEst.getCdeCalificacionConceptualFk().getCalOrden() >= calEst.getCdeCalificacionDiplomadoFk().getCadModuloDiplomado().getMdiEscalaCalificacion().getEcaValorMinimo().getCalOrden() ) {
                        return "aprobado";
                    } else {
                        return "reprobado";
                    }
                }
            }
        }
     
        return null;
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

    public List<Integer> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<Integer> periodos) {
        this.periodos = periodos;
    }

    public Boolean getExisteEstudianteSinCalificacion() {
        return existeEstudianteSinCalificacion;
    }

    public void setExisteEstudianteSinCalificacion(Boolean existeEstudianteSinCalificacion) {
        this.existeEstudianteSinCalificacion = existeEstudianteSinCalificacion;
    }

}
