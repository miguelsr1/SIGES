/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.BooleanUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.enumerados.EnumEstadosProceso;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroLiquidacionProyectos;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.validaciones.LiquidacionProyectoValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.LiquidacionProyectoDAO;
import sv.gob.mined.siges.persistencia.entidades.SgAfDepreciacionMaestro;
import sv.gob.mined.siges.persistencia.entidades.SgAfLiquidacionProyecto;

@Stateless
@Traced
public class LiquidacionProyectoBean {
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private DepreciacionMaestroBean depreciacionMaestroBean;
    
    @Inject
    @ConfigProperty(name = "anio-limite-menor-admitido", defaultValue = "1960")
    private Integer anioLimiteMenorAdmitido;
    
    @Inject
    @ConfigProperty(name = "tareas-noprocesadas-admitidas", defaultValue = "15")
    private Long tareasNoProcesadasAdmitidas;
    
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgAfLiquidacionProyecto
     * @throws GeneralException
     * 
     */
    public SgAfLiquidacionProyecto obtenerPorId(Long id, Boolean dataSecurity) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgAfLiquidacionProyecto> codDAO = new CodigueraDAO<>(em, SgAfLiquidacionProyecto.class);
                if (BooleanUtils.isTrue(dataSecurity)){
                    return codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_LIQUIDACIONES_PROYECTOS);
                } else {
                    return codDAO.obtenerPorId(id, null);
                }
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
                CodigueraDAO<SgAfLiquidacionProyecto> codDAO = new CodigueraDAO<>(em, SgAfLiquidacionProyecto.class);
                if (BooleanUtils.isTrue(dataSecurity)){
                    return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_LIQUIDACIONES_PROYECTOS);
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
     * Elimina el objeto con la id indicada
     *
     * @param id Long
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void eliminar(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgAfLiquidacionProyecto> codDAO = new CodigueraDAO<>(em, SgAfLiquidacionProyecto.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }
    
    
    /**
     * Procesa el objeto indicado para liquidar un proyecto.
     *
     * @param entity SgAfLiquidacionProyecto
     * @return SgAfLiquidacionProyecto
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    @Interceptors({AuditInterceptor.class,})
    public Boolean procesar(SgAfLiquidacionProyecto entity, Boolean dataSecurity, Long primero, Long maxResult) throws GeneralException {
        Boolean precesado = Boolean.FALSE;
        try {
            //Validar el elemento a guardar. Si no valida, se lanza una excepcion
            if (LiquidacionProyectoValidacionNegocio.validar(entity, anioLimiteMenorAdmitido)) {
                SgAfDepreciacionMaestro maestro = new SgAfDepreciacionMaestro();
                maestro.setDmaFuenteFinanciamientoFk(entity.getLprFuenteFinanciamientoOrigenFk());
                maestro.setDmaProyectoFk(entity.getLprProyectoFk());
                depreciacionMaestroBean.procesar(maestro, dataSecurity, Boolean.FALSE, Boolean.TRUE,entity.getLprFechaLiquidacion(), null, null, entity.getLprFuenteFinanciamientoDestinoFk(), primero, maxResult);
                precesado = Boolean.TRUE;
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return precesado;
    }
    
    /**
     * Guarda el objeto indicado
     *
     * @param entity SgAfLiquidacionProyecto
     * @return SgAfLiquidacionProyecto
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgAfLiquidacionProyecto guardar(SgAfLiquidacionProyecto entity, Boolean dataSecurity, Boolean validarExistente) throws GeneralException {
        try {
            Boolean guardar = Boolean.FALSE;
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (LiquidacionProyectoValidacionNegocio.validar(entity, anioLimiteMenorAdmitido)) {
                    if(validarExistente != null && validarExistente) {
                        guardar = validarExistentes(entity);
                    } else {
                        guardar = Boolean.TRUE;
                    }
                    if(guardar) {
                        CodigueraDAO<SgAfLiquidacionProyecto> codDAO = new CodigueraDAO<>(em, SgAfLiquidacionProyecto.class);
                        if (BooleanUtils.isTrue(dataSecurity)){
                            entity = codDAO.guardar(entity, entity.getLprPk() != null ? ConstantesOperaciones.ACTUALIZAR_LIQUIDACION_PROYECTO : ConstantesOperaciones.CREAR_LIQUIDACION_PROYECTO);
                        } else {
                            entity = codDAO.guardar(entity, null);
                        }
                    }  
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
     * Valida que no hayan procesos repetidos con estado PENDIENTE O EN_PROCESO
     * @param entity
     * @return
     * @throws GeneralException 
     */
    public boolean validarExistentes(SgAfLiquidacionProyecto entity) throws GeneralException {
        Boolean resultado = Boolean.FALSE;
            try {
            FiltroLiquidacionProyectos filtro = new FiltroLiquidacionProyectos();
            filtro.setFuenteId(entity.getLprFuenteFinanciamientoOrigenFk() != null ? entity.getLprFuenteFinanciamientoOrigenFk().getFfiPk() : null);
            filtro.setProyectoId(entity.getLprProyectoFk() != null ? entity.getLprProyectoFk().getProPk() : null);
     
            List<EnumEstadosProceso> estados = new ArrayList();
            estados.add(EnumEstadosProceso.PENDIENTE);
            estados.add(EnumEstadosProceso.EN_PROCESO);
            filtro.setEstados(estados);
            
            Long total = obtenerTotalPorFiltro(filtro);
            //Si es el existente, se permite maximo 1, de lo contrario ninguno existente con el filtro induicado
            if((entity.getLprPk() != null && total.compareTo(1L) <= 0) || (entity.getLprPk() == null && total.compareTo(0L) == 0)) {
               resultado = Boolean.TRUE;
            } else {
                BusinessException ge = new BusinessException();
                ge.addError(Errores.ERROR_TAREA_EXISTE_PENDIENTE_PROCESAR_O_EN_PROCESO);
                throw ge;
            }
            filtro = new FiltroLiquidacionProyectos();
            filtro.setEstados(estados);
            Long totalPendientesFinalizar = obtenerTotalPorFiltro(filtro);
            if(tareasNoProcesadasAdmitidas.compareTo(totalPendientesFinalizar) <= 0) {
                BusinessException ge = new BusinessException();
                ge.addError(Errores.ERROR_LIMITE_TAREAS_NO_FINALIZADAS);
                throw ge;
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
        return resultado;
    }
    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroLiquidacionProyectos
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroLiquidacionProyectos filtro) throws GeneralException {
        try {
            LiquidacionProyectoDAO codDAO = new LiquidacionProyectoDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroLiquidacionProyectos
     * @return Lista de <LiquidacionProyectoValidacionNegocio>
     * @throws GeneralException
     */
    public List<SgAfLiquidacionProyecto> obtenerPorFiltro(FiltroLiquidacionProyectos filtro) throws GeneralException {
        try {
            LiquidacionProyectoDAO codDAO = new LiquidacionProyectoDAO(em);
            return codDAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
}