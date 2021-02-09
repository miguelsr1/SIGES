/*
 *  Clase desarrollada por Sofis Solutions
 *  
 */
package com.sofis.sofisform.to;

import org.jdom.Element;

/**
 * Clase desarrollada por Sofis Solutions
 *                                     <inputtextlabel key =""
entityvar="personaFisica"
property="apellido"
propertyClass="String.class"
labelkey="Apellido"/>

 * @author Sofis Solutions
 */
public class InputTextLabelTO extends InputTextTO {

    private String labelKey;

    public String getLabelKey() {
        return labelKey;
    }

    public void setLabelKey(String labelKey) {
        this.labelKey = labelKey;
    }

    @Override
    public Element toXML() {
        Element toReturn = new Element("inputtextlabel");
        toReturn = super.toXMLMetadata(toReturn);
        if (this.getLabelKey() == null) {
            toReturn.setAttribute("labelKey", "");
        } else {
            toReturn.setAttribute("labelKey", this.getLabelKey());
        }

        return toReturn;
    }
}
