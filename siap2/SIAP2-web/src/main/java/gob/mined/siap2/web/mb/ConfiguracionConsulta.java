/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.Configuracion;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.utils.dataProvider.EntityReferenceDataProvider;
import gob.mined.siap2.web.utils.dataProvider.GeneralLazyDataModel;
import gob.mined.siap2.web.utils.dataProvider.IDataProvider;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
@Named(value = "configuracionConsulta")
public class ConfiguracionConsulta implements Serializable {

    @Inject
    JSFUtils jSFUtils;
    @Inject
    EntityManagementDelegate emd;

    private LazyDataModel lazyModel;
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    private String filtroCodigo;
    private String filtroDescripcion;
    private String filtroValor;

    @PostConstruct
    public void init() {

        filterTable();
    }

    
    /**
     * Este método inicia los filtros de la consulta
     */
    private void initFilter(){
        filtroCodigo = null;
        filtroValor = null;
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
                        MatchCriteriaTO.types.CONTAINS, "cnfCodigo", filtroCodigo);
                criterios.add(criterio);
            }
            if (!TextUtils.isEmpty(filtroValor)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                        MatchCriteriaTO.types.CONTAINS, "cnfValor", filtroValor);
                criterios.add(criterio);
            }
            if (!TextUtils.isEmpty(filtroDescripcion)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                        MatchCriteriaTO.types.CONTAINS, "cnfDescripcion", filtroDescripcion);
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
                        MatchCriteriaTO.types.NOT_NULL, "id", 1);
            }

            String[] propiedades = {"id", "cnfCodigo", "cnfDescripcion", "cnfValor"};

            String className = Configuracion.class.getName();
            String[] orderBy = {"id"};
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

    private Configuracion editando = new Configuracion();

    /**
     * Este método carga un objeto a edición
     * 
     * @param id 
     */
    public void cargarToEditar(String id) {
        if (!TextUtils.isEmpty(id)) {
            editando = (Configuracion) emd.getEntityById(Configuracion.class.getName(), Integer.valueOf(id));
        } else {
            editando = new Configuracion();
        }
    }

    /**
     * Guarda el objeto en edición
     */
    public void guardarEditando() {
        try {

            emd.saveOrUpdate(editando);
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

    public void setFiltroCodigo(String filtroCodigo) {
        this.filtroCodigo = filtroCodigo;
    }

    public String getFiltroValor() {
        return filtroValor;
    }

    public void setFiltroValor(String filtroValor) {
        this.filtroValor = filtroValor;
    }

    public String getFiltroDescripcion() {
        return filtroDescripcion;
    }

    public Configuracion getEditando() {
        return editando;
    }

    public void setEditando(Configuracion editando) {
        this.editando = editando;
    }

    public void setFiltroDescripcion(String filtroDescripcion) {
        this.filtroDescripcion = filtroDescripcion;
    }

    // </editor-fold>
}
