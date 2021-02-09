/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.eclipse.microprofile.opentracing.Traced;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.dto.SgGradoPlanOrigenDestino;
import sv.gob.mined.siges.enumerados.EnumEstadoSolicitudTraslado;
import sv.gob.mined.siges.enumerados.EnumMatriculaEstado;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroCalendario;
import sv.gob.mined.siges.filtros.FiltroConfirmacionSolicitudTraslado;
import sv.gob.mined.siges.filtros.FiltroMatricula;
import sv.gob.mined.siges.filtros.FiltroSeccion;
import sv.gob.mined.siges.filtros.FiltroSolicitudTraslado;
import sv.gob.mined.siges.filtros.catalogo.FiltroMotivoRetiro;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.validaciones.ConfirmacionSolicitudTrasladoValidacionNegocio;
import sv.gob.mined.siges.negocio.validaciones.SolicitudTrasladoValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.ConfirmacionSolicitudTrasladoDAO;
import sv.gob.mined.siges.persistencia.daos.SolicitudTrasladoDAO;
import sv.gob.mined.siges.persistencia.entidades.SgArchivo;
import sv.gob.mined.siges.persistencia.entidades.SgCalendario;
import sv.gob.mined.siges.persistencia.entidades.SgConfirmacionSolicitudTraslado;
import sv.gob.mined.siges.persistencia.entidades.SgEstudiante;
import sv.gob.mined.siges.persistencia.entidades.SgMatricula;
import sv.gob.mined.siges.persistencia.entidades.SgSeccion;
import sv.gob.mined.siges.persistencia.entidades.SgSolicitudTraslado;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgConfiguracionFirmaElectronica;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgMotivoRetiro;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;
import sv.gob.mined.siges.wsclient.ResultadoValidacion;

@Stateless
@Traced
public class SolicitudTrasladoBean {

    private static final Logger LOGGER = Logger.getLogger(SolicitudTrasladoBean.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    @Inject
    private MatriculaBean matriculaBean;

    @Inject
    private CatalogoBean catalogoBean;

    @Inject
    private CalendarioBean calendarioBean;

    @Inject
    private SeguridadBean seguridadBean;

    @Inject
    private SeccionBean seccionBean;

    @Inject
    private ReglaEquivalenciaBean reglaEquivalenciaBean;

    @Inject
    private FirmaElectronicaBean firmaElectronicaBean;

    @Inject
    private ConfiguracionFirmaElectronicaBean configFirmaElectronicaBean;

    @Inject
    @ConfigProperty(name = "files.tmp.path")
    private String tmpBasePath;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgSolicitudTraslado
     * @throws GeneralException
     */
    public SgSolicitudTraslado obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgSolicitudTraslado> codDAO = new CodigueraDAO<>(em, SgSolicitudTraslado.class);
                SgSolicitudTraslado ret = codDAO.obtenerPorId(id, null);
                if (ret.getSotSedeOrigen() != null) {
                    ret.getSotSedeOrigen().getSedPk();
                }
                if (ret.getSotSedeSolicitante() != null) {
                    ret.getSotSedeSolicitante().getSedPk();
                }
                if (ret.getSotServicioEducativoOrigen() != null) {
                    ret.getSotServicioEducativoOrigen().getSduPk();
                }
                if (ret.getSotServicioEducativoSolicitado() != null) {
                    ret.getSotServicioEducativoSolicitado().getSduPk();
                }
                if (ret.getSotEstudiante() != null) {
                    ret.getSotEstudiante().getEstPk();
                    ret.getSotEstudiante().getEstPersona().getPerPk();
                }
                if (ret.getSotSeccion() != null) {
                    ret.getSotSeccion().getSecPk();
                }
                if (ret.getSotUsuarioSolicitud() != null) {
                    ret.getSotUsuarioSolicitud().getUsuPk();
                }
                if (ret.getSotConfirmacion() != null){
                    ret.getSotConfirmacion().getSotPk();
                }
                return ret;
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
                CodigueraDAO<SgSolicitudTraslado> codDAO = new CodigueraDAO<>(em, SgSolicitudTraslado.class);
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
     * @param entity SgSolicitudTraslado
     * @return SgSolicitudTraslado
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgSolicitudTraslado guardar(SgSolicitudTraslado entity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (SolicitudTrasladoValidacionNegocio.validar(entity)) {

                    //Nuevo
                    if (entity.getSotPk() == null) {
                        //Validar si tiene una solicitud en proceso
                        FiltroSolicitudTraslado fsol = new FiltroSolicitudTraslado();
                        fsol.setPerNie(entity.getSotEstudiante().getEstPersona().getPerNie());
                        fsol.setSolicitudEnProceso(new EnumEstadoSolicitudTraslado[]{EnumEstadoSolicitudTraslado.CONFIRMADO,
                            EnumEstadoSolicitudTraslado.ANULADO,
                            EnumEstadoSolicitudTraslado.RECHAZADA});
                        if (this.obtenerTotalPorFiltro(fsol) > 0) {
                            BusinessException ge = new BusinessException();
                            ge.addError("sotEstudiante", Errores.ERROR_SOLICITUD_TRASLADO_ACTIVA);
                            throw ge;
                        }

                        //Validar que la sede origen sea distinta a la sede destino
                        if (entity.getSotSedeOrigen().equals(entity.getSotSedeSolicitante())) {
                            BusinessException ge = new BusinessException();
                            ge.addError("sotSedeSolicitante", Errores.ERROR_SEDE_IGUAL_TRASLADO);
                            throw ge;
                        }
                    }

                    if (entity.getSotEstado().equals(EnumEstadoSolicitudTraslado.CONFIRMADO)) {
                        //Para confirmar se utilizan nuevos métodos. Preconfirmar y confirmar
                        throw new IllegalStateException();
                    }

                    CodigueraDAO<SgSolicitudTraslado> codDAO = new CodigueraDAO<>(em, SgSolicitudTraslado.class);
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


    private void confirmarSolicitudTraslado(SgConfirmacionSolicitudTraslado confirmacion) throws GeneralException {
        try {
            SgSeccion sec = null;
            SgSolicitudTraslado entity = em.find(SgSolicitudTraslado.class, confirmacion.getSotSolicitudTrasladoPk());
            entity.setSotEstado(EnumEstadoSolicitudTraslado.CONFIRMADO);
            entity.setSotResolucion(confirmacion.getSotResolucion());
            entity.setSotFechaTraslado(confirmacion.getSotFechaTraslado());
            entity.setSotFechaConfirmacion(LocalDateTime.now());
            entity.setSotUsuarioConfirmacion(Lookup.obtenerJWT().getSubject());
            
            if (confirmacion.getSotSeccion() != null && confirmacion.getSotSeccion().getSecPk() != null) {
                FiltroSeccion fs = new FiltroSeccion();
                fs.setIncluirCampos(new String[]{
                    "secVersion",
                    "secAnioLectivo.alePk",
                    "secServicioEducativo.sduGrado.graPk",
                    "secPlanEstudio.pesPk",
                    "secServicioEducativo.sduProgramaEducativo.pedPk",
                    "secServicioEducativo.sduOpcion.opcPk"});
                fs.setSecPk(confirmacion.getSotSeccion().getSecPk());
                fs.setSecurityOperation(null);
                List<SgSeccion> secciones = seccionBean.obtenerPorFiltro(fs);
                if (secciones.isEmpty()) {
                    BusinessException be = new BusinessException();
                    be.addError(Errores.ERROR_SECCION_VACIO);
                    throw be;
                }
                sec = secciones.get(0);
                entity.setSotSeccion(sec);
            }

            if (confirmacion.getSotArchivoFirmado() != null && confirmacion.getSotFirmaTransactionId() != null) {
                entity.setSotFirmada(Boolean.TRUE);

                Path folder = Paths.get(tmpBasePath);
                Path tmpFile = Files.createTempFile(folder, null, ".pdf");
                Files.write(tmpFile, confirmacion.getSotArchivoFirmado());

                SgArchivo ar = new SgArchivo();
                ar.setAchTmpPath(tmpFile.getFileName().toString());
                ar.setAchExt("pdf");
                ar.setAchContentType("application/pdf");
                ar.setAchNombre("confirmar_traslado" + entity.getSotEstudiante().getEstPersona().getPerNie() + "_" + entity.getSotSeccion().getSecCodigo());
                entity.setSotArchivoFirmado(ar);
            }

            

            if (SolicitudTrasladoValidacionNegocio.validar(entity)) {

                //Obtener último calendario
                //Por constraint no puede haber más de un calendario para el mismo año con mismo tipo
                FiltroCalendario filtroCal = new FiltroCalendario();
                if (entity.getSotSedeSolicitante().getSedTipoCalendario() == null) {
                    BusinessException ge = new BusinessException();
                    ge.addError("sotSedeSolicitante", Errores.ERROR_SEDE_SOLICITANTE_SIN_CALENDARIO);
                    throw ge;
                }
                filtroCal.setTipoCalendarioPk(entity.getSotSedeSolicitante().getSedTipoCalendario().getTcePk());
                filtroCal.setOrderBy(new String[]{"calAnioLectivo.aleAnio"});
                filtroCal.setAscending(new boolean[]{false});
                filtroCal.setAnioLectivoFk(sec.getSecAnioLectivo().getAlePk());
                filtroCal.setIncluirCampos(new String[]{"calFechaInicio", "calFechaFin", "calAnioLectivo.aleAnio"});
                filtroCal.setMaxResults(1L);
                List<SgCalendario> calendarios = calendarioBean.obtenerPorFiltro(filtroCal);
                if (!calendarios.isEmpty()) {
                    SgCalendario cal = calendarios.get(0);
                    if (entity.getSotFechaTraslado().isBefore(cal.getCalFechaInicio())
                            || entity.getSotFechaTraslado().isAfter(cal.getCalFechaFin())) {
                        BusinessException ge = new BusinessException();
                        ge.addError("sotSedeSolicitante", Errores.ERROR_FECHA_TRASLADO_FUERA_DE_CALENDARIO_SEDE_SOLICITANTE);
                        throw ge;
                    }
                } else {
                    BusinessException ge = new BusinessException();
                    ge.addError("sotSedeSolicitante", Errores.ERROR_SEDE_SOLICITANTE_SIN_CALENDARIO);
                    throw ge;
                }

                //Buscar la matricula actual
                FiltroMatricula fmat = new FiltroMatricula();
                fmat.setMatEstudiantePk(entity.getSotEstudiante().getEstPk());
                fmat.setMatEstado(EnumMatriculaEstado.ABIERTO);
                fmat.setIncluirCampos(new String[]{"matPk", "matVersion"});
                fmat.setSecurityOperation(null);
                List<SgMatricula> matriculas = matriculaBean.obtenerPorFiltro(fmat);
                SgMatricula matriculaActual;
                SgMatricula matriculaNueva = new SgMatricula();
                if (matriculas.size() == 1) {
                    matriculaActual = matriculaBean.obtenerPorId(matriculas.get(0).getMatPk());
                } else if (matriculas.isEmpty()) {
                    BusinessException ge = new BusinessException();
                    ge.addError("sotSedeSolicitante", Errores.ERROR_MATRICULA_ACTUAL_VACIO);
                    throw ge;
                } else {
                    BusinessException ge = new BusinessException();
                    ge.addError("sotSedeSolicitante", Errores.ERROR_MATRICULA_ABIERTA_MULTIPLE);
                    throw ge;
                }

                //Validar reglas equivalencia
                SgGradoPlanOrigenDestino od = new SgGradoPlanOrigenDestino();

                od.getOrigen().setGrado(matriculaActual.getMatSeccion().getSecServicioEducativo().getSduGrado().getGraPk());
                od.getOrigen().setPlanEstudio(matriculaActual.getMatSeccion().getSecPlanEstudio().getPesPk());
                od.getOrigen().setProgramaEducativo(matriculaActual.getMatSeccion().getSecServicioEducativo().getSduProgramaEducativo() != null ? matriculaActual.getMatSeccion().getSecServicioEducativo().getSduProgramaEducativo().getPedPk() : null);
                od.getOrigen().setOpcion(matriculaActual.getMatSeccion().getSecServicioEducativo().getSduOpcion() != null ? matriculaActual.getMatSeccion().getSecServicioEducativo().getSduOpcion().getOpcPk() : null);

                od.getDestino().setGrado(sec.getSecServicioEducativo().getSduGrado().getGraPk());
                od.getDestino().setPlanEstudio(sec.getSecPlanEstudio().getPesPk());
                od.getDestino().setProgramaEducativo(sec.getSecServicioEducativo().getSduProgramaEducativo() != null ? sec.getSecServicioEducativo().getSduProgramaEducativo().getPedPk() : null);
                od.getDestino().setOpcion(sec.getSecServicioEducativo().getSduOpcion() != null ? sec.getSecServicioEducativo().getSduOpcion().getOpcPk() : null);

                if (!reglaEquivalenciaBean.cumpleReglaEquivalencia(od)) {
                    BusinessException ge = new BusinessException();
                    ge.addError("matEstudiante", Errores.ERROR_MATRICULA_NO_CUMPLE_EQUIVALENCIA);
                    throw ge;
                }

                //Actualiza la matricula actual                
                //Buscar motivo de retiro
                SgMotivoRetiro motivoRetiro;
                FiltroMotivoRetiro fmotivo = new FiltroMotivoRetiro();
                fmotivo.setTraslado(Boolean.TRUE);
                List<SgMotivoRetiro> listMotivo = catalogoBean.buscarMotivoRetiro(fmotivo);
                if (listMotivo.size() == 1) {
                    motivoRetiro = listMotivo.get(0);
                } else {
                    BusinessException ge = new BusinessException();
                    ge.addError("sotSedeSolicitante", Errores.ERROR_MOTIVO_TRASLADO_VACIO);
                    throw ge;
                }

                matriculaActual.setMatEstado(EnumMatriculaEstado.CERRADO);
                matriculaActual.setMatRetirada(Boolean.TRUE);
                matriculaActual.setMatFechaHasta(entity.getSotFechaTraslado().minusDays(1));
                matriculaActual.setMatMotivoRetiro(motivoRetiro);

                //Crear la nueva matricula
                matriculaNueva.setMatEstado(EnumMatriculaEstado.ABIERTO);
                matriculaNueva.setMatEstudiante(new SgEstudiante(entity.getSotEstudiante().getEstPk(), entity.getSotEstudiante().getEstVersion()));
                matriculaNueva.setMatValidacionAcademica(matriculaActual.getMatValidacionAcademica());
                matriculaNueva.setMatRepetidor(matriculaActual.getMatRepetidor());
                matriculaNueva.setMatSeccion(entity.getSotSeccion());
                matriculaNueva.setMatFechaIngreso(entity.getSotFechaTraslado());

                matriculaNueva.setMatProvisional(matriculaActual.getMatProvisional());
                if (matriculaActual.getMatProvisional()) {
                    matriculaActual.setMatProvisional(Boolean.FALSE);
                    matriculaNueva.setMatObservacioneProvisional(matriculaActual.getMatObservacioneProvisional());
                    matriculaActual.setMatObservacioneProvisional(null);
                }

                SgMatricula[][] mats = new SgMatricula[1][2];
                mats[0][0] = matriculaActual;
                mats[0][1] = matriculaNueva;
                //Llamar cambio de seccion
                matriculaBean.cambiarSeccion(mats);

            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

    }

    public void validarPreconfirmacionFirma(SgConfirmacionSolicitudTraslado confirmacion) throws GeneralException {
        try {
            //En ConfirmacionSolicitudTrasladoValidacionNegocio.validar se valida que sotSeccionPk != null y sotSolicitudTrasladoPk != null

            SgSolicitudTraslado entity = em.find(SgSolicitudTraslado.class, confirmacion.getSotSolicitudTrasladoPk());
            em.detach(entity); //Para que no se guarden los cambios automáticamente
            entity.setSotEstado(EnumEstadoSolicitudTraslado.CONFIRMADO);
            entity.setSotResolucion(confirmacion.getSotResolucion());
            entity.setSotFechaTraslado(confirmacion.getSotFechaTraslado());

            FiltroSeccion fs = new FiltroSeccion();
            fs.setIncluirCampos(new String[]{
                "secVersion",
                "secAnioLectivo.alePk",
                "secServicioEducativo.sduGrado.graPk",
                "secPlanEstudio.pesPk",
                "secServicioEducativo.sduProgramaEducativo.pedPk",
                "secServicioEducativo.sduOpcion.opcPk"});
            fs.setSecPk(confirmacion.getSotSeccion().getSecPk());
            fs.setSecurityOperation(null);
            List<SgSeccion> secciones = seccionBean.obtenerPorFiltro(fs);
            if (secciones.isEmpty()) {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_SECCION_VACIO);
                throw be;
            }
            SgSeccion sec = secciones.get(0);

            //Obtener último calendario
            //Por constraint no puede haber más de un calendario para el mismo año con mismo tipo
            FiltroCalendario filtroCal = new FiltroCalendario();
            if (entity.getSotSedeSolicitante().getSedTipoCalendario() == null) {
                BusinessException ge = new BusinessException();
                ge.addError("sotSedeSolicitante", Errores.ERROR_SEDE_SOLICITANTE_SIN_CALENDARIO);
                throw ge;
            }
            filtroCal.setTipoCalendarioPk(entity.getSotSedeSolicitante().getSedTipoCalendario().getTcePk());
            filtroCal.setOrderBy(new String[]{"calAnioLectivo.aleAnio"});
            filtroCal.setAscending(new boolean[]{false});
            filtroCal.setAnioLectivoFk(sec.getSecAnioLectivo().getAlePk());
            filtroCal.setIncluirCampos(new String[]{"calFechaInicio", "calFechaFin", "calAnioLectivo.aleAnio"});
            filtroCal.setMaxResults(1L);
            List<SgCalendario> calendarios = calendarioBean.obtenerPorFiltro(filtroCal);
            if (!calendarios.isEmpty()) {
                SgCalendario cal = calendarios.get(0);
                if (entity.getSotFechaTraslado().isBefore(cal.getCalFechaInicio())
                        || entity.getSotFechaTraslado().isAfter(cal.getCalFechaFin())) {
                    BusinessException ge = new BusinessException();
                    ge.addError("sotSedeSolicitante", Errores.ERROR_FECHA_TRASLADO_FUERA_DE_CALENDARIO_SEDE_SOLICITANTE);
                    throw ge;
                }
            } else {
                BusinessException ge = new BusinessException();
                ge.addError("sotSedeSolicitante", Errores.ERROR_SEDE_SOLICITANTE_SIN_CALENDARIO);
                throw ge;
            }

            //Buscar la matricula actual
            FiltroMatricula fmat = new FiltroMatricula();
            fmat.setMatEstudiantePk(entity.getSotEstudiante().getEstPk());
            fmat.setMatEstado(EnumMatriculaEstado.ABIERTO);
            fmat.setIncluirCampos(new String[]{"matPk", "matVersion"});
            fmat.setSecurityOperation(null);
            List<SgMatricula> matriculas = matriculaBean.obtenerPorFiltro(fmat);
            SgMatricula matriculaActual;
            if (matriculas.size() == 1) {
                matriculaActual = matriculaBean.obtenerPorId(matriculas.get(0).getMatPk());
            } else if (matriculas.isEmpty()) {
                BusinessException ge = new BusinessException();
                ge.addError("sotSedeSolicitante", Errores.ERROR_MATRICULA_ACTUAL_VACIO);
                throw ge;
            } else {
                BusinessException ge = new BusinessException();
                ge.addError("sotSedeSolicitante", Errores.ERROR_MATRICULA_ABIERTA_MULTIPLE);
                throw ge;
            }

            //Validar reglas equivalencia
            SgGradoPlanOrigenDestino od = new SgGradoPlanOrigenDestino();

            od.getOrigen().setGrado(matriculaActual.getMatSeccion().getSecServicioEducativo().getSduGrado().getGraPk());
            od.getOrigen().setPlanEstudio(matriculaActual.getMatSeccion().getSecPlanEstudio().getPesPk());
            od.getOrigen().setProgramaEducativo(matriculaActual.getMatSeccion().getSecServicioEducativo().getSduProgramaEducativo() != null ? matriculaActual.getMatSeccion().getSecServicioEducativo().getSduProgramaEducativo().getPedPk() : null);
            od.getOrigen().setOpcion(matriculaActual.getMatSeccion().getSecServicioEducativo().getSduOpcion() != null ? matriculaActual.getMatSeccion().getSecServicioEducativo().getSduOpcion().getOpcPk() : null);

            od.getDestino().setGrado(sec.getSecServicioEducativo().getSduGrado().getGraPk());
            od.getDestino().setPlanEstudio(sec.getSecPlanEstudio().getPesPk());
            od.getDestino().setProgramaEducativo(sec.getSecServicioEducativo().getSduProgramaEducativo() != null ? sec.getSecServicioEducativo().getSduProgramaEducativo().getPedPk() : null);
            od.getDestino().setOpcion(sec.getSecServicioEducativo().getSduOpcion() != null ? sec.getSecServicioEducativo().getSduOpcion().getOpcPk() : null);

            if (!reglaEquivalenciaBean.cumpleReglaEquivalencia(od)) {
                BusinessException ge = new BusinessException();
                ge.addError("matEstudiante", Errores.ERROR_MATRICULA_NO_CUMPLE_EQUIVALENCIA);
                throw ge;
            }

            FiltroMotivoRetiro fmotivo = new FiltroMotivoRetiro();
            fmotivo.setTraslado(Boolean.TRUE);
            List<SgMotivoRetiro> listMotivo = catalogoBean.buscarMotivoRetiro(fmotivo);
            if (listMotivo.size() != 1) {
                BusinessException ge = new BusinessException();
                ge.addError("sotSedeSolicitante", Errores.ERROR_MOTIVO_TRASLADO_VACIO);
                throw ge;
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    public SgConfirmacionSolicitudTraslado generarConfirmacionSolicitudTraslado(SgConfirmacionSolicitudTraslado confirmacion) throws GeneralException {
        try {
            if (ConfirmacionSolicitudTrasladoValidacionNegocio.validar(confirmacion)) {
                CodigueraDAO<SgConfirmacionSolicitudTraslado> codDAO = new CodigueraDAO<>(em, SgConfirmacionSolicitudTraslado.class);
                confirmacion.setSotFirmada(Boolean.FALSE);
                confirmacion = codDAO.guardar(confirmacion, null);

                //Asociar conf a solicitud
                SgSolicitudTraslado sol = em.find(SgSolicitudTraslado.class, confirmacion.getSotSolicitudTrasladoPk());
                
                //Eliminamos confirmación anterior en caso de que haya quedado alguna asociada
                SgConfirmacionSolicitudTraslado confAnterior = sol.getSotConfirmacion();
                if (confAnterior != null && !confAnterior.getSotPk().equals(confirmacion.getSotPk())){
                    em.remove(confAnterior);
                }
                
                sol.setSotConfirmacion(confirmacion);
                em.merge(sol);
                
                return confirmacion;
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
        return null;
    }

    /**
     * Preconfirma solicitud de traslado si firma activa. En caso contrario
     * confirma.
     *
     * @param confirmacion SgConfirmacionSolicitudTraslado
     * @return SgConfirmacionSolicitudTraslado
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgConfirmacionSolicitudTraslado preconfirmar(SgConfirmacionSolicitudTraslado confirmacion) throws GeneralException {
        try {
            if (confirmacion != null) {
                CloseableHttpClient httpclient = null;
                CloseableHttpResponse response = null;
                int timeout = 2 * 120 * 1000;

                if (confirmacion.getSotPk() == null){
                    throw new IllegalStateException(); //para poder generar el reporte debe estar guardada
                }
                
                if (ConfirmacionSolicitudTrasladoValidacionNegocio.validar(confirmacion)) {

                    SgConfiguracionFirmaElectronica conf = configFirmaElectronicaBean.obtenerPorCodigo(Constantes.CONFIG_FIRMA_ELECTRONICA_CONFIRMACION_TRASLADO);

                    if (conf != null && BooleanUtils.isTrue(conf.getConActivada())) {

                        this.validarPreconfirmacionFirma(confirmacion);

                        CodigueraDAO<SgConfirmacionSolicitudTraslado> codDAO = new CodigueraDAO<>(em, SgConfirmacionSolicitudTraslado.class);

                        SSLContextBuilder builder = new SSLContextBuilder();

                        builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
                        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                                builder.build(), SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

                        RequestConfig config = RequestConfig.custom()
                                .setConnectTimeout(timeout)
                                .setConnectionRequestTimeout(timeout)
                                .setSocketTimeout(timeout).build();

                        httpclient = HttpClients.custom().setDefaultRequestConfig(config).setSSLSocketFactory(
                                sslsf).build();

                        URIBuilder ubuilder = new URIBuilder(System.getProperty("service.report-generator.baseUrl") + "/ma/trasladoConfirmado");
                        ubuilder.setParameter("trasladoId", confirmacion.getSotSolicitudTrasladoPk().toString());
                        HttpGet get = new HttpGet(ubuilder.build());
                        get.addHeader(org.apache.http.HttpHeaders.AUTHORIZATION, "Bearer " + Lookup.obtenerJWT().getRawToken());

                        response = httpclient.execute(get);

                        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                            InputStream is = response.getEntity().getContent();
                            byte[] byteArray = IOUtils.toByteArray(is);

                            List<byte[]> send = new ArrayList<>();
                            send.add(byteArray);

                            String usuarioEnviaYFirma = Lookup.obtenerJWT().getSubject();

                            List<ResultadoValidacion> res = firmaElectronicaBean.enviarDocumentosParaFirmar(send, confirmacion.getSotTransactionReturnUrl(), "confirmar_traslado.pdf", usuarioEnviaYFirma, usuarioEnviaYFirma);

                            if (res == null || res.isEmpty() || !res.get(0).getCode().equalsIgnoreCase("OK")) {
                                BusinessException be = new BusinessException();
                                be.addError("ERROR_AL_ENVIAR_DOCUMENTO");
                                throw be;
                            }

                            confirmacion.setSotFirmaTransactionId(res.get(0).getMessage());
                            confirmacion = codDAO.guardar(confirmacion, null);

                            confirmacion.setSotTransactionSignatureUrl(System.getProperty("service.firmar-documentos.baseUrl") + "?id_transaccion=" + res.get(0).getMessage());
                            return confirmacion;
                        } else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_FORBIDDEN) {
                            BusinessException be = new BusinessException();
                            be.addError(Errores.ERROR_SEGURIDAD);
                            throw be;
                        } else {
                            throw new TechnicalException("ERROR_AL_GENERAR_PFD");
                        }

                    } else {
                        //Confirmar sin firma
                        this.confirmarSolicitudTraslado(confirmacion);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return confirmacion;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param sol SgSolicitudTraslado
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgConfirmacionSolicitudTraslado confirmarFirma(String firmaTransactionId) throws GeneralException {
        try {
            if (firmaTransactionId != null) {
                FiltroConfirmacionSolicitudTraslado fil = new FiltroConfirmacionSolicitudTraslado();
                fil.setFirmaTransactionId(firmaTransactionId);
                fil.setFirmada(Boolean.FALSE);
                fil.setIncluirCampos(new String[]{"sotSolicitudTrasladoPk", "sotSeccion.secPk", "sotFechaTraslado", "sotResolucion", "sotFirmaTransactionId"});
                ConfirmacionSolicitudTrasladoDAO dao = new ConfirmacionSolicitudTrasladoDAO(em);
                List<SgConfirmacionSolicitudTraslado> confs = dao.obtenerPorFiltro(fil);
                
                if (!confs.isEmpty()) {
                    byte[] doc = firmaElectronicaBean.obtenerDocumentoFirmado(firmaTransactionId, "confirmar_traslado.pdf");
                    
                    SgConfirmacionSolicitudTraslado confirmacion = confs.get(0);
                    confirmacion.setSotArchivoFirmado(doc);
                    this.confirmarSolicitudTraslado(confirmacion);
                    
                    //Se hace así para no tener que levantar la sección en memoria, ya que "confirmacion" la utiliza
                    SgConfirmacionSolicitudTraslado confParaGuardar = em.find(SgConfirmacionSolicitudTraslado.class, confirmacion.getSotPk()); 
                    confParaGuardar.setSotFirmada(Boolean.TRUE);
                    confParaGuardar = em.merge(confParaGuardar);
                    
                    return confirmacion;
                } else {
                    BusinessException be = new BusinessException();
                    be.addError(Errores.ERROR_NO_EXISTE_PROMOCION_SIN_FIRMAR_PARA_DATOS_INGRESADOS);
                    throw be;
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
        return null;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long obtenerTotalPorFiltro(FiltroSolicitudTraslado filtro) throws GeneralException {
        try {
            SolicitudTrasladoDAO DAO = new SolicitudTrasladoDAO(em, seguridadBean);
            return DAO.obtenerTotalPorFiltro(filtro, ConstantesOperaciones.BUSCAR_SOLICITUD_TRASLADO);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgSolicitudTraslado
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgSolicitudTraslado> obtenerPorFiltro(FiltroSolicitudTraslado filtro) throws GeneralException {
        try {
            SolicitudTrasladoDAO DAO = new SolicitudTrasladoDAO(em, seguridadBean);
            return DAO.obtenerPorFiltro(filtro, ConstantesOperaciones.BUSCAR_SOLICITUD_TRASLADO);
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
                CodigueraDAO<SgSolicitudTraslado> codDAO = new CodigueraDAO<>(em, SgSolicitudTraslado.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
