/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.SsOperacion;
import gob.mined.siap2.entities.data.SsRelRolOperacion;
import gob.mined.siap2.entities.data.SsRol;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.UsuarioDelegate;
import gob.mined.siap2.web.utils.dataProvider.EntityReferenceDataProvider;
import gob.mined.siap2.web.utils.dataProvider.GeneralLazyDataModel;
import gob.mined.siap2.web.utils.dataProvider.IDataProvider;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "rolesConsulta")
public class RolesConsulta implements Serializable {

    @Inject
    JSFUtils jSFUtils;
    @Inject
    EntityManagementDelegate emd;
    @Inject
    UsuarioDelegate ud;

    private LazyDataModel lazyModel;
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    private String filtroCodigo;
    private String filtroNombre;
    private String filtroDescripcion;

    private String selectedOperacion;
    private Map<String, String> operacionesToAdd;
    
    private List<SsRol> historico;

    @PostConstruct
    public void init() {

        filterTable();
    }

    
    /**
     * Este método inicia los filtros de la consulta
     */
    private void initFilter(){
        filtroCodigo = null;
        filtroNombre = null;
        filtroDescripcion = null;
    }

    
    /**
     * Este método corresponde al evento de consulta y obtiene el resultado
     * según el filtro aplicado
     */
    public void filterTable() {
        try {

            List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();

            if (!TextUtils.isEmpty(filtroCodigo)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                        MatchCriteriaTO.types.CONTAINS, "rolCod", filtroCodigo);
                criterios.add(criterio);
            }
            if (!TextUtils.isEmpty(filtroNombre)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                        MatchCriteriaTO.types.CONTAINS, "rolNombre", filtroNombre);
                criterios.add(criterio);
            }
            if (!TextUtils.isEmpty(filtroDescripcion)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                        MatchCriteriaTO.types.CONTAINS, "rolDescripcion", filtroDescripcion);
                criterios.add(criterio);
            }

            CriteriaTO condicion = null;
            if (!criterios.isEmpty()) {
                if (criterios.size() == 1) {
                    condicion = criterios.get(0);
                } else {
                    condicion = CriteriaTOUtils.createANDTO(criterios
                            .toArray(new CriteriaTO[0]));
                }
            } else {
                // condición dummy para que el count by criteria funcione
                condicion = CriteriaTOUtils.createMatchCriteriaTO(
                        MatchCriteriaTO.types.NOT_NULL, "rolId", 1);
            }

            String[] propiedades = {"rolId", "rolCod", "rolDescripcion", "rolNombre", "rolVigente"};

            String className = SsRol.class.getName();
            String[] orderBy = {"rolId"};
            boolean[] asc = {false};

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
     * Este método vuelve a iniciar los filtros y aplica la consulta
     */
    public void limpiar() {
        initFilter();
        filterTable();
    }

    private SsRol editando = new SsRol();
    
    

    
    /**
     * Este método carga un objeto a edición
     * 
     * @param id 
     */
    public void cargarToEditar(String id) {
        if (!TextUtils.isEmpty(id)) {
            editando = (SsRol) emd.getEntityById(SsRol.class.getName(), Integer.valueOf(id));
        } else {
            editando = new SsRol();

        }

        loadOperaciones(editando.getRolId());
        loadOperacionesToAdd(editando.getRolId());

    }
    
    /**
     * Duplica un rol nuevo a partir del que se encuentra en edición
     * 
     * @param id 
     */
    public void duplicarToEditar(Integer id) {
        editando = new SsRol();
        if (id != null) {
            loadOperaciones(id);
        }

        List<SsRelRolOperacion> relRol2 = new LinkedList<>();
        for(SsRelRolOperacion rro: relRol){
            SsRelRolOperacion relacion = new SsRelRolOperacion();
            relacion.setRelRolOperacionOperacionId(rro.getRelRolOperacionOperacionId());
            relacion.setRelRolOperacionRolId(editando);
            relacion.setRelRolOperacionVisible(true);
            relacion.setRelRolOperacionEditable(true);
            relacion.setRelRolOperacionId(null);
            relRol2.add(relacion);
        }
        relRol = relRol2;
    }
    
    
   
    
    /**
     * Guarda el objeto en edición
     */
    public void guardarEditando() {
        try {
            if (editando.getRolOrigen() == null){
                editando.setRolOrigen("SIAP-WEB");
            }
            ud.actualizarPermisos(editando, relRolToDelate, relRol);

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
     * Guarda el rol duplicado
     */
    public void guardarDuplicando() {
        try {
            if (editando.getRolCod() == null ||editando.getRolCod().trim().length() == 0){
                jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_CODIGO_ROL_REPETIDO);
                return;
            }

            if (ud.obtenerRolPorCodigo(editando.getRolCod()) != null){
                jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_CODIGO_ROL_REPETIDO);
                return;
            }
            
            if (editando.getRolOrigen() == null){
                editando.setRolOrigen("SIAP-WEB");
            }
            ud.actualizarPermisos(editando, relRolToDelate, relRol);

            filterTable();
            RequestContext.getCurrentInstance().execute("$('#editModal2').modal('hide');");


        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    private List<SsRelRolOperacion> relRol;
    private Set<Integer> relRolToDelate;

    /**
     * Carga una lista de operaciones para un rol
     * 
     * @param idRol 
     */
    public void loadOperaciones(Integer idRol) {
        relRolToDelate = new HashSet();

        if (idRol != null) {
            String className = SsRelRolOperacion.class.getName();
            relRol = emd.findByOneProperty(className, "relRolOperacionRolId.rolId", idRol);
        } else {
            relRol = new LinkedList();
        }

    }

    /**
     * Carga la lista de operaciones que se pueden añadir al rol pasado por parámetro
     * 
     * @param idRol 
     */
    public void loadOperacionesToAdd(Integer idRol) {
        operacionesToAdd = new LinkedHashMap<String, String>();
        List<SsOperacion> l = emd.getEntities(SsOperacion.class.getName(), "opeCodigo");
        if (l != null) {
            for (SsOperacion item : l) {
                operacionesToAdd.put(item.getOpeCodigo(), String.valueOf(item.getOpeId()));
            }
        }

        for (SsRelRolOperacion rRol : relRol) {
            operacionesToAdd.remove(rRol.getRelRolOperacionOperacionId().getOpeCodigo());
        }

    }

    /**
     * Añade una nueva operación al rol que se esta editando
     */
    public void addOperacion() {
        Integer operacionId = Integer.valueOf(selectedOperacion);
        SsOperacion operacion = (SsOperacion) emd.getEntityById(SsOperacion.class.getName(), operacionId);

        SsRelRolOperacion relacion = new SsRelRolOperacion();
        relacion.setRelRolOperacionOperacionId(operacion);
        relacion.setRelRolOperacionRolId(editando);
        relacion.setRelRolOperacionVisible(true);
        relacion.setRelRolOperacionEditable(true);
        relacion.setRelRolOperacionId(null);

        relRol.add(relacion);
        operacionesToAdd.remove(operacion.getOpeCodigo());
    }

    /**
     * Elimina una operación del rol que es esta editando.
     * 
     * @param index 
     */
    public void eliminarOperacion(Integer index) {
        SsRelRolOperacion item = relRol.get(index.intValue());
        relRol.remove(item);
        if (item.getRelRolOperacionId() != null) {
            relRolToDelate.add(item.getRelRolOperacionId());
        }
        operacionesToAdd.put(item.getRelRolOperacionOperacionId().getOpeCodigo(), String.valueOf(item.getRelRolOperacionOperacionId().getOpeId()));
    }

    // <editor-fold defaultstate="collapsed" desc="getter-setter">   
    public JSFUtils getjSFUtils() {
        return jSFUtils;
    }

    public void setjSFUtils(JSFUtils jSFUtils) {
        this.jSFUtils = jSFUtils;
    }

    public LazyDataModel getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel lazyModel) {
        this.lazyModel = lazyModel;
    }

    public EntityManagementDelegate getEmd() {
        return emd;
    }

    public void setEmd(EntityManagementDelegate emd) {
        this.emd = emd;
    }

    public String getFiltroCodigo() {
        return filtroCodigo;
    }

    public String getSelectedOperacion() {
        return selectedOperacion;
    }

    public void setSelectedOperacion(String selectedOperacion) {
        this.selectedOperacion = selectedOperacion;
    }

    public Map<String, String> getOperacionesToAdd() {
        return operacionesToAdd;
    }

    public void setOperacionesToAdd(Map<String, String> operacionesToAdd) {
        this.operacionesToAdd = operacionesToAdd;
    }

    public SsRol getEditando() {
        return editando;
    }

    public void setEditando(SsRol editando) {
        this.editando = editando;
    }

    public void setFiltroCodigo(String filtroCodigo) {
        this.filtroCodigo = filtroCodigo;
    }

    public String getFiltroNombre() {
        return filtroNombre;
    }

    public void setFiltroNombre(String filtroNombre) {
        this.filtroNombre = filtroNombre;
    }

    public List<SsRelRolOperacion> getRelRol() {
        return relRol;
    }

    public void setRelRol(List<SsRelRolOperacion> relRol) {
        this.relRol = relRol;
    }

    public String getFiltroDescripcion() {
        return filtroDescripcion;
    }

    public void setFiltroDescripcion(String filtroDescripcion) {
        this.filtroDescripcion = filtroDescripcion;
    }

    // </editor-fold>
}
