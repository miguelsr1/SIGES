/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.centros_educativos;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoParentesco;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "allPk", scope = SgAllegado.class)
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
    
    ///ViewUtils
    private Boolean allSoloLectura;

    public SgAllegado() {
        this.allSoloLectura = Boolean.FALSE;
        this.allPersona = new SgPersona();
        this.allReferente = Boolean.FALSE;
        this.allContactoEmergencia = Boolean.FALSE;
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
