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
public enum TipoImpuesto {
    PORCENTAJE("EstadoComun.PORCENTAJE"),
    TAZA_FIJA("EstadoComun.TAZA_FIJA");

    private String label;

    private TipoImpuesto(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
