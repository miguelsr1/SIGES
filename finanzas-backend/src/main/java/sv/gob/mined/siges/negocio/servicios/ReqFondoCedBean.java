/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.dto.DetalleRequerimientoFondo;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroReqFondoCed;
import sv.gob.mined.siges.negocio.validaciones.ReqFondoCedValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.ReqFondoCedDAO;
import sv.gob.mined.siges.persistencia.entidades.SgReqFondoCed;

/**
 * Servicio que gestiona los requerimientos de fondos
 *
 * @author Sofis Solutions
 */
@Stateless
public class ReqFondoCedBean {

    private static final Logger LOGGER = Logger.getLogger(ReqFondoCedBean.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    @Inject
    private SeguridadBean seguridadBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgReqFondoCed
     * @throws GeneralException
     */
    public SgReqFondoCed obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgReqFondoCed> codDAO = new CodigueraDAO<>(em, SgReqFondoCed.class);
                return codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_REQUERIMIENTO_FONDO_A_CED);
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
    public Boolean objetoExistePorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgReqFondoCed> codDAO = new CodigueraDAO<>(em, SgReqFondoCed.class);
                return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_REQUERIMIENTO_FONDO_A_CED);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param rfc SgReqFondoCed
     * @return SgReqFondoCed
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgReqFondoCed guardar(SgReqFondoCed rfc, Boolean dataSecurity) throws GeneralException {
        try {
            if (rfc != null) {
                if (ReqFondoCedValidacionNegocio.validar(rfc)) {
                    CodigueraDAO<SgReqFondoCed> codDAO = new CodigueraDAO<>(em, SgReqFondoCed.class);
                    if (BooleanUtils.isTrue(dataSecurity)) {
                        return codDAO.guardar(rfc, rfc.getRfcPk() == null ? ConstantesOperaciones.CREAR_REQUERIMIENTO_FONDO_A_CED : ConstantesOperaciones.ACTUALIZAR_REQUERIMIENTO_FONDO_A_CED);
                    } else {
                        return codDAO.guardar(rfc, null);
                    }
                }
            }
        } catch (BusinessException be) {
            LOGGER.log(Level.SEVERE, be.getMessage(), be);
            throw be;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            throw new TechnicalException(ex);
        }

        return rfc;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroReqFondoCed filtro) throws GeneralException {
        try {
            ReqFondoCedDAO codDAO = new ReqFondoCedDAO(em, seguridadBean);
            return codDAO.obtenerTotalPorFiltro(filtro, ConstantesOperaciones.BUSCAR_REQUERIMIENTO_FONDO_A_CED);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgReqFondoCed>
     * @throws GeneralException
     */
    public List<SgReqFondoCed> obtenerPorFiltro(FiltroReqFondoCed filtro) throws GeneralException {
        try {
            ReqFondoCedDAO codDAO = new ReqFondoCedDAO(em, seguridadBean);
            return codDAO.obtenerPorFiltro(filtro, ConstantesOperaciones.BUSCAR_REQUERIMIENTO_FONDO_A_CED);
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
    public void eliminar(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgReqFondoCed> codDAO = new CodigueraDAO<>(em, SgReqFondoCed.class);
                codDAO.eliminarPorId(id, ConstantesOperaciones.ELIMINAR_REQUERIMIENTO_FONDO_A_CED);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }
    
    
        
    /**
     * Devuelve los requerimientos de sedes con su monto por fuente de ingreso
     * contexto de seguridad.
     *
     * @param reqId Long
     * @return
     * @throws DAOGeneralException
     */
    public List<DetalleRequerimientoFondo> obtenerDetalleReqFondo(Long reqId) throws GeneralException {
        try {
            String query = "select row_number() OVER (PARTITION BY true) as id,rel.sed_codigo,rel.sed_nombre, ROUND (sum(rel.fondo),2) as fondo, ROUND(sum(rel.prestamo),2) as prestamo, ROUND (SUM(COALESCE(rel.fondo,0) + COALESCE(rel.prestamo,0)),2) as total " +
                            "from " +
                            "(select sed_codigo,sed_nombre,case when fue_codigo='01' then tac_monto_autorizado else ROUND(0.00,2) end as fondo, " +
                            "case when fue_codigo='03' then tac_monto_autorizado else ROUND(0.00,2) end as prestamo from finanzas.detalle_req_fondo_view " +
                            "where rfc_sol_transferencia_fk="+ reqId+"" +
                            ") rel " +
                            "group by rel.sed_codigo,rel.sed_nombre";

            Query detalleReqFondo = em.createNativeQuery(query);
            
            List resultado = detalleReqFondo.getResultList();
            
            List<DetalleRequerimientoFondo> respuesta = new ArrayList<>();

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
     * Transforma una fila en el objeto DetalleRequerimientoFondo
     * @param fila
     * @return
     */
    public DetalleRequerimientoFondo transformarFilaEnDTO(Object[] fila) {

        DetalleRequerimientoFondo ele = new DetalleRequerimientoFondo();
        ele.setCorrelativo(fila[0].toString());
        ele.setCodSede(fila[1].toString());
        ele.setNomSede(fila[2].toString());
        ele.setMontoFondo((BigDecimal) fila[3]);
        ele.setMontoPrestamo((BigDecimal) fila[4]);
        ele.setMontoTotal((BigDecimal) fila[5]);
        
        return ele;
    }

}
