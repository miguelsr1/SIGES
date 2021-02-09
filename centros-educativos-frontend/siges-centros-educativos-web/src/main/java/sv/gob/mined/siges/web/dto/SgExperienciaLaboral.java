/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoInstitucionPaga;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "elaPk", scope = SgExperienciaLaboral.class)
public class SgExperienciaLaboral implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long elaPk;

    private SgDatoEmpleado elaDatoEmpleado;

    private String elaInstitucion;

    private SgTipoInstitucionPaga elaTipoInstitucion;

    private String elaDireccion;

    private String elaCargo;

    private LocalDate elaDesde;

    private LocalDate elaHasta;
    
    private Boolean elaValidada;

    private LocalDateTime elaUltModFecha;

    private String elaUltModUsuario;

    private Integer elaVersion;

    public SgExperienciaLaboral() {
        this.elaValidada = Boolean.FALSE;
    }

    public SgExperienciaLaboral(Long elaPk) {
        this.elaPk = elaPk;
    }

    public Long getElaPk() {
        return elaPk;
    }

    public void setElaPk(Long elaPk) {
        this.elaPk = elaPk;
    }

    public String getElaInstitucion() {
        return elaInstitucion;
    }

    public void setElaInstitucion(String elaInstitucion) {
        this.elaInstitucion = elaInstitucion;
    }

    public SgTipoInstitucionPaga getElaTipoInstitucion() {
        return elaTipoInstitucion;
    }

    public void setElaTipoInstitucion(SgTipoInstitucionPaga elaTipoInstitucion) {
        this.elaTipoInstitucion = elaTipoInstitucion;
    }

    public String getElaDireccion() {
        return elaDireccion;
    }

    public void setElaDireccion(String elaDireccion) {
        this.elaDireccion = elaDireccion;
    }

    public String getElaCargo() {
        return elaCargo;
    }

    public void setElaCargo(String elaCargo) {
        this.elaCargo = elaCargo;
    }

    public LocalDate getElaDesde() {
        return elaDesde;
    }

    public void setElaDesde(LocalDate elaDesde) {
        this.elaDesde = elaDesde;
    }

    public LocalDate getElaHasta() {
        return elaHasta;
    }

    public void setElaHasta(LocalDate elaHasta) {
        this.elaHasta = elaHasta;
    }

    public LocalDateTime getElaUltModFecha() {
        return elaUltModFecha;
    }

    public void setElaUltModFecha(LocalDateTime elaUltModFecha) {
        this.elaUltModFecha = elaUltModFecha;
    }

    public String getElaUltModUsuario() {
        return elaUltModUsuario;
    }

    public void setElaUltModUsuario(String elaUltModUsuario) {
        this.elaUltModUsuario = elaUltModUsuario;
    }

    public Integer getElaVersion() {
        return elaVersion;
    }

    public void setElaVersion(Integer elaVersion) {
        this.elaVersion = elaVersion;
    }

    public SgDatoEmpleado getElaDatoEmpleado() {
        return elaDatoEmpleado;
    }

    public void setElaDatoEmpleado(SgDatoEmpleado elaDatoEmpleado) {
        this.elaDatoEmpleado = elaDatoEmpleado;
    }

    public Boolean getElaValidada() {
        return elaValidada;
    }

    public void setElaValidada(Boolean elaValidada) {
        this.elaValidada = elaValidada;
    }
    
       
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (elaPk != null ? elaPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgExperienciaLaboral)) {
            return false;
        }
        SgExperienciaLaboral other = (SgExperienciaLaboral) object;
        if ((this.elaPk == null && other.elaPk != null) || (this.elaPk != null && !this.elaPk.equals(other.elaPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgExperienciaLaboral[ elaPk=" + elaPk + " ]";
    }

}
