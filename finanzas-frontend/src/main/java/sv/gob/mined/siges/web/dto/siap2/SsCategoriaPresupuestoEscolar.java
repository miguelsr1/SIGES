/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.siap2;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "cpeId", scope = SsCategoriaPresupuestoEscolar.class)
public class SsCategoriaPresupuestoEscolar implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long cpeId;

    private String cpeCodigo;

    private String cpeNombre;

    private Boolean cpeHabilitado;

    private Integer cpeVersion;

    public SsCategoriaPresupuestoEscolar() {
    }

    // <editor-fold defaultstate="collapsed" desc="Getter-Setter">
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getCpeId() {
        return cpeId;
    }

    public String getCpeCodigo() {
        return cpeCodigo;
    }

    public String getCpeNombre() {
        return cpeNombre;
    }

    public Boolean getCpeHabilitado() {
        return cpeHabilitado;
    }

    public Integer getCpeVersion() {
        return cpeVersion;
    }

    public void setCpeId(Long cpeId) {
        this.cpeId = cpeId;
    }

    public void setCpeCodigo(String cpeCodigo) {
        this.cpeCodigo = cpeCodigo;
    }

    public void setCpeNombre(String cpeNombre) {
        this.cpeNombre = cpeNombre;
    }

    public void setCpeHabilitado(Boolean cpeHabilitado) {
        this.cpeHabilitado = cpeHabilitado;
    }

    public void setCpeVersion(Integer cpeVersion) {
        this.cpeVersion = cpeVersion;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Hash-Equals">
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cpeId != null ? cpeId.hashCode() : 0);
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
        final SsCategoriaPresupuestoEscolar other = (SsCategoriaPresupuestoEscolar) obj;
        if (!Objects.equals(this.cpeId, other.cpeId)) {
            return false;
        }
        return true;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="toString">
    @Override
    public String toString() {
        return "com.sofis.entidades.SsCategoriaPresupuestoEscolar[ cpeId=" + cpeId + " ]";
    }
    // </editor-fold>
}
