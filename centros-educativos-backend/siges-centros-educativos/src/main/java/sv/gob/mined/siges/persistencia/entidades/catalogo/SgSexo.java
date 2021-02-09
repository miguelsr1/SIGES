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
import javax.persistence.UniqueConstraint;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
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

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_sexos", uniqueConstraints = {
    @UniqueConstraint(name = "sex_codigo_uk", columnNames = {"sex_codigo"})
    ,
    @UniqueConstraint(name = "sex_nombre_uk", columnNames = {"sex_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "sexPk", scope = SgSexo.class)
public class SgSexo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "sex_pk", nullable = false)
    private Long sexPk;

    @Size(max = 4)
    @Column(name = "sex_codigo", length = 4)
    @AtributoCodigo
    private String sexCodigo;

    @Size(max = 255)
    @Column(name = "sex_nombre", length = 255)
    @AtributoNormalizable
    private String sexNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "sex_nombre_busqueda", length = 255)
    private String sexNombreBusqueda;

    @Column(name = "sex_habilitado")
    @AtributoHabilitado
    private Boolean sexHabilitado;

    @Column(name = "sex_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime sexUltModFecha;

    @Size(max = 45)
    @Column(name = "sex_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String sexUltModUsuario;

    @Column(name = "sex_version")
    @Version
    private Integer sexVersion;

    public SgSexo() {
    }

    public SgSexo(Long sexPk, Integer sexVersion) {
        this.sexPk = sexPk;
        this.sexVersion = sexVersion;
    }
    
    @PrePersist
    @PreUpdate
    public void preSave() {
        this.sexNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.sexNombre);
    }

    public Long getSexPk() {
        return sexPk;
    }

    public void setSexPk(Long sexPk) {
        this.sexPk = sexPk;
    }

    public String getSexCodigo() {
        return sexCodigo;
    }

    public void setSexCodigo(String sexCodigo) {
        this.sexCodigo = sexCodigo;
    }

    public String getSexNombre() {
        return sexNombre;
    }

    public void setSexNombre(String sexNombre) {
        this.sexNombre = sexNombre;
    }

    public String getSexNombreBusqueda() {
        return sexNombreBusqueda;
    }

    public void setSexNombreBusqueda(String sexNombreBusqueda) {
        this.sexNombreBusqueda = sexNombreBusqueda;
    }

    public Boolean getSexHabilitado() {
        return sexHabilitado;
    }

    public void setSexHabilitado(Boolean sexHabilitado) {
        this.sexHabilitado = sexHabilitado;
    }

    public LocalDateTime getSexUltModFecha() {
        return sexUltModFecha;
    }

    public void setSexUltModFecha(LocalDateTime sexUltModFecha) {
        this.sexUltModFecha = sexUltModFecha;
    }

    public String getSexUltModUsuario() {
        return sexUltModUsuario;
    }

    public void setSexUltModUsuario(String sexUltModUsuario) {
        this.sexUltModUsuario = sexUltModUsuario;
    }

    public Integer getSexVersion() {
        return sexVersion;
    }

    public void setSexVersion(Integer sexVersion) {
        this.sexVersion = sexVersion;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + Objects.hashCode(this.sexPk);
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
        final SgSexo other = (SgSexo) obj;
        if (!Objects.equals(this.sexPk, other.sexPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SigesSexo[ sexPk=" + sexPk + " ]";
    }

}
