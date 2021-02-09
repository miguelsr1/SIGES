/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.ejbs.impl;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.data.daos.IndicadorDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.Categoria;
import gob.mined.siap2.entities.data.impl.Indicador;
import gob.mined.siap2.entities.data.impl.IndicadorFormaMedicion;
import gob.mined.siap2.entities.data.impl.ProgramaIndicador;
import gob.mined.siap2.entities.data.impl.UnidadDeMedida;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.filtros.FiltroIndicador;
import gob.mined.siap2.filtros.FiltroIndicadorPrograma;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import java.util.ArrayList;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Este método implementa los métodos correspondientes a la gestión de indicadores.
 * @author Sofis Solutions
 */
@Stateless(name = "IndicadoresBean")
@LocalBean
public class IndicadoresBean {

    @Inject
    @JPADAO
    private GeneralDAO generalDAO;
    
    @Inject
    private IndicadorDAO indicadorDAO;
    
    @Inject
    private GeneralBean generalBean;

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    /**
     * Este método crea o actualiza un indicador.
     * @param indicador
     * @param idCategoria
     * @param idFormaMedicion
     * @param idUnidadMedida 
     */
    public void crearActualizarIndicador(Indicador indicador, Integer idCategoria, Integer idFormaMedicion, Integer idUnidadMedida) {
        try {
            /*cheuqea no exista igual nombre */
            generalBean.chequearIgualNombre(Indicador.class, indicador.getId(), indicador.getNombre());

            if (indicador.getFinToleranciaVerde().compareTo(indicador.getFinToleranciaAmarillo()) >= 0) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_EN_INDICADOR_FIN_DE_TOLERANCIA_VERDE_DEBE_SER_MENOR_A_FIN_TOLERANCIA_AMARILLO);
                throw b;
            }
            /**
             * que no se superpongan los colores *
             */
 

            indicador.setTipo((Categoria) generalDAO.find(Categoria.class, idCategoria));
            indicador.setUnidadDeMedida((UnidadDeMedida) generalDAO.find(UnidadDeMedida.class, idUnidadMedida));

            indicador.setFormaMedicion((IndicadorFormaMedicion) generalDAO.find(IndicadorFormaMedicion.class, idFormaMedicion));

            generalDAO.update(indicador);
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
     * Este método elimina un indicador
     * @param id 
     */
    public void eliminarIndicador(Integer id) {
        try {
            Indicador i = (Indicador) generalDAO.findById(Indicador.class, id);

            /**
             * chequear que no exista entidad asociada *
             */
            if (i.getProgramasIndicador().size() > 0) {
                BusinessException b = new BusinessException();
                String[] params = {"programa", "al Indicador"};
                b.addError(ConstantesErrores.ERR_EXISTE_ENTIDAD_ASOCIADA, params);
                throw b;
            }
            generalDAO.delete(i);
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_EXISTE_ENTIDAD_ASOCIADA_SIN_PARAMETROS);
            throw b;
        }
    }
    /**
     * Obtiene una lista de indicadores de acuerdo a un criterio de búsqueda por texto y a un programa
     * @param query
     * @param programa
     * @return 
     */
    public List<Indicador> getIndicadorByNombreAndPrograma(FiltroIndicadorPrograma filtro) {
        List<Indicador> resultado = new LinkedList<>();
        List<ProgramaIndicador> listado = obtenerProgramasIndicadoresPorFiltro(filtro);
        for(ProgramaIndicador pi:listado) {
            resultado.add(pi.getIndicador());
        }
        return resultado;
    }
    
    public List<Indicador> obtenerIndicadoresPorFiltro(FiltroIndicador filtro) {
        try {
            List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();

            if (filtro.getCodigo() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "codigo", filtro.getCodigo());
                criterios.add(criterio);
            }

            if (filtro.getId() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "id", filtro.getId());
                criterios.add(criterio);
            }

            if (filtro.getNombre() != null && !filtro.getNombre().isEmpty()) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "nombreBusqueda", filtro.getNombre().trim().toLowerCase());
                criterios.add(criterio);
            }

            if(filtro.getProgramaId() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "id", filtro.getProgramaId());
                criterios.add(criterio);
            }
            
            
            CriteriaTO condicion = null;
            if (criterios.size() == 1) {
                condicion = criterios.get(0);
            } else {
                condicion = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[0]));
            }

            return indicadorDAO.findEntityByCriteria(Indicador.class, condicion, filtro.getOrderBy(), filtro.getAscending(), filtro.getFirst(), filtro.getMaxResults(), filtro.getIncluirCampos());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
  
    public List<ProgramaIndicador> obtenerProgramasIndicadoresPorFiltro(FiltroIndicadorPrograma filtro) {
        try {
            List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();

            if (filtro.getIndicadorCodigo() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "indicador.codigo", filtro.getIndicadorCodigo());
                criterios.add(criterio);
            }

            if (filtro.getIndicadorNombre() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "indicador.nombreBusqueda", filtro.getIndicadorNombre().trim().toLowerCase());
                criterios.add(criterio);
            }
            
            if (filtro.getProgramaCodigo() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "programa.codigo", filtro.getProgramaCodigo());
                criterios.add(criterio);
            }

            if (filtro.getProgramaNombre() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "programa.nombreBusqueda", filtro.getProgramaNombre().trim().toLowerCase());
                criterios.add(criterio);
            }
            
            if (filtro.getId() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "id", filtro.getId());
                criterios.add(criterio);
            }

            if (filtro.getIndicadorId() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "indicador.id", filtro.getIndicadorId());
                criterios.add(criterio);
            }

            if(filtro.getProgramaId() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "programa.id", filtro.getProgramaId());
                criterios.add(criterio);
            }
            
            
            CriteriaTO condicion = null;
            if (criterios.size() == 1) {
                condicion = criterios.get(0);
            } else {
                condicion = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[0]));
            }

            return generalDAO.findEntityByCriteria(ProgramaIndicador.class, condicion, filtro.getOrderBy(), filtro.getAscending(), filtro.getFirst(), filtro.getMaxResults(), filtro.getIncluirCampos());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
}
