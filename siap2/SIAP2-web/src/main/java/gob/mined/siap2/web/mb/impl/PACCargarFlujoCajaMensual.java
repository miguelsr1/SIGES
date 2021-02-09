/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.business.utils.FlujoCajaMensualUtils;
import gob.mined.siap2.business.utils.TipoSeguimientoUtils;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.FlujoCajaAnio;
import gob.mined.siap2.entities.data.impl.MetodoAdquisicion;
import gob.mined.siap2.entities.data.impl.PAC;
import gob.mined.siap2.entities.data.impl.PACGrupo;
import gob.mined.siap2.entities.data.impl.POFlujoCajaMenusal;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.enums.EstadoGrupoPAC;
import gob.mined.siap2.entities.enums.TipoSeguimiento;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.datatypes.DataFCMGrupo;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.TextoDelegate;
import gob.mined.siap2.web.delegates.impl.PACDelegate;
import gob.mined.siap2.web.delegates.impl.VersionesDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.NodeUnselectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "pacCargarFlujoCajaMensual")
public class PACCargarFlujoCajaMensual implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
    private static final String ESTADO_INICIO = "ESTADO_INICIO";
    private static final String ESTADO_CREAR_EDITAR_PAC = "ESTADO_CREAR_EDITAR_PAC";

    @Inject
    PACDelegate pACDelegate;
    @Inject
    JSFUtils jSFUtils;

    @Inject
    EntityManagementDelegate emd;
    @Inject
    VersionesDelegate versionDelegate;
    @Inject
    private TextoDelegate textoDelegate;

    private TreeNode rootNodeArbol;
    private TreeNode selectedNode;
    
    private PAC objeto;
    private PACGrupo tempGrupo;

    private String filtroGrupoNro;
    private EstadoGrupoPAC filtroGrupoEstado;
    private String filtroGrupoidMetodoAdquisicion;

    private List<POInsumos> insumosSeleccionados = new LinkedList();

    @PostConstruct
    public void init() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (!TextUtils.isEmpty(id)) {
            objeto = pACDelegate.getPACForFCM(Integer.valueOf(id));
        } 
        initArbolPAC();
    }

    
     /**
      * Guarda el objeto en edición
      * 
      * @return 
      */
    public String guardar()  {
        try {
            pACDelegate.guardarFCMPAC(objeto);
            return "consultaPAC.xhtml?faces-redirect=true";                    
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().scrollTo("formHeader");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);            
            RequestContext.getCurrentInstance().scrollTo("formHeader");
        }
        return null;
    }

    /**
     * Carga el flujo de caja del PAC
     */
    public void reloadFlujoCajaPAC() {
        tipoGeneracion = "MANUAL";
        if (tempFlujoCajaPAC == null) {
            tempFlujoCajaPAC =FlujoCajaMensualUtils.generarFCM(objeto.getAnio());
        }
        anioFiscal = String.valueOf(tempFlujoCajaPAC.getAnio());
    }

    /**
     * Guarda los cambios realizados al flujo de caja del PAC
     */
    public void guardarFlujoCajaPAC() {
        try {
            if ("AUTOMATICO".equals(tipoGeneracion)) {
                BigDecimal montoTotal = BigDecimal.ZERO;
                if (nodoSeleccionado.getTipo().equals(DataFCMGrupo.INSUMO)){
                    POInsumos insumo = (POInsumos) nodoSeleccionado.getObjeto();
                    montoTotal = insumo.getMontoTotal();
                }else if (nodoSeleccionado.getTipo().equals(DataFCMGrupo.GRUPO)){
                    PACGrupo grupo = (PACGrupo) nodoSeleccionado.getObjeto();
                    montoTotal = grupo.getTotal();
                }
                flujoCaja = FlujoCajaMensualUtils.generarFlujoCajaMensual(desdeFCM, cantMeses, montoTotal);
                FlujoCajaMensualUtils.aplicarProrrateoAlFlujoDeCajaAnual(flujoCaja, montoTotal);
            } else {                
                FlujoCajaAnio fcaRemover = null;
                for(FlujoCajaAnio fca : flujoCaja){
                    if(fca.getAnio().equals(Integer.valueOf(anioFiscal))){
                        fcaRemover = fca;
                    }
                }      
                if(fcaRemover!=null){
                    flujoCaja.remove(fcaRemover);
                }
                tempFlujoCajaPAC.setAnio(Integer.valueOf(anioFiscal));
                flujoCaja.add(tempFlujoCajaPAC);
            }
                        
            if (nodoSeleccionado.getTipo().equals(DataFCMGrupo.INSUMO)){
                POInsumos insumo = (POInsumos) nodoSeleccionado.getObjeto();
                insumo.setFlujosDeCajaAnio(flujoCaja);
                
            }else if (nodoSeleccionado.getTipo().equals(DataFCMGrupo.GRUPO)){
                PACGrupo grupo = (PACGrupo) nodoSeleccionado.getObjeto();
                if ("AUTOMATICO".equals(tipoGeneracion)) {
                    FlujoCajaMensualUtils.generarFCMPlanificadoGrupo(grupo, flujoCaja);
                }else{
                    FlujoCajaMensualUtils.distribuirFCMPlanificadoGrupo(grupo, tempFlujoCajaPAC, flujoCaja);
                }
            }
            loadFCM();            
            RequestContext.getCurrentInstance().execute("$('#anadirFCM').modal('hide');");
        } catch (GeneralException ex) {
            loadFCM();
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            loadFCM();
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Elimina un flujo de caja anual de un PAC
     * @param objeto 
     */
    public void eliminarFlujoCajaPAC(FlujoCajaAnio objeto) {
        try{
            if (nodoSeleccionado.getTipo().equals(DataFCMGrupo.INSUMO)){
                POInsumos insumo = (POInsumos) nodoSeleccionado.getObjeto(); 
                pACDelegate.eliminarFlujoDeCajaPlanificado(insumo.getId(), objeto.getId());
            }
            loadFCM(); 
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
     * Devuelve el nombre de un mes específico 
     * @param i
     * @return 
     */
    public String getMesName(Integer i) {
        return TipoSeguimientoUtils.getListName(TipoSeguimiento.MENSUAL).get(i);
    }


    // <editor-fold defaultstate="collapsed" desc="getter-generados">
    public List<String> getMeses() {
        return TipoSeguimientoUtils.getListName(TipoSeguimiento.MENSUAL);
    }

    public BigDecimal getValorFCM(FlujoCajaAnio fcm, int index) {
        if (fcm.getFlujoCajaMenusal().size() <= index) {
            return null;
        }
        return fcm.getFlujoCajaMenusal().get(index).getMonto();
    }

    public String getCincoDigitos(Integer number) {
        String reducido = "00000" + number;
        return reducido.substring(reducido.length() - 5, reducido.length());
    }

    public EstadoGrupoPAC[] getEstadosGrupos() {
        return EstadoGrupoPAC.values();
    }

    public void limpiarGrupo() {
        try {
            filtroGrupoNro = null;
            filtroGrupoEstado = null;
            filtroGrupoidMetodoAdquisicion = null;
            initArbolPAC();
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    
    public void initArbolPAC(){
        rootNodeArbol = new DefaultTreeNode(new DataFCMGrupo(DataFCMGrupo.ROOT, null), null);
        
        for (PACGrupo g :objeto.getGrupos()){
            boolean cumpleFiltro = true;
            if (cumpleFiltro && !TextUtils.isEmpty(filtroGrupoNro)) {
                if (!g.getId().toString().contains(filtroGrupoNro)) {
                    cumpleFiltro = false;
                }
            }
            if (cumpleFiltro && filtroGrupoEstado != null) {
                if (filtroGrupoEstado != g.getEstado()) {
                    cumpleFiltro = false;
                }
            }

            if (cumpleFiltro && !TextUtils.isEmpty(filtroGrupoidMetodoAdquisicion)) {
                if (g.getMetodoAdquisicion() == null) {
                    cumpleFiltro = false;
                } else if (!g.getMetodoAdquisicion().getId().equals(Integer.valueOf(filtroGrupoidMetodoAdquisicion))) {
                    cumpleFiltro = false;
                }
            }

            if (cumpleFiltro){
                DataFCMGrupo dt = new DataFCMGrupo(DataFCMGrupo.GRUPO, g);
                TreeNode itemNode = new DefaultTreeNode(dt, rootNodeArbol);
                for (POInsumos insumo : g.getInsumos()) {
                    DataFCMGrupo dltInsumo = new DataFCMGrupo(DataFCMGrupo.INSUMO, insumo);
                    TreeNode insumoNode = new DefaultTreeNode(dltInsumo, itemNode);
                }
            }
        }
    }
    
    private DataFCMGrupo nodoSeleccionado= null;
    private Set<FlujoCajaAnio> flujoCaja; 
    private FlujoCajaAnio tempFlujoCajaPAC;
    private String anioFiscal;
    private String estado = ESTADO_INICIO;
    private Date desdeFCM;
    private Integer cantMeses;
    private String tipoGeneracion;
    private String nombreGrupoSeleccionado;
    private Date menorFechaEstimadaContratacion;
    private BigDecimal montoTotalgrupo;

    
    public void onNodeSelect(NodeSelectEvent event) {        
        nodoSeleccionado=  null;
        flujoCaja = null;
        nombreGrupoSeleccionado = "";
        TreeNode node =event.getTreeNode();
        nodoSeleccionado =(DataFCMGrupo) node.getData();
        
        if(nodoSeleccionado.getTipo().equals(DataFCMGrupo.GRUPO)){
            PACGrupo pacGrupo = (PACGrupo)nodoSeleccionado.getObjeto();
            if(pacGrupo!=null){
                nombreGrupoSeleccionado = pacGrupo.getNombre();
                menorFechaEstimadaContratacion = pacGrupo.getFechaMinimoInsumo();
                montoTotalgrupo = pacGrupo.getTotal();
            }
        } else if(nodoSeleccionado.getTipo().equals(DataFCMGrupo.INSUMO)){
            POInsumos insumo = (POInsumos)nodoSeleccionado.getObjeto();
            if(insumo!=null){
                nombreGrupoSeleccionado = insumo.getPacGrupo().getNombre();
                menorFechaEstimadaContratacion = insumo.getPacGrupo().getFechaMinimoInsumo();
                montoTotalgrupo = insumo.getPacGrupo().getTotal();
            }
        }
        
        loadFCM();
    }
 
    public void onNodeUnselect(NodeUnselectEvent event) {
        nodoSeleccionado=  null;
        flujoCaja = null;
    }
    
    
    
           
    public void loadFCM(){
        if (nodoSeleccionado!= null){
            if (nodoSeleccionado.getTipo().equals(DataFCMGrupo.INSUMO)){
                POInsumos insumo = (POInsumos) nodoSeleccionado.getObjeto();
                flujoCaja= insumo.getFlujosDeCajaAnio();
            }else if (nodoSeleccionado.getTipo().equals(DataFCMGrupo.GRUPO)){
                PACGrupo g = (PACGrupo) nodoSeleccionado.getObjeto();
                flujoCaja= FlujoCajaMensualUtils.getFCMPlanificadoGrupo(g);
            }else{
                nodoSeleccionado=null;    
            }   
        }
    }
    
    
    
    

    public Map<String, String> getMetodosAdquisicion() {
        Map<String, String> map = new HashMap();
        List<MetodoAdquisicion> l = emd.getEntities(MetodoAdquisicion.class.getName(), "nombre");
        for (MetodoAdquisicion o : l) {
            map.put(o.getNombre(), o.getId().toString());
        }
        return map;
    }

    
    public BigDecimal sumaTotalPorAnio(Integer anio){
        BigDecimal total = BigDecimal.ZERO;
        for(FlujoCajaAnio fca: flujoCaja){
            Integer anioFCA = fca.getAnio();
            if(anioFCA.equals(anio)){
                for(POFlujoCajaMenusal fcm : fca.getFlujoCajaMenusal()){
                    if(fcm.getMonto()!= null){
                        total = total.add(fcm.getMonto());
                    }
                }
            }
        }
        return total;
    }
    
    
    
    public boolean esGrupoElSelecionado(){
        if (nodoSeleccionado == null){
            return false;
        }
        return nodoSeleccionado.getTipo().equals(DataFCMGrupo.GRUPO);
    }
    
    
    
    
    public BigDecimal getTotalFCMEnEdicion(){
        BigDecimal total = BigDecimal.ZERO;
        if (tempFlujoCajaPAC != null){
            if (tempFlujoCajaPAC.getFlujoCajaMenusal() != null){
                for (POFlujoCajaMenusal fc :tempFlujoCajaPAC.getFlujoCajaMenusal()){
                    if (fc.getMonto() != null){
                        total= total.add(fc.getMonto());
                    }
                }
                
            }
            
        }


        return total;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public void test() {
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public JSFUtils getjSFUtils() {
        return jSFUtils;
    }

    public void setjSFUtils(JSFUtils jSFUtils) {
        this.jSFUtils = jSFUtils;
    }

    public PACGrupo getTempGrupo() {
        return tempGrupo;
    }

    public void setTempGrupo(PACGrupo tempGrupo) {
        this.tempGrupo = tempGrupo;
    }

    public String getTipoGeneracion() {
        return tipoGeneracion;
    }

    public void setTipoGeneracion(String tipoGeneracion) {
        this.tipoGeneracion = tipoGeneracion;
    }

    public EntityManagementDelegate getEmd() {
        return emd;
    }

    public FlujoCajaAnio getTempFlujoCajaPAC() {
        return tempFlujoCajaPAC;
    }

    public void setTempFlujoCajaPAC(FlujoCajaAnio tempFlujoCajaPAC) {
        this.tempFlujoCajaPAC = tempFlujoCajaPAC;
    }

    public PAC getObjeto() {
        return objeto;
    }

    public void setObjeto(PAC objeto) {
        this.objeto = objeto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFiltroGrupoNro() {
        return filtroGrupoNro;
    }

    public List<POInsumos> getInsumosSeleccionados() {
        return insumosSeleccionados;
    }

    public void setInsumosSeleccionados(List<POInsumos> insumosSeleccionados) {
        this.insumosSeleccionados = insumosSeleccionados;
    }

    public void setFiltroGrupoNro(String filtroGrupoNro) {
        this.filtroGrupoNro = filtroGrupoNro;
    }

    public PACDelegate getpACDelegate() {
        return pACDelegate;
    }

    public void setpACDelegate(PACDelegate pACDelegate) {
        this.pACDelegate = pACDelegate;
    }

    public VersionesDelegate getVersionDelegate() {
        return versionDelegate;
    }

    public void setVersionDelegate(VersionesDelegate versionDelegate) {
        this.versionDelegate = versionDelegate;
    }
    public TextoDelegate getTextoDelegate() {
        return textoDelegate;
    }

    public void setTextoDelegate(TextoDelegate textoDelegate) {
        this.textoDelegate = textoDelegate;
    }

    public TreeNode getRootNodeArbol() {
        return rootNodeArbol;
    }

    public void setRootNodeArbol(TreeNode rootNodeArbol) {
        this.rootNodeArbol = rootNodeArbol;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public EstadoGrupoPAC getFiltroGrupoEstado() {
        return filtroGrupoEstado;
    }

    public void setFiltroGrupoEstado(EstadoGrupoPAC filtroGrupoEstado) {
        this.filtroGrupoEstado = filtroGrupoEstado;
    }

    public String getFiltroGrupoidMetodoAdquisicion() {
        return filtroGrupoidMetodoAdquisicion;
    }

    public void setFiltroGrupoidMetodoAdquisicion(String filtroGrupoidMetodoAdquisicion) {
        this.filtroGrupoidMetodoAdquisicion = filtroGrupoidMetodoAdquisicion;
    }

    

    public String getAnioFiscal() {
        return anioFiscal;
    }

    public void setAnioFiscal(String anioFiscal) {
        this.anioFiscal = anioFiscal;
    }

    public Date getDesdeFCM() {
        return desdeFCM;
    }

    public DataFCMGrupo getNodoSeleccionado() {
        return nodoSeleccionado;
    }

    public void setNodoSeleccionado(DataFCMGrupo nodoSeleccionado) {
        this.nodoSeleccionado = nodoSeleccionado;
    }

    public Set<FlujoCajaAnio> getFlujoCaja() {
        return flujoCaja;
    }

    public void setFlujoCaja(Set<FlujoCajaAnio> flujoCaja) {
        this.flujoCaja = flujoCaja;
    }

    public void setDesdeFCM(Date desdeFCM) {
        this.desdeFCM = desdeFCM;
    }

    
    public Integer getCantMeses() {
        return cantMeses;
    }

    public void setCantMeses(Integer cantMeses) {
        this.cantMeses = cantMeses;
    }
   
    public String getNombreGrupoSeleccionado() {
        return nombreGrupoSeleccionado;
    }

    public void setNombreGrupoSeleccionado(String nombreGrupoSeleccionado) {
        this.nombreGrupoSeleccionado = nombreGrupoSeleccionado;
    }
    
    public Date getMenorFechaEstimadaContratacion() {
        return menorFechaEstimadaContratacion;
    }

    public void setMenorFechaEstimadaContratacion(Date menorFechaEstimadaContratacion) {
        this.menorFechaEstimadaContratacion = menorFechaEstimadaContratacion;
    }
    
    public BigDecimal getMontoTotalgrupo() {
        return montoTotalgrupo;
    }

    public void setMontoTotalgrupo(BigDecimal montoTotalgrupo) {
        this.montoTotalgrupo = montoTotalgrupo;
    }
    
    
    // </editor-fold>

    
}
