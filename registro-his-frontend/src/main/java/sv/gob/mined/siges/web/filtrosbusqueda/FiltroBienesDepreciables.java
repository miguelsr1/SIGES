/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import sv.gob.mined.siges.web.enumerados.TipoUnidad;


public class FiltroBienesDepreciables implements Serializable {
    
    private Long id;
    private TipoUnidad tipoUnidad;
    private TipoUnidad tipoUnidadDestino;
    private Long unidadActivoFijoId;
    private Long municipioId;
    private Long unidadAdministrativaId;
    private Long unidadActivoFijoDestinoId;
    private Long municipioDestinoId;
    private Long unidadAdministrativaDestinoId;
    private Long categoriaId;
    private Long tipoBienId;
    private Long calidadId;
    private Long EstadoId;
    private Long formaAdquisicionId;
    private Long departamentoId;
    private Long departamentoDestinoId;
    private Boolean esUnidadAdministrativa;
    private List<Integer> estadoProcesoLote;
    
    private String codigoInventario;
    private Boolean activos;
    
    private Long fuenteId;
    private Long proyectoId;
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
    
    private LocalDate fechaTrasladoDesde;
    private LocalDate fechaTrasladoHasta;
    
    private Boolean cargarCosto;
    private Boolean cargarTotalBienes;
    
    private String usuarioModificacion;
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    public FiltroBienesDepreciables() {
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
        return EstadoId;
    }

    public void setEstadoId(Long EstadoId) {
        this.EstadoId = EstadoId;
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
    
}
