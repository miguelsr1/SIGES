/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.dto;

import java.io.Serializable;


public class SgConsultaPersonaDUIRNPNResponse implements Serializable {
    
    private Boolean resp;
    private SgConsultaPersonaDUIRNPNResponseData data;
   

    public SgConsultaPersonaDUIRNPNResponse() {
    }

    public Boolean getResp() {
        return resp;
    }

    public void setResp(Boolean resp) {
        this.resp = resp;
    }

    public SgConsultaPersonaDUIRNPNResponseData getData() {
        return data;
    }

    public void setData(SgConsultaPersonaDUIRNPNResponseData data) {
        this.data = data;
    }

   

    
}
