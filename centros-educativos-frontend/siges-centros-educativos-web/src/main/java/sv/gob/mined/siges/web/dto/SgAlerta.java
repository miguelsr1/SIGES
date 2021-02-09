/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.time.LocalDateTime;
import java.util.Objects;
import sv.gob.mined.siges.web.enumerados.EnumRiesgoAlerta;
import sv.gob.mined.siges.web.enumerados.EnumVariableAlerta;

/**
 *
 * @author Sofis Solutions
 */

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "alePk", scope = SgAlerta.class)
public class SgAlerta implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long alePk;

    private EnumVariableAlerta aleVariable;

    private EnumRiesgoAlerta aleRiesgo;

    private LocalDateTime aleUltModFecha;
    
    private SgEstudiante aleEstudiante;


    public SgAlerta() {
    }

    public Long getAlePk() {
        return alePk;
    }

    public void setAlePk(Long alePk) {
        this.alePk = alePk;
    }

    public EnumVariableAlerta getAleVariable() {
        return aleVariable;
    }

    public void setAleVariable(EnumVariableAlerta aleVariable) {
        this.aleVariable = aleVariable;
    }

    public EnumRiesgoAlerta getAleRiesgo() {
        return aleRiesgo;
    }

    public void setAleRiesgo(EnumRiesgoAlerta aleRiesgo) {
        this.aleRiesgo = aleRiesgo;
    }

    public LocalDateTime getAleUltModFecha() {
        return aleUltModFecha;
    }

    public void setAleUltModFecha(LocalDateTime aleUltModFecha) {
        this.aleUltModFecha = aleUltModFecha;
    }

    public SgEstudiante getAleEstudiante() {
        return aleEstudiante;
    }

    public void setAleEstudiante(SgEstudiante aleEstudiante) {
        this.aleEstudiante = aleEstudiante;
    }
    
   
    @Override
    public int hashCode() {
        return Objects.hashCode(this.alePk);
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
        final SgAlerta other = (SgAlerta) obj;
        if (!Objects.equals(this.alePk, other.alePk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgEstAlerta{" + "alePk=" + alePk + '}';
    }

    

}
