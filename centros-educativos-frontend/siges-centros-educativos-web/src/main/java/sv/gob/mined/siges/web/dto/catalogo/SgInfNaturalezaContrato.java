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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "nacPk", scope = SgInfNaturalezaContrato.class)
public class SgInfNaturalezaContrato implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long nacPk;

    private String nacCodigo;

    private String nacNombre;

    private Boolean nacHabilitado;

    private LocalDateTime nacUltModFecha;

    private String nacUltModUsuario;

    private Integer nacVersion;
    
    
    public SgInfNaturalezaContrato() {
        this.nacHabilitado = Boolean.TRUE;
    }

    public Long getNacPk() {
        return nacPk;
    }

    public void setNacPk(Long nacPk) {
        this.nacPk = nacPk;
    }

    public String getNacCodigo() {
        return nacCodigo;
    }

    public void setNacCodigo(String nacCodigo) {
        this.nacCodigo = nacCodigo;
    }

    public String getNacNombre() {
        return nacNombre;
    }

    public void setNacNombre(String nacNombre) {
        this.nacNombre = nacNombre;
    }

    public LocalDateTime getNacUltModFecha() {
        return nacUltModFecha;
    }

    public void setNacUltModFecha(LocalDateTime nacUltModFecha) {
        this.nacUltModFecha = nacUltModFecha;
    }

    public String getNacUltModUsuario() {
        return nacUltModUsuario;
    }

    public void setNacUltModUsuario(String nacUltModUsuario) {
        this.nacUltModUsuario = nacUltModUsuario;
    }

    public Integer getNacVersion() {
        return nacVersion;
    }

    public void setNacVersion(Integer nacVersion) {
        this.nacVersion = nacVersion;
    }

    
     public Boolean getNacHabilitado() {
        return nacHabilitado;
    }

    public void setNacHabilitado(Boolean nacHabilitado) {
        this.nacHabilitado = nacHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nacPk != null ? nacPk.hashCode() : 0);
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
        final SgInfNaturalezaContrato other = (SgInfNaturalezaContrato) obj;
        if (!Objects.equals(this.nacPk, other.nacPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgInfNaturalezaContrato[ nacPk=" + nacPk + " ]";
    }
    
}
