/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.entities.data.SsUsuario;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.UnidadTecnicaDelegate;
import gob.mined.siap2.web.delegates.UsuarioDelegate;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.inject.Inject;
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 * Este clase es un controlador genérico para el componente de seleccionar una UT.
 * Para usarla basta agregar el componente y extender el MB de dicha clase
 *
 * @author sofis
 */
//@ViewScoped
//@Named(value = "selectOneUTBean")
public class SelectOneUTBean implements Serializable {

    @Inject
    EntityManagementDelegate emd;
    @Inject
    UnidadTecnicaDelegate unidadTecnicaDelegate;
    @Inject
    UsuarioDelegate usuarioDelegate;

    private List<TreeNode> listaNodosConstruidos = new LinkedList();
    private TreeNode root;
    private TreeNode selectedNodeArbolUT;

    protected UnidadTecnica unidadTecnicaSelecionada;
    protected boolean mostrarARbolUT = false;
    private List<UnidadTecnica> cacheUnidadesTecnicas = null;

    public void initSelectOneUTBean(){
        mostrarARbolUT = false;
    }
    /**
     * método que se llama cuando el usuario escribe en el autocomplete y filtra
     * los resultados
     *
     * @param query
     * @return
     */
    public List<UnidadTecnica> completeTextUT(String query) {
        List<UnidadTecnica> l = new LinkedList<>();
        for (UnidadTecnica ut : getUnidadesTecnicas()) {
            if (ut.getNombre().contains(query)) {
                l.add(ut);
            }
        }
        return l;
    }

    /**
     * método que se llama cuando selecciona una UT en el árbol. Marva como
     * seleccionada la UT en el autocomplete
     *
     * @param event
     */
    public void onNodeSelectArbolUT(NodeSelectEvent event) {
        selectedNodeArbolUT = event.getTreeNode();
        unidadTecnicaSelecionada = (UnidadTecnica) selectedNodeArbolUT.getData();
    }

    //otro evento del arbol
    public void nodeExpandArbolUT(NodeExpandEvent event) {
        event.getTreeNode().setExpanded(true);
    }

    
    /**
     * Este método corresponde al evento colapsar del árbol
     * @param event 
     */
    public void nodeCollapseArbolUT(NodeCollapseEvent event) {
        event.getTreeNode().setExpanded(false);
    }

    /**
     * retorna el árbol con la unidad técnicas
     *
     * @return
     */
    public TreeNode getArbolUT() {
        //si el arbol no es construido se construye por primera vez
        if (root == null) {
            for (UnidadTecnica ut : getUnidadesTecnicas()) {
                construir(ut);
            }
            root = buscarRaiz();
        } else {
            //sino limpa las selecion
            clearSelection(root);
        }
        if (unidadTecnicaSelecionada != null) {
            TreeNode node = getNode(unidadTecnicaSelecionada.getId());
            if (node != null) {
                node.setSelected(true);
                expand(node);
            }
        }
        return root;
    }

    /**
     * Carga el árbol de UT
     * @param root 
     */
    public void setArbolUT(TreeNode root) {
        this.root = root;
    }

    /**
     * retorna el nodo del árbol que contiene esa UT
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
     * retorna la lista de unidades técnicas del sistema
     *
     * @return
     */
    private List<UnidadTecnica> getUnidadesTecnicas() {
        if (cacheUnidadesTecnicas == null) {
            cacheUnidadesTecnicas = emd.getEntities(UnidadTecnica.class.getName());
        }
        return cacheUnidadesTecnicas;
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
     *
     * @param node
     */
    public void clearSelection(TreeNode node) {
        node.setSelected(false);
        for (TreeNode t : node.getChildren()) {
            clearSelection(t);
        }
    }
    
    /**
     * Hace que se pueda mostrar o ocultar el árbol de UT
     */
    public void cambiarMostrarArbolUT(){
        this.mostrarARbolUT = !this.mostrarARbolUT;
    }

    
    
    /**
     * completa el campo cuando se escribe el usuario
     * @param query
     * @return 
     */
    public List<SsUsuario> completeTextUsuario(String query) {
        if (unidadTecnicaSelecionada != null) {
            List<SsUsuario> l = usuarioDelegate.getUsuariosConNombreEnUT(unidadTecnicaSelecionada.getId(), query);
            return l;
        }
        return Collections.emptyList();
    }
    
    /**
     * método que muestra el label del usuario
     * @param usuario
     * @return 
     */
    public String getNombreResponsable(SsUsuario usuario) {
        if (usuario == null) {
            return null;
        }
        return usuario.getUsuPrimerNombre() + " " + usuario.getUsuPrimerApellido();
    }

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public UnidadTecnica getUnidadTecnicaSelecionada() {
        return unidadTecnicaSelecionada;
    }

    public void setUnidadTecnicaSelecionada(UnidadTecnica unidadTecnicaSelecionada) {
        this.unidadTecnicaSelecionada = unidadTecnicaSelecionada;
    }

    public TreeNode getSelectedNodeArbolUT() {
        return selectedNodeArbolUT;
    }

    public void setSelectedNodeArbolUT(TreeNode selectedNodeArbolUT) {
        this.selectedNodeArbolUT = selectedNodeArbolUT;
    }


    public boolean isMostrarARbolUT() {
        return mostrarARbolUT;
    }

    public void setMostrarARbolUT(boolean mostrarARbolUT) {
        this.mostrarARbolUT = mostrarARbolUT;
    }

   
    // </editor-fold>
}
