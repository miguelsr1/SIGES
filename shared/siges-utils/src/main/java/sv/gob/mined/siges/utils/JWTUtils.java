/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.utils;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwa.AlgorithmConstraints.ConstraintType;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;

public class JWTUtils {

    public static String generarTokenContexto(String usuario,
            String usuarioIp,
            String privateKeyPath,
            List<String> operaciones,
            Integer expirationTimeMinutes,
            String ambito,
            Long contexto,
            String issuer,
            String audience) throws Exception {
        PrivateKey p = readPrivateKey(privateKeyPath); //TODO: leer de directorio compartido
        JwtClaims claims = new JwtClaims();
        claims.setIssuer(issuer);
        claims.setAudience(audience);
        claims.setExpirationTimeMinutesInTheFuture(expirationTimeMinutes);
        claims.setGeneratedJwtId();
        claims.setIssuedAtToNow();
        claims.setNotBeforeMinutesInThePast(2);
        claims.setSubject(usuario);
        if (usuarioIp != null) {
            claims.setClaim("ip", usuarioIp);
        }
        if (ambito != null) {
            claims.setClaim("ambito", ambito);
        }
        if (contexto != null) {
            claims.setClaim("contexto", contexto.toString());
        }
        if (operaciones != null) {
            claims.setStringListClaim("groups", operaciones);
        }

        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setKey(p);
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
        String jwt = jws.getCompactSerialization();
        return jwt;
    }

    public static String generarToken(String usuario,
            String usuarioIp,
            String privateKeyPath,
            List<String> operaciones,
            Integer expirationTimeMinutes,
            String issuer,
            String audience) throws Exception {
        PrivateKey p = readPrivateKey(privateKeyPath);
        JwtClaims claims = new JwtClaims();
        claims.setIssuer(issuer);
        claims.setAudience(audience);
        claims.setExpirationTimeMinutesInTheFuture(expirationTimeMinutes);
        claims.setGeneratedJwtId();
        claims.setIssuedAtToNow();
        claims.setNotBeforeMinutesInThePast(2);
        claims.setSubject(usuario);
        if (usuarioIp != null) {
            claims.setClaim("ip", usuarioIp);
        }
        if (operaciones != null) {
            claims.setStringListClaim("groups", operaciones);
        }
        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setKey(p);
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
        String jwt = jws.getCompactSerialization();
        return jwt;
    }

    public static String generarToken(String usuario,
            String usuarioIp,
            String privateKeyPath,
            List<String> operaciones,
            Integer expirationTimeMinutes,
            String issuer,
            String audience,
            Integer maxResultadosPermitidos,
            Boolean incluirCamposRequerido) throws Exception {
        PrivateKey p = readPrivateKey(privateKeyPath);
        JwtClaims claims = new JwtClaims();
        claims.setIssuer(issuer);
        claims.setAudience(audience);
        claims.setExpirationTimeMinutesInTheFuture(expirationTimeMinutes);
        claims.setGeneratedJwtId();
        claims.setIssuedAtToNow();
        claims.setNotBeforeMinutesInThePast(2);
        claims.setSubject(usuario);
        if (usuarioIp != null) {
            claims.setClaim("ip", usuarioIp);
        }
        if (operaciones != null) {
            claims.setStringListClaim("groups", operaciones);
        }
        if (maxResultadosPermitidos != null) {
            claims.setClaim("maxResultadosPermitidos", maxResultadosPermitidos);
        }
        if (incluirCamposRequerido != null) {
            claims.setClaim("incluirCamposRequerido", incluirCamposRequerido);
        }
        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setKey(p);
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
        String jwt = jws.getCompactSerialization();
        return jwt;
    }

    @Deprecated
    public static String generarToken(String usuario, String usuarioIp, String privateKeyPath, List<String> operaciones, Integer expirationTimeMinutes) throws Exception {
        return JWTUtils.generarToken(usuario, usuarioIp, privateKeyPath, operaciones, expirationTimeMinutes, "SIGES", "SIGES");

    }

    @Deprecated
    public static JwtClaims validarToken(String token, String publicKeyPath) throws InvalidJwtException, Exception {
        return JWTUtils.validarToken(token, publicKeyPath, "SIGES", "SIGES");
    }

    public static JwtClaims validarToken(String token, String publicKeyPath, String issuer, String audience) throws InvalidJwtException, Exception {
        PublicKey p = readPublicKey(publicKeyPath);
        JwtConsumerBuilder builder = new JwtConsumerBuilder()
                .setRequireExpirationTime()
                .setAllowedClockSkewInSeconds(30)
                .setRequireSubject()
                .setExpectedIssuer(issuer)
                .setVerificationKey(p)
                .setJwsAlgorithmConstraints(new AlgorithmConstraints(ConstraintType.WHITELIST, AlgorithmIdentifiers.RSA_USING_SHA256));
        if (audience != null) {
            builder.setExpectedAudience(audience);
        } else {
            builder.setSkipDefaultAudienceValidation();
        }
        JwtConsumer jwtConsumer = builder.build();
        JwtClaims jwt = jwtConsumer.processToClaims(token);
        return jwt;
    }

    public static JwtClaims readToken(String token) throws InvalidJwtException, Exception {
        JwtConsumer consumerAgain = new JwtConsumerBuilder()
                .setSkipAllValidators()
                .setDisableRequireSignature()
                .setSkipSignatureVerification()
                .build();
        return consumerAgain.processToClaims(token);
    }

    public static PrivateKey readPrivateKey(final String pemResName) throws Exception {
        InputStream contentIS = JWTUtils.class.getResourceAsStream(pemResName);
        byte[] tmp = new byte[4096];
        int length = contentIS.read(tmp);
        return decodePrivateKey(new String(tmp, 0, length));
    }

    public static PublicKey readPublicKey(final String pubResName) throws Exception {
        InputStream contentIS = JWTUtils.class.getResourceAsStream(pubResName);
        byte[] tmp = new byte[4096];
        int length = contentIS.read(tmp);
        return decodePublicKey(new String(tmp, 0, length));
    }

    public static PrivateKey decodePrivateKey(final String pemEncoded) throws Exception {
        byte[] encodedBytes = toEncodedBytes(pemEncoded);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(keySpec);
    }

    public static PublicKey decodePublicKey(final String pubEncoded) throws Exception {
        byte[] encodedBytes = toEncodedBytes(pubEncoded);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encodedBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(keySpec);
    }

    private static byte[] toEncodedBytes(final String pemEncoded) {
        final String normalizedPem = removeBeginEnd(pemEncoded);
        return Base64.getDecoder().decode(normalizedPem);
    }

    private static String removeBeginEnd(String pem) {
        pem = pem.replaceAll("-----BEGIN (.*)-----", "");
        pem = pem.replaceAll("-----END (.*)----", "");
        pem = pem.replaceAll("\r\n", "");
        pem = pem.replaceAll("\n", "");
        return pem.trim();
    }

}
