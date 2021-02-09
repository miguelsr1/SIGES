/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.enums;

/**
 *
 * @author eduardo
 */
public enum TipoActaContrato {
    ANTICIPO("TipoActaContrato.ANTICIPO", "ANT"),
    RECEPCION("TipoActaContrato.RECEPCION", "REC"),
    DEVOLUCION("TipoActaContrato.DEVOLUCION", "DEV");
   

    private String label;
    private String labelAbreviado;

    private TipoActaContrato(String label, String abrev) {
        this.label = label;        
        this.labelAbreviado = abrev;
    }
    
    public String getLabel() {
        return label;
    }
    
    public String getLabelAbreviado() {
        return labelAbreviado;
    }

}
