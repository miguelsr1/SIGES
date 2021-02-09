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
import sv.gob.mined.siges.negocio.validaciones.MotivoAnulacionTituloValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgMotivoAnulacionTitulo;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class MotivoAnulacionTituloBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgMotivoAnulacionTitulo
     * @throws GeneralException
     */
    public SgMotivoAnulacionTitulo obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgMotivoAnulacionTitulo> codDAO = new CodigueraDAO<>(em, SgMotivoAnulacionTitulo.class);
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
                CodigueraDAO<SgMotivoAnulacionTitulo> codDAO = new CodigueraDAO<>(em, SgMotivoAnulacionTitulo.class);
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
     * @param mat SgMotivoAnulacionTitulo
     * @return SgMotivoAnulacionTitulo
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgMotivoAnulacionTitulo guardar(SgMotivoAnulacionTitulo mat) throws GeneralException {
        try {
            if (mat != null) {
                if (MotivoAnulacionTituloValidacionNegocio.validar(mat)) {
                    CodigueraDAO<SgMotivoAnulacionTitulo> codDAO = new CodigueraDAO<>(em, SgMotivoAnulacionTitulo.class);
                    boolean guardar = true;
                    if (mat.getMatPk() != null) {
                        //Si el dato ya fue guardado, se determina que haya cambiado alguno de los valores. En caso contrario no se guarda
                        Object valorAnt = ch.obtenerEnVersion(mat.getClass(), mat.getMatPk() , mat.getMatVersion());
                        SgMotivoAnulacionTitulo valorAnterior = (SgMotivoAnulacionTitulo) valorAnt;
                        guardar = !MotivoAnulacionTituloValidacionNegocio.compararParaGrabar(valorAnterior, mat);
                    }
                    if (guardar) {
                        return codDAO.guardar(mat);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return mat;
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
            CodigueraDAO<SgMotivoAnulacionTitulo> codDAO = new CodigueraDAO<>(em, SgMotivoAnulacionTitulo.class);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgMotivoAnulacionTitulo>
     * @throws GeneralException
     */
    public List<SgMotivoAnulacionTitulo> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgMotivoAnulacionTitulo> codDAO = new CodigueraDAO<>(em, SgMotivoAnulacionTitulo.class);
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
                CodigueraDAO<SgMotivoAnulacionTitulo> codDAO = new CodigueraDAO<>(em, SgMotivoAnulacionTitulo.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
