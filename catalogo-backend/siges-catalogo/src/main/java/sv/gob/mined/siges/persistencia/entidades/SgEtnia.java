/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
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
@Table(name = "sg_etnias", uniqueConstraints = {
    @UniqueConstraint(name = "etn_codigo_uk", columnNames = {"etn_codigo"})
    ,
    @UniqueConstraint(name = "etn_nombre_uk", columnNames = {"etn_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "etnPk", scope = SgEtnia.class)
public class SgEtnia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "etn_pk", nullable = false)
    private Long etnPk;

    @Size(max = 4)
    @Column(name = "etn_codigo", length = 4)
    @AtributoCodigo
    private String etnCodigo;

    @Size(max = 255)
    @Column(name = "etn_nombre", length = 255)
    @AtributoNormalizable
    private String etnNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "etn_nombre_busqueda", length = 255)
    private String etnNombreBusqueda;

    @Column(name = "etn_habilitado")
    @AtributoHabilitado
    private Boolean etnHabilitado;

    @Column(name = "etn_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime etnUltModFecha;

    @Size(max = 45)
    @Column(name = "etn_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String etnUltModUsuario;

    @Column(name = "etn_version")
    @Version
    private Integer etnVersion;

    public SgEtnia() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.etnNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.etnNombre);
    }

    public Long getEtnPk() {
        return etnPk;
    }

    public void setEtnPk(Long etnPk) {
        this.etnPk = etnPk;
    }

    public String getEtnCodigo() {
        return etnCodigo;
    }

    public void setEtnCodigo(String etnCodigo) {
        this.etnCodigo = etnCodigo;
    }

    public String getEtnNombre() {
        return etnNombre;
    }

    public void setEtnNombre(String etnNombre) {
        this.etnNombre = etnNombre;
    }

    public String getEtnNombreBusqueda() {
        return etnNombreBusqueda;
    }

    public void setEtnNombreBusqueda(String etnNombreBusqueda) {
        this.etnNombreBusqueda = etnNombreBusqueda;
    }

    public Boolean getEtnHabilitado() {
        return etnHabilitado;
    }

    public void setEtnHabilitado(Boolean etnHabilitado) {
        this.etnHabilitado = etnHabilitado;
    }

    public LocalDateTime getEtnUltModFecha() {
        return etnUltModFecha;
    }

    public void setEtnUltModFecha(LocalDateTime etnUltModFecha) {
        this.etnUltModFecha = etnUltModFecha;
    }

    public String getEtnUltModUsuario() {
        return etnUltModUsuario;
    }

    public void setEtnUltModUsuario(String etnUltModUsuario) {
        this.etnUltModUsuario = etnUltModUsuario;
    }

    public Integer getEtnVersion() {
        return etnVersion;
    }

    public void setEtnVersion(Integer etnVersion) {
        this.etnVersion = etnVersion;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + Objects.hashCode(this.etnPk);
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
        final SgEtnia other = (SgEtnia) obj;
        if (!Objects.equals(this.etnPk, other.etnPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SigesEtnia[ etnPk=" + etnPk + " ]";
    }

}
