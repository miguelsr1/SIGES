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
import sv.gob.mined.siges.dto.SgRegistroDiplomadosSede;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroRelSedeDiplomado;
import sv.gob.mined.siges.negocio.validaciones.RelSedeDiplomadoValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.RelSedeDiplomadoDAO;
import sv.gob.mined.siges.persistencia.entidades.SgDiplomado;
import sv.gob.mined.siges.persistencia.entidades.SgRelSedeDiplomado;
import sv.gob.mined.siges.persistencia.entidades.SgSede;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class RelSedeDiplomadoBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    @Inject
    private SeguridadBean segBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgRelSedeDiplomado
     * @throws GeneralException
     */
    public SgRelSedeDiplomado obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgRelSedeDiplomado> codDAO = new CodigueraDAO<>(em, SgRelSedeDiplomado.class);
                return codDAO.obtenerPorId(id, null);
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
                CodigueraDAO<SgRelSedeDiplomado> codDAO = new CodigueraDAO<>(em, SgRelSedeDiplomado.class);
                return codDAO.objetoExistePorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param rsd SgRelSedeDiplomado
     * @return SgRelSedeDiplomado
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgRelSedeDiplomado guardar(SgRelSedeDiplomado rsd) throws GeneralException {
        try {
            if (rsd != null) {
                if (RelSedeDiplomadoValidacionNegocio.validar(rsd)) {
                    CodigueraDAO<SgRelSedeDiplomado> codDAO = new CodigueraDAO<>(em, SgRelSedeDiplomado.class);

                    return codDAO.guardar(rsd, null);

                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return rsd;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroRelSedeDiplomado filtro) throws GeneralException {
        try {
            RelSedeDiplomadoDAO codDAO = new RelSedeDiplomadoDAO(em, segBean);
            return codDAO.obtenerTotalPorFiltro(filtro, null);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgRelSedeDiplomado>
     * @throws GeneralException
     */
    public List<SgRelSedeDiplomado> obtenerPorFiltro(FiltroRelSedeDiplomado filtro) throws GeneralException {
        try {
            RelSedeDiplomadoDAO codDAO = new RelSedeDiplomadoDAO(em, segBean);
            return codDAO.obtenerPorFiltro(filtro, null);
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
                CodigueraDAO<SgRelSedeDiplomado> codDAO = new CodigueraDAO<>(em, SgRelSedeDiplomado.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    @Interceptors(AuditInterceptor.class)
    public void autorizarRelaciones(SgRegistroDiplomadosSede rsd) throws GeneralException {
        try {
            if (rsd != null) {

                if (RelSedeDiplomadoValidacionNegocio.validar(rsd)) {
                    CodigueraDAO<SgRelSedeDiplomado> codDAO = new CodigueraDAO<>(em, SgRelSedeDiplomado.class);

                    for (Long diplomado : rsd.getDiplomadosPk()) {

                        FiltroRelSedeDiplomado frsd = new FiltroRelSedeDiplomado();
                        frsd.setRsdDiplomadoPk(diplomado);
                        frsd.setRsdSedePk(rsd.getSedePk());

                        List<SgRelSedeDiplomado> rels = this.obtenerPorFiltro(frsd);

                        SgRelSedeDiplomado relSedDip = null;

                        if (rels.isEmpty()) {
                            relSedDip = new SgRelSedeDiplomado();
                            relSedDip.setRsdDiplomadoFk(em.getReference(SgDiplomado.class, diplomado));
                            relSedDip.setRsdSedeFk(em.getReference(SgSede.class, rsd.getSedePk()));

                        } else {
                            relSedDip = rels.get(0);
                        }

                        relSedDip.setRsdNumeroTramite(rsd.getNumTramite());
                        relSedDip.setRsdHabilitado(Boolean.TRUE);

                        codDAO.guardar(relSedDip, null);
                    }

                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

    }

    @Interceptors(AuditInterceptor.class)
    public void desautorizarRelaciones(SgRegistroDiplomadosSede rsd) throws GeneralException {
        try {
            if (rsd != null) {

                if (RelSedeDiplomadoValidacionNegocio.validar(rsd)) {
                    CodigueraDAO<SgRelSedeDiplomado> codDAO = new CodigueraDAO<>(em, SgRelSedeDiplomado.class);

                    for (Long diplomado : rsd.getDiplomadosPk()) {

                        FiltroRelSedeDiplomado frsd = new FiltroRelSedeDiplomado();
                        frsd.setRsdDiplomadoPk(diplomado);
                        frsd.setRsdSedePk(rsd.getSedePk());

                        List<SgRelSedeDiplomado> rels = this.obtenerPorFiltro(frsd);

                        SgRelSedeDiplomado relSedDip = null;

                        if (!rels.isEmpty()) {
                            relSedDip = rels.get(0);
                            relSedDip.setRsdNumeroTramite(rsd.getNumTramite());
                            relSedDip.setRsdHabilitado(Boolean.FALSE);
                            codDAO.guardar(relSedDip, null);
                        }
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

    }

}
