/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import org.eclipse.microprofile.opentracing.Traced;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroDiploma;
import sv.gob.mined.siges.filtros.FiltroDiplomado;
import sv.gob.mined.siges.filtros.FiltroDiplomadosEstudiante;
import sv.gob.mined.siges.filtros.FiltroModuloDiplomado;
import sv.gob.mined.siges.filtros.FiltroRelSedeDiplomado;
import sv.gob.mined.siges.negocio.validaciones.DiplomadoValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.DiplomaDAO;
import sv.gob.mined.siges.persistencia.daos.DiplomadoDAO;
import sv.gob.mined.siges.persistencia.daos.DiplomadosEstudianteDAO;
import sv.gob.mined.siges.persistencia.entidades.SgDiplomado;
import sv.gob.mined.siges.persistencia.entidades.SgModuloDiplomado;
import sv.gob.mined.siges.persistencia.entidades.SgDiplomadosEstudiante;
import sv.gob.mined.siges.persistencia.entidades.SgRelSedeDiplomado;
import sv.gob.mined.siges.persistencia.entidades.SgDiploma;

@Stateless
@Traced
public class DiplomadoBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private SeguridadBean seguridadBean;

    @Inject
    private ModuloDiplomaBean moduloDiplomadoBean;

    @Inject
    private DiplomaEstudianteBean diplomaEstudianteBean;

    @Inject
    private DiplomaBean diplomaBean;

    @Inject
    private RelSedeDiplomadoBean relSedeDiplomadoBean;

    @Inject
    private DiplomadosEstudianteBean diplomadoEstudianteBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgDiplomado
     * @throws GeneralException
     */
    public SgDiplomado obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgDiplomado> codDAO = new CodigueraDAO<>(em, SgDiplomado.class);
                SgDiplomado dip = codDAO.obtenerPorId(id, null);
                if (dip != null) {
                    if (dip.getDipModulosDiplomado() != null) {
                        dip.getDipModulosDiplomado().size();
                    }
                }
                return dip;
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
                CodigueraDAO<SgDiplomado> codDAO = new CodigueraDAO<>(em, SgDiplomado.class);
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
     * @param entity SgDiplomado
     * @return SgDiplomado
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgDiplomado guardar(SgDiplomado entity) throws GeneralException {
        try {
            if (entity != null) {
                if (DiplomadoValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgDiplomado> codDAO = new CodigueraDAO<>(em, SgDiplomado.class);
                    SgDiplomado dip = codDAO.guardar(entity, null);
                    return dip;
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
                FiltroModuloDiplomado fmd = new FiltroModuloDiplomado();
                fmd.setDiplomadoFk(id);
                fmd.setIncluirCampos(new String[]{"mdiPk"});
                List<SgModuloDiplomado> listMdi = moduloDiplomadoBean.obtenerPorFiltro(fmd);

                if (listMdi != null && !listMdi.isEmpty()) {
                    for (SgModuloDiplomado mdi : listMdi) {
                        moduloDiplomadoBean.eliminar(mdi.getMdiPk());
                    }
                }
                em.flush();

                FiltroDiplomadosEstudiante fde = new FiltroDiplomadosEstudiante();
                fde.setDiplomadoId(id);
                fde.setIncluirCampos(new String[]{"depPk"});
                DiplomadosEstudianteDAO codDAODipEst = new DiplomadosEstudianteDAO(em, seguridadBean);
                List<SgDiplomadosEstudiante> dipEstudiantesList = codDAODipEst.obtenerPorFiltro(fde, null);
                if (dipEstudiantesList != null && !dipEstudiantesList.isEmpty()) {
                    for (SgDiplomadosEstudiante dipEst : dipEstudiantesList) {
                        CodigueraDAO<SgDiplomadosEstudiante> codDAO = new CodigueraDAO<>(em, SgDiplomadosEstudiante.class);
                        codDAO.eliminarPorId(dipEst.getDepPk(), null);
                    }
                }
                em.flush();

                FiltroRelSedeDiplomado frsd = new FiltroRelSedeDiplomado();
                frsd.setRsdDiplomadoPk(id);
                frsd.setIncluirCampos(new String[]{"rsdPk"});
                List<SgRelSedeDiplomado> listRelSede = relSedeDiplomadoBean.obtenerPorFiltro(frsd);
                if (listRelSede != null && !listRelSede.isEmpty()) {
                    for (SgRelSedeDiplomado rel : listRelSede) {
                        CodigueraDAO<SgRelSedeDiplomado> codDAO = new CodigueraDAO<>(em, SgRelSedeDiplomado.class);
                        codDAO.eliminarPorId(rel.getRsdPk(), null);
                    }
                }
                em.flush();

                FiltroDiploma fd = new FiltroDiploma();
                fd.setDiplomadoFk(id);
                fd.setIncluirCampos(new String[]{"dilPk"});
                DiplomaDAO codDAODiploma = new DiplomaDAO(em, seguridadBean);
                List<SgDiploma> listDiploma = codDAODiploma.obtenerPorFiltro(fd, null);
                if (listDiploma != null && !listDiploma.isEmpty()) {
                    for (SgDiploma dip : listDiploma) {
                        CodigueraDAO<SgDiploma> codDAO = new CodigueraDAO<>(em, SgDiploma.class);
                        codDAO.eliminarPorId(dip.getDilPk(), null);
                    }
                }
                em.flush();

                CodigueraDAO<SgDiplomado> codDAO = new CodigueraDAO<>(em, SgDiplomado.class);
                codDAO.eliminarPorId(id, null);
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
    public Long obtenerTotalPorFiltro(FiltroDiplomado filtro) throws GeneralException {
        try {
            DiplomadoDAO codDAO = new DiplomadoDAO(em, seguridadBean);
            return codDAO.obtenerTotalPorFiltro(filtro, null);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgDiplomado
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgDiplomado> obtenerPorFiltro(FiltroDiplomado filtro) throws GeneralException {
        try {
            DiplomadoDAO codDAO = new DiplomadoDAO(em, seguridadBean);
            return codDAO.obtenerPorFiltro(filtro, null);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    public List<SgDiplomado> obtenerDiplomadosAutorizados(Long sedPk) {
        try {
            DiplomadoDAO codDAO = new DiplomadoDAO(em, seguridadBean);
            return codDAO.obtenerDiplomadosAutorizados(sedPk);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    public List<SgDiplomado> obtenerDiplomadosParaAutorizacion(Long sedPk) {
        try {
            DiplomadoDAO codDAO = new DiplomadoDAO(em, seguridadBean);
            return codDAO.obtenerDiplomadosParaAutorizacion(sedPk);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

}
