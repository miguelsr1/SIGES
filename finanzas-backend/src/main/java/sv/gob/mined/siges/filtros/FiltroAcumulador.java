/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import sv.gob.mined.siges.enumerados.EnumAcumulador;

/**
 * Clase que se utiliza para los filtros de las consultas acumulativas.
 *
 * @author jgiron
 */
public class FiltroAcumulador implements Serializable {

    private EnumAcumulador acum1;
    private EnumAcumulador acum2;

    private Boolean formulado = Boolean.FALSE;
    private Boolean aprobado = Boolean.FALSE;
    private String sedeEducativa = null;

    // <editor-fold defaultstate="collapsed" desc="getters y setters">
    public EnumAcumulador getAcum1() {
        return acum1;
    }

    public void setAcum1(EnumAcumulador acum1) {
        this.acum1 = acum1;
    }

    public EnumAcumulador getAcum2() {
        return acum2;
    }

    public void setAcum2(EnumAcumulador acum2) {
        this.acum2 = acum2;
    }

    public Boolean getFormulado() {
        return formulado;
    }

    public void setFormulado(Boolean formulado) {
        this.formulado = formulado;
    }

    public String getSedeEducativa() {
        return sedeEducativa;
    }

    public void setSedeEducativa(String sedeEducativa) {
        this.sedeEducativa = sedeEducativa;
    }

    public Boolean getAprobado() {
        return aprobado;
    }

    public void setAprobado(Boolean aprobado) {
        this.aprobado = aprobado;
    }
    // </editor-fold>
}
