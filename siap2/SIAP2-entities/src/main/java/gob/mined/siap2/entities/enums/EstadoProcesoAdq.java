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
public enum EstadoProcesoAdq {
    NORMAL(0,"EstadoProcesoAdq.NORMAL"),
    PAUSA(1,"EstadoProcesoAdq.PAUSA"),
    DESIERTO(2,"EstadoProcesoAdq.DESIERTO"),
    SIN_EFECTO(3,"EstadoProcesoAdq.SIN_EFECTO");
    
    
    private final String label;
    private final int posicion;

    private EstadoProcesoAdq(int posicion, String label) {
        this.label = label;
        this.posicion=posicion;
    }
    public String getLabel() {
        return label;
    }
    public int getPosicion() {
        return posicion;
    }
}
