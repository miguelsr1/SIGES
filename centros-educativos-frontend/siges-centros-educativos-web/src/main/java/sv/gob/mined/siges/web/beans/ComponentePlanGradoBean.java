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
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgComponentePlanEstudio;
import sv.gob.mined.siges.web.dto.SgComponentePlanGrado;
import sv.gob.mined.siges.web.dto.SgGrado;
import sv.gob.mined.siges.web.dto.SgModalidad;
import sv.gob.mined.siges.web.dto.SgPlanEstudio;
import sv.gob.mined.siges.web.dto.catalogo.SgEscalaCalificacion;
import sv.gob.mined.siges.web.dto.catalogo.SgProgramaEducativo;
import sv.gob.mined.siges.web.enumerados.EnumCalculoNotaInstitucional;
import sv.gob.mined.siges.web.enumerados.EnumFuncionRedondeo;
import sv.gob.mined.siges.web.enumerados.TipoComponentePlanEstudio;
import sv.gob.mined.siges.web.enumerados.TipoEscalaCalificacion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroComponentePlanEstudio;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroComponentePlanGrado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEscalaCalificacion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroGrado;
import sv.gob.mined.siges.web.lazymodels.LazyComponentePlanGradoDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.ComponentePlanEstudioRestClient;
import sv.gob.mined.siges.web.restclient.ComponentePlanGradoRestClient;
import sv.gob.mined.siges.web.restclient.GradoRestClient;
import sv.gob.mined.siges.web.restclient.PlanesEstudioRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.SgCalificacionCE;
import sv.gob.mined.siges.web.dto.SgEstudianteImpresion;
import sv.gob.mined.siges.web.dto.SgRelGradoPlanEstudio;
import sv.gob.mined.siges.web.dto.SgTitulo;
import sv.gob.mined.siges.web.dto.catalogo.SgDefinicionTitulo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelGradoPlanEstudio;
import sv.gob.mined.siges.web.restclient.RelGradoPlanEstudioRestClient;
import sv.gob.mined.siges.web.dto.catalogo.SgFormula;
import sv.gob.mined.siges.web.enumerados.EnumEstadoSolicitudImpresion;
import sv.gob.mined.siges.web.enumerados.EnumTipoFormula;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCalificacion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEstudianteImpresion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPlanEstudio;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroTitulo;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroDefinicionTitulo;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroFormula;
import sv.gob.mined.siges.web.restclient.CalificacionRestClient;
import sv.gob.mined.siges.web.restclient.EstudianteImpresionRestClient;
import sv.gob.mined.siges.web.restclient.TituloRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class ComponentePlanGradoBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ComponentePlanGradoBean.class.getName());

    @Inject
    @Param(name = "id")
    private Long planEstId;

    @Inject
    private ComponentePlanGradoRestClient restClient;

    @Inject
    private PlanesEstudioRestClient planEstRest;

    @Inject
    private ComponentePlanEstudioRestClient cpeRestClient;

    @Inject
    private CatalogosRestClient catalogoRestClient;

    @Inject
    private GradoRestClient gradoRestClient;

    @Inject
    private RelGradoPlanEstudioRestClient relGradoPlanEstRestClient;

    @Inject
    private CalificacionRestClient calificacionRestClient;
    
    @Inject
    private TituloRestClient titulosRestClient;
    
    @Inject
    private EstudianteImpresionRestClient estudianteImpresionRestClient;

    @Inject
    private SessionBean sessionBean;

    private SgComponentePlanGrado entidadEnEdicion = new SgComponentePlanGrado();
    private List<RevHistorico> historialComponentePlanGrado = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyComponentePlanGradoDataModel componentePlanGradoLazyModel;
    private SgPlanEstudio planEstEnEdicion;
    private List<SgGrado> listaGrados;
    private SgGrado cpgGrado;
    private SofisComboG<SgEscalaCalificacion> comboEscalaCalificacion;
    private SgComponentePlanEstudio componentesPlanEstudioSeleccionado;
    private SofisComboG<SgProgramaEducativo> comboProgEducativo;
    private SofisComboG<TipoComponentePlanEstudio> comboTiposCompPlanEst;
    private SofisComboG<SgFormula> comboFormulasComponentePlanGrado;
    private SofisComboG<SgFormula> comboFormulasGrado;
    private SofisComboG<SgFormula> comboFormulasPP;
    private SofisComboG<SgFormula> comboFormulasPS;
    private SofisComboG<SgFormula> comboFormulasSP;
    private SofisComboG<SgFormula> comboFormulasSS;
    private SgModalidad modalidadEnEdicion;
    private SofisComboG<EnumCalculoNotaInstitucional> comboCalculoNotaInstitucional;
    private SofisComboG<EnumFuncionRedondeo> comboFuncionRedondeo;
    private SgRelGradoPlanEstudio relGradoPlanEst;
    private Boolean esEscalaNumerica = Boolean.FALSE;
    private SofisComboG<SgComponentePlanGrado> parametroAprobacion;
    private SofisComboG<SgComponentePlanGrado> parametroFormulaPP;
    private SofisComboG<SgComponentePlanGrado> parametroFormulaPS;
    private SofisComboG<SgComponentePlanGrado> parametroFormulaSP;
    private SofisComboG<SgComponentePlanGrado> parametroFormulaSS;
    private Boolean componenteSePuedeEliminar;

    public ComponentePlanGradoBean() {

    }

    public void redirectToIndex() throws Exception {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.redirect(externalContext.getRequestContextPath());
    }

    @PostConstruct
    public void init() {

        validarAcceso();
        try {
            if (planEstId == null || planEstId <= 0) {
                redirectToIndex();
                return;
            }

            this.cargarPlanEst(planEstId);
            cargarCombos();
        } catch (HttpClientException ce) {
            JSFUtils.redirectNotFound();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }

    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.BUSCAR_COMPONENTES_GRADO)) {
            JSFUtils.redirectToIndex();
        }
    }

    public void cargarPlanEst(Long planEstudioId) {
        try {

            FiltroPlanEstudio filtro = new FiltroPlanEstudio();
            filtro.setPesPk(planEstudioId);
            filtro.setIncluirCampos(new String[]{
                "pesCodigo",
                "pesNombre",
                "pesRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivNombre",
                "pesRelacionModalidad.reaModalidadEducativa.modCiclo.cicNombre",
                "pesRelacionModalidad.reaModalidadEducativa.modNombre",
                "pesRelacionModalidad.reaModalidadAtencion.matNombre",
                "pesRelacionModalidad.reaPk",
                "pesOpcion.opcNombre",
                "pesProgramaEducativo.pedNombre",
                "pesVersion"});
            planEstEnEdicion = planEstRest.buscar(filtro).get(0);

            FiltroGrado fg = new FiltroGrado();
            fg.setHabilitado(Boolean.TRUE);
            fg.setRelModEdModAtenPk(planEstEnEdicion.getPesRelacionModalidad().getReaPk());
            fg.setIncluirCampos(new String[]{"graPk", "graNombre", "graVersion"});
            fg.setOrderBy(new String[]{"graOrden"});
            fg.setAscending(new boolean[]{true});
            listaGrados = gradoRestClient.buscar(fg);
            for (SgGrado g : listaGrados) {
                g.setGraCompPlanGrado(obtenerComponentesPlanGrado(g.getGraPk(), planEstEnEdicion.getPesPk())); //TODO: optimizar en una sola query y luego agrupar
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        try {
            FiltroEscalaCalificacion fec = new FiltroEscalaCalificacion();
            fec.setEcaHabilitado(Boolean.TRUE);
            List<SgEscalaCalificacion> listaSectorEconomico = catalogoRestClient.buscarEscalaCalificacion(fec);
            comboEscalaCalificacion = new SofisComboG(listaSectorEconomico, "ecaNombre");
            comboEscalaCalificacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboEscalaCalificacion.ordenar();

            List<TipoComponentePlanEstudio> escalas = new ArrayList(Arrays.asList(TipoComponentePlanEstudio.values()));
            escalas.remove(TipoComponentePlanEstudio.AESS);
            comboTiposCompPlanEst = new SofisComboG(escalas, "text");
            comboTiposCompPlanEst.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboCalculoNotaInstitucional = new SofisComboG();
            comboCalculoNotaInstitucional.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            List<EnumFuncionRedondeo> funcionesRedondeo = new ArrayList(Arrays.asList(EnumFuncionRedondeo.values()));
            comboFuncionRedondeo = new SofisComboG(funcionesRedondeo, "text");
            comboFuncionRedondeo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            FiltroFormula fc = new FiltroFormula();
            fc.setHabilitado(Boolean.TRUE);

            fc.setListTipoFormula(Arrays.asList(new EnumTipoFormula[]{EnumTipoFormula.COMPONENTE_PLAN_ESTUDIO, EnumTipoFormula.COMPONENTE_PLAN_ESTUDIO_PARAM}));
            fc.setAscending(new boolean[]{true});
            fc.setOrderBy(new String[]{"fomNombre"});
            List<SgFormula> listaFormula = catalogoRestClient.buscarFormula(fc);
            comboFormulasComponentePlanGrado = new SofisComboG(listaFormula, "fomNombre");
            comboFormulasComponentePlanGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            fc.setListTipoFormula(null);
            fc.setTipoFormula(EnumTipoFormula.GRADO);
            fc.setAscending(new boolean[]{true});
            fc.setOrderBy(new String[]{"fomNombre"});
            listaFormula = catalogoRestClient.buscarFormula(fc);
            comboFormulasGrado = new SofisComboG(listaFormula, "fomNombre");
            comboFormulasGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            List<EnumTipoFormula> listTipo = new ArrayList();
            fc.setTipoFormula(null);
            listTipo.add(EnumTipoFormula.PRUEBA_EXTRAORDINARIA);
            listTipo.add(EnumTipoFormula.PRUEBA_EXTRAORDINARIA_PARAM);
            fc.setListTipoFormula(listTipo);
            fc.setAscending(new boolean[]{true});
            fc.setOrderBy(new String[]{"fomNombre"});
            listaFormula = catalogoRestClient.buscarFormula(fc);
            comboFormulasPP = new SofisComboG(listaFormula, "fomNombre");
            comboFormulasPP.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboFormulasPS = new SofisComboG(listaFormula, "fomNombre");
            comboFormulasPS.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboFormulasSP = new SofisComboG(listaFormula, "fomNombre");
            comboFormulasSP.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboFormulasSS = new SofisComboG(listaFormula, "fomNombre");
            comboFormulasSS.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            parametroFormulaPP = new SofisComboG();
            parametroFormulaPP.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            parametroFormulaPS = new SofisComboG();
            parametroFormulaPS.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            parametroFormulaSP = new SofisComboG();
            parametroFormulaSP.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            parametroFormulaSS = new SofisComboG();
            parametroFormulaSS.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            parametroAprobacion = new SofisComboG();
            parametroAprobacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cambioReqValidacionAcademica() {
        if (BooleanUtils.isFalse(relGradoPlanEst.getRgpRequiereValidacionAcademica())) {
            relGradoPlanEst.setRgpPermiteCalificarSinMatValidada(Boolean.FALSE);
            relGradoPlanEst.setRgpPermiteValidarMatriculaSinNIE(Boolean.FALSE);
        }
    }

    public void eleccionEscalaCalificacion() {
        esEscalaNumerica = Boolean.FALSE;
        if (comboEscalaCalificacion.getSelectedT() != null) {
            List<EnumCalculoNotaInstitucional> list = new ArrayList();
            list.add(EnumCalculoNotaInstitucional.MAY);
            list.add(EnumCalculoNotaInstitucional.ULT);
            if (comboEscalaCalificacion.getSelectedT().getEcaTipoEscala().equals(TipoEscalaCalificacion.NUMERICA)) {
                list.add(EnumCalculoNotaInstitucional.PROM);
                list.add(EnumCalculoNotaInstitucional.MED);
                esEscalaNumerica = Boolean.TRUE;
            }
            entidadEnEdicion.setCpgPrecision(null);
            comboCalculoNotaInstitucional = new SofisComboG(list, "text");
            comboCalculoNotaInstitucional.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        }

    }

    public void cargarComponentePlanEstudio() {
        try {
            this.componentesPlanEstudioSeleccionado = null;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {
        comboFormulasComponentePlanGrado.setSelected(-1);
        comboFormulasGrado.setSelected(-1);
        this.comboTiposCompPlanEst.setSelected(-1);
        this.componentesPlanEstudioSeleccionado = null;
        this.comboEscalaCalificacion.setSelected(-1);
        comboCalculoNotaInstitucional.setSelected(-1);
        comboFuncionRedondeo.setSelected(-1);
        comboFormulasPP.setSelected(-1);
        comboFormulasPS.setSelected(-1);
        comboFormulasSP.setSelected(-1);
        comboFormulasSS.setSelected(-1);
        parametroAprobacion.setSelected(-1);
    }

    public void limpiar() {
    }

    public void cargarCombosParametroFormula(SgGrado gra) {
        List<SgGrado> graList = listaGrados.stream().filter(c -> c.getGraPk().equals(gra.getGraPk())).collect(Collectors.toList());
        List<SgComponentePlanGrado> cpg = graList.get(0).getGraCompPlanGrado();
        List<SgComponentePlanGrado> cpgPrueba = new ArrayList();
        if (cpg != null && !cpg.isEmpty()) {
            for (SgComponentePlanGrado comp : cpg) {
                if (TipoComponentePlanEstudio.PRU.equals(comp.getCpgComponentePlanEstudio().getCpeTipo())) {
                    cpgPrueba.add(comp);
                }
            }
        }
        parametroFormulaPP = new SofisComboG(cpgPrueba, "cpgNombrePublicable");
        parametroFormulaPP.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        parametroFormulaPS = new SofisComboG(cpgPrueba, "cpgNombrePublicable");
        parametroFormulaPS.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        parametroFormulaSP = new SofisComboG(cpgPrueba, "cpgNombrePublicable");
        parametroFormulaSP.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        parametroFormulaSS = new SofisComboG(cpgPrueba, "cpgNombrePublicable");
        parametroFormulaSS.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        parametroAprobacion = new SofisComboG(cpgPrueba, "cpgNombrePublicable");
        parametroAprobacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
    }

    public List<SgDefinicionTitulo> completeDefinicionTitulo(String query) {
        try {
            FiltroDefinicionTitulo fil = new FiltroDefinicionTitulo();
            fil.setNombre(query);
            fil.setHabilitado(Boolean.TRUE);
            fil.setOrderBy(new String[]{"dtiNombre"});
            fil.setAscending(new boolean[]{false});
            return this.relGradoPlanEst.getRgpDefinicionTitulo() != null
                    ? catalogoRestClient.buscarDefinicionTitulo(fil).stream()
                            .filter(i -> !this.relGradoPlanEst.getRgpDefinicionTitulo().contains(i))
                            .collect(Collectors.toList())
                    : catalogoRestClient.buscarDefinicionTitulo(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void agregar(SgGrado grd) {
        limpiarCombos();
        cargarCombosParametroFormula(grd);
        entidadEnEdicion = new SgComponentePlanGrado();
        cpgGrado = grd;
        JSFUtils.limpiarMensajesError();

    }

    public void actualizar(SgComponentePlanGrado var) {
        limpiarCombos();
        try {
            entidadEnEdicion = restClient.obtenerPorId(var.getCpgPk());
            if (entidadEnEdicion.getCpgEscalaCalificacion() != null) {
                comboEscalaCalificacion.setSelectedT(entidadEnEdicion.getCpgEscalaCalificacion());
                esEscalaNumerica = TipoEscalaCalificacion.NUMERICA.equals(entidadEnEdicion.getCpgEscalaCalificacion().getEcaTipoEscala());
            }
            if (entidadEnEdicion.getCpgComponentePlanEstudio() != null) {
                comboTiposCompPlanEst.setSelectedT(entidadEnEdicion.getCpgComponentePlanEstudio().getCpeTipo());
                cargarComponentePlanEstudio();
                componentesPlanEstudioSeleccionado = entidadEnEdicion.getCpgComponentePlanEstudio();
            }
            Integer precision = entidadEnEdicion.getCpgPrecision();
            eleccionEscalaCalificacion();
            entidadEnEdicion.setCpgPrecision(precision);
            if (entidadEnEdicion.getCpgCalculoNotaInstitucional() != null) {
                comboCalculoNotaInstitucional.setSelectedT(entidadEnEdicion.getCpgCalculoNotaInstitucional());
                comboFuncionRedondeo.setSelectedT(entidadEnEdicion.getCpgFuncionRedondeo());
            }
            if (entidadEnEdicion.getCpgFormula() != null) {
                comboFormulasComponentePlanGrado.setSelectedT(entidadEnEdicion.getCpgFormula());
            }
            comboFormulasPP.setSelectedT(entidadEnEdicion.getCpgFormulaHabilitacionPP() != null ? entidadEnEdicion.getCpgFormulaHabilitacionPP() : null);
            comboFormulasPS.setSelectedT(entidadEnEdicion.getCpgFormulaHabilitacionPS() != null ? entidadEnEdicion.getCpgFormulaHabilitacionPS() : null);
            comboFormulasSP.setSelectedT(entidadEnEdicion.getCpgFormulaHabilitacionSP() != null ? entidadEnEdicion.getCpgFormulaHabilitacionSP() : null);
            comboFormulasSS.setSelectedT(entidadEnEdicion.getCpgFormulaHabilitacionSS() != null ? entidadEnEdicion.getCpgFormulaHabilitacionSS() : null);

            cargarCombosParametroFormula(var.getCpgGrado());
            parametroFormulaPP.setSelectedT(entidadEnEdicion.getCpgParametroFormulaPruebaPP() != null ? entidadEnEdicion.getCpgParametroFormulaPruebaPP() : null);
            parametroFormulaPS.setSelectedT(entidadEnEdicion.getCpgParametroFormulaPruebaPS() != null ? entidadEnEdicion.getCpgParametroFormulaPruebaPS() : null);
            parametroFormulaSP.setSelectedT(entidadEnEdicion.getCpgParametroFormulaPruebaSP() != null ? entidadEnEdicion.getCpgParametroFormulaPruebaSP() : null);
            parametroFormulaSS.setSelectedT(entidadEnEdicion.getCpgParametroFormulaPruebaSS() != null ? entidadEnEdicion.getCpgParametroFormulaPruebaSS() : null);
            parametroAprobacion.setSelectedT(entidadEnEdicion.getCpgParametroFormulaAprobacion() != null ? entidadEnEdicion.getCpgParametroFormulaAprobacion() : null);

            cpgGrado = entidadEnEdicion.getCpgGrado();
            JSFUtils.limpiarMensajesError();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void prepararParaEliminar(SgComponentePlanGrado var) {
        limpiarCombos();
        try {
            componenteSePuedeEliminar=Boolean.TRUE;
            FiltroComponentePlanGrado fpg = new FiltroComponentePlanGrado();
            fpg.setCpgPk(var.getCpgPk());
            fpg.setIncluirCampos(new String[]{"cpgPk", "cpgGrado.graPk",
                "cpgPlanEstudio.pesPk",
                "cpgComponentePlanEstudio.cpePk",
                "cpgComponentePlanEstudio.cpeNombre",
                "cpgComponentePlanEstudio.cpeTipo",
                "cpgComponentePlanEstudio.cpeVersion",
                "cpgExclusivoSeccion.secPk",
                "cpgExclusivoSeccion.secVersion", "cpgVersion"});

            List<SgComponentePlanGrado> listCpg = restClient.buscar(fpg);
            if (!listCpg.isEmpty()) {
                entidadEnEdicion = listCpg.get(0);
                FiltroCalificacion fc = new FiltroCalificacion();
                fc.setSecGradoFk(entidadEnEdicion.getCpgGrado().getGraPk());
                fc.setSecPlanEstudioFk(entidadEnEdicion.getCpgPlanEstudio().getPesPk());
                fc.setCalComponentePlanEstudio(entidadEnEdicion.getCpgComponentePlanEstudio().getCpePk());
                fc.setIncluirCampos(new String[]{"calPk"});
                fc.setMaxResults(1L);
                List<SgCalificacionCE> calificaciones = calificacionRestClient.buscar(fc);
                if (!calificaciones.isEmpty()) {

                    //5-ANULAR TITULO O ANULAR SOLICITUD
                    FiltroTitulo ft = new FiltroTitulo();
                    ft.setTitNoAnulada(Boolean.TRUE);
                    ft.setTitGradoFk(entidadEnEdicion.getCpgGrado().getGraPk());
                    ft.setTitPlanEstudioFk(entidadEnEdicion.getCpgPlanEstudio().getPesPk());
                    ft.setIncluirCampos(new String[]{"titVersion"                        
                    });
                    ft.setMaxResults(1L);
                    List<SgTitulo> listTitulo = titulosRestClient.buscar(ft);
                    if (!listTitulo.isEmpty()) {
                        componenteSePuedeEliminar=Boolean.FALSE;
                       JSFUtils.agregarError("eliminarMsg", "El componente no puede ser eliminado dado que existe un estudiante con título generado", "");
                        return;
                    } else {
                        FiltroEstudianteImpresion fei = new FiltroEstudianteImpresion();
                        fei.setEimGradoFk(entidadEnEdicion.getCpgGrado().getGraPk());
                        fei.setEimNoAnulada(Boolean.TRUE);
                        fei.setEimPlanEstudioFk(entidadEnEdicion.getCpgPlanEstudio().getPesPk());
                        List<EnumEstadoSolicitudImpresion> listEstado = new ArrayList<>();
                        listEstado.add(EnumEstadoSolicitudImpresion.ENVIADA);
                        listEstado.add(EnumEstadoSolicitudImpresion.CON_EXCEPCIONES);
                        listEstado.add(EnumEstadoSolicitudImpresion.CONFIRMADA);
                        fei.setEimEstadosSolicitud(listEstado);
                        fei.setIncluirCampos(new String[]{"eimVersion"
                        });
                        fei.setMaxResults(1L);
                        List<SgEstudianteImpresion> solIMpList = estudianteImpresionRestClient.buscar(fei);

                      if(!solIMpList.isEmpty()){
                          componenteSePuedeEliminar=Boolean.FALSE;
                          JSFUtils.agregarError("eliminarMsg", "El componente no puede ser eliminado dado que existe un estudiante con título generado", "");
                          return;
                      }

                    }

                    JSFUtils.agregarWarn("eliminarMsg", "ADVERTENCIA: El componente que desea eliminar tiene calificaciones asociadas, de continuar las mismas serán eliminadas", "");
                }
            } else {
                entidadEnEdicion = null;
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardar() {
        try {
            if (StringUtils.isBlank(entidadEnEdicion.getCpgNombrePublicable())) {
                entidadEnEdicion.setCpgNombrePublicable(componentesPlanEstudioSeleccionado != null ? componentesPlanEstudioSeleccionado.getCpeNombre() : null);
            }
            entidadEnEdicion.setCpgPlanEstudio(planEstEnEdicion);
            entidadEnEdicion.setCpgGrado(cpgGrado);
            entidadEnEdicion.setCpgComponentePlanEstudio(componentesPlanEstudioSeleccionado);
            entidadEnEdicion.setCpgEscalaCalificacion(comboEscalaCalificacion.getSelectedT());
            entidadEnEdicion.setCpgCalculoNotaInstitucional(comboCalculoNotaInstitucional.getSelectedT());
            entidadEnEdicion.setCpgFuncionRedondeo(EnumCalculoNotaInstitucional.PROM.equals(comboCalculoNotaInstitucional.getSelectedT()) ? comboFuncionRedondeo.getSelectedT() : null);
            entidadEnEdicion.setCpgFormula(comboFormulasComponentePlanGrado.getSelectedT());

            entidadEnEdicion.setCpgFormulaHabilitacionPP(comboFormulasPP.getSelectedT());
            entidadEnEdicion.setCpgFormulaHabilitacionPS(comboFormulasPS.getSelectedT());
            entidadEnEdicion.setCpgFormulaHabilitacionSP(comboFormulasSP.getSelectedT());
            entidadEnEdicion.setCpgFormulaHabilitacionSS(comboFormulasSS.getSelectedT());

            entidadEnEdicion.setCpgParametroFormulaPruebaPP(parametroFormulaPP.getSelectedT());
            entidadEnEdicion.setCpgParametroFormulaPruebaPS(parametroFormulaPS.getSelectedT());
            entidadEnEdicion.setCpgParametroFormulaPruebaSP(parametroFormulaSP.getSelectedT());
            entidadEnEdicion.setCpgParametroFormulaPruebaSS(parametroFormulaSS.getSelectedT());
            entidadEnEdicion.setCpgParametroFormulaAprobacion(parametroAprobacion.getSelectedT());

            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            this.cargarPlanEst(planEstEnEdicion.getPesPk());
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminar() {
        try {
            if(BooleanUtils.isTrue(componenteSePuedeEliminar)){
                restClient.eliminarComponentePlanGrado(entidadEnEdicion);
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
                cargarPlanEst(planEstId);
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void historial(Long id) {
        try {
            historialComponentePlanGrado = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public List<SgComponentePlanGrado> obtenerComponentesPlanGrado(Long gradoPk, Long planEstudioPk) {
        try {

            //Consultar la formula del grado
            FiltroRelGradoPlanEstudio fgpe = new FiltroRelGradoPlanEstudio();
            fgpe.setRgpGrado(gradoPk);
            fgpe.setRgpPlanEstudio(planEstudioPk);
            List<SgRelGradoPlanEstudio> relGradoPlan = relGradoPlanEstRestClient.buscar(fgpe);
            SgRelGradoPlanEstudio formula = new SgRelGradoPlanEstudio();
            if (relGradoPlan.size() > 0) {
                formula = relGradoPlan.get(0);
            }

            //Obtener los componenes del plan grado
            FiltroComponentePlanGrado fcpg = new FiltroComponentePlanGrado();
            fcpg.setCpgGradoPk(gradoPk);
            fcpg.setCpgPlanEstudioPk(planEstudioPk);
            fcpg.setIncluirCampos(new String[]{
                "cpgNombrePublicable",
                "cpgObjetoPromocion",
                "cpgCantidadHorasSemanales",
                "cpgPeriodosCalificacion",
                "cpgCantidadPrimeraPrueba",
                "cpgCantidadPrimeraSuficiencia",
                "cpgCantidadSegundaPrueba",
                "cpgCantidadSegundaSuficiencia",
                "cpgEscalaCalificacion.ecaNombre",
                "cpgComponentePlanEstudio.cpePk",
                "cpgComponentePlanEstudio.cpeNombre",
                "cpgComponentePlanEstudio.cpeTipo",
                "cpgComponentePlanEstudio.cpeVersion",
                "cpgGrado.graPk",
                "cpgVersion",
                "cpgOrden"});
            if (formula != null && BooleanUtils.isTrue(formula.getRgpConsiderarOrden())) {
                fcpg.setOrderBy(new String[]{"cpgOrden"});
            } else {
                fcpg.setOrderBy(new String[]{"cpgNombrePublicable"});
            }
            fcpg.setAscending(new boolean[]{true});
            return restClient.buscar(fcpg);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return new ArrayList();
    }

    public List<SgComponentePlanEstudio> completePlanEstudio(String query) {
        try {
            FiltroComponentePlanEstudio fil = new FiltroComponentePlanEstudio();
            fil.setCpeNombre(query);
            fil.setCpeHabilitado(Boolean.TRUE);
            fil.setCpeTipo(comboTiposCompPlanEst.getSelectedT());
            fil.setMaxResults(11L);
            fil.setOrderBy(new String[]{"cpeNombre"});
            fil.setAscending(new boolean[]{true});
            return cpeRestClient.buscar(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void agregarFormula(SgGrado grado) {
        try {
            limpiarCombos();
            relGradoPlanEst = new SgRelGradoPlanEstudio();
            relGradoPlanEst.setRgpGradoFk(grado);
            relGradoPlanEst.setRgpPlanEstudioFk(planEstEnEdicion);
            comboFormulasGrado.setSelected(-1);
            relGradoPlanEst.setRgpDefinicionTitulo(new ArrayList());

            FiltroRelGradoPlanEstudio fgpe = new FiltroRelGradoPlanEstudio();
            fgpe.setRgpGrado(grado.getGraPk());
            fgpe.setRgpPlanEstudio(planEstEnEdicion.getPesPk());
            List<SgRelGradoPlanEstudio> relGradoPlan = relGradoPlanEstRestClient.buscar(fgpe);
            if (relGradoPlan.size() > 0) {
                relGradoPlanEst = relGradoPlan.get(0);
                comboFormulasGrado.setSelectedT(relGradoPlanEst.getRgpFormulaFk() != null ? relGradoPlanEst.getRgpFormulaFk() : null);
                comboFormulasPP.setSelectedT(relGradoPlanEst.getRgpFormulaHabilitacionPP() != null ? relGradoPlanEst.getRgpFormulaHabilitacionPP() : null);
                comboFormulasPS.setSelectedT(relGradoPlanEst.getRgpFormulaHabilitacionPS() != null ? relGradoPlanEst.getRgpFormulaHabilitacionPS() : null);
                comboFormulasSP.setSelectedT(relGradoPlanEst.getRgpFormulaHabilitacionSP() != null ? relGradoPlanEst.getRgpFormulaHabilitacionSP() : null);
                comboFormulasSS.setSelectedT(relGradoPlanEst.getRgpFormulaHabilitacionSS() != null ? relGradoPlanEst.getRgpFormulaHabilitacionSS() : null);
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public void guardarFormula() {
        try {

            relGradoPlanEst.setRgpFormulaFk(comboFormulasGrado.getSelectedT());
            relGradoPlanEst.setRgpFormulaHabilitacionPP(comboFormulasPP.getSelectedT());
            relGradoPlanEst.setRgpFormulaHabilitacionPS(comboFormulasPS.getSelectedT());
            relGradoPlanEst.setRgpFormulaHabilitacionSP(comboFormulasSP.getSelectedT());
            relGradoPlanEst.setRgpFormulaHabilitacionSS(comboFormulasSS.getSelectedT());
            relGradoPlanEstRestClient.guardar(relGradoPlanEst);
            PrimeFaces.current().executeScript("PF('itemDialogFormula').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP_2, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarAprobacion() {
        entidadEnEdicion.setCpgFormula(comboFormulasComponentePlanGrado.getSelectedT());
        if (comboFormulasComponentePlanGrado.getSelectedT() == null || !EnumTipoFormula.COMPONENTE_PLAN_ESTUDIO_PARAM.equals(comboFormulasComponentePlanGrado.getSelectedT().getFomTipoFormula())) {
            parametroFormulaPP.setSelected(-1);
        }
    }

    public void seleccionarFormulaPP() {
        entidadEnEdicion.setCpgFormulaHabilitacionPP(comboFormulasPP.getSelectedT());
        if (comboFormulasPP.getSelectedT() == null || !EnumTipoFormula.PRUEBA_EXTRAORDINARIA_PARAM.equals(comboFormulasPP.getSelectedT().getFomTipoFormula())) {
            parametroFormulaPP.setSelected(-1);
        }
    }

    public void seleccionarFormulaPS() {
        entidadEnEdicion.setCpgFormulaHabilitacionPS(comboFormulasPS.getSelectedT());
        if (comboFormulasPS.getSelectedT() == null || !EnumTipoFormula.PRUEBA_EXTRAORDINARIA_PARAM.equals(comboFormulasPS.getSelectedT().getFomTipoFormula())) {
            parametroFormulaPS.setSelected(-1);
        }
    }

    public void seleccionarFormulaSP() {
        entidadEnEdicion.setCpgFormulaHabilitacionSP(comboFormulasSP.getSelectedT());
        if (comboFormulasSP.getSelectedT() == null || !EnumTipoFormula.PRUEBA_EXTRAORDINARIA_PARAM.equals(comboFormulasSP.getSelectedT().getFomTipoFormula())) {
            parametroFormulaSP.setSelected(-1);
        }
    }

    public void seleccionarFormulaSS() {
        entidadEnEdicion.setCpgFormulaHabilitacionSS(comboFormulasSS.getSelectedT());
        if (comboFormulasSS.getSelectedT() == null || !EnumTipoFormula.PRUEBA_EXTRAORDINARIA_PARAM.equals(comboFormulasSS.getSelectedT().getFomTipoFormula())) {
            parametroFormulaSS.setSelected(-1);
        }
    }

    public SgComponentePlanGrado getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgComponentePlanGrado entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<RevHistorico> getHistorialComponentePlanGrado() {
        return historialComponentePlanGrado;
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

    public LazyComponentePlanGradoDataModel getComponentePlanGradoLazyModel() {
        return componentePlanGradoLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyComponentePlanGradoDataModel componentePlanGradoLazyModel) {
        this.componentePlanGradoLazyModel = componentePlanGradoLazyModel;
    }

    public SgPlanEstudio getPlanEstEnEdicion() {
        return planEstEnEdicion;
    }

    public void setPlanEstEnEdicion(SgPlanEstudio planEstEnEdicion) {
        this.planEstEnEdicion = planEstEnEdicion;
    }

    public List<SgGrado> getListaGrados() {
        return listaGrados;
    }

    public void setListaGrados(List<SgGrado> listaGrados) {
        this.listaGrados = listaGrados;
    }

    public SgGrado getCpgGrado() {
        return cpgGrado;
    }

    public void setCpgGrado(SgGrado cpgGrado) {
        this.cpgGrado = cpgGrado;
    }

    public SofisComboG<SgEscalaCalificacion> getComboEscalaCalificacion() {
        return comboEscalaCalificacion;
    }

    public void setComboEscalaCalificacion(SofisComboG<SgEscalaCalificacion> comboEscalaCalificacion) {
        this.comboEscalaCalificacion = comboEscalaCalificacion;
    }

    public SgComponentePlanEstudio getComponentesPlanEstudioSeleccionado() {
        return componentesPlanEstudioSeleccionado;
    }

    public void setComponentesPlanEstudioSeleccionado(SgComponentePlanEstudio componentesPlanEstudioSeleccionado) {
        this.componentesPlanEstudioSeleccionado = componentesPlanEstudioSeleccionado;
    }

    public SofisComboG<SgProgramaEducativo> getComboProgEducativo() {
        return comboProgEducativo;
    }

    public void setComboProgEducativo(SofisComboG<SgProgramaEducativo> comboProgEducativo) {
        this.comboProgEducativo = comboProgEducativo;
    }

    public SgModalidad getModalidadEnEdicion() {
        return modalidadEnEdicion;
    }

    public void setModalidadEnEdicion(SgModalidad modalidadEnEdicion) {
        this.modalidadEnEdicion = modalidadEnEdicion;
    }

    public SofisComboG<TipoComponentePlanEstudio> getComboTiposCompPlanEst() {
        return comboTiposCompPlanEst;
    }

    public void setComboTiposCompPlanEst(SofisComboG<TipoComponentePlanEstudio> comboTiposCompPlanEst) {
        this.comboTiposCompPlanEst = comboTiposCompPlanEst;
    }

    public SofisComboG<EnumFuncionRedondeo> getComboFuncionRedondeo() {
        return comboFuncionRedondeo;
    }

    public void setComboFuncionRedondeo(SofisComboG<EnumFuncionRedondeo> comboFuncionRedondeo) {
        this.comboFuncionRedondeo = comboFuncionRedondeo;
    }

    public SofisComboG<EnumCalculoNotaInstitucional> getComboCalculoNotaInstitucional() {
        return comboCalculoNotaInstitucional;
    }

    public void setComboCalculoNotaInstitucional(SofisComboG<EnumCalculoNotaInstitucional> comboCalculoNotaInstitucional) {
        this.comboCalculoNotaInstitucional = comboCalculoNotaInstitucional;
    }

    public SofisComboG<SgFormula> getComboFormulasComponentePlanGrado() {
        return comboFormulasComponentePlanGrado;
    }

    public void setComboFormulasComponentePlanGrado(SofisComboG<SgFormula> comboFormulasComponentePlanGrado) {
        this.comboFormulasComponentePlanGrado = comboFormulasComponentePlanGrado;
    }

    public SofisComboG<SgFormula> getComboFormulasGrado() {
        return comboFormulasGrado;
    }

    public void setComboFormulasGrado(SofisComboG<SgFormula> comboFormulasGrado) {
        this.comboFormulasGrado = comboFormulasGrado;
    }

    public SgRelGradoPlanEstudio getRelGradoPlanEst() {
        return relGradoPlanEst;
    }

    public void setRelGradoPlanEst(SgRelGradoPlanEstudio relGradoPlanEst) {
        this.relGradoPlanEst = relGradoPlanEst;
    }

    public Boolean getEsEscalaNumerica() {
        return esEscalaNumerica;
    }

    public void setEsEscalaNumerica(Boolean esEscalaNumerica) {
        this.esEscalaNumerica = esEscalaNumerica;
    }

    public SofisComboG<SgFormula> getComboFormulasPP() {
        return comboFormulasPP;
    }

    public void setComboFormulasPP(SofisComboG<SgFormula> comboFormulasPP) {
        this.comboFormulasPP = comboFormulasPP;
    }

    public SofisComboG<SgFormula> getComboFormulasPS() {
        return comboFormulasPS;
    }

    public void setComboFormulasPS(SofisComboG<SgFormula> comboFormulasPS) {
        this.comboFormulasPS = comboFormulasPS;
    }

    public SofisComboG<SgFormula> getComboFormulasSP() {
        return comboFormulasSP;
    }

    public void setComboFormulasSP(SofisComboG<SgFormula> comboFormulasSP) {
        this.comboFormulasSP = comboFormulasSP;
    }

    public SofisComboG<SgFormula> getComboFormulasSS() {
        return comboFormulasSS;
    }

    public void setComboFormulasSS(SofisComboG<SgFormula> comboFormulasSS) {
        this.comboFormulasSS = comboFormulasSS;
    }

    public SofisComboG<SgComponentePlanGrado> getParametroFormulaPP() {
        return parametroFormulaPP;
    }

    public void setParametroFormulaPP(SofisComboG<SgComponentePlanGrado> parametroFormulaPP) {
        this.parametroFormulaPP = parametroFormulaPP;
    }

    public SofisComboG<SgComponentePlanGrado> getParametroFormulaPS() {
        return parametroFormulaPS;
    }

    public void setParametroFormulaPS(SofisComboG<SgComponentePlanGrado> parametroFormulaPS) {
        this.parametroFormulaPS = parametroFormulaPS;
    }

    public SofisComboG<SgComponentePlanGrado> getParametroFormulaSP() {
        return parametroFormulaSP;
    }

    public void setParametroFormulaSP(SofisComboG<SgComponentePlanGrado> parametroFormulaSP) {
        this.parametroFormulaSP = parametroFormulaSP;
    }

    public SofisComboG<SgComponentePlanGrado> getParametroFormulaSS() {
        return parametroFormulaSS;
    }

    public void setParametroFormulaSS(SofisComboG<SgComponentePlanGrado> parametroFormulaSS) {
        this.parametroFormulaSS = parametroFormulaSS;
    }

    public SofisComboG<SgComponentePlanGrado> getParametroAprobacion() {
        return parametroAprobacion;
    }

    public void setParametroAprobacion(SofisComboG<SgComponentePlanGrado> parametroAprobacion) {
        this.parametroAprobacion = parametroAprobacion;
    }

    public Boolean getComponenteSePuedeEliminar() {
        return componenteSePuedeEliminar;
    }

    public void setComponenteSePuedeEliminar(Boolean componenteSePuedeEliminar) {
        this.componenteSePuedeEliminar = componenteSePuedeEliminar;
    }

}
