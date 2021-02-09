/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.dto;

import java.util.List;

public class SgAuthRequest {

    //Basic
    private String username;
    private String password;
    private String audience; //clientid en cas
    private String audienceSecret;
    
    //OAuth
    private String oauthToken;

    
    //Shared
    private String address;
    private List<Long> categoriasOperacion;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Long> getCategoriasOperacion() {
        return categoriasOperacion;
    }

    public void setCategoriasOperacion(List<Long> categoriasOperacion) {
        this.categoriasOperacion = categoriasOperacion;
    }

    public String getOauthToken() {
        return oauthToken;
    }

    public void setOauthToken(String oauthToken) {
        this.oauthToken = oauthToken;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public String getAudienceSecret() {
        return audienceSecret;
    }

    public void setAudienceSecret(String audienceSecret) {
        this.audienceSecret = audienceSecret;
    }
    
    

}