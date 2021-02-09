/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

@Entity
@Table(name = "sg_sedes_circulos", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@Audited
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "sedPk", scope = SgSede.class)
public class SgSedeCirculo extends SgSede implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "sed_ub_alf_fecha_fin_op")
    private LocalDateTime sedUbAlfFechaFinOp;
    
   

    public SgSedeCirculo() {
        super();
    }

    public SgSedeCirculo(Long sedPk) {
        super(sedPk);
    }

    public LocalDateTime getSedUbAlfFechaFinOp() {
        return sedUbAlfFechaFinOp;
    }

    public void setSedUbAlfFechaFinOp(LocalDateTime sedUbAlfFechaFinOp) {
        this.sedUbAlfFechaFinOp = sedUbAlfFechaFinOp;
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
        return "siges.jpa.entities.SigesSedeCirculo[ sedPk=" + super.getSedPk() + " ]";
    }

}
