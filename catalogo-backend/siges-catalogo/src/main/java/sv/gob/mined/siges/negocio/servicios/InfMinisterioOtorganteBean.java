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
import sv.gob.mined.siges.negocio.validaciones.InfMinisterioOtorganteValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgInfMinisterioOtorgante;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class InfMinisterioOtorganteBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgInfMinisterioOtorgante
     * @throws GeneralException
     */
    public SgInfMinisterioOtorgante obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgInfMinisterioOtorgante> codDAO = new CodigueraDAO<>(em, SgInfMinisterioOtorgante.class);
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
                CodigueraDAO<SgInfMinisterioOtorgante> codDAO = new CodigueraDAO<>(em, SgInfMinisterioOtorgante.class);
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
     * @param mio SgInfMinisterioOtorgante
     * @return SgInfMinisterioOtorgante
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgInfMinisterioOtorgante guardar(SgInfMinisterioOtorgante mio) throws GeneralException {
        try {
            if (mio != null) {
                if (InfMinisterioOtorganteValidacionNegocio.validar(mio)) {
                    CodigueraDAO<SgInfMinisterioOtorgante> codDAO = new CodigueraDAO<>(em, SgInfMinisterioOtorgante.class);
                    boolean guardar = true;
                    if (mio.getMioPk() != null) {
                        //Si el dato ya fue guardado, se determina que haya cambiado alguno de los valores. En caso contrario no se guarda
                        Object valorAnt = ch.obtenerEnVersion(mio.getClass(), mio.getMioPk() , mio.getMioVersion());
                        SgInfMinisterioOtorgante valorAnterior = (SgInfMinisterioOtorgante) valorAnt;
                        guardar = !InfMinisterioOtorganteValidacionNegocio.compararParaGrabar(valorAnterior, mio);
                    }
                    if (guardar) {
                        return codDAO.guardar(mio);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return mio;
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
            CodigueraDAO<SgInfMinisterioOtorgante> codDAO = new CodigueraDAO<>(em, SgInfMinisterioOtorgante.class);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgInfMinisterioOtorgante>
     * @throws GeneralException
     */
    public List<SgInfMinisterioOtorgante> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgInfMinisterioOtorgante> codDAO = new CodigueraDAO<>(em, SgInfMinisterioOtorgante.class);
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
                CodigueraDAO<SgInfMinisterioOtorgante> codDAO = new CodigueraDAO<>(em, SgInfMinisterioOtorgante.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
