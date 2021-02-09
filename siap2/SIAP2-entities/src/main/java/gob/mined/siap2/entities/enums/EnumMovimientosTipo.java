/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package gob.mined.siap2.entities.enums;

/**
 * Tipo de movimiento del presupuesto escolar
 *
 * @author jgiron
 */
public enum EnumMovimientosTipo {
    I("Ingresos"),
    E("Egresos"),
    IM("Ingresos Modificado"),
    EM("Egresos Modificado");

    private final String text;

    private EnumMovimientosTipo(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}
