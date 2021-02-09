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
import com.sofis.security.DaoOperation;
import com.sofis.security.DataSecurity;
import com.sofis.security.OperationSecurity;
import com.sofis.sofisform.to.CriteriaTO;
import com.sofis.sofisform.to.MatchCriteriaTO;
import com.sofis.utils.CriteriaTOUtils;
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
import sv.gob.mined.siges.enumerados.EnumAmbito;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgDepartamento;
import sv.gob.mined.siges.persistencia.entidades.siap2.SsTransferencia;

/**
 * Entidad correspondiente a las transferencias globales departamentales
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_transferencias_gdep", schema =  Constantes.SCHEMA_FINANZAS)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tgdPk", scope = SgTransferenciaGDep.class)
@Audited
public class SgTransferenciaGDep implements Serializable,DataSecurity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tgd_pk", nullable = false)
    private Long tgdPk;

    @JoinColumn(name = "tgd_transferencia_fk", referencedColumnName = "tra_id")
    @ManyToOne
    private SsTransferencia tgdTransferenciaFk;
    
    @JoinColumn(name = "tgd_departamento_fk", referencedColumnName = "dep_pk")
    @ManyToOne
    private SgDepartamento tgdDepartamentoFk;

    @Column(name = "tgd_monto")
    private BigDecimal tgdMonto;

    @Column(name = "tgd_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime tgdUltModFecha;

    @Size(max = 45)
    @Column(name = "tgd_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String tgdUltModUsuario;

    @Column(name = "tgd_version")
    @Version
    private Integer tgdVersion;
    
    @OneToOne(mappedBy = "strTransferenciaGDepFk")
    private SgRequerimientoFondo tgdSolicitudTrans;

    public SgTransferenciaGDep() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
    }

    public Long getTgdPk() {
        return tgdPk;
    }

    public void setTgdPk(Long tgdPk) {
        this.tgdPk = tgdPk;
    }

    public SsTransferencia getTgdTransferenciaFk() {
        return tgdTransferenciaFk;
    }

    public void setTgdTransferenciaFk(SsTransferencia tgdTransferenciaFk) {
        this.tgdTransferenciaFk = tgdTransferenciaFk;
    }

    public SgDepartamento getTgdDepartamentoFk() {
        return tgdDepartamentoFk;
    }

    public void setTgdDepartamentoFk(SgDepartamento tgdDepartamentoFk) {
        this.tgdDepartamentoFk = tgdDepartamentoFk;
    }
    
    public BigDecimal getTgdMonto() {
        return tgdMonto;
    }

    public void setTgdMonto(BigDecimal tgdMonto) {
        this.tgdMonto = tgdMonto;
    }

    public LocalDateTime getTgdUltModFecha() {
        return tgdUltModFecha;
    }

    public void setTgdUltModFecha(LocalDateTime tgdUltModFecha) {
        this.tgdUltModFecha = tgdUltModFecha;
    }

    public String getTgdUltModUsuario() {
        return tgdUltModUsuario;
    }

    public void setTgdUltModUsuario(String tgdUltModUsuario) {
        this.tgdUltModUsuario = tgdUltModUsuario;
    }

    public Integer getTgdVersion() {
        return tgdVersion;
    }

    public void setTgdVersion(Integer tgdVersion) {
        this.tgdVersion = tgdVersion;
    }

    public SgRequerimientoFondo getTgdSolicitudTrans() {
        return tgdSolicitudTrans;
    }

    public void setTgdSolicitudTrans(SgRequerimientoFondo tgdSolicitudTrans) {
        this.tgdSolicitudTrans = tgdSolicitudTrans;
    }
    
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.tgdPk);
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
        final SgTransferenciaGDep other = (SgTransferenciaGDep) obj;
        if (!Objects.equals(this.tgdPk, other.tgdPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgTransferenciaGDep{" + "tgdPk=" + tgdPk + ", tgdTransferenciaFk=" + tgdTransferenciaFk + ", tgdMonto=" + tgdMonto + ", tgdUltModFecha=" + tgdUltModFecha + ", tgdUltModUsuario=" + tgdUltModUsuario + ", tgdVersion=" + tgdVersion + '}';
    }

    @Override
    public String securityAmbitCreate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation d) {
        if (o.getAmbit().equalsIgnoreCase(EnumAmbito.DEPARTAMENTAL.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "tgdDepartamentoFk.depPk", o.getContext());
        } else {
            return null;
        }
    }

    
    
    

}
