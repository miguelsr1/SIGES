/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business;

import gob.mined.siap2.business.ejbs.impl.GeneralBean;
import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.RelTechoPresupuestarioFR;
import gob.mined.siap2.entities.data.impl.TechoPresupuestarioGOES;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Esta clase procesa los techos presupuestales
 *
 * @author Sofis Solutions
 */
@Stateless(name = "TechoPresupuestarioGOESBean")
@LocalBean
public class TechoPresupuestarioGOESBean {

    @Inject
    @JPADAO
    private GeneralDAO generalDAO;

    @Inject
    private GeneralBean generalBean;

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    /**
     * Este método permite determinar si un año fiscal está repetido.
     *
     * @param id
     * @param anioFiscal
     */
    private void chequearAnioFiscalRepetido(Integer id, Integer anioFiscal) {
        try {
            List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();

            MatchCriteriaTO igualAnio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "anioFiscal", anioFiscal);
            criterios.add(igualAnio);

            //que no sea el            
            if (id != null) {
                MatchCriteriaTO distintoId = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_EQUALS, "id", id);
                criterios.add(distintoId);
            }

            CriteriaTO condicion;
            if (criterios.size() == 1) {
                condicion = criterios.get(0);
            } else {
                condicion = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[0]));
            }

            Long resultado = generalDAO.countByCriteria(TechoPresupuestarioGOES.class, condicion, null, null);
            if (resultado > 0) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_YA_EXISTE_ANIO_FISCAL_PARA_TECHO_PRESUPUESTARIO);
                throw b;
            }
        } catch (GeneralException be) {
            throw be;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método crea el techo presupuestario.
     *
     * @param techo
     */
    public void crearOActualizarTechoPresupuestarioGOES(TechoPresupuestarioGOES techo) {
        try {
            chequearAnioFiscalRepetido(techo.getId(), techo.getAnioFiscal());
            generalDAO.update(techo);
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
     * Este método devuelve un techo presupuestario según su id.
     *
     * @param id
     * @return
     */
    public TechoPresupuestarioGOES getTechoPresupuestario(Integer id) {
        TechoPresupuestarioGOES t = (TechoPresupuestarioGOES) generalDAO.find(TechoPresupuestarioGOES.class, id);
        for (RelTechoPresupuestarioFR u : t.getTechosPresupuestariosFuente()) {
            u.getTechoPresupuestarioUT().size();
        }
        return t;
    }

    /**
     * Este método permite eliminar un techo presupuestario según su id.
     *
     * @param id
     */
    public void eleiminarTechoPresupuestarioGOES(Integer id) {
        try {
            TechoPresupuestarioGOES t = (TechoPresupuestarioGOES) generalDAO.find(TechoPresupuestarioGOES.class, id);
            generalDAO.delete(t);
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception be) {
            logger.log(Level.SEVERE, null, be);
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_EXISTE_ENTIDAD_ASOCIADA_SIN_PARAMETROS);
            throw b;
        }
    }

}
