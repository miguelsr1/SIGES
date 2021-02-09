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
import javax.persistence.UniqueConstraint;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoInicializarColeccion;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_perfiles_usuarios_ingresados_ce", uniqueConstraints = {
    @UniqueConstraint(name = "pui_codigo_uk", columnNames = {"pui_codigo"})
    ,
    @UniqueConstraint(name = "pui_nombre_uk", columnNames = {"pui_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "puiPk", scope = SgPerfilesUsuariosIngresadosCe.class)
@Audited
public class SgPerfilesUsuariosIngresadosCe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pui_pk", nullable = false)
    private Long puiPk;

    @Size(max = 45)
    @Column(name = "pui_codigo", length = 45)
    @AtributoCodigo
    private String puiCodigo;

    @Size(max = 255)
    @Column(name = "pui_nombre", length = 255)
    @AtributoNormalizable
    private String puiNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "pui_nombre_busqueda", length = 255)
    private String puiNombreBusqueda;

    @Column(name = "pui_habilitado")
    @AtributoHabilitado
    private Boolean puiHabilitado;

    @OneToMany(mappedBy = "uirPerfil", cascade = CascadeType.ALL, orphanRemoval = true)
    @AtributoInicializarColeccion
    private List<SgPerfilRoles> puiPerfilRoles;
    
    @Column(name = "pui_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime puiUltModFecha;

    @Size(max = 45)
    @Column(name = "pui_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String puiUltModUsuario;

    @Column(name = "pui_version")
    @Version
    private Integer puiVersion;

    public SgPerfilesUsuariosIngresadosCe() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.puiNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.puiNombre);
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

    public String getPuiNombreBusqueda() {
        return puiNombreBusqueda;
    }

    public void setPuiNombreBusqueda(String puiNombreBusqueda) {
        this.puiNombreBusqueda = puiNombreBusqueda;
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
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.puiPk);
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
        return "SgPerfilesUsuariosIngresadosCe{" + "puiPk=" + puiPk + ", puiCodigo=" + puiCodigo + ", puiNombre=" + puiNombre + ", puiNombreBusqueda=" + puiNombreBusqueda + ", puiHabilitado=" + puiHabilitado + ", puiUltModFecha=" + puiUltModFecha + ", puiUltModUsuario=" + puiUltModUsuario + ", puiVersion=" + puiVersion + '}';
    }
    
    

}
