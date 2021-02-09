/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.siap2;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.SgAnioFiscal;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

/**
 * Entidad correspondiente a las transferencias.
 *
 * @author sofis-iquezada
 */
@Entity
@Table(name = "ss_transferencias", schema = Constantes.SCHEMA_SIAP2)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "traId", scope = SsTransferencia.class)
@Audited
public class SsTransferencia implements Serializable {

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tra_id")
    private Long traId;

    @ManyToOne
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @JoinColumn(name = "tra_id_anio_fiscal")
    private SgAnioFiscal traAnioFiscal;

    @ManyToOne
    @JoinColumn(name = "tra_id_subcomponente")
    private SsGesPresEs traSubcomponente;

    @ManyToOne
    @JoinColumn(name = "tra_id_linea_presupuestaria")
    private SsCuenta traLineaPresupuestaria;

    @Column(name = "tra_porcentaje")
    private BigDecimal traPorcentaje;

    @Column(name = "tra_importe_fijo_centro")
    private BigDecimal traImporteFijoCentro;

    @Column(name = "tra_remanente")
    private Boolean traRemanente;

    @Column(name = "tra_ult_mod")
    @Temporal(TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date traUltModFecha;

    @Column(name = "tra_ult_usuario")
    @AtributoUltimoUsuario
    private String traUltModUsuario;

    @Column(name = "tra_version")
    @Version
    private Integer traVersion;

    public Long getTraId() {
        return traId;
    }

    public void setTraId(Long traId) {
        this.traId = traId;
    }

    public SgAnioFiscal getTraAnioFiscal() {
        return traAnioFiscal;
    }

    public void setTraAnioFiscal(SgAnioFiscal traAnioFiscal) {
        this.traAnioFiscal = traAnioFiscal;
    }

    public SsGesPresEs getTraSubcomponente() {
        return traSubcomponente;
    }

    public void setTraSubcomponente(SsGesPresEs traSubcomponente) {
        this.traSubcomponente = traSubcomponente;
    }

    public SsCuenta getTraLineaPresupuestaria() {
        return traLineaPresupuestaria;
    }

    public void setTraLineaPresupuestaria(SsCuenta traLineaPresupuestaria) {
        this.traLineaPresupuestaria = traLineaPresupuestaria;
    }

    public BigDecimal getTraPorcentaje() {
        return traPorcentaje;
    }

    public void setTraPorcentaje(BigDecimal traPorcentaje) {
        this.traPorcentaje = traPorcentaje;
    }

    public BigDecimal getTraImporteFijoCentro() {
        return traImporteFijoCentro;
    }

    public void setTraImporteFijoCentro(BigDecimal traImporteFijoCentro) {
        this.traImporteFijoCentro = traImporteFijoCentro;
    }

    public Boolean getTraRemanente() {
        return traRemanente;
    }

    public void setTraRemanente(Boolean traRemanente) {
        this.traRemanente = traRemanente;
    }

    public Date getTraUltModFecha() {
        return traUltModFecha;
    }

    public void setTraUltModFecha(Date traUltModFecha) {
        this.traUltModFecha = traUltModFecha;
    }

    public String getTraUltModUsuario() {
        return traUltModUsuario;
    }

    public void setTraUltModUsuario(String traUltModUsuario) {
        this.traUltModUsuario = traUltModUsuario;
    }

    public Integer getTraVersion() {
        return traVersion;
    }

    public void setTraVersion(Integer traVersion) {
        this.traVersion = traVersion;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.traId);
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
        final SsTransferencia other = (SsTransferencia) obj;
        if (!Objects.equals(this.traId, other.traId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SsTransferencia{" + "traId=" + traId + ", traAnioFiscal=" + traAnioFiscal + ", traSubcomponente=" + traSubcomponente + ", traLineaPresupuestaria=" + traLineaPresupuestaria + ", traPorcentaje=" + traPorcentaje + ", traImporteFijoCentro=" + traImporteFijoCentro + '}';
    }

}
