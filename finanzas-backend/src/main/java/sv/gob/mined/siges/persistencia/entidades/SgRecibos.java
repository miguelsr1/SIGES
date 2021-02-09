/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
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
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumEstadoDocumento;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_recibos", schema = Constantes.SCHEMA_FINANZAS)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "recPk", scope = SgRecibos.class)
@Audited
/**
 * Entidad correspondiente a los recibos de una transferencia de fondos.
 */
public class SgRecibos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rec_pk", nullable = false)
    private Long recPk;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rec_transferencia_fk", referencedColumnName = "tac_pk")
    private SgTransferenciaACed recTransferencia;

    @ManyToOne
    @JoinColumn(name = "rec_archivo_fk", referencedColumnName = "ach_pk")
    private SgArchivo recArchivo;
    
    @Column(name = "rec_fecha")
    private LocalDate recFecha;
    
    @Column(name = "rec_estado", nullable = false, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private EnumEstadoDocumento recEstado;

    @Column(name = "rec_monto")
    private BigDecimal recMonto;

    @Column(name = "rec_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime recUltModFecha;

    @Size(max = 45)
    @Column(name = "rec_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String recUltModUsuario;

    @Column(name = "rec_version")
    @Version
    private Integer recVersion;

    public SgRecibos() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {

    }

    public Long getRecPk() {
        return recPk;
    }

    public void setRecPk(Long recPk) {
        this.recPk = recPk;
    }

    public SgTransferenciaACed getRecTransferencia() {
        return recTransferencia;
    }

    public void setRecTransferencia(SgTransferenciaACed recTransferencia) {
        this.recTransferencia = recTransferencia;
    }

    public SgArchivo getRecArchivo() {
        return recArchivo;
    }

    public void setRecArchivo(SgArchivo recArchivo) {
        this.recArchivo = recArchivo;
    }

    
    public LocalDate getRecFecha() {
        return recFecha;
    }
    
    public void setRecFecha(LocalDate recFecha) {
        this.recFecha = recFecha;
    }

    public BigDecimal getRecMonto() {
        return recMonto;
    }

    public void setRecMonto(BigDecimal recMonto) {
        this.recMonto = recMonto;
    }

    public LocalDateTime getRecUltModFecha() {
        return recUltModFecha;
    }

    public void setRecUltModFecha(LocalDateTime recUltModFecha) {
        this.recUltModFecha = recUltModFecha;
    }

    public String getRecUltModUsuario() {
        return recUltModUsuario;
    }

    public void setRecUltModUsuario(String recUltModUsuario) {
        this.recUltModUsuario = recUltModUsuario;
    }

    public Integer getRecVersion() {
        return recVersion;
    }

    public void setRecVersion(Integer recVersion) {
        this.recVersion = recVersion;
    }

    public EnumEstadoDocumento getRecEstado() {
        return recEstado;
    }

    public void setRecEstado(EnumEstadoDocumento recEstado) {
        this.recEstado = recEstado;
    }
    

    @Override
    public int hashCode() {
        return Objects.hashCode(this.recPk);
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
        final SgRecibos other = (SgRecibos) obj;
        if (!Objects.equals(this.recPk, other.recPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgRecibos{" + "recPk=" + recPk + ", recArchivo=" + recArchivo + ", recUltModFecha=" + recUltModFecha + ", recUltModUsuario=" + recUltModUsuario + ", recVersion=" + recVersion + '}';
    }

}
