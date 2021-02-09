/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgPerfilesUsuariosIngresadosCe;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_asignacion_perfil_personal", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "appPk", scope = SgAsignacionPerfilPersonal.class)
@Audited
public class SgAsignacionPerfilPersonal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "app_pk", nullable = false)
    private Long appPk;

    @JoinColumn(name = "app_personal_fk", referencedColumnName = "pse_pk")
    @ManyToOne(fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgPersonalSedeEducativaLite appPersonalFk;
    
    @JoinColumn(name = "app_perfil_fk", referencedColumnName = "pui_pk")
    @ManyToOne(fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgPerfilesUsuariosIngresadosCe appPerfilFk;
    
    @JoinColumn(name = "app_asignacion_perfil_fk", referencedColumnName = "ape_pk")
    @ManyToOne
    private SgAsignacionPerfil appAsignacionPerfilFk;
    
    @Column(name = "app_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime appUltModFecha;

    @Size(max = 45)
    @Column(name = "app_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String appUltModUsuario;

    @Column(name = "app_version")
    @Version
    private Integer appVersion;

    public SgAsignacionPerfilPersonal() {
    }


    public Long getAppPk() {
        return appPk;
    }

    public void setAppPk(Long appPk) {
        this.appPk = appPk;
    }

    public SgPersonalSedeEducativaLite getAppPersonalFk() {
        return appPersonalFk;
    }

    public void setAppPersonalFk(SgPersonalSedeEducativaLite appPersonalFk) {
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
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.appPk);
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
