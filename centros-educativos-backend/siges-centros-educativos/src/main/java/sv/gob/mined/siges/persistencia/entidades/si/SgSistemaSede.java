
package sv.gob.mined.siges.persistencia.entidades.si;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.entidades.SgSede;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_sistemas_sedes", schema = Constantes.SCHEMA_SISTEMAS_INTEGRADOS)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "sinPk", scope = SgSistemaIntegrado.class)
public class SgSistemaSede implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @JoinColumn(name = "sin_pk", referencedColumnName = "sin_pk")
    @ManyToOne
    private SgSistemaIntegrado sinPk;
    
    @Id
    @JoinColumn(name = "sed_pk", referencedColumnName = "sed_pk")
    @ManyToOne
    private SgSede sedPk;
    
    public SgSistemaSede(){
        
    }

    public SgSistemaIntegrado getSinPk() {
        return sinPk;
    }

    public void setSinPk(SgSistemaIntegrado sinPk) {
        this.sinPk = sinPk;
    }

    public SgSede getSedPk() {
        return sedPk;
    }

    public void setSedPk(SgSede sedPk) {
        this.sedPk = sedPk;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.sinPk);
        hash = 79 * hash + Objects.hashCode(this.sedPk);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SgSistemaSede other = (SgSistemaSede) obj;
        if (!Objects.equals(this.sinPk, other.sinPk)) {
            return false;
        }
        if (!Objects.equals(this.sedPk, other.sedPk)) {
            return false;
        }
        return true;
    }
    
    
    
}
