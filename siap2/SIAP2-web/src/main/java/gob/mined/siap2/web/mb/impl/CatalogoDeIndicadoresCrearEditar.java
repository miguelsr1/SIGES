/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.Indicador;
import gob.mined.siap2.entities.enums.EstadoComun;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.IndicadorDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
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
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "catalogoDeIndicadoresCE")
public class CatalogoDeIndicadoresCrearEditar implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @Inject
    JSFUtils jSFUtils;
    @Inject
    EntityManagementDelegate emd;
    @Inject
    IndicadorDelegate indicadorDelegate;

    private boolean update = false;
    private Indicador objeto;
    private Integer idCategoria;
    private Integer idFormaMedicion;
    private String idUnidadMedida=null;

    @PostConstruct
    public void init() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (!TextUtils.isEmpty(id)) {
            update = true;
            objeto = (Indicador) emd.getEntityById(Indicador.class.getName(), Integer.valueOf(id));
            idCategoria = objeto.getTipo().getId();
            idFormaMedicion = objeto.getFormaMedicion().getId();
            if (objeto.getUnidadDeMedida() != null) {
                idUnidadMedida = String.valueOf(objeto.getUnidadDeMedida().getId());
            }

        } else {
            objeto = new Indicador();
            objeto.setEstado(EstadoComun.VIGENTE);
        }
    }

    
     /**
      * Guarda el objeto en edición
      * 
      * @return 
      */
    public String guardar()  {
        try {
            indicadorDelegate.crearActualizarIndicador(objeto, idCategoria, idFormaMedicion, Integer.valueOf(idUnidadMedida));
            return "consultaCatalogoDeIndicadores.xhtml?faces-redirect=true";
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
     * Redirige el sitio a la consulta de indicadores
     * @return 
     */
    public String cerrar() {
        return "consultaCatalogoDeIndicadores.xhtml?faces-redirect=true";
    }

    public Integer sumarUno(String valor) {
        if (TextUtils.isEmpty(valor)) {
            return null;
        }
        return Integer.valueOf(valor) + 1;
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

    public Indicador getObjeto() {
        return objeto;
    }

    public IndicadorDelegate getIndicadorDelegate() {
        return indicadorDelegate;
    }

    public Integer getIdFormaMedicion() {
        return idFormaMedicion;
    }

    public void setIdFormaMedicion(Integer idFormaMedicion) {
        this.idFormaMedicion = idFormaMedicion;
    }

    public void setIndicadorDelegate(IndicadorDelegate indicadorDelegate) {
        this.indicadorDelegate = indicadorDelegate;
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public void setObjeto(Indicador objeto) {
        this.objeto = objeto;
    }

    public String getIdUnidadMedida() {
        return idUnidadMedida;
    }

    public void setIdUnidadMedida(String idUnidadMedida) {
        this.idUnidadMedida = idUnidadMedida;
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

    public void setUpdate(boolean update) {
        this.update = update;
    }

    // </editor-fold>
}
