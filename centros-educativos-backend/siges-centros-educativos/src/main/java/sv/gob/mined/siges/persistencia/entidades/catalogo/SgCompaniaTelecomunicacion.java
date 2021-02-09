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
@Table(name = "sg_companias_telecomunicacion", schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ctePk", scope = SgCompaniaTelecomunicacion.class)
@Audited
public class SgCompaniaTelecomunicacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cte_pk", nullable = false)
    private Long ctePk;

    @Size(max = 45)
    @Column(name = "cte_codigo", length = 45)
    @AtributoCodigo
    private String cteCodigo;

    @Size(max = 255)
    @Column(name = "cte_nombre", length = 255)
    @AtributoNormalizable
    private String cteNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "cte_nombre_busqueda", length = 255)
    private String cteNombreBusqueda;

    @Column(name = "cte_habilitado")
    @AtributoHabilitado
    private Boolean cteHabilitado;

    @Column(name = "cte_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime cteUltModFecha;

    @Size(max = 45)
    @Column(name = "cte_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String cteUltModUsuario;

    @Column(name = "cte_version")
    @Version
    private Integer cteVersion;

    public SgCompaniaTelecomunicacion() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.cteNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.cteNombre);
    }

    public Long getCtePk() {
        return ctePk;
    }

    public void setCtePk(Long ctePk) {
        this.ctePk = ctePk;
    }

    public String getCteCodigo() {
        return cteCodigo;
    }

    public void setCteCodigo(String cteCodigo) {
        this.cteCodigo = cteCodigo;
    }

    public String getCteNombre() {
        return cteNombre;
    }

    public void setCteNombre(String cteNombre) {
        this.cteNombre = cteNombre;
    }

    public String getCteNombreBusqueda() {
        return cteNombreBusqueda;
    }

    public void setCteNombreBusqueda(String cteNombreBusqueda) {
        this.cteNombreBusqueda = cteNombreBusqueda;
    }

    public Boolean getCteHabilitado() {
        return cteHabilitado;
    }

    public void setCteHabilitado(Boolean cteHabilitado) {
        this.cteHabilitado = cteHabilitado;
    }

    public LocalDateTime getCteUltModFecha() {
        return cteUltModFecha;
    }

    public void setCteUltModFecha(LocalDateTime cteUltModFecha) {
        this.cteUltModFecha = cteUltModFecha;
    }

    public String getCteUltModUsuario() {
        return cteUltModUsuario;
    }

    public void setCteUltModUsuario(String cteUltModUsuario) {
        this.cteUltModUsuario = cteUltModUsuario;
    }

    public Integer getCteVersion() {
        return cteVersion;
    }

    public void setCteVersion(Integer cteVersion) {
        this.cteVersion = cteVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.ctePk);
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
        final SgCompaniaTelecomunicacion other = (SgCompaniaTelecomunicacion) obj;
        if (!Objects.equals(this.ctePk, other.ctePk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgCompaniaTelecomunicacion{" + "ctePk=" + ctePk + ", cteCodigo=" + cteCodigo + ", cteNombre=" + cteNombre + ", cteNombreBusqueda=" + cteNombreBusqueda + ", cteHabilitado=" + cteHabilitado + ", cteUltModFecha=" + cteUltModFecha + ", cteUltModUsuario=" + cteUltModUsuario + ", cteVersion=" + cteVersion + '}';
    }
    
    

}
