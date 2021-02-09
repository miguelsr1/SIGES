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
public enum EstadoTopePresupuestal {
    EN_PROCESO("En proceso", 1),
    FORMULACION("Formulación", 2),
    APROBACION("En ajuste", 3),
    CERRADO("Cerrado", 4);

    private final String label;
    private final Integer valor;

     private EstadoTopePresupuestal(String label, Integer valor) {
        this.label = label;
        this.valor = valor;
    }

    public Integer getValor() {
        return valor;
    }

    public String getLabel() {
        return label;
    }

}
