/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.sofisform.to;

import org.jdom.Element;

/**
 * Clase desarrollada por Sofis Solutions
 * @author Sofis Solutions
 */
public class PanelGridTO extends ContainerComponentTO{

    private int columns;

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    @Override
    public Element toXML() {
        Element toReturn = new Element("panelgrid");
        toReturn = super.toXMLMetadata(toReturn);
        toReturn.setAttribute("columns", this.getColumns() +"");
        return toReturn;
    }

}
