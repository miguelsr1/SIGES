/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import sv.gob.mined.siges.web.dto.catalogo.SgEstNombreExtraccion;
import sv.gob.mined.siges.web.enumerados.EnumDesagregacion;
import sv.gob.mined.siges.web.enumerados.EnumTipoNumericoValorEstadistica;

/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "indPk", scope = SgIndicadorMaterializado.class)
public class SgIndicadorMaterializado implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long indPk;

    private Integer indAnio;
   
    private SgEstIndicador indIndicador;
    
    private SgEstNombreExtraccion indNombreExtraccion;
    
    private EnumDesagregacion indDesagregacion;
    
    private EnumTipoNumericoValorEstadistica indTipoNumerico;
       
    private LocalDateTime indUltmodFecha;

    private String indUltmodUsuario;

    private Integer indVersion;
        
 
    public Long getIndPk() {
        return indPk;
    }

    public void setIndPk(Long indPk) {
        this.indPk = indPk;
    }

    public Integer getIndAnio() {
        return indAnio;
    }

    public void setIndAnio(Integer indAnio) {
        this.indAnio = indAnio;
    }

    public LocalDateTime getIndUltmodFecha() {
        return indUltmodFecha;
    }

    public void setIndUltmodFecha(LocalDateTime indUltmodFecha) {
        this.indUltmodFecha = indUltmodFecha;
    }

    public String getIndUltmodUsuario() {
        return indUltmodUsuario;
    }

    public void setIndUltmodUsuario(String indUltmodUsuario) {
        this.indUltmodUsuario = indUltmodUsuario;
    }

    public Integer getIndVersion() {
        return indVersion;
    }

    public void setIndVersion(Integer indVersion) {
        this.indVersion = indVersion;
    }

    public SgEstIndicador getIndIndicador() {
        return indIndicador;
    }

    public void setIndIndicador(SgEstIndicador indIndicador) {
        this.indIndicador = indIndicador;
    }

    public EnumDesagregacion getIndDesagregacion() {
        return indDesagregacion;
    }

    public void setIndDesagregacion(EnumDesagregacion indDesagregacion) {
        this.indDesagregacion = indDesagregacion;
    }

    public EnumTipoNumericoValorEstadistica getIndTipoNumerico() {
        return indTipoNumerico;
    }

    public void setIndTipoNumerico(EnumTipoNumericoValorEstadistica indTipoNumerico) {
        this.indTipoNumerico = indTipoNumerico;
    }

    public SgEstNombreExtraccion getIndNombreExtraccion() {
        return indNombreExtraccion;
    }

    public void setIndNombreExtraccion(SgEstNombreExtraccion indNombreExtraccion) {
        this.indNombreExtraccion = indNombreExtraccion;
    }
    
    
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.indPk);
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
        final SgIndicadorMaterializado other = (SgIndicadorMaterializado) obj;
        if (!Objects.equals(this.indPk, other.indPk)) {
            return false;
        }
        return true;
    }

}
