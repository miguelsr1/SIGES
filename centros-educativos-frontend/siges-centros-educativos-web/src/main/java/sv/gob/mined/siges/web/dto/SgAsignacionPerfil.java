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
import java.util.List;
import java.util.Objects;


/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "apePk", scope = SgAsignacionPerfil.class)
public class SgAsignacionPerfil implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long apePk;

    private SgSede apeSedeFk;

    private List<SgAsignacionPerfilPersonal> apeAsignacionesPerfilPersonal;  

    private LocalDateTime apeUltModFecha;

    private String apeUltModUsuario;

    private Integer apeVersion;
    
    
    public SgAsignacionPerfil() {
    }

    public Long getApePk() {
        return apePk;
    }

    public void setApePk(Long apePk) {
        this.apePk = apePk;
    }

    public SgSede getApeSedeFk() {
        return apeSedeFk;
    }

    public void setApeSedeFk(SgSede apeSedeFk) {
        this.apeSedeFk = apeSedeFk;
    }

    public List<SgAsignacionPerfilPersonal> getApeAsignacionesPerfilPersonal() {
        return apeAsignacionesPerfilPersonal;
    }

    public void setApeAsignacionesPerfilPersonal(List<SgAsignacionPerfilPersonal> apeAsignacionesPerfilPersonal) {
        this.apeAsignacionesPerfilPersonal = apeAsignacionesPerfilPersonal;
    }

  

    public LocalDateTime getApeUltModFecha() {
        return apeUltModFecha;
    }

    public void setApeUltModFecha(LocalDateTime apeUltModFecha) {
        this.apeUltModFecha = apeUltModFecha;
    }

    public String getApeUltModUsuario() {
        return apeUltModUsuario;
    }

    public void setApeUltModUsuario(String apeUltModUsuario) {
        this.apeUltModUsuario = apeUltModUsuario;
    }

    public Integer getApeVersion() {
        return apeVersion;
    }

    public void setApeVersion(Integer apeVersion) {
        this.apeVersion = apeVersion;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (apePk != null ? apePk.hashCode() : 0);
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
        final SgAsignacionPerfil other = (SgAsignacionPerfil) obj;
        if (!Objects.equals(this.apePk, other.apePk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgAsignacionPerfil[ apePk=" + apePk + " ]";
    }
    
}
