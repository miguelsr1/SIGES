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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "calPk", scope = SgCalificacion.class)
public class SgCalificacion implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long calPk;

    private String calCodigo;

    private String calValor;
    
    private String calValorEnCertificado;

    private Integer calOrden;

    private Boolean calHabilitado;

    private LocalDateTime calUltModFecha;

    private String calUltModUsuario;

    private Integer calVersion;

    private SgEscalaCalificacion calEscala;

    public SgCalificacion() {
        this.calHabilitado = Boolean.TRUE;
    }

    public Long getCalPk() {
        return calPk;
    }

    public void setCalPk(Long calPk) {
        this.calPk = calPk;
    }

    public String getCalCodigo() {
        return calCodigo;
    }

    public void setCalCodigo(String calCodigo) {
        this.calCodigo = calCodigo;
    }

    public String getCalValor() {
        return calValor;
    }

    public void setCalValor(String calValor) {
        this.calValor = calValor;
    }

    public Integer getCalOrden() {
        return calOrden;
    }

    public void setCalOrden(Integer calOrden) {
        this.calOrden = calOrden;
    }

    public Boolean getCalHabilitado() {
        return calHabilitado;
    }

    public void setCalHabilitado(Boolean calHabilitado) {
        this.calHabilitado = calHabilitado;
    }

    public LocalDateTime getCalUltModFecha() {
        return calUltModFecha;
    }

    public void setCalUltModFecha(LocalDateTime calUltModFecha) {
        this.calUltModFecha = calUltModFecha;
    }

    public String getCalUltModUsuario() {
        return calUltModUsuario;
    }

    public void setCalUltModUsuario(String calUltModUsuario) {
        this.calUltModUsuario = calUltModUsuario;
    }

    public Integer getCalVersion() {
        return calVersion;
    }

    public void setCalVersion(Integer calVersion) {
        this.calVersion = calVersion;
    }

    public SgEscalaCalificacion getCalEscala() {
        return calEscala;
    }

    public void setCalEscala(SgEscalaCalificacion calEscala) {
        this.calEscala = calEscala;
    }

    public String getCalValorEnCertificado() {
        return calValorEnCertificado;
    }

    public void setCalValorEnCertificado(String calValorEnCertificado) {
        this.calValorEnCertificado = calValorEnCertificado;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (calPk != null ? calPk.hashCode() : 0);
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
        final SgCalificacion other = (SgCalificacion) obj;
        if (!Objects.equals(this.calPk, other.calPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgCalificacion{" + "calPk=" + calPk + ", calCodigo=" + calCodigo + ", calValor=" + calValor + ", calOrden=" + calOrden + ", calHabilitado=" + calHabilitado + ", calUltModFecha=" + calUltModFecha + ", calUltModUsuario=" + calUltModUsuario + ", calVersion=" + calVersion + ", calEscala=" + calEscala + '}';
    }

    

}
