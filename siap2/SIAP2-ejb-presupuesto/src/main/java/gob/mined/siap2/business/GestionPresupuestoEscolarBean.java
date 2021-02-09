/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business;

import gob.mined.siap2.business.validaciones.ComponentePresupuestoValidaciones;
import gob.mined.siap2.persistence.dao.imp.GestionPresupuestoEscolarDAO;
import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.impl.ComponentePresupuestoEscolar;
import gob.mined.siap2.entities.data.impl.ObjetoEspecificoGasto;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.filtros.FiltroComponentePresupuestoEscolar;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import gob.mined.siap2.persistence.dao.imp.POADAO;
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
import javax.persistence.NoResultException;

/**
 * Esta clase incluye los métodos para la gestión de SsGesPresEs
 *
 * @author Sofis Solutions
 */
@Stateless(name = "GestionPresupuestoEscolarBean")
@LocalBean
public class GestionPresupuestoEscolarBean {

    private static final Logger logger = Logger.getLogger(GestionPresupuestoEscolarBean.class.getName());

    @Inject
    @JPADAO
    private GestionPresupuestoEscolarDAO gesPresEsDao;

    @Inject
    @JPADAO
    private GeneralDAO generalDAO;

    @Inject
    @JPADAO
    private POADAO poadao;
    
    
    
    /**
     * Este método crea o actualiza una Gestion de presuspuesto escolar.
     *
     * @param ssGesPresEs
     */
    public void crearActualizarComponente(ComponentePresupuestoEscolar ssGesPresEs) {
        try {
           
            ComponentePresupuestoValidaciones.validar(ssGesPresEs);
            
            if (ssGesPresEs.getId() == null) {
                generalDAO.persist(ssGesPresEs);
            } else {
                generalDAO.merge(ssGesPresEs);
            }

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
     * Método que se encarga de eliminar un componente de gestion de presupeusto
     * escolar actividades
     *
     * @param id Identificador de registro a eliminar
     */
    public void eliminarComponente(Integer id) {
        try {
            BusinessException exception = new BusinessException();
            ComponentePresupuestoEscolar cpe = (ComponentePresupuestoEscolar) generalDAO.find(ComponentePresupuestoEscolar.class, id);
            if(cpe != null){
                generalDAO.delete(cpe);
            }else{
                exception.addError(ConstantesErrores.ERR_REGISTRO_NO_ENCONTRADO);
                throw exception;
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
     * Este método devuelve una Gestion de presuspuesto escolar a partir de su
     * id.
     *
     * @param gesId
     * @return
     */
    public ComponentePresupuestoEscolar getGesPresEsById(Integer gesId) {
        return generalDAO.getEntityManager().find(ComponentePresupuestoEscolar.class, gesId);
    }

    /**
     * Este método devuelve todos los registros de Gestion de presuspuesto
     * escolar.
     *
     * @return
     */
    public List<ComponentePresupuestoEscolar> getGesPresEs() {
        try {
            List<ComponentePresupuestoEscolar> gestiones = gesPresEsDao.getGesPresEs();
            return gestiones;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    /**
     * Este método devuelve todos los registros de Gestion de presuspuesto
     * escolar que estén habilitados.
     *
     * @return
     */
    public List<ComponentePresupuestoEscolar> getGesPresEsHabilitados() {
        try {
            List<ComponentePresupuestoEscolar> gestiones = gesPresEsDao.getGesPresEsHabilitados();
            gestiones.isEmpty();
            return gestiones;
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
     * Este método devuelve todos los registros de Gestion de presuspuesto
     * escolar que estén habilitados.
     *
     * @param idCateogoria
     * @return
     */
    public List<ComponentePresupuestoEscolar> getGesPresEsByCategoria(Integer idCateogoria) {
        try {
            List<ComponentePresupuestoEscolar> gestiones = gesPresEsDao.getGesPresEsByCategoria(idCateogoria);
            gestiones.isEmpty();
            return gestiones;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    public List<ComponentePresupuestoEscolar> getComponentesPresupuestoEscolarByFiltro(FiltroComponentePresupuestoEscolar filtro) {
        try {
            List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();

            if (filtro.getCategoriaComponenteId() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "categoriaPresupuestoEscolar.id", filtro.getCategoriaComponenteId());
                criterios.add(criterio);
            }

            if (filtro.getHabilitado() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "habilitado", filtro.getHabilitado());
                criterios.add(criterio);
            }

            if (filtro.getTipoSubcomponente() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tipo", filtro.getTipoSubcomponente());
                criterios.add(criterio);
            }
            if (filtro.getNombre() != null && !filtro.getNombre().isEmpty()) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "nombreBusqueda", filtro.getNombre().trim().toLowerCase());
                criterios.add(criterio);
            }
            CriteriaTO condicion = null;
            if (criterios.size() == 1) {
                condicion = criterios.get(0);
            } else {
                condicion = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[0]));
            }

            return generalDAO.findEntityByCriteria(ComponentePresupuestoEscolar.class, condicion, filtro.getOrderBy(), filtro.getAscending(), filtro.getFirst(), filtro.getMaxResults(), filtro.getIncluirCampos());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }

    }

    /**
     * Este método devuelve todos los registros de componentes habilitados de
     * una misma categoria
     *
     * @param idCateogoria
     * @return
     */
    public List<ComponentePresupuestoEscolar> getComponentesHabilitadosByCategoria(Integer idCateogoria) {
        try {
            List<ComponentePresupuestoEscolar> gestiones = gesPresEsDao.getComponentesHabilitadosByCategoria(idCateogoria);
            gestiones.isEmpty();
            return gestiones;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método devuelve un objeto específico de gasto, filtrado por obj_clasificador
     *
     * @param idClasificador
     * @return
     */
    public ObjetoEspecificoGasto getOEGByCodigo(Integer idClasificador) {
        try {
            return poadao.getOEGByCodigo(idClasificador);
        } catch(NoResultException ne){
            return null;
        }catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
}
