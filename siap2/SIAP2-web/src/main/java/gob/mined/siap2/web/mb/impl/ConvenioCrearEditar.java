/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.CategoriaConvenio;
import gob.mined.siap2.entities.data.impl.Convenio;
import gob.mined.siap2.entities.enums.TipoAporteProyecto;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.persistence.dao.reference.EntityReference;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.ConvenioDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
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
@Named(value = "convenioCE")
public class ConvenioCrearEditar implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @Inject
    JSFUtils jSFUtils;

    @Inject
    EntityManagementDelegate emd;
    @Inject
    ConvenioDelegate convenioDelegate;

    private boolean update = false;
    private Convenio objeto;    
    private String idLineaToAdd;

    private CategoriaConvenio categoriaNueva = new CategoriaConvenio();

    @PostConstruct
    public void init() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (!TextUtils.isEmpty(id)) {
            update = true;
            objeto = (Convenio) convenioDelegate.getConvenio(Integer.valueOf(id));

        } else {
            objeto = new Convenio();
            objeto.setHabilitado(Boolean.TRUE);
            objeto.setCategorias(new LinkedList<CategoriaConvenio>());
        }
    }

    
     /**
      * Guarda el objeto en edición
      * 
      * @return 
      */
    public String guardar()  {
        try {
            convenioDelegate.crearActualizarConvenio(objeto);
            return "consultaConvenio.xhtml?faces-redirect=true";
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
     * Redirige el sitio a la consulta de convenio
     * @return 
     */
    public String cerrar() {
        return "consultaConvenio.xhtml?faces-redirect=true";
    }

    /**
     * Inicializa una categoría nueva
     */
    public void initCategoriaNueva() {
        if (categoriaNueva == null) {
            categoriaNueva = new CategoriaConvenio();
            categoriaNueva.setTipo(TipoAporteProyecto.POR_PORCENTAJE);
        }
    }

    /**
     * Remueve una categoría de un convenio
     * @param le 
     */
    public void eliminarLinea(CategoriaConvenio le) {
        try {
            objeto.getCategorias().remove(le);
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
     * Agrega una categoría a un convenio
     */
    public void addLinea() {
        if (!objeto.getCategorias().contains(categoriaNueva)) {
            objeto.getCategorias().add(categoriaNueva);
        }
        RequestContext.getCurrentInstance().execute("$('#addLineaEstrategica').modal('hide');");
    }

    /**
     * Devuelve las categorías disponibles
     * @return 
     */
    public Map<String, String> getLineasDisponibles() {
        Map<String, String> lineas = new LinkedHashMap();

        String[] propiedades = {"id", "nombre"};
        String className = CategoriaConvenio.class.getName();
        MatchCriteriaTO vigente = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_NULL, "id", 1);

        String[] orderBy = {"nombre"};
        boolean[] asc = {true};
        List<EntityReference<Integer>> ll = emd.getEntitiesReferenceByCriteria(className, vigente, null, null, propiedades, orderBy, asc);

        for (EntityReference l : ll) {
            if (!containsLinea(l)) {
                lineas.put((String) l.getPropertyMap().get("nombre"), String.valueOf(l.getPropertyMap().get("id"))); // value, label, the value to choose and label to appear fo the user
            }
        }
        return lineas;
    }

    /**
     * Verifica si determinada categoría está en un convenio
     * @param toCheck
     * @return 
     */
    private boolean containsLinea(EntityReference toCheck) {
        for (CategoriaConvenio l : objeto.getCategorias()) {
            if (l.getId().equals(toCheck.getPropertyMap().get("id"))) {
                return true;
            }
        }
        return false;
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

    public Convenio getObjeto() {
        return objeto;
    }

    public void setObjeto(Convenio objeto) {
        this.objeto = objeto;
    }

    public void setIdLineaToAdd(String idLineaToAdd) {
        this.idLineaToAdd = idLineaToAdd;
    }

    public String getIdLineaToAdd() {
        return idLineaToAdd;
    }
    
    public CategoriaConvenio getCategoriaNueva() {
        return categoriaNueva;
    }

    public void setCategoriaNueva(CategoriaConvenio categoriaNueva) {
        this.categoriaNueva = categoriaNueva;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    // </editor-fold>
}
