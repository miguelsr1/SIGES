/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package gob.mined.siges.entities.data.impl;

import gob.mined.siap2.entities.constantes.Constantes;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_af_categoria_bienes", schema = Constantes.SCHEMA_CATALOGO)
@Cacheable(false)
public class SgAfCategoriaBienes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "cab_pk")
    private Long cabPk;

    @Size(max = 4)
    @Column(name = "cab_codigo", length = 4)
    private String cabCodigo;

    @Size(max = 255)
    @Column(name = "cab_nombre", length = 255)
    private String cabNombre;
    
    @Column(name = "cab_vida_util")
    private Integer cabVidaUtil;
 
    @Column(name = "cab_valor_maximo")
    private BigDecimal cabValorMaximo;
    
    
    public SgAfCategoriaBienes() {
    }

    public Long getCabPk() {
        return cabPk;
    }

    public void setCabPk(Long cabPk) {
        this.cabPk = cabPk;
    }

    public String getCabCodigo() {
        return cabCodigo;
    }

    public void setCabCodigo(String cabCodigo) {
        this.cabCodigo = cabCodigo;
    }
    
    public Integer getCabVidaUtil() {
        return cabVidaUtil;
    }

    public void setCabVidaUtil(Integer cabVidaUtil) {
        this.cabVidaUtil = cabVidaUtil;
    }

    public BigDecimal getCabValorMaximo() {
        return cabValorMaximo;
    }

    public void setCabValorMaximo(BigDecimal cabValorMaximo) {
        this.cabValorMaximo = cabValorMaximo;
    }

    public String getCabNombre() {
        return cabNombre;
    }

    public void setCabNombre(String cabNombre) {
        this.cabNombre = cabNombre;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.cabPk);
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
        final SgAfCategoriaBienes other = (SgAfCategoriaBienes) obj;
        if (!Objects.equals(this.cabPk, other.cabPk)) {
            return false;
        }
        return true;
    }

    
    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgCategoriaBienes[ cabPk =" + cabPk + " ]";
    }

}
