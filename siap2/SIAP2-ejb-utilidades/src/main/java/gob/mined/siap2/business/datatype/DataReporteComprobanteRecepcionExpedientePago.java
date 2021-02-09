/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatype;

/**
 * Datos para el reporte del comprobante de recepción de expediente de pago.
 * @author rgonzalez
 */
public class DataReporteComprobanteRecepcionExpedientePago extends DataReporteTemplate{

    private String nombreProyecto;
    private String quedanNO;
    private String nombreProveedor;
    private String monto;
    private String telefono;
    private String mail;
    private String documentoDePago;
    private String fechaEntrega;
    private String nombreFirmaTecnico;
    private String telefonoFirmanteTecnico;
    private String NITProveedor;
    private String notaPiePagina;
    

    public String getNombreProyecto() {
        return nombreProyecto;
    }

    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }

    public String getQuedanNO() {
        return quedanNO;
    }

    public void setQuedanNO(String quedanNO) {
        this.quedanNO = quedanNO;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getDocumentoDePago() {
        return documentoDePago;
    }

    public void setDocumentoDePago(String documentoDePago) {
        this.documentoDePago = documentoDePago;
    }

    public String getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(String fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getNombreFirmaTecnico() {
        return nombreFirmaTecnico;
    }

    public void setNombreFirmaTecnico(String nombreFirmaTecnico) {
        this.nombreFirmaTecnico = nombreFirmaTecnico;
    }

    public String getTelefonoFirmanteTecnico() {
        return telefonoFirmanteTecnico;
    }

    public void setTelefonoFirmanteTecnico(String telefonoFirmanteTecnico) {
        this.telefonoFirmanteTecnico = telefonoFirmanteTecnico;
    }
    
    public String getNITProveedor() {
        return NITProveedor;
    }

    public void setNITProveedor(String NITProveedor) {
        this.NITProveedor = NITProveedor;
    }

    public String getNotaPiePagina() {
        return notaPiePagina;
    }

    public void setNotaPiePagina(String notaPiePagina) {
        this.notaPiePagina = notaPiePagina;
    }
    
}
