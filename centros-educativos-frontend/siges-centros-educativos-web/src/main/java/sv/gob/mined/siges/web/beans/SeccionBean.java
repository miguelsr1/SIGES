/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgAnioLectivo;
import sv.gob.mined.siges.web.dto.SgPlanEstudio;
import sv.gob.mined.siges.web.dto.SgSeccion;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.SgServicioEducativo;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.catalogo.SgJornadaLaboral;
import sv.gob.mined.siges.web.dto.catalogo.SgMunicipio;
import sv.gob.mined.siges.web.enumerados.EnumSeccionEstado;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAnioLectivo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPlanEstudio;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroServicioEducativo;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroMunicipio;
import sv.gob.mined.siges.web.lazymodels.LazySeccionDataModel;
import sv.gob.mined.siges.web.lazymodels.LazyServicioEducativoDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AnioLectivoRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.NivelRestClient;
import sv.gob.mined.siges.web.restclient.PlanesEstudioRestClient;
import sv.gob.mined.siges.web.restclient.SeccionRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.restclient.ServicioEducativoRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.SgAula;
import sv.gob.mined.siges.web.dto.SgCalendario;
import sv.gob.mined.siges.web.dto.SgCopiarSecciones;
import sv.gob.mined.siges.web.dto.SgNivel;
import sv.gob.mined.siges.web.dto.SgRelGradoPlanEstudio;
import sv.gob.mined.siges.web.dto.SgSedeLite;
import sv.gob.mined.siges.web.dto.catalogo.SgAsociacion;
import sv.gob.mined.siges.web.enumerados.EnumAnioLectivoEstado;
import sv.gob.mined.siges.web.enumerados.EnumEstadoProcesamientoCalificacionPromocion;
import sv.gob.mined.siges.web.enumerados.EnumRegimenSeccion;
import sv.gob.mined.siges.web.enumerados.EnumTipoPeriodoSeccion;
import sv.gob.mined.siges.web.enumerados.TipoSede;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAula;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCalendario;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroNivel;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelGradoPlanEstudio;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSeccion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroAsociacion;
import sv.gob.mined.siges.web.restclient.AulaRestClient;
import sv.gob.mined.siges.web.restclient.CalendarioRestClient;
import sv.gob.mined.siges.web.restclient.CalificacionRestClient;
import sv.gob.mined.siges.web.restclient.RelGradoPlanEstudioRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class SeccionBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(SeccionBean.class.getName());

    @Inject
    private SeccionRestClient restClient;

    @Inject
    private SedeRestClient restSede;

    @Inject
    private ServicioEducativoRestClient restServicioEducativo;

    @Inject
    private AnioLectivoRestClient restAnioLectivo;

    @Inject
    private PlanesEstudioRestClient restPlanEstudio;

    @Inject
    private CatalogosRestClient restCatalogo;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private NivelRestClient restNivel;

    @Inject
    private FiltrosSeccionBean filtrosSeccion;

    @Inject
    private AulaRestClient aulaRestClient;

    @Inject
    private CalendarioRestClient calendarioRestClient;

    @Inject
    private RelGradoPlanEstudioRestClient relGradoPlanEstudioRestClient;
    
    @Inject
    private CalificacionRestClient calificacionClient;
    
    private SgSeccion entidadEnEdicion;
    private FiltroServicioEducativo filtroServicioEducativo = new FiltroServicioEducativo();
    private LazySeccionDataModel seccionLazyModel;
    private LazyServicioEducativoDataModel servicioEducativoLazyModel;
    private List<RevHistorico> historialSeccion = new ArrayList();
    private String formatoSeleccionado = "csv";
    private Integer paginado = 5;
    private Long totalResultados;
    private Long totalSecciones;
    private SofisComboG<SgAnioLectivo> comboAnioLectivoInsert;
    private SofisComboG<EnumSeccionEstado> comboEstado;
    private SofisComboG<SgPlanEstudio> comboPlanEstudio;
    private SofisComboG<SgJornadaLaboral> comboJornadaLaboral;
    private SofisComboG<SgDepartamento> comboDepartamento;
    private SofisComboG<SgMunicipio> comboMunicipio;
    private SgServicioEducativo servicioEducativoSeleccionado;
    private SgSede sedeEnEdicion;
    private Boolean soloLectura;
    private SgAnioLectivo anioFiltroPorDefecto = null;

    //datos secciones modalidad flexible
    private Boolean renderSeccionIntegrada;
    private Boolean renderDatosSeccionModalidadFlexible;
    private SofisComboG<SgAsociacion> comboAsociaciones;
    private SofisComboG<EnumRegimenSeccion> comboRegimenSeccion;

    //Datos para copia de secciones
    private List<SgSeccion> seccionesCopia;
    private List<SgSeccion> seccionesCopiaSelected;
    private Integer anioLectivoOrigen;
    private SgAnioLectivo anioLectivoDestino;

    //Datos para seleccion de aula
    private SofisComboG<SgAula>[] comboAula;
    private SofisComboG<SgAnioLectivo> comboAnio;
    private List<SgSeccion> seccionesAula;
    private List<SgAula> listAulas;
    private Boolean[] seccionConCambioAula;
    private List<SgSeccion> seccionesAulaOriginal;
    private SofisComboG<SgNivel> comboNivelAula;
    private SgCalendario calendarioSede;

    private String letraSeleccionada;
    private List<SelectItem> letras;
    private Boolean aplicaAnual;
    private Boolean esAnual = Boolean.TRUE;
    private List<SelectItem> periodos;
    private Integer periodoSeleccionado;
    private Integer promocionSeleccionada;
    
    public SeccionBean() {
    }

    @PostConstruct
    public void init() {
        try {

            validarAcceso();
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            sedeEnEdicion = (SgSede) request.getAttribute("sedeEducativa");
            if (sedeEnEdicion == null) {
                //Seccion utilizado como página
                cargarCombosBusquedaCompleta();
                this.filtrosSeccion.setFiltro(this.filtroServicioEducativo);
                this.filtrosSeccion.setRenderSede(Boolean.TRUE);
                this.filtrosSeccion.setRenderProgramaOpcion(Boolean.TRUE);
                this.filtrosSeccion.setRenderSeccion(Boolean.FALSE);
                this.filtrosSeccion.setRenderSeccionIntegrada(Boolean.TRUE);
                this.filtrosSeccion.cargarCombos();
                this.filtrosSeccion.seleccionarUltimoAnio();
            } else {
                //Utilizado como componente dentro de sede
                this.filtrosSeccion.setFiltro(this.filtroServicioEducativo);
                this.filtrosSeccion.setRenderSede(Boolean.FALSE);
                this.filtrosSeccion.setRenderProgramaOpcion(Boolean.TRUE);
                this.filtrosSeccion.setRenderSeccion(Boolean.FALSE);
                this.filtrosSeccion.setRenderSeccionIntegrada(Boolean.TRUE);
                this.filtrosSeccion.cargarCombos();
                this.filtrosSeccion.seleccionarUltimoAnio();
                this.filtrosSeccion.cargarSedeSeleccionada(sedeEnEdicion);
                filtroServicioEducativo.setSecSedeFk(sedeEnEdicion.getSedPk());
                buscar();
                buscarAulas();
                buscarCalendario();
            }
            filtrosSeccion.setRenderFiltrosSeccion(Boolean.TRUE);
            cargarCombo();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void cargarCombo() {
        letras = new ArrayList<SelectItem>();
        SelectItem si = new SelectItem(null, Etiquetas.getValue("comboEmptyItem"));
        letras.add(si);
        for (Integer i = 0; i < 26; i++) {
            letras.add(new SelectItem((char) (65 + i) + "", (char) (65 + i) + ""));

        }

        periodos = new ArrayList<SelectItem>();
        periodos.add(si);
        periodos.add(new SelectItem(1, "Primer período"));
        periodos.add(new SelectItem(2, "Segundo período"));

    }

    public void forceInit() {
        //Utilizado para forzar init de SeccionBean antes que FiltrosSeccionBean
    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_SECCION)) {
            JSFUtils.redirectToIndex();
        }
    }

    public void buscarCalendario() {
        try {
            FiltroCalendario fc = new FiltroCalendario();
            fc.setTipoCalendarioPk(sedeEnEdicion.getSedTipoCalendario().getTcePk());
            fc.setHabilitado(Boolean.TRUE);
            fc.setOrderBy(new String[]{"calAnioLectivo.aleAnio"});
            fc.setAscending(new boolean[]{false});
            fc.setIncluirCampos(new String[]{"calAnioLectivo.aleAnio", "calPermiteCopiarSeccion"});
            List<SgCalendario> cal = calendarioRestClient.buscarConCache(fc);
            if (!cal.isEmpty()) {
                calendarioSede = cal.get(0);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public String buscar() {
        try {
            filtroServicioEducativo.setIncluirSecciones(Boolean.TRUE);
            filtroServicioEducativo.setDepartamento(comboDepartamento != null && comboDepartamento.getSelectedT() != null ? comboDepartamento.getSelectedT().getDepPk() : null);
            filtroServicioEducativo.setMunicipio(comboMunicipio != null && comboMunicipio.getSelectedT() != null ? comboMunicipio.getSelectedT().getMunPk() : null);
            setFiltroPromocion();
            filtroServicioEducativo.setEstadoSeccion(filtrosSeccion.getFiltro().getEstadoSeccion());
            filtroServicioEducativo.setFirst(0L);
            totalSecciones = restClient.buscarTotal(filtroServicioEducativo);
            totalResultados = restServicioEducativo.buscarTotal(filtroServicioEducativo);

            filtroServicioEducativo.setIncluirCampos(new String[]{
                "sduSede.sedCodigo",
                "sduFechaHabilitado",
                "sduVersion",
                "sduEstado",
                "sduSede.sedPk",
                "sduSede.sedTipo",
                "sduSede.sedNombre",
                "sduSede.sedCodigo",
                "sduSede.sedVersion",
                "sduSede.sedPk",
                "sduSede.sedTelefono",
                "sduSede.sedDireccion.dirDepartamento.depNombre",
                "sduSede.sedDireccion.dirMunicipio.munNombre",
                "sduGrado.graNombre",
                "sduGrado.graPk",
                "sduGrado.graVersion",
                "sduOpcion.opcNombre",
                "sduProgramaEducativo.pedNombre",
                "sduGrado.graRelacionModalidad.reaModalidadAtencion.matNombre",
                "sduGrado.graRelacionModalidad.reaSubModalidadAtencion.smoNombre",
                "sduGrado.graRelacionModalidad.reaSubModalidadAtencion.smoIntegrada",
                "sduGrado.graRelacionModalidad.reaModalidadEducativa.modNombre",
                "sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNombre",
                "sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivNombre",
                "sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivNombreSeccionLibre"
            });

            servicioEducativoLazyModel = new LazyServicioEducativoDataModel(restServicioEducativo, filtroServicioEducativo, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void setFiltroPromocion(){
        filtroServicioEducativo.setEstadoPromocion(null);
        if(promocionSeleccionada != null){
            if(promocionSeleccionada.equals(1)){
                filtroServicioEducativo.setEstadoPromocion(EnumEstadoProcesamientoCalificacionPromocion.PROCESADO);
            }
            if(promocionSeleccionada.equals(2)){
                filtroServicioEducativo.setEstadoPromocion(EnumEstadoProcesamientoCalificacionPromocion.SIN_PROCESAR);
            }
        }
    }
    
    public void buscarAulas() {
        try {
            FiltroAnioLectivo fal = new FiltroAnioLectivo();
            fal.setAscending(new boolean[]{false});
            fal.setOrderBy(new String[]{"aleAnio"});
            List<SgAnioLectivo> anioLectivo = restAnioLectivo.buscar(fal);
            comboAnio = new SofisComboG(anioLectivo, "aleAnio");
            comboAnio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboAnio.setSelectedT((SgAnioLectivo) this.comboAnio.getAllTs().get(this.comboAnio.getAllTs().size() - 1));

            FiltroNivel fn = new FiltroNivel();
            fn.setSedPk(sedeEnEdicion.getSedPk());
            fn.setIncluirCampos(new String[]{"nivNombre", "nivVersion"});
            List<SgNivel> nivelList = restNivel.buscar(fn);
            comboNivelAula = new SofisComboG(nivelList, "nivNombre");
            comboNivelAula.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            listAulas = new ArrayList();
            if (sedeEnEdicion != null && sessionBean.getOperaciones().contains(ConstantesOperaciones.BUSCAR_AULA)) {
                FiltroAula fa = new FiltroAula();
                fa.setSedeId(sedeEnEdicion.getSedPk());
                fa.setIncluirCampos(new String[]{"aulCodigo", "aulNombre", "aulVersion"});
                listAulas = aulaRestClient.buscar(fa);
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarDialogAulas() {
        comboAnio.setSelectedT((SgAnioLectivo) this.comboAnio.getAllTs().get(this.comboAnio.getAllTs().size() - 1));
        comboNivelAula.setSelected(-1);
        comboAula = null;
        seccionesAula = null;
        seccionesAulaOriginal = null;
        seleccionarAnio();
    }

    public void seleccionarAnio() {
        try {
            comboAula = null;
            seccionesAula = null;
            seccionConCambioAula = null;
            seccionesAulaOriginal = null;
            if (comboAnio.getSelectedT() != null && !listAulas.isEmpty()) {
                FiltroSeccion fsec = new FiltroSeccion();
                fsec.setSecAnioLectivoFk(comboAnio.getSelectedT().getAlePk());
                fsec.setSecSedeFk(sedeEnEdicion.getSedPk());
                fsec.setIncluirCampos(new String[]{"secCodigo", "secNombre", "secVersion", "secEstado",
                    "secPlanEstudio.pesPk", "secPlanEstudio.pesVersion", "secAccesoInternet", "secAccesoComputadora", "secCopiadaAnioSiguiente",
                    "secServicioEducativo.sduPk", "secServicioEducativo.sduVersion",
                    "secJornadaLaboral.jlaPk", "secJornadaLaboral.jlaVersion", "secJornadaLaboral.jlaNombre",
                    "secServicioEducativo.sduGrado.graNombre",
                    "secServicioEducativo.sduOpcion.opcNombre",
                    "secServicioEducativo.sduProgramaEducativo.pedNombre",
                    "secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadAtencion.matNombre",
                    "secServicioEducativo.sduGrado.graRelacionModalidad.reaSubModalidadAtencion.smoNombre",
                    "secServicioEducativo.sduGrado.graRelacionModalidad.reaSubModalidadAtencion.smoIntegrada",
                    "secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modNombre",
                    "secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNombre",
                    "secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivNombre",
                    "secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivPk",
                    "secAnioLectivo.alePk", "secAnioLectivo.aleVersion", "secAnioLectivo.aleAnio", "secAula.aulVersion", "secAula.aulPk", "secAula.aulCodigo"});
                fsec.setOrderBy(new String[]{"secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivOrden",
                    "secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicOrden",
                    "secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modOrden",
                    "secServicioEducativo.sduGrado.graOrden", "secNombre"});
                fsec.setAscending(new boolean[]{true, true, true, true, true});
                seccionesAula = restClient.buscar(fsec);
                seccionesAulaOriginal = seccionesAula;
                if (!seccionesAula.isEmpty()) {
                    comboAula = new SofisComboG[seccionesAula.size()];
                    for (SgSeccion sec : seccionesAula) {
                        comboAula[seccionesAula.indexOf(sec)] = new SofisComboG(this.listAulas, "aulCodigo");
                        comboAula[seccionesAula.indexOf(sec)].addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                        if (sec.getSecAula() != null) {
                            comboAula[seccionesAula.indexOf(sec)].setSelectedT(sec.getSecAula());
                        }

                    }
                    seccionConCambioAula = new Boolean[seccionesAula.size()];
                    Arrays.fill(seccionConCambioAula, Boolean.FALSE);
                } else {
                    seccionesAula = null;
                }
            }
            comboNivelAula.setSelected(-1);
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarNivelAula() {
        if (comboAnio.getSelectedT() != null && !seccionesAulaOriginal.isEmpty()) {
            if (comboNivelAula.getSelectedT() == null) {
                seccionesAula = seccionesAulaOriginal;
            } else {
                seccionesAula = seccionesAulaOriginal;
                seccionesAula = seccionesAula.stream().filter(c -> c.getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaModalidadEducativa().getModCiclo().getCicNivel().getNivPk().equals(comboNivelAula.getSelectedT().getNivPk())).collect(Collectors.toList());
            }
            comboAula = new SofisComboG[seccionesAula.size()];
            for (SgSeccion sec : seccionesAula) {
                comboAula[seccionesAula.indexOf(sec)] = new SofisComboG(this.listAulas, "aulCodigo");
                comboAula[seccionesAula.indexOf(sec)].addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                if (sec.getSecAula() != null) {
                    comboAula[seccionesAula.indexOf(sec)].setSelectedT(sec.getSecAula());
                }

            }
            seccionConCambioAula = new Boolean[seccionesAula.size()];
            Arrays.fill(seccionConCambioAula, Boolean.FALSE);

        }
    }

    public void seccionConCambio(SgSeccion sec) {
        seccionConCambioAula[seccionesAula.indexOf(sec)] = Boolean.TRUE;
    }

    public void guardarAulas() {
        try {
            List<SgSeccion> seccionCambio = new ArrayList();
            for (SgSeccion sec : seccionesAula) {
                if (seccionConCambioAula[seccionesAula.indexOf(sec)]) {
                    sec.setSecAula(comboAula[seccionesAula.indexOf(sec)].getSelectedT());
                    seccionCambio.add(sec);
                }

            }
            if (!seccionCambio.isEmpty()) {
                restClient.guardarSeccionAula(seccionCambio);
                buscar();
            }
            PrimeFaces.current().executeScript("PF('cargarAulasDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP_3, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP_3, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void buscarSeccionesAnioAnterior() {
        try {
            FiltroAnioLectivo fal = new FiltroAnioLectivo();
            fal.setAscending(new boolean[]{false});
            fal.setOrderBy(new String[]{"aleAnio"});
            fal.setAleEstado(EnumAnioLectivoEstado.ABIERTO);
            fal.setMaxResults(1L);
            List<SgAnioLectivo> aniosLectivos = restAnioLectivo.buscar(fal);

            if (!aniosLectivos.isEmpty()) {
                anioLectivoDestino = aniosLectivos.get(0);
                anioLectivoOrigen = anioLectivoDestino.getAleAnio() - 1;
                FiltroSeccion fSeccion = new FiltroSeccion();
                fSeccion.setOrderBy(new String[]{"secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivOrden",
                    "secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicOrden",
                    "secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modOrden",
                    "secServicioEducativo.sduGrado.graOrden", "secNombre"});
                fSeccion.setAscending(new boolean[]{true, true, true, true, true});
                fSeccion.setSecAnioLectivoAnio(anioLectivoOrigen);
                fSeccion.setSecSedeFk(sedeEnEdicion.getSedPk());
                fSeccion.setIncluirCampos(new String[]{"secCodigo", "secNombre", "secVersion", "secEstado",
                    "secPlanEstudio.pesPk", "secPlanEstudio.pesVersion", "secAccesoInternet", "secAccesoComputadora", "secCopiadaAnioSiguiente",
                    "secServicioEducativo.sduPk", "secServicioEducativo.sduVersion",
                    "secJornadaLaboral.jlaPk", "secJornadaLaboral.jlaVersion", "secJornadaLaboral.jlaNombre",
                    "secServicioEducativo.sduGrado.graNombre",
                    "secServicioEducativo.sduOpcion.opcNombre",
                    "secServicioEducativo.sduProgramaEducativo.pedNombre",
                    "secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadAtencion.matNombre",
                    "secServicioEducativo.sduGrado.graRelacionModalidad.reaSubModalidadAtencion.smoNombre",
                    "secServicioEducativo.sduGrado.graRelacionModalidad.reaSubModalidadAtencion.smoIntegrada",
                    "secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modNombre",
                    "secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNombre",
                    "secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivNombre",
                    "secAnioLectivo.alePk", "secAnioLectivo.aleAnio"});
                seccionesCopia = restClient.buscar(fSeccion);
            } else {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_NO_HAY_ANIO_LECTIVO_HABILITADO), "");
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardarCopiaSecciones() {
        try {
            SgCopiarSecciones dto = new SgCopiarSecciones();
            dto.setNuevoAnio(anioLectivoDestino);
            dto.setSecciones(seccionesCopiaSelected);
            this.restClient.copiarSecciones(dto);
            totalSecciones = restClient.buscarTotal(filtroServicioEducativo);
            PrimeFaces.current().executeScript("PF('seleccionarSeccionesDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP_2, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP_2, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombosBusquedaCompleta() {

        try {

            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setAscending(new boolean[]{true});

            fc.setOrderBy(new String[]{"depNombre"});
            fc.setIncluirCampos(new String[]{"depNombre", "depVersion"});
            List<SgDepartamento> listaDepartamento = restCatalogo.buscarDepartamento(fc);
            comboDepartamento = new SofisComboG(listaDepartamento, "depNombre");
            comboDepartamento.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboMunicipio = new SofisComboG();
            comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public void cargarCombos() {

        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setOrderBy(new String[]{"jlaNombre"});
            fc.setIncluirCampos(new String[]{"jlaNombre", "jlaVersion"});
            List<SgJornadaLaboral> listaJornadaLaboral = restCatalogo.buscarJornadasLaborales(fc);
            comboJornadaLaboral = new SofisComboG(listaJornadaLaboral, "jlaNombre");
            comboJornadaLaboral.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboPlanEstudio = new SofisComboG();
            comboPlanEstudio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            List<EnumSeccionEstado> estados = new ArrayList(Arrays.asList(EnumSeccionEstado.values()));
            comboEstado = new SofisComboG(estados, "text");
            comboEstado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            if (comboAnioLectivoInsert == null) {
                FiltroAnioLectivo fal = new FiltroAnioLectivo();
                fal.setAscending(new boolean[]{false});
                fal.setOrderBy(new String[]{"aleAnio"});
                fal.setAleEstado(EnumAnioLectivoEstado.ABIERTO);
                List<SgAnioLectivo> anioLectivo = restAnioLectivo.buscar(fal);
                comboAnioLectivoInsert = new SofisComboG(anioLectivo, "aleAnio");
                comboAnioLectivoInsert.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombosSeccionModalidadFlexible() {
        try {
            if (comboAsociaciones == null) {
                FiltroAsociacion filAs = new FiltroAsociacion();
                filAs.setHabilitado(Boolean.TRUE);
                filAs.setIncluirCampos(new String[]{"asoNombre", "asoVersion"});
                List<SgAsociacion> asociaciones = restCatalogo.buscarAsociaciones(filAs);
                comboAsociaciones = new SofisComboG(asociaciones, "asoNombre");
                comboAsociaciones.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
            if (comboRegimenSeccion == null) {
                List<EnumRegimenSeccion> reg = new ArrayList(Arrays.asList(EnumRegimenSeccion.values()));
                comboRegimenSeccion = new SofisComboG(reg, "text");
                comboRegimenSeccion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {
        if (comboEstado != null) {
            comboEstado.setSelected(-1);
        }
        if (comboAnioLectivoInsert != null) {
            comboAnioLectivoInsert.setSelected(-1);
        }
        if (comboJornadaLaboral != null) {
            comboJornadaLaboral.setSelected(-1);
        }
        if (comboRegimenSeccion != null) {
            comboRegimenSeccion.setSelected(-1);
        }
        if (comboAsociaciones != null) {
            comboAsociaciones.setSelected(-1);
        }
        letraSeleccionada = null;

    }

    private void limpiarCombosBusqueda() {
        if (comboDepartamento != null) {
            comboDepartamento.setSelected(-1);
        }
        if (comboMunicipio != null) {
            comboMunicipio.setSelected(-1);
        }

    }

    public String limpiar() {
        //filtroServicioEducativo = new FiltroServicioEducativo();
        this.filtrosSeccion.limpiarCombos();
        limpiarCombosBusqueda();
        limpiarCombos();
        servicioEducativoLazyModel = null;
        promocionSeleccionada = null;
        filtroServicioEducativo.setSduExisteSeccion(Boolean.FALSE);
        return null;
    }

    public void agregar(SgServicioEducativo servicioEducativo) {
        try {
            limpiarCombos();
            if (servicioEducativo != null) {
                aplicaAnual = Boolean.FALSE;
                esAnual = Boolean.TRUE;
                servicioEducativoSeleccionado = servicioEducativo;
                entidadEnEdicion = new SgSeccion();
                entidadEnEdicion.setSecServicioEducativo(servicioEducativo);
                servicioEducativoSeleccionado = servicioEducativo;

                renderSeccionIntegrada = Boolean.FALSE;
                renderDatosSeccionModalidadFlexible = Boolean.FALSE;
                if (entidadEnEdicion.getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaSubModalidadAtencion() != null) {
                    renderSeccionIntegrada = entidadEnEdicion.getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaSubModalidadAtencion().getSmoIntegrada();
                    renderDatosSeccionModalidadFlexible = Boolean.TRUE;
                    cargarCombosSeccionModalidadFlexible();
                }

                FiltroSedes fs = new FiltroSedes();
                fs.setIncluirJornadas(Boolean.TRUE);
                fs.setSedPk(servicioEducativo.getSduSede().getSedPk());
                List<SgSedeLite> listLite = restSede.buscarLite(fs);
                if (!listLite.isEmpty()) {
                    SgSedeLite sede = listLite.get(0);
                    servicioEducativoSeleccionado.getSduSede().setSedJornadasLaborales(sede.getSedJornadasLaborales());

                    //Si alf, entonces se sugiere cod sec
                    if (TipoSede.CIR_ALF.equals(sede.getSedTipo())) {
                        entidadEnEdicion.setSecCodigo(sede.getSedCodigo());
                        entidadEnEdicion.setSecNombre(sede.getSedCodigo());
                    }
                }

                cargarCombos();

                FiltroPlanEstudio fpe = new FiltroPlanEstudio();
                fpe.setServicioEducativoFk(servicioEducativo.getSduPk());
                List<SgPlanEstudio> listPlanEst = restPlanEstudio.buscar(fpe);
                comboPlanEstudio = new SofisComboG(listPlanEst, "pesNombre");
                comboPlanEstudio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

                if (listPlanEst.size() == 1) {
                    comboPlanEstudio.setSelectedT(listPlanEst.get(0));
                    seleccionarPlanEstudio();
                }

            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void actualizar(SgServicioEducativo servicio, SgSeccion var) {
        try {
            limpiarCombos();
            entidadEnEdicion = this.restClient.obtenerPorId(var.getSecPk());
            servicioEducativoSeleccionado = servicio;

            renderSeccionIntegrada = Boolean.FALSE;
            renderDatosSeccionModalidadFlexible = Boolean.FALSE;
            if (BooleanUtils.isTrue(entidadEnEdicion.getSecIntegrada())) {
                renderSeccionIntegrada = Boolean.TRUE;
            } else if (entidadEnEdicion.getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaSubModalidadAtencion() != null) {
                renderSeccionIntegrada = entidadEnEdicion.getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaSubModalidadAtencion().getSmoIntegrada();
            }

            if (entidadEnEdicion.getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaSubModalidadAtencion() != null) {
                renderDatosSeccionModalidadFlexible = Boolean.TRUE;
                cargarCombosSeccionModalidadFlexible();
            }

            servicioEducativoSeleccionado.getSduSede().setSedJornadasLaborales(entidadEnEdicion.getSecServicioEducativo().getSduSede().getSedJornadasLaborales());
            cargarCombos();

            FiltroPlanEstudio fpe = new FiltroPlanEstudio();
            fpe.setServicioEducativoFk(servicio.getSduPk());
            List<SgPlanEstudio> listPlanEst = restPlanEstudio.buscar(fpe);
            comboPlanEstudio = new SofisComboG(listPlanEst, "pesNombre");
            comboPlanEstudio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboPlanEstudio.setSelectedT(entidadEnEdicion.getSecPlanEstudio());

            List<SgJornadaLaboral> jornadasSede = comboJornadaLaboral.getAllTs();
            if (!jornadasSede.contains(entidadEnEdicion.getSecJornadaLaboral())) {
                comboJornadaLaboral.add(entidadEnEdicion.getSecJornadaLaboral());
            }
            comboJornadaLaboral.setSelectedT(entidadEnEdicion.getSecJornadaLaboral());
            comboAnioLectivoInsert.setSelectedT(entidadEnEdicion.getSecAnioLectivo());
            comboEstado.setSelectedT(entidadEnEdicion.getSecEstado());
            if (comboAsociaciones != null) {
                comboAsociaciones.setSelectedT(entidadEnEdicion.getSecAsociacion());
            }
            if (comboRegimenSeccion != null) {
                comboRegimenSeccion.setSelectedT(entidadEnEdicion.getSecRegimen());
            }
            letraSeleccionada = null;
            if (!servicio.getSduGrado().getGraRelacionModalidad().getReaModalidadEducativa().getModCiclo().getCicNivel().getNivNombreSeccionLibre()) {
                if (!StringUtils.isAllBlank(entidadEnEdicion.getSecCodigo()) && entidadEnEdicion.getSecCodigo().length() == 1
                        && Character.isUpperCase(entidadEnEdicion.getSecCodigo().charAt(0))) {
                    letraSeleccionada = entidadEnEdicion.getSecNombre();
                } else {
                    if (!StringUtils.isAllBlank(entidadEnEdicion.getSecNombre()) && entidadEnEdicion.getSecNombre().length() == 1
                            && Character.isUpperCase(entidadEnEdicion.getSecNombre().charAt(0))) {
                        letraSeleccionada = entidadEnEdicion.getSecNombre();
                    }
                }
            }
            FiltroRelGradoPlanEstudio frg = new FiltroRelGradoPlanEstudio();
            frg.setRgpGrado(entidadEnEdicion.getSecServicioEducativo().getSduGrado().getGraPk());
            frg.setRgpPlanEstudio(entidadEnEdicion.getSecPlanEstudio().getPesPk());
            frg.setIncluirCampos(new String[]{"rgpAnual"});

            aplicaAnual = Boolean.FALSE;
            esAnual = Boolean.TRUE;
            List<SgRelGradoPlanEstudio> gradoPlan = relGradoPlanEstudioRestClient.buscar(frg);
            if (!gradoPlan.isEmpty() && gradoPlan.get(0).getRgpAnual() != null && !gradoPlan.get(0).getRgpAnual()) {
                aplicaAnual = Boolean.TRUE;
                esAnual = Boolean.FALSE;
             //   esAnual = entidadEnEdicion.getSecTipoPeriodo()==null?Boolean.TRUE: EnumTipoPeriodoSeccion.ANUAL.equals(entidadEnEdicion.getSecTipoPeriodo());
                periodoSeleccionado = entidadEnEdicion.getSecNumeroPeriodo();
            }

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarPlanEstudio() {
        try {
            aplicaAnual = Boolean.FALSE;
            esAnual = Boolean.TRUE;
            periodoSeleccionado =null;
            if (comboPlanEstudio.getSelectedT() != null && servicioEducativoSeleccionado != null) {
                FiltroRelGradoPlanEstudio frg = new FiltroRelGradoPlanEstudio();
                frg.setRgpGrado(servicioEducativoSeleccionado.getSduGrado().getGraPk());
                frg.setRgpPlanEstudio(comboPlanEstudio.getSelectedT().getPesPk());
                frg.setIncluirCampos(new String[]{"rgpAnual"});
                List<SgRelGradoPlanEstudio> gradoPlan = relGradoPlanEstudioRestClient.buscar(frg);           
                if (!gradoPlan.isEmpty() && gradoPlan.get(0).getRgpAnual() != null && !gradoPlan.get(0).getRgpAnual()) {
                    aplicaAnual = Boolean.TRUE;
                    esAnual = Boolean.FALSE;
                }
            }

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public void guardar() {
        try {
            if (!entidadEnEdicion.getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaModalidadEducativa().getModCiclo().getCicNivel().getNivNombreSeccionLibre()) {

                entidadEnEdicion.setSecCodigo(letraSeleccionada);
                entidadEnEdicion.setSecNombre(letraSeleccionada);

            }
            if (BooleanUtils.isTrue(aplicaAnual)) {
                if (BooleanUtils.isTrue(esAnual)) {
                    entidadEnEdicion.setSecTipoPeriodo(EnumTipoPeriodoSeccion.ANUAL);
                    entidadEnEdicion.setSecNumeroPeriodo(null);
                } else {
                    entidadEnEdicion.setSecTipoPeriodo(EnumTipoPeriodoSeccion.COHORTE);
                    entidadEnEdicion.setSecNumeroPeriodo(this.periodoSeleccionado);
                }
            } else {
                entidadEnEdicion.setSecTipoPeriodo(EnumTipoPeriodoSeccion.ANUAL);
                entidadEnEdicion.setSecNumeroPeriodo(null);
            }

            entidadEnEdicion.setSecJornadaLaboral(comboJornadaLaboral.getSelectedT());
            entidadEnEdicion.setSecPlanEstudio(comboPlanEstudio.getSelectedT());
            entidadEnEdicion.setSecEstado(this.comboEstado.getSelectedT());
            entidadEnEdicion.setSecServicioEducativo(servicioEducativoSeleccionado);
            entidadEnEdicion.setSecAnioLectivo(this.comboAnioLectivoInsert.getSelectedT());
            entidadEnEdicion.setSecAsociacion(this.comboAsociaciones != null ? this.comboAsociaciones.getSelectedT() : null);
            entidadEnEdicion.setSecRegimen(this.comboRegimenSeccion != null ? this.comboRegimenSeccion.getSelectedT() : null);
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscar();
            servicioEducativoSeleccionado = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getSecPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            buscar();
            entidadEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public String historial(Long id) {
        try {
            historialSeccion = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void seleccionarDepartamento() {
        try {
            comboMunicipio = new SofisComboG();
            comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            if (comboDepartamento.getSelectedT() != null) {
                FiltroMunicipio fCod = new FiltroMunicipio();
                fCod.setOrderBy(new String[]{"munNombre"});
                fCod.setAscending(new boolean[]{true});
                fCod.setIncluirCampos(new String[]{"munNombre", "munVersion"});
                fCod.setMunDepartamentoFk(comboDepartamento.getSelectedT().getDepPk());
                List<SgMunicipio> municipios = restCatalogo.buscarMunicipio(fCod);
                comboMunicipio = new SofisComboG(municipios, "munNombre");
                comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public SeccionRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(SeccionRestClient restClient) {
        this.restClient = restClient;
    }

    public LazySeccionDataModel getSeccionLazyModel() {
        return seccionLazyModel;
    }

    public void setSeccionLazyModel(LazySeccionDataModel seccionLazyModel) {
        this.seccionLazyModel = seccionLazyModel;
    }

    public List<RevHistorico> getHistorialSeccion() {
        return historialSeccion;
    }

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

    public Long getTotalResultados() {
        return totalResultados;
    }

    public void setTotalResultados(Long totalResultados) {
        this.totalResultados = totalResultados;
    }

    public SgSeccion getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgSeccion entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public SedeRestClient getRestSede() {
        return restSede;
    }

    public void setRestSede(SedeRestClient restSede) {
        this.restSede = restSede;
    }

    public AnioLectivoRestClient getRestAnioLectivo() {
        return restAnioLectivo;
    }

    public void setRestAnioLectivo(AnioLectivoRestClient restAnioLectivo) {
        this.restAnioLectivo = restAnioLectivo;
    }

    public ServicioEducativoRestClient getRestServicioEducativo() {
        return restServicioEducativo;
    }

    public void setRestServicioEducativo(ServicioEducativoRestClient restServicioEducativo) {
        this.restServicioEducativo = restServicioEducativo;
    }

    public FiltroServicioEducativo getFiltroServicioEducativo() {
        return filtroServicioEducativo;
    }

    public void setFiltroServicioEducativo(FiltroServicioEducativo filtroServicioEducativo) {
        this.filtroServicioEducativo = filtroServicioEducativo;
    }

    public LazyServicioEducativoDataModel getServicioEducativoLazyModel() {
        return servicioEducativoLazyModel;
    }

    public void setServicioEducativoLazyModel(LazyServicioEducativoDataModel servicioEducativoLazyModel) {
        this.servicioEducativoLazyModel = servicioEducativoLazyModel;
    }

    public SofisComboG<EnumSeccionEstado> getComboEstado() {
        return comboEstado;
    }

    public void setComboEstado(SofisComboG<EnumSeccionEstado> comboEstado) {
        this.comboEstado = comboEstado;
    }

    public SgServicioEducativo getServicioEducativoSeleccionado() {
        return servicioEducativoSeleccionado;
    }

    public void setServicioEducativoSeleccionado(SgServicioEducativo servicioEducativoSeleccionado) {
        this.servicioEducativoSeleccionado = servicioEducativoSeleccionado;
    }

    public SofisComboG<SgAnioLectivo> getComboAnioLectivoInsert() {
        return comboAnioLectivoInsert;
    }

    public void setComboAnioLectivoInsert(SofisComboG<SgAnioLectivo> comboAnioLectivoInsert) {
        this.comboAnioLectivoInsert = comboAnioLectivoInsert;
    }

    public SofisComboG<SgPlanEstudio> getComboPlanEstudio() {
        return comboPlanEstudio;
    }

    public void setComboPlanEstudio(SofisComboG<SgPlanEstudio> comboPlanEstudio) {
        this.comboPlanEstudio = comboPlanEstudio;
    }

    public SofisComboG<SgJornadaLaboral> getComboJornadaLaboral() {
        return comboJornadaLaboral;
    }

    public void setComboJornadaLaboral(SofisComboG<SgJornadaLaboral> comboJornadaLaboral) {
        this.comboJornadaLaboral = comboJornadaLaboral;
    }

    public PlanesEstudioRestClient getRestPlanEstudio() {
        return restPlanEstudio;
    }

    public void setRestPlanEstudio(PlanesEstudioRestClient restPlanEstudio) {
        this.restPlanEstudio = restPlanEstudio;
    }

    public CatalogosRestClient getRestCatalogo() {
        return restCatalogo;
    }

    public void setRestCatalogo(CatalogosRestClient restCatalogo) {
        this.restCatalogo = restCatalogo;
    }

    public SofisComboG<SgDepartamento> getComboDepartamento() {
        return comboDepartamento;
    }

    public void setComboDepartamento(SofisComboG<SgDepartamento> comboDepartamento) {
        this.comboDepartamento = comboDepartamento;
    }

    public SofisComboG<SgMunicipio> getComboMunicipio() {
        return comboMunicipio;
    }

    public void setComboMunicipio(SofisComboG<SgMunicipio> comboMunicipio) {
        this.comboMunicipio = comboMunicipio;
    }

    public NivelRestClient getRestNivel() {
        return restNivel;
    }

    public void setRestNivel(NivelRestClient restNivel) {
        this.restNivel = restNivel;
    }

    public SgSede getSedeEnEdicion() {
        return sedeEnEdicion;
    }

    public void setSedeEnEdicion(SgSede sedeEnEdicion) {
        this.sedeEnEdicion = sedeEnEdicion;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public SgAnioLectivo getAnioFiltroPorDefecto() {
        return anioFiltroPorDefecto;
    }

    public void setAnioFiltroPorDefecto(SgAnioLectivo anioFiltroPorDefecto) {
        this.anioFiltroPorDefecto = anioFiltroPorDefecto;
    }

    public Long getTotalSecciones() {
        return totalSecciones;
    }

    public void setTotalSecciones(Long totalSecciones) {
        this.totalSecciones = totalSecciones;
    }

    public List<SgSeccion> getSeccionesCopia() {
        return seccionesCopia;
    }

    public void setSeccionesCopia(List<SgSeccion> seccionesCopia) {
        this.seccionesCopia = seccionesCopia;
    }

    public List<SgSeccion> getSeccionesCopiaSelected() {
        return seccionesCopiaSelected;
    }

    public void setSeccionesCopiaSelected(List<SgSeccion> seccionesCopiaSelected) {
        this.seccionesCopiaSelected = seccionesCopiaSelected;
    }

    public Integer getAnioLectivoOrigen() {
        return anioLectivoOrigen;
    }

    public void setAnioLectivoOrigen(Integer anioLectivoOrigen) {
        this.anioLectivoOrigen = anioLectivoOrigen;
    }

    public SgAnioLectivo getAnioLectivoDestino() {
        return anioLectivoDestino;
    }

    public void setAnioLectivoDestino(SgAnioLectivo anioLectivoDestino) {
        this.anioLectivoDestino = anioLectivoDestino;
    }

    public FiltrosSeccionBean getFiltrosSeccion() {
        return filtrosSeccion;
    }

    public void setFiltrosSeccion(FiltrosSeccionBean filtrosSeccion) {
        this.filtrosSeccion = filtrosSeccion;
    }

    public SofisComboG<SgAula>[] getComboAula() {
        return comboAula;
    }

    public void setComboAula(SofisComboG<SgAula>[] comboAula) {
        this.comboAula = comboAula;
    }

    public SofisComboG<SgAnioLectivo> getComboAnio() {
        return comboAnio;
    }

    public void setComboAnio(SofisComboG<SgAnioLectivo> comboAnio) {
        this.comboAnio = comboAnio;
    }

    public List<SgSeccion> getSeccionesAula() {
        return seccionesAula;
    }

    public void setSeccionesAula(List<SgSeccion> seccionesAula) {
        this.seccionesAula = seccionesAula;
    }

    public List<SgAula> getListAulas() {
        return listAulas;
    }

    public void setListAulas(List<SgAula> listAulas) {
        this.listAulas = listAulas;
    }

    public Boolean[] getSeccionConCambioAula() {
        return seccionConCambioAula;
    }

    public void setSeccionConCambioAula(Boolean[] seccionConCambioAula) {
        this.seccionConCambioAula = seccionConCambioAula;
    }

    public List<SgSeccion> getSeccionesAulaOriginal() {
        return seccionesAulaOriginal;
    }

    public void setSeccionesAulaOriginal(List<SgSeccion> seccionesAulaOriginal) {
        this.seccionesAulaOriginal = seccionesAulaOriginal;
    }

    public SofisComboG<SgNivel> getComboNivelAula() {
        return comboNivelAula;
    }

    public void setComboNivelAula(SofisComboG<SgNivel> comboNivelAula) {
        this.comboNivelAula = comboNivelAula;
    }

    public SgCalendario getCalendarioSede() {
        return calendarioSede;
    }

    public void setCalendarioSede(SgCalendario calendarioSede) {
        this.calendarioSede = calendarioSede;
    }

    public Boolean getRenderSeccionIntegrada() {
        return renderSeccionIntegrada;
    }

    public void setRenderSeccionIntegrada(Boolean renderSeccionIntegrada) {
        this.renderSeccionIntegrada = renderSeccionIntegrada;
    }

    public SofisComboG<SgAsociacion> getComboAsociaciones() {
        return comboAsociaciones;
    }

    public void setComboAsociaciones(SofisComboG<SgAsociacion> comboAsociaciones) {
        this.comboAsociaciones = comboAsociaciones;
    }

    public Boolean getRenderDatosSeccionModalidadFlexible() {
        return renderDatosSeccionModalidadFlexible;
    }

    public void setRenderDatosSeccionModalidadFlexible(Boolean renderDatosSeccionModalidadFlexible) {
        this.renderDatosSeccionModalidadFlexible = renderDatosSeccionModalidadFlexible;
    }

    public SofisComboG<EnumRegimenSeccion> getComboRegimenSeccion() {
        return comboRegimenSeccion;
    }

    public void setComboRegimenSeccion(SofisComboG<EnumRegimenSeccion> comboRegimenSeccion) {
        this.comboRegimenSeccion = comboRegimenSeccion;
    }

    public String getLetraSeleccionada() {
        return letraSeleccionada;
    }

    public void setLetraSeleccionada(String letraSeleccionada) {
        this.letraSeleccionada = letraSeleccionada;
    }

    public List<SelectItem> getLetras() {
        return letras;
    }

    public void setLetras(List<SelectItem> letras) {
        this.letras = letras;
    }

    public Boolean getAplicaAnual() {
        return aplicaAnual;
    }

    public void setAplicaAnual(Boolean aplicaAnual) {
        this.aplicaAnual = aplicaAnual;
    }

    public List<SelectItem> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<SelectItem> periodos) {
        this.periodos = periodos;
    }

    public Integer getPeriodoSeleccionado() {
        return periodoSeleccionado;
    }

    public void setPeriodoSeleccionado(Integer periodoSeleccionado) {
        this.periodoSeleccionado = periodoSeleccionado;
    }

    public Boolean getEsAnual() {
        return esAnual;
    }

    public void setEsAnual(Boolean esAnual) {
        this.esAnual = esAnual;
    }

    public Integer getPromocionSeleccionada() {
        return promocionSeleccionada;
    }

    public void setPromocionSeleccionada(Integer promocionSeleccionada) {
        this.promocionSeleccionada = promocionSeleccionada;
    }

}
