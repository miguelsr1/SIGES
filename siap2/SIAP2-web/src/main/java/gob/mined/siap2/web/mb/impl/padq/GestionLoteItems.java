/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl.padq;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.Archivo;
import gob.mined.siap2.entities.data.impl.Insumo;
import gob.mined.siap2.entities.data.impl.PAC;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicion;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionInsumo;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionItem;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionLote;
import gob.mined.siap2.entities.data.impl.RelacionProAdqItemInsumo;
import gob.mined.siap2.entities.data.impl.TDR;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.datatypes.DataLoteItem;
import gob.mined.siap2.web.datatypes.DataPacItem;
import gob.mined.siap2.web.delegates.ArchivoDelegate;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.ProcesoAdquisicionDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.TextMB;
import gob.mined.siap2.web.utils.ArchivoUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.model.UploadedFile;

/**
 * Esta clase implementa los métodos del backing bean de lotes e ítems del
 * proceso de adquisición.
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "gestionLoteItems")
public class GestionLoteItems implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @Inject
    JSFUtils jSFUtils;

    @Inject
    ProcesoAdquisicionDelegate pAdqDelegate;

    @Inject
    EntityManagementDelegate emd;

    @Inject
    TextMB textMB;
    @Inject
    private ArchivoDelegate archivoDelegate;

    private boolean update = false;

    private TreeNode[] selectedNodes;

    private List<ProcesoAdquisicionInsumo> insumosProceso = new ArrayList<>();
    private List<ProcesoAdquisicionInsumo> insumosSeleccionados = new ArrayList<>();

    private List<ProcesoAdquisicionItem> itemsProceso = new ArrayList<>();
    private List<ProcesoAdquisicionItem> itemsSeleccionados = new ArrayList<>();

    private List<ProcesoAdquisicionLote> lotesProceso = new ArrayList<>();

    private List<PAC> pacsAgregarInsumos;
    private TreeNode rootItem;
    private TreeNode rootLote;
    private TreeNode[] nodosItemSelecionados;
    private TreeNode nodoLoteSelecionado;
    private Integer procesoAdquisicionId;
    private String nombreItem;
    private String nombreLote;
    private ProcesoAdqMain mainBean;
    private boolean editItem = false;
    private boolean editLote = false;
    private DataLoteItem itemModificar;
    private DataLoteItem loteModificar;
    private boolean cargarItemsLote = false;

    private List<ProcesoAdquisicionInsumo> InsumosEliminados = new ArrayList<>();
    private List<RelacionProAdqItemInsumo> relacionesItemsInsumosEliminadas = new ArrayList<>();
    private List<ProcesoAdquisicionItem> ItemsEliminados = new ArrayList<>();
    private List<ProcesoAdquisicionLote> LotesEliminados = new ArrayList<>();
    private UploadedFile uploadedFile;
    private ProcesoAdquisicionItem tempItem;
    private boolean modificandoTDR = false;
    private String nroItem;
    private Integer sugerenciaNroItem;
    private String nombreLoteOriginal;
    protected TDR tempTDR;

    @PostConstruct
    public void init() {
        mainBean = (ProcesoAdqMain) JSFUtils.getBean("procesoAdqMain");
        procesoAdquisicionId = mainBean.getObjeto().getId();
        sugerenciaNroItem = pAdqDelegate.obtenerUltimoNroItemProceso(procesoAdquisicionId);
        sugerenciaNroItem++;
        nroItem = "" + sugerenciaNroItem;
        cargarArbolItem();
        cargarArbolLote();
        cargarItemsLote = false;
    }

    /**
     * Este método recarga los ítems y lotes del proceso de adquisición.
     */
    public void recargarGestionLoteItems() {
        cargarArbolItem();
        cargarArbolLote();
    }

    /**
     * Construye el árbol de ítems asociados al proceso de adquisición
     */
    private void cargarArbolItem() {
        rootItem = new DefaultTreeNode(new DataLoteItem(DataLoteItem.ROOT, null, false), null);
        List<ProcesoAdquisicionItem> items = mainBean.getObjeto().getItems();
        itemsProceso = items;
        for (ProcesoAdquisicionItem item : items) {
            DataLoteItem dt = new DataLoteItem(DataLoteItem.ITEM, item, true);
            dt.setNombre(item.getNroItem() + "-" + item.getNombre());
            dt.setNroItem(item.getNroItem());
            TreeNode itemNode = new DefaultTreeNode(dt, rootItem);
            Integer cantidadAdjudicadaItem = 0;
            //La lista de insumos del Item se obtiene de la tabla de relación entre insumos e item de los procesos de adquisición
            List<ProcesoAdquisicionInsumo> insumosDelItem = item.getInsumosTemporalesDelItem();
            for (ProcesoAdquisicionInsumo insumo : insumosDelItem) {
                DataLoteItem dltInsumo = new DataLoteItem(DataLoteItem.INSUMO, insumo, true);
                dltInsumo.setNombre(insumo.getPoInsumo().getId() + " - " + insumo.getInsumo().getNombre());
                Integer cantidadAUtilizarInsumo = item.getCantidadAUtilizarPorInsumo(insumo);
                dltInsumo.setCantidadItemInsumo(cantidadAUtilizarInsumo);
                TreeNode insumoNode = new DefaultTreeNode(dltInsumo, itemNode);
                cantidadAdjudicadaItem += cantidadAUtilizarInsumo;
            }
            dt.setCantidadItemInsumo(cantidadAdjudicadaItem);

        }

    }

    /**
     * Este método carga el árbol de lote.
     */
    private void cargarArbolLote() {
        rootLote = new DefaultTreeNode(new DataLoteItem(DataLoteItem.ROOT, null, false), null);
        List<ProcesoAdquisicionLote> lotes = mainBean.getObjeto().getLotes();
        /*pAdqDelegate.obtenerLotesProcesoAdquisicion(procesoAdquisicionId);*/
        for (ProcesoAdquisicionLote lote : lotes) {
            DataLoteItem dltLote = new DataLoteItem(DataLoteItem.LOTE, lote, true);
            dltLote.setNombre(lote.getNombre());

            TreeNode loteNode = new DefaultTreeNode(dltLote, rootLote);
            for (ProcesoAdquisicionItem item : lote.getItems()) {

                DataLoteItem dltItem = new DataLoteItem(DataLoteItem.ITEM, item, true);
                dltItem.setNombre(item.getNroItem() + "-" + item.getNombre());
                dltItem.setNroItem(item.getNroItem());
                dltItem.setObjeto(item);
                TreeNode itemNode = new DefaultTreeNode(dltItem, loteNode);
                //La lista de insumos del Item se obtiene de la tabla de relación entre insumos e item de los procesos de adquisición
                List<ProcesoAdquisicionInsumo> insumosDelItem = item.getInsumosTemporalesDelItem();
                Integer cantidadAdjudicadaItem = 0;
                for (ProcesoAdquisicionInsumo insumo : insumosDelItem) {
                    DataLoteItem dltInsumo = new DataLoteItem(DataLoteItem.INSUMO, insumo, true);
                    dltInsumo.setNombre(insumo.getPoInsumo().getId() + " - " + insumo.getInsumo().getNombre());
                    Integer cantidadAUtilizarInsumo = item.getCantidadAUtilizarPorInsumo(insumo);
                    dltInsumo.setCantidadItemInsumo(cantidadAUtilizarInsumo);
                    TreeNode insumoNode = new DefaultTreeNode(dltInsumo, itemNode);
                    cantidadAdjudicadaItem += cantidadAUtilizarInsumo;
                }
                dltItem.setCantidadItemInsumo(cantidadAdjudicadaItem);
            }

        }
    }

    /**
     * Este método determina si un lote está seleccionado.
     *
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
     * Este método marca un TDR como modificado.
     */
    public void modificarTDR() {
        modificandoTDR = true;
    }

    /**
     * Este método registra un nodo como seleccionado.
     *
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
     * Este método devuelve los insumos que aún no están asociados a un ítem.
     *
     * @return
     */
    public List<ProcesoAdquisicionInsumo> getInsumosPendientesItem() {
        insumosProceso = mainBean.getObjeto().getInsumos();

        List<ProcesoAdquisicionInsumo> listaInsumos = new ArrayList<>();
        //obtener nodos items

        for (ProcesoAdquisicionInsumo insumoProceso : insumosProceso) {
            if (insumoProceso.getCantidadRestante() > 0) {
                insumoProceso.setCantidadAUtilizar(insumoProceso.getCantidadRestante());
                listaInsumos.add(insumoProceso);
            }
        }

        Collections.sort(listaInsumos, new Comparator<ProcesoAdquisicionInsumo>() {
            @Override
            public int compare(ProcesoAdquisicionInsumo o1, ProcesoAdquisicionInsumo o2) {
                return o1.getPoInsumo().getId() - o2.getPoInsumo().getId();
            }
        });

        return listaInsumos;
    }

    /**
     * Este método devuelve todos los nodos del árbol correspondientes a ítems.
     *
     * @return
     */
    private List<TreeNode> obtenerTodosLosNodosItems() {
        List<TreeNode> nodosItems = new ArrayList<>();
        if (!rootItem.getChildren().isEmpty()) {
            nodosItems = rootItem.getChildren();
        }

        if (!rootLote.getChildren().isEmpty()) {
            List<TreeNode> nodosLote = rootLote.getChildren();
            for (TreeNode nodoLote : nodosLote) {
                List<TreeNode> nodosLoteItems = nodoLote.getChildren();
                if (!nodosLoteItems.isEmpty()) {
                    nodosItems.addAll(nodosLoteItems);
                }

            }
        }
        return nodosItems;

    }

    /**
     * Este método devuelve todos los insumos que ya están en un ítem.
     *
     * @return
     */
    public List<ProcesoAdquisicionInsumo> obtenerInsumosUtilizados() {

        List<ProcesoAdquisicionInsumo> insumosUtilizados = new ArrayList<>();
        if (rootLote == null) {

            List<ProcesoAdquisicionLote> lotes = mainBean.getObjeto().getLotes();
            for (ProcesoAdquisicionLote lote : lotes) {
                for (ProcesoAdquisicionItem item : lote.getItems()) {
                    //La lista de insumos del Item se obtiene de la tabla de relación entre insumos e item de los procesos de adquisición
                    List<ProcesoAdquisicionInsumo> insumosDelItem = item.getInsumosTemporalesDelItem();
                    insumosUtilizados.addAll(insumosDelItem);
                }

            }

        } else {

            List<TreeNode> nodosLotes = rootLote.getChildren();
            for (TreeNode nodoLote : nodosLotes) {
                for (TreeNode nodoItem : nodoLote.getChildren()) {
                    DataLoteItem dtlote = (DataLoteItem) nodoItem.getData();
                    ProcesoAdquisicionItem item = (ProcesoAdquisicionItem) dtlote.getObjeto();
                    //La lista de insumos del Item se obtiene de la tabla de relación entre insumos e item de los procesos de adquisición
                    List<ProcesoAdquisicionInsumo> insumosDelItem = item.getInsumosTemporalesDelItem();
                    insumosUtilizados.addAll(insumosDelItem);
                }
            }

        }

        List<TreeNode> nodosItems = rootItem.getChildren();
        for (TreeNode nodoItem : nodosItems) {

            DataLoteItem dtl = (DataLoteItem) nodoItem.getData();
            ProcesoAdquisicionItem Item = (ProcesoAdquisicionItem) dtl.getObjeto();
            //La lista de insumos del Item se obtiene de la tabla de relación entre insumos e item de los procesos de adquisición
            List<ProcesoAdquisicionInsumo> insumosDelItem = Item.getInsumosTemporalesDelItem();
            if (insumosDelItem != null && !insumosDelItem.isEmpty()) {
                insumosUtilizados.addAll(insumosDelItem);
            }
        }

        return insumosUtilizados;
    }

    /**
     * Este método es la acción correspondiente a la edición de un ítem.
     */
    public void editarItem() {
        try {
            if (nombreItem == null || nombreItem.isEmpty()) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_INGRESAR_NOMBRE_ITEM);
                throw b;
            }
            if (nroItem == null || nroItem.isEmpty()) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_INGRESAR_NRO_ITEM);
                throw b;
            }

            Integer enteroNroItem = null;
            try {
                enteroNroItem = Integer.valueOf(nroItem);
            } catch (Exception e) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_NRO_ITEM_INVALIDO);
                throw b;
            }

            boolean yaExisteNro = false;
            List<TreeNode> nodosLotes = rootLote.getChildren();
            for (TreeNode nodoLote : nodosLotes) {
                List<TreeNode> nodosItemsLote = nodoLote.getChildren();
                if (!nodosItemsLote.isEmpty()) {
                    Iterator<TreeNode> itNodesLote = nodosItemsLote.iterator();
                    while (!yaExisteNro && itNodesLote.hasNext()) {
                        TreeNode next = itNodesLote.next();
                        DataLoteItem dtl = (DataLoteItem) next.getData();
                        if (!itemModificar.equals(dtl) && dtl.getNroItem().equals(enteroNroItem)) {
                            yaExisteNro = true;
                        }
                    }

                    if (yaExisteNro) {
                        BusinessException b = new BusinessException();
                        b.addError(ConstantesErrores.ERR_YA_EXISTE_NRO_ITEM);
                        throw b;
                    }
                }
            }

            List<TreeNode> nodosItems = rootItem.getChildren();
            Iterator<TreeNode> itNodes = nodosItems.iterator();

            while (!yaExisteNro && itNodes.hasNext()) {
                TreeNode next = itNodes.next();
                DataLoteItem dtl = (DataLoteItem) next.getData();
                if (!itemModificar.equals(dtl) && dtl.getNroItem().equals(enteroNroItem)) {
                    yaExisteNro = true;
                }
            }

            if (yaExisteNro) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_YA_EXISTE_NRO_ITEM);
                throw b;
            }

            itemModificar.setNombre(nroItem + "-" + nombreItem);
            itemModificar.setNroItem(enteroNroItem);
            ProcesoAdquisicionItem item = (ProcesoAdquisicionItem) itemModificar.getObjeto();
            item.setNombre(nombreItem);
            item.setNroItem(enteroNroItem);
            editItem = false;
            nombreItem = "";

            if (enteroNroItem.equals(sugerenciaNroItem)) {
                sugerenciaNroItem++;
            } else if (enteroNroItem > sugerenciaNroItem) {
                sugerenciaNroItem = enteroNroItem + 1;
            }

            nroItem = "" + sugerenciaNroItem;

        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().scrollTo("tabsPanel");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().scrollTo("tabsPanel");
        }
    }

    /**
     * Este método permite agregar un ítem al proceso de adquisición.
     */
    public void agregarItem() {
        try {

            if (nombreItem == null || nombreItem.isEmpty()) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_INGRESAR_NOMBRE_ITEM);
                throw b;
            }

            if (nroItem == null || nroItem.isEmpty()) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_INGRESAR_NRO_ITEM);
                throw b;
            }
            Integer enteroNroItem = null;
            try {
                enteroNroItem = Integer.valueOf(nroItem);
            } catch (Exception e) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_NRO_ITEM_INVALIDO);
                throw b;
            }

            String nombreCompuesto = nroItem + "-" + nombreItem;
            boolean yaExisteNro = false;
            List<TreeNode> nodosLotes = rootLote.getChildren();
            for (TreeNode nodoLote : nodosLotes) {
                List<TreeNode> nodosItemsLote = nodoLote.getChildren();
                if (!nodosItemsLote.isEmpty()) {
                    Iterator<TreeNode> itNodesLote = nodosItemsLote.iterator();
                    while (!yaExisteNro && itNodesLote.hasNext()) {
                        TreeNode next = itNodesLote.next();
                        DataLoteItem dtl = (DataLoteItem) next.getData();
                        if (dtl.getNroItem().equals(enteroNroItem)) {
                            yaExisteNro = true;
                        }
                    }

                    if (yaExisteNro) {
                        BusinessException b = new BusinessException();
                        b.addError(ConstantesErrores.ERR_YA_EXISTE_NRO_ITEM);
                        throw b;
                    }
                }
            }

            List<TreeNode> nodosItems = rootItem.getChildren();
            Iterator<TreeNode> itNodes = nodosItems.iterator();

            while (!yaExisteNro && itNodes.hasNext()) {
                TreeNode next = itNodes.next();
                DataLoteItem dtl = (DataLoteItem) next.getData();
                if (dtl.getNroItem().equals(enteroNroItem)) {
                    yaExisteNro = true;
                }
            }

            if (yaExisteNro) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_YA_EXISTE_NRO_ITEM);
                throw b;
            }
            ProcesoAdquisicionItem ItemNuevo = new ProcesoAdquisicionItem();
            ItemNuevo.setNombre(nombreItem);
            ItemNuevo.setNroItem(enteroNroItem);
            ItemNuevo.setProcesoAdquisicion(mainBean.getObjeto());
            DataLoteItem dt = new DataLoteItem(DataLoteItem.ITEM, ItemNuevo, false);
            dt.setNombre(ItemNuevo.getNroItem() + "-" + ItemNuevo.getNombre());
            dt.setCantidadItemInsumo(0);
            dt.setNroItem(enteroNroItem);
            TreeNode itemNode = new DefaultTreeNode(dt, rootItem);
            nombreItem = "";
            if (enteroNroItem.equals(sugerenciaNroItem)) {
                sugerenciaNroItem++;
            } else if (enteroNroItem > sugerenciaNroItem) {
                sugerenciaNroItem = enteroNroItem + 1;
            }

            nroItem = "" + sugerenciaNroItem;

            itemsProceso.add(ItemNuevo);

        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().scrollTo("tabsPanel");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().scrollTo("tabsPanel");
        }
    }

    /**
     * Este método permite eliminar un ítem de la UI.
     *
     * @param Item
     */
    public void eliminarItem(DataLoteItem Item) {
        List<TreeNode> nodosItems = rootItem.getChildren();
        Iterator<TreeNode> itItems = nodosItems.iterator();
        TreeNode Nodoeliminar = null;
        boolean seborraInsumo = false;
        while (itItems.hasNext() && !seborraInsumo && Nodoeliminar == null) {
            TreeNode nextItem = itItems.next();
            DataLoteItem dtlItem = (DataLoteItem) nextItem.getData();
            if (dtlItem.getTipo().equals(Item.getTipo())) {
                if (dtlItem.getTipo().equals(DataLoteItem.ITEM)) {
                    ProcesoAdquisicionItem proItem = (ProcesoAdquisicionItem) dtlItem.getObjeto();
                    ProcesoAdquisicionItem proItemEliminar = (ProcesoAdquisicionItem) Item.getObjeto();
                    if (proItem.equals(proItemEliminar)) {
                        Nodoeliminar = nextItem;
                        if (proItemEliminar.getId() != null) {
                            ItemsEliminados.add(proItemEliminar);
                            itemsProceso.remove(proItemEliminar);
                        }

                    }

                }

            } else {
                //el nodo seleccionado no es hijo del nodo raiz, es nieto
                DataLoteItem dataloteItem = (DataLoteItem) nextItem.getData();
                ProcesoAdquisicionItem procesoItem = (ProcesoAdquisicionItem) dataloteItem.getObjeto();
                ProcesoAdquisicionInsumo insumo = (ProcesoAdquisicionInsumo) Item.getObjeto();
                //La lista de insumos del Item se obtiene de la tabla de relación entre insumos e item de los procesos de adquisición
                List<ProcesoAdquisicionInsumo> insumosDelItem = procesoItem.getInsumosTemporalesDelItem();
                if (insumosDelItem != null && !insumosDelItem.isEmpty()) {
                    insumosDelItem.remove(insumo);
                    seborraInsumo = true;
                }
            }
        }
        if (Nodoeliminar != null) {
            DataLoteItem dtl = (DataLoteItem) Nodoeliminar.getData();
            if (dtl.getTipo().equals(DataLoteItem.ITEM)) {
                rootItem.getChildren().remove(Nodoeliminar);
                if (rootItem.getChildren().isEmpty()) {
                    rootItem = new DefaultTreeNode(new DataLoteItem(DataLoteItem.ROOT, null, false), null);
                }
            } else {
                //ya no se ejecuta, se elimina el insumo del item, no andaba los hijos del item
                TreeNode nodoPadre = Nodoeliminar.getParent();
                nodoPadre.getChildren().remove(Nodoeliminar);
                ProcesoAdquisicionItem padqItem = (ProcesoAdquisicionItem) ((DataLoteItem) nodoPadre.getData()).getObjeto();
                ProcesoAdquisicionInsumo insumo = (ProcesoAdquisicionInsumo) ((DataLoteItem) Nodoeliminar.getData()).getObjeto();
                //La lista de insumos del Item se obtiene de la tabla de relación entre insumos e item de los procesos de adquisición
                List<ProcesoAdquisicionInsumo> insumosDelItem = padqItem.getInsumosTemporalesDelItem();
                insumosDelItem.remove(insumo);
                DataLoteItem dataloteItem = (DataLoteItem) nodoPadre.getData();
                if (dataloteItem.getCantidadItemInsumo() > 0) {
                    Integer cantidadItem = dataloteItem.getCantidadItemInsumo();
                    cantidadItem = cantidadItem - insumo.getCantidadAUtilizar();
                    dataloteItem.setCantidadItemInsumo(cantidadItem);
                }
            }

        }

    }

    /**
     * Este método permite eliminar un insumo de un ítems en la UI.
     */
    public void eliminarItemsInsumo() {
        try {
            if (nodosItemSelecionados == null || nodosItemSelecionados.length == 0) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_AL_MENOS_DEBE_SELECCIONAR_UN_ITEM);
                throw b;
            }
            List<TreeNode> nodosItems = new ArrayList<>();
            List<TreeNode> nodosInsumos = new ArrayList<>();
            for (TreeNode selectedNode : nodosItemSelecionados) {
                DataLoteItem dtl = (DataLoteItem) selectedNode.getData();
                if (dtl.getTipo().equals(DataLoteItem.ITEM)) {
                    nodosItems.add(selectedNode);
                } else {
                    nodosInsumos.add(selectedNode);
                }
            }
            for (TreeNode nodoItem : nodosItems) {
                DataLoteItem dataLoteItem = (DataLoteItem) nodoItem.getData();

                ProcesoAdquisicionItem itemEliminar = (ProcesoAdquisicionItem) dataLoteItem.getObjeto();

                if (itemEliminar.getOfertasProvedor() != null && !itemEliminar.getOfertasProvedor().isEmpty()) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_ITEM_TIENE_OFERTAS_ASOCIADAS);
                    throw b;
                }
            }
            /*
             Saco lo nodos insumos que se seleccionaron, 
             y pertenecen a un nodo item padre
             */
            for (TreeNode nodeInsumo : nodosInsumos) {
                TreeNode nodoItemPadre = nodeInsumo.getParent();
                ProcesoAdquisicionItem itemDeInsumoSelec = null;
                Iterator<TreeNode> itnodeItems = rootItem.getChildren().iterator();
                boolean encontrado = false;
                while (itnodeItems.hasNext() && !encontrado) {
                    TreeNode nodoItem = itnodeItems.next();
                    if (nodoItem.equals(nodoItemPadre)) {
                        itemDeInsumoSelec = (ProcesoAdquisicionItem) ((DataLoteItem) nodoItem.getData()).getObjeto();
                        encontrado = true;
                    }

                }

                ProcesoAdquisicionInsumo insumoEliminarDeItem = (ProcesoAdquisicionInsumo) ((DataLoteItem) nodeInsumo.getData()).getObjeto();
                RelacionProAdqItemInsumo relacion = null;
                if (itemDeInsumoSelec != null) {
                    relacion = itemDeInsumoSelec.getRelacionItemInsumoPorInsumo(insumoEliminarDeItem);
                }
                boolean encontroInsumoProceso = false;
                ProcesoAdquisicionInsumo insumoProceso = null;
                for (int i = 0; i < insumosProceso.size() && !encontroInsumoProceso; i++) {
                    if (insumosProceso.get(i).getId().equals(insumoEliminarDeItem.getId())) {
                        insumoProceso = insumosProceso.get(i);
                        encontroInsumoProceso = true;
                    }
                }
                //Antes de remover la relación, la agrego a la lista de Relaciones a eliminar
                relacionesItemsInsumosEliminadas.add(relacion);
                if (itemDeInsumoSelec != null && relacion != null) {
                    itemDeInsumoSelec.getRelItemInsumos().remove(relacion);
                }
                if (insumoProceso != null) {                    
                    insumoProceso.setCantidadAUtilizar(0);
                    if (relacion != null) {
                        insumoProceso.getRelItemInsumos().remove(relacion);
                    }
                }
            }

            for (TreeNode nodoItem : nodosItems) {
                DataLoteItem dataLoteItem = (DataLoteItem) nodoItem.getData();

                ProcesoAdquisicionItem itemEliminar = (ProcesoAdquisicionItem) dataLoteItem.getObjeto();
                if (itemEliminar.getOfertasProvedor() != null && !itemEliminar.getOfertasProvedor().isEmpty()) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_ITEM_TIENE_OFERTAS_ASOCIADAS);
                    throw b;
                }
                for (RelacionProAdqItemInsumo relacionesItem : itemEliminar.getRelItemInsumos()) {
                    ProcesoAdquisicionInsumo insumoModifCant = relacionesItem.getInsumo();
                    boolean encontroInsumoProceso = false;
                    ProcesoAdquisicionInsumo insumoProceso = null;
                    for (int i = 0; i < insumosProceso.size() && !encontroInsumoProceso; i++) {
                        if (insumosProceso.get(i).getId().equals(insumoModifCant.getId())) {
                            insumoProceso = insumosProceso.get(i);
                            insumoProceso.setCantidadAUtilizar(0);
                            encontroInsumoProceso = true;
                        }
                    }
                }
                itemEliminar.setRelItemInsumos(null);
                eliminarItem(dataLoteItem);
            }

            for (TreeNode nodoInsumo : nodosInsumos) {
                DataLoteItem dataLoteItem = (DataLoteItem) nodoInsumo.getData();
                eliminarItem(dataLoteItem);
            }
            nombreItem = null;
            nroItem = null;
            editItem = false;
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().scrollTo("tabsPanel");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().scrollTo("tabsPanel");
        }

    }

    /**
     * Este método permite eliminar ítems de un lote en la UI.
     */
    public void eliminarLoteItems() {
        try {
            if (nodoLoteSelecionado == null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECCIONAR_UN_LOTE);
                throw b;
            }
            DataLoteItem dataLoteItem = (DataLoteItem) nodoLoteSelecionado.getData();
            if (dataLoteItem.getTipo().equals(DataLoteItem.INSUMO)) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECCIONAR_UN_LOTE_O_ITEM);
                throw b;
            }

            eliminarLote(dataLoteItem);
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().scrollTo("tabsPanel");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().scrollTo("tabsPanel");
        }

    }

    /**
     * Este método permite eliminar un lote en la UI
     *
     * @param lote
     */
    public void eliminarLote(DataLoteItem lote) {
        List<TreeNode> nodosLotes = rootLote.getChildren();
        Iterator<TreeNode> itLotes = nodosLotes.iterator();
        TreeNode Nodoeliminar = null;
        while (Nodoeliminar == null && itLotes.hasNext()) {
            TreeNode nextLote = itLotes.next();
            DataLoteItem dtlLote = (DataLoteItem) nextLote.getData();

            if (dtlLote.getTipo().equals(lote.getTipo())) {
                if (dtlLote.getTipo().equals(DataLoteItem.LOTE)) {
                    ProcesoAdquisicionLote proLote = (ProcesoAdquisicionLote) dtlLote.getObjeto();
                    ProcesoAdquisicionLote proLoteEliminar = (ProcesoAdquisicionLote) lote.getObjeto();
                    if (proLote.equals(proLoteEliminar)) {
                        Nodoeliminar = nextLote;
                        if (proLoteEliminar.getId() != null) {
                            LotesEliminados.add(proLoteEliminar);
                        }

                    }

                }

            } else {

                //el nodo seleccionado no es hijo del nodo raiz, es nieto
                List<TreeNode> nodosItems = nextLote.getChildren();
                Iterator<TreeNode> itItems = nodosItems.iterator();
                while (Nodoeliminar == null && itItems.hasNext()) {

                    TreeNode nextItem = itItems.next();
                    DataLoteItem dtlItem = (DataLoteItem) nextItem.getData();

                    if (dtlItem.getTipo().equals(lote.getTipo())) {

                        ProcesoAdquisicionItem proItem = (ProcesoAdquisicionItem) dtlItem.getObjeto();
                        ProcesoAdquisicionItem proItemEliminar = (ProcesoAdquisicionItem) lote.getObjeto();
                        if (proItem.equals(proItemEliminar)) {

                            Nodoeliminar = nextItem;
                        }
                    }

                }
            }
        }

        if (Nodoeliminar != null) {
            DataLoteItem dtl = (DataLoteItem) Nodoeliminar.getData();
            if (dtl.getTipo().equals(DataLoteItem.LOTE)) {
                rootLote.getChildren().remove(Nodoeliminar);
                if (rootLote.getChildren().isEmpty()) {
                    rootLote = new DefaultTreeNode(new DataLoteItem(DataLoteItem.ROOT, null, false), null);
                }
                //tengo que volver a modificar rootItem para que aparezcan todos los nodos que se pasaron
                //el nodo a eliminar es de tipo lote debo obtener todos los nodos de tipo item y agregarlo al arbol de item
                List<TreeNode> nodosItems = Nodoeliminar.getChildren();

                rootItem.getChildren().addAll(nodosItems);

            } else {
                TreeNode nodoPadre = Nodoeliminar.getParent();
                nodoPadre.getChildren().remove(Nodoeliminar);
                ProcesoAdquisicionLote padqLote = (ProcesoAdquisicionLote) ((DataLoteItem) nodoPadre.getData()).getObjeto();
                ProcesoAdquisicionItem padqItemEliminar = (ProcesoAdquisicionItem) ((DataLoteItem) Nodoeliminar.getData()).getObjeto();
                padqLote.getItems().remove(padqItemEliminar);
                padqItemEliminar.setLote(null);
                padqItemEliminar.setProcesoAdquisicion(mainBean.getObjeto());
                //tengo que volver a modificar rootItem para que aparezca el nodo que se paso
                rootItem.getChildren().add(Nodoeliminar);
            }

        }

    }

    /**
     * Este método permite realizar las acciones de edición de un lote.
     */
    public void editarLote() {
        try {
            if (nombreLote == null || nombreLote.isEmpty()) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_INGRESAR_NOMBRE_LOTE);
                throw b;
            }

            List<TreeNode> nodosLotes = rootLote.getChildren();
            Iterator<TreeNode> itNodes = nodosLotes.iterator();
            Boolean seRepite = false;
            while (!seRepite && itNodes.hasNext()) {
                TreeNode next = itNodes.next();
                DataLoteItem dtl = (DataLoteItem) next.getData();
                if (!nombreLote.equals(nombreLoteOriginal) && dtl.getNombre().equalsIgnoreCase(nombreLote)) {
                    /*Puede estar solo una vez (el mismo que se está editando)*/
                    seRepite = true;
                }
            }

            if (seRepite) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_YA_EXISTE_NOMBRE_LOTE);
                throw b;
            }

            loteModificar.setNombre(nombreLote);
            ProcesoAdquisicionLote lote = (ProcesoAdquisicionLote) loteModificar.getObjeto();
            lote.setNombre(nombreLote);
            editLote = false;
            nombreLote = "";
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().scrollTo("tabsPanel");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().scrollTo("tabsPanel");
        }
    }

    /**
     * Este método es el evento asociado a la carga de un archivo.
     *
     * @param event
     */
    public void fileUploadListener(FileUploadEvent event) {
        try {
            uploadedFile = event.getFile();
            if (uploadedFile != null) {
                tempTDR.setTempUploadedFile(ArchivoUtils.getDataFile(uploadedFile));
            }
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Este método está asociado al evento de agregar un lote.
     */
    public void agregarLote() {
        try {
            if (nombreLote == null || nombreLote.isEmpty()) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_INGRESAR_NOMBRE_LOTE);
                throw b;
            }

            List<TreeNode> nodosLotes = rootLote.getChildren();
            Iterator<TreeNode> itNodes = nodosLotes.iterator();
            boolean yaExisteNombre = false;
            while (!yaExisteNombre && itNodes.hasNext()) {
                TreeNode next = itNodes.next();
                DataLoteItem dtl = (DataLoteItem) next.getData();
                if (dtl.getNombre().equalsIgnoreCase(nombreLote)) {
                    yaExisteNombre = true;
                }
            }

            if (yaExisteNombre) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_YA_EXISTE_NOMBRE_LOTE);
                throw b;
            }

            ProcesoAdquisicionLote loteNuevo = new ProcesoAdquisicionLote();
            loteNuevo.setNombre(nombreLote);
            loteNuevo.setProcesoAdquisicion(mainBean.getObjeto());
            DataLoteItem dt = new DataLoteItem(DataLoteItem.LOTE, loteNuevo, false);
            dt.setNombre(loteNuevo.getNombre());
            TreeNode itemNode = new DefaultTreeNode(dt, rootLote);
            nombreLote = "";
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().scrollTo("tabsPanel");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().scrollTo("tabsPanel");
        }
    }

    /**
     * Este método permite la descarga de un archivo.
     *
     * @param archivo
     */
    public void downloadFile(Archivo archivo) {
        try {
            ArchivoUtils.downloadFile(archivo, archivoDelegate.getFile(archivo));
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Este método realiza el movimiento de ítems seleccionados.
     */
    public void moverInsumosSelecionados() {
        try {

            //verificaque tenga insumo selecionado
            if (nodosItemSelecionados == null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECCIONAR_UN_ITEM);
                throw b;
            }

            //verifica que sea uno solo el item selecionado
            int cantNodoItem = 0;
            TreeNode nodoItem = null;
            for (TreeNode selectedNode : nodosItemSelecionados) {
                DataLoteItem dtl = (DataLoteItem) selectedNode.getData();
                if (dtl.getTipo().equals(DataLoteItem.ITEM)) {

                    cantNodoItem++;
                    nodoItem = selectedNode;
                }
            }
            if (cantNodoItem > 1) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECCIONAR_UN_SOLO_ITEM);
                throw b;
            }

            //verifica que exista un insumo selecionado
            if (insumosSeleccionados == null || insumosSeleccionados.isEmpty()) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_AL_MENOS_DEBE_SELECCIONAR_UN_INSUMO);
                throw b;
            }

            DataLoteItem dlt = (DataLoteItem) nodoItem.getData();
            Integer cantidadItem = dlt.getCantidadItemInsumo();

            ProcesoAdquisicionInsumo insumo = insumosSeleccionados.get(0);
            Insumo TipoInsumo = insumo.getInsumo();
            for (ProcesoAdquisicionInsumo insumoSelect : insumosSeleccionados) {
                if (!insumoSelect.getInsumo().equals(TipoInsumo)) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_NO_SE_PERMITE_INSUMOS_DISTINTO_TIPO_EN_ITEM);
                    throw b;
                }
                if (insumoSelect.getCantidadAUtilizar() > insumoSelect.getCantidadRestante()) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_CANTIDAD_ACTUAL_INSUMO_DEBE_SER_MAYOR_IGUAL_CANTIDAD_UTILIZAR);
                    throw b;
                }
                if (insumoSelect.getCantidadAUtilizar() < 1) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_CANTIDAD_UTILIZAR_INSUMO_DEBE_SER_MAYOR_0);
                    throw b;
                }
                TipoInsumo = insumoSelect.getInsumo();

                cantidadItem += insumo.getCantidadAUtilizar();
            }

            dlt.setCantidadItemInsumo(cantidadItem);
            ProcesoAdquisicionItem item = (ProcesoAdquisicionItem) dlt.getObjeto();
            //La lista de insumos del Item se obtiene de la tabla de relación entre insumos e item de los procesos de adquisición
            List<ProcesoAdquisicionInsumo> insumosDelItem = item.getInsumosTemporalesDelItem();

            if (insumosDelItem != null) {
                for (ProcesoAdquisicionInsumo insumoItem : insumosDelItem) {
                    for (ProcesoAdquisicionInsumo insumoSelect : insumosSeleccionados) {
                        if (insumoItem.equals(insumoSelect)) {
                            BusinessException b = new BusinessException();
                            b.addError(ConstantesErrores.ERR_EL_INSUMO_YA_ESTA_ASIGNADO_AL_ITEM);
                            throw b;
                        }
                        if (!insumoItem.getInsumo().equals(insumoSelect.getInsumo())) {
                            BusinessException b = new BusinessException();
                            b.addError(ConstantesErrores.ERR_NO_SE_PERMITE_INSUMOS_DISTINTO_TIPO_EN_ITEM);
                            throw b;
                        }
                    }

                }
                insumosDelItem.addAll(insumosSeleccionados);
            }

            //recorro los insumos del proceso y a los que estan seleccionados le seteo la nueva relación con el item
            for (ProcesoAdquisicionInsumo ins : mainBean.getObjeto().getInsumos()) {
                for (ProcesoAdquisicionInsumo insSelect : insumosSeleccionados) {
                    if (ins.getId().equals(insSelect.getId())) {
                        RelacionProAdqItemInsumo relacionItemInsumo = new RelacionProAdqItemInsumo();
                        relacionItemInsumo.setInsumo(ins);
                        relacionItemInsumo.setItem(item);
                        relacionItemInsumo.setCantidad(ins.getCantidadAUtilizar());
                        item.getRelItemInsumos().add(relacionItemInsumo);
                        ins.getRelItemInsumos().add(relacionItemInsumo);
                    }
                }
            }
            nodosItemSelecionados = null;

        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().scrollTo("tabsPanel");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().scrollTo("tabsPanel");
        }
    }

    /**
     * Este método permite obtener el ítem raíz.
     *
     * @return
     */
    public TreeNode getRootItem() {
        //fixes bug primefaces
        List<TreeNode> Items = rootItem.getChildren();
        TreeNode root = new DefaultTreeNode(new DataLoteItem(DataLoteItem.ROOT, null, false), null);
        for (TreeNode Item : Items) {
            TreeNode ItemCopia = new DefaultTreeNode(Item.getData(), root);
            DataLoteItem dtl = (DataLoteItem) Item.getData();
            ProcesoAdquisicionItem item = (ProcesoAdquisicionItem) dtl.getObjeto();
            //La lista de insumos del Item se obtiene de la tabla de relación entre insumos e item de los procesos de adquisición
            List<ProcesoAdquisicionInsumo> insumosDelItem = item.getInsumosTemporalesDelItem();
            if (insumosDelItem != null) {
                Integer cantidadItem = 0;
                for (ProcesoAdquisicionInsumo insumo : insumosDelItem) {
                    DataLoteItem dltInsumo = new DataLoteItem(DataLoteItem.INSUMO, insumo, false);
                    dltInsumo.setNombre(insumo.getPoInsumo().getId() + " - " + insumo.getInsumo().getNombre());
                    Integer cantidadAUtilizarInsumo = item.getCantidadAUtilizarPorInsumo(insumo);
                    dltInsumo.setCantidadItemInsumo(cantidadAUtilizarInsumo);
                    TreeNode insumoNode = new DefaultTreeNode(dltInsumo, ItemCopia);
                    cantidadItem += cantidadAUtilizarInsumo;
                }
                dtl.setCantidadItemInsumo(cantidadItem);
            }

        }
        return root;
    }

    /**
     * Este método realiza la carga de un ítem.
     */
    public void cargarItem() {
        try {
            int cantNodoItem = 0;
            TreeNode nodoItem = null;
            if (nodosItemSelecionados == null || nodosItemSelecionados.length == 0) {
                nroItem = "" + sugerenciaNroItem;
                nombreItem = "";
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_AL_MENOS_DEBE_SELECCIONAR_UN_ITEM);
                throw b;
            }
            for (TreeNode selectedNode : nodosItemSelecionados) {
                DataLoteItem dtl = (DataLoteItem) selectedNode.getData();
                if (dtl.getTipo().equals(DataLoteItem.ITEM)) {

                    cantNodoItem++;
                    nodoItem = selectedNode;
                }

            }
            if (cantNodoItem > 1) {
                BusinessException b = new BusinessException();
                nroItem = "" + sugerenciaNroItem;
                nombreItem = "";
                b.addError(ConstantesErrores.ERR_DEBE_SELECCIONAR_UN_SOLO_ITEM);
                throw b;
            }

            DataLoteItem dtl = null;
            if (nodoItem == null) {
                nroItem = "" + sugerenciaNroItem;
                nombreItem = "";
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_NO_SELECCIONO_UN_ITEM_DEBE_SELECCIONAR_UN_ITEM);
                throw b;
            } else {
                dtl = (DataLoteItem) nodoItem.getData();
                if (!dtl.getTipo().equals(DataLoteItem.ITEM)) {
                    nroItem = "" + sugerenciaNroItem;
                    nombreItem = "";
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_NO_SELECCIONO_UN_ITEM_DEBE_SELECCIONAR_UN_ITEM);
                    throw b;
                }
            }

            editItem = true;
            nombreItem = ((ProcesoAdquisicionItem) dtl.getObjeto()).getNombre();
            nroItem = "" + dtl.getNroItem();
            itemModificar = dtl;
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().scrollTo("tabsPanel");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().scrollTo("tabsPanel");
        }
    }

    /**
     * Este método realiza la carga de lotes en la UI.
     */
    public void cargarLote() {
        try {

            if (nodoLoteSelecionado == null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECCIONAR_UN_LOTE);
                throw b;
            }
            TreeNode nodoLote = nodoLoteSelecionado;
            DataLoteItem dtl = (DataLoteItem) nodoLote.getData();
            if (!dtl.getTipo().equals(DataLoteItem.LOTE)) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_NO_SELECCIONO_UN_LOTE_DEBE_SELECCIONAR_UN_LOTE);
                throw b;
            }
            editLote = true;
            nombreLoteOriginal = dtl.getNombre();
            nombreLote = dtl.getNombre();
            loteModificar = dtl;
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().scrollTo("tabsPanel");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().scrollTo("tabsPanel");
        }
    }

    /**
     * Este método mueve los ítems seleccionados al lote.
     */
    public void moverItemsSelecionados() {
        try {
            if (nodoLoteSelecionado == null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECCIONAR_UN_LOTE);
                throw b;
            }
            if (nodosItemSelecionados == null || nodosItemSelecionados.length == 0) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_AL_MENOS_DEBE_SELECCIONAR_UN_ITEM);
                throw b;
            }
            DataLoteItem dl = (DataLoteItem) nodoLoteSelecionado.getData();
            if (!dl.getTipo().equals(DataLoteItem.LOTE)) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECCIONAR_UN_LOTE);
                throw b;
            }

            List<TreeNode> NodosAPasar = new ArrayList<>();
            for (TreeNode itemNode : nodosItemSelecionados) {
                DataLoteItem dltItem = (DataLoteItem) itemNode.getData();
                if (dltItem.getTipo().equals(DataLoteItem.ITEM)) {
                    //La lista de insumos del Item se obtiene de la tabla de relación entre insumos e item de los procesos de adquisición
                    List<ProcesoAdquisicionInsumo> insumosDelItem = ((ProcesoAdquisicionItem) dltItem.getObjeto()).getInsumosTemporalesDelItem();
                    if (insumosDelItem.isEmpty()) {
                        BusinessException b = new BusinessException();
                        b.addError(ConstantesErrores.ERR_ITEM_NO_TIENE_INSUMOS);
                        throw b;
                    }
                    NodosAPasar.add(itemNode);
                } else {
                    TreeNode nodoPadre = itemNode.getParent();
                    if (nodoPadre != null && ((DataLoteItem) nodoPadre.getData()).getTipo().equals(DataLoteItem.ITEM)) {
                        if (!NodosAPasar.contains(nodoPadre)) {

                            NodosAPasar.add(nodoPadre);
                        }

                    }

                }

            }

            for (TreeNode nodosAPasar : NodosAPasar) {
                DataLoteItem dltItem = (DataLoteItem) nodosAPasar.getData();

                DataLoteItem dtlLote = (DataLoteItem) nodoLoteSelecionado.getData();
                ProcesoAdquisicionLote padqlote = (ProcesoAdquisicionLote) dtlLote.getObjeto();
                ProcesoAdquisicionItem item = (ProcesoAdquisicionItem) dltItem.getObjeto();
                if (padqlote.getItems() == null) {
                    padqlote.setItems(new ArrayList<ProcesoAdquisicionItem>());
                }
                padqlote.getItems().add(item);
                item.setLote(padqlote);
                item.setProcesoAdquisicion(null);

                nodosAPasar.setParent(nodoLoteSelecionado);
                rootItem.getChildren().remove(nodosAPasar);

            }

            nodoLoteSelecionado.getChildren().addAll(NodosAPasar);

            if (rootItem.getChildren().isEmpty()) {
                rootItem = new DefaultTreeNode(new DataLoteItem(DataLoteItem.ROOT, null, false), null);
            }
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().scrollTo("tabsPanel");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().scrollTo("tabsPanel");
        }
    }

    /**
     * Este método realiza la carga los TDR de un ítem en la UI.
     *
     * @param item
     */
    public void cargarItemTDR(DataLoteItem item) {

        tempItem = (ProcesoAdquisicionItem) item.getObjeto();
        modificandoTDR = false;
        initTDR();
    }

    /**
     * Este método realiza la inicialización de los TDR.
     */
    public void initTDR() {
        tempTDR = null;
        if (tempItem.getId() != null) {
            tempTDR = pAdqDelegate.getTDRItem(tempItem.getId());
        }

        if (tempTDR == null) {
            tempTDR = new TDR();
            RequestContext.getCurrentInstance().execute("$('#anadirTDRItem').modal('show');");
        } else {
            RequestContext.getCurrentInstance().execute("$('#verTDRItemModal').modal('show');");
        }
    }

    /**
     * Este método permite guardar los TDR cargados.
     */
    public void guardarTDR() {
        try {
            if (TextUtils.isEmpty(tempTDR.getContenido()) && tempTDR.getTempUploadedFile() == null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_TDR_VACIO_AL_MENOS_ESCRIBA_TEXTO_O_SELECCIONE_ARCHIVO);
                throw b;
            }

            pAdqDelegate.saveTDRItem(tempItem.getId(), tempTDR);

            mainBean.reloadProceso();
            recargarGestionLoteItems();

            RequestContext.getCurrentInstance().execute("$('#anadirTDRItem').modal('hide');");
            RequestContext.getCurrentInstance().execute("$('#verTDRItemModal').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Este método se corresponde con el evento guardar de un proceso de
     * adquisición.
     */
    public void guardar() {
        try {
            ProcesoAdquisicion proceso = mainBean.getObjeto();
            List<ProcesoAdquisicionLote> lotes = new ArrayList<>();
            List<TreeNode> nodosLotes = rootLote.getChildren();
            for (TreeNode nodoLote : nodosLotes) {
                DataLoteItem dtl = (DataLoteItem) nodoLote.getData();
                ProcesoAdquisicionLote pal = (ProcesoAdquisicionLote) dtl.getObjeto();
                List<ProcesoAdquisicionItem> items = pal.getItems();
                if (items == null || items.isEmpty()) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_LOTE_SIN_ITEMS);
                    throw b;
                }

                pal.setProcesoAdquisicion(proceso);
                lotes.add(pal);
            }

            List<ProcesoAdquisicionItem> items = new ArrayList<>();
            List<TreeNode> nodosItem = rootItem.getChildren();

            for (TreeNode nodoItem : nodosItem) {
                DataLoteItem dtl = (DataLoteItem) nodoItem.getData();
                ProcesoAdquisicionItem pai = (ProcesoAdquisicionItem) dtl.getObjeto();
                pai.setLote(null);
                //La lista de insumos del Item se obtiene de la tabla de relación entre insumos e item de los procesos de adquisición
                List<ProcesoAdquisicionInsumo> insumosDelItem = pai.getInsumosTemporalesDelItem();
                if (insumosDelItem == null || insumosDelItem.isEmpty()) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_ITEM_NO_TIENE_INSUMOS);
                    throw b;
                }
                items.add(pai);
            }
            /*"Emparejo" las relaciones (Items-Insumos) que tienen asociadas los items con las que tienen asociadas los insumos*/

            mainBean.setObjeto(pAdqDelegate.guardarLoteItem(proceso, items, lotes, ItemsEliminados, LotesEliminados, relacionesItemsInsumosEliminadas));
            mainBean.reloadProceso();
            relacionesItemsInsumosEliminadas = new ArrayList<>();
            ItemsEliminados = new ArrayList<>();
            LotesEliminados = new ArrayList<>();

            recargarGestionLoteItems();
            String texto = textMB.obtenerTexto("labels.CambiosGuardadoCorrectamente");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, texto, texto));
            RequestContext.getCurrentInstance().scrollTo("tabsPanel");
        } catch (GeneralException ex) {

            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().scrollTo("tabsPanel");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().scrollTo("tabsPanel");
        }

    }

    /**
     * Este método corresponde al evento deshacer.
     */
    public void deshacer() {
        cargarArbolItem();
        cargarArbolLote();
        cargarInsumos();
    }

    /**
     * Este método corresponde al evento de cargar insumos.
     */
    private void cargarInsumos() {
        List<ProcesoAdquisicionInsumo> insumosBD = mainBean.delegate.obtenerInsumosDelProceso(mainBean.getObjeto().getId());
        mainBean.getObjeto().setInsumos(insumosBD);
    }

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public boolean getModificandoTDR() {
        return modificandoTDR;
    }

    public void setModificandoTDR(boolean modificandoTDR) {
        this.modificandoTDR = modificandoTDR;
    }

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

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public List<ProcesoAdquisicionInsumo> getInsumosProceso() {
        return insumosProceso;
    }

    public void setInsumosProceso(List<ProcesoAdquisicionInsumo> insumosProceso) {
        this.insumosProceso = insumosProceso;
    }

    public List<ProcesoAdquisicionInsumo> getInsumosSeleccionados() {
        return insumosSeleccionados;
    }

    public void setInsumosSeleccionados(List<ProcesoAdquisicionInsumo> insumosSeleccionados) {
        this.insumosSeleccionados = insumosSeleccionados;
    }

    public String getNombreItem() {
        return nombreItem;
    }

    public void setNombreItem(String nombreItem) {
        this.nombreItem = nombreItem;
    }

    public void setRootItem(TreeNode rootItem) {
        this.rootItem = rootItem;
    }

    public TreeNode[] getNodosItemSelecionados() {
        return nodosItemSelecionados;
    }

    public void setNodosItemSelecionados(TreeNode[] nodosItemSelecionados) {
        this.nodosItemSelecionados = nodosItemSelecionados;
    }

    public TreeNode getRootLote() {
        if (!cargarItemsLote) {
            cargarArbolLote();
            cargarItemsLote = true;
        }

        return rootLote;
    }

    public void setRootLote(TreeNode rootLote) {
        this.rootLote = rootLote;
    }

    public String getNombreLote() {
        return nombreLote;
    }

    public void setNombreLote(String nombreLote) {
        this.nombreLote = nombreLote;
    }

    public TreeNode getNodoLoteSelecionado() {
        return nodoLoteSelecionado;
    }

    public void setNodoLoteSelecionado(TreeNode nodoLoteSelecionado) {
        this.nodoLoteSelecionado = nodoLoteSelecionado;
    }

    public boolean isEditItem() {
        return editItem;
    }

    public void setEditItem(boolean editarItem) {
        this.editItem = editarItem;
    }

    public boolean isEditLote() {
        return editLote;
    }

    public void setEditLote(boolean editLote) {
        this.editLote = editLote;
    }

    public ProcesoAdquisicionItem getTempItem() {
        return tempItem;
    }

    public void setTempItem(ProcesoAdquisicionItem tempItem) {
        this.tempItem = tempItem;
    }

    public TDR getTempTDR() {
        return tempTDR;
    }

    public void setTempTDR(TDR tempTDR) {
        this.tempTDR = tempTDR;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public String getNroItem() {
        return nroItem;
    }

    public void setNroItem(String nroItem) {
        this.nroItem = nroItem;
    }

    public List<ProcesoAdquisicionItem> getItemsProceso() {
        return itemsProceso;
    }

    public void setItemsProceso(List<ProcesoAdquisicionItem> itemsProceso) {
        this.itemsProceso = itemsProceso;
    }

    public String getNombreLoteOriginal() {
        return nombreLoteOriginal;
    }

    public void setNombreLoteOriginal(String nombreLoteOriginal) {
        this.nombreLoteOriginal = nombreLoteOriginal;
    }
    // </editor-fold>

}
