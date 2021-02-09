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
import java.util.List;
import sv.gob.mined.siges.web.dto.catalogo.SgAsociacion;
import sv.gob.mined.siges.web.dto.catalogo.SgBeneficio;
import sv.gob.mined.siges.web.dto.catalogo.SgProyectoInstitucional;


@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "proPk", scope = SgProyectoInstitucionalSede.class)
public class SgProyectoInstitucionalSede implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long proPk;
    
    private SgSede proSede;
    
    private SgProyectoInstitucional proProyectoInstitucional;
    
    private LocalDate proFechaOtorgado;
    
    private String proObservaciones;
    
    private SgBeneficio proBeneficio;
    
    private SgAsociacion proAsociacion;
    
    private LocalDateTime proUltModFecha;
    
    private String proUltModUsuario;
    
    private Integer proVersion;
    
    private List<SgProyectoInstEstudiante> proyectoInstEstudiante;

    public SgProyectoInstitucionalSede() {
        
    }
    
    public SgProyectoInstitucionalSede(Long proPk) {
        this.proPk = proPk;
    }

    public Long getProPk() {
        return proPk;
    }

    public void setProPk(Long proPk) {
        this.proPk = proPk;
    }

    public SgSede getProSede() {
        return proSede;
    }

    public void setProSede(SgSede proSede) {
        this.proSede = proSede;
    }

    public SgProyectoInstitucional getProProyectoInstitucional() {
        return proProyectoInstitucional;
    }

    public void setProProyectoInstitucional(SgProyectoInstitucional proProyectoInstitucional) {
        this.proProyectoInstitucional = proProyectoInstitucional;
    }

    public LocalDate getProFechaOtorgado() {
        return proFechaOtorgado;
    }

    public void setProFechaOtorgado(LocalDate proFechaOtorgado) {
        this.proFechaOtorgado = proFechaOtorgado;
    }

    public String getProObservaciones() {
        return proObservaciones;
    }

    public void setProObservaciones(String proObservaciones) {
        this.proObservaciones = proObservaciones;
    }

    public SgBeneficio getProBeneficio() {
        return proBeneficio;
    }

    public void setProBeneficio(SgBeneficio proBeneficio) {
        this.proBeneficio = proBeneficio;
    }

    public SgAsociacion getProAsociacion() {
        return proAsociacion;
    }

    public void setProAsociacion(SgAsociacion proAsociacion) {
        this.proAsociacion = proAsociacion;
    }

    public LocalDateTime getProUltModFecha() {
        return proUltModFecha;
    }

    public void setProUltModFecha(LocalDateTime proUltModFecha) {
        this.proUltModFecha = proUltModFecha;
    }

    public String getProUltModUsuario() {
        return proUltModUsuario;
    }

    public void setProUltModUsuario(String proUltModUsuario) {
        this.proUltModUsuario = proUltModUsuario;
    }

    public Integer getProVersion() {
        return proVersion;
    }

    public void setProVersion(Integer proVersion) {
        this.proVersion = proVersion;
    }

    public List<SgProyectoInstEstudiante> getProyectoInstEstudiante() {
        return proyectoInstEstudiante;
    }

    public void setProyectoInstEstudiante(List<SgProyectoInstEstudiante> proyectoInstEstudiante) {
        this.proyectoInstEstudiante = proyectoInstEstudiante;
    }


    


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (proPk != null ? proPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgProyectoInstitucionalSede)) {
            return false;
        }
        SgProyectoInstitucionalSede other = (SgProyectoInstitucionalSede) object;
        if ((this.proPk == null && other.proPk != null) || (this.proPk != null && !this.proPk.equals(other.proPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgProyectoInstitucionalSede[ proPk=" + proPk + " ]";
    }
    
}
