/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.centros;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.TipoSede;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

@Entity
@Table(name = "sg_sedes_ced_pri", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@DiscriminatorValue(value = TipoSede.Codes.CENTRO_EDUCATIVO_PRIVADO)
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "sedPk", scope = SgSede.class)
public class SgCentroEducativoPrivado extends SgCentroEducativo implements Serializable {

    @Column(name = "pri_subvencionada")
    private Boolean priSubvencionada;

    public SgCentroEducativoPrivado() {
        super();
    }
    
    public SgCentroEducativoPrivado(Long sedPk) {
        super(sedPk);
    }

    public Boolean getPriSubvencionada() {
        return priSubvencionada;
    }

    public void setPriSubvencionada(Boolean priSubvencionada) {
        this.priSubvencionada = priSubvencionada;
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
        return "sv.gob.mined.siges.persistencia.entidades.SgCentroEducativoPrivado{" + "sedPk=" + this.getSedPk() + '}';
    }  

    

    

}
