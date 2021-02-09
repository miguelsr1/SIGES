/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "plaPk", scope = SgPlantilla.class)
public class SgPlantilla implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long plaPk;

    private String plaCodigo;

    private String plaNombre;

    private String plaNombreBusqueda;
    
    private String plaDescripcion;
    
    private Boolean plaHabilitado;
  
    private LocalDate plaFechaHabilitada;
    
    private LocalDate plaFechaDeshabilitada;

    private LocalDateTime plaUltModFecha;

    private String plaUltModUsuario;

    private Integer plaVersion;
    
    private SgArchivo plaArchivo;
    
    private SgOrganizacionCurricular plaOrganizacionCurricular;
    
    private Boolean plaHabilitadaReemplazoOrg;
    

    
    
    public SgPlantilla() {
        this.plaHabilitado = Boolean.TRUE;
    }

    public Long getPlaPk() {
        return plaPk;
    }

    public void setPlaPk(Long plaPk) {
        this.plaPk = plaPk;
    }

    public String getPlaCodigo() {
        return plaCodigo;
    }

    public void setPlaCodigo(String plaCodigo) {
        this.plaCodigo = plaCodigo;
    }

    public String getPlaNombre() {
        return plaNombre;
    }

    public void setPlaNombre(String plaNombre) {
        this.plaNombre = plaNombre;
    }

    public String getPlaDescripcion() {
        return plaDescripcion;
    }

    public void setPlaDescripcion(String plaDescripcion) {
        this.plaDescripcion = plaDescripcion;
    }

    public LocalDateTime getPlaUltModFecha() {
        return plaUltModFecha;
    }

    public void setPlaUltModFecha(LocalDateTime plaUltModFecha) {
        this.plaUltModFecha = plaUltModFecha;
    }

    public String getPlaUltModUsuario() {
        return plaUltModUsuario;
    }

    public void setPlaUltModUsuario(String plaUltModUsuario) {
        this.plaUltModUsuario = plaUltModUsuario;
    }

    public Integer getPlaVersion() {
        return plaVersion;
    }

    public void setPlaVersion(Integer plaVersion) {
        this.plaVersion = plaVersion;
    }

    
     public Boolean getPlaHabilitado() {
        return plaHabilitado;
    }

    public void setPlaHabilitado(Boolean plaHabilitado) {
        this.plaHabilitado = plaHabilitado;
    }

    public String getPlaNombreBusqueda() {
        return plaNombreBusqueda;
    }

    public void setPlaNombreBusqueda(String plaNombreBusqueda) {
        this.plaNombreBusqueda = plaNombreBusqueda;
    }

    public SgArchivo getPlaArchivo() {
        return plaArchivo;
    }

    public void setPlaArchivo(SgArchivo plaArchivo) {
        this.plaArchivo = plaArchivo;
    }

    public LocalDate getPlaFechaHabilitada() {
        return plaFechaHabilitada;
    }

    public void setPlaFechaHabilitada(LocalDate plaFechaHabilitada) {
        this.plaFechaHabilitada = plaFechaHabilitada;
    }

    public LocalDate getPlaFechaDeshabilitada() {
        return plaFechaDeshabilitada;
    }

    public void setPlaFechaDeshabilitada(LocalDate plaFechaDeshabilitada) {
        this.plaFechaDeshabilitada = plaFechaDeshabilitada;
    }

    public SgOrganizacionCurricular getPlaOrganizacionCurricular() {
        return plaOrganizacionCurricular;
    }

    public void setPlaOrganizacionCurricular(SgOrganizacionCurricular plaOrganizacionCurricular) {
        this.plaOrganizacionCurricular = plaOrganizacionCurricular;
    }

    public Boolean getPlaHabilitadaReemplazoOrg() {
        return plaHabilitadaReemplazoOrg;
    }

    public void setPlaHabilitadaReemplazoOrg(Boolean plaHabilitadaReemplazoOrg) {
        this.plaHabilitadaReemplazoOrg = plaHabilitadaReemplazoOrg;
    }
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (plaPk != null ? plaPk.hashCode() : 0);
        return hash;
    }
	
	@Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SgPlantilla other = (SgPlantilla) obj;
        if (!Objects.equals(this.plaPk, other.plaPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgPlantilla[ plaPk=" + plaPk + " ]";
    }
    
}
