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
import sv.gob.mined.siges.negocio.validaciones.PreguntaCierreAnioValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgPreguntaCierreAnio;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class PreguntaCierreAnioBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgPreguntaCierreAnio
     * @throws GeneralException
     */
    public SgPreguntaCierreAnio obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgPreguntaCierreAnio> codDAO = new CodigueraDAO<>(em, SgPreguntaCierreAnio.class);
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
                CodigueraDAO<SgPreguntaCierreAnio> codDAO = new CodigueraDAO<>(em, SgPreguntaCierreAnio.class);
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
     * @param pca SgPreguntaCierreAnio
     * @return SgPreguntaCierreAnio
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgPreguntaCierreAnio guardar(SgPreguntaCierreAnio pca) throws GeneralException {
        try {
            if (pca != null) {
                if (PreguntaCierreAnioValidacionNegocio.validar(pca)) {
                    CodigueraDAO<SgPreguntaCierreAnio> codDAO = new CodigueraDAO<>(em, SgPreguntaCierreAnio.class);
                    boolean guardar = true;
                    if (pca.getPcaPk() != null) {
                        //Si el dato ya fue guardado, se determina que haya cambiado alguno de los valores. En caso contrario no se guarda
                        Object valorAnt = ch.obtenerEnVersion(pca.getClass(), pca.getPcaPk() , pca.getPcaVersion());
                        SgPreguntaCierreAnio valorAnterior = (SgPreguntaCierreAnio) valorAnt;
                        guardar = !PreguntaCierreAnioValidacionNegocio.compararParaGrabar(valorAnterior, pca);
                    }
                    if (guardar) {
                        return codDAO.guardar(pca);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return pca;
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
            CodigueraDAO<SgPreguntaCierreAnio> codDAO = new CodigueraDAO<>(em, SgPreguntaCierreAnio.class);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgPreguntaCierreAnio>
     * @throws GeneralException
     */
    public List<SgPreguntaCierreAnio> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgPreguntaCierreAnio> codDAO = new CodigueraDAO<>(em, SgPreguntaCierreAnio.class);
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
                CodigueraDAO<SgPreguntaCierreAnio> codDAO = new CodigueraDAO<>(em, SgPreguntaCierreAnio.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
