package gob.mined.siap2.business.ejbs.impl;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.impl.AnioLectivo;
import gob.mined.siges.entities.data.impl.SgPresupuestoEscolar;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import gob.mined.siap2.persistence.dao.imp.AnioLectivoDAO;
import gob.mined.siap2.persistence.dao.imp.PresupuestoEscolarDAO;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;

@LocalBean
@Stateless(name = "PresupuestoEscolarBean")
public class PresupuestoEscolarBean {
    
    private static final Logger logger = Logger.getLogger(PresupuestoEscolarBean.class.getName());
    
    @Inject
    @JPADAO
    private GeneralDAO generalDAO;
    
    @Inject
    @JPADAO
    private PresupuestoEscolarDAO presupuestoEscolarDAO;
    
    @Inject
    @JPADAO
    private AnioLectivoDAO anioLectivoDAO;
    
    
    /**
     * Este método crea o actualiza un registro de PresupuestoEscolar
     * @param pe
     */
    public void crearActualizar(SgPresupuestoEscolar pe) {
        try {
            generalDAO.update(pe);
        } catch (BusinessException be) {
            throw be;
        } catch (DAOGeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
     /**
     * Este método busca un registro de presupuestoEscolar filtrado por anio y sede
     * @param anio
     * @param idSede
     * @return 
     */
    public SgPresupuestoEscolar getPresupuestoEscolarByAnioYSede(Integer anio, Integer idSede) {
        try {
            return presupuestoEscolarDAO.getPresupuestoEscolarByAnioYSede(anio, idSede);
        } catch (BusinessException be) {
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch(NoResultException rex){
            logger.log(Level.SEVERE, "NO SE ENCONTRO UN PRESUPUESTO POR ANIO Y SEDE");
            return null;
        }catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    
    
    /**
     * Este método busca un registro de anioLectivo filtrado por el anio
     * @param anio
     * @return 
     */
    public AnioLectivo getAnioLectivoByAnio(Integer anio) {
        try {
            return anioLectivoDAO.getAnioLectivoByAnio(anio);
        } catch (BusinessException be) {
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }

    }
}
