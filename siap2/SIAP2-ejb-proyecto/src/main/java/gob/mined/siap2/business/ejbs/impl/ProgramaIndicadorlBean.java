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
import gob.mined.siap2.entities.data.impl.AnioIndicador;
import gob.mined.siap2.entities.data.impl.Indicador;
import gob.mined.siap2.entities.data.impl.Programa;
import gob.mined.siap2.entities.data.impl.ProgramaIndicador;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import gob.mined.siap2.sofisform.to.AND_TO;
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
 * Esta clase implementa los métodos que asocian indicadores a programas.
 *
 * @author Sofis Solutions
 */
@Stateless(name = "ProgramaIndicadorlBean")
@LocalBean
public class ProgramaIndicadorlBean {

    @Inject
    @JPADAO
    private GeneralDAO generalDAO;

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    /**
     * Este método crea un par programa indicador o actualiza el mismo.
     *
     * @param idPrograma
     * @param idIndicador
     * @param objeto
     */
    public void crearOActualizarProgramaIndicador(Integer idPrograma, Integer idIndicador, ProgramaIndicador objeto) {
        try { 
            if (objeto.getId() == null) {
                //verifica que este asociando a un elemento de la línea de trabajo
                Programa p = (Programa) generalDAO.find(Programa.class, idPrograma);
                objeto.setPrograma(p);

                Indicador i = (Indicador) generalDAO.find(Indicador.class, idIndicador);
                objeto.setIndicador(i);
            }

            //verifica que ya no exista una asociacion de ese programa con la planificacion
            List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();
            MatchCriteriaTO criterioPorPrograma = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "programa.id", idPrograma);
            criterios.add(criterioPorPrograma);
            MatchCriteriaTO criterioPorIndicador = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "indicador.id", idIndicador);
            criterios.add(criterioPorIndicador);
            if (objeto.getId() != null) {
                MatchCriteriaTO noel = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_EQUALS, "id", objeto.getId());
                criterios.add(noel);
            }

            AND_TO criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[0]));
            List l = generalDAO.findEntityByCriteria(ProgramaIndicador.class, criterio, null, null, null, null);
            if (!l.isEmpty()) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_EXISTE_ASOCIACION_PROGRAMA_INDICADOR);
                throw b;
            }

            generalDAO.update(objeto);
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
     * Este método elimina una asociación entre un programa y un indicador.
     *
     * @param id
     */
    public void eleiminarProgramaIndicador(Integer id) {
        try {
            ProgramaIndicador p = (ProgramaIndicador) generalDAO.find(ProgramaIndicador.class, id);

            for (AnioIndicador a : p.getAnioIndicadors()) {
                if (a.getValoresIndicadores().size() > 0) {
                    String[] params = {"valores", "al Indicador Estratégico"};
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_EXISTE_ENTIDAD_ASOCIADA, params);
                    throw b;
                }
            }
            generalDAO.delete(p);

        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_EXISTE_ENTIDAD_ASOCIADA_SIN_PARAMETROS);
            throw b;
        }
    }

}
