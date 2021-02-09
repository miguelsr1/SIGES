/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.delegates.impl;

import gob.mined.siap2.business.ejbs.impl.ProgramaBean;
import gob.mined.siap2.entities.data.impl.Programa;
import gob.mined.siap2.entities.data.impl.ProgramaInstitucional;
import gob.mined.siap2.entities.data.impl.ProgramaPresupuestario;
import gob.mined.siap2.filtros.FiltroPrograma;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Sofis Solutions
 */
@Named
public class ProgramaDelegate implements Serializable {

    @Inject
    private ProgramaBean bean;

    public void eleiminarProgramaInstitucional(Integer idPrograma) {
        bean.eleiminarProgramaInstitucional(idPrograma);
    }

    public void crearActualizarProgramaInstitucional(ProgramaInstitucional programa, Integer idPlanificacion) {
        bean.crearActualizarProgramaInstitucional(programa, idPlanificacion);
    }

    public void eleiminarProgramaPresupuestario(Integer idPrograma ){
        bean.eleiminarProgramaPresupuestario(idPrograma);
    }    
    
    public void crearActualizarProgramaPresupuestario(ProgramaPresupuestario p, Integer idPlanificacion){
         bean.crearActualizarProgramaPresupuestario(p, idPlanificacion);
    }
    
    public List<ProgramaPresupuestario> getProgramasPresupuestariosParaCargaDeTechos(Integer idPlanificacionEstrategica){
        return bean.getProgramasPresupuestariosParaCargaDeTechos(idPlanificacionEstrategica );
    }
    
    public List<Programa> obtenerProgramasPorFiltro(FiltroPrograma filtro) {
       return bean.obtenerPorFiltro(filtro);
    }
    
    public Set<String> guardarTechos(List<ProgramaPresupuestario> programas) {
        return bean.guardarTechos(programas);
    }
    
    public boolean eliminableProgramaPresupuestario(Integer id){
        return bean.eliminableProgramaPresupuestario(id);
    }

}
