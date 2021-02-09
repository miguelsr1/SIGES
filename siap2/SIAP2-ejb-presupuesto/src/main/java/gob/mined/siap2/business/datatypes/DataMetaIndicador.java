/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatypes;

import gob.mined.siap2.entities.data.impl.Indicador;
import gob.mined.siap2.entities.data.impl.MetaIndicador;
import gob.mined.siap2.entities.data.impl.Proyecto;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;

/**
 *
 * @author Sofis Solutions
 */
public class DataMetaIndicador {
    
    private Proyecto proyecto;
    private UnidadTecnica ut;
    private MetaIndicador metaIndicador;
    private Indicador indicador;

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public UnidadTecnica getUt() {
        return ut;
    }

    public void setUt(UnidadTecnica ut) {
        this.ut = ut;
    }

    public MetaIndicador getMetaIndicador() {
        return metaIndicador;
    }

    public void setMetaIndicador(MetaIndicador metaIndicador) {
        this.metaIndicador = metaIndicador;
    }

    public Indicador getIndicador() {
        return indicador;
    }

    public void setIndicador(Indicador indicador) {
        this.indicador = indicador;
    }



    
    
    
    
    
}
