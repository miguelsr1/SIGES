package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.constantes.Constantes;
import gob.mined.siap2.entities.enums.EstadoTransferenciaACed;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
@Table(schema = Constantes.SCHEMA_FINANZAS, name = "sg_transferencia_direccion_departamental")
@EntityListeners({DecoratorEntityListener.class})
@Cacheable(false)
public class TransferenciaDireccionDepartamental implements Serializable{
    
    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "sg_transferencias_a_ced_tac_pk_seq", sequenceName = Constantes.SCHEMA_FINANZAS + ".sg_transferencias_a_ced_tac_pk_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sg_transferencias_a_ced_tac_pk_seq")
    @Column(name = "tdd_pk")
    private Integer pk;
    
    @ManyToOne
    @JoinColumn(name = "tdd_transferencia_fk")
    private TransferenciasComponente transferenciasComponente;
    
    @ManyToOne
    @JoinColumn(name = "tdd_direccion_dep_fk")
    private DireccionDepartamental direccionDep;
    
    @Column(name = "tdd_monto_autorizado")
    private BigDecimal montoAutorizado;
    
    @Column(name = "tdd_monto_solicitado")
    private BigDecimal montoSolicitado;
    
    @Column(name = "tdd_estado")
    @Enumerated(EnumType.STRING)
    private EstadoTransferenciaACed estado;
    
    @Column(name = "tdd_beneficiarios")
    private Integer beneficiarios;
    
    @Column(name = "tdd_ult_mod_fecha")
    @Temporal(TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultModFecha;

    @Column(name = "tdd_ult_mod_usuario")
    @AtributoUltimoUsuario
    private String ultModUsuario;
    
    @Column(name = "tdd_version")
    @Version
    private Integer version;

    
    
    
    
    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public TransferenciasComponente getTransferenciasComponente() {
        return transferenciasComponente;
    }

    public void setTransferenciasComponente(TransferenciasComponente transferenciasComponente) {
        this.transferenciasComponente = transferenciasComponente;
    }

    public DireccionDepartamental getDireccionDep() {
        return direccionDep;
    }

    public void setDireccionDep(DireccionDepartamental direccionDep) {
        this.direccionDep = direccionDep;
    }

    public BigDecimal getMontoAutorizado() {
        return montoAutorizado;
    }

    public void setMontoAutorizado(BigDecimal montoAutorizado) {
        this.montoAutorizado = montoAutorizado;
    }

    public BigDecimal getMontoSolicitado() {
        return montoSolicitado;
    }

    public void setMontoSolicitado(BigDecimal montoSolicitado) {
        this.montoSolicitado = montoSolicitado;
    }

    public EstadoTransferenciaACed getEstado() {
        return estado;
    }

    public void setEstado(EstadoTransferenciaACed estado) {
        this.estado = estado;
    }

    public Date getUltModFecha() {
        return ultModFecha;
    }

    public void setUltModFecha(Date ultModFecha) {
        this.ultModFecha = ultModFecha;
    }

    public String getUltModUsuario() {
        return ultModUsuario;
    }

    public void setUltModUsuario(String ultModUsuario) {
        this.ultModUsuario = ultModUsuario;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getBeneficiarios() {
        return beneficiarios;
    }

    public void setBeneficiarios(Integer beneficiarios) {
        this.beneficiarios = beneficiarios;
    }
    
    

    @Override
    public String toString() {
        return "TransferenciaDireccionDepartamental{" + "pk=" + pk + ", transferenciasComponente=" + transferenciasComponente + ", direccionDep=" + direccionDep + ", montoAutorizado=" + montoAutorizado + ", montoSolicitado=" + montoSolicitado + ", estado=" + estado + ", ultModFecha=" + ultModFecha + ", ultModUsuario=" + ultModUsuario + ", version=" + version + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.pk);
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
        final TransferenciaDireccionDepartamental other = (TransferenciaDireccionDepartamental) obj;
        if (!Objects.equals(this.pk, other.pk)) {
            return false;
        }
        if (!Objects.equals(this.transferenciasComponente, other.transferenciasComponente)) {
            return false;
        }
        if (!Objects.equals(this.direccionDep, other.direccionDep)) {
            return false;
        }
        if (!Objects.equals(this.montoAutorizado, other.montoAutorizado)) {
            return false;
        }
        return true;
    }

    

    
}
