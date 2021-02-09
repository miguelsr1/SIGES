/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "accPk", scope = SgInfAccesibilidad.class)
public class SgInfAccesibilidad implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long accPk;

    private String accCodigo;

    private String accNombre;

    private Boolean accHabilitado;

    private LocalDateTime accUltModFecha;

    private String accUltModUsuario;

    private Integer accVersion;
    
    private Integer accOrden;
    
    private Boolean accAplicaOtros;
    
    
    public SgInfAccesibilidad() {
        this.accHabilitado = Boolean.TRUE;
        accAplicaOtros=Boolean.FALSE;
    }

    public Long getAccPk() {
        return accPk;
    }

    public void setAccPk(Long accPk) {
        this.accPk = accPk;
    }

    public String getAccCodigo() {
        return accCodigo;
    }

    public void setAccCodigo(String accCodigo) {
        this.accCodigo = accCodigo;
    }

    public String getAccNombre() {
        return accNombre;
    }

    public void setAccNombre(String accNombre) {
        this.accNombre = accNombre;
    }

    public LocalDateTime getAccUltModFecha() {
        return accUltModFecha;
    }

    public void setAccUltModFecha(LocalDateTime accUltModFecha) {
        this.accUltModFecha = accUltModFecha;
    }

    public String getAccUltModUsuario() {
        return accUltModUsuario;
    }

    public void setAccUltModUsuario(String accUltModUsuario) {
        this.accUltModUsuario = accUltModUsuario;
    }

    public Integer getAccVersion() {
        return accVersion;
    }

    public void setAccVersion(Integer accVersion) {
        this.accVersion = accVersion;
    }

    
     public Boolean getAccHabilitado() {
        return accHabilitado;
    }

    public void setAccHabilitado(Boolean accHabilitado) {
        this.accHabilitado = accHabilitado;
    }

    public Integer getAccOrden() {
        return accOrden;
    }

    public void setAccOrden(Integer accOrden) {
        this.accOrden = accOrden;
    }

    public Boolean getAccAplicaOtros() {
        return accAplicaOtros;
    }

    public void setAccAplicaOtros(Boolean accAplicaOtros) {
        this.accAplicaOtros = accAplicaOtros;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (accPk != null ? accPk.hashCode() : 0);
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
        final SgInfAccesibilidad other = (SgInfAccesibilidad) obj;
        if (!Objects.equals(this.accPk, other.accPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgInfAccesibilidad[ accPk=" + accPk + " ]";
    }
    
}
