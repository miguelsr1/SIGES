/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl.padq;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.PAC;
import gob.mined.siap2.entities.data.impl.PACGrupo;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicion;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.web.datatypes.DataPacItem;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.ProcesoAdquisicionDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.TextMB;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.model.CheckboxTreeNode;
import org.primefaces.model.TreeNode;

/**
 * Esta clase incluye los métodos para gestionar insumos en un proceso de adquisición.
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "procesoAdqAgregarInsumos")
public class ProcesoAdqAgregarInsumos implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @Inject
    JSFUtils jSFUtils;

    @Inject
    ProcesoAdquisicionDelegate pAdqDelegate;

    @Inject
    EntityManagementDelegate emd;

    @Inject
    TextMB textMB;

    private boolean update = false;

    private String rubro = null;
    private String codigo = null;
    private String nombre = null;
    private String pacId = null;
    private String nombrePac = null;
    private String grupoId = null;
    private String nombreGrupo = null;

    private TreeNode[] selectedNodes;
    private List<POInsumos> insumosSeleccionados = new LinkedList();
    private Set<POInsumos> insumosAAgregar = new LinkedHashSet();
    private List<PAC> pacsAgregarInsumos;
    private TreeNode rootNode;
    private Integer procesoAdquisicionId;
    private String unidadTecnicaId;
    private ProcesoAdqMain mainBean;

    @PostConstruct
    public void init() {

        mainBean = (ProcesoAdqMain) JSFUtils.getBean("procesoAdqMain");
        procesoAdquisicionId = mainBean.getObjeto().getId();

        createRootNode();

    }

    /**
     * Borra todos los filtros de búsqueda
     */
    public void limpiarFiltros() {

        rubro = null;
        codigo = null;
        nombre = null;
        pacId = null;
        nombrePac = null;
        grupoId = null;
        nombreGrupo = null;

    }

    /**
     * Este método corresponde al evento de crear el nodo raíz.
     */
    public void createRootNode() {
        TreeNode root = new CheckboxTreeNode(new DataPacItem(DataPacItem.ROOT, null, false, null), null);

        Integer pacIdInt = null;
        try {
            pacIdInt = Integer.valueOf(pacId);
        } catch (Exception e) {
            pacIdInt = null;
        }

        Integer rubroInt = null;
        try {
            pacIdInt = Integer.valueOf(rubro);
        } catch (Exception e) {
            rubroInt = null;
        }

        Integer grupoIdInt = null;
        try {
            grupoIdInt = Integer.valueOf(grupoId);
        } catch (Exception e) {
            grupoIdInt = null;
        }

        Integer unidadTecnicaIdInt = null;
        try {
            unidadTecnicaIdInt = Integer.valueOf(unidadTecnicaId);
        } catch (Exception e) {
            unidadTecnicaIdInt = null;
        }

        pacsAgregarInsumos = pAdqDelegate.obtenerPacsGruposAgregarInsumos(pacIdInt, nombrePac, rubroInt, codigo, nombre, unidadTecnicaIdInt, grupoIdInt, nombreGrupo);

        for (PAC p : pacsAgregarInsumos) {
            String nombre = p.getNombre() + " " + p.getId();
            DataPacItem dpac = new DataPacItem(DataPacItem.PAC, nombre, false, p);
            TreeNode document1 = new CheckboxTreeNode(dpac, root);

            //AÑADE UN DUMMY PARA TODOS
            DataPacItem temp = new DataPacItem(DataPacItem.DUMMY, null, false, p);
            TreeNode dummy = new CheckboxTreeNode(temp, document1);
        }

        rootNode = root;

    }

    /**
     * Este método corresponde al evento de expandir un nodo.
     * @param event 
     */
    public void onNodeExpand(NodeExpandEvent event) {
        CheckboxTreeNode padre = (CheckboxTreeNode) event.getTreeNode();
        DataPacItem dataPadre = (DataPacItem) padre.getData();
        //borro dummy

        padre.getChildren().remove(0);
        padre.getChildren().clear();

        Integer pacIdInt = null;
        try {
            pacIdInt = Integer.valueOf(pacId);
        } catch (Exception e) {
            pacIdInt = null;
        }

        Integer rubroInt = null;
        try {
            pacIdInt = Integer.valueOf(rubro);
        } catch (Exception e) {
            rubroInt = null;
        }

        Integer grupoIdInt = null;
        try {
            grupoIdInt = Integer.valueOf(grupoId);
        } catch (Exception e) {
            grupoIdInt = null;
        }

        Integer unidadTecnicaIdInt = null;
        try {
            unidadTecnicaIdInt = Integer.valueOf(unidadTecnicaId);
        } catch (Exception e) {
            unidadTecnicaIdInt = null;
        }
        if (dataPadre.getTipo().equals(DataPacItem.PAC)) {
            List<PACGrupo> grupos = pAdqDelegate.obtenerPacsGruposConInsumosParaProcesosDeAdquisicionPorPacId(((PAC) dataPadre.getObjeto()).getId(), rubroInt, codigo, nombre, unidadTecnicaIdInt, grupoIdInt, nombreGrupo);
            for (PACGrupo grupo : grupos) {
                String nombre = grupo.getNombre() + " " + grupo.getId();
                DataPacItem dpac = new DataPacItem(DataPacItem.GRUPO, nombre, false, grupo);
                TreeNode document = new CheckboxTreeNode(dpac, event.getTreeNode());
                DataPacItem temp = new DataPacItem(DataPacItem.DUMMY, null, false, grupo);
                TreeNode dummy = new CheckboxTreeNode(temp, document);
                if (estaSeleccionado(grupo)) {
                    seleccionarNodo(document);
                    seleccionarNodo(dummy);
                }
            }
        } else if (dataPadre.getTipo().equals(DataPacItem.GRUPO)) {
            List<POInsumos> insumos = pAdqDelegate.obtenerPoInsumosParaProcesosDeAdquisicionPorPacGrupoId(((PACGrupo) dataPadre.getObjeto()).getId(), rubroInt, codigo, nombre, unidadTecnicaIdInt);
            for (POInsumos insumo : insumos) {
                String nom = insumo.getInsumo()!=null?insumo.getInsumo().getNombre(): "-";
                String id= insumo.getId()!=null? insumo.getId().toString():"";
                String observacion =insumo.getObservacion();
                String nombre = nom + " " + id + ((observacion!=null)? " ("+observacion+")":"");
                DataPacItem dpac = new DataPacItem(DataPacItem.INSUMO, nombre, false, insumo);
                TreeNode document = new CheckboxTreeNode(dpac, event.getTreeNode());

            }
        }
    }

    /**
     * Este método determina si un nodo está seleccionado.
     * @param nodo
     * @return 
     */
    public boolean estaSeleccionado(Object nodo) {
        if (selectedNodes == null) {
            return false;
        }
        if (nodo == null) {
            return false;
        }
        for (TreeNode iter : selectedNodes) {
            Object iterO = ((DataPacItem) iter.getData()).getObjeto();
            if (nodo.equals(iterO)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Identifica el nodo de insumo seleccionado
     * @param nodo 
     */
    public void seleccionarNodo(TreeNode nodo) {
        if (nodo != null) {
            nodo.setSelected(true);
            if (selectedNodes == null) {
                selectedNodes = new TreeNode[1];
            } else {
                selectedNodes = Arrays.copyOf(selectedNodes, selectedNodes.length + 1);
            }
            selectedNodes[selectedNodes.length - 1] = nodo;
        }
    }

    /**
     * Regenera el árbol de insumos a generar
     */
    public void filtrarArbol() {
        try {
            createRootNode();
            selectedNodes = null;
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
     * Este método corresponde al evento de agregar insumos seleccionados al proceso de compra.
     */
    public void agregarInsumosSelecionados() {
        Integer pacIdInt = null;
        try {
            pacIdInt = Integer.valueOf(pacId);
        } catch (Exception e) {
            pacIdInt = null;
        }

        Integer rubroInt = null;
        try {
            pacIdInt = Integer.valueOf(rubro);
        } catch (Exception e) {
            rubroInt = null;
        }

        Integer grupoIdInt = null;
        try {
            grupoIdInt = Integer.valueOf(grupoId);
        } catch (Exception e) {
            grupoIdInt = null;
        }

        Integer unidadTecnicaIdInt = null;
        try {
            unidadTecnicaIdInt = Integer.valueOf(unidadTecnicaId);
        } catch (Exception e) {
            unidadTecnicaIdInt = null;
        }
        try {
            if (selectedNodes == null || selectedNodes.length == 0) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECCIONAR_UN_INSUMO);
                throw b;
            }
            insumosAAgregar = new LinkedHashSet();
            for (TreeNode nodo : selectedNodes) {
                DataPacItem data = (DataPacItem) nodo.getData();
                if (data.getTipo().equals(DataPacItem.PAC)) {
                    List<TreeNode> nodosGrupos = nodo.getChildren();
                    DataPacItem dataGrupo = (DataPacItem) nodosGrupos.get(0).getData();
                    if (dataGrupo.getTipo().equals(DataPacItem.DUMMY)) {
                        //si es dummy traigo de la base todos los insumos de cada grupo
                        List<PACGrupo> grupos = pAdqDelegate.obtenerPacsGruposConInsumosParaProcesosDeAdquisicionPorPacId(((PAC) data.getObjeto()).getId(), rubroInt, codigo, nombre, unidadTecnicaIdInt, grupoIdInt, nombreGrupo);

                        for (PACGrupo grupo : grupos) {
                            insumosAAgregar.addAll(grupo.getInsumos());
                        }
                    }

                } else if (data.getTipo().equals(DataPacItem.GRUPO)) {
                    List<TreeNode> nodosInsumos = nodo.getChildren();
                    DataPacItem dataInsumo = (DataPacItem) nodosInsumos.get(0).getData();
                    if (dataInsumo.getTipo().equals(DataPacItem.DUMMY)) {
                        //si es dummy traigo de la base todos los insumos del grupo
                        List<POInsumos> insumosGrupo = pAdqDelegate.obtenerPoInsumosParaProcesosDeAdquisicionPorPacGrupoId(((PACGrupo) data.getObjeto()).getId(), rubroInt, codigo, nombre, unidadTecnicaIdInt);
                        insumosAAgregar.addAll(insumosGrupo);
                    }
                } else if (data.getTipo().equals(DataPacItem.INSUMO)) {
                    //es de tipo insumo
                    insumosAAgregar.add((POInsumos) data.getObjeto());

                }
            }

            ProcesoAdquisicion procAdq = mainBean.getObjeto();

            Boolean exito = pAdqDelegate.agregarInsumosProceso(procAdq, insumosAAgregar, true);
            if (!exito) {
                RequestContext.getCurrentInstance().execute("$('#confirmModalMonto').modal('show');");
                return;
            }
            mainBean.reloadProceso();
            InsumosDelProceso insumosProcesoBean = (InsumosDelProceso) JSFUtils.getBean("insumosDelProceso");
            insumosProcesoBean.cargarMapPoInsumosRecepcionTDR();

            createRootNode();

            String texto = textMB.obtenerTexto("labels.AgregaronInsumosCorrectamente");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, texto, texto));
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
     * Este método corresponde a la opción de agregar insumos al proceso.
     */
    public void confirmarAgregarInsumos() {
        try {
            ProcesoAdquisicion procAdq = mainBean.getObjeto();
            pAdqDelegate.agregarInsumosProceso(procAdq, new LinkedHashSet<>(insumosAAgregar), false);

            mainBean.reloadProceso();
            createRootNode();
            String texto = textMB.obtenerTexto("labels.AgregaronInsumosCorrectamente");
            RequestContext.getCurrentInstance().execute("$('#confirmModalMonto').modal('hide');");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, texto, texto));
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
     * Deshace los cambios no guardados en base
     */
    public void deshacer() {
        createRootNode();
    }

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public TreeNode[] getSelectedNodes() {
        return selectedNodes;
    }

    public void setSelectedNodes(TreeNode[] selectedNodes) {
        this.selectedNodes = selectedNodes;
    }

    public JSFUtils getjSFUtils() {
        return jSFUtils;
    }

    public void setjSFUtils(JSFUtils jSFUtils) {
        this.jSFUtils = jSFUtils;
    }

    public TreeNode getRootNode() {
        return rootNode;
    }

    public void setRootNode(TreeNode rootNode) {
        this.rootNode = rootNode;
    }

    public boolean isUpdate() {
        return update;
    }

    public List<POInsumos> getInsumosSeleccionados() {
        return insumosSeleccionados;
    }

    public void setInsumosSeleccionados(List<POInsumos> insumosSeleccionados) {
        this.insumosSeleccionados = insumosSeleccionados;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public String getRubro() {
        return rubro;
    }

    public void setRubro(String rubro) {
        this.rubro = rubro;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPacId() {
        return pacId;
    }

    public void setPacId(String pacId) {
        this.pacId = pacId;
    }

    public String getNombrePac() {
        return nombrePac;
    }

    public void setNombrePac(String nombrePac) {
        this.nombrePac = nombrePac;
    }

    public String getGrupoId() {
        return grupoId;
    }

    public void setGrupoId(String grupoId) {
        this.grupoId = grupoId;
    }

    public String getNombreGrupo() {
        return nombreGrupo;
    }

    public void setNombreGrupo(String nombreGrupo) {
        this.nombreGrupo = nombreGrupo;
    }

    public Map getUnidadesTecnicas() {
        Map unidades = new HashMap();
        List<UnidadTecnica> ll = emd.getEntities(UnidadTecnica.class.getName());
        for (UnidadTecnica u : ll) {
            unidades.put(u.getNombre(), String.valueOf(u.getId()));
        }
        return unidades;
    }

    public String getUnidadTecnicaId() {
        return unidadTecnicaId;
    }

    public void setUnidadTecnicaId(String unidadTecnicaId) {
        this.unidadTecnicaId = unidadTecnicaId;
    }

    // </editor-fold>
}
