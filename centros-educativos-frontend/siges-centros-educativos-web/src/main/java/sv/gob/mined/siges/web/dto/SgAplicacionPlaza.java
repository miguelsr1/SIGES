/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import sv.gob.mined.siges.web.dto.catalogo.SgCalidadIngresoAplicantes;
import sv.gob.mined.siges.web.dto.catalogo.SgMotivosSeleccionPLaza;
import sv.gob.mined.siges.web.enumerados.EnumEstadoAplicacionPlaza;

/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "aplPk", scope = SgAplicacionPlaza.class)
public class SgAplicacionPlaza implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long aplPk;
            
    private SgSolicitudPlaza aplPlaza;
    
    private SgCalidadIngresoAplicantes aplCalidadAplicantes;
    
    private SgPersonalSedeEducativa aplPersonal;
    
    private String aplCodigoUsuario;
    
    private EnumEstadoAplicacionPlaza aplEstado;
    
    private String aplObservacion;
    
    private LocalDateTime aplFechaAplico;
    
    private LocalDateTime aplUltModFecha;
    
    private String aplUltModUsuario;
    
    private Integer aplVersion;
    
    private Boolean aplSeleccionadoEnMatriz;
    
    private Long aplRevPersonalCuandoAplica;
    
    private List<SgEspecialidadesPersonalAlAplicar> aplEspecialidadesAlAplicar;   
    
    private SgAplicacionPlaza plazaSeleccionado;
    
    @JsonIgnore
    private LocalDate aplMenorFechaGraduacion;

    public SgAplicacionPlaza() {
        this.aplSeleccionadoEnMatriz=Boolean.FALSE;
    }

    public SgAplicacionPlaza(Long aplPk) {
        this.aplPk = aplPk;        
    }

    private List<SgMotivosSeleccionPLaza> aplMotivosSeleccionPLaza;
    
    //<editor-fold defaultstate="collapsed" desc="GET & SET">

    public Long getAplPk() {
        return aplPk;
    }

    public void setAplPk(Long aplPk) {
        this.aplPk = aplPk;
    }

    public String getAplCodigoUsuario() {
        return aplCodigoUsuario;
    }

    public void setAplCodigoUsuario(String aplCodigoUsuario) {
        this.aplCodigoUsuario = aplCodigoUsuario;
    }

    public SgSolicitudPlaza getAplPlaza() {
        return aplPlaza;
    }

    public void setAplPlaza(SgSolicitudPlaza aplPlaza) {
        this.aplPlaza = aplPlaza;
    }

    public SgPersonalSedeEducativa getAplPersonal() {
        return aplPersonal;
    }

    public void setAplPersonal(SgPersonalSedeEducativa aplPersonal) {
        this.aplPersonal = aplPersonal;
    }

    public EnumEstadoAplicacionPlaza getAplEstado() {
        return aplEstado;
    }

    public void setAplEstado(EnumEstadoAplicacionPlaza aplEstado) {
        this.aplEstado = aplEstado;
    }

    public String getAplObservacion() {
        return aplObservacion;
    }

    public void setAplObservacion(String aplObservacion) {
        this.aplObservacion = aplObservacion;
    }

    public LocalDateTime getAplFechaAplico() {
        return aplFechaAplico;
    }

    public void setAplFechaAplico(LocalDateTime aplFechaAplico) {
        this.aplFechaAplico = aplFechaAplico;
    }

    public LocalDateTime getAplUltModFecha() {
        return aplUltModFecha;
    }

    public void setAplUltModFecha(LocalDateTime aplUltModFecha) {
        this.aplUltModFecha = aplUltModFecha;
    }

    public String getAplUltModUsuario() {
        return aplUltModUsuario;
    }

    public void setAplUltModUsuario(String aplUltModUsuario) {
        this.aplUltModUsuario = aplUltModUsuario;
    }

    public Integer getAplVersion() {
        return aplVersion;
    }

    public void setAplVersion(Integer aplVersion) {
        this.aplVersion = aplVersion;
    }

    public Boolean getAplSeleccionadoEnMatriz() {
        return aplSeleccionadoEnMatriz;
    }

    public void setAplSeleccionadoEnMatriz(Boolean aplSeleccionadoEnMatriz) {
        this.aplSeleccionadoEnMatriz = aplSeleccionadoEnMatriz;
    }

    public Long getAplRevPersonalCuandoAplica() {
        return aplRevPersonalCuandoAplica;
    }

    public void setAplRevPersonalCuandoAplica(Long aplRevPersonalCuandoAplica) {
        this.aplRevPersonalCuandoAplica = aplRevPersonalCuandoAplica;
    }
  

    public List<SgEspecialidadesPersonalAlAplicar> getAplEspecialidadesAlAplicar() {    
        return aplEspecialidadesAlAplicar;
    }

    //</editor-fold>
    public void setAplEspecialidadesAlAplicar(List<SgEspecialidadesPersonalAlAplicar> aplEspecialidadesAlAplicar) {  
        this.aplEspecialidadesAlAplicar = aplEspecialidadesAlAplicar;
    }

    public LocalDate getAplMenorFechaGraduacion() {
        return aplMenorFechaGraduacion;
    }

    public void setAplMenorFechaGraduacion(LocalDate aplMenorFechaGraduacion) {
        this.aplMenorFechaGraduacion = aplMenorFechaGraduacion;
    }

    public List<SgMotivosSeleccionPLaza> getAplMotivosSeleccionPLaza() {
        return aplMotivosSeleccionPLaza;
    }

    public void setAplMotivosSeleccionPLaza(List<SgMotivosSeleccionPLaza> aplMotivosSeleccionPLaza) {
        this.aplMotivosSeleccionPLaza = aplMotivosSeleccionPLaza;
    }

    public SgAplicacionPlaza getPlazaSeleccionado() {
        return plazaSeleccionado;
    }

    public void setPlazaSeleccionado(SgAplicacionPlaza plazaSeleccionado) {
        this.plazaSeleccionado = plazaSeleccionado;
    }

    public SgCalidadIngresoAplicantes getAplCalidadAplicantes() {
        return aplCalidadAplicantes;
    }

    public void setAplCalidadAplicantes(SgCalidadIngresoAplicantes aplCalidadAplicantes) {
        this.aplCalidadAplicantes = aplCalidadAplicantes;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (aplPk != null ? aplPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAplicacionPlaza)) {
            return false;
        }
        SgAplicacionPlaza other = (SgAplicacionPlaza) object;
        if ((this.aplPk == null && other.aplPk != null) || (this.aplPk != null && !this.aplPk.equals(other.aplPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgAplicacionPlaza[ aplPk=" + aplPk + " ]";
    }
    
}
