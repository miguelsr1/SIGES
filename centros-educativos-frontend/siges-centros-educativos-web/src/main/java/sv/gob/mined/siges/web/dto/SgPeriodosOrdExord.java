/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.web.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import sv.gob.mined.siges.web.enumerados.EnumCalendarioEscolar;

/**
 *
 * @author usuario
 */

public class SgPeriodosOrdExord implements Serializable {     
    
    private static final long serialVersionUID = 1L;
    
    private List<SgRangoFecha> rangoFechas;
    private List<EnumCalendarioEscolar> periodosExtraordinarios;
    private List<SgComponentePlanGrado> componentesPlanGrado;
    

    public SgPeriodosOrdExord() {
    }


    public List<SgRangoFecha> getRangoFechas() {
        return rangoFechas;
    }

    public void setRangoFechas(List<SgRangoFecha> rangoFechas) {
        this.rangoFechas = rangoFechas;
    }

    public List<EnumCalendarioEscolar> getPeriodosExtraordinarios() {
        return periodosExtraordinarios;
    }

    public void setPeriodosExtraordinarios(List<EnumCalendarioEscolar> periodosExtraordinarios) {
        this.periodosExtraordinarios = periodosExtraordinarios;
    }

    public List<SgComponentePlanGrado> getComponentesPlanGrado() {
        return componentesPlanGrado;
    }

    public void setComponentesPlanGrado(List<SgComponentePlanGrado> componentesPlanGrado) {
        this.componentesPlanGrado = componentesPlanGrado;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.rangoFechas);
        hash = 29 * hash + Objects.hashCode(this.periodosExtraordinarios);
        hash = 29 * hash + Objects.hashCode(this.componentesPlanGrado);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SgPeriodosOrdExord other = (SgPeriodosOrdExord) obj;
        if (!Objects.equals(this.rangoFechas, other.rangoFechas)) {
            return false;
        }
        if (!Objects.equals(this.periodosExtraordinarios, other.periodosExtraordinarios)) {
            return false;
        }
        if (!Objects.equals(this.componentesPlanGrado, other.componentesPlanGrado)) {
            return false;
        }
        return true;
    }


     
        

   
}
