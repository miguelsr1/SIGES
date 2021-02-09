/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.siap2;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "ss_insumo", schema = Constantes.SCHEMA_SIAP2)
@XmlRootElement
@EntityListeners({EntidadListener.class})
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "insPk", scope = SgInsumo.class)
/**
 * Entidad correspondiente a los insumos.
 */
public class SgInsumo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ins_id", nullable = false)
    private Long insPk;

    @Size(max = 45)
    @Column(name = "ins_cod", length = 45)
    private String insCodigo;

    @Size(max = 45)
    @Column(name = "ins_descr", length = 45)
    private String insDescr;

    @Size(max = 45)
    @AtributoNombre
    @Column(name = "ins_nombre", length = 45)
    private String insNombre;

    @Column(name = "ins_aplica_a_centros_educativos")
    private Boolean ins_Ce;

    @OneToMany(mappedBy = "rinInsumoFk", fetch = FetchType.LAZY)
    private List<SsRelAreasInversionInsumo> insAreaRel;

    @AtributoUltimaModificacion
    @Column(name = "ins_ult_mod")
    private LocalDateTime insUltmodFecha;

    @Size(max = 45)
    @AtributoUltimoUsuario
    @Column(name = "ins_ult_usuario", length = 45)
    private String insUltmodUsuario;

    @Column(name = "ins_version")
    @Version
    private Integer insVersion;

    public SgInsumo() {

    }

    public Long getInsPk() {
        return insPk;
    }

    public void setInsPk(Long insPk) {
        this.insPk = insPk;
    }

    public String getInsCodigo() {
        return insCodigo;
    }

    public void setInsCodigo(String insCodigo) {
        this.insCodigo = insCodigo;
    }

    public String getInsDescr() {
        return insDescr;
    }

    public void setInsDescr(String insDescr) {
        this.insDescr = insDescr;
    }

    public String getInsNombre() {
        return insNombre;
    }

    public void setInsNombre(String insNombre) {
        this.insNombre = insNombre;
    }

    public Boolean getIns_Ce() {
        return ins_Ce;
    }

    public void setIns_Ce(Boolean ins_Ce) {
        this.ins_Ce = ins_Ce;
    }

    public LocalDateTime getInsUltmodFecha() {
        return insUltmodFecha;
    }

    public void setInsUltmodFecha(LocalDateTime insUltmodFecha) {
        this.insUltmodFecha = insUltmodFecha;
    }

    public String getInsUltmodUsuario() {
        return insUltmodUsuario;
    }

    public List<SsRelAreasInversionInsumo> getInsAreaRel() {
        return insAreaRel;
    }

    public void setInsAreaRel(List<SsRelAreasInversionInsumo> insAreaRel) {
        this.insAreaRel = insAreaRel;
    }

    public void setInsUltmodUsuario(String insUltmodUsuario) {
        this.insUltmodUsuario = insUltmodUsuario;
    }

    public Integer getInsVersion() {
        return insVersion;
    }

    // <editor-fold defaultstate="collapsed" desc="Getter-Setter">
    public void setInsVersion(Integer insVersion) {
        this.insVersion = insVersion;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Hash-Equals">
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.insPk);
        hash = 41 * hash + Objects.hashCode(this.insCodigo);
        hash = 41 * hash + Objects.hashCode(this.insDescr);
        hash = 41 * hash + Objects.hashCode(this.insNombre);
        hash = 41 * hash + Objects.hashCode(this.ins_Ce);
        hash = 41 * hash + Objects.hashCode(this.insUltmodFecha);
        hash = 41 * hash + Objects.hashCode(this.insUltmodUsuario);
        hash = 41 * hash + Objects.hashCode(this.insVersion);
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
        final SgInsumo other = (SgInsumo) obj;
        if (!Objects.equals(this.insPk, other.insPk)) {
            return false;
        }
        return true;
    }
    // </editor-fold>
}
