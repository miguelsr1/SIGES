/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "cdePk", scope = SgAfComisionDescargo.class)
public class SgAfComisionDescargo implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long cdePk;
    private String cdeNombreRepresentante;
    private String cdeCargoRepresentante;
    private LocalDateTime cdeUltModFecha;
    private String cdeUltModUsuario;
    private Integer cdeVersion;
    private Boolean cdeHabilitado;
    private SgAfUnidadesActivoFijo cdeActivoFijoFk;

    public SgAfComisionDescargo() {
    }

    public Long getCdePk() {
        return cdePk;
    }

    public void setCdePk(Long cdePk) {
        this.cdePk = cdePk;
    }

    public String getCdeNombreRepresentante() {
        return cdeNombreRepresentante;
    }

    public void setCdeNombreRepresentante(String cdeNombreRepresentante) {
        this.cdeNombreRepresentante = cdeNombreRepresentante;
    }

    public String getCdeCargoRepresentante() {
        return cdeCargoRepresentante;
    }

    public void setCdeCargoRepresentante(String cdeCargoRepresentante) {
        this.cdeCargoRepresentante = cdeCargoRepresentante;
    }

    public LocalDateTime getCdeUltModFecha() {
        return cdeUltModFecha;
    }

    public void setCdeUltModFecha(LocalDateTime cdeUltModFecha) {
        this.cdeUltModFecha = cdeUltModFecha;
    }

    public String getCdeUltModUsuario() {
        return cdeUltModUsuario;
    }

    public void setCdeUltModUsuario(String cdeUltModUsuario) {
        this.cdeUltModUsuario = cdeUltModUsuario;
    }

    public Integer getCdeVersion() {
        return cdeVersion;
    }

    public void setCdeVersion(Integer cdeVersion) {
        this.cdeVersion = cdeVersion;
    }

    public SgAfUnidadesActivoFijo getCdeActivoFijoFk() {
        return cdeActivoFijoFk;
    }

    public void setCdeActivoFijoFk(SgAfUnidadesActivoFijo cdeActivoFijoFk) {
        this.cdeActivoFijoFk = cdeActivoFijoFk;
    }

    public Boolean getCdeHabilitado() {
        return cdeHabilitado;
    }

    public void setCdeHabilitado(Boolean cdeHabilitado) {
        this.cdeHabilitado = cdeHabilitado;
    }

    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cdePk != null ? cdePk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAfComisionDescargo)) {
            return false;
        }
        SgAfComisionDescargo other = (SgAfComisionDescargo) object;
        if ((this.cdePk == null && other.cdePk != null) || (this.cdePk != null && !this.cdePk.equals(other.cdePk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgAfComisionDescargo[ cdePk=" + cdePk + " ]";
    }
    
}
