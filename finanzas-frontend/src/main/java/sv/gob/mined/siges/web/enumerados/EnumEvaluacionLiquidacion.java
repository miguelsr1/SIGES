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
public enum EnumEvaluacionLiquidacion {
    CORRECTO("Correcto"),
    NO_CORRECTO("No Correcto");

    private final String text;

    private EnumEvaluacionLiquidacion(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
