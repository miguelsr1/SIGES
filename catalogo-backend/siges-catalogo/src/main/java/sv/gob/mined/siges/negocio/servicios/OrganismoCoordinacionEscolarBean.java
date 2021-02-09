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
import sv.gob.mined.siges.negocio.validaciones.OrganismoCoordinacionEscolarValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgOrganismoCoordinacionEscolar;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class OrganismoCoordinacionEscolarBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgOrganismoCoordinacionEscolar
     * @throws GeneralException
     */
    public SgOrganismoCoordinacionEscolar obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgOrganismoCoordinacionEscolar> codDAO = new CodigueraDAO<>(em, SgOrganismoCoordinacionEscolar.class);
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
                CodigueraDAO<SgOrganismoCoordinacionEscolar> codDAO = new CodigueraDAO<>(em, SgOrganismoCoordinacionEscolar.class);
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
     * @param oce SgOrganismoCoordinacionEscolar
     * @return SgOrganismoCoordinacionEscolar
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgOrganismoCoordinacionEscolar guardar(SgOrganismoCoordinacionEscolar oce) throws GeneralException {
        try {
            if (oce != null) {
                if (OrganismoCoordinacionEscolarValidacionNegocio.validar(oce)) {
                    CodigueraDAO<SgOrganismoCoordinacionEscolar> codDAO = new CodigueraDAO<>(em, SgOrganismoCoordinacionEscolar.class);
                    boolean guardar = true;
                    if (oce.getOcePk() != null) {
                        //Si el dato ya fue guardado, se determina que haya cambiado alguno de los valores. En caso contrario no se guarda
                        Object valorAnt = ch.obtenerEnVersion(oce.getClass(), oce.getOcePk() , oce.getOceVersion());
                        SgOrganismoCoordinacionEscolar valorAnterior = (SgOrganismoCoordinacionEscolar) valorAnt;
                        guardar = !OrganismoCoordinacionEscolarValidacionNegocio.compararParaGrabar(valorAnterior, oce);
                    }
                    if (guardar) {
                        return codDAO.guardar(oce);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return oce;
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
            CodigueraDAO<SgOrganismoCoordinacionEscolar> codDAO = new CodigueraDAO<>(em, SgOrganismoCoordinacionEscolar.class);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgOrganismoCoordinacionEscolar>
     * @throws GeneralException
     */
    public List<SgOrganismoCoordinacionEscolar> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgOrganismoCoordinacionEscolar> codDAO = new CodigueraDAO<>(em, SgOrganismoCoordinacionEscolar.class);
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
                CodigueraDAO<SgOrganismoCoordinacionEscolar> codDAO = new CodigueraDAO<>(em, SgOrganismoCoordinacionEscolar.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
