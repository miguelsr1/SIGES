/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoInicializarColeccion;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;

@Entity
@Table(name = "sg_roles", uniqueConstraints = {
    @UniqueConstraint(name = "rol_codigo_uk", columnNames = {"rol_codigo"})}, 
        schema = Constantes.SCHEMA_SEGURIDAD)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "rolPk", scope = SgRol.class)
@Audited
public class SgRol implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rol_pk")
    private Long rolPk;
    
    @Size(max = 10)
    @Column(name = "rol_codigo",length = 10)
    @AtributoCodigo
    private String rolCodigo;
    
    @Size(max = 255)
    @Column(name = "rol_nombre",length = 255)
    @AtributoNombre
    private String rolNombre;
    
    @Size(max = 500)
    @Column(name = "rol_descripcion",length = 500)
    private String rolDescripcion;
    
    @Column(name = "rol_habilitado")
    @AtributoHabilitado
    private Boolean rolHabilitado;
    
    @Column(name = "rol_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime rolUltModFecha;
    
    @Size(max = 45)
    @Column(name = "rol_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String rolUltModUsuario;
    
    @Column(name = "rol_version")
    @Version
    private Integer rolVersion;
    
    @OneToMany(mappedBy = "ropRol", cascade = CascadeType.ALL, orphanRemoval = true)
    @AtributoInicializarColeccion
    private List<SgRolOperacion> rolRolOperacion;
    
    @Column(name = "rol_delegable")
    private Boolean rolDelegable;
    
    

    public SgRol() {
    }
    
    public Long getRolPk() {
        return rolPk;
    }

    public void setRolPk(Long rolPk) {
        this.rolPk = rolPk;
    }

    public String getRolCodigo() {
        return rolCodigo;
    }

    public void setRolCodigo(String rolCodigo) {
        this.rolCodigo = rolCodigo;
    }

    public String getRolNombre() {
        return rolNombre;
    }

    public void setRolNombre(String rolNombre) {
        this.rolNombre = rolNombre;
    }

    public String getRolDescripcion() {
        return rolDescripcion;
    }

    public void setRolDescripcion(String rolDescripcion) {
        this.rolDescripcion = rolDescripcion;
    }

    public Boolean getRolHabilitado() {
        return rolHabilitado;
    }

    public void setRolHabilitado(Boolean rolHabilitado) {
        this.rolHabilitado = rolHabilitado;
    }

    public LocalDateTime getRolUltModFecha() {
        return rolUltModFecha;
    }

    public void setRolUltModFecha(LocalDateTime rolUltModFecha) {
        this.rolUltModFecha = rolUltModFecha;
    }

    public String getRolUltModUsuario() {
        return rolUltModUsuario;
    }

    public void setRolUltModUsuario(String rolUltModUsuario) {
        this.rolUltModUsuario = rolUltModUsuario;
    }

    public Integer getRolVersion() {
        return rolVersion;
    }

    public void setRolVersion(Integer rolVersion) {
        this.rolVersion = rolVersion;
    }

    public List<SgRolOperacion> getRolRolOperacion() {
        return rolRolOperacion;
    }

    public void setRolRolOperacion(List<SgRolOperacion> rolRolOperacion) {
        this.rolRolOperacion = rolRolOperacion;
    }

    public Boolean getRolDelegable() {
        return rolDelegable;
    }

    public void setRolDelegable(Boolean rolDelegable) {
        this.rolDelegable = rolDelegable;
    }

   


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rolPk != null ? rolPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgRol)) {
            return false;
        }
        SgRol other = (SgRol) object;
        if ((this.rolPk == null && other.rolPk != null) || (this.rolPk != null && !this.rolPk.equals(other.rolPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgRol[ rolPk=" + rolPk + " ]";
    }
    
}
