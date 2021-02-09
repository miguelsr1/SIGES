/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.delegates.impl;

import gob.mined.siap2.business.ejbs.impl.PlanificacionEstrategicaBean;
import gob.mined.siap2.entities.data.impl.LineaEstrategica;
import gob.mined.siap2.entities.data.impl.PlanificacionEstrategica;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Sofis Solutions
 */
@Named
public class PlanificacionEstrategicaDelegate implements Serializable {
    @Inject
    private PlanificacionEstrategicaBean bean;
    
    public void crearActualizarPlanificacion(PlanificacionEstrategica p, Set<LineaEstrategica> toAdd){
       bean.crearActualizarPlanificacion(p, toAdd);
    }
    
    public void crearActualizarTechos(PlanificacionEstrategica p) {
        bean.crearActualizarTechos(p);
    }
    
    public List<LineaEstrategica> getLineas(Integer idPlanificacion){
        return bean.getLineas(idPlanificacion);
    }
   
    public void eliminarPlanificacion(Integer idPlanificaion){
        bean.eliminarPlanificacion(idPlanificaion);
    }
     
    public void chequearEliminableLinea(Integer idPlanificacion, Integer idLinea){
        bean.chequearEliminableLinea(idPlanificacion, idLinea);
    }
    
    public List<HashMap> obtenerPlaLineasMetasIndicadores(Integer idPlanificacion){
        return bean.obtenerPlaLineasMetasIndicadores(idPlanificacion);
    }
}
