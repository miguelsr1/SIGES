package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.business.GestionPresupuestoEscolarBean;
import gob.mined.siap2.business.RelacionPresAnioFinanciamientoBean;
import gob.mined.siap2.business.ejbs.impl.CategoriaPresupuestoEscolarBean;
import gob.mined.siap2.business.ejbs.impl.CuentasBean;
import gob.mined.siap2.business.ejbs.impl.GeneralBean;
import gob.mined.siap2.business.ejbs.impl.TopePresupuestalBean;
import gob.mined.siap2.business.ejbs.impl.DireccionDepartamentalBean;
import gob.mined.siap2.business.ejbs.impl.SedeBean;
import gob.mined.siap2.business.ejbs.impl.SgBeneficiariosBean;
import gob.mined.siap2.business.ejbs.impl.TransferenciaDireccionDepartamentalBean;
import gob.mined.siap2.business.ejbs.impl.TransferenciasACedBean;
import gob.mined.siap2.business.ejbs.impl.TransferenciasComponenteBean;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.Beneficiarios;
import gob.mined.siap2.entities.data.impl.CategoriaPresupuestoEscolar;
import gob.mined.siap2.entities.data.impl.ComponentePresupuestoEscolar;
import gob.mined.siap2.entities.data.impl.Cuentas;
import gob.mined.siap2.entities.data.impl.DireccionDepartamental;
import gob.mined.siap2.entities.data.impl.FuenteFinanciamiento;
import gob.mined.siap2.entities.data.impl.FuenteRecursos;
import gob.mined.siap2.entities.data.impl.RelacionPresAnioFinanciamiento;
import gob.mined.siap2.entities.data.impl.TopePresupuestal;
import gob.mined.siap2.entities.data.impl.TransferenciaDireccionDepartamental;
import gob.mined.siap2.entities.data.impl.TransferenciasACed;
import gob.mined.siap2.entities.data.impl.TransferenciasComponente;
import gob.mined.siap2.entities.data.impl.TransferenciasComponenteLite;
import gob.mined.siap2.entities.enums.EstadoTopePresupuestal;
import gob.mined.siap2.entities.enums.EstadoTransferenciaACed;
import gob.mined.siap2.entities.enums.EstadoTransferenciaComponente;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.filtros.FiltroCodiguera;
import gob.mined.siap2.filtros.FiltroComponentePresupuestoEscolar;
import gob.mined.siap2.filtros.FiltroRelPresAnioFinanciamiento;
import gob.mined.siap2.filtros.FiltroTopePresupuestal;
import gob.mined.siap2.filtros.FiltroTransferenciaComponente;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
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
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;

@ViewScoped
@Named(value = "transferenciasComponenteMB")
public class TransferenciasComponenteMB implements Serializable{
    
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
    
    private Integer codigoSeleccion;
    
    private TransferenciasComponente transferenciasComponente;
    private List<TransferenciasComponenteLite> listaHistoricoTransferenciasComponente;
    private HashMap<TransferenciaDireccionDepartamental, List<TransferenciasACed>> sedesPorTDD;
    
    private LazyDataModel lazyModel;
    
    private SofisComboG<CategoriaPresupuestoEscolar> comboComponente;
    private SofisComboG<ComponentePresupuestoEscolar> comboSubcomponente;
    private SofisComboG<Cuentas> comboUnidadPresupuestaria;
    private SofisComboG<Cuentas> comboLineaPresupuestaria;
    private SofisComboG<AnioFiscal> comboAnioFiscal;
    private SofisComboG<EstadoTransferenciaComponente> comboEstado;
    private SofisComboG<FuenteFinanciamiento> comboFuenteFinanciamiento;
    private SofisComboG<FuenteRecursos> comboFuenteRecursos;
    
    
    @Inject
    private TransferenciasComponenteBean transferenciasComponenteBean;
    @Inject
    private CategoriaPresupuestoEscolarBean categoriaBean;
    @Inject
    private GestionPresupuestoEscolarBean escolarBean;
    @Inject 
    private CuentasBean cuentasBean;
    @Inject
    private GeneralBean generalBean;
    @Inject
    private EntityManagementDelegate emd;
    @Inject
    private JSFUtils jSFUtils;
    @Inject
    private TopePresupuestalBean presupuestalBean;
    @Inject
    private DireccionDepartamentalBean direccionDepartamentalBean;
    @Inject
    private TransferenciasACedBean transferenciasACedBean;
    @Inject
    private TransferenciaDireccionDepartamentalBean transferenciaDireccionDepartamentalBean;
    @Inject
    private SedeBean sedeBean;
    @Inject
    private RelacionPresAnioFinanciamientoBean relPresAnioFinanciamientoBean;
    @Inject
    private SgBeneficiariosBean beneficiariosBean;
    
    private CategoriaPresupuestoEscolar componenteSelected;
    private ComponentePresupuestoEscolar subComponenteSelected;
    private Cuentas lineaPresupuestariaSelected;
    private Cuentas unidadPresupuestariaSelected;
    
    @PostConstruct
    public void init(){
        cargarCombos();
        cleanObjects();
    }
    
    
    
    
    
    
    /**
     * Este método limpia los objetos de filtrado y creacion, y reinicia la tabla de resultados
     */
    public void cleanObjects(){
        this.transferenciasComponente = new TransferenciasComponente();
        this.codigoSeleccion = null;
        limpiarSeleccionCombos();
        filterTable();
    }
    
    /**
     * Metodo que limpia los objetos utilizados para 
     */
    public void limpiarSeleccionCombos(){
        comboAnioFiscal.clearSelectedT();
        comboUnidadPresupuestaria.clearSelectedT();
        comboLineaPresupuestaria.clearSelectedT();
        comboComponente.clearSelectedT();
        comboSubcomponente.clearSelectedT();
        comboEstado.clearSelectedT();
        comboFuenteFinanciamiento.clearSelectedT();
        comboFuenteRecursos.clearSelectedT();
    }

    
    
    
    
    
    
    //******** METODOS DE CARGA DE COMBOS ********
    
    /**
     * Metodo principal para la carga de listas de recursos para combos de filtros y creacion
     */
    public void cargarCombos(){
        cargarAnioFiscal();
        cargarComponentes();
        cargarEstadosTransferencias();
        cargarFuenteFinanciamiento();
    }
    
    /**
     * Este método llena el objeto de Combo para los Componentes
     */
    public void cargarComponentes(){
        try {
            List<CategoriaPresupuestoEscolar> lista = categoriaBean.getAllCategoriaPresupuestoEscolarHabilitados();
            this.comboComponente = new SofisComboG<>(lista, "nombre");
            this.comboComponente.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
            
            this.comboSubcomponente= new SofisComboG();
            this.comboSubcomponente.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    
    /**
     * Este método llena el objeto de Combo para las Subcomponentes
     */
    public void cargarSubcomponentes(){
        try {
            if(comboComponente != null && comboComponente.getSelectedT() != null){
                List<ComponentePresupuestoEscolar> lista = escolarBean.getComponentesHabilitadosByCategoria(comboComponente.getSelectedT().getId());
                this.comboSubcomponente = new SofisComboG<>(lista, "nombre");
                this.comboSubcomponente.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    
    /**
     * Este método llena el objeto de Combo para las Subcomponentes
     */
    public void cargarUnidadPresupuestaria(){
        try {
            if(comboAnioFiscal != null && comboAnioFiscal.getSelectedT() != null){
                List<Cuentas> lista = cuentasBean.getCuentasByAnioFiscal(comboAnioFiscal.getSelectedT().getId());
                this.comboUnidadPresupuestaria = new SofisComboG<>(lista, "nombre");
                this.comboUnidadPresupuestaria.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
            }
            this.comboLineaPresupuestaria = new SofisComboG();
            this.comboLineaPresupuestaria.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    
    /**
     * Este método llena el objeto de Combo para las Subcomponentes
     */
    public void cargarLineaPresupuestaria(){
        try {
            if(comboUnidadPresupuestaria != null && comboUnidadPresupuestaria.getSelectedT() != null){
                List<Cuentas> lista = cuentasBean.getSubcuentasHabilitadasByIdPadre(comboUnidadPresupuestaria.getSelectedT().getId());
                this.comboLineaPresupuestaria = new SofisComboG<>(lista, "nombre");
                this.comboLineaPresupuestaria.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
            }else{
                this.comboLineaPresupuestaria = new SofisComboG<>();
                this.comboLineaPresupuestaria.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
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
            logger.log(Level.SEVERE, null, ex);
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
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    
    /**
     * Este método carga una lista de registros FuenteFinanciamiento
     */
    public void cargarFuenteFinanciamiento() {
        try {
            if(this.subComponenteSelected != null && this.comboLineaPresupuestaria.getSelectedT() != null){
                List<FuenteFinanciamiento> lista = transferenciasComponenteBean.getTransferenciasComponenteByAnio(this.comboLineaPresupuestaria.getSelectedT().getId(), this.subComponenteSelected.getId());
                comboFuenteFinanciamiento = new SofisComboG(lista, "nombre");
                comboFuenteFinanciamiento.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
            }else{
                comboFuenteFinanciamiento = new SofisComboG();
                comboFuenteFinanciamiento.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
            }
            comboFuenteRecursos = new SofisComboG();
            comboFuenteRecursos.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    
    /**
     * Este método carga una lista de registros FuenteRecursos segun la seleccion de la fuente de financiamiento
     */
    public void cargarFuenteRecursos() {
        if (comboFuenteFinanciamiento.getSelectedT() != null  && this.subComponenteSelected != null) {
            List<FuenteRecursos> lista = transferenciasComponenteBean.getFuentesRecursosByRelaciones(
                    this.comboLineaPresupuestaria.getSelectedT().getId(), 
                    this.subComponenteSelected.getId(),
                    this.comboFuenteFinanciamiento.getSelectedT().getId());
            
            comboFuenteRecursos = new SofisComboG(lista, "nombre");
            comboFuenteRecursos.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
        } else {
            comboFuenteRecursos = new SofisComboG();
            comboFuenteRecursos.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
        }
    }
    
    
    
    
    //******** EDICION DE REGISTRO ********
    
    /**
     * Este método busca un registro de TransferenciasComponente filtrado por su identificador y lo asigna al objeto de contexto
     * @param id 
     */
    public void findTransferenciaComponente(Integer id){
        try {
            this.codigoSeleccion = null;
            limpiarSeleccionCombos();
            
            setTransferenciasComponente(transferenciasComponenteBean.getTransferenciaComponenteById(id));
            comboAnioFiscal.setSelectedT(getTransferenciasComponente().getAnioFiscal());
            
            cargarUnidadPresupuestaria();
            comboUnidadPresupuestaria.setSelectedT(getTransferenciasComponente().getUnidadPresupuestaria());
            
            cargarLineaPresupuestaria();
            comboLineaPresupuestaria.setSelectedT(getTransferenciasComponente().getLineaPresupuestaria());
            
            comboEstado.setSelectedT(getTransferenciasComponente().getEstado());
            
            componenteSelected = getTransferenciasComponente().getComponente();
            //cargarSubcomponentes();
            subComponenteSelected = getTransferenciasComponente().getSubcomponente();
            
            cargarFuenteFinanciamiento();
            if(getTransferenciasComponente().getFuenteRecursos() != null){
                comboFuenteFinanciamiento.setSelectedT(getTransferenciasComponente().getFuenteRecursos().getFuenteFinanciamiento());
            }
            cargarFuenteRecursos();
            comboFuenteRecursos.setSelectedT(getTransferenciasComponente().getFuenteRecursos());
            
            setCodigoSeleccion(getTransferenciasComponente().getPorcentaje() != null ? 1 : (getTransferenciasComponente().getImporteFijoCentro() != null ? 2 : 3));
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    
    public List<Cuentas> completeUnidadesPresupuestarias(String query){
        try {
            if(comboAnioFiscal != null && comboAnioFiscal.getSelectedT() != null) {
                FiltroCodiguera filtro = new FiltroCodiguera();
                filtro.setNombre(query);
                filtro.setAnioId(comboAnioFiscal.getSelectedT().getId());
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
    public List<CategoriaPresupuestoEscolar> completeComponentes(String query){
        try {
            FiltroComponentePresupuestoEscolar filtro = new FiltroComponentePresupuestoEscolar();
            filtro.setNombre(query);
            filtro.setHabilitado(Boolean.TRUE);
            filtro.setAscending(new boolean[]{true});
            filtro.setOrderBy(new String[]{"nombre"});
            filtro.setMaxResults(10L);
            filtro.setIncluirCampos(new String[]{"codigo","nombre","version"});
            return categoriaBean.obtenerComponentesPorFiltro(filtro);
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
    
    //******** PERSISTENCIA ********
    
    /**
     * Este método crea o actualiza un registro de TransferenciaComponente
     */
    public void crearActualizar(){
        try {
            this.transferenciasComponente.setComponente(comboComponente.getSelectedT());
            this.transferenciasComponente.setSubcomponente(comboSubcomponente.getSelectedT());
            this.transferenciasComponente.setUnidadPresupuestaria(comboUnidadPresupuestaria.getSelectedT());
            this.transferenciasComponente.setLineaPresupuestaria(comboLineaPresupuestaria.getSelectedT());
            this.transferenciasComponente.setAnioFiscal(comboAnioFiscal.getSelectedT());
            this.transferenciasComponente.setEstado(comboEstado.getSelectedT());
            this.transferenciasComponente.setFuenteRecursos(comboFuenteRecursos.getSelectedT());
            
            if(!validarCampos()){
                return;
            }
            if(!validarOpcionesPorcentaje()){
                jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_OPCION_PORCENTAJE_NO_SELECCION);
                return;
            }
            if(!validarPorcentajeTransferencia()){
                return;
            }
            this.transferenciasComponente = transferenciasComponenteBean.crearActualizar(this.transferenciasComponente);
            jSFUtils.agregarInfo("Registro guardado correctamente");
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
    
    /**
     * Metodo utilizado para eliminar un registro de TransferenciaComponente
     * @param id ID del registro a eliminar
     */
    public void eliminar(Integer id) {
        try {
            transferenciasComponenteBean.eliminar(id);
            cleanObjects();
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
     * Método utilizado para validar la sumatoria de porcentajes de una combinacion de tranferenciaComponente
     * @return 
     */
    public boolean validarPorcentajeTransferencia(){
        try {
            if(this.transferenciasComponente.getPorcentaje() != null){
                
                FiltroTransferenciaComponente filtro = new FiltroTransferenciaComponente();
                filtro.setIdComponente(this.transferenciasComponente.getComponente().getId());
                filtro.setIdSubcomponente(this.transferenciasComponente.getSubcomponente().getId());
                filtro.setIdUnidadPresupuestaria(this.transferenciasComponente.getUnidadPresupuestaria().getId());
                filtro.setIdLineaPresupuestaria(this.transferenciasComponente.getLineaPresupuestaria().getId());
                filtro.setIdAnioFiscal(this.transferenciasComponente.getAnioFiscal().getId());
                filtro.setIdFuenteRecursos(this.transferenciasComponente.getFuenteRecursos().getId());
                filtro.setIncluirCampos(new String[]{"porcentaje","id"});
                
                List<TransferenciasComponente> lista = transferenciasComponenteBean.getTransferenciaComponenteByFiltro(filtro);

                //SUMA TODOS LOS PORCENTAJES DE LA LISTA
                BigDecimal obj = BigDecimal.ZERO;
                if(lista != null && !lista.isEmpty()){
                    for(TransferenciasComponente item : lista){
                        obj = obj.add(item.getPorcentaje());
                    }
                }
            
                //SI SE ESTA INTENTANDO ACTUALIZAR UN REGISTROS, HACE EL CALCULO EN BASE AL NUEVO PORCENTAJE
                if(this.transferenciasComponente.getId() != null && this.transferenciasComponente.getPorcentaje() != null){
                    if(lista != null && !lista.isEmpty()){
                        for(TransferenciasComponente item : lista){
                            if(item.getId().equals(this.transferenciasComponente.getId())){
                                obj = obj.subtract(item.getPorcentaje());
                                obj = obj.add(this.transferenciasComponente.getPorcentaje());
                                break;
                            }
                        }
                    }
                }else if(this.transferenciasComponente.getId() == null && this.transferenciasComponente.getPorcentaje() != null){
                    obj = obj.add(this.transferenciasComponente.getPorcentaje());
                }
                
                //VALIDA LA SUMATORIA DE PORCENTAJES
                if(obj != null && !obj.equals(BigDecimal.ZERO)){
                    if(this.transferenciasComponente.getPorcentaje().equals(BigDecimal.ZERO)){
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
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$(window).scrollTop(0);");
        }
        return false;
    }
    
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
                        getTransferenciasComponente().setImporteFijoCentro(null);
                        getTransferenciasComponente().setRemanente(false);
                        return true;
                    case 2:
                        getTransferenciasComponente().setPorcentaje(null);
                        getTransferenciasComponente().setRemanente(false);
                        return true;
                    case 3:
                        getTransferenciasComponente().setPorcentaje(null);
                        getTransferenciasComponente().setImporteFijoCentro(null);
                        getTransferenciasComponente().setRemanente(true);
                        return true;
                    default:
                        return false;
                }
            }
        }
        return false;
    }
    
    /**
     * Este metodo valida la seleccion de los campos necesarios para la creacion de una TransferenciaComponente
     * @return 
     */
    public boolean validarCampos(){
        if(getTransferenciasComponente().getSubcomponente() == null){
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_SUBCOMPONENTE_NO_SELECCIONADO);
            return false;
        }else if(getTransferenciasComponente().getLineaPresupuestaria() == null){
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_LINEA_PRESUPUESTARIA_NO_SELECCIONADA);
            return false;
        }else if(getTransferenciasComponente().getEstado() == null){
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_ESTADO_NO_SELECCIONADO);
            return false;
        }else if(getTransferenciasComponente().getAnioFiscal() == null){
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_ANIO_FISCAL_NO_SELECCIONADO);
            return false;
        }else if(getTransferenciasComponente().getFuenteRecursos() == null){
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_FUENTE_RECURSOS_NO_SELECCIONADA);
            return false;
        }
        return true;
    }
    
    /**
     * Metodo utilizado para limpiar el objeto de contexto y cerrar el popup de creación/edición de TransferenciaComponente
     */
    public void cerrar(){
        try {
            cleanObjects();
            RequestContext.getCurrentInstance().execute("$('#editModal').modal('hide');");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
            
            
            
    
    //******** FILTRADO, HISTORICO ********
    
    /**
     * Este método corresponde al evento de consulta y obtiene el resultado
     * según el filtro aplicado
     */
    public void filterTable() {
        try {
            List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();
            
            if (getTransferenciasComponente().getId() != null && getTransferenciasComponente().getId() != 0) {
                 MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO( MatchCriteriaTO.types.EQUALS, "id", getTransferenciasComponente().getId());
                 criterios.add(criterio);
            }
            
            if (this.componenteSelected != null) {
                 MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO( MatchCriteriaTO.types.EQUALS, "componente.id", this.componenteSelected.getId());
                 criterios.add(criterio);
            }

            if (this.unidadPresupuestariaSelected != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO( MatchCriteriaTO.types.EQUALS, "unidadPresupuestaria.id", this.unidadPresupuestariaSelected.getId());
                criterios.add(criterio);
            }
            
            if (getComboAnioFiscal() != null && getComboAnioFiscal().getSelectedT() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO( MatchCriteriaTO.types.EQUALS, "anioFiscal.id", getComboAnioFiscal().getSelectedT().getId());
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

            String[] propiedades = {"id", "componente.nombre", "subcomponente.nombre", 
                                    "unidadPresupuestaria.nombre", "lineaPresupuestaria.nombre","importeFijoCentro",
                                    "anioFiscal.anio", "estado", "porcentaje","remanente","transferencia.id"};
            String className = TransferenciasComponente.class.getName();
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
     * Este método carga el listado historico del registro seleccionado
     * @param id 
     */
    public void cargarHistorico(Integer id) {
        this.listaHistoricoTransferenciasComponente = emd.obtenerHistorico(TransferenciasComponenteLite.class, id);
    }
    
    
    public void limpiarCampos(Integer opcion) {
        if(opcion!= null) {
            switch(opcion) {
                case 1 :
                   //this.componenteSelected = null;
                   //this.presupuestoSelected = null;
                   this.cargarUnidadPresupuestaria();
                   break;
                default : 
            }
        }
    }

    
    //******** GENERAR TRANSFERENCIAS A CENTROS EDUCATIVOS ********
    
    /**
     * Este método es utilizado para generar registros de TransferenciaACed y TransferenciaDireccionDepartamental segun la actualización de estado
     * de un registro de TransferenciaComponente
     */
    public void generarTransferenciasCentrosEducativos(){
        try {
            FiltroRelPresAnioFinanciamiento filtro = new FiltroRelPresAnioFinanciamiento();
            filtro.setIdAnioFiscal(this.transferenciasComponente.getAnioFiscal().getId());
            filtro.setIdSubcomponente(this.transferenciasComponente.getSubcomponente().getId());
            filtro.setIdLineaPresupuestaria(this.transferenciasComponente.getLineaPresupuestaria().getId());
            filtro.setIdFuenteFinanciamiento(this.transferenciasComponente.getFuenteRecursos().getFuenteFinanciamiento().getId());
            filtro.setIdFuenteRecursos(this.transferenciasComponente.getFuenteRecursos().getId());
            filtro.setIncluirCampos(new String[]{"id", "relAnioPresupuesto"});
            List<RelacionPresAnioFinanciamiento> lista = relPresAnioFinanciamientoBean.getComponentesPresupuestoEscolarByFiltro(filtro);
            
            RelacionPresAnioFinanciamiento rel = (lista != null && !lista.isEmpty() ? lista.get(0) : null);
            if(rel == null || rel.getRelAnioPresupuesto() == null){
                //MENSAJE RELACION NULO
                return;
            }
            
            BigDecimal montoTotal = BigDecimal.ZERO;
            
            List<TopePresupuestal> listaTopes = buscarListaTopes();
            if(listaTopes == null || listaTopes.isEmpty()){
                jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_TECHOS_NO_ENCONTRADOS);
                return;
            }
            
            //Se clasifican los topes por TDD
            HashMap<TransferenciaDireccionDepartamental, List<TopePresupuestal>> topesPorDepartamento = new HashMap<TransferenciaDireccionDepartamental, List<TopePresupuestal>>();
            for(TopePresupuestal tope : listaTopes){
                DireccionDepartamental ddp = direccionDepartamentalBean.getDireccionDepartamentalByIdDepartamento(tope.getSede().getDireccion().getDepartamento().getPk());
                TransferenciaDireccionDepartamental tdd = transferenciaDireccionDepartamentalBean.getTransferenciasDirDepaByTransfCompYDirDepa(getTransferenciasComponente().getId(), ddp.getPk());
                if(tdd == null){
                    tdd = nuevoTDD(ddp);
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
                Integer sumaBeneficiarios = 0;
                for(TopePresupuestal tope: item.getValue()){
                    aced = transferenciasACedBean.getTransferenciaACedByComponenteSedeTDD(getTransferenciasComponente().getId(), tope.getSede().getId(), item.getKey().getPk());
                    if(aced == null){
                        aced = crearNuevaTransferenciaACed(tope);
                    }
                    montoTotal = aced.getMontoAutorizado() != null ? montoTotal.add(aced.getMontoAutorizado()) : montoTotal;
                    
                    List<Beneficiarios> beneficiarios = beneficiariosBean.getBeneficiariosPorPresEsAnioFiscal(rel.getRelAnioPresupuesto().getId());
                    Integer matriculas = null;
                    List<Object[]> matriculasPorSede = sedeBean.getCantidadMatriculasPorSede(
                            rel.getRelAnioPresupuesto().getFechaMatricula(),
                            Arrays.asList(tope.getSede().getId()),
                            beneficiarios,
                            rel.getRelAnioPresupuesto().getComponentePresupuestoEscolar().getSedesAdscritas());
                    
                    for (Object[] ob : matriculasPorSede) {
                        matriculas = (ob[1] != null ? ((Long) ob[1]).intValue() : null);
                        sumaBeneficiarios = sumaBeneficiarios + matriculas;
                    }
                    aced.setBeneficiarios(matriculas);
                    
                    if(sedesPorTDD.containsKey(item.getKey())){
                        sedesPorTDD.get(item.getKey()).add(aced);
                    }else{
                        sedesPorTDD.put(item.getKey(), new ArrayList<TransferenciasACed>());
                        sedesPorTDD.get(item.getKey()).add(aced);
                    }
                }
                item.getKey().setBeneficiarios(sumaBeneficiarios);
                item.getKey().setMontoAutorizado(montoTotal);
            }
            RequestContext.getCurrentInstance().execute("$('#editModal').modal('hide');");
            RequestContext.getCurrentInstance().execute("$('#tranferenciasACed').modal('show');");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
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
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    
    /**
     * Metodo utilizado para crear un nuevo registro de TransferenciaDireccionDepartamental
     * @param ddp
     * @return 
     */
    public TransferenciaDireccionDepartamental nuevoTDD(DireccionDepartamental ddp){
        TransferenciaDireccionDepartamental tdd = new TransferenciaDireccionDepartamental();
        tdd.setDireccionDep(ddp);
        tdd.setTransferenciasComponente(getTransferenciasComponente());
        tdd.setEstado(EstadoTransferenciaACed.TRANSFERIDO);
        return tdd;
    }
    
    /**
     * Metodo utilizado para buscar una lista de TechoPresupuestal filtrada
     * @return 
     */
    public List<TopePresupuestal> buscarListaTopes(){
        try {
            FiltroTopePresupuestal filtro = new FiltroTopePresupuestal();
            filtro.setComponentePresupuestoEscolarId(getTransferenciasComponente().getSubcomponente().getId());
            filtro.setCuentaId(getTransferenciasComponente().getLineaPresupuestaria().getId());
            filtro.setAnioFiscalId(getComboAnioFiscal().getSelectedT().getId());
            filtro.setEstado(EstadoTopePresupuestal.APROBACION);
            filtro.setIncluirCampos(new String[]{"sede.id","sede.nombre", "montoAprobado","sede.direccion.departamento.pk", "sede.direccion.departamento.nombre"});
            return presupuestalBean.getTopesPresupuestalesByFiltro(filtro);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
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
     * Método utilizado para verificar el estado de un registro de TransferenciaComponente
     * 
     * @return 
     */
    public boolean habilitarGeneracionTransferencia(){
        return getTransferenciasComponente().getEstado() != null && getTransferenciasComponente().getEstado().compareTo(EstadoTransferenciaComponente.AUTORIZADA) == 0;
    }
    
    /**
     * Método utilizado para obtener el porcentaje del monto aprobado de un topePresupuestal, según la transferenciaComponente
     * 
     * @param tope
     * @return 
     */
    public BigDecimal obtenerPorcentajeSede(TopePresupuestal tope){
        BigDecimal monto = null;
        try {
            if(tope.getMontoAprobado() != null && tope.getMontoAprobado().compareTo(BigDecimal.ZERO) > 0){
                if(getTransferenciasComponente().getPorcentaje()!= null && getTransferenciasComponente().getPorcentaje().compareTo(BigDecimal.ZERO) > 0){
                    monto = tope.getMontoAprobado().multiply(getTransferenciasComponente().getPorcentaje().divide(new BigDecimal(100)));
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
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return monto;
    }
    
    /**
     * Método utilizado para crear un nuevo objeto de TransferenciaACed
     * 
     * @param tope
     * @return 
     */
    public TransferenciasACed crearNuevaTransferenciaACed(TopePresupuestal tope) {
        TransferenciasACed aCed = new TransferenciasACed();
        aCed.setCed(tope.getSede());
        aCed.setEstado(EstadoTransferenciaACed.AUTORIZADO);
        aCed.setTransferencia(getTransferenciasComponente());
        aCed.setMontoAutorizado(obtenerPorcentajeSede(tope));
        return aCed;
    }
    
    
    
    
    
    //******** UTILIDAD ********
        
    /**
     * Método utilizado para concatener los ID a mostrar
     * 
     * @param idTransferencia
     * @param idTransferenciaComponente
     * @return 
     */
    public String concatenarID(String idTransferencia, String idTransferenciaComponente) {
        if (idTransferencia != null && !idTransferencia.isEmpty()) {
            return idTransferencia + "-" + idTransferenciaComponente;
        }
        return idTransferenciaComponente;
    }

    
    
    public TransferenciasComponente getTransferenciasComponente() {
        return transferenciasComponente;
    }

    public void setTransferenciasComponente(TransferenciasComponente transferenciasComponente) {
        this.transferenciasComponente = transferenciasComponente;
    }

    public List<TransferenciasComponenteLite> getListaHistoricoTransferenciasComponente() {
        return listaHistoricoTransferenciasComponente;
    }

    public void setListaHistoricoTransferenciasComponente(List<TransferenciasComponenteLite> listaHistoricoTransferenciasComponente) {
        this.listaHistoricoTransferenciasComponente = listaHistoricoTransferenciasComponente;
    }

    public SofisComboG<CategoriaPresupuestoEscolar> getComboComponente() {
        return comboComponente;
    }

    public void setComboComponente(SofisComboG<CategoriaPresupuestoEscolar> comboComponente) {
        this.comboComponente = comboComponente;
    }

    public SofisComboG<ComponentePresupuestoEscolar> getComboSubcomponente() {
        return comboSubcomponente;
    }

    public void setComboSubcomponente(SofisComboG<ComponentePresupuestoEscolar> comboSubcomponente) {
        this.comboSubcomponente = comboSubcomponente;
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

    public LazyDataModel getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel lazyModel) {
        this.lazyModel = lazyModel;
    }

    public SofisComboG<FuenteFinanciamiento> getComboFuenteFinanciamiento() {
        return comboFuenteFinanciamiento;
    }

    public void setComboFuenteFinanciamiento(SofisComboG<FuenteFinanciamiento> comboFuenteFinanciamiento) {
        this.comboFuenteFinanciamiento = comboFuenteFinanciamiento;
    }

    public Integer getCodigoSeleccion() {
        return codigoSeleccion;
    }

    public void setCodigoSeleccion(Integer codigoSeleccion) {
        this.codigoSeleccion = codigoSeleccion;
    }

    public SofisComboG<FuenteRecursos> getComboFuenteRecursos() {
        return comboFuenteRecursos;
    }

    public void setComboFuenteRecursos(SofisComboG<FuenteRecursos> comboFuenteRecursos) {
        this.comboFuenteRecursos = comboFuenteRecursos;
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

    
    
    
    
}
