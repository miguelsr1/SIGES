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
@Table(name = "sg_est_nombres_extracciones", uniqueConstraints = {
    @UniqueConstraint(name = "nex_codigo_uk", columnNames = {"nex_codigo"})
    ,
    @UniqueConstraint(name = "nex_nombre_uk", columnNames = {"nex_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "nexPk", scope = SgEstNombreExtraccion.class)
@Audited
public class SgEstNombreExtraccion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "nex_pk", nullable = false)
    private Long nexPk;

    @Size(max = 45)
    @Column(name = "nex_codigo", length = 45)
    @AtributoCodigo
    private String nexCodigo;

    @Size(max = 255)
    @Column(name = "nex_nombre", length = 255)
    @AtributoNormalizable
    private String nexNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "nex_nombre_busqueda", length = 255)
    private String nexNombreBusqueda;

    @Column(name = "nex_habilitado")
    @AtributoHabilitado
    private Boolean nexHabilitado;

    @Column(name = "nex_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime nexUltModFecha;

    @Size(max = 45)
    @Column(name = "nex_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String nexUltModUsuario;

    @Column(name = "nex_version")
    @Version
    private Integer nexVersion;

    public SgEstNombreExtraccion() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.nexNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.nexNombre);
    }

    public Long getNexPk() {
        return nexPk;
    }

    public void setNexPk(Long nexPk) {
        this.nexPk = nexPk;
    }

    public String getNexCodigo() {
        return nexCodigo;
    }

    public void setNexCodigo(String nexCodigo) {
        this.nexCodigo = nexCodigo;
    }

    public String getNexNombre() {
        return nexNombre;
    }

    public void setNexNombre(String nexNombre) {
        this.nexNombre = nexNombre;
    }

    public String getNexNombreBusqueda() {
        return nexNombreBusqueda;
    }

    public void setNexNombreBusqueda(String nexNombreBusqueda) {
        this.nexNombreBusqueda = nexNombreBusqueda;
    }

    public Boolean getNexHabilitado() {
        return nexHabilitado;
    }

    public void setNexHabilitado(Boolean nexHabilitado) {
        this.nexHabilitado = nexHabilitado;
    }

    public LocalDateTime getNexUltModFecha() {
        return nexUltModFecha;
    }

    public void setNexUltModFecha(LocalDateTime nexUltModFecha) {
        this.nexUltModFecha = nexUltModFecha;
    }

    public String getNexUltModUsuario() {
        return nexUltModUsuario;
    }

    public void setNexUltModUsuario(String nexUltModUsuario) {
        this.nexUltModUsuario = nexUltModUsuario;
    }

    public Integer getNexVersion() {
        return nexVersion;
    }

    public void setNexVersion(Integer nexVersion) {
        this.nexVersion = nexVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.nexPk);
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
        final SgEstNombreExtraccion other = (SgEstNombreExtraccion) obj;
        if (!Objects.equals(this.nexPk, other.nexPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgNombreExtracciones{" + "nexPk=" + nexPk + ", nexCodigo=" + nexCodigo + ", nexNombre=" + nexNombre + ", nexNombreBusqueda=" + nexNombreBusqueda + ", nexHabilitado=" + nexHabilitado + ", nexUltModFecha=" + nexUltModFecha + ", nexUltModUsuario=" + nexUltModUsuario + ", nexVersion=" + nexVersion + '}';
    }
    
    

}
