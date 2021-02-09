/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.centros_educativos;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoParentesco;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "famPk", scope = SgFamiliar.class)
public class SgFamiliar implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long famPk;
    
    private Boolean famHabilitado;
    
    private Boolean famReferente;
    
    private LocalDateTime famUltModFecha;
    
    private String famUltModUsuario;
    
    private Integer famVersion;
    
    private SgEstudiante famEstudiante;
    
    private SgPersona famPersona;

    private SgTipoParentesco famTipoParentesco;


    public SgFamiliar() {
        this.famPersona = new SgPersona();
        this.famReferente=Boolean.FALSE;
    }
    
    public Long getFamPk() {
        return famPk;
    }

    public void setFamPk(Long famPk) {
        this.famPk = famPk;
    }

    public Boolean getFamHabilitado() {
        return famHabilitado;
    }

    public void setFamHabilitado(Boolean famHabilitado) {
        this.famHabilitado = famHabilitado;
    }

    public Boolean getFamReferente() {
        return famReferente;
    }

    public void setFamReferente(Boolean famReferente) {
        this.famReferente = famReferente;
    }

    public LocalDateTime getFamUltModFecha() {
        return famUltModFecha;
    }

    public void setFamUltModFecha(LocalDateTime famUltModFecha) {
        this.famUltModFecha = famUltModFecha;
    }

    public String getFamUltModUsuario() {
        return famUltModUsuario;
    }

    public void setFamUltModUsuario(String famUltModUsuario) {
        this.famUltModUsuario = famUltModUsuario;
    }

    public Integer getFamVersion() {
        return famVersion;
    }

    public void setFamVersion(Integer famVersion) {
        this.famVersion = famVersion;
    }

    public SgEstudiante getFamEstudiante() {
        return famEstudiante;
    }

    public void setFamEstudiante(SgEstudiante famEstudiante) {
        this.famEstudiante = famEstudiante;
    }

    public SgPersona getFamPersona() {
        return famPersona;
    }

    public void setFamPersona(SgPersona famPersona) {
        this.famPersona = famPersona;
    }

    public SgTipoParentesco getFamTipoParentesco() {
        return famTipoParentesco;
    }

    public void setFamTipoParentesco(SgTipoParentesco famTipoParentesco) {
        this.famTipoParentesco = famTipoParentesco;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (famPk != null ? famPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgFamiliar)) {
            return false;
        }
        SgFamiliar other = (SgFamiliar) object;
        if ((this.famPk == null && other.famPk != null) || (this.famPk != null && !this.famPk.equals(other.famPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgFamiliar[ famPk=" + famPk + " ]";
    }
    
}
