/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.dto.EgresoArchivoPost;
import sv.gob.mined.siges.dto.EgresosArchivo;
import sv.gob.mined.siges.enumerados.EnumMovimientosOrigen;
import sv.gob.mined.siges.enumerados.EnumMovimientosTipo;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroMovimientos;
import sv.gob.mined.siges.negocio.validaciones.MovimientosValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.MovimientosDAO;
import sv.gob.mined.siges.persistencia.entidades.SgDetallePlanEscolar;
import sv.gob.mined.siges.persistencia.entidades.SgMovimientos;
import sv.gob.mined.siges.persistencia.entidades.SgMovimientosEdicion;
import sv.gob.mined.siges.persistencia.entidades.SgPresupuestoEscolar;
import sv.gob.mined.siges.persistencia.entidades.SgRubros;
import sv.gob.mined.siges.persistencia.entidades.siap2.SgAreaInversion;

/**
 * Servicio que gestiona los movimientos del presupuesto escolar
 *
 * @author Sofis Solutions
 */
@Stateless
public class MovimientosBean {

    private static final Logger LOGGER = Logger.getLogger(FacturaBean.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Inject
    private MovimientosEdicionBean movEdicionBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgMovimientos
     * @throws GeneralException
     *
     */
    //@Interceptors(LoadLazyCollectionsViewInterceptor.class)
    public SgMovimientos obtenerPorId(Long id, Boolean dataSecurity) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgMovimientos> codDAO = new CodigueraDAO<>(em, SgMovimientos.class);
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
     * Guarda el objeto indicado
     *
     * @param entity SgMovimientos
     * @return SgMovimientos
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgMovimientos guardar(SgMovimientos entity, Boolean dataSecurity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (entity.getMovTipo().equals(EnumMovimientosTipo.E)) {
                    if (MovimientosValidacionNegocio.validarEgresos(entity)) {
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

                        CodigueraDAO<SgMovimientos> codDAO = new CodigueraDAO<>(em, SgMovimientos.class);
                        if (BooleanUtils.isTrue(dataSecurity)) {
                            return codDAO.guardar(entity, entity.getMovPk() == null ? ConstantesOperaciones.CREAR_MOVIMIENTO_PRESUPUESTO : ConstantesOperaciones.ACTUALIZAR_MOVIMIENTO_PRESUPUESTO);
                        } else {
                            return codDAO.guardar(entity, null);
                        }
                    }
                } else {
                    if (MovimientosValidacionNegocio.validarIngresos(entity)) {
                        if (entity.getMovNumMoviento() == null) {
                            Query contador = em.createNativeQuery("SELECT MAX(mov_num_movimiento) from finanzas.sg_presupuesto_escolar_movimiento where mov_tipo= 'I' AND mov_presupuesto_fk = :movPresupuestoFk ")
                                    .setParameter("movPresupuestoFk", entity.getMovPresupuestoFk());
                            Integer cont = ((Integer) contador.getSingleResult());
                            if (cont != null) {
                                entity.setMovNumMoviento(cont + 1);
                            } else {
                                entity.setMovNumMoviento(1);
                            }

                        }

                        CodigueraDAO<SgMovimientos> codDAO = new CodigueraDAO<>(em, SgMovimientos.class);
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
                CodigueraDAO<SgMovimientos> codDAO = new CodigueraDAO<>(em, SgMovimientos.class);
                codDAO.eliminarPorId(id, null);
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
     * Elimina Ingreso el objeto con la id indicada. Verifica que no existan
     * egresos relacionados, sino elmina egresos luego el ingreso.
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
                List<SgMovimientos> egresosMovimientos = obtenerPorFiltro(mov1);  // Primera lista

                if (id != null && !egresosMovimientos.isEmpty()) {
                    for (SgMovimientos move : egresosMovimientos) {
                        eliminar(move.getMovPk());
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
     * Guarda el objeto indicado
     *
     * @param entity SgMovimientos
     * @return SgMovimientos
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgMovimientos guardarEditado(SgMovimientos entity, Boolean dataSecurity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (entity.getMovTipo().equals(EnumMovimientosTipo.E)) {
                    if (MovimientosValidacionNegocio.validarEgresos(entity)) {
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

                        CodigueraDAO<SgMovimientos> codDAO = new CodigueraDAO<>(em, SgMovimientos.class);
                        if (BooleanUtils.isTrue(dataSecurity)) {

                            //SE BUSCA EL MOV ORIGINAL
                            SgMovimientos movOri = obtenerPorId(entity.getMovPk(), true);
                            //SE CARGAN LOS DATOS DEL MOV ORIGINAL PARA SER EDITADOS
                            SgMovimientosEdicion mov = new SgMovimientosEdicion();
                            mov.setMovOriginalEditado(movOri);
                            mov.setMovPresupuestoFk(movOri.getMovPresupuestoFk());
                            mov.setMovFuenteRecursos(movOri.getMovFuenteRecursos());
                            mov.setMovFuenteIngresosOriginal(movOri.getMovFuenteIngresos());
                            mov.setMovMontoAprobado(entity.getMovMontoAprobado());

                            mov.setMovTipo(EnumMovimientosTipo.EM);
                            mov.setMovActividadPk(movOri.getMovActividadPk());
                            mov.setMovAreaInversionPk(movOri.getMovAreaInversionPk());
                            mov.setMovSubAreaInversionPk(movOri.getMovSubAreaInversionPk());
                            mov.setMovMonto(entity.getMovMonto());
                            mov.setMovNumMoviento(movOri.getMovNumMoviento());
                            mov = movEdicionBean.guardar(mov, true);
                            //SE SETEAN CON EL DATO DE MOVIMIENTO ORIGINAL
                            entity.setMovEditado(Boolean.TRUE);
                            entity.setMovMonto(movOri.getMovMonto());
                            entity.setMovMontoAprobado(movOri.getMovMontoAprobado());

                            return codDAO.guardar(entity, entity.getMovPk() == null ? ConstantesOperaciones.CREAR_MOVIMIENTO_PRESUPUESTO : ConstantesOperaciones.ACTUALIZAR_MOVIMIENTO_PRESUPUESTO);
                        } else {
                            return codDAO.guardar(entity, null);
                        }
                    }
                } else {
                    if (MovimientosValidacionNegocio.validarIngresos(entity)) {
                        if (entity.getMovNumMoviento() == null) {
                            Query contador = em.createNativeQuery("SELECT MAX(mov_num_movimiento) from finanzas.sg_presupuesto_escolar_movimiento where mov_tipo= 'I' AND mov_presupuesto_fk = :movPresupuestoFk ")
                                    .setParameter("movPresupuestoFk", entity.getMovPresupuestoFk());
                            Integer cont = ((Integer) contador.getSingleResult());
                            if (cont != null) {
                                entity.setMovNumMoviento(cont + 1);
                            } else {
                                entity.setMovNumMoviento(1);
                            }

                        }

                        CodigueraDAO<SgMovimientos> codDAO = new CodigueraDAO<>(em, SgMovimientos.class);
                        if (BooleanUtils.isTrue(dataSecurity)) {
                            //SE BUSCA EL MOV ORIGINAL
                            SgMovimientos movOri = obtenerPorId(entity.getMovPk(), true);
                            //SE CARGAN LOS DATOS DEL MOV ORIGINAL PARA SER EDITADOS
                            SgMovimientosEdicion mov = new SgMovimientosEdicion();
                            mov.setMovOriginalEditado(movOri);
                            mov.setMovPresupuestoFk(movOri.getMovPresupuestoFk());
                            mov.setMovFuenteRecursos(movOri.getMovFuenteRecursos());
                            mov.setMovTipo(EnumMovimientosTipo.IM);
                            mov.setMovMonto(entity.getMovMonto());
                            mov.setMovOrigen(movOri.getMovOrigen().toString());
                            mov.setMovRubroPk(movOri.getMovRubroPk());
                            mov.setMovNumMoviento(movOri.getMovNumMoviento());
                            mov = movEdicionBean.guardar(mov, true);
                            //SE SETEAN CON EL DATO DE MOVIMIENTO ORIGINAL
                            entity.setMovEditado(Boolean.TRUE);
                            entity.setMovMonto(movOri.getMovMonto());

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
     * Guarda el objeto indicado
     *
     * @param entity
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public void importar(String[][] entity) throws GeneralException {
        try {
            if (entity != null) {
                for (String[] fila : entity) {
                    String[] datos = new String[5];
                    datos[0] = fila[0]; // ID
                    datos[1] = fila[1]; // DESCRIPCION
                    datos[2] = fila[2]; // MONTO
                    datos[3] = fila[3]; // PRESUPUESTO

                    SgMovimientos movNuevo = new SgMovimientos();
                    movNuevo.setMovTipo(EnumMovimientosTipo.I);
                    movNuevo.setMovMonto(new BigDecimal(datos[2]));
                    movNuevo.setMovFuenteRecursos(datos[1]);
                    movNuevo.setMovRubroPk(em.getReference(SgRubros.class, new Long(datos[0])));
                    movNuevo.setMovOrigen(EnumMovimientosOrigen.P);
                    movNuevo.setMovPresupuestoFk(em.getReference(SgPresupuestoEscolar.class, new Long(datos[3])));
                    movNuevo = guardar(movNuevo, true);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Guarda el objeto indicado desde una lista
     *
     * @param listDes List<DesembolsoCE>
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void guardarEgresos(EgresoArchivoPost egresoPost, Boolean dataSecurity) throws GeneralException {
        try {
            if (egresoPost != null) {
                SgPresupuestoEscolar presupuesto = em.getReference(SgPresupuestoEscolar.class, egresoPost.getPresupuestoPk());
                if (egresoPost.getEgresos() != null && !egresoPost.getEgresos().isEmpty()) {
                    for (EgresosArchivo egreso : egresoPost.getEgresos()) {
                        SgMovimientos mov = new SgMovimientos();
                        mov.setMovPk(null);
                        mov.setMovActividadPk(em.getReference(SgDetallePlanEscolar.class, egreso.getDetallePaePk()));
                        mov.setMovFuenteRecursos(egreso.getDescripcion());
                        mov.setMovFuenteIngresos(em.getReference(SgMovimientos.class, egreso.getMovFuenteIngresoPk()));
                        mov.setMovAreaInversionPk(em.getReference(SgAreaInversion.class, egreso.getAreaInversionPk()));
                        mov.setMovSubAreaInversionPk(em.getReference(SgAreaInversion.class, egreso.getAreaInversionPk()));
                        mov.setMovMonto(egreso.getMontoFormulado());
                        mov.setMovPresupuestoFk(presupuesto);
                        mov.setMovTipo(EnumMovimientosTipo.E);
                        mov.setMovVersion(0);
                        guardar(mov, true);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Guarda el objeto indicado
     *
     * @param entity
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public void importarEgresos(String[][] entity) throws GeneralException {
        try {
            if (entity != null) {
                for (String[] fila : entity) {
                    String[] datos = new String[7];
                    datos[0] = fila[0]; // Descripcion
                    datos[1] = fila[1]; // Monto
                    datos[2] = fila[2]; // Actividad
                    datos[3] = fila[3]; // Fuente de Recursos
                    datos[4] = fila[4]; // Sub Area de inversion
                    datos[5] = fila[5]; // Presupuesto id

                    FiltroMovimientos movi = new FiltroMovimientos();
                    movi.setMovPresupuestoFk(new Long(datos[5]));
                    movi.setMovTipo(EnumMovimientosTipo.I);
                    movi.setMovNumMoviento(new Integer(datos[3]));
                    movi.setIncluirCampos(new String[]{
                        "movPk",
                        "movTipo",
                        "movVersion",
                        "movNumMoviento",
                        "movFuenteIngresos.movPk",
                        "movFuenteIngresos.movVersion"
                    });
                    movi.setAscending(new boolean[]{true});
                    List<SgMovimientos> fuentes = obtenerPorFiltro(movi);  // Primera lista

                    if (!fuentes.isEmpty()) {
                        SgMovimientos fuente = fuentes.get(0);

                        SgMovimientos newEgreso = new SgMovimientos();
                        newEgreso.setMovTipo(EnumMovimientosTipo.E);
                        newEgreso.setMovFuenteRecursos(new String(datos[0]));
                        newEgreso.setMovMonto(new BigDecimal(datos[1]));
                        newEgreso.setMovActividadPk(em.getReference(SgDetallePlanEscolar.class, new Long(datos[2])));
                        newEgreso.setMovFuenteIngresos(fuente);
                        newEgreso.setMovSubAreaInversionPk(em.getReference(SgAreaInversion.class, new Long(datos[4])));
                        newEgreso.setMovPresupuestoFk(em.getReference(SgPresupuestoEscolar.class, new Long(datos[5])));
                        newEgreso = guardar(newEgreso, true);
                    }

                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

}
