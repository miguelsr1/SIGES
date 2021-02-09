/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sofis.security.DaoOperation;
import com.sofis.security.DataSecurity;
import com.sofis.security.OperationSecurity;
import com.sofis.sofisform.to.CriteriaTO;
import com.sofis.sofisform.to.MatchCriteriaTO;
import com.sofis.utils.CriteriaTOUtils;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumAmbito;
import sv.gob.mined.siges.enumerados.EnumEstadoLiquidacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.siap2.SsCategoriaPresupuestoEscolar;
import sv.gob.mined.siges.persistencia.entidades.siap2.SsGesPresEs;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;

/**
 * Entidad correspondiente a la liquidación
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_liquidaciones", schema = Constantes.SCHEMA_FINANZAS)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "liqPk", scope = SgLiquidacion.class)
@Audited
public class SgLiquidacion implements Serializable, DataSecurity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "liq_pk", nullable = false)
    private Long liqPk;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "liq_componente_pk", referencedColumnName = "cpe_id")
    private SsCategoriaPresupuestoEscolar liqComponentePk;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "liq_sub_componente_fk", referencedColumnName = "ges_id")
    private SsGesPresEs liqSubComponenteFk;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "liq_sede_pk", referencedColumnName = "sed_pk")
    private SgSede liqSedePk;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "liq_anio_pk", referencedColumnName = "ale_pk")
    private SgAnioLectivo liqAnioPk;

    @Column(name = "liq_estado", nullable = false, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private EnumEstadoLiquidacion liqEstado;

    @OneToOne(mappedBy = "elqLiquidacionFk")
    private SgEvaluacionLiquidacion evaluacionLiquidacion;

    @Column(name = "liq_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime liqUltModFecha;

    @Size(max = 45)
    @Column(name = "liq_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String liqUltModUsuario;

    @Column(name = "liq_version")
    @Version
    private Integer liqVersion;
    
    @OneToMany(mappedBy = "mlqLiquidacionPk", fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    private List<SgMovimientoLiquidacion> movimientosLiquidacion;

    public SgLiquidacion() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {

    }

    public Long getLiqPk() {
        return liqPk;
    }

    public void setLiqPk(Long liqPk) {
        this.liqPk = liqPk;
    }

    public SsCategoriaPresupuestoEscolar getLiqComponentePk() {
        return liqComponentePk;
    }

    public void setLiqComponentePk(SsCategoriaPresupuestoEscolar liqComponentePk) {
        this.liqComponentePk = liqComponentePk;
    }

    public SgSede getLiqSedePk() {
        return liqSedePk;
    }

    public void setLiqSedePk(SgSede liqSedePk) {
        this.liqSedePk = liqSedePk;
    }

    public SgAnioLectivo getLiqAnioPk() {
        return liqAnioPk;
    }

    public void setLiqAnioPk(SgAnioLectivo liqAnioPk) {
        this.liqAnioPk = liqAnioPk;
    }

    public EnumEstadoLiquidacion getLiqEstado() {
        return liqEstado;
    }

    public void setLiqEstado(EnumEstadoLiquidacion liqEstado) {
        this.liqEstado = liqEstado;
    }

    public SsGesPresEs getLiqSubComponenteFk() {
        return liqSubComponenteFk;
    }

    public void setLiqSubComponenteFk(SsGesPresEs liqSubComponenteFk) {
        this.liqSubComponenteFk = liqSubComponenteFk;
    }

    public SgEvaluacionLiquidacion getEvaluacionLiquidacion() {
        return evaluacionLiquidacion;
    }

    public void setEvaluacionLiquidacion(SgEvaluacionLiquidacion evaluacionLiquidacion) {
        this.evaluacionLiquidacion = evaluacionLiquidacion;
    }

    public LocalDateTime getLiqUltModFecha() {
        return liqUltModFecha;
    }

    public void setLiqUltModFecha(LocalDateTime liqUltModFecha) {
        this.liqUltModFecha = liqUltModFecha;
    }

    public String getLiqUltModUsuario() {
        return liqUltModUsuario;
    }

    public void setLiqUltModUsuario(String liqUltModUsuario) {
        this.liqUltModUsuario = liqUltModUsuario;
    }

    public Integer getLiqVersion() {
        return liqVersion;
    }

    public void setLiqVersion(Integer liqVersion) {
        this.liqVersion = liqVersion;
    }

    public List<SgMovimientoLiquidacion> getMovimientosLiquidacion() {
        return movimientosLiquidacion;
    }

    public void setMovimientosLiquidacion(List<SgMovimientoLiquidacion> movimientosLiquidacion) {
        this.movimientosLiquidacion = movimientosLiquidacion;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.liqPk);
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
        final SgLiquidacion other = (SgLiquidacion) obj;
        if (!Objects.equals(this.liqPk, other.liqPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgLiquidacion{" + "liqPk=" + liqPk + ", liqEstado=" + liqEstado + ", liqUltModFecha=" + liqUltModFecha + ", liqUltModUsuario=" + liqUltModUsuario + ", liqVersion=" + liqVersion + '}';
    }

    @Override
    public String securityAmbitCreate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation d) {
        if (o.getAmbit().equalsIgnoreCase(EnumAmbito.DEPARTAMENTAL.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "liqSedePk.sedDireccion.dirDepartamento.depPk", o.getContext());
        } else {
            return null;
        }
    }

}
