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
import sv.gob.mined.siges.enumerados.EnumEstadosProceso;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAfFuenteFinanciamiento;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAfProyectos;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_af_depreciaciones_maestro", schema = Constantes.SCHEMA_ACTIVO_FIJO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "dmaPk", scope = SgAfDepreciacionMaestro.class)
public class SgAfDepreciacionMaestro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "dma_pk")
    private Long dmaPk;
    
    @Basic(optional = false)
    @Column(name = "dma_anio_proceso")
    private Integer dmaAnioProceso;
    
    @Column(name = "dma_mes_proceso")
    private Integer dmaMesProceso;

    @JoinColumn(name = "dma_fuente_financiamiento_fk", referencedColumnName = "ffi_pk")
    @ManyToOne
    private SgAfFuenteFinanciamiento dmaFuenteFinanciamientoFk;
      
    @JoinColumn(name = "dma_proyecto_fk", referencedColumnName = "pro_pk")
    @ManyToOne
    private SgAfProyectos dmaProyectoFk;
    
    @Size(max = 20)
    @Column(name = "dma_codigo_inventario", length = 20)
    private String dmaCodigoInventario;
    
    @Column(name = "dma_fecha_creacion")
    private LocalDateTime dmaFechaCreacion;
    
    @Column(name = "dma_fecha_inicio_procesamiento")
    private LocalDateTime dmaFechaInicioProcesamiento;
    
    @Column(name = "dma_fecha_final_procesamiento")
    private LocalDateTime dmaFechaFinalProcesamiento;

    @Column(name = "dma_estado")
    @Enumerated(value = EnumType.STRING)
    private EnumEstadosProceso dmaEstado;
    
    @Column(name = "dma_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime dmaUltModFecha;
    
    @Size(max = 45)
    @Column(name = "dma_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String dmaUltModUsuario;
    
    @Column(name = "dma_version")
    @Version
    private Integer dmaVersion;

    public SgAfDepreciacionMaestro() {
    }

    public Long getDmaPk() {
        return dmaPk;
    }

    public void setDmaPk(Long dmaPk) {
        this.dmaPk = dmaPk;
    }

    public Integer getDmaAnioProceso() {
        return dmaAnioProceso;
    }

    public void setDmaAnioProceso(Integer dmaAnioProceso) {
        this.dmaAnioProceso = dmaAnioProceso;
    }

    public Integer getDmaMesProceso() {
        return dmaMesProceso;
    }

    public void setDmaMesProceso(Integer dmaMesProceso) {
        this.dmaMesProceso = dmaMesProceso;
    }

    public SgAfFuenteFinanciamiento getDmaFuenteFinanciamientoFk() {
        return dmaFuenteFinanciamientoFk;
    }

    public void setDmaFuenteFinanciamientoFk(SgAfFuenteFinanciamiento dmaFuenteFinanciamientoFk) {
        this.dmaFuenteFinanciamientoFk = dmaFuenteFinanciamientoFk;
    }

    public SgAfProyectos getDmaProyectoFk() {
        return dmaProyectoFk;
    }

    public void setDmaProyectoFk(SgAfProyectos dmaProyectoFk) {
        this.dmaProyectoFk = dmaProyectoFk;
    }

    public String getDmaCodigoInventario() {
        return dmaCodigoInventario;
    }

    public void setDmaCodigoInventario(String dmaCodigoInventario) {
        this.dmaCodigoInventario = dmaCodigoInventario;
    }

    public LocalDateTime getDmaFechaCreacion() {
        return dmaFechaCreacion;
    }

    public void setDmaFechaCreacion(LocalDateTime dmaFechaCreacion) {
        this.dmaFechaCreacion = dmaFechaCreacion;
    }

    public LocalDateTime getDmaFechaInicioProcesamiento() {
        return dmaFechaInicioProcesamiento;
    }

    public void setDmaFechaInicioProcesamiento(LocalDateTime dmaFechaInicioProcesamiento) {
        this.dmaFechaInicioProcesamiento = dmaFechaInicioProcesamiento;
    }

    public LocalDateTime getDmaFechaFinalProcesamiento() {
        return dmaFechaFinalProcesamiento;
    }

    public void setDmaFechaFinalProcesamiento(LocalDateTime dmaFechaFinalProcesamiento) {
        this.dmaFechaFinalProcesamiento = dmaFechaFinalProcesamiento;
    }

    
    
    public EnumEstadosProceso getDmaEstado() {
        return dmaEstado;
    }

    /**
    public String getDmaEstado() {
    return dmaEstado;
    }
    public void setDmaEstado(String dmaEstado) {
    this.dmaEstado = dmaEstado;
    }
     **/
    public void setDmaEstado(EnumEstadosProceso dmaEstado) {
        this.dmaEstado = dmaEstado;
    }

    public LocalDateTime getDmaUltModFecha() {
        return dmaUltModFecha;
    }

    public void setDmaUltModFecha(LocalDateTime dmaUltModFecha) {
        this.dmaUltModFecha = dmaUltModFecha;
    }

    public String getDmaUltModUsuario() {
        return dmaUltModUsuario;
    }

    public void setDmaUltModUsuario(String dmaUltModUsuario) {
        this.dmaUltModUsuario = dmaUltModUsuario;
    }

    public Integer getDmaVersion() {
        return dmaVersion;
    }

    public void setDmaVersion(Integer dmaVersion) {
        this.dmaVersion = dmaVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dmaPk != null ? dmaPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAfDepreciacionMaestro)) {
            return false;
        }
        SgAfDepreciacionMaestro other = (SgAfDepreciacionMaestro) object;
        if ((this.dmaPk == null && other.dmaPk != null) || (this.dmaPk != null && !this.dmaPk.equals(other.dmaPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgAfDepreciacionMaestro[ dmaPk=" + dmaPk + " ]";
    }
    
}
