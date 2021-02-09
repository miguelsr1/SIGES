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
