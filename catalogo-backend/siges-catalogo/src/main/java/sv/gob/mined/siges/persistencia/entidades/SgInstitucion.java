/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
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
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_instituciones", uniqueConstraints = {
    @UniqueConstraint(name = "ins_codigo_uk", columnNames = {"ins_codigo"})
    ,
    @UniqueConstraint(name = "ins_nombre_uk", columnNames = {"ins_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "insPk", scope = SgInstitucion.class)
public class SgInstitucion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ins_pk", nullable = false)
    private Long insPk;

    @Size(max = 45)
    @Column(name = "ins_codigo", length = 45)
    @AtributoCodigo
    private String insCodigo;

    @Size(max = 255)
    @Column(name = "ins_nombre", length = 255)
    @AtributoNormalizable
    private String insNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "ins_nombre_busqueda", length = 255)
    private String insNombreBusqueda;

    @Column(name = "ins_habilitado")
    @AtributoHabilitado
    private Boolean insHabilitado;

    @Column(name = "ins_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime insUltModFecha;

    @Size(max = 45)
    @Column(name = "ins_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String insUltModUsuario;

    @Column(name = "ins_version")
    @Version
    private Integer insVersion;

    public SgInstitucion() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.insNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.insNombre);
    }

    public Long getInsPk() {
        return insPk;
    }

    public void setInsPk(Long insPk) {
        this.insPk = insPk;
    }

    public String getInsCodigo() {
        return insCodigo;
    }

    public void setInsCodigo(String insCodigo) {
        this.insCodigo = insCodigo;
    }

    public String getInsNombre() {
        return insNombre;
    }

    public void setInsNombre(String insNombre) {
        this.insNombre = insNombre;
    }

    public String getInsNombreBusqueda() {
        return insNombreBusqueda;
    }

    public void setInsNombreBusqueda(String insNombreBusqueda) {
        this.insNombreBusqueda = insNombreBusqueda;
    }

    public Boolean getInsHabilitado() {
        return insHabilitado;
    }

    public void setInsHabilitado(Boolean insHabilitado) {
        this.insHabilitado = insHabilitado;
    }

    public LocalDateTime getInsUltModFecha() {
        return insUltModFecha;
    }

    public void setInsUltModFecha(LocalDateTime insUltModFecha) {
        this.insUltModFecha = insUltModFecha;
    }

    public String getInsUltModUsuario() {
        return insUltModUsuario;
    }

    public void setInsUltModUsuario(String insUltModUsuario) {
        this.insUltModUsuario = insUltModUsuario;
    }

    public Integer getInsVersion() {
        return insVersion;
    }

    public void setInsVersion(Integer insVersion) {
        this.insVersion = insVersion;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + Objects.hashCode(this.insPk);
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
        final SgInstitucion other = (SgInstitucion) obj;
        if (!Objects.equals(this.insPk, other.insPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SigesInstitucion{" + "insPk=" + insPk + '}';
    }

}
