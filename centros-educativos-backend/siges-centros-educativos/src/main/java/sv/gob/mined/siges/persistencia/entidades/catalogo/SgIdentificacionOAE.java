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
@Table(name = "sg_identificacion_oae", uniqueConstraints = {
    @UniqueConstraint(name = "ioa_codigo_uk", columnNames = {"ioa_codigo"})
    ,
    @UniqueConstraint(name = "ioa_nombre_uk", columnNames = {"ioa_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ioaPk", scope = SgIdentificacionOAE.class)
@Audited
public class SgIdentificacionOAE implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ioa_pk", nullable = false)
    private Long ioaPk;

    @Size(max = 45)
    @Column(name = "ioa_codigo", length = 45)
    @AtributoCodigo
    private String ioaCodigo;

    @Size(max = 255)
    @Column(name = "ioa_nombre", length = 255)
    @AtributoNormalizable
    private String ioaNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "ioa_nombre_busqueda", length = 255)
    private String ioaNombreBusqueda;

    @Column(name = "ioa_habilitado")
    @AtributoHabilitado
    private Boolean ioaHabilitado;

    
    @Column(name = "ioa_es_obligatorio")
    private Boolean ioaEsObligatorio;
    
    @Column(name = "ioa_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime ioaUltModFecha;

    @Size(max = 45)
    @Column(name = "ioa_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String ioaUltModUsuario;

    @Column(name = "ioa_version")
    @Version
    private Integer ioaVersion;

    public SgIdentificacionOAE() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.ioaNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.ioaNombre);
    }

    public Long getIoaPk() {
        return ioaPk;
    }

    public void setIoaPk(Long ioaPk) {
        this.ioaPk = ioaPk;
    }

    public String getIoaCodigo() {
        return ioaCodigo;
    }

    public void setIoaCodigo(String ioaCodigo) {
        this.ioaCodigo = ioaCodigo;
    }

    public String getIoaNombre() {
        return ioaNombre;
    }

    public void setIoaNombre(String ioaNombre) {
        this.ioaNombre = ioaNombre;
    }

    public String getIoaNombreBusqueda() {
        return ioaNombreBusqueda;
    }

    public void setIoaNombreBusqueda(String ioaNombreBusqueda) {
        this.ioaNombreBusqueda = ioaNombreBusqueda;
    }

    public Boolean getIoaHabilitado() {
        return ioaHabilitado;
    }

    public void setIoaHabilitado(Boolean ioaHabilitado) {
        this.ioaHabilitado = ioaHabilitado;
    }

    public LocalDateTime getIoaUltModFecha() {
        return ioaUltModFecha;
    }

    public void setIoaUltModFecha(LocalDateTime ioaUltModFecha) {
        this.ioaUltModFecha = ioaUltModFecha;
    }

    public String getIoaUltModUsuario() {
        return ioaUltModUsuario;
    }

    public void setIoaUltModUsuario(String ioaUltModUsuario) {
        this.ioaUltModUsuario = ioaUltModUsuario;
    }

    public Integer getIoaVersion() {
        return ioaVersion;
    }

    public void setIoaVersion(Integer ioaVersion) {
        this.ioaVersion = ioaVersion;
    }

    public Boolean getIoaEsObligatorio() {
        return ioaEsObligatorio;
    }

    public void setIoaEsObligatorio(Boolean ioaEsObligatorio) {
        this.ioaEsObligatorio = ioaEsObligatorio;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.ioaPk);
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
        final SgIdentificacionOAE other = (SgIdentificacionOAE) obj;
        if (!Objects.equals(this.ioaPk, other.ioaPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgIdentificacionOAE{" + "ioaPk=" + ioaPk + ", ioaCodigo=" + ioaCodigo + ", ioaNombre=" + ioaNombre + ", ioaNombreBusqueda=" + ioaNombreBusqueda + ", ioaHabilitado=" + ioaHabilitado + ", ioaUltModFecha=" + ioaUltModFecha + ", ioaUltModUsuario=" + ioaUltModUsuario + ", ioaVersion=" + ioaVersion + '}';
    }
    
    

}
