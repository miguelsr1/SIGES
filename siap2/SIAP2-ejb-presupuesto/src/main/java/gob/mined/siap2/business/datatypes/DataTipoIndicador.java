/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatypes;

import gob.mined.siap2.entities.data.impl.Categoria;
import java.util.List;

/**
 *
 * @author Sofis Solutions
 */
public class DataTipoIndicador {
    
    private Categoria tipoIndicador;
    private List<DataMetaIndicador> metas;

    public Categoria getTipoIndicador() {
        return tipoIndicador;
    }

    public void setTipoIndicador(Categoria tipoIndicador) {
        this.tipoIndicador = tipoIndicador;
    }

    public List<DataMetaIndicador> getMetas() {
        return metas;
    }

    public void setMetas(List<DataMetaIndicador> metas) {
        this.metas = metas;
    }
    
    
    
    
    
    
    
}
