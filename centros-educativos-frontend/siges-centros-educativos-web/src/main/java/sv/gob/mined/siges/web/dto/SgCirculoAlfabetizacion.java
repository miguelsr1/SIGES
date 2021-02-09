/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class,  property = "sedPk", scope = SgSede.class)
public class SgCirculoAlfabetizacion extends SgSedeCirculo implements Serializable {

    public SgCirculoAlfabetizacion() {
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
        return "sv.gob.mined.siges.persistencia.entidades.SgCirculoAlfabetizacion[ sedPk=" + super.getSedPk() + " ]";
    }

}
