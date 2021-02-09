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
import sv.gob.mined.siges.filtros.FiltroTipoRepresentante;
import sv.gob.mined.siges.negocio.validaciones.TipoRepresentanteValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.TipoRepresentanteDAO;
import sv.gob.mined.siges.persistencia.entidades.SgTipoRepresentante;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class TipoRepresentanteBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgTipoRepresentante
     * @throws GeneralException
     */
    public SgTipoRepresentante obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgTipoRepresentante> codDAO = new CodigueraDAO<>(em, SgTipoRepresentante.class);
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
                CodigueraDAO<SgTipoRepresentante> codDAO = new CodigueraDAO<>(em, SgTipoRepresentante.class);
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
     * @param tre SgTipoRepresentante
     * @return SgTipoRepresentante
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgTipoRepresentante guardar(SgTipoRepresentante tre) throws GeneralException {
        try {
            if (tre != null) {
                if (TipoRepresentanteValidacionNegocio.validar(tre)) {
                    CodigueraDAO<SgTipoRepresentante> codDAO = new CodigueraDAO<>(em, SgTipoRepresentante.class);
                    boolean guardar = true;
                    if (tre.getTrePk() != null) {
                        //Si el dato ya fue guardado, se determina que haya cambiado alguno de los valores. En caso contrario no se guarda
                        Object valorAnt = ch.obtenerEnVersion(tre.getClass(), tre.getTrePk() , tre.getTreVersion());
                        SgTipoRepresentante valorAnterior = (SgTipoRepresentante) valorAnt;
                        guardar = !TipoRepresentanteValidacionNegocio.compararParaGrabar(valorAnterior, tre);
                    }
                    if (guardar) {
                        return codDAO.guardar(tre);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return tre;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroTipoRepresentante
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroTipoRepresentante filtro) throws GeneralException {
        try {
            TipoRepresentanteDAO dao = new TipoRepresentanteDAO(em);
            return dao.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroTipoRepresentante
     * @return Lista de <SgTipoRepresentante>
     * @throws GeneralException
     */
    public List<SgTipoRepresentante> obtenerPorFiltro(FiltroTipoRepresentante filtro) throws GeneralException {
        try {
            TipoRepresentanteDAO dao = new TipoRepresentanteDAO(em);
            return dao.obtenerPorFiltro(filtro);
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
                CodigueraDAO<SgTipoRepresentante> codDAO = new CodigueraDAO<>(em, SgTipoRepresentante.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
