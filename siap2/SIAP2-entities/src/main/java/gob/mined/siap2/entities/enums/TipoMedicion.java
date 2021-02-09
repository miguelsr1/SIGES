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
public enum TipoMedicion {
    PORCENTAJE("Porcentaje"),
    VALOR_REAL("Valor Real");
    
    private String label;

    private TipoMedicion(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
