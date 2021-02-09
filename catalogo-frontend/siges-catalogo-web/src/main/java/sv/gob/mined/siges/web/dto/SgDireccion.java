/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "dirPk", scope = SgDireccion.class)
public class SgDireccion implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long dirPk;
    
    private String dirDireccion;
    
    private SgDepartamento dirDepartamento;
    
    private SgMunicipio dirMunicipio;

    private SgCanton dirCanton;

    private SgCaserio dirCaserio;
    
    private String dirCaserioTexto;
    
    private SgTipoCalle dirTipoCalle;
    
    private SgZona dirZona;
    
    private Double dirLatitud;    
    
    private Double dirLongitud;     
    
    private LocalDateTime dirUltModFecha;  
    
    private String dirUltModUsuario;
    
    private Integer dirVersion;
      
  
    public SgDireccion() {
    }

    public Long getDirPk() {
        return dirPk;
    }

    public void setDirPk(Long dirPk) {
        this.dirPk = dirPk;
    }

    public String getDirDireccion() {
        return dirDireccion;
    }

    public void setDirDireccion(String dirDireccion) {
        this.dirDireccion = dirDireccion;
    }

    public SgDepartamento getDirDepartamento() {
        return dirDepartamento;
    }

    public void setDirDepartamento(SgDepartamento dirDepartamento) {
        this.dirDepartamento = dirDepartamento;
    }

    public SgMunicipio getDirMunicipio() {
        return dirMunicipio;
    }

    public void setDirMunicipio(SgMunicipio dirMunicipio) {
        this.dirMunicipio = dirMunicipio;
    }

    public SgCanton getDirCanton() {
        return dirCanton;
    }

    public void setDirCanton(SgCanton dirCanton) {
        this.dirCanton = dirCanton;
    }

    public SgCaserio getDirCaserio() {
        return dirCaserio;
    }

    public void setDirCaserio(SgCaserio dirCaserio) {
        this.dirCaserio = dirCaserio;
    }

    public SgZona getDirZona() {
        return dirZona;
    }

    public void setDirZona(SgZona dirZona) {
        this.dirZona = dirZona;
    }

    
    public Double getDirLatitud() {
        return dirLatitud;
    }

    public void setDirLatitud(Double dirLatitud) {
        this.dirLatitud = dirLatitud;
    }

    public Double getDirLongitud() {
        return dirLongitud;
    }

    public void setDirLongitud(Double dirLongitud) {
        this.dirLongitud = dirLongitud;
    }

    public LocalDateTime getDirUltModFecha() {
        return dirUltModFecha;
    }

    public void setDirUltModFecha(LocalDateTime dirUltModFecha) {
        this.dirUltModFecha = dirUltModFecha;
    }

    public String getDirUltModUsuario() {
        return dirUltModUsuario;
    }

    public void setDirUltModUsuario(String dirUltModUsuario) {
        this.dirUltModUsuario = dirUltModUsuario;
    }

    public Integer getDirVersion() {
        return dirVersion;
    }

    public void setDirVersion(Integer dirVersion) {
        this.dirVersion = dirVersion;
    }


    public String getDirCaserioTexto() {
        return dirCaserioTexto;
    }

    public void setDirCaserioTexto(String dirCaserioTexto) {
        this.dirCaserioTexto = dirCaserioTexto;
    }

    public SgTipoCalle getDirTipoCalle() {
        return dirTipoCalle;
    }

    public void setDirTipoCalle(SgTipoCalle dirTipoCalle) {
        this.dirTipoCalle = dirTipoCalle;
    }
     
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + Objects.hashCode(this.dirPk);
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
        final SgDireccion other = (SgDireccion) obj;
        if (!Objects.equals(this.dirPk, other.dirPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgDireccion{" + "dirPk=" + dirPk + ", dirDireccion=" + dirDireccion + ", dirDepartamento=" + dirDepartamento + ", dirMunicipio=" + dirMunicipio + ", dirCanton=" + dirCanton + ", dirCaserio=" + dirCaserio + ", dirZona=" + dirZona + ", dirLatitud=" + dirLatitud + ", dirLongitud=" + dirLongitud  + ", dirUltModFecha=" + dirUltModFecha + ", dirUltModUsuario=" + dirUltModUsuario + ", dirVersion=" + dirVersion + '}';
    }

    
  

}
