/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.dto.SgCabezalEscolaridadEstudiante;
import sv.gob.mined.siges.enumerados.EnumMatriculaEstado;
import sv.gob.mined.siges.enumerados.EnumPromocionGradoMatricula;
import sv.gob.mined.siges.enumerados.EnumPromovidoCalificacion;
import sv.gob.mined.siges.enumerados.EnumSeccionEstado;
import sv.gob.mined.siges.enumerados.EnumTipoPeriodoSeccion;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroEscolaridadEstudiante;
import sv.gob.mined.siges.filtros.FiltroMatricula;
import sv.gob.mined.siges.negocio.validaciones.EscolaridadEstudianteValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.EscolaridadEstudianteDAO;
import sv.gob.mined.siges.persistencia.entidades.SgCalificacionCE;
import sv.gob.mined.siges.persistencia.entidades.SgCalificacionCELite;
import sv.gob.mined.siges.persistencia.entidades.SgEscolaridadEstudiante;
import sv.gob.mined.siges.persistencia.entidades.SgMatricula;
import sv.gob.mined.siges.persistencia.entidades.SgSeccion;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTipoCalendarioEscolar;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class EscolaridadEstudianteBean {

    private static final Logger LOGGER = Logger.getLogger(EscolaridadEstudianteBean.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Inject
    private SeccionBean seccionBean;

    @Inject
    private MatriculaBean matriculaBean;

    @Inject
    private CalificacionBean calificacionBean;

    @Inject
    private ConfiguracionBean configuracionBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgEscolaridadEstudiante
     * @throws GeneralException
     */
    public SgEscolaridadEstudiante obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgEscolaridadEstudiante> codDAO = new CodigueraDAO<>(em, SgEscolaridadEstudiante.class);
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
                CodigueraDAO<SgEscolaridadEstudiante> codDAO = new CodigueraDAO<>(em, SgEscolaridadEstudiante.class);
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
     * @param esc SgEscolaridadEstudiante
     * @return SgEscolaridadEstudiante
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgEscolaridadEstudiante guardar(SgEscolaridadEstudiante esc) throws GeneralException {
        try {
            if (esc != null) {
                if (EscolaridadEstudianteValidacionNegocio.validar(esc)) {
                    CodigueraDAO<SgEscolaridadEstudiante> codDAO = new CodigueraDAO<>(em, SgEscolaridadEstudiante.class);

                    return codDAO.guardar(esc, null);

                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return esc;
    }

    /**
     * Genera escolaridad para matricula
     *
     * @param mat SgMatricula
     * @return SgEscolaridadEstudiante
     * @throws GeneralException
     */
    public SgEscolaridadEstudiante generarEscolaridad(Long matPk) throws GeneralException {
        try {
            if (matPk != null) {

                FiltroMatricula filtro = new FiltroMatricula();
                filtro.setMatPk(matPk);
                filtro.setIncluirCampos(new String[]{
                    "matEstudiante.estPk",
                    "matEstudiante.estVersion",
                    "matSeccion.secAnioLectivo.alePk",
                    "matSeccion.secAnioLectivo.aleVersion",
                    "matPromocionGrado",
                    "matVersion",
                    "matSeccion.secServicioEducativo.sduPk",
                    "matSeccion.secServicioEducativo.sduVersion"
                });

                List<SgMatricula> mats = matriculaBean.obtenerPorFiltro(filtro);

                SgMatricula mat = mats.get(0);

                SgEscolaridadEstudiante escolaridadEst = new SgEscolaridadEstudiante();
                escolaridadEst.setEscEstudiante(mat.getMatEstudiante());
                escolaridadEst.setEscAnio(mat.getMatSeccion().getSecAnioLectivo());
                escolaridadEst.setEscServicioEducativo(mat.getMatSeccion().getSecServicioEducativo());
                escolaridadEst.setEscResultado(EnumPromocionGradoMatricula.PROMOVIDO.equals(mat.getMatPromocionGrado())
                        ? EnumPromovidoCalificacion.PROMOVIDO : EnumPromocionGradoMatricula.NO_PROMOVIDO.equals(mat.getMatPromocionGrado())
                        ? EnumPromovidoCalificacion.NO_PROMOVIDO : null);
                escolaridadEst.setEscMatriculaFk(mat);
                escolaridadEst.setEscCreadoCierre(Boolean.FALSE);
                escolaridadEst.setEscGeneradaPorEquivalencia(Boolean.FALSE);
                return this.guardar(escolaridadEst);

            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
        return null;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param esc SgEscolaridadEstudiante
     * @return SgEscolaridadEstudiante
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgCabezalEscolaridadEstudiante guardarEscolaridades(SgCabezalEscolaridadEstudiante esc) throws GeneralException {
        try {
            if (esc != null) {

                SgTipoCalendarioEscolar tipoEsc = esc.getSeccion().getSecServicioEducativo().getSduSede().getSedTipoCalendario();
                LocalDate cierre;
                
                if(esc.getSeccion().getSecFechaCierreSeccion()==null){
                    if (esc.getSeccion().getSecTipoPeriodo() != null && EnumTipoPeriodoSeccion.COHORTE.equals(esc.getSeccion().getSecTipoPeriodo())
                            && esc.getSeccion().getSecNumeroPeriodo().equals(1)) {
                        cierre = configuracionBean.obtenerFechaCierreMatriculasPorDefecto(esc.getSeccion().getSecAnioLectivo().getAleAnio(), tipoEsc, Boolean.FALSE);
                        cierre = cierre != null ? cierre : LocalDate.now();
                    } else {
                        cierre = configuracionBean.obtenerFechaCierreMatriculasPorDefecto(esc.getSeccion().getSecAnioLectivo().getAleAnio(), tipoEsc, Boolean.TRUE);
                        cierre = cierre != null ? cierre : LocalDate.now();
                    }
                }else{
                    cierre=esc.getSeccion().getSecFechaCierreSeccion();
                }

                List<Long> listEst = esc.getEscolaridadEstudianteList().stream().map(c -> c.getEscEstudiante().getEstPk()).collect(Collectors.toList());
                List<SgMatricula> listMatriculas = new ArrayList();
                if (!listEst.isEmpty()) {
                    FiltroMatricula fm = new FiltroMatricula();
                    fm.setMatEstudiantesPk(listEst);
                    fm.setMatEstado(EnumMatriculaEstado.ABIERTO);
                    fm.setSecPk(esc.getSeccion().getSecPk());
                    listMatriculas = matriculaBean.obtenerPorFiltro(fm);
                }

                List<SgEscolaridadEstudiante> list = new ArrayList();
                for (SgEscolaridadEstudiante escolaridad : esc.getEscolaridadEstudianteList()) {
                    if (EscolaridadEstudianteValidacionNegocio.validar(escolaridad)) {

                        if (!listMatriculas.isEmpty()) {
                            List<SgMatricula> matList = listMatriculas.stream().filter(c -> c.getMatPk().equals(escolaridad.getEscEstudiante().getEstUltimaMatricula().getMatPk())).collect(Collectors.toList());
                            if (!matList.isEmpty()) {
                                SgMatricula mat = matList.get(0);
                                mat.setMatFechaHasta(cierre);
                                mat.setMatEstado(EnumMatriculaEstado.CERRADO);
                                mat.setMatCerradoPorCierreAnio(Boolean.TRUE);
                                mat = matriculaBean.guardar(mat);
                                escolaridad.setEscMatriculaFk(mat);
                            }
                        }
                        SgEscolaridadEstudiante escRet = this.guardar(escolaridad);
                        list.add(escRet);
                    }
                }
                esc.setEscolaridadEstudianteList(list);
                if (BooleanUtils.isTrue(esc.getCerrarSeccion())) {
                    SgSeccion sec = esc.getSeccion();
                    sec.setSecEstado(EnumSeccionEstado.CERRADA);
                    sec.setSecFechaCierreSeccion(cierre);
                    sec = seccionBean.guardar(sec);
                    esc.setSeccion(sec);

                    SgCalificacionCE calCe = esc.getCalificacion();
                    SgCalificacionCELite calLite = new SgCalificacionCELite();
                    calLite.setCalPk(calCe.getCalPk());
                    calLite.setCalVersion(calCe.getCalVersion());
                    calLite.setCalCerrado(Boolean.TRUE);
                    calLite.setCalFecha(LocalDate.now());
                    calLite = calificacionBean.guardarCabezal(calLite);
                    calCe.setCalVersion(calLite.getCalVersion());
                    calCe.setCalCerrado(Boolean.TRUE);
                    esc.setCalificacion(calCe);
                }else{
                    SgSeccion sec = esc.getSeccion();
                    sec.setSecFechaCierreSeccion(cierre);
                    sec = seccionBean.guardar(sec);
                    esc.setSeccion(sec);
                }
                return esc;

            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return esc;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroEscolaridadEstudiante filtro) throws GeneralException {
        try {
            EscolaridadEstudianteDAO codDAO = new EscolaridadEstudianteDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgEscolaridadEstudiante>
     * @throws GeneralException
     */
    public List<SgEscolaridadEstudiante> obtenerPorFiltro(FiltroEscolaridadEstudiante filtro) throws GeneralException {
        try {
            EscolaridadEstudianteDAO codDAO = new EscolaridadEstudianteDAO(em);
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
                CodigueraDAO<SgEscolaridadEstudiante> codDAO = new CodigueraDAO<>(em, SgEscolaridadEstudiante.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
