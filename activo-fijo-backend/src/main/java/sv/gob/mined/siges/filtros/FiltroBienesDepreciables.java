/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.enumerados.EnumEstadosProceso;
import sv.gob.mined.siges.enumerados.TipoUnidad;


public class FiltroBienesDepreciables implements Serializable {
    
    private Long id;
    private Long idInmueble;
    private Long depreciacionMaestroId;
    private TipoUnidad tipoUnidad;
    private TipoUnidad tipoUnidadDestino;
    private Long unidadActivoFijoId;
    private Long municipioId;
    private Boolean esBienSiap;
    private Long unidadAdministrativaId;
    private Long unidadActivoFijoDestinoId;
    private Long municipioDestinoId;
    private Long unidadAdministrativaDestinoId;
    private Long categoriaId;
    private Long clasificacionId;
    private Long tipoBienId;
    private Long empleadoId;
    private Long calidadId;
    private Long estadoId;
    private Long tipoTraslado;
    private Long formaAdquisicionId;
    private Long departamentoId;
    private Long departamentoDestinoId;
    private Boolean esUnidadAdministrativa;
    private List<Integer> estadoProcesoLote;
    private Boolean soloVehiculos;
    private String codigoInventario;
    private Boolean activos;
    private Boolean validarEstadoSolicitudVacio;
    private Boolean validarEstadoSolicitud;
    private Boolean completado; 
    private Boolean depreciadoCompleto; 
    private Long fuenteId;
    private Long proyectoId;
    private Long anio;
    private Boolean solicitudVencida;
    private Long mes;
    private EnumEstadosProceso estado;
    private String estadoCodigo;
    private Boolean diferenteEstado;
    private String motor;
    private String chasis;
    private String placa;        
    private Boolean menorIgualMes;
    private Boolean menorIgualAnio;
    private Boolean mayorIgualMes;
    private Boolean mayorIgualAnio;
    private LocalDate fechaCalculoDepreciacionDesde;
    private LocalDate fechaCalculoDepreciacionHasta;
    private Long idBien;
    private String asignadoA;
    private String marca;
    private String modelo;
    private String noSerie;
    private String codigoDescargo;
    private String codigoTraslado;
    private BigDecimal valorAdquisicionDesde;
    private BigDecimal valorAdquisicionHasta;
    private LocalDate fechaAdquisicionDesde;
    private LocalDate fechaAdquisicionHasta;
    private LocalDate fechaCreacionDesde;
    private LocalDate fechaCreacionHasta;
    private LocalDate fechaModificacionDesde;
    private LocalDate fechaModificacionHasta;
    
    private LocalDate fechaSolicitudDesde;
    private LocalDate fechaSolicitudHasta;
    private LocalDate fechaDescargoDesde;
    private LocalDate fechaDescargoHasta;
    private LocalDate fechaTrasladoDesde;
    private LocalDate fechaTrasladoHasta;
    
    private String operacion;
    
    private List<EnumEstadosProceso> estados;
    private Boolean trasladado;
    private Boolean cargarCosto;
    private Boolean cargarTotalBienes;
    private Boolean paraDepreciar;
    private String usuarioModificacion;
    
    private Boolean obtenerDepreciacion = false;
    private String dui;
    private String nombresEmpleado;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;
    
    private String securityOperation;
    private Boolean obtenerCantidad;
    //Auxiliar
    private Boolean seNecesitaDistinct;


    public FiltroBienesDepreciables() {
        securityOperation = ConstantesOperaciones.BUSCAR_BIENES_DEPRECIABLES;
        seNecesitaDistinct = Boolean.FALSE;
    }

    public TipoUnidad getTipoUnidad() {
        return tipoUnidad;
    }

    public void setTipoUnidad(TipoUnidad tipoUnidad) {
        this.tipoUnidad = tipoUnidad;
    }
    

    public Long getUnidadActivoFijoId() {
        return unidadActivoFijoId;
    }

    public void setUnidadActivoFijoId(Long unidadActivoFijoId) {
        this.unidadActivoFijoId = unidadActivoFijoId;
    }

    public Long getMunicipioId() {
        return municipioId;
    }

    public void setMunicipioId(Long municipioId) {
        this.municipioId = municipioId;
    }

    public Long getUnidadAdministrativaId() {
        return unidadAdministrativaId;
    }

    public void setUnidadAdministrativaId(Long unidadAdministrativaId) {
        this.unidadAdministrativaId = unidadAdministrativaId;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }

    public Long getTipoBienId() {
        return tipoBienId;
    }

    public void setTipoBienId(Long tipoBienId) {
        this.tipoBienId = tipoBienId;
    }

    public Long getCalidadId() {
        return calidadId;
    }

    public void setCalidadId(Long calidadId) {
        this.calidadId = calidadId;
    }

    public Long getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(Long estadoId) {
        this.estadoId = estadoId;
    }

    public String getEstadoCodigo() {
        return estadoCodigo;
    }

    public void setEstadoCodigo(String estadoCodigo) {
        this.estadoCodigo = estadoCodigo;
    }
    public Long getFormaAdquisicionId() {
        return formaAdquisicionId;
    }

    public void setFormaAdquisicionId(Long formaAdquisicionId) {
        this.formaAdquisicionId = formaAdquisicionId;
    }

    public String getCodigoInventario() {
        return codigoInventario;
    }

    public void setCodigoInventario(String codigoInventario) {
        this.codigoInventario = codigoInventario;
    }

    public Boolean getActivos() {
        return activos;
    }

    public void setActivos(Boolean activos) {
        this.activos = activos;
    }

    public Long getFuenteId() {
        return fuenteId;
    }

    public void setFuenteId(Long fuenteId) {
        this.fuenteId = fuenteId;
    }

    public Long getProyectoId() {
        return proyectoId;
    }

    public void setProyectoId(Long proyectoId) {
        this.proyectoId = proyectoId;
    }

    public String getAsignadoA() {
        return asignadoA;
    }

    public void setAsignadoA(String asignadoA) {
        this.asignadoA = asignadoA;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getNoSerie() {
        return noSerie;
    }

    public void setNoSerie(String noSerie) {
        this.noSerie = noSerie;
    }

    public BigDecimal getValorAdquisicionDesde() {
        return valorAdquisicionDesde;
    }

    public void setValorAdquisicionDesde(BigDecimal valorAdquisicionDesde) {
        this.valorAdquisicionDesde = valorAdquisicionDesde;
    }

    public BigDecimal getValorAdquisicionHasta() {
        return valorAdquisicionHasta;
    }

    public void setValorAdquisicionHasta(BigDecimal valorAdquisicionHasta) {
        this.valorAdquisicionHasta = valorAdquisicionHasta;
    }

    public LocalDate getFechaAdquisicionDesde() {
        return fechaAdquisicionDesde;
    }

    public void setFechaAdquisicionDesde(LocalDate fechaAdquisicionDesde) {
        this.fechaAdquisicionDesde = fechaAdquisicionDesde;
    }

    public LocalDate getFechaAdquisicionHasta() {
        return fechaAdquisicionHasta;
    }

    public void setFechaAdquisicionHasta(LocalDate fechaAdquisicionHasta) {
        this.fechaAdquisicionHasta = fechaAdquisicionHasta;
    }

    public LocalDate getFechaCreacionDesde() {
        return fechaCreacionDesde;
    }

    public void setFechaCreacionDesde(LocalDate fechaCreacionDesde) {
        this.fechaCreacionDesde = fechaCreacionDesde;
    }

    public LocalDate getFechaCreacionHasta() {
        return fechaCreacionHasta;
    }

    public void setFechaCreacionHasta(LocalDate fechaCreacionHasta) {
        this.fechaCreacionHasta = fechaCreacionHasta;
    }

    public LocalDate getFechaModificacionHasta() {
        return fechaModificacionHasta;
    }

    public void setFechaModificacionHasta(LocalDate fechaModificacionHasta) {
        this.fechaModificacionHasta = fechaModificacionHasta;
    }

    public LocalDate getFechaModificacionDesde() {
        return fechaModificacionDesde;
    }

    public void setFechaModificacionDesde(LocalDate fechaModificacionDesde) {
        this.fechaModificacionDesde = fechaModificacionDesde;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public Long getFirst() {
        return first;
    }

    public void setFirst(Long first) {
        this.first = first;
    }

    public Long getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(Long maxResults) {
        this.maxResults = maxResults;
    }

    public String[] getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String[] orderBy) {
        this.orderBy = orderBy;
    }

    public boolean[] getAscending() {
        return ascending;
    }

    public void setAscending(boolean[] ascending) {
        this.ascending = ascending;
    }

    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
    }

    public Boolean getEsUnidadAdministrativa() {
        return esUnidadAdministrativa;
    }

    public void setEsUnidadAdministrativa(Boolean esUnidadAdministrativa) {
        this.esUnidadAdministrativa = esUnidadAdministrativa;
    }

    public Long getDepartamentoId() {
        return departamentoId;
    }

    public void setDepartamentoId(Long departamentoId) {
        this.departamentoId = departamentoId;
    }

    public List<Integer> getEstadoProcesoLote() {
        return estadoProcesoLote;
    }

    public void setEstadoProcesoLote(List<Integer> estadoProcesoLote) {
        this.estadoProcesoLote = estadoProcesoLote;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaSolicitudDesde() {
        return fechaSolicitudDesde;
    }

    public void setFechaSolicitudDesde(LocalDate fechaSolicitudDesde) {
        this.fechaSolicitudDesde = fechaSolicitudDesde;
    }

    public LocalDate getFechaSolicitudHasta() {
        return fechaSolicitudHasta;
    }

    public void setFechaSolicitudHasta(LocalDate fechaSolicitudHasta) {
        this.fechaSolicitudHasta = fechaSolicitudHasta;
    }

    public Boolean getCargarCosto() {
        return cargarCosto;
    }

    public void setCargarCosto(Boolean cargarCosto) {
        this.cargarCosto = cargarCosto;
    }

    public Boolean getCargarTotalBienes() {
        return cargarTotalBienes;
    }

    public void setCargarTotalBienes(Boolean cargarTotalBienes) {
        this.cargarTotalBienes = cargarTotalBienes;
    }

    public String getCodigoDescargo() {
        return codigoDescargo;
    }

    public void setCodigoDescargo(String codigoDescargo) {
        this.codigoDescargo = codigoDescargo;
    }

    public String getCodigoTraslado() {
        return codigoTraslado;
    }

    public void setCodigoTraslado(String codigoTraslado) {
        this.codigoTraslado = codigoTraslado;
    }

    public TipoUnidad getTipoUnidadDestino() {
        return tipoUnidadDestino;
    }

    public void setTipoUnidadDestino(TipoUnidad tipoUnidadDestino) {
        this.tipoUnidadDestino = tipoUnidadDestino;
    }

    public Long getUnidadActivoFijoDestinoId() {
        return unidadActivoFijoDestinoId;
    }

    public void setUnidadActivoFijoDestinoId(Long unidadActivoFijoDestinoId) {
        this.unidadActivoFijoDestinoId = unidadActivoFijoDestinoId;
    }

    public Long getMunicipioDestinoId() {
        return municipioDestinoId;
    }

    public void setMunicipioDestinoId(Long municipioDestinoId) {
        this.municipioDestinoId = municipioDestinoId;
    }

    public Long getUnidadAdministrativaDestinoId() {
        return unidadAdministrativaDestinoId;
    }

    public void setUnidadAdministrativaDestinoId(Long unidadAdministrativaDestinoId) {
        this.unidadAdministrativaDestinoId = unidadAdministrativaDestinoId;
    }

    public LocalDate getFechaTrasladoDesde() {
        return fechaTrasladoDesde;
    }

    public void setFechaTrasladoDesde(LocalDate fechaTrasladoDesde) {
        this.fechaTrasladoDesde = fechaTrasladoDesde;
    }

    public LocalDate getFechaTrasladoHasta() {
        return fechaTrasladoHasta;
    }

    public void setFechaTrasladoHasta(LocalDate fechaTrasladoHasta) {
        this.fechaTrasladoHasta = fechaTrasladoHasta;
    }

    public Long getDepartamentoDestinoId() {
        return departamentoDestinoId;
    }

    public void setDepartamentoDestinoId(Long departamentoDestinoId) {
        this.departamentoDestinoId = departamentoDestinoId;
    }

    public Boolean getValidarEstadoSolicitudVacio() {
        return validarEstadoSolicitudVacio;
    }

    public void setValidarEstadoSolicitudVacio(Boolean validarEstadoSolicitudVacio) {
        this.validarEstadoSolicitudVacio = validarEstadoSolicitudVacio;
    }

    public Boolean getValidarEstadoSolicitud() {
        return validarEstadoSolicitud;
    }

    public void setValidarEstadoSolicitud(Boolean validarEstadoSolicitud) {
        this.validarEstadoSolicitud = validarEstadoSolicitud;
    }

    public Long getAnio() {
        return anio;
    }

    public void setAnio(Long anio) {
        this.anio = anio;
    }

    public Long getMes() {
        return mes;
    }

    public void setMes(Long mes) {
        this.mes = mes;
    }

    public LocalDate getFechaCalculoDepreciacionDesde() {
        return fechaCalculoDepreciacionDesde;
    }

    public void setFechaCalculoDepreciacionDesde(LocalDate fechaCalculoDepreciacionDesde) {
        this.fechaCalculoDepreciacionDesde = fechaCalculoDepreciacionDesde;
    }

    public LocalDate getFechaCalculoDepreciacionHasta() {
        return fechaCalculoDepreciacionHasta;
    }

    public void setFechaCalculoDepreciacionHasta(LocalDate fechaCalculoDepreciacionHasta) {
        this.fechaCalculoDepreciacionHasta = fechaCalculoDepreciacionHasta;
    }

    public Long getIdBien() {
        return idBien;
    }

    public void setIdBien(Long idBien) {
        this.idBien = idBien;
    }

    public Boolean getMenorIgualMes() {
        return menorIgualMes;
    }

    public void setMenorIgualMes(Boolean menorIgualMes) {
        this.menorIgualMes = menorIgualMes;
    }

    public Boolean getMenorIgualAnio() {
        return menorIgualAnio;
    }

    public void setMenorIgualAnio(Boolean menorIgualAnio) {
        this.menorIgualAnio = menorIgualAnio;
    }

    public Boolean getMayorIgualMes() {
        return mayorIgualMes;
    }

    public void setMayorIgualMes(Boolean mayorIgualMes) {
        this.mayorIgualMes = mayorIgualMes;
    }

    public Boolean getMayorIgualAnio() {
        return mayorIgualAnio;
    }

    public void setMayorIgualAnio(Boolean mayorIgualAnio) {
        this.mayorIgualAnio = mayorIgualAnio;
    }

    public EnumEstadosProceso getEstado() {
        return estado;
    }

    public void setEstado(EnumEstadosProceso estado) {
        this.estado = estado;
    }

    public Boolean getDiferenteEstado() {
        return diferenteEstado;
    }

    public void setDiferenteEstado(Boolean diferenteEstado) {
        this.diferenteEstado = diferenteEstado;
    }

    public Long getDepreciacionMaestroId() {
        return depreciacionMaestroId;
    }

    public void setDepreciacionMaestroId(Long depreciacionMaestroId) {
        this.depreciacionMaestroId = depreciacionMaestroId;
    }

    public Long getTipoTraslado() {
        return tipoTraslado;
    }

    public void setTipoTraslado(Long tipoTraslado) {
        this.tipoTraslado = tipoTraslado;
    }

    public Long getEmpleadoId() {
        return empleadoId;
    }

    public void setEmpleadoId(Long empleadoId) {
        this.empleadoId = empleadoId;
    }

    public Boolean getSoloVehiculos() {
        return soloVehiculos;
    }

    public void setSoloVehiculos(Boolean soloVehiculos) {
        this.soloVehiculos = soloVehiculos;
    }

    public Long getClasificacionId() {
        return clasificacionId;
    }

    public void setClasificacionId(Long clasificacionId) {
        this.clasificacionId = clasificacionId;
    }

    public String getSecurityOperation() {
        return securityOperation;
    }

    public void setSecurityOperation(String securityOperation) {
        this.securityOperation = securityOperation;
    }

    public Boolean getSeNecesitaDistinct() {
        return seNecesitaDistinct;
    }

    public void setSeNecesitaDistinct(Boolean seNecesitaDistinct) {
        this.seNecesitaDistinct = seNecesitaDistinct;
    }

    public String getMotor() {
        return motor;
    }

    public void setMotor(String motor) {
        this.motor = motor;
    }

    public String getChasis() {
        return chasis;
    }

    public void setChasis(String chasis) {
        this.chasis = chasis;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public Long getIdInmueble() {
        return idInmueble;
    }

    public void setIdInmueble(Long idInmueble) {
        this.idInmueble = idInmueble;
    }

    public Boolean getCompletado() {
        return completado;
    }

    public void setCompletado(Boolean completado) {
        this.completado = completado;
    }

    public LocalDate getFechaDescargoDesde() {
        return fechaDescargoDesde;
    }

    public void setFechaDescargoDesde(LocalDate fechaDescargoDesde) {
        this.fechaDescargoDesde = fechaDescargoDesde;
    }

    public LocalDate getFechaDescargoHasta() {
        return fechaDescargoHasta;
    }

    public Boolean getTrasladado() {
        return trasladado;
    }

    public void setTrasladado(Boolean trasladado) {
        this.trasladado = trasladado;
    }

    public void setFechaDescargoHasta(LocalDate fechaDescargoHasta) {
        this.fechaDescargoHasta = fechaDescargoHasta;
    }

    public Boolean getSolicitudVencida() {
        return solicitudVencida;
    }

    public void setSolicitudVencida(Boolean solicitudVencida) {
        this.solicitudVencida = solicitudVencida;
    }

    public Boolean getDepreciadoCompleto() {
        return depreciadoCompleto;
    }

    public void setDepreciadoCompleto(Boolean depreciadoCompleto) {
        this.depreciadoCompleto = depreciadoCompleto;
    }

    public List<EnumEstadosProceso> getEstados() {
        return estados;
    }

    public void setEstados(List<EnumEstadosProceso> estados) {
        this.estados = estados;
    }

    public Boolean getParaDepreciar() {
        return paraDepreciar;
    }

    public void setParaDepreciar(Boolean paraDepreciar) {
        this.paraDepreciar = paraDepreciar;
    }

    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    public Boolean getObtenerCantidad() {
        return obtenerCantidad;
    }

    public void setObtenerCantidad(Boolean obtenerCantidad) {
        this.obtenerCantidad = obtenerCantidad;
    }

    public String getDui() {
        return dui;
    }

    public void setDui(String dui) {
        this.dui = dui;
    }

    public String getNombresEmpleado() {
        return nombresEmpleado;
    }

    public void setNombresEmpleado(String nombresEmpleado) {
        this.nombresEmpleado = nombresEmpleado;
    }

    public Boolean getObtenerDepreciacion() {
        return obtenerDepreciacion;
    }

    public void setObtenerDepreciacion(Boolean obtenerDepreciacion) {
        this.obtenerDepreciacion = obtenerDepreciacion;
    }

    public Boolean getEsBienSiap() {
        return esBienSiap;
    }

    public void setEsBienSiap(Boolean esBienSiap) {
        this.esBienSiap = esBienSiap;
    }
    
}
