/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.centroseducativos;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "sedPk", scope = SgSede.class)
public class SgCentroEducativoOficial extends SgCentroEducativo implements Serializable {

    public SgCentroEducativoOficial() {
        super();
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
        return "sv.gob.mined.siges.persistencia.entidades.SgCentroEducativoOficial{" + "sedPk=" + this.getSedPk() + '}';
    }

}
