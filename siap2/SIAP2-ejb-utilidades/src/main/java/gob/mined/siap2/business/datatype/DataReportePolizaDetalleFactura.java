package gob.mined.siap2.business.datatype;

/**
 *
 * @author eduardo
 */


public class DataReportePolizaDetalleFactura {
    
    private String numeroFactura;
    private String numeroDocumento;
    private String tipoDocumento;
    private String descripcion;
    private String valor;
    
    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroPoliza) {
        this.numeroFactura = numeroPoliza;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
   
}
