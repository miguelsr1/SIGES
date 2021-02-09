/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "megPk", scope = SgMotivoEgreso.class)
public class SgMotivoEgreso implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long megPk;

    private String megCodigo;

    private String megNombre;

    private Boolean megHabilitado;

    private LocalDateTime megUltModFecha;

    private String megUltModUsuario;

    private Integer megVersion;

    public SgMotivoEgreso() {
        this.megHabilitado = Boolean.TRUE;
    }

    public Long getMegPk() {
        return megPk;
    }

    public void setMegPk(Long megPk) {
        this.megPk = megPk;
    }

    public String getMegCodigo() {
        return megCodigo;
    }

    public void setMegCodigo(String megCodigo) {
        this.megCodigo = megCodigo;
    }

    public String getMegNombre() {
        return megNombre;
    }

    public void setMegNombre(String megNombre) {
        this.megNombre = megNombre;
    }

    public LocalDateTime getMegUltModFecha() {
        return megUltModFecha;
    }

    public void setMegUltModFecha(LocalDateTime megUltModFecha) {
        this.megUltModFecha = megUltModFecha;
    }

    public String getMegUltModUsuario() {
        return megUltModUsuario;
    }

    public void setMegUltModUsuario(String megUltModUsuario) {
        this.megUltModUsuario = megUltModUsuario;
    }

    public Integer getMegVersion() {
        return megVersion;
    }

    public void setMegVersion(Integer megVersion) {
        this.megVersion = megVersion;
    }

    public Boolean getMegHabilitado() {
        return megHabilitado;
    }

    public void setMegHabilitado(Boolean megHabilitado) {
        this.megHabilitado = megHabilitado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (megPk != null ? megPk.hashCode() : 0);
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
        final SgMotivoEgreso other = (SgMotivoEgreso) obj;
        if (!Objects.equals(this.megPk, other.megPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgMotivoEgreso[ megPk=" + megPk + " ]";
    }

}
