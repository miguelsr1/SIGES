/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumEstadosProceso;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAfFuenteFinanciamiento;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAfProyectos;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_af_liquidacion_proyecto", schema = Constantes.SCHEMA_ACTIVO_FIJO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "lprPk", scope = SgAfLiquidacionProyecto.class)
public class SgAfLiquidacionProyecto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "lpr_pk")
    private Long lprPk;
 
    @Basic(optional = false)
    @Column(name = "lpr_fecha_liquidacion")
    private LocalDate lprFechaLiquidacion;
    
    @JoinColumn(name = "lpr_fuente_financiamiento_origen_fk", referencedColumnName = "ffi_pk")
    @ManyToOne
    private SgAfFuenteFinanciamiento lprFuenteFinanciamientoOrigenFk;
      
    @JoinColumn(name = "lpr_proyecto_fk", referencedColumnName = "pro_pk")
    @ManyToOne
    private SgAfProyectos lprProyectoFk;
    
    @JoinColumn(name = "lpr_fuente_financiamiento_destino_fk", referencedColumnName = "ffi_pk")
    @ManyToOne
    private SgAfFuenteFinanciamiento lprFuenteFinanciamientoDestinoFk;
    
    @Column(name = "lpr_fecha_creacion")
    private LocalDateTime lprFechaCreacion;
    
    @Column(name = "lpr_fecha_inicio_procesamiento")
    private LocalDateTime lprFechaInicioProcesamiento;
    
    @Column(name = "lpr_fecha_final_procesamiento")
    private LocalDateTime lprFechaFinalProcesamiento;
    
    @Basic(optional = false)
    @Column(name = "lpr_estado")
    @Enumerated(value = EnumType.STRING)
    private EnumEstadosProceso lprEstado;
    
    @Column(name = "lpr_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime lprUltModFecha;
    
    @Size(max = 45)
    @Column(name = "lpr_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String lprUltModUsuario;
    
    @Column(name = "lpr_version")
    @Version
    private Integer lprVersion;

    public SgAfLiquidacionProyecto() {
    }

    public Long getLprPk() {
        return lprPk;
    }

    public void setLprPk(Long lprPk) {
        this.lprPk = lprPk;
    }

    public LocalDate getLprFechaLiquidacion() {
        return lprFechaLiquidacion;
    }

    public void setLprFechaLiquidacion(LocalDate lprFechaLiquidacion) {
        this.lprFechaLiquidacion = lprFechaLiquidacion;
    }

    public SgAfFuenteFinanciamiento getLprFuenteFinanciamientoOrigenFk() {
        return lprFuenteFinanciamientoOrigenFk;
    }

    public void setLprFuenteFinanciamientoOrigenFk(SgAfFuenteFinanciamiento lprFuenteFinanciamientoOrigenFk) {
        this.lprFuenteFinanciamientoOrigenFk = lprFuenteFinanciamientoOrigenFk;
    }

    public SgAfFuenteFinanciamiento getLprFuenteFinanciamientoDestinoFk() {
        return lprFuenteFinanciamientoDestinoFk;
    }

    public void setLprFuenteFinanciamientoDestinoFk(SgAfFuenteFinanciamiento lprFuenteFinanciamientoDestinoFk) {
        this.lprFuenteFinanciamientoDestinoFk = lprFuenteFinanciamientoDestinoFk;
    }

    public SgAfProyectos getLprProyectoFk() {
        return lprProyectoFk;
    }

    public void setLprProyectoFk(SgAfProyectos lprProyectoFk) {
        this.lprProyectoFk = lprProyectoFk;
    }

    public LocalDateTime getLprFechaCreacion() {
        return lprFechaCreacion;
    }

    public void setLprFechaCreacion(LocalDateTime lprFechaCreacion) {
        this.lprFechaCreacion = lprFechaCreacion;
    }

    public LocalDateTime getLprFechaInicioProcesamiento() {
        return lprFechaInicioProcesamiento;
    }

    public void setLprFechaInicioProcesamiento(LocalDateTime lprFechaInicioProcesamiento) {
        this.lprFechaInicioProcesamiento = lprFechaInicioProcesamiento;
    }

    public LocalDateTime getLprFechaFinalProcesamiento() {
        return lprFechaFinalProcesamiento;
    }

    public void setLprFechaFinalProcesamiento(LocalDateTime lprFechaFinalProcesamiento) {
        this.lprFechaFinalProcesamiento = lprFechaFinalProcesamiento;
    }

    public EnumEstadosProceso getLprEstado() {
        return lprEstado;
    }

    public void setLprEstado(EnumEstadosProceso lprEstado) {
        this.lprEstado = lprEstado;
    }

    public LocalDateTime getLprUltModFecha() {
        return lprUltModFecha;
    }

    public void setLprUltModFecha(LocalDateTime lprUltModFecha) {
        this.lprUltModFecha = lprUltModFecha;
    }

    public String getLprUltModUsuario() {
        return lprUltModUsuario;
    }

    public void setLprUltModUsuario(String lprUltModUsuario) {
        this.lprUltModUsuario = lprUltModUsuario;
    }

    public Integer getLprVersion() {
        return lprVersion;
    }

    public void setLprVersion(Integer lprVersion) {
        this.lprVersion = lprVersion;
    }

    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (lprPk != null ? lprPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAfLiquidacionProyecto)) {
            return false;
        }
        SgAfLiquidacionProyecto other = (SgAfLiquidacionProyecto) object;
        if ((this.lprPk == null && other.lprPk != null) || (this.lprPk != null && !this.lprPk.equals(other.lprPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgAfLiquidacionProyecto[ lprPk=" + lprPk + " ]";
    }
    
}
