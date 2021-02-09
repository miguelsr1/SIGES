/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.siap2;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.SgAnioFiscal;
import sv.gob.mined.siges.persistencia.entidades.SgTransferenciaDireccionDep;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "ss_transferencias_componente", schema = Constantes.SCHEMA_SIAP2)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "tcId", scope = SsTransferenciaComponente.class)
@Audited
/**
 * Entidad correspondiente a las transferencias por componente y fuente.
 */
public class SsTransferenciaComponente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tc_id", nullable = false)
    private Long tcId;

    @JoinColumn(name = "tc_componente", referencedColumnName = "cpe_id")
    @ManyToOne
    private SsCategoriaPresupuestoEscolar componente;

    @JoinColumn(name = "tc_subcomponente", referencedColumnName = "ges_id")
    @ManyToOne
    private SsGesPresEs subComponente;

    @ManyToOne
    @JoinColumn(name = "tc_unidad_presupuestaria", referencedColumnName = "cu_id")
    private SsCuenta unidadPresupuestaria;

    @ManyToOne
    @JoinColumn(name = "tc_linea_presupuestaria", referencedColumnName = "cu_id")
    private SsCuenta lineaPresupuestaria;

    @ManyToOne
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @JoinColumn(name = "tc_anio_fiscal", referencedColumnName = "ani_id")
    private SgAnioFiscal anioFiscal;

    @ManyToOne
    @JoinColumn(name = "tc_transferencia", referencedColumnName = "tra_id")
    private SsTransferencia tcTransferencia;

    @ManyToOne
    @JoinColumn(name = "tc_fuente_recursos_fk", referencedColumnName = "fue_id")
    private SsFuenteRecursos tcFuenteRecursosFk;

    @OneToOne(mappedBy = "tddTransferenciaFk")
    private SgTransferenciaDireccionDep transferenciaDireccionDep;

    @Column(name = "tc_porcentaje")
    private Double tcPorcentaje;

    @Column(name = "tc_estado")
    private Integer tcEstado;

    @Column(name = "tc_ult_mod")
    @AtributoUltimaModificacion
    private LocalDateTime tcUltMod;

    @Size(max = 45)
    @Column(name = "tc_ult_usuario", length = 45)
    @AtributoUltimoUsuario
    private String tcUltUsuario;

    @Column(name = "tc_version")
    @Version
    private Integer tcVersion;

    public SsTransferenciaComponente() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {

    }

    public Long getTcId() {
        return tcId;
    }

    public void setTcId(Long tcId) {
        this.tcId = tcId;
    }

    public Double getTcPorcentaje() {
        return tcPorcentaje;
    }

    public void setTcPorcentaje(Double tcPorcentaje) {
        this.tcPorcentaje = tcPorcentaje;
    }

    public Integer getTcEstado() {
        return tcEstado;
    }

    public void setTcEstado(Integer tcEstado) {
        this.tcEstado = tcEstado;
    }

    public LocalDateTime getTcUltMod() {
        return tcUltMod;
    }

    public void setTcUltMod(LocalDateTime tcUltMod) {
        this.tcUltMod = tcUltMod;
    }

    public String getTcUltUsuario() {
        return tcUltUsuario;
    }

    public void setTcUltUsuario(String tcUltUsuario) {
        this.tcUltUsuario = tcUltUsuario;
    }

    public Integer getTcVersion() {
        return tcVersion;
    }

    public void setTcVersion(Integer tcVersion) {
        this.tcVersion = tcVersion;
    }

    public SsCategoriaPresupuestoEscolar getComponente() {
        return componente;
    }

    public void setComponente(SsCategoriaPresupuestoEscolar componente) {
        this.componente = componente;
    }

    public SsGesPresEs getSubComponente() {
        return subComponente;
    }

    public void setSubComponente(SsGesPresEs subComponente) {
        this.subComponente = subComponente;
    }

    public SsCuenta getUnidadPresupuestaria() {
        return unidadPresupuestaria;
    }

    public void setUnidadPresupuestaria(SsCuenta unidadPresupuestaria) {
        this.unidadPresupuestaria = unidadPresupuestaria;
    }

    public SsCuenta getLineaPresupuestaria() {
        return lineaPresupuestaria;
    }

    public void setLineaPresupuestaria(SsCuenta lineaPresupuestaria) {
        this.lineaPresupuestaria = lineaPresupuestaria;
    }

    public SgAnioFiscal getAnioFiscal() {
        return anioFiscal;
    }

    public void setAnioFiscal(SgAnioFiscal anioFiscal) {
        this.anioFiscal = anioFiscal;
    }

    public SsTransferencia getTcTransferencia() {
        return tcTransferencia;
    }

    public void setTcTransferencia(SsTransferencia tcTransferencia) {
        this.tcTransferencia = tcTransferencia;
    }

    public SsFuenteRecursos getTcFuenteRecursosFk() {
        return tcFuenteRecursosFk;
    }

    public void setTcFuenteRecursosFk(SsFuenteRecursos tcFuenteRecursosFk) {
        this.tcFuenteRecursosFk = tcFuenteRecursosFk;
    }

    public SgTransferenciaDireccionDep getTransferenciaDireccionDep() {
        return transferenciaDireccionDep;
    }

    public void setTransferenciaDireccionDep(SgTransferenciaDireccionDep transferenciaDireccionDep) {
        this.transferenciaDireccionDep = transferenciaDireccionDep;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.tcId);
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
        final SsTransferenciaComponente other = (SsTransferenciaComponente) obj;
        if (!Objects.equals(this.tcId, other.tcId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SsTransferenciaComponente{" + "tcId=" + tcId + ", tcPorcentaje=" + tcPorcentaje + ", tcEstado=" + tcEstado + ", tcUltMod=" + tcUltMod + ", tcUltUsuario=" + tcUltUsuario + ", tcVersion=" + tcVersion + '}';
    }

}
