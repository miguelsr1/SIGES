/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl.po;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.ActividadAsignacionNP;
import gob.mined.siap2.entities.data.impl.AsignacionNoProgramable;
import gob.mined.siap2.entities.data.impl.GeneralPOA;
import gob.mined.siap2.entities.data.impl.POActividadAsignacionNoProgramable;
import gob.mined.siap2.entities.data.impl.POActividadBase;
import gob.mined.siap2.entities.enums.EstadoActividadPOA;
import gob.mined.siap2.entities.enums.TipoMontoPorAnio;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.VersionesDelegate;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;

/**
 * Este backing bean implementa los eventos y lógica de presentación de la página
 * que trabaja con los POA de asignaciones no programables.
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "poaAsignacionesNoProgramablesCE")
public class POAAsignacionesNoProgramablesCE extends POAConActividadesBasico implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @Inject
    EntityManagementDelegate emd;
    @Inject
    VersionesDelegate versionDelegate;

    private String idAsignacionNoProgramable;
    private Integer idActividadNP;    
    private Boolean vuelveABandejaDeEntrada;

    @PostConstruct
    public void init() {
        super.init();
        idAsignacionNoProgramable = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        idAnioFiscal = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idAnioFiscal");
        idUnidadTecnica = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idUnidadTecnica");        
        vuelveABandejaDeEntrada = idUnidadTecnica != null;
        if (TextUtils.isEmpty(idUnidadTecnica) && usuarioUnidadTecnicas.size() == 1) {
            this.idUnidadTecnica = usuarioUnidadTecnicas.get(0).getId().toString();
        }
        if (!TextUtils.isEmpty(idAsignacionNoProgramable)) {
            update = true;
            objeto = versionDelegate.getAsignacionNoProgramable(Integer.valueOf(idAsignacionNoProgramable));
        }

        initAsignacionNoProgramable();
    }

    
    /**
     * Este método  se utiliza para realizar la inicialización
     */
    public void initAsignacionNoProgramable() {
        try {
            poa = null;
            if (!TextUtils.isEmpty(idAnioFiscal) && !TextUtils.isEmpty(idUnidadTecnica)) {
                reloadPO();
            }
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            //RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    
    /**
     * Retorna el POA en edición
     * 
     * @return 
     */
    @Override
    public GeneralPOA getPOAEnTrabajo() {
        return poa;
    }

    /**
     * Este método vuelve a recargar el POA
     */
    @Override
    public void reloadPO() {
        poa = pOAConActividadesDelegate.getPOATrabajo(objeto.getId(), Integer.valueOf(idAnioFiscal), Integer.valueOf(idUnidadTecnica));
    }


    /**
     * Retorna el objeto del POA en edición
     * 
     * @return 
     */
    public AsignacionNoProgramable getObjeto() {
        return (AsignacionNoProgramable) objeto;
    }

    /**
     * Envía el POA para validación
     * 
     * @throws IOException 
     */
    public void enviar() throws IOException {
        try {
            podaAConActividadesDelegate.enviarPOAConActividades(objeto.getId(), poa.getId(), TipoMontoPorAnio.ASIGN_NO_PROGRAMABLE);
            FacesContext.getCurrentInstance().getExternalContext().redirect("consultaPOAAccionCentral.xhtml");
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
     * Este método se encarga de validar el POA y genera una linea base
     * 
     * @throws IOException 
     */
    public void generarLineaBase() throws IOException {
        try {
            podaAConActividadesDelegate.validarPOA(poa.getId());
            FacesContext.getCurrentInstance().getExternalContext().redirect("consultaNotificaciones.xhtml");
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
     * Este método se utiliza para rechazar el POA que se esta validando
     * 
     * @throws IOException 
     */
    public void rechazarPOA() throws IOException {
        try {
            podaAConActividadesDelegate.rechazarPOA(poa.getId(), objeto.getId(), motivoRechazo, TipoMontoPorAnio.ASIGN_NO_PROGRAMABLE);
            FacesContext.getCurrentInstance().getExternalContext().redirect("consultaNotificaciones.xhtml");
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
     * Se sobre escribe el método de inicializar una actividad, ya que las 
     * actividades de los distintos tipos POAs son distintas
     */
    @Override
    public void initActividad() {
        if (tempActividad == null) {
            tempActividad = new POActividadAsignacionNoProgramable();
            tempActividad.setEstado(EstadoActividadPOA.NO_FINALIZADA);
            tempActividad.setInsumos(new LinkedList());
        }
        POActividadAsignacionNoProgramable tanp = (POActividadAsignacionNoProgramable) tempActividad;
        if (tanp.getResponsable() == null) {
            idUsuarioActividad = null;
        } else {
            idUsuarioActividad = tanp.getResponsable().getUsuId().toString();
        }
        if (tanp.getActividadNP() == null) {
            idUsuarioActividad = null;
        } else {
            idActividadNP = tanp.getActividadNP().getId();
        }
    }

    /**
     * Se sobre escribe el método de retornar la actividad en edición, ya que las 
     * actividades de los distintos tipos POAs son distintas
     * 
     * @return 
     */
    @Override
    public POActividadAsignacionNoProgramable getTempActividad() {
        return (POActividadAsignacionNoProgramable) tempActividad;
    }

    
    /**
     * Se sobre escribe el método de guardar la actividad en edición, ya que las 
     * actividades de los distintos tipos POAs son distintas
     * 
     * @param actividad
     */    
    @Override
    public void guardarActividad(POActividadBase actividad) {
        if (!poa.getActividades().contains(actividad)) {
            poa.getActividades().add(actividad);
        }
        ActividadAsignacionNP anp = (ActividadAsignacionNP) emd.getEntityById(ActividadAsignacionNP.class.getName(), idActividadNP);
        ((POActividadAsignacionNoProgramable) actividad).setActividadNP(anp);
    }

    /**
     * Retorna el tipo de POA con el que se esta trabajando
     * 
     * @return 
     */
    @Override
    public String getTipoPO() {
        return POConActividadesEInsumosAbstract.TIPO_PO_ASIGNACION_NP;
    }
    
    /**
     * Vuelve a la página anterior
     * 
     * @return 
     */
    public String volver(){
        String urlVuelta = "";
        if(vuelveABandejaDeEntrada){
            urlVuelta = "consultaNotificaciones.xhtml?faces-redirect=true";
        } else {
            urlVuelta = "consultaPOAAsignacionesNoProgramables.xhtml?faces-redirect=true";
        }
        return urlVuelta;
    }

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public EntityManagementDelegate getEmd() {
        return emd;
    }

    public void setEmd(EntityManagementDelegate emd) {
        this.emd = emd;
    }

    public VersionesDelegate getVersionDelegate() {
        return versionDelegate;
    }

    public Integer getIdActividadNP() {
        return idActividadNP;
    }

    public void setIdActividadNP(Integer idActividadNP) {
        this.idActividadNP = idActividadNP;
    }

    public void setVersionDelegate(VersionesDelegate versionDelegate) {
        this.versionDelegate = versionDelegate;
    }

    // </editor-fold>
}
