/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "ioaPk", scope = SgIdentificacionOAE.class)
public class SgIdentificacionOAE implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long ioaPk;

    private String ioaCodigo;

    private String ioaNombre;
    
    private Boolean ioaEsObligatorio;

    private Boolean ioaHabilitado;

    private LocalDateTime ioaUltModFecha;

    private String ioaUltModUsuario;

    private Integer ioaVersion;
    
    
    public SgIdentificacionOAE() {
        this.ioaHabilitado = Boolean.TRUE;
        this.ioaEsObligatorio = Boolean.TRUE;
    }

    public Long getIoaPk() {
        return ioaPk;
    }

    public void setIoaPk(Long ioaPk) {
        this.ioaPk = ioaPk;
    }

    public String getIoaCodigo() {
        return ioaCodigo;
    }

    public void setIoaCodigo(String ioaCodigo) {
        this.ioaCodigo = ioaCodigo;
    }

    public String getIoaNombre() {
        return ioaNombre;
    }

    public void setIoaNombre(String ioaNombre) {
        this.ioaNombre = ioaNombre;
    }

    public LocalDateTime getIoaUltModFecha() {
        return ioaUltModFecha;
    }

    public void setIoaUltModFecha(LocalDateTime ioaUltModFecha) {
        this.ioaUltModFecha = ioaUltModFecha;
    }

    public String getIoaUltModUsuario() {
        return ioaUltModUsuario;
    }

    public void setIoaUltModUsuario(String ioaUltModUsuario) {
        this.ioaUltModUsuario = ioaUltModUsuario;
    }

    public Integer getIoaVersion() {
        return ioaVersion;
    }

    public void setIoaVersion(Integer ioaVersion) {
        this.ioaVersion = ioaVersion;
    }

    
     public Boolean getIoaHabilitado() {
        return ioaHabilitado;
    }

    public void setIoaHabilitado(Boolean ioaHabilitado) {
        this.ioaHabilitado = ioaHabilitado;
    }

    public Boolean getIoaEsObligatorio() {
        return ioaEsObligatorio;
    }

    public void setIoaEsObligatorio(Boolean ioaEsObligatorio) {
        this.ioaEsObligatorio = ioaEsObligatorio;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ioaPk != null ? ioaPk.hashCode() : 0);
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
        final SgIdentificacionOAE other = (SgIdentificacionOAE) obj;
        if (!Objects.equals(this.ioaPk, other.ioaPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgIdentificacionOAE[ ioaPk=" + ioaPk + " ]";
    }
    
}
