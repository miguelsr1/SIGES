/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.centros_educativos;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;

@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property = "psePk", scope = SgAtpi.class)
public class SgAtpi extends SgPersonalSedeEducativa implements Serializable {

    public SgAtpi() {
        super();
    }

    public SgAtpi(Long psePk) {
            super(psePk);
    }   

    @Override
    public String toString() {
        return "SgAtpi{" + '}';
    }
    
}
