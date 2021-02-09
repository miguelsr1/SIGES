/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import gob.mined.siap2.entities.constantes.Constantes;

/**
 * Esta entidad aplica sobre una vista, por lo tanto no soporta actualizaciones.
 * @author Sofis Solutions
 */
@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "view_compromiso_safi")
public class CompromisoSafi implements Serializable {
    

    @Id
    @Column(name = "inf_id")
    private Integer id;

    @Column(name = "inf_anio")
    private String ejercicio;    
    
    @Column(name = "inf_nit")
    private String nit;    
        
    @Column(name = "inf_nro_comprobante")
    private String nroCompromiso;    
            
    @Column(name = "inf_mes_comprometido")
    private String mesComprometido;

    @Column(name = "inf_comprometido")
    private BigDecimal comprometido;
    
    @Column(name = "inf_descomp")
    private BigDecimal descomp;
    
    @Column(name = "inf_congelado")
    private BigDecimal congelado;
    
    @Column(name = "inf_disponible")
    private BigDecimal disponible;
    
    @Column(name = "inf_mes_devengado")
    private String mesDevengado;    
    
    @Column(name = "inf_devengado")
    private BigDecimal devengado;
    
    @Column(name = "inf_mes_pagado")
    private String mesPagado;    
    
    @Column(name = "inf_pagado")
    private BigDecimal pagado;


    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(String ejercicio) {
        this.ejercicio = ejercicio;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getNroCompromiso() {
        return nroCompromiso;
    }

    public void setNroCompromiso(String nroCompromiso) {
        this.nroCompromiso = nroCompromiso;
    }

    public String getMesComprometido() {
        return mesComprometido;
    }

    public void setMesComprometido(String mesComprometido) {
        this.mesComprometido = mesComprometido;
    }

    public BigDecimal getComprometido() {
        return comprometido;
    }

    public void setComprometido(BigDecimal comprometido) {
        this.comprometido = comprometido;
    }

    public BigDecimal getDescomp() {
        return descomp;
    }

    public void setDescomp(BigDecimal descomp) {
        this.descomp = descomp;
    }

    public BigDecimal getCongelado() {
        return congelado;
    }

    public void setCongelado(BigDecimal congelado) {
        this.congelado = congelado;
    }

    public BigDecimal getDisponible() {
        return disponible;
    }

    public void setDisponible(BigDecimal disponible) {
        this.disponible = disponible;
    }

    public String getMesDevengado() {
        return mesDevengado;
    }

    public void setMesDevengado(String mesDevengado) {
        this.mesDevengado = mesDevengado;
    }

    public BigDecimal getDevengado() {
        return devengado;
    }

    public void setDevengado(BigDecimal devengado) {
        this.devengado = devengado;
    }

    public String getMesPagado() {
        return mesPagado;
    }

    public void setMesPagado(String mesPagado) {
        this.mesPagado = mesPagado;
    }

    public BigDecimal getPagado() {
        return pagado;
    }

    public void setPagado(BigDecimal pagado) {
        this.pagado = pagado;
    }



    // </editor-fold>
    
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CompromisoSafi other = (CompromisoSafi) obj;
        if (this.id!=null && other.id!=null) {
            return Objects.equals(this.id, other.id);
        }
        return (this== other);
    }    
    
}
