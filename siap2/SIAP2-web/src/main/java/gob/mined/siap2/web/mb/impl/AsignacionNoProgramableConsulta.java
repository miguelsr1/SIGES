/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.AsignacionNoProgramable;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.AsignacionNoProgramableDelegate;
import gob.mined.siap2.web.delegates.impl.ReporteDelegate;
import gob.mined.siap2.web.delegates.impl.VersionesDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.PermisosMB;
import gob.mined.siap2.web.utils.dataProvider.EntityReferenceDataProvider;
import gob.mined.siap2.web.utils.dataProvider.GeneralLazyDataModel;
import gob.mined.siap2.web.utils.dataProvider.IDataProvider;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;

/**
 * Esta clase implementa el backing bean de la consulta de asignaciones no programables.
 * @author sofis
 */
@ViewScoped
@Named(value = "asignacionNoProgramableConsulta")
public class AsignacionNoProgramableConsulta implements Serializable {

    @Inject
    PermisosMB permisosMB;
    @Inject
    JSFUtils jSFUtils;
    @Inject
    EntityManagementDelegate emd;
    @Inject
    AsignacionNoProgramableDelegate asignacionD;
    @Inject
    VersionesDelegate versionDelegate;  
    @Inject
    ReporteDelegate reporteDelegate;
    
    private LazyDataModel lazyModel;
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    private String nombre;
    private String planificacionEstrategicaId;
    private String lineaEstrategicaId;


    @PostConstruct
    public void init() {
        filterTable();
    }

    
    /**
     * Este método inicia los filtros de la consulta
     */
    private void initFilter(){
        nombre = null;
        planificacionEstrategicaId = null;
        lineaEstrategicaId = null;
    }


    /**
     * Este método implementa el evento correspondiente a la consulta de asignaciones no programables.
     */
    public void filterTable() {
        try {             
              
            List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();
            if (!TextUtils.isEmpty(nombre)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO( MatchCriteriaTO.types.CONTAINS, "nombre", nombre);
                criterios.add(criterio);
            }
            
            if (!TextUtils.isEmpty(planificacionEstrategicaId)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO( MatchCriteriaTO.types.EQUALS, "planificacionEstrategica.id", Integer.valueOf(planificacionEstrategicaId));
                criterios.add(criterio);
            }
            
            if (!TextUtils.isEmpty(lineaEstrategicaId)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO( MatchCriteriaTO.types.EQUALS, "lineasEstrategicas.id", Integer.valueOf(lineaEstrategicaId));
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

            
            String[] propiedades = {"id", "nombre", "tipo"};

            String className = AsignacionNoProgramable.class.getName();
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
     * Este método corresponde al evento eliminar una asignación no programable
     */
    public void eliminar(Integer idToEliminar){
        try {
            asignacionD.eleiminarAsignacionNoProgramable(Integer.valueOf(idToEliminar));
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
     * Este método permite descargar el archivo de una asignación no programable.
     */
    public void descargarReporte() {
        try {
            byte[] bytespdf = reporteDelegate.generarReporteGralAsignacionNoProgramable();
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            ec.responseReset();

            ec.setResponseContentType("application/pdf");

            ec.setResponseContentLength(bytespdf.length);

            ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + "ReporteAsignacionNoProgramable.pdf" + "\"");
            OutputStream output;
            try {
                output = ec.getResponseOutputStream();
                output.write(bytespdf);
            } catch (IOException ex) {
                logger.log(Level.SEVERE, ex.getMessage(), ex);
                jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            }
 
            fc.responseComplete();

        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }
    
    
    
    //geters

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

    public String getNombre() {
        return nombre;
    }

    public String getPlanificacionEstrategicaId() {
        return planificacionEstrategicaId;
    }

    public void setPlanificacionEstrategicaId(String planificacionEstrategicaId) {
        this.planificacionEstrategicaId = planificacionEstrategicaId;
    }

    public String getLineaEstrategicaId() {
        return lineaEstrategicaId;
    }

    public void setLineaEstrategicaId(String lineaEstrategicaId) {
        this.lineaEstrategicaId = lineaEstrategicaId;
    }
   
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public static Logger getLogger() {
        return logger;
    }

    
   
    public EntityManagementDelegate getEmd() {
        return emd;
    }



    public void setEmd(EntityManagementDelegate emd) {
        this.emd = emd;
    }

  


    // </editor-fold>
    
    
}
