/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
import sv.gob.mined.siges.dto.RequerimientoPorRecurso;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroRequerimientosFondo;
import sv.gob.mined.siges.negocio.validaciones.RequerimientoFondoNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.RequerimientoFondoDAO;
import sv.gob.mined.siges.persistencia.entidades.SgRequerimientoFondo;

/**
 * Servicio que gestiona las solicitudes de transferencias
 *
 * @author Sofis Solutions
 */
@Stateless
public class RequerimientoFondoBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private SeguridadBean seguridadBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgMovimientos
     * @throws GeneralException
     *
     */
    public SgRequerimientoFondo obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgRequerimientoFondo> codDAO = new CodigueraDAO<>(em, SgRequerimientoFondo.class);
                return codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_SOLICITUD_DE_TRANSFERENCIA);
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
                CodigueraDAO<SgRequerimientoFondo> codDAO = new CodigueraDAO<>(em, SgRequerimientoFondo.class);
                if (BooleanUtils.isTrue(dataSecurity)) {
                    return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_SOLICITUD_DE_TRANSFERENCIA);
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
    public SgRequerimientoFondo guardar(SgRequerimientoFondo entity, Boolean dataSecurity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (RequerimientoFondoNegocio.validar(entity)) {
                    CodigueraDAO<SgRequerimientoFondo> codDAO = new CodigueraDAO<>(em, SgRequerimientoFondo.class);
                    if (BooleanUtils.isTrue(dataSecurity)) {

                        return codDAO.guardar(entity, entity.getStrPk() == null ? ConstantesOperaciones.CREAR_SOLICITUD_DE_TRANSFERENCIA : ConstantesOperaciones.ACTUALIZAR_SOLICITUD_DE_TRANSFERENCIA);
                    } else {
                        return codDAO.guardar(entity, null);
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
                CodigueraDAO<SgRequerimientoFondo> codDAO = new CodigueraDAO<>(em, SgRequerimientoFondo.class
                );
                codDAO.eliminarPorId(id, ConstantesOperaciones.ELIMINAR_SOLICITUD_DE_TRANSFERENCIA);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    /**
     * Devuelve si el objeto existe
     *
     * @param filtro
     * @param dataSecurity
     * @param id Long
     * @return Boolean
     * @throws GeneralException
     */
    public List<SgRequerimientoFondo> obtenerPorFiltro(FiltroRequerimientosFondo filtro) throws GeneralException {
        try {
            RequerimientoFondoDAO codDAO = new RequerimientoFondoDAO(em, seguridadBean);
            List<SgRequerimientoFondo> listado = codDAO.obtenerPorFiltro(filtro, ConstantesOperaciones.BUSCAR_SOLICITUD_DE_TRANSFERENCIA);

            for (SgRequerimientoFondo s : listado) {
                if (s.getReqsFondo() != null && !s.getReqsFondo().isEmpty()) {
                    s.getReqsFondo().size();
                }
            }

            return listado;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroRequerimientosFondo filtro) throws GeneralException {
        try {
            RequerimientoFondoDAO codDAO = new RequerimientoFondoDAO(em, seguridadBean);
            return codDAO.obtenerTotalPorFiltro(filtro, ConstantesOperaciones.BUSCAR_SOLICITUD_DE_TRANSFERENCIA);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los requerimientos acumulados por fuente de recurso
     *
     * @param
     * @return List
     * @throws Exception
     */
    public List<RequerimientoPorRecurso> obtenerReqPorRecurso() throws GeneralException {
        try {

            String squery = "select req.str_pk,dep.dep_codigo,dep.dep_nombre,req.str_compromiso_presupuestario,req.str_sac_ufi, " +
                            "req.str_sac_goes,req.str_estado,fin.fue_nombre,fun.fue_id,REGEXP_REPLACE(fun.fue_codigo, '\\s+$', '') codigo, " +
                            "round(sum(tac.tac_monto_autorizado),2) total_recurso " +
                            "from finanzas.sg_req_fond_ced rfc " +
                            "inner join finanzas.sg_requerimientos_fondo req on rfc.rfc_sol_transferencia_fk=req.str_pk " +
                            "inner join finanzas.sg_cuenta_bancaria_dd cbd on req.str_cuenta_banc_dd_fk=cbd.cbd_pk " +
                            "inner join finanzas.sg_direcciones_departamentales dde on cbd.cbd_dde_fk=dde.ded_pk " +
                            "inner join catalogo.sg_departamentos dep on dde.ded_departamento_fk=dep.dep_pk " +
                            "inner join finanzas.sg_transferencias_a_ced tac on rfc.rfc_transferencia_ced_fk=tac.tac_pk " +
                            "inner join siap2.ss_transferencias_componente trc on tac.tac_transferencia_fk=trc.tc_id " +
                            "inner join siap2.ss_fuente_recursos fun on trc.tc_fuente_recursos_fk=fun.fue_id " +
                            "inner join siap2.ss_fuente_financiamiento fin on fun.fue_financiamiento=fin.fue_id " +
                            "where req.str_estado='TRAMITADO_ANTE_HACIENDA' " +
                            "group by (req.str_pk,fin.fue_id,fun.fue_id,dep.dep_pk)";

            Query query = em.createNativeQuery(squery);

            List resultado = query.getResultList();

            List<RequerimientoPorRecurso> respuesta = new ArrayList<>();

            resultado.forEach(
                    z -> {
                        Object[] fila = (Object[]) z;
                        respuesta.add(transformarFilaEnDTO(fila));
                    });

            return respuesta;

        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Transforma una fila en el objeto TransferenciaCedAgrup
     *
     * @param fila
     * @return
     */
    public RequerimientoPorRecurso transformarFilaEnDTO(Object[] fila) {

        RequerimientoPorRecurso ele = new RequerimientoPorRecurso();
        ele.setReqPk(new Long(fila[0].toString()));
        ele.setCodDep(fila[1].toString());
        ele.setNomDep(fila[2].toString());
        ele.setCompromiso(fila[3] != null ? fila[3].toString() : null);
        ele.setSacUfi(fila[4] != null ? fila[4].toString() : null);
        ele.setSacGoes(fila[5] != null ? fila[5].toString() : null);
        ele.setReqEstado(fila[6] != null ? fila[6].toString() : null);
        ele.setFinaciamiento(fila[7] != null ? fila[7].toString() : null);
        ele.setRecursoPk(new Long(fila[8].toString()));
        ele.setRecurso(fila[9].toString());
        ele.setTotalRecurso((BigDecimal) fila[10]);

        return ele;
    }

}
