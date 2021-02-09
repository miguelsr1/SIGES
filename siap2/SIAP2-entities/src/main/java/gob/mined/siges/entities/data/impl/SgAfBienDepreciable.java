/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package gob.mined.siges.entities.data.impl;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.constantes.Constantes;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_af_bienes_depreciables", uniqueConstraints = {
    @UniqueConstraint(name = "codigo_inventario_uk", columnNames = {"bde_codigo_inventario"})}, schema = Constantes.SCHEMA_ACTIVO_FIJO)
@EntityListeners({DecoratorEntityListener.class})
@Cacheable(false)
public class SgAfBienDepreciable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "sg_af_bienes_depreciables_pk_seq", sequenceName = Constantes.SCHEMA_ACTIVO_FIJO + ".sg_af_bienes_depreciables_pk_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sg_af_bienes_depreciables_pk_seq")
    @Column(name = "bde_pk")
    private Long bdePk;

    @Size(max = 10)
    @Column(name = "bde_codigo_correlativo", length = 10)
    private String bdeCodigoCorrelativo;

    @Column(name = "bde_num_correlativo", length = 10)
    private Integer bdeNumCorrelativo;
    
    @Column(name = "bde_numero_correlativo_siap", length = 10)
    private Integer bdeNumCorrelativoSiap;
    
    @Size(max = 20)
    @Column(name = "bde_codigo_inventario", length = 20)
    private String bdeCodigoInventario;
    
    @JoinColumn(name = "bde_tipo_bien_fk")
    @ManyToOne
    private SgAfTipoBienes bdeTipoBien;
    
    @JoinColumn(name = "bde_calidad_fk")
    @ManyToOne
    private SgAfEstadosCalidad bdeEstadoCalidad;
    
    @JoinColumn(name = "bde_estado_fk")
    @ManyToOne
    private SgAfEstadosBienes bdeEstadosBienes;
    
    @JoinColumn(name = "bde_forma_adquisicion_fk")
    @ManyToOne
    private SgAfFormaAdquisicion bdeFormaAdquisicion;
    
    @JoinColumn(name = "bde_fuente_financiamiento_fk")
    @ManyToOne
    private SgAfFuenteFinanciamiento bdeFuenteFinanciamiento;
    
    @JoinColumn(name = "bde_categoria_fk")
    @ManyToOne
    private SgAfCategoriaBienes bdeCategoriaFk;
    
    @Size(max = 750)
    @Column(name = "bde_descripcion", length = 750)
    private String bdeDescripcion;
    
    @Size(max = 100)
    @Column(name = "bde_documento_adquisicion", length = 100)
    private String bdeDocumentoAdquisicion;
    
    @Column(name = "bde_fecha_adquisicion")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date bdeFechaAdquisicion;
    
    @Column(name = "bde_fecha_creacion")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date bdeFechaCreacion;
    
    @Column(name = "bde_valor_adquisicion")
    private BigDecimal bdeValorAdquisicion;
    
    @Column(name = "bde_es_valor_estimado")
    private Boolean bdeEsValorEstimado;
    
    @Column(name = "bde_depreciado")
    private Boolean bdeDepreciado;
    
    @Column(name = "bde_depreciado_completo")
    private Boolean bdeDepreciadoCompleto;
    
    @Column(name = "bde_tipo_bien_categoria_vinculada")
    private Boolean bdeTipoBienCategoriaVinculada;
    
    @Size(max = 100)
    @Column(name = "bde_proveedor", length = 100)
    private String bdeProveedor;
    
    @Size(max = 30)
    @Column(name = "bde_numero_partida", length = 30)
    private String bdeNumeroPartida;
    
    @Column(name = "bde_es_lote")
    private Boolean bdeEsLote;
    
    @Column(name = "bde_bien_es_fuente_siap")
    private Boolean bdeBienEsFuenteSiap;
    
    @Column(name = "bde_es_lote_siap")
    private Boolean bdeEsLoteSiap;
    
    @Column(name = "bde_cantidad_lote")
    private Integer bdeCantidadLote;
    
    @Column(name = "bde_valor_residual", length = 20)
    private BigDecimal bdeValorResidual;
    
    @Column(name = "bde_vida_util", length = 20)
    private Integer bdeVidaUtil;
    
    @Column(name = "bde_completado")
    private Boolean bdeCompletado;

    //Auditoria
    @Column(name = "bde_ult_mod_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "bde_ult_mod_fecha")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;
    

    @Column(name = "bde_version")
    @Version
    private Integer bdeVersion;
    
    public SgAfBienDepreciable() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        if(StringUtils.isNotBlank(bdeCodigoCorrelativo)) {
            this.bdeNumCorrelativo = Integer.parseInt(bdeCodigoCorrelativo);
        }
    }
    
    public Long getBdePk() {
        return bdePk;
    }

    public void setBdePk(Long bdePk) {
        this.bdePk = bdePk;
    }

    public String getBdeCodigoCorrelativo() {
        return bdeCodigoCorrelativo;
    }

    public void setBdeCodigoCorrelativo(String bdeCodigoCorrelativo) {
        this.bdeCodigoCorrelativo = bdeCodigoCorrelativo;
    }

    public String getBdeCodigoInventario() {
        return bdeCodigoInventario;
    }

    public void setBdeCodigoInventario(String bdeCodigoInventario) {
        this.bdeCodigoInventario = bdeCodigoInventario;
    }

    public Integer getBdeNumCorrelativo() {
        return bdeNumCorrelativo;
    }

    public void setBdeNumCorrelativo(Integer bdeNumCorrelativo) {
        this.bdeNumCorrelativo = bdeNumCorrelativo;
    }

    public SgAfTipoBienes getBdeTipoBien() {
        return bdeTipoBien;
    }

    public void setBdeTipoBien(SgAfTipoBienes bdeTipoBien) {
        this.bdeTipoBien = bdeTipoBien;
    }

    public SgAfEstadosBienes getBdeEstadosBienes() {
        return bdeEstadosBienes;
    }

    public void setBdeEstadosBienes(SgAfEstadosBienes bdeEstadosBienes) {
        this.bdeEstadosBienes = bdeEstadosBienes;
    }

    public SgAfEstadosCalidad getBdeEstadoCalidad() {
        return bdeEstadoCalidad;
    }

    public void setBdeEstadoCalidad(SgAfEstadosCalidad bdeEstadoCalidad) {
        this.bdeEstadoCalidad = bdeEstadoCalidad;
    }

    public String getUltUsuario() {
        return ultUsuario;
    }

    public void setUltUsuario(String ultUsuario) {
        this.ultUsuario = ultUsuario;
    }

    public Date getUltMod() {
        return ultMod;
    }

    public void setUltMod(Date ultMod) {
        this.ultMod = ultMod;
    }
    
    public Integer getBdeVersion() {
        return bdeVersion;
    }

    public void setBdeVersion(Integer bdeVersion) {
        this.bdeVersion = bdeVersion;
    }

    public String getBdeDescripcion() {
        return bdeDescripcion;
    }

    public void setBdeDescripcion(String bdeDescripcion) {
        this.bdeDescripcion = bdeDescripcion;
    }

    public String getBdeDocumentoAdquisicion() {
        return bdeDocumentoAdquisicion;
    }

    public void setBdeDocumentoAdquisicion(String bdeDocumentoAdquisicion) {
        this.bdeDocumentoAdquisicion = bdeDocumentoAdquisicion;
    }

    public Date getBdeFechaAdquisicion() {
        return bdeFechaAdquisicion;
    }

    public void setBdeFechaAdquisicion(Date bdeFechaAdquisicion) {
        this.bdeFechaAdquisicion = bdeFechaAdquisicion;
    }

    public SgAfFuenteFinanciamiento getBdeFuenteFinanciamiento() {
        return bdeFuenteFinanciamiento;
    }

    public void setBdeFuenteFinanciamiento(SgAfFuenteFinanciamiento bdeFuenteFinanciamiento) {
        this.bdeFuenteFinanciamiento = bdeFuenteFinanciamiento;
    }

    public BigDecimal getBdeValorAdquisicion() {
        return bdeValorAdquisicion;
    }

    public void setBdeValorAdquisicion(BigDecimal bdeValorAdquisicion) {
        this.bdeValorAdquisicion = bdeValorAdquisicion;
    }

    public Boolean getBdeEsValorEstimado() {
        return bdeEsValorEstimado;
    }

    public void setBdeEsValorEstimado(Boolean bdeEsValorEstimado) {
        this.bdeEsValorEstimado = bdeEsValorEstimado;
    }

    public Boolean getBdeDepreciado() {
        return bdeDepreciado;
    }

    public void setBdeDepreciado(Boolean bdeDepreciado) {
        this.bdeDepreciado = bdeDepreciado;
    }

    public Boolean getBdeDepreciadoCompleto() {
        return bdeDepreciadoCompleto;
    }

    public void setBdeDepreciadoCompleto(Boolean bdeDepreciadoCompleto) {
        this.bdeDepreciadoCompleto = bdeDepreciadoCompleto;
    }

    public Boolean getBdeTipoBienCategoriaVinculada() {
        return bdeTipoBienCategoriaVinculada;
    }

    public void setBdeTipoBienCategoriaVinculada(Boolean bdeTipoBienCategoriaVinculada) {
        this.bdeTipoBienCategoriaVinculada = bdeTipoBienCategoriaVinculada;
    }

    public String getBdeProveedor() {
        return bdeProveedor;
    }

    public void setBdeProveedor(String bdeProveedor) {
        this.bdeProveedor = bdeProveedor;
    }

    public String getBdeNumeroPartida() {
        return bdeNumeroPartida;
    }

    public void setBdeNumeroPartida(String bdeNumeroPartida) {
        this.bdeNumeroPartida = bdeNumeroPartida;
    }

    public Boolean getBdeEsLote() {
        return bdeEsLote;
    }

    public void setBdeEsLote(Boolean bdeEsLote) {
        this.bdeEsLote = bdeEsLote;
    }

    public Integer getBdeCantidadLote() {
        return bdeCantidadLote;
    }

    public void setBdeCantidadLote(Integer bdeCantidadLote) {
        this.bdeCantidadLote = bdeCantidadLote;
    }

    public BigDecimal getBdeValorResidual() {
        return bdeValorResidual;
    }

    public void setBdeValorResidual(BigDecimal bdeValorResidual) {
        this.bdeValorResidual = bdeValorResidual;
    }

    public Integer getBdeVidaUtil() {
        return bdeVidaUtil;
    }

    public void setBdeVidaUtil(Integer bdeVidaUtil) {
        this.bdeVidaUtil = bdeVidaUtil;
    }

    public Boolean getBdeCompletado() {
        return bdeCompletado;
    }

    public void setBdeCompletado(Boolean bdeCompletado) {
        this.bdeCompletado = bdeCompletado;
    }

    public Date getBdeFechaCreacion() {
        return bdeFechaCreacion;
    }

    public void setBdeFechaCreacion(Date bdeFechaCreacion) {
        this.bdeFechaCreacion = bdeFechaCreacion;
    }

    public SgAfFormaAdquisicion getBdeFormaAdquisicion() {
        return bdeFormaAdquisicion;
    }

    public void setBdeFormaAdquisicion(SgAfFormaAdquisicion bdeFormaAdquisicion) {
        this.bdeFormaAdquisicion = bdeFormaAdquisicion;
    }

    public SgAfCategoriaBienes getBdeCategoriaFk() {
        return bdeCategoriaFk;
    }

    public void setBdeCategoriaFk(SgAfCategoriaBienes bdeCategoriaFk) {
        this.bdeCategoriaFk = bdeCategoriaFk;
    }

    public Boolean getBdeBienEsFuenteSiap() {
        return bdeBienEsFuenteSiap;
    }

    public void setBdeBienEsFuenteSiap(Boolean bdeBienEsFuenteSiap) {
        this.bdeBienEsFuenteSiap = bdeBienEsFuenteSiap;
    }

    public Boolean getBdeEsLoteSiap() {
        return bdeEsLoteSiap;
    }

    public void setBdeEsLoteSiap(Boolean bdeEsLoteSiap) {
        this.bdeEsLoteSiap = bdeEsLoteSiap;
    }

    public Integer getBdeNumCorrelativoSiap() {
        return bdeNumCorrelativoSiap;
    }

    public void setBdeNumCorrelativoSiap(Integer bdeNumCorrelativoSiap) {
        this.bdeNumCorrelativoSiap = bdeNumCorrelativoSiap;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bdePk != null ? bdePk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAfBienDepreciable)) {
            return false;
        }
        SgAfBienDepreciable other = (SgAfBienDepreciable) object;
        if ((this.bdePk == null && other.bdePk != null) || (this.bdePk != null && !this.bdePk.equals(other.bdePk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgAfBienDepreciableLite{" + "bdePk=" + bdePk + '}';
    }
}
