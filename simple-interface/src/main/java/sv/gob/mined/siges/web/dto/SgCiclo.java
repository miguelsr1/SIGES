/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */

@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property = "cicPk", scope = SgCiclo.class)
public class SgCiclo implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long cicPk;

    private String cicCodigo;

    private String cicNombre;
    
    private Boolean cicAdmiteModalidad;
    
    private Boolean cicObligatorio;

    private Boolean cicHabilitado;
    
    private Integer cicOrden;
    
    private SgNivel cicNivel;

    private Integer cicVersion;
    
    
    public SgCiclo() {
        this.cicHabilitado = Boolean.TRUE;
        this.cicObligatorio= Boolean.TRUE;
        this.cicAdmiteModalidad= Boolean.TRUE;
    }

    public Long getCicPk() {
        return cicPk;
    }

    public void setCicPk(Long cicPk) {
        this.cicPk = cicPk;
    }

    public String getCicCodigo() {
        return cicCodigo;
    }

    public void setCicCodigo(String cicCodigo) {
        this.cicCodigo = cicCodigo;
    }

    public String getCicNombre() {
        return cicNombre;
    }

    public void setCicNombre(String cicNombre) {
        this.cicNombre = cicNombre;
    }

    public Boolean getCicHabilitado() {
        return cicHabilitado;
    }

    public void setCicHabilitado(Boolean cicHabilitado) {
        this.cicHabilitado = cicHabilitado;
    }

    public Integer getCicVersion() {
        return cicVersion;
    }

    public void setCicVersion(Integer cicVersion) {
        this.cicVersion = cicVersion;
    }

    public Boolean getCicAdmiteModalidad() {
        return cicAdmiteModalidad;
    }

    public void setCicAdmiteModalidad(Boolean cicAdmiteModalidad) {
        this.cicAdmiteModalidad = cicAdmiteModalidad;
    }

    public Boolean getCicObligatorio() {
        return cicObligatorio;
    }

    public void setCicObligatorio(Boolean cicObligatorio) {
        this.cicObligatorio = cicObligatorio;
    }

    public Integer getCicOrden() {
        return cicOrden;
    }

    public void setCicOrden(Integer cicOrden) {
        this.cicOrden = cicOrden;
    }        
   

    public SgNivel getCicNivel() {
        return cicNivel;
    }

    public void setCicNivel(SgNivel cicNivel) {
        this.cicNivel = cicNivel;
    }    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cicPk != null ? cicPk.hashCode() : 0);
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
        final SgCiclo other = (SgCiclo) obj;
        if (!Objects.equals(this.cicPk, other.cicPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgCiclo[ cicPk=" + cicPk + " ]";
    }
    
}
