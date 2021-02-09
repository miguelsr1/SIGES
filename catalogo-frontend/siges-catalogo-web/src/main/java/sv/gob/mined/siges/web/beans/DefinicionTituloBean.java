/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgDefinicionTitulo;
import sv.gob.mined.siges.web.dto.SgFormula;
import sv.gob.mined.siges.web.dto.SgPlantilla;
import sv.gob.mined.siges.web.enumerados.EnumTipoFormula;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDefinicionTitulo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroFormula;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPlantilla;
import sv.gob.mined.siges.web.lazymodels.LazyDefinicionTituloDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.DefinicionTituloRestClient;
import sv.gob.mined.siges.web.restclient.FormulaRestClient;
import sv.gob.mined.siges.web.restclient.PlantillaRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class DefinicionTituloBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(DefinicionTituloBean.class.getName());

    @Inject
    private DefinicionTituloRestClient restClient;
    
    @Inject
    private PlantillaRestClient plantillaRestClient;
    
    @Inject
    private FormulaRestClient formulaRestClient;

    private FiltroDefinicionTitulo filtro = new FiltroDefinicionTitulo();
    private SgDefinicionTitulo entidadEnEdicion = new SgDefinicionTitulo();
    private List<SgDefinicionTitulo> historialDefinicionTitulo = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyDefinicionTituloDataModel definicionTituloLazyModel;
    private SofisComboG<SgPlantilla> comboPlantilla;
    private SofisComboG<SgFormula> comboFormulas;


    public DefinicionTituloBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            buscar();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void buscar() {
        try {
            filtro.setFirst(new Long(0));
            totalResultados = restClient.buscarTotal(filtro);
            definicionTituloLazyModel = new LazyDefinicionTituloDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        try{       
            FiltroPlantilla fp=new FiltroPlantilla();
            List<SgPlantilla> plantillaList=plantillaRestClient.buscar(fp);
            comboPlantilla = new SofisComboG(plantillaList, "plaNombre");
            comboPlantilla.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            FiltroFormula filtroFormulas = new FiltroFormula();
            filtroFormulas.setTipoFormula(EnumTipoFormula.TITULO);
            filtroFormulas.setHabilitado(Boolean.TRUE);
            List<SgFormula> formulas = formulaRestClient.buscar(filtroFormulas);
            Collections.sort(formulas, (o1, o2) -> o1.getFomNombre().compareTo(o2.getFomNombre())); 
            comboFormulas = new SofisComboG(formulas, "fomNombre");
            comboFormulas.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        }catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {
      comboPlantilla.setSelected(-1);
      comboFormulas.setSelected(-1);
    }

    public void limpiar() {
        filtro = new FiltroDefinicionTitulo();
        buscar();
    }

    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgDefinicionTitulo();
    }

    public void actualizar(SgDefinicionTitulo var) {
        try{
            JSFUtils.limpiarMensajesError();
            limpiarCombos();

            entidadEnEdicion = restClient.obtenerPorId(var.getDtiPk());
            comboPlantilla.setSelectedT(entidadEnEdicion.getDtiPlantilla());
            comboFormulas.setSelectedT(entidadEnEdicion.getDtiFormula());
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardar() {
        try {
            entidadEnEdicion.setDtiPlantilla(comboPlantilla!=null?comboPlantilla.getSelectedT():null);
            entidadEnEdicion.setDtiFormula(comboFormulas.getSelectedT());
            
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscar();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getDtiPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            buscar();
            entidadEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void historial(Long id) {
        try {
            historialDefinicionTitulo = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }


    
    //<editor-fold defaultstate="collapsed" desc="GET & SET">
    public FiltroDefinicionTitulo getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroDefinicionTitulo filtro) {
        this.filtro = filtro;
    }

    public SgDefinicionTitulo getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgDefinicionTitulo entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgDefinicionTitulo> getHistorialDefinicionTitulo() {
        return historialDefinicionTitulo;
    }

    public void setHistorialDefinicionTitulo(List<SgDefinicionTitulo> historialDefinicionTitulo) {
        this.historialDefinicionTitulo = historialDefinicionTitulo;
    }

    public Integer getPaginado() {
        return paginado;
    }

    public void setPaginado(Integer paginado) {
        this.paginado = paginado;
    }

    public Long getTotalResultados() {
        return totalResultados;
    }

    public void setTotalResultados(Long totalResultados) {
        this.totalResultados = totalResultados;
    }

    public LazyDefinicionTituloDataModel getDefinicionTituloLazyModel() {
        return definicionTituloLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyDefinicionTituloDataModel definicionTituloLazyModel) {
        this.definicionTituloLazyModel = definicionTituloLazyModel;
    }

    public SofisComboG<SgPlantilla> getComboPlantilla() {
        return comboPlantilla;
    }

    public void setComboPlantilla(SofisComboG<SgPlantilla> comboPlantilla) {
        this.comboPlantilla = comboPlantilla;
    }

    public SofisComboG<SgFormula> getComboFormulas() {
        return comboFormulas;
    }

    public void setComboFormulas(SofisComboG<SgFormula> comboFormulas) {
        this.comboFormulas = comboFormulas;
    }
    
     //</editor-fold>


}
