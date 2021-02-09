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
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.UniqueConstraint;
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
@Table(name = "sg_modalidades_atencion",uniqueConstraints = {
    @UniqueConstraint(name = "mat_codigo_uk", columnNames = {"mat_codigo"})
    ,
    @UniqueConstraint(name = "mat_nombre_uk", columnNames = {"mat_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "matPk", scope = SgModalidadAtencion.class) 
public class SgModalidadAtencion implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "mat_pk")
    private Long matPk;
    
    @Size(max = 10)
    @Column(name = "mat_codigo",length = 10)
    @AtributoCodigo
    private String matCodigo;
    
    @Size(max = 255)
    @Column(name = "mat_nombre",length = 255)
    @AtributoNormalizable
    private String matNombre;
    
    @Size(max = 255)
    @Column(name = "mat_nombre_busqueda",length = 255)
    @AtributoNombre
    private String matNombreBusqueda;
    
    @Size(max = 500)
    @Column(name = "mat_descripcion",length = 500)
    private String matDescripcion;
    
    @Column(name = "mat_habilitado")
    @AtributoHabilitado
    private Boolean matHabilitado;
    
    @Column(name = "mat_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime matUltModFecha;
    
    @Size(max = 45)
    @Column(name = "mat_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String matUltModUsuario;
    
    @Column(name = "mat_version")
    @Version
    private Integer matVersion;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "smoModalidadFk", fetch = FetchType.LAZY)
    private List<SgSubModalidadAtencion> matSubmodalidades;

    public SgModalidadAtencion() {
    }
    
    
    @PrePersist
    @PreUpdate
    public void preSave() {
        this.matNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.matNombre);
    }

    public Long getMatPk() {
        return matPk;
    }

    public void setMatPk(Long matPk) {
        this.matPk = matPk;
    }

    public String getMatCodigo() {
        return matCodigo;
    }

    public void setMatCodigo(String matCodigo) {
        this.matCodigo = matCodigo;
    }

    public String getMatNombre() {
        return matNombre;
    }

    public void setMatNombre(String matNombre) {
        this.matNombre = matNombre;
    }

    public String getMatNombreBusqueda() {
        return matNombreBusqueda;
    }

    public void setMatNombreBusqueda(String matNombreBusqueda) {
        this.matNombreBusqueda = matNombreBusqueda;
    }

    public String getMatDescripcion() {
        return matDescripcion;
    }

    public void setMatDescripcion(String matDescripcion) {
        this.matDescripcion = matDescripcion;
    }

    public Boolean getMatHabilitado() {
        return matHabilitado;
    }

    public void setMatHabilitado(Boolean matHabilitado) {
        this.matHabilitado = matHabilitado;
    }

    public LocalDateTime getMatUltModFecha() {
        return matUltModFecha;
    }

    public void setMatUltModFecha(LocalDateTime matUltModFecha) {
        this.matUltModFecha = matUltModFecha;
    }

    public String getMatUltModUsuario() {
        return matUltModUsuario;
    }

    public void setMatUltModUsuario(String matUltModUsuario) {
        this.matUltModUsuario = matUltModUsuario;
    }

    public Integer getMatVersion() {
        return matVersion;
    }

    public void setMatVersion(Integer matVersion) {
        this.matVersion = matVersion;
    }

    public List<SgSubModalidadAtencion> getMatSubmodalidades() {
        return matSubmodalidades;
    }

    public void setMatSubmodalidades(List<SgSubModalidadAtencion> matSubmodalidades) {
        this.matSubmodalidades = matSubmodalidades;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (matPk != null ? matPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgModalidadAtencion)) {
            return false;
        }
        SgModalidadAtencion other = (SgModalidadAtencion) object;
        if ((this.matPk == null && other.matPk != null) || (this.matPk != null && !this.matPk.equals(other.matPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgModalidadAtencion[ matPk=" + matPk + " ]";
    }
    
}
