/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatype;

import gob.mined.siap2.utils.generalutils.NumberUtils;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Esta clase corresponde a un tipo de dato que es una fila del reporte global
 * PEP.
 *
 * @note Tipo de dato que corresponde a una fila del reporte global PEP.
 * @author Sofis Solutions
 */
public class DataFilaGlobalReportePEP {

    String nombre;
    List<String> montoMesesString = new LinkedList<>();
    List<BigDecimal> montoMeses = new LinkedList<>();
    Integer colorFila;

    /**
     * Constructor por defecto. Inicializa todos los meses en el valor 0.
     */
    public DataFilaGlobalReportePEP() {
        montoMesesString = new LinkedList<>();
        montoMeses = new LinkedList<>();
        for (int i = 0; i < 12; i++) {
            montoMesesString.add("");
            montoMeses.add(BigDecimal.ZERO);
        }
    }

    /**
     * Procesa una flujo de cada y lo adiciona en cada uno de los meses de la
     * PEP.
     *
     * @param lfc
     */
    public void sumarFCM(Collection<Object[]> lfc) {
        for (Object[] iter : lfc) {
            Integer mes = (Integer) iter[0];
            BigDecimal valorMes = (BigDecimal) iter[1];

            mes = mes - 1;

            BigDecimal totalMes = BigDecimal.ZERO;
            if (mes < montoMeses.size()) {
                totalMes = montoMeses.get(mes);
            }

            if (valorMes != null) {
                totalMes = totalMes.add(valorMes);
            }

            //se añade a los meses
            addListOrreplace(montoMeses, mes, totalMes);
            addListOrreplace(montoMesesString, mes, NumberUtils.nomberToString(totalMes));
        }
    }

    /**
     * Agrega o sustituye un elemento de una lista.
     *
     * @param l Lista
     * @param pos posición en la que se debe insertar o sustituir.
     * @param valor valor que se agregará o sustituirá.
     */
    public void addListOrreplace(List l, int pos, Object valor) {
        if (pos < l.size()) {
            l.set(pos, valor);
        } else {
            l.add(pos, valor);
        }
    }

    /**
     * Calcula el monto total de montoMeses (cargado previamente).
     *
     * @return Devuelve el valor en tipo String.
     */
    public String getSumaTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (BigDecimal iter : montoMeses) {
            total = total.add(iter);
        }
        return NumberUtils.nomberToString(total);
    }

    /**
     * Permite obtener el valor del mes i en montoMeses (cargado previamente).
     *
     * @param i es el mes.
     * @return en formato String.
     */
    public String getValorMes(int i) {
        BigDecimal valor = montoMeses.get(i);
        if (valor == null) {
            valor = BigDecimal.ZERO;
        }
        return NumberUtils.nomberToString(valor);
    }

    public DataFilaGlobalReportePEP getThis() {
        return this;
    }

    // <editor-fold defaultstate="collapsed" desc="getter-setter"> 
    public List<BigDecimal> getMontoMeses() {
        return montoMeses;
    }

    public void setMontoMeses(List<BigDecimal> montoMeses) {
        this.montoMeses = montoMeses;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getColorFila() {
        return colorFila;
    }

    public void setColorFila(Integer colorFila) {
        this.colorFila = colorFila;
    }

    public List<String> getMontoMesesString() {
        return montoMesesString;
    }

    public void setMontoMesesString(List<String> montoMesesString) {
        this.montoMesesString = montoMesesString;
    }
    // </editor-fold>
}
