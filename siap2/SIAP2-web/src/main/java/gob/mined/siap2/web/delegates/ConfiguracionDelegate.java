/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.delegates;

import gob.mined.siap2.business.ejbs.ConfiguracionBean;
import gob.mined.siap2.entities.data.Configuracion;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import java.io.Serializable;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Esta clase implementa el delegate del EJB ConfiguracionBean
 * @author Sofis Solutions
 */
@Named
public class ConfiguracionDelegate implements Serializable {
    
    @Inject
    private ConfiguracionBean cnfBean;
    
    /**
     * Este método guarda un elemento de tipo Configuración
     * @param cnf
     * @return
     * @throws GeneralException 
     */
     public Configuracion guardar(Configuracion cnf) throws GeneralException  {
         return cnfBean.guardar(cnf);
     }
    
     /**
      * Este método permite obtener un elemento de tipo configuración a partir de su id.
      * @param id
      * @return 
      */
     public Configuracion obtenerCnfPorId(Integer id)   {
         return cnfBean.obtenerCnfPorId(id);
     }
     
     /**
      * Este método permite obtener un elemento de tipo configuración a partir de su código.
      * @param codigo
      * @return
      * @throws GeneralException 
      */
     public Configuracion obtenerCnfPorCodigo(String codigo) throws GeneralException  {
         return cnfBean.obtenerCnfPorCodigo(codigo);
     }
     
     /**
      * Este método devuelve todos los objetos de tipo configuración.
      * @return
      * @throws GeneralException 
      */
     public List<Configuracion> obtenerTodos() throws GeneralException {
         return cnfBean.obtenerTodos();
     }
     
     /**
      * Este método permite obtener los objetos de tipo configuración según un criterio o filtro.
      * @param cto
      * @param orderBy
      * @param ascending
      * @param startPosition
      * @param cantidad
      * @return
      * @throws GeneralException 
      */
     public List<Configuracion> obtenerPorCriteria(CriteriaTO cto,String[] orderBy, boolean[] ascending,Long startPosition, Long cantidad) throws GeneralException {
         return cnfBean.obtenerPorCriteria(cto, orderBy, ascending, startPosition, cantidad);
     }
}
