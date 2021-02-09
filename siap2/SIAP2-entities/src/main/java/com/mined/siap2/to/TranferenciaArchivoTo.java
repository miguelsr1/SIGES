/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mined.siap2.to;

import gob.mined.siap2.entities.data.SsUsuario;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.CategoriaPresupuestoEscolar;
import gob.mined.siap2.entities.data.impl.ComponentePresupuestoEscolar;
import gob.mined.siap2.entities.data.impl.RelacionGesPresEsAnioFiscal;
import gob.mined.siap2.entities.enums.TipoMonto;
import gob.mined.siap2.entities.enums.TipoPresupuestoAnio;
import gob.mined.siap2.ws.to.DataFile;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author fabricio
 */
public class TranferenciaArchivoTo implements Serializable {
    private DataFile file;
    private AnioFiscal anioFiscal;
    private TipoMonto tipoMonto;
    private CategoriaPresupuestoEscolar componente;
    private ComponentePresupuestoEscolar subComponente;
    private RelacionGesPresEsAnioFiscal relGesPres;
    private TipoPresupuestoAnio tipo;
    private BigDecimal porcentaje;
    private BigDecimal importeFijoCentro;
    private Boolean remanente;
    private List<SsUsuario> usuarios;
    public DataFile getFile() {
        return file;
    }

    public void setFile(DataFile file) {
        this.file = file;
    }

    public AnioFiscal getAnioFiscal() {
        return anioFiscal;
    }

    public void setAnioFiscal(AnioFiscal anioFiscal) {
        this.anioFiscal = anioFiscal;
    }

    public TipoMonto getTipoMonto() {
        return tipoMonto;
    }

    public void setTipoMonto(TipoMonto tipoMonto) {
        this.tipoMonto = tipoMonto;
    }

    public RelacionGesPresEsAnioFiscal getRelGesPres() {
        return relGesPres;
    }

    public void setRelGesPres(RelacionGesPresEsAnioFiscal relGesPres) {
        this.relGesPres = relGesPres;
    }

    public TipoPresupuestoAnio getTipo() {
        return tipo;
    }

    public void setTipo(TipoPresupuestoAnio tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }

    public BigDecimal getImporteFijoCentro() {
        return importeFijoCentro;
    }

    public void setImporteFijoCentro(BigDecimal importeFijoCentro) {
        this.importeFijoCentro = importeFijoCentro;
    }

    public Boolean getRemanente() {
        return remanente;
    }

    public void setRemanente(Boolean remanente) {
        this.remanente = remanente;
    }

    public CategoriaPresupuestoEscolar getComponente() {
        return componente;
    }

    public void setComponente(CategoriaPresupuestoEscolar componente) {
        this.componente = componente;
    }

    public ComponentePresupuestoEscolar getSubComponente() {
        return subComponente;
    }

    public void setSubComponente(ComponentePresupuestoEscolar subComponente) {
        this.subComponente = subComponente;
    }

    public List<SsUsuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<SsUsuario> usuarios) {
        this.usuarios = usuarios;
    }
    
    
}
