/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import sv.gob.mined.siges.web.dto.catalogo.SgSiPromotor;

/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "sinPk", scope = SgSistemaIntegrado.class)
public class SgSistemaIntegrado implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long sinPk;
    
    private String sinCodigo;
    
    private String sinNombre;
    
    private Boolean sinHabilitado;
    
    private String sinDescripcion;
    
    private LocalDateTime sinUltModFecha;
    
    private String sinUltModUsuario;
    
    private Integer sinVersion;
    
    private Long totalSedes;
    
    private SgSiPromotor sinPromotor;

    public SgSistemaIntegrado() {
        this.sinHabilitado = Boolean.TRUE;
    }
    
    @JsonIgnore
    public String getSinCodigoNombre(){
        return this.sinCodigo+" - "+this.sinNombre;
    }

    public Long getSinPk() {
        return sinPk;
    }

    public void setSinPk(Long sinPk) {
        this.sinPk = sinPk;
    }

    public String getSinCodigo() {
        return sinCodigo;
    }

    public void setSinCodigo(String sinCodigo) {
        this.sinCodigo = sinCodigo;
    }

    public String getSinNombre() {
        return sinNombre;
    }

    public void setSinNombre(String sinNombre) {
        this.sinNombre = sinNombre;
    }

    public Boolean getSinHabilitado() {
        return sinHabilitado;
    }

    public void setSinHabilitado(Boolean sinHabilitado) {
        this.sinHabilitado = sinHabilitado;
    }

    public String getSinDescripcion() {
        return sinDescripcion;
    }

    public void setSinDescripcion(String sinDescripcion) {
        this.sinDescripcion = sinDescripcion;
    }

    public LocalDateTime getSinUltModFecha() {
        return sinUltModFecha;
    }

    public void setSinUltModFecha(LocalDateTime sinUltModFecha) {
        this.sinUltModFecha = sinUltModFecha;
    }    

    public String getSinUltModUsuario() {
        return sinUltModUsuario;
    }

    public void setSinUltModUsuario(String sinUltModUsuario) {
        this.sinUltModUsuario = sinUltModUsuario;
    }

    public Integer getSinVersion() {
        return sinVersion;
    }

    public void setSinVersion(Integer sinVersion) {
        this.sinVersion = sinVersion;
    }

    public Long getTotalSedes() {
        return totalSedes;
    }

    public void setTotalSedes(Long totalSedes) {
        this.totalSedes = totalSedes;
    }

    public SgSiPromotor getSinPromotor() {
        return sinPromotor;
    }

    public void setSinPromotor(SgSiPromotor sinPromotor) {
        this.sinPromotor = sinPromotor;
    }


    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sinPk != null ? sinPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgSistemaIntegrado)) {
            return false;
        }
        SgSistemaIntegrado other = (SgSistemaIntegrado) object;
        if ((this.sinPk == null && other.sinPk != null) || (this.sinPk != null && !this.sinPk.equals(other.sinPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgSistemaIntegrado[ sinPk=" + sinPk + " ]";
    }
    
}
