
package gob.mined.siap2.business.ejbs.impl;
import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siges.entities.data.impl.SgClasificacion;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.persistence.dao.imp.SgCatalogosSigesDAO;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
@LocalBean
public class SgCatalogosBean {
    
    private static final Logger logger = Logger.getLogger(SgCatalogosBean.class.getName());
    
    @Inject
    @JPADAO        
    private SgCatalogosSigesDAO sgCatalogosDAO;
    
    @Inject
    @JPADAO
    private GeneralDAO generalDAO; 
   

    /**
     * Este metodo devuelve el listado completo de Ciclos
     * @return 
     */
    public List<SgClasificacion> getClasificaciones() {
        try {
            List<SgClasificacion> listaRegistros = generalDAO.findAll(SgClasificacion.class);
            return listaRegistros;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
        
    /**
     * Este metodo devuelve todos los registros de Ciclos, que se encuentren habilitados
     * @return 
     */
    public List<SgClasificacion> getClasificacionesHabilitadas() {
        try {
            return sgCatalogosDAO.findClasificacionesHabilitadas();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    
    
  
}
