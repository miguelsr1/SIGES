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
import sv.gob.mined.siges.negocio.validaciones.CategoriaCursoValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgCategoriaCurso;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class CategoriaCursoBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgCategoriaCurso
     * @throws GeneralException
     */
    public SgCategoriaCurso obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgCategoriaCurso> codDAO = new CodigueraDAO<>(em, SgCategoriaCurso.class);
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
                CodigueraDAO<SgCategoriaCurso> codDAO = new CodigueraDAO<>(em, SgCategoriaCurso.class);
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
     * @param ccu SgCategoriaCurso
     * @return SgCategoriaCurso
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgCategoriaCurso guardar(SgCategoriaCurso ccu) throws GeneralException {
        try {
            if (ccu != null) {
                if (CategoriaCursoValidacionNegocio.validar(ccu)) {
                    CodigueraDAO<SgCategoriaCurso> codDAO = new CodigueraDAO<>(em, SgCategoriaCurso.class);
                    boolean guardar = true;
                    if (ccu.getCcuPk() != null) {
                        //Si el dato ya fue guardado, se determina que haya cambiado alguno de los valores. En caso contrario no se guarda
                        Object valorAnt = ch.obtenerEnVersion(ccu.getClass(), ccu.getCcuPk() , ccu.getCcuVersion());
                        SgCategoriaCurso valorAnterior = (SgCategoriaCurso) valorAnt;
                        guardar = !CategoriaCursoValidacionNegocio.compararParaGrabar(valorAnterior, ccu);
                    }
                    if (guardar) {
                        return codDAO.guardar(ccu);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return ccu;
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
            CodigueraDAO<SgCategoriaCurso> codDAO = new CodigueraDAO<>(em, SgCategoriaCurso.class);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgCategoriaCurso>
     * @throws GeneralException
     */
    public List<SgCategoriaCurso> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgCategoriaCurso> codDAO = new CodigueraDAO<>(em, SgCategoriaCurso.class);
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
                CodigueraDAO<SgCategoriaCurso> codDAO = new CodigueraDAO<>(em, SgCategoriaCurso.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
