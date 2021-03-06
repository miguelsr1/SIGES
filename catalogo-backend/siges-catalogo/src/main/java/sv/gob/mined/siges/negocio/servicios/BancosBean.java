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
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroCodiguera;
import sv.gob.mined.siges.negocio.validaciones.BancosValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgBancos;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class BancosBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;
    
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgBancos
     * @throws GeneralException
     */
    public SgBancos obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgBancos> codDAO = new CodigueraDAO<>(em, SgBancos.class);
                return codDAO.obtenerPorId(id);
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
                CodigueraDAO<SgBancos> codDAO = new CodigueraDAO<>(em, SgBancos.class);
                return codDAO.objetoExistePorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }


    /**
     * Guarda el objeto indicado
     *
     * @param bnc SgBancos
     * @return SgBancos
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgBancos guardar(SgBancos bnc) throws GeneralException {
        try {
            if (bnc != null) {
                if (BancosValidacionNegocio.validar(bnc)) {
                    CodigueraDAO<SgBancos> codDAO = new CodigueraDAO<>(em, SgBancos.class);
                    codDAO.guardar(bnc);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
        return bnc;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroDocumentos
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgBancos> codDAO = new CodigueraDAO<>(em, SgBancos.class);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroDocumentos
     * @return Lista de <SgBancos>
     * @throws GeneralException
     */
    public List<SgBancos> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgBancos> codDAO = new CodigueraDAO<>(em, SgBancos.class);
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
    public void eliminar(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgBancos> codDAO = new CodigueraDAO<>(em, SgBancos.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
