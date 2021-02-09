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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "puiPk", scope = SgPerfilesUsuariosIngresadosCe.class)
public class SgPerfilesUsuariosIngresadosCe implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long puiPk;

    private String puiCodigo;

    private String puiNombre;

    private Boolean puiHabilitado;
    
    private List<SgPerfilRoles> puiPerfilRoles;

    private LocalDateTime puiUltModFecha;

    private String puiUltModUsuario;

    private Integer puiVersion;
    
    
    public SgPerfilesUsuariosIngresadosCe() {
        this.puiHabilitado = Boolean.TRUE;
        this.puiPerfilRoles = new ArrayList<>();
    }

    public Long getPuiPk() {
        return puiPk;
    }

    public void setPuiPk(Long puiPk) {
        this.puiPk = puiPk;
    }

    public String getPuiCodigo() {
        return puiCodigo;
    }

    public void setPuiCodigo(String puiCodigo) {
        this.puiCodigo = puiCodigo;
    }

    public String getPuiNombre() {
        return puiNombre;
    }

    public void setPuiNombre(String puiNombre) {
        this.puiNombre = puiNombre;
    }

    public LocalDateTime getPuiUltModFecha() {
        return puiUltModFecha;
    }

    public void setPuiUltModFecha(LocalDateTime puiUltModFecha) {
        this.puiUltModFecha = puiUltModFecha;
    }

    public String getPuiUltModUsuario() {
        return puiUltModUsuario;
    }

    public void setPuiUltModUsuario(String puiUltModUsuario) {
        this.puiUltModUsuario = puiUltModUsuario;
    }

    public Integer getPuiVersion() {
        return puiVersion;
    }

    public void setPuiVersion(Integer puiVersion) {
        this.puiVersion = puiVersion;
    }

    
     public Boolean getPuiHabilitado() {
        return puiHabilitado;
    }

    public void setPuiHabilitado(Boolean puiHabilitado) {
        this.puiHabilitado = puiHabilitado;
    }

    public List<SgPerfilRoles> getPuiPerfilRoles() {
        return puiPerfilRoles;
    }

    public void setPuiPerfilRoles(List<SgPerfilRoles> puiPerfilRoles) {
        this.puiPerfilRoles = puiPerfilRoles;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (puiPk != null ? puiPk.hashCode() : 0);
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
        final SgPerfilesUsuariosIngresadosCe other = (SgPerfilesUsuariosIngresadosCe) obj;
        if (!Objects.equals(this.puiPk, other.puiPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgPerfilesUsuariosIngresadosCe[ puiPk=" + puiPk + " ]";
    }
    
}
