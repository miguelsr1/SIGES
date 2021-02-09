/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.business.GestionPresupuestoEscolarBean;
import gob.mined.siap2.business.RelacionGesPresEsAnioFiscalBean;
import gob.mined.siap2.business.RelacionPresAnioFinanciamientoBean;
import gob.mined.siap2.business.ejbs.UsuarioBean;
import gob.mined.siap2.business.ejbs.impl.AreasInversionBean;
import gob.mined.siap2.business.ejbs.impl.CategoriaPresupuestoEscolarBean;
import gob.mined.siap2.business.ejbs.impl.CuentasBean;
import gob.mined.siap2.business.ejbs.impl.GeneralBean;
import gob.mined.siap2.business.ejbs.impl.RangoMatriculaBean;
import gob.mined.siap2.business.ejbs.impl.SgBeneficiariosBean;
import gob.mined.siap2.business.ejbs.impl.SgCatalogosBean;
import gob.mined.siap2.business.ejbs.impl.SgOrganizacionesCurricularBean;
import gob.mined.siap2.business.ejbs.impl.TipoCreditoBean;
import gob.mined.siap2.business.ejbs.impl.TipoCuentaBancariaBean;
import gob.mined.siap2.business.ejbs.impl.TipoTransferenciaBean;
import gob.mined.siap2.business.ejbs.impl.TopePresupuestalBean;
import gob.mined.siap2.business.ejbs.impl.TransferenciasComponenteBean;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.SsUsuario;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.AreasInversion;
import gob.mined.siap2.entities.data.impl.CategoriaPresupuestoEscolar;
import gob.mined.siap2.entities.data.impl.ComponentePresupuestoEscolar;
import gob.mined.siap2.entities.data.impl.Cuentas;
import gob.mined.siap2.entities.data.impl.FuenteFinanciamiento;
import gob.mined.siap2.entities.data.impl.FuenteRecursos;
import gob.mined.siap2.entities.data.impl.RelacionGesPresEsAnioFiscal;
import gob.mined.siap2.entities.data.impl.RelacionPresAnioFinanciamiento;
import gob.mined.siap2.entities.data.impl.Beneficiarios;
import gob.mined.siap2.entities.data.impl.ObjetoEspecificoGasto;
import gob.mined.siap2.entities.data.impl.RangoMatricula;
import gob.mined.siap2.entities.data.impl.ComponentePresupuestoEscolarLite;
import gob.mined.siges.entities.data.impl.SgCiclo;
import gob.mined.siges.entities.data.impl.SgClasificacion;
import gob.mined.siges.entities.data.impl.SgModalidad;
import gob.mined.siges.entities.data.impl.SgModalidadAtencion;
import gob.mined.siges.entities.data.impl.SgNivel;
import gob.mined.siges.entities.data.impl.SgOrganizacionCurricular;
import gob.mined.siges.entities.data.impl.SgSubModalidad;
import gob.mined.siap2.entities.data.impl.TipoCredito;
import gob.mined.siap2.entities.data.impl.TipoTransferencia;
import gob.mined.siap2.entities.data.impl.TopePresupuestal;
import gob.mined.siap2.entities.data.impl.TransferenciasComponente;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.entities.enums.TipoEjecucion;
import gob.mined.siap2.entities.enums.TipoMedida;
import gob.mined.siap2.entities.enums.TipoParametro;
import gob.mined.siap2.entities.enums.TipoPresupuestoAnio;
import gob.mined.siap2.entities.enums.TipoSubcomponente;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.filtros.FiltroComponentePresupuestoEscolar;
import gob.mined.siap2.filtros.FiltroRelPresAnioFinanciamiento;
import gob.mined.siap2.filtros.FiltroRelPresAnioFiscal;
import gob.mined.siap2.filtros.FiltroUnidadTecnica;
import gob.mined.siap2.filtros.FiltroUsuario;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.UnidadTecnicaDelegate;
import gob.mined.siap2.web.delegates.UsuarioDelegate;
import gob.mined.siap2.web.genericos.constantes.ConstantesPresentacion;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.UsuarioInfo;
import gob.mined.siap2.web.utils.SofisComboG;
import gob.mined.siap2.web.utils.dataProvider.EntityReferenceDataProvider;
import gob.mined.siap2.web.utils.dataProvider.GeneralLazyDataModel;
import gob.mined.siap2.web.utils.dataProvider.IDataProvider;
import gob.mined.siges.entities.data.impl.SgTipoCuentaBancaria;
import gob.mined.siges.entities.data.impl.SgTipoOrganismoAdministrativo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.TreeNode;

/**
 * Esta clase incluye los métodos para gestionar los presupuestos escolares.
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "gestionPresupuestoEscolar")
public class GestionPresupuestoEscolar implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    private ComponentePresupuestoEscolar gesPresEs;
    private ComponentePresupuestoEscolar filtroComponente;
    private ComponentePresupuestoEscolar subcomponenteEliminar;
    private RelacionGesPresEsAnioFiscal relacionGesPresEsAnioFiscal;
    private ObjetoEspecificoGasto oeg;

    private List<SgTipoOrganismoAdministrativo> tiposOrganismos;
    private List<AnioFiscal> listaAnioFiscal;
    private List<ComponentePresupuestoEscolarLite> historico;
    private List<RelacionGesPresEsAnioFiscal> listaGesPresEsAnioFiscal;
    private List<SgOrganizacionCurricular> listaSgOrganizacionesCurriculars;
    private List<AreasInversion> listaAreasInversion;
    private List<AreasInversion> listaSubAreasInversion;
    private List<Beneficiarios> listaBeneficiarios;
    private List<Beneficiarios> listaEliminarBeneficiarios;
    private List<RangoMatricula> listaRangoMatricula;
    private List<RangoMatricula> listaRangoMatriculaEliminar;

    private TreeNode arbolOrganizaciones;

    private Integer ctdadRangos;
    private Boolean correspondeProyecto;
    private String filtroHabilitado;
    private String filtroMontoMinimo;
    private String filtroConvenio;
    private String filtroProyecto;
    private String filtroComponenteConvenio;
    private String filtroSubcomponenteConvenio;
    private String filtroCategoriaGastoConvenio;
    
    private TreeNode[] nodosSeleccionados;

    private boolean flagEliminar = false;
    private String mensaje = "";
    private boolean flagEdicion = false;
    private boolean flagNombreComplemento = false;
    private Boolean editarSubComponente = false;
    
    
    protected LazyDataModel lazyModel;

    private Boolean busquedaAvanzada = Boolean.FALSE;

    //Datos generales
    private SofisComboG<AreasInversion> comboAreas;
    private SofisComboG<AreasInversion> comboSubareas;
    private SofisComboG<TipoTransferencia> comboTiposTransferencia;
    private SofisComboG<TipoSubcomponente> comboTiposSubcomponente;
    private SofisComboG<TipoMedida> comboTiposMedida;
    private SofisComboG<TipoParametro> comboTiposParametros;
    private SofisComboG<TipoCredito> comboTiposCredito;
    private SofisComboG<FuenteRecursos> comboFuentesRecurso;
    private SofisComboG<FuenteFinanciamiento> comboFuenteFinanciamiento;
    private SofisComboG<TipoEjecucion> comboTipoEjecucion;
    //private SofisComboG<UnidadTecnica> comboUnidadesTecnicas;
    private SofisComboG<AnioFiscal> comboAnioFiscal;
    private SofisComboG<AnioFiscal> comboAnioFiscalFiltro;
    private SofisComboG<SgTipoCuentaBancaria> comboTipoCuentaBancaria;
    private SofisComboG<SsUsuario> comboUsuarios;

    private CategoriaPresupuestoEscolar componenteSelected;
    private UnidadTecnica unidadTecnicaSelected;
    //Presupuesto
    private SofisComboG<Cuentas> comboCuentas;
    private SofisComboG<Cuentas> comboSubcuentas;
    private SofisComboG<ComponentePresupuestoEscolar> comboComponentesDeduccion;
    private SofisComboG<TipoPresupuestoAnio> comboTipoPresupuestoAnio;
    private List<SgClasificacion> clasificaciones;
    private List<RelacionPresAnioFinanciamiento> relFinanciamientos;
    private RelacionPresAnioFinanciamiento relFinanciamientoEdicion;
    private SofisComboG<FuenteFinanciamiento> comboFuenteFinanciamientoRelAnio;
    private SofisComboG<FuenteRecursos> comboFuenteRecursosRelAnio;

    private Integer idUnidadT;

    @Inject
    private JSFUtils jSFUtils;
    @Inject
    private EntityManagementDelegate emd;
    @Inject
    private GestionPresupuestoEscolarBean gesPresEsBean;
    @Inject
    private TipoCreditoBean tipoCreditoBean;
    @Inject
    private UsuarioBean usuarioBean;
    @Inject
    private UsuarioDelegate usuarioDelegate;
    
    @Inject
    private GeneralBean generalBean;
    @Inject
    private RelacionGesPresEsAnioFiscalBean gesPresEsAnioFiscalBean;
    @Inject
    private CategoriaPresupuestoEscolarBean cpeBean;
    @Inject
    private AreasInversionBean areasInversionBean;
    @Inject
    private CuentasBean cuentasBean;
    @Inject
    private SgBeneficiariosBean beneficiariosBean;
    @Inject
    private SgOrganizacionesCurricularBean curricularBean;
    @Inject
    private RelacionPresAnioFinanciamientoBean relPresAnioFinanciamientoBean;
    @Inject
    private TipoCuentaBancariaBean tipoCuentBancariaBean;
    @Inject
    private SgCatalogosBean catalogosSigesBean;
    @Inject
    private RangoMatriculaBean rangoMatriculaBean;
    @Inject
    private TopePresupuestalBean topePresupuestalBean;
    @Inject
    private TransferenciasComponenteBean transferenciasComponenteBean;
    @Inject
    private TipoTransferenciaBean tipoTransferenciaBean;
    @Inject
    private UnidadTecnicaDelegate unidadTecnicaDelegate;
    
    @Inject
    private UsuarioInfo usuarioInfo;

    private SsUsuario usuarioLogueado;
    
    private Integer tabIndex;
    @PostConstruct
    public void init() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        String tab = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("tab");
        usuarioLogueado = usuarioInfo.getUsuario();
        comboTiposSubcomponente = new SofisComboG(Arrays.asList(TipoSubcomponente.values()), "label");
        comboTiposSubcomponente.addEmptyItem(ConstantesPresentacion.FILTRO_VACIO);

        comboTiposMedida = new SofisComboG(Arrays.asList(TipoMedida.values()), "label");
        comboTiposMedida.addEmptyItem(ConstantesPresentacion.FILTRO_VACIO);

        comboTiposParametros = new SofisComboG(Arrays.asList(TipoParametro.values()), "label");
        comboTiposParametros.addEmptyItem(ConstantesPresentacion.FILTRO_VACIO);

        cargarAniosFiscales();
        cargarTiposTransferencia();
        cargarCombosTabGeneral();
        cargarAreasInversion();
        cargarTipoCuentaBancaria();
        cargarTiposOrganismosPresupuestales();
        cleanObject();
        if (!TextUtils.isEmpty(id)) {
            asignarEdicion(Integer.parseInt(id));
        } else {
            editarSubComponente = Boolean.TRUE;
        }
        cargarListaRelacionModalidades();
        if (!TextUtils.isEmpty(tab)) {
            tabIndex = Integer.parseInt(tab);
            if(tabIndex.compareTo(1) == 0) {
                if (comboCuentas == null) {
                    cargarCombosPresupuesto();
                }
            }
        } else {
            tabIndex = 0;
        }
       /** if (!TextUtils.isEmpty(gen)) {
            Integer genAfter = Integer.parseInt(gen);
            if(genAfter.compareTo(1) == 0) {
                jSFUtils.agregarInfo("Ha iniciado la generación de los techos desde archivo. Cuando el proceso finalice se enviará una notitifcación a su bandeja de tareas.");
            }
        }**/
        
    }

    
    
    //******** INICIALIZACION DE OBJETOS ********//
    
    /**
     * Este método inicializa un objeto de coponente de gestion y también sus
     * relaciones
     *
     * @param local
     * @return
     */
    public ComponentePresupuestoEscolar inicializarObjetos(ComponentePresupuestoEscolar local) {
        if (local == null) {
            local = new ComponentePresupuestoEscolar();
        }
        return local;
    }

    /**
     * Este método inicializa un objeto de Relacion entre Anpño fiscal y Gestion
     * de componente escolar
     *
     * @param relacion
     * @return
     */
    public RelacionGesPresEsAnioFiscal inicializarObjetoRelacion(RelacionGesPresEsAnioFiscal relacion) {
        if (relacion == null) {
            relacion = new RelacionGesPresEsAnioFiscal();
        }
        inicializarRelaciones(relacion);
        return relacion;
    }

    /**
     * Este método inicializa las relaciones de los miembros de un objeto
     *
     * @param relacion
     */
    public void inicializarRelaciones(RelacionGesPresEsAnioFiscal relacion) {
        if (relacion.getAnioFiscal() == null) {
            relacion.setAnioFiscal(new AnioFiscal());
        }
        if (relacion.getComponentePresupuestoEscolar() == null) {
            relacion.setComponentePresupuestoEscolar(new ComponentePresupuestoEscolar());
        }
    }

    /**
     * Este método inicializa los objetos base del mantenimiento
     */
    public void cleanObject() {
        this.gesPresEs = inicializarObjetos(null);
        this.filtroComponente = inicializarObjetos(null);
        this.listaBeneficiarios = new ArrayList<Beneficiarios>();
        this.listaEliminarBeneficiarios = new ArrayList<Beneficiarios>();
        this.componenteSelected = null;
        this.comboTiposSubcomponente.clearSelectedT();
        this.comboTiposParametros.clearSelectedT();
        this.comboAnioFiscalFiltro.clearSelectedT();
        this.filtroHabilitado = null;
        filterTable();
    }

    /**
     * Este método inicializa los objetos de Relacion de AnioFiscal y Gestion de
     * presupuesto escolar
     */
    public void cleanObjectRelacionAnioFiscalGesPresEs() {
        this.relacionGesPresEsAnioFiscal = inicializarObjetoRelacion(null);
        this.flagNombreComplemento = false;
        this.oeg = null;
        if (this.comboCuentas != null) {
            this.comboCuentas.setSelected(-1);
        }
        if (this.comboComponentesDeduccion != null) {
            this.comboComponentesDeduccion.setSelected(-1);
        }
        if (this.comboTipoPresupuestoAnio != null) {
            this.comboTipoPresupuestoAnio.setSelected(-1);
        }
        if (this.comboAnioFiscal != null) {
            this.comboAnioFiscal.setSelected(-1);
        }
        listaRangoMatricula = new LinkedList();
        comboSubcuentas = new SofisComboG();
        comboSubcuentas.addEmptyItem(ConstantesPresentacion.FILTRO_VACIO);
    }

    /**
     * Este método agrega un registro nuevo a la lista de Areas de Inversion del Subcomponente
     */
    public void agregarSubareainversion() {
        if (this.comboSubareas.getSelectedT() != null) {
            if (!gesPresEs.getSubareasInversion().contains(this.comboSubareas.getSelectedT())) {
                gesPresEs.getSubareasInversion().add(this.comboSubareas.getSelectedT());
            }
            RequestContext.getCurrentInstance().execute("$('#addLineaEstrategica').modal('hide');");
        }
    }

    /**
     * Este método elimina un registro de la lista de Areas de Inversion del Subcomponente
     * @param area 
     */
    public void eliminarSubareainversion(AreasInversion area) {
        try {
            gesPresEs.getSubareasInversion().remove(area);
            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    

    public void cargarCombosTabGeneral() {
        componenteSelected = null;
        comboTiposCredito = new SofisComboG(tipoCreditoBean.getTipoCreditoHabilitados(), "nombre");
        comboTiposCredito.addEmptyItem(ConstantesPresentacion.FILTRO_VACIO);

        List<FuenteFinanciamiento> ff = generalBean.getClasesGeneralCodiguera(FuenteFinanciamiento.class);

        comboFuenteFinanciamiento = new SofisComboG(ff, "nombre");
        comboFuenteFinanciamiento.addEmptyItem(ConstantesPresentacion.FILTRO_VACIO);

        comboFuenteFinanciamientoRelAnio = new SofisComboG(ff, "nombre");
        comboFuenteFinanciamientoRelAnio.addEmptyItem(ConstantesPresentacion.FILTRO_VACIO);

        comboFuenteRecursosRelAnio = new SofisComboG();
        comboFuenteRecursosRelAnio.addEmptyItem(ConstantesPresentacion.FILTRO_VACIO);

        comboFuentesRecurso = new SofisComboG();
        comboFuentesRecurso.addEmptyItem(ConstantesPresentacion.FILTRO_VACIO);

        comboTipoEjecucion = new SofisComboG(Arrays.asList(TipoEjecucion.values()), "label");
        comboTipoEjecucion.addEmptyItem(ConstantesPresentacion.FILTRO_VACIO);

        unidadTecnicaSelected = null;
        //comboUnidadesTecnicas = new SofisComboG(emd.getEntities(UnidadTecnica.class.getName(), "nombre"), "nombre");
        //comboUnidadesTecnicas.addEmptyItem(ConstantesPresentacion.FILTRO_VACIO);
        
        comboUsuarios = new SofisComboG();
        comboUsuarios.addEmptyItem(ConstantesPresentacion.FILTRO_VACIO);

    }

    public void fuenteFinanciamientoSelected() {
        if (comboFuenteFinanciamiento.getSelectedT() != null) {
            comboFuentesRecurso = new SofisComboG(generalBean.getFuentesRecursos(comboFuenteFinanciamiento.getSelectedT().getId()), "nombre");
            comboFuentesRecurso.addEmptyItem(ConstantesPresentacion.FILTRO_VACIO);
        } else {
            comboFuentesRecurso = new SofisComboG();
            comboFuentesRecurso.addEmptyItem(ConstantesPresentacion.FILTRO_VACIO);
        }
    }

    public void fuenteFinanciamientoRelAnioSelected() {
        if (comboFuenteFinanciamientoRelAnio.getSelectedT() != null) {
            comboFuenteRecursosRelAnio = new SofisComboG(generalBean.getFuentesRecursos(comboFuenteFinanciamientoRelAnio.getSelectedT().getId()), "nombre");
            comboFuenteRecursosRelAnio.addEmptyItem(ConstantesPresentacion.FILTRO_VACIO);
        } else {
            comboFuenteRecursosRelAnio = new SofisComboG();
            comboFuenteRecursosRelAnio.addEmptyItem(ConstantesPresentacion.FILTRO_VACIO);
        }
    }

    /**
     * Este método verifica la existencia de un registro de
     * ObjetoEspecificoGasto segun el codigo ingresado y asigna a contexto
     */
    public void cargarOEG() {
        try {
            this.oeg = null;
            if (this.relacionGesPresEsAnioFiscal.getObjetoEspecificoGasto() != null 
                    && !this.relacionGesPresEsAnioFiscal.getObjetoEspecificoGasto().isEmpty() 
                    && this.relacionGesPresEsAnioFiscal.getObjetoEspecificoGasto().length() == 7) {
                
                try {
                    this.oeg = gesPresEsBean.getOEGByCodigo(Integer.parseInt(this.relacionGesPresEsAnioFiscal.getObjetoEspecificoGasto()));
                } catch (NumberFormatException | ClassCastException nfe) {
                    logger.log(Level.SEVERE, "No se pudo castear el numero de OEG", nfe);
                }
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    public List<CategoriaPresupuestoEscolar> completeComponentes(String query){
        try {
                FiltroComponentePresupuestoEscolar filtro = new FiltroComponentePresupuestoEscolar();
                filtro.setCodigoNombre(query);
                filtro.setHabilitado(Boolean.TRUE);
                filtro.setAscending(new boolean[]{true});
                filtro.setOrderBy(new String[]{"nombre"});
                filtro.setMaxResults(10L);
                filtro.setIncluirCampos(new String[]{"nombre","codigo","version"});
                return  cpeBean.obtenerComponentesPorFiltro(filtro);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;
    }
    
    public List<UnidadTecnica> completeUnidadesTecnicas(String query){
        try {
            if(query != null && !query.isEmpty()) {
                FiltroUnidadTecnica filtro = new FiltroUnidadTecnica();
                filtro.setNombre(query);
                filtro.setAscending(new boolean[]{true});
                filtro.setOrderBy(new String[]{"nombre"});
                filtro.setMaxResults(10L);
                filtro.setIncluirCampos(new String[]{"nombre","version"});
                return this.unidadTecnicaDelegate.obtenerUnidadesPorFiltro(filtro);
            }
            
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;
    }
    
    //******** CARGA DE LISTA DE PRESUPUESTO ********
    
    /**
     * Este método carga un listado de Relaciones de anios fiscales y
     * componentes de gestion escolar
     *
     * @param id
     */
    public void cargarRelacionesAnioFiscalGesPresEs(Integer id) {

        FiltroRelPresAnioFiscal filtro = new FiltroRelPresAnioFiscal();
        filtro.setComponentePresupuestoEscolarId(id);
        filtro.setIncluirCampos(new String[]{
            "id","anioFiscal.anio", "subCuenta.cuentaPadre.nombre","subCuenta.cuentaPadre.codigo",
            "subCuenta.codigo", "subCuenta.nombre","procesoEnCurso",
            "objetoEspecificoGasto", "tipo", "montoTotalFormulado", "montoTotalAprobado","descripcion",
            "componentePresupuestoEscolar.categoriaPresupuestoEscolar.nombre","cantidadMatriculas","cantidadMatriculasAprobadas"
        });

        this.listaGesPresEsAnioFiscal = gesPresEsAnioFiscalBean.getComponentesPresupuestoEscolarByFiltro(filtro);
    }
    
    /**
     * Este método carga un listado de Relaciones de anios fiscales y
     * componentes de gestion escolar filtrados por anioFiscal
     */
    public void cargarRelacionesByFiltro() {
        FiltroRelPresAnioFiscal filtro = new FiltroRelPresAnioFiscal();
        filtro.setComponentePresupuestoEscolarId(getGesPresEs().getId());
        if(getComboAnioFiscalFiltro().getSelectedT() != null){
            filtro.setAnioFiscalId(getComboAnioFiscalFiltro().getSelectedT().getId());
        }
        logger.log(Level.SEVERE, "[FILTRO] ---");
        filtro.setIncluirCampos(new String[]{
            "anioFiscal.anio", "subCuenta.cuentaPadre.nombre","subCuenta.cuentaPadre.codigo",
            "subCuenta.codigo", "subCuenta.nombre","procesoEnCurso",
            "objetoEspecificoGasto", "tipo", "montoTotalFormulado", "montoTotalAprobado",
            "componentePresupuestoEscolar.categoriaPresupuestoEscolar.nombre","cantidadMatriculas","descripcion"
        });

        this.listaGesPresEsAnioFiscal = gesPresEsAnioFiscalBean.getComponentesPresupuestoEscolarByFiltro(filtro);
    }
    
    /**
     * Metodo utilizado para limpiar la seleccion del filtro de registros presupuesto por anioFiscal
     */
    public void limpiarComboAnioFiscal(){
        if(this.comboAnioFiscalFiltro != null){
            this.comboAnioFiscalFiltro.clearSelectedT();
            cargarRelacionesByFiltro();
        }
    }
    
    
 
    
    
    //******** CARGA DE COMBOS ********//
    
    /**
     * Este método carga un listado de relacion
     */
    public void cargarListaRelacionModalidades() {
        try {
            this.listaSgOrganizacionesCurriculars = curricularBean.getSgOrganizacionesCurriculars();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    /**
     * Este método carga una lista de beneficiarios que pertenezcan a un mismo
     * componente
     */
    public void cargarListaBeneficiarios() {
        try {
            if (this.relacionGesPresEsAnioFiscal != null && this.relacionGesPresEsAnioFiscal.getId() != null) {
                this.listaBeneficiarios = beneficiariosBean.getBeneficiariosPorPresEsAnioFiscal(this.relacionGesPresEsAnioFiscal.getId());
            }
            this.listaEliminarBeneficiarios = new ArrayList<Beneficiarios>();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    /**
     * Este método carga una lista de Tipos de cuenta bancaria componente
     */
    public void cargarTipoCuentaBancaria() {
        try {
            comboTipoCuentaBancaria = new SofisComboG(tipoCuentBancariaBean.getTipoCuentaBancariaHabilitado(), "nombre");
            comboTipoCuentaBancaria.addEmptyItem(ConstantesPresentacion.FILTRO_VACIO);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    /**
     * Este método carga el listado de Fuentes de financiamiento de un presupuesto por año fiscal
     */
    public void cargarListaFuentesFinanciamientoAnio() {
        if (this.relacionGesPresEsAnioFiscal != null && this.relacionGesPresEsAnioFiscal.getId() != null) {
            FiltroRelPresAnioFinanciamiento fr = new FiltroRelPresAnioFinanciamiento();
            fr.setRelAnioPresupuesto(this.relacionGesPresEsAnioFiscal.getId());
            fr.setIncluirCampos(new String[]{"id", "cifradoPresupuestario", "montoTotalFormulado", "fuenteRecursos.nombre", "montoTotalAprobado", "fuenteFinanciamiento.nombre"});
            this.relFinanciamientos = relPresAnioFinanciamientoBean.getComponentesPresupuestoEscolarByFiltro(fr);
        }
    }

    /**
     * Este método carga los combos a utilizar para la pestaña de Presupuesto
     */
    public void cargarCombosPresupuesto() {

        this.comboCuentas = new SofisComboG();
        this.comboCuentas.addEmptyItem(ConstantesPresentacion.FILTRO_VACIO);

        this.comboSubcuentas = new SofisComboG();
        this.comboSubcuentas.addEmptyItem(ConstantesPresentacion.FILTRO_VACIO);

        this.comboTipoPresupuestoAnio = new SofisComboG(Arrays.asList(TipoPresupuestoAnio.values()), "label");
        this.comboTipoPresupuestoAnio.addEmptyItem(ConstantesPresentacion.FILTRO_VACIO);

        clasificaciones = catalogosSigesBean.getClasificacionesHabilitadas();

        FiltroComponentePresupuestoEscolar filtro = new FiltroComponentePresupuestoEscolar();
        filtro.setCategoriaComponenteId(this.gesPresEs.getCategoriaPresupuestoEscolar().getId());
        filtro.setIncluirCampos(new String[]{"nombre", "version"});
        this.comboComponentesDeduccion = new SofisComboG(gesPresEsBean.getComponentesPresupuestoEscolarByFiltro(filtro), "nombre");
        this.comboComponentesDeduccion.addEmptyItem(ConstantesPresentacion.FILTRO_VACIO);
    }

    /**
     * Este método carga un listado de AniosFiscales
     */
    public void cargarAniosFiscales() {
        try {
            List<AnioFiscal> lista = generalBean.getAniosFiscalesPlanificacion();
            this.comboAnioFiscal = new SofisComboG(lista, "anio");
            this.comboAnioFiscal.addEmptyItem(ConstantesPresentacion.FILTRO_VACIO);
            
            this.comboAnioFiscalFiltro = new SofisComboG(lista, "anio");
            this.comboAnioFiscalFiltro.addEmptyItem(ConstantesPresentacion.FILTRO_VACIO);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    /**
     * Este método carga un listado de AniosFiscales
     */
    public void cargarTiposTransferencia() {
        try {
            List<TipoTransferencia> lista = tipoTransferenciaBean.getTiposTransferenciasHabilitados();

            comboTiposTransferencia = new SofisComboG(lista, "nombre");
            comboTiposTransferencia.addEmptyItem(ConstantesPresentacion.FILTRO_VACIO);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    
    public void cargarTiposOrganismosPresupuestales(){
        try {
            tiposOrganismos = generalBean.getTiposOrganismosAdministrativos();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    /**
     * Este método carga una lista de Cuentas según el anioFiscal seleccionado
     */
    public void cargarCuentas() {
        try {
            if (this.comboAnioFiscal != null && this.comboAnioFiscal.getSelectedT() != null) {
                List<Cuentas> cuentas = cuentasBean.getCuentasByAnioFiscal(this.comboAnioFiscal.getSelectedT().getId());
                comboCuentas = new SofisComboG(cuentas, "nombre");
                comboCuentas.addEmptyItem(ConstantesPresentacion.FILTRO_VACIO);
            } else {
                comboCuentas = new SofisComboG();
                comboCuentas.addEmptyItem(ConstantesPresentacion.FILTRO_VACIO);
            }
            comboSubcuentas = new SofisComboG();
            comboSubcuentas.addEmptyItem(ConstantesPresentacion.FILTRO_VACIO);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    /**
     * Este método carga una lista de Cuentas según el registro de cuenta padre
     * seleccionado
     *
     */
    public void cargarSubCuentas() {
        try {
            if (this.comboCuentas != null && this.comboCuentas.getSelectedT() != null) {
                List<Cuentas> cuentas = cuentasBean.getCuentasByIdPadre(this.comboCuentas.getSelectedT().getId());
                this.comboSubcuentas = new SofisComboG(cuentas, "nombre");
                this.comboSubcuentas.addEmptyItem(ConstantesPresentacion.FILTRO_VACIO);
            } else {
                this.comboSubcuentas = new SofisComboG();
                this.comboSubcuentas.addEmptyItem(ConstantesPresentacion.FILTRO_VACIO);
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    /**
     * Este método carga un listado de Areas de Inversion habilitados
     */
    public void cargarAreasInversion() {
        try {
            this.listaAreasInversion = areasInversionBean.getAreasInversionHabilitadas();
            comboAreas = new SofisComboG(listaAreasInversion, "nombre");
            comboAreas.addEmptyItem(ConstantesPresentacion.FILTRO_VACIO);

            comboSubareas = new SofisComboG();
            comboSubareas.addEmptyItem(ConstantesPresentacion.FILTRO_VACIO);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    /**
     * Este método carga un listado de Areas de Inversion, filtrado por el
     * codigo de un AreaInversion padre
     */
    public void cargarSubAreasInversion() {
        try {
            if (this.comboAreas.getSelectedT() != null) {
                this.listaSubAreasInversion = areasInversionBean.getAreasInversionByIdPadre(this.comboAreas.getSelectedT().getId());
                comboSubareas = new SofisComboG(listaSubAreasInversion, "nombre");
                comboSubareas.addEmptyItem(ConstantesPresentacion.FILTRO_VACIO);
            } else {
                comboSubareas = new SofisComboG();
                comboSubareas.addEmptyItem(ConstantesPresentacion.FILTRO_VACIO);
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }

    }

    /**
     * Metodo utilizado para buscar un usuario por el identicador de un registro
     * de unidadTecnica
     *
     */
    public void cargarComboUsuarios() {
        try {
            if (this.unidadTecnicaSelected!= null && this.unidadTecnicaSelected.getId() > 0) {
                
                FiltroUsuario filtro = new FiltroUsuario();
                filtro.setIdUnidadTecnica(this.unidadTecnicaSelected.getId());
                usuarioBean.getUsuariosByFiltro(filtro);
                
                List<SsUsuario> lista = usuarioBean.getUsuariosPorIdUnidadTenica(this.unidadTecnicaSelected.getId());
                this.comboUsuarios = new SofisComboG(lista, "usuCod");
                this.comboUsuarios.addEmptyItem(ConstantesPresentacion.FILTRO_VACIO);
            }else{
                this.comboUsuarios = new SofisComboG();
                this.comboUsuarios.addEmptyItem(ConstantesPresentacion.FILTRO_VACIO);
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    
    
    
    
    /**
     * Método utilizado para limpiar la selección de los combos de Fuentes de financiamiento por año presupuesto
     */
    public void agregarRelFinanciamiento() {
        this.relFinanciamientoEdicion = new RelacionPresAnioFinanciamiento();
        this.comboFuenteFinanciamientoRelAnio.setSelected(-1);
        
        this.comboFuenteRecursosRelAnio = new SofisComboG();
        this.comboFuenteRecursosRelAnio.addEmptyItem(ConstantesPresentacion.FILTRO_VACIO);
    }

    /**
     * Metodo utilizado para buscar un registro de componente de gestion
     * escolar, filtrado por ID
     *
     * @param id
     */
    public void findGesPresEsById(Integer id) {
        try {
            this.gesPresEs = gesPresEsBean.getGesPresEsById(id);
            if(gesPresEs.getUnidadTecnica() != null) {
                editarSubComponente = usuarioDelegate.getUsuarioTienePermisoEnUTPadreConOperacion(gesPresEs.getUnidadTecnica().getId(), ConstantesEstandares.Operaciones.EDITAR_SUBCOMPONENTE, usuarioInfo.getUserCod());
            }
            
            
            List<AreasInversion> listadoAreas = this.gesPresEs.getSubareasInversion();
            listadoAreas.sort(new Comparator<AreasInversion>() {
                @Override
                public int compare(AreasInversion a1, AreasInversion a2) {
                    int respuesta = 1;
                    
                    
                    if (a1 != null && a2 != null) {
                        if (a1 instanceof AreasInversion && a2 instanceof AreasInversion) {
                            
                            Integer ordenA1Total = a1.getOrden() + a1.getAreaPadre().getOrden();

                            Integer ordenA2Total = a2.getOrden()+ a2.getAreaPadre().getOrden();


                            if (ordenA1Total != null) {
                                respuesta = ordenA1Total.compareTo(ordenA2Total);
                            }
                        }
                    }
                    return respuesta;
                 }
            });
            
            this.gesPresEs.setSubareasInversion(listadoAreas);
            
            this.comboTiposTransferencia.setSelectedT(gesPresEs.getTipoTransferencia());
            this.comboTipoCuentaBancaria.setSelectedT(gesPresEs.getTipoCuentaBancaria());
            this.comboTiposSubcomponente.setSelectedT(gesPresEs.getTipo());
            this.comboTiposMedida.setSelectedT(gesPresEs.getUnidadMedida());
            this.comboTiposParametros.setSelectedT(gesPresEs.getParametro());

            this.componenteSelected = gesPresEs.getCategoriaPresupuestoEscolar();
            this.comboTipoEjecucion.setSelectedT(gesPresEs.getTipoEjecucion());
            this.unidadTecnicaSelected = gesPresEs.getUnidadTecnica();
            cargarComboUsuarios();
            this.comboUsuarios.setSelectedT(gesPresEs.getUsuario());
            this.comboTiposCredito.setSelectedT(gesPresEs.getTipoCredito());
            cargarRelacionesAnioFiscalGesPresEs(this.gesPresEs.getId());
            verificarCorrespondeProyecto();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    /**
     * Este método realiza la seleccion de registros seleccionados de
     * beneficiarios
     *
     * @param beneficiarios
     */
    public void chequearCheck(Beneficiarios beneficiarios) {
        try {
            if (beneficiarios != null) {
                if (getListaBeneficiarios().isEmpty()) {
                    getListaBeneficiarios().add(beneficiarios);
                } else {
                    if (beneficiarios.getValor()) {
                        getListaBeneficiarios().add(beneficiarios);
                    } else {
                        if (beneficiarios.getId() == null) {
                            getListaBeneficiarios().remove(beneficiarios);
                        } else {
                            removerItemLista(beneficiarios.getId());
                            getListaEliminarBeneficiarios().add(beneficiarios);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    /**
     * Este método elimina un elemento de una lista en base a su ID
     *
     * @param id
     */
    public void removerItemLista(Integer id) {
        try {
            if (getListaBeneficiarios() != null && !getListaBeneficiarios().isEmpty()) {
                ListIterator<Beneficiarios> lit = getListaBeneficiarios().listIterator();
                Beneficiarios ben = null;
                while (lit.hasNext()) {
                    ben = lit.next();
                    if (ben.getId().intValue() == id) {
                        lit.remove();
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    public List<SgClasificacion> completeClasificacion(String query) {
        try {
            List<SgClasificacion> ret = new ArrayList<>();
            for (SgClasificacion c : this.clasificaciones) {
                if (c.getClaNombre().toLowerCase().contains(query.toLowerCase())) {
                    ret.add(c);
                }
            }
            return ret;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;
    }

    
    
    
    //******** RANGOS DE MATRICULA ********//
    
    /**
     * Método utilizado para agregar registros vacios de RangoMatricula a la lista de registros de contexto
     */
    public void agregarRangosMatricula(){
        try {
            if(ctdadRangos != null){
                if(this.listaRangoMatricula == null || this.listaRangoMatricula.isEmpty()){
                   RangoMatricula rango;
                    listaRangoMatricula = new ArrayList<RangoMatricula>();
                    for (int i = 0; i < ctdadRangos; i++) {
                        rango = new RangoMatricula();
                        rango.setRango(BigDecimal.ZERO);
                        rango.setRelacionAnioPresupuesto(this.relacionGesPresEsAnioFiscal);
                        rango.setValor(0);
                        listaRangoMatricula.add(rango);
                    } 
                }else{
                    if(this.listaRangoMatricula.size() < this.ctdadRangos){
                        RangoMatricula rango;
                        for(int i = this.listaRangoMatricula.size(); i < this.ctdadRangos; i++){
                            rango = new RangoMatricula();
                            rango.setRango(BigDecimal.ZERO);
                            rango.setRelacionAnioPresupuesto(this.relacionGesPresEsAnioFiscal);
                            rango.setValor(0);
                            listaRangoMatricula.add(rango);
                        }
                    }
                }
            }else{
                jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_CTDAD_RANGO_MATRICULA_NO_INGRESADO);
            }
            
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    
    /**
     * Método utilizado para cargar todos los registros de RangoMatricula que estén relacionados
     * a una relacion de componente con AnioFiscal
     */
    public void cargarRangosMatricula(){
        try {
            listaRangoMatriculaEliminar = new ArrayList<RangoMatricula>();
            if(this.relacionGesPresEsAnioFiscal.getId() != null && (this.listaRangoMatricula == null || this.listaRangoMatricula.isEmpty())){
                this.listaRangoMatricula = rangoMatriculaBean.getRangoMatriculaByRelacion(this.relacionGesPresEsAnioFiscal.getId());
            }
            RequestContext.getCurrentInstance().execute("$('#addDivRangoMatricula').modal('show');");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    
    /**
     * Método utilizado para guardar/actualizar los registros de la lista de RangoMatricula
     */
    public void guardarRangosMatricula(){
        try {
            if(listaRangoMatricula != null){
                rangoMatriculaBean.crearActualizar(this.listaRangoMatricula, this.listaRangoMatriculaEliminar);
            }else{
                jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_NINGUN_RANGO_MATRICULA_CREADO);
            }
            this.listaRangoMatricula = new ArrayList<RangoMatricula>();
            this.ctdadRangos = 0;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
            throw ex;
        }
    }
    
    /**
     * Método utilizado para eliminar un registro de la lista de RangoMatricula y de la base de datos
     * @param rango 
     */
    public void eliminarRangoMatricula(RangoMatricula rango){
        try {
            ListIterator<RangoMatricula> lista = this.listaRangoMatricula.listIterator();
            RangoMatricula item = null;
            while (lista.hasNext()) {
                item = lista.next();
                if (item.equals(rango)) {
                    if (rango.getId() == null) {
                        lista.remove();
                        break;
                    } else {
                        lista.remove();
                        listaRangoMatriculaEliminar.add(rango);
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    
    /**
     * Método utilizado para cerrar el popup de listado de RangoMatricula
     */
    public void cerrarRangosMatricula(){
        RequestContext.getCurrentInstance().execute("$('#addDivRangoMatricula').modal('hide');");
    }
    
    
    
    
    
    //******** PERSISTENCIA SUBCOMPONENTE ********//
    
    /**
     * Este método busca un registro de componente de gestion escolar por su ID
     * y asigna el resultado a un contexto de edición
     *
     * @param id
     */
    public void asignarEdicion(Integer id) {
        findGesPresEsById(id);
        this.flagEdicion = true;
    }

    /**
     * Metodo utilizado para crear y/o actualizar un componente de gestion
     * escolar
     *
     * @return
     */
    public String guardarActualizar() {
        try {
            gesPresEs.setTipoCuentaBancaria(this.comboTipoCuentaBancaria.getSelectedT());
            gesPresEs.setTipoTransferencia(this.comboTiposTransferencia.getSelectedT());
            gesPresEs.setTipo(this.comboTiposSubcomponente.getSelectedT());
            gesPresEs.setParametro(this.comboTiposParametros.getSelectedT());
            gesPresEs.setUnidadMedida(this.comboTiposMedida.getSelectedT());
            gesPresEs.setCategoriaPresupuestoEscolar(this.componenteSelected);
            gesPresEs.setTipoEjecucion(this.comboTipoEjecucion.getSelectedT());
            gesPresEs.setUnidadTecnica(this.unidadTecnicaSelected);
            gesPresEs.setTipoCredito(this.comboTiposCredito.getSelectedT());
            gesPresEs.setUsuario(this.comboUsuarios.getSelectedT());

            asignarCamposCorrespondeProyecto();
            gesPresEsBean.crearActualizarComponente(this.gesPresEs);
            if (this.gesPresEs.getId() != null) {
                findGesPresEsById(this.gesPresEs.getId());
                jSFUtils.agregarInfo("Elemento guardado correctamente");
                RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
            } else {
                return "gestionPresupuestoEscolar.xhtml?faces-redirect=true";
            }
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } finally {
            this.flagEdicion = false;
            RequestContext.getCurrentInstance().execute("$('#verGestionPresupuesto').modal('hide');");
        }
        return null;
    }

    /**
     * Metodo utilizado para eliminar un registro de componente de gestion
     * escolar
     *
     * @param id ID del registro a eliminar
     */
    public void eliminar() {
        try {
            if(!flagEliminar){
                gesPresEsBean.eliminarComponente(subcomponenteEliminar.getId());
            }else{
                jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_NO_POSIBLE_ELIMINAR);
            }
            cleanObject();
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    
    

    
    
    //******** PERSISTENCIA PRESUPUESTO ********//
    
    //*** PRESUPUESTO ***//
    
    /**
     * Método utilizado para asignar y preparar para la edicion un registro de RelacionSubcomponenteAnioFiscal
     * @param idAnio 
     */
    public void editarRelAnioFiscal(Integer idAnio) {
        try {
            this.listaRangoMatricula = new ArrayList<RangoMatricula>();
            this.relacionGesPresEsAnioFiscal = gesPresEsAnioFiscalBean.getRelacionGesAnioById(idAnio);
            comboAnioFiscal.setSelectedT(relacionGesPresEsAnioFiscal.getAnioFiscal());
            cargarOEG();
            cargarCuentas();
            if (this.relacionGesPresEsAnioFiscal.getSubCuenta() != null) {
                this.comboCuentas.setSelectedT(this.relacionGesPresEsAnioFiscal.getSubCuenta().getCuentaPadre());
                cargarSubCuentas();
                this.comboSubcuentas.setSelectedT(this.relacionGesPresEsAnioFiscal.getSubCuenta());
            }
            if (this.comboComponentesDeduccion != null) {
                this.comboComponentesDeduccion.setSelectedT(this.relacionGesPresEsAnioFiscal.getDeducirComponentePresupuestoEscolar());
            }
            if(this.comboTipoPresupuestoAnio != null) {
                this.comboTipoPresupuestoAnio.setSelectedT(this.relacionGesPresEsAnioFiscal.getTipo());
            }
            
            validarMostrarNombreComplemento();
            cargarListaFuentesFinanciamientoAnio();
            cargarListaBeneficiarios();
            cargarListaRelacionModalidades();
            llenarArbol();
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    
    /**
     * Metodo utilizado para crear y/o actualizar una Relacion de AnioFiscal y
     * un componente de gestion escolar
     */
    public void guardarRelacionAnioFiscalGesPresEs() {
        try {
            this.relacionGesPresEsAnioFiscal.setAnioFiscal(comboAnioFiscal.getSelectedT());
            this.relacionGesPresEsAnioFiscal.setComponentePresupuestoEscolar(getGesPresEs());
            this.relacionGesPresEsAnioFiscal.setSubCuenta(this.comboSubcuentas.getSelectedT());
            this.relacionGesPresEsAnioFiscal.setDeducirComponentePresupuestoEscolar(this.comboComponentesDeduccion.getSelectedT());
            this.relacionGesPresEsAnioFiscal.setTipo(this.comboTipoPresupuestoAnio.getSelectedT());
            
            
            if(!validacionesRelacionComponenteAnioFiscal()){
                RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
                return;
            }
            
            if(this.relacionGesPresEsAnioFiscal.getMontoTotalFormulado() != null && this.relacionGesPresEsAnioFiscal.getMontoTotalFormulado().compareTo(totalMontos("F")) < 0){
                jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_MONTO_TOTAL_FORMULADO_MENOR);
                return;
                
            }else if(relacionGesPresEsAnioFiscal.getMontoTotalAprobado() != null && this.relacionGesPresEsAnioFiscal.getMontoTotalAprobado().compareTo(totalMontos("A")) < 0){
                jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_MONTO_TOTAL_APROBADO_MENOR);
                return;
            }
            
            this.relacionGesPresEsAnioFiscal = gesPresEsAnioFiscalBean.crearActualizar(this.relacionGesPresEsAnioFiscal);
            
            guardarRangosMatricula();
           
            
            cleanObjectRelacionAnioFiscalGesPresEs();
            cargarRelacionesAnioFiscalGesPresEs(this.gesPresEs.getId());
            this.relacionGesPresEsAnioFiscal = null;
            jSFUtils.agregarInfo("Elemento guardado correctamente");
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (BusinessException ex) {
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    
    /**
     * Metodo utilizado para eliminar un registro de Relacion de AnioFiscal y un
     * componente de gestion escolar
     *
     * @param id
     */
    public void eliminarRelacionAnioFiscalGesPresEs(Integer id) {
        try {
            gesPresEsAnioFiscalBean.eliminarRelacion(id);
            jSFUtils.agregarInfo("Elemento eliminado correctamente");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } finally {
            cargarRelacionesAnioFiscalGesPresEs(getGesPresEs().getId());
        }
    }
    
    
    public void habilitarProceso(Integer relId) {
        try {
            Boolean mostraFormulario = Boolean.TRUE;
            if(relacionGesPresEsAnioFiscal == null) {
                mostraFormulario = Boolean.FALSE;
                relacionGesPresEsAnioFiscal = gesPresEsAnioFiscalBean.getRelacionGesAnioById(relId); 
            }
            relacionGesPresEsAnioFiscal.setProcesoEnCurso(Boolean.FALSE);
            relacionGesPresEsAnioFiscal = gesPresEsAnioFiscalBean.crearActualizar(relacionGesPresEsAnioFiscal);
            if(!mostraFormulario) {
                relacionGesPresEsAnioFiscal = null;
            }
            cargarRelacionesAnioFiscalGesPresEs(this.gesPresEs.getId());
            jSFUtils.agregarInfo("El proceso se ha habilitado correctamente.");
        } catch (BusinessException ex) {
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
        RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
    }
    
    /**
     * Metodo utilizado para la generación de TechosPresupuestales segun recursos parametrizados
     * 
     * @param relId 
     */
    //@Asynchronous
    public void generarTechos(Integer relId) {
        List<SsUsuario> usuarios = new ArrayList();
        try {
            if(usuarioLogueado != null && usuarioLogueado.getUsuId() != null) {
                usuarios.add(usuarioLogueado);
            }
            if(gesPresEs != null && gesPresEs.getUsuario() != null) {
                if(usuarioLogueado != null) {
                    if(!gesPresEs.getUsuario().getUsuId().equals(usuarioLogueado.getUsuId())) {
                        usuarios.add(gesPresEs.getUsuario());
                    }
                } else {
                    usuarios.add(gesPresEs.getUsuario());
                }
                
            }
            
            gesPresEsAnioFiscalBean.generarTechos(relId, usuarios);
            Boolean mostraFormulario = Boolean.TRUE;
            if(relacionGesPresEsAnioFiscal == null) {
                mostraFormulario = Boolean.FALSE;
                relacionGesPresEsAnioFiscal = gesPresEsAnioFiscalBean.getRelacionGesAnioById(relId); 
            }
            relacionGesPresEsAnioFiscal.setProcesoEnCurso(Boolean.TRUE);
            relacionGesPresEsAnioFiscal = gesPresEsAnioFiscalBean.crearActualizar(relacionGesPresEsAnioFiscal);
            
            if(!mostraFormulario) {
                relacionGesPresEsAnioFiscal = null;
            }
            cargarRelacionesAnioFiscalGesPresEs(this.gesPresEs.getId());
            // jSFUtils.agregarInfo("Techos generados correctamente");
            jSFUtils.agregarInfo("Ha iniciado la generación de los techos desde sistema. Verifique su bandeja de tareas para ver la ejecución del proceso.");
        } catch (BusinessException ex) {
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
        RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
    }

    /**
     * Metodo utilizado para la generación de TechosPresupuestales segun recursos parametrizados
     * 
     * @param relId 
     */
    public void actualizarEliminarTechos(Integer relId, Integer op) {
        try {
            gesPresEsAnioFiscalBean.actualizarEliminarTechos(relId, op);
            if(op.compareTo(0) ==0) {
                jSFUtils.agregarInfo("Techos deshechos correctamente");
                
               // cargarRelacionesAnioFiscalGesPresEs(this.gesPresEs.getId());
            } else if (op.compareTo(1) ==0) {
                jSFUtils.agregarInfo("Techos aprobados deshechos correctamente");
                //cargarRelacionesAnioFiscalGesPresEs(this.gesPresEs.getId());
            }
            if(relacionGesPresEsAnioFiscal != null) {
                editarRelAnioFiscal(relId);
            }
            cargarRelacionesAnioFiscalGesPresEs(this.gesPresEs.getId());
        } catch (BusinessException ex) {
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
        RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
    }
    /**
     * Metodo utilizado para actualizar los registros de techos
     *
     * @param relId
     */
    public void actualizarTechos(Integer relId) {
        List<SsUsuario> usuarios = new ArrayList();
        try {
            if(usuarioLogueado != null && usuarioLogueado.getUsuId() != null) {
                usuarios.add(usuarioLogueado);
            }
            if(gesPresEs != null && gesPresEs.getUsuario() != null) {
                if(usuarioLogueado != null) {
                    if(!gesPresEs.getUsuario().getUsuId().equals(usuarioLogueado.getUsuId())) {
                        usuarios.add(gesPresEs.getUsuario());
                    }
                } else {
                    usuarios.add(gesPresEs.getUsuario());
                }
                
            }
            gesPresEsAnioFiscalBean.actualizarTechos(relId, usuarios);
            
            Boolean mostraFormulario = Boolean.TRUE;
            if(relacionGesPresEsAnioFiscal == null) {
                mostraFormulario = Boolean.FALSE;
                relacionGesPresEsAnioFiscal = gesPresEsAnioFiscalBean.getRelacionGesAnioById(relId); 
            }
            relacionGesPresEsAnioFiscal.setProcesoEnCurso(Boolean.TRUE);
            relacionGesPresEsAnioFiscal = gesPresEsAnioFiscalBean.crearActualizar(relacionGesPresEsAnioFiscal);
            if(!mostraFormulario) {
                relacionGesPresEsAnioFiscal = null;
            }
            cargarRelacionesAnioFiscalGesPresEs(this.gesPresEs.getId());
           // jSFUtils.agregarInfo("Techos actualizados correctamente");
            jSFUtils.agregarInfo("Ha iniciado la actualización de los techos desde sistema. Verifique su bandeja de tareas para ver la ejecución del proceso.");
        } catch (BusinessException ex) {
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
        RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
    }

    //*** BENEFICIARIOS ***//
    
    /**
     * Este método guarda la seleccion de Beneficiarios realizada
     */
    public void guardarSeleccion() {
        try {
            if (getListaBeneficiarios() != null && !getListaBeneficiarios().isEmpty()) {
                for (Beneficiarios ben : getListaBeneficiarios()) {
                    ben.setSgPresEsAnioFiscal(this.relacionGesPresEsAnioFiscal);
                }
            }
            beneficiariosBean.actualizarCollecciones(getListaBeneficiarios(), getListaEliminarBeneficiarios());
            jSFUtils.agregarInfo("Elemento guardado correctamente");
            cargarListaBeneficiarios();
            cargarListaRelacionModalidades();
            llenarArbol();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
        RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
    }
    
    //*** FINANCIAMIENTO ***//
    
    /**
     * Método utilizado para asignar y preparar para la edicion un registro de RelacionAnioFinanciamiento
     * @param id 
     */
    public void editarRelAnioFinanciamiento(Integer id) {
        try {
            this.relFinanciamientoEdicion = this.relPresAnioFinanciamientoBean.getById(id);
            this.comboFuenteFinanciamientoRelAnio.setSelectedT(this.relFinanciamientoEdicion.getFuenteFinanciamiento());
            fuenteFinanciamientoRelAnioSelected();
            this.comboFuenteRecursosRelAnio.setSelectedT(this.relFinanciamientoEdicion.getFuenteRecursos());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }
    
    /**
     * Metodo utilizado para guardar los registros de FuentesFinanciamiento para un presupuesto
     * 
     */
    public void guardarRelFinanciamiento() {
        try {
            if(relFinanciamientoEdicion.getMontoTotalFormulado() == null) {
                relFinanciamientoEdicion.setMontoTotalFormulado(BigDecimal.ZERO);
            }
            if(relFinanciamientoEdicion.getMontoTotalAprobado() == null) {
                relFinanciamientoEdicion.setMontoTotalAprobado(BigDecimal.ZERO);
            }
            this.relFinanciamientoEdicion.setFuenteFinanciamiento(this.comboFuenteFinanciamientoRelAnio.getSelectedT());
            this.relFinanciamientoEdicion.setFuenteRecursos(this.comboFuenteRecursosRelAnio.getSelectedT());
            this.relFinanciamientoEdicion.setRelAnioPresupuesto(relacionGesPresEsAnioFiscal);
            
            if(relFinanciamientoEdicion.getId() != null) {
                if(relacionGesPresEsAnioFiscal.getMontoTotalFormulado() == null || relacionGesPresEsAnioFiscal.getMontoTotalFormulado().compareTo(totalMontos("F")) < 0){
                    jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_MONTO_TOTAL_FORMULADO_MENOR);
                    return;

                }else if(relacionGesPresEsAnioFiscal.getMontoTotalAprobado() == null || relacionGesPresEsAnioFiscal.getMontoTotalAprobado().compareTo(totalMontos("A")) < 0){
                    jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_MONTO_TOTAL_APROBADO_MENOR);
                    return;
                }
            }
            
            //ASIGNACION AUTOMATICA DE CIFRADO PRESUPUESTARIO
            StringBuilder builder = new StringBuilder();
            builder
                .append(relacionGesPresEsAnioFiscal.getSubCuenta().getCuentaPadre().getCodigo())
                .append("-").append(relacionGesPresEsAnioFiscal.getSubCuenta().getCodigo())
                .append("-").append(comboFuenteFinanciamientoRelAnio.getSelectedT().getCodigo());
            this.relFinanciamientoEdicion.setCifradoPresupuestario(builder.toString());            
            
            this.relPresAnioFinanciamientoBean.crearActualizar(relFinanciamientoEdicion);
            this.cargarListaFuentesFinanciamientoAnio();
            RequestContext.getCurrentInstance().execute("$('#addRelFuenteFinanciamiento').modal('hide');");
            jSFUtils.agregarInfo("Elemento guardado correctamente");
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (BusinessException ex) {
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        } 
    }
    
    /**
     * Método utilizado para eliminar un registro de FuentesFinanciamiento para un presupuesto
     * @param rel 
     */
    public void eliminarRelFinanciamiento(RelacionPresAnioFinanciamiento rel) {
        try {
            this.relPresAnioFinanciamientoBean.eliminarRelacion(rel.getId());
            this.cargarListaFuentesFinanciamientoAnio();
            jSFUtils.agregarInfo("Elemento eliminado correctamente");
            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }
    
    
    
    //******** PERSISTENCIA SEDES ********//
    
    /**
     * Método utilizado para guardar la selección realizada en la pestaña Sedes
     * @return 
     */
    public String guardarTabSedes() {
        try {
            gesPresEsBean.crearActualizarComponente(this.gesPresEs);
            if (this.gesPresEs.getId() != null) {
                findGesPresEsById(this.gesPresEs.getId());
                jSFUtils.agregarInfo("Elemento guardado correctamente");
                RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
            } else {
                return "gestionPresupuestoEscolar.xhtml?faces-redirect=true";
            }
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } finally {
            this.flagEdicion = false;
            RequestContext.getCurrentInstance().execute("$('#verGestionPresupuesto').modal('hide');");
        }
        return null;
    }

    

    

    //******** UTILIDADES ********//
    
    /**
     * Metodo de validacion para datos de objeto relacion componente anio fiscal
     * @return 
     */
    public boolean validacionesRelacionComponenteAnioFiscal(){
        try {
            if(this.relacionGesPresEsAnioFiscal != null){
                if(this.relacionGesPresEsAnioFiscal.getObjetoEspecificoGasto() == null || this.relacionGesPresEsAnioFiscal.getObjetoEspecificoGasto().isEmpty()){
                    jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_OEG_NO_INGRESADO);
                    return false;
                }
                if(this.relacionGesPresEsAnioFiscal.getTipo() != null 
                        && this.relacionGesPresEsAnioFiscal.getTipo().equals(TipoPresupuestoAnio.COMPLEMENTO)
                        && TextUtils.isEmpty(this.relacionGesPresEsAnioFiscal.getNombreComplemento())){
                    jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_NOMBRE_COMPLEMENTO_NO_INGRESADO);
                    return false;
                }
                if (this.oeg == null) {
                    jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_OEG_NO_INGRESADO_NO_VALIDO);
                    return false;
                }
              /**  if(this.relacionGesPresEsAnioFiscal.getTipo().equals(TipoPresupuestoAnio.BASE)){
                    if(!validarCreacionRelacionComponenteAnioFiscal()){
                        return false;
                    }
                }**/
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return true;
    }
    
    /**
     * Este método es utilizado para obtener el total de los montos de una relacion de fuentes de financiamiento
     * @param parametroMonto
     * @return 
     */
    public BigDecimal totalMontos(String parametroMonto){
        BigDecimal total = BigDecimal.ZERO;
        try {
            if (getRelFinanciamientos() != null && !getRelFinanciamientos().isEmpty()) {
                if (parametroMonto.equals("F")) {
                    for (RelacionPresAnioFinanciamiento item : getRelFinanciamientos()) {
                        if(item.getMontoTotalFormulado() != null) {
                            total = total.add(item.getMontoTotalFormulado());
                        } 
                    }
                    if(this.relFinanciamientoEdicion != null && this.relFinanciamientoEdicion.getMontoTotalFormulado() != null){
                        if(this.relFinanciamientoEdicion.getId() == null){
                            if(this.relFinanciamientoEdicion.getMontoTotalFormulado() != null) {
                                total = total.add(this.relFinanciamientoEdicion.getMontoTotalFormulado());
                            }
                        }else{
                            for(RelacionPresAnioFinanciamiento item : getRelFinanciamientos()){
                                if(item.getId().equals(this.relFinanciamientoEdicion.getId())){
                                    if(item.getMontoTotalFormulado() != null) {
                                        total = total.subtract(item.getMontoTotalFormulado());
                                        total = total.add(this.relFinanciamientoEdicion.getMontoTotalFormulado());
                                    }
                                    break;
                                }
                            }
                        }
                    }
                    
                } else if (parametroMonto.equals("A")) {
                    for (RelacionPresAnioFinanciamiento item : getRelFinanciamientos()) {
                        if(item.getMontoTotalAprobado() != null) {
                            total = total.add(item.getMontoTotalAprobado());
                        } 
                    }
                    if(this.relFinanciamientoEdicion != null && this.relFinanciamientoEdicion.getMontoTotalAprobado() != null){
                        if(this.relFinanciamientoEdicion.getId() == null){
                            if(this.relFinanciamientoEdicion.getMontoTotalAprobado() != null) {
                                total = total.add(this.relFinanciamientoEdicion.getMontoTotalAprobado());
                            }
                        }else{
                            for(RelacionPresAnioFinanciamiento item : getRelFinanciamientos()){
                                if(item.getId().equals(this.relFinanciamientoEdicion.getId())){
                                    if(item.getMontoTotalAprobado() != null) {
                                        total = total.subtract(item.getMontoTotalAprobado());
                                        total = total.add(this.relFinanciamientoEdicion.getMontoTotalAprobado());
                                    }
                                    break;
                                }
                            }
                        }
                        
                    }
                }
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } 
        return total;
    }

    /**
     * Este método corresponde al evento de consulta y obtiene el resultado
     * según el filtro aplicado
     */
    public void filterTable() {
        try {
            List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();

            if (!TextUtils.isEmpty(getFiltroComponente().getCod())) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cod", getFiltroComponente().getCod());
                criterios.add(criterio);
            }
            if (!TextUtils.isEmpty(getFiltroComponente().getNombre())) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "nombre", getFiltroComponente().getNombre());
                criterios.add(criterio);
            }

            if (getFiltroHabilitado() != null && !getFiltroHabilitado().isEmpty()) {
                if (getFiltroHabilitado().equals("true")) {
                    MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "habilitado", true);
                    criterios.add(criterio);
                } else {
                    MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "habilitado", false);
                    criterios.add(criterio);
                }
            }
            if (this.comboTiposSubcomponente.getSelectedT() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tipo", this.comboTiposSubcomponente.getSelectedT());
                criterios.add(criterio);
            }
            if (this.comboTiposParametros.getSelectedT() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "parametro", this.comboTiposParametros.getSelectedT());
                criterios.add(criterio);
            }
            if (this.comboTiposMedida.getSelectedT() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "unidadMedida", this.comboTiposMedida.getSelectedT());
                criterios.add(criterio);
            }
            if (getFiltroComponente().getCantidadAnio() != null && getFiltroComponente().getCantidadAnio() != 0) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cantidadAnio", getFiltroComponente().getCantidadAnio());
                criterios.add(criterio);
            }

            if (this.comboTiposCredito.getSelectedT() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tipoCredito.id", this.comboTiposCredito.getSelectedT().getId());
                criterios.add(criterio);
            }
            if (this.comboTipoEjecucion.getSelectedT() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tipoEjecucion", this.comboTipoEjecucion.getSelectedT());
                criterios.add(criterio);
            }

            if (!TextUtils.isEmpty(getFiltroComponente().getDescripcion())) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "descripcion", getFiltroComponente().getDescripcion());
                criterios.add(criterio);
            }
            if (this.unidadTecnicaSelected != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "unidadTecnica.id", this.unidadTecnicaSelected.getId());
                criterios.add(criterio);
            }
            if (this.comboAnioFiscalFiltro .getSelectedT() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "relacionGesPresEsAnioFiscal.anioFiscal.id", this.comboAnioFiscalFiltro.getSelectedT().getId());
                criterios.add(criterio);
            }

            if (this.componenteSelected != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "categoriaPresupuestoEscolar.id", this.componenteSelected.getId());
                criterios.add(criterio);
            }
            
            if (!TextUtils.isEmpty(filtroConvenio)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "convenio", filtroConvenio.trim());
                criterios.add(criterio);
            }
            
            if (!TextUtils.isEmpty(filtroProyecto)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "proyecto", filtroProyecto.trim());
                criterios.add(criterio);
            }
            
            if (!TextUtils.isEmpty(filtroComponenteConvenio)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "categoria", filtroComponenteConvenio.trim());
                criterios.add(criterio);
            }
            
            if (!TextUtils.isEmpty(filtroSubcomponenteConvenio)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "subactividad", filtroSubcomponenteConvenio.trim());
                criterios.add(criterio);
            }
            if (!TextUtils.isEmpty(filtroCategoriaGastoConvenio)) {
               try {
                    MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "categoriaGastoConvenio", Integer.parseInt(filtroCategoriaGastoConvenio.trim()));
                    criterios.add(criterio);
               } catch (Exception e) {
                   logger.log(Level.SEVERE, "Eroor al convertir a entero", e);
               }
            }
            
            if (!TextUtils.isEmpty(getFiltroComponente().getDescripcion())) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "descripcion", getFiltroComponente().getDescripcion());
                criterios.add(criterio);
            }

            if (getFiltroMontoMinimo() != null && !getFiltroMontoMinimo().isEmpty()) {
                if (getFiltroMontoMinimo().equals("true")) {
                    MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "montoMinimo", true);
                    criterios.add(criterio);
                } else {
                    MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "montoMinimo", false);
                    criterios.add(criterio);
                }
            }

            CriteriaTO condicion = null;
            if (!criterios.isEmpty()) {
                if (criterios.size() == 1) {
                    condicion = criterios.get(0);
                } else {
                    condicion = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[0]));
                }
            } else {
                // condición dummy para que el count by criteria funcione
                condicion = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_NULL, "id", 1);
            }

            String[] propiedades = {"id", "cod", "nombre", "habilitado", "tipo", "parametro",
                "unidadMedida", "cantidadAnio",
                "montoMinimo", "categoriaPresupuestoEscolar.nombre","convenio","proyecto","categoria","subactividad","categoriaGastoConvenio"};
            String className = ComponentePresupuestoEscolar.class.getName();
            String[] orderBy = {"id"};
            boolean[] asc = {true};

            IDataProvider dataProvider = new EntityReferenceDataProvider(propiedades, className, condicion, orderBy, asc);
            lazyModel = new GeneralLazyDataModel(dataProvider);
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Este método completa el árbol de recursos para la seccion de
     * beneficiarios
     *
     * @return
     */
    public TreeNode llenarArbol() {
        try {
            Beneficiarios benes;

            TreeNode raiz = new DefaultTreeNode(new Beneficiarios(), null);
            if (this.listaSgOrganizacionesCurriculars != null && !this.listaSgOrganizacionesCurriculars.isEmpty()) {

                for (SgOrganizacionCurricular oc : this.listaSgOrganizacionesCurriculars) {
                    TreeNode t1 = new DefaultTreeNode("Organizaciones", new Beneficiarios(oc.getNombre(), "-", "-", "-", "-", "-"), raiz);

                    for (SgNivel niv : oc.getNiveles()) {
                        TreeNode t2 = new DefaultTreeNode("Nivel", new Beneficiarios("-", niv.getNombre(), "-", "-", "-", "-"), t1);

                        for (SgCiclo cic : niv.getListaCiclosHijos()) {
                            TreeNode t3 = new DefaultTreeNode("Ciclo", new Beneficiarios("-", "-", cic.getNombre(), "-", "-", "-"), t2);

                            for (SgModalidad mod : cic.getListaModalidadesHijos()) {

                                benes = new Beneficiarios();
                                benes.setOrganizacionesCurricular(oc);
                                benes.setSgNiveles(niv);
                                benes.setSgCiclos(cic);
                                benes.setSgModalidades(mod);
                                benes.setSgPresEsAnioFiscal(relacionGesPresEsAnioFiscal);
                                for (Beneficiarios ben : this.listaBeneficiarios) {
                                    if (oc.getId().intValue() == ben.getOrganizacionesCurricular().getId()
                                            && this.relacionGesPresEsAnioFiscal.getId().equals(ben.getSgPresEsAnioFiscal().getId())
                                            && niv.getId().equals(ben.getSgNiveles().getId())
                                            && cic.getId().equals(ben.getSgCiclos().getId())
                                            && mod.getId().equals(ben.getSgModalidades().getId())
                                            && ben.getSgModalidadAtencion() == null) {

                                        benes = ben;
                                        benes.setValor(true);
                                        break;
                                    }
                                }
                                benes.setNivel(niv.getNombre());
                                benes.setCiclo(niv.getNombre());
                                benes.setModalidad(mod.getNombre());
                                TreeNode t4 = new DefaultTreeNode("Modalidad", benes, t3);

                                List<SgModalidadAtencion> modalidades = new ArrayList<>(mod.getModalidadesAtencion());
                                if (modalidades != null && !modalidades.isEmpty()) {
                                    for (SgModalidadAtencion mad : modalidades) {
                                        benes = new Beneficiarios();
                                        benes.setOrganizacionesCurricular(oc);
                                        benes.setSgNiveles(niv);
                                        benes.setSgCiclos(cic);
                                        benes.setSgModalidades(mod);
                                        benes.setSgModalidadAtencion(mad);
                                        benes.setSgPresEsAnioFiscal(relacionGesPresEsAnioFiscal);

                                        for (Beneficiarios ben : this.listaBeneficiarios) {
                                            if (oc.getId().intValue() == ben.getOrganizacionesCurricular().getId()
                                                    && this.relacionGesPresEsAnioFiscal.getId().equals(ben.getSgPresEsAnioFiscal().getId())
                                                    && niv.getId().equals(ben.getSgNiveles().getId())
                                                    && cic.getId().equals(ben.getSgCiclos().getId())
                                                    && (ben.getSgModalidades() != null && ben.getSgModalidades().getId() != null && mod.getId().equals(ben.getSgModalidades().getId()))
                                                    && (ben.getSgModalidadAtencion() != null && ben.getSgModalidadAtencion().getId() != null && mad.getId().equals(ben.getSgModalidadAtencion().getId()))
                                                    && (ben.getSgSubModalidad() == null || ben.getSgSubModalidad().getId() == null)) {
                                                benes = ben;
                                                benes.setValor(true);
                                                break;
                                            }
                                        }
                                        benes.setNivel(niv.getNombre());
                                        benes.setCiclo(cic.getNombre());
                                        benes.setModalidad(mod.getNombre());
                                        benes.setModalidadAtencion(mad.getNombre());
                                        TreeNode t5 = new DefaultTreeNode("ModalidadAtencion", benes, t4);

                                        for (SgSubModalidad sub : mad.getSubModalidades()) {
                                            benes = new Beneficiarios();
                                            benes.setOrganizacionesCurricular(oc);
                                            benes.setSgNiveles(niv);
                                            benes.setSgCiclos(cic);
                                            benes.setSgModalidades(mod);
                                            benes.setSgModalidadAtencion(mad);
                                            benes.setSgSubModalidad(sub);
                                            benes.setSgPresEsAnioFiscal(relacionGesPresEsAnioFiscal);

                                            for (Beneficiarios ben : this.listaBeneficiarios) {
                                                if (oc.getId().intValue() == ben.getOrganizacionesCurricular().getId()
                                                        && this.relacionGesPresEsAnioFiscal.getId().equals(ben.getSgPresEsAnioFiscal().getId())
                                                        && niv.getId().equals(ben.getSgNiveles().getId())
                                                        && cic.getId().equals(ben.getSgCiclos().getId())
                                                        && (ben.getSgModalidades() != null && ben.getSgModalidades().getId() != null && mod.getId().equals(ben.getSgModalidades().getId()))
                                                        && (ben.getSgModalidadAtencion() != null && ben.getSgModalidadAtencion().getId() != null && mad.getId().equals(ben.getSgModalidadAtencion().getId()))
                                                        && (ben.getSgSubModalidad() != null && ben.getSgSubModalidad().getId() != null && sub.getId().equals(ben.getSgSubModalidad().getId()))) {

                                                    benes = ben;
                                                    benes.setValor(true);
                                                    break;
                                                }
                                            }
                                            benes.setNivel(niv.getNombre());
                                            benes.setCiclo(niv.getNombre());
                                            benes.setModalidad(mod.getNombre());
                                            benes.setModalidadAtencion(mad.getNombre());
                                            benes.setSubModalidad(sub.getNombre());
                                            TreeNode t6 = new DefaultTreeNode("SubModalidad", benes, t5);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            arbolOrganizaciones = raiz;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;
    }

    /**
     * Este método carga el listado historico del registro seleccionado
     *
     * @param id
     */
    public void cargarHistorico(Integer id) {
        historico = emd.obtenerHistorico(ComponentePresupuestoEscolarLite.class, id);
    }

    /**
     * Dirige al sitio de componentes de gestion de presupuesto escolar
     *
     * @return
     */
    public String cerrar() {
        return "gestionPresupuestoEscolar.xhtml?faces-redirect=true";
    }

    /**
     * Método utilizado para mostrar el correo del usuario seleccionado
     * @return 
     */
    public String mostrarCorreoUsuarioSeleccionado(){
        if(getComboUsuarios().getSelectedT() != null){
            return getComboUsuarios().getSelectedT().getUsuCorreoElectronico() + (getComboUsuarios().getSelectedT().getUsuTelefono() != null 
                    && StringUtils.isNotBlank(getComboUsuarios().getSelectedT().getUsuTelefono()) ? "-" + getComboUsuarios().getSelectedT().getUsuTelefono() : "");
        }
        return "";
    }
    
    /**
     * Este método verifica las relaciones que tiene el registro Subcomponente
     * @param id 
     */
    public void verificarEliminarComponente(Integer id){
        try {
            subcomponenteEliminar = gesPresEsBean.getGesPresEsById(id);
            flagEliminar = false;
            List<TopePresupuestal> listaTope = topePresupuestalBean.getTopePresupuestalByIdComponentePresupuestoEscolar(id);
            if(listaTope != null && !listaTope.isEmpty()){
                jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_NO_ELIMINAR_REGISTROS_TOPE);
                flagEliminar = true;
            }

            List<TransferenciasComponente> listaTransComp = transferenciasComponenteBean.getTransferenciasComponenteByComponentePresupuesto(id);
            if(listaTransComp != null && !listaTransComp.isEmpty()){
                jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_NO_ELIMINAR_REGISTROS_TRANSFERENCIA_COMPONENTE);
                flagEliminar = true;
            }

            FiltroRelPresAnioFiscal filtro = new FiltroRelPresAnioFiscal();
            filtro.setComponentePresupuestoEscolarId(id);
            filtro.setIncluirCampos(new String[]{"id"});
            List<RelacionGesPresEsAnioFiscal> listaRela = gesPresEsAnioFiscalBean.getComponentesPresupuestoEscolarByFiltro(filtro);
            if(listaRela != null && !listaRela.isEmpty()){
                jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_COMPONENTE_RELACION_PRESUPUESTO_SUBCOMPONENTE);
                flagEliminar = true;
            }
            RequestContext.getCurrentInstance().update("formEliminar");
            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('show');");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    
    /**
     * Método utilizado para verificar si el subcomponente corresponde a proyecto
     */
    public void verificarCorrespondeProyecto() {
        try {
            setCorrespondeProyecto((!TextUtils.isEmpty(gesPresEs.getProyecto())
                    || !TextUtils.isEmpty(gesPresEs.getSubactividad())
                    || !TextUtils.isEmpty(gesPresEs.getCategoria())
                    || !TextUtils.isEmpty(gesPresEs.getConvenio())
                    || gesPresEs.getCategoriaGastoConvenio() != null)
            );
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    
    /**
     * Método utilizado para validar el contenido de los campos de la seccion corresponde a proyecto, del Subcomponente
     */
    public void asignarCamposCorrespondeProyecto(){
        if (!this.correspondeProyecto) {
            this.gesPresEs.setProyecto(null);
            this.gesPresEs.setSubactividad(null);
            this.gesPresEs.setCategoria(null);
            this.gesPresEs.setConvenio(null);
            this.gesPresEs.setCategoriaGastoConvenio(null);
        }else if(this.gesPresEs.getCategoriaGastoConvenio() != null && this.gesPresEs.getCategoriaGastoConvenio() == 0){
            this.gesPresEs.setCategoriaGastoConvenio(null);
        }
    }
    
    /**
     * Método utilizado para validar cuando se debe de mostrar el campo "NombreComplemento"
     * @return 
     */
    public void validarMostrarNombreComplemento(){
        if(this.comboTipoPresupuestoAnio.getSelectedT() != null && this.comboTipoPresupuestoAnio.getSelectedT().equals(TipoPresupuestoAnio.COMPLEMENTO)){
            this.flagNombreComplemento = true;
        }else{
            this.flagNombreComplemento = false;
            this.relacionGesPresEsAnioFiscal.setNombreComplemento(null);
        }
    }
    
    /**
     * Método utilizado para verificar que no exista mas de un presupuesto base para el año fiscal seleccionado
     * @return 
     */
    public boolean validarCreacionRelacionComponenteAnioFiscal(){
        try {
            FiltroRelPresAnioFiscal filtro = new FiltroRelPresAnioFiscal();
            filtro.setComponentePresupuestoEscolarId(this.relacionGesPresEsAnioFiscal.getComponentePresupuestoEscolar().getId());
            filtro.setAnioFiscalId(this.relacionGesPresEsAnioFiscal.getAnioFiscal().getId());
            filtro.setSubCuentaId(this.relacionGesPresEsAnioFiscal.getSubCuenta().getId());
            filtro.setTipo(TipoPresupuestoAnio.BASE);
            List<RelacionGesPresEsAnioFiscal> lista = gesPresEsAnioFiscalBean.getComponentesPresupuestoEscolarByFiltro(filtro);
            if(lista != null && !lista.isEmpty()){
                for(RelacionGesPresEsAnioFiscal item : lista){
                    if(!item.getId().equals(this.relacionGesPresEsAnioFiscal.getId())){
                        jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_BASE_PRESUPUESTO_ANIO_YA_CREADA);
                        return false;
                    }
                }
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return true;
    }
    
    /**
     * Método utilizado para cargar los combos a utilizar en la pestaña de Presupuesto, cuando se seleccione dicha pestaña
     * @param event 
     */
    public void changeTab(TabChangeEvent event) {
        String tabActiveId = event.getTab().getId();
        if (tabActiveId.equals("idTabPresupuesto")) {
            if (comboCuentas == null) {
                cargarCombosPresupuesto();
            }
        }
    }
    
    /**
     * Método utilizado para activar/desactivar la busqueda avanzada en la sección de filtros de Subcomponente
     */
    public void toggleBusquedaAvanzada() {
        this.busquedaAvanzada = !this.busquedaAvanzada;
    }
    
    /**
     * Método utilizado para limpiar la selección de los combos de Areas de Inversión
     */
    public void limpiarCombosArea() {
        comboAreas.setSelected(-1);
        comboSubareas.setSelected(-1);
    }
    
    
    
    
    
    public List<ComponentePresupuestoEscolarLite> getHistorico() {
        return historico;
    }

    public void setHistorico(List<ComponentePresupuestoEscolarLite> historico) {
        this.historico = historico;
    }

    public LazyDataModel getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel lazyModel) {
        this.lazyModel = lazyModel;
    }

    public ComponentePresupuestoEscolar getGesPresEs() {
        return gesPresEs;
    }

    public void setGesPresEs(ComponentePresupuestoEscolar gesPresEs) {
        this.gesPresEs = gesPresEs;
    }

    public boolean isFlagEdicion() {
        return flagEdicion;
    }

    public void setFlagEdicion(boolean flagEdicion) {
        this.flagEdicion = flagEdicion;
    }

    public ComponentePresupuestoEscolar getFiltroComponente() {
        return filtroComponente;
    }

    public void setFiltroComponente(ComponentePresupuestoEscolar filtroComponente) {
        this.filtroComponente = filtroComponente;
    }

    public String getFiltroHabilitado() {
        return filtroHabilitado;
    }

    public void setFiltroHabilitado(String filtroHabilitado) {
        this.filtroHabilitado = filtroHabilitado;
    }

    public String getFiltroMontoMinimo() {
        return filtroMontoMinimo;
    }

    public void setFiltroMontoMinimo(String filtroMontoMinimo) {
        this.filtroMontoMinimo = filtroMontoMinimo;
    }

    public Integer getIdUnidadT() {
        return idUnidadT;
    }

    public void setIdUnidadT(Integer idUnidadT) {
        this.idUnidadT = idUnidadT;
    }

    public List<RelacionGesPresEsAnioFiscal> getListaGesPresEsAnioFiscal() {
        return listaGesPresEsAnioFiscal;
    }

    public void setListaGesPresEsAnioFiscal(List<RelacionGesPresEsAnioFiscal> listaGesPresEsAnioFiscal) {
        this.listaGesPresEsAnioFiscal = listaGesPresEsAnioFiscal;
    }

    public RelacionGesPresEsAnioFiscal getRelacionGesPresEsAnioFiscal() {
        return relacionGesPresEsAnioFiscal;
    }

    public void setRelacionGesPresEsAnioFiscal(RelacionGesPresEsAnioFiscal relacionGesPresEsAnioFiscal) {
        this.relacionGesPresEsAnioFiscal = relacionGesPresEsAnioFiscal;
    }

    public List<AnioFiscal> getListaAnioFiscal() {
        return listaAnioFiscal;
    }

    public void setListaAnioFiscal(List<AnioFiscal> listaAnioFiscal) {
        this.listaAnioFiscal = listaAnioFiscal;
    }

    public List<AreasInversion> getListaAreasInversion() {
        return listaAreasInversion;
    }

    public void setListaAreasInversion(List<AreasInversion> listaAreasInversion) {
        this.listaAreasInversion = listaAreasInversion;
    }

    public SofisComboG<AreasInversion> getComboAreas() {
        return comboAreas;
    }

    public void setComboAreas(SofisComboG<AreasInversion> comboAreas) {
        this.comboAreas = comboAreas;
    }

    public SofisComboG<AreasInversion> getComboSubareas() {
        return comboSubareas;
    }

    public void setComboSubareas(SofisComboG<AreasInversion> comboSubareas) {
        this.comboSubareas = comboSubareas;
    }

    public List<AreasInversion> getListaSubAreasInversion() {
        return listaSubAreasInversion;
    }

    public void setListaSubAreasInversion(List<AreasInversion> listaSubAreasInversion) {
        this.listaSubAreasInversion = listaSubAreasInversion;
    }

    public TreeNode[] getNodosSeleccionados() {
        return nodosSeleccionados;
    }

    public void setNodosSeleccionados(TreeNode[] nodosSeleccionados) {
        this.nodosSeleccionados = nodosSeleccionados;
    }

    public List<Beneficiarios> getListaBeneficiarios() {
        return listaBeneficiarios;
    }

    public void setListaBeneficiarios(List<Beneficiarios> listaBeneficiarios) {
        this.listaBeneficiarios = listaBeneficiarios;
    }

    public TreeNode getArbolOrganizaciones() {
        return arbolOrganizaciones;
    }

    public void setArbolOrganizaciones(TreeNode arbolOrganizaciones) {
        this.arbolOrganizaciones = arbolOrganizaciones;
    }

    public List<Beneficiarios> getListaEliminarBeneficiarios() {
        return listaEliminarBeneficiarios;
    }

    public void setListaEliminarBeneficiarios(List<Beneficiarios> listaEliminarBeneficiarios) {
        this.listaEliminarBeneficiarios = listaEliminarBeneficiarios;
    }

    public SofisComboG<Cuentas> getComboCuentas() {
        return comboCuentas;
    }

    public void setComboCuentas(SofisComboG<Cuentas> comboCuentas) {
        this.comboCuentas = comboCuentas;
    }

    public SofisComboG<Cuentas> getComboSubcuentas() {
        return comboSubcuentas;
    }

    public void setComboSubcuentas(SofisComboG<Cuentas> comboSubcuentas) {
        this.comboSubcuentas = comboSubcuentas;
    }

    public SofisComboG<TipoTransferencia> getComboTiposTransferencia() {
        return comboTiposTransferencia;
    }

    public void setComboTiposTransferencia(SofisComboG<TipoTransferencia> comboTiposTransferencia) {
        this.comboTiposTransferencia = comboTiposTransferencia;
    }

    public SofisComboG<TipoSubcomponente> getComboTiposSubcomponente() {
        return comboTiposSubcomponente;
    }

    public void setComboTiposSubcomponente(SofisComboG<TipoSubcomponente> comboTiposSubcomponente) {
        this.comboTiposSubcomponente = comboTiposSubcomponente;
    }

    public Boolean getBusquedaAvanzada() {
        return busquedaAvanzada;
    }

    public void setBusquedaAvanzada(Boolean busquedaAvanzada) {
        this.busquedaAvanzada = busquedaAvanzada;
    }

    public SofisComboG<TipoMedida> getComboTiposMedida() {
        return comboTiposMedida;
    }

    public void setComboTiposMedida(SofisComboG<TipoMedida> comboTiposMedida) {
        this.comboTiposMedida = comboTiposMedida;
    }

    public SofisComboG<TipoParametro> getComboTiposParametros() {
        return comboTiposParametros;
    }

    public void setComboTiposParametros(SofisComboG<TipoParametro> comboTiposParametros) {
        this.comboTiposParametros = comboTiposParametros;
    }

    public SofisComboG<ComponentePresupuestoEscolar> getComboComponentesDeduccion() {
        return comboComponentesDeduccion;
    }

    public void setComboComponentesDeduccion(SofisComboG<ComponentePresupuestoEscolar> comboComponentesDeduccion) {
        this.comboComponentesDeduccion = comboComponentesDeduccion;
    }

    public SofisComboG<TipoPresupuestoAnio> getComboTipoPresupuestoAnio() {
        return comboTipoPresupuestoAnio;
    }

    public void setComboTipoPresupuestoAnio(SofisComboG<TipoPresupuestoAnio> comboTipoPresupuestoAnio) {
        this.comboTipoPresupuestoAnio = comboTipoPresupuestoAnio;
    }

    public SofisComboG<SgTipoCuentaBancaria> getComboTipoCuentaBancaria() {
        return comboTipoCuentaBancaria;
    }

    public void setComboTipoCuentaBancaria(SofisComboG<SgTipoCuentaBancaria> comboTipoCuentaBancaria) {
        this.comboTipoCuentaBancaria = comboTipoCuentaBancaria;
    }

    public SofisComboG<AnioFiscal> getComboAnioFiscal() {
        return comboAnioFiscal;
    }

    public void setComboAnioFiscal(SofisComboG<AnioFiscal> comboAnioFiscal) {
        this.comboAnioFiscal = comboAnioFiscal;
    }

    public SofisComboG<TipoCredito> getComboTiposCredito() {
        return comboTiposCredito;
    }

    public void setComboTiposCredito(SofisComboG<TipoCredito> comboTiposCredito) {
        this.comboTiposCredito = comboTiposCredito;
    }

    public SofisComboG<FuenteRecursos> getComboFuentesRecurso() {
        return comboFuentesRecurso;
    }

    public void setComboFuentesRecurso(SofisComboG<FuenteRecursos> comboFuentesRecurso) {
        this.comboFuentesRecurso = comboFuentesRecurso;
    }

    public SofisComboG<FuenteFinanciamiento> getComboFuenteFinanciamiento() {
        return comboFuenteFinanciamiento;
    }

    public void setComboFuenteFinanciamiento(SofisComboG<FuenteFinanciamiento> comboFuenteFinanciamiento) {
        this.comboFuenteFinanciamiento = comboFuenteFinanciamiento;
    }

    public SofisComboG<TipoEjecucion> getComboTipoEjecucion() {
        return comboTipoEjecucion;
    }

    public void setComboTipoEjecucion(SofisComboG<TipoEjecucion> comboTipoEjecucion) {
        this.comboTipoEjecucion = comboTipoEjecucion;
    }

    public List<RelacionPresAnioFinanciamiento> getRelFinanciamientos() {
        return relFinanciamientos;
    }

    public void setRelFinanciamientos(List<RelacionPresAnioFinanciamiento> relFinanciamientos) {
        this.relFinanciamientos = relFinanciamientos;
    }

    public RelacionPresAnioFinanciamiento getRelFinanciamientoEdicion() {
        return relFinanciamientoEdicion;
    }

    public void setRelFinanciamientoEdicion(RelacionPresAnioFinanciamiento relFinanciamientoEdicion) {
        this.relFinanciamientoEdicion = relFinanciamientoEdicion;
    }

    public SofisComboG<FuenteFinanciamiento> getComboFuenteFinanciamientoRelAnio() {
        return comboFuenteFinanciamientoRelAnio;
    }

    public void setComboFuenteFinanciamientoRelAnio(SofisComboG<FuenteFinanciamiento> comboFuenteFinanciamientoRelAnio) {
        this.comboFuenteFinanciamientoRelAnio = comboFuenteFinanciamientoRelAnio;
    }

    public SofisComboG<FuenteRecursos> getComboFuenteRecursosRelAnio() {
        return comboFuenteRecursosRelAnio;
    }

    public void setComboFuenteRecursosRelAnio(SofisComboG<FuenteRecursos> comboFuenteRecursosRelAnio) {
        this.comboFuenteRecursosRelAnio = comboFuenteRecursosRelAnio;
    }

    public ObjetoEspecificoGasto getOeg() {
        return oeg;
    }

    public void setOeg(ObjetoEspecificoGasto oeg) {
        this.oeg = oeg;
    }

    public SofisComboG<SsUsuario> getComboUsuarios() {
        return comboUsuarios;
    }

    public void setComboUsuarios(SofisComboG<SsUsuario> comboUsuarios) {
        this.comboUsuarios = comboUsuarios;
    }

    public SofisComboG<AnioFiscal> getComboAnioFiscalFiltro() {
        return comboAnioFiscalFiltro;
    }

    public void setComboAnioFiscalFiltro(SofisComboG<AnioFiscal> comboAnioFiscalFiltro) {
        this.comboAnioFiscalFiltro = comboAnioFiscalFiltro;
    }

    public List<RangoMatricula> getListaRangoMatricula() {
        return listaRangoMatricula;
    }

    public void setListaRangoMatricula(List<RangoMatricula> listaRangoMatricula) {
        this.listaRangoMatricula = listaRangoMatricula;
    }

    public Integer getCtdadRangos() {
        return ctdadRangos;
    }

    public void setCtdadRangos(Integer ctdadRangos) {
        this.ctdadRangos = ctdadRangos;
    }

    public ComponentePresupuestoEscolar getSubcomponenteEliminar() {
        return subcomponenteEliminar;
    }

    public void setSubcomponenteEliminar(ComponentePresupuestoEscolar subcomponenteEliminar) {
        this.subcomponenteEliminar = subcomponenteEliminar;
    }

    public boolean isFlagEliminar() {
        return flagEliminar;
    }

    public void setFlagEliminar(boolean flagEliminar) {
        this.flagEliminar = flagEliminar;
    }

    public Boolean getCorrespondeProyecto() {
        return correspondeProyecto;
    }

    public void setCorrespondeProyecto(Boolean correspondeProyecto) {
        this.correspondeProyecto = correspondeProyecto;
    }

    public boolean isFlagNombreComplemento() {
        return flagNombreComplemento;
    }

    public void setFlagNombreComplemento(boolean flagNombreComplemento) {
        this.flagNombreComplemento = flagNombreComplemento;
    }

    public List<SgTipoOrganismoAdministrativo> getTiposOrganismos() {
        return tiposOrganismos;
    }

    public void setTiposOrganismos(List<SgTipoOrganismoAdministrativo> tiposOrganismos) {
        this.tiposOrganismos = tiposOrganismos;
    }

    public String getFiltroConvenio() {
        return filtroConvenio;
    }

    public void setFiltroConvenio(String filtroConvenio) {
        this.filtroConvenio = filtroConvenio;
    }

    public String getFiltroProyecto() {
        return filtroProyecto;
    }

    public void setFiltroProyecto(String filtroProyecto) {
        this.filtroProyecto = filtroProyecto;
    }

    public String getFiltroComponenteConvenio() {
        return filtroComponenteConvenio;
    }

    public void setFiltroComponenteConvenio(String filtroComponenteConvenio) {
        this.filtroComponenteConvenio = filtroComponenteConvenio;
    }

    public String getFiltroSubcomponenteConvenio() {
        return filtroSubcomponenteConvenio;
    }

    public void setFiltroSubcomponenteConvenio(String filtroSubcomponenteConvenio) {
        this.filtroSubcomponenteConvenio = filtroSubcomponenteConvenio;
    }

    public String getFiltroCategoriaGastoConvenio() {
        return filtroCategoriaGastoConvenio;
    }

    public void setFiltroCategoriaGastoConvenio(String filtroCategoriaGastoConvenio) {
        this.filtroCategoriaGastoConvenio = filtroCategoriaGastoConvenio;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Integer getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(Integer tabIndex) {
        this.tabIndex = tabIndex;
    }
    
    public CategoriaPresupuestoEscolar getComponenteSelected() {
        return componenteSelected;
    }

    public void setComponenteSelected(CategoriaPresupuestoEscolar componenteSelected) {
        this.componenteSelected = componenteSelected;
    }

    public UnidadTecnica getUnidadTecnicaSelected() {
        return unidadTecnicaSelected;
    }

    public void setUnidadTecnicaSelected(UnidadTecnica unidadTecnicaSelected) {
        this.unidadTecnicaSelected = unidadTecnicaSelected;
    }

    public Boolean getEditarSubComponente() {
        return editarSubComponente;
    }

    public void setEditarSubComponente(Boolean editarSubComponente) {
        this.editarSubComponente = editarSubComponente;
    }
    
    
}
