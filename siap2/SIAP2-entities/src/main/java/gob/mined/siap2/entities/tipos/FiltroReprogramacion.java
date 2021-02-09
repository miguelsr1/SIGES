/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.tipos;

import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.entities.enums.EstadoReprogramacion;
import gob.mined.siap2.entities.enums.TipoReprogramacion;
import java.io.Serializable;

/**
 *
 * @author Sofis Solutions
 */
public class FiltroReprogramacion implements Serializable {
    private EstadoReprogramacion estado;
    private UnidadTecnica unidadTecnica;
    private TipoReprogramacion tipoReprog;

    public EstadoReprogramacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoReprogramacion estado) {
        this.estado = estado;
    }

    public UnidadTecnica getUnidadTecnica() {
        return unidadTecnica;
    }

    public void setUnidadTecnica(UnidadTecnica unidadTecnica) {
        this.unidadTecnica = unidadTecnica;
    }

    public TipoReprogramacion getTipoReprog() {
        return tipoReprog;
    }

    public void setTipoReprog(TipoReprogramacion tipoReprog) {
        this.tipoReprog = tipoReprog;
    }
    
    
}
