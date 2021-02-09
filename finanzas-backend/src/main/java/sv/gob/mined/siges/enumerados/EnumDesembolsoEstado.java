/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.enumerados;

/**
 * Estado de Desembolso
 * @author sofis-iquezada
 */
public enum EnumDesembolsoEstado {
    EN_PROCESO("En proceso"),
    AUTORIZADO("Autorizado"),
    DEPOSITADO("Depositado");

    private final String text;

    private EnumDesembolsoEstado(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
