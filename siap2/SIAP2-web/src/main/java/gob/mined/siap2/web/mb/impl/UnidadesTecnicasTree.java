/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Inject;
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 * Este backing bean implementa los eventos y lógica de presentación de la página
 * que realiza la administración de las unidades técnica
 * 
 *
 * @author sofis
 */
@ViewScoped
@Named(value = "unidadesTecnicasTree")
public class UnidadesTecnicasTree implements Serializable {

    @Inject
    EntityManagementDelegate emd;
    private TreeNode root;
    private TreeNode selectedNode;
    private List<TreeNode> listaNodosConstruidos = new ArrayList<>();

    List<UnidadTecnica> lista;

    @PostConstruct
    public void init() {
        lista = emd.getEntities(UnidadTecnica.class.getName());
        for (UnidadTecnica ut : lista) {
            construir(ut);
        }
        root = buscarRaiz();
    }

    /**
     * Este método retorna el nodo correspondiente a una UT
     * 
     * @param idUT
     * @return 
     */
    public TreeNode getNode(Integer idUT) {
        for (TreeNode node : listaNodosConstruidos) {
            if (((UnidadTecnica) node.getData()).getId().equals(idUT)) {
                return node;
            }
        }
        return null;
    }

    /**
     * retorna la base del árbol
     *
     * @return
     */
    public TreeNode getRoot() {
        return root;
    }

    /**
     * retorna el árbol con la unidad técnica seleccionada
     *
     * @return
     */
    public TreeNode getRoot(Integer idUnidadTecnica) {
        clearSelection(root);
        if (idUnidadTecnica != null) {
            TreeNode node = getNode(idUnidadTecnica);
            if (node!= null){
                node.setSelected(true);
                expand(node);                
            }
        }
        return root;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public void nodeExpand(NodeExpandEvent event) {
        event.getTreeNode().setExpanded(true);
    }

    public void nodeCollapse(NodeCollapseEvent event) {
        event.getTreeNode().setExpanded(false);
    }

    public List<TreeNode> getListaNodosConstruidos() {
        return listaNodosConstruidos;
    }

    /**
     * agrega una UT al árbol en caso de no existir (y todos sus padres)
     *
     * @param uniTecnica
     * @return
     */
    private TreeNode construir(UnidadTecnica uniTecnica) {
        TreeNode result = buscarEnConstruidos(uniTecnica.getNombre());
        if (result != null) {//Encuentro en construidos
            return result;
        } else if (uniTecnica.getPadre() == null) {
            TreeNode node = new DefaultTreeNode(uniTecnica, null);
            listaNodosConstruidos.add(node);
            return node;
        } else {
            TreeNode node = new DefaultTreeNode(uniTecnica, construir(uniTecnica.getPadre()));
            listaNodosConstruidos.add(node);
            return node;
        }
    }

    /**
     * busca si una UT existe en el árbol construido
     *
     * @param uniNombre
     * @return
     */
    private TreeNode buscarEnConstruidos(String uniNombre) {
        for (TreeNode node : listaNodosConstruidos) {
            UnidadTecnica ut = (UnidadTecnica) node.getData();
            if (ut.getNombre().equals(uniNombre)) {
                return node;
            }
        }
        return null;
    }

    /**
     * retorna la raíz del árbol
     *
     * @return
     */
    private TreeNode buscarRaiz() {
        for (TreeNode node : listaNodosConstruidos) {
            UnidadTecnica ut = (UnidadTecnica) node.getData();
            if (ut.getPadre() == null) {
                return node;
            }
        }
        return null;
    }

    /**
     * expande todos los padres hasta un nodo
     *
     * @param treeNode
     */
    private void expand(TreeNode treeNode) {
        if (treeNode.getParent() != null) {
            treeNode.getParent().setExpanded(true);
            expand(treeNode.getParent());
        }
    }

    /**
     * elimina todas las selecciones
     * @param node 
     */
    public void clearSelection(TreeNode node) {
        node.setSelected(false);
        for (TreeNode t : node.getChildren()) {
            clearSelection(t);
        }
    }
    class KK {

        public KK(String contenido) {
            this.contenido = contenido;
        }
        String contenido;

        public String toString() {
            return contenido;
        }
    }

}
