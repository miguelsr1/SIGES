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
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgEstudiante;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.dto.SgComponentePlanEstudio;
import sv.gob.mined.siges.web.dto.catalogo.SgConfiguracion;
import sv.gob.mined.siges.web.dto.catalogo.SgTrastornoAprendizaje;
import sv.gob.mined.siges.web.graficos.DataColumna;
import sv.gob.mined.siges.web.graficos.GraficoColumnas;
import sv.gob.mined.siges.web.graficos.GraficoRadar;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.ComponentePlanEstudioRestClient;
import sv.gob.mined.siges.web.restclient.EstudianteRestClient;
import sv.gob.mined.siges.web.restclient.GraficosRestClient;
import sv.gob.mined.siges.web.restclient.PersonaRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class EstudianteEvolucionBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(EstudianteEvolucionBean.class.getName());

    @Inject
    @Param(name = "id")
    private Long estId;

    @Inject
    @Param(name = "edit")
    private Boolean editable;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private GraficosRestClient graficosClient;

    @Inject
    private ComponentePlanEstudioRestClient componentePlanEstudioClient;

    @Inject
    private CatalogosRestClient catalogoClient;

    @Inject
    private PersonaRestClient personaRestClient;

    @Inject
    private EstudianteRestClient estudianteRestClient;

    private SgEstudiante entidadEnEdicion;

    private SofisComboG<SgComponentePlanEstudio> comboComponentePlanEstudioNumericos;
    private SofisComboG<SgComponentePlanEstudio> comboComponentePlanEstudioNumericosORDActuales;
    private SofisComboG<SgComponentePlanEstudio> comboComponentePlanEstudioConceptuales;

    private GraficoColumnas graficoCalificacionesNumericas;
    private GraficoColumnas graficoPromedioCalificacionesNumericas;
    private GraficoColumnas graficoCalificacionesConceptuales = new GraficoColumnas();
    private GraficoColumnas graficoAsistencias;
    private GraficoColumnas graficoAsistenciasMesEnAnio = new GraficoColumnas();
    private GraficoColumnas graficoMotInasistencias = new GraficoColumnas();
    private GraficoColumnas graficoMotInasistenciasMesEnAnio = new GraficoColumnas();
    private GraficoColumnas graficoCalificacionesORDActuales = new GraficoColumnas();

    private GraficoRadar graficoAlertas = new GraficoRadar();

    private Boolean renderGraficoCalificacionesNumericas = Boolean.FALSE;
    private Boolean renderGraficoCalificacionesConceptuales = Boolean.FALSE;
    private Boolean renderGraficoPromedioCalificacionesNumericas = Boolean.FALSE;
    private Boolean renderGraficoAsistencias = Boolean.FALSE;
    private Boolean renderGraficoAsistenciasPorMesEnAnio = Boolean.FALSE;
    private Boolean renderGraficoMotivosInasistencias = Boolean.FALSE;
    private Boolean renderGraficoMotivosInastenciasPorMesEnAnio = Boolean.FALSE;
    private Boolean renderGraficoAlertas = Boolean.FALSE;
    private Boolean renderGraficoPredictibilidadDesercion = Boolean.FALSE;
    private Boolean renderComboCalificacionesNumericasORDActuales = Boolean.FALSE;
    private Boolean renderGraficoCalificacionesNumericasORDActuales = Boolean.FALSE;
    private List<SgTrastornoAprendizaje> listTrastornos = new ArrayList();

    private String mensajeEvolucionEstudiante;
    private String mensajeAlertasTempranas;
    private String mensajePredictibilidadDesercion;
    private int[] rangoColoresGauge = new int[]{75, 90, 100};
    private Double probabilidadDesercion = 0d;

    private List<SelectItem> comboAniosAsistencia;
    private Integer comboAnioAsistenciasSeleccionado;

    private List<SelectItem> comboAniosInasistencia;
    private Integer comboAnioInasistenciasSeleccionado;

    public EstudianteEvolucionBean() {
    }

    @PostConstruct
    public void init() {
        try {
            comboComponentePlanEstudioNumericos = new SofisComboG();
            comboComponentePlanEstudioNumericos.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboComponentePlanEstudioConceptuales = new SofisComboG();
            comboComponentePlanEstudioConceptuales.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboComponentePlanEstudioNumericosORDActuales = new SofisComboG();
            comboComponentePlanEstudioNumericosORDActuales.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            SgConfiguracion c = catalogoClient.buscarConfiguracionPorCodigo(Constantes.CONFIG_MENSAJE_EVOLUCION_ESTUDIANTE);
            if (c != null && !StringUtils.isBlank(c.getConValor())) {
                mensajeEvolucionEstudiante = c.getConValor();
            }

            c = catalogoClient.buscarConfiguracionPorCodigo(Constantes.CONFIG_MENSAJE_EVOLUCION_ESTUDIANTE_ALERTAS_TEMPRANAS);
            if (c != null && !StringUtils.isBlank(c.getConValor())) {
                mensajeAlertasTempranas = c.getConValor();
            }

            c = catalogoClient.buscarConfiguracionPorCodigo(Constantes.CONFIG_MENSAJE_EVOLUCION_ESTUDIANTE_PREDICTIBILIDAD_DESERCION);
            if (c != null && !StringUtils.isBlank(c.getConValor())) {
                mensajePredictibilidadDesercion = c.getConValor();
            }

            SgConfiguracion cr = catalogoClient.buscarConfiguracionPorCodigo(Constantes.CONFIG_RANGO_COLORES_GAUGE_PREDICTIBILIDAD_DESERCION);
            if (cr != null && !StringUtils.isBlank(cr.getConValor())) {
                rangoColoresGauge = Stream.of(cr.getConValor().split(",")).mapToInt(Integer::parseInt).toArray();
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }

    }

    public void buscarTrastornos() {
        try {
            listTrastornos = personaRestClient.obtenerTrastornos(entidadEnEdicion.getEstPersona().getPerPk());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }

    }

    public void cargarCombos() {
        try {

            comboComponentePlanEstudioNumericos = new SofisComboG(componentePlanEstudioClient.obtenerComponentesPlanEstudioNumericosCursadosEstudiante(entidadEnEdicion.getEstPk()), "cpeNombre");
            comboComponentePlanEstudioNumericos.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboComponentePlanEstudioConceptuales = new SofisComboG(componentePlanEstudioClient.obtenerComponentesPlanEstudioConceptualesCursadosEstudiante(entidadEnEdicion.getEstPk()), "cpeNombre");
            comboComponentePlanEstudioConceptuales.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboComponentePlanEstudioNumericosORDActuales = new SofisComboG(componentePlanEstudioClient.obtenerComponentesPlanEstudioNumericosActualesEstudiante(entidadEnEdicion.getEstPk()), "cpeNombre");
            comboComponentePlanEstudioNumericosORDActuales.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            if (!comboComponentePlanEstudioNumericosORDActuales.getAllTs().isEmpty()) {
                renderComboCalificacionesNumericasORDActuales = Boolean.TRUE;
            } else {
                renderComboCalificacionesNumericasORDActuales = Boolean.FALSE;
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void recargarYRenderizarCalificacionesNumericas() {
        try {
            cargarCalificacionesNumericas();
            PrimeFaces.current().executeScript("renderizarCalificacionesNumericas()");
            PrimeFaces.current().executeScript("PF('blocker').hide()");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }
    }

    public void recargarYRenderizarCalificacionesNumericasORDActuales() {
        try {
            cargarCalificacionesNumericasORDActuales();
            PrimeFaces.current().executeScript("renderizarCalificacionesNumericasORDActuales()");
            PrimeFaces.current().executeScript("PF('blocker').hide()");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }
    }

    public void recargarYRenderizarCalificacionesConceptuales() {
        try {
            cargarCalificacionesConceptuales();
            PrimeFaces.current().executeScript("renderizarCalificacionesConceptuales()");
            PrimeFaces.current().executeScript("PF('blocker').hide()");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }
    }

    public void renderizarAsistenciasEnAnio() {
        try {
            //No hace falta recargar ya que es siempre la misma
            renderGraficoAsistenciasPorMesEnAnio = Boolean.FALSE;
            PrimeFaces.current().executeScript("renderizarAsistencias()");
            PrimeFaces.current().executeScript("PF('blocker').hide()");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }

    }

    public void recargarYRenderizarAsistenciasPorMesEnAnio() {
        try {
            cargarAsistenciasPorMesEnAnio();
            renderGraficoAsistenciasPorMesEnAnio = Boolean.TRUE;
            PrimeFaces.current().executeScript("renderizarAsistenciasMesEnAnio()");
            PrimeFaces.current().executeScript("PF('blocker').hide()");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }
    }

    public void renderizarMotInasistenciasEnAnio() {
        try {
            //No hace falta recargar ya que es siempre la misma
            renderGraficoMotivosInastenciasPorMesEnAnio = Boolean.FALSE;
            PrimeFaces.current().executeScript("renderizarMotInasistencias()");
            PrimeFaces.current().executeScript("PF('blocker').hide()");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }

    }

    public void recargarYRenderizarMotInasistenciasPorMesEnAnio() {
        try {
            cargarMotInasistenciasPorMesEnAnio();
            renderGraficoMotivosInastenciasPorMesEnAnio = Boolean.TRUE;
            PrimeFaces.current().executeScript("renderizarMotInasistenciasMesEnAnio()");
            PrimeFaces.current().executeScript("PF('blocker').hide()");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }
    }

    public void anioAsistenciaSelected() {
        if (this.comboAnioAsistenciasSeleccionado == 0) {
            renderizarAsistenciasEnAnio();
        } else {
            recargarYRenderizarAsistenciasPorMesEnAnio();
        }
    }

    public void anioMotInasistenciaSelected() {
        if (this.comboAnioInasistenciasSeleccionado == 0) {
            renderizarMotInasistenciasEnAnio();
        } else {
            recargarYRenderizarMotInasistenciasPorMesEnAnio();
        }
    }

    private void cargarAlertas() {
        try {
            graficoAlertas = graficosClient.obtenerAlertasEstudiante(entidadEnEdicion.getEstPk());

            boolean sinAlertas = true;
            if (graficoAlertas != null && graficoAlertas.getValores() != null) {
                for (Double d : graficoAlertas.getValores()) {
                    if (d != null && !d.equals(0d)) {
                        sinAlertas = false;
                        break;
                    }
                }
            }
            if (graficoAlertas == null || graficoAlertas.getValores() == null || sinAlertas) {
                renderGraficoAlertas = Boolean.FALSE;
            } else {
                renderGraficoAlertas = Boolean.TRUE;
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }

    }

    private void cargarPredictibilidadDesercion() {
        try {
            renderGraficoPredictibilidadDesercion = Boolean.TRUE;
            probabilidadDesercion = estudianteRestClient.prediccionDesercion(entidadEnEdicion.getEstPk()).getPorcentaje();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }

    }

    private void cargarAsistenciasPorMesEnAnio() {
        try {
            graficoAsistenciasMesEnAnio = graficosClient.obtenerAsistenciasPorMesEnAnio(entidadEnEdicion.getEstPk(), this.comboAnioAsistenciasSeleccionado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }
    }

    private void cargarMotInasistenciasPorMesEnAnio() {
        try {
            graficoMotInasistenciasMesEnAnio = graficosClient.obtenerMotivosInasistenciasPorMesEnAnio(entidadEnEdicion.getEstPk(), this.comboAnioInasistenciasSeleccionado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }
    }

    private void cargarMotInasistencias() {
        try {
            graficoMotInasistencias = graficosClient.obtenerMotivosInasistenciasPorMesEnAnio(entidadEnEdicion.getEstPk());
            if (graficoMotInasistencias.getValores() != null && graficoMotInasistencias.getValores().size() > 0) {
                renderGraficoMotivosInasistencias = Boolean.TRUE;
            } else {
                renderGraficoMotivosInasistencias = Boolean.FALSE;
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }

    }

    private void cargarAsistencias() {
        try {
            graficoAsistencias = graficosClient.obtenerAsistenciasEstudiantePorAnio(entidadEnEdicion.getEstPk());
            if (graficoAsistencias.getValores() != null && graficoAsistencias.getValores().size() > 0) {
                renderGraficoAsistencias = Boolean.TRUE;

                comboAniosAsistencia = new ArrayList();
                comboAniosAsistencia.add(new SelectItem(0, Etiquetas.getValue("comboEmptyItem")));

                comboAniosInasistencia = new ArrayList();
                comboAniosInasistencia.add(new SelectItem(0, Etiquetas.getValue("comboEmptyItem")));

                for (DataColumna dc : graficoAsistencias.getValores()) {
                    Integer anio = (Integer) dc.getRotulo();
                    comboAniosAsistencia.add(new SelectItem(anio, anio.toString()));
                    comboAniosInasistencia.add(new SelectItem(anio, anio.toString()));
                }

            } else {
                renderGraficoAsistencias = Boolean.FALSE;
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }
    }

    private void cargarCalificacionesNumericas() {
        try {
            if (!comboComponentePlanEstudioNumericos.getAllTs().isEmpty()) {
                Long cpeNumericoPk = this.comboComponentePlanEstudioNumericos.getSelectedT() != null ? this.comboComponentePlanEstudioNumericos.getSelectedT().getCpePk() : null;
                graficoCalificacionesNumericas = graficosClient.obtenerCalificacionesAPRNumericasEstudiantePorGradoYComponente(entidadEnEdicion.getEstPk(), cpeNumericoPk);
                renderGraficoCalificacionesNumericas = Boolean.TRUE;

            } else {
                renderGraficoCalificacionesNumericas = Boolean.FALSE;

            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }
    }

    private void cargarCalificacionesNumericasORDActuales() {
        try {
            if (this.comboComponentePlanEstudioNumericosORDActuales.getSelectedT() == null) {
                graficoCalificacionesORDActuales = null;
                renderGraficoCalificacionesNumericasORDActuales = Boolean.FALSE;
            } else {
                Long cpeNumericoPk = this.comboComponentePlanEstudioNumericosORDActuales.getSelectedT().getCpePk();
                graficoCalificacionesORDActuales = graficosClient.obtenerCalificacionesNumericasOrdinariasActualesEstudiantePorGradoYComponente(entidadEnEdicion.getEstPk(), cpeNumericoPk);
                renderGraficoCalificacionesNumericasORDActuales = Boolean.TRUE;
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }
    }

    private void cargarPromedioCalificacionesNumericas() {
        try {
            if (!comboComponentePlanEstudioNumericos.getAllTs().isEmpty()) {
                graficoPromedioCalificacionesNumericas = graficosClient.obtenerCalificacionesNumericasAprobacionEstudiantePorGradoComparadoContraPromedioNacionalYSede(entidadEnEdicion.getEstPk());
                renderGraficoPromedioCalificacionesNumericas = Boolean.TRUE;
            } else {
                renderGraficoPromedioCalificacionesNumericas = Boolean.FALSE;
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }
    }

    private void cargarCalificacionesConceptuales() {
        try {
            if (!comboComponentePlanEstudioConceptuales.getAllTs().isEmpty()) {
                Long cpeConceptualPk = this.comboComponentePlanEstudioConceptuales.getSelectedT() != null ? this.comboComponentePlanEstudioConceptuales.getSelectedT().getCpePk() : null;
                graficoCalificacionesConceptuales = graficosClient.obtenerCalificacionesAPRConceptualesEstudiantePorGradoYComponente(entidadEnEdicion.getEstPk(), cpeConceptualPk);
                renderGraficoCalificacionesConceptuales = Boolean.TRUE;
            } else {
                renderGraficoCalificacionesConceptuales = Boolean.FALSE;
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }
    }

    public void cargarGraficas() {
        cargarCalificacionesNumericas();
        cargarCalificacionesConceptuales();
        cargarPromedioCalificacionesNumericas();
        cargarAsistencias();
        cargarMotInasistencias();
        cargarAlertas();
        cargarPredictibilidadDesercion();
        PrimeFaces.current().executeScript("PF('blocker').hide()");
    }

    public void actualizar(SgEstudiante est) {
        try {
            if (est != null) {
                entidadEnEdicion = est;
                cargarCombos();
                cargarGraficas();
                buscarTrastornos();
            }
            JSFUtils.limpiarMensajesError();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public SgEstudiante getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public GraficoColumnas getGraficoCalificacionesNumericas() {
        return graficoCalificacionesNumericas;
    }

    public SofisComboG<SgComponentePlanEstudio> getComboComponentePlanEstudioNumericos() {
        return comboComponentePlanEstudioNumericos;
    }

    public void setComboComponentePlanEstudioNumericos(SofisComboG<SgComponentePlanEstudio> comboComponentePlanEstudioNumericos) {
        this.comboComponentePlanEstudioNumericos = comboComponentePlanEstudioNumericos;
    }

    public SofisComboG<SgComponentePlanEstudio> getComboComponentePlanEstudioConceptuales() {
        return comboComponentePlanEstudioConceptuales;
    }

    public void setComboComponentePlanEstudioConceptuales(SofisComboG<SgComponentePlanEstudio> comboComponentePlanEstudioConceptuales) {
        this.comboComponentePlanEstudioConceptuales = comboComponentePlanEstudioConceptuales;
    }

    public GraficoColumnas getGraficoCalificacionesConceptuales() {
        return graficoCalificacionesConceptuales;
    }

    public GraficoColumnas getGraficoPromedioCalificacionesNumericas() {
        return graficoPromedioCalificacionesNumericas;
    }

    public GraficoColumnas getGraficoAsistencias() {
        return graficoAsistencias;
    }

    public void setGraficoAsistencias(GraficoColumnas graficoAsistencias) {
        this.graficoAsistencias = graficoAsistencias;
    }

    public Boolean getRenderGraficoCalificacionesNumericas() {
        return renderGraficoCalificacionesNumericas;
    }

    public void setRenderGraficoCalificacionesNumericas(Boolean renderGraficoCalificacionesNumericas) {
        this.renderGraficoCalificacionesNumericas = renderGraficoCalificacionesNumericas;
    }

    public Boolean getRenderGraficoCalificacionesConceptuales() {
        return renderGraficoCalificacionesConceptuales;
    }

    public void setRenderGraficoCalificacionesConceptuales(Boolean renderGraficoCalificacionesConceptuales) {
        this.renderGraficoCalificacionesConceptuales = renderGraficoCalificacionesConceptuales;
    }

    public Boolean getRenderGraficoPromedioCalificacionesNumericas() {
        return renderGraficoPromedioCalificacionesNumericas;
    }

    public void setRenderGraficoPromedioCalificacionesNumericas(Boolean renderGraficoPromedioCalificacionesNumericas) {
        this.renderGraficoPromedioCalificacionesNumericas = renderGraficoPromedioCalificacionesNumericas;
    }

    public Boolean getRenderGraficoAsistencias() {
        return renderGraficoAsistencias;
    }

    public void setRenderGraficoAsistencias(Boolean renderGraficoAsistencias) {
        this.renderGraficoAsistencias = renderGraficoAsistencias;
    }

    public Boolean getRenderGraficoAlertas() {
        return renderGraficoAlertas;
    }

    public void setRenderGraficoAlertas(Boolean renderGraficoAlertas) {
        this.renderGraficoAlertas = renderGraficoAlertas;
    }

    public GraficoRadar getGraficoAlertas() {
        return graficoAlertas;
    }

    public String getMensajeEvolucionEstudiante() {
        return mensajeEvolucionEstudiante;
    }

    public void setMensajeEvolucionEstudiante(String mensajeEvolucionEstudiante) {
        this.mensajeEvolucionEstudiante = mensajeEvolucionEstudiante;
    }

    public List<SgTrastornoAprendizaje> getListTrastornos() {
        return listTrastornos;
    }

    public void setListTrastornos(List<SgTrastornoAprendizaje> listTrastornos) {
        this.listTrastornos = listTrastornos;
    }

    public GraficoColumnas getGraficoAsistenciasMesEnAnio() {
        return graficoAsistenciasMesEnAnio;
    }

    public void setGraficoAsistenciasMesEnAnio(GraficoColumnas graficoAsistenciasMesEnAnio) {
        this.graficoAsistenciasMesEnAnio = graficoAsistenciasMesEnAnio;
    }

    public Boolean getRenderGraficoAsistenciasPorMesEnAnio() {
        return renderGraficoAsistenciasPorMesEnAnio;
    }

    public List<SelectItem> getComboAniosAsistencia() {
        return comboAniosAsistencia;
    }

    public void setComboAniosAsistencia(List<SelectItem> comboAniosAsistencia) {
        this.comboAniosAsistencia = comboAniosAsistencia;
    }

    public Integer getComboAnioAsistenciasSeleccionado() {
        return comboAnioAsistenciasSeleccionado;
    }

    public void setComboAnioAsistenciasSeleccionado(Integer comboAnioAsistenciasSeleccionado) {
        this.comboAnioAsistenciasSeleccionado = comboAnioAsistenciasSeleccionado;
    }

    public List<SelectItem> getComboAniosInasistencia() {
        return comboAniosInasistencia;
    }

    public void setComboAniosInasistencia(List<SelectItem> comboAniosInasistencia) {
        this.comboAniosInasistencia = comboAniosInasistencia;
    }

    public Integer getComboAnioInasistenciasSeleccionado() {
        return comboAnioInasistenciasSeleccionado;
    }

    public void setComboAnioInasistenciasSeleccionado(Integer comboAnioInasistenciasSeleccionado) {
        this.comboAnioInasistenciasSeleccionado = comboAnioInasistenciasSeleccionado;
    }

    public Boolean getRenderGraficoMotivosInastenciasPorMesEnAnio() {
        return renderGraficoMotivosInastenciasPorMesEnAnio;
    }

    public void setRenderGraficoMotivosInastenciasPorMesEnAnio(Boolean renderGraficoMotivosInastenciasPorMesEnAnio) {
        this.renderGraficoMotivosInastenciasPorMesEnAnio = renderGraficoMotivosInastenciasPorMesEnAnio;
    }

    public GraficoColumnas getGraficoMotInasistencias() {
        return graficoMotInasistencias;
    }

    public void setGraficoMotInasistencias(GraficoColumnas graficoMotInasistencias) {
        this.graficoMotInasistencias = graficoMotInasistencias;
    }

    public GraficoColumnas getGraficoMotInasistenciasMesEnAnio() {
        return graficoMotInasistenciasMesEnAnio;
    }

    public void setGraficoMotInasistenciasMesEnAnio(GraficoColumnas graficoMotInasistenciasMesEnAnio) {
        this.graficoMotInasistenciasMesEnAnio = graficoMotInasistenciasMesEnAnio;
    }

    public Boolean getRenderGraficoMotivosInasistencias() {
        return renderGraficoMotivosInasistencias;
    }

    public void setRenderGraficoMotivosInasistencias(Boolean renderGraficoMotivosInasistencias) {
        this.renderGraficoMotivosInasistencias = renderGraficoMotivosInasistencias;
    }

    public Boolean getRenderGraficoPredictibilidadDesercion() {
        return renderGraficoPredictibilidadDesercion;
    }

    public void setRenderGraficoPredictibilidadDesercion(Boolean renderGraficoPredictibilidadDesercion) {
        this.renderGraficoPredictibilidadDesercion = renderGraficoPredictibilidadDesercion;
    }

    public int[] getRangoColoresGauge() {
        return rangoColoresGauge;
    }

    public void setRangoColoresGauge(int[] rangoColoresGauge) {
        this.rangoColoresGauge = rangoColoresGauge;
    }

    public Double getProbabilidadDesercion() {
        return probabilidadDesercion;
    }

    public void setProbabilidadDesercion(Double probabilidadDesercion) {
        this.probabilidadDesercion = probabilidadDesercion;
    }

    public String getMensajeAlertasTempranas() {
        return mensajeAlertasTempranas;
    }

    public String getMensajePredictibilidadDesercion() {
        return mensajePredictibilidadDesercion;
    }

    public GraficoColumnas getGraficoCalificacionesORDActuales() {
        return graficoCalificacionesORDActuales;
    }

    public void setGraficoCalificacionesORDActuales(GraficoColumnas graficoCalificacionesORDActuales) {
        this.graficoCalificacionesORDActuales = graficoCalificacionesORDActuales;
    }

    public Boolean getRenderGraficoCalificacionesNumericasORDActuales() {
        return renderGraficoCalificacionesNumericasORDActuales;
    }

    public void setRenderGraficoCalificacionesNumericasORDActuales(Boolean renderGraficoCalificacionesNumericasORDActuales) {
        this.renderGraficoCalificacionesNumericasORDActuales = renderGraficoCalificacionesNumericasORDActuales;
    }

    public SofisComboG<SgComponentePlanEstudio> getComboComponentePlanEstudioNumericosORDActuales() {
        return comboComponentePlanEstudioNumericosORDActuales;
    }

    public void setComboComponentePlanEstudioNumericosORDActuales(SofisComboG<SgComponentePlanEstudio> comboComponentePlanEstudioNumericosORDActuales) {
        this.comboComponentePlanEstudioNumericosORDActuales = comboComponentePlanEstudioNumericosORDActuales;
    }

    public Boolean getRenderComboCalificacionesNumericasORDActuales() {
        return renderComboCalificacionesNumericasORDActuales;
    }

    public void setRenderComboCalificacionesNumericasORDActuales(Boolean renderComboCalificacionesNumericasORDActuales) {
        this.renderComboCalificacionesNumericasORDActuales = renderComboCalificacionesNumericasORDActuales;
    }
    
    

}
