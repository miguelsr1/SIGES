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
 * //<entitymap var="personaFisica" toentityvar="colono" property="personaFisica"/>
 * @author Sofis Solutions
 */
public class EntityMapTO extends ComponentTO{

    private String var;
    private String toentityvar;
    private String property;

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getToentityvar() {
        return toentityvar;
    }

    public void setToentityvar(String toentityvar) {
        this.toentityvar = toentityvar;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    @Override
    public Element toXML() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    

}
