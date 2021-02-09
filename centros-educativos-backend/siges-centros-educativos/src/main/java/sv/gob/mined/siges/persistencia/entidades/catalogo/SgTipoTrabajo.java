/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.catalogo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
import java.io.Serializable;
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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_tipos_trabajo", uniqueConstraints = {
    @UniqueConstraint(name = "ttr_codigo_uk", columnNames = {"ttr_codigo"})
    ,
    @UniqueConstraint(name = "ttr_nombre_uk", columnNames = {"ttr_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "ttrPk", scope = SgTipoTrabajo.class)
public class SgTipoTrabajo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ttr_pk")
    private Long ttrPk;

    @Size(max = 4)
    @Column(name = "ttr_codigo", length = 4)
    @AtributoCodigo
    private String ttrCodigo;

    @Size(max = 255)
    @Column(name = "ttr_nombre", length = 255)
    @AtributoNombre
    private String ttrNombre;

    @Size(max = 255)
    @Column(name = "ttr_nombre_busqueda", length = 255)
    @AtributoNombre
    private String ttrNombreBusqueda;

    @Column(name = "ttr_habilitado")
    @AtributoHabilitado
    private Boolean ttrHabilitado;

    @Column(name = "ttr_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime ttrUltModFecha;

    @Size(max = 45)
    @Column(name = "ttr_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String ttrUltModUsuario;

    @Column(name = "ttr_version")
    @Version
    private Integer ttrVersion;

    public SgTipoTrabajo() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.ttrNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.ttrNombre);
    }

    public String getTtrNombreBusqueda() {
        return ttrNombreBusqueda;
    }

    public void setTtrNombreBusqueda(String ttrNombreBusqueda) {
        this.ttrNombreBusqueda = ttrNombreBusqueda;
    }

    public Long getTtrPk() {
        return ttrPk;
    }

    public void setTtrPk(Long ttrPk) {
        this.ttrPk = ttrPk;
    }

    public String getTtrCodigo() {
        return ttrCodigo;
    }

    public void setTtrCodigo(String ttrCodigo) {
        this.ttrCodigo = ttrCodigo;
    }

    public String getTtrNombre() {
        return ttrNombre;
    }

    public void setTtrNombre(String ttrNombre) {
        this.ttrNombre = ttrNombre;
    }

    public Boolean getTtrHabilitado() {
        return ttrHabilitado;
    }

    public void setTtrHabilitado(Boolean ttrHabilitado) {
        this.ttrHabilitado = ttrHabilitado;
    }

    public LocalDateTime getTtrUltModFecha() {
        return ttrUltModFecha;
    }

    public void setTtrUltModFecha(LocalDateTime ttrUltModFecha) {
        this.ttrUltModFecha = ttrUltModFecha;
    }

    public String getTtrUltModUsuario() {
        return ttrUltModUsuario;
    }

    public void setTtrUltModUsuario(String ttrUltModUsuario) {
        this.ttrUltModUsuario = ttrUltModUsuario;
    }

    public Integer getTtrVersion() {
        return ttrVersion;
    }

    public void setTtrVersion(Integer ttrVersion) {
        this.ttrVersion = ttrVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ttrPk != null ? ttrPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgTipoTrabajo)) {
            return false;
        }
        SgTipoTrabajo other = (SgTipoTrabajo) object;
        if ((this.ttrPk == null && other.ttrPk != null) || (this.ttrPk != null && !this.ttrPk.equals(other.ttrPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SigesTipoTrabajo[ ttrPk=" + ttrPk + " ]";
    }

}
