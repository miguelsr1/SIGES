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
public class InputRichTextLabelTO extends BindingComponentTO{

    private String labelKey;
    private String customConfigPath;

    public String getLabelKey() {
        return labelKey;
    }

    public void setLabelKey(String labelKey) {
        this.labelKey = labelKey;
    }

    public String getCustomConfigPath() {
        return customConfigPath;
    }

    public void setCustomConfigPath(String customConfigPath) {
        this.customConfigPath = customConfigPath;
    }

    @Override
    public Element toXML() {
        Element toReturn = new Element("inputrichtextlabel");
        toReturn = super.toXMLMetadata(toReturn);
        if (this.getCustomConfigPath() == null){
            toReturn.setAttribute("customConfigPath", "");
        }else{
            toReturn.setAttribute("customConfigPath", this.getCustomConfigPath());
        }

        if (this.getLabelKey() == null){
            toReturn.setAttribute("labelKey", "");
        }else{
            toReturn.setAttribute("labelKey", this.getLabelKey());
        }
        
        return toReturn;
    }
    



}
