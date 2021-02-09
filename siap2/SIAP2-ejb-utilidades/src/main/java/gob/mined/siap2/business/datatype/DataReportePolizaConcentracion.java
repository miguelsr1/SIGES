package gob.mined.siap2.business.datatype;

import gob.mined.siap2.business.datatype.DataReporteTemplate;
import java.util.List;

/**
 *
 * @author eduardo
 */


public class DataReportePolizaConcentracion extends DataReporteTemplate{
    
    private String nroPoliza;
    private String institución;
    private String unidadEjecutora;
    private String codigoPrespuestario;
    private String estadoReserva;
    private String ejercicioFiscal;
    private String nombreSuministrante;
    private String nit;
    private String monto;
    private String nroCuentaBancaria;
    private String fechaEmision;
    
    private List<DataReportePolizaDetalleFactura> listaDetallesFactura;
    
    private List<DataReportePolizaDetalleAplicacionEnPOAyPEP> listaDetallesAplicacionesEnPOAyPEP;    
    
    
    
    public String getNroPoliza() {
        return nroPoliza;
    }

    public void setNroPoliza(String numeroCabezal) {
        this.nroPoliza = numeroCabezal;
    }

    public String getInstitución() {
        return institución;
    }

    public void setInstitución(String institución) {
        this.institución = institución;
    }

    public String getUnidadEjecutora() {
        return unidadEjecutora;
    }

    public void setUnidadEjecutora(String unidadEjecutora) {
        this.unidadEjecutora = unidadEjecutora;
    }

    public String getCodigoPrespuestario() {
        return codigoPrespuestario;
    }

    public void setCodigoPrespuestario(String codigoPrespuestario) {
        this.codigoPrespuestario = codigoPrespuestario;
    }

    public String getEstadoReserva() {
        return estadoReserva;
    }

    public void setEstadoReserva(String estadoReserva) {
        this.estadoReserva = estadoReserva;
    }
    
    public String getEjercicioFiscal() {
        return ejercicioFiscal;
    }

    public void setEjercicioFiscal(String ejercicioFiscal) {
        this.ejercicioFiscal = ejercicioFiscal;
    }

    public String getNombreSuministrante() {
        return nombreSuministrante;
    }

    public void setNombreSuministrante(String nombreSuministrante) {
        this.nombreSuministrante = nombreSuministrante;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    public String getNroCuentaBancaria() {
        return nroCuentaBancaria;
    }

    public void setNroCuentaBancaria(String nroCuentaBancaria) {
        this.nroCuentaBancaria = nroCuentaBancaria;
    }
    
    public String getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(String fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public List<DataReportePolizaDetalleFactura> getListaDetallesFactura() {
        return listaDetallesFactura;
    }

    public void setListaDetallesFactura(List<DataReportePolizaDetalleFactura> listaDetallesFactura) {
        this.listaDetallesFactura = listaDetallesFactura;
    }
    
    public List<DataReportePolizaDetalleAplicacionEnPOAyPEP> getListaDetallesAplicacionesEnPOAyPEP() {
        return listaDetallesAplicacionesEnPOAyPEP;
    }

    public void setListaDetallesAplicacionesEnPOAyPEP(List<DataReportePolizaDetalleAplicacionEnPOAyPEP> listaDetallesAplicacionesEnPOAyPEP) {
        this.listaDetallesAplicacionesEnPOAyPEP = listaDetallesAplicacionesEnPOAyPEP;
    }
    
}
