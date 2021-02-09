/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;


@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "uirPk", scope = SgLeySalario.class)
public class SgPerfilRoles implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long uirPk;

    private SgPerfilesUsuariosIngresadosCe uirPerfil;

    private SgRol uirRol;

    private LocalDateTime uirUltModFecha;  

    private String uirUltModUsuario;
        
    private Integer uirVersion;
    
    public Long getUirPk() {
        return uirPk;
    }

    public void setUirPk(Long uirPk) {
        this.uirPk = uirPk;
    }

    public SgPerfilesUsuariosIngresadosCe getUirPerfil() {
        return uirPerfil;
    }

    public void setUirPerfil(SgPerfilesUsuariosIngresadosCe uirPerfil) {
        this.uirPerfil = uirPerfil;
    }

    public SgRol getUirRol() {
        return uirRol;
    }

    public void setUirRol(SgRol uirRol) {
        this.uirRol = uirRol;
    }

    public LocalDateTime getUirUltModFecha() {
        return uirUltModFecha;
    }

    public void setUirUltModFecha(LocalDateTime uirUltModFecha) {
        this.uirUltModFecha = uirUltModFecha;
    }

    public String getUirUltModUsuario() {
        return uirUltModUsuario;
    }

    public void setUirUltModUsuario(String uirUltModUsuario) {
        this.uirUltModUsuario = uirUltModUsuario;
    }

    public Integer getUirVersion() {
        return uirVersion;
    }

    public void setUirVersion(Integer uirVersion) {
        this.uirVersion = uirVersion;
    }
    
    
}
