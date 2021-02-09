/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDate;
import sv.gob.mined.siges.web.enumerados.EnumServicioEducativoEstado;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "sduPk", scope = SgServicioEducativo.class)
public class SgServicioEducativo implements Serializable {

    private Long sduPk;

    private SgGrado sduGrado;

    private EnumServicioEducativoEstado sduEstado;

    private LocalDate sduFechaHabilitado;

    private String sduNumeroTramite;

    private SgSede sduSede;

    private Integer sduVersion;

    private SgOpcion sduOpcion;

    private SgProgramaEducativo sduProgramaEducativo;
    
    private Boolean sduOrigenExterno;

    public SgServicioEducativo() {
        this.sduOrigenExterno = Boolean.TRUE;
    }

    public SgServicioEducativo(Long sduPk) {
        this.sduPk = sduPk;
    }

    public Long getSduPk() {
        return sduPk;
    }

    public void setSduPk(Long sduPk) {
        this.sduPk = sduPk;
    }

    public EnumServicioEducativoEstado getSduEstado() {
        return sduEstado;
    }

    public void setSduEstado(EnumServicioEducativoEstado sduEstado) {
        this.sduEstado = sduEstado;
    }

    public LocalDate getSduFechaHabilitado() {
        return sduFechaHabilitado;
    }

    public void setSduFechaHabilitado(LocalDate sduFechaHabilitado) {
        this.sduFechaHabilitado = sduFechaHabilitado;
    }

    public String getSduNumeroTramite() {
        return sduNumeroTramite;
    }

    public void setSduNumeroTramite(String sduNumeroTramite) {
        this.sduNumeroTramite = sduNumeroTramite;
    }

    public Integer getSduVersion() {
        return sduVersion;
    }

    public void setSduVersion(Integer sduVersion) {
        this.sduVersion = sduVersion;
    }

    public SgGrado getSduGrado() {
        return sduGrado;
    }

    public void setSduGrado(SgGrado sduGrado) {
        this.sduGrado = sduGrado;
    }

    public SgSede getSduSede() {
        return sduSede;
    }

    public void setSduSede(SgSede sduSede) {
        this.sduSede = sduSede;
    }

    public SgOpcion getSduOpcion() {
        return sduOpcion;
    }

    public void setSduOpcion(SgOpcion sduOpcion) {
        this.sduOpcion = sduOpcion;
    }

    public SgProgramaEducativo getSduProgramaEducativo() {
        return sduProgramaEducativo;
    }

    public void setSduProgramaEducativo(SgProgramaEducativo sduProgramaEducativo) {
        this.sduProgramaEducativo = sduProgramaEducativo;
    }

    public Boolean getSduOrigenExterno() {
        return sduOrigenExterno;
    }

    public void setSduOrigenExterno(Boolean sduOrigenExterno) {
        this.sduOrigenExterno = sduOrigenExterno;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sduPk != null ? sduPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgServicioEducativo)) {
            return false;
        }
        SgServicioEducativo other = (SgServicioEducativo) object;
        if ((this.sduPk == null && other.sduPk != null) || (this.sduPk != null && !this.sduPk.equals(other.sduPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgServicioEducativo[ sduPk=" + sduPk + " ]";
    }

}
