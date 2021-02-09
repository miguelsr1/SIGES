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
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.EstGenerica;
import sv.gob.mined.siges.web.dto.SgEstIndicador;
import sv.gob.mined.siges.web.dto.catalogo.SgEstCategoriaIndicador;
import sv.gob.mined.siges.web.dto.catalogo.SgEstNombreExtraccion;
import sv.gob.mined.siges.web.enumerados.EnumDesagregacion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroIndicadores;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEstadisticas;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.EstadisticasEstudiantesRestClient;
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
    private SessionBean sessionBean;

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

    @PostConstruct
    public void init() {
        try {
            cargarCombos();

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
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

    public Boolean getRenderDesagregacionPanel() {
        SgEstIndicador ind = this.comboIndicadores.getSelectedT();
        if (ind != null
                && (ind.getIndCodigo().equals(Constantes.INDICADOR_ESTUDIANTES_POR_SECCION)
                || ind.getIndCodigo().equals(Constantes.INDICADOR_CENTROS_EDUCATIVOS_SEGUN_NIVEL))) {
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
            Boolean error = Boolean.FALSE;
            filtro.setNombrePkComparacion(null);

            if (filtro.getAnio() == null) {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_ANIO_VACIO), "");
                error = Boolean.TRUE;
            }

            SgEstNombreExtraccion nomExt = this.comboNombresExtr.getSelectedT();
            if (nomExt == null) {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_NOMBRE_EXTRACCION_VACIO), "");
                error = Boolean.TRUE;
            }

            SgEstIndicador ind = this.comboIndicadores.getSelectedT();
            if (ind == null) {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_INDICADOR_VACIO), "");
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

            filtro.setNombrePk(this.comboNombresExtr.getSelectedT().getNexPk());
            filtro.setDesagregacion(comboDesagregaciones.getSelectedT());

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
                    etiquetaDato = "Edad población";
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
                    estGenerica = estEstudiantesClient.tasaEspecíficaDeEscolarizaciónPorEdad(filtro);
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
                    etiquetaDato = Etiquetas.getValue("hsede");
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
                default:
                    return;
            }

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

}
