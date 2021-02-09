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
@Table(name = "sg_glosarios", uniqueConstraints = {
    @UniqueConstraint(name = "glo_nombre_uk", columnNames = {"glo_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "gloPk", scope = SgGlosario.class)
@Audited
public class SgGlosario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "glo_pk", nullable = false)
    private Long gloPk;

    @Size(max = 45)
    @Column(name = "glo_codigo", length = 45)
    @AtributoCodigo
    private String gloCodigo;

    @Column(name = "glo_nombre")
    @AtributoNormalizable
    private String gloNombre;

    @AtributoNombre
    @Column(name = "glo_nombre_busqueda")
    private String gloNombreBusqueda;
    
    @Column(name = "glo_fuente")
    private String gloFuente;

    @Column(name = "glo_habilitado")
    @AtributoHabilitado
    private Boolean gloHabilitado;

    @Column(name = "glo_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime gloUltModFecha;

    @Size(max = 45)
    @Column(name = "glo_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String gloUltModUsuario;

    @Column(name = "glo_version")
    @Version
    private Integer gloVersion;

    public SgGlosario() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.gloNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.gloNombre);
    }

    public Long getGloPk() {
        return gloPk;
    }

    public void setGloPk(Long gloPk) {
        this.gloPk = gloPk;
    }

    public String getGloCodigo() {
        return gloCodigo;
    }

    public void setGloCodigo(String gloCodigo) {
        this.gloCodigo = gloCodigo;
    }

    public String getGloNombre() {
        return gloNombre;
    }

    public void setGloNombre(String gloNombre) {
        this.gloNombre = gloNombre;
    }

    public String getGloNombreBusqueda() {
        return gloNombreBusqueda;
    }

    public void setGloNombreBusqueda(String gloNombreBusqueda) {
        this.gloNombreBusqueda = gloNombreBusqueda;
    }

    public String getGloFuente() {
        return gloFuente;
    }

    public void setGloFuente(String gloFuente) {
        this.gloFuente = gloFuente;
    }
    

    public Boolean getGloHabilitado() {
        return gloHabilitado;
    }

    public void setGloHabilitado(Boolean gloHabilitado) {
        this.gloHabilitado = gloHabilitado;
    }

    public LocalDateTime getGloUltModFecha() {
        return gloUltModFecha;
    }

    public void setGloUltModFecha(LocalDateTime gloUltModFecha) {
        this.gloUltModFecha = gloUltModFecha;
    }

    public String getGloUltModUsuario() {
        return gloUltModUsuario;
    }

    public void setGloUltModUsuario(String gloUltModUsuario) {
        this.gloUltModUsuario = gloUltModUsuario;
    }

    public Integer getGloVersion() {
        return gloVersion;
    }

    public void setGloVersion(Integer gloVersion) {
        this.gloVersion = gloVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.gloPk);
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
        final SgGlosario other = (SgGlosario) obj;
        if (!Objects.equals(this.gloPk, other.gloPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgGlosario{" + "gloPk=" + gloPk + ", gloCodigo=" + gloCodigo + ", gloNombre=" + gloNombre + ", gloNombreBusqueda=" + gloNombreBusqueda + ", gloHabilitado=" + gloHabilitado + ", gloUltModFecha=" + gloUltModFecha + ", gloUltModUsuario=" + gloUltModUsuario + ", gloVersion=" + gloVersion + '}';
    }
    
    

}
