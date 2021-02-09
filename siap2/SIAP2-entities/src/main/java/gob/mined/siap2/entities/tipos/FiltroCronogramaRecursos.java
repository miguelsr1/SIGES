/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.mined.siap2.entities.tipos;

import java.io.Serializable;

/**
 *
 * @author Sofis Solutions
 */
public class FiltroCronogramaRecursos implements Serializable {

    private String idAnioFiscal;
    private String idProyecto;
    private String idPOA;
    private String idPAC;
    private Integer rubro;
    private String idFuenteRecursos;
    private String idFuenteFinanciamiento;
    private String tipoInsumo;

    public String getIdAnioFiscal() {
        return idAnioFiscal;
    }

    public void setIdAnioFiscal(String idAnioFiscal) {
        this.idAnioFiscal = idAnioFiscal;
    }

    public String getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(String idProyecto) {
        this.idProyecto = idProyecto;
    }

    public String getIdPOA() {
        return idPOA;
    }

    public void setIdPOA(String idPOA) {
        this.idPOA = idPOA;
    }

    public String getIdPAC() {
        return idPAC;
    }

    public void setIdPAC(String idPAC) {
        this.idPAC = idPAC;
    }

    public Integer getRubro() {
        return rubro;
    }

    public void setRubro(Integer rubro) {
        this.rubro = rubro;
    }

    public String getIdFuenteRecursos() {
        return idFuenteRecursos;
    }

    public void setIdFuenteRecursos(String idFuenteRecursos) {
        this.idFuenteRecursos = idFuenteRecursos;
    }

    public void setIdFuenteFinanciamiento(String idFuenteFinanciamiento) {
        this.idFuenteFinanciamiento = idFuenteFinanciamiento;
    }

    public String getIdFuenteFinanciamiento() {
        return idFuenteFinanciamiento;
    }

    public String getTipoInsumo() {
        return tipoInsumo;
    }

    public void setTipoInsumo(String tipoInsumo) {
        this.tipoInsumo = tipoInsumo;
    }

}
