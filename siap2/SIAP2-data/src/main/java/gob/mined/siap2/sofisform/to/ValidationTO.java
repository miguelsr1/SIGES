/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.sofisform.to;

import java.io.Serializable;
import org.jdom.Element;

/**
 *
 * @author Sofis Solutions (www.sofis-solutions.com)
 */
public class ValidationTO implements Serializable{

    private String class_ = "";

    public String getClass_() {
        return class_;
    }

    public void setClass_(String class_) {
        this.class_ = class_;
    }

    public Element toXML() {
        Element toReturn = new Element("validation");
        toReturn.setAttribute("class", this.getClass_());
        return toReturn;
    }
    
}
