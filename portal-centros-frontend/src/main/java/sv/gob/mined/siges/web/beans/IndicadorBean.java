/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.BooleanUtils;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import org.primefaces.event.TabChangeEvent;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.dto.EstGenerica;
import sv.gob.mined.siges.web.dto.SgCargaExterna;
import sv.gob.mined.siges.web.dto.SgEstIndicador;
import sv.gob.mined.siges.web.dto.SgIndicadorMaterializado;
import sv.gob.mined.siges.web.dto.catalogo.SgEstNombreExtraccion;
import sv.gob.mined.siges.web.enumerados.EnumDesagregacion;
import sv.gob.mined.siges.web.enumerados.EnumTipoNumericoValorEstadistica;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCargaExterna;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEstadisticas;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroIndicadorMaterializado;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.restclient.CargaExternaRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.EstadisticasEstudiantesRestClient;
import sv.gob.mined.siges.web.restclient.IndicadorMaterializadoRestClient;
import sv.gob.mined.siges.web.restclient.IndicadorRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class IndicadorBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(IndicadorBean.class.getName());

    @Inject
    private IndicadorRestClient restClient;

    @Inject
    private HandleArchivoBean handleArchivoBean;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private CatalogosRestClient catalogosClient;

    @Inject
    private EstadisticasEstudiantesRestClient estEstudiantesClient;

    @Inject
    private CargaExternaRestClient cargaExternaClient;

    @Inject
    private IndicadorMaterializadoRestClient indicadorMaterializadoClient;

    @Inject
    @Param(name = "indicador")
    private Long indicadorId;

    private Boolean soloLectura = Boolean.FALSE;
    private String tabActiveId;
    private SgEstIndicador entidadEnEdicion;
    private FiltroEstadisticas filtro = new FiltroEstadisticas();
    private SofisComboG<SgEstNombreExtraccion> comboNombresExtrComp;
    private SofisComboG<EnumDesagregacion> comboDesagregaciones;

    private List<Object> principalesLabels;
    private List<String> desagregacionesLabels;
    private HashMap<Object, HashMap<String, Object>> desagregaciones;
    private String etiquetaDato;
    private Boolean verColDesagregacion; // creado debido a que no funciona a veces el elem.desagregacion != null y no mostraba la columna
    private Boolean resultadoDouble = Boolean.FALSE;
    private Boolean crossTable = Boolean.FALSE;
    private List<EstGenerica> estGenerica;
    private Integer paginado = 50;

    public IndicadorBean() {
    }

    @PostConstruct
    public void init() {
        try {

            if (indicadorId != null && indicadorId > 0) {
                this.actualizar(restClient.obtenerPorId(indicadorId));
                soloLectura = true;
            }
            cargarCombos();
        } catch (HttpClientException ce) {
            JSFUtils.redirectNotFound();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }

    }

    public void cargarCombos() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});
            fc.setOrderBy(new String[]{"nexNombre"});
            fc.setIncluirCampos(new String[]{"nexNombre", "nexVersion"});

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public Object obtenerCantidadDesagregacion(Object labelPrincipal, String labelDesagregacion) {
        if (this.desagregaciones.containsKey(labelPrincipal) && this.desagregaciones.get(labelPrincipal).containsKey(labelDesagregacion)) {
            return this.desagregaciones.get(labelPrincipal).get(labelDesagregacion);
        }
        if (resultadoDouble) {
            return 0d;
        }
        return 0L;
    }

    public void actualizar(SgEstIndicador indicador) {
        try {
            entidadEnEdicion = indicador;

            if (indicador.getIndDesagregaciones().isEmpty()) {
                comboDesagregaciones = new SofisComboG();
                comboDesagregaciones.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            } else {
                comboDesagregaciones = new SofisComboG(indicador.getIndDesagregaciones(), "text");
                comboDesagregaciones.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public Boolean getRenderDesagregacionPanel() {
        SgEstIndicador ind = entidadEnEdicion;
        if (ind != null
                && (ind.getIndCodigo().equals(Constantes.INDICADOR_ESTUDIANTES_POR_SECCION)
                || ind.getIndCodigo().equals(Constantes.INDICADOR_CENTROS_EDUCATIVOS_SEGUN_NIVEL))) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public void convertirListaACrossTable() {
        crossTable = Boolean.TRUE;
        desagregaciones = new HashMap<>();
        Set<String> desagregacionesLabelsSet = new HashSet<>();
        principalesLabels = new ArrayList<>();
        for (EstGenerica e : estGenerica) {
            if (!desagregaciones.containsKey(e.getDato())) {
                principalesLabels.add(e.getDato()); //Ordered
            }
            desagregaciones.computeIfAbsent(e.getDato(), s -> new HashMap<>()).put(e.getDesagregacion(), e.getCantidad());
            if (!desagregacionesLabelsSet.contains(e.getDesagregacion())) {
                desagregacionesLabelsSet.add(e.getDesagregacion());
            }
        }
        desagregacionesLabels = new ArrayList<>(desagregacionesLabelsSet);
        Collections.sort(desagregacionesLabels);
    }

    public void limpiar() {
        filtro = new FiltroEstadisticas();
        comboDesagregaciones.setSelected(-1);
    }

    public void generar() {
        try {

            Boolean error = Boolean.FALSE;

            if (filtro.getAnio() == null) {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, "Debe seleccionar año", "");
                error = Boolean.TRUE;
            }
            SgEstIndicador ind = entidadEnEdicion;

            if (ind == null) {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, "Debe seleccionar un indicador", "");
                error = Boolean.TRUE;
            }

            if (error) {
                return;
            }

            filtro.setDesagregacion(comboDesagregaciones.getSelectedT());
            filtro.setIndicadorPk(ind.getIndPk());

            if (BooleanUtils.isTrue(ind.getIndEsExterno())) {
                etiquetaDato = Etiquetas.getValue("hidentificador");

                FiltroCargaExterna filEx = new FiltroCargaExterna();
                filEx.setAnio(filtro.getAnio());
                filEx.setIndicadorPk(filtro.getIndicadorPk());
                filEx.setNombrePk(filtro.getNombrePk());
                filEx.setDesagregacion(filtro.getDesagregacion());
                if (filtro.getDesagregacion() == null) {
                    filEx.setSinDesagregacion(Boolean.TRUE);
                }
                filEx.setIncluirCampos(new String[]{"carPk", "carTipoNumerico"});

                List<SgCargaExterna> cargas = cargaExternaClient.buscar(filEx);

                if (cargas.isEmpty()) {
                    JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_NO_EXISTE_CARGA_EXTERNA), "");
                    estGenerica = null;
                    return;
                } else {
                    SgCargaExterna c = cargas.get(0);
                    resultadoDouble = EnumTipoNumericoValorEstadistica.DECIMAL.equals(c.getCarTipoNumerico());
                }

                estGenerica = cargaExternaClient.obtenerEstadisticaDeCargaExterna(filtro);
                if (this.comboDesagregaciones.getSelectedT() != null) {
                    verColDesagregacion = Boolean.TRUE;
                    convertirListaACrossTable();
                } else {
                    crossTable = Boolean.FALSE;
                    verColDesagregacion = Boolean.FALSE;
                }

            } else {
                
                etiquetaDato = Etiquetas.getValue("hidentificador");

                FiltroIndicadorMaterializado filEx = new FiltroIndicadorMaterializado();
                filEx.setAnio(filtro.getAnio());
                filEx.setIndicadorPk(filtro.getIndicadorPk());
                filEx.setDesagregacion(filtro.getDesagregacion());
                if (filtro.getDesagregacion() == null) {
                    filEx.setSinDesagregacion(Boolean.TRUE);
                }
                filEx.setIncluirCampos(new String[]{"indPk", "indTipoNumerico"});

                List<SgIndicadorMaterializado> inds = indicadorMaterializadoClient.buscar(filEx);
                        
                if (inds.isEmpty()) {
                    JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_NO_EXISTE_EXTRACCION_MATERIALIZADA), "");
                    estGenerica = null;
                    return;
                } else {
                    SgIndicadorMaterializado c = inds.get(0);
                    resultadoDouble = EnumTipoNumericoValorEstadistica.DECIMAL.equals(c.getIndTipoNumerico());
                }
               
                estGenerica = indicadorMaterializadoClient.obtenerEstadisticaDeIndicadorMaterializado(filtro);

                
                switch (ind.getIndCodigo()) {
                case Constantes.INDICADOR_MATRICULA_NIVEL_EDUCATIVO:
                    etiquetaDato = Etiquetas.getValue("hnivel");
                    if (this.comboDesagregaciones.getSelectedT() != null) {
                        verColDesagregacion = Boolean.TRUE;
                        convertirListaACrossTable();
                    } else {
                        crossTable = Boolean.FALSE;
                        verColDesagregacion = Boolean.FALSE;
                    }
                    break;
                case Constantes.INDICADOR_POBLACION_NO_ESCOLARIZADA_POR_EDAD:
                    etiquetaDato = Etiquetas.getValue("hedad");
                    if (this.comboDesagregaciones.getSelectedT() != null) {
                        verColDesagregacion = Boolean.TRUE;
                        convertirListaACrossTable();
                    } else {
                        crossTable = Boolean.FALSE;
                        verColDesagregacion = Boolean.FALSE;
                    }
                    break;
                case Constantes.INDICADOR_PORCENTAJE_POBLACION_NO_ESCOLARIZADA_POR_EDAD:
                    etiquetaDato = Etiquetas.getValue("hedad");
                    if (this.comboDesagregaciones.getSelectedT() != null) {
                        verColDesagregacion = Boolean.TRUE;
                        convertirListaACrossTable();
                    } else {
                        crossTable = Boolean.FALSE;
                        verColDesagregacion = Boolean.FALSE;
                    }
                    break;
                case Constantes.INDICADOR_PORCENTAJE_ESTUDIANTES_DESERTORES:
                    etiquetaDato = Etiquetas.getValue("hgrado");
                    if (this.comboDesagregaciones.getSelectedT() != null) {
                        verColDesagregacion = Boolean.TRUE;
                        convertirListaACrossTable();
                    } else {
                        crossTable = Boolean.FALSE;
                        verColDesagregacion = Boolean.FALSE;
                    }
                    break;
                case Constantes.INDICADOR_TASA_BRUTA_INGRESO_PRIMER_GRADO_EDUCACION_BASICA:
                    etiquetaDato = "Edad población";
                    if (this.comboDesagregaciones.getSelectedT() != null) {
                        verColDesagregacion = Boolean.TRUE;
                        convertirListaACrossTable();
                    } else {
                        crossTable = Boolean.FALSE;
                        verColDesagregacion = Boolean.FALSE;
                    }
                    break;
                case Constantes.INDICADOR_PORCENTAJE_DE_REPETIDORES:
                    etiquetaDato = Etiquetas.getValue("hgrado");
                    if (this.comboDesagregaciones.getSelectedT() != null) {
                        verColDesagregacion = Boolean.TRUE;
                        convertirListaACrossTable();
                    } else {
                        crossTable = Boolean.FALSE;
                        verColDesagregacion = Boolean.FALSE;
                    }
                    break;
                case Constantes.INDICADOR_TASA_REPETICION:
                    etiquetaDato = Etiquetas.getValue("hgrado");
                    if (this.comboDesagregaciones.getSelectedT() != null) {
                        verColDesagregacion = Boolean.TRUE;
                        convertirListaACrossTable();
                    } else {
                        crossTable = Boolean.FALSE;
                        verColDesagregacion = Boolean.FALSE;
                    }
                    break;
                case Constantes.INDICADOR_TASA_TRANSICION_NIVEL:
                    etiquetaDato = Etiquetas.getValue("hnivel");
                    if (this.comboDesagregaciones.getSelectedT() != null) {
                        verColDesagregacion = Boolean.TRUE;
                        convertirListaACrossTable();
                    } else {
                        crossTable = Boolean.FALSE;
                        verColDesagregacion = Boolean.FALSE;
                    }
                    break;
                case Constantes.INDICADOR_TASA_TRANSICION_CICLO:
                    etiquetaDato = Etiquetas.getValue("hnivel");
                    if (this.comboDesagregaciones.getSelectedT() != null) {
                        verColDesagregacion = Boolean.TRUE;
                        convertirListaACrossTable();
                    } else {
                        crossTable = Boolean.FALSE;
                        verColDesagregacion = Boolean.FALSE;
                    }
                    break;
                case Constantes.INDICADOR_TASA_DESERCION:
                    etiquetaDato = Etiquetas.getValue("hgrado");
                    if (this.comboDesagregaciones.getSelectedT() != null) {
                        verColDesagregacion = Boolean.TRUE;
                        convertirListaACrossTable();
                    } else {
                        crossTable = Boolean.FALSE;
                        verColDesagregacion = Boolean.FALSE;
                    }
                    break;
                case Constantes.INDICADOR_PORCENTAJE_DE_TRABAJADORES:
                    etiquetaDato = null;
                    crossTable = Boolean.FALSE;
                    if (this.comboDesagregaciones.getSelectedT() != null) {
                        verColDesagregacion = Boolean.TRUE;
                    } else {
                        verColDesagregacion = Boolean.FALSE;
                    }
                    break;
                case Constantes.INDICADOR_PORCENTAJE_ESTUDIANTES_DISCAPACIDAD:
                    etiquetaDato = null;
                    crossTable = Boolean.FALSE;
                    if (this.comboDesagregaciones.getSelectedT() != null) {
                        verColDesagregacion = Boolean.TRUE;
                    } else {
                        verColDesagregacion = Boolean.FALSE;
                    }
                    break;
                case Constantes.INDICADOR_DISTRIBUCION_PORCENTUAL_ESTUDIANTES_CON_DISCAPACIDAD:
                    etiquetaDato = "Discapacidad";
                    if (this.comboDesagregaciones.getSelectedT() != null) {
                        verColDesagregacion = Boolean.TRUE;
                        convertirListaACrossTable();
                    } else {
                        crossTable = Boolean.FALSE;
                        verColDesagregacion = Boolean.FALSE;
                    }
                    break;
                case Constantes.INDICADOR_DISTRIBUCION_PORCENTUAL_ESTUDIANTES_SEGUN_CONVIVENCIA_FAMILIAR:
                    etiquetaDato = "Parentesco";
                    if (this.comboDesagregaciones.getSelectedT() != null) {
                        verColDesagregacion = Boolean.TRUE;
                        convertirListaACrossTable();
                    } else {
                        crossTable = Boolean.FALSE;
                        verColDesagregacion = Boolean.FALSE;
                    }
                    break;         
                case Constantes.INDICADOR_TASA_NETA_INGRESO_PRIMER_GRADO_EDUCACION_BASICA:
                    etiquetaDato = null;
                    verColDesagregacion = Boolean.TRUE;
                    crossTable = Boolean.FALSE;
                    if (this.comboDesagregaciones.getSelectedT() != null) {
                        verColDesagregacion = Boolean.TRUE;
                    } else {
                        verColDesagregacion = Boolean.FALSE;
                    }
                    break;
                case Constantes.INDICADOR_TASA_ESPECIFICA_ESCOLARIZACION_POR_EDAD:
                    etiquetaDato = Etiquetas.getValue("hedad");
                    verColDesagregacion = Boolean.TRUE;
                    crossTable = Boolean.FALSE;
                    if (this.comboDesagregaciones.getSelectedT() != null) {
                        verColDesagregacion = Boolean.TRUE;
                    } else {
                        verColDesagregacion = Boolean.FALSE;
                    }
                    break;
                case Constantes.INDICADOR_PORCENTAJE_ESTUDIANTES_PRIMER_GRADO_CON_EXPERIENCIA_EDU_PARVPARVULARIA:
                    etiquetaDato = null;
                    if (this.comboDesagregaciones.getSelectedT() != null) {
                        verColDesagregacion = Boolean.TRUE;
                        convertirListaACrossTable();
                    } else {
                        crossTable = Boolean.FALSE;
                        verColDesagregacion = Boolean.FALSE;
                    }
                    break;
                case Constantes.INDICADOR_DISTRIBUCION_PORCENTUAL_MATRICULA_POR_NIVEL_EDUCATIVO:
                    etiquetaDato = Etiquetas.getValue("hnivel");
                    if (this.comboDesagregaciones.getSelectedT() != null) {
                        verColDesagregacion = Boolean.TRUE;
                        convertirListaACrossTable();
                    } else {
                        crossTable = Boolean.FALSE;
                        verColDesagregacion = Boolean.FALSE;
                    }
                    break;
                case Constantes.INDICADOR_TASA_BRUTA_MATRICULA_NIVEL_EDUCATIVO:
                    etiquetaDato = Etiquetas.getValue("hnivel");
                    if (this.comboDesagregaciones.getSelectedT() != null) {
                        verColDesagregacion = Boolean.TRUE;
                        convertirListaACrossTable();
                    } else {
                        crossTable = Boolean.FALSE;
                        verColDesagregacion = Boolean.FALSE;
                    }
                    break;
                case Constantes.INDICADOR_TASA_NETA_MATRICULA_NIVEL_EDUCATIVO:
                    etiquetaDato = Etiquetas.getValue("hnivel");
                    if (this.comboDesagregaciones.getSelectedT() != null) {
                        verColDesagregacion = Boolean.TRUE;
                        convertirListaACrossTable();
                    } else {
                        crossTable = Boolean.FALSE;
                        verColDesagregacion = Boolean.FALSE;
                    }
                    break;
                case Constantes.INDICADOR_TASA_ESPECIFICA_MATRICULA_GRADO:
                    etiquetaDato = Etiquetas.getValue("hgrado");
                    if (this.comboDesagregaciones.getSelectedT() != null) {
                        verColDesagregacion = Boolean.TRUE;
                        convertirListaACrossTable();
                    } else {
                        crossTable = Boolean.FALSE;
                        verColDesagregacion = Boolean.FALSE;
                    }
                    break;
                case Constantes.INDICADOR_DISTRIBUCION_PORCENTUAL_ESTUDIANTES_ACTIVIDAD_LABORAL:
                    etiquetaDato = Etiquetas.getValue("hedad");
                    verColDesagregacion = Boolean.TRUE;
                    if (this.comboDesagregaciones.getSelectedT() != null) {
                        verColDesagregacion = Boolean.TRUE;
                        convertirListaACrossTable();
                    } else {
                        crossTable = Boolean.FALSE;
                        verColDesagregacion = Boolean.FALSE;
                    }
                    break;
                case Constantes.INDICADOR_PORCENTAJE_ESTUDIANTES_APROBADOS:
                    etiquetaDato = Etiquetas.getValue("hgrado");
                    if (this.comboDesagregaciones.getSelectedT() != null) {
                        verColDesagregacion = Boolean.TRUE;
                        convertirListaACrossTable();
                    } else {
                        crossTable = Boolean.FALSE;
                        verColDesagregacion = Boolean.FALSE;
                    }
                    break;
                case Constantes.INDICADOR_TASA_BRUTA_APROBACION:
                    etiquetaDato = Etiquetas.getValue("hgrado");
                    if (this.comboDesagregaciones.getSelectedT() != null) {
                        verColDesagregacion = Boolean.TRUE;
                        convertirListaACrossTable();
                    } else {
                        crossTable = Boolean.FALSE;
                        verColDesagregacion = Boolean.FALSE;
                    }
                    break;
                case Constantes.INDICADOR_TASA_PROMOCION:
                    etiquetaDato = Etiquetas.getValue("hgrado");
                    if (this.comboDesagregaciones.getSelectedT() != null) {
                        verColDesagregacion = Boolean.TRUE;
                        convertirListaACrossTable();
                    } else {
                        crossTable = Boolean.FALSE;
                        verColDesagregacion = Boolean.FALSE;
                    }
                    break;
                case Constantes.INDICADOR_CENTROS_EDUCATIVOS_SEGUN_NIVEL:
                    verColDesagregacion = Boolean.FALSE;
                    etiquetaDato = Etiquetas.getValue("hnivel");
                    crossTable = Boolean.FALSE;
                    if (this.comboDesagregaciones.getSelectedT() != null) {
                        verColDesagregacion = Boolean.TRUE;
                    } else {
                        verColDesagregacion = Boolean.FALSE;
                    }
                    break;
                case Constantes.INDICADOR_PORCENTAJE_ESTUDIANTES_ACCESO_INTERNET:
                    etiquetaDato = Etiquetas.getValue("hnivel");
                    if (this.comboDesagregaciones.getSelectedT() != null) {
                        verColDesagregacion = Boolean.TRUE;
                        convertirListaACrossTable();
                    } else {
                        crossTable = Boolean.FALSE;
                        verColDesagregacion = Boolean.FALSE;
                    }
                    break;
                case Constantes.INDICADOR_PORCENTAJE_ESTUDIANTES_ACCESO_COMPUTADORA:
                    etiquetaDato = Etiquetas.getValue("hnivel");
                    if (this.comboDesagregaciones.getSelectedT() != null) {
                        verColDesagregacion = Boolean.TRUE;
                        convertirListaACrossTable();
                    } else {
                        crossTable = Boolean.FALSE;
                        verColDesagregacion = Boolean.FALSE;
                    }
                    break;
                case Constantes.INDICADOR_ESTUDIANTES_POR_SECCION:
                    etiquetaDato = Etiquetas.getValue("hnivel");
                    verColDesagregacion = Boolean.FALSE;
                    crossTable = Boolean.FALSE;
                    if (this.comboDesagregaciones.getSelectedT() != null) {
                        verColDesagregacion = Boolean.TRUE;
                    } else {
                        verColDesagregacion = Boolean.FALSE;
                    }
                    break;
                case Constantes.INDICADOR_TASA_BRUTA_INGRESO_GRADO:
                    verColDesagregacion = Boolean.TRUE;
                    etiquetaDato = Etiquetas.getValue("hgrado");
                    crossTable = Boolean.FALSE;
                    if (this.comboDesagregaciones.getSelectedT() != null) {
                        verColDesagregacion = Boolean.TRUE;
                    } else {
                        verColDesagregacion = Boolean.FALSE;
                    }
                    break;
                case Constantes.INDICADOR_PORCENTAJE_CON_SOBRE_EDAD_GRADO:
                    etiquetaDato = Etiquetas.getValue("hgrado");
                    if (this.comboDesagregaciones.getSelectedT() != null) {
                        verColDesagregacion = Boolean.TRUE;
                        convertirListaACrossTable();
                    } else {
                        crossTable = Boolean.FALSE;
                        verColDesagregacion = Boolean.FALSE;
                    }
                    break;
                case Constantes.INDICADOR_DISTRIBUCION_PORCENTUAL_DOCENTES_SEGUN_ANIOS_SERVICIO:
                    etiquetaDato = Etiquetas.getValue("haniosServicio");
                    if (this.comboDesagregaciones.getSelectedT() != null) {
                        verColDesagregacion = Boolean.TRUE;
                        convertirListaACrossTable();
                    } else {
                        crossTable = Boolean.FALSE;
                        verColDesagregacion = Boolean.FALSE;
                    }
                    break;
                case Constantes.INDICADOR_PROMEDIO_ESTUDIANTES_POR_DOCENTE:
                    etiquetaDato = Etiquetas.getValue("hnivel");
                    if (this.comboDesagregaciones.getSelectedT() != null) {
                        verColDesagregacion = Boolean.TRUE;
                        convertirListaACrossTable();
                    } else {
                        crossTable = Boolean.FALSE;
                        verColDesagregacion = Boolean.FALSE;
                    }
                    break;
                case Constantes.INDICADOR_PROMEDIO_DOCENTES_POR_GRADO_ACADEMICO_ALCANZADO:
                    etiquetaDato = Etiquetas.getValue("hnivel");
                    if (this.comboDesagregaciones.getSelectedT() != null) {
                        verColDesagregacion = Boolean.TRUE;
                        convertirListaACrossTable();
                    } else {
                        crossTable = Boolean.FALSE;
                        verColDesagregacion = Boolean.FALSE;
                    }
                    break;
                case Constantes.INDICADOR_PORCENTAJE_DOCENTES_CON_ACCESO_INTERNET:
                    etiquetaDato = null;
                    if (this.comboDesagregaciones.getSelectedT() != null) {
                        verColDesagregacion = Boolean.TRUE;
                        convertirListaACrossTable();
                    } else {
                        crossTable = Boolean.FALSE;
                        verColDesagregacion = Boolean.FALSE;
                    }
                    break;
                case Constantes.INDICADOR_PORCENTAJE_CENTROS_EDUCATIVOS_ACCESO_SERVICIOS_BASICOS:
                    etiquetaDato = Etiquetas.getValue("hservicio");
                    if (this.comboDesagregaciones.getSelectedT() != null) {
                        verColDesagregacion = Boolean.TRUE;
                        convertirListaACrossTable();
                    } else {
                        crossTable = Boolean.FALSE;
                        verColDesagregacion = Boolean.FALSE;
                    }
                    break;
                case Constantes.INDICADOR_PORCENTAJE_DE_DOCENTES_CERTIFICADOS_POR_NIVEL_EDUCATIVO:
                    etiquetaDato = Etiquetas.getValue("hnivel");
                    if (this.comboDesagregaciones.getSelectedT() != null) {
                        verColDesagregacion = Boolean.TRUE;
                        convertirListaACrossTable();
                    } else {
                        crossTable = Boolean.FALSE;
                        verColDesagregacion = Boolean.FALSE;
                    }
                    break;
                case Constantes.INDICADOR_DISTRIBUCION_DE_DOCENTES_SEGUN_NIVEL_EDUCATIVO:
                    etiquetaDato = Etiquetas.getValue("hnivel");
                    if (this.comboDesagregaciones.getSelectedT() != null) {
                        verColDesagregacion = Boolean.TRUE;
                        convertirListaACrossTable();
                    } else {
                        crossTable = Boolean.FALSE;
                        verColDesagregacion = Boolean.FALSE;
                    }
                    break;
                default:
                    return;
            }

            }

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public String getTituloPagina() {
        return entidadEnEdicion.getIndNombre();
    }

    public void changeTab(TabChangeEvent event) {
        this.tabActiveId = event.getTab().getId();
        PrimeFaces.current().executeScript("PF('tabsBlocker').hide()");
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public String getTabActiveId() {
        return tabActiveId;
    }

    public void setTabActiveId(String tabActiveId) {
        this.tabActiveId = tabActiveId;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public Long getIndicadorId() {
        return indicadorId;
    }

    public void setIndicadorId(Long indicadorId) {
        this.indicadorId = indicadorId;
    }

    public SgEstIndicador getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgEstIndicador entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public FiltroEstadisticas getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroEstadisticas filtro) {
        this.filtro = filtro;
    }

    public SofisComboG<SgEstNombreExtraccion> getComboNombresExtrComp() {
        return comboNombresExtrComp;
    }

    public void setComboNombresExtrComp(SofisComboG<SgEstNombreExtraccion> comboNombresExtrComp) {
        this.comboNombresExtrComp = comboNombresExtrComp;
    }

    public IndicadorRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(IndicadorRestClient restClient) {
        this.restClient = restClient;
    }

    public HandleArchivoBean getHandleArchivoBean() {
        return handleArchivoBean;
    }

    public void setHandleArchivoBean(HandleArchivoBean handleArchivoBean) {
        this.handleArchivoBean = handleArchivoBean;
    }

    public EstadisticasEstudiantesRestClient getEstEstudiantesClient() {
        return estEstudiantesClient;
    }

    public void setEstEstudiantesClient(EstadisticasEstudiantesRestClient estEstudiantesClient) {
        this.estEstudiantesClient = estEstudiantesClient;
    }

    public SofisComboG<EnumDesagregacion> getComboDesagregaciones() {
        return comboDesagregaciones;
    }

    public void setComboDesagregaciones(SofisComboG<EnumDesagregacion> comboDesagregaciones) {
        this.comboDesagregaciones = comboDesagregaciones;
    }

    public List<Object> getPrincipalesLabels() {
        return principalesLabels;
    }

    public void setPrincipalesLabels(List<Object> principalesLabels) {
        this.principalesLabels = principalesLabels;
    }

    public List<String> getDesagregacionesLabels() {
        return desagregacionesLabels;
    }

    public void setDesagregacionesLabels(List<String> desagregacionesLabels) {
        this.desagregacionesLabels = desagregacionesLabels;
    }

    public HashMap<Object, HashMap<String, Object>> getDesagregaciones() {
        return desagregaciones;
    }

    public void setDesagregaciones(HashMap<Object, HashMap<String, Object>> desagregaciones) {
        this.desagregaciones = desagregaciones;
    }

    public String getEtiquetaDato() {
        return etiquetaDato;
    }

    public void setEtiquetaDato(String etiquetaDato) {
        this.etiquetaDato = etiquetaDato;
    }

    public Boolean getVerColDesagregacion() {
        return verColDesagregacion;
    }

    public void setVerColDesagregacion(Boolean verColDesagregacion) {
        this.verColDesagregacion = verColDesagregacion;
    }

    public Boolean getResultadoDouble() {
        return resultadoDouble;
    }

    public void setResultadoDouble(Boolean resultadoDouble) {
        this.resultadoDouble = resultadoDouble;
    }

    public Boolean getCrossTable() {
        return crossTable;
    }

    public void setCrossTable(Boolean crossTable) {
        this.crossTable = crossTable;
    }

    public List<EstGenerica> getEstGenerica() {
        return estGenerica;
    }

    public void setEstGenerica(List<EstGenerica> estGenerica) {
        this.estGenerica = estGenerica;
    }

    public Integer getPaginado() {
        return paginado;
    }

    public void setPaginado(Integer paginado) {
        this.paginado = paginado;
    }

}
