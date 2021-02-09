/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.graficos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Usuario
 */
public class GraficoColumnas implements Serializable {
    
    private List<DataColumna> valores = new ArrayList();
    private String etiquetaRotulo;
    private String[] etiquetasValor;
    private List<Integer> series;
    private List<Escala> escalas;

    public GraficoColumnas() {
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

    public List<Integer> getSeries() {
        return series;
    }

    public void setSeries(List<Integer> series) {
        this.series = series;
    }

    public List<Escala> getEscalas() {
        return escalas;
    }

    public void setEscalas(List<Escala> escalas) {
        this.escalas = escalas;
    }
    
    
    
}
