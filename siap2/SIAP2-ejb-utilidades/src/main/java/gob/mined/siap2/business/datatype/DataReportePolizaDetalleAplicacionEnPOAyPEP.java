package gob.mined.siap2.business.datatype;

import gob.mined.siap2.business.datatype.DataReporteTemplate;
import java.util.List;

/**
 *
 * @author eduardo
 */
public class DataReportePolizaDetalleAplicacionEnPOAyPEP extends DataReporteTemplate {

    private String codigoRecurso;
    private String oeg;
    private String mes;
    private String fuenteFinanciamiento;
    private String valorAplicado;
    
    
    public String getCodigoRecurso() {
        return codigoRecurso;
    }

    public void setCodigoRecurso(String codigoRecurso) {
        this.codigoRecurso = codigoRecurso;
    }

    public String getOeg() {
        return oeg;
    }

    public void setOeg(String oeg) {
        this.oeg = oeg;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getFuenteFinanciamiento() {
        return fuenteFinanciamiento;
    }

    public void setFuenteFinanciamiento(String fuenteFinanciamiento) {
        this.fuenteFinanciamiento = fuenteFinanciamiento;
    }

    public String getValorAplicado() {
        return valorAplicado;
    }

    public void setValorAplicado(String valorAplicado) {
        this.valorAplicado = valorAplicado;
    }

}
