/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Sofis Solutions
 */
public class DetalleDescargo implements Serializable {
    private Long id;
    private String codigoInventario;
    private String codigoSolicitud;
    private String descripcion;
    private String marca;
    private String modelo;
    private String noserie;
    private String nombreCategoria;
    private String fechaAdquisicion;
    private String fechaSolicitud;
    private String fechaFallo;
    private String direccionUnidad;
    private String fechaDescargo;
    private String tipoDecargadoNombre;
    private String tipoUnidad;
    private String nombreUnidad;
    private String codigoUnidad;
    private BigDecimal valoradquisicion;
    private BigDecimal depreciacion;

    public String getCodigoInventario() {
        return codigoInventario;
    }

    public void setCodigoInventario(String codigoInventario) {
        this.codigoInventario = codigoInventario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public String getNoserie() {
        return noserie;
    }

    public void setNoserie(String noserie) {
        this.noserie = noserie;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public String getFechaAdquisicion() {
        return fechaAdquisicion;
    }

    public void setFechaAdquisicion(String fechaAdquisicion) {
        this.fechaAdquisicion = fechaAdquisicion;
    }

    public String getFechaDescargo() {
        return fechaDescargo;
    }

    public void setFechaDescargo(String fechaDescargo) {
        this.fechaDescargo = fechaDescargo;
    }

    public BigDecimal getValoradquisicion() {
        return valoradquisicion;
    }

    public void setValoradquisicion(BigDecimal valoradquisicion) {
        this.valoradquisicion = valoradquisicion;
    }

    public BigDecimal getDepreciacion() {
        return depreciacion;
    }

    public void setDepreciacion(BigDecimal depreciacion) {
        this.depreciacion = depreciacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipoDecargadoNombre() {
        return tipoDecargadoNombre;
    }

    public void setTipoDecargadoNombre(String tipoDecargadoNombre) {
        this.tipoDecargadoNombre = tipoDecargadoNombre;
    }

    public String getTipoUnidad() {
        return tipoUnidad;
    }

    public void setTipoUnidad(String tipoUnidad) {
        this.tipoUnidad = tipoUnidad;
    }

    public String getNombreUnidad() {
        return nombreUnidad;
    }

    public void setNombreUnidad(String nombreUnidad) {
        this.nombreUnidad = nombreUnidad;
    }

    public String getCodigoUnidad() {
        return codigoUnidad;
    }

    public void setCodigoUnidad(String codigoUnidad) {
        this.codigoUnidad = codigoUnidad;
    }

    public String getDireccionUnidad() {
        return direccionUnidad;
    }

    public void setDireccionUnidad(String direccionUnidad) {
        this.direccionUnidad = direccionUnidad;
    }

    public String getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(String fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public String getCodigoSolicitud() {
        return codigoSolicitud;
    }

    public void setCodigoSolicitud(String codigoSolicitud) {
        this.codigoSolicitud = codigoSolicitud;
    }

    public String getFechaFallo() {
        return fechaFallo;
    }

    public void setFechaFallo(String fechaFallo) {
        this.fechaFallo = fechaFallo;
    }
}
