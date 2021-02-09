/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;
import sv.gob.mined.siges.web.enumerados.EnumEstadoSolicitudCurso;

@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "scdPk", scope = SgServicioEducativo.class)
public class SgSolicitudCursoDocente implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long scdPk;
    
    private SgCursoDocente scdCurso;
    
    private SgPersonalSedeEducativa scdPersonal;
    
    private EnumEstadoSolicitudCurso scdEstado;
    
    private LocalDateTime scdUltModFecha;
    
    private String scdUltModUsuario;
    
    private Integer scdVersion;

    public SgSolicitudCursoDocente() {
    }

    public SgSolicitudCursoDocente(Long scdPk) {
        this.scdPk = scdPk;
    }

    public Long getScdPk() {
        return scdPk;
    }

    public void setScdPk(Long scdPk) {
        this.scdPk = scdPk;
    }

    public SgCursoDocente getScdCurso() {
        return scdCurso;
    }

    public void setScdCurso(SgCursoDocente scdCurso) {
        this.scdCurso = scdCurso;
    }

    public SgPersonalSedeEducativa getScdPersonal() {
        return scdPersonal;
    }

    public void setScdPersonal(SgPersonalSedeEducativa scdPersonal) {
        this.scdPersonal = scdPersonal;
    }

    public EnumEstadoSolicitudCurso getScdEstado() {
        return scdEstado;
    }

    public void setScdEstado(EnumEstadoSolicitudCurso scdEstado) {
        this.scdEstado = scdEstado;
    }

    public LocalDateTime getScdUltModFecha() {
        return scdUltModFecha;
    }

    public void setScdUltModFecha(LocalDateTime scdUltModFecha) {
        this.scdUltModFecha = scdUltModFecha;
    }

    public String getScdUltModUsuario() {
        return scdUltModUsuario;
    }

    public void setScdUltModUsuario(String scdUltModUsuario) {
        this.scdUltModUsuario = scdUltModUsuario;
    }

    public Integer getScdVersion() {
        return scdVersion;
    }

    public void setScdVersion(Integer scdVersion) {
        this.scdVersion = scdVersion;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (scdPk != null ? scdPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgSolicitudCursoDocente)) {
            return false;
        }
        SgSolicitudCursoDocente other = (SgSolicitudCursoDocente) object;
        if ((this.scdPk == null && other.scdPk != null) || (this.scdPk != null && !this.scdPk.equals(other.scdPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgSolicitudCursoDocente[ scdPk=" + scdPk + " ]";
    }
    
}
