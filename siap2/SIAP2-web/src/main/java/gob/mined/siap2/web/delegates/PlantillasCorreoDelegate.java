/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.delegates;

import gob.mined.siap2.business.ejbs.PlantillasCorreoBean;
import gob.mined.siap2.entities.data.impl.SsPlantillasCorreo;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import java.io.Serializable;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Esta clase implementa un delegate para la plantilla de correo.
 * @author Sofis Solutions
 */
@Named
public class PlantillasCorreoDelegate implements Serializable {
    
    @Inject
    private PlantillasCorreoBean cnfBean;
    
    /**
     * Este método permite guardar una plantilla de correo.
     * @param cnf
     * @return
     * @throws GeneralException 
     */
     public SsPlantillasCorreo guardar(SsPlantillasCorreo cnf) throws GeneralException  {
         return cnfBean.guardar(cnf);
     }
    
     /**
      * Este método permite obtener una plantilla de correo a partir de su id.
      * @param id
      * @return 
      */
     public SsPlantillasCorreo obtenerCnfPorId(Integer id)   {
         return cnfBean.obtenerCnfPorId(id);
     }
     
     /**
      * Este método permite obtener una plantilla de correo a partir de su código.
      * @param codigo
      * @return
      * @throws GeneralException 
      */
     public SsPlantillasCorreo obtenerCnfPorCodigo(String codigo) throws GeneralException  {
         return cnfBean.obtenerCnfPorCodigo(codigo);
     }
     
     /**
      * Este método permite obtener todas las plantillas de correo.
      * @return
      * @throws GeneralException 
      */
     public List<SsPlantillasCorreo> obtenerTodos() throws GeneralException {
         return cnfBean.obtenerTodos();
     }
     
     /**
      * Este método permite obtener plantillas de correo por criteria.
      * @param cto
      * @param orderBy
      * @param ascending
      * @param startPosition
      * @param cantidad
      * @return
      * @throws GeneralException 
      */
     public List<SsPlantillasCorreo> obtenerPorCriteria(CriteriaTO cto,String[] orderBy, boolean[] ascending,Long startPosition, Long cantidad) throws GeneralException {
         return cnfBean.obtenerPorCriteria(cto, orderBy, ascending, startPosition, cantidad);
     }
}
