/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.enumerados.EnumMatriculaEstado;

public class FiltroEstudiante extends FiltroPersona implements Serializable {

    private Long estPk;
    private Long estPersona;
    private Boolean estTrabaja;
    private Boolean estEmbarazo;
    private Boolean estSinNie;
    private EnumMatriculaEstado estEstadoMatricula;
    private Boolean habilitado;
    private Long estDepartamentoMatricula;
    private Long estMunicipioMatricula;
    private Boolean matAnulada;
    private Boolean buscarSoloEnUltimaMatricula;
    private Boolean cargarNombreEstudiantes;
    private Boolean estEncuestaRealizada;

    public FiltroEstudiante() {
        super();
        this.setSecurityOperation(ConstantesOperaciones.BUSCAR_ESTUDIANTES);
        this.buscarSoloEnUltimaMatricula = Boolean.FALSE;
    }

    public Long getEstPk() {
        return estPk;
    }

    public void setEstPk(Long estPk) {
        this.estPk = estPk;
    }

    public Long getEstPersona() {
        return estPersona;
    }

    public void setEstPersona(Long estPersona) {
        this.estPersona = estPersona;
    }

    public Boolean getEstTrabaja() {
        return estTrabaja;
    }

    public void setEstTrabaja(Boolean estTrabaja) {
        this.estTrabaja = estTrabaja;
    }

    public Boolean getEstEmbarazo() {
        return estEmbarazo;
    }

    public void setEstEmbarazo(Boolean estEmbarazo) {
        this.estEmbarazo = estEmbarazo;
    }

    public Boolean getEstSinNie() {
        return estSinNie;
    }

    public void setEstSinNie(Boolean estSinNie) {
        this.estSinNie = estSinNie;
    }

    public EnumMatriculaEstado getEstEstadoMatricula() {
        return estEstadoMatricula;
    }

    public void setEstEstadoMatricula(EnumMatriculaEstado estEstadoMatricula) {
        this.estEstadoMatricula = estEstadoMatricula;
    }

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
    }

    public Long getEstDepartamentoMatricula() {
        return estDepartamentoMatricula;
    }

    public void setEstDepartamentoMatricula(Long estDepartamentoMatricula) {
        this.estDepartamentoMatricula = estDepartamentoMatricula;
    }

    public Long getEstMunicipioMatricula() {
        return estMunicipioMatricula;
    }

    public void setEstMunicipioMatricula(Long estMunicipioMatricula) {
        this.estMunicipioMatricula = estMunicipioMatricula;
    }

    public Boolean getMatAnulada() {
        return matAnulada;
    }

    public void setMatAnulada(Boolean matAnulada) {
        this.matAnulada = matAnulada;
    }

    public Boolean getBuscarSoloEnUltimaMatricula() {
        return buscarSoloEnUltimaMatricula;
    }

    public void setBuscarSoloEnUltimaMatricula(Boolean buscarSoloEnUltimaMatricula) {
        this.buscarSoloEnUltimaMatricula = buscarSoloEnUltimaMatricula;
    }

    public Boolean getCargarNombreEstudiantes() {
        return cargarNombreEstudiantes;
    }

    public void setCargarNombreEstudiantes(Boolean cargarNombreEstudiantes) {
        this.cargarNombreEstudiantes = cargarNombreEstudiantes;
    }

    public Boolean getEstEncuestaRealizada() {
        return estEncuestaRealizada;
    }

    public void setEstEncuestaRealizada(Boolean estEncuestaRealizada) {
        this.estEncuestaRealizada = estEncuestaRealizada;
    }
    
    

    

}
