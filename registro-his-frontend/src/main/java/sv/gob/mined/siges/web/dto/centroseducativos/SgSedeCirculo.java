/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.centroseducativos;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "sedPk", scope = SgSede.class)
public abstract class SgSedeCirculo extends SgSede implements Serializable {

    private static final long serialVersionUID = 1L;

    private LocalDateTime sedUbAlfFechaFinOp;

    public SgSedeCirculo() {
        super();
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
        return "sv.gob.mined.siges.persistencia.entidades.SgSedeCirculo[ sedPk=" + super.getSedPk() + " ]";
    }

}
