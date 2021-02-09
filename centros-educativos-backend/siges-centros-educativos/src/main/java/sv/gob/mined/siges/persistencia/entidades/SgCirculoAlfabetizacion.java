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
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

@Entity
@Table(name = "sg_sedes_circulos_alfa", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@DiscriminatorValue(value = "CIR_ALF")
@Audited
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "sedPk", scope = SgSede.class)
public class SgCirculoAlfabetizacion extends SgSedeCirculo implements Serializable {

    public SgCirculoAlfabetizacion() {
        super();
    }

    public SgCirculoAlfabetizacion(Long sedPk) {
        super(sedPk);
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
        return "sv.gob.mined.siges.persistencia.entidades.SgCirculoAlfabetizacion{" + super.getSedPk() + '}';
    }

    

}
