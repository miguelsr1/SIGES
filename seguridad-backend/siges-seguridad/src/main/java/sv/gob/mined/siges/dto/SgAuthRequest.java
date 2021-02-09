/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.dto;

import java.util.List;

/**
 *
 * @author USUARIO
 */
public class SgAuthRequest {

    private String username;
    private String password;
    private Integer tokenExpirationMinutes;
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

    public Integer getTokenExpirationMinutes() {
        return tokenExpirationMinutes;
    }

    public void setTokenExpirationMinutes(Integer tokenExpirationMinutes) {
        this.tokenExpirationMinutes = tokenExpirationMinutes;
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

    
    


}