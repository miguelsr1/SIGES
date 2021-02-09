package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.business.GestionPresupuestoEscolarBean;
import gob.mined.siap2.business.ejbs.impl.CategoriaPresupuestoEscolarBean;
import gob.mined.siap2.business.ejbs.impl.CuentasBean;
import gob.mined.siap2.business.ejbs.impl.FuenteFinanciamientoBean;
import gob.mined.siap2.business.ejbs.impl.FuenteRecursosBean;
import gob.mined.siap2.business.ejbs.impl.GeneralBean;
import gob.mined.siap2.business.ejbs.impl.PresupuestoEscolarBean;
import gob.mined.siap2.business.ejbs.impl.SedeBean;
import gob.mined.siap2.business.ejbs.impl.TipoCreditoBean;
import gob.mined.siap2.business.ejbs.impl.TopePresupuestalBean;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.CategoriaPresupuestoEscolar;
import gob.mined.siap2.entities.data.impl.ComponentePresupuestoEscolar;
import gob.mined.siap2.entities.data.impl.Cuentas;
import gob.mined.siap2.entities.data.impl.FuenteFinanciamiento;
import gob.mined.siap2.entities.data.impl.FuenteRecursos;
import gob.mined.siap2.entities.data.impl.RelacionGesPresEsAnioFiscal;
import gob.mined.siges.entities.data.impl.SgPresupuestoEscolar;
import gob.mined.siges.entities.data.impl.SgPresupuestoEscolarMovimiento;
import gob.mined.siges.entities.data.impl.SgSede;
import gob.mined.siap2.entities.data.impl.TipoCredito;
import gob.mined.siap2.entities.data.impl.TopeDetalleMatriculas;
import gob.mined.siap2.entities.data.impl.TopePresupuestal;
import gob.mined.siap2.entities.data.impl.TopePresupuestalLite;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.entities.enums.EstadoMovimientos;
import gob.mined.siap2.entities.enums.EstadoPresupuestoEscolar;
import gob.mined.siap2.entities.enums.EstadoTopePresupuestal;
import gob.mined.siap2.entities.enums.TipoPresupuestoAnio;
import gob.mined.siap2.entities.enums.TipoSubcomponente;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.filtros.FiltroCodiguera;
import gob.mined.siap2.filtros.FiltroComponentePresupuestoEscolar;
import gob.mined.siap2.filtros.FiltroTopePresupuestal;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.genericos.constantes.ConstantesPresentacion;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.utils.SofisComboG;
import gob.mined.siap2.web.utils.dataProvider.EntityReferenceDataProvider;
import gob.mined.siap2.web.utils.dataProvider.IDataProvider;
import gob.mined.siap2.web.utils.dataProvider.LazyLoadingDetalleTopePresupuestal;
import gob.mined.siap2.web.utils.dataProvider.LazyLoadingTopePresupuestal;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;

@ViewScoped
@Named(value = "detalleTopePresupuestalMB")
public class DetalleTopePresupuestalMB implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    private Cuentas cuenta;
    
    private Integer valorMaxGuardado;
    private Boolean flagCreate;
    private String codSede;
    
    private SofisComboG<AnioFiscal> comboAnioFiscal = new SofisComboG<>();
    private SofisComboG<TipoCredito> comboTipoCredito = new SofisComboG<>();
    private SofisComboG<EstadoTopePresupuestal> comboEstadoTope;
    private SofisComboG<EstadoTopePresupuestal> comboEstadoMultiples;
    private SofisComboG<TipoPresupuestoAnio> comboTipoPresupuestoAnio;
    private Boolean mostrarCampos = true;
    protected LazyDataModel lazyModel;

    private List<AnioFiscal> listaAnioFiscal;
    private List<TopePresupuestalLite> listaTopePresupuestalHistortico;
    private List<TopePresupuestal> listaSeleccionTopes;
    private List<ComponentePresupuestoEscolar> listaComponentes;
    private List<Cuentas> listaCuentas;
    private List<Cuentas> listaCuentasPadres;
    private List<CategoriaPresupuestoEscolar> listaCategoria;
    private List<TipoCredito> listaTipoCreditos;

    @Inject
    private TopePresupuestalBean presupuestalBean;
    @Inject
    private JSFUtils jSFUtils;
    @Inject
    private EntityManagementDelegate emd;
    @Inject
    private CuentasBean cuentasBean;
    @Inject
    private GestionPresupuestoEscolarBean escolarBean;
    @Inject
    private GeneralBean generalBean;
    @Inject
    private CategoriaPresupuestoEscolarBean categoriaBean;
    @Inject
    private TipoCreditoBean tipoCreditoBean;
    @Inject
    private SedeBean sedeBean;
    @Inject
    private PresupuestoEscolarBean presupuestoEscolarBean;
    @Inject
    private FuenteFinanciamientoBean fuenteFinanciamientoBean;
    @Inject
    private FuenteRecursosBean fuenteRecursosBean;
    
    private RelacionGesPresEsAnioFiscal rel;
    private CategoriaPresupuestoEscolar componenteSelected;
    private ComponentePresupuestoEscolar subComponenteSelected;
    private FuenteFinanciamiento fuenteFinanciamientoSelected;
    private FuenteRecursos fuenteRecursosSelected;
    private Cuentas lineaPresupuestariaSelected;
    private Cuentas unidadPresupuestariaSelected;
    private UnidadTecnica unidadTecnicaSelected;
    
    @PostConstruct
    public void init() {
        try {
            String id = buscarIdentificador("id");
            String gen = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("gen");
            cargarCombos();
            AnioFiscal anioFiscal = null;
            
            
            String compoId = buscarIdentificador("compoId");
            String opcion = buscarIdentificador("op");
            if (!TextUtils.isEmpty(compoId)) {
                rel = (RelacionGesPresEsAnioFiscal) emd.getEntityById(RelacionGesPresEsAnioFiscal.class.getCanonicalName(), Integer.valueOf(compoId));
                if(rel != null) {
                    this.mostrarCampos = Boolean.FALSE;
                }
            } else {
                anioFiscal = generalBean.getUltimoAnioFiscalFormulacion();
            }
            comboAnioFiscal.setSelectedT(anioFiscal);
            cargarCuentas();
           
            
            if (!TextUtils.isEmpty(opcion)) {
                if("1".equals(opcion)) {
                    comboEstadoTope.setSelectedT(EstadoTopePresupuestal.EN_PROCESO);
                } else if("2".equals(opcion)) {
                    comboEstadoTope.setSelectedT(EstadoTopePresupuestal.APROBACION);
                }
            }
            if (!TextUtils.isEmpty(gen)) {
                Integer genAfter = Integer.parseInt(gen);
                if(genAfter.compareTo(1) == 0) {
                    jSFUtils.agregarInfo("Ha iniciado la generación de los techos desde archivo. Cuando el proceso finalice se enviará una notitifcación a su bandeja de tareas.");
                }
            }
            filterTable();
        } catch (Exception ex){
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$(window).scrollTop(0);");
        }
    }
    
    /**
     * Metodo utilizado para llamar los procesos de llenado de combos
     */
    public void cargarCombos(){
        this.comboTipoPresupuestoAnio = new SofisComboG(Arrays.asList(TipoPresupuestoAnio.values()), "label");
        this.comboTipoPresupuestoAnio.addEmptyItem(ConstantesPresentacion.FILTRO_VACIO);
        
        cargarAnioFiscal();
        cargarCategoriaPresupuesto();
        cargarTipoCreditos();
        cargarCuentas();
        cargarEstados();
        //cargarFuenteFinanciamiento();
        setCodSede(null); 
    }


    public Boolean getMostrarMonto(BigDecimal monto) {
        if(monto == null || (monto != null && (monto.compareTo(new BigDecimal(BigInteger.ZERO)) == 0))) {
            return false;
        }
        return true;
    }
    /**
     * Este método limpia los objetos de contexto para la creacion y filtro
     */
    public void cleanObject() {
        setCodSede(null);
        comboAnioFiscal.clearSelectedT();
        componenteSelected = null;
        subComponenteSelected = null;
        comboTipoCredito.clearSelectedT();
        unidadPresupuestariaSelected = null;
        lineaPresupuestariaSelected = null;
        comboEstadoTope.clearSelectedT();
        if (this.comboTipoPresupuestoAnio != null) {
            comboTipoPresupuestoAnio.clearSelectedT();
        };
        filterTable();
    }

    /**
     * Este método busca la llave de los parametros enviados desde la vista y
     * obtiene su valor
     *
     * @param parametro
     * @return
     */
    public String buscarIdentificador(String parametro) {
        try {
            String valor = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(parametro);
            if (valor == null) {
                valor = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get(parametro);
            }
            return valor;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;
    }

    /**
     * Este método se utiliza para buscar un centro educativo filtrado por el codigo
     * @return 
     */
    public SgSede findSede(){
        try {
            if (getCodSede() != null && !getCodSede().isEmpty()) {
                return sedeBean.getSedeByCodigo(getCodSede());
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;
    }
    
    
    
    
    
    /**
     * Metodo para la carga de registros de Componentes de gestion escolar
     */
    public void cargarComponentes() {
        try {
            this.listaComponentes = escolarBean.getGesPresEsHabilitados();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    /**
     * Metodo para la carga de estados de topePresupuestal
     */
    public void cargarEstados() {
        try {
            List<EstadoTopePresupuestal> estados = new ArrayList(Arrays.asList(EstadoTopePresupuestal.values()));
            comboEstadoMultiples = new SofisComboG(estados, "label");
            comboEstadoMultiples.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
            
            comboEstadoTope = new SofisComboG(estados, "label");
            comboEstadoTope.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    /**
     * Metodo para la carga de registros de Componentes de gestion escolar
     */
    public void cargarComponentesPorCategoria() {
        try {
            if (componenteSelected != null) {
                this.listaComponentes = escolarBean.getGesPresEsByCategoria(componenteSelected.getId());
                this.subComponenteSelected = null;
                //comboComponentes = new SofisComboG<>(listaComponentes, "nombre");
               // comboComponentes.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    public List<CategoriaPresupuestoEscolar> completeComponentes(String query){
        try {
           // if(query != null && !query.isEmpty()) {
                FiltroComponentePresupuestoEscolar filtro = new FiltroComponentePresupuestoEscolar();
                filtro.setNombre(query);
                filtro.setHabilitado(Boolean.TRUE);
                filtro.setAscending(new boolean[]{true});
                filtro.setOrderBy(new String[]{"nombre"});
                filtro.setMaxResults(10L);
                filtro.setIncluirCampos(new String[]{"nombre","codigo","version"});
                return categoriaBean.obtenerComponentesPorFiltro(filtro);
            //}
            
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;
    }
    
    public List<ComponentePresupuestoEscolar> completeSubComponentes(String query){
        try {
            if(this.componenteSelected != null) {
                FiltroComponentePresupuestoEscolar filtro = new FiltroComponentePresupuestoEscolar();
                filtro.setCategoriaComponenteId(componenteSelected.getId());
                filtro.setNombre(query);
                filtro.setHabilitado(Boolean.TRUE);
                filtro.setAscending(new boolean[]{true});
                filtro.setOrderBy(new String[]{"nombre"});
                filtro.setTipoSubcomponente(TipoSubcomponente.CALCULO_POR_SISTEMA);
                filtro.setMaxResults(10L);
                filtro.setIncluirCampos(new String[]{"id","nombre","version"});
                return escolarBean.getComponentesPresupuestoEscolarByFiltro(filtro);
            }
            
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;
    }
    
    public List<FuenteFinanciamiento> completeFuentesFinanciamiento(String query){
        try {
            FiltroCodiguera filtro = new FiltroCodiguera();
            filtro.setNombre(query);
            filtro.setHabilitado(Boolean.TRUE);
            filtro.setAscending(new boolean[]{true});
            filtro.setOrderBy(new String[]{"nombre"});
            filtro.setMaxResults(10L);
            filtro.setIncluirCampos(new String[]{"id","nombre","version"});
            return fuenteFinanciamientoBean.getFuentesFinanciamientoFiltro(filtro);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;
    }
    public List<FuenteRecursos> completeFuentesRecursos(String query){
        try {
            if(fuenteFinanciamientoSelected != null) {
                FiltroCodiguera filtro = new FiltroCodiguera();
                filtro.setNombre(query);
                filtro.setHabilitado(Boolean.TRUE);
                filtro.setFuenteId(fuenteFinanciamientoSelected != null ? fuenteFinanciamientoSelected.getId() : null);
                filtro.setAscending(new boolean[]{true});
                filtro.setOrderBy(new String[]{"nombre"});
                filtro.setMaxResults(10L);
                filtro.setIncluirCampos(new String[]{"id","nombre","version"});
                return fuenteRecursosBean.getFuentesRecursosFiltro(filtro);
            }
            
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;
    }
    public List<Cuentas> completeUnidadesPresupuestarias(String query){
        try {
            FiltroCodiguera filtro = new FiltroCodiguera();
            filtro.setNombre(query);
            filtro.setAnioId(comboAnioFiscal != null && comboAnioFiscal.getSelectedT() != null ? comboAnioFiscal.getSelectedT().getId() : null);
            filtro.setHabilitado(Boolean.TRUE);
            filtro.setAscending(new boolean[]{true});
            filtro.setOrderBy(new String[]{"nombre"});
            filtro.setMaxResults(10L);
            filtro.setIncluirCampos(new String[]{"id","nombre","version"});
            return cuentasBean.getCuentasPorFiltro(filtro);
            
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;
    }
    public List<Cuentas> completeLineaPresupuestarias(String query){
        try {
            if(unidadPresupuestariaSelected != null) {
                FiltroCodiguera filtro = new FiltroCodiguera();
                filtro.setNombre(query);
                filtro.setPadreId(unidadPresupuestariaSelected.getId());
                filtro.setHabilitado(Boolean.TRUE);
                filtro.setAscending(new boolean[]{true});
                filtro.setOrderBy(new String[]{"nombre"});
                filtro.setMaxResults(10L);
                filtro.setIncluirCampos(new String[]{"id","nombre","version"});
                return cuentasBean.getCuentasPorFiltro(filtro);
            }
 
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;
    }
    /**
     * Metodo para la carga de registros de Anios fiscales
     */
    public void cargarAnioFiscal() {
        try {
            this.listaAnioFiscal = generalBean.getAniosFiscalesPlanificacion();
            comboAnioFiscal = new SofisComboG<>(listaAnioFiscal, "nombre");
            comboAnioFiscal.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    /**
     * Metodo para la carga de registros de Categorias de presupuesto escolar
     */
    public void cargarCategoriaPresupuesto() {
        try {
            this.listaCategoria = categoriaBean.getAllCategoriaPresupuestoEscolarHabilitados();
            this.componenteSelected = null;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    /**
     * Metodo para la carga de registros de SubCuentas
     */
    public void cargarCuentas() {
        try {
            if(comboAnioFiscal.getSelectedT() != null){
                this.listaCuentasPadres = cuentasBean.getCuentasByAnioFiscal(this.comboAnioFiscal.getSelectedT().getId());
                unidadPresupuestariaSelected = null;
                
                lineaPresupuestariaSelected = null;
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    /**
     * Metodo para la carga de registros de SubCuentas, filtrado por un ID de
     * cuenta padre seleccionada en el objeto de contexto
     */
    public void cargarSubCuentas() {
        try {
            if (unidadPresupuestariaSelected != null) {
                this.listaCuentas = cuentasBean.getCuentasByIdPadre(unidadPresupuestariaSelected.getId());
                lineaPresupuestariaSelected = null;
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    /**
     * Este método carga una lista de registros TipoCreditos
     */
    public void cargarTipoCreditos() {
        try {
            this.listaTipoCreditos = tipoCreditoBean.getTipoCreditoHabilitados();
            comboTipoCredito = new SofisComboG<>(listaTipoCreditos, "nombre");
            comboTipoCredito.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    
    /**
     * Método utilizado para cargar el combo de Fuente Financiamiento
     */
    public void cargarFuenteFinanciamiento() {
        try {
            List<FuenteFinanciamiento> ff = generalBean.getClasesGeneralCodiguera(FuenteFinanciamiento.class);
            //comboFuenteFinanciamiento = new SofisComboG(ff, "nombre");
            //comboFuenteFinanciamiento.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
            
           // comboFuenteRecursos = new SofisComboG();
            //comboFuenteRecursos.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    
    /**
     * Método utilizado para cargar el combo de Fuente Recursos
     */
    public void cargarFuenteRecursos() {
        try {
           /** if (comboFuenteFinanciamiento.getSelectedT() != null) {
                comboFuenteRecursos = new SofisComboG(generalBean.getFuentesRecursos(comboFuenteFinanciamiento.getSelectedT().getId()), "nombre");
                comboFuenteRecursos.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
            } else {
                comboFuenteRecursos = new SofisComboG();
                comboFuenteRecursos.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
            }**/
           fuenteRecursosSelected = null;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    
    
    public boolean verificarSeleccion() {
        if (this.subComponenteSelected == null) {
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_SUBCOMPONENTE_NO_SELECCIONADO);
            return false;
        }
        if (this.lineaPresupuestariaSelected == null) {
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_LINEA_PRESUPUESTARIA_NO_SELECCIONADA);
            return false;
        }
        if (this.comboEstadoTope.getSelectedT() == null) {
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_ESTADO_NO_SELECCIONADO);
            return false;
        }
        return true;
    }
    
    
    /**
     * Metodo utilizado para eliminar un registro de TopePresupuestal
     *
     * @param id ID del registro a eliminar
     */
    public void eliminar(Integer id) {
        try {
            if(verificarEliminacion(id)){
                presupuestalBean.eliminar(id);
                jSFUtils.agregarInfo("Elemento eliminado correctamente");
                filterTable();
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
            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('hide');");
        }
    }

    /**
     * Método utilizado para verificar si el registro seleccionado a eliminar no tiene relaciones
     * @param id
     * @return 
     */
    public boolean verificarEliminacion(Integer id){
        try {
            FiltroTopePresupuestal filtro = new FiltroTopePresupuestal();
            filtro.setPresupuestoEscolarMovimientoTopeId(id);
            filtro.setIncluirCampos(new String[]{"id"});
            
            List<TopePresupuestal> listaTope = presupuestalBean.getTopesPresupuestalesByFiltro(filtro);
            if(listaTope != null && !listaTope.isEmpty()){
                jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_ELIMINE_LOS_MOVIMIENTOS_DE_PRESUPUESTO_DE_TECHO);
                return false;
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return true;
    }
    
    
    

    /**
     * Metodo que verifica la existencia de registros de TopePresupuestal con las mismas condiciones
     * @param tope
     * @return 
     */
    public boolean verificarTechoExistente(TopePresupuestal tope){
        try {
            FiltroTopePresupuestal filtro = new FiltroTopePresupuestal();
            filtro.setIncluirCampos(new String[]{"id"});
            filtro.setComponentePresupuestoEscolarId(tope.getGesPresEs().getId());
            filtro.setCuentaId(tope.getSubCuenta().getId());
            filtro.setSedeId(tope.getSede().getId());
            List<TopePresupuestal> lista = presupuestalBean.getTopesPresupuestalesByFiltro(filtro);
            return !(lista != null && !lista.isEmpty());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return false;
    }
    
    /**
     * Este método se encarga de obtener el texto del enumerado segun el codigo
     *
     * @param estado
     * @return
     */
    public String obtenerNombreEstado(EstadoTopePresupuestal estado) {
        try {
            return estado.getLabel();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return "";
    }

    /**
     * Este método corresponde al evento de consulta y obtiene el resultado
     * según el filtro aplicado
     */
    public void filterTable() {
        try {
            List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();

            if (getComboAnioFiscal().getSelectedT() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "topePresupuestal.anioFiscal.id", getComboAnioFiscal().getSelectedT().getId());
                criterios.add(criterio);
            }
            if (this.componenteSelected != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "topePresupuestal.gesPresEs.categoriaPresupuestoEscolar.id", this.componenteSelected.getId());
                criterios.add(criterio);
            }
            if (this.subComponenteSelected != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "topePresupuestal.gesPresEs.id", this.subComponenteSelected.getId());
                criterios.add(criterio);
            }
            if (this.fuenteFinanciamientoSelected != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "topePresupuestal.fuenteFinanciamiento.id", this.fuenteFinanciamientoSelected.getId());
                criterios.add(criterio);
            }
            
            if (this.fuenteRecursosSelected != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "topePresupuestal.fuenteRecursos.id", this.fuenteRecursosSelected.getId());
                criterios.add(criterio);
            }
            
            if (getComboTipoCredito().getSelectedT() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "topePresupuestal.gesPresEs.tipoCredito.id", getComboTipoCredito().getSelectedT().getId());
                criterios.add(criterio);
            }
            if (this.unidadPresupuestariaSelected!= null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "topePresupuestal.subCuenta.cuentaPadre.id", this.unidadPresupuestariaSelected.getId());
                criterios.add(criterio);
            }
            if (this.lineaPresupuestariaSelected != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "topePresupuestal.subCuenta.id", this.lineaPresupuestariaSelected.getId());
                criterios.add(criterio);
            }
            if (getComboEstadoTope() != null && getComboEstadoTope().getSelectedT() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "topePresupuestal.estado", getComboEstadoTope().getSelectedT());
                criterios.add(criterio);
            }
            if (getCodSede() != null && !getCodSede().isEmpty()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "topePresupuestal.sede.codigo", getCodSede());
                criterios.add(criterio);
            }
            if (getComboTipoPresupuestoAnio().getSelectedT() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "topePresupuestal.gesPresEs.relacionGesPresEsAnioFiscals.tipo", getComboTipoPresupuestoAnio().getSelectedT());
                criterios.add(criterio);
            }
            
            if(rel != null && rel.getId() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "topePresupuestal.relGesPres.id", rel.getId());
                criterios.add(criterio);
            }
            ArrayList<CriteriaTO> listaMontos = new ArrayList();
            MatchCriteriaTO criterioMonto = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATER, "topePresupuestal.monto", 0);
            listaMontos.add(criterioMonto);
                
            MatchCriteriaTO criterioMontoAprobado = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATER, "topePresupuestal.montoAprobado", 0);
            listaMontos.add(criterioMontoAprobado);
            
            CriteriaTO condicionOR = CriteriaTOUtils.createORTO(listaMontos.toArray(new CriteriaTO[listaMontos.size()]));
            criterios.add(condicionOR);
            
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "topePresupuestal.gesPresEs.tipo", TipoSubcomponente.CALCULO_POR_SISTEMA);
            criterios.add(criterio);
            
//            if (getComboFuenteFin anciamiento().getSelectedT() != null) {
//                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "", getComboFuenteFinanciamiento().getSelectedT().getId());
//                criterios.add(criterio);
//            }
//            if (getComboFuenteRecursos().getSelectedT() != null) {
//                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "", getComboFuenteRecursos().getSelectedT().getId());
//                criterios.add(criterio);
//            }
            CriteriaTO condicion;
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

            String[] propiedades = {"topePresupuestal.anioFiscal.anio","topePresupuestal.gesPresEs.categoriaPresupuestoEscolar.nombre","topePresupuestal.gesPresEs.nombre",
                "topePresupuestal.subCuenta.codigo","topePresupuestal.sede.codigo","topePresupuestal.montoAprobado","topePresupuestal.monto",
                "topePresupuestal.cantidadMatricula","topePresupuestal.cantidadMatriculaAprobada",
                "topePresupuestal.sede.nombre","topePresupuestal.sede.direccion.departamento.nombre","cantidadMatriculas",
                "topePresupuestal.sede.organismosAdministracionEscolar.oaeTipoOrganismoAdministrativo.toaNombre",
                "nivel.nombre","modalidadEducativa.nombre","modalidadAtencion.nombre"
            };
            
            String className = TopeDetalleMatriculas.class.getName();
            String[] orderBy = {"topePresupuestal.sede.codigo","nivel.nombre","modalidadEducativa.nombre","modalidadAtencion.nombre"};
            boolean[] asc = {true};

            IDataProvider dataProvider = new EntityReferenceDataProvider(propiedades, className, condicion, orderBy, asc);
            lazyModel = new LazyLoadingDetalleTopePresupuestal(dataProvider);
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    public BigDecimal obtenerMonto(BigDecimal monto, Integer matriculasTotales, Integer matriculas) {
        BigDecimal montoRetorno = BigDecimal.ZERO;
        if(monto != null && matriculasTotales != null && matriculas != null && matriculasTotales > 0) {
            montoRetorno = monto.divide(new BigDecimal(matriculasTotales), 2, RoundingMode.HALF_UP);
            montoRetorno = montoRetorno.multiply(new BigDecimal(matriculas));
        }
        return montoRetorno.setScale(2, RoundingMode.HALF_UP);
    }
    /**
     * Este método carga un listado historico de un registro
     *
     * @param id
     */
    public void cargarHistorico(Integer id) {
        this.listaTopePresupuestalHistortico = emd.obtenerHistorico(TopePresupuestalLite.class, id);
    }

    /**
     * Dirige al sitio de Tope Presupuestal
     *
     * @return
     */
    public String cerrar() {
        return "consultaTopePresupuestal.xhtml?faces-redirect=true";
    }

    public List<TopePresupuestalLite> getListaTopePresupuestalHistortico() {
        return listaTopePresupuestalHistortico;
    }

    public void setListaTopePresupuestalHistortico(List<TopePresupuestalLite> listaTopePresupuestalHistortico) {
        this.listaTopePresupuestalHistortico = listaTopePresupuestalHistortico;
    }

    public List<ComponentePresupuestoEscolar> getListaComponentes() {
        return listaComponentes;
    }

    public void setListaComponentes(List<ComponentePresupuestoEscolar> listaComponentes) {
        this.listaComponentes = listaComponentes;
    }

    public List<Cuentas> getListaCuentas() {
        return listaCuentas;
    }

    public void setListaCuentas(List<Cuentas> listaCuentas) {
        this.listaCuentas = listaCuentas;
    }

    public List<AnioFiscal> getListaAnioFiscal() {
        return listaAnioFiscal;
    }

    public void setListaAnioFiscal(List<AnioFiscal> listaAnioFiscal) {
        this.listaAnioFiscal = listaAnioFiscal;
    }

    public LazyDataModel getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel lazyModel) {
        this.lazyModel = lazyModel;
    }

    public List<CategoriaPresupuestoEscolar> getListaCategoria() {
        return listaCategoria;
    }

    public void setListaCategoria(List<CategoriaPresupuestoEscolar> listaCategoria) {
        this.listaCategoria = listaCategoria;
    }

    public List<TipoCredito> getListaTipoCreditos() {
        return listaTipoCreditos;
    }

    public void setListaTipoCreditos(List<TipoCredito> listaTipoCreditos) {
        this.listaTipoCreditos = listaTipoCreditos;
    }

    public List<Cuentas> getListaCuentasPadres() {
        return listaCuentasPadres;
    }

    public void setListaCuentasPadres(List<Cuentas> listaCuentasPadres) {
        this.listaCuentasPadres = listaCuentasPadres;
    }

    public SofisComboG<AnioFiscal> getComboAnioFiscal() {
        return comboAnioFiscal;
    }

    public void setComboAnioFiscal(SofisComboG<AnioFiscal> comboAnioFiscal) {
        this.comboAnioFiscal = comboAnioFiscal;
    }

    public String getCodSede() {
        return codSede;
    }

    public void setCodSede(String codSede) {
        this.codSede = codSede;
    }

    public Cuentas getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuentas cuenta) {
        this.cuenta = cuenta;
    }

    public SofisComboG<EstadoTopePresupuestal> getComboEstadoTope() {
        return comboEstadoTope;
    }

    public void setComboEstadoTope(SofisComboG<EstadoTopePresupuestal> comboEstadoTope) {
        this.comboEstadoTope = comboEstadoTope;
    }

    public SofisComboG<TipoCredito> getComboTipoCredito() {
        return comboTipoCredito;
    }

    public void setComboTipoCredito(SofisComboG<TipoCredito> comboTipoCredito) {
        this.comboTipoCredito = comboTipoCredito;
    }

    public List<TopePresupuestal> getListaSeleccionTopes() {
        return listaSeleccionTopes;
    }

    public void setListaSeleccionTopes(List<TopePresupuestal> listaSeleccionTopes) {
        this.listaSeleccionTopes = listaSeleccionTopes;
    }

    public SofisComboG<EstadoTopePresupuestal> getComboEstadoMultiples() {
        return comboEstadoMultiples;
    }

    public void setComboEstadoMultiples(SofisComboG<EstadoTopePresupuestal> comboEstadoMultiples) {
        this.comboEstadoMultiples = comboEstadoMultiples;
    }

    public Integer getValorMaxGuardado() {
        return valorMaxGuardado;
    }

    public void setValorMaxGuardado(Integer valorMaxGuardado) {
        this.valorMaxGuardado = valorMaxGuardado;
    }

    public Boolean getFlagCreate() {
        return flagCreate;
    }

    public void setFlagCreate(Boolean flagCreate) {
        this.flagCreate = flagCreate;
    }

    public SofisComboG<TipoPresupuestoAnio> getComboTipoPresupuestoAnio() {
        return comboTipoPresupuestoAnio;
    }

    public void setComboTipoPresupuestoAnio(SofisComboG<TipoPresupuestoAnio> comboTipoPresupuestoAnio) {
        this.comboTipoPresupuestoAnio = comboTipoPresupuestoAnio;
    }

    public RelacionGesPresEsAnioFiscal getRel() {
        return rel;
    }

    public void setRel(RelacionGesPresEsAnioFiscal rel) {
        this.rel = rel;
    }

    public Boolean getMostrarCampos() {
        return mostrarCampos;
    }

    public void setMostrarCampos(Boolean mostrarCampos) {
        this.mostrarCampos = mostrarCampos;
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

    public ComponentePresupuestoEscolar getSubComponenteSelected() {
        return subComponenteSelected;
    }

    public void setSubComponenteSelected(ComponentePresupuestoEscolar subComponenteSelected) {
        this.subComponenteSelected = subComponenteSelected;
    }

    public FuenteFinanciamiento getFuenteFinanciamientoSelected() {
        return fuenteFinanciamientoSelected;
    }

    public void setFuenteFinanciamientoSelected(FuenteFinanciamiento fuenteFinanciamientoSelected) {
        this.fuenteFinanciamientoSelected = fuenteFinanciamientoSelected;
    }

    public FuenteRecursos getFuenteRecursosSelected() {
        return fuenteRecursosSelected;
    }

    public void setFuenteRecursosSelected(FuenteRecursos fuenteRecursosSelected) {
        this.fuenteRecursosSelected = fuenteRecursosSelected;
    }

    public Cuentas getLineaPresupuestariaSelected() {
        return lineaPresupuestariaSelected;
    }

    public void setLineaPresupuestariaSelected(Cuentas lineaPresupuestariaSelected) {
        this.lineaPresupuestariaSelected = lineaPresupuestariaSelected;
    }

    public Cuentas getUnidadPresupuestariaSelected() {
        return unidadPresupuestariaSelected;
    }

    public void setUnidadPresupuestariaSelected(Cuentas unidadPresupuestariaSelected) {
        this.unidadPresupuestariaSelected = unidadPresupuestariaSelected;
    }

    
}
