/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.delegates.impl;

import gob.mined.siap2.business.ejbs.impl.IndicadoresBean;
import gob.mined.siap2.entities.data.impl.Indicador;
import gob.mined.siap2.filtros.FiltroIndicadorPrograma;
import java.io.Serializable;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Esta clase implementa un delegate de indicadores
 * @author Sofis Solutions
 */
@Named
public class IndicadorDelegate implements Serializable {
    @Inject
    private IndicadoresBean bean;
    
    /**
     * Este método permite crear o actualizar un indicador.
     * @param indicador
     * @param idCategoria
     * @param idFormaMedicion
     * @param idUnidadMedida 
     */
    public void crearActualizarIndicador(Indicador indicador, Integer idCategoria, Integer idFormaMedicion, Integer idUnidadMedida){
        bean.crearActualizarIndicador(indicador, idCategoria, idFormaMedicion, idUnidadMedida);
    }
    
    /**
     * Este método permite eliminar un indicador según su id.
     * @param id 
     */
    public void eliminarIndicador(Integer id){
        bean.eliminarIndicador(id);
    }
     /**
      * Obtiene una lista de Indicadores de acuerdo a un programa
      * @param query
      * @param programa
      * @return 
      */   
    public List<Indicador> getIndicadorByNombreAndPrograma(FiltroIndicadorPrograma filtro) {
       return bean.getIndicadorByNombreAndPrograma(filtro);
    }

   
}
