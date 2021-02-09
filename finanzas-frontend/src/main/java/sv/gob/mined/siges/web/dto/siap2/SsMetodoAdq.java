/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.siap2;

//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 * @author Sofis Solutions
 */
//@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "metId", scope = SsMetodoAdq.class)
public class SsMetodoAdq implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long metId;

    private String metNombre;

    private Boolean metHabilitado;

    private LocalDateTime metUltMod;

    private String metUltUsuario;

    private Integer meVersion;

    private List<SsMetodoAdq> metodosNombre;

    public SsMetodoAdq() {

    }

    @JsonIgnore
    public String getNomMetodoAdqTooltip() {
        if (this.metodosNombre != null) {
            return this.metodosNombre.stream()
                    .map(d -> d.getMetNombre())
                    .collect(Collectors.joining(", "));
        }
        return null;
    }

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Long getMetId() {
        return metId;
    }

    public List<SsMetodoAdq> getMetodosNombre() {
        return metodosNombre;
    }

    public void setMetodosNombre(List<SsMetodoAdq> metodosNombre) {
        this.metodosNombre = metodosNombre;
    }

    public void setMetId(Long metId) {
        this.metId = metId;
    }

    public String getMetNombre() {
        return metNombre;
    }

    public void setMetNombre(String metNombre) {
        this.metNombre = metNombre;
    }

    public Boolean getMetHabilitado() {
        return metHabilitado;
    }

    public void setMetHabilitado(Boolean metHabilitado) {
        this.metHabilitado = metHabilitado;
    }

    public LocalDateTime getMetUltMod() {
        return metUltMod;
    }

    public void setMetUltMod(LocalDateTime metUltMod) {
        this.metUltMod = metUltMod;
    }

    public String getMetUltUsuario() {
        return metUltUsuario;
    }

    public void setMetUltUsuario(String metUltUsuario) {
        this.metUltUsuario = metUltUsuario;
    }

    public Integer getMeVersion() {
        return meVersion;
    }

    public void setMeVersion(Integer meVersion) {
        this.meVersion = meVersion;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.metId);
        hash = 37 * hash + Objects.hashCode(this.metNombre);
        hash = 37 * hash + Objects.hashCode(this.metHabilitado);
        hash = 37 * hash + Objects.hashCode(this.metUltMod);
        hash = 37 * hash + Objects.hashCode(this.metUltUsuario);
        hash = 37 * hash + Objects.hashCode(this.meVersion);
        return hash;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="hash-equals">
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
        final SsMetodoAdq other = (SsMetodoAdq) obj;
        if (!Objects.equals(this.metId, other.metId)) {
            return false;
        }
        return true;
    }// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="nombre-metodo">

    @Override
    public String toString() {
        return "com.sofis.entidades.SsMetodoAdq[ metId=" + metId + " ]";
    }
// </editor-fold>
}
