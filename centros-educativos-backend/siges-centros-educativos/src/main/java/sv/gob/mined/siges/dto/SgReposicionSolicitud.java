/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.dto;

import java.io.Serializable;
import java.util.Objects;
import sv.gob.mined.siges.persistencia.entidades.SgArchivo;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgDefinicionTitulo;
import sv.gob.mined.siges.persistencia.entidades.seguridad.SgUsuarioLite;

/**
 *
 * @author usuario
 */
public class SgReposicionSolicitud implements Serializable {
    
    
    private Long tituloPk;
    
    private String numeroResolucion;
    
    private SgArchivo resolucion;
    
    private SgUsuarioLite usuarioSolicitante;
    
    private SgDefinicionTitulo definicionTitulo;
       
    

    public SgReposicionSolicitud() {
    }

    public Long getTituloPk() {
        return tituloPk;
    }

    public void setTituloPk(Long tituloPk) {
        this.tituloPk = tituloPk;
    }

    

    public String getNumeroResolucion() {
        return numeroResolucion;
    }

    public void setNumeroResolucion(String numeroResolucion) {
        this.numeroResolucion = numeroResolucion;
    }

    public SgArchivo getResolucion() {
        return resolucion;
    }

    public void setResolucion(SgArchivo resolucion) {
        this.resolucion = resolucion;
    }

    public SgUsuarioLite getUsuarioSolicitante() {
        return usuarioSolicitante;
    }

    public void setUsuarioSolicitante(SgUsuarioLite usuarioSolicitante) {
        this.usuarioSolicitante = usuarioSolicitante;
    }

    public SgDefinicionTitulo getDefinicionTitulo() {
        return definicionTitulo;
    }

    public void setDefinicionTitulo(SgDefinicionTitulo definicionTitulo) {
        this.definicionTitulo = definicionTitulo;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 19 * hash + Objects.hashCode(this.tituloPk);
        hash = 19 * hash + Objects.hashCode(this.numeroResolucion);
        hash = 19 * hash + Objects.hashCode(this.resolucion);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SgReposicionSolicitud other = (SgReposicionSolicitud) obj;
        if (!Objects.equals(this.tituloPk, other.tituloPk)) {
            return false;
        }
        if (!Objects.equals(this.numeroResolucion, other.numeroResolucion)) {
            return false;
        }
        if (!Objects.equals(this.resolucion, other.resolucion)) {
            return false;
        }
        return true;
    }





    
    
    

   
}
