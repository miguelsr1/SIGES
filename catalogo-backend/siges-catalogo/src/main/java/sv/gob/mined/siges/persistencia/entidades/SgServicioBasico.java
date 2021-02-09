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
@Table(name = "sg_servicios_basicos", uniqueConstraints = {
    @UniqueConstraint(name = "sba_codigo_uk", columnNames = {"sba_codigo"})
    ,
    @UniqueConstraint(name = "sba_nombre_uk", columnNames = {"sba_nombre"})},schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "sbaPk", scope = SgServicioBasico.class)
@Audited
public class SgServicioBasico implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "sba_pk", nullable = false)
    private Long sbaPk;

    @Size(max = 45)
    @Column(name = "sba_codigo", length = 45)
    @AtributoCodigo
    private String sbaCodigo;

    @Size(max = 255)
    @Column(name = "sba_nombre", length = 255)
    @AtributoNormalizable
    private String sbaNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "sba_nombre_busqueda", length = 255)
    private String sbaNombreBusqueda;

    @Column(name = "sba_habilitado")
    @AtributoHabilitado
    private Boolean sbaHabilitado;
    
    @Column(name = "sba_aplica_terreno")
    private Boolean sbaAplicaTerreno;
    
    @Column(name = "sba_aplica_edificio")
    private Boolean sbaAplicaEdificio;
    
    @Column(name = "sba_aplica_aula")
    private Boolean sbaAplicaAula;
    
    @Size(max = 500)
    @Column(name = "sba_descripcion", length = 500)
    @AtributoNormalizable
    private String sbaDescripcion;

    @Column(name = "sba_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime sbaUltModFecha;

    @Size(max = 45)
    @Column(name = "sba_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String sbaUltModUsuario;

    @Column(name = "sba_version")
    @Version
    private Integer sbaVersion;

    public SgServicioBasico() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.sbaNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.sbaNombre);
    }

    public Long getSbaPk() {
        return sbaPk;
    }

    public void setSbaPk(Long sbaPk) {
        this.sbaPk = sbaPk;
    }

    public String getSbaCodigo() {
        return sbaCodigo;
    }

    public void setSbaCodigo(String sbaCodigo) {
        this.sbaCodigo = sbaCodigo;
    }

    public String getSbaNombre() {
        return sbaNombre;
    }

    public void setSbaNombre(String sbaNombre) {
        this.sbaNombre = sbaNombre;
    }

    public String getSbaNombreBusqueda() {
        return sbaNombreBusqueda;
    }

    public void setSbaNombreBusqueda(String sbaNombreBusqueda) {
        this.sbaNombreBusqueda = sbaNombreBusqueda;
    }

    public Boolean getSbaHabilitado() {
        return sbaHabilitado;
    }

    public void setSbaHabilitado(Boolean sbaHabilitado) {
        this.sbaHabilitado = sbaHabilitado;
    }

    public LocalDateTime getSbaUltModFecha() {
        return sbaUltModFecha;
    }

    public void setSbaUltModFecha(LocalDateTime sbaUltModFecha) {
        this.sbaUltModFecha = sbaUltModFecha;
    }

    public String getSbaUltModUsuario() {
        return sbaUltModUsuario;
    }

    public void setSbaUltModUsuario(String sbaUltModUsuario) {
        this.sbaUltModUsuario = sbaUltModUsuario;
    }

    public Integer getSbaVersion() {
        return sbaVersion;
    }

    public void setSbaVersion(Integer sbaVersion) {
        this.sbaVersion = sbaVersion;
    }

    public Boolean getSbaAplicaTerreno() {
        return sbaAplicaTerreno;
    }

    public void setSbaAplicaTerreno(Boolean sbaAplicaTerreno) {
        this.sbaAplicaTerreno = sbaAplicaTerreno;
    }

    public Boolean getSbaAplicaEdificio() {
        return sbaAplicaEdificio;
    }

    public void setSbaAplicaEdificio(Boolean sbaAplicaEdificio) {
        this.sbaAplicaEdificio = sbaAplicaEdificio;
    }

    public Boolean getSbaAplicaAula() {
        return sbaAplicaAula;
    }

    public void setSbaAplicaAula(Boolean sbaAplicaAula) {
        this.sbaAplicaAula = sbaAplicaAula;
    }

    public String getSbaDescripcion() {
        return sbaDescripcion;
    }

    public void setSbaDescripcion(String sbaDescripcion) {
        this.sbaDescripcion = sbaDescripcion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.sbaPk);
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
        final SgServicioBasico other = (SgServicioBasico) obj;
        if (!Objects.equals(this.sbaPk, other.sbaPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgServicioBasico{" + "sbaPk=" + sbaPk + ", sbaCodigo=" + sbaCodigo + ", sbaNombre=" + sbaNombre + ", sbaNombreBusqueda=" + sbaNombreBusqueda + ", sbaHabilitado=" + sbaHabilitado + ", sbaUltModFecha=" + sbaUltModFecha + ", sbaUltModUsuario=" + sbaUltModUsuario + ", sbaVersion=" + sbaVersion + '}';
    }
    
    

}
