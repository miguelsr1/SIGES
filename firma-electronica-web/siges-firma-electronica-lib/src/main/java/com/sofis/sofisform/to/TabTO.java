/*
 *  Clase desarrollada por Sofis Solutions
 *  
 */
package com.sofis.sofisform.to;

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
