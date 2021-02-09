/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.centros_educativos;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "peaPk", scope = SgPlanEscolarAnual.class)
public class SgPlanEscolarAnual implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long peaPk;
    
    private SgSede peaSede;
    
    private SgAnioLectivo peaAnioLectivo;

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
