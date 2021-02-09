/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.catalogo;

import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
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
@Table(name = "sg_areas_indicador", uniqueConstraints = {
    @UniqueConstraint(name = "ari_codigo_uk", columnNames = {"ari_codigo"})
    ,
    @UniqueConstraint(name = "ari_nombre_uk", columnNames = {"ari_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "ariPk", scope = SgAreaIndicador.class)
@Audited
public class SgAreaIndicador implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ari_pk", nullable = false)
    private Long ariPk;

    @Size(max = 45)
    @Column(name = "ari_codigo", length = 45)
    @AtributoCodigo
    private String ariCodigo;

    @Size(max = 255)
    @Column(name = "ari_nombre", length = 255)
    @AtributoNormalizable
    private String ariNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "ari_nombre_busqueda", length = 255)
    private String ariNombreBusqueda;

    @Column(name = "ari_habilitado")
    @AtributoHabilitado
    private Boolean ariHabilitado;

    @Column(name = "ari_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime ariUltModFecha;

    @Size(max = 45)
    @Column(name = "ari_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String ariUltModUsuario;

    @Column(name = "ari_version")
    @Version
    private Integer ariVersion;

    public SgAreaIndicador() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.ariNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.ariNombre);
    }

    public Long getAriPk() {
        return ariPk;
    }

    public void setAriPk(Long ariPk) {
        this.ariPk = ariPk;
    }

    public String getAriCodigo() {
        return ariCodigo;
    }

    public void setAriCodigo(String ariCodigo) {
        this.ariCodigo = ariCodigo;
    }

    public String getAriNombre() {
        return ariNombre;
    }

    public void setAriNombre(String ariNombre) {
        this.ariNombre = ariNombre;
    }

    public String getAriNombreBusqueda() {
        return ariNombreBusqueda;
    }

    public void setAriNombreBusqueda(String ariNombreBusqueda) {
        this.ariNombreBusqueda = ariNombreBusqueda;
    }

    public Boolean getAriHabilitado() {
        return ariHabilitado;
    }

    public void setAriHabilitado(Boolean ariHabilitado) {
        this.ariHabilitado = ariHabilitado;
    }

    public LocalDateTime getAriUltModFecha() {
        return ariUltModFecha;
    }

    public void setAriUltModFecha(LocalDateTime ariUltModFecha) {
        this.ariUltModFecha = ariUltModFecha;
    }

    public String getAriUltModUsuario() {
        return ariUltModUsuario;
    }

    public void setAriUltModUsuario(String ariUltModUsuario) {
        this.ariUltModUsuario = ariUltModUsuario;
    }

    public Integer getAriVersion() {
        return ariVersion;
    }

    public void setAriVersion(Integer ariVersion) {
        this.ariVersion = ariVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.ariPk);
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
        final SgAreaIndicador other = (SgAreaIndicador) obj;
        if (!Objects.equals(this.ariPk, other.ariPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgAreaIndicador{" + "ariPk=" + ariPk + ", ariCodigo=" + ariCodigo + ", ariNombre=" + ariNombre + ", ariNombreBusqueda=" + ariNombreBusqueda + ", ariHabilitado=" + ariHabilitado + ", ariUltModFecha=" + ariUltModFecha + ", ariUltModUsuario=" + ariUltModUsuario + ", ariVersion=" + ariVersion + '}';
    }
    
    

}
