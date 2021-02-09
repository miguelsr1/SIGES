/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
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
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

/**
 *
 * @author usuario
 */
    
@Entity
@Table(name = "sg_rango_fecha", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "rfePk", scope = SgRangoFecha.class)
public class SgRangoFecha implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rfe_pk")
    private Long rfePk;
  
    @Size(max = 4)
    @Column(name = "rfe_codigo",length = 4)
    @AtributoCodigo
    private String rfeCodigo;

    
    @Basic(optional = false)
    @Column(name = "rfe_fecha_desde")
    private LocalDate rfeFechaDesde;
    
    @Basic(optional = false)
    @Column(name = "rfe_fecha_hasta")
    private LocalDate rfeFechaHasta;

    
    @Column(name = "rfe_habilitado")
    @AtributoHabilitado
    private Boolean rfeHabilitado;
    
    @Column(name = "rfe_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime rfeUltModFecha;
    
    @Size(max = 45)
    @Column(name = "rfe_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String rfeUltModUsuario;
    
    @Column(name = "rfe_version")
    @Version
    private Integer rfeVersion;    
        
    @JoinColumn(name = "rfe_periodo_calificacion_fk", referencedColumnName = "pca_pk")
    @ManyToOne
    private SgPeriodoCalificacion rfePeriodoCalificacion;
    
    public SgRangoFecha() {
    }

    public Long getRfePk() {
        return rfePk;
    }

    public void setRfePk(Long rfePk) {
        this.rfePk = rfePk;
    }

    public String getRfeCodigo() {
        return rfeCodigo;
    }

    public void setRfeCodigo(String rfeCodigo) {
        this.rfeCodigo = rfeCodigo;
    }

    public LocalDate getRfeFechaDesde() {
        return rfeFechaDesde;
    }

    public void setRfeFechaDesde(LocalDate rfeFechaDesde) {
        this.rfeFechaDesde = rfeFechaDesde;
    }

    public LocalDate getRfeFechaHasta() {
        return rfeFechaHasta;
    }

    public void setRfeFechaHasta(LocalDate rfeFechaHasta) {
        this.rfeFechaHasta = rfeFechaHasta;
    }

    public Boolean getRfeHabilitado() {
        return rfeHabilitado;
    }

    public void setRfeHabilitado(Boolean rfeHabilitado) {
        this.rfeHabilitado = rfeHabilitado;
    }

    public LocalDateTime getRfeUltModFecha() {
        return rfeUltModFecha;
    }

    public void setRfeUltModFecha(LocalDateTime rfeUltModFecha) {
        this.rfeUltModFecha = rfeUltModFecha;
    }

    public String getRfeUltModUsuario() {
        return rfeUltModUsuario;
    }

    public void setRfeUltModUsuario(String rfeUltModUsuario) {
        this.rfeUltModUsuario = rfeUltModUsuario;
    }

    public Integer getRfeVersion() {
        return rfeVersion;
    }

    public void setRfeVersion(Integer rfeVersion) {
        this.rfeVersion = rfeVersion;
    }

    public SgPeriodoCalificacion getRfePeriodoCalificacion() {
        return rfePeriodoCalificacion;
    }

    public void setRfePeriodoCalificacion(SgPeriodoCalificacion rfePeriodoCalificacion) {
        this.rfePeriodoCalificacion = rfePeriodoCalificacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rfePk != null ? rfePk.hashCode() : 0);
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
        final SgRangoFecha other = (SgRangoFecha) obj;
        if (!Objects.equals(this.rfePk, other.rfePk)) {
            return false;
        }
        return true;
    }
    
    
    
    
    
}
