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
import sv.gob.mined.siges.filtros.FiltroDetalleDesembolso;
import sv.gob.mined.siges.negocio.validaciones.DetalleDesembolsoValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.DetalleDesembolsosDAO;
import sv.gob.mined.siges.persistencia.entidades.SgDetalleDesembolso;

/**
 * Servicio que gestiona detalle desembolso
 * @author Sofis Solutions
 */
@Stateless
public class DetalleDesembolsoBean {

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
     * @return SgDetalleDesembolso
     * @throws GeneralException
     */
    public SgDetalleDesembolso obtenerPorId(Long id, Boolean dataSecurity) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgDetalleDesembolso> codDAO = new CodigueraDAO<>(em, SgDetalleDesembolso.class);
                if (BooleanUtils.isTrue(dataSecurity)) {
                    return codDAO.obtenerPorId(id,ConstantesOperaciones.BUSCAR_DETALLE_DESEMBOLSO);
                }
                else{
                    return codDAO.obtenerPorId(id,null);
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
    public Boolean objetoExistePorId(Long id, Boolean dataSecurity) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgDetalleDesembolso> codDAO = new CodigueraDAO<>(em, SgDetalleDesembolso.class);
                if(BooleanUtils.isTrue(dataSecurity)){
                    return codDAO.objetoExistePorId(id,ConstantesOperaciones.BUSCAR_DETALLE_DESEMBOLSO);
                }
                else{
                    return codDAO.objetoExistePorId(id,null);
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
     * @param ded SgDetalleDesembolso
     * @return SgDetalleDesembolso
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgDetalleDesembolso guardar(SgDetalleDesembolso ded,Boolean dataSecurity) throws GeneralException {
        try {
            if (ded != null) {
                if (DetalleDesembolsoValidacionNegocio.validar(ded)) {
                    CodigueraDAO<SgDetalleDesembolso> codDAO = new CodigueraDAO<>(em, SgDetalleDesembolso.class);
                    if (BooleanUtils.isTrue(dataSecurity)) {
                        return codDAO.guardar(ded, ded.getDedPk() == null ? ConstantesOperaciones.CREAR_DETALLE_DESEMBOLSO : ConstantesOperaciones.ACTUALIZAR_DETALLE_DESEMBOLSO);
                    } else {
                        return codDAO.guardar(ded, null);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return ded;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroDetalleDesembolso filtro) throws GeneralException {
        try {
            DetalleDesembolsosDAO codDAO = new DetalleDesembolsosDAO(em,seguridadBean);
            return codDAO.obtenerTotalPorFiltro(filtro,ConstantesOperaciones.BUSCAR_DETALLE_DESEMBOLSO);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgDetalleDesembolso>
     * @throws GeneralException
     */
    public List<SgDetalleDesembolso> obtenerPorFiltro(FiltroDetalleDesembolso filtro) throws GeneralException {
        try {
            DetalleDesembolsosDAO codDAO = new DetalleDesembolsosDAO(em,seguridadBean);
            return codDAO.obtenerPorFiltro(filtro,ConstantesOperaciones.BUSCAR_DETALLE_DESEMBOLSO);
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
                CodigueraDAO<SgDetalleDesembolso> codDAO = new CodigueraDAO<>(em, SgDetalleDesembolso.class);
                codDAO.eliminarPorId(id,ConstantesOperaciones.ELIMINAR_DETALLE_DESEMBOLSO);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
