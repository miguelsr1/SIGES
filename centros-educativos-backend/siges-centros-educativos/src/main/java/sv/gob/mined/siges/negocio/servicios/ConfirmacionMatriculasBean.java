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
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroConfirmacionMatriculas;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.validaciones.ConfirmacionMatriculasValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.ConfirmacionMatriculasDAO;
import sv.gob.mined.siges.persistencia.entidades.SgArchivo;
import sv.gob.mined.siges.persistencia.entidades.SgConfirmacionMatriculas;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgConfiguracionFirmaElectronica;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;
import sv.gob.mined.siges.wsclient.ResultadoValidacion;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
@Traced
public class ConfirmacionMatriculasBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private SeguridadBean seguridadBean;

    @Inject
    private FirmaElectronicaBean firmaElectronicaBean;

    @Inject
    private ConfiguracionFirmaElectronicaBean configFirmaElectronicaBean;

    @Inject
    @ConfigProperty(name = "files.tmp.path")
    private String tmpBasePath;

    private static final Logger LOGGER = Logger.getLogger(ConfirmacionMatriculasBean.class.getName());

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgConfirmacionMatriculas
     * @throws GeneralException
     */
    public SgConfirmacionMatriculas obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgConfirmacionMatriculas> codDAO = new CodigueraDAO<>(em, SgConfirmacionMatriculas.class);
                return codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_CONFIRMACIONES_MATRICULAS);
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
                CodigueraDAO<SgConfirmacionMatriculas> codDAO = new CodigueraDAO<>(em, SgConfirmacionMatriculas.class);
                return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_CONFIRMACIONES_MATRICULAS);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param cma SgConfirmacionMatriculas
     * @return SgConfirmacionMatriculas
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgConfirmacionMatriculas preconfirmar(SgConfirmacionMatriculas cma) throws GeneralException {
        try {
            if (cma != null) {
                CloseableHttpClient httpclient = null;
                CloseableHttpResponse response = null;
                int timeout = 2 * 120 * 1000;
                if (ConfirmacionMatriculasValidacionNegocio.validar(cma)) {

                    SgConfiguracionFirmaElectronica conf = configFirmaElectronicaBean.obtenerPorCodigo(Constantes.CONFIG_FIRMA_ELECTRONICA_CONFIRMACION_MATRICULAS);

                    if (conf != null && BooleanUtils.isTrue(conf.getConActivada())) {

                        CodigueraDAO<SgConfirmacionMatriculas> codDAO = new CodigueraDAO<>(em, SgConfirmacionMatriculas.class);
                        cma.setCmaFirmada(Boolean.FALSE);

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

                        URIBuilder ubuilder = new URIBuilder(System.getProperty("service.report-generator.baseUrl") + "/ma/confirmacionMatriculaSede");
                        ubuilder.setParameter("sedId", cma.getCmaSede().getSedPk().toString())
                                .setParameter("a", cma.getCmaAnioLectivo().getAleAnio().toString());
                        HttpGet get = new HttpGet(ubuilder.build());
                        get.addHeader(org.apache.http.HttpHeaders.AUTHORIZATION, "Bearer " + Lookup.obtenerJWT().getRawToken());

                        response = httpclient.execute(get);

                        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                            InputStream is = response.getEntity().getContent();
                            byte[] byteArray = IOUtils.toByteArray(is);

                            List<byte[]> send = new ArrayList<>();
                            send.add(byteArray);

                            String usuarioEnviaYFirma = Lookup.obtenerJWT().getSubject();

                            List<ResultadoValidacion> res = firmaElectronicaBean.enviarDocumentosParaFirmar(send, cma.getCmaTransactionReturnUrl(), "confirmar_matricula.pdf", usuarioEnviaYFirma, usuarioEnviaYFirma);

                            if (res == null || res.isEmpty() || !res.get(0).getCode().equalsIgnoreCase("OK")) {
                                BusinessException be = new BusinessException();
                                be.addError("ERROR_AL_ENVIAR_DOCUMENTO");
                                throw be;
                            }

                            cma.setCmaFirmaTransactionId(res.get(0).getMessage());
                            cma = codDAO.guardar(cma, ConstantesOperaciones.CONFIRMAR_MATRICULAS);

                            cma.setCmaTransactionSignatureUrl(System.getProperty("service.firmar-documentos.baseUrl") + "?id_transaccion=" + res.get(0).getMessage());
                            return cma;
                        } else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_FORBIDDEN) {
                            BusinessException be = new BusinessException();
                            be.addError(Errores.ERROR_SEGURIDAD);
                            throw be;
                        } else {
                            throw new TechnicalException("ERROR_AL_GENERAR_PFD");
                        }

                    } else {

                        //Firma deshabilitada. Se confirma de forma inmediata
                        CodigueraDAO<SgConfirmacionMatriculas> codDAO = new CodigueraDAO<>(em, SgConfirmacionMatriculas.class);
                        cma.setCmaFechaConfirmacion(LocalDateTime.now());
                        cma.setCmaUsuarioConfirmacion(Lookup.obtenerJWT().getSubject());
                        cma.setCmaFirmada(Boolean.FALSE);
                        return codDAO.guardar(cma, ConstantesOperaciones.CONFIRMAR_MATRICULAS);

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
     * @return SgConfirmacionMatriculas
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgConfirmacionMatriculas confirmar(String firmaTransactionId) throws GeneralException {
        try {
            if (firmaTransactionId != null) {

                byte[] doc = firmaElectronicaBean.obtenerDocumentoFirmado(firmaTransactionId, "confirmar_matricula.pdf");

                FiltroConfirmacionMatriculas fil = new FiltroConfirmacionMatriculas();
                fil.setFirmaTransactionId(firmaTransactionId);
                fil.setFirmada(Boolean.FALSE);
                List<SgConfirmacionMatriculas> confs = this.obtenerPorFiltro(fil);

                if (!confs.isEmpty()) {

                    SgConfirmacionMatriculas cma = confs.get(0);
                    cma.setCmaFirmada(Boolean.TRUE);
                    cma.setCmaFechaConfirmacion(LocalDateTime.now());
                    cma.setCmaUsuarioConfirmacion(Lookup.obtenerJWT().getSubject());

                    Path folder = Paths.get(tmpBasePath);
                    Path tmpFile = Files.createTempFile(folder, null, ".pdf");
                    Files.write(tmpFile, doc);

                    SgArchivo ar = new SgArchivo();
                    ar.setAchTmpPath(tmpFile.getFileName().toString());
                    ar.setAchExt("pdf");
                    ar.setAchContentType("application/pdf");
                    ar.setAchNombre("confirmar_matricula_" + cma.getCmaSede().getSedCodigo() + "_" + cma.getCmaAnioLectivo().getAleAnio());
                    cma.setCmaArhivoFirmado(ar);

                    CodigueraDAO<SgConfirmacionMatriculas> codDAO = new CodigueraDAO<>(em, SgConfirmacionMatriculas.class);
                    return codDAO.guardar(cma, ConstantesOperaciones.CONFIRMAR_MATRICULAS);

                } else {
                    BusinessException be = new BusinessException();
                    be.addError(Errores.ERROR_NO_EXISTE_CONFIRMACION_SIN_FIRMAR_PARA_DATOS_INGRESADOS);
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
     * @param filtro FiltroConfirmacionMatriculas
     * @return Long
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long obtenerTotalPorFiltro(FiltroConfirmacionMatriculas filtro) throws GeneralException {
        try {
            ConfirmacionMatriculasDAO codDAO = new ConfirmacionMatriculasDAO(em, seguridadBean);
            return codDAO.obtenerTotalPorFiltro(filtro, ConstantesOperaciones.BUSCAR_CONFIRMACIONES_MATRICULAS);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroConfirmacionMatriculas
     * @return Lista de <SgConfirmacionMatriculas>
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgConfirmacionMatriculas> obtenerPorFiltro(FiltroConfirmacionMatriculas filtro) throws GeneralException {
        try {
            ConfirmacionMatriculasDAO codDAO = new ConfirmacionMatriculasDAO(em, seguridadBean);
            return codDAO.obtenerPorFiltro(filtro, ConstantesOperaciones.BUSCAR_CONFIRMACIONES_MATRICULAS);
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
                CodigueraDAO<SgConfirmacionMatriculas> codDAO = new CodigueraDAO<>(em, SgConfirmacionMatriculas.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
