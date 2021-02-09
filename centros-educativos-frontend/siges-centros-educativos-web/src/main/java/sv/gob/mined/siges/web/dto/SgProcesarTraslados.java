/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;

@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, scope = SgProcesarTraslados.class)
public class SgProcesarTraslados implements Serializable {
    
    private SgMatricula matriculaActual;
    
    private SgMatricula matriculaNueva;
    
    private SgSolicitudTraslado solicitud;

    
    public SgProcesarTraslados() {
    }

    public SgMatricula getMatriculaActual() {
        return matriculaActual;
    }

    public void setMatriculaActual(SgMatricula matriculaActual) {
        this.matriculaActual = matriculaActual;
    }

    public SgMatricula getMatriculaNueva() {
        return matriculaNueva;
    }

    public void setMatriculaNueva(SgMatricula matriculaNueva) {
        this.matriculaNueva = matriculaNueva;
    }

    public SgSolicitudTraslado getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(SgSolicitudTraslado solicitud) {
        this.solicitud = solicitud;
    }
    
    
    
}
