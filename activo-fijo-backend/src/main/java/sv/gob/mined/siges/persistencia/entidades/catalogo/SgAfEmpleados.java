/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.catalogo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.centroseducativos.SgPersona;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_af_empleados", schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "empPk", resolver = JsonIdentityResolver.class, scope = SgAfEmpleados.class)
public class SgAfEmpleados implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "emp_pk")
    private Long empPk;
    /**
    @Column(name = "emp_nombres")
    private String empNombres;
    
    @Column(name = "emp_apellidos")
    private String empApellidos;
    
    @Column(name = "emp_cargo")
    private String empCargo;
    **/
    @Column(name = "emp_habilitado")
    private Boolean empHabilitado;
    
    @Column(name = "emp_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime empUltModFecha;
    
     @Size(max = 45)
    @Column(name = "emp_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String empUltModUsuario;
    
    @Column(name = "emp_version")
    @Version
    private Integer empVersion;
    
    @JoinColumn(name = "emp_unidad_administrativa_fk", referencedColumnName = "uad_pk")
    @ManyToOne(optional = false)
    private SgAfUnidadesAdministrativas empUnidadAdministrativaFk;

    @JoinColumn(name = "emp_persona_fk", referencedColumnName = "per_pk")
    @ManyToOne
    private SgPersona empPersonaFk;
    
    public SgAfEmpleados() {
    }

    public SgAfEmpleados(Long empPk) {
        this.empPk = empPk;
    }

    public Long getEmpPk() {
        return empPk;
    }

    public void setEmpPk(Long empPk) {
        this.empPk = empPk;
    }

    public Boolean getEmpHabilitado() {
        return empHabilitado;
    }

    public void setEmpHabilitado(Boolean empHabilitado) {
        this.empHabilitado = empHabilitado;
    }

    public LocalDateTime getEmpUltModFecha() {
        return empUltModFecha;
    }

    public void setEmpUltModFecha(LocalDateTime empUltModFecha) {
        this.empUltModFecha = empUltModFecha;
    }

    public String getEmpUltModUsuario() {
        return empUltModUsuario;
    }

    public void setEmpUltModUsuario(String empUltModUsuario) {
        this.empUltModUsuario = empUltModUsuario;
    }

    public Integer getEmpVersion() {
        return empVersion;
    }

    public void setEmpVersion(Integer empVersion) {
        this.empVersion = empVersion;
    }

    public SgAfUnidadesAdministrativas getEmpUnidadAdministrativaFk() {
        return empUnidadAdministrativaFk;
    }

    public void setEmpUnidadAdministrativaFk(SgAfUnidadesAdministrativas empUnidadAdministrativaFk) {
        this.empUnidadAdministrativaFk = empUnidadAdministrativaFk;
    }

    public SgPersona getEmpPersonaFk() {
        return empPersonaFk;
    }

    public void setEmpPersonaFk(SgPersona empPersonaFk) {
        this.empPersonaFk = empPersonaFk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (empPk != null ? empPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAfEmpleados)) {
            return false;
        }
        SgAfEmpleados other = (SgAfEmpleados) object;
        if ((this.empPk == null && other.empPk != null) || (this.empPk != null && !this.empPk.equals(other.empPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgAfEmpleados[ empPk=" + empPk + " ]";
    }
    
}
