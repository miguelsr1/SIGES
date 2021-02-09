/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.datatype;

import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionInsumo;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionItemOfertaInsumo;
import java.math.BigDecimal;

/**
 *
 * @author Sofis Solutions
 */
public class DataOfertaInsumo {
    private ProcesoAdquisicionInsumo procesoInsumo;
    private ProcesoAdquisicionItemOfertaInsumo ofetaInsumo;
    private BigDecimal montoOfertaInsumo;
    private Integer cantidadOfertaInsumo;
    private Boolean editCantidad;
    private Boolean alcanzaAPagarOferta;
    private Integer index;
    
    

    public ProcesoAdquisicionInsumo getProcesoInsumo() {
        return procesoInsumo;
    }

    public void setProcesoInsumo(ProcesoAdquisicionInsumo insumo) {
        this.procesoInsumo = insumo;
    }

    public BigDecimal getMontoOfertaInsumo() {
        return montoOfertaInsumo;
    }

    public void setMontoOfertaInsumo(BigDecimal ofertaInsumo) {
        this.montoOfertaInsumo = ofertaInsumo;
    }

    public ProcesoAdquisicionItemOfertaInsumo getOfetaInsumo() {
        return ofetaInsumo;
    }

    public void setOfetaInsumo(ProcesoAdquisicionItemOfertaInsumo ofetaInsumo) {
        this.ofetaInsumo = ofetaInsumo;
    }

    public Integer getCantidadOfertaInsumo() {
        return cantidadOfertaInsumo;
    }

    public void setCantidadOfertaInsumo(Integer cantidadOfertaInsumo) {
        this.cantidadOfertaInsumo = cantidadOfertaInsumo;
    }

    public Boolean getEditCantidad() {
        return editCantidad;
    }

    public void setEditCantidad(Boolean editCantidad) {
        this.editCantidad = editCantidad;
    }

    public Boolean getAlcanzaAPagarOferta() {
        return alcanzaAPagarOferta;
    }

    public void setAlcanzaAPagarOferta(Boolean alcanzaAPagarOferta) {
        this.alcanzaAPagarOferta = alcanzaAPagarOferta;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
    
    
    
}
