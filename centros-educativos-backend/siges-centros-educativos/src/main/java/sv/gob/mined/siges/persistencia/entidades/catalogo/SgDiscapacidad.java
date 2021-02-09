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
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_discapacidades", uniqueConstraints = {
    @UniqueConstraint(name = "dis_codigo_uk", columnNames = {"dis_codigo"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "disPk", scope = SgDiscapacidad.class)
public class SgDiscapacidad implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "dis_pk")
    private Long disPk;

    @Size(max = 4)
    @Column(name = "dis_codigo", length = 4)
    @AtributoCodigo
    private String disCodigo;

    @Size(max = 255)
    @Column(name = "dis_nombre", length = 255)
    @AtributoNormalizable
    private String disNombre;

    @Size(max = 255)
    @Column(name = "dis_nombre_busqueda", length = 255)
    @AtributoNombre
    private String disNombreBusqueda;

    @Size(max = 255)
    @Column(name = "dis_descripcion", length = 255)
    private String disDescripcion;

    @Column(name = "dis_habilitado")
    @AtributoHabilitado
    private Boolean disHabilitado;

    @Column(name = "dis_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime disUltModFecha;

    @Size(max = 45)
    @Column(name = "dis_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String disUltModUsuario;

    @Column(name = "dis_version")
    @Version
    private Integer disVersion;

    public SgDiscapacidad() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.disNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.disNombre);
    }

    public String getDisNombreBusqueda() {
        return disNombreBusqueda;
    }

    public void setDisNombreBusqueda(String disNombreBusqueda) {
        this.disNombreBusqueda = disNombreBusqueda;
    }

    public Long getDisPk() {
        return disPk;
    }

    public void setDisPk(Long disPk) {
        this.disPk = disPk;
    }

    public String getDisCodigo() {
        return disCodigo;
    }

    public void setDisCodigo(String disCodigo) {
        this.disCodigo = disCodigo;
    }

    public String getDisNombre() {
        return disNombre;
    }

    public void setDisNombre(String disNombre) {
        this.disNombre = disNombre;
    }

    public String getDisDescripcion() {
        return disDescripcion;
    }

    public void setDisDescripcion(String disDescripcion) {
        this.disDescripcion = disDescripcion;
    }

    public Boolean getDisHabilitado() {
        return disHabilitado;
    }

    public void setDisHabilitado(Boolean disHabilitado) {
        this.disHabilitado = disHabilitado;
    }

    public LocalDateTime getDisUltModFecha() {
        return disUltModFecha;
    }

    public void setDisUltModFecha(LocalDateTime disUltModFecha) {
        this.disUltModFecha = disUltModFecha;
    }

    public String getDisUltModUsuario() {
        return disUltModUsuario;
    }

    public void setDisUltModUsuario(String disUltModUsuario) {
        this.disUltModUsuario = disUltModUsuario;
    }

    public Integer getDisVersion() {
        return disVersion;
    }

    public void setDisVersion(Integer disVersion) {
        this.disVersion = disVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (disPk != null ? disPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgDiscapacidad)) {
            return false;
        }
        SgDiscapacidad other = (SgDiscapacidad) object;
        if ((this.disPk == null && other.disPk != null) || (this.disPk != null && !this.disPk.equals(other.disPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SigesDiscapacidad[ disPk=" + disPk + " ]";
    }

}
