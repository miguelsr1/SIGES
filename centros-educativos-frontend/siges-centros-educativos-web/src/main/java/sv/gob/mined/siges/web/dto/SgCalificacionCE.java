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
import sv.gob.mined.siges.web.enumerados.EnumCalendarioEscolar;
import sv.gob.mined.siges.web.enumerados.EnumEstadoProcesamientoCalificacionPromocion;
import sv.gob.mined.siges.web.enumerados.EnumTiposPeriodosCalificaciones;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "calPk", scope = SgCalificacionCE.class)
public class SgCalificacionCE implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long calPk;

    private SgComponentePlanEstudio calComponentePlanEstudio;

    private SgRangoFecha calRangoFecha;

    private SgSeccion calSeccion;

    private LocalDate calFecha;

    private LocalDateTime calUltModFecha;

    private String calUltModUsuario;

    private Integer calVersion;

    private EnumTiposPeriodosCalificaciones calTipoPeriodoCalificacion;

    private EnumCalendarioEscolar calTipoCalendarioEscolar;

    private Integer calNumero;

    private List<SgCalificacionEstudiante> calCalificacionesEstudiante;
    
    private Boolean calCerrado;
    
    private EnumEstadoProcesamientoCalificacionPromocion calEstadoProcesamientoPromocion;
    
    private Boolean calTieneEstudiantesPendientes;
    
    private SgInfoProcesamientoCalificacion calInfoProcesamientoCalificacionFk;


    public SgCalificacionCE() {
        this.calCalificacionesEstudiante = new ArrayList<>();        
    }
    
    public Long getCalPk() {
        return calPk;
    }

    public void setCalPk(Long calPk) {
        this.calPk = calPk;
    }

    public SgComponentePlanEstudio getCalComponentePlanEstudio() {
        return calComponentePlanEstudio;
    }

    public void setCalComponentePlanEstudio(SgComponentePlanEstudio calComponentePlanEstudio) {
        this.calComponentePlanEstudio = calComponentePlanEstudio;
    }

    public SgRangoFecha getCalRangoFecha() {
        return calRangoFecha;
    }

    public void setCalRangoFecha(SgRangoFecha calRangoFecha) {
        this.calRangoFecha = calRangoFecha;
    }

    public SgSeccion getCalSeccion() {
        return calSeccion;
    }

    public void setCalSeccion(SgSeccion calSeccion) {
        this.calSeccion = calSeccion;
    }

    public LocalDate getCalFecha() {
        return calFecha;
    }

    public void setCalFecha(LocalDate calFecha) {
        this.calFecha = calFecha;
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

    public List<SgCalificacionEstudiante> getCalCalificacionesEstudiante() {
        return calCalificacionesEstudiante;
    }

    public void setCalCalificacionesEstudiante(List<SgCalificacionEstudiante> calCalificacionesEstudiante) {
        this.calCalificacionesEstudiante = calCalificacionesEstudiante;
    }

    public EnumTiposPeriodosCalificaciones getCalTipoPeriodoCalificacion() {
        return calTipoPeriodoCalificacion;
    }

    public void setCalTipoPeriodoCalificacion(EnumTiposPeriodosCalificaciones calTipoPeriodoCalificacion) {
        this.calTipoPeriodoCalificacion = calTipoPeriodoCalificacion;
    }

    public EnumCalendarioEscolar getCalTipoCalendarioEscolar() {
        return calTipoCalendarioEscolar;
    }

    public void setCalTipoCalendarioEscolar(EnumCalendarioEscolar calTipoCalendarioEscolar) {
        this.calTipoCalendarioEscolar = calTipoCalendarioEscolar;
    }

    public Integer getCalNumero() {
        return calNumero;
    }

    public void setCalNumero(Integer calNumero) {
        this.calNumero = calNumero;
    }

    public Boolean getCalCerrado() {
        return calCerrado;
    }

    public void setCalCerrado(Boolean calCerrado) {
        this.calCerrado = calCerrado;
    }

    public EnumEstadoProcesamientoCalificacionPromocion getCalEstadoProcesamientoPromocion() {
        return calEstadoProcesamientoPromocion;
    }

    public void setCalEstadoProcesamientoPromocion(EnumEstadoProcesamientoCalificacionPromocion calEstadoProcesamientoPromocion) {
        this.calEstadoProcesamientoPromocion = calEstadoProcesamientoPromocion;
    }

    public Boolean getCalTieneEstudiantesPendientes() {
        return calTieneEstudiantesPendientes;
    }

    public void setCalTieneEstudiantesPendientes(Boolean calTieneEstudiantesPendientes) {
        this.calTieneEstudiantesPendientes = calTieneEstudiantesPendientes;
    }

    public SgInfoProcesamientoCalificacion getCalInfoProcesamientoCalificacionFk() {
        return calInfoProcesamientoCalificacionFk;
    }

    public void setCalInfoProcesamientoCalificacionFk(SgInfoProcesamientoCalificacion calInfoProcesamientoCalificacionFk) {
        this.calInfoProcesamientoCalificacionFk = calInfoProcesamientoCalificacionFk;
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
        final SgCalificacionCE other = (SgCalificacionCE) obj;
        if (!Objects.equals(this.calPk, other.calPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgCalificacion[ calPk=" + calPk + " ]";
    }

}
