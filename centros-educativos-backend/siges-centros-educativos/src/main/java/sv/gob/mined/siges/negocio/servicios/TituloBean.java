/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.enumerados.EnumEstadoSolicitudImpresion;
import sv.gob.mined.siges.enumerados.EnumTipoSolicitudImpresion;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroCodiguera;
import sv.gob.mined.siges.filtros.FiltroEstudianteImpresion;
import sv.gob.mined.siges.filtros.FiltroSolicitudImpresion;
import sv.gob.mined.siges.filtros.FiltroTitulo;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.validaciones.TituloValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.TituloDAO;
import sv.gob.mined.siges.persistencia.entidades.SgEstudianteImpresion;
import sv.gob.mined.siges.persistencia.entidades.SgSede;
import sv.gob.mined.siges.persistencia.entidades.SgSolicitudImpresion;
import sv.gob.mined.siges.persistencia.entidades.SgSolicitudImpresionTituloLite;
import sv.gob.mined.siges.persistencia.entidades.SgTitulo;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgConfiguracion;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;
import sv.gob.mined.siges.rest.AreaRecurso;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class TituloBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    @Inject
    private ConfiguracionBean configuracionBean;

    @Inject
    private SolicitudImpresionBean SolicitudImpresionBean;

    @Inject
    private EstudianteImpresionBean estudianteImpresionBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgTitulo
     * @throws GeneralException
     */
    public SgTitulo obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgTitulo> codDAO = new CodigueraDAO<>(em, SgTitulo.class);
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
                CodigueraDAO<SgTitulo> codDAO = new CodigueraDAO<>(em, SgTitulo.class);
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
     * @param tit SgTitulo
     * @return SgTitulo
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgTitulo guardar(SgTitulo tit) throws GeneralException {
        try {
            if (tit != null) {
                if (TituloValidacionNegocio.validar(tit)) {
                    FiltroCodiguera fc = new FiltroCodiguera();
                    fc.setCodigoExacto(Constantes.TITULO_FECHA_VALIDEZ_DESDE);
                    List<SgConfiguracion> conf = configuracionBean.obtenerPorFiltro(fc);
                    LocalDate fechaValidez = null;
                    if (!conf.isEmpty()) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MM-yyyy");
                        fechaValidez = LocalDate.parse(conf.get(0).getConValor(), formatter);
                    }
                    tit.setTitFechaValidez(fechaValidez);
                    CodigueraDAO<SgTitulo> codDAO = new CodigueraDAO<>(em, SgTitulo.class);

                    return codDAO.guardar(tit, null);

                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return tit;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroTitulo filtro) throws GeneralException {
        try {
            TituloDAO codDAO = new TituloDAO(em);
            return codDAO.cantidadTotal(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgTitulo>
     * @throws GeneralException
     */
    public List<SgTitulo> obtenerPorFiltro(FiltroTitulo filtro) throws GeneralException {
        try {
            TituloDAO codDAO = new TituloDAO(em);
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
                CodigueraDAO<SgTitulo> codDAO = new CodigueraDAO<>(em, SgTitulo.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    private static final Logger LOGGER = Logger.getLogger(AreaRecurso.class.getName());

    /**
     * Guarda el objeto indicado
     *
     * @param sim SgSolicitudImpresion
     * @return SgSolicitudImpresion
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgSolicitudImpresion crearTitulosDeSolicitud(SgSolicitudImpresion sim) throws GeneralException {
        try {
            if (sim != null && sim.getSimPk() != null) {

                //TODO: 
                //Agregar lazyToOne en seccion de Solicitud
                //Agregar lazyToOne en estudiante de SgEstudianteImpresion
                //Sacar el sim.getSimEstudianteImpresion().size() de obtenerporid de solicitud. Es una lista muy grande con muchas entidades grandes. 
                FiltroSolicitudImpresion fsi = new FiltroSolicitudImpresion();
                fsi.setSimPk(sim.getSimPk());
                fsi.setIncluirCampos(new String[]{"simFechaSolicitud",
                    "simFechaEnviadoImprimir",
                    "simEstado",
                    "simTipoImpresion",
                    "simSeccion.secPk",
                    "simSeccion.secVersion",
                    "simSeccion.secServicioEducativo.sduOpcion.opcNombre",
                    "simSeccion.secServicioEducativo.sduOpcion.opcSectorEconomico.secNombre",
                    "simSeccion.secServicioEducativo.sduProgramaEducativo.pedNombre",
                    "simSeccion.secAnioLectivo.aleAnio",
                    "simSeccion.secServicioEducativo.sduSede.sedCodigo",
                    "simSeccion.secServicioEducativo.sduSede.sedPk",
                    "simSeccion.secServicioEducativo.sduSede.sedVersion",
                    "simSeccion.secServicioEducativo.sduSede.sedTipo",
                    "simSeccion.secServicioEducativo.sduSede.sedNombre",
                    "simSeccion.secServicioEducativo.sduSede.sedSedeAdscritaDe.sedPk",
                    "simSeccion.secServicioEducativo.sduSede.sedSedeAdscritaDe.sedCodigo",
                    "simSeccion.secServicioEducativo.sduSede.sedSedeAdscritaDe.sedNombre",
                    "simSeccion.secServicioEducativo.sduSede.sedSedeAdscritaDe.sedTipo",
                    "simSeccion.secServicioEducativo.sduSede.sedSedeAdscritaDe.sedVersion",
                    "simSeccion.secServicioEducativo.sduPk",
                    "simSeccion.secServicioEducativo.sduVersion",
                    "simUsuario.usuPk",
                    "simUsuario.usuVersion",
                    "simInicioNumeroCorrelativo",
                    "simVersion",
                    "simDefinicionTitulo.dtiPk",
                    "simDefinicionTitulo.dtiVersion",
                    "simDefinicionTitulo.dtiNombreCertificado",
                    "simSelloDirector.sfiPk",
                    "simSelloDirector.sfiVersion",
                    "simSelloDirector.sfiPersona.perPrimerNombre",
                    "simSelloDirector.sfiPersona.perSegundoNombre",
                    "simSelloDirector.sfiPersona.perTercerNombre",
                    "simSelloDirector.sfiPersona.perPrimerApellido",
                    "simSelloDirector.sfiPersona.perSegundoApellido",
                    "simSelloDirector.sfiPersona.perTercerApellido",
                    "simSelloMinistro.sftPk",
                    "simSelloMinistro.sftPrimerNombre",
                    "simSelloMinistro.sftSegundoNombre",
                    "simSelloMinistro.sftPrimerApellido",
                    "simSelloMinistro.sftSegundoApellido",
                    "simSelloMinistro.sftVersion",
                    "simSelloAutentica.sftPk",
                    "simSelloAutentica.sftVersion",
                    "simSelloAutentica.sftPrimerNombre",
                    "simSelloAutentica.sftSegundoNombre",
                    "simSelloAutentica.sftPrimerApellido",
                    "simSelloAutentica.sftSegundoApellido",
                    "simMotivoReimpresion.mriPk",
                    "simMotivoReimpresion.mriVersion",
                    "simReposicionTitulo.retPk",
                    "simReposicionTitulo.retVersion",
                    "simReposicionTitulo.retNombreEstudiantePartida",
                    "simReposicionTitulo.retNombreEstudiantePartidaBusqueda",
                    "simReposicionTitulo.retAnio",
                    "simReposicionTitulo.retSedeNombre",
                    "simReposicionTitulo.retSedeNombreBusqueda",
                    "simReposicionTitulo.retEstadoReposicion",
                    "simReposicionTitulo.retUsuarioEnviaImprimir",
                    "simNumeroResolucion",
                    "simReposicionTitulo.retSede.sedPk",
                    "simReposicionTitulo.retSede.sedVersion",
                    "simReposicionTitulo.retSede.sedTipo",
                    "simReposicionTitulo.retSede.sedCodigo",
                    "simReposicionTitulo.retSede.sedNombre",
                    "simReposicionTitulo.retSede.sedTipo",
                    "simReposicionTitulo.retSede.sedSedeAdscritaDe.sedPk",
                    "simReposicionTitulo.retSede.sedSedeAdscritaDe.sedCodigo",
                    "simReposicionTitulo.retSede.sedSedeAdscritaDe.sedNombre",
                    "simReposicionTitulo.retSede.sedSedeAdscritaDe.sedTipo",
                    "simReposicionTitulo.retSede.sedSedeAdscritaDe.sedVersion",
                    "simReposicionTitulo.retSolicitudImpresion.simPk",
                    "simReposicionTitulo.retSolicitudImpresion.simVersion",
                    "simReposicionTitulo.retAnulada",
                    "simReposicionTitulo.retMotivoAnulacion.matPk",
                    "simReposicionTitulo.retMotivoAnulacion.matVersion",
                    "simReposicionTitulo.retObservacionAnulada",
                    "simReposicionTitulo.retOpcionBachillerato",
                    "simReposicionTitulo.retDuiSolicitante",
                    "simReposicionTitulo.retFechaLegalizacionTitulo",
                    "simReposicionTitulo.retTituloAnterior2008.tanNombre",
                    "simReposicionTitulo.retEsAnterior2008",
                    "simReposicionTitulo.retNombreTituloPosterior2008",
                    "simNumeroResolucion",
                    "simTituloEntregadoDepartamental",
                    "simFechaEntregadoDepartamental",
                    "simNumeroRegistro",
                    "simTituloEntregadoCentroEducativo",
                    "simFechaEntregadoCentroEducativo",
                    "simImpresora",
                    "simReposicionEstudianteSiges",
                    "simFechaValidez"});
                List<SgSolicitudImpresion> solImpresion = SolicitudImpresionBean.obtenerPorFiltro(fsi);
                sim = solImpresion.get(0);

                FiltroCodiguera fc = new FiltroCodiguera();
                fc.setCodigoExacto(Constantes.TITULO_FECHA_VALIDEZ_DESDE);
                List<SgConfiguracion> conf = configuracionBean.obtenerPorFiltro(fc);
                LocalDate fechaValidez = null;
                if (!conf.isEmpty()) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MM-yyyy");
                    fechaValidez = LocalDate.parse(conf.get(0).getConValor(), formatter);
                }

                if (EnumEstadoSolicitudImpresion.CONFIRMADA.equals(sim.getSimEstado()) || EnumEstadoSolicitudImpresion.CON_EXCEPCIONES.equals(sim.getSimEstado())) {

                    FiltroEstudianteImpresion fei = new FiltroEstudianteImpresion();
                    fei.setEimSolicitudImpresionPk(sim.getSimPk());
                    fei.setEimNoAnulada(Boolean.TRUE);
                    fei.setIncluirCampos(new String[]{"eimEstudiante.estPk",
                        "eimEstudiante.estVersion",
                        "eimEstudiante.estPersona.perPrimerNombre",
                        "eimEstudiante.estPersona.perSegundoNombre",
                        "eimEstudiante.estPersona.perTercerNombre",
                        "eimEstudiante.estPersona.perPrimerApellido",
                        "eimEstudiante.estPersona.perSegundoApellido",
                        "eimEstudiante.estPersona.perTercerApellido",
                        "eimAnulada",
                        "eimVersion",
                        "eimEstudiante.estPersona.perNombrePartidaNacimiento"});
                    List<SgEstudianteImpresion> estImpresionList = estudianteImpresionBean.obtenerPorFiltro(fei);

                    if (!estImpresionList.isEmpty()) {
                        String nombreCerificado = sim.getSimDefinicionTitulo().getDtiNombreCertificado();
                        if (sim.getSimDefinicionTitulo() != null && sim.getSimSeccion() != null) {
                            if (sim.getSimDefinicionTitulo().getDtiNombreCertificado().contains("{OPCION}")) {

                                nombreCerificado = nombreCerificado.replace("{OPCION}", sim.getSimSeccion().getSecServicioEducativo().getSduOpcion() != null ? "" + sim.getSimSeccion().getSecServicioEducativo().getSduOpcion().getOpcNombre() : "");
                            }

                            if (sim.getSimDefinicionTitulo().getDtiNombreCertificado().contains("{PROGRAMA}")) {
                                nombreCerificado = nombreCerificado.replace("{PROGRAMA}", sim.getSimSeccion().getSecServicioEducativo().getSduProgramaEducativo() != null ? "" + sim.getSimSeccion().getSecServicioEducativo().getSduProgramaEducativo().getPedNombre() : "");
                            }

                            if (sim.getSimDefinicionTitulo().getDtiNombreCertificado().contains("{SECTOR}")) {
                                nombreCerificado = nombreCerificado.replace("{SECTOR}", sim.getSimSeccion().getSecServicioEducativo().getSduOpcion() != null && sim.getSimSeccion().getSecServicioEducativo().getSduOpcion().getOpcSectorEconomico() != null ? "" + sim.getSimSeccion().getSecServicioEducativo().getSduOpcion().getOpcSectorEconomico().getSecNombre() : "");
                            }
                        }

                        if (EnumTipoSolicitudImpresion.REI.equals(sim.getSimTipoImpresion())) {

                            FiltroTitulo ft = new FiltroTitulo();
                            ft.setTitNoAnulada(Boolean.TRUE);
                            ft.setTitEstadoSolicitudImp(EnumEstadoSolicitudImpresion.IMPRESA);
                            ft.setTitAnio(sim.getSimSeccion().getSecAnioLectivo().getAleAnio());
                            ft.setTitDefinicionTitulo(sim.getSimDefinicionTitulo().getDtiPk());
                            //Por ser de reimpresión es un único estudiante que tiene en lista
                            ft.setTitEstudiante(estImpresionList.get(0).getEimEstudiante().getEstPk());
                            List<SgTitulo> titulos = this.obtenerPorFiltro(ft);
                            if (!titulos.isEmpty()) {
                                SgTitulo titulo = titulos.get(0);
                                titulo.setTitAnulada(Boolean.TRUE);
                                titulo.setTitMotivoReimpresion(sim.getSimMotivoReimpresion());
                                this.guardar(titulo);
                            }
                        }

                        SgSede sedeTitulo = sim.getSimSeccion().getSecServicioEducativo().getSduSede();
                        if (sim.getSimSeccion().getSecServicioEducativo().getSduSede().getSedSedeAdscritaDe() != null) {
                            sedeTitulo = new SgSede(sim.getSimSeccion().getSecServicioEducativo().getSduSede().getSedSedeAdscritaDe().getSedPk(), sim.getSimSeccion().getSecServicioEducativo().getSduSede().getSedSedeAdscritaDe().getSedVersion());
                            sedeTitulo.setSedCodigo(sim.getSimSeccion().getSecServicioEducativo().getSduSede().getSedSedeAdscritaDe().getSedCodigo());
                            sedeTitulo.setSedNombre(sim.getSimSeccion().getSecServicioEducativo().getSduSede().getSedSedeAdscritaDe().getSedNombre());
                        }

                        FiltroTitulo ft = new FiltroTitulo();
                        ft.setTitSolicitudImpresion(sim.getSimPk());
                        Long cantTitulos = this.obtenerTotalPorFiltro(ft);
                        if (cantTitulos.equals(0L)) {

                            SgSolicitudImpresionTituloLite solImpLite = new SgSolicitudImpresionTituloLite();
                            solImpLite.setSimFechaEnviadoImprimir(LocalDate.now());

                            List<SgTitulo> listTitulo = new ArrayList();
                            for (SgEstudianteImpresion estImp : estImpresionList) {
                                if (!BooleanUtils.isTrue(estImp.getEimAnulada())) {
                                    SgTitulo tit = new SgTitulo();
                                    tit.setTitAnio(sim.getSimSeccion().getSecAnioLectivo().getAleAnio());
                                    tit.setTitEstudianteFk(estImp.getEimEstudiante());
                                    tit.setTitFechaValidez(sim.getSimFechaValidez() == null ? fechaValidez : sim.getSimFechaValidez());
                                    tit.setTitNombreCertificado(nombreCerificado);
                                    tit.setTitNombreDirector(sim.getSimSelloDirector() != null ? sim.getSimSelloDirector().getSfiPersona().getPerNombreCompleto() : null);
                                    tit.setTitNombreEstudiante(estImp.getEimEstudiante().getEstPersona().getPerNombreCompleto());
                                    tit.setTitNombreEstudiantePartida(estImp.getEimEstudiante().getEstPersona().getPerNombrePartidaNacimiento());
                                    tit.setTitNombreMinistro(sim.getSimSelloMinistro().getPerNombreCompleto());
                                    tit.setTitSedeCodigo(sedeTitulo.getSedCodigo());
                                    tit.setTitSedeFk(sim.getSimSeccion().getSecServicioEducativo().getSduSede());
                                    tit.setTitSedeNombre(sedeTitulo.getSedNombre());
                                    tit.setTitSelloFirmaDirectorFk(sim.getSimSelloDirector());
                                    tit.setTitSelloFirmaTitularMinistroFk(sim.getSimSelloMinistro());
                                    tit.setTitSelloFirmaTitularAutenticaFk(sim.getSimSelloAutentica());
                                    tit.setTitServicioEducativoFk(sim.getSimSeccion().getSecServicioEducativo());
                                    tit.setTitSolicitudImpresionFk(sim);
                                    tit.setTitFechaEmision(sim.getSimFechaEnviadoImprimir());
                                    tit.setTitNombreTitular(sim.getSimSelloAutentica() != null ? sim.getSimSelloAutentica().getPerNombreCompleto() : null);
                                    tit.setTitUsuarioEnviaImprimir(Lookup.obtenerJWT().getSubject());
                                    tit.setTitReposicion(BooleanUtils.isTrue(sim.getSimReposicionEstudianteSiges())?Boolean.TRUE:Boolean.FALSE);
                                    tit.setTitNumeroRegistro(sim.getSimNumeroRegistro());
                                    tit.setTitNumeroResolucion(sim.getSimNumeroResolucion());
                                    tit.setTitSeccionFk(sim.getSimSeccion());
                                    listTitulo.add(tit);
                                }
                            }
                            solImpLite.setSimPk(sim.getSimPk());
                            solImpLite.setSimVersion(sim.getSimVersion());
                            solImpLite.setSimTitulos(listTitulo);
                            solImpLite.setSimEstado(EnumEstadoSolicitudImpresion.PENDIENTE_IMPRESION);
                            CodigueraDAO<SgSolicitudImpresionTituloLite> codDAO = new CodigueraDAO<>(em, SgSolicitudImpresionTituloLite.class);
                            SgSolicitudImpresionTituloLite simp = codDAO.guardar(solImpLite, sim.getSimPk() == null ? ConstantesOperaciones.CREAR_SOLICITUD_IMPRESION : ConstantesOperaciones.ACTUALIZAR_SOLICITUD_IMPRESION);

                            sim.setSimEstado(EnumEstadoSolicitudImpresion.PENDIENTE_IMPRESION);
                            sim.setSimVersion(simp.getSimVersion());
                            return sim;

                        } else {
                            BusinessException ge = new BusinessException();
                            ge.addError(Errores.ERROR_TITULOS_YA_GNERADOS_PARA_SOLICITUD);
                            throw ge;
                        }
                    } else {
                        if (EnumTipoSolicitudImpresion.REP.equals(sim.getSimTipoImpresion()) || EnumTipoSolicitudImpresion.SIM.equals(sim.getSimTipoImpresion())) {

                            String nombreCerificado = sim.getSimDefinicionTitulo().getDtiNombreCertificado();
                            if (sim.getSimDefinicionTitulo() != null 
                                    && !StringUtils.isBlank(sim.getSimDefinicionTitulo().getDtiNombreCertificado())
                                    && !StringUtils.isBlank(sim.getSimReposicionTitulo().getRetOpcionBachillerato())) {                   
                                     if (sim.getSimDefinicionTitulo().getDtiNombreCertificado().contains("{OPCION}")) {

                                        nombreCerificado = nombreCerificado.replace("{OPCION}", sim.getSimReposicionTitulo().getRetOpcionBachillerato());
                                    }
                            }
                           

                            List<SgTitulo> listTitulo = new ArrayList();
                            if (sim.getSimReposicionTitulo() != null) {
                                SgSolicitudImpresionTituloLite solImpLite = new SgSolicitudImpresionTituloLite();
                                solImpLite.setSimFechaEnviadoImprimir(LocalDate.now());

                                SgTitulo tit = new SgTitulo();
                                tit.setTitAnio(sim.getSimReposicionTitulo().getRetAnio());
                                tit.setTitFechaValidez(sim.getSimFechaValidez() == null ? fechaValidez : sim.getSimFechaValidez());
                                tit.setTitNombreEstudiante(sim.getSimReposicionTitulo().getRetNombreEstudiantePartida());
                                tit.setTitNombreEstudiantePartida(sim.getSimReposicionTitulo().getRetNombreEstudiantePartida());
                                tit.setTitSelloFirmaTitularMinistroFk(sim.getSimSelloMinistro());
                                tit.setTitSelloFirmaTitularAutenticaFk(sim.getSimSelloAutentica());
                                tit.setTitNombreMinistro(sim.getSimSelloMinistro().getPerNombreCompleto());
                                tit.setTitFechaEmision(sim.getSimFechaEnviadoImprimir());
                                tit.setTitNombreTitular(sim.getSimSelloAutentica() != null ? sim.getSimSelloAutentica().getPerNombreCompleto() : null);
                                tit.setTitUsuarioEnviaImprimir(Lookup.obtenerJWT().getSubject());
                                tit.setTitNombreCertificado(nombreCerificado);
                                tit.setTitDuiEstudiante(sim.getSimReposicionTitulo().getRetDuiSolicitante());
                                tit.setTitTituloAnterior2008(sim.getSimReposicionTitulo().getRetTituloAnterior2008()!=null?sim.getSimReposicionTitulo().getRetTituloAnterior2008().getTanNombre():null);
                                tit.setTitEsAnterior2008(sim.getSimReposicionTitulo().getRetEsAnterior2008());
                                tit.setTitNombreTituloPosterior2008(sim.getSimReposicionTitulo().getRetNombreTituloPosterior2008());
                                tit.setTitFechaLegalizacionTitulo(sim.getSimReposicionTitulo().getRetFechaLegalizacionTitulo());
                                tit.setTitNumeroRegistro(sim.getSimNumeroRegistro());
                                tit.setTitNumeroResolucion(sim.getSimNumeroResolucion());
                                if (sim.getSimReposicionTitulo().getRetSede() != null) {
                                    tit.setTitSedeCodigo(sim.getSimReposicionTitulo().getRetSede().getSedCodigo());
                                    tit.setTitSedeFk(sim.getSimReposicionTitulo().getRetSede());
                                    tit.setTitSedeNombre(sim.getSimReposicionTitulo().getRetSede().getSedNombre());
                                } else {
                                    tit.setTitSedeNombre(sim.getSimReposicionTitulo().getRetSedeNombre());
                                }
                                
                                tit.setTitReposicion(Boolean.TRUE);
                                tit.setTitSolicitudImpresionFk(sim);
                                tit.setTitFechaEmision(sim.getSimFechaEnviadoImprimir());
                                tit.setTitUsuarioEnviaImprimir(Lookup.obtenerJWT().getSubject());
                                listTitulo.add(tit);

                                solImpLite.setSimPk(sim.getSimPk());
                                solImpLite.setSimVersion(sim.getSimVersion());
                                solImpLite.setSimTitulos(listTitulo);
                                solImpLite.setSimEstado(EnumEstadoSolicitudImpresion.PENDIENTE_IMPRESION);
                                CodigueraDAO<SgSolicitudImpresionTituloLite> codDAO = new CodigueraDAO<>(em, SgSolicitudImpresionTituloLite.class);
                                SgSolicitudImpresionTituloLite simp = codDAO.guardar(solImpLite, sim.getSimPk() == null ? ConstantesOperaciones.CREAR_SOLICITUD_IMPRESION : ConstantesOperaciones.ACTUALIZAR_SOLICITUD_IMPRESION);
                                sim.setSimEstado(EnumEstadoSolicitudImpresion.PENDIENTE_IMPRESION);
                                sim.setSimVersion(simp.getSimVersion());
                                return sim;
                            }

                        }
                    }

                } else {
                    if (!EnumEstadoSolicitudImpresion.PENDIENTE_IMPRESION.equals(sim.getSimEstado())) {
                        BusinessException ge = new BusinessException();
                        ge.addError(Errores.ERROR_ESTADO_INCORRECTO);
                        throw ge;
                    }

                }
            }

        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return sim;
    }

}
