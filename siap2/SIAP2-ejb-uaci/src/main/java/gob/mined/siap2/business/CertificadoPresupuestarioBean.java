/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business;

import gob.mined.siap2.business.ejbs.DatosUsuario;
import gob.mined.siap2.business.ejbs.SecuenciaBean;
import gob.mined.siap2.business.ejbs.impl.ArchivoBean;
import gob.mined.siap2.business.ejbs.impl.ReporteBean;
import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.SsUsuario;
import gob.mined.siap2.entities.data.impl.Archivo;
import gob.mined.siap2.entities.data.impl.CertificadoDisponibilidadPresupuestaria;
import gob.mined.siap2.entities.data.impl.ComentarioCertificadoDisponibilidadPresupuestaria;
import gob.mined.siap2.entities.data.impl.Notificacion;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.data.impl.POMontoFuenteInsumo;
import gob.mined.siap2.entities.data.impl.POMontoFuenteInsumoFlujoCajaMensual;
import gob.mined.siap2.entities.data.impl.Secuencia;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.entities.enums.EstadoCertificadoDispPresupuestaria;
import gob.mined.siap2.entities.enums.TipoNotificacion;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import gob.mined.siap2.persistence.dao.imp.CertificadoPresupuestarioDAO;
import gob.mined.siap2.persistence.dao.imp.UsuarioDAO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Esta clase implementa los métodos de servicio del catálogo de insumos
 *
 * @author Sofis Solutions
 */
@Stateless(name = "CertificadoPresupuestarioBean")
@LocalBean
public class CertificadoPresupuestarioBean {

    @Inject
    @JPADAO
    private GeneralDAO generalDAO;

    @Inject
    private ArchivoBean archivoBean;

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @Inject
    @JPADAO
    private UsuarioDAO usuarioDAO;

    @Inject
    private ReporteBean reporteBean;

    @Inject
    @JPADAO
    private CertificadoPresupuestarioDAO certPresupDAO;

    @Inject
    private SecuenciaBean secuenciaBean;
    @Inject
    private DatosUsuario datosUsuario;

    /**
     * Este método se encarga de cargar un certificado de disponibilidad
     * presupuestaria
     *
     * @param id
     * @return
     */
    public CertificadoDisponibilidadPresupuestaria getCertificadoPresupuestario(Integer id) {
        try {
            CertificadoDisponibilidadPresupuestaria certificado = (CertificadoDisponibilidadPresupuestaria) generalDAO.find(CertificadoDisponibilidadPresupuestaria.class, id);
            certificado.getFuentes().isEmpty();
            return certificado;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método se encarga de aprobar un certificado de disponibilidad
     * presupuestaria
     *
     * @param certificado
     * @return
     */
    public CertificadoDisponibilidadPresupuestaria aprobarCertificadoDispPresupuestario(Integer idCertificado) {
        try {
            CertificadoDisponibilidadPresupuestaria certificado = (CertificadoDisponibilidadPresupuestaria) generalDAO.find(CertificadoDisponibilidadPresupuestaria.class, idCertificado);
            if (certificado.getEstado() != EstadoCertificadoDispPresupuestaria.ENVIADO) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.YA_SE_CAMBIO_EL_ESTADO_DEL_CERTIFICADO);
                throw b;
            }

            for (POMontoFuenteInsumo fuente : certificado.getFuentes()) {
                fuente.setCertificadoDisponibilidadPresupuestariaAprobada(true);
                POInsumos poInsumo = fuente.getInsumo();
                caluclarAprovacionCertificadoDisponibilidadPresupuestaria(poInsumo);
            }

            distribuirMontoCertificadoMensualPorFuente(certificado);

            certificado.setEstado(EstadoCertificadoDispPresupuestaria.APROBADO);
            certificado.setFechaCertificado(new Date());
            Archivo archivo = generarArchivoCertificado(certificado);
            certificado.setArchivoCertificado(archivo);

            return (CertificadoDisponibilidadPresupuestaria) generalDAO.update(certificado);

        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Calcula y setea en cada monto fuente por mes el monto certificado en
     * proporción a lo certificado para la fuente
     *
     * @param cert
     */
    private void distribuirMontoCertificadoMensualPorFuente(CertificadoDisponibilidadPresupuestaria certificado) {
        for (POMontoFuenteInsumo montoFuente : certificado.getFuentes()) {
            if (montoFuente.getMontosFuentesInsumosFCM() != null) {
                List<POMontoFuenteInsumoFlujoCajaMensual> listaMontoFuenteFCMConMonto = new LinkedList<>();
                for (POMontoFuenteInsumoFlujoCajaMensual montoFuenteFCM : montoFuente.getMontosFuentesInsumosFCM()) {
                    if (montoFuenteFCM.getMonto() != null && montoFuenteFCM.getMonto().compareTo(BigDecimal.ZERO) != 0) {
                        listaMontoFuenteFCMConMonto.add(montoFuenteFCM);
                    }
                }
                if (!listaMontoFuenteFCMConMonto.isEmpty()) {
                    this.ordenarListaMontoFuenteFCMPorMes(listaMontoFuenteFCMConMonto);
                    //Me quedo con el último de la lista para hacer el prorrateo
                    POMontoFuenteInsumoFlujoCajaMensual montoFuenteFCMUltimo = listaMontoFuenteFCMConMonto.get(listaMontoFuenteFCMConMonto.size() - 1);
                    BigDecimal sumaMontosCertExceptoUltimo = BigDecimal.ZERO;
                    for (POMontoFuenteInsumoFlujoCajaMensual montoFuenteFCM : listaMontoFuenteFCMConMonto) {
                        //Si el monto certificado de la fuente es igual al monto original, para cada mes de la fuente, el monto certificado también será igual al monto original
                        if (montoFuente.getMonto().equals(montoFuente.getCertificado())) {
                            montoFuenteFCM.setMontoCertificado(montoFuenteFCM.getMonto());
                        } else {
                            //Si no es el último, hago el calculo normal
                            if (montoFuenteFCMUltimo.getId() != montoFuenteFCM.getId()) {
                                //Monto Fuente Mes Cert = (Monto fuente mes / monto fuente) * monto fuente cert
                                BigDecimal montoFuenteFMCCertificado = montoFuenteFCM.getMonto().divide(montoFuente.getMonto(), 2, RoundingMode.DOWN);
                                montoFuenteFMCCertificado = montoFuenteFMCCertificado.multiply(montoFuente.getCertificado());
                                montoFuenteFMCCertificado = montoFuenteFMCCertificado.setScale(2, RoundingMode.CEILING);
                                montoFuenteFCM.setMontoCertificado(montoFuenteFMCCertificado);
                                sumaMontosCertExceptoUltimo = sumaMontosCertExceptoUltimo.add(montoFuenteFMCCertificado);
                            }
                        }
                    }
                    sumaMontosCertExceptoUltimo = sumaMontosCertExceptoUltimo.setScale(2, RoundingMode.CEILING);
                    //Si el monto a certificar no era excactamente igual al monto original
                    if (!montoFuente.getMonto().equals(montoFuente.getCertificado())) {
                        //Prorateo en el último monto fuente FCM con lo que resta para completar el monto certificado de la fuente
                        BigDecimal montoDiferenciaCertFCM = montoFuente.getCertificado().subtract(sumaMontosCertExceptoUltimo);
                        montoFuenteFCMUltimo.setMontoCertificado(montoDiferenciaCertFCM);
                    }
                }

            }
        }
    }

    /**
     * Ordena una lista de POMontoFuenteInsumoFlujoCajaMensual por mes
     *
     * @param listaMontoFuenteFCMConMonto
     */
    private void ordenarListaMontoFuenteFCMPorMes(List<POMontoFuenteInsumoFlujoCajaMensual> listaMontoFuenteFCMConMonto) {
        Collections.sort(listaMontoFuenteFCMConMonto, new Comparator<POMontoFuenteInsumoFlujoCajaMensual>() {
            @Override
            public int compare(POMontoFuenteInsumoFlujoCajaMensual o1, POMontoFuenteInsumoFlujoCajaMensual o2) {
                return o1.getFlujoCajaMensual().getMes().compareTo(o2.getFlujoCajaMensual().getMes());
            }
        });
    }

    /**
     * Calcula y setea en el insumo el monto total certificado y marca en true
     * el atributo que indica que fue certificado
     *
     * @param insumo
     */
    private void caluclarAprovacionCertificadoDisponibilidadPresupuestaria(POInsumos insumo) {
        BigDecimal totalCert = BigDecimal.ZERO;
        for (POMontoFuenteInsumo fuente : insumo.getMontosFuentes()) {
            if (fuente.getCertificadoDisponibilidadPresupuestariaAprobada() != null && fuente.getCertificadoDisponibilidadPresupuestariaAprobada().equals(Boolean.TRUE)) {
                if (fuente.getCertificado() != null) {
                    if (fuente.getCertificado().compareTo(fuente.getMonto()) > 0) {
                        String[] params = {fuente.getInsumo().getId().toString(), fuente.getFuente().getFuenteRecursos().getNombre()};
                        BusinessException b = new BusinessException();
                        b.addError(ConstantesErrores.ERR_EN_INSUMO_0_LA_FUENTE_1_SE_CERTIFICA_POR_MONTO_MAYOR_AL_DE_LA_FUENTE, params);
                        throw b;
                    }
                    totalCert = totalCert.add(fuente.getCertificado());
                }
            }
        }
        insumo.setMontoTotalCertificado(totalCert);
        insumo.setPasoValidacionCertificadoDeDispPresupuestaria(Boolean.TRUE);

    }

    /**
     * Este método se encarga de rechazar un certificado de disponibilidad
     * presupuestaria
     *
     * @param certificado
     * @return
     */
    public CertificadoDisponibilidadPresupuestaria rechazarCertificadoDispPresupuestario(CertificadoDisponibilidadPresupuestaria certificado) {
        try {
            if (certificado.getEstado() != EstadoCertificadoDispPresupuestaria.ENVIADO) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.YA_SE_CAMBIO_EL_ESTADO_DEL_CERTIFICADO);
                throw b;
            }

            certificado.setEstado(EstadoCertificadoDispPresupuestaria.RECHAZADO);
            return (CertificadoDisponibilidadPresupuestaria) generalDAO.update(certificado);

        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método se encarga de solicitar información adicional un certificado
     * de disponibilidad presupuestaria
     *
     * @param id
     * @param contenido
     * @return
     */
    public CertificadoDisponibilidadPresupuestaria
            agregarComentarioCertificado(Integer id, String contenido) {
        try {
            CertificadoDisponibilidadPresupuestaria certificado = (CertificadoDisponibilidadPresupuestaria) generalDAO.find(CertificadoDisponibilidadPresupuestaria.class,
                    id);
            SsUsuario usuarioComentario = usuarioDAO.getUsuarioByCodigo(datosUsuario.getCodigoUsuario());

            ComentarioCertificadoDisponibilidadPresupuestaria comentario = new ComentarioCertificadoDisponibilidadPresupuestaria();
            comentario.setFecha(new Date());
            comentario.setComentario(contenido);
            comentario.setCertificado(certificado);
            comentario.setUsuario(usuarioComentario);

            certificado.getComentarios().add(comentario);

            //se notifica a los usuarios de la ut con rol de eviar certificado
            List<SsUsuario> usuariosQueEnviaron = usuarioDAO.getUsuariosEnUTConOperacion(certificado.getUnidadTecnica().getId(), ConstantesEstandares.Operaciones.ENVIAR_CERTIFICADO_DISPONIBILIDAD_PRESUPESTARIA);
            for (SsUsuario usuario : usuariosQueEnviaron) {
                if (!usuarioComentario.equals(usuario)) {
                    Notificacion n = new Notificacion();
                    n.setTipo(TipoNotificacion.CERTIFICADO_DISP_PRESUPUESTARIA_COMETNARIO_PARA_SOLICITANTE);
                    n.setContenido(contenido);
                    n.setUsuario(usuario);
                    n.setFecha(new Date());
                    n.setObjetoId(certificado.getId());
                    n.setObjeto2Id(certificado.getNumero());
                    generalDAO.update(n);
                }
            }

            //se notifica a los usuarios con operacion de validar
            List<SsUsuario> usuariosQueValidan = usuarioDAO.getUsuariosConOperacion(ConstantesEstandares.Operaciones.VALIDAR_CERTIFICADO_PRES_POR_PROCESO, null);
            for (SsUsuario usuario : usuariosQueValidan) {
                if (!usuarioComentario.equals(usuario)) {
                    Notificacion n = new Notificacion();
                    n.setTipo(TipoNotificacion.CERTIFICADO_DISP_PRESUPUESTARIA_COMETNARIO_PARA_VALIDADOR);
                    n.setContenido(contenido);
                    n.setUsuario(usuario);
                    n.setFecha(new Date());
                    n.setObjetoId(certificado.getId());
                    n.setObjeto2Id(certificado.getNumero());
                    generalDAO.update(n);
                }
            }

            return certificado;

        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    private Archivo generarArchivoCertificado(CertificadoDisponibilidadPresupuestaria cert) {
        List<POMontoFuenteInsumo> fuentesSeleccionadas = cert.getFuentes();
        if (fuentesSeleccionadas.size() > 1) {
            return reporteBean.generarArchivoCertificadoDisponibilidadProceso(cert);
        } else {
            return reporteBean.generarArchivoCertificadoDisponibilidadPOInsumo(cert);
        }
    }

    private CertificadoDisponibilidadPresupuestaria actualizarCertificadoParaEnviar(CertificadoDisponibilidadPresupuestaria cert) throws Exception {

        Secuencia secuenciaNumCert = secuenciaBean.obtenerSecPorCodigo(ConstantesEstandares.Secuencias.SEC_NUM_CER_DISP_PRESUP);
        Integer numeroCert = secuenciaNumCert.getNumero() + 1;
        cert.setNumero(numeroCert);
        secuenciaNumCert.setNumero(numeroCert);
        generalDAO.update(secuenciaNumCert);
        Archivo archivo = generarArchivoCertificado(cert);
        cert.setArchivo(archivo);

        for (POMontoFuenteInsumo data : cert.getFuentes()) {
            if (data.getCertificado() == null || data.getCertificado().compareTo(BigDecimal.ZERO) <= 0) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_INGRESAR_MONTO_CERTIFICADO_PARA_FUENTES_SELECCIONADAS);
                throw b;
            }
            if (data.getCertificadoDisponibilidadPresupuestariaAprobada() != null && data.getCertificadoDisponibilidadPresupuestariaAprobada().equals(Boolean.TRUE)) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_UNA_FEUNTE_EN_EL_CERTIFICADO_YA_ESTA_APROBADA);
                throw b;

            }

            //POMontoFuenteInsumo fuente = (POMontoFuenteInsumo) generalDAO.find(POMontoFuenteInsumo.class, data.getId());
            if (data.getCertificado().compareTo(data.getMonto()) > 0) {
                String[] params = {data.getInsumo().getId().toString(), data.getFuente().getFuenteRecursos().getNombre()};
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_EN_INSUMO_0_LA_FUENTE_1_SE_CERTIFICA_POR_MONTO_MAYOR_AL_DE_LA_FUENTE, params);
                throw b;
            }

            data.getInsumo().setEnviadoParaCertificar(Boolean.TRUE);
            data.setCertificadoDisponibilidadPresupuestaria(cert);
        }

        return cert;

    }

    /**
     * Este método envía una notificación para validar las fuentes de los insumo
     *
     * @param idUT
     * @param idProcesoAdq
     * @param fuentesSeleccionadas
     */
    public void enviarAvalidarFuentes(Integer idUT, List<POMontoFuenteInsumo> fuentesSeleccionadas) {
        try {
            UnidadTecnica unidadTecnica = (UnidadTecnica) generalDAO.find(UnidadTecnica.class,
                    idUT);

            CertificadoDisponibilidadPresupuestaria cert = new CertificadoDisponibilidadPresupuestaria();
            cert.setFuentes(new LinkedList<POMontoFuenteInsumo>());

            cert.setEstado(EstadoCertificadoDispPresupuestaria.ENVIADO);
            cert.setUnidadTecnica(unidadTecnica);
            cert.setFecha(new Date());

            //se setea el motno certificado
            for (POMontoFuenteInsumo data : fuentesSeleccionadas) {
                POMontoFuenteInsumo fuente = (POMontoFuenteInsumo) generalDAO.find(POMontoFuenteInsumo.class,
                        data.getId());
                fuente.setCertificado(data.getCertificado());
                fuente.setMontoDescertificado(BigDecimal.ZERO);
                cert.getFuentes().add(fuente);
            }

            cert = actualizarCertificadoParaEnviar(cert);
            cert = (CertificadoDisponibilidadPresupuestaria) generalDAO.update(cert);

            //se notifica a los usuarios
            List<SsUsuario> anotificar = usuarioDAO.getUsuariosConOperacion(ConstantesEstandares.Operaciones.VALIDAR_CERTIFICADO_PRES_POR_PROCESO, null);
            for (SsUsuario usu : anotificar) {
                Notificacion notificacion = new Notificacion();
                notificacion.setTipo(TipoNotificacion.CERTIFICADO_DISPONIBILIDAD_PRESUPUESTARIA_PARA_VALIDAR_POR_PROCESO);
                notificacion.setUsuario(usu);
                notificacion.setFecha(new Date());
                notificacion.setObjetoId(cert.getId());
                notificacion.setContenido("UT: " + unidadTecnica.getNombre());

                notificacion = (Notificacion) generalDAO.update(notificacion);
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException ge = new TechnicalException();
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método reenvía una notificación para validar las fuentes de los
     * insumo
     *
     * @param cert
     * @return
     */
    public CertificadoDisponibilidadPresupuestaria reenviarCertificadoParaEnviar(CertificadoDisponibilidadPresupuestaria cert) {
        try {
            if (cert.getEstado() != EstadoCertificadoDispPresupuestaria.ENVIADO) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.YA_SE_CAMBIO_EL_ESTADO_DEL_CERTIFICADO);
                throw b;
            }

            cert = actualizarCertificadoParaEnviar(cert);
            return (CertificadoDisponibilidadPresupuestaria) generalDAO.update(cert);

        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException ge = new TechnicalException();
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Verifica si un monto fuente está validado o si está asociado a un
     * certificado de disponibilidad presupuestaria con estado vacío o rechazado
     *
     * @param montoFienteId
     * @return
     */
    public Boolean estaMontoFuenteDisponibleParaEnviar(Integer montoFienteId) {
        POMontoFuenteInsumo montoFuente = (POMontoFuenteInsumo) generalDAO.find(POMontoFuenteInsumo.class,
                montoFienteId);
        Boolean estaDisponible = true;

        //Si está aprobado no estará disponible para enviar
        if (montoFuente.getCertificadoDisponibilidadPresupuestariaAprobada() != null && montoFuente.getCertificadoDisponibilidadPresupuestariaAprobada()) {
            estaDisponible = false;
        }

        //Si tiene un certificado enviado o aprobado no estará disponible para enviar
        if (estaDisponible) {
            if (montoFuente.getCertificadoDisponibilidadPresupuestaria() != null) {
                if (!montoFuente.getCertificadoDisponibilidadPresupuestaria().getEstado().equals(EstadoCertificadoDispPresupuestaria.RECHAZADO)) {
                    estaDisponible = false;
                }
            }
        }
        return estaDisponible;
    }

    /**
     * Este método es el encargado de traer los certificados de disponibilidad
     * presupuestaria por filtro
     *
     * @param count
     * @param numero
     * @param fecha
     * @param estado
     * @param idPOInsumo
     * @param idFuenteRecursos
     * @param idFuenteFinanciamiento
     * @param idProcesoAdq
     * @param idProgramaPres
     * @param idSubProgramaPres
     * @param idProyecto
     * @param idAccCentral
     * @param idAsigNp
     * @param firstResult
     * @param maxResults
     * @param orderBy
     * @param ascending
     * @return
     */
    public List<CertificadoDisponibilidadPresupuestaria> getCertificadoDispPresupuestaria(
            Integer numero,
            Date fecha,
            EstadoCertificadoDispPresupuestaria estado,
            Integer idPOInsumo,
            Integer idFuenteRecursos,
            Integer idFuenteFinanciamiento,
            Integer idProcesoAdq,
            Integer idProgramaPres,
            Integer idSubProgramaPres,
            Integer idProyecto,
            Integer idAccCentral,
            Integer idAsigNp,
            Integer firstResult,
            Integer maxResults,
            String[] orderBy,
            boolean[] ascending) {

        List<CertificadoDisponibilidadPresupuestaria> l = certPresupDAO.getCertificadoDispPresupuestaria(numero, fecha, estado, idPOInsumo, idFuenteRecursos, idFuenteFinanciamiento, idProcesoAdq, idProgramaPres, idSubProgramaPres, idProyecto, idAccCentral, idAsigNp, firstResult, maxResults, orderBy, ascending);
        l.isEmpty();
        return l;
    }

    /**
     * Este método es el encargado de contar los certificados de disponibilidad
     * presupuestaria por filtro
     *
     * @param count
     * @param numero
     * @param fecha
     * @param estado
     * @param idPOInsumo
     * @param idFuenteRecursos
     * @param idFuenteFinanciamiento
     * @param idProcesoAdq
     * @param idProgramaPres
     * @param idSubProgramaPres
     * @param idProyecto
     * @param idAccCentral
     * @param idAsigNp
     * @return
     */
    public long countCertificadoDispPresupuestaria(
            Integer numero,
            Date fecha,
            EstadoCertificadoDispPresupuestaria estado,
            Integer idPOInsumo,
            Integer idFuenteRecursos,
            Integer idFuenteFinanciamiento,
            Integer idProcesoAdq,
            Integer idProgramaPres,
            Integer idSubProgramaPres,
            Integer idProyecto,
            Integer idAccCentral,
            Integer idAsigNp) {

        return certPresupDAO.countCertificadoDispPresupuestaria(numero, fecha, estado, idPOInsumo, idFuenteRecursos, idFuenteFinanciamiento, idProcesoAdq, idProgramaPres, idSubProgramaPres, idProyecto, idAccCentral, idAsigNp);
    }

    /**
     * Verifica si un existe un monto fuente entre los que tiene el poInsumo que
     * esté asociado a un certificado de disponibilidad presupuestaria con
     * estado vacío o rechazado
     *
     * @return
     */
    public Boolean existeMontoFuenteDisponibleParaEnviar(List<POMontoFuenteInsumo> montosFuentes) {

        Boolean existeMontoFuenteParaEnviar = false;
        Iterator<POMontoFuenteInsumo> itMontosFuentes = montosFuentes.iterator();

        while (itMontosFuentes.hasNext() && !existeMontoFuenteParaEnviar) {
            POMontoFuenteInsumo montoFuenteTemp = itMontosFuentes.next();
            if (montoFuenteTemp.getId() != null) {
                POMontoFuenteInsumo montoFuente = (POMontoFuenteInsumo) generalDAO.find(POMontoFuenteInsumo.class, montoFuenteTemp.getId());
                //montoFuente puede estar en null porque el insumo fue eliminado
                if (montoFuente != null) {
                    //Si no está aprobado
                    if (montoFuente.getCertificadoDisponibilidadPresupuestariaAprobada() == null || !montoFuente.getCertificadoDisponibilidadPresupuestariaAprobada()) {
                        existeMontoFuenteParaEnviar = true;
                    }

                    if (existeMontoFuenteParaEnviar) {
                        //Si no tiene certificado se podrá enviar
                        if (montoFuente.getCertificadoDisponibilidadPresupuestaria() == null) {
                            existeMontoFuenteParaEnviar = true;
                        } else {
                            //Si tiene certificado en estado RECHAZADO se podrá enviar
                            if (montoFuente.getCertificadoDisponibilidadPresupuestaria().getEstado().equals(EstadoCertificadoDispPresupuestaria.RECHAZADO)) {
                                existeMontoFuenteParaEnviar = true;
                            }
                        }
                        if (montoFuente.getCertificadoDisponibilidadPresupuestaria() != null
                                && !montoFuente.getCertificadoDisponibilidadPresupuestaria().getEstado().equals(EstadoCertificadoDispPresupuestaria.RECHAZADO)) {
                            existeMontoFuenteParaEnviar = false;
                        }
                    }
                }
            }
        }

        return existeMontoFuenteParaEnviar;
    }

}
