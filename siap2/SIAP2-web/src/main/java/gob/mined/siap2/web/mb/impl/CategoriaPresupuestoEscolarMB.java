package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.business.RelacionGesPresEsAnioFiscalBean;
import gob.mined.siap2.business.ejbs.impl.CategoriaPresupuestoEscolarBean;
import gob.mined.siap2.business.ejbs.impl.TopePresupuestalBean;
import gob.mined.siap2.business.ejbs.impl.TransferenciasComponenteBean;
import gob.mined.siap2.business.ejbs.impl.UnidadTecnicaBean;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.CategoriaPresupuestoEscolar;
import gob.mined.siap2.entities.data.impl.CategoriaPresupuestoEscolarLite;
import gob.mined.siap2.entities.data.impl.RelacionGesPresEsAnioFiscal;
import gob.mined.siap2.entities.data.impl.TopePresupuestal;
import gob.mined.siap2.entities.data.impl.TransferenciasComponente;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.filtros.FiltroRelPresAnioFiscal;
import gob.mined.siap2.filtros.FiltroUnidadTecnica;
import gob.mined.siap2.persistence.dao.exceptions.DAOConstraintViolationException;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.UnidadTecnicaDelegate;
import gob.mined.siap2.web.genericos.constantes.ConstantesPresentacion;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.utils.SofisComboG;
import gob.mined.siap2.web.utils.dataProvider.EntityReferenceDataProvider;
import gob.mined.siap2.web.utils.dataProvider.GeneralLazyDataModel;
import gob.mined.siap2.web.utils.dataProvider.IDataProvider;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;

@ViewScoped
@Named(value = "categoriaPresupuestoEscolarMB")
public class CategoriaPresupuestoEscolarMB implements Serializable{
 
    
    
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
 
    private CategoriaPresupuestoEscolar cpe;
    private CategoriaPresupuestoEscolar cpeFiltro;
    private CategoriaPresupuestoEscolar categoriaEliminar;
    
    private List<CategoriaPresupuestoEscolarLite> listaCpeHistorico;
    private SofisComboG<UnidadTecnica> comboUnidadTecnica;
    protected LazyDataModel lazyModel;
    
    private String filtroHabilitado;
    private boolean flagEliminar;
    private UnidadTecnica filtroUnidadTecnicaSelected;
    
    @Inject
    UnidadTecnicaDelegate unidadTecnicaDelegate;
    
    @Inject 
    private CategoriaPresupuestoEscolarBean escolarBean;
    @Inject
    private JSFUtils jSFUtils;
    @Inject
    private EntityManagementDelegate emd;
    @Inject
    private UnidadTecnicaBean unidadTecnicaBean;
    @Inject
    private TopePresupuestalBean topePresupuestalBean;
    @Inject
    private TransferenciasComponenteBean transferenciasComponenteBean;
    @Inject 
    private RelacionGesPresEsAnioFiscalBean relacionGesPresEsAnioFiscalBean;
    
    
    
    @PostConstruct
    public void init(){
        cargarUnidadTecnica();
        cleanObject();
    }
    
    
    public void cleanObject(){
        this.cpe = inicializarObjetos(null);
        this.cpeFiltro = inicializarObjetos(null);
        this.filtroHabilitado = null;
        this.comboUnidadTecnica.clearSelectedT();
        filterTable();
    }
    
    
    public CategoriaPresupuestoEscolar inicializarObjetos(CategoriaPresupuestoEscolar local){
        if(local == null){
            local = new CategoriaPresupuestoEscolar();
        }
        return local;
    }
    

    /**
     * Metodo utilizado para cargar una lista de UnidadTecnica
     */
    public void cargarUnidadTecnica(){
        try {
            List<UnidadTecnica> lista = unidadTecnicaBean.obtenerTodos();
            this.comboUnidadTecnica = new SofisComboG(lista, "nombre");
            this.comboUnidadTecnica.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);            
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    
    public void findListaCatPresEscolarById(Integer id){
        this.cpe = escolarBean.getCategoriaPresupuestoEscolarById(id);
    }
    
    
    public List<UnidadTecnica> completeUnidadesTecnicas(String query){
        try {
            if(query != null && !query.isEmpty()) {
                FiltroUnidadTecnica filtro = new FiltroUnidadTecnica();
                filtro.setNombre(query);
                filtro.setAscending(new boolean[]{true});
                filtro.setOrderBy(new String[]{"nombre"});
                filtro.setMaxResults(10L);
                filtro.setIncluirCampos(new String[]{"id","nombre","version"});
                return unidadTecnicaDelegate.obtenerUnidadesPorFiltro(filtro);
            }
            
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;
    }
    
    
    
    
    

    public void asignarEdicion(Integer id) {
        findListaCatPresEscolarById(id);
    }
    
    /**
     * Este método guarda un registro de Componente
     */
    public void guardarActualizar() {
        try {
            escolarBean.crearActualizar(this.cpe);
            cleanObject();
            jSFUtils.agregarInfo("Registro guardado correctamente");
            RequestContext.getCurrentInstance().execute("$('#editModal').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");

        } catch(DAOConstraintViolationException e){
            logger.log(Level.SEVERE, null, e);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_CODIGO_REPETIDO);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
            
        }catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } 
    }
    
    /**
     * Metodo utilizado para eliminar un registro de Componente
     */
     public void eliminar() {
        try {
            if(!flagEliminar){
                escolarBean.eliminar(categoriaEliminar.getId());
                jSFUtils.agregarInfo("Elemento eliminado correctamente");
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
     
     /**
      * Este método filtra los registros de Componente segun lo seleccionado
      */
     public void filterTable() {
        try {
            List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();
            
            if (!TextUtils.isEmpty(getCpeFiltro().getCodigo())) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "codigo", getCpeFiltro().getCodigo());
                criterios.add(criterio);
            }
            if (!TextUtils.isEmpty(getCpeFiltro().getNombre())) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO( MatchCriteriaTO.types.CONTAINS, "nombre", getCpeFiltro().getNombre());
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
            
            if (this.filtroUnidadTecnicaSelected != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO( MatchCriteriaTO.types.EQUALS, "subComponentes.unidadTecnica.id", this.filtroUnidadTecnicaSelected.getId());
                criterios.add(criterio);
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

            String[] propiedades = {"id","codigo", "nombre", "habilitado"};
            String className = CategoriaPresupuestoEscolar.class.getName();
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
        this.listaCpeHistorico = emd.obtenerHistorico(CategoriaPresupuestoEscolarLite.class, id);
    }
    
    /**
     * Este método verifica las relaciones que tiene el registro Componente
     * 
     * @param idCat
     */
    public void verificarEliminar(Integer idCat){
        try {
            this.categoriaEliminar = escolarBean.getCategoriaPresupuestoEscolarById(idCat);
            flagEliminar = false;
            List<TopePresupuestal> listaTope = topePresupuestalBean.getTopePresupuestalByIdCategoriaComponente(categoriaEliminar.getId());
            if(listaTope != null && !listaTope.isEmpty()){
                jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_NO_ELIMINAR_REGISTROS_TOPE);
                flagEliminar = true;
            }

            List<TransferenciasComponente> listaTransComp = transferenciasComponenteBean.getTransferenciasComponenteByCategoriaComponente(categoriaEliminar.getId());
            if(listaTransComp != null && !listaTransComp.isEmpty()){
                jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_NO_ELIMINAR_REGISTROS_TRANSFERENCIA_COMPONENTE);
                flagEliminar = true;
            }
            
            FiltroRelPresAnioFiscal filtro = new FiltroRelPresAnioFiscal();
            filtro.setCategoriaPresupuestoEscolarId(categoriaEliminar.getId());
            filtro.setIncluirCampos(new String[]{"id"});
            List<RelacionGesPresEsAnioFiscal> listaRela = relacionGesPresEsAnioFiscalBean.getComponentesPresupuestoEscolarByFiltro(filtro);
            if(listaRela != null && !listaRela.isEmpty()){
                jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_COMPONENTE_RELACION_PRESUPUESTO_SUBCOMPONENTE);
                flagEliminar = true;
            }

            RequestContext.getCurrentInstance().update("formEliminar");
            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('show');");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    
    
    
    
    
    
    
    
    
    public List<CategoriaPresupuestoEscolarLite> getListaCpeHistorico() {
        return listaCpeHistorico;
    }

    public void setListaCpeHistorico(List<CategoriaPresupuestoEscolarLite> listaCpeHistorico) {
        this.listaCpeHistorico = listaCpeHistorico;
    }

    public CategoriaPresupuestoEscolar getCpe() {
        return cpe;
    }

    public void setCpe(CategoriaPresupuestoEscolar cpe) {
        this.cpe = cpe;
    }

    public CategoriaPresupuestoEscolar getCpeFiltro() {
        return cpeFiltro;
    }

    public void setCpeFiltro(CategoriaPresupuestoEscolar cpeFiltro) {
        this.cpeFiltro = cpeFiltro;
    }

    public String getFiltroHabilitado() {
        return filtroHabilitado;
    }

    public void setFiltroHabilitado(String filtroHabilitado) {
        this.filtroHabilitado = filtroHabilitado;
    }

    public LazyDataModel getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel lazyModel) {
        this.lazyModel = lazyModel;
    }

    public SofisComboG<UnidadTecnica> getComboUnidadTecnica() {
        return comboUnidadTecnica;
    }

    public void setComboUnidadTecnica(SofisComboG<UnidadTecnica> comboUnidadTecnica) {
        this.comboUnidadTecnica = comboUnidadTecnica;
    }

    public CategoriaPresupuestoEscolar getCategoriaEliminar() {
        return categoriaEliminar;
    }

    public void setCategoriaEliminar(CategoriaPresupuestoEscolar categoriaEliminar) {
        this.categoriaEliminar = categoriaEliminar;
    }

    public boolean isFlagEliminar() {
        return flagEliminar;
    }

    public void setFlagEliminar(boolean flagEliminar) {
        this.flagEliminar = flagEliminar;
    }

    public UnidadTecnica getFiltroUnidadTecnicaSelected() {
        return filtroUnidadTecnicaSelected;
    }

    public void setFiltroUnidadTecnicaSelected(UnidadTecnica filtroUnidadTecnicaSelected) {
        this.filtroUnidadTecnicaSelected = filtroUnidadTecnicaSelected;
    }
    
    
}
