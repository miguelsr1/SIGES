/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.datatypes;

import java.math.BigDecimal;

/**
 *
 * @author Sofis Solutions
 */
public class DataProveedorOferta {
    private Integer proveedorId;
    private String proveedorNombre;
    private BigDecimal montoOferta;
    private Boolean ofertaGanadora;

    public Integer getProveedorId() {
        return proveedorId;
    }

    public void setProveedorId(Integer proveedorId) {
        this.proveedorId = proveedorId;
    }

    public String getProveedorNombre() {
        return proveedorNombre;
    }

    public void setProveedorNombre(String proveedorNombre) {
        this.proveedorNombre = proveedorNombre;
    }

    public BigDecimal getMontoOferta() {
        return montoOferta;
    }

    public void setMontoOferta(BigDecimal montoOferta) {
        this.montoOferta = montoOferta;
    }

    public Boolean getOfertaGanadora() {
        return ofertaGanadora;
    }

    public void setOfertaGanadora(Boolean ofertaGanadora) {
        this.ofertaGanadora = ofertaGanadora;
    }

    
    
    
}
