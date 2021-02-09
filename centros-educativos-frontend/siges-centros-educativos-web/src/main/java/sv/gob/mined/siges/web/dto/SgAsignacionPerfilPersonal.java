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
import sv.gob.mined.siges.web.dto.catalogo.SgPerfilesUsuariosIngresadosCe;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "appPk", scope = SgAsignacionPerfilPersonal.class)
public class SgAsignacionPerfilPersonal implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long appPk;
    
    private SgPersonalSedeEducativaNoJsonIdentity appPersonalFk;

    private SgPerfilesUsuariosIngresadosCe appPerfilFk;

    private SgAsignacionPerfil appAsignacionPerfilFk;

    private LocalDateTime appUltModFecha;

    private String appUltModUsuario;

    private Integer appVersion;
    
    
    public SgAsignacionPerfilPersonal() {

    }

    public Long getAppPk() {
        return appPk;
    }

    public void setAppPk(Long appPk) {
        this.appPk = appPk;
    }


    public LocalDateTime getAppUltModFecha() {
        return appUltModFecha;
    }

    public void setAppUltModFecha(LocalDateTime appUltModFecha) {
        this.appUltModFecha = appUltModFecha;
    }

    public String getAppUltModUsuario() {
        return appUltModUsuario;
    }

    public void setAppUltModUsuario(String appUltModUsuario) {
        this.appUltModUsuario = appUltModUsuario;
    }

    public Integer getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(Integer appVersion) {
        this.appVersion = appVersion;
    }

    public SgPersonalSedeEducativaNoJsonIdentity getAppPersonalFk() {
        return appPersonalFk;
    }

    public void setAppPersonalFk(SgPersonalSedeEducativaNoJsonIdentity appPersonalFk) {
        this.appPersonalFk = appPersonalFk;
    }

  

    public SgPerfilesUsuariosIngresadosCe getAppPerfilFk() {
        return appPerfilFk;
    }

    public void setAppPerfilFk(SgPerfilesUsuariosIngresadosCe appPerfilFk) {
        this.appPerfilFk = appPerfilFk;
    }

    public SgAsignacionPerfil getAppAsignacionPerfilFk() {
        return appAsignacionPerfilFk;
    }

    public void setAppAsignacionPerfilFk(SgAsignacionPerfil appAsignacionPerfilFk) {
        this.appAsignacionPerfilFk = appAsignacionPerfilFk;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (appPk != null ? appPk.hashCode() : 0);
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
        final SgAsignacionPerfilPersonal other = (SgAsignacionPerfilPersonal) obj;
        if (!Objects.equals(this.appPk, other.appPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgAsignacionPerfilPersonal{" + "appPk=" + appPk + ", appPersonalFk=" + appPersonalFk + ", appPerfilFk=" + appPerfilFk + ", appAsignacionPerfilFk=" + appAsignacionPerfilFk + ", appUltModFecha=" + appUltModFecha + ", appUltModUsuario=" + appUltModUsuario + ", appVersion=" + appVersion + '}';
    }


}
