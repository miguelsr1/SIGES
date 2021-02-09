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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.TipoComponentePlanEstudio;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAreaAsignaturaModulo;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;


@Entity
@Table(name = "sg_asignaturas", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@DiscriminatorValue(value = TipoComponentePlanEstudio.Codes.ASIGNATURA)
@Audited
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "cpePk", scope = SgComponentePlanEstudio.class)
public class SgAsignatura extends SgComponentePlanEstudio implements Serializable {

    @JoinColumn(name = "asig_area_asignatura_modulo_fk")
    @ManyToOne
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
