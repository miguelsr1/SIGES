/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatypes;

import gob.mined.siap2.entities.data.impl.Indicador;
import java.util.List;

/**
 *
 * @author Sofis Solutions
 */
public class DataTipoIndicadorPOA {
    private Indicador indicador;
    private List<DataMetaIndicadorPOA> metas;

    public Indicador getIndicador() {
        return indicador;
    }

    public void setIndicador(Indicador indicador) {
        this.indicador = indicador;
    }

    public List<DataMetaIndicadorPOA> getMetas() {
        return metas;
    }

    public void setMetas(List<DataMetaIndicadorPOA> metas) {
        this.metas = metas;
    }

    
}
