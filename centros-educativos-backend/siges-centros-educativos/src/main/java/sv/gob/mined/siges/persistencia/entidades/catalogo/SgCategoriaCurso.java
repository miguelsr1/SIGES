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
@Table(name = "sg_categorias_cursos", uniqueConstraints = {
    @UniqueConstraint(name = "ccu_codigo_uk", columnNames = {"ccu_codigo"})
    ,
    @UniqueConstraint(name = "ccu_nombre_uk", columnNames = {"ccu_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "ccuPk", scope = SgCategoriaCurso.class)
@Audited
public class SgCategoriaCurso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ccu_pk", nullable = false)
    private Long ccuPk;

    @Size(max = 45)
    @Column(name = "ccu_codigo", length = 45)
    @AtributoCodigo
    private String ccuCodigo;

    @Size(max = 255)
    @Column(name = "ccu_nombre", length = 255)
    @AtributoNormalizable
    private String ccuNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "ccu_nombre_busqueda", length = 255)
    private String ccuNombreBusqueda;

    @Column(name = "ccu_habilitado")
    @AtributoHabilitado
    private Boolean ccuHabilitado;

    @Column(name = "ccu_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime ccuUltModFecha;

    @Size(max = 45)
    @Column(name = "ccu_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String ccuUltModUsuario;

    @Column(name = "ccu_version")
    @Version
    private Integer ccuVersion;

    public SgCategoriaCurso() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.ccuNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.ccuNombre);
    }

    public Long getCcuPk() {
        return ccuPk;
    }

    public void setCcuPk(Long ccuPk) {
        this.ccuPk = ccuPk;
    }

    public String getCcuCodigo() {
        return ccuCodigo;
    }

    public void setCcuCodigo(String ccuCodigo) {
        this.ccuCodigo = ccuCodigo;
    }

    public String getCcuNombre() {
        return ccuNombre;
    }

    public void setCcuNombre(String ccuNombre) {
        this.ccuNombre = ccuNombre;
    }

    public String getCcuNombreBusqueda() {
        return ccuNombreBusqueda;
    }

    public void setCcuNombreBusqueda(String ccuNombreBusqueda) {
        this.ccuNombreBusqueda = ccuNombreBusqueda;
    }

    public Boolean getCcuHabilitado() {
        return ccuHabilitado;
    }

    public void setCcuHabilitado(Boolean ccuHabilitado) {
        this.ccuHabilitado = ccuHabilitado;
    }

    public LocalDateTime getCcuUltModFecha() {
        return ccuUltModFecha;
    }

    public void setCcuUltModFecha(LocalDateTime ccuUltModFecha) {
        this.ccuUltModFecha = ccuUltModFecha;
    }

    public String getCcuUltModUsuario() {
        return ccuUltModUsuario;
    }

    public void setCcuUltModUsuario(String ccuUltModUsuario) {
        this.ccuUltModUsuario = ccuUltModUsuario;
    }

    public Integer getCcuVersion() {
        return ccuVersion;
    }

    public void setCcuVersion(Integer ccuVersion) {
        this.ccuVersion = ccuVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.ccuPk);
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
        final SgCategoriaCurso other = (SgCategoriaCurso) obj;
        if (!Objects.equals(this.ccuPk, other.ccuPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgCategoriaCurso{" + "ccuPk=" + ccuPk + ", ccuCodigo=" + ccuCodigo + ", ccuNombre=" + ccuNombre + ", ccuNombreBusqueda=" + ccuNombreBusqueda + ", ccuHabilitado=" + ccuHabilitado + ", ccuUltModFecha=" + ccuUltModFecha + ", ccuUltModUsuario=" + ccuUltModUsuario + ", ccuVersion=" + ccuVersion + '}';
    }
    
    

}
