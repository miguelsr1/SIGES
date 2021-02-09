/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.Indicador;
import gob.mined.siap2.entities.enums.EstadoComun;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.IndicadorDelegate;
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
@Named(value = "catalogoDIndicadoresConsulta")
public class CatalogoDeIndicadoresConsulta implements Serializable {

    @Inject
    JSFUtils jSFUtils;
    @Inject
    EntityManagementDelegate emd;
    @Inject
    IndicadorDelegate indicadorDelegate;

    private LazyDataModel lazyModel;
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    private String nombre;
    private EstadoComun estado;
    private  String idCategoria;
    private String idProgramaPresupuestario;
    private String idProgramaInstitucional;
    
    
    private List<Indicador> historico;
    
    
    @PostConstruct
    public void init() {
        filterTable();
    }

    
    /**
     * Este método inicia los filtros de la consulta
     */
    private void initFilter(){
        nombre = null;
        estado = null;
        idCategoria = null;
        idProgramaPresupuestario= null;
        idProgramaInstitucional = null;
    }


    
    /**
     * Este método corresponde al evento de consulta y obtiene el resultado
     * según el filtro aplicado
     */
    public void filterTable() {
        try {
            List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();
            if (!TextUtils.isEmpty(nombre)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO( MatchCriteriaTO.types.CONTAINS, "nombre", nombre);
                criterios.add(criterio);
            }
            
            if (estado != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO( MatchCriteriaTO.types.EQUALS, "estado", estado);
                criterios.add(criterio);
            }
            
            
            if (!TextUtils.isEmpty(idCategoria)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO( MatchCriteriaTO.types.EQUALS, "tipo.id", Integer.valueOf(idCategoria));
                criterios.add(criterio);
            }
            
            if (!TextUtils.isEmpty(idProgramaPresupuestario)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO( MatchCriteriaTO.types.EQUALS, "programasIndicador.programa.id", Integer.valueOf(idProgramaPresupuestario));
                criterios.add(criterio);
            }
            if (!TextUtils.isEmpty(idProgramaInstitucional)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO( MatchCriteriaTO.types.EQUALS, "programasIndicador.programa.id", Integer.valueOf(idProgramaInstitucional));
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

            
            String[] propiedades = {"id", "nombre", "estado", "tipo.nombre"};

            String className = Indicador.class.getName();
            String[] orderBy = {"nombre"};
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
     * Este método vuelve a iniciar los filtros y aplica la consulta
     */
    public void limpiar() {
        initFilter();
        filterTable();
    }
    
    /**
     * Elimina un determinado indicador
     * @param idToEliminar 
     */
    public void eliminar(Integer idToEliminar){
        try {
            indicadorDelegate.eliminarIndicador(Integer.valueOf(idToEliminar));
            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }
    
    /**
     * Obtiene el histórico de un indicador
     * @param id 
     */
    public void cargarHistorico(Integer id) {
        historico = emd.obtenerHistorico(Indicador.class,id);
    }


    // <editor-fold defaultstate="collapsed" desc="getter-setter">   


    public void setLazyModel(LazyDataModel lazyModel) {
        this.lazyModel = lazyModel;
    }

    public String getNombre() {
        return nombre;
    }

    public String getIdProgramaInstitucional() {
        return idProgramaInstitucional;
    }

    public void setIdProgramaInstitucional(String idProgramaInstitucional) {
        this.idProgramaInstitucional = idProgramaInstitucional;
    }

    
    public String getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(String idCategoria) {
        this.idCategoria = idCategoria;
    }

    public EstadoComun getEstado() {
        return estado;
    }

    public List<Indicador> getHistorico() {
        return historico;
    }

    public void setHistorico(List<Indicador> historico) {
        this.historico = historico;
    }

    public void setEstado(EstadoComun estado) {
        this.estado = estado;
    }

    public LazyDataModel getLazyModel() {
        return lazyModel;
    }

    public String getIdProgramaPresupuestario() {
        return idProgramaPresupuestario;
    }

    public void setIdProgramaPresupuestario(String idProgramaPresupuestario) {
        this.idProgramaPresupuestario = idProgramaPresupuestario;
    }

    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public EntityManagementDelegate getEmd() {
        return emd;
    }

    public void setEmd(EntityManagementDelegate emd) {
        this.emd = emd;
    }



    // </editor-fold>
}
