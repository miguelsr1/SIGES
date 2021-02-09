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
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumMovimientosTipo;

/**
 * Entidad correspondiente a los movimientos de liquidaciones de otros ingresos
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_movimientos_liquidacion_otros", schema = Constantes.SCHEMA_FINANZAS)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "mloPk", scope = SgMovimientoLiquidacionOtro.class)
@Audited
public class SgMovimientoLiquidacionOtro implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "mlo_pk", nullable = false)
    private Long mloPk;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="mlo_movimiento_fk" , referencedColumnName = "mcb_pk", nullable = false)
    private SgMovimientoCuentaBancaria mloMovimientoFk;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="mlo_liquidacion_otro_fk" , referencedColumnName = "loi_pk", nullable = false)
    private SgLiquidacionOtroIngreso mloLiquidacionOtroFk;

    @Column(name = "mlo_evaluado")
    private Boolean mloEvaluado;
    
    @Size(max = 4000)
    @Column(name = "mlo_comentario", length = 4000)
    private String mloComentario;
    
    @Column(name = "mlo_tipo_movimiento")
    @Enumerated(value = EnumType.STRING)
    private EnumMovimientosTipo mloTipoMovimiento;

    @Column(name = "mlo_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime mloUltModFecha;

    @Size(max = 45)
    @Column(name = "mlo_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String mloUltModUsuario;

    @Column(name = "mlo_version")
    @Version
    private Integer mloVersion;

    public SgMovimientoLiquidacionOtro() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        
    }

    public Long getMloPk() {
        return mloPk;
    }

    public void setMloPk(Long mloPk) {
        this.mloPk = mloPk;
    }

    public SgMovimientoCuentaBancaria getMloMovimientoFk() {
        return mloMovimientoFk;
    }

    public void setMloMovimientoFk(SgMovimientoCuentaBancaria mloMovimientoFk) {
        this.mloMovimientoFk = mloMovimientoFk;
    }

    public SgLiquidacionOtroIngreso getMloLiquidacionOtroFk() {
        return mloLiquidacionOtroFk;
    }

    public void setMloLiquidacionOtroFk(SgLiquidacionOtroIngreso mloLiquidacionOtroFk) {
        this.mloLiquidacionOtroFk = mloLiquidacionOtroFk;
    }

    public Boolean getMloEvaluado() {
        return mloEvaluado;
    }

    public void setMloEvaluado(Boolean mloEvaluado) {
        this.mloEvaluado = mloEvaluado;
    }

    public String getMloComentario() {
        return mloComentario;
    }

    public void setMloComentario(String mloComentario) {
        this.mloComentario = mloComentario;
    }

    public EnumMovimientosTipo getMloTipoMovimiento() {
        return mloTipoMovimiento;
    }

    public void setMloTipoMovimiento(EnumMovimientosTipo mloTipoMovimiento) {
        this.mloTipoMovimiento = mloTipoMovimiento;
    }

    
    public LocalDateTime getMloUltModFecha() {
        return mloUltModFecha;
    }

    public void setMloUltModFecha(LocalDateTime mloUltModFecha) {
        this.mloUltModFecha = mloUltModFecha;
    }

    public String getMloUltModUsuario() {
        return mloUltModUsuario;
    }

    public void setMloUltModUsuario(String mloUltModUsuario) {
        this.mloUltModUsuario = mloUltModUsuario;
    }

    public Integer getMloVersion() {
        return mloVersion;
    }

    public void setMloVersion(Integer mloVersion) {
        this.mloVersion = mloVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.mloPk);
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
        final SgMovimientoLiquidacionOtro other = (SgMovimientoLiquidacionOtro) obj;
        if (!Objects.equals(this.mloPk, other.mloPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgMovimientoLiquidacionOtro{" + "mloPk=" + mloPk + ", mloEvaluado=" + mloEvaluado + ", mloComentario=" + mloComentario + ", mloTipoMovimiento=" + mloTipoMovimiento + ", mloUltModFecha=" + mloUltModFecha + ", mloUltModUsuario=" + mloUltModUsuario + ", mloVersion=" + mloVersion + '}';
    }

    
    
    

}
