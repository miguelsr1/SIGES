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
public enum TipoProyecto {
    ADMINISTRATIVO("TipoProyecto.Administrativo", "Administrativo"),
    INVERSION("TipoProyecto.Inversion", "Inversión"),
    NO_INVERSION("TipoProyecto.NoInversion", "No Inversión");

    private String label;
    private String nombre;

    private TipoProyecto(String label, String nombre) {
        this.label = label;
        this.nombre = nombre;
    }

    public String getLabel() {
        return label;
    }

    public String getNombre() {
        return nombre;
    }
    
    
    
}
