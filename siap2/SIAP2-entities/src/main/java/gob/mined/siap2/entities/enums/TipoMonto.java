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
public enum TipoMonto {
    FORMULADO("Formulado", 1),
    APROBADO("Aprobado", 2);


    private final String label;
    private final Integer valor;

     private TipoMonto(String label, Integer valor) {
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
