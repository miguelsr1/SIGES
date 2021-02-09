package gob.mined.siap2.business.ejbs.impl;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.impl.CategoriaPresupuestoEscolar;
import gob.mined.siap2.entities.data.impl.ComponentePresupuestoEscolar;
import gob.mined.siap2.entities.data.impl.TopePresupuestal;
import gob.mined.siap2.entities.data.impl.TransferenciasComponente;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.filtros.FiltroComponentePresupuestoEscolar;
import gob.mined.siap2.filtros.FiltroUnidadTecnica;
import gob.mined.siap2.persistence.dao.exceptions.DAOConstraintViolationException;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import gob.mined.siap2.persistence.dao.imp.CategoriaPresupuestoEscolarDAO;
import gob.mined.siap2.persistence.dao.imp.GestionPresupuestoEscolarDAO;
import gob.mined.siap2.persistence.dao.imp.TopePresupuestalDAO;
import gob.mined.siap2.persistence.dao.imp.TransferenciasComponenteDAO;
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


@LocalBean
@Stateless(name = "CategoriaPresupuestoEscolarBean")
public class CategoriaPresupuestoEscolarBean {
    
    
    private static final Logger logger = Logger.getLogger(CategoriaPresupuestoEscolarBean.class.getName());
    
    public static final String ERR_CONSTRAINT_VIOLADA = "duplicate key value violates unique constraint";
    
    @Inject
    @JPADAO
    private GeneralDAO generalDAO;
    
    @Inject
    @JPADAO
    private TopePresupuestalDAO topePresupuestalDAO;
    
    @Inject
    @JPADAO
    private TransferenciasComponenteDAO transferenciasComponenteDAO;
    
    @Inject
    @JPADAO
    private GestionPresupuestoEscolarDAO gesPresEsDao;
    
    @Inject
    @JPADAO
    private CategoriaPresupuestoEscolarDAO cpedao;
    
    
    
    
    /**
     * Este método crea o actualiza un registro de CategoriaPresupuestoEscolar
     * 
     * @param catgoria
     * @throws gob.mined.siap2.persistence.dao.exceptions.DAOConstraintViolationException
     */
    public void crearActualizar(CategoriaPresupuestoEscolar catgoria) throws DAOConstraintViolationException {
        try {
            generalDAO.update(catgoria);
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        }catch (DAOGeneralException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            if (ex.getMessage().contains(ERR_CONSTRAINT_VIOLADA)) {
                DAOConstraintViolationException eDao = new DAOConstraintViolationException(ex);
                throw eDao;
            } else {
                TechnicalException ge = new TechnicalException();
                ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
                ge.addError(ex.getMessage());
                throw ge;
            }
        } 

    }
    
    /**
     * Método que se encarga de eliminar una registro categoria de presupuesto escolar
     * actividades
     * @param id Identificador de registro a eliminar
     */
    public void eliminar(Integer id) {
        try {
            CategoriaPresupuestoEscolar ai = (CategoriaPresupuestoEscolar) generalDAO.find(CategoriaPresupuestoEscolar.class, id);
            
            BusinessException b = new BusinessException();
            if(ai != null){

                List<TopePresupuestal> listaTope = topePresupuestalDAO.getTopePresupuestalByIdCategoriaPresupuestoEscolar(ai.getId());
                if(listaTope != null && !listaTope.isEmpty()){
                     b.addError(ConstantesErrores.ERR_NO_ELIMINAR_REGISTROS_TOPE);
                }
                
                List<TransferenciasComponente> listaTransComp = transferenciasComponenteDAO.getTransferenciasComponenteByCategoriaPresupuesto(id);
                if(listaTransComp != null && !listaTransComp.isEmpty()){
                     b.addError(ConstantesErrores.ERR_NO_ELIMINAR_REGISTROS_TRANSFERENCIA_COMPONENTE);
                }
                
                if (!b.getErrores().isEmpty()) {
                    throw b;
                }
                
                List<ComponentePresupuestoEscolar> listaComp = gesPresEsDao.getGesPresEsByCategoria(ai.getId());
                if(listaComp != null && !listaComp.isEmpty()){
                    for(ComponentePresupuestoEscolar item : listaComp){
                        generalDAO.delete(item);
                    }
                }
                
                
                generalDAO.delete(ai);
            }else{
                b.addError(ConstantesErrores.ERR_REGISTRO_NO_ENCONTRADO);
                throw b;
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
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    
    
    
    /**
     * Este método devuelve un registro de CategoriaPresupuestoEscolar, filtrado por ID
     * @param id
     * @return 
     */
    public CategoriaPresupuestoEscolar getCategoriaPresupuestoEscolarById(Integer id) {
        return generalDAO.getEntityManager().find(CategoriaPresupuestoEscolar.class, id);
    }

    /**
     * Este método devuelve todos los registros de CategoriaPresupuestoEscolar
     * @return 
     */
    public List<CategoriaPresupuestoEscolar> getAllCategoriaPresupuestoEscolar() {
        try {
            return cpedao.getAllCatPresupuestoEscolar();
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    /**
     * Este método devuelve todos los registros de CategoriaPresupuestoEscolar que se encuentren habilitados
     * @return 
     */
    public List<CategoriaPresupuestoEscolar> getAllCategoriaPresupuestoEscolarHabilitados() {
        try {
            return cpedao.getAllCatPresupuestoEscolarHabilitados();
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    public List<CategoriaPresupuestoEscolar> obtenerComponentesPorFiltro(FiltroComponentePresupuestoEscolar filtro) {
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

            if(filtro.getCodigoNombre() != null && !filtro.getCodigoNombre().isEmpty()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "codigo", filtro.getCodigoNombre().toLowerCase().trim());
                MatchCriteriaTO criterio1 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "nombreBusqueda", filtro.getCodigoNombre().toLowerCase().trim());
                criterios.add(CriteriaTOUtils.createORTO(criterio, criterio1));
            }
            
            CriteriaTO condicion = null;
            if (criterios.size() == 1) {
                condicion = criterios.get(0);
            } else {
                condicion = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[0]));
            }

            return generalDAO.findEntityByCriteria(CategoriaPresupuestoEscolar.class, condicion, filtro.getOrderBy(), filtro.getAscending(), filtro.getFirst(), filtro.getMaxResults(), filtro.getIncluirCampos());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
}
