/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl.po;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.GeneralPOA;
import gob.mined.siap2.entities.data.impl.POAProyecto;
import gob.mined.siap2.entities.data.impl.POARiesgo;
import gob.mined.siap2.entities.data.impl.POActividadBase;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.data.impl.POLinea;
import gob.mined.siap2.entities.data.impl.ProyectoComponente;
import gob.mined.siap2.entities.data.impl.ProyectoMacroActividad;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.entities.enums.EstadoPOA;
import gob.mined.siap2.entities.enums.OrigenRiesgo;
import gob.mined.siap2.entities.enums.ValoracionRiesgo;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.DatesUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.POAProyectoDelegate;
import gob.mined.siap2.web.delegates.ProyectoDelegate;
import gob.mined.siap2.web.delegates.impl.VersionesDelegate;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 * Este backing bean implementa los eventos y lógica de presentación de la página
 * que consolida un POA de proyecto.
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "poaConsolidadoProyMB")
public class POAConsolidadoProyMB extends POAProyectoBasico /*POProyectoConLineasAbstract*/ implements Serializable {

    private static final long serialVersionUID = 1L;
    protected static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
    private static final String PAGE_VOLVER = "consultaPOAporProyecto.xhtml";

    @Inject
    EntityManagementDelegate emd;
    @Inject
    VersionesDelegate versionDelegate;

    @Inject
    ProyectoDelegate proyectoDelegate;
    @Inject
    POAProyectoDelegate pOAProyectoDelegate;

    protected String idProyecto;
    protected String idAnioFiscal;
    protected List<AnioFiscal> posiblesAniosPOA;

    protected Integer idActividadNP;
    protected List<POAProyecto> listPoa;
    protected List<UnidadTecnica> utPendientes;
    protected boolean completoParaConsolidado = false;

    @PostConstruct
    public void init() {
        verTodosLosMontos = true;
        idProyecto = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        idAnioFiscal = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idAnioFiscal");
        //idUnidadTecnica = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idUnidadTecnica");

        if (!TextUtils.isEmpty(idProyecto)) {
            update = true;
            objeto = versionDelegate.getProyecto(Integer.valueOf(idProyecto));
            posiblesAniosPOA = pOAProyectoDelegate.getAniosDisponiblesProgramacionPOA(objeto.getId());
        }
        initProyecto();
    }

    /**
     * Este método se encarga de realizar la inicialización
     */
    public void initProyecto() {
        try {
            listPoa = null;
            if (!TextUtils.isEmpty(idAnioFiscal)) {
                listPoa = pOAProyectoDelegate.getPOAsTrabajo(objeto.getId(), Integer.valueOf(idAnioFiscal), EstadoPOA.CONSOLIDANDO_POA);
                utPendientes = pOAProyectoDelegate.getUTPendientesParaConsolidar(objeto.getId(), Integer.valueOf(idAnioFiscal));
                completoParaConsolidado = utPendientes.isEmpty() && (listPoa != null && !listPoa.isEmpty());
            }
            recalcularTotales();
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    /**
     * Este método se encarga de reinicializar
     */
    @Override
    public void reloadPO() {
        initProyecto();
    }


    /**
     * Este método realiza la consolidación 
     * @throws IOException 
     */
    public void consolidar() throws IOException {
        try {
            pOAProyectoDelegate.consolidar(listPoa, objeto.getId());
            FacesContext.getCurrentInstance().getExternalContext().redirect(PAGE_VOLVER);
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    /**
     * Este método que retorna los años a seleccionar
     *
     * @return
     */
    public Map<String, String> getPosiblesAniosPOA() {
        Map<String, String> m = new LinkedHashMap<>();
        if (objeto != null) {
            for (AnioFiscal anio : posiblesAniosPOA) {
                m.put(String.valueOf(anio.getAnio()), String.valueOf(anio.getId()));
            }
        }
        return m;
    }

    /**
     * Este método retorna las lineas a de los POAs a consolidar
     * @return 
     */
    @Override
    public List<POLinea> getLineas() {
        List<POLinea> l = new LinkedList<>();
        if (listPoa != null) {
            for (POAProyecto iter : listPoa) {
                l.addAll(iter.getLineas());
            }
        }
        return l;
    }

    /**
     * Este método retorna los años disponibles para un PO
     * 
     * @return 
     */
    @Override
    public List<Integer> getListAniosPO() {
        List<Integer> l = new LinkedList<>();
        if (objeto != null) {
            int anio = DatesUtils.getYearOfDate(objeto.getInicio());
            int anioFin = DatesUtils.getYearOfDate(objeto.getFin());
            while (anio <= anioFin) {
                l.add(anio);
                anio++;
            }
        }
        return l;
    }


    private Map<String, BigDecimal> totales =new HashMap();;

    /**
     * Este método recalcula los totales y los carga en un Map.
     */
    public void recalcularTotales() {
        totales = new HashMap();
        if (listPoa != null) {
            BigDecimal totalglobal = BigDecimal.ZERO;
            for (POAProyecto iter : listPoa) {
                BigDecimal totalPOA = BigDecimal.ZERO;
                for (POLinea linea : iter.getLineas()) {
                    BigDecimal totalLinea = BigDecimal.ZERO;
                    for (POActividadBase actividad : linea.getActividades()) {
                        BigDecimal totalActividad = BigDecimal.ZERO;
                        for (POInsumos insumo : actividad.getInsumos()) {
                            if (insumo.getMontoTotal() != null) {
                                totalActividad = totalActividad.add(insumo.getMontoTotal());
                            }
                        }
                        totalLinea = totalLinea.add(totalActividad);
                        totales.put(getKeyMonto(actividad), totalActividad);
                    }
                    totalPOA = totalPOA.add(totalLinea);
                    totales.put(getKeyMonto(linea), totalLinea);
                }
                totalglobal = totalglobal.add(totalPOA);
                totales.put(getKeyMonto(iter), totalPOA);
            }
            totales.put(getKeyMonto(), totalglobal);
        }
    }

    /**
     * Este método retorna la clave que se uso en el mapa para guardar el monto
     * @return 
     */
    private String getKeyMonto() {
        return "global";
    }

    /**
     * Este método retorna la clave que se uso en el mapa para guardar los totales de las lineas
     * @param poa
     * @return 
     */
    private String getKeyMonto(GeneralPOA poa) {
        return "linea-" + poa.getId();
    }

    /**
     * Este método retorna las claves donde esta el monto de las lineas
     * @param linea
     * @return 
     */
    private String getKeyMonto(POLinea linea) {
        return "linea-" + linea.getId();
    }

    /**
     * Este método retorna la clave donde se encuentra el monto de la actividad
     * @param actividad
     * @return 
     */
    private String getKeyMonto(POActividadBase actividad) {
        return "actividad-" + actividad.getId();
    }
    
    
    
    /**
     * Este método retorna el total de las lineas
     * @param linea
     * @return 
     */
    @Override
    public BigDecimal getTotalLinea(POLinea linea) {
        BigDecimal total = totales.get(getKeyMonto(linea));
        if (total == null ){
            total = BigDecimal.ZERO;
        }
        return total;
    }

    /**
     * Este método retorna el total de las actividad
     * @param actividad
     * @return 
     */
    @Override
    public BigDecimal getTotalActividad(POActividadBase actividad) {
        BigDecimal total = totales.get(getKeyMonto(actividad));
        if (total == null ){
            total = BigDecimal.ZERO;
        }
        return total;
    }

    /**
     * Este método retorna el total del poa para el proyecto
     * @param poa
     * @return 
     */
    public BigDecimal getTotalPOA(POAProyecto poa) {
        BigDecimal total = totales.get(getKeyMonto(poa));
        if (total == null ){
            total = BigDecimal.ZERO;
        }
        return total;
    }
    
    /**
     * Este método retorna el total del monto global
     * @return 
     */
    public BigDecimal getTotalGlobal() {
        BigDecimal total = totales.get(getKeyMonto());
        if (total == null ){
            total = BigDecimal.ZERO;
        }
        return total;
    }
    
    
    
    /**
     * Este método se utiliza para eliminar una linea en el POA
     * @param tempLinea 
     */
    @Override
    public void eliminarTmpLinea(POLinea tempLinea) {
        poa.getLineas().remove(tempLinea);
    }

    /**
     * Este método retorna la fecha de inicio del POA
     * @return 
     */
    @Override
    public Date getInicioPO() {
        return objeto.getInicio();
    }
    
    /**
     * Este método retorna la fecha de fin del POA
     * @return 
     */
    @Override
    public Date getFinPO() {
        return objeto.getFin();
    }

    // <editor-fold defaultstate="collapsed" desc="MANEJO DE RIESGOS">
    protected POARiesgo tmpRiesgo;

    /**
     * Este método inicializa un riesgo nuevo
     */
    public void initRiesgo() {
        if (tmpRiesgo == null) {
            tmpRiesgo = new POARiesgo();
        }
    }

    /**
     * Este método guarda un el riesgo en edición
     */
    public void saveRiesgo() {
        try {
            if (!poa.getRiesgos().contains(tmpRiesgo)) {
                poa.getRiesgos().add(tmpRiesgo);
            }
            guardar();
            RequestContext.getCurrentInstance().execute("$('#anadirRiesgo').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Este método elimina el riesgo en edición
     */
    public void eliminarRiesgo() {
        try {
            poa.getRiesgos().remove(tmpRiesgo);
            guardar();
            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="get Componentes y macroActividades">
    /**
     * Este método se utiliza para armar la visualización de los montos usados por componentes
     * 
     * @param componente
     * @param map
     * @param nivel
     * @param tienePadreUT 
     */
    @Override
    protected void addComponentesRecursivo(ProyectoComponente componente, Map<String, Integer> map, int nivel, boolean tienePadreUT) {
        String padding = "";
        for (int i = 0; i < nivel; i++) {
            padding = padding + "&nbsp;";
        }
        map.put(padding + componente.getNombre(), componente.getId());

        for (ProyectoComponente hijo : componente.getComponenteHijos()) {
            addComponentesRecursivo(hijo, map, nivel + 1, tienePadreUT);
        }

    }

    /**
     * Este método se utiliza para armar la visualización de las  macroactividades 
     * 
     * @return 
     */
    @Override
    public Map<String, Integer> getMacroActividadesProyecto() {
        Map<String, Integer> map = new LinkedHashMap();
        if (objeto != null) {
            for (ProyectoMacroActividad macroActividad : objeto.getProyectoMacroactividad()) {
                if (macroActividad.getUnidadTecnica() != null) {
                    map.put(macroActividad.getMacroActividad().getNombre(), macroActividad.getId());
                }
            }
        }
        return map;
    }

    //para traer todos los montos y no filtrar por ut
    /**
     * Este método arma la estructura de árbol de los componentes.
     * 
     * @return 
     */
    @Override
    public TreeNode getRootNodeComponente() {
        TreeNode root = new DefaultTreeNode();
        root.setExpanded(true);
        for (ProyectoComponente componente : objeto.getProyectoComponentes()) {
            if (componente.getComponentePadre() == null) {
                root.getChildren().add(conectToTreeNodeComponente(componente, root));
            }
        }

        return root;
    }

    
    /**
     * Este método retorna las macroactividades del proyecto.
     * 
     * @return 
     */
    @Override
    public List<ProyectoMacroActividad> getMacroActividades() {
        return objeto.getProyectoMacroactividad();
    }

    /**
     * Este método añade la linea en edición.
     * 
     * @param tempLinea 
     */
    @Override
    public void addTmpLinea(POLinea tempLinea) {
        if (!poa.getLineas().contains(tempLinea)) {
            poa.getLineas().add(tempLinea);
        }
    }

    /**
     * Este método es el encargado de guardar una  linea
     */
    @Override
    public void saveLinea() {
        try {
            tempLinea.setColaboradoras(utColaboradoras.getTarget());
            tempLinea.setValoresProducto(valoresSeguimiento);

            UnidadTecnica ut = tempLinea.getProducto().getUnidadTecnica();

            poa = null;
            Iterator<POAProyecto> iter = listPoa.iterator();
            while (iter.hasNext() && poa == null) {
                POAProyecto tmpPOA = iter.next();
                if (tmpPOA.getUnidadTecnica().getId().equals(ut.getId())) {
                    poa = tmpPOA;
                }
            }

            addTmpLinea(tempLinea);

            //se vuelve a guardar el proyecto
            guardar();

            RequestContext.getCurrentInstance().execute("$('#anadirLinea').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }
    
    

    //getter
    /**
     * Este método retorna los posibles riesgos
     * @return 
     */
    public OrigenRiesgo[] getOrigenRiesgos() {
        return OrigenRiesgo.values();
    }
    
    /***
     * Este método retorna las valoraciones para cada riesgo
     * @return 
     */
    public ValoracionRiesgo[] getValoracionRiesgos() {
        return ValoracionRiesgo.values();
    }

    /**
     * Este método retorna si fue realizado el cierre de los  POA
     * @return 
     */
    public Boolean cirreAnualCompleto(){
        if (listPoa == null || listPoa.isEmpty()){
            return null;
        }
        
        for (POAProyecto p :listPoa ){
            if (p.getCierreAnual() == null || p.getCierreAnual().booleanValue() == false){
                return false;
            }
        }
        return true;
    }
    
    
    
    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public String getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(String idProyecto) {
        this.idProyecto = idProyecto;
    }

    public EntityManagementDelegate getEmd() {
        return emd;
    }

    public void setEmd(EntityManagementDelegate emd) {
        this.emd = emd;
    }

    public VersionesDelegate getVersionDelegate() {
        return versionDelegate;
    }

    public void setVersionDelegate(VersionesDelegate versionDelegate) {
        this.versionDelegate = versionDelegate;
    }

    public List<POAProyecto> getListPoa() {
        return listPoa;
    }

    public void setListPoa(List<POAProyecto> listPoa) {
        this.listPoa = listPoa;
    }

    public boolean isCompletoParaConsolidado() {
        return completoParaConsolidado;
    }

    public void setCompletoParaConsolidado(boolean completoParaConsolidado) {
        this.completoParaConsolidado = completoParaConsolidado;
    }

    public String getIdAnioFiscal() {
        return idAnioFiscal;
    }

    public void setIdAnioFiscal(String idAnioFiscal) {
        this.idAnioFiscal = idAnioFiscal;
    }

    public List<UnidadTecnica> getUtPendientes() {
        return utPendientes;
    }

    public void setUtPendientes(List<UnidadTecnica> utPendientes) {
        this.utPendientes = utPendientes;
    }

    public POAProyecto getPoa() {
        return poa;
    }

    public void setPoa(POAProyecto poa) {
        this.poa = poa;
    }

    public Integer getIdActividadNP() {
        return idActividadNP;
    }

    public POARiesgo getTmpRiesgo() {
        return tmpRiesgo;
    }

    public void setTmpRiesgo(POARiesgo tmpRiesgo) {
        this.tmpRiesgo = tmpRiesgo;
    }

    public void setIdActividadNP(Integer idActividadNP) {
        this.idActividadNP = idActividadNP;
    }
    // </editor-fold>
}
