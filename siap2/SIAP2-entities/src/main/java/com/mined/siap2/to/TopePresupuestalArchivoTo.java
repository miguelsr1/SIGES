package com.mined.siap2.to;

import gob.mined.siap2.entities.data.SsUsuario;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.ComponentePresupuestoEscolar;
import gob.mined.siap2.entities.data.impl.RelacionGesPresEsAnioFiscal;
import gob.mined.siap2.entities.enums.TipoMonto;
import gob.mined.siap2.entities.enums.TipoPresupuestoAnio;
import gob.mined.siap2.ws.to.DataFile;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author fabricio
 */
public class TopePresupuestalArchivoTo implements Serializable {
    
    
    private DataFile file;
    private AnioFiscal anioFiscal;
    private TipoMonto tipoMonto;
    private ComponentePresupuestoEscolar gesPresEs;
    private RelacionGesPresEsAnioFiscal relGesPres;
    private TipoPresupuestoAnio tipo;
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

    public ComponentePresupuestoEscolar getGesPresEs() {
        return gesPresEs;
    }

    public void setGesPresEs(ComponentePresupuestoEscolar gesPresEs) {
        this.gesPresEs = gesPresEs;
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

    public List<SsUsuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<SsUsuario> usuarios) {
        this.usuarios = usuarios;
    }
    
}
