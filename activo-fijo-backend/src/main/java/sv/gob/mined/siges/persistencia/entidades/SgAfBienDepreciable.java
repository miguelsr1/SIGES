/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import sv.gob.mined.siges.persistencia.entidades.centroseducativos.SgSede;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAfProyectos;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAfEstadosCalidad;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAfUnidadesAdministrativas;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAfTipoBienes;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAfEstadosBienes;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAfFormaAdquisicion;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sofis.security.DaoOperation;
import com.sofis.security.DataSecurity;
import com.sofis.security.OperationSecurity;
import com.sofis.sofisform.to.CriteriaTO;
import com.sofis.sofisform.to.MatchCriteriaTO;
import com.sofis.utils.CriteriaTOUtils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Basic;
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
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.SeguridadAmbitos;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAfCategoriaBienes;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAfEmpleados;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAfFuenteFinanciamiento;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_af_bienes_depreciables", uniqueConstraints = {
    @UniqueConstraint(name = "codigo_inventario_uk", columnNames = {"bde_codigo_inventario"})}, schema = Constantes.SCHEMA_ACTIVO_FIJO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "bdePk", scope = SgAfBienDepreciable.class)
public class SgAfBienDepreciable implements Serializable, DataSecurity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "bde_pk", nullable = false)
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

    @Size(max = 100)
    @Column(name = "bde_asignado_a", length = 100)
    private String bdeAsignadoA;
    
    @Size(max = 750)
    @Column(name = "bde_descripcion", length = 750)
    private String bdeDescripcion;
    
    @Size(max = 150)
    @Column(name = "bde_observacion", length = 150)
    private String bdeObservacion;
    
    @Size(max = 50)
    @Column(name = "bde_marca", length = 50)
    private String bdeMarca;
    
    @Size(max = 100)
    @Column(name = "bde_modelo", length = 100)
    private String bdeModelo;
    
    @Size(max = 100)
    @Column(name = "bde_no_serie", length = 100)
    private String bdeNoSerie;
    
    @Size(max = 50)
    @Column(name = "bde_no_placa", length = 50)
    private String bdeNoPlaca;
    
    @Size(max = 100)
    @Column(name = "bde_no_motor", length = 100)
    private String bdeNoMotor;
    
    @Column(name = "bde_anio")
    private Integer bdeAnio;
    
    @Size(max = 50)
    @Column(name = "bde_no_chasis", length = 50)
    private String bdeNoChasis;

    @Size(max = 50)
    @Column(name = "bde_no_vin", length = 50)
    private String bdeNoVin;
    
    @Size(max = 50)
    @Column(name = "bde_color_carroceria", length = 50)
    private String bdeColorCarroceria;
    
    @Size(max = 100)
    @Column(name = "bde_documento_adquisicion", length = 100)
    private String bdeDocumentoAdquisicion;
    
    @Column(name = "bde_fecha_adquisicion")
    private LocalDate bdeFechaAdquisicion;
    
    @Column(name = "bde_fecha_creacion")
    private LocalDateTime bdeFechaCreacion;
      
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
    
    @Column(name = "bde_bien_es_fuente_siap")
    private Boolean bdeBienEsFuenteSiap;
    
    @Column(name = "bde_es_lote_siap")
    private Boolean bdeEsLoteSiap;
    
    @Size(max = 100)
    @Column(name = "bde_proveedor", length = 100)
    private String bdeProveedor;
    
    @Size(max = 30)
    @Column(name = "bde_numero_partida", length = 30)
    private String bdeNumeroPartida;
    
    @Column(name = "bde_estado_proceso_lote")
    private Integer bdeEstadoProcesoLote;
    
    @Column(name = "bde_es_lote")
    private Boolean bdeEsLote;
    
    @Column(name = "bde_cantidad_lote")
    private Integer bdeCantidadLote;
    
    @Column(name = "bde_fecha_descargo")
    private LocalDateTime bdeFechaDescargo;
    
    @Column(name = "bde_fecha_reg_contable")
    private LocalDate bdeFechaRegContable;
    
    @Column(name = "bde_fecha_recepcion")
    private LocalDateTime bdeFechaRecepcion;
    
    @Column(name = "bde_valor_residual", length = 20)
    private BigDecimal bdeValorResidual;
    
    @Column(name = "bde_vida_util", length = 20)
    private Integer bdeVidaUtil;
    
    @Size(max = 150)
    @Column(name = "bde_observacion_dde", length = 150)
    private String bdeObservacionDde;

    @Column(name = "bde_inmueble_id")
    private Long bdeInmuebleId;
    
    @Column(name = "bde_completado")
    private Boolean bdeCompletado;
    
    @JoinColumn(name = "bde_empleado_fk", referencedColumnName = "emp_pk")
    @ManyToOne
    private SgAfEmpleados bdeEmpleadoFk;
     
    @JoinColumn(name = "bde_calidad_fk", referencedColumnName = "eca_pk")
    @ManyToOne(optional = false)
    private SgAfEstadosCalidad bdeEstadoCalidad;
    
    @JoinColumn(name = "bde_tipo_bien_fk", referencedColumnName = "tbi_pk")
    @ManyToOne(optional = false)
    private SgAfTipoBienes bdeTipoBien;
     
    @JoinColumn(name = "bde_categoria_fk", referencedColumnName = "cab_pk")
    @ManyToOne(optional = false)
    private SgAfCategoriaBienes bdeCategoriaFk;
            
    @JoinColumn(name = "bde_forma_adquisicion_fk", referencedColumnName = "fad_pk")
    @ManyToOne
    private SgAfFormaAdquisicion bdeFormaAdquisicion;
     
    @JoinColumn(name = "bde_fuente_financiamiento_fk", referencedColumnName = "ffi_pk")
    @ManyToOne
    private SgAfFuenteFinanciamiento bdeFuenteFinanciamiento;
      
    @JoinColumn(name = "bde_proyecto_fk", referencedColumnName = "pro_pk")
    @ManyToOne
    private SgAfProyectos bdeProyectos;
    
    @JoinColumn(name = "bde_estado_fk", referencedColumnName = "ebi_pk")
    @ManyToOne(optional = false)
    private SgAfEstadosBienes bdeEstadosBienes;

    @JoinColumn(name = "bde_estado_solicitud_fk", referencedColumnName = "ebi_pk")
    @ManyToOne
    private SgAfEstadosBienes bdeEstadosSolicitud;
    
    @JoinColumn(name = "bde_sede_fk", referencedColumnName = "sed_pk")
    @ManyToOne
    private SgSede bdeSede;

    @JoinColumn(name = "bde_unidad_administrativa_fk", referencedColumnName = "uad_pk")
    @ManyToOne
    private SgAfUnidadesAdministrativas bdeUnidadesAdministrativas;
    
    
    @Column(name = "bde_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime bdeUltModFecha;

    @Size(max = 45)
    @Column(name = "bde_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String bdeUltModUsuario;

    /**
     * Guarda el ID del lote
     */
    @Column(name = "bde_lote_id")
    private Long bdeLoteId;
    
    @Column(name = "bde_version")
    @Version
    private Integer bdeVersion;
    
    private transient BigDecimal bdeMontoDepreciado;
    
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

    public String getBdeAsignadoA() {
        return bdeAsignadoA;
    }

    public void setBdeAsignadoA(String bdeAsignadoA) {
        this.bdeAsignadoA = bdeAsignadoA;
    }

    public String getBdeDescripcion() {
        return bdeDescripcion;
    }

    public void setBdeDescripcion(String bdeDescripcion) {
        this.bdeDescripcion = bdeDescripcion;
    }

    public String getBdeObservacion() {
        return bdeObservacion;
    }

    public void setBdeObservacion(String bdeObservacion) {
        this.bdeObservacion = bdeObservacion;
    }

    public String getBdeMarca() {
        return bdeMarca;
    }

    public void setBdeMarca(String bdeMarca) {
        this.bdeMarca = bdeMarca;
    }

    public String getBdeModelo() {
        return bdeModelo;
    }

    public void setBdeModelo(String bdeModelo) {
        this.bdeModelo = bdeModelo;
    }

    public String getBdeNoSerie() {
        return bdeNoSerie;
    }

    public void setBdeNoSerie(String bdeNoSerie) {
        this.bdeNoSerie = bdeNoSerie;
    }

    public String getBdeNoPlaca() {
        return bdeNoPlaca;
    }

    public void setBdeNoPlaca(String bdeNoPlaca) {
        this.bdeNoPlaca = bdeNoPlaca;
    }

    public String getBdeNoMotor() {
        return bdeNoMotor;
    }

    public void setBdeNoMotor(String bdeNoMotor) {
        this.bdeNoMotor = bdeNoMotor;
    }

    public Integer getBdeAnio() {
        return bdeAnio;
    }

    public void setBdeAnio(Integer bdeAnio) {
        this.bdeAnio = bdeAnio;
    }

    public String getBdeNoChasis() {
        return bdeNoChasis;
    }

    public void setBdeNoChasis(String bdeNoChasis) {
        this.bdeNoChasis = bdeNoChasis;
    }

    public String getBdeNoVin() {
        return bdeNoVin;
    }

    public void setBdeNoVin(String bdeNoVin) {
        this.bdeNoVin = bdeNoVin;
    }

    public String getBdeColorCarroceria() {
        return bdeColorCarroceria;
    }

    public void setBdeColorCarroceria(String bdeColorCarroceria) {
        this.bdeColorCarroceria = bdeColorCarroceria;
    }

    public String getBdeDocumentoAdquisicion() {
        return bdeDocumentoAdquisicion;
    }

    public void setBdeDocumentoAdquisicion(String bdeDocumentoAdquisicion) {
        this.bdeDocumentoAdquisicion = bdeDocumentoAdquisicion;
    }

    public LocalDate getBdeFechaAdquisicion() {
        return bdeFechaAdquisicion;
    }

    public void setBdeFechaAdquisicion(LocalDate bdeFechaAdquisicion) {
        this.bdeFechaAdquisicion = bdeFechaAdquisicion;
    }

    public LocalDateTime getBdeFechaCreacion() {
        return bdeFechaCreacion;
    }

    public void setBdeFechaCreacion(LocalDateTime bdeFechaCreacion) {
        this.bdeFechaCreacion = bdeFechaCreacion;
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

    public String getBdeProveedor() {
        return bdeProveedor;
    }

    public void setBdeProveedor(String bdeProveedor) {
        this.bdeProveedor = bdeProveedor;
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

    public SgAfEstadosCalidad getBdeEstadoCalidad() {
        return bdeEstadoCalidad;
    }

    public void setBdeEstadoCalidad(SgAfEstadosCalidad bdeEstadoCalidad) {
        this.bdeEstadoCalidad = bdeEstadoCalidad;
    }

    public SgAfTipoBienes getBdeTipoBien() {
        return bdeTipoBien;
    }

    public void setBdeTipoBien(SgAfTipoBienes bdeTipoBien) {
        this.bdeTipoBien = bdeTipoBien;
    }

    public SgAfFormaAdquisicion getBdeFormaAdquisicion() {
        return bdeFormaAdquisicion;
    }

    public void setBdeFormaAdquisicion(SgAfFormaAdquisicion bdeFormaAdquisicion) {
        this.bdeFormaAdquisicion = bdeFormaAdquisicion;
    }

    public SgAfFuenteFinanciamiento getBdeFuenteFinanciamiento() {
        return bdeFuenteFinanciamiento;
    }

    public void setBdeFuenteFinanciamiento(SgAfFuenteFinanciamiento bdeFuenteFinanciamiento) {
        this.bdeFuenteFinanciamiento = bdeFuenteFinanciamiento;
    }

    public SgAfProyectos getBdeProyectos() {
        return bdeProyectos;
    }

    public void setBdeProyectos(SgAfProyectos bdeProyectos) {
        this.bdeProyectos = bdeProyectos;
    }

    public SgAfEstadosBienes getBdeEstadosBienes() {
        return bdeEstadosBienes;
    }

    public void setBdeEstadosBienes(SgAfEstadosBienes bdeEstadosBienes) {
        this.bdeEstadosBienes = bdeEstadosBienes;
    }

    public LocalDateTime getBdeUltModFecha() {
        return bdeUltModFecha;
    }

    public void setBdeUltModFecha(LocalDateTime bdeUltModFecha) {
        this.bdeUltModFecha = bdeUltModFecha;
    }

    public String getBdeUltModUsuario() {
        return bdeUltModUsuario;
    }

    public void setBdeUltModUsuario(String bdeUltModUsuario) {
        this.bdeUltModUsuario = bdeUltModUsuario;
    }

    public Integer getBdeVersion() {
        return bdeVersion;
    }

    public void setBdeVersion(Integer bdeVersion) {
        this.bdeVersion = bdeVersion;
    }

    public String getBdeNumeroPartida() {
        return bdeNumeroPartida;
    }

    public void setBdeNumeroPartida(String bdeNumeroPartida) {
        this.bdeNumeroPartida = bdeNumeroPartida;
    }

    public Integer getBdeEstadoProcesoLote() {
        return bdeEstadoProcesoLote;
    }

    public void setBdeEstadoProcesoLote(Integer bdeEstadoProcesoLote) {
        this.bdeEstadoProcesoLote = bdeEstadoProcesoLote;
    }

    public SgSede getBdeSede() {
        return bdeSede;
    }

    public void setBdeSede(SgSede bdeSede) {
        this.bdeSede = bdeSede;
    }

    public SgAfUnidadesAdministrativas getBdeUnidadesAdministrativas() {
        return bdeUnidadesAdministrativas;
    }

    public void setBdeUnidadesAdministrativas(SgAfUnidadesAdministrativas bdeUnidadesAdministrativas) {
        this.bdeUnidadesAdministrativas = bdeUnidadesAdministrativas;
    }

    public Integer getBdeNumCorrelativo() {
        return bdeNumCorrelativo;
    }

    public void setBdeNumCorrelativo(Integer bdeNumCorrelativo) {
        this.bdeNumCorrelativo = bdeNumCorrelativo;
    }

    public LocalDateTime getBdeFechaDescargo() {
        return bdeFechaDescargo;
    }

    public void setBdeFechaDescargo(LocalDateTime bdeFechaDescargo) {
        this.bdeFechaDescargo = bdeFechaDescargo;
    }

    public LocalDate getBdeFechaRegContable() {
        return bdeFechaRegContable;
    }

    public void setBdeFechaRegContable(LocalDate bdeFechaRegContable) {
        this.bdeFechaRegContable = bdeFechaRegContable;
    }

    public LocalDateTime getBdeFechaRecepcion() {
        return bdeFechaRecepcion;
    }

    public void setBdeFechaRecepcion(LocalDateTime bdeFechaRecepcion) {
        this.bdeFechaRecepcion = bdeFechaRecepcion;
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

    public String getBdeObservacionDde() {
        return bdeObservacionDde;
    }

    public void setBdeObservacionDde(String bdeObservacionDde) {
        this.bdeObservacionDde = bdeObservacionDde;
    }

    public SgAfEmpleados getBdeEmpleadoFk() {
        return bdeEmpleadoFk;
    }

    public void setBdeEmpleadoFk(SgAfEmpleados bdeEmpleadoFk) {
        this.bdeEmpleadoFk = bdeEmpleadoFk;
    }

    public SgAfEstadosBienes getBdeEstadosSolicitud() {
        return bdeEstadosSolicitud;
    }

    public void setBdeEstadosSolicitud(SgAfEstadosBienes bdeEstadosSolicitud) {
        this.bdeEstadosSolicitud = bdeEstadosSolicitud;
    }

    public Boolean getBdeDepreciado() {
        return bdeDepreciado;
    }

    public void setBdeDepreciado(Boolean bdeDepreciado) {
        this.bdeDepreciado = bdeDepreciado;
    }

    public SgAfCategoriaBienes getBdeCategoriaFk() {
        return bdeCategoriaFk;
    }

    public void setBdeCategoriaFk(SgAfCategoriaBienes bdeCategoriaFk) {
        this.bdeCategoriaFk = bdeCategoriaFk;
    }

    public Boolean getBdeTipoBienCategoriaVinculada() {
        return bdeTipoBienCategoriaVinculada;
    }

    public void setBdeTipoBienCategoriaVinculada(Boolean bdeTipoBienCategoriaVinculada) {
        this.bdeTipoBienCategoriaVinculada = bdeTipoBienCategoriaVinculada;
    }

    public Long getBdeInmuebleId() {
        return bdeInmuebleId;
    }

    public void setBdeInmuebleId(Long bdeInmuebleId) {
        this.bdeInmuebleId = bdeInmuebleId;
    }

    public Boolean getBdeCompletado() {
        return bdeCompletado;
    }

    public void setBdeCompletado(Boolean bdeCompletado) {
        this.bdeCompletado = bdeCompletado;
    }

    public Boolean getBdeDepreciadoCompleto() {
        return bdeDepreciadoCompleto;
    }

    public void setBdeDepreciadoCompleto(Boolean bdeDepreciadoCompleto) {
        this.bdeDepreciadoCompleto = bdeDepreciadoCompleto;
    }

    public Long getBdeLoteId() {
        return bdeLoteId;
    }

    public void setBdeLoteId(Long bdeLoteId) {
        this.bdeLoteId = bdeLoteId;
    }

    public BigDecimal getBdeMontoDepreciado() {
        return bdeMontoDepreciado;
    }

    public void setBdeMontoDepreciado(BigDecimal bdeMontoDepreciado) {
        this.bdeMontoDepreciado = bdeMontoDepreciado;
    }

    public Integer getBdeNumCorrelativoSiap() {
        return bdeNumCorrelativoSiap;
    }

    public void setBdeNumCorrelativoSiap(Integer bdeNumCorrelativoSiap) {
        this.bdeNumCorrelativoSiap = bdeNumCorrelativoSiap;
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
    
    @Override
    public String securityAmbitCreate() {
        if(bdeSede != null) {
            return "bdeSede";
        } else if(bdeUnidadesAdministrativas != null) {
            return "bdeUnidadesAdministrativas";
        } else {
            return null;
        }
    } 
    
    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation daoop) {
        if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.DEPARTAMENTAL.name())) {
            return CriteriaTOUtils.createORTO(
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "bdeSede.sedDireccion.dirDepartamento.depPk", o.getContext()),
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "bdeUnidadesAdministrativas.uadUnidadActivoFijoFk.uafDepartamento.depPk", o.getContext())
            );
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SEDE.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "bdeSede.sedPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.UNIDAD_ADMINISTRATIVA.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "bdeUnidadesAdministrativas.uadPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.MINED.name())) {
            return null;
        } else {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "bdePk", -1L);
        }
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
        return "sv.gob.mined.siges.persistencia.entidades.SgAfBienesDepreciables{" + "bdePk=" + bdePk + '}';
    }
}
