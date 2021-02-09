/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import sv.gob.mined.siges.web.enumerados.TipoEscalaCalificacion;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ecaPk", scope = SgEscalaCalificacion.class)
public class SgEscalaCalificacion implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long ecaPk;

    private String ecaCodigo;

    private Boolean ecaHabilitado;

    private String ecaNombre;

    private String ecaNombreBusqueda;

    private TipoEscalaCalificacion ecaTipoEscala;

    private LocalDateTime ecaUltModFecha;

    private String ecaUltModUsuario;

    private Integer ecaVersion;

    private SgCalificacion ecaValorMinimo;
    
    private Double ecaMinimo;
    
    private Double ecaMaximo;
    
    private Double ecaMinimoAprobacion;
    
    private Integer ecaPrecision;

    private List<SgCalificacion> ecaCalificaciones;

    public SgEscalaCalificacion() {
        this.ecaHabilitado = Boolean.TRUE;
    }

    public Long getEcaPk() {
        return ecaPk;
    }

    public void setEcaPk(Long ecaPk) {
        this.ecaPk = ecaPk;
    }

    public String getEcaCodigo() {
        return ecaCodigo;
    }

    public void setEcaCodigo(String ecaCodigo) {
        this.ecaCodigo = ecaCodigo;
    }

    public Boolean getEcaHabilitado() {
        return ecaHabilitado;
    }

    public void setEcaHabilitado(Boolean ecaHabilitado) {
        this.ecaHabilitado = ecaHabilitado;
    }

    public String getEcaNombre() {
        return ecaNombre;
    }

    public void setEcaNombre(String ecaNombre) {
        this.ecaNombre = ecaNombre;
    }

    public String getEcaNombreBusqueda() {
        return ecaNombreBusqueda;
    }

    public void setEcaNombreBusqueda(String ecaNombreBusqueda) {
        this.ecaNombreBusqueda = ecaNombreBusqueda;
    }

    public TipoEscalaCalificacion getEcaTipoEscala() {
        return ecaTipoEscala;
    }

    public void setEcaTipoEscala(TipoEscalaCalificacion ecaTipoEscala) {
        this.ecaTipoEscala = ecaTipoEscala;
    }

    public LocalDateTime getEcaUltModFecha() {
        return ecaUltModFecha;
    }

    public void setEcaUltModFecha(LocalDateTime ecaUltModFecha) {
        this.ecaUltModFecha = ecaUltModFecha;
    }

    public String getEcaUltModUsuario() {
        return ecaUltModUsuario;
    }

    public void setEcaUltModUsuario(String ecaUltModUsuario) {
        this.ecaUltModUsuario = ecaUltModUsuario;
    }

    public Integer getEcaVersion() {
        return ecaVersion;
    }

    public void setEcaVersion(Integer ecaVersion) {
        this.ecaVersion = ecaVersion;
    }

    public SgCalificacion getEcaValorMinimo() {
        return ecaValorMinimo;
    }

    public void setEcaValorMinimo(SgCalificacion ecaValorMinimo) {
        this.ecaValorMinimo = ecaValorMinimo;
    }

    public List<SgCalificacion> getEcaCalificaciones() {
        return ecaCalificaciones;
    }

    public void setEcaCalificaciones(List<SgCalificacion> ecaCalificaciones) {
        this.ecaCalificaciones = ecaCalificaciones;
    }

    public Double getEcaMinimo() {
        return ecaMinimo;
    }

    public void setEcaMinimo(Double ecaMinimo) {
        this.ecaMinimo = ecaMinimo;
    }

    public Double getEcaMaximo() {
        return ecaMaximo;
    }

    public void setEcaMaximo(Double ecaMaximo) {
        this.ecaMaximo = ecaMaximo;
    }

    public Double getEcaMinimoAprobacion() {
        return ecaMinimoAprobacion;
    }

    public void setEcaMinimoAprobacion(Double ecaMinimoAprobacion) {
        this.ecaMinimoAprobacion = ecaMinimoAprobacion;
    }

    public Integer getEcaPrecision() {
        return ecaPrecision;
    }

    public void setEcaPrecision(Integer ecaPrecision) {
        this.ecaPrecision = ecaPrecision;
    }
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ecaPk != null ? ecaPk.hashCode() : 0);
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
        final SgEscalaCalificacion other = (SgEscalaCalificacion) obj;
        if (!Objects.equals(this.ecaPk, other.ecaPk)) {
            return false;
        }
        return true;
    }

    
    @Override
    public String toString() {
        return "SgEscalaCalificacion{" + "ecaPk=" + ecaPk + ", ecaCodigo=" + ecaCodigo + ", ecaHabilitado=" + ecaHabilitado + ", ecaNombre=" + ecaNombre + ", ecaNombreBusqueda=" + ecaNombreBusqueda + ", ecaTipoEscala=" + ecaTipoEscala + ", ecaUltModFecha=" + ecaUltModFecha + ", ecaUltModUsuario=" + ecaUltModUsuario + ", ecaVersion=" + ecaVersion + ", ecaValorMinimo=" + ecaValorMinimo + ", ecaCalificaciones=" + ecaCalificaciones + '}';
    }

}
