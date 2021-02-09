/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.delegates.impl;

import com.mined.siap2.interfaces.BaseCodiguera;
import gob.mined.siap2.business.ejbs.impl.GeneralBean;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.FuenteRecursos;
import gob.mined.siap2.persistence.dao.exceptions.DAOConstraintViolationException;
import java.io.Serializable;
import java.util.Collection;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Esta clase implementa un delegate para métodos generales o genéricos.
 * @author Sofis Solutions
 */
@Named
public class GeneralDelegate implements Serializable {
    @Inject
    private GeneralBean bean;
  
          
    public void chequearIgualNombre (Class clase, Integer id, String nombre){
        bean.chequearIgualNombre(clase, id, nombre);
    }

    /**
     * Este método obtiene la fuente de recursos GOES
     * @return 
     */
    public FuenteRecursos getGOES() {
        return bean.getGOES();
    }
    
    /**
     * Este método permite guardar una codiguera.
     * @param codiguera
     * @return 
     */
     public BaseCodiguera guardarCodiguera( BaseCodiguera codiguera){
         return bean.guardarCodiguera(codiguera);
     }
     /**
     * Este método permite guardar una codiguera.
     * @param codiguera
     * @return 
     */
    /** public BaseCodiguera guardarCodigueraValidacionUnico( BaseCodiguera codiguera) throws DAOConstraintViolationException{
         return bean.guardarCodigueraValidacionUnico(codiguera);
     }**/

     /**
      * Este método permite obtener los años fiscales en ejecución.
      * @return 
      */
    public Collection<AnioFiscal> obtenerAnioFiscalEnEjecucion() {
      return bean.obtenerAnioFiscalEnEjecucion();
    }

}
