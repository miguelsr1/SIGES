/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_plan_escolar_anual", uniqueConstraints = {
    @UniqueConstraint(name = "pea_sede_propuesta_uk", columnNames = {"pea_sede_fk", "pea_anio_lectivo_fk"})},
        schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "peaPk", scope = SgPlanEscolarAnual.class)
/**
 * Entidad correspondiente al plan escolar anual de los centros educativos.
 */

public class SgPlanEscolarAnual implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pea_pk")
    private Long peaPk;

    @JoinColumn(name = "pea_sede_fk", referencedColumnName = "sed_pk")
    @ManyToOne
    private SgSede peaSede;

    @JoinColumn(name = "pea_anio_lectivo_fk", referencedColumnName = "ale_pk")
    @ManyToOne
    private SgAnioLectivo peaAnioLectivo;

    @OneToMany(mappedBy = "dpePlanEscolarAnual", cascade = CascadeType.ALL)
    private List<SgDetallePlanEscolar> peaDetallePlanEscolar;

    public SgPlanEscolarAnual() {
    }

    public SgPlanEscolarAnual(Long peaPk) {
        this.peaPk = peaPk;
    }

    public Long getPeaPk() {
        return peaPk;
    }

    public void setPeaPk(Long peaPk) {
        this.peaPk = peaPk;
    }

    public SgSede getPeaSede() {
        return peaSede;
    }

    public void setPeaSede(SgSede peaSede) {
        this.peaSede = peaSede;
    }

    public SgAnioLectivo getPeaAnioLectivo() {
        return peaAnioLectivo;
    }

    public void setPeaAnioLectivo(SgAnioLectivo peaAnioLectivo) {
        this.peaAnioLectivo = peaAnioLectivo;
    }

    public List<SgDetallePlanEscolar> getPeaDetallePlanEscolar() {
        return peaDetallePlanEscolar;
    }

    public void setPeaDetallePlanEscolar(List<SgDetallePlanEscolar> peaDetallePlanEscolar) {
        this.peaDetallePlanEscolar = peaDetallePlanEscolar;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (peaPk != null ? peaPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgPlanEscolarAnual)) {
            return false;
        }
        SgPlanEscolarAnual other = (SgPlanEscolarAnual) object;
        if ((this.peaPk == null && other.peaPk != null) || (this.peaPk != null && !this.peaPk.equals(other.peaPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgPlanEscolarAnual[ peaPk=" + peaPk + " ]";
    }

}
