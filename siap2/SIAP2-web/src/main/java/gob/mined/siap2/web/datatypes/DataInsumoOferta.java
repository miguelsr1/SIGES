/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.datatypes;

import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionInsumo;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionItemOfertaInsumo;
import java.math.BigDecimal;

/**
 *
 * @author Sofis Solutions
 */
public class DataInsumoOferta {
    private ProcesoAdquisicionItemOfertaInsumo ofertaInsumo;
    private ProcesoAdquisicionInsumo procesoInsumo;
    private Integer cantidadEditable;
    private BigDecimal montoCalculadoProveedor;
    private boolean alcanzaAPagarOferta;
    

    public ProcesoAdquisicionItemOfertaInsumo getOfertaInsumo() {
        return ofertaInsumo;
    }

    public void setOfertaInsumo(ProcesoAdquisicionItemOfertaInsumo ofertaInsumo) {
        this.ofertaInsumo = ofertaInsumo;
    }

    public ProcesoAdquisicionInsumo getProcesoInsumo() {
        return procesoInsumo;
    }

    public void setProcesoInsumo(ProcesoAdquisicionInsumo procesoInsumo) {
        this.procesoInsumo = procesoInsumo;
    }

    public Integer getCantidadEditable() {
        return cantidadEditable;
    }

    public void setCantidadEditable(Integer cantidadEditable) {
        this.cantidadEditable = cantidadEditable;
    }

    public BigDecimal getMontoCalculadoProveedor() {
        return montoCalculadoProveedor;
    }

    public void setMontoCalculadoProveedor(BigDecimal montoCalculadoProveedor) {
        this.montoCalculadoProveedor = montoCalculadoProveedor;
    }

    public boolean getAlcanzaAPagarOferta() {
        return alcanzaAPagarOferta;
    }

    public void setAlcanzaAPagarOferta(boolean alcanzaAPagarOferta) {
        this.alcanzaAPagarOferta = alcanzaAPagarOferta;
    }
    
    
    
}
