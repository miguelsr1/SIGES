/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.web.enumerados;

/**
 *
 * @author sofis-smena
 */
public enum EnumDocumento {
    
    CONVENIO("Convenio de Transferencia"),
    CONGELACION("Carta de congelación de fondos");

    private final String text;

    private EnumDocumento(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
