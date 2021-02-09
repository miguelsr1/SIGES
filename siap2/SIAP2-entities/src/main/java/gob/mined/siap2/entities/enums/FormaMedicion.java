/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.enums;

/**
 *
 * @author Sofis Solutions
 */
public enum FormaMedicion  {
    CANTIDAD ("FormaMedicion.CANTIDAD"),
    MONEDA ("FormaMedicion.MONEDA");

    private String label;

    private FormaMedicion(String label) {
        this.label = label;
    }
    
    public String getLabel() {
        return label;
    }
    
}
