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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "ipePk", scope = SgInfoProcesamientoCalificacionEst.class)
public class SgInfoProcesamientoCalificacionEst implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long ipePk;

    private String ipeError;    

    private LocalDateTime ipeUltModFecha;   

    private String ipeUltModUsuario;

    private Integer ipeVersion;
    
    private Boolean ipePromocionPendiente;
    
    public SgInfoProcesamientoCalificacionEst() {
       
    }

    public Long getIpePk() {
        return ipePk;
    }

    public void setIpePk(Long ipePk) {
        this.ipePk = ipePk;
    }

    public String getIpeError() {
        return ipeError;
    }

    public void setIpeError(String ipeError) {
        this.ipeError = ipeError;
    }

    public LocalDateTime getIpeUltModFecha() {
        return ipeUltModFecha;
    }

    public void setIpeUltModFecha(LocalDateTime ipeUltModFecha) {
        this.ipeUltModFecha = ipeUltModFecha;
    }

    public String getIpeUltModUsuario() {
        return ipeUltModUsuario;
    }

    public void setIpeUltModUsuario(String ipeUltModUsuario) {
        this.ipeUltModUsuario = ipeUltModUsuario;
    }

    public Integer getIpeVersion() {
        return ipeVersion;
    }

    public void setIpeVersion(Integer ipeVersion) {
        this.ipeVersion = ipeVersion;
    }

    public Boolean getIpePromocionPendiente() {
        return ipePromocionPendiente;
    }

    public void setIpePromocionPendiente(Boolean ipePromocionPendiente) {
        this.ipePromocionPendiente = ipePromocionPendiente;
    }

    


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ipePk != null ? ipePk.hashCode() : 0);
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
        final SgInfoProcesamientoCalificacionEst other = (SgInfoProcesamientoCalificacionEst) obj;
        if (!Objects.equals(this.ipePk, other.ipePk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgAcuerdo[ acuPk=" + ipePk + " ]";
    }
    
}
