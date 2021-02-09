/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "calPk", scope = SgCalendario.class)
public class SgCalendario implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long calPk;

    private String calCodigo;

    private String calNombre;

    private String calNombreBusqueda;

    private String calDescripcion;

    private Boolean calHabilitado;

    private LocalDate calFechaInicio;

    private LocalDate calFechaFin;
    
    private Integer calCantidadDiasLectivo;

    private LocalDateTime calUltModFecha;

    private String calUltModUsuario;

    private Integer calVersion;

    private SgAnioLectivo calAnioLectivo;

    private SgTipoCalendario calTipoCalendario;


    public SgCalendario() {
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

    public String getCalNombre() {
        return calNombre;
    }

    public void setCalNombre(String calNombre) {
        this.calNombre = calNombre;
    }

    public String getCalNombreBusqueda() {
        return calNombreBusqueda;
    }

    public void setCalNombreBusqueda(String calNombreBusqueda) {
        this.calNombreBusqueda = calNombreBusqueda;
    }

    public LocalDate getCalFechaInicio() {
        return calFechaInicio;
    }

    public void setCalFechaInicio(LocalDate calFechaInicio) {
        this.calFechaInicio = calFechaInicio;
    }

    public LocalDate getCalFechaFin() {
        return calFechaFin;
    }

    public void setCalFechaFin(LocalDate calFechaFin) {
        this.calFechaFin = calFechaFin;
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

    public Boolean getCalHabilitado() {
        return calHabilitado;
    }

    public void setCalHabilitado(Boolean calHabilitado) {
        this.calHabilitado = calHabilitado;
    }

    public SgAnioLectivo getCalAnioLectivo() {
        return calAnioLectivo;
    }

    public void setCalAnioLectivo(SgAnioLectivo calAnioLectivo) {
        this.calAnioLectivo = calAnioLectivo;
    }

    public String getCalDescripcion() {
        return calDescripcion;
    }

    public void setCalDescripcion(String calDescripcion) {
        this.calDescripcion = calDescripcion;
    }

    public SgTipoCalendario getCalTipoCalendario() {
        return calTipoCalendario;
    }

    public void setCalTipoCalendario(SgTipoCalendario calTipoCalendario) {
        this.calTipoCalendario = calTipoCalendario;
    }

    public Integer getCalCantidadDiasLectivo() {
        return calCantidadDiasLectivo;
    }

    public void setCalCantidadDiasLectivo(Integer calCantidadDiasLectivo) {
        this.calCantidadDiasLectivo = calCantidadDiasLectivo;
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
        final SgCalendario other = (SgCalendario) obj;
        if (!Objects.equals(this.calPk, other.calPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgCalendario[ calPk=" + calPk + " ]";
    }

}
