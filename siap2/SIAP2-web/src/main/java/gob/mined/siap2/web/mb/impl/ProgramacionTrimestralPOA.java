/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.ProgramacionTrimestral;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.web.delegates.impl.ProgramacionTrimestralDelegate;
import gob.mined.siap2.web.delegates.impl.VersionesDelegate;
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

/**
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "programacionTrimestral")
public class ProgramacionTrimestralPOA implements Serializable {
    @Inject
    JSFUtils jSFUtils;
    
    @Inject
    ProgramacionTrimestralDelegate progTrimestralDelegate;
    
    @Inject
    VersionesDelegate versionDelegate;
    
    private ProgramacionTrimestral editando = new ProgramacionTrimestral();
    
    private LazyDataModel lazyModel;
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
    private Integer anioFiscalId;
    private Integer anioFiscalIdNew;
    @PostConstruct
    public void init() {
        filterTable();
    }
    
    /**
     * Este método inicia los filtros de la consulta
     */
    private void initFilter(){
        anioFiscalId = 0;
    }
    
    /**
     * Este método corresponde al evento de consulta y obtiene el resultado
     * según el filtro aplicado
     */
    public void filterTable() {
        try {

            List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();


            if (anioFiscalId != null && anioFiscalId > 0) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "anio.id", anioFiscalId);
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

            String[] propiedades = {"id", "anio.anio", "fechaDesdePrimerTrimestre","fechaHastaPrimerTrimestre","fechaDesdeSegundoTrimestre",
                "fechaHastaSegundoTrimestre","fechaDesdeTercerTrimestre","fechaHastaTercerTrimestre","fechaDesdeCuartoTrimestre","fechaHastaCuartoTrimestre"};

            String className = ProgramacionTrimestral.class.getName();
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
     * Guarda el poa en edición
     *
     * @return
     */
    public String guardar() {
        try {
            if(anioFiscalIdNew != null && anioFiscalIdNew > 0) {
                editando.setAnio(versionDelegate.getAniosFiscalesPorId(anioFiscalIdNew));  
            } else {
                editando.setAnio(null);
            }
            editando = progTrimestralDelegate.guardar(editando);
            RequestContext.getCurrentInstance().execute("$('#editModal').modal('hide');");
            filterTable();
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
        return null;
    }
    
    /**
     * Guarda la unidad en edición
     * 
     * @param id 
     */
    public void eliminar() {
        try {
            if(editando != null && editando.getId() != null) {
                progTrimestralDelegate.eliminar(editando.getId());
            } else{
                jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_NO_POSIBLE_ELIMINAR);
            }
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
     * Guarda la unidad en edición
     * 
     * @param id 
     */
    public void editar(Integer id) {
        try {
            if (id != null) {
                editando = progTrimestralDelegate.obtenerPorId(id);
                if (editando != null) {
                    anioFiscalIdNew = editando.getAnio() != null ? editando.getAnio().getId() : null;
                } else {
                    BusinessException bs = new BusinessException("No se pudo determinar la POA.");
                    bs.addError(ConstantesErrores.ERR_GEST_UNIDAD_TECNICA_DETERMINADA);
                    throw bs;
                }
            } else {
                BusinessException bs = new BusinessException("No se pudo determinar el identificador de la POA");
                bs.addError(ConstantesErrores.ERR_GEST_UNIDAD_TECNICA_ID_NO_DETERMINADO);
                throw bs;
            }

        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }
    
    /**
     * Este método verifica las relaciones que tiene el registro Subcomponente
     * @param id 
     */
    public void verificarEliminar(Integer id){
        try {
            editando = progTrimestralDelegate.obtenerPorId(id);
            if(editando != null) {
                RequestContext.getCurrentInstance().update("formEliminar");
                RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('show');");
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }
    
    public void crearPeriodoSeguimientoPOA() throws Exception{
        editando = new ProgramacionTrimestral();
    }

    public LazyDataModel getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel lazyModel) {
        this.lazyModel = lazyModel;
    }

    public ProgramacionTrimestral getEditando() {
        return editando;
    }

    public void setEditando(ProgramacionTrimestral editando) {
        this.editando = editando;
    }

    public Integer getAnioFiscalId() {
        return anioFiscalId;
    }

    public void setAnioFiscalId(Integer anioFiscalId) {
        this.anioFiscalId = anioFiscalId;
    }

    public Integer getAnioFiscalIdNew() {
        return anioFiscalIdNew;
    }

    public void setAnioFiscalIdNew(Integer anioFiscalIdNew) {
        this.anioFiscalIdNew = anioFiscalIdNew;
    }
    
    
    
}
