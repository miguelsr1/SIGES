/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.TipoComponentePlanEstudio;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

@Entity
@Table(name = "sg_pruebas", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@DiscriminatorValue(value = TipoComponentePlanEstudio.Codes.PRUEBA)
@Audited
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "cpePk", scope = SgComponentePlanEstudio.class)
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
