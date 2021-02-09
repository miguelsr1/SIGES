/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.MetodoAdquisicion;
import gob.mined.siap2.entities.data.impl.PAC;
import gob.mined.siap2.entities.data.impl.PACGrupo;
import gob.mined.siap2.entities.enums.EstadoGrupoPAC;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.GanttDelegate;
import gob.mined.siap2.web.delegates.impl.PACDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.TextMB;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "pacGrupoCE")
public class PACGrupoCrearEditar implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @Inject
    PACDelegate pACDelegate;
    @Inject
    JSFUtils jSFUtils;
    @Inject
    GanttDelegate ganttDelegate;
    @Inject
    TextMB textMB;


    @Inject
    EntityManagementDelegate emd;

    private boolean update = false;
    private PACGrupo tempGrupo;
    private PAC pac;
    private String idMetodoAdquisicion;
    private String ganttJson;
    
    @PostConstruct
    public void init() {
        
        String idGrupo = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idGrupo");
        String idPacStr = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idPac");
        pac = (PAC) emd.getEntityById(PAC.class.getName(), Integer.valueOf(idPacStr));

       initGrupo(idGrupo);
    }

    /**
     * Inicializa un grupo de un PAC
     * @param idGrupo 
     */
    private void initGrupo(String idGrupo){
         if (!TextUtils.isEmpty(idGrupo)) {
            tempGrupo = pACDelegate.getGrupo(Integer.valueOf(idGrupo));
            if (tempGrupo.getGantt()!= null){
                ganttJson = ganttDelegate.getGantt(tempGrupo.getGantt().getId());
            }
            if (tempGrupo.getMetodoAdquisicion()!= null){
                idMetodoAdquisicion = tempGrupo.getMetodoAdquisicion().getId().toString();
            }
        } else {
            update = true;
            tempGrupo = new PACGrupo();
            tempGrupo.setEstado(EstadoGrupoPAC.PENDIENTE);
            tempGrupo.setInsumos(new LinkedList());
            tempGrupo.setTotal(BigDecimal.ZERO);
            tempGrupo.setIniciado(false);
        }
    }
    
    /**
     * Elimina un grupo de un PAC
     * @return 
     */
    public String eliminarGrupo() {
        try {
            if (null != tempGrupo.getId()) {
                pACDelegate.eliminarGrupo(tempGrupo.getId());
            }
            return "crearEditarPAC.xhtml?faces-redirect=true&id=" + pac.getId();
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

    /**
     * Guarda un grupo de un PAC
     */
    public void guardarGrupo() {
        try {
            tempGrupo =pACDelegate.guardarGrupo(tempGrupo, pac.getId(), Integer.valueOf(idMetodoAdquisicion), ganttJson);
            initGrupo(tempGrupo.getId().toString());
                    
            String texto = textMB.obtenerTexto("labels.CambiosGuardadosCorrectamente");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, texto, texto));
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
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
     * Cierra un grupo de un PAC
     * @return 
     */
    public String cerrarGrupo() {
        try {
            
            tempGrupo =pACDelegate.cambiarEstadoGrupo( pac.getId(), tempGrupo.getId(),EstadoGrupoPAC.CERRADO);
            regenerarGantt();
            
            return "crearEditarPAC.xhtml?faces-redirect=true&id=" + pac.getId();
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
        
    /**
     * Abre un grupo de un PAC luego de haber estado cerrado
     */
    public void reAbrirGrupo() {
        try {
            tempGrupo =pACDelegate.cambiarEstadoGrupo( pac.getId(), tempGrupo.getId(),EstadoGrupoPAC.PENDIENTE);
           
            regenerarGantt();
            String texto = textMB.obtenerTexto("labels.CambiosGuardadosCorrectamente");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, texto, texto));
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
     * Inicia un grupo de un PAC
     * @return 
     */
    public String iniciarGrupoGrupo() {
        try {
            tempGrupo =pACDelegate.iniciarGrupo(pac.getId(), tempGrupo );
            return "crearEditarPAC.xhtml?faces-redirect=true&id=" + pac.getId();
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
    
    /**
     * Regenera el cronograma asociado a un grupo de un PAC
     */
    public void regenerarGantt(){
        try{
            if (!TextUtils.isEmpty(idMetodoAdquisicion)){
                ganttJson =ganttDelegate.regenerarGantt(Integer.valueOf(idMetodoAdquisicion), new Date());
                RequestContext.getCurrentInstance().execute("loadFromLocalStorage();");
            }
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
     * Devuelve la lista de métodos de adquisición que pueden ser asociados a un grupo de PAC
     * @return 
     */
    public Map<String, String> getMetodosAdquisicion(){
        List <MetodoAdquisicion> l = ganttDelegate.getMetodosCumplenRango(tempGrupo.getTotal(), pac.getAnio());
        
        Map<String, String> map = new HashMap();
        for (MetodoAdquisicion o : l){
            map.put(o.getNombre(), o.getId().toString());
        }
        return map;
    }
    
    /**
     * Devuelve los posibles estados de grupos de PAC
     * @return 
     */
    public EstadoGrupoPAC[] getEstadosGrupos() {
        return EstadoGrupoPAC.values();
    }

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public JSFUtils getjSFUtils() {
        return jSFUtils;
    }

    public void setjSFUtils(JSFUtils jSFUtils) {
        this.jSFUtils = jSFUtils;
    }

    public PACGrupo getTempGrupo() {
        return tempGrupo;
    }

    public void setTempGrupo(PACGrupo tempGrupo) {
        this.tempGrupo = tempGrupo;
    }

    public EntityManagementDelegate getEmd() {
        return emd;
    }

    public PAC getPac() {
        return pac;
    }

    public String getIdMetodoAdquisicion() {
        return idMetodoAdquisicion;
    }

    public void setIdMetodoAdquisicion(String idMetodoAdquisicion) {
        this.idMetodoAdquisicion = idMetodoAdquisicion;
    }

    public String getGanttJson() {
        return ganttJson;
    }

    public void setGanttJson(String ganttJson) {
        this.ganttJson = ganttJson;
    }

    public PACDelegate getpACDelegate() {
        return pACDelegate;
    }

    public void setpACDelegate(PACDelegate pACDelegate) {
        this.pACDelegate = pACDelegate;
    }
    

    
    public void setPac(PAC pac) {
        this.pac = pac;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    // </editor-fold>
}
