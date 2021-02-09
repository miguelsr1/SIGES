/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumCalendarioEscolar;
import sv.gob.mined.siges.enumerados.EnumTiposPeriodosCalificaciones;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgCalificacion;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_rel_periodos_habilitacion_periodo", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "rphPk", scope = SgRelPeriodosHabilitacionPeriodo.class)
@Audited
public class SgRelPeriodosHabilitacionPeriodo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rph_pk", nullable = false)
    private Long rphPk;
    
    @Column(name = "rph_tipo_periodo_calificacion")
    @Enumerated(EnumType.STRING)
    private EnumTiposPeriodosCalificaciones rphTipoPeriodoCalificacion;

    @Column(name = "rph_tipo_calendario_escolar")
    @Enumerated(EnumType.STRING)
    private EnumCalendarioEscolar rphTipoCalendarioEscolar;
    
    @JoinColumn(name = "rph_rango_fecha_fk", referencedColumnName = "rfe_pk")
    @ManyToOne
    private SgRangoFecha rphRangoFechaFk;
    
    @JoinColumn(name = "rph_habilitacion_periodo_calificacion_fk", referencedColumnName = "hpc_pk")
    @ManyToOne
    private SgHabilitacionPeriodoCalificacion rphHabilitacionPeriodoCalificacionFk;
    
    @JoinColumn(name = "rph_componente_plan_grado_fk", referencedColumnName = "cpg_pk")
    @ManyToOne
    private SgComponentePlanGrado rphComponentePlanGradoFk;
    
    @Column(name = "rph_numero_extraordinario")
    private Integer rphNumeroExtraordinario;
    
    @Column(name = "rph_calificacion_numerica")
    private String rphCalificacionNumerica;
    
    @JoinColumn(name = "rph_calificacion_conceptual", referencedColumnName = "cal_pk")
    @ManyToOne
    private SgCalificacion rphCalificacionConceptual;
    
    @Column(name = "rph_calificacion_actual_valor")
    private String rphCalificacionActualValor;
    
    @Column(name = "rph_info_procesamiento")
    private String rphInfoProcesamiento;

    @Column(name = "rph_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime rphUltModFecha;

    @Size(max = 45)
    @Column(name = "rph_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String rphUltModUsuario;

    @Column(name = "rph_version")
    @Version
    private Integer rphVersion;

    public SgRelPeriodosHabilitacionPeriodo() {
    }


    public Long getRphPk() {
        return rphPk;
    }

    public void setRphPk(Long rphPk) {
        this.rphPk = rphPk;
    }

    public EnumTiposPeriodosCalificaciones getRphTipoPeriodoCalificacion() {
        return rphTipoPeriodoCalificacion;
    }

    public void setRphTipoPeriodoCalificacion(EnumTiposPeriodosCalificaciones rphTipoPeriodoCalificacion) {
        this.rphTipoPeriodoCalificacion = rphTipoPeriodoCalificacion;
    }

    public EnumCalendarioEscolar getRphTipoCalendarioEscolar() {
        return rphTipoCalendarioEscolar;
    }

    public void setRphTipoCalendarioEscolar(EnumCalendarioEscolar rphTipoCalendarioEscolar) {
        this.rphTipoCalendarioEscolar = rphTipoCalendarioEscolar;
    }

    public SgRangoFecha getRphRangoFechaFk() {
        return rphRangoFechaFk;
    }

    public void setRphRangoFechaFk(SgRangoFecha rphRangoFechaFk) {
        this.rphRangoFechaFk = rphRangoFechaFk;
    }

   
    public SgHabilitacionPeriodoCalificacion getRphHabilitacionPeriodoCalificacionFk() {
        return rphHabilitacionPeriodoCalificacionFk;
    }

    public void setRphHabilitacionPeriodoCalificacionFk(SgHabilitacionPeriodoCalificacion rphHabilitacionPeriodoCalificacionFk) {
        this.rphHabilitacionPeriodoCalificacionFk = rphHabilitacionPeriodoCalificacionFk;
    }


    public LocalDateTime getRphUltModFecha() {
        return rphUltModFecha;
    }

    public void setRphUltModFecha(LocalDateTime rphUltModFecha) {
        this.rphUltModFecha = rphUltModFecha;
    }

    public String getRphUltModUsuario() {
        return rphUltModUsuario;
    }

    public void setRphUltModUsuario(String rphUltModUsuario) {
        this.rphUltModUsuario = rphUltModUsuario;
    }

    public Integer getRphVersion() {
        return rphVersion;
    }

    public void setRphVersion(Integer rphVersion) {
        this.rphVersion = rphVersion;
    }

    public SgComponentePlanGrado getRphComponentePlanGradoFk() {
        return rphComponentePlanGradoFk;
    }

    public void setRphComponentePlanGradoFk(SgComponentePlanGrado rphComponentePlanGradoFk) {
        this.rphComponentePlanGradoFk = rphComponentePlanGradoFk;
    }

    public Integer getRphNumeroExtraordinario() {
        return rphNumeroExtraordinario;
    }

    public void setRphNumeroExtraordinario(Integer rphNumeroExtraordinario) {
        this.rphNumeroExtraordinario = rphNumeroExtraordinario;
    }

    public String getRphCalificacionNumerica() {
        return rphCalificacionNumerica;
    }

    public void setRphCalificacionNumerica(String rphCalificacionNumerica) {
        this.rphCalificacionNumerica = rphCalificacionNumerica;
    }

    public SgCalificacion getRphCalificacionConceptual() {
        return rphCalificacionConceptual;
    }

    public void setRphCalificacionConceptual(SgCalificacion rphCalificacionConceptual) {
        this.rphCalificacionConceptual = rphCalificacionConceptual;
    }

    public String getRphInfoProcesamiento() {
        return rphInfoProcesamiento;
    }

    public void setRphInfoProcesamiento(String rphInfoProcesamiento) {
        this.rphInfoProcesamiento = rphInfoProcesamiento;
    }

    public String getRphCalificacionActualValor() {
        return rphCalificacionActualValor;
    }

    public void setRphCalificacionActualValor(String rphCalificacionActualValor) {
        this.rphCalificacionActualValor = rphCalificacionActualValor;
    }

    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.rphPk);
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
        final SgRelPeriodosHabilitacionPeriodo other = (SgRelPeriodosHabilitacionPeriodo) obj;
        if (!Objects.equals(this.rphPk, other.rphPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgRelPeriodosHabilitacionPeriodo{" + "rphPk=" + rphPk + ", rphTipoPeriodoCalificacion=" + rphTipoPeriodoCalificacion + ", rphTipoCalendarioEscolar=" + rphTipoCalendarioEscolar + ", rphRangoFechaFk=" + rphRangoFechaFk + ", rphHabilitacionPeriodoCalificacionFk=" + rphHabilitacionPeriodoCalificacionFk + ", rphUltModFecha=" + rphUltModFecha + ", rphUltModUsuario=" + rphUltModUsuario + ", rphVersion=" + rphVersion + '}';
    }


}
