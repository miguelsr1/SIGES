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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "mefPk", scope = SgMedidasExamenFisico.class)
public class SgMedidasExamenFisico implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long mefPk;

    private Integer mefEdad;

    private Double mefPeso;

    private Double mefTalla;
      
    private Double mefImc;
    
    private Double mefCbd;
  
    private SgEstudiante mefEstudianteFk;

    private LocalDateTime mefUltModFecha;

    private String mefUltModUsuario;

    private Integer mefVersion;
    
    
    public SgMedidasExamenFisico() {
   
    }

    public Long getMefPk() {
        return mefPk;
    }

    public void setMefPk(Long mefPk) {
        this.mefPk = mefPk;
    }

    public Integer getMefEdad() {
        return mefEdad;
    }

    public void setMefEdad(Integer mefEdad) {
        this.mefEdad = mefEdad;
    }

    public Double getMefPeso() {
        return mefPeso;
    }

    public void setMefPeso(Double mefPeso) {
        this.mefPeso = mefPeso;
    }

    public Double getMefTalla() {
        return mefTalla;
    }

    public void setMefTalla(Double mefTalla) {
        this.mefTalla = mefTalla;
    }

    public Double getMefImc() {
        return mefImc;
    }

    public void setMefImc(Double mefImc) {
        this.mefImc = mefImc;
    }

    public Double getMefCbd() {
        return mefCbd;
    }

    public void setMefCbd(Double mefCbd) {
        this.mefCbd = mefCbd;
    }

    public SgEstudiante getMefEstudianteFk() {
        return mefEstudianteFk;
    }

    public void setMefEstudianteFk(SgEstudiante mefEstudianteFk) {
        this.mefEstudianteFk = mefEstudianteFk;
    }
    

    public LocalDateTime getMefUltModFecha() {
        return mefUltModFecha;
    }

    public void setMefUltModFecha(LocalDateTime mefUltModFecha) {
        this.mefUltModFecha = mefUltModFecha;
    }

    public String getMefUltModUsuario() {
        return mefUltModUsuario;
    }

    public void setMefUltModUsuario(String mefUltModUsuario) {
        this.mefUltModUsuario = mefUltModUsuario;
    }

    public Integer getMefVersion() {
        return mefVersion;
    }

    public void setMefVersion(Integer mefVersion) {
        this.mefVersion = mefVersion;
    }

    


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mefPk != null ? mefPk.hashCode() : 0);
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
        final SgMedidasExamenFisico other = (SgMedidasExamenFisico) obj;
        if (!Objects.equals(this.mefPk, other.mefPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgMedidasExamenFisico[ mefPk=" + mefPk + " ]";
    }
    
}
