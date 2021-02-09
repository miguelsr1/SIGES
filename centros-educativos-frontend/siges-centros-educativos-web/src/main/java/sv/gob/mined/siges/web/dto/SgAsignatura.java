/*
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
public class SgAsignatura extends SgComponentePlanEstudio implements Serializable {

    private SgAreaAsignaturaModulo asigAreaAsignaturaModulo;

    public SgAsignatura() {
        super();
    }

    public SgAsignatura(Long cpePk) {
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
        return "sv.gob.mined.siges.persistencia.entidades.SgAsignatura[ cpePk=" + super.getCpePk() + " ]";
    }

    public SgAreaAsignaturaModulo getAsigAreaAsignaturaModulo() {
        return asigAreaAsignaturaModulo;
    }

    public void setAsigAreaAsignaturaModulo(SgAreaAsignaturaModulo asigAreaAsignaturaModulo) {
        this.asigAreaAsignaturaModulo = asigAreaAsignaturaModulo;
    }

}
