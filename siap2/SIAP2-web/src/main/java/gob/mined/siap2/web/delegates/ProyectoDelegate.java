/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.delegates;

import gob.mined.siap2.business.ejbs.impl.ProyectoBean;
import gob.mined.siap2.entities.data.impl.POGActividadProyecto;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.data.impl.POLinea;
import gob.mined.siap2.entities.data.impl.Proyecto;
import gob.mined.siap2.entities.data.impl.ProyectoDocumentos;
import gob.mined.siap2.entities.data.impl.ProyectoProrroga;
import java.io.Serializable;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Esta clase implementa un delegate de Proyectos 
 * @author Sofis Solutions
 */
@Named
public class ProyectoDelegate implements Serializable {

    @Inject
    private ProyectoBean bean;

    /**
     * Este método permite guardar o actualizar un proyecto 
     * @param proyecto
     * @param paso
     * @return 
     */
    public Proyecto crearOActualizarProyecto(Proyecto proyecto, Integer paso) {
        return bean.crearOActualizarProyecto(proyecto, paso);
    }
    
        
    /**
     * Este método encargado de guardar una prorroga a un proyecto
     * 
     * @param idProyecto
     * @param prorroga
     * @return 
     */
    public Proyecto guardarProrroga(Integer idProyecto, ProyectoProrroga prorroga) {
        return bean.guardarProrroga(idProyecto, prorroga);
    }

    /**
     * Este método permite obtener un proyecto a partir de su id.
     * @param id
     * @return 
     */
    public Proyecto getProyecto(Integer id) {
        return bean.getProyecto(id);
    }

    /**
     * Este método permite eliminar un proyecto según su id
     * @param idProyecto 
     */
    public void eleiminarProyecto(Integer idProyecto) {
        bean.eleiminarProyecto(idProyecto);
    }

    /**
     * Este método permite obtener un Proyecto para la elaboración o edición de un POG.
     * @param id
     * @return 
     */
    public Proyecto getProyectoForPOG(Integer id) {
        return bean.getProyectoForPOG(id);
    }

    /**
     * Este método permite actualizar una línea del POG.
     * @param idP
     * @param lineaAGuardar 
     */
    public void actualizarLineaPOG(Integer idP, POLinea lineaAGuardar) {
        bean.actualizarLineaPOG(idP, lineaAGuardar);
    }

    /**
     * Este método permite cerrar una ficha de proyecto en planificación.
     * @param idProyecto 
     */
    public void cerrarProyecto(Integer idProyecto) {
        bean.cerrarProyecto(idProyecto);
    }

    /**
     * Este método permite crear o actualizar una actividad de un POG.
     * @param idP
     * @param idLinea
     * @param actividadAGuardar 
     */
    public void crearActualizarActividadPOG(Integer idP, Integer idLinea, POGActividadProyecto actividadAGuardar) {
        bean.crearActualizarActividadPOG(idP, idLinea, actividadAGuardar);
    }

    /**
     * Este método permite guardar una distribución por años de insumos delPOG.
     * @param idP
     * @param idLinea
     * @param actividadAGuardar 
     */
    public void guardarDistribucionAniosInsumosPOG(Integer idP, Integer idLinea, POGActividadProyecto actividadAGuardar) {
        bean.guardarDistribucionAniosInsumosPOG(idP, idLinea, actividadAGuardar);
    }

    /**
     * Este método permite crear o actualizar los insumos del POG.
     * @param idP
     * @param idLineaPO
     * @param idActividadPO
     * @param insumoAguardar 
     */
    public void crearActualizarInsumoPOG(Integer idP, Integer idLineaPO, Integer idActividadPO, POInsumos insumoAguardar) {
        bean.crearActualizarInsumoPOG(idP, idLineaPO, idActividadPO, insumoAguardar);
    }

    /**
     * Este método genera los POA de un proyecto.
     * @param idProyecto 
     */
    public void generarPOAsPAraProyevo(Integer idProyecto) {
        bean.generarPOAsPAraProyevoAnio1(idProyecto);
    }

    /**
     * Este método realiza la consolidación de un POG.
     * @param idProyecto 
     */
    public void consolidarPOG(Integer idProyecto) {
        bean.consolidarPOG(idProyecto);
    }
    
    /**
     * Este método que se encarga de guardar un documento en un proyecto
     * 
     * @param idProyecto
     * @param pd
     * @return 
     */
    public ProyectoDocumentos guardarDocumento(Integer idProyecto, ProyectoDocumentos pd) {
        return bean.guardarDocumento(idProyecto, pd);
    }
    
    

}
