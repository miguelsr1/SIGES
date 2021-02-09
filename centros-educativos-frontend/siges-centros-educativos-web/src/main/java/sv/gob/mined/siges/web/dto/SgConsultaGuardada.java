/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import sv.gob.mined.siges.web.enumerados.EnumConsultaGuardada;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "cgrPk", scope = SgConsultaGuardada.class)
public class SgConsultaGuardada implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long cgrPk;

    private String cgrNombre;

    private Boolean cgrHabilitado;   
        
    private String cgrConsulta;    
         
    private String cgrUsuario;
        
    private EnumConsultaGuardada cgrFiltro;

    private LocalDateTime cgrUltModFecha;

    private String cgrUltModUsuario;

    private Integer cgrVersion;
    
    
    public SgConsultaGuardada() {
        this.cgrHabilitado = Boolean.TRUE;
    }

    public Long getCgrPk() {
        return cgrPk;
    }

    public void setCgrPk(Long cgrPk) {
        this.cgrPk = cgrPk;
    }

    public String getCgrConsulta() {
        return cgrConsulta;
    }

    public void setCgrConsulta(String cgrConsulta) {
        this.cgrConsulta = cgrConsulta;
    }

    public String getCgrUsuario() {
        return cgrUsuario;
    }

    public void setCgrUsuario(String cgrUsuario) {
        this.cgrUsuario = cgrUsuario;
    }

    public EnumConsultaGuardada getCgrFiltro() {
        return cgrFiltro;
    }

    public void setCgrFiltro(EnumConsultaGuardada cgrFiltro) {
        this.cgrFiltro = cgrFiltro;
    }

    
    public String getCgrNombre() {
        return cgrNombre;
    }

    public void setCgrNombre(String cgrNombre) {
        this.cgrNombre = cgrNombre;
    }

    public LocalDateTime getCgrUltModFecha() {
        return cgrUltModFecha;
    }

    public void setCgrUltModFecha(LocalDateTime cgrUltModFecha) {
        this.cgrUltModFecha = cgrUltModFecha;
    }

    public String getCgrUltModUsuario() {
        return cgrUltModUsuario;
    }

    public void setCgrUltModUsuario(String cgrUltModUsuario) {
        this.cgrUltModUsuario = cgrUltModUsuario;
    }

    public Integer getCgrVersion() {
        return cgrVersion;
    }

    public void setCgrVersion(Integer cgrVersion) {
        this.cgrVersion = cgrVersion;
    }

    
     public Boolean getCgrHabilitado() {
        return cgrHabilitado;
    }

    public void setCgrHabilitado(Boolean cgrHabilitado) {
        this.cgrHabilitado = cgrHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cgrPk != null ? cgrPk.hashCode() : 0);
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
        final SgConsultaGuardada other = (SgConsultaGuardada) obj;
        if (!Objects.equals(this.cgrPk, other.cgrPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgConsultaGuardada[ cgrPk=" + cgrPk + " ]";
    }
    
}
