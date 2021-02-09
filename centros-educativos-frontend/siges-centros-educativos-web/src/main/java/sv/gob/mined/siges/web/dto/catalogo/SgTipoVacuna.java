/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "tvcPk", scope = SgTipoVacuna.class)
public class SgTipoVacuna implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long tvcPk;

    private String tvcCodigo;

    private String tvcNombre;

    private Boolean tvcHabilitado;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime tvcUltModFecha;

    private String tvcUltModUsuario;

    private Integer tvcVersion;

    public SgTipoVacuna() {
        this.tvcHabilitado = Boolean.TRUE;
    }

    public Long getTvcPk() {
        return tvcPk;
    }

    public void setTvcPk(Long tvcPk) {
        this.tvcPk = tvcPk;
    }

    public String getTvcCodigo() {
        return tvcCodigo;
    }

    public void setTvcCodigo(String tvcCodigo) {
        this.tvcCodigo = tvcCodigo;
    }

    public String getTvcNombre() {
        return tvcNombre;
    }

    public void setTvcNombre(String tvcNombre) {
        this.tvcNombre = tvcNombre;
    }

    public LocalDateTime getTvcUltModFecha() {
        return tvcUltModFecha;
    }

    public void setTvcUltModFecha(LocalDateTime tvcUltModFecha) {
        this.tvcUltModFecha = tvcUltModFecha;
    }

    public String getTvcUltModUsuario() {
        return tvcUltModUsuario;
    }

    public void setTvcUltModUsuario(String tvcUltModUsuario) {
        this.tvcUltModUsuario = tvcUltModUsuario;
    }

    public Integer getTvcVersion() {
        return tvcVersion;
    }

    public void setTvcVersion(Integer tvcVersion) {
        this.tvcVersion = tvcVersion;
    }

    public Boolean getTvcHabilitado() {
        return tvcHabilitado;
    }

    public void setTvcHabilitado(Boolean tvcHabilitado) {
        this.tvcHabilitado = tvcHabilitado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tvcPk != null ? tvcPk.hashCode() : 0);
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
        final SgTipoVacuna other = (SgTipoVacuna) obj;
        if (!Objects.equals(this.tvcPk, other.tvcPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgTipoVacuna[ tvcPk=" + tvcPk + " ]";
    }

}
