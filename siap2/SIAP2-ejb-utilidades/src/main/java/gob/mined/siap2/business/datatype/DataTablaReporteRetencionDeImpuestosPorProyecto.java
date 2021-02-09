/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatype;

/**
 * Datos para el reporte de retención de impuestos por proyecto.
 *
 * @author rgonzalez
 */
public class DataTablaReporteRetencionDeImpuestosPorProyecto {

    private String numero;
    private String apellidos;
    private String nombres;
    private String razonSocialODenominacion;
    private String quedan;
    private String nit;
    private String codigoDeIngreso;
    private String numeroDeFactura;
    private String ingresosSujetosDeRetencion;
    private String impuestoRetenido;
    private String total;
    private String calidadEnQueActua;
    private String modalidad;
    private String codigoDocmento;
    private String numeroDocumento;


    // <editor-fold defaultstate="collapsed" desc="getter-setter"> 
    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getQuedan() {
        return quedan;
    }

    public void setQuedan(String quedan) {
        this.quedan = quedan;
    }

    public String getRazonSocialODenominacion() {
        return razonSocialODenominacion;
    }

    public void setRazonSocialODenominacion(String razonSocialODenominacion) {
        this.razonSocialODenominacion = razonSocialODenominacion;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getCodigoDeIngreso() {
        return codigoDeIngreso;
    }

    public void setCodigoDeIngreso(String codigoDeIngreso) {
        this.codigoDeIngreso = codigoDeIngreso;
    }

    public String getNumeroDeFactura() {
        return numeroDeFactura;
    }

    public void setNumeroDeFactura(String numeroDeFactura) {
        this.numeroDeFactura = numeroDeFactura;
    }

    public String getIngresosSujetosDeRetencion() {
        return ingresosSujetosDeRetencion;
    }

    public void setIngresosSujetosDeRetencion(String ingresosSujetosDeRetencion) {
        this.ingresosSujetosDeRetencion = ingresosSujetosDeRetencion;
    }

    public String getImpuestoRetenido() {
        return impuestoRetenido;
    }

    public void setImpuestoRetenido(String impuestoRetenido) {
        this.impuestoRetenido = impuestoRetenido;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
    
    public String getCalidadEnQueActua() {
        return calidadEnQueActua;
    }

    public void setCalidadEnQueActua(String calidadEnQueActua) {
        this.calidadEnQueActua = calidadEnQueActua;
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    public String getCodigoDocmento() {
        return codigoDocmento;
    }

    public void setCodigoDocmento(String codigoDocmento) {
        this.codigoDocmento = codigoDocmento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

      // </editor-fold>
}
