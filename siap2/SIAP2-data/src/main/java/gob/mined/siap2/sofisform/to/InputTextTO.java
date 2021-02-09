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
 * <inputtext  key =""
 *            entityvar="personaFisica"
 *            property="nombre"
 *            propertyClass="String.class"
 *            defaultModelName =""
 *            defaultModelNameProperty ="">
 * </inputtext>
 * @author Sofis Solutions
 */
public class InputTextTO extends BindingComponentTO{

    @Override
    public Element toXML() {
       Element toReturn = new Element("inputtext");
       toReturn = super.toXMLMetadata(toReturn);
       return toReturn;
    }

   
}
