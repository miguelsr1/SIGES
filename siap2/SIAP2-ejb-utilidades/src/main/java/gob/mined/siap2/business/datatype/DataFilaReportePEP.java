/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatype;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * Esta clase corresponde una fila de datos del reporte PEP.
 *
 * @note Fila de datos del reporte PEP.
 * @author Sofis Solutions
 */
public class DataFilaReportePEP {

    //0 blanco comun
    //1 gris total cuenta
    //2 gris del todo, total rubro
    private Integer colorFila;
    private String nombre;
    private BigDecimal[] motnoMeses;

    /**
     * Inicialización de los montos de los meses. El valor de inicialización es
     * 0.
     */
    public void initMontoMeses() {
        motnoMeses = new BigDecimal[12];
        for (int i = 0; i < 12; i++) {
            motnoMeses[i] = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
        }
    }

    /**
     * Este método devuelve los montos como una lista de BigDecimal.
     *
     * @return
     */
    public List<BigDecimal> getMotosComoLista() {
        return Arrays.asList(motnoMeses);
    }

    // <editor-fold defaultstate="collapsed" desc="getter-setter"> 
    /**
     * Devuelve el color que corresponde a la fila 0 blanco comun 1 gris total
     * cuenta 2 gris del todo, total rubro
     *
     * @return
     */
    public Integer getColorFila() {
        return colorFila;
    }

    /**
     * Ver {@see #getColorFila}
     * @param colorFila 
     */
    public void setColorFila(Integer colorFila) {
        this.colorFila = colorFila;
    }

    /**
     * Devuelve el nombre
     * @return 
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Ver {@see #getNombre}
     * @param nombre 
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Montos por mes
     * @return 
     */
    public BigDecimal[] getMotnoMeses() {
        return motnoMeses;
    }

    /**
     * Ver {@see #getMotnoMeses}
     * @param motnoMeses 
     */
    public void setMotnoMeses(BigDecimal[] motnoMeses) {
        this.motnoMeses = motnoMeses;
    }

 // </editor-fold>
}
