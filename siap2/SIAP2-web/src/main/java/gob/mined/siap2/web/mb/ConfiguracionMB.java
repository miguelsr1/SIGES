/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb;

import gob.mined.siap2.entities.data.Configuracion;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.persistence.dao.reference.EntityReference;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.utils.generalutils.StringsUtils;
import gob.mined.siap2.web.delegates.ConfiguracionDelegate;
import gob.mined.siap2.web.genericos.constantes.ConstantesPresentacion;
import gob.mined.siap2.web.utils.dataProvider.EntityReferenceDataProvider;
import gob.mined.siap2.web.utils.dataProvider.LazyLoadingList;
import gob.mined.siap2.web.utils.ProcesarMensajes;
import gob.mined.siap2.web.utils.SofisCombo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;

/**
 *
 * @author Sofis Solutions
 */
@Named(value = "configuracionMB")
@ViewScoped
public class ConfiguracionMB implements Serializable {

    private String bCodigo;
    private String bDescripcion;
    private SofisCombo comboCategoria = new SofisCombo();
    private List<EntityReference<Integer>> listaResultado = new ArrayList();
    private Boolean renderResultado = false;
    private Configuracion confEnEdicion = new Configuracion();
    private Boolean renderPopupEdicion = false;
    private List<Configuracion> listaHitorial = new ArrayList();
    private Boolean renderPopupHistorial = false;
    private String cantElementosPorPagina="25";
    @Inject
    ConfiguracionDelegate confDelegate;

    /**
     * Creates a new instance of ConfiguracionMB
     */
    public ConfiguracionMB() {
        comboCategoria = new SofisCombo();
        comboCategoria.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
        comboCategoria.setSelected(0);
    }

    /**
     * Limpia los parámetros de la configuración
     */
    private void reset() {
        bCodigo = "";
        bDescripcion = "";
        listaResultado = new ArrayList();
        renderResultado = false;
    }

    // <editor-fold defaultstate="collapsed" desc="eventos">
    
    
    /**
     * Filtra la tabla de resultados
     * 
     * @return 
     */
    public String buscar() {
        renderResultado = true;
        
        List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();

        if (!StringsUtils.isEmpty(bCodigo)) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS, "cnfCodigo",
                    bCodigo.toUpperCase().trim());
            criterios.add(criterio);
        }

        if (!StringsUtils.isEmpty(bDescripcion)) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS, "cnfDescripcion",
                    bDescripcion.toUpperCase().trim());
            criterios.add(criterio);
        }
        CriteriaTO condicion = null;
        if (!criterios.isEmpty()) {
            if (criterios.size() == 1) {
                condicion = criterios.get(0);
            } else {
                
                condicion = CriteriaTOUtils.createANDTO(criterios
                        .toArray(new CriteriaTO[0]));
            }
        } else {
            // condición dummy para que el count by criteria funcione
            condicion = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.NOT_NULL, "id", 1);
        }
        String[] propiedades = {"id", "cnfCodigo", "cnfValor", "cnfDescripcion"};
        String className = Configuracion.class.getName();
        String[] orderBy = {"cnfCodigo"};
        boolean[] asc = {true};

        EntityReferenceDataProvider cd = new EntityReferenceDataProvider(
                propiedades, className, condicion, orderBy, asc);
        
        listaResultado = new LazyLoadingList(cd, ConstantesPresentacion.CANTIDAD_PAGINACION, ConstantesPresentacion.PAGINAS_BUFFERED, false);
        return null;
    }

    /**
     * Limpia los valores
     * 
     * @return 
     */
    public String limpiar() {
        
        reset();
        return null;
    }

    /**
     * Crea una configuración nueva
     * 
     * @return 
     */
    public String agregar() {
        confEnEdicion = new Configuracion();
        renderPopupEdicion = true;
        return null;
    }

    /**
     * Carga una configuración a edición
     * 
     * @param id
     * @return 
     */
    public String editar(Integer id) {
        try {
            confEnEdicion = confDelegate.obtenerCnfPorId(id);
            renderPopupEdicion = true;
        } catch (GeneralException ex) {
            Logger.getLogger(ConfiguracionMB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
/*
    public String consultarHistorial(Integer id) {
        
        try {
            listaHitorial = histDelegate.obtenerConfiguracion(id);
            
            renderPopupHistorial = true;
        } catch (GeneralException ex) {
            
            Logger.getLogger(ConfiguracionMB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }*/

    public String cerrarPopupHistorial() {
        renderPopupHistorial = false;
        return null;
    }

    /**
     * Guarda el objeto en edición
     * 
     * @return 
     */
    public String guardar() {
        try {
            
            confDelegate.guardar(confEnEdicion);
            renderPopupEdicion = false;
            buscar();
        } catch (GeneralException ex) {
            for (FacesMessage s : ProcesarMensajes.obtenerMensajes(ex.getErrores())) {
                FacesContext.getCurrentInstance().addMessage("", s);
            }
        }  catch (Exception ex) {
            
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), ex.getMessage()));

        }
        return null;
    }

       
    /**
     * Retorna al menú anterior
     * 
     * @return 
     */
    public String cancelar() {
        renderPopupEdicion = false;
        return null;
    }

    
    public void cambiarCantPaginas(ValueChangeEvent evt) {
        
        buscar();
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public String getCantElementosPorPagina() {
        return cantElementosPorPagina;
    }

    public void setCantElementosPorPagina(String cantElementosPorPagina) {
        this.cantElementosPorPagina = cantElementosPorPagina;
    }
    
    
    
    
    public List<Configuracion> getListaHitorial() {
        return listaHitorial;
    }

    public void setListaHitorial(List<Configuracion> listaHitorial) {
        this.listaHitorial = listaHitorial;
    }

    public Boolean getRenderPopupHistorial() {
        return renderPopupHistorial;
    }

    public void setRenderPopupHistorial(Boolean renderPopupHistorial) {
        this.renderPopupHistorial = renderPopupHistorial;
    }

    public Configuracion getConfEnEdicion() {
        return confEnEdicion;
    }

    public void setConfEnEdicion(Configuracion confEnEdicion) {
        this.confEnEdicion = confEnEdicion;
    }

    public Boolean getRenderPopupEdicion() {
        return renderPopupEdicion;
    }

    public void setRenderPopupEdicion(Boolean renderPopupEdicion) {
        this.renderPopupEdicion = renderPopupEdicion;
    }

    public String getbCodigo() {
        return bCodigo;
    }

    public void setbCodigo(String bCodigo) {
        this.bCodigo = bCodigo;
    }

    public String getbDescripcion() {
        return bDescripcion;
    }

    public void setbDescripcion(String bDescripcion) {
        this.bDescripcion = bDescripcion;
    }

    public List<EntityReference<Integer>> getListaResultado() {
        return listaResultado;
    }

    public void setListaResultado(List<EntityReference<Integer>> listaResultado) {
        this.listaResultado = listaResultado;
    }

    public SofisCombo getComboCategoria() {
        return comboCategoria;
    }

    public void setComboCategoria(SofisCombo comboCategoria) {
        this.comboCategoria = comboCategoria;
    }

    public Boolean getRenderResultado() {
        return renderResultado;
    }

    public void setRenderResultado(Boolean renderResultado) {
        this.renderResultado = renderResultado;
    }
    // </editor-fold>
}
