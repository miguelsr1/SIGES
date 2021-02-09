/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.math.BigInteger;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.enumerados.EnumMovimientosTipo;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroMovimientos;
import sv.gob.mined.siges.filtros.FiltroMovimientosEdicion;
import sv.gob.mined.siges.negocio.validaciones.MovimientosEdicionValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.MovimientosDAO;
import sv.gob.mined.siges.persistencia.daos.MovimientosEdicionDAO;
import sv.gob.mined.siges.persistencia.entidades.SgMovimientos;
import sv.gob.mined.siges.persistencia.entidades.SgMovimientosEdicion;
import sv.gob.mined.siges.persistencia.entidades.SgPresupuestoEscolar;
import sv.gob.mined.siges.persistencia.entidades.siap2.SgAreaInversion;
import sv.gob.mined.siges.rest.MovimientosEdicionRecurso;

/**
 * Servicio que gestiona los movimientos del presupuesto escolar
 *
 * @author Sofis Solutions
 */
@Stateless
public class MovimientosEdicionBean {

    private static final Logger LOGGER = Logger.getLogger(MovimientosEdicionRecurso.class.getName());

    @PersistenceContext
    private EntityManager em;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgMovimientosEdicion
     * @throws GeneralException
     *
     */
    //@Interceptors(LoadLazyCollectionsViewInterceptor.class)
    public SgMovimientosEdicion obtenerPorId(Long id, Boolean dataSecurity) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgMovimientosEdicion> codDAO = new CodigueraDAO<>(em, SgMovimientosEdicion.class);
                if (BooleanUtils.isTrue(dataSecurity)) {
                    return codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_MOVIMIENTO_PRESUPUESTO);
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
                CodigueraDAO<SgMovimientosEdicion> codDAO = new CodigueraDAO<>(em, SgMovimientosEdicion.class);
                if (BooleanUtils.isTrue(dataSecurity)) {
                    return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_MOVIMIENTO_PRESUPUESTO);
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
     * @param entity SgMovimientosEdicion
     * @return SgMovimientosEdicion
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgMovimientosEdicion guardar(SgMovimientosEdicion entity, Boolean dataSecurity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (entity.getMovTipo().equals(EnumMovimientosTipo.EM)) {
                    if (MovimientosEdicionValidacionNegocio.validarEgresos(entity)) {
                        if (entity.getMovAreaInversionPk() == null) {
                            String query = "select ai_area_padre from siap2.ss_areas_inversion where ai_id = "
                                    + entity.getMovSubAreaInversionPk().getAdiPk();
                            Query areaPadre = em.createNativeQuery(query);
                            List resultado = areaPadre.getResultList();
                            resultado.forEach(
                                    z -> {
                                        if (z != null) {
                                            BigInteger padre = (BigInteger) z;
                                            entity.setMovAreaInversionPk(em.getReference(SgAreaInversion.class, padre.longValue()));
                                        }
                                    });
                        }

                        CodigueraDAO<SgMovimientosEdicion> codDAO = new CodigueraDAO<>(em, SgMovimientosEdicion.class);
                        if (BooleanUtils.isTrue(dataSecurity)) {

                            return codDAO.guardar(entity, entity.getMovPk() == null ? ConstantesOperaciones.CREAR_MOVIMIENTO_PRESUPUESTO : ConstantesOperaciones.ACTUALIZAR_MOVIMIENTO_PRESUPUESTO);
                        } else {
                            return codDAO.guardar(entity, null);
                        }
                    }
                } else if (entity.getMovTipo().equals(EnumMovimientosTipo.IM)) {
                    if (MovimientosEdicionValidacionNegocio.validarIngresos(entity)) {
                        if (entity.getMovNumMoviento() == null) {
                            Query contador = em.createNativeQuery("SELECT MAX(mov_num_movimiento) from finanzas.sg_presupuesto_escolar_edicion_movimiento where mov_tipo= 'IM' AND mov_presupuesto_fk = :movPresupuestoFk ")
                                    .setParameter("movPresupuestoFk", entity.getMovPresupuestoFk());
                            Integer cont = ((Integer) contador.getSingleResult());
                            if (cont != null) {
                                entity.setMovNumMoviento(cont + 1);
                            } else {
                                Query contador2 = em.createNativeQuery("SELECT MAX(mov_num_movimiento) from finanzas.sg_presupuesto_escolar_movimiento where mov_tipo= 'I' AND mov_presupuesto_fk = :movPresupuestoFk ")
                                        .setParameter("movPresupuestoFk", entity.getMovPresupuestoFk());
                                Integer cont2 = ((Integer) contador2.getSingleResult());

                                entity.setMovNumMoviento(cont2 + 1);
                            }
                        }

                        CodigueraDAO<SgMovimientosEdicion> codDAO = new CodigueraDAO<>(em, SgMovimientosEdicion.class);
                        if (BooleanUtils.isTrue(dataSecurity)) {

                            return codDAO.guardar(entity, entity.getMovPk() == null ? ConstantesOperaciones.CREAR_MOVIMIENTO_PRESUPUESTO : ConstantesOperaciones.ACTUALIZAR_MOVIMIENTO_PRESUPUESTO);
                        } else {
                            return codDAO.guardar(entity, null);
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
     * Elimina el objeto con la id indicada
     *
     * @param id Long
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void eliminar(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgMovimientosEdicion> codDAO = new CodigueraDAO<>(em, SgMovimientosEdicion.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroMovimientosEdicion
     * @return Lista de SgMovimientosEdicion
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgMovimientosEdicion> obtenerPorFiltro(FiltroMovimientosEdicion filtro) throws GeneralException {
        try {
            MovimientosEdicionDAO codDAO = new MovimientosEdicionDAO(em);
            return codDAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Boolean objetoExistePorIdOriginal(Long id, Boolean dataSecurity) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgMovimientos> codDAO = new CodigueraDAO<>(em, SgMovimientos.class);
                if (BooleanUtils.isTrue(dataSecurity)) {
                    return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_MOVIMIENTO_PRESUPUESTO);
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
     * Elimina-Anula el objeto con la id indicada
     *
     * @param id Long
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void eliminarAnular(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgMovimientos> codDAO = new CodigueraDAO<>(em, SgMovimientos.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    /**
     * Elimina el objeto con la id indicada
     *
     * @param id Long
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void eliminarIngreso(Long id) throws GeneralException {
        if (id != null) {
            try {
                FiltroMovimientos mov1 = new FiltroMovimientos();
                mov1.setOrderBy(new String[]{"movPk"});
                mov1.setIncluirCampos(new String[]{
                    "movPk",
                    "movTipo",
                    "movVersion",
                    "movFuenteIngresos.movPk",
                    "movFuenteIngresos.movVersion"
                });
                mov1.setMovFuenteIngresos(id);
                mov1.setAscending(new boolean[]{true});
                List<SgMovimientos> fuenteIngreso = obtenerPorFiltro(mov1);  // Primera lista

                FiltroMovimientosEdicion mov2 = new FiltroMovimientosEdicion();
                mov2.setOrderBy(new String[]{"movPk"});
                mov2.setIncluirCampos(new String[]{
                    "movPk",
                    "movTipo",
                    "movVersion",
                    "movFuenteIngresos.movPk",
                    "movFuenteIngresos.movVersion"
                });
                mov2.setMovFuenteIngresos(id);
                mov2.setAscending(new boolean[]{true});
                List<SgMovimientosEdicion> fuenteIngresoEditado = obtenerPorFiltro(mov2);  // Segunda lista

                FiltroMovimientosEdicion mov3 = new FiltroMovimientosEdicion();
                mov3.setOrderBy(new String[]{"movPk"});
                mov3.setIncluirCampos(new String[]{
                    "movPk",
                    "movTipo",
                    "movVersion",
                    "movFuenteIngresosOriginal.movPk",
                    "movFuenteIngresosOriginal.movVersion"
                });
                mov3.setMovFuenteIngresosOriginal(id);
                mov3.setAscending(new boolean[]{true});
                List<SgMovimientosEdicion> fuenteIngresoOriginal = obtenerPorFiltro(mov3);  // Segunda lista

                // Borrando Egresos Tabla Movimientos
                if (id != null && !fuenteIngreso.isEmpty()) {
                    for (SgMovimientos mova : fuenteIngreso) {
                        eliminarMov(mova.getMovPk());
                    }
                }
                // Borrando Egresos Tabla Movimientos Edicion
                if (id != null && !fuenteIngresoEditado.isEmpty()) {
                    for (SgMovimientosEdicion movb : fuenteIngresoEditado) {
                        eliminar(movb.getMovPk());
                    }
                }
                // Borrando Egresos Tabla Movimientos - Ingresos Movimientos
                if (id != null && !fuenteIngresoOriginal.isEmpty()) {
                    for (SgMovimientosEdicion movc : fuenteIngresoOriginal) {
                        eliminar(movc.getMovPk());
                    }
                } else {
                    //mensaje de error
                }
                eliminar(id);

            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroMovimientos
     * @return Lista de SgMovimientos
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgMovimientos> obtenerPorFiltro(FiltroMovimientos filtro) throws GeneralException {
        try {
            MovimientosDAO codDAO = new MovimientosDAO(em);
            return codDAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Elimina el objeto con la id indicada
     *
     * @param id Long
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void eliminarMov(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgMovimientos> codDAO = new CodigueraDAO<>(em, SgMovimientos.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    /**
     * Elimina el objeto con la id indicada
     *
     * @param id Long
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void eliminarEditados(Long id) throws GeneralException {
        if (id != null) {

            try {
                FiltroMovimientosEdicion movE = new FiltroMovimientosEdicion();
                movE.setOrderBy(new String[]{"movPk"});
                movE.setIncluirCampos(new String[]{
                    "movPk",
                    "movTipo",
                    "movVersion",
                    "movPresupuestoFk.pesPk",
                    "movPresupuestoFk.pesVersion"
                });
                movE.setMovPresupuestoFk(id);
                movE.setMovTipo(EnumMovimientosTipo.EM);
                movE.setAscending(new boolean[]{true});
                List<SgMovimientosEdicion> egresosEliminar = obtenerPorFiltro(movE);

                FiltroMovimientosEdicion movI = new FiltroMovimientosEdicion();
                movI.setOrderBy(new String[]{"movPk"});
                movI.setIncluirCampos(new String[]{
                    "movPk",
                    "movTipo",
                    "movVersion",
                    "movPresupuestoFk.pesPk",
                    "movPresupuestoFk.pesVersion"
                });
                movI.setMovPresupuestoFk(id);
                movI.setMovTipo(EnumMovimientosTipo.IM);
                movI.setAscending(new boolean[]{true});
                List<SgMovimientosEdicion> ingresosEliminar = obtenerPorFiltro(movI);

                // Borrando Egresos Tabla Movimientos - Ingresos Movimientos
                if (id != null && !egresosEliminar.isEmpty()) {
                    for (SgMovimientosEdicion deleteEgresos : egresosEliminar) {
                        eliminar(deleteEgresos.getMovPk());
                    }
                }
                // Borrando Egresos Tabla Movimientos - Ingresos Movimientos
                if (id != null && !ingresosEliminar.isEmpty()) {
                    for (SgMovimientosEdicion deleteIngresos : ingresosEliminar) {
                        eliminar(deleteIngresos.getMovPk());
                    }
                } else {
                    //mensaje de error
                }

            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    /**
     * Devuelve si el objeto existe
     *
     * @param id Long
     * @return Boolean
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Boolean objetoPresPorId(Long id, Boolean dataSecurity) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgPresupuestoEscolar> codDAO = new CodigueraDAO<>(em, SgPresupuestoEscolar.class);
                if (BooleanUtils.isTrue(dataSecurity)) {
                    return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_MOVIMIENTO_PRESUPUESTO);
                } else {
                    return codDAO.objetoExistePorId(id, null);
                }
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }
}
