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
public enum EnumEstadoLiquidacion {
    CONFIRMADA("Confirmada por el Centro"),
    EVALUADA("Evaluada Parcial"),
    APROBADA("Aprobada");

    private final String text;

    private EnumEstadoLiquidacion(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
