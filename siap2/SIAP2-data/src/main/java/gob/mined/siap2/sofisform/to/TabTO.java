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
public class TabTO extends ContainerComponentTO {

    @Override
    public Element toXML() {
        Element toReturn = new Element("tab");
        toReturn = super.toXMLMetadata(toReturn);
        return toReturn;
    }
}