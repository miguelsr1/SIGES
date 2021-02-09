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
 *                                     <inputtextarea key =""
                                                    entityvar="personaFisica"
                                                    property="apellido"
                                                    propertyClass="String.class"
                                                    labelkey="Apellido"/>

 * @author Sofis Solutions
 */
public class InputTextAreaTO extends BindingComponentTO{

    private String cols;

    public String getCols() {
        return cols;
    }

    public void setCols(String cols) {
        this.cols = cols;
    }


    @Override
    public Element toXML() {
        Element toReturn = new Element("inputtextarea");
        toReturn = super.toXMLMetadata(toReturn);
        if (this.getCols() == null){
            toReturn.setAttribute("cols", "");
        }else{
            toReturn.setAttribute("cols", this.getCols());
        }
        
        return toReturn;
    }

}
