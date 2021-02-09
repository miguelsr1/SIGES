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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "casPk", scope = SgCaserio.class)
public class SgCaserio implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long casPk;

    private String casCodigo;

    private String casNombre;

    private SgCanton casCanton;

    private Boolean casHabilitado;

    private LocalDateTime casUltModFecha;

    private String casUltModUsuario;

    private Integer casVersion;

    public SgCaserio() {
        this.casHabilitado = Boolean.TRUE;
    }

    public Long getCasPk() {
        return casPk;
    }

    public void setCasPk(Long casPk) {
        this.casPk = casPk;
    }

    public String getCasCodigo() {
        return casCodigo;
    }

    public void setCasCodigo(String casCodigo) {
        this.casCodigo = casCodigo;
    }

    public String getCasNombre() {
        return casNombre;
    }

    public void setCasNombre(String casNombre) {
        this.casNombre = casNombre;
    }

    public LocalDateTime getCasUltModFecha() {
        return casUltModFecha;
    }

    public void setCasUltModFecha(LocalDateTime casUltModFecha) {
        this.casUltModFecha = casUltModFecha;
    }

    public String getCasUltModUsuario() {
        return casUltModUsuario;
    }

    public void setCasUltModUsuario(String casUltModUsuario) {
        this.casUltModUsuario = casUltModUsuario;
    }

    public Integer getCasVersion() {
        return casVersion;
    }

    public void setCasVersion(Integer casVersion) {
        this.casVersion = casVersion;
    }

    public Boolean getCasHabilitado() {
        return casHabilitado;
    }

    public void setCasHabilitado(Boolean casHabilitado) {
        this.casHabilitado = casHabilitado;
    }

    public SgCanton getCasCanton() {
        return casCanton;
    }

    public void setCasCanton(SgCanton casCanton) {
        this.casCanton = casCanton;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (casPk != null ? casPk.hashCode() : 0);
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
        final SgCaserio other = (SgCaserio) obj;
        if (!Objects.equals(this.casPk, other.casPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgCaserio[ casPk=" + casPk + " ]";
    }

}
