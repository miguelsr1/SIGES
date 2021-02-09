/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroTransferenciaDireccionDep;
import sv.gob.mined.siges.negocio.validaciones.TransferenciaDireccionDepValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.TransferenciaDireccionDepDAO;
import sv.gob.mined.siges.persistencia.entidades.SgTransferenciaDireccionDep;

/**
 * Servicio que gestiona las trasnferencias a las direcciones departamentales
 *
 * @author Sofis Solutions
 */
@Stateless
public class TransferenciaDireccionDepBean {

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
     * @return SgTransferenciaDireccionDep
     * @throws GeneralException
     */
    public SgTransferenciaDireccionDep obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgTransferenciaDireccionDep> codDAO = new CodigueraDAO<>(em, SgTransferenciaDireccionDep.class);
                return codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_TRANSFERENCIA_DIRECCION_DEPARTAMENTAL);
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
                CodigueraDAO<SgTransferenciaDireccionDep> codDAO = new CodigueraDAO<>(em, SgTransferenciaDireccionDep.class);
                return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_TRANSFERENCIA_DIRECCION_DEPARTAMENTAL);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param tdd SgTransferenciaDireccionDep
     * @return SgTransferenciaDireccionDep
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgTransferenciaDireccionDep guardar(SgTransferenciaDireccionDep entity, Boolean dataSecurity) throws GeneralException {
        try {
            if (entity != null) {
                if (TransferenciaDireccionDepValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgTransferenciaDireccionDep> codDAO = new CodigueraDAO<>(em, SgTransferenciaDireccionDep.class);
                    if (BooleanUtils.isTrue(dataSecurity)) {
                        return codDAO.guardar(entity, entity.getTddPk() == null ? ConstantesOperaciones.CREAR_TRANSFERENCIA_DIRECCION_DEPARTAMENTAL : ConstantesOperaciones.ACTUALIZAR_TRANSFERENCIA_DIRECCION_DEPARTAMENTAL);
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
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroTransferenciaDireccionDep filtro) throws GeneralException {
        try {
            TransferenciaDireccionDepDAO transDAO = new TransferenciaDireccionDepDAO(em, seguridadBean);
            return transDAO.obtenerTotalPorFiltro(filtro, ConstantesOperaciones.BUSCAR_TRANSFERENCIA_DIRECCION_DEPARTAMENTAL);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgTransferenciaDireccionDep>
     * @throws GeneralException
     */
    public List<SgTransferenciaDireccionDep> obtenerPorFiltro(FiltroTransferenciaDireccionDep filtro) throws GeneralException {
        try {
            TransferenciaDireccionDepDAO transDAO = new TransferenciaDireccionDepDAO(em, seguridadBean);
            return transDAO.obtenerPorFiltro(filtro, ConstantesOperaciones.BUSCAR_TRANSFERENCIA_DIRECCION_DEPARTAMENTAL);
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
                CodigueraDAO<SgTransferenciaDireccionDep> codDAO = new CodigueraDAO<>(em, SgTransferenciaDireccionDep.class);
                codDAO.eliminarPorId(id, ConstantesOperaciones.ELIMINAR_TRANSFERENCIA_DIRECCION_DEPARTAMENTAL);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
