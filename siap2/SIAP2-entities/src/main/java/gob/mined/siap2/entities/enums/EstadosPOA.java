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
public enum EstadosPOA {
    EN_ELABORACION("En Elaboración"),
    ENVIADO("Enviado"),
    APROBADO("Aprobado"),
    CERRADO("Cerrado");
    
    private String label;

    private EstadosPOA(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
