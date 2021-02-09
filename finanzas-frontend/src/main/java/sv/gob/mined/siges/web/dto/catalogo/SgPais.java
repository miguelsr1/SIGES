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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "paiPk", scope = SgPais.class)
public class SgPais implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long paiPk;

    private String paiCodigo;

    private String paiNombre;

    private Boolean paiHabilitado;

    private LocalDateTime paiUltModFecha;

    private String paiUltModUsuario;

    private Integer paiVersion;

    public SgPais() {
        this.paiHabilitado = Boolean.TRUE;
    }

    public Long getPaiPk() {
        return paiPk;
    }

    public void setPaiPk(Long paiPk) {
        this.paiPk = paiPk;
    }

    public String getPaiCodigo() {
        return paiCodigo;
    }

    public void setPaiCodigo(String paiCodigo) {
        this.paiCodigo = paiCodigo;
    }

    public String getPaiNombre() {
        return paiNombre;
    }

    public void setPaiNombre(String paiNombre) {
        this.paiNombre = paiNombre;
    }

    public LocalDateTime getPaiUltModFecha() {
        return paiUltModFecha;
    }

    public void setPaiUltModFecha(LocalDateTime paiUltModFecha) {
        this.paiUltModFecha = paiUltModFecha;
    }

    public String getPaiUltModUsuario() {
        return paiUltModUsuario;
    }

    public void setPaiUltModUsuario(String paiUltModUsuario) {
        this.paiUltModUsuario = paiUltModUsuario;
    }

    public Integer getPaiVersion() {
        return paiVersion;
    }

    public void setPaiVersion(Integer paiVersion) {
        this.paiVersion = paiVersion;
    }

    public Boolean getPaiHabilitado() {
        return paiHabilitado;
    }

    public void setPaiHabilitado(Boolean paiHabilitado) {
        this.paiHabilitado = paiHabilitado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (paiPk != null ? paiPk.hashCode() : 0);
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
        final SgPais other = (SgPais) obj;
        if (!Objects.equals(this.paiPk, other.paiPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgPais[ paiPk=" + paiPk + " ]";
    }

}
