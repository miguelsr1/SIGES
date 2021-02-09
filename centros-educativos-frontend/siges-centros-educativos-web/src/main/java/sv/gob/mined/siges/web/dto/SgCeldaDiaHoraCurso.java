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
import java.time.LocalTime;
import sv.gob.mined.siges.web.enumerados.EnumCeldaDiaHora;

/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "cdcPk", scope = SgCeldaDiaHoraCurso.class)
public class SgCeldaDiaHoraCurso implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long cdcPk;
    
    private SgCursoDocente cdcCurso;
    
    private EnumCeldaDiaHora cdcDia;
    
    private Integer cdcFila;
    
    private LocalTime cdcHora;
    
    private LocalDateTime cdcUltModFecha;
    
    private String cdcUltModUsuario;
    
    private Integer cdcVersion;

    public SgCeldaDiaHoraCurso() {
    }

    public SgCeldaDiaHoraCurso(Long cdcPk) {
        this.cdcPk = cdcPk;
    }

    public Long getCdcPk() {
        return cdcPk;
    }

    public void setCdcPk(Long cdcPk) {
        this.cdcPk = cdcPk;
    }

    public SgCursoDocente getCdcCurso() {
        return cdcCurso;
    }

    public void setCdcCurso(SgCursoDocente cdcCurso) {
        this.cdcCurso = cdcCurso;
    }

    public EnumCeldaDiaHora getCdcDia() {
        return cdcDia;
    }

    public void setCdcDia(EnumCeldaDiaHora cdcDia) {
        this.cdcDia = cdcDia;
    }

    public Integer getCdcFila() {
        return cdcFila;
    }

    public void setCdcFila(Integer cdcFila) {
        this.cdcFila = cdcFila;
    }

    public LocalTime getCdcHora() {
        return cdcHora;
    }

    public void setCdcHora(LocalTime cdcHora) {
        this.cdcHora = cdcHora;
    }

    public LocalDateTime getCdcUltModFecha() {
        return cdcUltModFecha;
    }

    public void setCdcUltModFecha(LocalDateTime cdcUltModFecha) {
        this.cdcUltModFecha = cdcUltModFecha;
    }

    public String getCdcUltModUsuario() {
        return cdcUltModUsuario;
    }

    public void setCdcUltModUsuario(String cdcUltModUsuario) {
        this.cdcUltModUsuario = cdcUltModUsuario;
    }

    public Integer getCdcVersion() {
        return cdcVersion;
    }

    public void setCdcVersion(Integer cdcVersion) {
        this.cdcVersion = cdcVersion;
    }

    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cdcPk != null ? cdcPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgCeldaDiaHoraCurso)) {
            return false;
        }
        SgCeldaDiaHoraCurso other = (SgCeldaDiaHoraCurso) object;
        if ((this.cdcPk == null && other.cdcPk != null) || (this.cdcPk != null && !this.cdcPk.equals(other.cdcPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgCeldaDiaHoraCurso[ cdcPk=" + cdcPk + " ]";
    }
    
}
