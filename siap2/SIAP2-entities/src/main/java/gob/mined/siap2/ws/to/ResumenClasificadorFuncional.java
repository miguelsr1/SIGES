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
public class ResumenClasificadorFuncional implements Serializable {
    private String nombreClasificador;
    private BigDecimal importeEstimado;
    private BigDecimal importeModificado;
    private BigDecimal pep;
    private BigDecimal importeAdjudicado;
    private BigDecimal importeComprometido;
    private BigDecimal importeCertificado;
    private BigDecimal saldo;
   

    public String getNombreClasificador() {
        return nombreClasificador;
    }

    public void setNombreClasificador(String nombreClasificador) {
        this.nombreClasificador = nombreClasificador;
    }

    public BigDecimal getImporteEstimado() {
        return importeEstimado;
    }

    public void setImporteEstimado(BigDecimal importeEstimado) {
        this.importeEstimado = importeEstimado;
    }

    public BigDecimal getImporteAdjudicado() {
        return importeAdjudicado;
    }

    public void setImporteAdjudicado(BigDecimal importeAdjudicado) {
        this.importeAdjudicado = importeAdjudicado;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public BigDecimal getImporteComprometido() {
        return importeComprometido;
    }

    public void setImporteComprometido(BigDecimal importeComprometido) {
        this.importeComprometido = importeComprometido;
    }

    public BigDecimal getPep() {
        return pep;
    }

    public void setPep(BigDecimal pep) {
        this.pep = pep;
    }

    public BigDecimal getImporteCertificado() {
        return importeCertificado;
    }

    public void setImporteCertificado(BigDecimal importeCertificado) {
        this.importeCertificado = importeCertificado;
    }

    public BigDecimal getImporteModificado() {
        return importeModificado;
    }

    public void setImporteModificado(BigDecimal importeModificado) {
        this.importeModificado = importeModificado;
    }
        
}
