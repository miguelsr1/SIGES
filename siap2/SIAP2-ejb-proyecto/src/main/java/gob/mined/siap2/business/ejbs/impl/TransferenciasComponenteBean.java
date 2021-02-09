package gob.mined.siap2.business.ejbs.impl;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.impl.DesembolsoTransferenciaComponente;
import gob.mined.siap2.entities.data.impl.FuenteFinanciamiento;
import gob.mined.siap2.entities.data.impl.FuenteRecursos;
import gob.mined.siap2.entities.data.impl.Transferencia;
import gob.mined.siap2.entities.data.impl.TransferenciaDireccionDepartamental;
import gob.mined.siap2.entities.data.impl.TransferenciasACed;
import gob.mined.siap2.entities.data.impl.TransferenciasComponente;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.filtros.FiltroTransferenciaComponente;
import gob.mined.siap2.filtros.FiltroTransferenciaDireccionDepartamental;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.imp.DesembolsosDAO;
import gob.mined.siap2.persistence.dao.imp.FuenteFinanciamientoDAO;
import gob.mined.siap2.persistence.dao.imp.TransferenciasACedDAO;
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
import javax.persistence.NoResultException;

@LocalBean
@Stateless(name = "TransferenciasComponenteBean")
public class TransferenciasComponenteBean {
    
    private static final Logger logger = Logger.getLogger(TransferenciasComponenteBean.class.getName());
    
    
    @Inject
    @JPADAO
    private TransferenciasComponenteDAO transferenciasComponenteDAO;
    @Inject
    @JPADAO
    private GeneralDAO generalDAO;
    @Inject
    @JPADAO
    private TransferenciasACedDAO transferenciasACedDAO;
    @Inject
    @JPADAO
    private DesembolsosDAO desembolsosDAO;
    @Inject
    @JPADAO
    private FuenteFinanciamientoDAO financiamientoDAO;
    @Inject
    private TransferenciaDireccionDepartamentalBean transferenciaDireccionDepartamentalBean;
    
    /**
     * Este método crea y actualiza un registro de TransferenciasComponente
     * @param tc 
     * @return  
     */
    public TransferenciasComponente crearActualizar(TransferenciasComponente tc) {
        try {
            BusinessException b = new BusinessException();
            if (tc.getComponente() == null){
                b.addError(ConstantesErrores.ERR_COMPONENTE_VACIO); 
            }
            if (tc.getSubcomponente() == null){
                b.addError(ConstantesErrores.ERR_SUBCOMPONENTE_VACIO); 
            }
            if (tc.getUnidadPresupuestaria() == null){
                b.addError(ConstantesErrores.ERR_UNIDAD_PRESUPUESTARIA_VACIO); 
            }
            if (tc.getEstado() == null){
                b.addError(ConstantesErrores.ERR_ESTADO_VACIO); 
            }
            if (tc.getAnioFiscal() == null){
                b.addError(ConstantesErrores.ERR_ANIO_VACIO); 
            }
            return (TransferenciasComponente) generalDAO.update(tc);
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
     * Este método crea y/o actualiza una lista de registros de TransferenciasComponente
     * 
     * @param listaTc
     */
    public void crearActualizar(List<TransferenciasComponente> listaTc) {
        try {
            BusinessException b = new BusinessException();
            for(TransferenciasComponente tc : listaTc){
                
                if (tc.getComponente() == null){
                    b.addError(ConstantesErrores.ERR_COMPONENTE_VACIO); 
                }
                if (tc.getSubcomponente() == null){
                    b.addError(ConstantesErrores.ERR_SUBCOMPONENTE_VACIO); 
                }
                if (tc.getUnidadPresupuestaria() == null){
                    b.addError(ConstantesErrores.ERR_UNIDAD_PRESUPUESTARIA_VACIO); 
                }
                if (tc.getEstado() == null){
                    b.addError(ConstantesErrores.ERR_ESTADO_VACIO); 
                }
                if (tc.getAnioFiscal() == null){
                    b.addError(ConstantesErrores.ERR_ANIO_VACIO); 
                }
                if(!b.getErrores().isEmpty()){
                    throw b;
                }
                
                if(tc.getId() == null){
                    generalDAO.getEntityManager().persist(tc);
                }else{
                    generalDAO.merge(tc);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    
    /**
     * Método que se encarga de eliminar una TransferenciasComponente y sus registros de 
     * TransferenciasACed Y TransferenciaDireccionDepartamental asociados.
     * 
     * @param id Identificador de registro a eliminar
     */
    public void eliminar(Integer id) {
        try {
            TransferenciasComponente tc = (TransferenciasComponente) generalDAO.find(TransferenciasComponente.class, id);
            
            if(tc != null){
                
                //BUSQUEDA Y ELIMINACION DE TRANSFERENCIA_A_CED
                List<TransferenciasACed> listaACed = transferenciasACedDAO.getTransferenciasACedByTransfComp(tc.getId());
                if(listaACed != null && !listaACed.isEmpty()){
                    for(TransferenciasACed aced : listaACed){
                        generalDAO.delete(aced);
                    }
                }
                
                //BUSQUEDA Y ELIMINACION DE TRANSFERENCIA_DIRECCION_DEPARTAMENTAL
                FiltroTransferenciaDireccionDepartamental filtro = new FiltroTransferenciaDireccionDepartamental();
                filtro.setIdTransferenciaComponente(tc.getId());
                List<TransferenciaDireccionDepartamental> listaTdd = transferenciaDireccionDepartamentalBean.getTDDByFiltro(filtro);
                logger.log(Level.SEVERE, "LISTA DE TC A ELIMINAR:"+listaTdd.size());
                if(listaTdd != null && !listaTdd.isEmpty()){
                    for(TransferenciaDireccionDepartamental tdd : listaTdd){
                        generalDAO.delete(tdd);
                    }
                }

                //BUSQUEDA Y ELIMINACION DE DESEMBOLSO_TRANSFERENCIA_COMPONENTE
                List<DesembolsoTransferenciaComponente> listaDtt = desembolsosDAO.getAllDesembolsByTransferenciaComponente(id);
                if(listaDtt != null && !listaDtt.isEmpty()){
                    for(DesembolsoTransferenciaComponente dtt : listaDtt){
                        generalDAO.delete(dtt);
                    }
                }
                generalDAO.delete(tc);
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    /**
     * Metodo utilizado para el filtrado de registros TransferenciaDireccionDepartamental
     * @param filtro
     * @return 
     */
    public List<TransferenciasComponente> getTransferenciaComponenteByFiltro(FiltroTransferenciaComponente filtro) {
        try {
            List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();

            if (filtro.getIdAnioFiscal() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "anioFiscal.id", filtro.getIdAnioFiscal());
                criterios.add(criterio);
            }

            if (filtro.getIdComponente() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "componente.id", filtro.getIdComponente());
                criterios.add(criterio);
            }

            if (filtro.getIdSubcomponente() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "subcomponente.id", filtro.getIdSubcomponente());
                criterios.add(criterio);
            }

            if (filtro.getIdUnidadPresupuestaria() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "unidadPresupuestaria.id", filtro.getIdUnidadPresupuestaria());
                criterios.add(criterio);
            }

            if (filtro.getIdLineaPresupuestaria() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "lineaPresupuestaria.id", filtro.getIdLineaPresupuestaria());
                criterios.add(criterio);
            }

            if (filtro.getIdFuenteFinanciamiento() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "fuenteFinanciamiento.id", filtro.getIdFuenteFinanciamiento());
                criterios.add(criterio);
            }

            if (filtro.getIdFuenteRecursos() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "fuenteRecursos.id", filtro.getIdFuenteRecursos());
                criterios.add(criterio);
            }

            if (filtro.getIdDireccionDepartamental() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "direccionDepartamental.pk", filtro.getIdDireccionDepartamental());
                criterios.add(criterio);
            }
            
            if (filtro.getIdTransferencia() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "transferencia.id", filtro.getIdTransferencia());
                criterios.add(criterio);
            }
            
            
            CriteriaTO condicion = null;
            if (criterios.size() == 1) {
                condicion = criterios.get(0);
            } else {
                condicion = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[0]));
            }
            return generalDAO.findEntityByCriteria(TransferenciasComponente.class, condicion, filtro.getOrderBy(), filtro.getAscending(), filtro.getFirst(), filtro.getMaxResults(), filtro.getIncluirCampos());
        } catch (DAOGeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }

    }
    
    
    /**
     * Este método busca un listado de registros que contengan el mismo anio
     * @param anio
     * @return 
     */
    public List<TransferenciasComponente> getTransferenciasComponenteByAnio(Integer anio) {
        try {
            return transferenciasComponenteDAO.getTransferenciasComponenteByAnioFiscal(anio);
        } catch (DAOGeneralException ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    
    /**
     * Este método busca un registro filtrado por su identificador
     * @param id
     * @return 
     */
    public TransferenciasComponente getTransferenciasComponenteById(Integer id) {
        try {
            return (TransferenciasComponente) generalDAO.find(TransferenciasComponente.class, id);
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    /**
     * Este método buscan un registro de TransferenciasComponente filtrado por su identiicador
     * @param id
     * @return 
     */
    public TransferenciasComponente getTransferenciaComponenteById(Integer id) {
        return generalDAO.getEntityManager().find(TransferenciasComponente.class, id);
    }
    
    
    /**
     * Este método busca un listado de FuentesFinanciamiento que pertenezcan a una relacion de Subcomponente y LineaPresupuestaria
     * 
     * @param idLinea
     * @param idComp
     * @return 
     */
    public List<FuenteFinanciamiento> getTransferenciasComponenteByAnio(Integer idLinea, Integer idComp) {
        try {
            return financiamientoDAO.getFuentesFinanciamientoByRelacionPresAnioFinanciamiento(idLinea, idComp);
        } catch(NoResultException nre){
            return null;
        }catch (DAOGeneralException ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    /**
     * Este método obtiene un listado de todos los registros de la tabla TransferenciasComponente que se relacionen con un registro de ComponentePresupuestoEscolar
     * 
     * @param id
     * @return 
     */
    public List<TransferenciasComponente> getTransferenciasComponenteByCategoriaComponente(Integer id) {
        try {
            return transferenciasComponenteDAO.getTransferenciasComponenteByCategoriaComponente(id);
        } catch(NoResultException nre){
            return null;
        }catch (DAOGeneralException ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    /**
     * Este método obtiene un listado de todos los registros de la tabla TransferenciasComponente que se relacionen con un registro de Componente
     * 
     * @param id
     * @return 
     */
    public List<TransferenciasComponente> getTransferenciasComponenteByComponentePresupuesto(Integer id) {
        try {
            return transferenciasComponenteDAO.getTransferenciasComponenteByComponentePresupuesto(id);
        } catch(NoResultException nre){
            return null;
        }catch (DAOGeneralException ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    /**
     * Este método busca un listado de FuentesRecursos que pertenezcan a una relacion de FuenteFinanciamiento, Subcomponente y LineaPresupuestaria
     * 
     * @param idLinea
     * @param idComp
     * @return 
     */
    public List<FuenteRecursos> getFuentesRecursosByRelaciones(Integer idLinea, Integer idComp, Integer idFuenteF) {
        try {
            return financiamientoDAO.getFuentesRecursosByRelaciones(idLinea, idComp, idFuenteF);
        } catch(NoResultException nre){
            return null;
        }catch (DAOGeneralException ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
}
