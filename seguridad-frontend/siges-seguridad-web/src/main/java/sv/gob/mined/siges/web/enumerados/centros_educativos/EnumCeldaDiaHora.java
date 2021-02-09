/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.web.enumerados.centros_educativos;

public enum EnumCeldaDiaHora {
    LUNES("Lunes"),
    MARTES("Martes"),
    MIERCOLES("Miércoles"),
    JUEVES("Jueves"),
    VIERNES("Viernes"),
    SABADO("Sabado"),
    DOMINGO("Domingo");
    private final String text;

    private EnumCeldaDiaHora(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    
}

