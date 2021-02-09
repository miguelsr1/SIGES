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
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroConfirmacionAprobacion;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.validaciones.ConfirmacionAprobacionValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.ConfirmacionAprobacionDAO;
import sv.gob.mined.siges.persistencia.entidades.SgArchivo;
import sv.gob.mined.siges.persistencia.entidades.SgConfirmacionAprobacion;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgConfiguracionFirmaElectronica;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;
import sv.gob.mined.siges.wsclient.ResultadoValidacion;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class ConfirmacionAprobacionBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private FirmaElectronicaBean firmaElectronicaBean;

    @Inject
    private ConfiguracionFirmaElectronicaBean configFirmaElectronicaBean;

    @Inject
    @ConfigProperty(name = "files.tmp.path")
    private String tmpBasePath;
    
    private static final Logger LOGGER = Logger.getLogger(ConfirmacionAprobacionBean.class.getName());



    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgConfirmacionAprobacion
     * @throws GeneralException
     */
    public SgConfirmacionAprobacion obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgConfirmacionAprobacion> codDAO = new CodigueraDAO<>(em, SgConfirmacionAprobacion.class);
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
                CodigueraDAO<SgConfirmacionAprobacion> codDAO = new CodigueraDAO<>(em, SgConfirmacionAprobacion.class);
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
     * @param cma SgConfirmacionAprobacion
     * @return SgConfirmacionAprobacion
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgConfirmacionAprobacion preconfirmar(SgConfirmacionAprobacion cma) throws GeneralException {
        try {
            if (cma != null) {
                CloseableHttpClient httpclient = null;
                CloseableHttpResponse response = null;
                int timeout = 2 * 120 * 1000;
                
                if (ConfirmacionAprobacionValidacionNegocio.validar(cma)) {

                    SgConfiguracionFirmaElectronica conf = configFirmaElectronicaBean.obtenerPorCodigo(Constantes.CONFIG_FIRMA_ELECTRONICA_CONFIRMACION_APROBACION);

                    if (conf != null && BooleanUtils.isTrue(conf.getConActivada())) {

                        CodigueraDAO<SgConfirmacionAprobacion> codDAO = new CodigueraDAO<>(em, SgConfirmacionAprobacion.class);
                        cma.setCprFirmada(Boolean.FALSE);

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

                        URIBuilder ubuilder = new URIBuilder(System.getProperty("service.report-generator.baseUrl") + "/ma/calificacionesEstudiantes");
                        ubuilder.setParameter("seccionId", cma.getCprSeccion().getSecPk().toString());
                        ubuilder.setParameter("componentePlanEstudioId", cma.getCprComponentePlanEstudio().getCpePk().toString());

                        
                        HttpGet get = new HttpGet(ubuilder.build());
                        get.addHeader(org.apache.http.HttpHeaders.AUTHORIZATION, "Bearer " + Lookup.obtenerJWT().getRawToken());

                        response = httpclient.execute(get);

                        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                            InputStream is = response.getEntity().getContent();
                            byte[] byteArray = IOUtils.toByteArray(is);

                            List<byte[]> send = new ArrayList<>();
                            send.add(byteArray);

                            String usuarioEnviaYFirma = Lookup.obtenerJWT().getSubject();

                            List<ResultadoValidacion> res = firmaElectronicaBean.enviarDocumentosParaFirmar(send, cma.getCprTransactionReturnUrl(), "confirmar_aprobacion.pdf", usuarioEnviaYFirma, usuarioEnviaYFirma);

                            if (res == null || res.isEmpty() || !res.get(0).getCode().equalsIgnoreCase("OK")) {
                                BusinessException be = new BusinessException();
                                be.addError("ERROR_AL_ENVIAR_DOCUMENTO");
                                throw be;
                            }

                            cma.setCprFirmaTransactionId(res.get(0).getMessage());
                            cma = codDAO.guardar(cma, null);

                            cma.setCprTransactionSignatureUrl(System.getProperty("service.firmar-documentos.baseUrl") + "?id_transaccion=" + res.get(0).getMessage());
                            return cma;
                        } else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_FORBIDDEN) {
                            BusinessException be = new BusinessException();
                            be.addError(Errores.ERROR_SEGURIDAD);
                            throw be;
                        } else {
                            throw new TechnicalException("ERROR_AL_GENERAR_PFD");
                        }

                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return cma;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param firmaTransactionId String
     * @return SgConfirmacionAprobacion
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgConfirmacionAprobacion confirmar(String firmaTransactionId) throws GeneralException {
        try {
            if (firmaTransactionId != null) {

                byte[] doc = firmaElectronicaBean.obtenerDocumentoFirmado(firmaTransactionId, "confirmar_aprobacion.pdf");

                FiltroConfirmacionAprobacion fil = new FiltroConfirmacionAprobacion();
                fil.setFirmaTransactionId(firmaTransactionId);
                fil.setFirmada(Boolean.FALSE);
                
                List<SgConfirmacionAprobacion> confs = this.obtenerPorFiltro(fil);

                if (!confs.isEmpty()) {

                    SgConfirmacionAprobacion cma = confs.get(0);
                    cma.setCprFirmada(Boolean.TRUE);
                    cma.setCprFechaConfirmacion(LocalDateTime.now());
                    cma.setCprUsuarioConfirmacion(Lookup.obtenerJWT().getSubject());

                    Path folder = Paths.get(tmpBasePath);
                    Path tmpFile = Files.createTempFile(folder, null, ".pdf");
                    Files.write(tmpFile, doc);

                    SgArchivo ar = new SgArchivo();
                    ar.setAchTmpPath(tmpFile.getFileName().toString());
                    ar.setAchExt("pdf");
                    ar.setAchContentType("application/pdf");
                    ar.setAchNombre("confirmar_aprobacion_" + cma.getCprSeccion().getSecCodigo() + "_" + cma.getCprSeccion().getSecAnioLectivo().getAleAnio());
                    cma.setCprArchivoFirmado(ar);

                    CodigueraDAO<SgConfirmacionAprobacion> codDAO = new CodigueraDAO<>(em, SgConfirmacionAprobacion.class);
                    return codDAO.guardar(cma, null);

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
     * @param filtro FiltroConfirmacionPromocion
     * @return Long
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long obtenerTotalPorFiltro(FiltroConfirmacionAprobacion filtro) throws GeneralException {
        try {
            ConfirmacionAprobacionDAO codDAO = new ConfirmacionAprobacionDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroConfirmacionPromocion
     * @return Lista de <SgConfirmacionAprobacion>
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgConfirmacionAprobacion> obtenerPorFiltro(FiltroConfirmacionAprobacion filtro) throws GeneralException {
        try {
            ConfirmacionAprobacionDAO codDAO = new ConfirmacionAprobacionDAO(em);
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
                CodigueraDAO<SgConfirmacionAprobacion> codDAO = new CodigueraDAO<>(em, SgConfirmacionAprobacion.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
