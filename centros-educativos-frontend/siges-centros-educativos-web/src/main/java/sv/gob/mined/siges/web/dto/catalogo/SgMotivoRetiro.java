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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "mrePk", scope = SgMotivoRetiro.class)
public class SgMotivoRetiro implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long mrePk;

    private String mreCodigo;

    private String mreNombre;

    private Boolean mreHabilitado;

    private LocalDateTime mreUltModFecha;

    private String mreUltModUsuario;

    private Integer mreVersion;

    private Boolean mreDefinitivo;

    private Boolean mreTraslado;

    private Boolean mreCambioSecc;

    public SgMotivoRetiro() {
        this.mreHabilitado = Boolean.TRUE;
    }

    public Long getMrePk() {
        return mrePk;
    }

    public void setMrePk(Long mrePk) {
        this.mrePk = mrePk;
    }

    public String getMreCodigo() {
        return mreCodigo;
    }

    public void setMreCodigo(String mreCodigo) {
        this.mreCodigo = mreCodigo;
    }

    public String getMreNombre() {
        return mreNombre;
    }

    public void setMreNombre(String mreNombre) {
        this.mreNombre = mreNombre;
    }

    public LocalDateTime getMreUltModFecha() {
        return mreUltModFecha;
    }

    public void setMreUltModFecha(LocalDateTime mreUltModFecha) {
        this.mreUltModFecha = mreUltModFecha;
    }

    public String getMreUltModUsuario() {
        return mreUltModUsuario;
    }

    public void setMreUltModUsuario(String mreUltModUsuario) {
        this.mreUltModUsuario = mreUltModUsuario;
    }

    public Integer getMreVersion() {
        return mreVersion;
    }

    public void setMreVersion(Integer mreVersion) {
        this.mreVersion = mreVersion;
    }

    public Boolean getMreHabilitado() {
        return mreHabilitado;
    }

    public void setMreHabilitado(Boolean mreHabilitado) {
        this.mreHabilitado = mreHabilitado;
    }

    public Boolean getMreDefinitivo() {
        return mreDefinitivo;
    }

    public void setMreDefinitivo(Boolean mreDefinitivo) {
        this.mreDefinitivo = mreDefinitivo;
    }

    public Boolean getMreTraslado() {
        return mreTraslado;
    }

    public void setMreTraslado(Boolean mreTraslado) {
        this.mreTraslado = mreTraslado;
    }

    public Boolean getMreCambioSecc() {
        return mreCambioSecc;
    }

    public void setMreCambioSecc(Boolean mreCambioSecc) {
        this.mreCambioSecc = mreCambioSecc;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mrePk != null ? mrePk.hashCode() : 0);
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
        final SgMotivoRetiro other = (SgMotivoRetiro) obj;
        if (!Objects.equals(this.mrePk, other.mrePk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgMotivoRetiro[ mrePk=" + mrePk + " ]";
    }

}
