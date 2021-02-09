/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import sv.gob.mined.siges.web.enumerados.EnumEstadoArchivoCalificacionPAES;

/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "acpPk", scope = SgArchivoCalificacionPAES.class)
public class SgArchivoCalificacionPAES implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long acpPk;
    
    private SgArchivo acpArchivo;
    
    private EnumEstadoArchivoCalificacionPAES acpEstadoArchivo;
    
    private String acpResultado;
    
    private LocalDateTime acpUltModFecha;
    
    private String acpUltModUsuario;
    
    private Integer acpVersion;
    
    private Integer acpAnio;

    public SgArchivoCalificacionPAES() {
        acpAnio=LocalDate.now().getYear();
    }

    public SgArchivoCalificacionPAES(Long acpPk) {
        this.acpPk = acpPk;
    }

    
    //<editor-fold defaultstate="collapsed" desc="GET & SET">
   
    @JsonIgnore
    public String getHtmlRespuesta(){
        if (this.acpResultado != null) {
            return this.acpResultado.replace("-", " <br/> ");
        }
        return null;
    }
    
    @JsonIgnore
    public String getHtmlRespuestaCorta(){
        if (this.acpResultado != null) {
            if (this.acpResultado.length() > 200){
                return this.acpResultado.substring(0,199).replace("-", " <br/> ").concat("...");
            } else {
                return this.acpResultado.replace("-", " <br/> ");
            }
        }
        return null;
    }

 

    public Long getAcpPk() {
        return acpPk;
    }

    public void setAcpPk(Long acpPk) {
        this.acpPk = acpPk;
    }

    public SgArchivo getAcpArchivo() {
        return acpArchivo;
    }

    public void setAcpArchivo(SgArchivo acpArchivo) {
        this.acpArchivo = acpArchivo;
    }

    public EnumEstadoArchivoCalificacionPAES getAcpEstadoArchivo() {
        return acpEstadoArchivo;
    }

    public void setAcpEstadoArchivo(EnumEstadoArchivoCalificacionPAES acpEstadoArchivo) {
        this.acpEstadoArchivo = acpEstadoArchivo;
    }

    public String getAcpResultado() {
        return acpResultado;
    }

    public void setAcpResultado(String acpResultado) {
        this.acpResultado = acpResultado;
    }

    public LocalDateTime getAcpUltModFecha() {
        return acpUltModFecha;
    }

    public void setAcpUltModFecha(LocalDateTime acpUltModFecha) {
        this.acpUltModFecha = acpUltModFecha;
    }

    public String getAcpUltModUsuario() {
        return acpUltModUsuario;
    }

    public void setAcpUltModUsuario(String acpUltModUsuario) {
        this.acpUltModUsuario = acpUltModUsuario;
    }

    public Integer getAcpVersion() {
        return acpVersion;
    }

    public void setAcpVersion(Integer acpVersion) {
        this.acpVersion = acpVersion;
    }

    public Integer getAcpAnio() {
        return acpAnio;
    }

    public void setAcpAnio(Integer acpAnio) {
        this.acpAnio = acpAnio;
    }
    
    
    //</editor-fold>
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (acpPk != null ? acpPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgArchivoCalificacionPAES)) {
            return false;
        }
        SgArchivoCalificacionPAES other = (SgArchivoCalificacionPAES) object;
        if ((this.acpPk == null && other.acpPk != null) || (this.acpPk != null && !this.acpPk.equals(other.acpPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgArchivoCalificacionPAES[ acpPk=" + acpPk + " ]";
    }
    
}
