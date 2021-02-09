/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import sv.gob.mined.siges.filtros.FiltroRNPN;


public class SgConsultaPersonaDUIRNPNRequest implements Serializable {
    
    private String codiApli;
    private String nombUsua;
    private String direIP;
    private String tokn;
    private String numTram;
    private String nombFunc;
    private List<FiltroRNPN> filt;

    public SgConsultaPersonaDUIRNPNRequest() {
        this.filt = new ArrayList<>();
        this.codiApli = System.getProperty("service.rnpn.dui.aplicacion");
        this.nombUsua = System.getProperty("service.rnpn.dui.usuario");
        this.direIP = System.getProperty("service.rnpn.dui.ip");
        this.tokn = System.getProperty("service.rnpn.dui.token");
        this.numTram = System.getProperty("service.rnpn.dui.tramite");
        this.nombFunc = System.getProperty("service.rnpn.dui.funcion");
    }
    
    public String getCodiApli() {
        return codiApli;
    }

    public void setCodiApli(String codiApli) {
        this.codiApli = codiApli;
    }

    public String getNombUsua() {
        return nombUsua;
    }

    public void setNombUsua(String nombUsua) {
        this.nombUsua = nombUsua;
    }

    public String getDireIP() {
        return direIP;
    }

    public void setDireIP(String direIP) {
        this.direIP = direIP;
    }

    public String getTokn() {
        return tokn;
    }

    public void setTokn(String tokn) {
        this.tokn = tokn;
    }

    public String getNumTram() {
        return numTram;
    }

    public void setNumTram(String numTram) {
        this.numTram = numTram;
    }

    public String getNombFunc() {
        return nombFunc;
    }

    public void setNombFunc(String nombFunc) {
        this.nombFunc = nombFunc;
    }

    public List<FiltroRNPN> getFilt() {
        return filt;
    }

    public void setFilt(List<FiltroRNPN> filt) {
        this.filt = filt;
    }
}
