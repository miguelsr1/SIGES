/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.mined.siap2.entities.enums;


public enum EstadoTransferenciaACed {
    
    AUTORIZADO("Autorizado"),
    SOLICITADO("Solicitado"),
    TRANSFERIDO("Transferido"),
    CERRADO("Cerrado");

    private final String label;

    private EstadoTransferenciaACed(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}
