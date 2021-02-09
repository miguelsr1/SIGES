/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author Sofis Solutions
 */

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "traPk", scope = SgTrastornoAprendizaje.class)
public class SgTrastornoAprendizaje implements Serializable {

    private static final long serialVersionUID = 1L;

 
    private Long traPk;

    private String traCodigo;

    private String traNombre;

    private String traNombreBusqueda;
            
    private Boolean traHabilitado;

    private LocalDateTime traUltModFecha;

    private String traUltModUsuario;

    private Integer traVersion;

    public SgTrastornoAprendizaje() {
        this.traHabilitado = Boolean.TRUE;
    }


    public String getTraNombreBusqueda() {
        return traNombreBusqueda;
    }

    public void setTraNombreBusqueda(String traNombreBusqueda) {
        this.traNombreBusqueda = traNombreBusqueda;
    }

    public Long getTraPk() {
        return traPk;
    }

    public void setTraPk(Long traPk) {
        this.traPk = traPk;
    }

    public String getTraCodigo() {
        return traCodigo;
    }

    public void setTraCodigo(String traCodigo) {
        this.traCodigo = traCodigo;
    }

    public String getTraNombre() {
        return traNombre;
    }

    public void setTraNombre(String traNombre) {
        this.traNombre = traNombre;
    }

    public Boolean getTraHabilitado() {
        return traHabilitado;
    }

    public void setTraHabilitado(Boolean traHabilitado) {
        this.traHabilitado = traHabilitado;
    }

    public LocalDateTime getTraUltModFecha() {
        return traUltModFecha;
    }

    public void setTraUltModFecha(LocalDateTime traUltModFecha) {
        this.traUltModFecha = traUltModFecha;
    }

    public String getTraUltModUsuario() {
        return traUltModUsuario;
    }

    public void setTraUltModUsuario(String traUltModUsuario) {
        this.traUltModUsuario = traUltModUsuario;
    }

    public Integer getTraVersion() {
        return traVersion;
    }

    public void setTraVersion(Integer traVersion) {
        this.traVersion = traVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (traPk != null ? traPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgTrastornoAprendizaje)) {
            return false;
        }
        SgTrastornoAprendizaje other = (SgTrastornoAprendizaje) object;
        if ((this.traPk == null && other.traPk != null) || (this.traPk != null && !this.traPk.equals(other.traPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgTrastornoAprendizaje[ traPk=" + traPk + " ]";
    }

}
