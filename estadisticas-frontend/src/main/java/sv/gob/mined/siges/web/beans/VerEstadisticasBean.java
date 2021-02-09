/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.cdi.Param;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.EstGenerica;
import sv.gob.mined.siges.web.dto.SgCargaExterna;
import sv.gob.mined.siges.web.dto.SgEstIndicador;
import sv.gob.mined.siges.web.dto.SgIndicadorMaterializado;
import sv.gob.mined.siges.web.dto.catalogo.SgEstCategoriaIndicador;
import sv.gob.mined.siges.web.dto.catalogo.SgEstNombreExtraccion;
import sv.gob.mined.siges.web.enumerados.EnumDesagregacion;
import sv.gob.mined.siges.web.enumerados.EnumTipoNumericoValorEstadistica;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCargaExterna;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroIndicadores;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEstadisticas;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroIndicadorMaterializado;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CargaExternaRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.EstadisticasEstudiantesRestClient;
import sv.gob.mined.siges.web.restclient.IndicadorMaterializadoRestClient;
import sv.gob.mined.siges.web.restclient.IndicadorRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class VerEstadisticasBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(VerEstadisticasBean.class.getName());

    @Inject
    private IndicadorRestClient indicadoresClient;

    @Inject
    private CatalogosRestClient catalogosClient;

    @Inject
    private EstadisticasEstudiantesRestClient estEstudiantesClient;

    @Inject
    private CargaExternaRestClient cargaExternaClient;

    @Inject
    private IndicadorMaterializadoRestClient inciadorMaterializadoClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    @Param(name = "n")
    private Long nombrePk;

    @Inject
    @Param(name = "c")
    private Long categoriaPk;

    @Inject
    @Param(name = "i")
    private Long indicadorPk;

    @Inject
    @Param(name = "d")
    private String desagregacion;

    @Inject
    @Param(name = "a")
    private Integer anio;

    private Integer paginado = 50;
    private FiltroEstadisticas filtro = new FiltroEstadisticas();
    private SofisComboG<SgEstIndicador> comboIndicadores;
    private SofisComboG<SgEstCategoriaIndicador> comboCategorias;
    private SofisComboG<SgEstNombreExtraccion> comboNombresExtr;
    private SofisComboG<EnumDesagregacion> comboDesagregaciones;

    private SofisComboG<SgEstNombreExtraccion> comboNombresExtrComp;

    private List<EstGenerica> estGenerica;

    private List<Object> principalesLabels;
    private List<String> desagregacionesLabels;
    private HashMap<Object, HashMap<String, Object>> desagregaciones;
    private String etiquetaDato;
    private Boolean verColDesagregacion; // creado debido a que no funciona a veces el elem.desagregacion != null y no mostraba la columna
    private Boolean resultadoDouble = Boolean.FALSE;
    private Boolean crossTable = Boolean.FALSE;
    private SgEstIndicador indicadorSeleccionado;

    private SgIndicadorMaterializado indicadorMaterializado = null;

    private String keySexoMasculino = "Masculino";
    private String keySexoFemenino = "Femenino";
    private Boolean verIndiceParidadDeGenero = Boolean.FALSE;

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            cargarCombos();

            try {
                if (nombrePk != null && indicadorPk != null && anio != null && categoriaPk != null) {

                    this.filtro.setAnio(anio);
                    this.comboNombresExtr.setSelected(nombrePk.hashCode());

                    this.comboCategorias.setSelected(categoriaPk.hashCode());
                    selectCategoria();

                    this.comboIndicadores.setSelected(indicadorPk.hashCode());
                    selectIndicador();

                    if (!StringUtils.isBlank(desagregacion)) {
                        EnumDesagregacion d = EnumDesagregacion.valueOf(desagregacion.toUpperCase());
                        this.comboDesagregaciones.setSelectedT(d);
                    }

                    generar();
                }
            } catch (Exception ex) {
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_ESTADISTICAS)) {
            JSFUtils.redirectToIndex();
        }
    }

    public void cargarCombos() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});

            fc.setOrderBy(new String[]{"cinNombre"});
            fc.setIncluirCampos(new String[]{"cinNombre", "cinVersion"});
            fc.setAscending(new boolean[]{true});
            fc.setOrderBy(new String[]{"cinNombre"});
            comboCategorias = new SofisComboG<>(catalogosClient.buscarCategoriaIndicador(fc), "cinNombre");
            comboCategorias.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            fc.setOrderBy(new String[]{"nexNombre"});
            fc.setIncluirCampos(new String[]{"nexNombre", "nexVersion"});
            List<SgEstNombreExtraccion> nombres = catalogosClient.buscarNombresExtraccion(fc);
            comboNombresExtr = new SofisComboG<>(nombres, "nexNombre");
            comboNombresExtr.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboNombresExtrComp = new SofisComboG<>(nombres, "nexNombre");
            comboNombresExtrComp.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboDesagregaciones = new SofisComboG<>();
            comboDesagregaciones.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboIndicadores = new SofisComboG<>();
            comboIndicadores.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void toggleVerIndiceParidadGenero(){
        verIndiceParidadDeGenero = !verIndiceParidadDeGenero;
    }

    public void selectCategoria() {
        try {
            estGenerica = null;
            comboIndicadores = new SofisComboG<>();
            comboIndicadores.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            if (this.comboCategorias.getSelectedT() != null) {
                FiltroIndicadores fc = new FiltroIndicadores();
                fc.setHabilitado(Boolean.TRUE);
                fc.setAscending(new boolean[]{true});
                fc.setCategoriaPk(this.comboCategorias.getSelectedT().getCinPk());
                fc.setOrderBy(new String[]{"indNombre"});
                //No se utiliza incluirCampos, para que traiga las desagregaciones
                comboIndicadores = new SofisComboG<>(indicadoresClient.buscar(fc), "indNombre");
                comboIndicadores.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void selectIndicador() {
        try {
            estGenerica = null;
            comboDesagregaciones = new SofisComboG<>();
            comboDesagregaciones.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            indicadorSeleccionado = this.comboIndicadores.getSelectedT();
            if (indicadorSeleccionado != null) {
                comboDesagregaciones = new SofisComboG<>(indicadorSeleccionado.getIndDesagregaciones(), "text");
                comboDesagregaciones.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarIndicadorMaterializado() {
        try {
            FiltroIndicadorMaterializado filtrom = new FiltroIndicadorMaterializado();
            filtrom.setAnio(this.filtro.getAnio());
            filtrom.setIndicadorPk(this.filtro.getIndicadorPk());
            List<SgIndicadorMaterializado> inds = inciadorMaterializadoClient.buscar(filtrom);
            if (!inds.isEmpty()) {
                indicadorMaterializado = inds.get(0);
            } else {

            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void crearIndicadorMaterializado() {
        try {
            inciadorMaterializadoClient.crear(filtro);
            cargarIndicadorMaterializado();
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminarIndicadorMaterializado() {
        try {
            inciadorMaterializadoClient.eliminar(this.indicadorSeleccionado.getIndPk(), this.filtro.getAnio());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            this.indicadorMaterializado = null;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public Boolean getRenderDesagregacionPanel() {
        SgEstIndicador ind = this.comboIndicadores.getSelectedT();
        if (ind != null
                && (ind.getIndCodigo().equals(Constantes.INDICADOR_ESTUDIANTES_POR_SECCION)
                || ind.getIndCodigo().equals(Constantes.INDICADOR_CENTROS_EDUCATIVOS_SEGUN_NIVEL)
                || ind.getIndCodigo().equals(Constantes.INDICADOR_PORCENTAJE_ESTUDIANTES_SEGUN_LOGRO_PAES))) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
    
    public Boolean getRenderFuenteNombre(){
        SgEstIndicador ind = this.comboIndicadores.getSelectedT();
        if (ind != null
                &&  ind.getIndCodigo().equals(Constantes.INDICADOR_PORCENTAJE_ESTUDIANTES_SEGUN_LOGRO_PAES)) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public Boolean getRenderCompararPanel() {
        SgEstIndicador ind = this.comboIndicadores.getSelectedT();
        if (ind != null
                && (ind.getIndCodigo().equals(Constantes.INDICADOR_TASA_REPETICION)
                || ind.getIndCodigo().equals(Constantes.INDICADOR_PORCENTAJE_ESTUDIANTES_DESERTORES)
                || ind.getIndCodigo().equals(Constantes.INDICADOR_TASA_PROMOCION)
                || ind.getIndCodigo().equals(Constantes.INDICADOR_TASA_TRANSICION_NIVEL)
                || ind.getIndCodigo().equals(Constantes.INDICADOR_TASA_TRANSICION_CICLO)
                || ind.getIndCodigo().equals(Constantes.INDICADOR_TASA_DESERCION))) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public void generar() {
        try {
            indicadorMaterializado = null;
            Boolean error = Boolean.FALSE;
            filtro.setNombrePkComparacion(null);

            if (filtro.getAnio() == null) {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_ANIO_VACIO), "");
                error = Boolean.TRUE;
            }

            SgEstIndicador ind = this.comboIndicadores.getSelectedT();
            if (ind == null) {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_INDICADOR_VACIO), "");
                error = Boolean.TRUE;
            }
            
            SgEstNombreExtraccion nomExt = this.comboNombresExtr.getSelectedT();
            if (ind != null && !ind.getIndCodigo().equals(Constantes.INDICADOR_PORCENTAJE_ESTUDIANTES_SEGUN_LOGRO_PAES) && nomExt == null) {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_NOMBRE_EXTRACCION_VACIO), "");
                error = Boolean.TRUE;
            }

            if (getRenderCompararPanel()) {
                if (filtro.getAnioComparacion() == null) {
                    JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_ANIO_VACIO), "");
                    error = Boolean.TRUE;
                }
                if (this.comboNombresExtrComp.getSelectedT() == null) {
                    JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_NOMBRE_EXTRACCION_VACIO), "");
                    error = Boolean.TRUE;
                } else {
                    filtro.setNombrePkComparacion(this.comboNombresExtrComp.getSelectedT().getNexPk());
                }
            }

            if (error) {
                return;
            }

            if (this.comboNombresExtr.getSelectedT() != null){
                filtro.setNombrePk(this.comboNombresExtr.getSelectedT().getNexPk());
            }
            filtro.setDesagregacion(comboDesagregaciones.getSelectedT());
            filtro.setIndicadorPk(this.comboIndicadores.getSelectedT().getIndPk());

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

                switch (ind.getIndCodigo()) {
                    case Constantes.INDICADOR_MATRICULA_NIVEL_EDUCATIVO:
                        etiquetaDato = Etiquetas.getValue("hnivel");
                        resultadoDouble = Boolean.FALSE;
                        estGenerica = estEstudiantesClient.matriculaPorNivelEducativo(filtro);
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
                        resultadoDouble = Boolean.FALSE;
                        estGenerica = estEstudiantesClient.poblacionNoEscolarizadaPorEdad(filtro);
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
                        resultadoDouble = Boolean.TRUE;
                        estGenerica = estEstudiantesClient.porcentajePoblacionNoEscolarizadaPorEdad(filtro);
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
                        resultadoDouble = Boolean.TRUE;
                        estGenerica = estEstudiantesClient.porcentajeEstudiantesDesertores(filtro);
                        if (this.comboDesagregaciones.getSelectedT() != null) {
                            verColDesagregacion = Boolean.TRUE;
                            convertirListaACrossTable();
                        } else {
                            crossTable = Boolean.FALSE;
                            verColDesagregacion = Boolean.FALSE;
                        }
                        break;
                    case Constantes.INDICADOR_TASA_BRUTA_INGRESO_PRIMER_GRADO_EDUCACION_BASICA:
                        etiquetaDato = null;
                        resultadoDouble = Boolean.TRUE;
                        estGenerica = estEstudiantesClient.tasaBrutaIngresoPrimerGradoEducacionBasica(filtro);
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
                        resultadoDouble = Boolean.TRUE;
                        estGenerica = estEstudiantesClient.porcentajeDeRepetidores(filtro);
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
                        resultadoDouble = Boolean.TRUE;
                        estGenerica = estEstudiantesClient.tasaDeRepeticion(filtro);
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
                        resultadoDouble = Boolean.TRUE;
                        estGenerica = estEstudiantesClient.tasaTransicionPorNivel(filtro);
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
                        resultadoDouble = Boolean.TRUE;
                        estGenerica = estEstudiantesClient.tasaTransicionPorCiclo(filtro);
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
                        resultadoDouble = Boolean.TRUE;
                        estGenerica = estEstudiantesClient.tasaDesercion(filtro);
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
                        resultadoDouble = Boolean.TRUE;
                        estGenerica = estEstudiantesClient.porcentajeDeTrabajadores(filtro);
                        crossTable = Boolean.FALSE;
                        if (this.comboDesagregaciones.getSelectedT() != null) {
                            verColDesagregacion = Boolean.TRUE;
                        } else {
                            verColDesagregacion = Boolean.FALSE;
                        }
                        break;
                    case Constantes.INDICADOR_PORCENTAJE_ESTUDIANTES_DISCAPACIDAD:
                        etiquetaDato = null;
                        resultadoDouble = Boolean.TRUE;
                        estGenerica = estEstudiantesClient.porcentajeConDiscapacidad(filtro);
                        crossTable = Boolean.FALSE;
                        if (this.comboDesagregaciones.getSelectedT() != null) {
                            verColDesagregacion = Boolean.TRUE;
                        } else {
                            verColDesagregacion = Boolean.FALSE;
                        }
                        break;
                    case Constantes.INDICADOR_DISTRIBUCION_PORCENTUAL_ESTUDIANTES_CON_DISCAPACIDAD:
                        etiquetaDato = "Discapacidad";
                        resultadoDouble = Boolean.TRUE;
                        estGenerica = estEstudiantesClient.distribucionPorcentualEstudiantesConDiscapacidad(filtro);
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
                        resultadoDouble = Boolean.TRUE;
                        estGenerica = estEstudiantesClient.distribucionPorcentualEstudiantesSegunConvivenciaFamiliar(filtro);
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
                        resultadoDouble = Boolean.TRUE;
                        estGenerica = estEstudiantesClient.tasaNetaIngresoPrimerGradoEducacionBasica(filtro);
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
                        resultadoDouble = Boolean.TRUE;
                        estGenerica = estEstudiantesClient.tasaEspecificaDeEscolarizacionPorEdad(filtro);
                        crossTable = Boolean.FALSE;
                        if (this.comboDesagregaciones.getSelectedT() != null) {
                            verColDesagregacion = Boolean.TRUE;
                        } else {
                            verColDesagregacion = Boolean.FALSE;
                        }
                        break;
                    case Constantes.INDICADOR_PORCENTAJE_ESTUDIANTES_PRIMER_GRADO_CON_EXPERIENCIA_EDU_PARVPARVULARIA:
                        etiquetaDato = null;
                        resultadoDouble = Boolean.TRUE;
                        estGenerica = estEstudiantesClient.porcentajeDeEstudiantesDePrimerGradoConExperienciaEnEdeucacionParvularia(filtro);
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
                        resultadoDouble = Boolean.TRUE;
                        estGenerica = estEstudiantesClient.distribucionPorcentualDeLaMatriculaPorNivelEducativo(filtro);
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
                        resultadoDouble = Boolean.TRUE;
                        estGenerica = estEstudiantesClient.tasaBrutaDeMatriculaPorNivelEducativo(filtro);
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
                        resultadoDouble = Boolean.TRUE;
                        estGenerica = estEstudiantesClient.tasaNetaDeMatriculaPorNivelEducativo(filtro);
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
                        resultadoDouble = Boolean.TRUE;
                        estGenerica = estEstudiantesClient.tasaEspecificaMatriculaPorGrado(filtro);
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
                        resultadoDouble = Boolean.TRUE;
                        estGenerica = estEstudiantesClient.distribucionPorcentualDeEstudiantesSegunActividadLaboral(filtro);
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
                        resultadoDouble = Boolean.TRUE;
                        estGenerica = estEstudiantesClient.porcentajeEstudiantesAprobados(filtro);
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
                        resultadoDouble = Boolean.TRUE;
                        estGenerica = estEstudiantesClient.tasaBrutaAprobacion(filtro);
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
                        resultadoDouble = Boolean.TRUE;
                        estGenerica = estEstudiantesClient.tasaPromocion(filtro);
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
                        resultadoDouble = Boolean.FALSE;
                        estGenerica = estEstudiantesClient.centrosEducativosSegunNivelEducativo(filtro);
                        crossTable = Boolean.FALSE;
                        if (this.comboDesagregaciones.getSelectedT() != null) {
                            verColDesagregacion = Boolean.TRUE;
                        } else {
                            verColDesagregacion = Boolean.FALSE;
                        }
                        break;
                    case Constantes.INDICADOR_PORCENTAJE_ESTUDIANTES_ACCESO_INTERNET:
                        etiquetaDato = Etiquetas.getValue("hnivel");
                        resultadoDouble = Boolean.TRUE;
                        estGenerica = estEstudiantesClient.porcentajeDeEstudiantesConAccesoAInternet(filtro);
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
                        resultadoDouble = Boolean.TRUE;
                        estGenerica = estEstudiantesClient.porcentajeDeEstudiantesConAccesoAComputadora(filtro);
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
                        resultadoDouble = Boolean.TRUE;
                        estGenerica = estEstudiantesClient.estudiantesPorSeccion(filtro);
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
                        resultadoDouble = Boolean.TRUE;
                        estGenerica = estEstudiantesClient.tasaBrutaIngresoPorGrado(filtro);
                        crossTable = Boolean.FALSE;
                        if (this.comboDesagregaciones.getSelectedT() != null) {
                            verColDesagregacion = Boolean.TRUE;
                        } else {
                            verColDesagregacion = Boolean.FALSE;
                        }
                        break;
                    case Constantes.INDICADOR_PORCENTAJE_CON_SOBRE_EDAD_GRADO:
                        etiquetaDato = Etiquetas.getValue("hgrado");
                        resultadoDouble = Boolean.TRUE;
                        estGenerica = estEstudiantesClient.porcentajeConSobreedad(filtro);
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
                        resultadoDouble = Boolean.TRUE;
                        estGenerica = estEstudiantesClient.distribucionPorcentualDocentesSegunAniosServicio(filtro);
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
                        resultadoDouble = Boolean.TRUE;
                        estGenerica = estEstudiantesClient.promedioEstudiantesPorDocente(filtro);
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
                        resultadoDouble = Boolean.TRUE;
                        estGenerica = estEstudiantesClient.porcentajeDeDocentesPorGradoAcademicoAlcanzado(filtro);
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
                        resultadoDouble = Boolean.TRUE;
                        estGenerica = estEstudiantesClient.porcentajeDeDocentesConAccesoAInternet(filtro);
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
                        resultadoDouble = Boolean.TRUE;
                        estGenerica = estEstudiantesClient.porcentajeDeCentrosEducativosConAccesoServiciosBasicos(filtro);
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
                        resultadoDouble = Boolean.TRUE;
                        estGenerica = estEstudiantesClient.porcentajeDeDocentesCertificadosPorNivelEducativo(filtro);
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
                        resultadoDouble = Boolean.TRUE;
                        estGenerica = estEstudiantesClient.distribucionDeDocentesSegunNivelEducativo(filtro);
                        if (this.comboDesagregaciones.getSelectedT() != null) {
                            verColDesagregacion = Boolean.TRUE;
                            convertirListaACrossTable();
                        } else {
                            crossTable = Boolean.FALSE;
                            verColDesagregacion = Boolean.FALSE;
                        }
                        break;
                    case Constantes.INDICADOR_PORCENTAJE_ESTUDIANTES_SEGUN_LOGRO_PAES:
                        etiquetaDato = Etiquetas.getValue("hcomponente");
                        resultadoDouble = Boolean.TRUE;
                        estGenerica = estEstudiantesClient.obtenerEstadisticas(filtro);
                        verColDesagregacion = Boolean.TRUE;
                        convertirListaACrossTable();
                        break;
                    case Constantes.INDICADOR_DISTRIBUCION_PORCENTUAL_ESTUDIANTES_SEGUN_CAUSA_RETIRO_CENTRO_EDUCATIVO:
                        etiquetaDato = Etiquetas.getValue("hmotivo");
                        resultadoDouble = Boolean.TRUE;
                        estGenerica = estEstudiantesClient.obtenerEstadisticas(filtro);
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

            cargarIndicadorMaterializado();

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
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

                if (EnumDesagregacion.SEXO.equals(this.comboDesagregaciones.getSelectedT())) {
                    // Para calcular el índice de paridad de género, necesitamos realizar mujeres dividido hombres.
                    // A nivel de frontend contamos solamente con el nombre del sexo para obtener el valor del mismo
                    // En caso de que cambien el nombre del sexo, agregamos un check lowercase de posibles valores que el catálogo puede tener
                    String desagregacion = e.getDesagregacion();
                    if (desagregacion.toLowerCase().contains("mas") || desagregacion.toLowerCase().contains("hom")) {
                        keySexoMasculino = desagregacion;
                    } else if (desagregacion.toLowerCase().contains("fem") || desagregacion.toLowerCase().contains("muj")) {
                        keySexoFemenino = desagregacion; 
                    }
                }

            }
        }
        desagregacionesLabels = new ArrayList<>(desagregacionesLabelsSet);
        Collections.sort(desagregacionesLabels);
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

    public Object obtenerIndiceParidadGenero(Object labelPrincipal) {
        if (this.desagregaciones.containsKey(labelPrincipal)) {
            return ((Number) this.desagregaciones.get(labelPrincipal).get(keySexoFemenino)).doubleValue() / ((Number) this.desagregaciones.get(labelPrincipal).get(keySexoMasculino)).doubleValue();
        }
        if (resultadoDouble) {
            return 0d;
        }
        return 0L;
    }

    public Object obtenerCantidadTotalDesagregacionPorColumna(String labelDesagregacion) {
        if (resultadoDouble) {
            return this.desagregaciones.entrySet().stream().map(pl -> pl.getValue()).filter(Objects::nonNull).map(d -> d.get(labelDesagregacion)).filter(Objects::nonNull).map(Double.class::cast).mapToDouble(d -> d).sum();
        } else {
            return this.desagregaciones.entrySet().stream().map(pl -> pl.getValue()).filter(Objects::nonNull).map(d -> d.get(labelDesagregacion)).filter(Objects::nonNull).map(Integer.class::cast).mapToInt(n -> n).sum();
        }
    }

    public Object obtenerCantidadTotalDesagregacionPorFila(Object labelPrincipal) {
        if (resultadoDouble) {
            return this.desagregaciones.get(labelPrincipal)
                    .entrySet().stream()
                    .map(d -> d.getValue())
                    .filter(Objects::nonNull)
                    .map(Double.class::cast)
                    .mapToDouble(d -> d)
                    .sum();
        } else {
            return this.desagregaciones.get(labelPrincipal)
                    .entrySet().stream()
                    .map(d -> d.getValue())
                    .filter(Objects::nonNull)
                    .map(Integer.class::cast)
                    .mapToInt(d -> d)
                    .sum();
        }
    }

    public void limpiar() {
        filtro = new FiltroEstadisticas();
        comboNombresExtr.setSelected(-1);
        comboIndicadores.setSelected(-1);
    }

    public SofisComboG<SgEstIndicador> getComboIndicadores() {
        return comboIndicadores;
    }

    public void setComboIndicadores(SofisComboG<SgEstIndicador> comboIndicadores) {
        this.comboIndicadores = comboIndicadores;
    }

    public SofisComboG<SgEstCategoriaIndicador> getComboCategorias() {
        return comboCategorias;
    }

    public void setComboCategorias(SofisComboG<SgEstCategoriaIndicador> comboCategorias) {
        this.comboCategorias = comboCategorias;
    }

    public FiltroEstadisticas getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroEstadisticas filtro) {
        this.filtro = filtro;
    }

    public SofisComboG<SgEstNombreExtraccion> getComboNombresExtr() {
        return comboNombresExtr;
    }

    public void setComboNombresExtr(SofisComboG<SgEstNombreExtraccion> comboNombresExtr) {
        this.comboNombresExtr = comboNombresExtr;
    }

    public List<EstGenerica> getEstGenerica() {
        return estGenerica;
    }

    public Integer getPaginado() {
        return paginado;
    }

    public void setPaginado(Integer paginado) {
        this.paginado = paginado;
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

    public List<String> getDesagregacionesLabels() {
        return desagregacionesLabels;
    }

    public HashMap<Object, HashMap<String, Object>> getDesagregaciones() {
        return desagregaciones;
    }

    public String getEtiquetaDato() {
        return etiquetaDato;
    }

    public Boolean getResultadoDouble() {
        return resultadoDouble;
    }

    public Boolean getCrossTable() {
        return crossTable;
    }

    public void setCrossTable(Boolean crossTable) {
        this.crossTable = crossTable;
    }

    public Boolean getVerColDesagregacion() {
        return verColDesagregacion;
    }

    public void setVerColDesagregacion(Boolean verColDesagregacion) {
        this.verColDesagregacion = verColDesagregacion;
    }

    public SofisComboG<SgEstNombreExtraccion> getComboNombresExtrComp() {
        return comboNombresExtrComp;
    }

    public void setComboNombresExtrComp(SofisComboG<SgEstNombreExtraccion> comboNombresExtrComp) {
        this.comboNombresExtrComp = comboNombresExtrComp;
    }

    public SgEstIndicador getIndicadorSeleccionado() {
        return indicadorSeleccionado;
    }

    public void setIndicadorSeleccionado(SgEstIndicador indicadorSeleccionado) {
        this.indicadorSeleccionado = indicadorSeleccionado;
    }

    public SgIndicadorMaterializado getIndicadorMaterializado() {
        return indicadorMaterializado;
    }

    public void setIndicadorMaterializado(SgIndicadorMaterializado indicadorMaterializado) {
        this.indicadorMaterializado = indicadorMaterializado;
    }

    public Boolean getVerIndiceParidadDeGenero() {
        return verIndiceParidadDeGenero;
    }

    public void setVerIndiceParidadDeGenero(Boolean verIndiceParidadDeGenero) {
        this.verIndiceParidadDeGenero = verIndiceParidadDeGenero;
    }
    
    

}
