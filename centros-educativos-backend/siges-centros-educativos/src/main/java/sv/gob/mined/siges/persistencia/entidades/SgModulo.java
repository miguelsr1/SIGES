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
@Table(name = "sg_modulos", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@DiscriminatorValue(value = TipoComponentePlanEstudio.Codes.MODULO)
@Audited
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "cpePk", scope = SgComponentePlanEstudio.class)
public class SgModulo extends SgComponentePlanEstudio implements Serializable {

    @JoinColumn(name = "mod_area_asignatura_modulo_fk")
    @ManyToOne
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
