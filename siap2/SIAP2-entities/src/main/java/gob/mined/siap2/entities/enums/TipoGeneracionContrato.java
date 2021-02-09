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
public enum TipoGeneracionContrato {
    POR_PROVEEDOR("TipoGeneracionContrato.POR_PROVEEDOR"),
    POR_ITEM ("TipoGeneracionContrato.POR_ITEM");

    private String label;

    private TipoGeneracionContrato(String label) {
        this.label = label;
    }
    
    public String getLabel() {
        return label;
    }
}
