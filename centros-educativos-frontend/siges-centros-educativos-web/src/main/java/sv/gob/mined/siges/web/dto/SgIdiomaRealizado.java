/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.time.LocalDateTime;
import sv.gob.mined.siges.web.dto.catalogo.SgIdioma;
import sv.gob.mined.siges.web.dto.catalogo.SgNivelIdioma;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "irePk", scope = SgIdiomaRealizado.class)
public class SgIdiomaRealizado implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long irePk;

    private SgEstudioRealizado ireEstudiosRealizados;

    private SgIdioma ireIdioma;

    private SgNivelIdioma ireNivelIdioma;

    private String ireObservaciones;
    
    private Boolean ireValidado;

    private LocalDateTime ireUltModFecha;

    private String ireUltModUsuario;

    private Integer ireVersion;

    public SgIdiomaRealizado() {
        this.ireValidado = Boolean.FALSE;
    }

    public SgIdiomaRealizado(Long irePk) {
        this.irePk = irePk;
    }

    public Long getIrePk() {
        return irePk;
    }

    public void setIrePk(Long irePk) {
        this.irePk = irePk;
    }

    public SgEstudioRealizado getIreEstudiosRealizados() {
        return ireEstudiosRealizados;
    }

    public void setIreEstudiosRealizados(SgEstudioRealizado ireEstudiosRealizados) {
        this.ireEstudiosRealizados = ireEstudiosRealizados;
    }

    public SgIdioma getIreIdioma() {
        return ireIdioma;
    }

    public void setIreIdioma(SgIdioma ireIdioma) {
        this.ireIdioma = ireIdioma;
    }

    public SgNivelIdioma getIreNivelIdioma() {
        return ireNivelIdioma;
    }

    public void setIreNivelIdioma(SgNivelIdioma ireNivelIdioma) {
        this.ireNivelIdioma = ireNivelIdioma;
    }

    public String getIreObservaciones() {
        return ireObservaciones;
    }

    public void setIreObservaciones(String ireObservaciones) {
        this.ireObservaciones = ireObservaciones;
    }

    public LocalDateTime getIreUltModFecha() {
        return ireUltModFecha;
    }

    public void setIreUltModFecha(LocalDateTime ireUltModFecha) {
        this.ireUltModFecha = ireUltModFecha;
    }

    public String getIreUltModUsuario() {
        return ireUltModUsuario;
    }

    public void setIreUltModUsuario(String ireUltModUsuario) {
        this.ireUltModUsuario = ireUltModUsuario;
    }

    public Integer getIreVersion() {
        return ireVersion;
    }

    public void setIreVersion(Integer ireVersion) {
        this.ireVersion = ireVersion;
    }

    public Boolean getIreValidado() {
        return ireValidado;
    }

    public void setIreValidado(Boolean ireValidado) {
        this.ireValidado = ireValidado;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (irePk != null ? irePk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgIdiomaRealizado)) {
            return false;
        }
        SgIdiomaRealizado other = (SgIdiomaRealizado) object;
        if ((this.irePk == null && other.irePk != null) || (this.irePk != null && !this.irePk.equals(other.irePk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgIdiomaRealizado[ irePk=" + irePk + " ]";
    }

}
