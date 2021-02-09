/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatype;

/**
 * DAtos correspondientes a los insumos de un compromiso presupuestario.
 * @author rgonzalez
 */
public class DataReporteCompromisoPresupuestarioInsumo {
    private String noContrato;
    private String proveedor;
    private String oeg;
    private String mes;
    private String monto;
    private String categoria;
    private String insumo;
    private String lineaPresupuestaria;
    private String poInsumo;

    public String getNoContrato() {
        return noContrato;
    }

    public void setNoContrato(String noContrato) {
        this.noContrato = noContrato;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getOeg() {
        return oeg;
    }

    public void setOeg(String oeg) {
        this.oeg = oeg;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getInsumo() {
        return insumo;
    }

    public void setInsumo(String insumo) {
        this.insumo = insumo;
    }

    public String getLineaPresupuestaria() {
        return lineaPresupuestaria;
    }

    public void setLineaPresupuestaria(String lineaPresupuestaria) {
        this.lineaPresupuestaria = lineaPresupuestaria;
    }

    public String getPoInsumo() {
        return poInsumo;
    }

    public void setPoInsumo(String poInsumo) {
        this.poInsumo = poInsumo;
    }
    
}
