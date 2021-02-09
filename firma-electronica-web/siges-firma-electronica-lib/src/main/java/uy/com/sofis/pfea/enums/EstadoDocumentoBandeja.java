/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uy.com.sofis.pfea.enums;

/**
 *
 * @author sofis3
 */
public enum EstadoDocumentoBandeja {
    PF("Pendiente"),
    FI("Firmado"),
    EX("Expirado"),
    EL("Eliminado"),
    EN("Enviado"),
    RE("Rechazado");
    
    private final String nombre;

    EstadoDocumentoBandeja(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

}
