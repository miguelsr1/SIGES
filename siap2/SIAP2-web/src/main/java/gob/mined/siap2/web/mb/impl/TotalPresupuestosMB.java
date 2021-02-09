package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.business.GestionPresupuestoEscolarBean;
import gob.mined.siap2.business.ejbs.impl.CategoriaPresupuestoEscolarBean;
import gob.mined.siap2.business.ejbs.impl.GeneralBean;
import gob.mined.siap2.business.ejbs.impl.TopePresupuestalBean;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.CategoriaPresupuestoEscolar;
import gob.mined.siap2.entities.data.impl.ComponentePresupuestoEscolar;
import gob.mined.siap2.entities.data.impl.TopePresupuestalGroup;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.filtros.FiltroComponentePresupuestoEscolar;
import gob.mined.siap2.web.genericos.constantes.ConstantesPresentacion;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.utils.SofisComboG;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.context.RequestContext;

@ViewScoped
@Named(value = "totalPresupuestosMB")
public class TotalPresupuestosMB implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    
    private List<AnioFiscal> listaAnioFiscal;
    private List<TopePresupuestalGroup> listaTopePresupuestalGroup;
    
    //private SofisComboG<CategoriaPresupuestoEscolar> comboComponente = new SofisComboG<CategoriaPresupuestoEscolar>();
    //private SofisComboG<ComponentePresupuestoEscolar> comboSubcomponente = new SofisComboG<ComponentePresupuestoEscolar>();;
    private SofisComboG<AnioFiscal> comboAnioFiscal = new SofisComboG<AnioFiscal>();;
    
    @Inject
    private TopePresupuestalBean presupuestalBean;
    @Inject
    private GeneralBean generalBean;
    @Inject
    private JSFUtils jSFUtils;
    @Inject
    private CategoriaPresupuestoEscolarBean categoriaBean;
    @Inject
    private GestionPresupuestoEscolarBean escolarBean;
    
    private CategoriaPresupuestoEscolar componenteSelected;
    private ComponentePresupuestoEscolar subComponenteSelected;
    
    @PostConstruct
    public void init() {
        setListaTopePresupuestalGroup(new ArrayList<TopePresupuestalGroup>());
        cargarAnioFiscal();
        cargarComponente();
    }

    
    /**
     * Metodo para la carga de registros de Anios fiscales
     */
    public void cargarAnioFiscal(){
        try {
            List<AnioFiscal> lista = generalBean.getAniosFiscalesPlanificacion();
            comboAnioFiscal = new SofisComboG(lista, "anio");
            comboAnioFiscal.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
            
            for(AnioFiscal af : lista){
                if(af.getAnio() == Calendar.getInstance().get(Calendar.YEAR)){
                    logger.log(Level.SEVERE, null, "ANIO_ENCONTRADO");
                    comboAnioFiscal.setSelectedT(af);
                    break;
                } 
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    
    /**
     * Metodo para la carga de registros de componentes
     */
    public void cargarComponente(){
        try {
            List<CategoriaPresupuestoEscolar> lista = categoriaBean.getAllCategoriaPresupuestoEscolarHabilitados();
           // comboComponente = new SofisComboG(lista, "nombre");
           // comboComponente.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$(window).scrollTop(0);");
        }
    }
    
    /**
     * Metodo para la carga de registros de Subcomponentes
     */
    public void cargarSubcomponente(){
        try {
            /**if(this.comboComponente != null && this.comboComponente.getSelectedT() != null){
                List<ComponentePresupuestoEscolar> lista = escolarBean.getComponentesHabilitadosByCategoria(comboComponente.getSelectedT().getId());
                comboSubcomponente = new SofisComboG(lista, "nombre");
                comboSubcomponente.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
            }**/
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$(window).scrollTop(0);");
        }
    }
    
    /**
     * Metodo utilizado para filtrar los registros agrupados de resultado
     */
    public void topePresupuestalAgrupado(){
        try {
            Integer idCategoria = null;
            Integer idComponente = null;
            Integer idAnioFiscal = null;
            if(this.componenteSelected != null)
                idCategoria = this.componenteSelected.getId();
            if(this.subComponenteSelected != null)
                idComponente = this.subComponenteSelected.getId();
            if(this.comboAnioFiscal != null && this.comboAnioFiscal.getSelectedT() != null)
                idAnioFiscal = this.comboAnioFiscal.getSelectedT().getId();
            
            logger.log(Level.SEVERE, "FILTRANDO ANIO:"+idAnioFiscal+", CAT:"+idCategoria+", COMP:"+idComponente);
            this.listaTopePresupuestalGroup = presupuestalBean.getTopePresupuestalAgrupado(idAnioFiscal, idCategoria, idComponente);
            
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }
    
    public List<CategoriaPresupuestoEscolar> completeComponentes(String query){
        try {
            FiltroComponentePresupuestoEscolar filtro = new FiltroComponentePresupuestoEscolar();
            filtro.setNombre(query);
            filtro.setHabilitado(Boolean.TRUE);
            filtro.setAscending(new boolean[]{true});
            filtro.setOrderBy(new String[]{"nombre"});
            filtro.setMaxResults(10L);
            filtro.setIncluirCampos(new String[]{"codigo","nombre","version"});
            return categoriaBean.obtenerComponentesPorFiltro(filtro);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;
    }
    public List<ComponentePresupuestoEscolar> completeSubComponentes(String query){
        try {
            if(this.componenteSelected != null) {
                FiltroComponentePresupuestoEscolar filtro = new FiltroComponentePresupuestoEscolar();
                filtro.setCategoriaComponenteId(componenteSelected.getId());
                filtro.setNombre(query);
                filtro.setHabilitado(Boolean.TRUE);
                filtro.setAscending(new boolean[]{true});
                filtro.setOrderBy(new String[]{"nombre"});
                filtro.setMaxResults(10L);
                filtro.setIncluirCampos(new String[]{"id","nombre","version"});
                return escolarBean.getComponentesPresupuestoEscolarByFiltro(filtro);
            }
            
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;
    }
    /**
     * Este método verifica el tamaño de la lista contenedora de la agrupacion
     * @return 
     */
    public int tamanioListaGrupo(){
        if(getListaTopePresupuestalGroup() != null && !getListaTopePresupuestalGroup().isEmpty()){
            return getListaTopePresupuestalGroup().size();
        }else{
            return 0;
        }
    }
    
    

    
    
    
    
    

    public List<AnioFiscal> getListaAnioFiscal() {
        return listaAnioFiscal;
    }

    public void setListaAnioFiscal(List<AnioFiscal> listaAnioFiscal) {
        this.listaAnioFiscal = listaAnioFiscal;
    }

    public List<TopePresupuestalGroup> getListaTopePresupuestalGroup() {
        return listaTopePresupuestalGroup;
    }

    public void setListaTopePresupuestalGroup(List<TopePresupuestalGroup> listaTopePresupuestalGroup) {
        this.listaTopePresupuestalGroup = listaTopePresupuestalGroup;
    }

    public SofisComboG<AnioFiscal> getComboAnioFiscal() {
        return comboAnioFiscal;
    }

    public void setComboAnioFiscal(SofisComboG<AnioFiscal> comboAnioFiscal) {
        this.comboAnioFiscal = comboAnioFiscal;
    }

    public CategoriaPresupuestoEscolar getComponenteSelected() {
        return componenteSelected;
    }

    public void setComponenteSelected(CategoriaPresupuestoEscolar componenteSelected) {
        this.componenteSelected = componenteSelected;
    }

    public ComponentePresupuestoEscolar getSubComponenteSelected() {
        return subComponenteSelected;
    }

    public void setSubComponenteSelected(ComponentePresupuestoEscolar subComponenteSelected) {
        this.subComponenteSelected = subComponenteSelected;
    }
    
    
    
    

}
