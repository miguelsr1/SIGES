/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.business.utils.TipoSeguimientoUtils;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.PeriodoSeguimientoIndicadores;
import gob.mined.siap2.entities.enums.TipoSeguimiento;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.FormulacionPorAnioFiscalDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.TextMB;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
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
@Named(value = "periodoDeSeguimientoDeIndicadoresCE")
public class PeriodoDeSeguimientoDeIndicadoresCrearEditar implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @Inject
    JSFUtils jSFUtils;
    @Inject
    TextMB textMB;

    @Inject
    EntityManagementDelegate emd;
    @Inject
    FormulacionPorAnioFiscalDelegate formulacionPorAnioFiscalDelegate;

    private boolean update = false;
    private PeriodoSeguimientoIndicadores objeto;
    private String idAnioFiscal = null;
   


    @PostConstruct
    public void init() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (!TextUtils.isEmpty(id)) {
            update = true;
            objeto = (PeriodoSeguimientoIndicadores) emd.getEntityById(PeriodoSeguimientoIndicadores.class.getName(), Integer.valueOf(id));
            

        } else {
            objeto = new PeriodoSeguimientoIndicadores();
            objeto.setHabilitadoAnual(false);
            objeto.setHabilitadoCuatrimestral(false);
            objeto.setHabilitadoMensual(false);
            objeto.setHabilitadoSemestral(false);
            objeto.setHabilitadoTrimestral(false);
            objeto.setAplicaProyectosAdministrarivos(false);
            objeto.setAplicaProyectosDeInversion(false);
            
        }
        
        if (objeto.getAnioFiscal()!=null){
            idAnioFiscal = objeto.getAnioFiscal().getId().toString();
        }
    }

    
     /**
      * Guarda el objeto en edición
      * 
      * @return 
      */
    public String guardar()  {
        try {
            AnioFiscal anio = (AnioFiscal) emd.getEntityById(AnioFiscal.class.getName(), Integer.valueOf(idAnioFiscal));
            this.objeto.setAnioFiscal(anio);
            
            
            emd.saveOrUpdate(this.objeto);
            return "consultaPeriodoDeSeguimientoDeIndicadores.xhtml?faces-redirect=true";     
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
     * Dirige el sitio a la página de consulta de periodo de seguimiento de indicadores
     * @return 
     */
    public String cerrar() {
        return "consultaPeriodoDeSeguimientoDeIndicadores.xhtml?faces-redirect=true";     
    }
    
    /**
     * Devuelve la lista de nombres de los tipos de seguimientos existentes
     * @param seguimiento
     * @return 
     */
    public Map<String, Integer> getListNombres(TipoSeguimiento seguimiento){
        Map<String, Integer>  map = new LinkedHashMap();
        List<String> nombres =TipoSeguimientoUtils.getListName(seguimiento);
        for (int i =0; i<nombres.size(); i++){
            map.put(textMB.obtenerTexto(nombres.get(i)), i);
        }
        return map;
    }

    /**
     * Devuelve el nombre de un indicador
     * @param seguimiento
     * @param posicion
     * @return 
     */
    public String getNombreIndicador(TipoSeguimiento seguimiento, Integer posicion){
         List<String> nombres =TipoSeguimientoUtils.getListName(seguimiento);
         return textMB.obtenerTexto(nombres.get(posicion));
    }
    
    
    
    
    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    
    
    
    public JSFUtils getjSFUtils() {
        return jSFUtils;
    }

    public void setjSFUtils(JSFUtils jSFUtils) {
        this.jSFUtils = jSFUtils;
    }


    public static Logger getLogger() {
        return logger;
    }

    public PeriodoSeguimientoIndicadores getObjeto() {
        return objeto;
    }

    public void setObjeto(PeriodoSeguimientoIndicadores objeto) {
        this.objeto = objeto;
    }
  
    
    public EntityManagementDelegate getEmd() {
        return emd;
    }

    public void setEmd(EntityManagementDelegate emd) {
        this.emd = emd;
    }

    public boolean isUpdate() {
        return update;
    }

    public String getIdAnioFiscal() {
        return idAnioFiscal;
    }

    public void setIdAnioFiscal(String idAnioFiscal) {
        this.idAnioFiscal = idAnioFiscal;
    }

    
    
    public void setUpdate(boolean update) {
        this.update = update;
    }


    // </editor-fold>
    
  

}
