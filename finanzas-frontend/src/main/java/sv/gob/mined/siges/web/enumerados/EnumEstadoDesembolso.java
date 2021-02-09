/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.enumerados;

/**
 *
 * @author jgiron
 */
public enum EnumEstadoDesembolso {
    EN_PROCESO("P"),
    APROBADO("A"),
    DEPOSITADO("D");

    private final String text;

    private EnumEstadoDesembolso(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
