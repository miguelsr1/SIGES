
package gob.mined.siap2.business.ejbs.impl;
import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siges.entities.data.impl.SgOrganizacionCurricular;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import gob.mined.siap2.persistence.dao.imp.SgOrganizacionesCurricularDAO;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless(name = "sgOrganizacionesCurricularBean")
@LocalBean
public class SgOrganizacionesCurricularBean {
    
    private static final Logger logger = Logger.getLogger(SgOrganizacionesCurricularBean.class.getName());
    
    @Inject
    @JPADAO        
    private SgOrganizacionesCurricularDAO sgOrCurDAO;
    
    @Inject
    @JPADAO
    private GeneralDAO generalDAO; 
    
    /**
     * Este metodo devuelve un registro SgOrganizacionCurricular, filtrado por ID
     * @param id
     * @return 
     */
    public SgOrganizacionCurricular getSgOrganizacionesCurricularById(Integer id) {
        return generalDAO.getEntityManager().find(SgOrganizacionCurricular.class, id);
    }

    /**
     * Este metodo devuelve el listado completo de SgOrganizacionCurricular
     * @return 
     */
    public List<SgOrganizacionCurricular> getSgOrganizacionesCurriculars() {
        try {
            return generalDAO.findAll(SgOrganizacionCurricular.class);
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
        
    /**
     * Este metodo devuelve todos los registros de SgOrganizacionCurricular, que se encuentren habilitados
     * @return 
     */
    public List<SgOrganizacionCurricular> getSgOrganizacionesCurricularHabilitados() {
        try {
            List<SgOrganizacionCurricular> listaRegistros = sgOrCurDAO.getSgOrganizacionesHabilitados();
            listaRegistros.isEmpty();
            return listaRegistros;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
}
