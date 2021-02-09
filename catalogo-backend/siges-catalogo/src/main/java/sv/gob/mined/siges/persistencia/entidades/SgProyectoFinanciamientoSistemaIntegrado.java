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
@Table(name = "sg_proyecto_financiamiento_sistema_integrado", uniqueConstraints = {
    @UniqueConstraint(name = "pfs_codigo_uk", columnNames = {"pfs_codigo"})
    ,
    @UniqueConstraint(name = "pfs_nombre_uk", columnNames = {"pfs_nombre"})}, schema=Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "pfsPk", scope = SgProyectoFinanciamientoSistemaIntegrado.class)
@Audited
public class SgProyectoFinanciamientoSistemaIntegrado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pfs_pk", nullable = false)
    private Long pfsPk;

    @Size(max = 45)
    @Column(name = "pfs_codigo", length = 45)
    @AtributoCodigo
    private String pfsCodigo;

    @Size(max = 255)
    @Column(name = "pfs_nombre", length = 255)
    @AtributoNormalizable
    private String pfsNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "pfs_nombre_busqueda", length = 255)
    private String pfsNombreBusqueda;

    @Column(name = "pfs_habilitado")
    @AtributoHabilitado
    private Boolean pfsHabilitado;

    @Column(name = "pfs_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime pfsUltModFecha;

    @Size(max = 45)
    @Column(name = "pfs_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String pfsUltModUsuario;

    @Column(name = "pfs_version")
    @Version
    private Integer pfsVersion;

    public SgProyectoFinanciamientoSistemaIntegrado() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.pfsNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.pfsNombre);
    }

    public Long getPfsPk() {
        return pfsPk;
    }

    public void setPfsPk(Long pfsPk) {
        this.pfsPk = pfsPk;
    }

    public String getPfsCodigo() {
        return pfsCodigo;
    }

    public void setPfsCodigo(String pfsCodigo) {
        this.pfsCodigo = pfsCodigo;
    }

    public String getPfsNombre() {
        return pfsNombre;
    }

    public void setPfsNombre(String pfsNombre) {
        this.pfsNombre = pfsNombre;
    }

    public String getPfsNombreBusqueda() {
        return pfsNombreBusqueda;
    }

    public void setPfsNombreBusqueda(String pfsNombreBusqueda) {
        this.pfsNombreBusqueda = pfsNombreBusqueda;
    }

    public Boolean getPfsHabilitado() {
        return pfsHabilitado;
    }

    public void setPfsHabilitado(Boolean pfsHabilitado) {
        this.pfsHabilitado = pfsHabilitado;
    }

    public LocalDateTime getPfsUltModFecha() {
        return pfsUltModFecha;
    }

    public void setPfsUltModFecha(LocalDateTime pfsUltModFecha) {
        this.pfsUltModFecha = pfsUltModFecha;
    }

    public String getPfsUltModUsuario() {
        return pfsUltModUsuario;
    }

    public void setPfsUltModUsuario(String pfsUltModUsuario) {
        this.pfsUltModUsuario = pfsUltModUsuario;
    }

    public Integer getPfsVersion() {
        return pfsVersion;
    }

    public void setPfsVersion(Integer pfsVersion) {
        this.pfsVersion = pfsVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.pfsPk);
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
        final SgProyectoFinanciamientoSistemaIntegrado other = (SgProyectoFinanciamientoSistemaIntegrado) obj;
        if (!Objects.equals(this.pfsPk, other.pfsPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgProyectoFinanciamientoSistemaIntegrado{" + "pfsPk=" + pfsPk + ", pfsCodigo=" + pfsCodigo + ", pfsNombre=" + pfsNombre + ", pfsNombreBusqueda=" + pfsNombreBusqueda + ", pfsHabilitado=" + pfsHabilitado + ", pfsUltModFecha=" + pfsUltModFecha + ", pfsUltModUsuario=" + pfsUltModUsuario + ", pfsVersion=" + pfsVersion + '}';
    }
    
    

}
