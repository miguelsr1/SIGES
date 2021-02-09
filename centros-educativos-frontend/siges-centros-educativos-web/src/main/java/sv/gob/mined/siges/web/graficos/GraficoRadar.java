/*
 *  Nombre del cliente
 *  Sistema de Gestión
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.graficos;

import java.io.Serializable;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class GraficoRadar implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(GraficoRadar.class.getName());

    private Double[] valores;
    private String[] etiquetasValor;

    private String chartData = null;
    private String chartLabels = null;

    public GraficoRadar() {
    }

    public String getChartData() {
        if (chartData == null || chartData.trim().length() <= 3) {
            populateData();
        }
        return chartData != null ? chartData : "[]";
    }

    public String getChartLabels() {
        if (chartLabels == null || chartLabels.trim().length() <= 3) {
            populateLabels();
        }
        return chartLabels != null ? chartLabels : "[]";
    }

    public void populateLabels() {
        if (etiquetasValor != null) {
            chartLabels = " ";

            chartLabels += "[";
            for (String s : etiquetasValor) {
                chartLabels += "'" + s + "',";
            }
            chartLabels = chartLabels.substring(0, chartLabels.length() - 1);
            chartLabels += "]";
        }
    }

    public void populateData() {
        if (valores != null) {
            chartData = "";
            chartData += "[";
            for (Double f : this.valores) {
                chartData += f + ", ";
            }
            chartData = chartData.substring(0, chartData.length() - 2);
            chartData += "]";
        }
    }

    public Double[] getValores() {
        return valores;
    }

    public void setValores(Double[] valores) {
        this.valores = valores;
    }

    public String[] getEtiquetasValor() {
        return etiquetasValor;
    }

    public void setEtiquetasValor(String[] etiquetasValor) {
        this.etiquetasValor = etiquetasValor;
    }

}
