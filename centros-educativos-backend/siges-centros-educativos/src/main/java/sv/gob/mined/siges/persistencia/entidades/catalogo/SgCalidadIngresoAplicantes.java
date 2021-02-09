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
@Table(name = "sg_calidades_ingreso_aplicantes", uniqueConstraints = {
    @UniqueConstraint(name = "cia_codigo_uk", columnNames = {"cia_codigo"})
    ,
    @UniqueConstraint(name = "cia_nombre_uk", columnNames = {"cia_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ciaPk", scope = SgCalidadIngresoAplicantes.class)
@Audited
public class SgCalidadIngresoAplicantes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cia_pk", nullable = false)
    private Long ciaPk;

    @Size(max = 45)
    @Column(name = "cia_codigo", length = 45)
    @AtributoCodigo
    private String ciaCodigo;

    @Size(max = 255)
    @Column(name = "cia_nombre", length = 255)
    @AtributoNormalizable
    private String ciaNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "cia_nombre_busqueda", length = 255)
    private String ciaNombreBusqueda;

    @Column(name = "cia_habilitado")
    @AtributoHabilitado
    private Boolean ciaHabilitado;

    @Column(name = "cia_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime ciaUltModFecha;

    @Size(max = 45)
    @Column(name = "cia_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String ciaUltModUsuario;

    @Column(name = "cia_version")
    @Version
    private Integer ciaVersion;

    public SgCalidadIngresoAplicantes() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.ciaNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.ciaNombre);
    }

    public Long getCiaPk() {
        return ciaPk;
    }

    public void setCiaPk(Long ciaPk) {
        this.ciaPk = ciaPk;
    }

    public String getCiaCodigo() {
        return ciaCodigo;
    }

    public void setCiaCodigo(String ciaCodigo) {
        this.ciaCodigo = ciaCodigo;
    }

    public String getCiaNombre() {
        return ciaNombre;
    }

    public void setCiaNombre(String ciaNombre) {
        this.ciaNombre = ciaNombre;
    }

    public String getCiaNombreBusqueda() {
        return ciaNombreBusqueda;
    }

    public void setCiaNombreBusqueda(String ciaNombreBusqueda) {
        this.ciaNombreBusqueda = ciaNombreBusqueda;
    }

    public Boolean getCiaHabilitado() {
        return ciaHabilitado;
    }

    public void setCiaHabilitado(Boolean ciaHabilitado) {
        this.ciaHabilitado = ciaHabilitado;
    }

    public LocalDateTime getCiaUltModFecha() {
        return ciaUltModFecha;
    }

    public void setCiaUltModFecha(LocalDateTime ciaUltModFecha) {
        this.ciaUltModFecha = ciaUltModFecha;
    }

    public String getCiaUltModUsuario() {
        return ciaUltModUsuario;
    }

    public void setCiaUltModUsuario(String ciaUltModUsuario) {
        this.ciaUltModUsuario = ciaUltModUsuario;
    }

    public Integer getCiaVersion() {
        return ciaVersion;
    }

    public void setCiaVersion(Integer ciaVersion) {
        this.ciaVersion = ciaVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.ciaPk);
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
        final SgCalidadIngresoAplicantes other = (SgCalidadIngresoAplicantes) obj;
        if (!Objects.equals(this.ciaPk, other.ciaPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgCalidadIngresoAplicantes{" + "ciaPk=" + ciaPk + ", ciaCodigo=" + ciaCodigo + ", ciaNombre=" + ciaNombre + ", ciaNombreBusqueda=" + ciaNombreBusqueda + ", ciaHabilitado=" + ciaHabilitado + ", ciaUltModFecha=" + ciaUltModFecha + ", ciaUltModUsuario=" + ciaUltModUsuario + ", ciaVersion=" + ciaVersion + '}';
    }
    
    

}
