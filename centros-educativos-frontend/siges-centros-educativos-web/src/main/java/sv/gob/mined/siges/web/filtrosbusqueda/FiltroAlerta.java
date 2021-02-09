package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.util.List;
import sv.gob.mined.siges.web.enumerados.EnumRiesgoAlerta;
import sv.gob.mined.siges.web.enumerados.EnumVariableAlerta;

/**
 *
 * @author Sofis Solutions
 */
public class FiltroAlerta extends FiltroEstudiante implements Serializable {
    
    private List<EnumVariableAlerta> aleVariables;
    private List<EnumRiesgoAlerta> aleRiesgos;
    
    
    public FiltroAlerta() {
    }

    public List<EnumVariableAlerta> getAleVariables() {
        return aleVariables;
    }

    public void setAleVariables(List<EnumVariableAlerta> aleVariables) {
        this.aleVariables = aleVariables;
    }

    public List<EnumRiesgoAlerta> getAleRiesgos() {
        return aleRiesgos;
    }

    public void setAleRiesgos(List<EnumRiesgoAlerta> aleRiesgos) {
        this.aleRiesgos = aleRiesgos;
    }
    
    

}
