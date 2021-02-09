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
import sv.gob.mined.siges.web.dto.siap2.SsCategoriaPresupuestoEscolar;
import sv.gob.mined.siges.web.dto.siap2.SsGesPresEs;
import sv.gob.mined.siges.web.enumerados.EnumEstadoLiquidacion;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "liqPk", scope = SgLiquidacion.class)
public class SgLiquidacion implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long liqPk;

    private SsCategoriaPresupuestoEscolar liqComponentePk;
    
    private SsGesPresEs liqSubComponenteFk;

    private SgSede liqSedePk;

    private SgAnioLectivo liqAnioPk;
    
    private EnumEstadoLiquidacion liqEstado;
    
    private SgEvaluacionLiquidacion evaluacionLiquidacion;

    private LocalDateTime liqUltModFecha;

    private String liqUltModUsuario;

    private Integer liqVersion;
    
    
    public SgLiquidacion() {
        
    }

    public Long getLiqPk() {
        return liqPk;
    }

    public void setLiqPk(Long liqPk) {
        this.liqPk = liqPk;
    }

    public SsCategoriaPresupuestoEscolar getLiqComponentePk() {
        return liqComponentePk;
    }

    public void setLiqComponentePk(SsCategoriaPresupuestoEscolar liqComponentePk) {
        this.liqComponentePk = liqComponentePk;
    }

    public SgSede getLiqSedePk() {
        return liqSedePk;
    }

    public void setLiqSedePk(SgSede liqSedePk) {
        this.liqSedePk = liqSedePk;
    }

    public SgAnioLectivo getLiqAnioPk() {
        return liqAnioPk;
    }

    public void setLiqAnioPk(SgAnioLectivo liqAnioPk) {
        this.liqAnioPk = liqAnioPk;
    }

    public EnumEstadoLiquidacion getLiqEstado() {
        return liqEstado;
    }

    public void setLiqEstado(EnumEstadoLiquidacion liqEstado) {
        this.liqEstado = liqEstado;
    }

    public SsGesPresEs getLiqSubComponenteFk() {
        return liqSubComponenteFk;
    }

    public void setLiqSubComponenteFk(SsGesPresEs liqSubComponenteFk) {
        this.liqSubComponenteFk = liqSubComponenteFk;
    }

    public SgEvaluacionLiquidacion getEvaluacionLiquidacion() {
        return evaluacionLiquidacion;
    }

    public void setEvaluacionLiquidacion(SgEvaluacionLiquidacion evaluacionLiquidacion) {
        this.evaluacionLiquidacion = evaluacionLiquidacion;
    }
       
    public LocalDateTime getLiqUltModFecha() {
        return liqUltModFecha;
    }

    public void setLiqUltModFecha(LocalDateTime liqUltModFecha) {
        this.liqUltModFecha = liqUltModFecha;
    }

    public String getLiqUltModUsuario() {
        return liqUltModUsuario;
    }

    public void setLiqUltModUsuario(String liqUltModUsuario) {
        this.liqUltModUsuario = liqUltModUsuario;
    }

    public Integer getLiqVersion() {
        return liqVersion;
    }

    public void setLiqVersion(Integer liqVersion) {
        this.liqVersion = liqVersion;
    }

   


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (liqPk != null ? liqPk.hashCode() : 0);
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
        final SgLiquidacion other = (SgLiquidacion) obj;
        if (!Objects.equals(this.liqPk, other.liqPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgLiquidacion[ liqPk=" + liqPk + " ]";
    }
    
}
