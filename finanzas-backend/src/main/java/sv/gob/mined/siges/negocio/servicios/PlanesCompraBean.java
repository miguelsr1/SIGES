/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroEncabezadoPlanCompra;
import sv.gob.mined.siges.negocio.validaciones.PlanesCompraValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.PlanesCompraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgEncabezadoPlanCompras;

/**
 * Servicio de planes de compra
 * @author Sofis Solutions
 */
@Stateless
public class PlanesCompraBean {

    @PersistenceContext
    private EntityManager em;

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroEncabezadoPlanCompra
     * @return Lista de <SgEncabezadoPlanCompras>
     * @throws GeneralException
     */
    public List<SgEncabezadoPlanCompras> obtenerPorFiltro(FiltroEncabezadoPlanCompra filtro) throws GeneralException {
        try {
            PlanesCompraDAO codDAO = new PlanesCompraDAO(em);
            return codDAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgEncabezadoPlanComprass
     * @throws GeneralException
     */
    public SgEncabezadoPlanCompras obtenerPorIdLazy(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgEncabezadoPlanCompras> codDAO = new CodigueraDAO<>(em, SgEncabezadoPlanCompras.class);
                return codDAO.obtenerPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return null;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param entity SgEncabezadoPlanCompras
     * @return SgEncabezadoPlanCompras
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgEncabezadoPlanCompras guardar(SgEncabezadoPlanCompras entity, Boolean dataSecurity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (PlanesCompraValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgEncabezadoPlanCompras> codDAO = new CodigueraDAO<>(em, SgEncabezadoPlanCompras.class);
                    if (BooleanUtils.isTrue(dataSecurity)) {
                        return codDAO.guardar(entity, entity.getPlaPk() == null ? ConstantesOperaciones.CREAR_PLAN_DE_COMPRAS : ConstantesOperaciones.ACTUALIZAR_PLAN_DE_COMPRAS);
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
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroEncabezadoPlanCompra
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroEncabezadoPlanCompra filtro) throws GeneralException {
        try {
            PlanesCompraDAO codDAO = new PlanesCompraDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

}
