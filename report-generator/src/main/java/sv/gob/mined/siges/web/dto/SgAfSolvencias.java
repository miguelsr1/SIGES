/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "solPk", scope = SgAfSolvencias.class)
public class SgAfSolvencias implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long solPk;
    private SgSede solSedeFk;
    private SgAfUnidadesAdministrativas solUnidadAdministrativaFk;
    private LocalDate solFechaSolvencia;
    private Integer solAnio;
    private LocalDateTime solUltModFecha;
    private String solUltModUsuario;
    private Integer solVersion;

    public SgAfSolvencias() {
    }

    public Long getSolPk() {
        return solPk;
    }

    public void setSolPk(Long solPk) {
        this.solPk = solPk;
    }

    public SgSede getSolSedeFk() {
        return solSedeFk;
    }

    public void setSolSedeFk(SgSede solSedeFk) {
        this.solSedeFk = solSedeFk;
    }

    public SgAfUnidadesAdministrativas getSolUnidadAdministrativaFk() {
        return solUnidadAdministrativaFk;
    }

    public void setSolUnidadAdministrativaFk(SgAfUnidadesAdministrativas solUnidadAdministrativaFk) {
        this.solUnidadAdministrativaFk = solUnidadAdministrativaFk;
    }

    public LocalDate getSolFechaSolvencia() {
        return solFechaSolvencia;
    }

    public void setSolFechaSolvencia(LocalDate solFechaSolvencia) {
        this.solFechaSolvencia = solFechaSolvencia;
    }

    public Integer getSolAnio() {
        return solAnio;
    }

    public void setSolAnio(Integer solAnio) {
        this.solAnio = solAnio;
    }

    public LocalDateTime getSolUltModFecha() {
        return solUltModFecha;
    }

    public void setSolUltModFecha(LocalDateTime solUltModFecha) {
        this.solUltModFecha = solUltModFecha;
    }

    public String getSolUltModUsuario() {
        return solUltModUsuario;
    }

    public void setSolUltModUsuario(String solUltModUsuario) {
        this.solUltModUsuario = solUltModUsuario;
    }

    public Integer getSolVersion() {
        return solVersion;
    }

    public void setSolVersion(Integer solVersion) {
        this.solVersion = solVersion;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (solPk != null ? solPk.hashCode() : 0);
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
        final SgAfSolvencias other = (SgAfSolvencias) obj;
        if (!Objects.equals(this.solPk, other.solPk)) {
            return false;
        }
        return true;
    }

   

    @Override
    public String toString() {
        return "sv.gob.mined.siges.web.dto.SgAfSolvencias[ solId=" + solPk + " ]";
    }
    
}
