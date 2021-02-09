/*
 *  Clase desarrollada por Sofis Solutions
 *   
 */

package com.sofis.sofisform.to;

import org.jdom.Element;

/**
 * Clase desarrollada por Sofis Solutions
 * <inputtext  key =""
 *            entityvar="personaFisica"
 *            property="nombre"
 *            propertyClass="String.class"
 *            defaultModelName =""
 *            defaultModelNameProperty ="">
 * </inputtext>
 * @author Sofis Solutions
 */
public class InputSecretTO extends BindingComponentTO{

    @Override
    public Element toXML() {
       Element toReturn = new Element("inputsecret");
       toReturn = super.toXMLMetadata(toReturn);
       return toReturn;
    }


   
}
