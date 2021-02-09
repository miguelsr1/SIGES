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
import sv.gob.mined.siges.web.enumerados.EnumEstadoArchivoCalificacionPAES;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "ecpPk", scope = SgEstructuraCalificacionPaes.class)
public class SgEstructuraCalificacionPaes implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long ecpPk;

    private String ecpNie;
    
    private EnumEstadoArchivoCalificacionPAES ecpEstado;
    
    private String ecpCalificacion;

    private String ecpCodigoCpe;

    private String ecpResultado;

    private LocalDateTime ecpUltModFecha;

    private String ecpUltModUsuario;

    private SgPersona ecpPersonaFk;

    private Integer ecpVersion;

    private SgArchivoCalificacionPAES ecpArchivoCalificacionPaesFk;
    
    
    public SgEstructuraCalificacionPaes() {
    }

    public Long getEcpPk() {
        return ecpPk;
    }

    public void setEcpPk(Long ecpPk) {
        this.ecpPk = ecpPk;
    }

    public SgPersona getEcpPersonaFk() {
        return ecpPersonaFk;
    }

    public void setEcpPersonaFk(SgPersona ecpPersonaFk) {
        this.ecpPersonaFk = ecpPersonaFk;
    }


    public String getEcpNie() {
        return ecpNie;
    }

    public void setEcpNie(String ecpNie) {
        this.ecpNie = ecpNie;
    }

    public EnumEstadoArchivoCalificacionPAES getEcpEstado() {
        return ecpEstado;
    }

    public void setEcpEstado(EnumEstadoArchivoCalificacionPAES ecpEstado) {
        this.ecpEstado = ecpEstado;
    }

    public String getEcpCalificacion() {
        return ecpCalificacion;
    }

    public void setEcpCalificacion(String ecpCalificacion) {
        this.ecpCalificacion = ecpCalificacion;
    }

    public String getEcpCodigoCpe() {
        return ecpCodigoCpe;
    }

    public void setEcpCodigoCpe(String ecpCodigoCpe) {
        this.ecpCodigoCpe = ecpCodigoCpe;
    }

    public String getEcpResultado() {
        return ecpResultado;
    }

    public void setEcpResultado(String ecpResultado) {
        this.ecpResultado = ecpResultado;
    }

    public SgArchivoCalificacionPAES getEcpArchivoCalificacionPaesFk() {
        return ecpArchivoCalificacionPaesFk;
    }

    public void setEcpArchivoCalificacionPaesFk(SgArchivoCalificacionPAES ecpArchivoCalificacionPaesFk) {
        this.ecpArchivoCalificacionPaesFk = ecpArchivoCalificacionPaesFk;
    }
  


    public LocalDateTime getEcpUltModFecha() {
        return ecpUltModFecha;
    }

    public void setEcpUltModFecha(LocalDateTime ecpUltModFecha) {
        this.ecpUltModFecha = ecpUltModFecha;
    }

    public String getEcpUltModUsuario() {
        return ecpUltModUsuario;
    }

    public void setEcpUltModUsuario(String ecpUltModUsuario) {
        this.ecpUltModUsuario = ecpUltModUsuario;
    }

    public Integer getEcpVersion() {
        return ecpVersion;
    }

    public void setEcpVersion(Integer ecpVersion) {
        this.ecpVersion = ecpVersion;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ecpPk != null ? ecpPk.hashCode() : 0);
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
        final SgEstructuraCalificacionPaes other = (SgEstructuraCalificacionPaes) obj;
        if (!Objects.equals(this.ecpPk, other.ecpPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgEstructuraCalificacionPaes[ ecpPk=" + ecpPk + " ]";
    }
    
}
