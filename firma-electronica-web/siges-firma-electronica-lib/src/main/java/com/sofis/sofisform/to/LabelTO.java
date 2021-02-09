/*
 *  Clase desarrollada por Sofis Solutions
 *  
 */

package com.sofis.sofisform.to;

import org.jdom.Element;

/**
 * Clase desarrollada por Sofis Solutions
 *                                     <label key =""
 *                                             entityvar=""
 *                                             property=""
 *                                             propertyClass=""
 *                                            text=""
 *                                       />
 *
 * @author Sofis Solutions
 */
public class LabelTO extends BindingComponentTO{

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public Element toXML() {
        Element toReturn = new Element("label");
        toReturn = super.toXMLMetadata(toReturn);
        toReturn.setAttribute("text", this.getText() +"");
        return toReturn;
    }




}
