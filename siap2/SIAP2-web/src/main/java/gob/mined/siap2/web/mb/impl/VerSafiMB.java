/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.CompromisoPresupuestario;
import gob.mined.siap2.entities.data.impl.CompromisoSafi;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.InformacionCompromisoDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.TextMB;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 * Este backing bean implementa los eventos y lógica de presentación de la 
 * página de ver los datos de SAFI
 * 
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "verSafiMB")
public class VerSafiMB implements Serializable {


    @Inject
    JSFUtils jSFUtils;
    @Inject
    TextMB textMB;
    @Inject
    EntityManagementDelegate emd;
    @Inject
    InformacionCompromisoDelegate informacionCompromisoDelegate;
    
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
    
    private String idCompromiso;
    private CompromisoPresupuestario objeto;
    List<CompromisoSafi> compromisosSafi;


    @PostConstruct
    public void init() {
        idCompromiso = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (!TextUtils.isEmpty(idCompromiso)) {
            objeto = (CompromisoPresupuestario) emd.getEntityById(CompromisoPresupuestario.class.getName(), new Integer(idCompromiso));
            if (objeto != null && objeto.getNumeroSAFI() != null && objeto.getNumeroSAFI().trim().length()>0){
                compromisosSafi = informacionCompromisoDelegate.obtenerCompromisosSafi(objeto.getNumeroSAFI());
            }
        }
    }
    


    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public String getIdCompromiso() {
        return idCompromiso;
    }

    public void setIdCompromiso(String idCompromiso) {
        this.idCompromiso = idCompromiso;
    }

    public List<CompromisoSafi> getCompromisosSafi() {
        return compromisosSafi;
    }

    public void setCompromisosSafi(List<CompromisoSafi> compromisosSafi) {
        this.compromisosSafi = compromisosSafi;
    }


    public CompromisoPresupuestario getObjeto() {
        return objeto;
    }

    public void setObjeto(CompromisoPresupuestario objeto) {
        this.objeto = objeto;
    }

    // </editor-fold>




}
