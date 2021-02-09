/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ayuPk", scope = SgAyuda.class)
public class SgAyuda implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long ayuPk;

    private String ayuCodigo;

    private String ayuTextoUso;

    private String ayuTextoNormativa;

    private String ayuVinculos;

    private String ayuNombre;

    private Boolean ayuHabilitado;

    private LocalDateTime ayuUltModFecha;

    private String ayuUltModUsuario;

    private Integer ayuVersion;

    public SgAyuda() {
        this.ayuHabilitado = Boolean.TRUE;
    }

    public Long getAyuPk() {
        return ayuPk;
    }

    public void setAyuPk(Long ayuPk) {
        this.ayuPk = ayuPk;
    }

    public String getAyuCodigo() {
        return ayuCodigo;
    }

    public void setAyuCodigo(String ayuCodigo) {
        this.ayuCodigo = ayuCodigo;
    }

    public String getAyuNombre() {
        return ayuNombre;
    }

    public void setAyuNombre(String ayuNombre) {
        this.ayuNombre = ayuNombre;
    }

    public LocalDateTime getAyuUltModFecha() {
        return ayuUltModFecha;
    }

    public void setAyuUltModFecha(LocalDateTime ayuUltModFecha) {
        this.ayuUltModFecha = ayuUltModFecha;
    }

    public String getAyuUltModUsuario() {
        return ayuUltModUsuario;
    }

    public void setAyuUltModUsuario(String ayuUltModUsuario) {
        this.ayuUltModUsuario = ayuUltModUsuario;
    }

    public Integer getAyuVersion() {
        return ayuVersion;
    }

    public void setAyuVersion(Integer ayuVersion) {
        this.ayuVersion = ayuVersion;
    }

    public Boolean getAyuHabilitado() {
        return ayuHabilitado;
    }

    public void setAyuHabilitado(Boolean ayuHabilitado) {
        this.ayuHabilitado = ayuHabilitado;
    }

    public String getAyuTextoUso() {
        return ayuTextoUso;
    }

    public void setAyuTextoUso(String ayuTextoUso) {
        this.ayuTextoUso = ayuTextoUso;
    }

    public String getAyuTextoNormativa() {
        return ayuTextoNormativa;
    }

    public void setAyuTextoNormativa(String ayuTextoNormativa) {
        this.ayuTextoNormativa = ayuTextoNormativa;
    }

    public String getAyuVinculos() {
        return ayuVinculos;
    }

    public void setAyuVinculos(String ayuVinculos) {
        this.ayuVinculos = ayuVinculos;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ayuPk != null ? ayuPk.hashCode() : 0);
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
        final SgAyuda other = (SgAyuda) obj;
        if (!Objects.equals(this.ayuPk, other.ayuPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgAyuda[ ayuPk=" + ayuPk + " ]";
    }

}
