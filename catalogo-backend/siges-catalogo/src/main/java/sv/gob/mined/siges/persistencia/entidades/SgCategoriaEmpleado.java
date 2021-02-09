/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
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


@Entity
@Table(name = "sg_categoria_empleado", uniqueConstraints = {
    @UniqueConstraint(name = "cem_codigo_uk", columnNames = {"cem_codigo"})
    ,
    @UniqueConstraint(name = "cem_nombre_uk", columnNames = {"cem_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "cemPk", scope = SgCategoriaEmpleado.class)
public class SgCategoriaEmpleado implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cem_pk")
    private Long cemPk;
    
    @Size(max = 45)
    @Column(name = "cem_codigo",length = 45)
    @AtributoCodigo
    private String cemCodigo;
    
    @Column(name = "cem_habilitado")
    @AtributoHabilitado
    private Boolean cemHabilitado;
    
    @Size(max = 255)
    @Column(name = "cem_nombre",length = 255)
    @AtributoNormalizable
    private String cemNombre;
    
    @Size(max = 255)
    @Column(name = "cem_nombre_busqueda",length = 255)
    @AtributoNombre
    private String cemNombreBusqueda;
    
    @Column(name = "cem_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime cemUltModFecha;
    
    @Size(max = 45)
    @Column(name = "cem_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String cemUltModUsuario;
    
    @Column(name = "cem_version")
    @Version
    private Integer cemVersion;

    public SgCategoriaEmpleado() {
    }
    @PrePersist
    @PreUpdate
    public void preSave() {
        this.cemNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.cemNombre);
    }

    public SgCategoriaEmpleado(Long cemPk) {
        this.cemPk = cemPk;
    }

    public Long getCemPk() {
        return cemPk;
    }

    public void setCemPk(Long cemPk) {
        this.cemPk = cemPk;
    }

    public String getCemCodigo() {
        return cemCodigo;
    }

    public void setCemCodigo(String cemCodigo) {
        this.cemCodigo = cemCodigo;
    }

    public Boolean getCemHabilitado() {
        return cemHabilitado;
    }

    public void setCemHabilitado(Boolean cemHabilitado) {
        this.cemHabilitado = cemHabilitado;
    }

    public String getCemNombre() {
        return cemNombre;
    }

    public void setCemNombre(String cemNombre) {
        this.cemNombre = cemNombre;
    }

    public String getCemNombreBusqueda() {
        return cemNombreBusqueda;
    }

    public void setCemNombreBusqueda(String cemNombreBusqueda) {
        this.cemNombreBusqueda = cemNombreBusqueda;
    }

    public LocalDateTime getCemUltModFecha() {
        return cemUltModFecha;
    }

    public void setCemUltModFecha(LocalDateTime cemUltModFecha) {
        this.cemUltModFecha = cemUltModFecha;
    }

    public String getCemUltModUsuario() {
        return cemUltModUsuario;
    }

    public void setCemUltModUsuario(String cemUltModUsuario) {
        this.cemUltModUsuario = cemUltModUsuario;
    }

    public Integer getCemVersion() {
        return cemVersion;
    }

    public void setCemVersion(Integer cemVersion) {
        this.cemVersion = cemVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cemPk != null ? cemPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgCategoriaEmpleado)) {
            return false;
        }
        SgCategoriaEmpleado other = (SgCategoriaEmpleado) object;
        if ((this.cemPk == null && other.cemPk != null) || (this.cemPk != null && !this.cemPk.equals(other.cemPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgCategoriaEmpleado[ cemPk=" + cemPk + " ]";
    }
    
}
