/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import sv.gob.mined.siges.web.dto.catalogo.SgAreaAsignaturaModulo;

/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "cpePk", scope = SgComponentePlanEstudio.class)
public class SgModulo extends SgComponentePlanEstudio implements Serializable {

    private SgAreaAsignaturaModulo modAreaAsignaturaModulo;

    public SgModulo() {
        super();
    }

    public SgModulo(Long cpePk) {
        super(cpePk);
    }

    public SgAreaAsignaturaModulo getModAreaAsignaturaModulo() {
        return modAreaAsignaturaModulo;
    }

    public void setModAreaAsignaturaModulo(SgAreaAsignaturaModulo modAreaAsignaturaModulo) {
        this.modAreaAsignaturaModulo = modAreaAsignaturaModulo;
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
        return "sv.gob.mined.siges.persistencia.entidades.SgModulo[ cpePk=" + super.getCpePk() + " ]";
    }

}
