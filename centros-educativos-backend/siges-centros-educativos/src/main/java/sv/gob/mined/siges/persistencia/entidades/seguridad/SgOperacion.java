/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.seguridad;

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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

@Entity
@Table(name = "sg_operaciones", uniqueConstraints = {
    @UniqueConstraint(name = "ope_codigo_uk", columnNames = {"ope_codigo"})}, 
        schema = Constantes.SCHEMA_SEGURIDAD)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "opePk", scope = SgOperacion.class)
@Audited
public class SgOperacion implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ope_pk")
    private Long opePk;
    
    @Size(max = 10)
    @Column(name = "ope_codigo",length = 10)
    @AtributoCodigo
    private String opeCodigo;
    
    @Size(max = 255)
    @Column(name = "ope_nombre",length = 255)
    @AtributoNombre
    private String opeNombre;

    
    @Column(name = "ope_descripcion")
    private String opeDescripcion;
    
    @Column(name = "ope_habilitado")
    @AtributoHabilitado
    private Boolean opeHabilitado;
    
    @Column(name = "ope_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime opeUltModFecha;
    
    @Size(max = 45)
    @Column(name = "ope_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String opeUltModUsuario;
    
    @Column(name = "ope_version")
    @Version
    private Integer opeVersion;
    
    @OneToMany(mappedBy = "ropOperacion")
    private List<SgRolOperacion> opeRolOperacion;
    
    @JoinColumn(name = "ope_categoria_operacion_fk", referencedColumnName = "cop_pk")
    @ManyToOne
    private SgCategoriaOperacion opeCategoriaOperacion;

    public SgOperacion() {
    }

    public Long getOpePk() {
        return opePk;
    }

    public void setOpePk(Long opePk) {
        this.opePk = opePk;
    }

    public String getOpeCodigo() {
        return opeCodigo;
    }

    public void setOpeCodigo(String opeCodigo) {
        this.opeCodigo = opeCodigo;
    }

    public String getOpeNombre() {
        return opeNombre;
    }

    public void setOpeNombre(String opeNombre) {
        this.opeNombre = opeNombre;
    }

    public String getOpeDescripcion() {
        return opeDescripcion;
    }

    public void setOpeDescripcion(String opeDescripcion) {
        this.opeDescripcion = opeDescripcion;
    }

    public Boolean getOpeHabilitado() {
        return opeHabilitado;
    }

    public void setOpeHabilitado(Boolean opeHabilitado) {
        this.opeHabilitado = opeHabilitado;
    }

    public LocalDateTime getOpeUltModFecha() {
        return opeUltModFecha;
    }

    public void setOpeUltModFecha(LocalDateTime opeUltModFecha) {
        this.opeUltModFecha = opeUltModFecha;
    }

    public String getOpeUltModUsuario() {
        return opeUltModUsuario;
    }

    public void setOpeUltModUsuario(String opeUltModUsuario) {
        this.opeUltModUsuario = opeUltModUsuario;
    }

    public Integer getOpeVersion() {
        return opeVersion;
    }

    public void setOpeVersion(Integer opeVersion) {
        this.opeVersion = opeVersion;
    }

    public List<SgRolOperacion> getOpeRolOperacion() {
        return opeRolOperacion;
    }

    public void setOpeRolOperacion(List<SgRolOperacion> opeRolOperacion) {
        this.opeRolOperacion = opeRolOperacion;
    }

    public SgCategoriaOperacion getOpeCategoriaOperacion() {
        return opeCategoriaOperacion;
    }

    public void setOpeCategoriaOperacion(SgCategoriaOperacion opeCategoriaOperacion) {
        this.opeCategoriaOperacion = opeCategoriaOperacion;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (opePk != null ? opePk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgOperacion)) {
            return false;
        }
        SgOperacion other = (SgOperacion) object;
        if ((this.opePk == null && other.opePk != null) || (this.opePk != null && !this.opePk.equals(other.opePk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgOperacion[ opePk=" + opePk + " ]";
    }
    
}
