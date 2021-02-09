/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.excepciones;

/**
 *
 * @author Sofis Solutions
 */
public class BusinessError {

    private String mensaje;
    private String campo;
    private Object[] variables;

    public BusinessError() {
    }

    public BusinessError(String mensaje) {
        this.mensaje = mensaje;
    }

    public BusinessError(String mensaje, String campo) {
        this.mensaje = mensaje;
        this.campo = campo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public Object[] getVariables() {
        return variables;
    }

    public void setVariables(Object[] variables) {
        this.variables = variables;
    }

    
}
