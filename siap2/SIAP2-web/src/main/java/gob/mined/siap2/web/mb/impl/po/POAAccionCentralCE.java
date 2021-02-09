/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl.po;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.AccionCentral;
import gob.mined.siap2.entities.data.impl.GeneralPOA;
import gob.mined.siap2.entities.data.impl.POActividad;
import gob.mined.siap2.entities.enums.TipoMontoPorAnio;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.AccionCentralDelegate;
import gob.mined.siap2.web.delegates.impl.VersionesDelegate;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;

/**
 * Este backing bean implementa los eventos y lógica de presentación de la 
 * página que trabaja con los POAS de acciones centrales
 * 
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "poaAccionCentralCE")
public class POAAccionCentralCE extends POAConActividadesBasico implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
    private String idAccionCentral;

    @Inject
    EntityManagementDelegate emd;
    @Inject
    VersionesDelegate versionDelegate;
    @Inject
    AccionCentralDelegate accionCentralD;
    private Boolean vuelveABandejaDeEntrada;
    

    @PostConstruct
    public void init() {
        super.init();
        idAccionCentral = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        idAnioFiscal = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idAnioFiscal");
        idUnidadTecnica = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idUnidadTecnica");             
        vuelveABandejaDeEntrada = idUnidadTecnica != null;
        if (TextUtils.isEmpty(idUnidadTecnica) && usuarioUnidadTecnicas.size() == 1) {
            this.idUnidadTecnica = usuarioUnidadTecnicas.get(0).getId().toString();
        }
        if (!TextUtils.isEmpty(idAccionCentral)) {
            update = true;
            objeto = versionDelegate.getAccionCentral(Integer.valueOf(idAccionCentral));
        }
        initAccionCentral();
    }

    /**
     * Este método se utiliza para inicializar
     */
    public void initAccionCentral() {
        try {
            poa = null;
            if (!TextUtils.isEmpty(idAnioFiscal) && !TextUtils.isEmpty(idUnidadTecnica)) {
                reloadPO();
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
     * recarga el POA en edición
     */
    @Override
    public void reloadPO() {
        poa = pOAConActividadesDelegate.getPOATrabajo(objeto.getId(), Integer.valueOf(idAnioFiscal), Integer.valueOf(idUnidadTecnica));
    }

    /**
     * El poa que se esta en uso
     * @return 
     */
    @Override
    public GeneralPOA getPOAEnTrabajo() {
        return poa;
    }

    /**
     * Manda el poa en edición para validar
     * @throws IOException 
     */    
    public void enviar() throws IOException {
        try {
            podaAConActividadesDelegate.enviarPOAConActividades(objeto.getId(), poa.getId(), TipoMontoPorAnio.ACCION_CENTRAL);
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
     * Valida el POA en edición
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
     * Rechaza el POA en edición
     * @throws IOException 
     */
    public void rechazarPOA() throws IOException {
        try {
            podaAConActividadesDelegate.rechazarPOA(poa.getId(), objeto.getId(), motivoRechazo, TipoMontoPorAnio.ACCION_CENTRAL);
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
     * Retorna el objeto al que pertenece el POA
     * 
     * @return 
     */
    public AccionCentral getObjeto() {
        return (AccionCentral) objeto;
    }

    /**
     * Retorna la actividad del POA de acción central en edición
     * 
     * @return 
     */
    @Override
    public POActividad getTempActividad() {
        return (POActividad) tempActividad;
    }
    
    /**
     * Retorna a la página anterior
     * @return 
     */
    public String volver(){
        String urlVuelta = "";
        if(vuelveABandejaDeEntrada){
            urlVuelta = "consultaNotificaciones.xhtml?faces-redirect=true";
        } else {
            urlVuelta = "consultaPOAAccionCentral.xhtml?faces-redirect=true";
        }
        return urlVuelta;
    }


    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public String getIdAccionCentral() {
        return idAccionCentral;
    }

    public void setIdAccionCentral(String idAccionCentral) {
        this.idAccionCentral = idAccionCentral;
    }

    public EntityManagementDelegate getEmd() {
        return emd;
    }

    public void setEmd(EntityManagementDelegate emd) {
        this.emd = emd;
    }

    public VersionesDelegate getVersionDelegate() {
        return versionDelegate;
    }

    public void setVersionDelegate(VersionesDelegate versionDelegate) {
        this.versionDelegate = versionDelegate;
    }

    public AccionCentralDelegate getAccionCentralD() {
        return accionCentralD;
    }

    public void setAccionCentralD(AccionCentralDelegate accionCentralD) {
        this.accionCentralD = accionCentralD;
    }

    
    // </editor-fold>
}
