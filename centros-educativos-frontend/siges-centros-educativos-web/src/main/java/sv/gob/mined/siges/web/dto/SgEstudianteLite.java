/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "estPk", scope = SgEstudianteLite.class)
public class SgEstudianteLite implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long estPk;

    private Boolean estHabilitado;

    private LocalDateTime estUltModFecha;

    private String estUltModUsuario;

    private Integer estVersion;

    private SgPersonaLite estPersona;

    private SgMatricula estUltimaMatricula;

    private Long estUltimaSedePk;

    private Long estUltimaSeccionPk;

    private List<SgMatricula> estMatriculas;

    public SgEstudianteLite() {
    }


    public SgEstudianteLite(Long estPk) {
        this.estPk = estPk;
    }

    public Long getEstPk() {
        return estPk;
    }

    public void setEstPk(Long estPk) {
        this.estPk = estPk;
    }

    public Boolean getEstHabilitado() {
        return estHabilitado;
    }

    public void setEstHabilitado(Boolean estHabilitado) {
        this.estHabilitado = estHabilitado;
    }

    public LocalDateTime getEstUltModFecha() {
        return estUltModFecha;
    }

    public void setEstUltModFecha(LocalDateTime estUltModFecha) {
        this.estUltModFecha = estUltModFecha;
    }

    public String getEstUltModUsuario() {
        return estUltModUsuario;
    }

    public void setEstUltModUsuario(String estUltModUsuario) {
        this.estUltModUsuario = estUltModUsuario;
    }

    public Integer getEstVersion() {
        return estVersion;
    }

    public void setEstVersion(Integer estVersion) {
        this.estVersion = estVersion;
    }

    public SgMatricula getEstUltimaMatricula() {
        return estUltimaMatricula;
    }

    public void setEstUltimaMatricula(SgMatricula estUltimaMatricula) {
        this.estUltimaMatricula = estUltimaMatricula;
    }

    public Long getEstUltimaSedePk() {
        return estUltimaSedePk;
    }

    public void setEstUltimaSedePk(Long estUltimaSedePk) {
        this.estUltimaSedePk = estUltimaSedePk;
    }

    public Long getEstUltimaSeccionPk() {
        return estUltimaSeccionPk;
    }

    public void setEstUltimaSeccionPk(Long estUltimaSeccionPk) {
        this.estUltimaSeccionPk = estUltimaSeccionPk;
    }

    public SgPersonaLite getEstPersona() {
        return estPersona;
    }

    public void setEstPersona(SgPersonaLite estPersona) {
        this.estPersona = estPersona;
    }

    public List<SgMatricula> getEstMatriculas() {
        return estMatriculas;
    }

    public void setEstMatriculas(List<SgMatricula> estMatriculas) {
        this.estMatriculas = estMatriculas;
    }
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (estPk != null ? estPk.hashCode() : 0);
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
        final SgEstudianteLite other = (SgEstudianteLite) obj;
        if (!Objects.equals(this.estPk, other.estPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgEstudiante[ estPk=" + estPk + " ]";
    }

}
