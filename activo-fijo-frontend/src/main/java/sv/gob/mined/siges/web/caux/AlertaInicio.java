/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.caux;

/**
 *
 * @author USUARIO
 */
public class AlertaInicio {
    
    private String texto;
    private String irALabel;
    private String irA;

    public AlertaInicio(String texto, String irA, String irALabel) {
        this.texto = texto;
        this.irA = irA;
        this.irALabel = irALabel;
    }
    
    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getIrA() {
        return irA;
    }

    public void setIrA(String irA) {
        this.irA = irA;
    }

    public String getIrALabel() {
        return irALabel;
    }

    public void setIrALabel(String irALabel) {
        this.irALabel = irALabel;
    }
    
    
    
}
