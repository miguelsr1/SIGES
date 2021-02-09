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
import javax.persistence.Query;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.enumerados.EnumMatriculaEstado;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroDiplomadosEstudiante;
import sv.gob.mined.siges.filtros.FiltroEstudiante;
import sv.gob.mined.siges.filtros.FiltroMatricula;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.validaciones.DiplomadosEstudianteValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.DiplomadosEstudianteDAO;
import sv.gob.mined.siges.persistencia.entidades.SgDiplomadosEstudiante;
import sv.gob.mined.siges.persistencia.entidades.SgEstudiante;
import sv.gob.mined.siges.persistencia.entidades.SgMatricula;

@Stateless
@Traced
public class DiplomadosEstudianteBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    EstudianteBean estudianteBean;

    @Inject
    private MatriculaBean matriculaBean;

    @Inject
    private SeguridadBean seguridadBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgDiplomadosEstudiante
     * @throws GeneralException
     */
    public SgDiplomadosEstudiante obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgDiplomadosEstudiante> codDAO = new CodigueraDAO<>(em, SgDiplomadosEstudiante.class);
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
                CodigueraDAO<SgDiplomadosEstudiante> codDAO = new CodigueraDAO<>(em, SgDiplomadosEstudiante.class);
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
     * @param entity SgDiplomadosEstudiante
     * @return SgDiplomadosEstudiante
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgDiplomadosEstudiante guardar(SgDiplomadosEstudiante entity) throws GeneralException {
        try {
            if (entity != null) {
                BusinessException be = new BusinessException();

                if (DiplomadosEstudianteValidacionNegocio.validar(entity)) {
                    if (entity.getDepPk() == null) {
                        if (entity.getDepNieSeleccionado() != null) {
                            if (entity.getDepSedeFk() != null) {
                                FiltroEstudiante filtro = new FiltroEstudiante();
                                filtro.setNie(entity.getDepNieSeleccionado());
                                filtro.setIncluirCampos(new String[]{"estPk", "estVersion"});
                                List<SgEstudiante> listaEstudiantes = estudianteBean.obtenerPorFiltro(filtro);
                                if (!listaEstudiantes.isEmpty()) {
                                    SgEstudiante est = listaEstudiantes.get(0);
                                    FiltroMatricula fm = new FiltroMatricula();
                                    fm.setMatEstudiantePk(est.getEstPk());
                                    fm.setMatEstado(EnumMatriculaEstado.ABIERTO);
                                    fm.setIncluirCampos(new String[]{"matSeccion.secServicioEducativo.sduSede.sedPk",
                                        "matSeccion.secServicioEducativo.sduSede.sedTipo",
                                        "matSeccion.secServicioEducativo.sduSede.sedVersion",
                                        "matVersion"});
                                    List<SgMatricula> matList = matriculaBean.obtenerPorFiltro(fm);

                                    if (!matList.isEmpty()) {
                                        entity.setDepEstudiante(est);
                                        if (!matList.get(0).getMatSeccion().getSecServicioEducativo().getSduSede().getSedPk().equals(entity.getDepSedeFk().getSedPk())) {
                                            be.addError(Errores.ERROR_SEDE_NO_COINCIDE_ULTIMA_MATRICULA_CERRADA);
                                            throw be;
                                        }
                                    } else {
                                        be.addError(Errores.ERROR_MATRICULA_YA_CERRADA);
                                        throw be;
                                    }

                                } else {
                                    be.addError(Errores.ERROR_NIE_ESTUDIANTE_NO_EXISTE);
                                    throw be;
                                }
                            } else {
                                be.addError(Errores.ERROR_SEDE_VACIO);
                                throw be;
                            }
                        } else {
                            be.addError(Errores.ERROR_NIE_VACIO);
                            throw be;

                        }
                    }

                    CodigueraDAO<SgDiplomadosEstudiante> codDAO = new CodigueraDAO<>(em, SgDiplomadosEstudiante.class);
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
                FiltroDiplomadosEstudiante fde = new FiltroDiplomadosEstudiante();
                fde.setDiplomadoEstudianteId(id);
                fde.setIncluirCampos(new String[]{"depAnioLectivo.alePk",
                    "depEstudiante.estPk",
                    "depSedeFk.sedPk",
                    "depDiplomado.dipPk"
                });

                List<SgDiplomadosEstudiante> dipEst = this.obtenerPorFiltro(fde);
                if (!dipEst.isEmpty()) {
                    SgDiplomadosEstudiante dipEstE = dipEst.get(0);
                    if (dipEstE.getDepEstudiante().getEstPk() != null
                            && dipEstE.getDepDiplomado().getDipPk() != null
                            && dipEstE.getDepAnioLectivo().getAlePk() != null
                            && dipEstE.getDepSedeFk().getSedPk() != null) {
                        Query q = em.createNativeQuery("delete from centros_educativos.sg_calificacion_diplomado_estudiante calestd\n"
                                + "  where calestd.cde_calificacion_diplomado_fk in (select cald.cad_pk from centros_educativos.sg_calificacion_diplomado cald\n"
                                + "  inner join centros_educativos.sg_modulos_diplomado mdip on mdip.mdi_pk=cald.cad_modulo_diplomado_fk\n"
                                + "  inner join centros_educativos.sg_diplomados dip on dip.dip_pk=mdip.mdi_diplomado_fk\n"
                                + "  	where cald.cad_anio_lectivo_fk=:anio and cald.cad_sede_fk=:sede and dip.dip_pk=:diplomado\n"
                                + "  ) and calestd.cde_estudiante_fk=:estudiante");
                        q.setParameter("anio", dipEstE.getDepAnioLectivo().getAlePk());
                        q.setParameter("sede", dipEstE.getDepSedeFk().getSedPk());
                        q.setParameter("diplomado", dipEstE.getDepDiplomado().getDipPk());
                        q.setParameter("estudiante", dipEstE.getDepEstudiante().getEstPk());
                        q.executeUpdate();
                    }
                }

                CodigueraDAO<SgDiplomadosEstudiante> codDAO = new CodigueraDAO<>(em, SgDiplomadosEstudiante.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroDiplomadosEstudiante
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroDiplomadosEstudiante filtro) throws GeneralException {
        try {
            DiplomadosEstudianteDAO codDAO = new DiplomadosEstudianteDAO(em, seguridadBean);
            return codDAO.obtenerTotalPorFiltro(filtro, ConstantesOperaciones.BUSCAR_INSCRIPCION_DIPLOMADO);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroDiplomadosEstudiante
     * @return Lista de SgDiplomadosEstudiante
     * @throws GeneralException
     */
    public List<SgDiplomadosEstudiante> obtenerPorFiltro(FiltroDiplomadosEstudiante filtro) throws GeneralException {
        try {
            DiplomadosEstudianteDAO codDAO = new DiplomadosEstudianteDAO(em, seguridadBean);
            return codDAO.obtenerPorFiltro(filtro, ConstantesOperaciones.BUSCAR_INSCRIPCION_DIPLOMADO);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

}
