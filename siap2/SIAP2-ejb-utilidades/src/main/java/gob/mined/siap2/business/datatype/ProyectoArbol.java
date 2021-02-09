/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatype;

import java.util.List;

/**
 * Estructura de árbol de un proyecto.
 *
 * @author Sofis Solutions
 */
public class ProyectoArbol {

    //texto html que se muestra

    private String name;
    //color de fondo
    private String color;
    private String cssClassNode = "";
    //hijos
    private List<ProyectoArbol> children;

    // <editor-fold defaultstate="collapsed" desc="getter-setter"> 
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCssClassNode() {
        return cssClassNode;
    }

    public void setCssClassNode(String cssClassNode) {
        this.cssClassNode = cssClassNode;
    }

    public List<ProyectoArbol> getChildren() {
        return children;
    }

    public void setChildren(List<ProyectoArbol> children) {
        this.children = children;
    }

    // </editor-fold>
}
