/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.caux;

import java.util.Objects;
import sv.gob.mined.siges.web.dto.SgAplicacionPlaza;
import sv.gob.mined.siges.web.dto.SgEspecialidadesPersonalAlAplicar;


public class SgMatrizComparativa {

    private SgAplicacionPlaza aplicacion;
    private SgEspecialidadesPersonalAlAplicar especialidades;
    private Boolean mostrarBotonDocente;
    private Boolean seleccionadoDocente;
    private Boolean seleccionadoOtraPlaza;
    
    public SgMatrizComparativa() {
    }

    public SgAplicacionPlaza getAplicacion() {
        return aplicacion;
    }

    public void setAplicacion(SgAplicacionPlaza aplicacion) {
        this.aplicacion = aplicacion;
    }

    public SgEspecialidadesPersonalAlAplicar getEspecialidades() {
        return especialidades;
    }

    public void setEspecialidades(SgEspecialidadesPersonalAlAplicar especialidades) {
        this.especialidades = especialidades;
    }

   
    public Boolean getMostrarBotonDocente() {
        return mostrarBotonDocente;
    }

    public void setMostrarBotonDocente(Boolean mostrarBotonDocente) {
        this.mostrarBotonDocente = mostrarBotonDocente;
    }

    public Boolean getSeleccionadoDocente() {
        return seleccionadoDocente;
    }

    public void setSeleccionadoDocente(Boolean seleccionadoDocente) {
        this.seleccionadoDocente = seleccionadoDocente;
    }

    public Boolean getSeleccionadoOtraPlaza() {
        return seleccionadoOtraPlaza;
    }

    public void setSeleccionadoOtraPlaza(Boolean seleccionadoOtraPlaza) {
        this.seleccionadoOtraPlaza = seleccionadoOtraPlaza;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.aplicacion);
        hash = 97 * hash + Objects.hashCode(this.especialidades);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SgMatrizComparativa other = (SgMatrizComparativa) obj;
        if (!Objects.equals(this.aplicacion, other.aplicacion)) {
            return false;
        }
        if (!Objects.equals(this.especialidades, other.especialidades)) {
            return false;
        }
        return true;
    }
    
    
}
