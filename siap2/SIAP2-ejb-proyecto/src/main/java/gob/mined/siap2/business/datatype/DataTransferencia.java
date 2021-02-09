/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.mined.siap2.business.datatype;

import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.ComponentePresupuestoEscolar;
import gob.mined.siap2.entities.data.impl.Cuentas;
import gob.mined.siap2.entities.data.impl.FuenteFinanciamiento;
import gob.mined.siap2.entities.data.impl.FuenteRecursos;
import gob.mined.siap2.entities.data.impl.RelacionGesPresEsAnioFiscal;
import gob.mined.siap2.entities.data.impl.Transferencia;
import gob.mined.siges.entities.data.impl.SgSede;
import java.math.BigDecimal;

/**
 *
 * @author bruno
 */
public class DataTransferencia {
    private Integer id;
    private ComponentePresupuestoEscolar gesPresEs;
    private Cuentas subCuenta;
    private SgSede sede;
    private BigDecimal monto;
    private AnioFiscal anioFiscal;
    private FuenteFinanciamiento fuenteFinanciamiento;
    private FuenteRecursos fuenteRecursos;
    private RelacionGesPresEsAnioFiscal relGesPres;
    private Transferencia tranferencia;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ComponentePresupuestoEscolar getGesPresEs() {
        return gesPresEs;
    }

    public void setGesPresEs(ComponentePresupuestoEscolar gesPresEs) {
        this.gesPresEs = gesPresEs;
    }

    public Cuentas getSubCuenta() {
        return subCuenta;
    }

    public void setSubCuenta(Cuentas subCuenta) {
        this.subCuenta = subCuenta;
    }

    public SgSede getSede() {
        return sede;
    }

    public void setSede(SgSede sede) {
        this.sede = sede;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public AnioFiscal getAnioFiscal() {
        return anioFiscal;
    }

    public void setAnioFiscal(AnioFiscal anioFiscal) {
        this.anioFiscal = anioFiscal;
    }

    public FuenteFinanciamiento getFuenteFinanciamiento() {
        return fuenteFinanciamiento;
    }

    public void setFuenteFinanciamiento(FuenteFinanciamiento fuenteFinanciamiento) {
        this.fuenteFinanciamiento = fuenteFinanciamiento;
    }

    public FuenteRecursos getFuenteRecursos() {
        return fuenteRecursos;
    }

    public void setFuenteRecursos(FuenteRecursos fuenteRecursos) {
        this.fuenteRecursos = fuenteRecursos;
    }

    public RelacionGesPresEsAnioFiscal getRelGesPres() {
        return relGesPres;
    }

    public void setRelGesPres(RelacionGesPresEsAnioFiscal relGesPres) {
        this.relGesPres = relGesPres;
    }

    public Transferencia getTranferencia() {
        return tranferencia;
    }

    public void setTranferencia(Transferencia tranferencia) {
        this.tranferencia = tranferencia;
    }
    
    
    
}
