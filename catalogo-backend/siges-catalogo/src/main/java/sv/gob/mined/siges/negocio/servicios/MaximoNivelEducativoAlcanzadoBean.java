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
import sv.gob.mined.siges.negocio.validaciones.MaximoNivelEducativoAlcanzadoValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgMaximoNivelEducativoAlcanzado;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class MaximoNivelEducativoAlcanzadoBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgMaximoNivelEducativoAlcanzado
     * @throws GeneralException
     */
    public SgMaximoNivelEducativoAlcanzado obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgMaximoNivelEducativoAlcanzado> codDAO = new CodigueraDAO<>(em, SgMaximoNivelEducativoAlcanzado.class);
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
                CodigueraDAO<SgMaximoNivelEducativoAlcanzado> codDAO = new CodigueraDAO<>(em, SgMaximoNivelEducativoAlcanzado.class);
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
     * @param mne SgMaximoNivelEducativoAlcanzado
     * @return SgMaximoNivelEducativoAlcanzado
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgMaximoNivelEducativoAlcanzado guardar(SgMaximoNivelEducativoAlcanzado mne) throws GeneralException {
        try {
            if (mne != null) {
                if (MaximoNivelEducativoAlcanzadoValidacionNegocio.validar(mne)) {
                    CodigueraDAO<SgMaximoNivelEducativoAlcanzado> codDAO = new CodigueraDAO<>(em, SgMaximoNivelEducativoAlcanzado.class);
                    boolean guardar = true;
                    if (mne.getMnePk() != null) {
                        //Si el dato ya fue guardado, se determina que haya cambiado alguno de los valores. En caso contrario no se guarda
                        Object valorAnt = ch.obtenerEnVersion(mne.getClass(), mne.getMnePk() , mne.getMneVersion());
                        SgMaximoNivelEducativoAlcanzado valorAnterior = (SgMaximoNivelEducativoAlcanzado) valorAnt;
                        guardar = !MaximoNivelEducativoAlcanzadoValidacionNegocio.compararParaGrabar(valorAnterior, mne);
                    }
                    if (guardar) {
                        return codDAO.guardar(mne);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return mne;
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
            CodigueraDAO<SgMaximoNivelEducativoAlcanzado> codDAO = new CodigueraDAO<>(em, SgMaximoNivelEducativoAlcanzado.class);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgMaximoNivelEducativoAlcanzado>
     * @throws GeneralException
     */
    public List<SgMaximoNivelEducativoAlcanzado> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgMaximoNivelEducativoAlcanzado> codDAO = new CodigueraDAO<>(em, SgMaximoNivelEducativoAlcanzado.class);
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
                CodigueraDAO<SgMaximoNivelEducativoAlcanzado> codDAO = new CodigueraDAO<>(em, SgMaximoNivelEducativoAlcanzado.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
