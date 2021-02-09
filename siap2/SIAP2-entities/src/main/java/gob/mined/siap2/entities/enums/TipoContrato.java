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
public enum TipoContrato {

    CONTRATO("TipoContrato.CONTRATO"),
    OC("TipoContrato.OC");
   

    private String label;

    private TipoContrato(String label) {
        this.label = label;
    }
    
    public String getLabel() {
        return label;
    }


}
