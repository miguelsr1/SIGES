/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.utils;

import gob.mined.siap2.business.datatype.DataReporteTemplate;
import java.util.List;

/**
 *
 * @author Sofis Solutions
 */
public class DataProveedorItems extends DataReporteTemplate{
    
    private String proveedorNombre;
    private String proveedorDir;
    private String proveedorTel;
    private String proveedorFax;
    private String proveedorMail;
    private String textoCabezal;
    private String nroInvitacion;
    private String usuarioProceso;
    private String autorizadoMINED;
    private List<DataItem> items;
    private String observaciones;
    private String telUsuarioProceso;
    private String fechaInvitacion;
    private String fechaLimiteRecepcion;
    private String fuentes;
    private String horaLimiteRecepción;
    private String numeroProcesoAdq;
    private Boolean existenLotes;

    public String getProveedorNombre() {
        return proveedorNombre;
    }

    public void setProveedorNombre(String proveedorNombre) {
        this.proveedorNombre = proveedorNombre;
    }

    public String getProveedorDir() {
        return proveedorDir;
    }

    public void setProveedorDir(String proveedorDir) {
        this.proveedorDir = proveedorDir;
    }

    public String getProveedorTel() {
        return proveedorTel;
    }

    public void setProveedorTel(String proveedorTel) {
        this.proveedorTel = proveedorTel;
    }

    public String getProveedorFax() {
        return proveedorFax;
    }

    public void setProveedorFax(String proveedorFax) {
        this.proveedorFax = proveedorFax;
    }

    public String getProveedorMail() {
        return proveedorMail;
    }

    public void setProveedorMail(String proveedorMail) {
        this.proveedorMail = proveedorMail;
    }

    public List<DataItem> getItems() {
        return items;
    }

    public void setItems(List<DataItem> items) {
        this.items = items;
    }

    public String getTextoCabezal() {
        return textoCabezal;
    }

    public void setTextoCabezal(String textoCabezal) {
        this.textoCabezal = textoCabezal;
    }

    public String getNroInvitacion() {
        return nroInvitacion;
    }

    public void setNroInvitacion(String nroInvitacion) {
        this.nroInvitacion = nroInvitacion;
    }

    public String getUsuarioProceso() {
        return usuarioProceso;
    }

    public void setUsuarioProceso(String usuarioProceso) {
        this.usuarioProceso = usuarioProceso;
    }

    public String getAutorizadoMINED() {
        return autorizadoMINED;
    }

    public void setAutorizadoMINED(String autorizadoMINED) {
        this.autorizadoMINED = autorizadoMINED;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    public String getTelUsuarioProceso() {
        return telUsuarioProceso;
    }

    public void setTelUsuarioProceso(String telUsuarioProceso) {
        this.telUsuarioProceso = telUsuarioProceso;
    }
    
    public String getFechaInvitacion() {
        return fechaInvitacion;
    }

    public void setFechaInvitacion(String fechaInvitacion) {
        this.fechaInvitacion = fechaInvitacion;
    }

    public String getFechaLimiteRecepcion() {
        return fechaLimiteRecepcion;
    }

    public void setFechaLimiteRecepcion(String fechaLimiteRecepcion) {
        this.fechaLimiteRecepcion = fechaLimiteRecepcion;
    }

    public String getFuentes() {
        return fuentes;
    }

    public void setFuentes(String fuentes) {
        this.fuentes = fuentes;
    }

    public String getHoraLimiteRecepción() {
        return horaLimiteRecepción;
    }

    public void setHoraLimiteRecepción(String horaLimiteRecepción) {
        this.horaLimiteRecepción = horaLimiteRecepción;
    }   

    public String getNumeroProcesoAdq() {
        return numeroProcesoAdq;
    }

    public void setNumeroProcesoAdq(String numeroProcesoAdq) {
        this.numeroProcesoAdq = numeroProcesoAdq;
    }

    public Boolean getExistenLotes() {
        return existenLotes;
    }

    public void setExistenLotes(Boolean existenLotes) {
        this.existenLotes = existenLotes;
    }
    

}
