/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import sv.gob.mined.siges.web.dto.centroseducativos.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgAfFormaAdquisicion;
import sv.gob.mined.siges.web.dto.catalogo.SgAfProyectos;
import sv.gob.mined.siges.web.dto.catalogo.SgAfUnidadesAdministrativas;
import sv.gob.mined.siges.web.dto.catalogo.SgAfEstadosBienes;
import sv.gob.mined.siges.web.dto.catalogo.SgAfEstadosCalidad;
import sv.gob.mined.siges.web.dto.catalogo.SgAfTipoBienes;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import sv.gob.mined.siges.web.dto.catalogo.SgAfCategoriaBienes;
import sv.gob.mined.siges.web.dto.catalogo.SgAfEmpleados;
import sv.gob.mined.siges.web.dto.catalogo.SgAfFuenteFinanciamiento;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class,property = "bdePk", scope = SgAfBienDepreciable.class)
public class SgAfBienDepreciable implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long bdePk;
    private String bdeCodigoCorrelativo;
    private Integer bdeNumCorrelativo;
    private Integer bdeNumCorrelativoSiap;
    private String bdeCodigoInventario;
    private String bdeAsignadoA;
    private String bdeDescripcion;
    private String bdeObservacion;
    private String bdeMarca;
    private String bdeModelo;
    private String bdeNoSerie;
    private String bdeNoPlaca;
    private String bdeNoMotor;
    private Integer bdeAnio;
    private String bdeNoChasis;
    private String bdeNoVin;
    private String bdeColorCarroceria;
    private String bdeDocumentoAdquisicion;
    private LocalDate bdeFechaAdquisicion;
    private LocalDateTime bdeFechaCreacion;
    private BigDecimal bdeValorAdquisicion;
    private BigDecimal bdeMontoDepreciado;
    private Boolean bdeEsValorEstimado;
    private Boolean bdeDepreciadoCompleto;
    private Boolean bdeDepreciado;
    private String bdeProveedor;
    private String bdeNumeroPartida;
    private Integer bdeEstadoProcesoLote;
    private Boolean bdeEsLote;
    private Integer bdeCantidadLote;
    private Boolean bdeBienEsFuenteSiap;
    private Boolean bdeEsLoteSiap;
    private Long bdeInmuebleId;
    private Boolean bdeCompletado;
    
    private SgAfEstadosCalidad bdeEstadoCalidad;
    private SgAfTipoBienes bdeTipoBien;
    private Boolean bdeTipoBienCategoriaVinculada;
    private SgAfCategoriaBienes bdeCategoriaFk;
    private SgAfFormaAdquisicion bdeFormaAdquisicion;
    private SgAfFuenteFinanciamiento bdeFuenteFinanciamiento;
    private SgAfProyectos bdeProyectos;
    private SgAfEstadosBienes bdeEstadosBienes;
    private SgAfEstadosBienes bdeEstadosSolicitud;
    private SgSede bdeSede;
    private SgAfUnidadesAdministrativas bdeUnidadesAdministrativas;
    private LocalDateTime bdeUltModFecha;
    private String bdeUltModUsuario;
    private Integer bdeVersion;
    private LocalDateTime bdeFechaDescargo;
    private LocalDate bdeFechaRegContable;
    private LocalDateTime bdeFechaRecepcion;
    private BigDecimal bdeValorResidual;
    private Integer bdeVidaUtil;
    private String bdeObservacionDde;
    private SgAfEmpleados bdeEmpleadoFk;
  
    private String bdeCodigoCorrelativoGenerar;
    private Integer bdeNumCorrelativoGenerar;
    private String bdeCodigoInventarioGenerar;
    
    
    public SgAfBienDepreciable() {
        bdeEsLote = Boolean.FALSE;
        bdeDepreciado = Boolean.FALSE;
        bdeDepreciadoCompleto = Boolean.FALSE;
        bdeTipoBienCategoriaVinculada = Boolean.TRUE;
        bdeMontoDepreciado = new BigDecimal(BigInteger.ZERO);
        bdeValorAdquisicion = new BigDecimal(BigInteger.ZERO);
    }

    @JsonIgnore
    public String getBdeCodigoNombreUnidadAdministrativa() {
        if(bdeUnidadesAdministrativas != null) {
            return bdeUnidadesAdministrativas.getCodigoNombre();
        } else if(bdeSede != null) {
            return  bdeSede.getSedCodigoNombre();
        }
        return "";
    }
    @JsonIgnore
    public String getBdeCodigoNombreUnidadActivoFijo() {
        if(bdeUnidadesAdministrativas != null && bdeUnidadesAdministrativas.getUadUnidadActivoFijoFk() != null) {
            return bdeUnidadesAdministrativas.getUadUnidadActivoFijoFk().getCodigoNombre();
        } else if(bdeSede != null) {
            return  bdeSede.getSedCodigoNombre();
        }
        return "";
    }
    @JsonIgnore
    public String getBdeNombreCategoria() {
        if(bdeCategoriaFk != null) {
            return bdeCategoriaFk.getCabNombre();
        }
        return "";
    }
    
    @JsonIgnore
    public String getBdeBienAsignadoA() {
        if(bdeEmpleadoFk != null) {
            return bdeEmpleadoFk.getNombresApellidos();
        } else {
            return bdeAsignadoA;
        }
    }
    
    @JsonIgnore
    public BigDecimal getBdeValorActual() {
        if(bdeValorAdquisicion != null && bdeMontoDepreciado != null) {
            return bdeValorAdquisicion.subtract(bdeMontoDepreciado).setScale(2, RoundingMode.HALF_UP);
        } else {
            return bdeValorAdquisicion;
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

    public BigDecimal getBdeMontoDepreciado() {
        if(bdeMontoDepreciado != null) {
            return bdeMontoDepreciado.setScale(2, RoundingMode.HALF_UP);
        } else {
            return new BigDecimal(BigInteger.ZERO);
        }
    }

    public void setBdeMontoDepreciado(BigDecimal bdeMontoDepreciado) {
        this.bdeMontoDepreciado = bdeMontoDepreciado;
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

    public Integer getBdeNumCorrelativoGenerar() {
        return bdeNumCorrelativoGenerar;
    }

    public void setBdeNumCorrelativoGenerar(Integer bdeNumCorrelativoGenerar) {
        this.bdeNumCorrelativoGenerar = bdeNumCorrelativoGenerar;
    }

    public String getBdeCodigoCorrelativoGenerar() {
        return bdeCodigoCorrelativoGenerar;
    }

    public void setBdeCodigoCorrelativoGenerar(String bdeCodigoCorrelativoGenerar) {
        this.bdeCodigoCorrelativoGenerar = bdeCodigoCorrelativoGenerar;
    }

    public String getBdeCodigoInventarioGenerar() {
        return bdeCodigoInventarioGenerar;
    }

    public void setBdeCodigoInventarioGenerar(String bdeCodigoInventarioGenerar) {
        this.bdeCodigoInventarioGenerar = bdeCodigoInventarioGenerar;
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
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + Objects.hashCode(this.bdePk);
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
        final SgAfBienDepreciable other = (SgAfBienDepreciable) obj;
        if (!Objects.equals(this.bdePk, other.bdePk)) {
            return false;
        }
        return true;
    }
}
