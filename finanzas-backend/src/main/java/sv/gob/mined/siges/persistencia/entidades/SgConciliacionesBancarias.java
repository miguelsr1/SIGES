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
import java.math.BigDecimal;
import java.time.LocalDate;
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
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumAmbito;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;

/**
 * Entidad correspondiente a las conciliaciones bancarias
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_conciliaciones_bancarias", schema = Constantes.SCHEMA_FINANZAS)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "conPk", scope = SgConciliacionesBancarias.class)
@Audited
public class SgConciliacionesBancarias implements Serializable,DataSecurity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "con_pk", nullable = false)
    private Long conPk;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @JoinColumn(name = "con_cuenta_fk", referencedColumnName = "cbc_pk")
    @ManyToOne
    private SgCuentasBancarias conCuentaFk;

    @Column(name = "con_fecha_desde")
    private LocalDate conFechaDesde;

    @Column(name = "con_fecha_hasta")
    private LocalDate conFechaHasta;

    @Column(name = "con_monto", nullable = true)
    private BigDecimal conMonto;

    @Column(name = "con_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime conUltModFecha;

    @Size(max = 45)
    @Column(name = "con_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String conUltModUsuario;

    @Column(name = "con_version")
    @Version
    private Integer conVersion;

    public SgConciliacionesBancarias() {
    }

    // <editor-fold defaultstate="collapsed" desc="Getters-Setters">
    public Long getConPk() {
        return conPk;
    }

    public void setConPk(Long conPk) {
        this.conPk = conPk;
    }

    public SgCuentasBancarias getConCuentaFk() {
        return conCuentaFk;
    }

    public void setConCuentaFk(SgCuentasBancarias conCuentaFk) {
        this.conCuentaFk = conCuentaFk;
    }

    public LocalDate getConFechaDesde() {
        return conFechaDesde;
    }

    public void setConFechaDesde(LocalDate conFechaDesde) {
        this.conFechaDesde = conFechaDesde;
    }

    public LocalDate getConFechaHasta() {
        return conFechaHasta;
    }

    public void setConFechaHasta(LocalDate conFechaHasta) {
        this.conFechaHasta = conFechaHasta;
    }

    public BigDecimal getConMonto() {
        return conMonto;
    }

    public void setConMonto(BigDecimal conMonto) {
        this.conMonto = conMonto;
    }

    public LocalDateTime getConUltModFecha() {
        return conUltModFecha;
    }

    public void setConUltModFecha(LocalDateTime conUltModFecha) {
        this.conUltModFecha = conUltModFecha;
    }

    public String getConUltModUsuario() {
        return conUltModUsuario;
    }

    public void setConUltModUsuario(String conUltModUsuario) {
        this.conUltModUsuario = conUltModUsuario;
    }

    public Integer getConVersion() {
        return conVersion;
    }

    public void setConVersion(Integer conVersion) {
        this.conVersion = conVersion;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Hash-Equals">
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.conPk);
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
        final SgConciliacionesBancarias other = (SgConciliacionesBancarias) obj;
        if (!Objects.equals(this.conPk, other.conPk)) {
            return false;
        }
        return true;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="toString">
    @Override
    public String toString() {
        return "com.sofis.entidades.SgConciliacionesBancarias[ conPk=" + conPk + " ]";
    }
    // </editor-fold>

    @Override
    public String securityAmbitCreate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation d) {
        if (o.getAmbit().equalsIgnoreCase(EnumAmbito.DEPARTAMENTAL.name())) {
            //return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estMatriculas.matSeccion.secServicioEducativo.sduSede.sedDireccion.dirDepartamento.depPk", o.getContext());
            return null;
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.SEDE.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "conCuentaFk.cbcSedeFk.sedPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.MINED.name())) {
            return null;
        } else {
            return null;
        }
    }
}
