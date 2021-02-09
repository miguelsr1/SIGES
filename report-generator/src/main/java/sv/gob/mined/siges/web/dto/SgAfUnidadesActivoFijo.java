/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "uafPk", scope = SgAfUnidadesActivoFijo.class)
public class SgAfUnidadesActivoFijo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long uafPk;
    private String uafCodigo;
    private String uafNombre;
    private String uafNombreBusqueda;
    private Boolean uafHabilitado;
    private SgDepartamento uafDepartamento;
    private LocalDateTime uafUltModFecha;
    private String uafUltModUsuario;
    private Integer uafVersion;
    private List<SgAfComisionDescargo> sgAfComisionDescargoList;
    private String uafResponsableAF;
    private String uafCargoResponsableAF;
    public SgAfUnidadesActivoFijo() {
        uafHabilitado = Boolean.TRUE;
        sgAfComisionDescargoList = new ArrayList();
    }

    public String getCodigoNombre() {
        return uafCodigo + " - " + uafNombre;
    }
    
    public Long getUafPk() {
        return uafPk;
    }

    public void setUafPk(Long uafPk) {
        this.uafPk = uafPk;
    }

    public String getUafCodigo() {
        return uafCodigo;
    }

    public void setUafCodigo(String uafCodigo) {
        this.uafCodigo = uafCodigo;
    }

    public String getUafNombre() {
        return uafNombre;
    }

    public void setUafNombre(String uafNombre) {
        this.uafNombre = uafNombre;
    }

    public String getUafNombreBusqueda() {
        return uafNombreBusqueda;
    }

    public void setUafNombreBusqueda(String uafNombreBusqueda) {
        this.uafNombreBusqueda = uafNombreBusqueda;
    }

    public Boolean getUafHabilitado() {
        return uafHabilitado;
    }

    public void setUafHabilitado(Boolean uafHabilitado) {
        this.uafHabilitado = uafHabilitado;
    }

    public SgDepartamento getUafDepartamento() {
        return uafDepartamento;
    }

    public void setUafDepartamento(SgDepartamento uafDepartamento) {
        this.uafDepartamento = uafDepartamento;
    }

    public LocalDateTime getUafUltModFecha() {
        return uafUltModFecha;
    }

    public void setUafUltModFecha(LocalDateTime uafUltModFecha) {
        this.uafUltModFecha = uafUltModFecha;
    }

    public String getUafUltModUsuario() {
        return uafUltModUsuario;
    }

    public void setUafUltModUsuario(String uafUltModUsuario) {
        this.uafUltModUsuario = uafUltModUsuario;
    }

    public Integer getUafVersion() {
        return uafVersion;
    }

    public void setUafVersion(Integer uafVersion) {
        this.uafVersion = uafVersion;
    }

    public List<SgAfComisionDescargo> getSgAfComisionDescargoList() {
        return sgAfComisionDescargoList;
    }

    public void setSgAfComisionDescargoList(List<SgAfComisionDescargo> sgAfComisionDescargoList) {
        this.sgAfComisionDescargoList = sgAfComisionDescargoList;
    }

    public String getUafResponsableAF() {
        return uafResponsableAF;
    }

    public void setUafResponsableAF(String uafResponsableAF) {
        this.uafResponsableAF = uafResponsableAF;
    }

    public String getUafCargoResponsableAF() {
        return uafCargoResponsableAF;
    }

    public void setUafCargoResponsableAF(String uafCargoResponsableAF) {
        this.uafCargoResponsableAF = uafCargoResponsableAF;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.uafPk);
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
        final SgAfUnidadesActivoFijo other = (SgAfUnidadesActivoFijo) obj;
        if (!Objects.equals(this.uafPk, other.uafPk)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "sv.gob.mined.siges.web.dto.SgAfUnidadesActivoFijo{" + "uafPk=" + uafPk + '}';
    }
}