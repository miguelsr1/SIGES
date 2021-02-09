package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.business.GestionPresupuestoEscolarBean;
import gob.mined.siap2.business.RelacionGesPresEsAnioFiscalBean;
import gob.mined.siap2.business.RelacionPresAnioFinanciamientoBean;
import gob.mined.siap2.business.ejbs.impl.CategoriaPresupuestoEscolarBean;
import gob.mined.siap2.business.ejbs.impl.CuentasBean;
import gob.mined.siap2.business.ejbs.impl.DireccionDepartamentalBean;
import gob.mined.siap2.business.ejbs.impl.GeneralBean;
import gob.mined.siap2.business.ejbs.impl.TopePresupuestalBean;
import gob.mined.siap2.business.ejbs.impl.TransferenciaDireccionDepartamentalBean;
import gob.mined.siap2.business.ejbs.impl.TransferenciasACedBean;
import gob.mined.siap2.business.ejbs.impl.TransferenciasBean;
import gob.mined.siap2.business.ejbs.impl.TransferenciasComponenteBean;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.CategoriaPresupuestoEscolar;
import gob.mined.siap2.entities.data.impl.ComponentePresupuestoEscolar;
import gob.mined.siap2.entities.data.impl.Cuentas;
import gob.mined.siap2.entities.data.impl.DireccionDepartamental;
import gob.mined.siap2.entities.data.impl.RelacionGesPresEsAnioFiscal;
import gob.mined.siap2.entities.data.impl.RelacionPresAnioFinanciamiento;
import gob.mined.siap2.entities.data.impl.TopePresupuestal;
import gob.mined.siap2.entities.data.impl.Transferencia;
import gob.mined.siap2.entities.data.impl.TransferenciaDireccionDepartamental;
import gob.mined.siap2.entities.data.impl.TransferenciaLite;
import gob.mined.siap2.entities.data.impl.TransferenciasACed;
import gob.mined.siap2.entities.data.impl.TransferenciasComponente;
import gob.mined.siap2.entities.enums.EstadoTopePresupuestal;
import gob.mined.siap2.entities.enums.EstadoTransferenciaACed;
import gob.mined.siap2.entities.enums.EstadoTransferenciaComponente;
import gob.mined.siap2.entities.enums.TipoPresupuestoAnio;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.filtros.FiltroCodiguera;
import gob.mined.siap2.filtros.FiltroComponentePresupuestoEscolar;
import gob.mined.siap2.filtros.FiltroRelPresAnioFinanciamiento;
import gob.mined.siap2.filtros.FiltroRelPresAnioFiscal;
import gob.mined.siap2.filtros.FiltroTopePresupuestal;
import gob.mined.siap2.filtros.FiltroTransferenciaComponente;
import gob.mined.siap2.filtros.FiltroTransferencias;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.genericos.constantes.ConstantesPresentacion;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.utils.SofisComboG;
import gob.mined.siap2.web.utils.dataProvider.EntityReferenceDataProvider;
import gob.mined.siap2.web.utils.dataProvider.GeneralLazyDataModel;
import gob.mined.siap2.web.utils.dataProvider.IDataProvider;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang.StringUtils;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;

@ViewScoped
@Named(value = "transferenciasMB")
public class TransferenciasMB implements Serializable{
    
    private static final Logger LOGGER = Logger.getLogger(ConstantesEstandares.LOGGER);
    
    private Transferencia transferencia;
    private Integer codigoSeleccion;
    private String nombreComplemento;
    private Integer numeroTransferencia;
    private boolean flagEliminar;
    private boolean flagCreate;
    private boolean flagNombreComplemento;
    private boolean flagRelacionFinanciamiento;
    
    private List<TransferenciasComponente> listaTC;
    private List<TransferenciaLite> listaTransferenciasHistorico;
    private List<TopePresupuestal> listaTopePresupuestal;
    private List<String> listaNombreComplemento;
    private List<RelacionPresAnioFinanciamiento> listaRelacionFinanciamiento;
    
    private SofisComboG<AnioFiscal> comboAnioFiscal;
   // private SofisComboG<CategoriaPresupuestoEscolar> comboComponente;
   // private SofisComboG<ComponentePresupuestoEscolar> comboSubcomponente;
    private SofisComboG<Cuentas> comboUnidadPresupuestaria;
    private SofisComboG<Cuentas> comboLineaPresupuestaria;
    private SofisComboG<EstadoTransferenciaComponente> comboEstado;
    private SofisComboG<TipoPresupuestoAnio> comboTipoPresupuestoAnio;
    //private SofisComboG<RelacionGesPresEsAnioFiscal> comboPresupuesto = new SofisComboG<>();
    @Inject
    private RelacionGesPresEsAnioFiscalBean relPresupuestoDao;
    protected LazyDataModel lazyModel;
    private HashMap<TransferenciaDireccionDepartamental, List<TransferenciasACed>> sedesPorTDD;
    
    @Inject
    private JSFUtils jSFUtils;
    @Inject
    private TransferenciasBean transferenciasBean;
    @Inject
    private EntityManagementDelegate emd;
    @Inject
    private CategoriaPresupuestoEscolarBean categoriaPresupuestoEscolarBean;
    @Inject
    private GestionPresupuestoEscolarBean gestionPresupuestoEscolarBean;
    @Inject 
    private CuentasBean cuentasBean;
    @Inject
    private GeneralBean generalBean;
    @Inject 
    private RelacionGesPresEsAnioFiscalBean relacionGesPresEsAnioFiscalBean;
    @Inject
    private RelacionPresAnioFinanciamientoBean relacionPresAnioFinanciamientoBean;
    @Inject
    private TransferenciasACedBean transferenciasACedBean;
    @Inject
    private TransferenciaDireccionDepartamentalBean transferenciaDireccionDepartamentalBean;
    @Inject
    private DireccionDepartamentalBean direccionDepartamentalBean;
    @Inject
    private TopePresupuestalBean topePresupuestalBean;
    @Inject
    private TransferenciasComponenteBean transferenciasComponenteBean;
    
    private CategoriaPresupuestoEscolar componenteSelected;
    private ComponentePresupuestoEscolar subComponenteSelected;
    private Cuentas lineaPresupuestariaSelected;
    private Cuentas unidadPresupuestariaSelected;
    private RelacionGesPresEsAnioFiscal presupuestoSelected;
    @PostConstruct
    public void init() {
        cargarCombos();
        presupuestoSelected = null;
        //this.comboPresupuesto = new SofisComboG();
        //this.comboPresupuesto.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
        cleanObject();
    }
    
    
    
    //******** CARGA ********//
    
    /**
     * Metodo utilizado para la carga de combos mostrados en el pantalla de transferencias
     */
    public void cargarCombos(){
        cargarAnioFiscal();
        cargarComponentes();
        cargarUnidadPresupuestaria();
        cargarEstadosTransferencias();
        cargarTipo();
    }
        
    /**
     * Metodo utilizado para la carga de combos mostrados en el pantalla de transferencias
     */
    public void cargarNombreYBuscarFuentes(){
       // cargarPresupuestosPorSubcomponente();
        cargarNombreComplemento();
        buscarRelacionFuentesFinanciamiento();
        
    }

    public void limpiarCampos(Integer opcion) {
        if(opcion!= null) {
            switch(opcion) {
                case 1 :
                   this.subComponenteSelected = null;
                   this.presupuestoSelected = null;
                   this.cargarUnidadPresupuestaria();
                   this.cargarNombreYBuscarFuentes();
                   break; // break es opcional

                case 2 :
                   this.presupuestoSelected = null;
                   this.cargarUnidadPresupuestaria();
                   this.cargarNombreYBuscarFuentes();
                   break; 
                case 3 :
                   this.presupuestoSelected = null;
                   this.cargarUnidadPresupuestaria();
                   this.cargarNombreYBuscarFuentes();
                   break;
                case 4 :
                   this.presupuestoSelected = null;
                   this.cargarUnidadPresupuestaria();
                   this.cargarNombreYBuscarFuentes();
                   break; 
                default : 
            }
        }
    }
    /**
     * Este método llena el objeto de Combo para los Componentes
     */
    public void cargarComponentes(){
        try {
            List<CategoriaPresupuestoEscolar> lista = categoriaPresupuestoEscolarBean.getAllCategoriaPresupuestoEscolarHabilitados();
            //this.comboComponente = new SofisComboG<>(lista, "nombre");
            //this.comboComponente.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
            
            //this.comboSubcomponente= new SofisComboG();
            //this.comboSubcomponente.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    
    /**
     * Este método llena el objeto de Combo para las Subcomponentes
     */
    public void cargarSubcomponentes(){
        try {
           /** if(comboComponente != null && comboComponente.getSelectedT() != null){
                List<ComponentePresupuestoEscolar> lista = gestionPresupuestoEscolarBean.getComponentesHabilitadosByCategoria(comboComponente.getSelectedT().getId());
                this.comboSubcomponente = new SofisComboG<>(lista, "nombre");
                this.comboSubcomponente.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
            }**/
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    
    /**
     * Este método llena el objeto de Combo para las Subcomponentes
     */
    public void cargarUnidadPresupuestaria(){
        try {
           /** if(comboAnioFiscal != null && comboAnioFiscal.getSelectedT() != null && comboPresupuesto.getSelectedT() != null){
                List<Cuentas> lista = cuentasBean.getCuentasByAnioFiscal(comboAnioFiscal.getSelectedT().getId());
                this.comboUnidadPresupuestaria = new SofisComboG<>(lista, "nombre");
                this.comboUnidadPresupuestaria.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
            }else{
                this.comboUnidadPresupuestaria = new SofisComboG();
                this.comboUnidadPresupuestaria.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
            }**/
           if(presupuestoSelected != null ){
                List<Cuentas> lista = new ArrayList();
               // lista.add(comboPresupuesto.getSelectedT().getSubCuenta().getCuentaPadre());
                Cuentas cuenta = presupuestoSelected.getSubCuenta().getCuentaPadre();
                if(cuenta != null && cuenta.getHabilitado() != null && cuenta.getHabilitado()) {
                    lista.add(cuenta);
                }
                this.comboUnidadPresupuestaria = new SofisComboG<>(lista, "nombre");
                this.comboUnidadPresupuestaria.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
            } else {
                this.comboUnidadPresupuestaria = new SofisComboG();
                this.comboUnidadPresupuestaria.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
            }
            this.comboLineaPresupuestaria = new SofisComboG();
            this.comboLineaPresupuestaria.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    
    /**
     * Este método llena el objeto de Combo para las Subcomponentes
     */
    public void cargarLineaPresupuestaria(){
        try {
            if(comboUnidadPresupuestaria!= null && comboUnidadPresupuestaria.getSelectedT() != null && presupuestoSelected != null){
                //List<Cuentas> lista = cuentasBean.getSubcuentasHabilitadasByIdPadre(comboUnidadPresupuestaria.getSelectedT().getId());
                List<Cuentas> lista = new ArrayList();
                Cuentas cuenta = presupuestoSelected.getSubCuenta();
                if(cuenta != null && cuenta.getHabilitado() != null && cuenta.getHabilitado()) {
                    lista.add(cuenta);
                }
                
                this.comboLineaPresupuestaria = new SofisComboG<>(lista, "nombre");
                this.comboLineaPresupuestaria.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
            }else{
                this.comboLineaPresupuestaria = new SofisComboG<>();
                this.comboLineaPresupuestaria.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    
    /**
     * Este método llena el objeto Combo para los aniosFiscales
     */
    public void cargarAnioFiscal(){
        try {
            List<AnioFiscal> lista = generalBean.getAniosFiscalesPlanificacion();
            this.comboAnioFiscal = new SofisComboG<>(lista, "nombre");
            this.comboAnioFiscal.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
            
            this.comboUnidadPresupuestaria = new SofisComboG();
            this.comboUnidadPresupuestaria.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
            
            this.comboLineaPresupuestaria = new SofisComboG();
            this.comboLineaPresupuestaria.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    
    public void cargarPresupuestosPorSubcomponente() {
        try {
            this.presupuestoSelected = null;
            if (this.subComponenteSelected != null && this.comboTipoPresupuestoAnio.getSelectedT() != null 
                    && this.comboAnioFiscal.getSelectedT() != null) {
                
                List<RelacionGesPresEsAnioFiscal> listado = relPresupuestoDao.getRelacionesAnioFiscalPorSubcomponente(subComponenteSelected.getId(), 
                                            comboTipoPresupuestoAnio.getSelectedT(),
                                            comboAnioFiscal.getSelectedT().getId());
                //comboPresupuesto = new SofisComboG<>(listado, "descripcion");
            } 
            //comboPresupuesto.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    
    /**
     * Este método llena el objeto Combo para los estados de Transferencias
     */
    public void cargarEstadosTransferencias(){
        try {
            List<EstadoTransferenciaComponente> lista = new ArrayList(Arrays.asList(EstadoTransferenciaComponente.values()));
            this.comboEstado = new SofisComboG<>(lista, "label");
            this.comboEstado.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    
    /**
     * Este método llena el objeto Combo para los estados de Transferencias
     */
    public void cargarTipo(){
        try {
            this.comboTipoPresupuestoAnio = new SofisComboG(Arrays.asList(TipoPresupuestoAnio.values()), "label");
            this.comboTipoPresupuestoAnio.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
            
            this.listaNombreComplemento = new ArrayList<String>();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    /**
     * Método utilizado para validar cuando se debe de mostrar el campo "NombreComplemento"
     */
    public void cargarNombreComplemento(){
        try {
            if(this.comboTipoPresupuestoAnio.getSelectedT() != null 
                    && this.comboTipoPresupuestoAnio.getSelectedT().equals(TipoPresupuestoAnio.COMPLEMENTO)
                    && this.comboAnioFiscal.getSelectedT() != null
                    && this.subComponenteSelected != null
                    && this.comboLineaPresupuestaria != null && this.comboLineaPresupuestaria.getSelectedT() != null
                    && this.presupuestoSelected != null){
                
                FiltroRelPresAnioFiscal filtro = new FiltroRelPresAnioFiscal();
                filtro.setId(presupuestoSelected.getId());
                filtro.setAnioFiscalId(this.comboAnioFiscal.getSelectedT().getId());
                filtro.setComponentePresupuestoEscolarId(this.subComponenteSelected.getId());
                filtro.setSubCuentaId(this.comboLineaPresupuestaria.getSelectedT().getId());
                filtro.setTipo(TipoPresupuestoAnio.COMPLEMENTO);
                filtro.setIncluirCampos(new String[]{"id","nombreComplemento"});
                
                List<RelacionGesPresEsAnioFiscal> listaRelacionAnioFiscal = relacionGesPresEsAnioFiscalBean.getComponentesPresupuestoEscolarByFiltro(filtro);
                if(listaRelacionAnioFiscal != null && !listaRelacionAnioFiscal.isEmpty()){
                    this.listaNombreComplemento = new ArrayList<String>();
                    for(RelacionGesPresEsAnioFiscal item : listaRelacionAnioFiscal){
                        this.listaNombreComplemento.add(item.getNombreComplemento());
                    }
                }
                this.flagNombreComplemento = true;
            }else{
                this.flagNombreComplemento = false;
                this.nombreComplemento = null;
            }
            
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
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
                filtro.setIncluirCampos(new String[]{"codigo","nombre","version"});
                return categoriaPresupuestoEscolarBean.obtenerComponentesPorFiltro(filtro);
            //}
            
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
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
                filtro.setMaxResults(10L);
                filtro.setIncluirCampos(new String[]{"id","nombre","version"});
                return gestionPresupuestoEscolarBean.getComponentesPresupuestoEscolarByFiltro(filtro);
            }
            
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;
    }
    
    public List<RelacionGesPresEsAnioFiscal> completePresupuestoAnio(String query){
        try {
            if(this.componenteSelected != null && this.comboAnioFiscal != null && this.comboAnioFiscal.getSelectedT() != null && subComponenteSelected != null) {
                FiltroRelPresAnioFiscal filtroPresupuesto = new FiltroRelPresAnioFiscal();
                filtroPresupuesto.setAnioFiscalId(this.comboAnioFiscal.getSelectedT().getId());
                filtroPresupuesto.setComponentePresupuestoEscolarId(subComponenteSelected.getId());
                filtroPresupuesto.setDescripcion(query);
                filtroPresupuesto.setTipo(this.comboTipoPresupuestoAnio.getSelectedT());
                return relacionGesPresEsAnioFiscalBean.getComponentesPresupuestoEscolarByFiltro(filtroPresupuesto);
            }
            
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
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
            LOGGER.log(Level.SEVERE, null, ex);
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
            LOGGER.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;
    }
    
    
    
    
    //******** LIMPIEZA ********//
    
    /**
     * Este método inicializa los objetos base del mantenimiento
     */
    public void cleanObject() {
        this.flagCreate = false;
        this.transferencia = new Transferencia();
        limpiarCombos();
        filterTable();
    }
    
    /**
     * Este método es utilizado para preparar los objetos de contexto para la creación de un registro
     */
    public void preCreate(){
        cleanObject();
        this.flagCreate = true;
        this.listaRelacionFinanciamiento = new ArrayList<RelacionPresAnioFinanciamiento>();
        this.flagRelacionFinanciamiento = false;
        this.nombreComplemento = null;
        this.codigoSeleccion = null;
        this.comboEstado.setSelectedT(EstadoTransferenciaComponente.EN_PROCESO);
    }
    
    /**
     * Método utilizado para limpiar la seleccion de combos realizada
     */
    public void limpiarCombos(){
        this.comboAnioFiscal.clearSelectedT();
        this.componenteSelected = null;
        this.subComponenteSelected = null;
        this.presupuestoSelected = null;
        this.lineaPresupuestariaSelected = null;
        this.unidadPresupuestariaSelected = null;
        //cargarComponentes();
        cargarUnidadPresupuestaria();
        this.comboEstado.clearSelectedT();
        this.comboTipoPresupuestoAnio.clearSelectedT();
    }

    
    
    
    
    //******** BUSQUEDA ********//
    
    /**
     * Este Método obtiene un registro Transferencia, filtrado por ID
     * @param id 
     */
    public void asignarEdicion(Integer id){
        try {
            this.codigoSeleccion = null;
            this.flagCreate = false;
            this.transferencia = transferenciasBean.getTransferenciaById(id);
            
            this.comboAnioFiscal.setSelectedT(this.transferencia.getAnioFiscal());
            this.comboEstado.setSelectedT(this.transferencia.getEstado());
            
            this.componenteSelected = this.transferencia.getSubcomponente().getCategoriaPresupuestoEscolar();
            //cargarSubcomponentes();
            this.subComponenteSelected = this.transferencia.getSubcomponente();
            this.comboTipoPresupuestoAnio.setSelectedT(this.transferencia.getRelacionGesPresEsAnioFiscal().getTipo());
            //cargarPresupuestosPorSubcomponente();
            
            this.presupuestoSelected = this.transferencia.getRelacionGesPresEsAnioFiscal();
            
            cargarUnidadPresupuestaria();
            this.comboUnidadPresupuestaria.setSelectedT(this.transferencia.getLineaPresupuestaria().getCuentaPadre());
            cargarLineaPresupuestaria();
            this.comboLineaPresupuestaria.setSelectedT(this.transferencia.getLineaPresupuestaria());
            
            
            cargarNombreComplemento();
            this.nombreComplemento = this.transferencia.getRelacionGesPresEsAnioFiscal().getNombreComplemento();
            
            buscarRelacionFuentesFinanciamiento();

            setCodigoSeleccion(this.transferencia.getPorcentaje() != null ? 1 : (this.transferencia.getImporteFijoCentro() != null ? 2 : 3));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
      
    /**
     * Este método corresponde al evento de consulta y obtiene el resultado
     * según el filtro aplicado
     */
    public void filterTable() {
        try {
            List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();
            
            if (this.comboAnioFiscal.getSelectedT() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO( MatchCriteriaTO.types.EQUALS, "anioFiscal.id", this.comboAnioFiscal.getSelectedT().getId());
                criterios.add(criterio);
            }
            if (this.componenteSelected != null) {
                 MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO( MatchCriteriaTO.types.EQUALS, "subcomponente.categoriaPresupuestoEscolar.id", this.componenteSelected.getId());
                 criterios.add(criterio);
            }
            if (this.subComponenteSelected != null) {
                 MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO( MatchCriteriaTO.types.EQUALS, "subcomponente.id", this.subComponenteSelected.getId());
                 criterios.add(criterio);
            }
            if (this.comboTipoPresupuestoAnio.getSelectedT() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "subcomponente.relacionGesPresEsAnioFiscals.tipo", this.comboTipoPresupuestoAnio.getSelectedT());
                criterios.add(criterio);
            }
            if (this.lineaPresupuestariaSelected != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO( MatchCriteriaTO.types.EQUALS, "lineaPresupuestaria.id", this.lineaPresupuestariaSelected.getId());
                criterios.add(criterio);
            }
            if (this.unidadPresupuestariaSelected != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO( MatchCriteriaTO.types.EQUALS, "lineaPresupuestaria.cuentaPadre.id", this.unidadPresupuestariaSelected.getId());
                criterios.add(criterio);
            }
            if (this.comboEstado.getSelectedT() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO( MatchCriteriaTO.types.EQUALS, "estado", this.comboEstado.getSelectedT());
                criterios.add(criterio);
            }
            
            if (this.comboTipoPresupuestoAnio.getSelectedT() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO( MatchCriteriaTO.types.EQUALS, "relacionGesPresEsAnioFiscal.tipo", this.comboTipoPresupuestoAnio.getSelectedT());
                criterios.add(criterio);
            }
            
            if (!TextUtils.isEmpty(this.nombreComplemento)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO( MatchCriteriaTO.types.EQUALS, "relacionGesPresEsAnioFiscal.nombreComplemento", this.nombreComplemento);
                criterios.add(criterio);
            }

            if(numeroTransferencia != null && numeroTransferencia.compareTo(0) > 0) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO( MatchCriteriaTO.types.EQUALS, "id", numeroTransferencia);
                criterios.add(criterio);
            }
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

            String[] propiedades = {
                "id", 
                "anioFiscal.anio", 
                "subcomponente.categoriaPresupuestoEscolar.nombre", 
                "subcomponente.nombre", 
                "porcentaje",
                "importeFijoCentro",
                "remanente",
                "estado",
                "lineaPresupuestaria.nombre",
                "lineaPresupuestaria.cuentaPadre.nombre",
                "relacionGesPresEsAnioFiscal.id",
                "relacionGesPresEsAnioFiscal.version",
                "relacionGesPresEsAnioFiscal.tipo",
                "relacionGesPresEsAnioFiscal.nombreComplemento"};
            String className = Transferencia.class.getName();
            String[] orderBy = {"id"};
            boolean[] asc = {true};

            IDataProvider dataProvider = new EntityReferenceDataProvider(propiedades, className, condicion, orderBy, asc);
            lazyModel = new GeneralLazyDataModel(dataProvider);
        } catch (GeneralException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }
    
    /**
     * Este método carga el listado historico del registro seleccionado
     * @param id 
     */
    public void cargarHistorico(Integer id) {
        this.listaTransferenciasHistorico = emd.obtenerHistorico(TransferenciaLite.class, id);
    }
    
    
    
    
    
    //******** PERSISTENCIA ********//
    
    /**
     * Metodo utilizado para guardar y/o actualizar un registro de Transferencias
     */
    public void guardarActualizar() {
        try {
            listaTC = new ArrayList();
            transferencia.setAnioFiscal(comboAnioFiscal.getSelectedT());
            transferencia.setSubcomponente(subComponenteSelected);
            transferencia.setLineaPresupuestaria(this.comboLineaPresupuestaria != null ? this.comboLineaPresupuestaria.getSelectedT() : null);
            transferencia.setEstado(comboEstado.getSelectedT());
            transferencia.setRelacionGesPresEsAnioFiscal(presupuestoSelected);
            if(!validarCampos()){
                return;
            }
            
           /** if(!validarOpcionesPorcentaje()){
                jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_OPCION_PORCENTAJE_NO_SELECCION);
                return;
            }
            if(!validarPorcentajeTransferencia()){
                return;
            }**/
            FiltroRelPresAnioFinanciamiento filtroFinanciamiento = new FiltroRelPresAnioFinanciamiento();
            filtroFinanciamiento.setRelAnioPresupuesto(transferencia.getRelacionGesPresEsAnioFiscal().getId());
            filtroFinanciamiento.setIncluirCampos(new String[]{"id","fuenteFinanciamiento.id", "fuenteRecursos.fuenteFinanciamiento.nombre","fuenteRecursos.codigo","fuenteRecursos.nombre","fuenteRecursos.id"});

            List<RelacionPresAnioFinanciamiento> listaFinanciamiento = relacionPresAnioFinanciamientoBean.getComponentesPresupuestoEscolarByFiltro(filtroFinanciamiento);
            if(listaFinanciamiento == null || listaFinanciamiento.isEmpty()){
                jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_PERSUPUESTO_NO_TIENE_REL_FINANCIAMIENTO);
                return;
            }

            TransferenciasComponente tc;
            for (RelacionPresAnioFinanciamiento ff : listaFinanciamiento) {
                List<TransferenciasComponente> resultado = new ArrayList();
                if(this.transferencia.getId() != null) {
                    FiltroTransferenciaComponente filtroTC = new FiltroTransferenciaComponente();
                    filtroTC.setIdAnioFiscal(this.transferencia.getAnioFiscal().getId());
                    filtroTC.setIdComponente(this.transferencia.getSubcomponente().getCategoriaPresupuestoEscolar().getId());
                    filtroTC.setIdSubcomponente(this.transferencia.getSubcomponente().getId());
                    filtroTC.setIdUnidadPresupuestaria(this.transferencia.getLineaPresupuestaria().getCuentaPadre().getId());
                    filtroTC.setIdLineaPresupuestaria(this.transferencia.getLineaPresupuestaria().getId());
                    filtroTC.setIdFuenteRecursos(ff.getFuenteRecursos().getId());
                    filtroTC.setIdTransferencia(this.transferencia.getId());
                    resultado = transferenciasComponenteBean.getTransferenciaComponenteByFiltro(filtroTC);
                }

                if(resultado != null && !resultado.isEmpty()){
                    for(TransferenciasComponente trans : resultado){
                        tc = trans;
                        tc.setPorcentaje(this.transferencia.getPorcentaje());
                        tc.setImporteFijoCentro(this.transferencia.getImporteFijoCentro());
                        tc.setRemanente(this.transferencia.getRemanente());
                        tc.setEstado(this.transferencia.getEstado());
                        listaTC.add(tc);
                    }
                }else{
                    tc = obtenerTransferenciaComponente(ff);
                    tc.setFuenteRecursos(ff.getFuenteRecursos());
                    tc.setPorcentaje(this.transferencia.getPorcentaje());
                    tc.setImporteFijoCentro(this.transferencia.getImporteFijoCentro());
                    tc.setRemanente(this.transferencia.getRemanente());
                    tc.setEstado(this.transferencia.getEstado());
                    listaTC.add(tc);
                }
            }
            this.transferencia = transferenciasBean.crearActualizarTransferencias(this.transferencia, listaTC);
            if(listaTC != null && !listaTC.isEmpty() && listaTC.size() > 0) {
                String recursos = "";
                Integer count = 0;
                for(TransferenciasComponente trc : listaTC) {
                    if(count == 0) {
                        recursos = trc.getFuenteRecursos().getNombre();
                    } else if(count == (listaTC.size() -1)) {
                        recursos += " y " +trc.getFuenteRecursos().getNombre();
                    } else {
                        recursos += ", " +trc.getFuenteRecursos().getNombre();
                    }
                    
                    count = count + 1;
                }
                jSFUtils.agregarInfo("Registro guardado correctamente. Se crearon/actualizaron los componentes de tranferencia con las siguientes fuentes de recursos." + recursos);
            } else {
                jSFUtils.agregarInfo("Registro guardado correctamente");
            }

            RequestContext.getCurrentInstance().execute("$('#editModal').modal('hide');");
            RequestContext.getCurrentInstance().update("formfiltro");
            //asignarEdicion(this.transferencia.getId());
        } catch (GeneralException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    
    /**
     * Metodo utilizado para eliminar un registro de Transferencias
     */
    public void eliminar() {
        try {
            if(!flagEliminar){
                transferenciasBean.eliminar(transferencia.getId());
            }else{
                jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_NO_POSIBLE_ELIMINAR);
            }
            cleanObject();
        } catch (GeneralException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } finally {
            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('hide');");
        }
    }
    
    
    
    
    
    //******** UTILIDADES ********//
    
    /**
     * Este método valida la opcion de porcentaje seleccionada
     * 
     * @return 
     */
    public boolean validarOpcionesPorcentaje(){
        if(getCodigoSeleccion() != null){
            if(null != codigoSeleccion){
                switch (codigoSeleccion) {
                    case 1:
                        getTransferencia().setImporteFijoCentro(null);
                        getTransferencia().setRemanente(false);
                        return true;
                    case 2:
                        getTransferencia().setPorcentaje(null);
                        getTransferencia().setRemanente(false);
                        return true;
                    case 3:
                        getTransferencia().setPorcentaje(null);
                        getTransferencia().setImporteFijoCentro(null);
                        getTransferencia().setRemanente(true);
                        return true;
                    default:
                        return false;
                }
            }
        }
        return false;
    }
    
    /**
     * Método utilizado para verificar el estado de un registro de TransferenciaComponente
     * 
     * @return 
     */
    public boolean habilitarGeneracionTransferencia(){
        return getTransferencia().getEstado() != null && getTransferencia().getEstado().compareTo(EstadoTransferenciaComponente.AUTORIZADA) == 0;
    }
 
    /**
     * Este metodo valida la seleccion de los campos necesarios para la creacion de una TransferenciaComponente
     * @return 
     */
    public boolean validarCamposGuardar(){
        if(validarCampos()){
            //obtenerRelacionPresupuestoAnioFiscal();
            if(this.transferencia.getRelacionGesPresEsAnioFiscal() == null) {
                jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_RELACION_PRESUPUESTO_ANIO_FISCAL_NO_ENCONTRADA);
                return false;
            }
        }else{
            return false;
        }
        return true;
    }
    
    /**
     * Este metodo valida la seleccion de los campos necesarios para la creacion de una TransferenciaComponente
     * @return 
     */
    public boolean validarCampos(){
        if(this.transferencia.getSubcomponente() == null){
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_SUBCOMPONENTE_NO_SELECCIONADO);
            return false;
        }else if (transferencia.getRelacionGesPresEsAnioFiscal() == null ) {
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_RELACION_PRESUPUESTO_ANIO_FISCAL_NO_ENCONTRADA);
            return false;
        }else if(this.transferencia.getLineaPresupuestaria() == null){
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_LINEA_PRESUPUESTARIA_NO_SELECCIONADA);
            return false;
        }else if(this.transferencia.getEstado() == null){
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_ESTADO_NO_SELECCIONADO);
            return false;
        }else if (this.transferencia.getAnioFiscal() == null) {
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_ANIO_FISCAL_NO_SELECCIONADO);
            return false;
        }else if (this.comboTipoPresupuestoAnio.getSelectedT() == null) {
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_TIPO_CLASIFICACION_PRESUPUESTO_NO_SELECCIONADO);
            return false;
        } /**else if (StringUtils.isBlank(this.transferencia.getDescripcion())) {
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_DESCRIPCION_VACIO);
            return false;
        }**/
        return true;
    }
    
    /**
     * Método utilizado para validar la sumatoria de porcentajes de una combinacion de tranferenciaComponente
     * @return 
     */
    public boolean validarPorcentajeTransferencia(){
        try {
            if(this.transferencia.getPorcentaje() != null){
                
                FiltroTransferencias filtro = new FiltroTransferencias();
                filtro.setIdSubcomponente(this.transferencia.getSubcomponente().getId());
                filtro.setIdLineaPresupuestaria(this.transferencia.getLineaPresupuestaria().getId());
                filtro.setIdAnioFiscal(this.transferencia.getAnioFiscal().getId());
                filtro.setIncluirCampos(new String[]{"porcentaje","id"});
                
                List<Transferencia> lista = transferenciasBean.getTransferenciaByFiltro(filtro);

                //SUMA TODOS LOS PORCENTAJES DE LA LISTA
                BigDecimal obj = BigDecimal.ZERO;
                if(lista != null && !lista.isEmpty()){
                    for(Transferencia item : lista){
                        obj = obj.add(item.getPorcentaje());
                    }
                }
            
                //SI SE ESTA INTENTANDO ACTUALIZAR UN REGISTROS, HACE EL CALCULO EN BASE AL NUEVO PORCENTAJE
                if(this.transferencia.getId() != null && this.transferencia.getPorcentaje() != null){
                    if(lista != null && !lista.isEmpty()){
                        for(Transferencia item : lista){
                            if(item.getId().equals(this.transferencia.getId())){
                                obj = obj.subtract(item.getPorcentaje());
                                obj = obj.add(this.transferencia.getPorcentaje());
                                break;
                            }
                        }
                    }
                }else if(this.transferencia.getId() == null && this.transferencia.getPorcentaje() != null){
                    obj = obj.add(this.transferencia.getPorcentaje());
                }
                
                //VALIDA LA SUMATORIA DE PORCENTAJES
                if(obj != null && !obj.equals(BigDecimal.ZERO)){
                    if(this.transferencia.getPorcentaje().equals(BigDecimal.ZERO)){
                        jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_PORCENTAJE_INGRESADO_NO_VALIDO);
                        return false;

                    }else if (obj.compareTo(new BigDecimal(100)) > 0){
                        jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_LIMITE_PORCENTAJES_ALCANZADO);
                        return false;
                    }
                }
            }
            return true;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$(window).scrollTop(0);");
        }
        return false;
    }
    
    /**
     * Método utilizado para verificar si el registro cuenta con relaciones en otras tablas
     * 
     * @param id 
     */
    public void verificarEliminar(Integer id){
        try {
            flagEliminar = false;
            transferencia = transferenciasBean.getTransferenciaById(id);
            
            FiltroTransferenciaComponente filtroTC = new FiltroTransferenciaComponente();
            filtroTC.setIdTransferencia(id);
            filtroTC.setIncluirCampos(new String[]{"id"});
            List<TransferenciasComponente> listaTC = transferenciasComponenteBean.getTransferenciaComponenteByFiltro(filtroTC);
            if(listaTC != null && !listaTC.isEmpty()){
                flagEliminar = true;
                jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_NO_ELIMINAR_REGISTROS_TRANSFERENCIA_COMPONENTE);
            }
            RequestContext.getCurrentInstance().update("formEliminar");
            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('show');");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
   
    /**
     * Método utilizado para la generacion de registro de PresupuestoAnioFiscal
     * @return 
     */
    public RelacionGesPresEsAnioFiscal obtenerRelacionPresupuestoAnioFiscal(){
        try {
            FiltroRelPresAnioFiscal filtroPresupuesto = new FiltroRelPresAnioFiscal();
            filtroPresupuesto.setAnioFiscalId(this.comboAnioFiscal.getSelectedT().getId());
            filtroPresupuesto.setComponentePresupuestoEscolarId(subComponenteSelected.getId());
            filtroPresupuesto.setSubCuentaId(this.lineaPresupuestariaSelected.getId());
            filtroPresupuesto.setTipo(this.comboTipoPresupuestoAnio.getSelectedT());
            
            if(this.nombreComplemento != null && !this.nombreComplemento.isEmpty()){
                filtroPresupuesto.setNombreComplemento(this.nombreComplemento);
            }
            List<RelacionGesPresEsAnioFiscal> listaPresupuesto = relacionGesPresEsAnioFiscalBean.getComponentesPresupuestoEscolarByFiltro(filtroPresupuesto);
            if(listaPresupuesto != null && !listaPresupuesto.isEmpty()){
                this.transferencia.setRelacionGesPresEsAnioFiscal(listaPresupuesto.get(0));
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;
    }
    
    /**
     * Método utilizado para buscar un registro de Fuente de Financiamiento según selección de combos
     */
    public void buscarRelacionFuentesFinanciamiento() {
        try {
            if (this.comboAnioFiscal.getSelectedT() != null
                    && this.subComponenteSelected != null
                    && this.comboLineaPresupuestaria != null && this.comboLineaPresupuestaria.getSelectedT() != null
                    && this.comboTipoPresupuestoAnio.getSelectedT() != null 
                    && (this.comboTipoPresupuestoAnio.getSelectedT().equals(TipoPresupuestoAnio.BASE)
                        || (this.comboTipoPresupuestoAnio.getSelectedT().equals(TipoPresupuestoAnio.COMPLEMENTO)
                            && this.nombreComplemento != null 
                            && !this.nombreComplemento.isEmpty()))
                    && this.presupuestoSelected != null) {
                
                 
                FiltroRelPresAnioFinanciamiento filtro = new FiltroRelPresAnioFinanciamiento();
                filtro.setIdAnioFiscal(this.comboAnioFiscal.getSelectedT().getId());
                filtro.setIdSubcomponente(this.subComponenteSelected.getId());
                filtro.setIdLineaPresupuestaria(this.comboLineaPresupuestaria.getSelectedT().getId());
                filtro.setTipoPresupuestoAnio(this.comboTipoPresupuestoAnio.getSelectedT());
                filtro.setRelAnioPresupuesto(this.presupuestoSelected.getId());
                if (this.nombreComplemento != null && !this.nombreComplemento.isEmpty()) {
                    filtro.setNombreComplemento(this.nombreComplemento);
                }

                filtro.setIncluirCampos(new String[]{"id", "fuenteFinanciamiento.nombre", "fuenteRecursos.nombre"});
                listaRelacionFinanciamiento = relacionPresAnioFinanciamientoBean.getComponentesPresupuestoEscolarByFiltro(filtro);
                flagRelacionFinanciamiento = true;
            }else{
                listaRelacionFinanciamiento = new ArrayList<RelacionPresAnioFinanciamiento>();
                flagRelacionFinanciamiento = false;
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    
    
    
    
    
    //******** GENERAR TRANSFERENCIAS A COMPONENTES ********//
    
    /**
     * Metodo utilizado para generar registros de TransferenciaComponente a partir de la relacion entre PresupuestoComponente y FuenteFinanciamiento
     */
    public void generarTransferenciasComponente(){
        try {
            listaTC = new ArrayList<TransferenciasComponente>();
            //this.transferencia = transferenciasBean.getTransferenciaById(transferencia.getId());
            
            FiltroRelPresAnioFiscal filtroPresupuesto = new FiltroRelPresAnioFiscal();
            filtroPresupuesto.setAnioFiscalId(this.transferencia.getAnioFiscal().getId());
            filtroPresupuesto.setComponentePresupuestoEscolarId(this.transferencia.getSubcomponente().getId());
            filtroPresupuesto.setSubCuentaId(this.transferencia.getLineaPresupuestaria().getId());
            filtroPresupuesto.setTipo(this.transferencia.getRelacionGesPresEsAnioFiscal().getTipo());
            
            if(this.transferencia.getRelacionGesPresEsAnioFiscal().getNombreComplemento() != null && !this.transferencia.getRelacionGesPresEsAnioFiscal().getNombreComplemento().isEmpty()){
                filtroPresupuesto.setNombreComplemento(this.transferencia.getRelacionGesPresEsAnioFiscal().getNombreComplemento());
            }
            filtroPresupuesto.setIncluirCampos(new String[]{"id"});
            
            List<RelacionGesPresEsAnioFiscal> listaPresupuesto = relacionGesPresEsAnioFiscalBean.getComponentesPresupuestoEscolarByFiltro(filtroPresupuesto);
            if(listaPresupuesto == null || listaPresupuesto.isEmpty()){
                jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_NO_EXISTE_REL_COMPONENTE_ANIO_FISCAL);
                return;
            }

            FiltroRelPresAnioFinanciamiento filtroFinanciamiento;
            for(RelacionGesPresEsAnioFiscal presupuesto : listaPresupuesto){
                filtroFinanciamiento = new FiltroRelPresAnioFinanciamiento();
                filtroFinanciamiento.setRelAnioPresupuesto(presupuesto.getId());
                filtroFinanciamiento.setIncluirCampos(new String[]{"id","fuenteFinanciamiento.id", "fuenteRecursos.fuenteFinanciamiento.nombre","fuenteRecursos.nombre","fuenteRecursos.id"});
                
                List<RelacionPresAnioFinanciamiento> listaFinanciamiento = relacionPresAnioFinanciamientoBean.getComponentesPresupuestoEscolarByFiltro(filtroFinanciamiento);
                if(listaFinanciamiento == null || listaFinanciamiento.isEmpty()){
                    jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_PERSUPUESTO_NO_TIENE_REL_FINANCIAMIENTO);
                    return;
                }
                
                TransferenciasComponente tc;
                for (RelacionPresAnioFinanciamiento ff : listaFinanciamiento) {
                    
                    FiltroTransferenciaComponente filtroTC = new FiltroTransferenciaComponente();
                    filtroTC.setIdAnioFiscal(this.transferencia.getAnioFiscal().getId());
                    filtroTC.setIdComponente(this.transferencia.getSubcomponente().getCategoriaPresupuestoEscolar().getId());
                    filtroTC.setIdSubcomponente(this.transferencia.getSubcomponente().getId());
                    filtroTC.setIdUnidadPresupuestaria(this.transferencia.getLineaPresupuestaria().getCuentaPadre().getId());
                    filtroTC.setIdLineaPresupuestaria(this.transferencia.getLineaPresupuestaria().getId());
                    filtroTC.setIdFuenteRecursos(ff.getFuenteRecursos().getId());
                    filtroTC.setIdTransferencia(this.transferencia.getId());
                    
                    List<TransferenciasComponente> resultado = transferenciasComponenteBean.getTransferenciaComponenteByFiltro(filtroTC);
                    if(resultado != null && !resultado.isEmpty()){
                        for(TransferenciasComponente trans : resultado){
                            tc = trans;
                            tc.setPorcentaje(this.transferencia.getPorcentaje());
                            tc.setImporteFijoCentro(this.transferencia.getImporteFijoCentro());
                            tc.setRemanente(this.transferencia.getRemanente());
                            tc.setEstado(this.transferencia.getEstado());
                            listaTC.add(tc);
                        }
                    }else{
                        tc = obtenerTransferenciaComponente(ff);
                        tc.setFuenteRecursos(ff.getFuenteRecursos());
                        tc.setPorcentaje(this.transferencia.getPorcentaje());
                        tc.setImporteFijoCentro(this.transferencia.getImporteFijoCentro());
                        tc.setRemanente(this.transferencia.getRemanente());
                        tc.setEstado(this.transferencia.getEstado());
                        listaTC.add(tc);
                    }
                }
            }
            RequestContext.getCurrentInstance().execute("$('#editModal').modal('hide');");
            RequestContext.getCurrentInstance().execute("$('#tranferenciasComponente').modal('show');");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    
    /**
     * Metodo utilizado para crear o actualizar registros de transferencias a
     * sedes por TransferenciaDireccionDepartamental
     */
    public void guardarActualizarTransferenciasComponente() {
        try {
            transferenciasComponenteBean.crearActualizar(listaTC);
            jSFUtils.agregarInfo("Registros generados correctamente");
            RequestContext.getCurrentInstance().execute("$('#tranferenciasComponente').modal('hide');");
            RequestContext.getCurrentInstance().update("formfiltro");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    
    /**
     * Método utilizado para crear un objeto de TransferenciaComponente y se alimenta con datos de Transferencia
     * 
     * @param ff
     * @return 
     */
    public TransferenciasComponente obtenerTransferenciaComponente(RelacionPresAnioFinanciamiento ff){
        TransferenciasComponente tc = new TransferenciasComponente();
        try {
            tc.setAnioFiscal(this.transferencia.getAnioFiscal());
            tc.setComponente(this.transferencia.getSubcomponente().getCategoriaPresupuestoEscolar());
            tc.setSubcomponente(this.transferencia.getSubcomponente());
            tc.setUnidadPresupuestaria(this.transferencia.getLineaPresupuestaria().getCuentaPadre());
            tc.setLineaPresupuestaria(this.transferencia.getLineaPresupuestaria());
            tc.setFuenteRecursos(ff.getFuenteRecursos());
            tc.setTransferencia(this.transferencia);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return tc;
    }
      
    
    
    
    
    //******** GENERAR TRANSFERENCIAS A CENTROS EDUCATIVOS ********//
    
    /**
     * Este método es utilizado para generar registros de TransferenciaACed y TransferenciaDireccionDepartamental segun la actualización de estado
     * de un registro de TransferenciaComponente
     * @param transferenciasComponente
     */
    public void generarTransferenciasCentrosEducativos(TransferenciasComponente transferenciasComponente){
        try {
            BigDecimal montoTotal = BigDecimal.ZERO;
            
            List<TopePresupuestal> listaTopes = buscarListaTopes(transferenciasComponente);
            if(listaTopes == null || listaTopes.isEmpty()){
                jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_TECHOS_NO_ENCONTRADOS);
                return;
            }
            
            //Se clasifican los topes por TDD
            HashMap<TransferenciaDireccionDepartamental, List<TopePresupuestal>> topesPorDepartamento = new HashMap<TransferenciaDireccionDepartamental, List<TopePresupuestal>>();
            for(TopePresupuestal tope : listaTopes){
                DireccionDepartamental ddp = direccionDepartamentalBean.getDireccionDepartamentalByIdDepartamento(tope.getSede().getDireccion().getDepartamento().getPk());
                TransferenciaDireccionDepartamental tdd = transferenciaDireccionDepartamentalBean.getTransferenciasDirDepaByTransfCompYDirDepa(transferenciasComponente.getId(), ddp.getPk());
                if(tdd == null){
                    tdd = nuevoTDD(ddp, transferenciasComponente);
                }
                if(topesPorDepartamento.containsKey(tdd)){
                    topesPorDepartamento.get(tdd).add(tope);
                }else{
                    topesPorDepartamento.put(tdd, new ArrayList<TopePresupuestal>());
                    topesPorDepartamento.get(tdd).add(tope);
                }
            }
            
            sedesPorTDD = new HashMap<TransferenciaDireccionDepartamental, List<TransferenciasACed>>();
            for(Entry<TransferenciaDireccionDepartamental, List<TopePresupuestal>> item : topesPorDepartamento.entrySet()){
                TransferenciasACed aced = null;
                for(TopePresupuestal tope: item.getValue()){
                    aced = transferenciasACedBean.getTransferenciaACedByComponenteSedeTDD(transferenciasComponente.getId(), tope.getSede().getId(), item.getKey().getPk());
                    if(aced == null){
                        aced = crearNuevaTransferenciaACed(tope, transferenciasComponente);
                    }
                    montoTotal = montoTotal.add(aced.getMontoAutorizado());
                    if(sedesPorTDD.containsKey(item.getKey())){
                        sedesPorTDD.get(item.getKey()).add(aced);
                    }else{
                        sedesPorTDD.put(item.getKey(), new ArrayList<TransferenciasACed>());
                        sedesPorTDD.get(item.getKey()).add(aced);
                    }
                }
                item.getKey().setMontoAutorizado(montoTotal);
            }
            RequestContext.getCurrentInstance().execute("$('#editModal').modal('hide');");
            RequestContext.getCurrentInstance().execute("$('#tranferenciasACed').modal('show');");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    
    /**
     * Metodo utilizado para crear o actualizar registros de transferencias a sedes por TransferenciaDireccionDepartamental
     */
    public void guardarActualizarSedesPorTDD(){
        try {
            HashMap<String, Integer> resultados = transferenciasACedBean.crearActualizarTransferenciasDireccionesDepartamental(sedesPorTDD);
            mostrarMensajesResultados(resultados);
            RequestContext.getCurrentInstance().execute("$('#tranferenciasACed').modal('hide');");
            RequestContext.getCurrentInstance().update("formfiltro");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    
    /**
     * Metodo utilizado para crear un nuevo registro de TransferenciaDireccionDepartamental
     * @param ddp
     * @param transferenciasComponente
     * @return 
     */
    public TransferenciaDireccionDepartamental nuevoTDD(DireccionDepartamental ddp, TransferenciasComponente transferenciasComponente){
        TransferenciaDireccionDepartamental tdd = new TransferenciaDireccionDepartamental();
        tdd.setDireccionDep(ddp);
        tdd.setTransferenciasComponente(transferenciasComponente);
        tdd.setEstado(EstadoTransferenciaACed.TRANSFERIDO);
        return tdd;
    }
    
    /**
     * Metodo utilizado para buscar una lista de TechoPresupuestal filtrada
     * 
     * @param transferenciasComponente
     * @return 
     */
    public List<TopePresupuestal> buscarListaTopes(TransferenciasComponente transferenciasComponente){
        try {
            FiltroTopePresupuestal filtro = new FiltroTopePresupuestal();
            filtro.setComponentePresupuestoEscolarId(transferenciasComponente.getSubcomponente().getId());
            filtro.setCuentaId(transferenciasComponente.getLineaPresupuestaria().getId());
            filtro.setEstado(EstadoTopePresupuestal.APROBACION);
            filtro.setIncluirCampos(new String[]{"sede.id","sede.nombre", "montoAprobado","sede.direccion.departamento.pk", "sede.direccion.departamento.nombre"});
            return topePresupuestalBean.getTopesPresupuestalesByFiltro(filtro);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;
    }
    
    /**
     * Metodo utilizado para evluar resultados de proceso de generacion de Transferencias a sedes y direcciones departamentales
     * 
     * @param resultados 
     */
    public void mostrarMensajesResultados(HashMap<String, Integer> resultados){
        if(resultados != null){
            if(resultados.get("tddCreado") != null && resultados.get("tddCreado") > 0){
                jSFUtils.mostrarMensaje("Se ha creado un registro de transferencias a dirección departamental", FacesMessage.SEVERITY_INFO);
            }
            if(resultados.get("tddActualizado") != null && resultados.get("tddActualizado") > 0){
                jSFUtils.mostrarMensaje("Se ha actualizado un registro de transferencias a dirección departamental", FacesMessage.SEVERITY_INFO);
            }
            if(resultados.get("acedCreado") != null && resultados.get("acedCreado") > 0){
                jSFUtils.mostrarMensaje("Numero de transferencias a sedes creadas:"+resultados.get("acedCreado"), FacesMessage.SEVERITY_INFO);
            }
            if(resultados.get("acedActualizado") != null && resultados.get("acedActualizado") > 0){
                jSFUtils.mostrarMensaje("Numero de transferencias a sedes actualizadas:"+resultados.get("acedActualizado"), FacesMessage.SEVERITY_INFO);
            }
        }else{
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_GENERACION_RESULTADOS);
        }
    }
    
    /**
     * Método utilizado para obtener el porcentaje del monto aprobado de un topePresupuestal, según la transferenciaComponente
     * 
     * @param tope
     * @param transferenciasComponente
     * @return 
     */
    public BigDecimal obtenerPorcentajeSede(TopePresupuestal tope, TransferenciasComponente transferenciasComponente){
        BigDecimal monto = null;
        try {
            if(tope.getMontoAprobado() != null && tope.getMontoAprobado().compareTo(BigDecimal.ZERO) > 0){
                if(transferenciasComponente.getPorcentaje()!= null && transferenciasComponente.getPorcentaje().compareTo(BigDecimal.ZERO) > 0){
                    monto = tope.getMontoAprobado().multiply(transferenciasComponente.getPorcentaje().divide(new BigDecimal(100)));
                }else{
                    jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_PORCENTAJE_INGRESADO_NO_VALIDO);
                }
            }else{
                jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_MONTO_APROBADO_NO_VALIDO);
            }
            if(tope.getSede() == null){
                jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_CENTRO_EDUCATIVO_NO_ENCONTRADO);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return monto;
    }
    
    /**
     * Método utilizado para crear un nuevo objeto de TransferenciaACed
     * 
     * @param tope
     * @param transferenciasComponente
     * @return 
     */
    public TransferenciasACed crearNuevaTransferenciaACed(TopePresupuestal tope, TransferenciasComponente transferenciasComponente) {
        TransferenciasACed aCed = new TransferenciasACed();
        aCed.setCed(tope.getSede());
        aCed.setEstado(EstadoTransferenciaACed.AUTORIZADO);
        aCed.setTransferencia(transferenciasComponente);
        aCed.setMontoAutorizado(obtenerPorcentajeSede(tope, transferenciasComponente));
        return aCed;
    }

    public List<TransferenciaLite> getListaTransferenciasHistorico() {
        return listaTransferenciasHistorico;
    }

    public void setListaTransferenciasHistorico(List<TransferenciaLite> listaTransferenciasHistorico) {
        this.listaTransferenciasHistorico = listaTransferenciasHistorico;
    }

    public Transferencia getTransferencia() {
        return transferencia;
    }

    public void setTransferencia(Transferencia transferencia) {
        this.transferencia = transferencia;
    }

    public List<TopePresupuestal> getListaTopePresupuestal() {
        return listaTopePresupuestal;
    }

    public void setListaTopePresupuestal(List<TopePresupuestal> listaTopePresupuestal) {
        this.listaTopePresupuestal = listaTopePresupuestal;
    }

    public LazyDataModel getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel lazyModel) {
        this.lazyModel = lazyModel;
    }

    public SofisComboG<AnioFiscal> getComboAnioFiscal() {
        return comboAnioFiscal;
    }

    public void setComboAnioFiscal(SofisComboG<AnioFiscal> comboAnioFiscal) {
        this.comboAnioFiscal = comboAnioFiscal;
    }

    public SofisComboG<EstadoTransferenciaComponente> getComboEstado() {
        return comboEstado;
    }

    public void setComboEstado(SofisComboG<EstadoTransferenciaComponente> comboEstado) {
        this.comboEstado = comboEstado;
    }

    public Integer getCodigoSeleccion() {
        return codigoSeleccion;
    }

    public void setCodigoSeleccion(Integer codigoSeleccion) {
        this.codigoSeleccion = codigoSeleccion;
    }

    public HashMap<TransferenciaDireccionDepartamental, List<TransferenciasACed>> getSedesPorTDD() {
        return sedesPorTDD;
    }

    public void setSedesPorTDD(HashMap<TransferenciaDireccionDepartamental, List<TransferenciasACed>> sedesPorTDD) {
        this.sedesPorTDD = sedesPorTDD;
    }

    public boolean isFlagEliminar() {
        return flagEliminar;
    }

    public void setFlagEliminar(boolean flagEliminar) {
        this.flagEliminar = flagEliminar;
    }

    public List<TransferenciasComponente> getListaTC() {
        return listaTC;
    }

    public void setListaTC(List<TransferenciasComponente> listaTC) {
        this.listaTC = listaTC;
    }

    public boolean isFlagCreate() {
        return flagCreate;
    }

    public void setFlagCreate(boolean flagCreate) {
        this.flagCreate = flagCreate;
    }

    public SofisComboG<TipoPresupuestoAnio> getComboTipoPresupuestoAnio() {
        return comboTipoPresupuestoAnio;
    }

    public void setComboTipoPresupuestoAnio(SofisComboG<TipoPresupuestoAnio> comboTipoPresupuestoAnio) {
        this.comboTipoPresupuestoAnio = comboTipoPresupuestoAnio;
    }

    public boolean isFlagNombreComplemento() {
        return flagNombreComplemento;
    }

    public void setFlagNombreComplemento(boolean flagNombreComplemento) {
        this.flagNombreComplemento = flagNombreComplemento;
    }

    public String getNombreComplemento() {
        return nombreComplemento;
    }

    public void setNombreComplemento(String nombreComplemento) {
        this.nombreComplemento = nombreComplemento;
    }

    public List<String> getListaNombreComplemento() {
        return listaNombreComplemento;
    }

    public void setListaNombreComplemento(List<String> listaNombreComplemento) {
        this.listaNombreComplemento = listaNombreComplemento;
    }

    public List<RelacionPresAnioFinanciamiento> getListaRelacionFinanciamiento() {
        return listaRelacionFinanciamiento;
    }

    public void setListaRelacionFinanciamiento(List<RelacionPresAnioFinanciamiento> listaRelacionFinanciamiento) {
        this.listaRelacionFinanciamiento = listaRelacionFinanciamiento;
    }

    public boolean isFlagRelacionFinanciamiento() {
        return flagRelacionFinanciamiento;
    }

    public void setFlagRelacionFinanciamiento(boolean flagRelacionFinanciamiento) {
        this.flagRelacionFinanciamiento = flagRelacionFinanciamiento;
    }

    public Integer getNumeroTransferencia() {
        return numeroTransferencia;
    }

    public void setNumeroTransferencia(Integer numeroTransferencia) {
        this.numeroTransferencia = numeroTransferencia;
    }   

    public CategoriaPresupuestoEscolar getComponenteSelected() {
        return componenteSelected;
    }

    public void setComponenteSelected(CategoriaPresupuestoEscolar componenteSelected) {
        this.componenteSelected = componenteSelected;
    }

    public ComponentePresupuestoEscolar getSubComponenteSelected() {
        return subComponenteSelected;
    }

    public void setSubComponenteSelected(ComponentePresupuestoEscolar subComponenteSelected) {
        this.subComponenteSelected = subComponenteSelected;
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

    public SofisComboG<Cuentas> getComboUnidadPresupuestaria() {
        return comboUnidadPresupuestaria;
    }

    public void setComboUnidadPresupuestaria(SofisComboG<Cuentas> comboUnidadPresupuestaria) {
        this.comboUnidadPresupuestaria = comboUnidadPresupuestaria;
    }

    public SofisComboG<Cuentas> getComboLineaPresupuestaria() {
        return comboLineaPresupuestaria;
    }

    public void setComboLineaPresupuestaria(SofisComboG<Cuentas> comboLineaPresupuestaria) {
        this.comboLineaPresupuestaria = comboLineaPresupuestaria;
    }

    public RelacionGesPresEsAnioFiscal getPresupuestoSelected() {
        return presupuestoSelected;
    }

    public void setPresupuestoSelected(RelacionGesPresEsAnioFiscal presupuestoSelected) {
        this.presupuestoSelected = presupuestoSelected;
    }
    
}
