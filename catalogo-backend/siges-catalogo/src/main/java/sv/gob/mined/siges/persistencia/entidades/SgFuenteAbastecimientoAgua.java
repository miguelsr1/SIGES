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
@Table(name = "sg_fuentes_abastecimiento_agua", schema = Constantes.SCHEMA_CATALOGO, uniqueConstraints = {
    @UniqueConstraint(name = "faa_codigo_uk", columnNames = {"faa_codigo"})
    ,
    @UniqueConstraint(name = "faa_nombre_uk", columnNames = {"faa_nombre"})})
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "faaPk", scope = SgFuenteAbastecimientoAgua.class)
@Audited
public class SgFuenteAbastecimientoAgua implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "faa_pk", nullable = false)
    private Long faaPk;

    @Size(max = 45)
    @Column(name = "faa_codigo", length = 45)
    @AtributoCodigo
    private String faaCodigo;

    @Size(max = 255)
    @Column(name = "faa_nombre", length = 255)
    @AtributoNormalizable
    private String faaNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "faa_nombre_busqueda", length = 255)
    private String faaNombreBusqueda;

    @Column(name = "faa_habilitado")
    @AtributoHabilitado
    private Boolean faaHabilitado;

    @Column(name = "faa_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime faaUltModFecha;

    @Size(max = 45)
    @Column(name = "faa_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String faaUltModUsuario;

    @Column(name = "faa_version")
    @Version
    private Integer faaVersion;

    public SgFuenteAbastecimientoAgua() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.faaNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.faaNombre);
    }

    public Long getFaaPk() {
        return faaPk;
    }

    public void setFaaPk(Long faaPk) {
        this.faaPk = faaPk;
    }

    public String getFaaCodigo() {
        return faaCodigo;
    }

    public void setFaaCodigo(String faaCodigo) {
        this.faaCodigo = faaCodigo;
    }

    public String getFaaNombre() {
        return faaNombre;
    }

    public void setFaaNombre(String faaNombre) {
        this.faaNombre = faaNombre;
    }

    public String getFaaNombreBusqueda() {
        return faaNombreBusqueda;
    }

    public void setFaaNombreBusqueda(String faaNombreBusqueda) {
        this.faaNombreBusqueda = faaNombreBusqueda;
    }

    public Boolean getFaaHabilitado() {
        return faaHabilitado;
    }

    public void setFaaHabilitado(Boolean faaHabilitado) {
        this.faaHabilitado = faaHabilitado;
    }

    public LocalDateTime getFaaUltModFecha() {
        return faaUltModFecha;
    }

    public void setFaaUltModFecha(LocalDateTime faaUltModFecha) {
        this.faaUltModFecha = faaUltModFecha;
    }

    public String getFaaUltModUsuario() {
        return faaUltModUsuario;
    }

    public void setFaaUltModUsuario(String faaUltModUsuario) {
        this.faaUltModUsuario = faaUltModUsuario;
    }

    public Integer getFaaVersion() {
        return faaVersion;
    }

    public void setFaaVersion(Integer faaVersion) {
        this.faaVersion = faaVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.faaPk);
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
        final SgFuenteAbastecimientoAgua other = (SgFuenteAbastecimientoAgua) obj;
        if (!Objects.equals(this.faaPk, other.faaPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgFuenteAbastecimientoAgua{" + "faaPk=" + faaPk + ", faaCodigo=" + faaCodigo + ", faaNombre=" + faaNombre + ", faaNombreBusqueda=" + faaNombreBusqueda + ", faaHabilitado=" + faaHabilitado + ", faaUltModFecha=" + faaUltModFecha + ", faaUltModUsuario=" + faaUltModUsuario + ", faaVersion=" + faaVersion + '}';
    }
    
    

}
