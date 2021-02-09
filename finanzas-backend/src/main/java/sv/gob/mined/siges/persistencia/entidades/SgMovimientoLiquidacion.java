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
import java.math.BigDecimal;
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
import javax.persistence.ManyToOne;
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
 * Entidad correspondiente a los movimientos de las liquidaciones
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_movimientos_liquidacion",schema= Constantes.SCHEMA_FINANZAS)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "mlqPk", scope = SgMovimientoLiquidacion.class)
@Audited
public class SgMovimientoLiquidacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "mlq_pk", nullable = false)
    private Long mlqPk;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="mlq_movimiento_pk" , referencedColumnName = "mcb_pk")
    private SgMovimientoCuentaBancaria mlqMovimientoPk;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="mlq_movimiento_cc_pk" , referencedColumnName = "mcc_pk")
    private SgMovimientoCajaChica mlqMovimientoCcPk;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="mlq_liquidacion_pk" , referencedColumnName = "liq_pk", nullable = false)
    private SgLiquidacion mlqLiquidacionPk;

    @Column(name = "mlq_evaluado")
    private Boolean mlqEvaluado;
    
    @Size(max = 4000)
    @Column(name = "mlq_comentario", length = 4000)
    private String mlqComentario;
    
    @Column(name = "mlq_tipo_movimiento")
    @Enumerated(value = EnumType.STRING)
    private EnumMovimientosTipo mlqTipoMovimiento;
    
    @Column(name = "mlq_reintegro")
    private BigDecimal mlqReintegro;
    
    @Column(name = "mlq_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime mlqUltModFecha;

    @Size(max = 45)
    @Column(name = "mlq_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String mlqUltModUsuario;

    @Column(name = "mlq_version")
    @Version
    private Integer mlqVersion;

    public SgMovimientoLiquidacion() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        
    }

    public Long getMlqPk() {
        return mlqPk;
    }

    public void setMlqPk(Long mlqPk) {
        this.mlqPk = mlqPk;
    }

    public SgMovimientoCuentaBancaria getMlqMovimientoPk() {
        return mlqMovimientoPk;
    }

    public void setMlqMovimientoPk(SgMovimientoCuentaBancaria mlqMovimientoPk) {
        this.mlqMovimientoPk = mlqMovimientoPk;
    }

    public SgMovimientoCajaChica getMlqMovimientoCcPk() {
        return mlqMovimientoCcPk;
    }

    public void setMlqMovimientoCcPk(SgMovimientoCajaChica mlqMovimientoCcPk) {
        this.mlqMovimientoCcPk = mlqMovimientoCcPk;
    }
    
    public SgLiquidacion getMlqLiquidacionPk() {
        return mlqLiquidacionPk;
    }

    public void setMlqLiquidacionPk(SgLiquidacion mlqLiquidacionPk) {
        this.mlqLiquidacionPk = mlqLiquidacionPk;
    }

    public String getMlqComentario() {
        return mlqComentario;
    }

    public void setMlqComentario(String mlqComentario) {
        this.mlqComentario = mlqComentario;
    }

    public Boolean getMlqEvaluado() {
        return mlqEvaluado;
    }

    public void setMlqEvaluado(Boolean mlqEvaluado) {
        this.mlqEvaluado = mlqEvaluado;
    }

    public EnumMovimientosTipo getMlqTipoMovimiento() {
        return mlqTipoMovimiento;
    }

    public void setMlqTipoMovimiento(EnumMovimientosTipo mlqTipoMovimiento) {
        this.mlqTipoMovimiento = mlqTipoMovimiento;
    }
    
    
    public LocalDateTime getMlqUltModFecha() {
        return mlqUltModFecha;
    }

    public void setMlqUltModFecha(LocalDateTime mlqUltModFecha) {
        this.mlqUltModFecha = mlqUltModFecha;
    }

    public String getMlqUltModUsuario() {
        return mlqUltModUsuario;
    }

    public void setMlqUltModUsuario(String mlqUltModUsuario) {
        this.mlqUltModUsuario = mlqUltModUsuario;
    }

    public Integer getMlqVersion() {
        return mlqVersion;
    }

    public void setMlqVersion(Integer mlqVersion) {
        this.mlqVersion = mlqVersion;
    }
    
    public BigDecimal getMlqReintegro() {
        return mlqReintegro;
    }

    public void setMlqReintegro(BigDecimal mlqReintegro) {
        this.mlqReintegro = mlqReintegro;
    }
    
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.mlqPk);
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
        final SgMovimientoLiquidacion other = (SgMovimientoLiquidacion) obj;
        if (!Objects.equals(this.mlqPk, other.mlqPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgMovimientoLiquidacion{" + "mlqPk=" + mlqPk + ", mlqEvaluado=" + mlqEvaluado + ", mlqComentario=" + mlqComentario + ", mlqUltModFecha=" + mlqUltModFecha + ", mlqUltModUsuario=" + mlqUltModUsuario + ", mlqVersion=" + mlqVersion + '}';
    }

    
    

}
