package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.business.ejbs.impl.RubroBean;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.Rubro;
import gob.mined.siap2.entities.data.impl.SubRubro;
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
@Named(value = "subRubroMB")
public class SubRubroMB implements Serializable{
    
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
    
    private SubRubro subRubro;
    private SubRubro subRubroFiltro;
    private List<SubRubro> listaSubRubro;
    private List<SubRubro> listaHistoricoSubRubro;
    private List<Rubro> listaRubro;
    
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
        cargarListaRubros();
    }
    

    
    /**
     * Este método inicializa los objetos base del mantenimiento
     */
    public void cleanObject() {
        this.subRubro = inicializarObjetos(null);
        this.subRubroFiltro = inicializarObjetos(null);
        filterTable();
    }
    

    /**
     * Este método inicializa un objeto de Rubros
     * @param local
     * @return 
     */
    public SubRubro inicializarObjetos(SubRubro local){
        if(local == null){
            local = new SubRubro();
        }
        inicializarSubRubro(local);
        return local;
    }
    
    /**
     * Metodo inicializador de relaciones con Rubro
     * @param local 
     */
    public void inicializarSubRubro(SubRubro local){
        if(local != null){
            if(local.getRubro() == null){
                local.setRubro(new Rubro());
            }
        }
    }
    
    /**
     * Este Método carga la lista de registros de Rubro que se encuetren habilitados
     */
    public void cargarListaRubros(){
        try {
            this.listaRubro = rubroBean.getRubrosHabilitados();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    
    /**
     * Este Método obtiene un registro de SubRubro filtrado por ID
     * @param id 
     */
    public void asignarEdicion(Integer id){
        this.subRubro = rubroBean.getSubRubroById(id);
    }
    
    /**
     * Metodo utilizado para buscar un registro de SubRubro
     * @param id
     */
    public void findRubroById(Integer id) {
        try {
            setSubRubro(rubroBean.getSubRubroById(id));
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    /**
     * Metodo utilizado para buscar todos los registros de SubRubro
     */
    public void findListaRubro() {
        try {
            setListaSubRubro(rubroBean.getSubRubros());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    
    /**
     * Metodo utilizado para crear y/o actualizar un registro de SubRubro
     */
    public void guardarActualizar() {
        try {
            if(getSubRubro().getRubro() == null || getSubRubro().getRubro().getId() == null || getSubRubro().getRubro().getId() == 0){
                getSubRubro().setRubro(null);
            }
            rubroBean.crearActualizarSubRubro(getSubRubro());
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
     * Metodo utilizado para eliminar un registro de SubRubro
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
            
            if (!TextUtils.isEmpty(getSubRubroFiltro().getCodigo())) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "codigo", getSubRubroFiltro().getCodigo());
                criterios.add(criterio);
            }
            if (!TextUtils.isEmpty(getSubRubroFiltro().getNombre())) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO( MatchCriteriaTO.types.CONTAINS, "nombre", getSubRubroFiltro().getNombre());
                criterios.add(criterio);
            }
            if (getSubRubroFiltro().getRubro() != null && getSubRubroFiltro().getRubro().getId() != null && getSubRubroFiltro().getRubro().getId() != 0) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO( MatchCriteriaTO.types.EQUALS, "rubro.id", getSubRubroFiltro().getRubro().getId());
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

            String[] propiedades = {"id", "codigo", "nombre", "habilitado"};
            String className = SubRubro.class.getName();
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
        this.listaHistoricoSubRubro = emd.obtenerHistorico(SubRubro.class, id);
    }
    
    
    
    
    
    
    
    
    public SubRubro getSubRubro() {
        return subRubro;
    }

    public void setSubRubro(SubRubro subRubro) {
        this.subRubro = subRubro;
    }

    public SubRubro getSubRubroFiltro() {
        return subRubroFiltro;
    }

    public void setSubRubroFiltro(SubRubro subRubroFiltro) {
        this.subRubroFiltro = subRubroFiltro;
    }

    public List<SubRubro> getListaSubRubro() {
        return listaSubRubro;
    }

    public void setListaSubRubro(List<SubRubro> listaSubRubro) {
        this.listaSubRubro = listaSubRubro;
    }

    public List<SubRubro> getListaHistoricoSubRubro() {
        return listaHistoricoSubRubro;
    }

    public void setListaHistoricoSubRubro(List<SubRubro> listaHistoricoSubRubro) {
        this.listaHistoricoSubRubro = listaHistoricoSubRubro;
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

    public List<Rubro> getListaRubro() {
        return listaRubro;
    }

    public void setListaRubro(List<Rubro> listaRubro) {
        this.listaRubro = listaRubro;
    }
    
    
    
    
    
}
