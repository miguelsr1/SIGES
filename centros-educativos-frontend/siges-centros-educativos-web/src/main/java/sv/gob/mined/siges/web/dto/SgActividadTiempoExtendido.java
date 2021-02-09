/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "cpePk", scope = SgComponentePlanEstudio.class)
public class SgActividadTiempoExtendido extends SgComponentePlanEstudio implements Serializable {

    public SgActividadTiempoExtendido() {
        super();
    }

    public SgActividadTiempoExtendido(Long cpePk) {
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
        return "sv.gob.mined.siges.persistencia.entidades.SgActividadTiempoExtendido[ cpePk=" + super.getCpePk() + " ]";
    }

}
