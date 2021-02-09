
package sv.gob.mined.siges.dto;

import java.time.LocalDate;
import sv.gob.mined.siges.persistencia.entidades.SgAfBienDepreciable;
import sv.gob.mined.siges.persistencia.entidades.SgAfDepreciacionMaestro;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAfFuenteFinanciamiento;


public class ProcesoDepreciacionDTO {
    private SgAfDepreciacionMaestro maestroDepreciacion;
    private Boolean dataSecurity;
    private Boolean anioEspecifico;
    private Boolean liquidarProyecto;
    private LocalDate fechaLiquidacion;
    private Boolean bienEspecifico;
    private SgAfBienDepreciable bienProcesar;
    private SgAfFuenteFinanciamiento fuenteDestino;

    public SgAfDepreciacionMaestro getMaestroDepreciacion() {
        return maestroDepreciacion;
    }

    public void setMaestroDepreciacion(SgAfDepreciacionMaestro maestroDepreciacion) {
        this.maestroDepreciacion = maestroDepreciacion;
    }

    public Boolean getDataSecurity() {
        return dataSecurity;
    }

    public void setDataSecurity(Boolean dataSecurity) {
        this.dataSecurity = dataSecurity;
    }

    public Boolean getAnioEspecifico() {
        return anioEspecifico;
    }

    public void setAnioEspecifico(Boolean anioEspecifico) {
        this.anioEspecifico = anioEspecifico;
    }

    public Boolean getLiquidarProyecto() {
        return liquidarProyecto;
    }

    public void setLiquidarProyecto(Boolean liquidarProyecto) {
        this.liquidarProyecto = liquidarProyecto;
    }

    public LocalDate getFechaLiquidacion() {
        return fechaLiquidacion;
    }

    public void setFechaLiquidacion(LocalDate fechaLiquidacion) {
        this.fechaLiquidacion = fechaLiquidacion;
    }

    public Boolean getBienEspecifico() {
        return bienEspecifico;
    }

    public void setBienEspecifico(Boolean bienEspecifico) {
        this.bienEspecifico = bienEspecifico;
    }

    public SgAfBienDepreciable getBienProcesar() {
        return bienProcesar;
    }

    public void setBienProcesar(SgAfBienDepreciable bienProcesar) {
        this.bienProcesar = bienProcesar;
    }

    public SgAfFuenteFinanciamiento getFuenteDestino() {
        return fuenteDestino;
    }

    public void setFuenteDestino(SgAfFuenteFinanciamiento fuenteDestino) {
        this.fuenteDestino = fuenteDestino;
    }
    
    
}
