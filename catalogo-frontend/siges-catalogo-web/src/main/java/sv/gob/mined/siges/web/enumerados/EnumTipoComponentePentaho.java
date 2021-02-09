/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.web.enumerados;

/**
 *
 * @author user
 */
public enum EnumTipoComponentePentaho {
    TABLERO("Tableros"),
    REPORTE("Reportes"), 
    CUBO("Cubos"); 
    private final String text;

    private EnumTipoComponentePentaho(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
