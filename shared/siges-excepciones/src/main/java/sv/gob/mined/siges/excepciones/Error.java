/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.excepciones;

/**
 *
 * @author Sofis Solutions
 */
public class Error {
    
    private String mensaje;
    private String campo;
    private Object[] variables;

    public Error() {
    }
    
    public Error(String mensaje) {
        this.mensaje = mensaje;
    }

    public Error(String mensaje, String campo) {
        this.mensaje = mensaje;
        this.campo = campo;
    }
    
    public Error(String mensaje, String campo, Object[] variables) {
        this.mensaje = mensaje;
        this.campo = campo;
        this.variables = variables;
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
