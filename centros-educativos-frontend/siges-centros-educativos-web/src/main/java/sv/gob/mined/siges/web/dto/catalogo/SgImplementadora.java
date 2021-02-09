/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "impPk", scope = SgImplementadora.class)
public class SgImplementadora implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long impPk;

    private String impCodigo;

    private String impNombre;

    private Boolean impHabilitado;

    private LocalDateTime impUltModFecha;

    private String impUltModUsuario;

    private Integer impVersion;
            
    private Integer impOrden;
    
    
    public SgImplementadora() {
        this.impHabilitado = Boolean.TRUE;
    }

    public Long getImpPk() {
        return impPk;
    }

    public void setImpPk(Long impPk) {
        this.impPk = impPk;
    }

    public String getImpCodigo() {
        return impCodigo;
    }

    public void setImpCodigo(String impCodigo) {
        this.impCodigo = impCodigo;
    }

    public String getImpNombre() {
        return impNombre;
    }

    public void setImpNombre(String impNombre) {
        this.impNombre = impNombre;
    }

    public LocalDateTime getImpUltModFecha() {
        return impUltModFecha;
    }

    public void setImpUltModFecha(LocalDateTime impUltModFecha) {
        this.impUltModFecha = impUltModFecha;
    }

    public String getImpUltModUsuario() {
        return impUltModUsuario;
    }

    public void setImpUltModUsuario(String impUltModUsuario) {
        this.impUltModUsuario = impUltModUsuario;
    }

    public Integer getImpVersion() {
        return impVersion;
    }

    public void setImpVersion(Integer impVersion) {
        this.impVersion = impVersion;
    }

    
     public Boolean getImpHabilitado() {
        return impHabilitado;
    }

    public void setImpHabilitado(Boolean impHabilitado) {
        this.impHabilitado = impHabilitado;
    }

    public Integer getImpOrden() {
        return impOrden;
    }

    public void setImpOrden(Integer impOrden) {
        this.impOrden = impOrden;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (impPk != null ? impPk.hashCode() : 0);
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
        final SgImplementadora other = (SgImplementadora) obj;
        if (!Objects.equals(this.impPk, other.impPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgImplementadoras[ impPk=" + impPk + " ]";
    }
    
}
