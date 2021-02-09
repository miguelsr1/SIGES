/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.utils;

import gob.mined.siap2.business.datatype.DataReporteTemplate;

/**
 *
 * @author eduardo
 */
public class DataOrdenInicio extends DataReporteTemplate{

    private String nombreProveedor;
    private String empresaProveedor;
    private String firmante;
    private String contenido;
    private String cargoFirmante;
    private String fechaEmision;
    private String UTFirmante;

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }

    public String getEmpresaProveedor() {
        return empresaProveedor;
    }

    public void setEmpresaProveedor(String empresaProveedor) {
        this.empresaProveedor = empresaProveedor;
    }

    public String getFirmante() {
        return firmante;
    }

    public void setFirmante(String firmante) {
        this.firmante = firmante;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getCargoFirmante() {
        return cargoFirmante;
    }

    public void setCargoFirmante(String cargoFirmante) {
        this.cargoFirmante = cargoFirmante;
    }

    public String getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(String fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getUTFirmante() {
        return UTFirmante;
    }

    public void setUTFirmante(String uTFirmante) {
        this.UTFirmante = uTFirmante;
    }

}
