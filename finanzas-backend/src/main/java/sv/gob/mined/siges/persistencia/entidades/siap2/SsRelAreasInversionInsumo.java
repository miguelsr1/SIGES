/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.siap2;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "ss_rel_areas_inversion_insumo", schema = Constantes.SCHEMA_SIAP2)
@XmlRootElement
@EntityListeners({EntidadListener.class})
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "rinPk", scope = SsRelAreasInversionInsumo.class)
/**
 * Entidad correspondiente a los insumos.
 */
public class SsRelAreasInversionInsumo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long rinPk;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @JoinColumn(name = "id_area_inversion", referencedColumnName = "ai_id")
    @ManyToOne
    private SgAreaInversion rinAreaInversionFk;

    @JoinColumn(name = "id_insumo", referencedColumnName = "ins_id")
    @ManyToOne
    private SgInsumo rinInsumoFk;

    public SsRelAreasInversionInsumo() {

    }

    // <editor-fold defaultstate="collapsed" desc="Getter-Setter">
    public Long getRinPk() {
        return rinPk;
    }

    public void setRinPk(Long rinPk) {
        this.rinPk = rinPk;
    }

    public SgAreaInversion getRinAreaInversionFk() {
        return rinAreaInversionFk;
    }

    public void setRinAreaInversionFk(SgAreaInversion rinAreaInversionFk) {
        this.rinAreaInversionFk = rinAreaInversionFk;
    }

    public SgInsumo getRinInsumoFk() {
        return rinInsumoFk;
    }

    public void setRinInsumoFk(SgInsumo rinInsumoFk) {
        this.rinInsumoFk = rinInsumoFk;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Hash-Equals">
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + Objects.hashCode(this.rinPk);
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
        final SsRelAreasInversionInsumo other = (SsRelAreasInversionInsumo) obj;
        if (!Objects.equals(this.rinPk, other.rinPk)) {
            return false;
        }
        return true;
    }

    // </editor-fold>
}
