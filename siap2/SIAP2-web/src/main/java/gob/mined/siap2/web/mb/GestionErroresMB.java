/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb;

import gob.mined.siap2.entities.data.Error;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.reference.EntityReference;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.utils.generalutils.StringsUtils;
import gob.mined.siap2.web.delegates.ErrorDelegate;
import gob.mined.siap2.web.genericos.constantes.ConstantesPresentacion;
import gob.mined.siap2.web.utils.dataProvider.EntityReferenceDataProvider;
import gob.mined.siap2.web.utils.dataProvider.LazyLoadingList;
import gob.mined.siap2.web.utils.ProcesarMensajes;
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
@Named(value = "gestionErroresMB")
@ViewScoped
public class GestionErroresMB  implements Serializable{

    /**
     * Creates a new instance of ErrorMB
     */
    public GestionErroresMB() {
    }
    
    
    
     private String bCodigo;
    private String bDescripcion;
     private List<EntityReference<Integer>> listaResultado = new ArrayList();
    private Boolean renderResultado = false;
    private Error errEnEdicion = new Error();
    private Boolean renderPopupEdicion = false;
    private List<Error> listaHitorial = new ArrayList();
    private Boolean renderPopupHistorial = false;
    private String cantElementosPorPagina = "25";
    private String elementoOrdenacion="errCodigo";
    private String ascendente = "Ascendente";
    @Inject
    private ErrorDelegate natDelegate;

    /**
     * Creates a new instance of ErrorMB
     */
   

    private void reset() {
        bCodigo = "";
        bDescripcion = "";
        listaResultado = new ArrayList();
        renderResultado = false;
    }

    // <editor-fold defaultstate="collapsed" desc="eventos">
    public String buscar() {
        renderResultado = true;
        List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();

        if (!StringsUtils.isEmpty(bCodigo)) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS, "errCodigo",
                    bCodigo.toUpperCase().trim());
            criterios.add(criterio);
        }

        if (!StringsUtils.isEmpty(bDescripcion)) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS, "errDescripcion",
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
        String[] propiedades = {"id", "errCodigo",  "errDescripcion"  };
        String className = Error.class.getName();
        String[] orderBy = {elementoOrdenacion};
        boolean[] asc = {"Ascendente".equals(ascendente)};

        EntityReferenceDataProvider cd = new EntityReferenceDataProvider(
                propiedades, className, condicion, orderBy, asc);
         listaResultado = new LazyLoadingList(cd, ConstantesPresentacion.CANTIDAD_PAGINACION, ConstantesPresentacion.PAGINAS_BUFFERED, false);
        return null;
    }

    public String limpiar() {
        reset();
        return null;
    }

    public String agregar() {
        errEnEdicion = new Error();
        renderPopupEdicion = true;
        return null;
    }

    public String editar(Integer id) {
        try {
            errEnEdicion = natDelegate.obtenerErrorPorId(id);
            renderPopupEdicion = true;
        } catch (GeneralException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (DAOGeneralException ex) {
            Logger.getLogger(GestionErroresMB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
/*
    public String consultarHistorial(Integer id) {
        try {
            listaHitorial = histDelegate.obtenerHistorialPorId(Error.class, id, "errVersion");
            renderPopupHistorial = true;
        } catch (GeneralException ex) {
            
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
*/
    public String cerrarPopupHistorial() {
        renderPopupHistorial = false;
        return null;
    }

    public String guardar() {
        try {
            natDelegate.guardar(errEnEdicion);
            renderPopupEdicion = false;
            buscar();
        } catch (GeneralException ex) {
            for (FacesMessage s : ProcesarMensajes.obtenerMensajes(ex.getErrores())) {
                FacesContext.getCurrentInstance().addMessage("", s);
            }
        } catch (Exception ex) {

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
    
    public void cambiarCriterioOrdenacion(ValueChangeEvent evt) {
        elementoOrdenacion=evt.getNewValue().toString();
        buscar();
    }
    
    public void cambiarAscendenteOrdenacion(ValueChangeEvent evt) {
        ascendente=evt.getNewValue().toString();
        buscar();
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Error getErrEnEdicion() {
        return errEnEdicion;
    }

    public void setErrEnEdicion(Error errEnEdicion) {
        this.errEnEdicion = errEnEdicion;
    }
    
    
    public String getAscendente() {
        return ascendente;
    }

    public void setAscendente(String ascendente) {
        this.ascendente = ascendente;
    }
    
    
    
    public String getElementoOrdenacion() {
        return elementoOrdenacion;
    }

    public void setElementoOrdenacion(String elementoOrdenacion) {
        this.elementoOrdenacion = elementoOrdenacion;
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

    public Boolean getRenderResultado() {
        return renderResultado;
    }

    public void setRenderResultado(Boolean renderResultado) {
        this.renderResultado = renderResultado;
    }

    public Boolean getRenderPopupEdicion() {
        return renderPopupEdicion;
    }

    public void setRenderPopupEdicion(Boolean renderPopupEdicion) {
        this.renderPopupEdicion = renderPopupEdicion;
    }

    public List<Error> getListaHitorial() {
        return listaHitorial;
    }

    public void setListaHitorial(List<Error> listaHitorial) {
        this.listaHitorial = listaHitorial;
    }

    public Boolean getRenderPopupHistorial() {
        return renderPopupHistorial;
    }

    public void setRenderPopupHistorial(Boolean renderPopupHistorial) {
        this.renderPopupHistorial = renderPopupHistorial;
    }

    public String getCantElementosPorPagina() {
        return cantElementosPorPagina;
    }

    public void setCantElementosPorPagina(String cantElementosPorPagina) {
        this.cantElementosPorPagina = cantElementosPorPagina;
    }
    
    
    
     
    // </editor-fold>
    
    
    
}
