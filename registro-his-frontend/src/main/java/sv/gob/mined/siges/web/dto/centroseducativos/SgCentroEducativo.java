/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.centroseducativos;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "sedPk", scope = SgSede.class)
public abstract class SgCentroEducativo extends SgSede implements Serializable {

    private static final long serialVersionUID = 1L;

    private Boolean cedLegalizado;

    private Boolean cedFinesDeLucro;


    public SgCentroEducativo() {
        super();
        this.cedLegalizado = Boolean.FALSE;
    }

    public Boolean getCedLegalizado() {
        return cedLegalizado;
    }

    public void setCedLegalizado(Boolean cedLegalizado) {
        this.cedLegalizado = cedLegalizado;
    }

    public Boolean getCedFinesDeLucro() {
        return cedFinesDeLucro;
    }

    public void setCedFinesDeLucro(Boolean cedFinesDeLucro) {
        this.cedFinesDeLucro = cedFinesDeLucro;
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
        return "sv.gob.mined.siges.persistencia.entidades.SgCentroEducativo{" + "sedPk=" + this.getSedPk() + '}';
    }

}
