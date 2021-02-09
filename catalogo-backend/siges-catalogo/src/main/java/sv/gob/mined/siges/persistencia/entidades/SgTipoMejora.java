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
@Table(name = "sg_inf_tipo_mejora", uniqueConstraints = {
    @UniqueConstraint(name = "tme_codigo_uk", columnNames = {"tme_codigo"})
    ,
    @UniqueConstraint(name = "tme_nombre_uk", columnNames = {"tme_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tmePk", scope = SgTipoMejora.class)
@Audited
public class SgTipoMejora implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tme_pk", nullable = false)
    private Long tmePk;

    @Size(max = 45)
    @Column(name = "tme_codigo", length = 45)
    @AtributoCodigo
    private String tmeCodigo;

    @Size(max = 255)
    @Column(name = "tme_nombre", length = 255)
    @AtributoNormalizable
    private String tmeNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "tme_nombre_busqueda", length = 255)
    private String tmeNombreBusqueda;

    @Column(name = "tme_habilitado")
    @AtributoHabilitado
    private Boolean tmeHabilitado;

    @Column(name = "tme_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime tmeUltModFecha;

    @Size(max = 45)
    @Column(name = "tme_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String tmeUltModUsuario;

    @Column(name = "tme_version")
    @Version
    private Integer tmeVersion;
    
    @Column(name = "tme_aplica_inmueble")
    private Boolean tmeAplicaInmueble;
    
    @Column(name = "tme_aplica_edificio")
    private Boolean tmeAplicaEdificio;

    public SgTipoMejora() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.tmeNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.tmeNombre);
    }

    public Long getTmePk() {
        return tmePk;
    }

    public void setTmePk(Long tmePk) {
        this.tmePk = tmePk;
    }

    public String getTmeCodigo() {
        return tmeCodigo;
    }

    public void setTmeCodigo(String tmeCodigo) {
        this.tmeCodigo = tmeCodigo;
    }

    public String getTmeNombre() {
        return tmeNombre;
    }

    public void setTmeNombre(String tmeNombre) {
        this.tmeNombre = tmeNombre;
    }

    public String getTmeNombreBusqueda() {
        return tmeNombreBusqueda;
    }

    public void setTmeNombreBusqueda(String tmeNombreBusqueda) {
        this.tmeNombreBusqueda = tmeNombreBusqueda;
    }

    public Boolean getTmeHabilitado() {
        return tmeHabilitado;
    }

    public void setTmeHabilitado(Boolean tmeHabilitado) {
        this.tmeHabilitado = tmeHabilitado;
    }

    public LocalDateTime getTmeUltModFecha() {
        return tmeUltModFecha;
    }

    public void setTmeUltModFecha(LocalDateTime tmeUltModFecha) {
        this.tmeUltModFecha = tmeUltModFecha;
    }

    public String getTmeUltModUsuario() {
        return tmeUltModUsuario;
    }

    public void setTmeUltModUsuario(String tmeUltModUsuario) {
        this.tmeUltModUsuario = tmeUltModUsuario;
    }

    public Integer getTmeVersion() {
        return tmeVersion;
    }

    public void setTmeVersion(Integer tmeVersion) {
        this.tmeVersion = tmeVersion;
    }

    public Boolean getTmeAplicaInmueble() {
        return tmeAplicaInmueble;
    }

    public void setTmeAplicaInmueble(Boolean tmeAplicaInmueble) {
        this.tmeAplicaInmueble = tmeAplicaInmueble;
    }

    public Boolean getTmeAplicaEdificio() {
        return tmeAplicaEdificio;
    }

    public void setTmeAplicaEdificio(Boolean tmeAplicaEdificio) {
        this.tmeAplicaEdificio = tmeAplicaEdificio;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.tmePk);
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
        final SgTipoMejora other = (SgTipoMejora) obj;
        if (!Objects.equals(this.tmePk, other.tmePk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgTipoMejora{" + "tmePk=" + tmePk + ", tmeCodigo=" + tmeCodigo + ", tmeNombre=" + tmeNombre + ", tmeNombreBusqueda=" + tmeNombreBusqueda + ", tmeHabilitado=" + tmeHabilitado + ", tmeUltModFecha=" + tmeUltModFecha + ", tmeUltModUsuario=" + tmeUltModUsuario + ", tmeVersion=" + tmeVersion + '}';
    }
    
    

}
