package gob.mined.siap2.business.datatype;

import java.util.Map;



public class DataReporteCompromisoPresupuestarioProgPagosItem {
   private Integer tipoFila;
   private String contrato = "";
   private String mes = "";
   private String item = "";
   private String insumo = "";
   private String ut = "";
   private String oeg ="";
   private String tipoPOA = "";
   private String id ="";
   private Map<String, String> montosFuentes;

    public String getContrato() {
        return contrato;
    }

    public void setContrato(String contrato) {
        this.contrato = contrato;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public Integer getTipoFila() {
        return tipoFila;
    }

    public void setTipoFila(Integer tipoFila) {
        this.tipoFila = tipoFila;
    }

    public String getOeg() {
        return oeg;
    }

    public void setOeg(String oeg) {
        this.oeg = oeg;
    }

    public String getTipoPOA() {
        return tipoPOA;
    }

    public void setTipoPOA(String tipoPOA) {
        this.tipoPOA = tipoPOA;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getUt() {
        return ut;
    }

    public void setUt(String ut) {
        this.ut = ut;
    }

    public Map<String, String> getMontosFuentes() {
        return montosFuentes;
    }

    public void setMontosFuentes(Map<String, String> montosFuentes) {
        this.montosFuentes = montosFuentes;
    }

   
   
   
   
   
   
    
}
