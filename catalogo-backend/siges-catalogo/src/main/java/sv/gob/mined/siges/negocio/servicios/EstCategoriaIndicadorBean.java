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
import sv.gob.mined.siges.negocio.validaciones.EstCategoriaIndicadorValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgEstCategoriaIndicador;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class EstCategoriaIndicadorBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgEstCategoriaIndicador
     * @throws GeneralException
     */
    public SgEstCategoriaIndicador obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgEstCategoriaIndicador> codDAO = new CodigueraDAO<>(em, SgEstCategoriaIndicador.class);
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
                CodigueraDAO<SgEstCategoriaIndicador> codDAO = new CodigueraDAO<>(em, SgEstCategoriaIndicador.class);
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
     * @param cin SgEstCategoriaIndicador
     * @return SgEstCategoriaIndicador
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgEstCategoriaIndicador guardar(SgEstCategoriaIndicador cin) throws GeneralException {
        try {
            if (cin != null) {
                if (EstCategoriaIndicadorValidacionNegocio.validar(cin)) {
                    CodigueraDAO<SgEstCategoriaIndicador> codDAO = new CodigueraDAO<>(em, SgEstCategoriaIndicador.class);
                    boolean guardar = true;
                    if (cin.getCinPk() != null) {
                        //Si el dato ya fue guardado, se determina que haya cambiado alguno de los valores. En caso contrario no se guarda
                        Object valorAnt = ch.obtenerEnVersion(cin.getClass(), cin.getCinPk() , cin.getCinVersion());
                        SgEstCategoriaIndicador valorAnterior = (SgEstCategoriaIndicador) valorAnt;
                        guardar = !EstCategoriaIndicadorValidacionNegocio.compararParaGrabar(valorAnterior, cin);
                    }
                    if (guardar) {
                        return codDAO.guardar(cin);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return cin;
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
            CodigueraDAO<SgEstCategoriaIndicador> codDAO = new CodigueraDAO<>(em, SgEstCategoriaIndicador.class);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgEstCategoriaIndicador>
     * @throws GeneralException
     */
    public List<SgEstCategoriaIndicador> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgEstCategoriaIndicador> codDAO = new CodigueraDAO<>(em, SgEstCategoriaIndicador.class);
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
                CodigueraDAO<SgEstCategoriaIndicador> codDAO = new CodigueraDAO<>(em, SgEstCategoriaIndicador.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
