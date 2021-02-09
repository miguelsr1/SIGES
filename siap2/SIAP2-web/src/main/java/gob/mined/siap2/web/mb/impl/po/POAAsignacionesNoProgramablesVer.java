/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl.po;

import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.AsignacionNoProgramable;
import gob.mined.siap2.entities.data.impl.POActividadAsignacionNoProgramable;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.VersionesDelegate;
import java.io.Serializable;
import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 * Este backing bean implementa los eventos y lógica de presentación de la página
 * que visualiza un POA de asignación no programable.
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "poaAsignacionesNoProgramableVer")
public class POAAsignacionesNoProgramablesVer extends POAConActividadesBasico implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @Inject
    EntityManagementDelegate emd;
    @Inject
    VersionesDelegate versionDelegate;


    private String idAsignacionNoProgramable;
    private Integer idActividadNP;
    private String idPOA;


    @PostConstruct
    public void init() {
        super.init();
        idAsignacionNoProgramable = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        idAnioFiscal = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idAnioFiscal");
        idUnidadTecnica = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idUnidadTecnica");
        idPOA  = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idPOA");
        if (TextUtils.isEmpty(idUnidadTecnica) && usuarioUnidadTecnicas.size() == 1) {
            this.idUnidadTecnica = usuarioUnidadTecnicas.get(0).getId().toString();
        }
        if (!TextUtils.isEmpty(idAsignacionNoProgramable)) {
            update = true;
            objeto = versionDelegate.getAsignacionNoProgramable(Integer.valueOf(idAsignacionNoProgramable));
        }

        initAsignacionNoProgramableBase();
    }

    /**
     * Este método se utiliza para incializar
     */
    public void initAsignacionNoProgramableBase() {
        poa = null;
        if (!TextUtils.isEmpty(idAnioFiscal) && !TextUtils.isEmpty(idUnidadTecnica)&& !TextUtils.isEmpty(idPOA)) {
            poa = pOAConActividadesDelegate.getPOAEnLineaBase(Integer.valueOf(idPOA));
        }
    }

    /**
     * Retorna el objeto correspondiente al POA en edición
     * 
     * @return 
     */
    public AsignacionNoProgramable getObjeto() {
        return (AsignacionNoProgramable) objeto;
    }
   
       
    /**
     * Retorna las lineas base del POA en edición
     * @return 
     */
    public Map<String, Integer> getLineasBase(){
       if (!TextUtils.isEmpty(idAnioFiscal) && !TextUtils.isEmpty(idUnidadTecnica) ) {
           return pOAConActividadesDelegate.getPOASEnLineaBase(objeto.getId(), Integer.valueOf(idAnioFiscal), Integer.valueOf(idUnidadTecnica));
       }
       return null;
    }
    

    /**
     * Se sobre sobre-escribe el método que retorna la actividad. Ya que todos
     * los POA tienen actividades de tipo distinto
     * 
     * @return 
     */
    @Override
    public POActividadAsignacionNoProgramable getTempActividad() {
        return (POActividadAsignacionNoProgramable) tempActividad;
    }

 
    /**
     * retorna el tipo de POA con el que se esta trabajando
     * 
     * @return 
     */
    @Override
    public String getTipoPO() {
         return POConActividadesEInsumosAbstract.TIPO_PO_ASIGNACION_NP;
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

    public String getIdAsignacionNoProgramable() {
        return idAsignacionNoProgramable;
    }

    public void setIdAsignacionNoProgramable(String idAsignacionNoProgramable) {
        this.idAsignacionNoProgramable = idAsignacionNoProgramable;
    }

    public String getIdPOA() {
        return idPOA;
    }

    public void setIdPOA(String idPOA) {
        this.idPOA = idPOA;
    }

    public void setVersionDelegate(VersionesDelegate versionDelegate) {
        this.versionDelegate = versionDelegate;
    }

    // </editor-fold>
}
