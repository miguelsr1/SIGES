/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mined.siap2.to;

import java.io.Serializable;

/**
 *
 * @author sofis
 */
public class CompromisoArchivoTO implements Serializable{
    
    private String idCompromiso;
    private String nombreArchivo;

    public String getIdCompromiso() {
        return idCompromiso;
    }

    public void setIdCompromiso(String idCompromiso) {
        this.idCompromiso = idCompromiso;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }
    
    
}
