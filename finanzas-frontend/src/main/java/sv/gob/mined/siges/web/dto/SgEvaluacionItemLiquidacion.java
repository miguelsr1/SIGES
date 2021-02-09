/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "eilPk", scope = SgEvaluacionItemLiquidacion.class)
public class SgEvaluacionItemLiquidacion implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long eilPk;

    private SgLiquidacion eilLiqFk;
    
    private SgItemLiquidacion eilItemFk;
    
    private Boolean eilMarcado;

    private LocalDateTime eilUltModFecha;

    private String eilUltModUsuario;

    private Integer eilVersion;
    
    private Boolean seleccionado;
    
    public SgEvaluacionItemLiquidacion() {
        
    }

    public Long getEilPk() {
        return eilPk;
    }

    public void setEilPk(Long eilPk) {
        this.eilPk = eilPk;
    }

    public SgLiquidacion getEilLiqFk() {
        return eilLiqFk;
    }

    public void setEilLiqFk(SgLiquidacion eilLiqFk) {
        this.eilLiqFk = eilLiqFk;
    }

    public SgItemLiquidacion getEilItemFk() {
        return eilItemFk;
    }

    public void setEilItemFk(SgItemLiquidacion eilItemFk) {
        this.eilItemFk = eilItemFk;
    }

    public Boolean getEilMarcado() {
        return eilMarcado;
    }

    public void setEilMarcado(Boolean eilMarcado) {
        this.eilMarcado = eilMarcado;
    }


    public LocalDateTime getEilUltModFecha() {
        return eilUltModFecha;
    }

    public void setEilUltModFecha(LocalDateTime eilUltModFecha) {
        this.eilUltModFecha = eilUltModFecha;
    }

    public String getEilUltModUsuario() {
        return eilUltModUsuario;
    }

    public void setEilUltModUsuario(String eilUltModUsuario) {
        this.eilUltModUsuario = eilUltModUsuario;
    }

    public Integer getEilVersion() {
        return eilVersion;
    }

    public void setEilVersion(Integer eilVersion) {
        this.eilVersion = eilVersion;
    }

    public Boolean getSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(Boolean seleccionado) {
        this.seleccionado = seleccionado;
    }

    


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (eilPk != null ? eilPk.hashCode() : 0);
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
        final SgEvaluacionItemLiquidacion other = (SgEvaluacionItemLiquidacion) obj;
        if (!Objects.equals(this.eilPk, other.eilPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgEvaluacionItemLiquidacion[ eilPk=" + eilPk + " ]";
    }
    
}
