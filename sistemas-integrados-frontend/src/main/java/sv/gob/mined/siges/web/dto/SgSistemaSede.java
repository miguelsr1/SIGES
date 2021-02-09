
package sv.gob.mined.siges.web.dto;

import java.io.Serializable;
import sv.gob.mined.siges.web.dto.centros_educativos.SgSede;

/**
 *
 * @author Sofis Solutions
 */
public class SgSistemaSede implements Serializable {
    
    private SgSistemaIntegrado sinPk;
    
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
    
}
