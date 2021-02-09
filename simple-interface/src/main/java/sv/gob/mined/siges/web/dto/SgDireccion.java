/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.Objects;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "dirPk", scope = SgDireccion.class)
public class SgDireccion implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long dirPk;

    private String dirDireccion;

    private SgDepartamento dirDepartamento;
    
    private SgMunicipio dirMunicipio;
    
    private SgCanton dirCanton;

    private String dirCaserioTexto;

    private SgZona dirZona;    

    private Integer dirVersion;

    public SgDireccion() {
    }

    public Long getDirPk() {
        return dirPk;
    }

    public void setDirPk(Long dirPk) {
        this.dirPk = dirPk;
    }

    public Integer getDirVersion() {
        return dirVersion;
    }

    public void setDirVersion(Integer dirVersion) {
        this.dirVersion = dirVersion;
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

    public String getDirCaserioTexto() {
        return dirCaserioTexto;
    }

    public void setDirCaserioTexto(String dirCaserioTexto) {
        this.dirCaserioTexto = dirCaserioTexto;
    }



    

    @Override
    public int hashCode() {
        return Objects.hashCode(this.dirPk);
    }

    public SgCanton getDirCanton() {
        return dirCanton;
    }

    public void setDirCanton(SgCanton dirCanton) {
        this.dirCanton = dirCanton;
    }

    public SgZona getDirZona() {
        return dirZona;
    }

    public void setDirZona(SgZona dirZona) {
        this.dirZona = dirZona;
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
        return "SgDireccion{" + "dirPk=" + dirPk + ", dirDireccion=" + dirDireccion + ", dirDepartamento=" + dirDepartamento + ", dirMunicipio=" + dirMunicipio + ", dirVersion=" + dirVersion + '}';
    }
    
    
}
