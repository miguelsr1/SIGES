/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.web.enumerados;

/**
 *
 * @author sofis-iquezada
 */
public enum EnumPlanCompraUnidadMedida {
    METRO("Metro (m)"),
    KILOGRAMO("Kilogramo (kg)"),
    UNIDAD("Unidadad (u)"),
    PAQUETES("Paquetes");

    private final String text;

    private EnumPlanCompraUnidadMedida(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
