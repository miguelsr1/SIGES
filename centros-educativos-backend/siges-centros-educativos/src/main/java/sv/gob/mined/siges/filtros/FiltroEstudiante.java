/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.util.List;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.enumerados.EnumMatriculaEstado;



public class FiltroEstudiante extends FiltroPersona {

    private Long estPk;
    private Long estPersona;
    private Boolean estTrabaja;
    private Boolean estEmbarazo;
    private Boolean estSinNie;    
    private EnumMatriculaEstado estEstadoMatricula;
    private Boolean habilitado;
    private Long estDepartamentoMatricula;
    private Long estMunicipioMatricula;
    private Long estComponentePlanEstudioPk;
    private List<Long> estudiantesPk;
    private Boolean matAnulada;
    private List<Long> estudiantesNie;
    private Long estadoCivilFk;

   

    private Boolean estEncuestaRealizada;

    
    
    //Auxiliar
    private Boolean incluirResponsableOContactoEmergencia;
    private Boolean buscarSoloEnUltimaMatricula;
    private Boolean incluirDiscapacidades;
    
    
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


    public Boolean getIncluirResponsableOContactoEmergencia() {
        return incluirResponsableOContactoEmergencia;
    }

    public void setIncluirResponsableOContactoEmergencia(Boolean incluirResponsableOContactoEmergencia) {
        this.incluirResponsableOContactoEmergencia = incluirResponsableOContactoEmergencia;
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

    public Long getEstComponentePlanEstudioPk() {
        return estComponentePlanEstudioPk;
    }

    public void setEstComponentePlanEstudioPk(Long estComponentePlanEstudioPk) {
        this.estComponentePlanEstudioPk = estComponentePlanEstudioPk;
    }

    public List<Long> getEstudiantesPk() {
        return estudiantesPk;
    }

    public void setEstudiantesPk(List<Long> estudiantesPk) {
        this.estudiantesPk = estudiantesPk;
    }

    public Boolean getMatAnulada() {
        return matAnulada;
    }

    public void setMatAnulada(Boolean matAnulada) {
        this.matAnulada = matAnulada;
    }

    public List<Long> getEstudiantesNie() {
        return estudiantesNie;
    }

    public void setEstudiantesNie(List<Long> estudiantesNie) {
        this.estudiantesNie = estudiantesNie;
    }

    public Boolean getBuscarSoloEnUltimaMatricula() {
        return buscarSoloEnUltimaMatricula;
    }

    public void setBuscarSoloEnUltimaMatricula(Boolean buscarSoloEnUltimaMatricula) {
        this.buscarSoloEnUltimaMatricula = buscarSoloEnUltimaMatricula;
    }

    public Long getEstadoCivilFk() {
        return estadoCivilFk;
    }

    public void setEstadoCivilFk(Long estadoCivilFk) {
        this.estadoCivilFk = estadoCivilFk;
    }


    public Boolean getEstEncuestaRealizada() {
        return estEncuestaRealizada;
    }

    public void setEstEncuestaRealizada(Boolean estEncuestaRealizada) {
        this.estEncuestaRealizada = estEncuestaRealizada;
    }

    public Boolean getIncluirDiscapacidades() {
        return incluirDiscapacidades;
    }

    public void setIncluirDiscapacidades(Boolean incluirDiscapacidades) {
        this.incluirDiscapacidades = incluirDiscapacidades;
    }
    
    
}
