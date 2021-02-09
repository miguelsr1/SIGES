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
import gob.mined.siap2.entities.data.impl.POActividad;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.VersionesDelegate;
import java.io.Serializable;
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
 * Este backing bean implementa los eventos y lógica de presentación de la 
 * página que trabaja con la visualización POAS de acciones centrales
 * 
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "poaAccionCentralVer")
public class POAAccionCentralVer extends POAConActividadesBasico implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
    private String idAccionCentral;
    private String idPOA;

    @Inject
    EntityManagementDelegate emd;
    @Inject
    VersionesDelegate versionDelegate;

    @PostConstruct
    public void init() {
        super.init();
        idAccionCentral = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        idAnioFiscal =  FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idAnioFiscal");
        idUnidadTecnica =  FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idUnidadTecnica");
        idPOA  = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idPOA");
        if (TextUtils.isEmpty(idUnidadTecnica) && usuarioUnidadTecnicas.size() == 1){
            this.idUnidadTecnica =  usuarioUnidadTecnicas.get(0).getId().toString();
        } 
        if (!TextUtils.isEmpty(idAccionCentral)) {
            update = true;
            objeto = versionDelegate.getAccionCentral(Integer.valueOf(idAccionCentral));
        }
        initAccionCentralBase();
    }

    /**
     * método que se encarga de inicializar
     */
    public void initAccionCentralBase() {
        try {
            poa = null;
            if (!TextUtils.isEmpty(idAnioFiscal) && !TextUtils.isEmpty(idUnidadTecnica) && !TextUtils.isEmpty(idPOA)) {
                poa = pOAConActividadesDelegate.getPOAEnLineaBase(Integer.valueOf(idPOA));
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
     * Retorna el objeto al que pertenece el POA en edición
     * 
     * @return 
     */
    public AccionCentral getObjeto() {
        return (AccionCentral) objeto;
    }
    
    /**
     * Se sobreescribe el método de retornar la actividad en edición.
     * Ya que se trabaja con una actividad de tipo diferente
     * 
     * @return 
     */
    @Override
    public POActividad getTempActividad() {
        return (POActividad) tempActividad;
    }

    
    /**
     * Retorna las lineas base de posibles del POA en edición
     * 
     * @return 
     */
    public Map<String, Integer> getLineasBase(){
       if (!TextUtils.isEmpty(idAnioFiscal) && !TextUtils.isEmpty(idUnidadTecnica) ) {
           return pOAConActividadesDelegate.getPOASEnLineaBase(objeto.getId(), Integer.valueOf(idAnioFiscal), Integer.valueOf(idUnidadTecnica));
       }
       return null;
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

    public String getIdPOA() {
        return idPOA;
    }

    public void setIdPOA(String idPOA) {
        this.idPOA = idPOA;
    }

    

    // </editor-fold>
}
