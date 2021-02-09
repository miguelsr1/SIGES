/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.FuenteRecursos;
import gob.mined.siap2.entities.data.impl.LineaEstrategica;
import gob.mined.siap2.entities.data.impl.RelTechoPresupuestarioFR;
import gob.mined.siap2.entities.data.impl.RelTechoPresupuestarioUT;
import gob.mined.siap2.entities.data.impl.TechoPresupuestarioGOES;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.GeneralDelegate;
import gob.mined.siap2.web.delegates.impl.TechoPresupuestarioGOESDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import java.io.Serializable;
import java.math.BigDecimal;
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
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.TreeNode;

/**
 * Este backing bean implementa los eventos y lógica de presentación de la página
 * que carga los techos presupuestarios de GOES
 * 
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "techoPresupuestarioGoesCE")
public class TechoPresupuestarioGOESCrearEditar implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @Inject
    JSFUtils jSFUtils;

    @Inject
    EntityManagementDelegate emd;
    @Inject
    TechoPresupuestarioGOESDelegate techoPresupuestarioGOESDelegate;
    @Inject
    GeneralDelegate generalDelegate;
    
    private boolean update = false;
    private TechoPresupuestarioGOES objeto;  
    private TreeNode selectedNode;
    private BigDecimal monto;
    

    private List<LineaEstrategica> lineasEstrategicas = new LinkedList<>();

    @PostConstruct
    public void init() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (!TextUtils.isEmpty(id)) {
            update = true;
            objeto = techoPresupuestarioGOESDelegate.getTechoPresupuestario( Integer.valueOf(id));
            //TODO: Cargar el arbol con los montos
            UnidadesTecnicasTree unidadesTree = (UnidadesTecnicasTree)JSFUtils.getBean("unidadesTecnicasTree");
            for(RelTechoPresupuestarioUT techoUT :objeto.getTechosPresupuestariosFuente().get(0).getTechoPresupuestarioUT()){
                TreeNode node = unidadesTree.getNode(techoUT.getUnidadTecnica().getId());
                if (node != null){
                    ((UnidadTecnica)node.getData()).setAny(techoUT.getTechoPresupuestario());
                }
            }
            
        } else {
           objeto = new TechoPresupuestarioGOES();
           objeto.setTechosPresupuestariosFuente(new LinkedList()); 
           FuenteRecursos f = generalDelegate.getGOES();
            if (f!= null){
                RelTechoPresupuestarioFR goes = new RelTechoPresupuestarioFR ();
                goes.setFuenteRecursos(f);
                goes.setTechoPresupuestarioUT(new LinkedList());    
                goes.setTechoPresupuestarioGOES(objeto);
                objeto.getTechosPresupuestariosFuente().add(goes);
            }           
        }
        reloadUT();
        
    }
    
    /**
     * Este método se encarga de recargar los techos para una UT
     */
    public void reloadUT() {  
        List<UnidadTecnica> ll =emd.getEntities(UnidadTecnica.class.getName());        
        for (RelTechoPresupuestarioFR fr :objeto.getTechosPresupuestariosFuente()){
            for (UnidadTecnica u : ll) {
                if (!existeUT(fr, u)){
                    RelTechoPresupuestarioUT rut = new RelTechoPresupuestarioUT();
                    rut.setUnidadTecnica(u);
                    rut.setTechoPresupuestarioFR(fr);
                    fr.getTechoPresupuestarioUT().add(rut);
                }
            }
        }   
    }     
    
    
    /**
     * Este método retorna si existe la UT en los techos
     * 
     * @param fr
     * @param u
     * @return 
     */
    private boolean existeUT (RelTechoPresupuestarioFR fr, UnidadTecnica u){
        for (RelTechoPresupuestarioUT ut :fr.getTechoPresupuestarioUT()){
            if (ut.getUnidadTecnica().equals(u)){
                return true;
            }
        }
        return false;
    }
    
    
    
     /**
      * Guarda el objeto en edición
      * 
      * @return 
      */
    public String guardar()  {
        try {
            UnidadesTecnicasTree unidadesTree = (UnidadesTecnicasTree)JSFUtils.getBean("unidadesTecnicasTree");
            for(TreeNode node: unidadesTree.getListaNodosConstruidos()){
                actualizarMontoEnTecho((UnidadTecnica)node.getData());
            }
            
            
            //TODO: Recorrer los techos viendo si tiene la UT, si la tiene actualizar el monto
            //caso contrario, agregar la UT
            
            techoPresupuestarioGOESDelegate.crearOActualizarTechoPresupuestarioGOES(objeto);
            return "consultaTechoPresupuestarioGOES.xhtml?faces-redirect=true";
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
     * Actualiza los montos de un techo presupuestario de una UT
     * @param ut 
     */
    private void actualizarMontoEnTecho(UnidadTecnica ut){
        BigDecimal monto = (BigDecimal)ut.getAny();
        RelTechoPresupuestarioUT techoUT = null;
        for(RelTechoPresupuestarioUT techoUTAux :objeto.getTechosPresupuestariosFuente().get(0).getTechoPresupuestarioUT()){
            if (techoUTAux.getUnidadTecnica().getId().equals(ut.getId())){
                techoUT = techoUTAux;
            }
        }
        if(techoUT == null){//No lo encontre, tengo que crearle uno.
            techoUT = new RelTechoPresupuestarioUT();
            techoUT.setUnidadTecnica(ut);
            objeto.getTechosPresupuestariosFuente().get(0).getTechoPresupuestarioUT().add(techoUT);
        }
        techoUT.setTechoPresupuestario(monto);
    }

    /**
     * Dirige el sitio a la página de consulta de techo presupuestario
     * @return 
     */
    public String cerrar() {
        return "consultaTechoPresupuestarioGOES.xhtml?faces-redirect=true";
    }
     
    /**
     * Setea un monto en eun nodo del Árbol de la UT
     */
    public void setearMonto(){
        if (selectedNode != null && monto != null){
            ((UnidadTecnica)selectedNode.getData()).setAny(monto);
            
            UnidadesTecnicasTree unidadesTree = (UnidadesTecnicasTree)JSFUtils.getBean("unidadesTecnicasTree");

        }
        
    }
    
    /**
     * Setea en null un monto
     */
    public void eliminarMonto(){
        if (selectedNode != null){
            ((UnidadTecnica)selectedNode.getData()).setAny(null);
            monto = null;
        }
        
    }
    
    /**
     * Carga un monto según la UT seleccionada
     * @param event 
     */
    public void onNodeSelect(NodeSelectEvent event) {
        UnidadTecnica ut =(UnidadTecnica)event.getTreeNode().getData();
        if (ut.getAny() != null){
            monto = (BigDecimal)ut.getAny();
        }else{
            monto = null;
        }
    }
   
    //getters generados
    /**
     * Devuelve la lista de fuentes de recurso
     * @return 
     */
     public Map<String, String> getFuentesRecurso() {
        Map<String, String> map = new LinkedHashMap();
        String[] orderBy = {"nombre"};
        boolean[] asc = {true};
        List<FuenteRecursos> ll = emd.findEntityByCriteria(FuenteRecursos.class, null, orderBy, asc, null, null);
        for (FuenteRecursos l : ll) {
            map.put(l.getNombre(), String.valueOf(l.getId()));
        }
        return map;
    }
     

    
    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public JSFUtils getjSFUtils() {
        return jSFUtils;
    }

    public void setjSFUtils(JSFUtils jSFUtils) {
        this.jSFUtils = jSFUtils;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }
    
    

    public static Logger getLogger() {
        return logger;
    }

    public List<LineaEstrategica> getLineasEstrategicas() {
        return lineasEstrategicas;
    }

    public void setLineasEstrategicas(List<LineaEstrategica> lineasEstrategicas) {
        this.lineasEstrategicas = lineasEstrategicas;
    }

    public TechoPresupuestarioGOES getObjeto() {
        return objeto;
    }

    public void setObjeto(TechoPresupuestarioGOES objeto) {
        this.objeto = objeto;
    }

    public TechoPresupuestarioGOESDelegate getTechoPresupuestarioGOESDelegate() {
        return techoPresupuestarioGOESDelegate;
    }

    public void setTechoPresupuestarioGOESDelegate(TechoPresupuestarioGOESDelegate techoPresupuestarioGOESDelegate) {
        this.techoPresupuestarioGOESDelegate = techoPresupuestarioGOESDelegate;
    }

    public GeneralDelegate getGeneralDelegate() {
        return generalDelegate;
    }

    public void setGeneralDelegate(GeneralDelegate generalDelegate) {
        this.generalDelegate = generalDelegate;
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
