/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.catalogo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
import java.io.Serializable;
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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
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
import sv.gob.mined.siges.utils.SofisStringUtils;

@Entity
@Table(name = "sg_clasificaciones", uniqueConstraints = {
    @UniqueConstraint(name = "cla_codigo_uk", columnNames = {"cla_codigo"})
    ,
    @UniqueConstraint(name = "cla_nombre_uk", columnNames = {"cla_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "claPk", scope = SgClasificacion.class)
public class SgClasificacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cla_pk", nullable = false)
    private Long claPk;

    @Size(max = 4)
    @Column(name = "cla_codigo", length = 4)
    @AtributoCodigo
    private String claCodigo;

    @Size(max = 255)
    @Column(name = "cla_nombre", length = 255)
    @AtributoNormalizable
    private String claNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "cla_nombre_busqueda", length = 255)
    private String claNombreBusqueda;

    @Column(name = "cla_habilitado")
    @AtributoHabilitado
    private Boolean claHabilitado;

    @Column(name = "cla_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime claUltModFecha;

    @Size(max = 45)
    @Column(name = "cla_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String claUltModUsuario;

    @Column(name = "cla_version")
    @Version
    private Integer claVersion;

    public SgClasificacion() {
    }


    @PrePersist
    @PreUpdate
    public void preSave() {
        this.claNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.claNombre);
    }

    public Long getClaPk() {
        return claPk;
    }

    public void setClaPk(Long claPk) {
        this.claPk = claPk;
    }

    public String getClaCodigo() {
        return claCodigo;
    }

    public void setClaCodigo(String claCodigo) {
        this.claCodigo = claCodigo;
    }

    public String getClaNombre() {
        return claNombre;
    }

    public void setClaNombre(String claNombre) {
        this.claNombre = claNombre;
    }

    public String getClaNombreBusqueda() {
        return claNombreBusqueda;
    }

    public void setClaNombreBusqueda(String claNombreBusqueda) {
        this.claNombreBusqueda = claNombreBusqueda;
    }

    public Boolean getClaHabilitado() {
        return claHabilitado;
    }

    public void setClaHabilitado(Boolean claHabilitado) {
        this.claHabilitado = claHabilitado;
    }

    public LocalDateTime getClaUltModFecha() {
        return claUltModFecha;
    }

    public void setClaUltModFecha(LocalDateTime claUltModFecha) {
        this.claUltModFecha = claUltModFecha;
    }

    public String getClaUltModUsuario() {
        return claUltModUsuario;
    }

    public void setClaUltModUsuario(String claUltModUsuario) {
        this.claUltModUsuario = claUltModUsuario;
    }

    public Integer getClaVersion() {
        return claVersion;
    }

    public void setClaVersion(Integer claVersion) {
        this.claVersion = claVersion;
    }


    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + Objects.hashCode(this.claPk);
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
        final SgClasificacion other = (SgClasificacion) obj;
        if (!Objects.equals(this.claPk, other.claPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgClasificacion{" + "claPk=" + claPk + '}';
    }

    public SgClasificacion(Long claPk) {
        this.claPk = claPk;
    }
}
