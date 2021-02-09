/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "estPk", scope = SgEstudiante.class)
public class SgEstudiante implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long estPk;

    private Double estDisKmCentro;

    private String estFacRiesgo;

    private Boolean estEmbarazo;

    private Boolean estHabilitado;

    private LocalDateTime estUltModFecha;

    private String estUltModUsuario;

    private Integer estVersion;

    private SgPersona estPersona;
    
    private String estNombreCompletoResponsable;

    public SgEstudiante() {
        this.estPersona = new SgPersona();
    }

    public SgEstudiante(Long estPk) {
        this.estPk = estPk;
    }

    public Long getEstPk() {
        return estPk;
    }

    public void setEstPk(Long estPk) {
        this.estPk = estPk;
    }

    public Double getEstDisKmCentro() {
        return estDisKmCentro;
    }

    public void setEstDisKmCentro(Double estDisKmCentro) {
        this.estDisKmCentro = estDisKmCentro;
    }

    public String getEstFacRiesgo() {
        return estFacRiesgo;
    }

    public void setEstFacRiesgo(String estFacRiesgo) {
        this.estFacRiesgo = estFacRiesgo;
    }

    public Boolean getEstEmbarazo() {
        return estEmbarazo;
    }

    public void setEstEmbarazo(Boolean estEmbarazo) {
        this.estEmbarazo = estEmbarazo;
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

    public SgPersona getEstPersona() {
        return estPersona;
    }

    public void setEstPersona(SgPersona estPersona) {
        this.estPersona = estPersona;
    }

    public String getEstNombreCompletoResponsable() {
        return estNombreCompletoResponsable;
    }

    public void setEstNombreCompletoResponsable(String estNombreCompletoResponsable) {
        this.estNombreCompletoResponsable = estNombreCompletoResponsable;
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
        final SgEstudiante other = (SgEstudiante) obj;
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
