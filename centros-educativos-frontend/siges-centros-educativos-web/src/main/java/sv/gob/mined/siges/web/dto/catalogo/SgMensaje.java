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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "msjPk", scope = SgMensaje.class)
public class SgMensaje implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long msjPk;

    private String msjCodigo;

    private String msjDescripcion;

    private String msjValor;

    private Boolean msjHabilitado;

    private LocalDateTime msjUltModFecha;

    private String msjUltModUsuario;

    private Integer msjVersion;

    public SgMensaje() {
        this.msjHabilitado = Boolean.TRUE;
    }

    public Long getMsjPk() {
        return msjPk;
    }

    public void setMsjPk(Long msjPk) {
        this.msjPk = msjPk;
    }

    public String getMsjCodigo() {
        return msjCodigo;
    }

    public void setMsjCodigo(String msjCodigo) {
        this.msjCodigo = msjCodigo;
    }

    public String getMsjValor() {
        return msjValor;
    }

    public void setMsjValor(String msjValor) {
        this.msjValor = msjValor;
    }

    public String getMsjDescripcion() {
        return msjDescripcion;
    }

    public void setMsjDescripcion(String msjDescripcion) {
        this.msjDescripcion = msjDescripcion;
    }

    public void setMsjNombre(String msjValor) {
        this.msjValor = msjValor;
    }

    public LocalDateTime getMsjUltModFecha() {
        return msjUltModFecha;
    }

    public void setMsjUltModFecha(LocalDateTime msjUltModFecha) {
        this.msjUltModFecha = msjUltModFecha;
    }

    public String getMsjUltModUsuario() {
        return msjUltModUsuario;
    }

    public void setMsjUltModUsuario(String msjUltModUsuario) {
        this.msjUltModUsuario = msjUltModUsuario;
    }

    public Integer getMsjVersion() {
        return msjVersion;
    }

    public void setMsjVersion(Integer msjVersion) {
        this.msjVersion = msjVersion;
    }

    public Boolean getMsjHabilitado() {
        return msjHabilitado;
    }

    public void setMsjHabilitado(Boolean msjHabilitado) {
        this.msjHabilitado = msjHabilitado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (msjPk != null ? msjPk.hashCode() : 0);
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
        final SgMensaje other = (SgMensaje) obj;
        if (!Objects.equals(this.msjPk, other.msjPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgMensaje[ msjPk=" + msjPk + " ]";
    }

}
