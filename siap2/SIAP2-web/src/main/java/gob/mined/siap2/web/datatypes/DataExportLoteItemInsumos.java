/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.datatypes;

/**
 *
 * @author Sofis Solutions
 */
public class DataExportLoteItemInsumos {

    private String lote;
    private String item;
    private String insumo;
    private String cantidad;
    
    public DataExportLoteItemInsumos(String unLote, String unItem, String unInsumo, String unaCantidad){
        this.lote = unLote;
        this.item = unItem;
        this.insumo = unInsumo;
        this.cantidad = unaCantidad;
    }
    
    public DataExportLoteItemInsumos(String unItem, String unInsumo, String unaCantidad){
        this.lote = "";
        this.item = unItem;
        this.insumo = unInsumo;
        this.cantidad = unaCantidad;
    }
    
    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getInsumo() {
        return insumo;
    }

    public void setInsumo(String insumo) {
        this.insumo = insumo;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }
    
    public DataExportLoteItemInsumos(){
        this.lote = "";
        this.item = "";
        this.insumo = "";
        this.cantidad = "";
    }
    
}
