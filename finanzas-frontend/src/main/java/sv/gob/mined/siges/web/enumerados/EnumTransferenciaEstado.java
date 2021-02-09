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
public enum EnumTransferenciaEstado {
    AUTORIZADO("Autorizado"),
    SOLICITADO("Solicitado"),
    TRANSFERIDO("Transferido"),
    CERRADO("Cerrado");

    private final String text;

    private EnumTransferenciaEstado(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
