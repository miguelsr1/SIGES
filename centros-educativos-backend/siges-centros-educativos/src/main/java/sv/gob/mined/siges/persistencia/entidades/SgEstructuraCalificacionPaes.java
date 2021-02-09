/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
import javax.persistence.CascadeType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumEstadoArchivoCalificacionPAES;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_estructura_calificacion_paes", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ecpPk", scope = SgEstructuraCalificacionPaes.class)
@Audited
public class SgEstructuraCalificacionPaes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ecp_pk", nullable = false)
    private Long ecpPk;
    
    @Column(name = "ecp_nie")
    private String ecpNie;
    
    @Column(name = "ecp_estado")
    @Enumerated(value = EnumType.STRING)
    private EnumEstadoArchivoCalificacionPAES ecpEstado;

  
    @Column(name = "ecp_calificacion")
    private String ecpCalificacion;

    @Column(name = "ecp_codigo_cpe")
    private String ecpCodigoCpe;
    
    @Column(name = "ecp_resultado")
    private String ecpResultado;

    @Column(name = "ecp_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime ecpUltModFecha;

    @Size(max = 45)
    @Column(name = "ecp_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String ecpUltModUsuario;

    @Column(name = "ecp_version")
    @Version
    private Integer ecpVersion;
    
    @JoinColumn(name = "ecp_archivo_calificacion_paes_fk")
    @ManyToOne(cascade = CascadeType.MERGE)
    private SgArchivoCalificacionPAES ecpArchivoCalificacionPaesFk;
    
    @JoinColumn(name = "ecp_persona_fk")
    @ManyToOne
    private SgPersona ecpPersonaFk;

    public SgEstructuraCalificacionPaes() {
        
    }
    
    @PrePersist
    public void preSave() {
        if(this.ecpEstado==null){
            ecpEstado=EnumEstadoArchivoCalificacionPAES.SIN_PROCESAR;
        }
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

    public String getEcpResultado() {
        return ecpResultado;
    }

    public void setEcpResultado(String ecpResultado) {
        this.ecpResultado = ecpResultado;
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

    public String getEcpNie() {
        return ecpNie;
    }

    public void setEcpNie(String ecpNie) {
        this.ecpNie = ecpNie;
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

    public SgArchivoCalificacionPAES getEcpArchivoCalificacionPaesFk() {
        return ecpArchivoCalificacionPaesFk;
    }

    public void setEcpArchivoCalificacionPaesFk(SgArchivoCalificacionPAES ecpArchivoCalificacionPaesFk) {
        this.ecpArchivoCalificacionPaesFk = ecpArchivoCalificacionPaesFk;
    }

    public EnumEstadoArchivoCalificacionPAES getEcpEstado() {
        return ecpEstado;
    }

    public void setEcpEstado(EnumEstadoArchivoCalificacionPAES ecpEstado) {
        this.ecpEstado = ecpEstado;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.ecpPk);
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
        return "SgEstructuraCalificacionPaes{" + "ecpPk=" + ecpPk + ", ecpNie=" + ecpNie + ", ecpCalificacion=" + ecpCalificacion + ", ecpCodigoCpe=" + ecpCodigoCpe + ", ecpUltModFecha=" + ecpUltModFecha + ", ecpUltModUsuario=" + ecpUltModUsuario + ", ecpVersion=" + ecpVersion + ", ecpArchivoCalificacionPaesFk=" + ecpArchivoCalificacionPaesFk + '}';
    }



}
