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
 *                                     <inputtextarealabel key =""
                                                    entityvar="personaFisica"
                                                    property="apellido"
                                                    propertyClass="String.class"
                                                    labelkey="Apellido"/>

 * @author Sofis Solutions
 */
public class InputTextAreaLabelTO extends BindingComponentTO{

    private String labelKey;
    private String cols;

    public String getCols() {
        return cols;
    }

    public void setCols(String cols) {
        this.cols = cols;
    }

    

    public String getLabelKey() {
        return labelKey;
    }

    public void setLabelKey(String labelKey) {
        this.labelKey = labelKey;
    }

    @Override
    public Element toXML() {
        Element toReturn = new Element("inputtextarealabel");
        toReturn = super.toXMLMetadata(toReturn);
        if (this.getCols() == null){
            toReturn.setAttribute("cols", "");
        }else{
            toReturn.setAttribute("cols", this.getCols());
        }

        if (this.getLabelKey() == null){
            toReturn.setAttribute("labelKey", "");
        }else{
            toReturn.setAttribute("labelKey", this.getLabelKey());
        }
        
        return toReturn;
    }
    



}
