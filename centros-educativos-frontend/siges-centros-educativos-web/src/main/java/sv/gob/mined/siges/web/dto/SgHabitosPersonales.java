/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "hapPk", scope = SgHabitosPersonales.class)
public class SgHabitosPersonales implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long hapPk;

    private Double hapTiempoFrentePantalla;
     
    private Double hapTiempoCompartidoConPadres;
     
    private Double hapTiempoTareasEscuela;
  
    private Double hapTiempoRecreacion;
    
    private Double hapTiempoActividadFisica;
   
    private String hapTipoActividadFisica;

    private Double hapTiempoDormir;
     
    private String hapObservaciones;

    private SgAnioLectivo hapAnioLectivoFk;

    private SgEstudiante hapEstudianteFk;

    private LocalDateTime hapUltModFecha;

    private String hapUltModUsuario;

    private Integer hapVersion;
    
    
    public SgHabitosPersonales() {
    }

    public Long getHapPk() {
        return hapPk;
    }

    public void setHapPk(Long hapPk) {
        this.hapPk = hapPk;
    }

    public LocalDateTime getHapUltModFecha() {
        return hapUltModFecha;
    }

    public void setHapUltModFecha(LocalDateTime hapUltModFecha) {
        this.hapUltModFecha = hapUltModFecha;
    }

    public String getHapUltModUsuario() {
        return hapUltModUsuario;
    }

    public void setHapUltModUsuario(String hapUltModUsuario) {
        this.hapUltModUsuario = hapUltModUsuario;
    }

    public Integer getHapVersion() {
        return hapVersion;
    }

    public void setHapVersion(Integer hapVersion) {
        this.hapVersion = hapVersion;
    }

    public Double getHapTiempoFrentePantalla() {
        return hapTiempoFrentePantalla;
    }

    public void setHapTiempoFrentePantalla(Double hapTiempoFrentePantalla) {
        this.hapTiempoFrentePantalla = hapTiempoFrentePantalla;
    }

    public Double getHapTiempoCompartidoConPadres() {
        return hapTiempoCompartidoConPadres;
    }

    public void setHapTiempoCompartidoConPadres(Double hapTiempoCompartidoConPadres) {
        this.hapTiempoCompartidoConPadres = hapTiempoCompartidoConPadres;
    }

    public Double getHapTiempoTareasEscuela() {
        return hapTiempoTareasEscuela;
    }

    public void setHapTiempoTareasEscuela(Double hapTiempoTareasEscuela) {
        this.hapTiempoTareasEscuela = hapTiempoTareasEscuela;
    }

    public Double getHapTiempoActividadFisica() {
        return hapTiempoActividadFisica;
    }

    public void setHapTiempoActividadFisica(Double hapTiempoActividadFisica) {
        this.hapTiempoActividadFisica = hapTiempoActividadFisica;
    }

    public Double getHapTiempoRecreacion() {
        return hapTiempoRecreacion;
    }

    public void setHapTiempoRecreacion(Double hapTiempoRecreacion) {
        this.hapTiempoRecreacion = hapTiempoRecreacion;
    }

    public String getHapTipoActividadFisica() {
        return hapTipoActividadFisica;
    }

    public void setHapTipoActividadFisica(String hapTipoActividadFisica) {
        this.hapTipoActividadFisica = hapTipoActividadFisica;
    }

    public Double getHapTiempoDormir() {
        return hapTiempoDormir;
    }

    public void setHapTiempoDormir(Double hapTiempoDormir) {
        this.hapTiempoDormir = hapTiempoDormir;
    }

    public String getHapObservaciones() {
        return hapObservaciones;
    }

    public void setHapObservaciones(String hapObservaciones) {
        this.hapObservaciones = hapObservaciones;
    }    

    public SgAnioLectivo getHapAnioLectivoFk() {
        return hapAnioLectivoFk;
    }

    public void setHapAnioLectivoFk(SgAnioLectivo hapAnioLectivoFk) {
        this.hapAnioLectivoFk = hapAnioLectivoFk;
    }

    public SgEstudiante getHapEstudianteFk() {
        return hapEstudianteFk;
    }

    public void setHapEstudianteFk(SgEstudiante hapEstudianteFk) {
        this.hapEstudianteFk = hapEstudianteFk;
    }

    
    
   

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (hapPk != null ? hapPk.hashCode() : 0);
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
        final SgHabitosPersonales other = (SgHabitosPersonales) obj;
        if (!Objects.equals(this.hapPk, other.hapPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgHabitosPersonales[ hapPk=" + hapPk + " ]";
    }
    
}
