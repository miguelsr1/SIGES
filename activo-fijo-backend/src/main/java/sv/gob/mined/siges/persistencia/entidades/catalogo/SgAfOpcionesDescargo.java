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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_af_opciones_descargo", uniqueConstraints = {
    @UniqueConstraint(name = "ode_codigo_uk", columnNames = {"ode_codigo"})
    ,
    @UniqueConstraint(name = "ode_nombre_uk", columnNames = {"ode_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "odePk",resolver = JsonIdentityResolver.class, scope = SgAfOpcionesDescargo.class)
public class SgAfOpcionesDescargo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ode_pk", nullable = false)
    private Long odePk;

    @Size(max = 4)
    @Column(name = "ode_codigo", length = 4)
    @AtributoCodigo
    private String odeCodigo;

    @Size(max = 255)
    @Column(name = "ode_nombre", length = 255)
    @AtributoNormalizable
    private String odeNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "ode_nombre_busqueda", length = 255)
    private String odeNombreBusqueda;

    @Column(name = "ode_habilitado")
    @AtributoHabilitado
    private Boolean odeHabilitado;

    @Column(name = "ode_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime odeUltModFecha;

    @Size(max = 45)
    @Column(name = "ode_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String odeUltModUsuario;

    @Column(name = "ode_version")
    @Version
    private Integer odeVersion;

    public SgAfOpcionesDescargo() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.odeNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.odeNombre);
    }

    public Long getOdePk() {
        return odePk;
    }

    public void setOdePk(Long odePk) {
        this.odePk = odePk;
    }

    public String getOdeCodigo() {
        return odeCodigo;
    }

    public void setOdeCodigo(String odeCodigo) {
        this.odeCodigo = odeCodigo;
    }

    public String getOdeNombre() {
        return odeNombre;
    }

    public void setOdeNombre(String odeNombre) {
        this.odeNombre = odeNombre;
    }

    public String getOdeNombreBusqueda() {
        return odeNombreBusqueda;
    }

    public void setOdeNombreBusqueda(String odeNombreBusqueda) {
        this.odeNombreBusqueda = odeNombreBusqueda;
    }

    public Boolean getOdeHabilitado() {
        return odeHabilitado;
    }

    public void setOdeHabilitado(Boolean odeHabilitado) {
        this.odeHabilitado = odeHabilitado;
    }

    public LocalDateTime getOdeUltModFecha() {
        return odeUltModFecha;
    }

    public void setOdeUltModFecha(LocalDateTime odeUltModFecha) {
        this.odeUltModFecha = odeUltModFecha;
    }

    public String getOdeUltModUsuario() {
        return odeUltModUsuario;
    }

    public void setOdeUltModUsuario(String odeUltModUsuario) {
        this.odeUltModUsuario = odeUltModUsuario;
    }

    public Integer getOdeVersion() {
        return odeVersion;
    }

    public void setOdeVersion(Integer odeVersion) {
        this.odeVersion = odeVersion;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 43 * hash + Objects.hashCode(this.odePk);
        hash = 43 * hash + Objects.hashCode(this.odeCodigo);
        hash = 43 * hash + Objects.hashCode(this.odeNombre);
        hash = 43 * hash + Objects.hashCode(this.odeNombreBusqueda);
        hash = 43 * hash + Objects.hashCode(this.odeHabilitado);
        hash = 43 * hash + Objects.hashCode(this.odeUltModFecha);
        hash = 43 * hash + Objects.hashCode(this.odeUltModUsuario);
        hash = 43 * hash + Objects.hashCode(this.odeVersion);
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
        final SgAfOpcionesDescargo other = (SgAfOpcionesDescargo) obj;
        if (!Objects.equals(this.odePk, other.odePk)) {
            return false;
        }
        return true;
    }

    
    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgOpcionesDescargo[ odePk=" + odePk + " ]";
    }

}
