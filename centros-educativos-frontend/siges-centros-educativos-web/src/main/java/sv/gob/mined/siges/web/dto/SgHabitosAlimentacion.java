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
import java.util.List;
import java.util.Objects;
import sv.gob.mined.siges.web.dto.catalogo.SgTiemposComidaDia;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "halPk", scope = SgHabitosAlimentacion.class)
public class SgHabitosAlimentacion implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long halPk;
 
    private Boolean halConsumeFrutasVerduras;
  
    private Integer halFrecuenciaConsumoFrutas;
 
    private Boolean halConsumeAgua;
 
    private Integer halCantidadVasos;

    private SgAnioLectivo halAnioLectivoFk;

    private SgEstudiante halEstudianteFk;

    private LocalDateTime halUltModFecha;

    private String halUltModUsuario;

    private Integer halVersion;
    
    private List<SgTiemposComidaDia> halTiempoComidaDia;
    
    
    public SgHabitosAlimentacion() {
       this.halConsumeAgua=Boolean.FALSE;
       this.halConsumeFrutasVerduras=Boolean.FALSE;
    }
    
  

    public Long getHalPk() {
        return halPk;
    }

    public void setHalPk(Long halPk) {
        this.halPk = halPk;
    }

    public Boolean getHalConsumeFrutasVerduras() {
        return halConsumeFrutasVerduras;
    }

    public void setHalConsumeFrutasVerduras(Boolean halConsumeFrutasVerduras) {
        this.halConsumeFrutasVerduras = halConsumeFrutasVerduras;
    }

    public Integer getHalFrecuenciaConsumoFrutas() {
        return halFrecuenciaConsumoFrutas;
    }

    public void setHalFrecuenciaConsumoFrutas(Integer halFrecuenciaConsumoFrutas) {
        this.halFrecuenciaConsumoFrutas = halFrecuenciaConsumoFrutas;
    }

    public Boolean getHalConsumeAgua() {
        return halConsumeAgua;
    }

    public void setHalConsumeAgua(Boolean halConsumeAgua) {
        this.halConsumeAgua = halConsumeAgua;
    }

    public Integer getHalCantidadVasos() {
        return halCantidadVasos;
    }

    public void setHalCantidadVasos(Integer halCantidadVasos) {
        this.halCantidadVasos = halCantidadVasos;
    }

    public SgAnioLectivo getHalAnioLectivoFk() {
        return halAnioLectivoFk;
    }

    public void setHalAnioLectivoFk(SgAnioLectivo halAnioLectivoFk) {
        this.halAnioLectivoFk = halAnioLectivoFk;
    }

    public SgEstudiante getHalEstudianteFk() {
        return halEstudianteFk;
    }

    public void setHalEstudianteFk(SgEstudiante halEstudianteFk) {
        this.halEstudianteFk = halEstudianteFk;
    }
    

    public LocalDateTime getHalUltModFecha() {
        return halUltModFecha;
    }

    public void setHalUltModFecha(LocalDateTime halUltModFecha) {
        this.halUltModFecha = halUltModFecha;
    }

    public String getHalUltModUsuario() {
        return halUltModUsuario;
    }

    public void setHalUltModUsuario(String halUltModUsuario) {
        this.halUltModUsuario = halUltModUsuario;
    }

    public Integer getHalVersion() {
        return halVersion;
    }

    public void setHalVersion(Integer halVersion) {
        this.halVersion = halVersion;
    }

    public List<SgTiemposComidaDia> getHalTiempoComidaDia() {
        return halTiempoComidaDia;
    }

    public void setHalTiempoComidaDia(List<SgTiemposComidaDia> halTiempoComidaDia) {
        this.halTiempoComidaDia = halTiempoComidaDia;
    }

   

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (halPk != null ? halPk.hashCode() : 0);
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
        final SgHabitosAlimentacion other = (SgHabitosAlimentacion) obj;
        if (!Objects.equals(this.halPk, other.halPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgHabitosAlimentacion[ halPk=" + halPk + " ]";
    }
    
}
