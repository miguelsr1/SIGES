/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.BooleanUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DoubleType;
import org.hibernate.type.StringType;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroPromedioCalificaciones;
import sv.gob.mined.siges.filtros.FiltroSistemaIntegrado;
import sv.gob.mined.siges.negocio.validaciones.SistemaIntegradoValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.SistemaIntegradoDAO;
import sv.gob.mined.siges.persistencia.entidades.SgConsultaCalifiacionesAreasBasicas;
import sv.gob.mined.siges.persistencia.entidades.SgSistemaIntegrado;

@Stateless
public class SistemaIntegradoBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    @Inject
    private SistemaSedeBean sistemaSedeBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgSistemaIntegrado
     * @throws GeneralException
     */
    public SgSistemaIntegrado obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgSistemaIntegrado> codDAO = new CodigueraDAO<>(em, SgSistemaIntegrado.class);
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
                CodigueraDAO<SgSistemaIntegrado> codDAO = new CodigueraDAO<>(em, SgSistemaIntegrado.class);
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
     * @param entity SgSistemaIntegrado
     * @return SgSistemaIntegrado
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgSistemaIntegrado guardar(SgSistemaIntegrado entity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (SistemaIntegradoValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgSistemaIntegrado> codDAO = new CodigueraDAO<>(em, SgSistemaIntegrado.class);
                    return codDAO.guardar(entity);
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
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroSistemaIntegrado filtro) throws GeneralException {
        try {
            SistemaIntegradoDAO codDAO = new SistemaIntegradoDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgSistemaIntegrado
     * @throws GeneralException
     */
    public List<SgSistemaIntegrado> obtenerPorFiltro(FiltroSistemaIntegrado filtro) throws GeneralException {
        try {
            SistemaIntegradoDAO codDAO = new SistemaIntegradoDAO(em);
            List<SgSistemaIntegrado> list = codDAO.obtenerPorFiltro(filtro);

            if (BooleanUtils.isTrue(filtro.getIncluirTotalSedes()) && !list.isEmpty()) {
                HashMap<Long, Long> totales = sistemaSedeBean.obtenerTotalPorSistema(list.stream().map(p -> p.getSinPk()).collect(Collectors.toList()));
                list.forEach((l) -> {
                    if (totales.containsKey(l.getSinPk())) {
                        l.setTotalSedes(totales.get(l.getSinPk()));
                    } else {
                        l.setTotalSedes(0L);
                    }
                });
            }

            return list;
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
                CodigueraDAO<SgSistemaIntegrado> codDAO = new CodigueraDAO<>(em, SgSistemaIntegrado.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    public List<SgConsultaCalifiacionesAreasBasicas> obtenerPromedioCalificacionesPorArea(FiltroPromedioCalificaciones filtro) throws GeneralException {
        try {
            Session session = em.unwrap(Session.class);

            // utiliza vista materializada. 
            SQLQuery q = session.createSQLQuery(" select cpe_tipo as tipoComponente, cpe_nombre as nombreComponente,  AVG(avg) as promedioCalificacion"
                    + " from sistemas_integrados.sg_promedio_calificaciones_si "
                    + " where true "
                    + (filtro.getSistemaPk() != null ? " and sin_pk=" + filtro.getSistemaPk() : " ")
                    + (filtro.getOrgCurricularPk()!= null ? " and ocu_pk=" + filtro.getOrgCurricularPk() : " ")
                    + (filtro.getSedePk() != null ? " and sed_pk=" + filtro.getSedePk() : " ")
                    + (filtro.getNivelPk() != null ? " and niv_pk=" + filtro.getNivelPk() : " ")
                    + (filtro.getCicloPk() != null ? " and cic_pk=" + filtro.getCicloPk() : " ")
                    + (filtro.getGradoPk() != null ? " and gra_pk=" + filtro.getGradoPk() : " ")
                    + (filtro.getSedModalidadEducativaPk() != null ? " and mod_pk=" + filtro.getSedModalidadEducativaPk() : " ")
                    + (filtro.getSedOpcionPk() != null ? " and opc_pk=" + filtro.getSedOpcionPk() : " ")
                    + (filtro.getSedModalidadAtencionPk() != null ? " and mat_pk=" + filtro.getSedModalidadAtencionPk() : " ")
                    + (filtro.getSedSubModalidadPk() != null ? " and smo_pk=" + filtro.getSedSubModalidadPk() : " ")
                    + (filtro.getSedProgramaEducativoPk() != null ? " and ped_pk=" + filtro.getSedProgramaEducativoPk() : " ")
                    + " group by cpe_tipo, cpe_pk,cpe_nombre");

            q.addScalar("tipoComponente", new StringType());
            q.addScalar("nombreComponente", new StringType());
            q.addScalar("promedioCalificacion", new DoubleType());
            q.setResultTransformer(Transformers.aliasToBean(SgConsultaCalifiacionesAreasBasicas.class));
            return q.list();
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

    }

}
