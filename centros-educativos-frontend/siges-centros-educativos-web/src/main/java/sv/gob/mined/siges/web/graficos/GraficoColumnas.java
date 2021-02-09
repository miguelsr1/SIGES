/*
 *  Nombre del cliente
 *  Sistema de Gestión
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.graficos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class GraficoColumnas implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(GraficoColumnas.class.getName());

    private List<DataColumna> valores = new ArrayList();
    private String etiquetaRotulo;
    private String[] etiquetasValor;
    private List<Integer> series;
    private List<Escala> escalas;

    private String chartData = null;
    private String seriesData = null;
    private String vAxesData = null;

    public GraficoColumnas() {
    }

    public String getChartData() {
        if (chartData == null || chartData.trim().length() <= 0) {
            populateData();
        }
        return chartData;
    }
    
    public String getSeriesData(){
        if (seriesData == null || seriesData.trim().length() <= 0){
            populateSeries();
        }
        return seriesData != null ? seriesData : "{}";
    }
    
    public String getVAxesData(){
        if (vAxesData == null || vAxesData.trim().length() <= 0){
            populateVAxesData();
        }
        return vAxesData != null ? vAxesData : "{}";
    }
    
    
    
    
    public void populateData(){
        if (valores != null && etiquetasValor != null){
            chartData = "";

            chartData += "['"+etiquetaRotulo+"',";
            for (String s : etiquetasValor) {
                chartData += "'" + s + "',";
            }
            chartData = chartData.substring(0, chartData.length() - 1);
            chartData += "],";

            for (DataColumna c : valores) {
                chartData += "['" + c.getRotulo() + "',";
                for (Double f : c.getValor()) {
                    chartData += f + ", ";
                }
                chartData = chartData.substring(0, chartData.length() - 2);
                chartData += "],";
            }
            chartData = chartData.substring(0, chartData.length() - 1);
        }
        
    }
    
    public void populateSeries(){
        if (series != null){
            seriesData = "{";
            
            Integer count = 0;
            for (Integer t : series){
                seriesData += count + ": {targetAxisIndex: " + t +"},"; // , color: 'red'
                count++;
            }               
            seriesData = seriesData.substring(0, seriesData.length() - 1);
            seriesData += "}";
        }
    }
    
    public void populateVAxesData(){
        if (escalas != null){
            
            vAxesData = "{";
            
            for (int i = 0; i < escalas.size(); i++){
                Escala c = escalas.get(i);
                
                vAxesData += i + ": { title: '" + c.getLabel() + "'," +
                        "ticks: [";
                
                for (Tick t : c.getTicks()){
                    vAxesData += "{v: " + t.getValor() + ", f: '" + t.getLabel() + "'},";
                }
                vAxesData = vAxesData.substring(0, vAxesData.length() - 1);            
                vAxesData += "]},";
            }
            vAxesData = vAxesData.substring(0, vAxesData.length() - 1);
            
            vAxesData += '}';
 
        }
    
    
    }

    public List<DataColumna> getValores() {
        return valores;
    }

    public void setValores(List<DataColumna> valores) {
        this.valores = valores;
    }

    public String getEtiquetaRotulo() {
        return etiquetaRotulo;
    }

    public void setEtiquetaRotulo(String etiquetaRotulo) {
        this.etiquetaRotulo = etiquetaRotulo;
    }

    public String[] getEtiquetasValor() {
        return etiquetasValor;
    }

    public void setEtiquetasValor(String[] etiquetasValor) {
        this.etiquetasValor = etiquetasValor;
    }

    public List<Escala> getEscalas() {
        return escalas;
    }

    public void setEscalas(List<Escala> escalas) {
        this.escalas = escalas;
    }
   
    public List<Integer> getSeries() {
        return series;
    }

    public void setSeries(List<Integer> series) {
        this.series = series;
    }

}
