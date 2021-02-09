/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.catalogo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.seguridad.SgRol;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;

@Entity
@Table(name = "sg_perfiles_roles", schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "uirPk", scope = SgPerfilRoles.class)
@Audited
public class SgPerfilRoles implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "uir_pk", nullable = false)
    private Long uirPk;
    
    @JoinColumn(name = "uir_perfil_fk")
    @ManyToOne
    private SgPerfilesUsuariosIngresadosCe uirPerfil;
    
    @JoinColumn(name = "uir_rol_fk")
    @ManyToOne
    private SgRol uirRol;
    
    @Column(name = "uir_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime uirUltModFecha;  
    
    @Size(max = 45)
    @Column(name = "uir_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String uirUltModUsuario;
        
    @Column(name = "uir_version")
    @Version
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.uirPk);
        hash = 97 * hash + Objects.hashCode(this.uirPerfil);
        hash = 97 * hash + Objects.hashCode(this.uirRol);
        hash = 97 * hash + Objects.hashCode(this.uirUltModFecha);
        hash = 97 * hash + Objects.hashCode(this.uirUltModUsuario);
        hash = 97 * hash + Objects.hashCode(this.uirVersion);
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
        final SgPerfilRoles other = (SgPerfilRoles) obj;
        if (!Objects.equals(this.uirPk, other.uirPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgPerfilRoles{" + "uirPk=" + uirPk + ", uirPerfil=" + uirPerfil + ", uirRol=" + uirRol + ", uirUltModFecha=" + uirUltModFecha + ", uirUltModUsuario=" + uirUltModUsuario + ", uirVersion=" + uirVersion + '}';
    }
    
    
}
