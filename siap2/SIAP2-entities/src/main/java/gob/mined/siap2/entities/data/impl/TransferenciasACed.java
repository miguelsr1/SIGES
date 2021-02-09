package gob.mined.siap2.entities.data.impl;

import gob.mined.siges.entities.data.impl.SgSede;
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
@Table(schema = Constantes.SCHEMA_FINANZAS, name = "sg_transferencias_a_ced")
@EntityListeners({DecoratorEntityListener.class})
public class TransferenciasACed implements Serializable{
    
    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "sg_transferencias_a_ced_tac_pk_seq", sequenceName = Constantes.SCHEMA_FINANZAS + ".sg_transferencias_a_ced_tac_pk_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sg_transferencias_a_ced_tac_pk_seq")
    @Column(name = "tac_pk")
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "tac_ced_fk")
    private SgSede ced;
    
    @ManyToOne
    @JoinColumn(name = "tac_transferencia_fk")
    private TransferenciasComponente transferencia;
    
    @ManyToOne
    @JoinColumn(name = "tac_transferencia_direccion_dep_fk")
    private TransferenciaDireccionDepartamental transferenciaDireccionDep;
    
    @Column(name = "tac_monto_autorizado")
    private BigDecimal montoAutorizado;
    
    @Column(name = "tac_monto_solicitado")
    private BigDecimal montoSolicitado;
    
    @Column(name = "tac_estado")
    @Enumerated(EnumType.STRING)
    private EstadoTransferenciaACed estado;
    
    @Column(name = "tac_beneficiarios")
    private Integer beneficiarios;
    
    @Column(name = "tac_ult_mod_fecha")
    @Temporal(TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultModFecha;

    @Column(name = "tac_ult_mod_usuario")
    @AtributoUltimoUsuario
    private String ultModUsuario;
    
    @Column(name = "tac_version")
    @Version
    private Integer version;


    
    
    
    
    
    
    
    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SgSede getCed() {
        return ced;
    }

    public void setCed(SgSede ced) {
        this.ced = ced;
    }

    public TransferenciasComponente getTransferencia() {
        return transferencia;
    }

    public void setTransferencia(TransferenciasComponente transferencia) {
        this.transferencia = transferencia;
    }

    public TransferenciaDireccionDepartamental getTransferenciaDireccionDep() {
        return transferenciaDireccionDep;
    }

    public void setTransferenciaDireccionDep(TransferenciaDireccionDepartamental transferenciaDireccionDep) {
        this.transferenciaDireccionDep = transferenciaDireccionDep;
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
        return "TransferenciasACed{" + "id=" + id + ", ced=" + ced + ", transferencia=" + transferencia 
                + ", transferenciaDireccionDep=" + transferenciaDireccionDep 
                + ", montoAutorizado=" + montoAutorizado 
                + ", montoSolicitado=" + montoSolicitado 
                + ", estado=" + estado 
                + ", ultModFecha=" + ultModFecha 
                + ", ultModUsuario=" 
                + ultModUsuario + ", version=" + version 
                + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.id);
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
        final TransferenciasACed other = (TransferenciasACed) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.ced, other.ced)) {
            return false;
        }
        if (!Objects.equals(this.transferencia, other.transferencia)) {
            return false;
        }
        return true;
    }

    

    
    
    
    
    
    
    
}
