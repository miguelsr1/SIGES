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
import sv.gob.mined.siges.negocio.validaciones.InfTipologiaConstruccionValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgInfTipologiaConstruccion;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class InfTipologiaConstruccionBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgInfTipologiaConstruccion
     * @throws GeneralException
     */
    public SgInfTipologiaConstruccion obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgInfTipologiaConstruccion> codDAO = new CodigueraDAO<>(em, SgInfTipologiaConstruccion.class);
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
                CodigueraDAO<SgInfTipologiaConstruccion> codDAO = new CodigueraDAO<>(em, SgInfTipologiaConstruccion.class);
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
     * @param tic SgInfTipologiaConstruccion
     * @return SgInfTipologiaConstruccion
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgInfTipologiaConstruccion guardar(SgInfTipologiaConstruccion tic) throws GeneralException {
        try {
            if (tic != null) {
                if (InfTipologiaConstruccionValidacionNegocio.validar(tic)) {
                    CodigueraDAO<SgInfTipologiaConstruccion> codDAO = new CodigueraDAO<>(em, SgInfTipologiaConstruccion.class);
                    boolean guardar = true;
                    if (tic.getTicPk() != null) {
                        //Si el dato ya fue guardado, se determina que haya cambiado alguno de los valores. En caso contrario no se guarda
                        Object valorAnt = ch.obtenerEnVersion(tic.getClass(), tic.getTicPk() , tic.getTicVersion());
                        SgInfTipologiaConstruccion valorAnterior = (SgInfTipologiaConstruccion) valorAnt;
                        guardar = !InfTipologiaConstruccionValidacionNegocio.compararParaGrabar(valorAnterior, tic);
                    }
                    if (guardar) {
                        return codDAO.guardar(tic);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return tic;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgInfTipologiaConstruccion> codDAO = new CodigueraDAO<>(em, SgInfTipologiaConstruccion.class);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgInfTipologiaConstruccion>
     * @throws GeneralException
     */
    public List<SgInfTipologiaConstruccion> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgInfTipologiaConstruccion> codDAO = new CodigueraDAO<>(em, SgInfTipologiaConstruccion.class);
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
                CodigueraDAO<SgInfTipologiaConstruccion> codDAO = new CodigueraDAO<>(em, SgInfTipologiaConstruccion.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
