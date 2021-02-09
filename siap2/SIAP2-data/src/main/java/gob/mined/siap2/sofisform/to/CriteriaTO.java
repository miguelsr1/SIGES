/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.sofisform.to;

import java.io.Serializable;
import java.util.HashMap;
import org.jdom.Element;

/**
 *
 * @author Sofis Solutions (www.sofis-solutions.com)
 */
public abstract class CriteriaTO implements Serializable{

    public abstract String getStringQuery(HashMap col, HashMap leftJoin);
    public abstract HashMap<String,ValueCriteriaTO> getValueCriteriaTO();
    public abstract Element toXML();
    
   
}
