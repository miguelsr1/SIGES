/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import sv.gob.mined.siges.web.enumerados.EnumMatriculaEstado;

public class FiltroEstudiante extends FiltroPersona implements Serializable {

    private Long estPk;
    private Long estPersona;
    private Boolean estTrabaja;
    private Boolean estEmbarazo;
    private Boolean estSinNie;
    private EnumMatriculaEstado estEstadoMatricula;
   
    //Auxiliar
    private Boolean incluirResponsableOContactoEmergencia;

    public FiltroEstudiante() {
        super();
        incluirResponsableOContactoEmergencia = Boolean.FALSE;
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

    public Boolean getIncluirResponsableOContactoEmergencia() {
        return incluirResponsableOContactoEmergencia;
    }

    public void setIncluirResponsableOContactoEmergencia(Boolean incluirResponsableOContactoEmergencia) {
        this.incluirResponsableOContactoEmergencia = incluirResponsableOContactoEmergencia;
    }

}
