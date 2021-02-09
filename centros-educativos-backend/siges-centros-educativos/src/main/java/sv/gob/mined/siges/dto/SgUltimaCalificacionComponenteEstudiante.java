/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.dto;

import java.time.LocalDate;
import sv.gob.mined.siges.persistencia.entidades.SgComponentePlanEstudio;


/**
 *
 * @author USUARIO
 */
public class SgUltimaCalificacionComponenteEstudiante {
    
    private SgComponentePlanEstudio uccComponentePlanEstudio;
    private LocalDate uccFechaRealizado;
    private String uccValor;
    private Double uccValorMinimoAprobacion;
    private Double uccValorMaximo;

    public SgComponentePlanEstudio getUccComponentePlanEstudio() {
        return uccComponentePlanEstudio;
    }

    public void setUccComponentePlanEstudio(SgComponentePlanEstudio uccComponentePlanEstudio) {
        this.uccComponentePlanEstudio = uccComponentePlanEstudio;
    }

    public String getUccValor() {
        return uccValor;
    }

    public void setUccValor(String uccValor) {
        this.uccValor = uccValor;
    }

    public LocalDate getUccFechaRealizado() {
        return uccFechaRealizado;
    }

    public void setUccFechaRealizado(LocalDate uccFechaRealizado) {
        this.uccFechaRealizado = uccFechaRealizado;
    }

    public Double getUccValorMinimoAprobacion() {
        return uccValorMinimoAprobacion;
    }

    public void setUccValorMinimoAprobacion(Double uccValorMinimoAprobacion) {
        this.uccValorMinimoAprobacion = uccValorMinimoAprobacion;
    }

    public Double getUccValorMaximo() {
        return uccValorMaximo;
    }

    public void setUccValorMaximo(Double uccValorMaximo) {
        this.uccValorMaximo = uccValorMaximo;
    }

    
    
    
}
