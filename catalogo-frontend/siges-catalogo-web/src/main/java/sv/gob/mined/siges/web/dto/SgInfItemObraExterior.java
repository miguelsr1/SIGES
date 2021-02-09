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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ioePk", scope = SgInfItemObraExterior.class)
public class SgInfItemObraExterior implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long ioePk;

    private String ioeCodigo;

    private String ioeNombre;

    private Boolean ioeHabilitado;

    private LocalDateTime ioeUltModFecha;

    private String ioeUltModUsuario;

    private Integer ioeVersion;
    
    private Boolean ioeAplicaOtros;
    
    private Integer ioeOrden;
    
    private SgInfObraExterior ioeObraExterior;
    
    
    public SgInfItemObraExterior() {
        this.ioeHabilitado = Boolean.TRUE;
    }

    public Long getIoePk() {
        return ioePk;
    }

    public void setIoePk(Long ioePk) {
        this.ioePk = ioePk;
    }

    public String getIoeCodigo() {
        return ioeCodigo;
    }

    public void setIoeCodigo(String ioeCodigo) {
        this.ioeCodigo = ioeCodigo;
    }

    public String getIoeNombre() {
        return ioeNombre;
    }

    public void setIoeNombre(String ioeNombre) {
        this.ioeNombre = ioeNombre;
    }

    public LocalDateTime getIoeUltModFecha() {
        return ioeUltModFecha;
    }

    public void setIoeUltModFecha(LocalDateTime ioeUltModFecha) {
        this.ioeUltModFecha = ioeUltModFecha;
    }

    public String getIoeUltModUsuario() {
        return ioeUltModUsuario;
    }

    public void setIoeUltModUsuario(String ioeUltModUsuario) {
        this.ioeUltModUsuario = ioeUltModUsuario;
    }

    public Integer getIoeVersion() {
        return ioeVersion;
    }

    public void setIoeVersion(Integer ioeVersion) {
        this.ioeVersion = ioeVersion;
    }

    
     public Boolean getIoeHabilitado() {
        return ioeHabilitado;
    }

    public void setIoeHabilitado(Boolean ioeHabilitado) {
        this.ioeHabilitado = ioeHabilitado;
    }

    public Boolean getIoeAplicaOtros() {
        return ioeAplicaOtros;
    }

    public void setIoeAplicaOtros(Boolean ioeAplicaOtros) {
        this.ioeAplicaOtros = ioeAplicaOtros;
    }

    public Integer getIoeOrden() {
        return ioeOrden;
    }

    public void setIoeOrden(Integer ioeOrden) {
        this.ioeOrden = ioeOrden;
    }

    public SgInfObraExterior getIoeObraExterior() {
        return ioeObraExterior;
    }

    public void setIoeObraExterior(SgInfObraExterior ioeObraExterior) {
        this.ioeObraExterior = ioeObraExterior;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ioePk != null ? ioePk.hashCode() : 0);
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
        final SgInfItemObraExterior other = (SgInfItemObraExterior) obj;
        if (!Objects.equals(this.ioePk, other.ioePk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgInfItemObraExterior[ ioePk=" + ioePk + " ]";
    }
    
}
