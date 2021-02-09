/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.finanzas;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import sv.gob.mined.siges.web.dto.SgAnioLectivo;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.enumerados.EnumEstadoLiquidacion;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "loiPk", scope = SgLiquidacionOtroIngreso.class)
public class SgLiquidacionOtroIngreso implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long loiPk;

    private SgSede loiSedePk;

    private SgAnioLectivo loiAnioPk;

    private EnumEstadoLiquidacion loiEstado;

    private LocalDateTime loiUltModFecha;

    private String loiUltModUsuario;

    private Integer loiVersion;
    
    
    public SgLiquidacionOtroIngreso() {
        
    }

    public Long getLoiPk() {
        return loiPk;
    }

    public void setLoiPk(Long loiPk) {
        this.loiPk = loiPk;
    }

    public SgSede getLoiSedePk() {
        return loiSedePk;
    }

    public void setLoiSedePk(SgSede loiSedePk) {
        this.loiSedePk = loiSedePk;
    }

    public SgAnioLectivo getLoiAnioPk() {
        return loiAnioPk;
    }

    public void setLoiAnioPk(SgAnioLectivo loiAnioPk) {
        this.loiAnioPk = loiAnioPk;
    }

    public EnumEstadoLiquidacion getLoiEstado() {
        return loiEstado;
    }

    public void setLoiEstado(EnumEstadoLiquidacion loiEstado) {
        this.loiEstado = loiEstado;
    }


    public LocalDateTime getLoiUltModFecha() {
        return loiUltModFecha;
    }

    public void setLoiUltModFecha(LocalDateTime loiUltModFecha) {
        this.loiUltModFecha = loiUltModFecha;
    }

    public String getLoiUltModUsuario() {
        return loiUltModUsuario;
    }

    public void setLoiUltModUsuario(String loiUltModUsuario) {
        this.loiUltModUsuario = loiUltModUsuario;
    }

    public Integer getLoiVersion() {
        return loiVersion;
    }

    public void setLoiVersion(Integer loiVersion) {
        this.loiVersion = loiVersion;
    }

    


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (loiPk != null ? loiPk.hashCode() : 0);
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
        final SgLiquidacionOtroIngreso other = (SgLiquidacionOtroIngreso) obj;
        if (!Objects.equals(this.loiPk, other.loiPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgLiquidacionOtroIngreso[ loiPk=" + loiPk + " ]";
    }
    
}
