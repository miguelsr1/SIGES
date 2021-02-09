/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,resolver = JsonIdentityResolver.class,  property = "sedPk", scope = SgSede.class)
public class SgCentroEducativoPrivado extends SgCentroEducativo implements Serializable {

    private Boolean priSubvencionada;

    public SgCentroEducativoPrivado() {
        super();
        this.priSubvencionada = Boolean.FALSE;
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
