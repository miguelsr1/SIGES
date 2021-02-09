/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.datatypes;

import gob.mined.siap2.entities.data.impl.Categoria;
import java.util.List;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Sofis Solutions
 */
public class DataTiposDeIndicadoresTree {
    Categoria tipo;
    TreeNode tree;
    List<String> titulosSeguimiento;

    public Categoria getTipo() {
        return tipo;
    }

    public void setTipo(Categoria tipo) {
        this.tipo = tipo;
    }

    public TreeNode getTree() {
        return tree;
    }

    public void setTree(TreeNode tree) {
        this.tree = tree;
    }

    public List<String> getTitulosSeguimiento() {
        return titulosSeguimiento;
    }

    public void setTitulosSeguimiento(List<String> titulosSeguimiento) {
        this.titulosSeguimiento = titulosSeguimiento;
    }
    
    
    
}
