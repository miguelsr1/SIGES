/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.mined.siap2.entities.enums;

/**
 *
 * @author bruno
 */
public enum OEGCaracteres {
    MENOR7("Menor 7 Caracteres"),
    IGUAL7("7 Caracteres"),
    MAYOR7("Mayor 7 Caracteres");

    private String label;

    private OEGCaracteres(String label) {
        this.label = label;
    }

    
    private OEGCaracteres() {
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    
    
}
