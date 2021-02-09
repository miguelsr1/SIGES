package gob.mined.siap2.business.ejbs.impl;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.impl.Beneficiarios;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import gob.mined.siap2.persistence.dao.imp.SgBeneficiariosDAO;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;

@LocalBean
@Stateless(name = "sgBeneficiariosBean")
public class SgBeneficiariosBean {

    private static final Logger logger = Logger.getLogger(SgBeneficiariosBean.class.getName());

    @Inject
    @JPADAO
    private GeneralDAO generalDAO;

    @Inject
    @JPADAO
    private SgBeneficiariosDAO sgBeneficiariosDAO;

    /**
     * Este método crea o actualiza una seleccion de beneficiario
     *
     * @param objeto
     */
    public void crearActualizar(Beneficiarios objeto) {
        try {
            generalDAO.update(objeto);
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
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
     * Método que se encarga de eliminar una seleccion de Beneficiario
     *
     * @param id Identificador de registro a eliminar
     */
    public void eliminar(Integer id) {
        try {
            Beneficiarios ai = (Beneficiarios) generalDAO.find(Beneficiarios.class, id);
            if (ai != null) {
                generalDAO.delete(ai);
            }
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
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

    /**
     * Este método crea o actualiza una seleccion de beneficiario
     *
     * @param listaCreacion
     * @param listaEliminacion
     * @return
     */
    public void actualizarCollecciones(List<Beneficiarios> listaCreacion, List<Beneficiarios> listaEliminacion) throws DAOGeneralException {
        try {
            if (listaCreacion != null && !listaCreacion.isEmpty()) {
                for (Beneficiarios entity : listaCreacion) {
                    if (entity.getId() == null) {
                        generalDAO.getEntityManager().persist(entity);
                    } else {
                        generalDAO.getEntityManager().merge(entity);
                    }
                }
            }
            if (listaEliminacion != null && !listaEliminacion.isEmpty()) {
                for (Beneficiarios entity : listaEliminacion) {
                    Beneficiarios existe = getBeneficiariosById(entity.getId());
                    if (existe != null) {
                        generalDAO.delete(existe);
                    }
                }
            }
        } catch (DAOGeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método devuelve una seleccion de beneficiario filtrada por Id
     *
     * @param gesId
     * @return
     */
    public Beneficiarios getBeneficiariosById(Integer gesId) {
        return generalDAO.getEntityManager().find(Beneficiarios.class, gesId);
    }

    /**
     * Este método devuelve todos los registros de Beneficiarios
     *
     * @return
     */
    public List<Beneficiarios> getBeneficiarios() {
        try {
            List<Beneficiarios> listaRegistros = sgBeneficiariosDAO.getBeneficiarios();
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

    /**
     * Este método devuelve todos los registros de beneficiarios que partenezcan
     * a un mismo componente
     *
     * @param idPresAnio
     * @return
     */
    public List<Beneficiarios> getBeneficiariosPorPresEsAnioFiscal(Integer idPresAnio) {
        try {
            return sgBeneficiariosDAO.getBeneficiariosPorPresEsAnioFiscal(idPresAnio);
        } catch (NoResultException nre) {
            logger.log(Level.SEVERE, null, nre);
            return null;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
}
