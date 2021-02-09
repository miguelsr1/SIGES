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
import sv.gob.mined.siges.web.dto.catalogo.SgEscalaCalificacion;
import sv.gob.mined.siges.web.enumerados.EnumCalculoNotaInstitucional;
import sv.gob.mined.siges.web.enumerados.EnumFuncionRedondeo;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "mdiPk", scope = SgModuloDiplomado.class)
public class SgModuloDiplomado implements Serializable {

    private Long mdiPk;

    private String mdiCodigo;

    private String mdiNombre;

    private String mdiDescripcion;

    private SgDiplomado mdiDiplomado;

    private LocalDateTime mdiUltModFecha;

    private String mdiUltModUsuario;

    private Integer mdiVersion;
    
    private SgEscalaCalificacion mdiEscalaCalificacion;  
   
    private EnumCalculoNotaInstitucional mdiCalculoNotaInstitucional;

    private EnumFuncionRedondeo mdiFuncionRedondeo;
 
    private Integer mdiPrecision;
   
    private Integer mdiPeriodosCalificacion;

    public SgModuloDiplomado() {
    }

    public Long getMdiPk() {
        return mdiPk;
    }

    public void setMdiPk(Long mdiPk) {
        this.mdiPk = mdiPk;
    }

    public String getMdiCodigo() {
        return mdiCodigo;
    }

    public void setMdiCodigo(String mdiCodigo) {
        this.mdiCodigo = mdiCodigo;
    }

    public String getMdiNombre() {
        return mdiNombre;
    }

    public void setMdiNombre(String mdiNombre) {
        this.mdiNombre = mdiNombre;
    }

    public String getMdiDescripcion() {
        return mdiDescripcion;
    }

    public void setMdiDescripcion(String mdiDescripcion) {
        this.mdiDescripcion = mdiDescripcion;
    }

    public SgDiplomado getMdiDiplomado() {
        return mdiDiplomado;
    }

    public void setMdiDiplomado(SgDiplomado mdiDiplomado) {
        this.mdiDiplomado = mdiDiplomado;
    }

    public LocalDateTime getMdiUltModFecha() {
        return mdiUltModFecha;
    }

    public void setMdiUltModFecha(LocalDateTime mdiUltModFecha) {
        this.mdiUltModFecha = mdiUltModFecha;
    }

    public String getMdiUltModUsuario() {
        return mdiUltModUsuario;
    }

    public void setMdiUltModUsuario(String mdiUltModUsuario) {
        this.mdiUltModUsuario = mdiUltModUsuario;
    }

    public Integer getMdiVersion() {
        return mdiVersion;
    }

    public void setMdiVersion(Integer mdiVersion) {
        this.mdiVersion = mdiVersion;
    }

    public SgEscalaCalificacion getMdiEscalaCalificacion() {
        return mdiEscalaCalificacion;
    }

    public void setMdiEscalaCalificacion(SgEscalaCalificacion mdiEscalaCalificacion) {
        this.mdiEscalaCalificacion = mdiEscalaCalificacion;
    }

    public EnumCalculoNotaInstitucional getMdiCalculoNotaInstitucional() {
        return mdiCalculoNotaInstitucional;
    }

    public void setMdiCalculoNotaInstitucional(EnumCalculoNotaInstitucional mdiCalculoNotaInstitucional) {
        this.mdiCalculoNotaInstitucional = mdiCalculoNotaInstitucional;
    }

    public EnumFuncionRedondeo getMdiFuncionRedondeo() {
        return mdiFuncionRedondeo;
    }

    public void setMdiFuncionRedondeo(EnumFuncionRedondeo mdiFuncionRedondeo) {
        this.mdiFuncionRedondeo = mdiFuncionRedondeo;
    }

    public Integer getMdiPrecision() {
        return mdiPrecision;
    }

    public void setMdiPrecision(Integer mdiPrecision) {
        this.mdiPrecision = mdiPrecision;
    }

    public Integer getMdiPeriodosCalificacion() {
        return mdiPeriodosCalificacion;
    }

    public void setMdiPeriodosCalificacion(Integer mdiPeriodosCalificacion) {
        this.mdiPeriodosCalificacion = mdiPeriodosCalificacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mdiPk != null ? mdiPk.hashCode() : 0);
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
        final SgModuloDiplomado other = (SgModuloDiplomado) obj;
        if (!Objects.equals(this.mdiPk, other.mdiPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.web.dto.SgModuloDiplomado[ mdiPk=" + mdiPk + " ]";
    }

}
