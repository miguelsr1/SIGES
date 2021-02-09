/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroBienesDepreciables;
import sv.gob.mined.siges.filtros.FiltroLoteBienes;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.validaciones.LoteBienesValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.LoteBienesDAO;
import sv.gob.mined.siges.persistencia.entidades.SgAfBienDepreciable;
import sv.gob.mined.siges.persistencia.entidades.SgAfLoteBienes;
import sv.gob.mined.siges.persistencia.utilidades.PersistenceHelper;

@Stateless
@Traced
public class LoteBienesBean {
    private static final Logger LOGGER = Logger.getLogger(LoteBienesBean.class.getName());
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private BienesDepreciablesBean bienes;
    
    @Inject
    @ConfigProperty(name = "centro-educativo.codigo-correlativo.size", defaultValue = "4")
    private Integer codigoCorrelativoCentroEducativoSize;
    
    @Inject
    @ConfigProperty(name = "unidad-administrativa.codigo-correlativo.size", defaultValue = "3")
    private Integer codigoCorrelativoUnidadAdministrativaSize;
    
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgAfLoteBienes
     * @throws GeneralException
     * 
     */
    public SgAfLoteBienes obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgAfLoteBienes> codDAO = new CodigueraDAO<>(em, SgAfLoteBienes.class);
                return codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_LOTE_BIENES);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return null;
    }

    /**
     * Devuelve si el objeto existe
     *
     * @param id Long
     * @return Boolean
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Boolean objetoExistePorId(Long id, Boolean dataSecurity) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgAfLoteBienes> codDAO = new CodigueraDAO<>(em, SgAfLoteBienes.class);
                if (BooleanUtils.isTrue(dataSecurity)){
                    return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_LOTE_BIENES);
                } else {
                    return codDAO.objetoExistePorId(id, null);
                }
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param entity SgAfLoteBienes
     * @return SgAfLoteBienes
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
   // @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public SgAfLoteBienes guardar(SgAfLoteBienes entity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (LoteBienesValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgAfLoteBienes> codDAO = new CodigueraDAO<>(em, SgAfLoteBienes.class);
                    return codDAO.guardar(entity, null);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return entity;
    }
    
    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroLoteBienes
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroLoteBienes filtro) throws GeneralException {
        try {
            LoteBienesDAO codDAO = new LoteBienesDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro, filtro.getSecurityOperation());
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroLoteBienes
     * @return Lista de <SgAfLoteBienes>
     * @throws GeneralException
     */
    public List<SgAfLoteBienes> obtenerPorFiltro(FiltroLoteBienes filtro) throws GeneralException {
        try {
            LoteBienesDAO codDAO = new LoteBienesDAO(em);
            return codDAO.obtenerPorFiltro(filtro, filtro.getSecurityOperation());
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Generación de bienes depreciables de acuerdo al párametro cantidadReplicar. La generación se hace generando códigos de inventario descendentemente.
     * En caso que el código a generar ya existe, entonces el registro se genera con el último código de inventario disponible((El mayor correlativo)
     * @param loteId Id del lote
     * @param bien Bien a replicar
     * @param cantidadReplicar Cantidad de bienes a replicar
     * @param numeroCorrelativo Número de correlativo
     * @param dataSecurity Se aplica seguridad =true, false en caso contrario
     * @param filtroBienes Filtro de bienes, en caso que sea necesario buscar el último correlativo disponible
     * @return número de correlativo
     * @throws GeneralException 
     */
    @Interceptors({AuditInterceptor.class})
    public Integer procesarLoteBienes(Long loteId,SgAfBienDepreciable bien, Integer cantidadReplicar, Integer numeroCorrelativo, Boolean dataSecurity, FiltroBienesDepreciables filtroBienes) throws GeneralException{
        SgAfBienDepreciable nuevo = null;
        
        FiltroBienesDepreciables filtro = new FiltroBienesDepreciables();
        filtro.setSecurityOperation(null);
        
        try {
            if(bien != null) {
                for(int registroCount=1; registroCount <= cantidadReplicar; registroCount++) {
                    nuevo = SerializationUtils.clone(bien);
                    try {
                        nuevo.setBdePk(null);
                        nuevo.setBdeEsLote(Boolean.FALSE);
                        nuevo.setBdeNoChasis(null);
                        nuevo.setBdeNoMotor(null);
                        nuevo.setBdeNoPlaca(null);
                        nuevo.setBdeVersion(0);

                        numeroCorrelativo = numeroCorrelativo - 1;

                        nuevo = bienes.generarCodigoInventario(nuevo, numeroCorrelativo);

                        nuevo.setBdeLoteId(loteId);
                        nuevo = bienes.guardarNormal(nuevo, dataSecurity, Boolean.FALSE, Boolean.TRUE);

                    } catch (BusinessException be) {
                        throw  be;
                    } catch (Exception ex) {
                        //Si la excepción es por un error de bloqueo o por violación de llave, entonces se retorna el contador a su valor anterior
                        if (PersistenceHelper.isOptimisticException(ex)) {
                            registroCount = registroCount -1;
                            LOGGER.log(Level.SEVERE, Errores.ERROR_OPTIMISTIC_LOCK, ex);
                        } else if (PersistenceHelper.isConstraintViolation(ex)) {//El código de inventario ya existe, entonces se busca el último para generar el bien
                            LOGGER.log(Level.SEVERE, PersistenceHelper.getCodigoInventarioViolationBusinessException(ex).getMessage(), ex);

                            if(filtroBienes != null) {
                                Integer ultimoCorrelativo = bienes.obtenerUltimoCorrelativo(filtroBienes, Boolean.FALSE);

                                ultimoCorrelativo = ultimoCorrelativo + 1;

                                nuevo = bienes.generarCodigoInventario(nuevo, ultimoCorrelativo);

                                nuevo = bienes.guardarNormal(nuevo, dataSecurity, Boolean.FALSE, Boolean.TRUE);
                            }

                        } else {
                            throw ex;
                        }
                    }

                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
        return numeroCorrelativo;
    }
}
