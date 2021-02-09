/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.mined.siap2.ws.to;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Esta clase corresponde al objeto para presentar el reporte de resumen de clasificadores funcionales del gasto..
 * @author Sofis Solutions
 */
public class CronogramaRecurso implements Serializable {
    private String nombreClasificador;
    private BigDecimal montoPlanificado;
    private BigDecimal montoActas;
    private BigDecimal montoQuedan;
    private BigDecimal montoProyectado;
    private Boolean ejecutadoMenorAPlanificado;
   

    public String getNombreClasificador() {
        return nombreClasificador;
    }

    public void setNombreClasificador(String nombreClasificador) {
        this.nombreClasificador = nombreClasificador;
    }

    public BigDecimal getMontoPlanificado() {
        return montoPlanificado;
    }

    public void setMontoPlanificado(BigDecimal montoPlanificado) {
        this.montoPlanificado = montoPlanificado;
    }

    public BigDecimal getMontoActas() {
        return montoActas;
    }

    public void setMontoActas(BigDecimal montoActas) {
        this.montoActas = montoActas;
    }

    public BigDecimal getMontoQuedan() {
        return montoQuedan;
    }

    public void setMontoQuedan(BigDecimal montoQuedan) {
        this.montoQuedan = montoQuedan;
    }

    public BigDecimal getMontoProyectado() {
        return montoProyectado;
    }

    public void setMontoProyectado(BigDecimal montoProyectado) {
        this.montoProyectado = montoProyectado;
    }

    public Boolean getEjecutadoMenorAPlanificado() {
        return ejecutadoMenorAPlanificado;
    }

    public void setEjecutadoMenorAPlanificado(Boolean ejecutadoMenorAPlanificado) {
        this.ejecutadoMenorAPlanificado = ejecutadoMenorAPlanificado;
    }
  
        
}
