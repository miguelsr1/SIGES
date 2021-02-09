/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "mfdPk", scope = SgModuloFormacionDocente.class)
public class SgModuloFormacionDocente implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long mfdPk;

    private String mfdCodigo;

    private String mfdNombre;

    private String mfdNombreBusqueda;

    private Boolean mfdPartePnfd;
      
    private Boolean mfdHabilitado;

    private LocalDateTime mfdUltModFecha;

    private String mfdUltModUsuario;

    private Integer mfdVersion;

    public SgModuloFormacionDocente() {
        this.mfdPartePnfd = Boolean.FALSE;
        this.mfdHabilitado = Boolean.FALSE;
    }

    public Long getMfdPk() {
        return mfdPk;
    }

    public void setMfdPk(Long mfdPk) {
        this.mfdPk = mfdPk;
    }

    public String getMfdCodigo() {
        return mfdCodigo;
    }

    public void setMfdCodigo(String mfdCodigo) {
        this.mfdCodigo = mfdCodigo;
    }

    public String getMfdNombre() {
        return mfdNombre;
    }

    public void setMfdNombre(String mfdNombre) {
        this.mfdNombre = mfdNombre;
    }

    public String getMfdNombreBusqueda() {
        return mfdNombreBusqueda;
    }

    public void setMfdNombreBusqueda(String mfdNombreBusqueda) {
        this.mfdNombreBusqueda = mfdNombreBusqueda;
    }

    public Boolean getMfdPartePnfd() {
        return mfdPartePnfd;
    }

    public void setMfdPartePnfd(Boolean mfdPartePnfd) {
        this.mfdPartePnfd = mfdPartePnfd;
    }

    public Boolean getMfdHabilitado() {
        return mfdHabilitado;
    }

    public void setMfdHabilitado(Boolean mfdHabilitado) {
        this.mfdHabilitado = mfdHabilitado;
    }

    public LocalDateTime getMfdUltModFecha() {
        return mfdUltModFecha;
    }

    public void setMfdUltModFecha(LocalDateTime mfdUltModFecha) {
        this.mfdUltModFecha = mfdUltModFecha;
    }

    public String getMfdUltModUsuario() {
        return mfdUltModUsuario;
    }

    public void setMfdUltModUsuario(String mfdUltModUsuario) {
        this.mfdUltModUsuario = mfdUltModUsuario;
    }

    public Integer getMfdVersion() {
        return mfdVersion;
    }

    public void setMfdVersion(Integer mfdVersion) {
        this.mfdVersion = mfdVersion;
    }

    
   
    @Override
    public int hashCode() {
        return Objects.hashCode(this.mfdPk);
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
        final SgModuloFormacionDocente other = (SgModuloFormacionDocente) obj;
        if (!Objects.equals(this.mfdPk, other.mfdPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgModuloFormacionDocente{" + "mfdPk=" + mfdPk + ", mfdCodigo=" + mfdCodigo + ", mfdNombre=" + mfdNombre + ", mfdNombreBusqueda=" + mfdNombreBusqueda + ", mfdHabilitado=" + mfdHabilitado + ", mfdUltModFecha=" + mfdUltModFecha + ", mfdUltModUsuario=" + mfdUltModUsuario + ", mfdVersion=" + mfdVersion + '}';
    }
    
    
    
    

}
