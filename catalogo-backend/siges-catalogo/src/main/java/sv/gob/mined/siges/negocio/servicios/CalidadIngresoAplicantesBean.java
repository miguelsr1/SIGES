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
import sv.gob.mined.siges.negocio.validaciones.CalidadIngresoAplicantesValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgCalidadIngresoAplicantes;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class CalidadIngresoAplicantesBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgCalidadIngresoAplicantes
     * @throws GeneralException
     */
    public SgCalidadIngresoAplicantes obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgCalidadIngresoAplicantes> codDAO = new CodigueraDAO<>(em, SgCalidadIngresoAplicantes.class);
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
                CodigueraDAO<SgCalidadIngresoAplicantes> codDAO = new CodigueraDAO<>(em, SgCalidadIngresoAplicantes.class);
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
     * @param cia SgCalidadIngresoAplicantes
     * @return SgCalidadIngresoAplicantes
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgCalidadIngresoAplicantes guardar(SgCalidadIngresoAplicantes cia) throws GeneralException {
        try {
            if (cia != null) {
                if (CalidadIngresoAplicantesValidacionNegocio.validar(cia)) {
                    CodigueraDAO<SgCalidadIngresoAplicantes> codDAO = new CodigueraDAO<>(em, SgCalidadIngresoAplicantes.class);
                    boolean guardar = true;
                    if (cia.getCiaPk() != null) {
                        //Si el dato ya fue guardado, se determina que haya cambiado alguno de los valores. En caso contrario no se guarda
                        Object valorAnt = ch.obtenerEnVersion(cia.getClass(), cia.getCiaPk() , cia.getCiaVersion());
                        SgCalidadIngresoAplicantes valorAnterior = (SgCalidadIngresoAplicantes) valorAnt;
                        guardar = !CalidadIngresoAplicantesValidacionNegocio.compararParaGrabar(valorAnterior, cia);
                    }
                    if (guardar) {
                        return codDAO.guardar(cia);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return cia;
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
            CodigueraDAO<SgCalidadIngresoAplicantes> codDAO = new CodigueraDAO<>(em, SgCalidadIngresoAplicantes.class);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgCalidadIngresoAplicantes>
     * @throws GeneralException
     */
    public List<SgCalidadIngresoAplicantes> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgCalidadIngresoAplicantes> codDAO = new CodigueraDAO<>(em, SgCalidadIngresoAplicantes.class);
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
                CodigueraDAO<SgCalidadIngresoAplicantes> codDAO = new CodigueraDAO<>(em, SgCalidadIngresoAplicantes.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
