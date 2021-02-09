/*
 *  Nombre del cliente
 *  Sistema de Gestión
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.graficos;

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

    public GraficoRadar() {
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
