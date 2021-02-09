/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.delegates.impl;

import gob.mined.siap2.business.AccionCentralBean;
import gob.mined.siap2.entities.data.impl.AccionCentral;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Esta clase implementa el delegate de acción central
 * @author Sofis Solutions
 */
@Named
public class AccionCentralDelegate implements Serializable {

    @Inject
    private AccionCentralBean bean;

    /**
     * Este método permite crear o actualizar una acción central en una determina planificación.
     * @param idPlanificacion
     * @param accion 
     */
    public void crearOActualizarAccionCentral(Integer idPlanificacion, AccionCentral accion) {
        bean.crearOActualizarAccionCentral(idPlanificacion,  accion);
    }

    /**
     * Este método permite eliminar una acción central por id.
     * @param idAccion 
     */
    public void eleiminarAccionCentral(Integer idAccion) {
        bean.eleiminarAccionCentral(idAccion);
    }

 
    /**
     * Este método permite obtener los montos usados por una UT en una planificación.
     * @param idPlanificacion
     * @param anio
     * @param idUnidadTecnica
     * @param idAccionCentralExcluir
     * @return 
     */
    public BigDecimal getMontoUsadoAccionCentralUTPlanificacion(Integer idPlanificacion, Integer anio, Integer idUnidadTecnica, Integer idAccionCentralExcluir) {
        return bean.getMontoUsadoAccionCentralUTPlanificacion( idPlanificacion,  anio,  idUnidadTecnica,  idAccionCentralExcluir);
    }
    
        
    
}
