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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "mspPk", scope = SgMotivosSeleccionPLaza.class)
public class SgMotivosSeleccionPLaza implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long mspPk;

    private String mspCodigo;

    private String mspNombre;
    
    private Integer mspOrden;

    private Boolean mspHabilitado;

    private LocalDateTime mspUltModFecha;

    private String mspUltModUsuario;

    private Integer mspVersion;
    
    
    public SgMotivosSeleccionPLaza() {
        this.mspHabilitado = Boolean.TRUE;
    }

    public Long getMspPk() {
        return mspPk;
    }

    public void setMspPk(Long mspPk) {
        this.mspPk = mspPk;
    }

    public String getMspCodigo() {
        return mspCodigo;
    }

    public void setMspCodigo(String mspCodigo) {
        this.mspCodigo = mspCodigo;
    }

    public String getMspNombre() {
        return mspNombre;
    }

    public void setMspNombre(String mspNombre) {
        this.mspNombre = mspNombre;
    }

    public Integer getMspOrden() {
        return mspOrden;
    }

    public void setMspOrden(Integer mspOrden) {
        this.mspOrden = mspOrden;
    }

    public LocalDateTime getMspUltModFecha() {
        return mspUltModFecha;
    }

    public void setMspUltModFecha(LocalDateTime mspUltModFecha) {
        this.mspUltModFecha = mspUltModFecha;
    }

    public String getMspUltModUsuario() {
        return mspUltModUsuario;
    }

    public void setMspUltModUsuario(String mspUltModUsuario) {
        this.mspUltModUsuario = mspUltModUsuario;
    }

    public Integer getMspVersion() {
        return mspVersion;
    }

    public void setMspVersion(Integer mspVersion) {
        this.mspVersion = mspVersion;
    }

    
     public Boolean getMspHabilitado() {
        return mspHabilitado;
    }

    public void setMspHabilitado(Boolean mspHabilitado) {
        this.mspHabilitado = mspHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mspPk != null ? mspPk.hashCode() : 0);
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
        final SgMotivosSeleccionPLaza other = (SgMotivosSeleccionPLaza) obj;
        if (!Objects.equals(this.mspPk, other.mspPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgMotivosSeleccionPLaza[ mspPk=" + mspPk + " ]";
    }
    
}
