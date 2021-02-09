package gob.mined.siap2.business.ejbs.impl;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.impl.AnioLectivo;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import gob.mined.siap2.persistence.dao.imp.AnioLectivoDAO;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

@LocalBean
@Stateless(name = "AnioLectivoBean")
public class AnioLectivoBean {
    
    private static final Logger logger = Logger.getLogger(AnioLectivoBean.class.getName());
    
    @Inject
    @JPADAO
    private GeneralDAO generalDAO;
    
    @Inject
    @JPADAO
    private AnioLectivoDAO anioLectivoDAO;
    
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
