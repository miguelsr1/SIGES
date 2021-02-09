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
public enum EstadoTransferencias {
    SOLICITADA("Solicitada", 1),
    REALIZADA("Realizada", 2),
    CERRADA("Cerrada", 3);

    private final String label;
    private final Integer valor;

     private EstadoTransferencias(String label, Integer valor) {
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
