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
@Table(name = "sg_motivo_reimpresion", uniqueConstraints = {
    @UniqueConstraint(name = "mri_codigo_uk", columnNames = {"mri_codigo"})
    ,
    @UniqueConstraint(name = "mri_nombre_uk", columnNames = {"mri_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "mriPk", scope = SgMotivoReimpresion.class)
@Audited
public class SgMotivoReimpresion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "mri_pk", nullable = false)
    private Long mriPk;

    @Size(max = 45)
    @Column(name = "mri_codigo", length = 45)
    @AtributoCodigo
    private String mriCodigo;

    @Size(max = 255)
    @Column(name = "mri_nombre", length = 255)
    @AtributoNormalizable
    private String mriNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "mri_nombre_busqueda", length = 255)
    private String mriNombreBusqueda;

    @Column(name = "mri_habilitado")
    @AtributoHabilitado
    private Boolean mriHabilitado;

    @Column(name = "mri_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime mriUltModFecha;

    @Size(max = 45)
    @Column(name = "mri_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String mriUltModUsuario;

    @Column(name = "mri_version")
    @Version
    private Integer mriVersion;

    public SgMotivoReimpresion() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.mriNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.mriNombre);
    }

    public Long getMriPk() {
        return mriPk;
    }

    public void setMriPk(Long mriPk) {
        this.mriPk = mriPk;
    }

    public String getMriCodigo() {
        return mriCodigo;
    }

    public void setMriCodigo(String mriCodigo) {
        this.mriCodigo = mriCodigo;
    }

    public String getMriNombre() {
        return mriNombre;
    }

    public void setMriNombre(String mriNombre) {
        this.mriNombre = mriNombre;
    }

    public String getMriNombreBusqueda() {
        return mriNombreBusqueda;
    }

    public void setMriNombreBusqueda(String mriNombreBusqueda) {
        this.mriNombreBusqueda = mriNombreBusqueda;
    }

    public Boolean getMriHabilitado() {
        return mriHabilitado;
    }

    public void setMriHabilitado(Boolean mriHabilitado) {
        this.mriHabilitado = mriHabilitado;
    }

    public LocalDateTime getMriUltModFecha() {
        return mriUltModFecha;
    }

    public void setMriUltModFecha(LocalDateTime mriUltModFecha) {
        this.mriUltModFecha = mriUltModFecha;
    }

    public String getMriUltModUsuario() {
        return mriUltModUsuario;
    }

    public void setMriUltModUsuario(String mriUltModUsuario) {
        this.mriUltModUsuario = mriUltModUsuario;
    }

    public Integer getMriVersion() {
        return mriVersion;
    }

    public void setMriVersion(Integer mriVersion) {
        this.mriVersion = mriVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.mriPk);
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
        final SgMotivoReimpresion other = (SgMotivoReimpresion) obj;
        if (!Objects.equals(this.mriPk, other.mriPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgMotivoReimpresion{" + "mriPk=" + mriPk + ", mriCodigo=" + mriCodigo + ", mriNombre=" + mriNombre + ", mriNombreBusqueda=" + mriNombreBusqueda + ", mriHabilitado=" + mriHabilitado + ", mriUltModFecha=" + mriUltModFecha + ", mriUltModUsuario=" + mriUltModUsuario + ", mriVersion=" + mriVersion + '}';
    }
    
    

}
