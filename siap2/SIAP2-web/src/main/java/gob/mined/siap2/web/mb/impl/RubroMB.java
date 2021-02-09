package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.business.ejbs.impl.RubroBean;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.Rubro;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
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
@Named(value = "rubroMB")
public class RubroMB implements Serializable{
 
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
    
    
    private Rubro rubro;
    private Rubro rubroFiltro;
    private List<Rubro> listaRubro;
    private List<Rubro> listaHistoricoRubro;
    
    protected LazyDataModel lazyModel;
    private String filtroHabilitado;
    
            
    @Inject
    private EntityManagementDelegate emd;
    
    @Inject
    private JSFUtils jSFUtils;
    
    @Inject
    private RubroBean rubroBean;

  
    
    @PostConstruct
    public void init() {
        cleanObject();
    }
    

    
    /**
     * Este método inicializa los objetos base del mantenimiento
     */
    public void cleanObject() {
        this.rubro = inicializarObjetos(null);
        this.rubroFiltro = inicializarObjetos(null);
        filterTable();
    }
    

    /**
     * Este método inicializa un objeto de Rubros
     * @param local
     * @return 
     */
    public Rubro inicializarObjetos(Rubro local){
        if(local == null){
            local = new Rubro();
        }
        return local;
    }
    
    /**
     * Este Método obtiene un registro de Rubro, filtrado por ID
     * @param id 
     */
    public void asignarEdicion(Integer id){
        this.rubro = rubroBean.getRubroById(id);
    }
    
    /**
     * Metodo utilizado para buscar un registro de Rubro
     * @param id
     */
    public void findRubroById(Integer id) {
        try {
            setRubro(rubroBean.getRubroById(id));
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    /**
     * Metodo utilizado para buscar todos los registros de Rubro
     */
    public void findListaRubro() {
        try {
            setListaRubro(rubroBean.getRubros());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    /**
     * Metodo utilizado para crear y/o actualizar un registro de Rubro
     */
    public void guardarActualizar() {
        try {
            rubroBean.crearActualizar(getRubro());
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
     * Metodo utilizado para eliminar un registro de Rubro
     * @param id ID del registro a eliminar
     */
    public void eliminar(Integer id) {
        try {
            rubroBean.eliminar(id);
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
     * Este método corresponde al evento de consulta y obtiene el resultado
     * según el filtro aplicado
     */
    public void filterTable() {
        try {
            List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();
            
            if (!TextUtils.isEmpty(getRubroFiltro().getCodigo())) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "codigo", getRubroFiltro().getCodigo());
                criterios.add(criterio);
            }
            if (!TextUtils.isEmpty(getRubroFiltro().getNombre())) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO( MatchCriteriaTO.types.CONTAINS, "nombre", getRubroFiltro().getNombre());
                criterios.add(criterio);
            }

            if (getFiltroHabilitado() != null && !getFiltroHabilitado().isEmpty()) {
                if (getFiltroHabilitado().equals("true")) {
                    MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "habilitado", true);
                    criterios.add(criterio);
                } else if(getFiltroHabilitado().equals("false")){
                    MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "habilitado", false);
                    criterios.add(criterio);
                }
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

            String[] propiedades = {"id", "codigo", "nombre", "habilitado"};
            String className = Rubro.class.getName();
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
        this.listaHistoricoRubro = emd.obtenerHistorico(Rubro.class, id);
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

    public Rubro getRubro() {
        return rubro;
    }

    public void setRubro(Rubro rubro) {
        this.rubro = rubro;
    }

    public Rubro getRubroFiltro() {
        return rubroFiltro;
    }

    public void setRubroFiltro(Rubro rubroFiltro) {
        this.rubroFiltro = rubroFiltro;
    }

    public List<Rubro> getListaRubro() {
        return listaRubro;
    }

    public void setListaRubro(List<Rubro> listaRubro) {
        this.listaRubro = listaRubro;
    }

    public List<Rubro> getListaHistoricoRubro() {
        return listaHistoricoRubro;
    }

    public void setListaHistoricoRubro(List<Rubro> listaHistoricoRubro) {
        this.listaHistoricoRubro = listaHistoricoRubro;
    }
    
}
