/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoAcuerdo;
import sv.gob.mined.siges.web.enumerados.EnumEstadoAcuerdoSistema;

/**
 *
 * @author sofis2
 */

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "asiPk", scope = SgAcuerdoSistema.class)
public class SgAcuerdoSistema implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long asiPk;
    
    private SgSistemaIntegrado asiSistemaIntegrado;
    
    private SgTipoAcuerdo asiTipoAcuerdo;
    
    private Long asiNumeroAcuerdo;
    
    private LocalDate asiFechaCreacion;
    
    private EnumEstadoAcuerdoSistema asiEstado;
    
    private String asiObservaciones;
    
    private LocalDateTime asiUltModFecha;
    
    private String asiUltModUsuario;
    
    private Integer asiVersion;

    public SgAcuerdoSistema() {
        this.asiFechaCreacion = LocalDate.now();
    }

    public Long getAsiPk() {
        return asiPk;
    }

    public void setAsiPk(Long asiPk) {
        this.asiPk = asiPk;
    }

    public SgSistemaIntegrado getAsiSistemaIntegrado() {
        return asiSistemaIntegrado;
    }

    public void setAsiSistemaIntegrado(SgSistemaIntegrado asiSistemaIntegrado) {
        this.asiSistemaIntegrado = asiSistemaIntegrado;
    }

    public SgTipoAcuerdo getAsiTipoAcuerdo() {
        return asiTipoAcuerdo;
    }

    public void setAsiTipoAcuerdo(SgTipoAcuerdo asiTipoAcuerdo) {
        this.asiTipoAcuerdo = asiTipoAcuerdo;
    }

    public Long getAsiNumeroAcuerdo() {
        return asiNumeroAcuerdo;
    }

    public void setAsiNumeroAcuerdo(Long asiNumeroAcuerdo) {
        this.asiNumeroAcuerdo = asiNumeroAcuerdo;
    }

    public LocalDate getAsiFechaCreacion() {
        return asiFechaCreacion;
    }

    public void setAsiFechaCreacion(LocalDate asiFechaCreacion) {
        this.asiFechaCreacion = asiFechaCreacion;
    }

    public EnumEstadoAcuerdoSistema getAsiEstado() {
        return asiEstado;
    }

    public void setAsiEstado(EnumEstadoAcuerdoSistema asiEstado) {
        this.asiEstado = asiEstado;
    }

    public String getAsiObservaciones() {
        return asiObservaciones;
    }

    public void setAsiObservaciones(String asiObservaciones) {
        this.asiObservaciones = asiObservaciones;
    }

    public LocalDateTime getAsiUltModFecha() {
        return asiUltModFecha;
    }

    public void setAsiUltModFecha(LocalDateTime asiUltModFecha) {
        this.asiUltModFecha = asiUltModFecha;
    }

    public String getAsiUltModUsuario() {
        return asiUltModUsuario;
    }

    public void setAsiUltModUsuario(String asiUltModUsuario) {
        this.asiUltModUsuario = asiUltModUsuario;
    }

    public Integer getAsiVersion() {
        return asiVersion;
    }

    public void setAsiVersion(Integer asiVersion) {
        this.asiVersion = asiVersion;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (asiPk != null ? asiPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAcuerdoSistema)) {
            return false;
        }
        SgAcuerdoSistema other = (SgAcuerdoSistema) object;
        if ((this.asiPk == null && other.asiPk != null) || (this.asiPk != null && !this.asiPk.equals(other.asiPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgAcuerdoSistema[ asiPk=" + asiPk + " ]";
    }
    
}
