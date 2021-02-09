/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.codigueras;

import com.mined.siap2.interfaces.BaseCodiguera;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.GeneralDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.utils.dataProvider.EntityReferenceDataProvider;
import gob.mined.siap2.web.utils.dataProvider.GeneralLazyDataModel;
import gob.mined.siap2.web.utils.dataProvider.IDataProvider;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
/**
 *
 * @author Sofis Solutions
 */

/**
 *
 * @author Sofis Solutions
 * @param <Clase>
 */
public abstract class CodigueraGenerico<Clase extends BaseCodiguera> implements Serializable {
    private Class<Clase> type;
    protected Clase editando ;

    @Inject
    protected JSFUtils jSFUtils;
    @Inject
    protected EntityManagementDelegate emd;
    @Inject
    protected GeneralDelegate generalDelegate;
    
    protected LazyDataModel lazyModel;
    protected static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    protected String filtroCodigo;
    protected String filtroNombre;
    protected String filtroOrden;
    protected String filtroHabilitado;

    private final static String ESTADO_HABILITADO="HABILITADO";
    private final static String ESTADO_DESHABILITADO="DESHABILITADO";
    
    private List<Clase> historico;
    
    public void init() {
        Type t = getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) t;
        type = (Class) pt.getActualTypeArguments()[0];
        filterTable();
    }

    
    /**
     * Este método inicia los filtros de la consulta
     */
    private void initFilter(){
        filtroOrden = null;
        filtroCodigo = null;
        filtroNombre = null;
        filtroHabilitado=null;
    }

    public String[] getPropiedades(){
       String[] propiedades = {"id","habilitado","orden","codigo","nombre"};
       return  propiedades;
    }
    public String[] getOrderBy(){
        String[] orderBy = {"orden", "nombre"};
        return orderBy;
    }
     
    public boolean[] getAsc(){
        boolean[] asc = {true,true};
        return asc;
    }

    
    /**
     * Este método corresponde al evento de consulta y obtiene el resultado
     * según el filtro aplicado
     */
    public void filterTable() {
        try {
            List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();
            if (!TextUtils.isEmpty(filtroOrden) && TextUtils.isInteger(filtroOrden)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "orden", Integer.valueOf(filtroOrden));
                criterios.add(criterio);
            }

            if (!TextUtils.isEmpty(filtroCodigo)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "codigo", filtroCodigo);
                criterios.add(criterio);
            }

            if (!TextUtils.isEmpty(filtroNombre)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "nombre", filtroNombre);
                criterios.add(criterio);
            }            
            
            if (!TextUtils.isEmpty(filtroHabilitado)) {
                if (ESTADO_HABILITADO.equals(filtroHabilitado)){
                    MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "habilitado", true);
                    criterios.add(criterio);
                }else if (ESTADO_DESHABILITADO.equals(filtroHabilitado)){
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


            IDataProvider dataProvider = new EntityReferenceDataProvider(getPropiedades(), type.getName(), condicion, getOrderBy(), getAsc());
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
     * Este método vuelve a iniciar los filtros y aplica la consulta
     */
    public void limpiar() {
        initFilter();
        filterTable();
    }

    
    public void crearCategoria() throws Exception{
        editando =type.newInstance();
    }
    
    public void cargarToEditar(Integer id) {
        editando = (Clase) emd.getEntityById(type.getName(), id);
    }

    public void cargarHistorico(Integer id) {
        historico = emd.obtenerHistorico(type,id);
    }

    
    /**
     * Guarda el objeto en edición
     */
    public void guardarEditando() {
        try {
            generalDelegate.guardarCodiguera(editando);
            filterTable();
            RequestContext.getCurrentInstance().execute("$('#editModal').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }
    

    
    /**
     * Este método corresponde al evento de eliminación del objeto seleccionado.
     *
     * @param idToEliminar
     */
    public void eliminar(Integer idToEliminar) {
         try {
             try{
                emd.delete(type, Integer.valueOf(idToEliminar));    
             }catch (Exception e){
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_EXISTE_ENTIDAD_ASOCIADA_SIN_PARAMETROS);
                throw b;
             }        
            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('hide');");
         } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="getter-setter">   
    public JSFUtils getjSFUtils() {
        return jSFUtils;
    }

    public void setjSFUtils(JSFUtils jSFUtils) {
        this.jSFUtils = jSFUtils;
    }

    public List<Clase> getHistorico() {
        return historico;
    }

    public void setHistorico(List<Clase> historico) {
        this.historico = historico;
    }

    public LazyDataModel getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel lazyModel) {
        this.lazyModel = lazyModel;
    }

    public String getFiltroOrden() {
        return filtroOrden;
    }

    public void setFiltroOrden(String filtroOrden) {
        this.filtroOrden = filtroOrden;
    }


    

    public String getFiltroCodigo() {
        return filtroCodigo;
    }

    public void setFiltroCodigo(String filtroCodigo) {
        this.filtroCodigo = filtroCodigo;
    }

    public String getFiltroNombre() {
        return filtroNombre;
    }

    public String getESTADO_HABILITADO() {
        return ESTADO_HABILITADO;
    }


    public String getESTADO_DESHABILITADO() {
        return ESTADO_DESHABILITADO;
    }

    public void setFiltroNombre(String filtroNombre) {
        this.filtroNombre = filtroNombre;
    }

    public String getFiltroHabilitado() {
        return filtroHabilitado;
    }

    public void setFiltroHabilitado(String filtroHabilitado) {
        this.filtroHabilitado = filtroHabilitado;
    }

   
    

    public EntityManagementDelegate getEmd() {
        return emd;
    }


    public void setEmd(EntityManagementDelegate emd) {
        this.emd = emd;
    }

    public Clase getEditando() {
        return editando;
    }

    public void setEditando(Clase editando) {
        this.editando = editando;
    }


    // </editor-fold>
}
