/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.constantes.Constantes;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "sg_info_procesamiento_calificacion_est", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "ipePk", scope = SgInfoProcesamientoCalificacionEst.class)
@Audited
public class SgInfoProcesamientoCalificacionEst implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ipe_pk")
    private Long ipePk;

    
    @Column(name = "ipe_error")
    private String ipeError;
    
    @Column(name = "ipe_ult_mod_fecha")
    @AtributoUltimaModificacion 
    private LocalDateTime ipeUltModFecha;
    
    @Size(max = 45)
    @Column(name = "ipe_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String ipeUltModUsuario;
    
    @Column(name = "ipe_version")
    @Version
    private Integer ipeVersion;
    
    @Column(name = "ipe_promocion_pendiente")
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
        return "sv.gob.mined.siges.persistencia.entidades.SigesCiclo[ ipePk=" + ipePk + " ]";
    }

  

}
