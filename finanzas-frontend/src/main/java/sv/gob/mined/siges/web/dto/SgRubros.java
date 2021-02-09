/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
//@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ruId", scope = SgRubros.class)
public class SgRubros implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long ruId;

    private String ruNombre;

    private String ruCodigo;

    private Boolean ruHabilitado;

    private LocalDateTime ruUltmodFecha;

    private String ruUltmodUsuario;

    private Integer ruVersion;

    public Long getRuId() {
        return ruId;
    }

    public void setRuId(Long ruId) {
        this.ruId = ruId;
    }

    public String getRuNombre() {
        return ruNombre;
    }

    public void setRuNombre(String ruNombre) {
        this.ruNombre = ruNombre;
    }

    public String getRuCodigo() {
        return ruCodigo;
    }

    public void setRuCodigo(String ruCodigo) {
        this.ruCodigo = ruCodigo;
    }

    public Boolean getRuHabilitado() {
        return ruHabilitado;
    }

    public void setRuHabilitado(Boolean ruHabilitado) {
        this.ruHabilitado = ruHabilitado;
    }

    public LocalDateTime getRuUltmodFecha() {
        return ruUltmodFecha;
    }

    public void setRuUltmodFecha(LocalDateTime ruUltmodFecha) {
        this.ruUltmodFecha = ruUltmodFecha;
    }

    public String getRuUltmodUsuario() {
        return ruUltmodUsuario;
    }

    public void setRuUltmodUsuario(String ruUltmodUsuario) {
        this.ruUltmodUsuario = ruUltmodUsuario;
    }

    public Integer getRuVersion() {
        return ruVersion;
    }

    public void setRuVersion(Integer ruVersion) {
        this.ruVersion = ruVersion;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.ruId);
        hash = 67 * hash + Objects.hashCode(this.ruNombre);
        hash = 67 * hash + Objects.hashCode(this.ruCodigo);
        hash = 67 * hash + Objects.hashCode(this.ruHabilitado);
        hash = 67 * hash + Objects.hashCode(this.ruUltmodFecha);
        hash = 67 * hash + Objects.hashCode(this.ruUltmodUsuario);
        hash = 67 * hash + Objects.hashCode(this.ruVersion);
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
        final SgRubros other = (SgRubros) obj;
        if (!Objects.equals(this.ruId, other.ruId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgRubros[ ruId=" + ruId + " ]";
    }

}
