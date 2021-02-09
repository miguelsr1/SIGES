/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.centros_educativos;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "psePk", scope = SgAdministrativo.class)
public class SgAdministrativo extends SgPersonalSedeEducativa implements Serializable {

    public SgAdministrativo() {
        super();
    }


    public SgAdministrativo(Long psePk) {
            super(psePk);
    }    

    @Override
    public String toString() {
        return "SgAdministrativo{" + '}';
    }    
}
