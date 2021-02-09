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
import java.time.LocalDateTime;
import java.util.Objects;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoParentesco;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "allPk", scope = SgAllegado.class)
public class SgAllegado implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long allPk;

    private Boolean allContactoEmergencia;

    private Boolean allReferente;

    private LocalDateTime allUltModFecha;

    private String allUltModUsuario;

    private Integer allVersion;

    private Boolean allEsFamiliar;

    private SgPersona allPersona;

    private SgTipoParentesco allTipoParentesco;

    private SgPersona allPersonaReferenciada;
    
    private Boolean allDependiente;
    
    private Boolean allViveConAllegado;
    
    ///ViewUtils
    @JsonIgnore
    private Boolean allSoloLectura;
    
    @JsonIgnore
    private Boolean allTieneRolPadreEnEstudiante;

    public SgAllegado() {
        this.allTieneRolPadreEnEstudiante = Boolean.FALSE;
        this.allSoloLectura = Boolean.FALSE;
        this.allPersona = new SgPersona();
        this.allReferente = Boolean.FALSE;
        this.allContactoEmergencia = Boolean.FALSE;
        this.allViveConAllegado = Boolean.TRUE;
    }

    public Long getAllPk() {
        return allPk;
    }

    public void setAllPk(Long allPk) {
        this.allPk = allPk;
    }

    public Boolean getAllReferente() {
        return allReferente;
    }

    public void setAllReferente(Boolean allReferente) {
        this.allReferente = allReferente;
    }

    public Boolean getAllContactoEmergencia() {
        return allContactoEmergencia;
    }

    public void setAllContactoEmergencia(Boolean allContactoEmergencia) {
        this.allContactoEmergencia = allContactoEmergencia;
    }

    public LocalDateTime getAllUltModFecha() {
        return allUltModFecha;
    }

    public void setAllUltModFecha(LocalDateTime allUltModFecha) {
        this.allUltModFecha = allUltModFecha;
    }

    public String getAllUltModUsuario() {
        return allUltModUsuario;
    }

    public void setAllUltModUsuario(String allUltModUsuario) {
        this.allUltModUsuario = allUltModUsuario;
    }

    public Integer getAllVersion() {
        return allVersion;
    }

    public void setAllVersion(Integer allVersion) {
        this.allVersion = allVersion;
    }

    public SgPersona getAllPersonaReferenciada() {
        return allPersonaReferenciada;
    }

    public void setAllPersonaReferenciada(SgPersona allPersonaReferenciada) {
        this.allPersonaReferenciada = allPersonaReferenciada;
    }

    public SgPersona getAllPersona() {
        return allPersona;
    }

    public void setAllPersona(SgPersona allPersona) {
        this.allPersona = allPersona;
    }

    public SgTipoParentesco getAllTipoParentesco() {
        return allTipoParentesco;
    }

    public void setAllTipoParentesco(SgTipoParentesco allTipoParentesco) {
        this.allTipoParentesco = allTipoParentesco;
    }

    public Boolean getAllEsFamiliar() {
        return allEsFamiliar;
    }

    public void setAllEsFamiliar(Boolean allEsFamiliar) {
        this.allEsFamiliar = allEsFamiliar;
    }

    public Boolean getAllDependiente() {
        return allDependiente;
    }

    public void setAllDependiente(Boolean allDependiente) {
        this.allDependiente = allDependiente;
    }

    public Boolean getAllSoloLectura() {
        return allSoloLectura;
    }

    public void setAllSoloLectura(Boolean allSoloLectura) {
        this.allSoloLectura = allSoloLectura;
    }

    public Boolean getAllViveConAllegado() {
        return allViveConAllegado;
    }

    public void setAllViveConAllegado(Boolean allViveConAllegado) {
        this.allViveConAllegado = allViveConAllegado;
    }

    public Boolean getAllTieneRolPadreEnEstudiante() {
        return allTieneRolPadreEnEstudiante;
    }

    public void setAllTieneRolPadreEnEstudiante(Boolean allTieneRolPadreEnEstudiante) {
        this.allTieneRolPadreEnEstudiante = allTieneRolPadreEnEstudiante;
    }
      
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (allPk != null ? allPk.hashCode() : 0);
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
        final SgAllegado other = (SgAllegado) obj;
        if (!Objects.equals(this.allPk, other.allPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgAllegado[ allPk=" + allPk + " ]";
    }

}
