/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.siap2;

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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "adiPk", scope = SgAreaInversion.class)
public class SgAreaInversion implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long adiPk;

    private String adiCodigo;

    private String adiNombre;

    private Boolean adiHabilitado;

    private LocalDateTime adiUltModFecha;

    private String adiUltModUsuario;

    private Integer adiVersion;

    private SgAreaInversion adiPadrePk;

    private Boolean adiAplicaPlanCompras;

    public Long getAdiPk() {
        return adiPk;
    }

    public String getAdiCodigo() {
        return adiCodigo;
    }

    public String getAdiNombre() {
        return adiNombre;
    }

    public Boolean getAdiHabilitado() {
        return adiHabilitado;
    }

    public LocalDateTime getAdiUltModFecha() {
        return adiUltModFecha;
    }

    public String getAdiUltModUsuario() {
        return adiUltModUsuario;
    }

    public Integer getAdiVersion() {
        return adiVersion;
    }

    public void setAdiPk(Long adiPk) {
        this.adiPk = adiPk;
    }

    public void setAdiCodigo(String adiCodigo) {
        this.adiCodigo = adiCodigo;
    }

    public void setAdiNombre(String adiNombre) {
        this.adiNombre = adiNombre;
    }

    public void setAdiHabilitado(Boolean adiHabilitado) {
        this.adiHabilitado = adiHabilitado;
    }

    public void setAdiUltModFecha(LocalDateTime adiUltModFecha) {
        this.adiUltModFecha = adiUltModFecha;
    }

    public void setAdiUltModUsuario(String adiUltModUsuario) {
        this.adiUltModUsuario = adiUltModUsuario;
    }

    public void setAdiVersion(Integer adiVersion) {
        this.adiVersion = adiVersion;
    }

    public SgAreaInversion getAdiPadrePk() {
        return adiPadrePk;
    }

    public void setAdiPadrePk(SgAreaInversion adiPadrePk) {
        this.adiPadrePk = adiPadrePk;
    }

    public Boolean getAdiAplicaPlanCompras() {
        return adiAplicaPlanCompras;
    }

    public void setAdiAplicaPlanCompras(Boolean adiAplicaPlanCompras) {
        this.adiAplicaPlanCompras = adiAplicaPlanCompras;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgAreaInversion[ adiPk=" + adiPk + " ]";
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.adiPk);
        hash = 97 * hash + Objects.hashCode(this.adiCodigo);
        hash = 97 * hash + Objects.hashCode(this.adiNombre);
        hash = 97 * hash + Objects.hashCode(this.adiHabilitado);
        hash = 97 * hash + Objects.hashCode(this.adiUltModFecha);
        hash = 97 * hash + Objects.hashCode(this.adiUltModUsuario);
        hash = 97 * hash + Objects.hashCode(this.adiVersion);
        hash = 97 * hash + Objects.hashCode(this.adiPadrePk);
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
        final SgAreaInversion other = (SgAreaInversion) obj;
        if (!Objects.equals(this.adiCodigo, other.adiCodigo)) {
            return false;
        }
        if (!Objects.equals(this.adiNombre, other.adiNombre)) {
            return false;
        }
        if (!Objects.equals(this.adiUltModUsuario, other.adiUltModUsuario)) {
            return false;
        }
        if (!Objects.equals(this.adiPk, other.adiPk)) {
            return false;
        }
        if (!Objects.equals(this.adiHabilitado, other.adiHabilitado)) {
            return false;
        }
        if (!Objects.equals(this.adiUltModFecha, other.adiUltModFecha)) {
            return false;
        }
        if (!Objects.equals(this.adiVersion, other.adiVersion)) {
            return false;
        }
        if (!Objects.equals(this.adiPadrePk, other.adiPadrePk)) {
            return false;
        }
        return true;
    }

}
