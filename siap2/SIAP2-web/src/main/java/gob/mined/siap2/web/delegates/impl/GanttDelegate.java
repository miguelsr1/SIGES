/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.delegates.impl;

import gob.mined.siap2.business.ejbs.impl.GanttBean;
import gob.mined.siap2.entities.data.impl.MetodoAdquisicion;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Esta clase implementa un delegate del Diagrama de Gantt utilizado en el proceso de adquisición.
 * @author Sofis Solutions
 */
@Named
public class GanttDelegate implements Serializable {

    @Inject
    private GanttBean bean;

    /**
     * Este método permite obtener un diagrama de Gantt según su id.
     * @param idGantt
     * @return 
     */
    public String getGantt(Integer idGantt) {
        return bean.getGantt(idGantt);
    }

    /**
     * Este método permite regenerar el diagrama de Gantt de un método de adquisición según su fecha de inicio.
     * @param idMetodoAdquisicion
     * @param fechaInicio
     * @return 
     */
    public String regenerarGantt(Integer idMetodoAdquisicion, Date fechaInicio) {
        return bean.regenerarGantt(idMetodoAdquisicion, fechaInicio);
    }

    /**
     * Este método permite obtener los métodos de adquisición que satisfacen un monto dado.
     * @param montoGrupo
     * @param anio
     * @return 
     */
     public List<MetodoAdquisicion> getMetodosCumplenRango(BigDecimal montoGrupo, Integer anio) {
         return bean.getMetodosCumplenRango(montoGrupo, anio);
     }
}
