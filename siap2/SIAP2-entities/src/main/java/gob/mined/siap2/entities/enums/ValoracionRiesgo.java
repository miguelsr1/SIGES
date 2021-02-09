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
public enum ValoracionRiesgo {
    ALTO("ValoracionRiesgo.ALTO"),
    MEDIO("ValoracionRiesgo.MEDIO"),
    BAJO("ValoracionRiesgo.BAJO");

    private String label;

    private ValoracionRiesgo(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
    
    public String obtenerColor() {
        switch(label) {
            case "ValoracionRiesgo.ALTO": return "red"; 
            case "ValoracionRiesgo.MEDIO": return "yellow";
                case "ValoracionRiesgo.BAJO": return "green";
        }
        return "";
    }
}
