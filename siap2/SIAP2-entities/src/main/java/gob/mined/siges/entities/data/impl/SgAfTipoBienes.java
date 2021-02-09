/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package gob.mined.siges.entities.data.impl;

import gob.mined.siap2.entities.constantes.Constantes;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_af_tipo_bienes", schema = Constantes.SCHEMA_CATALOGO)
@EntityListeners({DecoratorEntityListener.class})
@Cacheable(false)
public class SgAfTipoBienes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "tbi_pk")
    private Long tbiPk;

    @Size(max = 4)
    @Column(name = "tbi_codigo", length = 4)
    private String tbiCodigo;
    
    @Size(max = 255)
    @Column(name = "tbi_nombre", length = 255)
    private String tbiNombre;
    
    @Size(max = 255)
    @Column(name = "tbi_nombre_busqueda", length = 255)
    private String tbiNombreBusqueda;
    
    @ManyToOne
    @JoinColumn(name = "tbi_categoria_bienes_fk")
    private SgAfCategoriaBienes tbiCategoriaBienes;
    
    private transient String tbiCodigoNombreCategoria;
    
    public SgAfTipoBienes() {
    }

    public Long getTbiPk() {
        return tbiPk;
    }

    public void setTbiPk(Long tbiPk) {
        this.tbiPk = tbiPk;
    }

    public String getTbiCodigo() {
        return tbiCodigo;
    }

    public void setTbiCodigo(String tbiCodigo) {
        this.tbiCodigo = tbiCodigo;
    }
    
    public SgAfCategoriaBienes getTbiCategoriaBienes() {
        return tbiCategoriaBienes;
    }

    public void setTbiCategoriaBienes(SgAfCategoriaBienes tbiCategoriaBienes) {
        this.tbiCategoriaBienes = tbiCategoriaBienes;
    }

    public String getTbiNombre() {
        return tbiNombre;
    }

    public void setTbiNombre(String tbiNombre) {
        this.tbiNombre = tbiNombre;
    }

    public String getTbiCodigoNombreCategoria() {
        return tbiCodigoNombreCategoria;
    }

    public void setTbiCodigoNombreCategoria(String tbiCodigoNombreCategoria) {
        this.tbiCodigoNombreCategoria = tbiCodigoNombreCategoria;
    }

    public String getTbiNombreBusqueda() {
        return tbiNombreBusqueda;
    }

    public void setTbiNombreBusqueda(String tbiNombreBusqueda) {
        this.tbiNombreBusqueda = tbiNombreBusqueda;
    }
    
    @PostLoad
    public void cargarDatos() {
        this.tbiCodigoNombreCategoria = this.tbiCodigo != null ? this.tbiCodigo.trim() : "";
        if(StringUtils.isNotBlank(this.tbiNombre)) {
            this.tbiCodigoNombreCategoria += "-" + this.tbiNombre;
        }
        if(this.tbiCategoriaBienes != null && this.tbiCategoriaBienes.getCabNombre() != null && StringUtils.isNotBlank(this.tbiCategoriaBienes.getCabNombre())) {
            this.tbiCodigoNombreCategoria += "-" + this.tbiCategoriaBienes.getCabNombre().trim();
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.tbiPk);
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
        final SgAfTipoBienes other = (SgAfTipoBienes) obj;
        if (!Objects.equals(this.tbiPk, other.tbiPk)) {
            return false;
        }
        return true;
    }
    
    
 
    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgTipoBienes[ tbiPk =" + tbiPk + " ]";
    }

}
