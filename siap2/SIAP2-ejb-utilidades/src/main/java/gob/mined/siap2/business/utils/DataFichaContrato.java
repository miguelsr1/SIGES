/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.utils;

import gob.mined.siap2.business.datatype.DataReporteTemplate;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sofis
 */
public class DataFichaContrato extends DataReporteTemplate implements Serializable{
    
    //Datos del cabezal
    private String fechaReporte;
    private String nroContrato;
    private String razonSocial;
    private String nit;
    private String tipoDocumento;
    private String vigenciaDesde;
    private String vigenciaHasta;
    private String montoTotal;
    private String montoGoes;
    private String montoOtro;
    private String montoModificado;
    private String montoConCambios;
    private String pacs;
    private String nroAdquisicion;//Es para poner en lugar de No. de Licitacion
    private String metodoAdquisicion;
    private String administradorContrato;
    private String fuentes;
    private String leyendaPunto7;
    private Boolean existenActasAnticipoYDevolucion;
    private Boolean existenActasRecepcion;
    private Boolean existenQuedanEmitidos;
       
    private List<DataInsumo2> insumos = new ArrayList<>();
    
    private List<DataActa> actasAnticipoYDevolucion = new ArrayList<>();
    
    private List<DataActa> actasRecepcion = new ArrayList<>();
    
    private List<DataQuedanEmitido> quedan = new ArrayList<>();

    public String getFechaReporte() {
        return fechaReporte;
    }

    public void setFechaReporte(String fechaReporte) {
        this.fechaReporte = fechaReporte;
    }

    public String getNroContrato() {
        return nroContrato;
    }

    public void setNroContrato(String nroContrato) {
        this.nroContrato = nroContrato;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getVigenciaDesde() {
        return vigenciaDesde;
    }

    public void setVigenciaDesde(String vigenciaDesde) {
        this.vigenciaDesde = vigenciaDesde;
    }

    public String getVigenciaHasta() {
        return vigenciaHasta;
    }

    public void setVigenciaHasta(String vigenciaHasta) {
        this.vigenciaHasta = vigenciaHasta;
    }

    public String getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(String montoTotal) {
        this.montoTotal = montoTotal;
    }

    public String getMontoGoes() {
        return montoGoes;
    }

    public void setMontoGoes(String montoGoes) {
        this.montoGoes = montoGoes;
    }

    public String getMontoOtro() {
        return montoOtro;
    }

    public void setMontoOtro(String montoOtro) {
        this.montoOtro = montoOtro;
    }

    public String getMontoModificado() {
        return montoModificado;
    }

    public void setMontoModificado(String montoModificado) {
        this.montoModificado = montoModificado;
    }

    public String getMontoConCambios() {
        return montoConCambios;
    }

    public void setMontoConCambios(String montoConCambios) {
        this.montoConCambios = montoConCambios;
    }

    public String getPacs() {
        return pacs;
    }

    public void setPacs(String pacs) {
        this.pacs = pacs;
    }

    public String getNroAdquisicion() {
        return nroAdquisicion;
    }

    public void setNroAdquisicion(String nroAdquisicion) {
        this.nroAdquisicion = nroAdquisicion;
    }

    public String getMetodoAdquisicion() {
        return metodoAdquisicion;
    }

    public void setMetodoAdquisicion(String metodoAdquisicion) {
        this.metodoAdquisicion = metodoAdquisicion;
    }

    public String getAdministradorContrato() {
        return administradorContrato;
    }

    public void setAdministradorContrato(String administradorContrato) {
        this.administradorContrato = administradorContrato;
    }

    public String getFuentes() {
        return fuentes;
    }

    public void setFuentes(String fuentes) {
        this.fuentes = fuentes;
    }

    public List<DataInsumo2> getInsumos() {
        return insumos;
    }

    public void setInsumos(List<DataInsumo2> insumos) {
        this.insumos = insumos;
    }

    public List<DataActa> getActasAnticipoYDevolucion() {
        return actasAnticipoYDevolucion;
    }

    public void setActasAnticipoYDevolucion(List<DataActa> actas) {
        this.actasAnticipoYDevolucion = actas;
    }

    public List<DataQuedanEmitido> getQuedan() {
        return quedan;
    }

    public void setQuedan(List<DataQuedanEmitido> quedan) {
        this.quedan = quedan;
    }

    public String getLeyendaPunto7() {
        return leyendaPunto7;
    }

    public void setLeyendaPunto7(String leyendaPunto7) {
        this.leyendaPunto7 = leyendaPunto7;
    }

    public List<DataActa> getActasRecepcion() {
        return actasRecepcion;
    }

    public void setActasRecepcion(List<DataActa> actasRecepcion) {
        this.actasRecepcion = actasRecepcion;
    }
    
    public Boolean getExistenActasAnticipoYDevolucion() {
        return existenActasAnticipoYDevolucion;
    }

    public void setExistenActasAnticipoYDevolucion(Boolean existenActasAnticipoYDevolucion) {
        this.existenActasAnticipoYDevolucion = existenActasAnticipoYDevolucion;
    }

    public Boolean getExistenActasRecepcion() {
        return existenActasRecepcion;
    }

    public void setExistenActasRecepcion(Boolean existenActasRecepcion) {
        this.existenActasRecepcion = existenActasRecepcion;
    }

    public Boolean getExistenQuedanEmitidos() {
        return existenQuedanEmitidos;
    }

    public void setExistenQuedanEmitidos(Boolean existenQuedanEmitidos) {
        this.existenQuedanEmitidos = existenQuedanEmitidos;
    }
    

    

    
}
