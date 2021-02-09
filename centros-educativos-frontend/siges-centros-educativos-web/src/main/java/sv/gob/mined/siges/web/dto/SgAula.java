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

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "aulPk", scope = SgAula.class)
public class SgAula implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long aulPk;

    private String aulCodigo;

    private String aulNombre;
    
    private LocalDateTime aulUltModFecha;
   
    private String aulUltModUsuario;
  
    private Integer aulVersion;    

    
    public SgAula() {
     
    }
    

    public Long getAulPk() {
        return aulPk;
    }

    public void setAulPk(Long aulPk) {
        this.aulPk = aulPk;
    }

    public String getAulCodigo() {
        return aulCodigo;
    }

    public void setAulCodigo(String aulCodigo) {
        this.aulCodigo = aulCodigo;
    }

    public String getAulNombre() {
        return aulNombre;
    }

    public void setAulNombre(String aulNombre) {
        this.aulNombre = aulNombre;
    }

    public LocalDateTime getAulUltModFecha() {
        return aulUltModFecha;
    }

    public void setAulUltModFecha(LocalDateTime aulUltModFecha) {
        this.aulUltModFecha = aulUltModFecha;
    }

    public String getAulUltModUsuario() {
        return aulUltModUsuario;
    }

    public void setAulUltModUsuario(String aulUltModUsuario) {
        this.aulUltModUsuario = aulUltModUsuario;
    }

    public Integer getAulVersion() {
        return aulVersion;
    }

    public void setAulVersion(Integer aulVersion) {
        this.aulVersion = aulVersion;
    }

  
    
   


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (aulPk != null ? aulPk.hashCode() : 0);
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
        final SgAula other = (SgAula) obj;
        if (!Objects.equals(this.aulPk, other.aulPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgAula[ aulPk=" + aulPk + " ]";
    }
    
}
