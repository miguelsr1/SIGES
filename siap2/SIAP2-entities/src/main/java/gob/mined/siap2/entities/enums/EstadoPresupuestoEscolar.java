/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.mined.siap2.entities.enums;


public enum EstadoPresupuestoEscolar {
    EN_PROCESO("En proceso"),
    FORMULADO("Formulado"),
    EN_AJUSTE("En ajuste"),
    AJUSTADO("Ajustado"),
    APROBADO("Aprobado"),
    EDITADO("Editado"),
    CERRADO("Cerrado");

    private final String text;

    private EstadoPresupuestoEscolar(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}
