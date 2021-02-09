/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "diePk", scope = SgDiplomaEstudiante.class)
public class SgDiplomaEstudiante implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long diePk;

    private Boolean dieConfirmado;
  
    private SgEstudiante dieEstudianteFk;    
    
    private SgDiploma dieDiplomaFk;
    
    private LocalDateTime dieUltModFecha;

    private String dieUltModUsuario;

    private Integer dieVersion;
    
    
    public SgDiplomaEstudiante() {
 
    }

    public Long getDiePk() {
        return diePk;
    }

    public void setDiePk(Long diePk) {
        this.diePk = diePk;
    }

    public Boolean getDieConfirmado() {
        return dieConfirmado;
    }

    public void setDieConfirmado(Boolean dieConfirmado) {
        this.dieConfirmado = dieConfirmado;
    }

    public SgEstudiante getDieEstudianteFk() {
        return dieEstudianteFk;
    }

    public void setDieEstudianteFk(SgEstudiante dieEstudianteFk) {
        this.dieEstudianteFk = dieEstudianteFk;
    }

    public SgDiploma getDieDiplomaFk() {
        return dieDiplomaFk;
    }

    public void setDieDiplomaFk(SgDiploma dieDiplomaFk) {
        this.dieDiplomaFk = dieDiplomaFk;
    }

    

    public LocalDateTime getDieUltModFecha() {
        return dieUltModFecha;
    }

    public void setDieUltModFecha(LocalDateTime dieUltModFecha) {
        this.dieUltModFecha = dieUltModFecha;
    }

    public String getDieUltModUsuario() {
        return dieUltModUsuario;
    }

    public void setDieUltModUsuario(String dieUltModUsuario) {
        this.dieUltModUsuario = dieUltModUsuario;
    }

    public Integer getDieVersion() {
        return dieVersion;
    }

    public void setDieVersion(Integer dieVersion) {
        this.dieVersion = dieVersion;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (diePk != null ? diePk.hashCode() : 0);
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
        final SgDiplomaEstudiante other = (SgDiplomaEstudiante) obj;
        if (!Objects.equals(this.diePk, other.diePk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgDiplomaEstudiante[ diePk=" + diePk + " ]";
    }
    
}
