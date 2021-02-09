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
public enum TipoSeguimiento {
    MENSUAL("TipoSeguimiento.mensual", "Mensual"),
    TRIMESTRAL("TipoSeguimiento.trimestral", "Trimestral"),
    CUATRIMESTRAL("TipoSeguimiento.cuatrimestral", "Cuatrimestral"),
    SEMESTRAL("TipoSeguimiento.semestral", "Semestral"),
    ANUAL("TipoSeguimiento.anual", "Anual") ;

    private String label;
    private String nombre;

    private TipoSeguimiento(String label, String nombre) {
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
