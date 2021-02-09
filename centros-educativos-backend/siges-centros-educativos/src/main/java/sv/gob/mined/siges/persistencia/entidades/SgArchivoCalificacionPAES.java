/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumEstadoArchivoCalificacionPAES;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_archivo_calificacion_paes", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "acpPk", scope = SgArchivoCalificacionPAES.class)
public class SgArchivoCalificacionPAES implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "acp_pk")
    private Long acpPk;
    
    @JoinColumn(name = "acp_archivo_fk", referencedColumnName = "ach_pk")
    @ManyToOne(cascade = CascadeType.ALL)
    private SgArchivo acpArchivo;
    
    @JoinColumn(name = "acp_periodo_calificacion_fk", referencedColumnName = "pca_pk")
    @ManyToOne
    private SgPeriodoCalificacion acpPeriodoCalificacion;
    
    @JoinColumn(name = "acp_rango_fecha_fk", referencedColumnName = "rfe_pk")
    @ManyToOne
    private SgRangoFecha acpRangoFecha;
    
    @Column(name = "acp_estado_archivo")
    @Enumerated(value = EnumType.STRING)
    private EnumEstadoArchivoCalificacionPAES acpEstadoArchivo;
    
    @Column(name = "acp_resultado")
    private String acpResultado;
    
    @Column(name = "acp_anio")
    private Integer acpAnio;
    
    @Column(name = "acp_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime acpUltModFecha;
    
    @Size(max = 45)
    @Column(name = "acp_ult_mod_usuario")
    @AtributoUltimoUsuario
    private String acpUltModUsuario;
    
    @Column(name = "acp_version")
    @Version
    private Integer acpVersion;

    public SgArchivoCalificacionPAES() {
    }

    public SgArchivoCalificacionPAES(Long acpPk) {
        this.acpPk = acpPk;
    }

    
    //<editor-fold defaultstate="collapsed" desc="GET & SET">

    public Long getAcpPk() {
        return acpPk;
    }

    public void setAcpPk(Long acpPk) {
        this.acpPk = acpPk;
    }

    public SgArchivo getAcpArchivo() {
        return acpArchivo;
    }

    public void setAcpArchivo(SgArchivo acpArchivo) {
        this.acpArchivo = acpArchivo;
    }

    public SgPeriodoCalificacion getAcpPeriodoCalificacion() {
        return acpPeriodoCalificacion;
    }

    public void setAcpPeriodoCalificacion(SgPeriodoCalificacion acpPeriodoCalificacion) {
        this.acpPeriodoCalificacion = acpPeriodoCalificacion;
    }

    public SgRangoFecha getAcpRangoFecha() {
        return acpRangoFecha;
    }

    public void setAcpRangoFecha(SgRangoFecha acpRangoFecha) {
        this.acpRangoFecha = acpRangoFecha;
    }

    public EnumEstadoArchivoCalificacionPAES getAcpEstadoArchivo() {
        return acpEstadoArchivo;
    }

    public void setAcpEstadoArchivo(EnumEstadoArchivoCalificacionPAES acpEstadoArchivo) {
        this.acpEstadoArchivo = acpEstadoArchivo;
    }

    public String getAcpResultado() {
        return acpResultado;
    }

    public void setAcpResultado(String acpResultado) {
        this.acpResultado = acpResultado;
    }

    public LocalDateTime getAcpUltModFecha() {
        return acpUltModFecha;
    }

    public void setAcpUltModFecha(LocalDateTime acpUltModFecha) {
        this.acpUltModFecha = acpUltModFecha;
    }

    public String getAcpUltModUsuario() {
        return acpUltModUsuario;
    }

    public void setAcpUltModUsuario(String acpUltModUsuario) {
        this.acpUltModUsuario = acpUltModUsuario;
    }

    public Integer getAcpVersion() {
        return acpVersion;
    }

    public void setAcpVersion(Integer acpVersion) {
        this.acpVersion = acpVersion;
    }
    
    //</editor-fold>

    public Integer getAcpAnio() {
        return acpAnio;
    }

    public void setAcpAnio(Integer acpAnio) {
        this.acpAnio = acpAnio;
    }



    @Override
    public int hashCode() {
        int hash = 0;
        hash += (acpPk != null ? acpPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgArchivoCalificacionPAES)) {
            return false;
        }
        SgArchivoCalificacionPAES other = (SgArchivoCalificacionPAES) object;
        if ((this.acpPk == null && other.acpPk != null) || (this.acpPk != null && !this.acpPk.equals(other.acpPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgArchivoCalificacionPAES[ acpPk=" + acpPk + " ]";
    }
    
}