/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Clase auxiliar para guardar los desembolsos de Dirección Dep.
 *
 * @author sofis-iquezada
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DesembolsoDDE implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private SgDesembolso desembolso;
    private List<RequerimientoPorRecurso> listReqsRecurso;
    private LocalDateTime fechaMov;

    public SgDesembolso getDesembolso() {
        return desembolso;
    }

    public void setDesembolso(SgDesembolso desembolso) {
        this.desembolso = desembolso;
    }

    public LocalDateTime getFechaMov() {
        return fechaMov;
    }

    public void setFechaMov(LocalDateTime fechaMov) {
        this.fechaMov = fechaMov;
    }

    public List<RequerimientoPorRecurso> getListReqsRecurso() {
        return listReqsRecurso;
    }

    public void setListReqsRecurso(List<RequerimientoPorRecurso> listReqsRecurso) {
        this.listReqsRecurso = listReqsRecurso;
    }

    // </editor-fold>
}
