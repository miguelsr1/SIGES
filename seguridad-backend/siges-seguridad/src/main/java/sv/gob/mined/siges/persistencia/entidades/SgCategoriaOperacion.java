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
import sv.gob.mined.siges.persistencia.annotations.AtributoDescripcion;
import sv.gob.mined.siges.persistencia.annotations.AtributoInicializarColeccion;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;

@Entity
@Table(name = "sg_categorias_operacion", uniqueConstraints = {
    @UniqueConstraint(name = "cop_codigo_uk", columnNames = {"cop_codigo"})}, 
        schema = Constantes.SCHEMA_SEGURIDAD)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "copPk", scope = SgCategoriaOperacion.class)
@Audited
public class SgCategoriaOperacion implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cop_pk")
    private Long copPk;
    
    @Size(max = 10)
    @Column(name = "cop_codigo",length = 10)
    @AtributoCodigo
    private String copCodigo;
    
    @Size(max = 255)
    @AtributoNombre
    @Column(name = "cop_nombre",length = 255)
    private String copNombre;
    
    @Size(max = 500)
    @AtributoDescripcion
    @Column(name = "cop_descripcion",length = 500)
    private String copDescripcion;
    
    @Column(name = "cop_habilitado")
    @AtributoHabilitado
    private Boolean copHabilitado;
    
    @Column(name = "cop_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime copUltModFecha;
    
    @Size(max = 45)
    @Column(name = "cop_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String copUltModUsuario;
    
    @Column(name = "cop_version")
    @Version
    private Integer copVersion;
    
    @OneToMany(mappedBy = "opeCategoriaOperacion")
    @AtributoInicializarColeccion
    private List<SgOperacion> copOperacion;

    public SgCategoriaOperacion() {
    }
    
    public Long getCopPk() {
        return copPk;
    }

    public void setCopPk(Long copPk) {
        this.copPk = copPk;
    }

    public String getCopCodigo() {
        return copCodigo;
    }

    public void setCopCodigo(String copCodigo) {
        this.copCodigo = copCodigo;
    }

    public String getCopNombre() {
        return copNombre;
    }

    public void setCopNombre(String copNombre) {
        this.copNombre = copNombre;
    }

    public String getCopDescripcion() {
        return copDescripcion;
    }

    public void setCopDescripcion(String copDescripcion) {
        this.copDescripcion = copDescripcion;
    }

    public Boolean getCopHabilitado() {
        return copHabilitado;
    }

    public void setCopHabilitado(Boolean copHabilitado) {
        this.copHabilitado = copHabilitado;
    }

    public LocalDateTime getCopUltModFecha() {
        return copUltModFecha;
    }

    public void setCopUltModFecha(LocalDateTime copUltModFecha) {
        this.copUltModFecha = copUltModFecha;
    }

    public String getCopUltModUsuario() {
        return copUltModUsuario;
    }

    public void setCopUltModUsuario(String copUltModUsuario) {
        this.copUltModUsuario = copUltModUsuario;
    }

    public Integer getCopVersion() {
        return copVersion;
    }

    public void setCopVersion(Integer copVersion) {
        this.copVersion = copVersion;
    }

    public List<SgOperacion> getCopOperacion() {
        return copOperacion;
    }

    public void setCopOperacion(List<SgOperacion> copOperacion) {
        this.copOperacion = copOperacion;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (copPk != null ? copPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgCategoriaOperacion)) {
            return false;
        }
        SgCategoriaOperacion other = (SgCategoriaOperacion) object;
        if ((this.copPk == null && other.copPk != null) || (this.copPk != null && !this.copPk.equals(other.copPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgCategoriaOperacion[ copPk=" + copPk + " ]";
    }
    
}
