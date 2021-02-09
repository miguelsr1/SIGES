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
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumAmbito;
import sv.gob.mined.siges.enumerados.EnumTipoDesembolso;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.siap2.SsFuenteRecursos;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

@Entity
@Table(name = "sg_detalle_desembolsos", schema = Constantes.SCHEMA_FINANZAS)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "dedPk", scope = SgDetalleDesembolso.class)
/**
 * Entidad correspondiente al detalle de los desembolsos
 */
public class SgDetalleDesembolso implements Serializable,DataSecurity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ded_pk")
    private Long dedPk;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ded_desembolso_fk", referencedColumnName = "dsb_pk")
    private SgDesembolso dedDesembolsoFk;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ded_fuente_recursos_fk", referencedColumnName = "fue_id")
    private SsFuenteRecursos dedFuenteRecursosFk;

    @OneToOne
    @JoinColumn(name = "ded_req_fondo_fk", referencedColumnName = "str_pk")
    private SgRequerimientoFondo dedReqFondoFk;

    @OneToOne(mappedBy = "mddDetDesembolsoFk", cascade = CascadeType.ALL)
    private SgMovimientoDireccionDepartamental movDireccionDep;

    @Column(name = "ded_tipo_desembolso")
    @Enumerated(EnumType.STRING)
    private EnumTipoDesembolso dedTipoDesembolso;

    @Column(name = "ded_porcentaje")
    private Double dedPorcentaje;

    @Column(name = "ded_monto")
    private BigDecimal dedMonto;

    @Column(name = "ded_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime dedUltModFecha;

    @Size(max = 45)
    @Column(name = "ded_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String dedUltModUsuario;

    @Column(name = "ded_version")
    @Version
    private Integer dedVersion;

    public SgDetalleDesembolso() {
    }

    public Long getDedPk() {
        return dedPk;
    }

    public void setDedPk(Long dedPk) {
        this.dedPk = dedPk;
    }

    public BigDecimal getDedMonto() {
        return dedMonto;
    }

    public void setDedMonto(BigDecimal dedMonto) {
        this.dedMonto = dedMonto;
    }

    public LocalDateTime getDedUltModFecha() {
        return dedUltModFecha;
    }

    public void setDedUltModFecha(LocalDateTime dedUltModFecha) {
        this.dedUltModFecha = dedUltModFecha;
    }

    public String getDedUltModUsuario() {
        return dedUltModUsuario;
    }

    public void setDedUltModUsuario(String dedUltModUsuario) {
        this.dedUltModUsuario = dedUltModUsuario;
    }

    public Integer getDedVersion() {
        return dedVersion;
    }

    public void setDedVersion(Integer dedVersion) {
        this.dedVersion = dedVersion;
    }

    public SgDesembolso getDedDesembolsoFk() {
        return dedDesembolsoFk;
    }

    public void setDedDesembolsoFk(SgDesembolso dedDesembolsoFk) {
        this.dedDesembolsoFk = dedDesembolsoFk;
    }

    public SgRequerimientoFondo getDedReqFondoFk() {
        return dedReqFondoFk;
    }

    public void setDedReqFondoFk(SgRequerimientoFondo dedReqFondoFk) {
        this.dedReqFondoFk = dedReqFondoFk;
    }

    public SsFuenteRecursos getDedFuenteRecursosFk() {
        return dedFuenteRecursosFk;
    }

    public void setDedFuenteRecursosFk(SsFuenteRecursos dedFuenteRecursosFk) {
        this.dedFuenteRecursosFk = dedFuenteRecursosFk;
    }

    public Double getDedPorcentaje() {
        return dedPorcentaje;
    }

    public void setDedPorcentaje(Double dedPorcentaje) {
        this.dedPorcentaje = dedPorcentaje;
    }

    public SgMovimientoDireccionDepartamental getMovDireccionDep() {
        return movDireccionDep;
    }

    public void setMovDireccionDep(SgMovimientoDireccionDepartamental movDireccionDep) {
        this.movDireccionDep = movDireccionDep;
    }

    public EnumTipoDesembolso getDedTipoDesembolso() {
        return dedTipoDesembolso;
    }

    public void setDedTipoDesembolso(EnumTipoDesembolso dedTipoDesembolso) {
        this.dedTipoDesembolso = dedTipoDesembolso;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + Objects.hashCode(this.dedPk);
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
        final SgDetalleDesembolso other = (SgDetalleDesembolso) obj;
        if (!Objects.equals(this.dedPk, other.dedPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgDetalleDesembolso{" + "dedPk=" + dedPk + ", dedDesembolsoFk=" + dedDesembolsoFk + ", dedFuenteRecursosFk=" + dedFuenteRecursosFk + ", dedReqFondoFk=" + dedReqFondoFk + ", dedPorcentaje=" + dedPorcentaje + ", dedMonto=" + dedMonto + ", dedUltModFecha=" + dedUltModFecha + ", dedUltModUsuario=" + dedUltModUsuario + ", dedVersion=" + dedVersion + '}';
    }

    @Override
    public String securityAmbitCreate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation d) {
        if (o.getAmbit().equalsIgnoreCase(EnumAmbito.DEPARTAMENTAL.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "dedReqFondoFk.strTransferenciaGDepFk.tgdDepartamentoFk.depPk", o.getContext());
        } else {
            return null;
        }
    }

}
