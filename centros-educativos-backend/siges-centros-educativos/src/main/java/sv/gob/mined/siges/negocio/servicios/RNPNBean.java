/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import org.eclipse.microprofile.opentracing.Traced;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.core.MediaType;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.dto.SgConsultaPersonaCUNRNPNResponse;
import sv.gob.mined.siges.dto.SgConsultaPersonaDUIRNPNRequest;
import sv.gob.mined.siges.dto.SgConsultaPersonaDUIRNPNResponse;
import sv.gob.mined.siges.dto.SgConsultaPersonaDUIRNPNResponseData;
import sv.gob.mined.siges.dto.SgConsultaPersonaPartidaNacRNPNResponse;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroRNPN;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgPersona;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgConfiguracion;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgDepartamento;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgMunicipio;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

@Stateless
@Traced
public class RNPNBean {

    private static final Logger LOGGER = Logger.getLogger(PersonaBean.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConfiguracionBean configBean;

    @Inject
    private RNPNAuditBean rnpnAuditBean;

    @Inject
    private PersonaBean personaBean;

    public void validarPersonasPendientesRNPN() throws GeneralException {
        try {

            LOGGER.log(Level.INFO, "Validando personas pendientes RNPN...");
            Session session = em.unwrap(Session.class);

            Criterion orCriteria = Restrictions.or(Restrictions.eq("perDuiPendienteValidacionRNPN", true),
                    Restrictions.eq("perCunPendienteValidacionRNPN", true),
                    Restrictions.eq("perPartidaNacPendienteValidacionRNPN", true));

            ScrollableResults results = session.createCriteria(SgPersona.class)
                    .add(orCriteria)
                    .setFetchSize(20)
                    .setMaxResults(400)
                    .scroll(ScrollMode.FORWARD_ONLY);

            int batch = 0;
            while (results.next()) {
                SgPersona p = (SgPersona) results.get(0);
                try {
                    if (BooleanUtils.isTrue(p.getPerDuiPendienteValidacionRNPN())) {
                        validarDUIPersonaRNPN(p);
                    } else if (BooleanUtils.isTrue(p.getPerCunPendienteValidacionRNPN())) {
                        validarCUNPersonaRNPN(p);
                    } else if (BooleanUtils.isTrue(p.getPerPartidaNacPendienteValidacionRNPN())) {
                        validarPartidaNacimientoPersonaRNPN(p);
                    }
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
                }
                if (batch % 40 == 0) {
                    session.flush();
                    session.clear();
                }
                batch++;
            }
            session.flush();
            session.clear();

            LOGGER.log(Level.INFO, "Fin validación personas. Cantidad procesadas: " + batch);

        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

    }

    private void validarDUIPersonaRNPN(SgPersona p) throws GeneralException {
        try {
            if (StringUtils.isBlank(p.getPerDui())) {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_DUI_VACIO);
                throw be;
            }

            SgPersona perRNPN = this.buscarPersonaPorDUI(p.getPerDui());

            p.setPerDuiValidadoRNPN(Boolean.TRUE);
            p.setPerCunValidadoRNPN(Boolean.FALSE);
            p.setPerPartidaNacValidadaRNPN(Boolean.FALSE);
            p.setPerDuiPendienteValidacionRNPN(Boolean.FALSE);
            p.setPerPrimerNombre(perRNPN.getPerPrimerNombre());
            p.setPerSegundoNombre(perRNPN.getPerSegundoNombre());
            if (perRNPN.getPerTercerNombre() != null) {
                p.setPerTercerNombre(perRNPN.getPerTercerNombre());
            }
            p.setPerPrimerApellido(perRNPN.getPerPrimerApellido());
            p.setPerSegundoApellido(perRNPN.getPerSegundoApellido());
            if (perRNPN.getPerTercerApellido() != null) {
                p.setPerTercerApellido(perRNPN.getPerTercerApellido());
            }
            if (perRNPN.getPerFechaNacimiento() != null) {
                p.setPerFechaNacimiento(perRNPN.getPerFechaNacimiento());
            }

            em.merge(p);

        } catch (BusinessException be) {
            if (BooleanUtils.isTrue(p.getPerDuiPendienteValidacionRNPN())) {
                personaBean.desmarcarPersonaParaValidacionDUIRNPN(p.getPerPk());
            }
            throw be;
        } catch (Exception ex) {
            personaBean.marcarPersonaParaValidacionDUIRNPN(p.getPerPk());
            throw new TechnicalException(ex);
        }
    }

    public void validarDUIPersonaRNPN(Long perPk) throws GeneralException {
        SgPersona p = em.find(SgPersona.class, perPk);
        validarDUIPersonaRNPN(p);
    }

    private void validarCUNPersonaRNPN(SgPersona p) throws GeneralException {
        try {

            if (p.getPerCun() == null) {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_CUN_VACIO);
                throw be;
            }

            SgPersona perRNPN = this.buscarPersonaPorCUN(p.getPerCun());

            p.setPerCunValidadoRNPN(Boolean.TRUE);
            p.setPerDuiValidadoRNPN(Boolean.FALSE);
            p.setPerPartidaNacValidadaRNPN(Boolean.FALSE);
            p.setPerCunPendienteValidacionRNPN(Boolean.FALSE);
            p.setPerPrimerNombre(perRNPN.getPerPrimerNombre());
            p.setPerSegundoNombre(perRNPN.getPerSegundoNombre());
            if (perRNPN.getPerTercerNombre() != null) {
                p.setPerTercerNombre(perRNPN.getPerTercerNombre());
            }
            p.setPerPrimerApellido(perRNPN.getPerPrimerApellido());
            p.setPerSegundoApellido(perRNPN.getPerSegundoApellido());
            if (perRNPN.getPerTercerApellido() != null) {
                p.setPerTercerApellido(perRNPN.getPerTercerApellido());
            }
            if (perRNPN.getPerFechaNacimiento() != null) {
                p.setPerFechaNacimiento(perRNPN.getPerFechaNacimiento());
            }

            em.merge(p);

        } catch (BusinessException be) {
            if (BooleanUtils.isTrue(p.getPerCunPendienteValidacionRNPN())) {
                personaBean.desmarcarPersonaParaValidacionCUNRNPN(p.getPerPk());
            }
            throw be;
        } catch (Exception ex) {
            personaBean.marcarPersonaParaValidacionCUNRNPN(p.getPerPk());
            throw new TechnicalException(ex);
        }
    }

    public void validarCUNPersonaRNPN(Long perPk) throws GeneralException {
        SgPersona p = em.find(SgPersona.class, perPk);
        validarCUNPersonaRNPN(p);
    }

    private void validarPartidaNacimientoPersonaRNPN(SgPersona p) throws GeneralException {
        try {

            if (p.getPerPartidaNacimiento() == null) {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_PARTIDA_NACIMIENTO_NUMERO_VACIO);
                throw be;
            }

            if (p.getPerPartidaNacimientoAnio() == null) {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_PARTIDA_NACIMIENTO_ANIO_VACIO);
                throw be;
            }

            if (StringUtils.isBlank(p.getPerPartidaNacimientoLibro())) {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_PARTIDA_NACIMIENTO_LIBRO_VACIO);
                throw be;
            }

            if (StringUtils.isBlank(p.getPerPartidaNacimientoFolio())) {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_PARTIDA_NACIMIENTO_FOLIO_VACIO);
                throw be;
            }

            if (p.getPerPartidaDepartamento() == null) {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_DEPARTAMENTO_PARTIDA_VACIO);
                throw be;
            }

            if (p.getPerPartidaMunicipio() == null) {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_MUNICIPIO_PARTIDA_VACIO);
                throw be;
            }

            SgPersona perRNPN = this.buscarPersonaPorPartidaNacimiento(
                    p.getPerPartidaNacimiento(),
                    p.getPerPartidaNacimientoAnio(),
                    p.getPerPartidaNacimientoFolio(),
                    p.getPerPartidaNacimientoLibro(),
                    p.getPerPartidaNacimientoComplemento(),
                    p.getPerPartidaDepartamento(),
                    p.getPerPartidaMunicipio());

            p.setPerPartidaNacValidadaRNPN(Boolean.TRUE);
            p.setPerDuiValidadoRNPN(Boolean.FALSE);
            p.setPerCunValidadoRNPN(Boolean.FALSE);
            p.setPerPartidaNacPendienteValidacionRNPN(Boolean.FALSE);

            p.setPerPrimerNombre(perRNPN.getPerPrimerNombre());
            p.setPerSegundoNombre(perRNPN.getPerSegundoNombre());
            if (perRNPN.getPerTercerNombre() != null) {
                p.setPerTercerNombre(perRNPN.getPerTercerNombre());
            }
            p.setPerPrimerApellido(perRNPN.getPerPrimerApellido());
            p.setPerSegundoApellido(perRNPN.getPerSegundoApellido());
            if (perRNPN.getPerTercerApellido() != null) {
                p.setPerTercerApellido(perRNPN.getPerTercerApellido());
            }
            if (perRNPN.getPerFechaNacimiento() != null) {
                p.setPerFechaNacimiento(perRNPN.getPerFechaNacimiento());
            }

            em.merge(p);

        } catch (BusinessException be) {
            if (BooleanUtils.isTrue(p.getPerPartidaNacPendienteValidacionRNPN())) {
                personaBean.desmarcarPersonaParaValidacionPartidaNacimientoRNPN(p.getPerPk());
            }
            throw be;
        } catch (Exception ex) {
            personaBean.marcarPersonaParaValidacionPartidaNacimientoRNPN(p.getPerPk());
            throw new TechnicalException(ex);
        }
    }

    public void validarPartidaNacimientoPersonaRNPN(Long perPk) throws GeneralException {
        SgPersona p = em.find(SgPersona.class, perPk);
        validarPartidaNacimientoPersonaRNPN(p);
    }

    public SgPersona buscarPersonaPorDUI(String dui) throws GeneralException {
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        int timeout = 30 * 1000; //30 segundos
        String documento = null;
        String jsonEnviar = null;
        InputStream inputStreamRecibido = null;
        String respuestaRecibida = null;
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String userCode = null;
        String endpoint = null;
        try {
            SgConfiguracion c = configBean.obtenerPorCodigo(Constantes.CONFIG_CONSUMO_SERVICIO_DUI_RNPN_HABILITADO);
            if (c != null && c.getConValor() != null && Boolean.parseBoolean(c.getConValor())) {

                SgConsultaPersonaDUIRNPNRequest consulta = new SgConsultaPersonaDUIRNPNRequest();
                if (StringUtils.isNotBlank(dui)) {

                    StringBuilder sb = new StringBuilder(dui);
                    sb.insert(sb.length() - 1, "-");
                    documento = sb.toString();

                    consulta.getFilt().add(new FiltroRNPN("dui", documento));
                } else {
                    BusinessException be = new BusinessException();
                    be.addError(Errores.ERROR_DUI_VACIO);
                    throw be;
                }

                JsonWebToken token = Lookup.obtenerJWT();
                if (token != null) {
                    userCode = token.getSubject();
                }

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

                URIBuilder ubuilder = new URIBuilder(System.getProperty("service.rnpn.dui.baseUrl"));
                URI uri = ubuilder.build();
                HttpPost post = new HttpPost(uri);

                endpoint = uri.toString();

                post.addHeader(org.apache.http.HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);

                jsonEnviar = om.writeValueAsString(consulta);

                StringEntity postingString = new StringEntity(jsonEnviar);
                post.setEntity(postingString);

                response = httpclient.execute(post);
                inputStreamRecibido = response.getEntity().getContent();
                respuestaRecibida = IOUtils.toString(inputStreamRecibido, StandardCharsets.UTF_8.name());

                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    SgConsultaPersonaDUIRNPNResponse respuesta = om.readValue(respuestaRecibida, SgConsultaPersonaDUIRNPNResponse.class);

                    if (respuesta.getData() == null || respuesta.getData() == null) {
                        rnpnAuditBean.guardarConsumo(dui, "DUI", jsonEnviar, respuestaRecibida, userCode, Errores.ERROR_NO_EXISTE_PERSONA_DUI_RNPN, endpoint);

                        BusinessException be = new BusinessException();
                        be.addError(Errores.ERROR_NO_EXISTE_PERSONA_DUI_RNPN);
                        throw be;
                    }

                    SgConsultaPersonaDUIRNPNResponseData data = respuesta.getData();
                    SgPersona p = new SgPersona();
                    p.setPerDui(dui);
                    p.setPerPrimerNombre(data.getNom1());
                    p.setPerSegundoNombre(data.getNom2());
                    p.setPerTercerNombre(data.getNom3());
                    p.setPerPrimerApellido(data.getApe1());
                    p.setPerSegundoApellido(data.getApe2());
                    p.setPerTercerApellido(data.getApelCsda());
                    p.setPerFechaNacimiento(data.getFechaNaci());
                    p.setPerDuiValidadoRNPN(Boolean.TRUE);
                    p.setPerDuiPendienteValidacionRNPN(Boolean.FALSE);

                    rnpnAuditBean.guardarConsumo(dui, "DUI", jsonEnviar, respuestaRecibida, userCode, null, endpoint);

                    return p;
                } else {
                    throw new Exception("ERROR_HTTP_STATUS_" + response.getStatusLine().getStatusCode());
                }

            } else {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_CONSUMO_DUI_RNPN_DESHABILITADO);
                throw be;
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            try {
                rnpnAuditBean.guardarExcepcion(dui, "DUI", jsonEnviar, respuestaRecibida, userCode, ex.getMessage(), endpoint);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
            throw new TechnicalException(ex);
        } finally {
            try {
                if (httpclient != null) {
                    httpclient.close();
                }
                if (response != null) {
                    response.close();
                }
                if (inputStreamRecibido != null) {
                    inputStreamRecibido.close();
                }
            } catch (Exception ex) {
            }
        }
    }

    public SgPersona buscarPersonaPorCUN(Long cun) throws GeneralException {
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        int timeout = 30 * 1000; //30 segundos
        String jsonEnviar = null;
        InputStream inputStreamRecibido = null;
        String respuestaRecibida = null;
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String userCode = null;
        String endpoint = null;
        try {
            SgConfiguracion c = configBean.obtenerPorCodigo(Constantes.CONFIG_CONSUMO_SERVICIO_CUN_RNPN_HABILITADO);
            if (c != null && c.getConValor() != null && Boolean.parseBoolean(c.getConValor())) {

                if (cun == null) {
                    BusinessException be = new BusinessException();
                    be.addError(Errores.ERROR_CUN_VACIO);
                    throw be;
                }

                JsonWebToken token = Lookup.obtenerJWT();
                if (token != null) {
                    userCode = token.getSubject();
                }

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

                URIBuilder ubuilder = new URIBuilder(System.getProperty("service.rnpn.cun.baseUrl"));
                ubuilder.setParameter("cun", cun.toString());

                URI uri = ubuilder.build();
                endpoint = uri.toString();

                HttpGet get = new HttpGet(uri);
                get.addHeader(org.apache.http.HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
                get.addHeader("x-xrd-messageid", System.getProperty("service.rnpn.cun.x-xrd-messageid"));
                get.addHeader("x-xrd-userid", System.getProperty("service.rnpn.cun.x-xrd-userid"));

                response = httpclient.execute(get);
                inputStreamRecibido = response.getEntity().getContent();
                respuestaRecibida = IOUtils.toString(inputStreamRecibido, StandardCharsets.UTF_8.name());

                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    SgConsultaPersonaCUNRNPNResponse[] respuesta = om.readValue(respuestaRecibida, SgConsultaPersonaCUNRNPNResponse[].class);

                    if (respuesta == null || respuesta.length == 0) {
                        rnpnAuditBean.guardarConsumo(cun.toString(), "CUN", jsonEnviar, respuestaRecibida, userCode, Errores.ERROR_NO_EXISTE_PERSONA_CUN_RNPN, endpoint);

                        BusinessException be = new BusinessException();
                        be.addError(Errores.ERROR_NO_EXISTE_PERSONA_CUN_RNPN);
                        throw be;
                    }

                    SgConsultaPersonaCUNRNPNResponse data = respuesta[0];
                    SgPersona p = new SgPersona();
                    p.setPerCun(cun);
                    p.setPerPrimerNombre(data.getNombre1());
                    p.setPerSegundoNombre(data.getNombre2());
                    p.setPerTercerNombre(data.getNombre3());
                    p.setPerPrimerApellido(data.getApellido1());
                    p.setPerSegundoApellido(data.getApellido2());
                    p.setPerTercerApellido(data.getApellido3());
                    if (data.getFechaHoraNacimiento() != null) {
                        p.setPerFechaNacimiento(data.getFechaHoraNacimiento().toLocalDate());
                    }
                    p.setPerCunValidadoRNPN(Boolean.TRUE);
                    p.setPerCunPendienteValidacionRNPN(Boolean.FALSE);

                    rnpnAuditBean.guardarConsumo(cun.toString(), "CUN", jsonEnviar, respuestaRecibida, userCode, null, endpoint);

                    return p;
                } else {
                    throw new Exception("ERROR_HTTP_STATUS_" + response.getStatusLine().getStatusCode());
                }

            } else {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_CONSUMO_CUN_RNPN_DESHABILITADO);
                throw be;
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            try {
                rnpnAuditBean.guardarExcepcion(cun.toString(), "CUN", jsonEnviar, respuestaRecibida, userCode, ex.getMessage(), endpoint);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
            throw new TechnicalException(ex);
        } finally {
            try {
                if (httpclient != null) {
                    httpclient.close();
                }
                if (response != null) {
                    response.close();
                }
                if (inputStreamRecibido != null) {
                    inputStreamRecibido.close();
                }
            } catch (Exception ex) {
            }
        }
    }
    
    public SgPersona buscarPersonaPorPartidaNacimiento(Long numero, Integer anio, String folio, String libro, String complemento, Long departamentoPartida, Long municipioPartida) throws GeneralException {
        return this.buscarPersonaPorPartidaNacimiento(numero, anio, folio, libro, complemento, em.find(SgDepartamento.class, departamentoPartida), em.find(SgMunicipio.class, municipioPartida));
    }

    private SgPersona buscarPersonaPorPartidaNacimiento(Long numero, Integer anio, String folio, String libro, String complemento, SgDepartamento departamentoPartida, SgMunicipio municipioPartida) throws GeneralException {
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        int timeout = 30 * 1000; //30 segundos
        String jsonEnviar = null;
        InputStream inputStreamRecibido = null;
        String respuestaRecibida = null;
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String userCode = null;
        String endpoint = null;
        StringBuilder documento = new StringBuilder();
        try {
            SgConfiguracion c = configBean.obtenerPorCodigo(Constantes.CONFIG_CONSUMO_SERVICIO_PARTIDA_NAC_RNPN_HABILITADO);
            if (c != null && c.getConValor() != null && Boolean.parseBoolean(c.getConValor())) {

                BusinessException be = new BusinessException();
                if (numero == null) {
                    be.addError(Errores.ERROR_PARTIDA_NACIMIENTO_NUMERO_VACIO);
                    throw be;
                }
                if (anio == null) {
                    be.addError(Errores.ERROR_PARTIDA_NACIMIENTO_ANIO_VACIO);
                    throw be;
                }
                if (StringUtils.isBlank(libro)) {
                    be.addError(Errores.ERROR_PARTIDA_NACIMIENTO_LIBRO_VACIO);
                    throw be;
                }
                if (StringUtils.isBlank(folio)) {
                    be.addError(Errores.ERROR_PARTIDA_NACIMIENTO_FOLIO_VACIO);
                    throw be;
                }
                if (departamentoPartida == null || StringUtils.isBlank(departamentoPartida.getDepCodigo()) || departamentoPartida.getDepPk() == null) {
                    be.addError(Errores.ERROR_DEPARTAMENTO_PARTIDA_VACIO);
                    throw be;
                }
                if (municipioPartida == null || StringUtils.isBlank(municipioPartida.getMunCodigo()) || municipioPartida.getMunPk() == null) {
                    be.addError(Errores.ERROR_MUNICIPIO_PARTIDA_VACIO);
                    throw be;
                }

                JsonWebToken token = Lookup.obtenerJWT();
                if (token != null) {
                    userCode = token.getSubject();
                }
                
                documento.append("N:").append(numero).append("-")
                        .append("A:").append(anio).append("-")
                        .append("L:").append(libro).append("-")
                        .append("F:").append(folio).append("-")
                        .append("D:").append(departamentoPartida.getDepCodigo()).append("-")
                        .append("M:").append(municipioPartida.getMunCodigo());
                if (StringUtils.isNotBlank(complemento)){
                    documento.append("-C:").append(complemento);
                }
                
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

                URIBuilder ubuilder = new URIBuilder(System.getProperty("service.rnpn.partida-nacimiento.baseUrl"));
                ubuilder.setParameter("num", numero.toString());
                ubuilder.setParameter("anio", anio.toString());
                ubuilder.setParameter("folio", folio);
                ubuilder.setParameter("libro", libro);
                ubuilder.setParameter("complemento", complemento);
                ubuilder.setParameter("departamento", departamentoPartida.getDepCodigo());
                ubuilder.setParameter("municipio", municipioPartida.getMunCodigo());

                URI uri = ubuilder.build();
                endpoint = uri.toString();

                HttpGet get = new HttpGet(uri);
                get.addHeader(org.apache.http.HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);

                response = httpclient.execute(get);
                inputStreamRecibido = response.getEntity().getContent();
                respuestaRecibida = IOUtils.toString(inputStreamRecibido, StandardCharsets.UTF_8.name());

                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    
                    SgConsultaPersonaPartidaNacRNPNResponse[] respuesta = om.readValue(respuestaRecibida, SgConsultaPersonaPartidaNacRNPNResponse[].class);

                    if (respuesta == null || respuesta.length == 0) {
                        rnpnAuditBean.guardarConsumo(documento.toString(), "PARTIDA_NAC", jsonEnviar, respuestaRecibida, userCode, Errores.ERROR_NO_EXISTE_PERSONA_PARTIDA_NACIMIENTO_RNPN, endpoint);

                        be.addError(Errores.ERROR_NO_EXISTE_PERSONA_PARTIDA_NACIMIENTO_RNPN);
                        throw be;
                    }

                    SgConsultaPersonaPartidaNacRNPNResponse data = respuesta[0];
                    SgPersona p = new SgPersona();
                    p.setPerPartidaNacimiento(numero);
                    p.setPerPartidaNacimientoAnio(anio);
                    p.setPerPartidaNacimientoLibro(libro);
                    p.setPerPartidaNacimientoFolio(folio);
                    p.setPerPartidaNacimientoComplemento(complemento);
                    p.setPerPartidaDepartamento(departamentoPartida);
                    p.setPerPartidaMunicipio(municipioPartida);
                    
                    p.setPerPrimerNombre(data.getNombre1());
                    p.setPerSegundoNombre(data.getNombre2());
                    p.setPerTercerNombre(data.getNombre3());
                    p.setPerPrimerApellido(data.getApellido1());
                    p.setPerSegundoApellido(data.getApellido2());
                    p.setPerTercerApellido(data.getApellido3());
                    if (data.getFechaHoraNacimiento() != null) {
                        p.setPerFechaNacimiento(data.getFechaHoraNacimiento().toLocalDate());
                    }
                    p.setPerPartidaNacValidadaRNPN(Boolean.TRUE);
                    p.setPerPartidaNacPendienteValidacionRNPN(Boolean.FALSE);

                    rnpnAuditBean.guardarConsumo(documento.toString(), "PARTIDA_NAC", jsonEnviar, respuestaRecibida, userCode, null, endpoint);

                    return p;
                } else {
                    throw new Exception("ERROR_HTTP_STATUS_" + response.getStatusLine().getStatusCode());
                }

            } else {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_CONSUMO_PARTIDA_NAC_RNPN_DESHABILITADO);
                throw be;
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            try {
                rnpnAuditBean.guardarExcepcion(documento.toString(), "PARTIDA_NAC", jsonEnviar, respuestaRecibida, userCode, ex.getMessage(), endpoint);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
            throw new TechnicalException(ex);
        } finally {
            try {
                if (httpclient != null) {
                    httpclient.close();
                }
                if (response != null) {
                    response.close();
                }
                if (inputStreamRecibido != null) {
                    inputStreamRecibido.close();
                }
            } catch (Exception ex) {
            }
        }
    }

}
