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
@Table(name = "sg_pregunta_cierre_anio", uniqueConstraints = {
    @UniqueConstraint(name = "pca_codigo_uk", columnNames = {"pca_codigo"})
    ,
    @UniqueConstraint(name = "pca_nombre_uk", columnNames = {"pca_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "pcaPk", scope = SgPreguntaCierreAnio.class)
@Audited
public class SgPreguntaCierreAnio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pca_pk", nullable = false)
    private Long pcaPk;

    @Size(max = 45)
    @Column(name = "pca_codigo", length = 45)
    @AtributoCodigo
    private String pcaCodigo;

    @Size(max = 255)
    @Column(name = "pca_nombre", length = 255)
    @AtributoNormalizable
    private String pcaNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "pca_nombre_busqueda", length = 255)
    private String pcaNombreBusqueda;
    
    
    @Column(name = "pca_pregunta")
    private String pcaPregunta;

    @Column(name = "pca_habilitado")
    @AtributoHabilitado
    private Boolean pcaHabilitado;

    @Column(name = "pca_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime pcaUltModFecha;

    @Size(max = 45)
    @Column(name = "pca_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String pcaUltModUsuario;

    @Column(name = "pca_version")
    @Version
    private Integer pcaVersion;

    public SgPreguntaCierreAnio() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.pcaNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.pcaNombre);
    }

    public Long getPcaPk() {
        return pcaPk;
    }

    public void setPcaPk(Long pcaPk) {
        this.pcaPk = pcaPk;
    }

    public String getPcaCodigo() {
        return pcaCodigo;
    }

    public void setPcaCodigo(String pcaCodigo) {
        this.pcaCodigo = pcaCodigo;
    }

    public String getPcaNombre() {
        return pcaNombre;
    }

    public void setPcaNombre(String pcaNombre) {
        this.pcaNombre = pcaNombre;
    }

    public String getPcaNombreBusqueda() {
        return pcaNombreBusqueda;
    }

    public void setPcaNombreBusqueda(String pcaNombreBusqueda) {
        this.pcaNombreBusqueda = pcaNombreBusqueda;
    }

    public Boolean getPcaHabilitado() {
        return pcaHabilitado;
    }

    public void setPcaHabilitado(Boolean pcaHabilitado) {
        this.pcaHabilitado = pcaHabilitado;
    }

    public LocalDateTime getPcaUltModFecha() {
        return pcaUltModFecha;
    }

    public void setPcaUltModFecha(LocalDateTime pcaUltModFecha) {
        this.pcaUltModFecha = pcaUltModFecha;
    }

    public String getPcaUltModUsuario() {
        return pcaUltModUsuario;
    }

    public void setPcaUltModUsuario(String pcaUltModUsuario) {
        this.pcaUltModUsuario = pcaUltModUsuario;
    }

    public Integer getPcaVersion() {
        return pcaVersion;
    }

    public void setPcaVersion(Integer pcaVersion) {
        this.pcaVersion = pcaVersion;
    }

    public String getPcaPregunta() {
        return pcaPregunta;
    }

    public void setPcaPregunta(String pcaPregunta) {
        this.pcaPregunta = pcaPregunta;
    }
    
    
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.pcaPk);
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
        final SgPreguntaCierreAnio other = (SgPreguntaCierreAnio) obj;
        if (!Objects.equals(this.pcaPk, other.pcaPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgPreguntaCierreAnio{" + "pcaPk=" + pcaPk + ", pcaCodigo=" + pcaCodigo + ", pcaNombre=" + pcaNombre + ", pcaNombreBusqueda=" + pcaNombreBusqueda + ", pcaHabilitado=" + pcaHabilitado + ", pcaUltModFecha=" + pcaUltModFecha + ", pcaUltModUsuario=" + pcaUltModUsuario + ", pcaVersion=" + pcaVersion + '}';
    }
    
    

}
