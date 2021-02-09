/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.enumerados.EnumEstadosProceso;
import sv.gob.mined.siges.web.enumerados.TipoUnidad;


public class FiltroBienesDepreciables implements Serializable {
    
    private Long id;
    private TipoUnidad tipoUnidad;
    private TipoUnidad tipoUnidadDestino;
    private Long unidadActivoFijoId;
    private Long municipioId;
    private Long unidadAdministrativaId;
    private Integer periodo;
    private Long notificacionId;
    private String responsable;
    private Long unidadActivoFijoDestinoId;
    private Long municipioDestinoId;
    private Long unidadAdministrativaDestinoId;
    private Long categoriaId;
    private Long tipoBienId;
    private Long calidadId;
    private Long estadoId;
    private Long tipoTraslado;
    private Long formaAdquisicionId;
    private Long departamentoId;
    private Long departamentoDestinoId;
    private Long empleadoId;
    private Boolean esUnidadAdministrativa;
    private List<Integer> estadoProcesoLote;
    private Long clasificacionId;
    private String codigoInventario;
    private Boolean activos;
    private Boolean soloVehiculos;
    private Boolean solicitudVencida;
    private Boolean validarEstadoSolicitudVacio;
    private Boolean validarEstadoSolicitud;
    private Boolean completado;
    private Boolean esBienSiap;
    private Long anio;
    private Long mes;
    private EnumEstadosProceso estado;
    private String estadoCodigo;
    private Long fuenteId;
    private Long proyectoId;
    private String asignadoA;
    private String marca;
    private String modelo;
    private String noSerie;
    
    private String motor;
    private String chasis;
    private String placa;
    private Boolean obtenerDepreciacion = false;
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
    private Boolean trasladado;
    private Boolean cargarCosto;
    private Boolean cargarTotalBienes;
    
    private String dui;
    private String nombresEmpleado;
    
    private String usuarioModificacion;
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;
    private Boolean obtenerCantidad;
    private String securityOperation;

    public FiltroBienesDepreciables() {
        securityOperation = ConstantesOperaciones.BUSCAR_BIENES_DEPRECIABLES;
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

    public EnumEstadosProceso getEstado() {
        return estado;
    }

    public void setEstado(EnumEstadosProceso estado) {
        this.estado = estado;
    }

    public Long getTipoTraslado() {
        return tipoTraslado;
    }

    public void setTipoTraslado(Long tipoTraslado) {
        this.tipoTraslado = tipoTraslado;
    }

    public String getEstadoCodigo() {
        return estadoCodigo;
    }

    public void setEstadoCodigo(String estadoCodigo) {
        this.estadoCodigo = estadoCodigo;
    }

    public Long getEmpleadoId() {
        return empleadoId;
    }

    public void setEmpleadoId(Long empleadoId) {
        this.empleadoId = empleadoId;
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

    public void setFechaDescargoHasta(LocalDate fechaDescargoHasta) {
        this.fechaDescargoHasta = fechaDescargoHasta;
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

    public Boolean getSoloVehiculos() {
        return soloVehiculos;
    }

    public void setSoloVehiculos(Boolean soloVehiculos) {
        this.soloVehiculos = soloVehiculos;
    }

    public String getSecurityOperation() {
        return securityOperation;
    }

    public void setSecurityOperation(String securityOperation) {
        this.securityOperation = securityOperation;
    } 

    public Long getClasificacionId() {
        return clasificacionId;
    }

    public void setClasificacionId(Long clasificacionId) {
        this.clasificacionId = clasificacionId;
    }

    public Boolean getCompletado() {
        return completado;
    }

    public void setCompletado(Boolean completado) {
        this.completado = completado;
    }

    public Integer getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Integer periodo) {
        this.periodo = periodo;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public Long getNotificacionId() {
        return notificacionId;
    }

    public void setNotificacionId(Long notificacionId) {
        this.notificacionId = notificacionId;
    }

    public Boolean getSolicitudVencida() {
        return solicitudVencida;
    }

    public void setSolicitudVencida(Boolean solicitudVencida) {
        this.solicitudVencida = solicitudVencida;
    }

    public Boolean getTrasladado() {
        return trasladado;
    }

    public void setTrasladado(Boolean trasladado) {
        this.trasladado = trasladado;
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