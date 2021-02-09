/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import sv.gob.mined.siges.web.dto.centroseducativos.SgPersona;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class,property = "empPk", scope = SgAfEmpleados.class)
public class SgAfEmpleados implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long empPk;
   /** private String empNombres;
    private String empApellidos;
    private String empCargo;**/
    private Boolean empHabilitado;
    private LocalDateTime empUltModFecha;
    private String empUltModUsuario;
    private Integer empVersion;
    private SgAfUnidadesAdministrativas empUnidadAdministrativaFk;
    private SgPersona empPersonaFk;
    
    public SgAfEmpleados() {
    }

    public SgAfEmpleados(Long empPk) {
        this.empPk = empPk;
    }

    @JsonIgnore
    public String getNombresApellidos() {
        if(empPersonaFk != null){
            return empPersonaFk.getPerNombreCompleto();
        }
        return "";
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
        return "sv.gob.mined.siges.web.dto.catalogo.SgAfEmpleados[ empPk=" + empPk + " ]";
    }
    
}
