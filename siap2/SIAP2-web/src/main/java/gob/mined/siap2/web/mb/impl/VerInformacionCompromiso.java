/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.InfCompromiso;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.impl.InformacionCompromisoDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.TextMB;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;

/**
 * Este backing bean implementa los eventos y lógica de presentación de la página
 * de información de compromiso 
 * 
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "verInformacionCompromiso")
public class VerInformacionCompromiso implements Serializable {//extends POProyectoConLineasAbstract implements Serializable {


    @Inject
    JSFUtils jSFUtils;
    @Inject
    TextMB textMB;
    @Inject
    InformacionCompromisoDelegate informacionCompromisoDelegate;
    
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
    
    private String idCompromiso;
    private InfCompromiso objeto;
    private List<SelectItem> nombresPlanillas = new ArrayList<>();


    @PostConstruct
    public void init() {
        idCompromiso = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (!TextUtils.isEmpty(idCompromiso)) {
            objeto = informacionCompromisoDelegate.getCompromiso(Integer.valueOf(idCompromiso));
        }
        
    }
    


    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public String getIdCompromiso() {
        return idCompromiso;
    }

    public void setIdCompromiso(String idCompromiso) {
        this.idCompromiso = idCompromiso;
    }

    public List<SelectItem> getNombresPlanillas() {
        return nombresPlanillas;
    }

    public void setNombresPlanillas(List<SelectItem> nombresPlanillas) {
        this.nombresPlanillas = nombresPlanillas;
    }
    
    

    public InfCompromiso getObjeto() {
        return objeto;
    }

    public void setObjeto(InfCompromiso objeto) {
        this.objeto = objeto;
    }

    // </editor-fold>




}
