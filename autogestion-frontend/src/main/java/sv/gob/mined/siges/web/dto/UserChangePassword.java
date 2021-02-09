/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

/**
 *
 * @author Sofis Solutions
 */
public class UserChangePassword {
    
    private String passwordActual;
    private String passwordNueva;

    public String getPasswordActual() {
        return passwordActual;
    }

    public void setPasswordActual(String passwordActual) {
        this.passwordActual = passwordActual;
    }

    public String getPasswordNueva() {
        return passwordNueva;
    }

    public void setPasswordNueva(String passwordNueva) {
        this.passwordNueva = passwordNueva;
    }
    
    
    
}
