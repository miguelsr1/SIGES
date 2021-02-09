/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.eclipse.microprofile.opentracing.Traced;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroCalificacionDiplomado;
import sv.gob.mined.siges.filtros.FiltroModuloDiplomado;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.validaciones.ModuloDiplomadoValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.ModuloDiplomadoDAO;
import sv.gob.mined.siges.persistencia.entidades.SgCalificacionDiplomado;
import sv.gob.mined.siges.persistencia.entidades.SgModuloDiplomado;

@Stateless
@Traced
public class ModuloDiplomaBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    @Inject
    private CalificacionDiplomadoBean calificacionDiplomadoBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgModuloDiplomado
     * @throws GeneralException
     */
    public SgModuloDiplomado obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgModuloDiplomado> codDAO = new CodigueraDAO<>(em, SgModuloDiplomado.class);
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
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Boolean objetoExistePorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgModuloDiplomado> codDAO = new CodigueraDAO<>(em, SgModuloDiplomado.class);
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
     * @param entity SgModuloDiplomado
     * @return SgModuloDiplomado
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgModuloDiplomado guardar(SgModuloDiplomado entity) throws GeneralException {
        try {
            if (entity != null) {
                if (ModuloDiplomadoValidacionNegocio.validar(entity)) {
                    if (entity.getMdiPk() != null) {
                        FiltroCalificacionDiplomado fcd = new FiltroCalificacionDiplomado();
                        fcd.setCalModuloDiplomadoFk(entity.getMdiPk());
                        fcd.setIncluirCampos(new String[]{"cadPk"});
                        fcd.setMaxResults(1L);
                        List<SgCalificacionDiplomado> calDip = calificacionDiplomadoBean.obtenerPorFiltro(fcd);

                        if (!calDip.isEmpty()) {
                            BusinessException ge = new BusinessException();
                            ge.addError(Errores.ERROR_MODULO_DIPLOMADO_CALIFICACIONES);
                            throw ge;
                        }
                    }

                    CodigueraDAO<SgModuloDiplomado> codDAO = new CodigueraDAO<>(em, SgModuloDiplomado.class);
                    return codDAO.guardar(entity, null);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
        return entity;
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
                FiltroCalificacionDiplomado fcd = new FiltroCalificacionDiplomado();
                fcd.setCalModuloDiplomadoFk(id);
                fcd.setIncluirCampos(new String[]{"cadPk"});
                List<SgCalificacionDiplomado> calDip = calificacionDiplomadoBean.obtenerPorFiltro(fcd);

                if (!calDip.isEmpty()) {
                    for(SgCalificacionDiplomado cal:calDip){
                        calificacionDiplomadoBean.eliminar(cal.getCadPk());
                    }
                    em.flush();
                    /*
                    BusinessException ge = new BusinessException();
                    ge.addError(Errores.ERROR_MODULO_DIPLOMADO_ELIMINAR_CALIFICACIONES);
                    throw ge;*/
                }

                CodigueraDAO<SgModuloDiplomado> codDAO = new CodigueraDAO<>(em, SgModuloDiplomado.class);
                codDAO.eliminarPorId(id, null);
            } catch (BusinessException be) {
                throw be;
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long obtenerTotalPorFiltro(FiltroModuloDiplomado filtro) throws GeneralException {
        try {
            ModuloDiplomadoDAO codDAO = new ModuloDiplomadoDAO(em);
            return codDAO.cantidadTotal(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgModuloDiplomado
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgModuloDiplomado> obtenerPorFiltro(FiltroModuloDiplomado filtro) throws GeneralException {
        try {
            ModuloDiplomadoDAO codDAO = new ModuloDiplomadoDAO(em);
            return codDAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

}
