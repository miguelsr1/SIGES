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
@Table(name = "sg_tipos_documentos_docente", uniqueConstraints = {
    @UniqueConstraint(name = "tdd_codigo_uk", columnNames = {"tdd_codigo"})
    ,
    @UniqueConstraint(name = "tdd_nombre_uk", columnNames = {"tdd_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tddPk", scope = SgTipoDocumentoDocente.class)
@Audited
public class SgTipoDocumentoDocente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tdd_pk", nullable = false)
    private Long tddPk;

    @Size(max = 45)
    @Column(name = "tdd_codigo", length = 45)
    @AtributoCodigo
    private String tddCodigo;

    @Size(max = 255)
    @Column(name = "tdd_nombre", length = 255)
    @AtributoNormalizable
    private String tddNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "tdd_nombre_busqueda", length = 255)
    private String tddNombreBusqueda;

    @Column(name = "tdd_habilitado")
    @AtributoHabilitado
    private Boolean tddHabilitado;

    @Column(name = "tdd_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime tddUltModFecha;

    @Size(max = 45)
    @Column(name = "tdd_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String tddUltModUsuario;

    @Column(name = "tdd_version")
    @Version
    private Integer tddVersion;

    public SgTipoDocumentoDocente() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.tddNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.tddNombre);
    }

    public Long getTddPk() {
        return tddPk;
    }

    public void setTddPk(Long tddPk) {
        this.tddPk = tddPk;
    }

    public String getTddCodigo() {
        return tddCodigo;
    }

    public void setTddCodigo(String tddCodigo) {
        this.tddCodigo = tddCodigo;
    }

    public String getTddNombre() {
        return tddNombre;
    }

    public void setTddNombre(String tddNombre) {
        this.tddNombre = tddNombre;
    }

    public String getTddNombreBusqueda() {
        return tddNombreBusqueda;
    }

    public void setTddNombreBusqueda(String tddNombreBusqueda) {
        this.tddNombreBusqueda = tddNombreBusqueda;
    }

    public Boolean getTddHabilitado() {
        return tddHabilitado;
    }

    public void setTddHabilitado(Boolean tddHabilitado) {
        this.tddHabilitado = tddHabilitado;
    }

    public LocalDateTime getTddUltModFecha() {
        return tddUltModFecha;
    }

    public void setTddUltModFecha(LocalDateTime tddUltModFecha) {
        this.tddUltModFecha = tddUltModFecha;
    }

    public String getTddUltModUsuario() {
        return tddUltModUsuario;
    }

    public void setTddUltModUsuario(String tddUltModUsuario) {
        this.tddUltModUsuario = tddUltModUsuario;
    }

    public Integer getTddVersion() {
        return tddVersion;
    }

    public void setTddVersion(Integer tddVersion) {
        this.tddVersion = tddVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.tddPk);
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
        final SgTipoDocumentoDocente other = (SgTipoDocumentoDocente) obj;
        if (!Objects.equals(this.tddPk, other.tddPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgTipoDocumentoDocente{" + "tddPk=" + tddPk + ", tddCodigo=" + tddCodigo + ", tddNombre=" + tddNombre + ", tddNombreBusqueda=" + tddNombreBusqueda + ", tddHabilitado=" + tddHabilitado + ", tddUltModFecha=" + tddUltModFecha + ", tddUltModUsuario=" + tddUltModUsuario + ", tddVersion=" + tddVersion + '}';
    }
    
    

}
