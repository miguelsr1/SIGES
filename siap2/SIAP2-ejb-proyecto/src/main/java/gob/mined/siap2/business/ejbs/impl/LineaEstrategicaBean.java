/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.ejbs.impl;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.LineaEstrategica;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import gob.mined.siap2.persistence.dao.imp.LineaEstrategicaDAO;
import gob.mined.siap2.persistence.dao.imp.PlanificacionEstrategicaDAO;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Esta clase implementa los métodos correspondientes a los servicios de líneas estratégicas
 * @author Sofis Solutions
 */
@Stateless(name = "LineaEstrategicaBean")
@LocalBean
public class LineaEstrategicaBean {
    
    @Inject @JPADAO
    private GeneralDAO generalDAO;
    
    @Inject @JPADAO
    private LineaEstrategicaDAO lineaEstrategicaDAO;    
    @Inject @JPADAO
    private PlanificacionEstrategicaDAO planificacionEstrategicaDAO;
          
    @Inject
    private GeneralBean peleBean;
      
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    /**
     * Este método crea o actualiza una línea estratégica
     * @param l 
     */
    public void crearEditarLineaEstrategica(LineaEstrategica l){
        try {             
            peleBean.chequearIgualNombre(LineaEstrategica.class, l.getId(), l.getNombre());     
            generalDAO.update(l);
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        }catch (DAOObjetoNoEditableException e){
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;        
        }catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    
    
    /**
     * Este método elimina una línea estratégica según su id
     * @param idLinea 
     */ 
    public void eliminarLinea(Integer idLinea){
        try{
            if (!planificacionEstrategicaDAO.getPlanificacionesConLinea(idLinea).isEmpty()){
                BusinessException b = new BusinessException();
                String[] values = {"una planificación","la línea"};
                b.addError(ConstantesErrores.ERR_EXISTE_ENTIDAD_ASOCIADA,values);
                throw b;
            }       
            LineaEstrategica l = (LineaEstrategica) generalDAO.findById(LineaEstrategica.class, idLinea);
            generalDAO.delete(l);
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        }catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_EXISTE_ENTIDAD_ASOCIADA_SIN_PARAMETROS);
            throw b;
        }
    }
   
   
    
}
