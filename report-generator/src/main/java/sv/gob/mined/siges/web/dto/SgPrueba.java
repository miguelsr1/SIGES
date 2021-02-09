/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "cpePk", scope = SgComponentePlanEstudio.class)
public class SgPrueba extends SgComponentePlanEstudio implements Serializable {

    public SgPrueba() {
        super();
    }

    public SgPrueba(Long cpePk) {
        super(cpePk);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        return super.equals(object);
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgPrueba[ cpePk=" + super.getCpePk() + " ]";
    }

}
