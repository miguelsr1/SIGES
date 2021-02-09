/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumDesembolsoEstado;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

@Entity
@Table(name = "sg_desembolsos", schema = Constantes.SCHEMA_FINANZAS)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "dsbPk", scope = SgDesembolso.class)
/**
 * Entidad correspondiente a los desembolsos de las transferencias
 */
public class SgDesembolso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "dsb_pk")
    private Long dsbPk;

    @Column(name = "dsb_estado", nullable = false, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private EnumDesembolsoEstado dsbEstado;

    @Column(name = "dsb_porcentaje")
    private Double dsbPorcentaje;

    @Column(name = "dsb_monto")
    private BigDecimal dsbMonto;

    @Column(name = "dsb_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime dsbUltModFecha;

    @Size(max = 45)
    @Column(name = "dsb_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String dsbltModUsuario;

    @Column(name = "dsb_version")
    @Version
    private Integer dsbVersion;

    @OneToMany(mappedBy = "dedDesembolsoFk", cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    private List<SgDetalleDesembolso> dsbDetalleDesembolso;

    public Long getDsbPk() {
        return dsbPk;
    }

    public void setDsbPk(Long dsbPk) {
        this.dsbPk = dsbPk;
    }

    public LocalDateTime getDsbUltModFecha() {
        return dsbUltModFecha;
    }

    public void setDsbUltModFecha(LocalDateTime dsbUltModFecha) {
        this.dsbUltModFecha = dsbUltModFecha;
    }

    public String getDsbltModUsuario() {
        return dsbltModUsuario;
    }

    public void setDsbltModUsuario(String dsbltModUsuario) {
        this.dsbltModUsuario = dsbltModUsuario;
    }

    public Integer getDsbVersion() {
        return dsbVersion;
    }

    public void setDsbVersion(Integer dsbVersion) {
        this.dsbVersion = dsbVersion;
    }

    public Double getDsbPorcentaje() {
        return dsbPorcentaje;
    }

    public void setDsbPorcentaje(Double dsbPorcentaje) {
        this.dsbPorcentaje = dsbPorcentaje;
    }

    public BigDecimal getDsbMonto() {
        return dsbMonto;
    }

    public void setDsbMonto(BigDecimal dsbMonto) {
        this.dsbMonto = dsbMonto;
    }

    public EnumDesembolsoEstado getDsbEstado() {
        return dsbEstado;
    }

    public void setDsbEstado(EnumDesembolsoEstado dsbEstado) {
        this.dsbEstado = dsbEstado;
    }

    public List<SgDetalleDesembolso> getDsbDetalleDesembolso() {
        return dsbDetalleDesembolso;
    }

    public void setDsbDetalleDesembolso(List<SgDetalleDesembolso> dsbDetalleDesembolso) {
        this.dsbDetalleDesembolso = dsbDetalleDesembolso;
    }

    public SgDesembolso() {
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dsbPk != null ? dsbPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgDesembolso)) {
            return false;
        }
        SgDesembolso other = (SgDesembolso) object;
        if ((this.dsbPk == null && other.dsbPk != null) || (this.dsbPk != null && !this.dsbPk.equals(other.dsbPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgDesembolso[ dsbPk=" + dsbPk + " ]";
    }

}
