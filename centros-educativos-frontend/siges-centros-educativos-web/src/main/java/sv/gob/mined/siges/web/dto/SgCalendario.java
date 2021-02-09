/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "calPk", scope = SgCalendario.class)
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

    private List<SgActividadCalendario> calActividadesCalendario;

    private List<SgPeriodoCalificacion> calPeriodosCalificacion;
    
    private Boolean calPermiteCalcularNotaInstitucional;   

    private Boolean calPermiteCalcularNotaAprobacion;
    
    private Boolean calPermiteCierreAnio;   

    private Boolean calPermiteMatricularSiguienteAnio;
    
    private Boolean calPermiteCopiarSeccion;   

    public SgCalendario() {
        this.calHabilitado = Boolean.TRUE;
        this.calActividadesCalendario = new ArrayList();
        this.calPermiteCalcularNotaAprobacion = Boolean.TRUE;
        this.calPermiteCalcularNotaInstitucional = Boolean.TRUE;
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

    public List<SgActividadCalendario> getCalActividadesCalendario() {
        return calActividadesCalendario;
    }

    public void setCalActividadesCalendario(List<SgActividadCalendario> calActividadesCalendario) {
        this.calActividadesCalendario = calActividadesCalendario;
    }

    public List<SgPeriodoCalificacion> getCalPeriodosCalificacion() {
        return calPeriodosCalificacion;
    }

    public void setCalPeriodosCalificacion(List<SgPeriodoCalificacion> calPeriodosCalificacion) {
        this.calPeriodosCalificacion = calPeriodosCalificacion;
    }

    public Integer getCalCantidadDiasLectivo() {
        return calCantidadDiasLectivo;
    }

    public void setCalCantidadDiasLectivo(Integer calCantidadDiasLectivo) {
        this.calCantidadDiasLectivo = calCantidadDiasLectivo;
    }

    public Boolean getCalPermiteCalcularNotaInstitucional() {
        return calPermiteCalcularNotaInstitucional;
    }

    public void setCalPermiteCalcularNotaInstitucional(Boolean calPermiteCalcularNotaInstitucional) {
        this.calPermiteCalcularNotaInstitucional = calPermiteCalcularNotaInstitucional;
    }

    public Boolean getCalPermiteCalcularNotaAprobacion() {
        return calPermiteCalcularNotaAprobacion;
    }

    public void setCalPermiteCalcularNotaAprobacion(Boolean calPermiteCalcularNotaAprobacion) {
        this.calPermiteCalcularNotaAprobacion = calPermiteCalcularNotaAprobacion;
    }

    public Boolean getCalPermiteCierreAnio() {
        return calPermiteCierreAnio;
    }

    public void setCalPermiteCierreAnio(Boolean calPermiteCierreAnio) {
        this.calPermiteCierreAnio = calPermiteCierreAnio;
    }

    public Boolean getCalPermiteMatricularSiguienteAnio() {
        return calPermiteMatricularSiguienteAnio;
    }

    public void setCalPermiteMatricularSiguienteAnio(Boolean calPermiteMatricularSiguienteAnio) {
        this.calPermiteMatricularSiguienteAnio = calPermiteMatricularSiguienteAnio;
    }

    public Boolean getCalPermiteCopiarSeccion() {
        return calPermiteCopiarSeccion;
    }

    public void setCalPermiteCopiarSeccion(Boolean calPermiteCopiarSeccion) {
        this.calPermiteCopiarSeccion = calPermiteCopiarSeccion;
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
