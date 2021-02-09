/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import org.eclipse.microprofile.opentracing.Traced;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.BooleanUtils;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroComponenteDocente;
import sv.gob.mined.siges.filtros.FiltroComponentePlanGrado;
import sv.gob.mined.siges.filtros.FiltroHorarioEscolar;
import sv.gob.mined.siges.filtros.FiltroSeccion;
import sv.gob.mined.siges.negocio.validaciones.ComponenteDocenteValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.ComponenteDocenteDAO;
import sv.gob.mined.siges.persistencia.daos.HorarioEscolarDAO;
import sv.gob.mined.siges.persistencia.entidades.SgComponenteDocente;
import sv.gob.mined.siges.persistencia.entidades.SgComponentePlanEstudio;
import sv.gob.mined.siges.persistencia.entidades.SgComponentePlanGrado;
import sv.gob.mined.siges.persistencia.entidades.SgHorarioEscolar;
import sv.gob.mined.siges.persistencia.entidades.SgHorarioEscolarLite;
import sv.gob.mined.siges.persistencia.entidades.SgSeccion;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
@Traced
public class ComponenteDocenteBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private HorarioEscolarBean horarioEscolarBean;

    @Inject
    private SeccionBean seccionBean;

    @Inject
    private ComponentePlanGradoBean componentePlanGradoBean;
    
    @Inject
    private SeguridadBean seguridadBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgComponenteDocente
     * @throws GeneralException
     */
    public SgComponenteDocente obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgComponenteDocente> codDAO = new CodigueraDAO<>(em, SgComponenteDocente.class);
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
                CodigueraDAO<SgComponenteDocente> codDAO = new CodigueraDAO<>(em, SgComponenteDocente.class);
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
     * @param cal SgComponenteDocente
     * @return SgComponenteDocente
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgComponenteDocente guardar(SgComponenteDocente cal) throws GeneralException {
        try {
            if (cal != null) {
                if (ComponenteDocenteValidacionNegocio.validar(cal)) {

                    if (cal.getCdoHorarioEscolar().getHesDocente() != null) {
                        SgHorarioEscolarLite horarioEscolar = new SgHorarioEscolarLite();
                        horarioEscolar.setHesPk(cal.getCdoHorarioEscolar().getHesPk());
                        horarioEscolar.setHesUnicoDocente(Boolean.FALSE);
                        horarioEscolar.setHesDocente(null);
                        horarioEscolar.setHesVersion(cal.getCdoHorarioEscolar().getHesVersion());
                        SgHorarioEscolarLite horario = horarioEscolarBean.guardarLite(horarioEscolar);
                        cal.getCdoHorarioEscolar().setHesVersion(horario.getHesVersion());
                        horarioEscolarBean.crearUsuarioRol(cal.getCdoHorarioEscolar().getHesPk(), cal.getCdoDocente().getPsePk(), Boolean.TRUE, null);
                    } else {
                        if (cal.getCdoDocente().getPsePk().equals(cal.getEliminarContextoDePersonalPk())) {
                            horarioEscolarBean.crearUsuarioRol(cal.getCdoHorarioEscolar().getHesPk(), cal.getCdoDocente().getPsePk(), Boolean.FALSE, null);
                        } else {
                            horarioEscolarBean.crearUsuarioRol(cal.getCdoHorarioEscolar().getHesPk(), cal.getCdoDocente().getPsePk(), Boolean.FALSE, cal.getEliminarContextoDePersonalPk());
                        }
                    }
                    CodigueraDAO<SgComponenteDocente> codDAO = new CodigueraDAO<>(em, SgComponenteDocente.class);
                    SgComponenteDocente doc = codDAO.guardar(cal, null);
                    return doc;

                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return cal;
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
                FiltroComponenteDocente fcd = new FiltroComponenteDocente();
                fcd.setCdoPk(id);
                fcd.setIncluirCampos(new String[]{"cdoHorarioEscolar.hesPk", "cdoDocente.psePersona.perDui"});
                List<SgComponenteDocente> compDocente = obtenerPorFiltro(fcd);
                if (!compDocente.isEmpty()) {
                    horarioEscolarBean.eliminarUsuarioRol(compDocente.get(0).getCdoHorarioEscolar().getHesPk(), compDocente.get(0).getCdoDocente());
                }

                CodigueraDAO<SgComponenteDocente> codDAO = new CodigueraDAO<>(em, SgComponenteDocente.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    @Interceptors(AuditInterceptor.class)
    public void eliminarElemento(SgComponenteDocente compDoc) throws GeneralException {
        if (compDoc != null) {
            try {
                FiltroComponenteDocente fcd = new FiltroComponenteDocente();
                fcd.setCdoHorarioEscolar(compDoc.getCdoHorarioEscolar().getHesPk());
                fcd.setCdoDocente(compDoc.getCdoDocente().getPsePk());
                Long cantidad = obtenerTotalPorFiltro(fcd);
                if (cantidad <= 1) {
                    horarioEscolarBean.eliminarUsuarioRol(compDoc.getCdoHorarioEscolar().getHesPk(), compDoc.getCdoDocente());
                }

                CodigueraDAO<SgComponenteDocente> codDAO = new CodigueraDAO<>(em, SgComponenteDocente.class);
                codDAO.eliminarPorId(compDoc.getCdoPk(), null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroComponenteDocente filtro) throws GeneralException {
        try {
            ComponenteDocenteDAO codDAO = new ComponenteDocenteDAO(em);
            return codDAO.cantidadTotal(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro
     * @return List
     * @throws GeneralException
     */
    public List<SgComponenteDocente> obtenerPorFiltro(FiltroComponenteDocente filtro) throws GeneralException {
        try {
            ComponenteDocenteDAO codDAO = new ComponenteDocenteDAO(em);
            return codDAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    public List<SgComponentePlanEstudio> obtenerComponentesAsociadosDocenteEnSeccion(Long seccion) throws GeneralException {
        try {

            List<SgComponentePlanEstudio> compPlanEst = new ArrayList();
            JsonWebToken jwt = Lookup.obtenerJWT();

            BigInteger personalPk = (BigInteger) em.createNativeQuery("select usu_personal_pk from seguridad.sg_usuarios where usu_codigo = :codigo")
                    .setParameter("codigo", jwt.getSubject())
                    .getSingleResult();
            if (personalPk != null && seccion != null) {
                FiltroHorarioEscolar fhe = new FiltroHorarioEscolar();
                fhe.setHesSeccion(seccion);
                fhe.setIncluirCampos(new String[]{"hesUnicoDocente", "hesDocente.psePk"});
                HorarioEscolarDAO codDAO = new HorarioEscolarDAO(em, seguridadBean);
                List<SgHorarioEscolar> horarioEsc = codDAO.obtenerPorFiltro(fhe, null);
                if (horarioEsc != null && !horarioEsc.isEmpty()) {
                    if (BooleanUtils.isTrue(horarioEsc.get(0).getHesUnicoDocente())) {
                        if (horarioEsc.get(0).getHesDocente() != null && horarioEsc.get(0).getHesDocente().getPsePk().equals(personalPk.longValue())) {                 
                            FiltroSeccion fs = new FiltroSeccion();
                            fs.setSecPk(seccion);
                            fs.setIncluirCampos(new String[]{"secServicioEducativo.sduGrado.graPk", "secPlanEstudio.pesPk"});
                            fs.setSecurityOperation(null);
                            List<SgSeccion> listSeccion = seccionBean.obtenerPorFiltro(fs);
                            if (listSeccion != null && !listSeccion.isEmpty()) {
                                FiltroComponentePlanGrado fpg = new FiltroComponentePlanGrado();
                                fpg.setCpgGradoPk(listSeccion.get(0).getSecServicioEducativo().getSduGrado().getGraPk());
                                fpg.setCpgPlanEstudioPk(listSeccion.get(0).getSecPlanEstudio().getPesPk());
                                fpg.setCpgAgregandoSeccionExclusiva(listSeccion.get(0).getSecPk());
                                fpg.setCpgCalificacionIngresadaCE(Boolean.TRUE);
                                fpg.setIncluirCampos(new String[]{
                                    "cpgComponentePlanEstudio.cpePk",
                                    "cpgComponentePlanEstudio.cpeCodigo",
                                    "cpgComponentePlanEstudio.cpeNombre",
                                    "cpgComponentePlanEstudio.cpeTipo",
                                    "cpgComponentePlanEstudio.cpeVersion",
                                    "cpgComponentePlanEstudio.cpeCodigoExterno",
                                    "cpgComponentePlanEstudio.cpeNombrePublicable"});
                                List<SgComponentePlanGrado> listCpg = componentePlanGradoBean.obtenerPorFiltro(fpg);                        
                                if (listCpg != null && !listCpg.isEmpty()) {
                                    compPlanEst = listCpg.stream().map(c -> c.getCpgComponentePlanEstudio()).collect(Collectors.toList());
                                }
                            }
                        }
                    } else {
                        FiltroComponenteDocente fcd = new FiltroComponenteDocente();
                        fcd.setCdoDocente(personalPk.longValue());
                        fcd.setCdoSeccion(seccion);
                        fcd.setIncluirCampos(new String[]{"cdoComponente.cpePk", "cdoComponente.cpeTipo", "cdoComponente.cpeNombre", "cdoComponente.cpeVersion"});
                        List<SgComponenteDocente> compDoc = obtenerPorFiltro(fcd);
                        compPlanEst = compDoc.stream().map(c -> c.getCdoComponente()).collect(Collectors.toList());
                    }
                }

            }

            return compPlanEst;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

}
