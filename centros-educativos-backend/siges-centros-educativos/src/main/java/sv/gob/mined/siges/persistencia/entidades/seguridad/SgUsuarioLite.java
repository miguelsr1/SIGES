/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.seguridad;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

@Entity
@Table(name = "sg_usuarios", uniqueConstraints = {
    @UniqueConstraint(name = "usu_codigo_uk", columnNames = {"usu_codigo"})},
        schema = Constantes.SCHEMA_SEGURIDAD)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "usuPk", scope = SgUsuarioLite.class)
@Audited
public class SgUsuarioLite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "usu_pk")
    private Long usuPk;

    @Size(max = 50)
    @Column(name = "usu_codigo", length = 50)
    @AtributoCodigo
    private String usuCodigo;

    @Size(max = 255)
    @Column(name = "usu_nombre", length = 255)
    @AtributoNombre
    private String usuNombre;

    @Size(max = 255)
    @Column(name = "usu_email", length = 255)
    private String usuEmail;

    @Size(max = 120)
    @Column(name = "usu_password", length = 120)
    private String usuPassword;

    @Column(name = "usu_habilitado")
    @AtributoHabilitado
    private Boolean usuHabilitado;

    @Column(name = "usu_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime usuUltModFecha;

    @Size(max = 45)
    @Column(name = "usu_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String usuUltModUsuario;

    @Column(name = "usu_version")
    @Version
    private Integer usuVersion;
    
    @Column(name = "usu_acepta_terminos")
    private Boolean usuAceptaTerminos;
    
    @Column(name = "usu_super_usuario")
    private Boolean usuSuperUsuario;
    
    @Column(name = "usu_persona_pk")
    private Long usuPersonaPk;
    
    @Column(name = "usu_personal_pk", insertable = false, updatable = false)
    private Long usuPersonalPk; 

    public SgUsuarioLite() {
    }
    
    public Long getUsuPk() {
        return usuPk;
    }

    public void setUsuPk(Long usuPk) {
        this.usuPk = usuPk;
    }

    public String getUsuCodigo() {
        return usuCodigo;
    }

    public void setUsuCodigo(String usuCodigo) {
        this.usuCodigo = usuCodigo;
    }

    public String getUsuNombre() {
        return usuNombre;
    }

    public void setUsuNombre(String usuNombre) {
        this.usuNombre = usuNombre;
    }

    public Boolean getUsuHabilitado() {
        return usuHabilitado;
    }

    public void setUsuHabilitado(Boolean usuHabilitado) {
        this.usuHabilitado = usuHabilitado;
    }

    public LocalDateTime getUsuUltModFecha() {
        return usuUltModFecha;
    }

    public void setUsuUltModFecha(LocalDateTime usuUltModFecha) {
        this.usuUltModFecha = usuUltModFecha;
    }

    public String getUsuUltModUsuario() {
        return usuUltModUsuario;
    }

    public void setUsuUltModUsuario(String usuUltModUsuario) {
        this.usuUltModUsuario = usuUltModUsuario;
    }

    public Integer getUsuVersion() {
        return usuVersion;
    }

    public void setUsuVersion(Integer usuVersion) {
        this.usuVersion = usuVersion;
    }

    public String getUsuPassword() {
        return usuPassword;
    }

    public void setUsuPassword(String usuPassword) {
        this.usuPassword = usuPassword;
    }

    public String getUsuEmail() {
        return usuEmail;
    }

    public void setUsuEmail(String usuEmail) {
        this.usuEmail = usuEmail;
    }

    public Boolean getUsuAceptaTerminos() {
        return usuAceptaTerminos;
    }

    public void setUsuAceptaTerminos(Boolean usuAceptaTerminos) {
        this.usuAceptaTerminos = usuAceptaTerminos;
    }

    public Boolean getUsuSuperUsuario() {
        return usuSuperUsuario;
    }

    public void setUsuSuperUsuario(Boolean usuSuperUsuario) {
        this.usuSuperUsuario = usuSuperUsuario;
    }

    public Long getUsuPersonaPk() {
        return usuPersonaPk;
    }

    public void setUsuPersonaPk(Long usuPersonaPk) {
        this.usuPersonaPk = usuPersonaPk;
    }

    public Long getUsuPersonalPk() {
        return usuPersonalPk;
    }

    public void setUsuPersonalPk(Long usuPersonalPk) {
        this.usuPersonalPk = usuPersonalPk;
    }
    
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usuPk != null ? usuPk.hashCode() : 0);
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
        final SgUsuarioLite other = (SgUsuarioLite) obj;
        if (!Objects.equals(this.usuPk, other.usuPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgUsuario[ usuPk=" + usuPk + " ]";
    }

}
