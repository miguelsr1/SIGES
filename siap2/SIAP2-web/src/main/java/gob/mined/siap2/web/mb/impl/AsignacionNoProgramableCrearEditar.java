/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.ActividadAsignacionNP;
import gob.mined.siap2.entities.data.impl.AsignacionNoProgramable;
import gob.mined.siap2.entities.data.impl.ClasificadorFuncional;
import gob.mined.siap2.entities.data.impl.LineaEstrategica;
import gob.mined.siap2.entities.data.impl.TechoAccionCentral;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.AsignacionNoProgramableDelegate;
import gob.mined.siap2.web.delegates.impl.ReporteDelegate;
import gob.mined.siap2.web.delegates.impl.VersionesDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.PermisosMB;
import gob.mined.siap2.web.mb.UtilsMB;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
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
import org.primefaces.model.DualListModel;

/**
 * Esta clase implementa el backing bean correspondiente al crear o editar de
 * una asignación no programable.
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "asignacionNoProgramableCE")
public class AsignacionNoProgramableCrearEditar implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @Inject
    PermisosMB permisosMB;
    @Inject
    JSFUtils jSFUtils;
    @Inject
    UtilsMB utilsMB;

    @Inject
    EntityManagementDelegate emd;
    @Inject
    VersionesDelegate versionDelegate;
    @Inject
    AsignacionNoProgramableDelegate asignacionD;

    @Inject
    private ReporteDelegate reporteDelegate;

    private boolean update = false;
    private AsignacionNoProgramable objeto;
    private TechoAccionCentral tempMontoUnidadTecnica;
    private ActividadAsignacionNP tempActividadAsignacionNP;
    protected DualListModel<LineaEstrategica> lineasEstrategicas;
    private String idClasificadorFuncional;
    private boolean newActividad;
    private String idPlanificacion;
    private String idUnidadTecnica;

    @PostConstruct
    public void init() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        idPlanificacion = null;
        if (!TextUtils.isEmpty(id)) {
            update = true;
            objeto = versionDelegate.getAsignacionNoProgramable(Integer.valueOf(id));
            if (objeto.getPlanificacionEstrategica() != null) {
                idPlanificacion = String.valueOf(objeto.getPlanificacionEstrategica().getId());
            }
            if (objeto.getClasificadorFuncional() != null) {
                idClasificadorFuncional = String.valueOf(objeto.getClasificadorFuncional().getId());
            }
            if (objeto.getUnidadTecnica() != null) {
                idUnidadTecnica = String.valueOf(objeto.getUnidadTecnica().getId());
            }
            loadLineasDisponibles(true);
        } else {
            objeto = new AsignacionNoProgramable();
            objeto.setActividadesNP(new LinkedList());
            objeto.setLineasEstrategicas(new HashSet<LineaEstrategica>());
            loadLineasDisponibles(false);
        }
    }

    /**
     * Este método corresponde al evento guardar. Realiza las validaciones en la
     * capa de presentación y delega a la capa lógica.
     *
     * @return
     */
    public String guardar() {
        try {
            //se carga el clasificadorFuncioal
            if (TextUtils.isEmpty(idClasificadorFuncional)) {
                objeto.setClasificadorFuncional(null);
            } else {
                ClasificadorFuncional c = (ClasificadorFuncional) emd.getEntityById(ClasificadorFuncional.class.getName(), Integer.valueOf(idClasificadorFuncional));
                objeto.setClasificadorFuncional(c);
            }
            UnidadTecnica u = null;
            if (!TextUtils.isEmpty(idUnidadTecnica)) {
                u = (UnidadTecnica) emd.getEntityById(UnidadTecnica.class.getName(), Integer.valueOf(idUnidadTecnica));
            }
            objeto.setUnidadTecnica(u);
            objeto.setLineasEstrategicas(new HashSet(lineasEstrategicas.getTarget()));
            asignacionD.crearOActualizarAsignacionNoProgramable(Integer.valueOf(idPlanificacion), objeto);
            return "consultaAsignacionesNoProgramables.xhtml?faces-redirect=true";
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;
    }

    public String cerrar() {
        return "consultaAsignacionesNoProgramables.xhtml?faces-redirect=true";
    }

    /**
     * Este método carga las líneas disponibles.
     *
     * @param conservarOriginales
     */
    public void loadLineasDisponibles(boolean conservarOriginales) {
        List<LineaEstrategica> disponibles = new LinkedList<>();
        if (!TextUtils.isEmpty(idPlanificacion)) {
            disponibles = versionDelegate.getLineasEstrategicasVigetnes(Integer.valueOf(idPlanificacion));
        }
        List<LineaEstrategica> enUso = new LinkedList<>();
        if (conservarOriginales) {
            for (LineaEstrategica l : objeto.getLineasEstrategicas()) {
                enUso.add(l);
                if (disponibles.contains(l)) {
                    disponibles.remove(l);
                }
            }

        }
        lineasEstrategicas = new DualListModel<>(disponibles, enUso);
    }

    /**
     * Carga las actividad de una asignación no programable
     * 
     * @param rowIndex 
     */
    public void loadActividad(String rowIndex) {
        if (TextUtils.isEmpty(rowIndex)) {
            newActividad = true;
            tempActividadAsignacionNP = new ActividadAsignacionNP();
        } else {
            newActividad = false;
            //Cargo tempActividadAsignacionNP con el valor correspondiente a rowIndex
            for (int i = 0; i < objeto.getActividadesNP().size(); i++) {
                if (i == Integer.valueOf(rowIndex).intValue()) {
                    tempActividadAsignacionNP = objeto.getActividadesNP().get(i);
                    break;
                }
            }
        }
        
    }

    /**
     * Este método elimina una de las actividades de una asignación no programable.
     * @param rowIndex 
     */
    public void eliminarActividad(Integer rowIndex) {
        try {
            objeto.getActividadesNP().remove(rowIndex.intValue());
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
     * Este método corresponde al evento de agregar una actividad.
     */
    public void addActividad() {
        try {
            if (newActividad) {
                objeto.getActividadesNP().add(tempActividadAsignacionNP);
            }
            RequestContext.getCurrentInstance().execute("$('#addActividad').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

 
    /**
     * Este método corresponde al evento de descargar el reporte de una asignación no programable.
     */
    public void descargarReporte() {
        try {
            byte[] bytespdf = reporteDelegate.generarReporteAsignacionNoProgramable(objeto.getId());
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

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public JSFUtils getjSFUtils() {
        return jSFUtils;
    }

    public void setjSFUtils(JSFUtils jSFUtils) {
        this.jSFUtils = jSFUtils;
    }

    public EntityManagementDelegate getEmd() {
        return emd;
    }

    public String getIdUnidadTecnica() {
        return idUnidadTecnica;
    }

    public void setIdUnidadTecnica(String idUnidadTecnica) {
        this.idUnidadTecnica = idUnidadTecnica;
    }

    public String getIdClasificadorFuncional() {
        return idClasificadorFuncional;
    }

    public void setIdClasificadorFuncional(String idClasificadorFuncional) {
        this.idClasificadorFuncional = idClasificadorFuncional;
    }

    public TechoAccionCentral getTempMontoUnidadTecnica() {
        return tempMontoUnidadTecnica;
    }

    public void setTempMontoUnidadTecnica(TechoAccionCentral tempMontoUnidadTecnica) {
        this.tempMontoUnidadTecnica = tempMontoUnidadTecnica;
    }

    public String getIdPlanificacion() {
        return idPlanificacion;
    }

    public void setIdPlanificacion(String idPlanificacion) {
        this.idPlanificacion = idPlanificacion;
    }

    public ActividadAsignacionNP getTempActividadAsignacionNP() {
        return tempActividadAsignacionNP;
    }

    public void setTempActividadAsignacionNP(ActividadAsignacionNP tempActividadAsignacionNP) {
        this.tempActividadAsignacionNP = tempActividadAsignacionNP;
    }

    public DualListModel<LineaEstrategica> getLineasEstrategicas() {
        return lineasEstrategicas;
    }

    public void setLineasEstrategicas(DualListModel<LineaEstrategica> lineasEstrategicas) {
        this.lineasEstrategicas = lineasEstrategicas;
    }

    public void setEmd(EntityManagementDelegate emd) {
        this.emd = emd;
    }

    public AsignacionNoProgramable getObjeto() {
        return objeto;
    }

    public void setObjeto(AsignacionNoProgramable objeto) {
        this.objeto = objeto;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    // </editor-fold>
}
