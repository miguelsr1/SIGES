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
import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroTipoAcuerdo;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.validaciones.TipoAcuerdoValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.TipoAcuerdoDAO;
import sv.gob.mined.siges.persistencia.entidades.SgTipoAcuerdo;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class TipoAcuerdoBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgTipoAcuerdo
     * @throws GeneralException
     */
    public SgTipoAcuerdo obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgTipoAcuerdo> codDAO = new CodigueraDAO<>(em, SgTipoAcuerdo.class);
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
                CodigueraDAO<SgTipoAcuerdo> codDAO = new CodigueraDAO<>(em, SgTipoAcuerdo.class);
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
     * @param tao SgTipoAcuerdo
     * @return SgTipoAcuerdo
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgTipoAcuerdo guardar(SgTipoAcuerdo tao) throws GeneralException {
        try {
            if (tao != null) {
                if (TipoAcuerdoValidacionNegocio.validar(tao)) {
                    CodigueraDAO<SgTipoAcuerdo> codDAO = new CodigueraDAO<>(em, SgTipoAcuerdo.class);
                    boolean guardar = true;

                    FiltroTipoAcuerdo filtro = new FiltroTipoAcuerdo();
                    filtro.setExcluirTipoAcuerdoPk(tao.getTaoPk());

                    Boolean aplicaATramite = Boolean.FALSE;

                    if (BooleanUtils.isTrue(tao.getTaoTramiteAmpliacionServicios())) {
                        filtro.setTaoTramiteAmpliacionServicios(Boolean.TRUE);
                        aplicaATramite = Boolean.TRUE;
                    }
                    if (BooleanUtils.isTrue(tao.getTaoTramiteCambioDomicilioSede())) {
                        filtro.setTaoTramiteCambioDomicilioSede(Boolean.TRUE);
                        aplicaATramite = Boolean.TRUE;
                    }
                    if (BooleanUtils.isTrue(tao.getTaoTramiteCreacionSede())) {
                        filtro.setTaoTramiteCreacionSede(Boolean.TRUE);
                        aplicaATramite = Boolean.TRUE;
                    }
                    if (BooleanUtils.isTrue(tao.getTaoTramiteDisminucionServicios())) {
                        filtro.setTaoTramiteDisminucionServicios(Boolean.TRUE);
                        aplicaATramite = Boolean.TRUE;
                    }
                    if (BooleanUtils.isTrue(tao.getTaoTramiteNominacionSede())) {
                        filtro.setTaoTramiteNominacionSede(Boolean.TRUE);
                        aplicaATramite = Boolean.TRUE;
                    }
                    if (BooleanUtils.isTrue(tao.getTaoTramiteRevocatoriaSede())) {
                        filtro.setTaoTramiteRevocatoriaSede(Boolean.TRUE);
                        aplicaATramite = Boolean.TRUE;
                    }

                    if (aplicaATramite) {
                        filtro.setRealizarORConTramites(Boolean.TRUE);

                        if (this.obtenerTotalPorFiltro(filtro) > 0L) {
                            BusinessException be = new BusinessException();
                            be.addError(Errores.ERROR_TIPO_ACUERDO_APLICA_A_TRAMITE_EXISTENTE);
                            throw be;
                        }
                    }

                    if (tao.getTaoPk() != null) {
                        //Si el dato ya fue guardado, se determina que haya cambiado alguno de los valores. En caso contrario no se guarda
                        Object valorAnt = ch.obtenerEnVersion(tao.getClass(), tao.getTaoPk(), tao.getTaoVersion());
                        SgTipoAcuerdo valorAnterior = (SgTipoAcuerdo) valorAnt;
                        guardar = !TipoAcuerdoValidacionNegocio.compararParaGrabar(valorAnterior, tao);
                    }
                    if (guardar) {
                        return codDAO.guardar(tao);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return tao;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroTipoAcuerdo filtro) throws GeneralException {
        try {
            TipoAcuerdoDAO codDAO = new TipoAcuerdoDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgTipoAcuerdo>
     * @throws GeneralException
     */
    public List<SgTipoAcuerdo> obtenerPorFiltro(FiltroTipoAcuerdo filtro) throws GeneralException {
        try {
            TipoAcuerdoDAO codDAO = new TipoAcuerdoDAO(em);
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
                CodigueraDAO<SgTipoAcuerdo> codDAO = new CodigueraDAO<>(em, SgTipoAcuerdo.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
