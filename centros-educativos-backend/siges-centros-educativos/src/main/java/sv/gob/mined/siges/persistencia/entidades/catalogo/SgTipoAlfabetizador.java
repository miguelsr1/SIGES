/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.catalogo;

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
import java.util.Objects;
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
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_tipo_alfabetizador", uniqueConstraints = {
    @UniqueConstraint(name = "tal_codigo_uk", columnNames = {"tal_codigo"})
    ,
    @UniqueConstraint(name = "tal_nombre_uk", columnNames = {"tal_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "talPk", scope = SgTipoAlfabetizador.class)
@Audited
public class SgTipoAlfabetizador implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tal_pk", nullable = false)
    private Long talPk;

    @Size(max = 45)
    @Column(name = "tal_codigo", length = 45)
    @AtributoCodigo
    private String talCodigo;

    @Size(max = 255)
    @Column(name = "tal_nombre", length = 255)
    @AtributoNormalizable
    private String talNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "tal_nombre_busqueda", length = 255)
    private String talNombreBusqueda;

    @Column(name = "tal_habilitado")
    @AtributoHabilitado
    private Boolean talHabilitado;

    @Column(name = "tal_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime talUltModFecha;

    @Size(max = 45)
    @Column(name = "tal_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String talUltModUsuario;

    @Column(name = "tal_version")
    @Version
    private Integer talVersion;

    public SgTipoAlfabetizador() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.talNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.talNombre);
    }

    public Long getTalPk() {
        return talPk;
    }

    public void setTalPk(Long talPk) {
        this.talPk = talPk;
    }

    public String getTalCodigo() {
        return talCodigo;
    }

    public void setTalCodigo(String talCodigo) {
        this.talCodigo = talCodigo;
    }

    public String getTalNombre() {
        return talNombre;
    }

    public void setTalNombre(String talNombre) {
        this.talNombre = talNombre;
    }

    public String getTalNombreBusqueda() {
        return talNombreBusqueda;
    }

    public void setTalNombreBusqueda(String talNombreBusqueda) {
        this.talNombreBusqueda = talNombreBusqueda;
    }

    public Boolean getTalHabilitado() {
        return talHabilitado;
    }

    public void setTalHabilitado(Boolean talHabilitado) {
        this.talHabilitado = talHabilitado;
    }

    public LocalDateTime getTalUltModFecha() {
        return talUltModFecha;
    }

    public void setTalUltModFecha(LocalDateTime talUltModFecha) {
        this.talUltModFecha = talUltModFecha;
    }

    public String getTalUltModUsuario() {
        return talUltModUsuario;
    }

    public void setTalUltModUsuario(String talUltModUsuario) {
        this.talUltModUsuario = talUltModUsuario;
    }

    public Integer getTalVersion() {
        return talVersion;
    }

    public void setTalVersion(Integer talVersion) {
        this.talVersion = talVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.talPk);
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
        final SgTipoAlfabetizador other = (SgTipoAlfabetizador) obj;
        if (!Objects.equals(this.talPk, other.talPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgTipoAlfabetizador{" + "talPk=" + talPk + ", talCodigo=" + talCodigo + ", talNombre=" + talNombre + ", talNombreBusqueda=" + talNombreBusqueda + ", talHabilitado=" + talHabilitado + ", talUltModFecha=" + talUltModFecha + ", talUltModUsuario=" + talUltModUsuario + ", talVersion=" + talVersion + '}';
    }
    
    

}
