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
import sv.gob.mined.siges.negocio.validaciones.EstadoDatoContratacionValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgEstadoDatoContratacion;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class EstadoDatoContratacionBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgEstadoDatoContratacion
     * @throws GeneralException
     */
    public SgEstadoDatoContratacion obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgEstadoDatoContratacion> codDAO = new CodigueraDAO<>(em, SgEstadoDatoContratacion.class);
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
                CodigueraDAO<SgEstadoDatoContratacion> codDAO = new CodigueraDAO<>(em, SgEstadoDatoContratacion.class);
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
     * @param edc SgEstadoDatoContratacion
     * @return SgEstadoDatoContratacion
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgEstadoDatoContratacion guardar(SgEstadoDatoContratacion edc) throws GeneralException {
        try {
            if (edc != null) {
                if (EstadoDatoContratacionValidacionNegocio.validar(edc)) {
                    CodigueraDAO<SgEstadoDatoContratacion> codDAO = new CodigueraDAO<>(em, SgEstadoDatoContratacion.class);
                    boolean guardar = true;
                    if (edc.getEdcPk() != null) {
                        //Si el dato ya fue guardado, se determina que haya cambiado alguno de los valores. En caso contrario no se guarda
                        Object valorAnt = ch.obtenerEnVersion(edc.getClass(), edc.getEdcPk() , edc.getEdcVersion());
                        SgEstadoDatoContratacion valorAnterior = (SgEstadoDatoContratacion) valorAnt;
                        guardar = !EstadoDatoContratacionValidacionNegocio.compararParaGrabar(valorAnterior, edc);
                    }
                    if (guardar) {
                        return codDAO.guardar(edc);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return edc;
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
            CodigueraDAO<SgEstadoDatoContratacion> codDAO = new CodigueraDAO<>(em, SgEstadoDatoContratacion.class);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgEstadoDatoContratacion>
     * @throws GeneralException
     */
    public List<SgEstadoDatoContratacion> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgEstadoDatoContratacion> codDAO = new CodigueraDAO<>(em, SgEstadoDatoContratacion.class);
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
                CodigueraDAO<SgEstadoDatoContratacion> codDAO = new CodigueraDAO<>(em, SgEstadoDatoContratacion.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
