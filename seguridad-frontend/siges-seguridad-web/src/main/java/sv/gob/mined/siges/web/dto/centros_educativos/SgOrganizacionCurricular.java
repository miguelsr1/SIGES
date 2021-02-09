/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.centros_educativos;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property = "ocuPk", scope = SgOrganizacionCurricular.class)
public class SgOrganizacionCurricular implements Serializable {
    
    private static final long serialVersionUID = 1L;

    
    private Long ocuPk;

    private String ocuCodigo;

    private String ocuNombre;
    
    private String ocuDescripcion;

    private Boolean ocuHabilitado;

    private LocalDateTime ocuUltModFecha;

    private String ocuUltModUsuario;

    private Integer ocuVersion;
    
    private List<SgNivel> ocuNiveles;
    
    
    public SgOrganizacionCurricular() {
        this.ocuHabilitado = Boolean.TRUE;
        this.ocuNiveles=new ArrayList();
    }

    public Long getOcuPk() {
        return ocuPk;
    }

    public void setOcuPk(Long ocuPk) {
        this.ocuPk = ocuPk;
    }

    public String getOcuCodigo() {
        return ocuCodigo;
    }

    public void setOcuCodigo(String ocuCodigo) {
        this.ocuCodigo = ocuCodigo;
    }

    public String getOcuNombre() {
        return ocuNombre;
    }

    public void setOcuNombre(String ocuNombre) {
        this.ocuNombre = ocuNombre;
    }

    public String getOcuDescripcion() {
        return ocuDescripcion;
    }

    public void setOcuDescripcion(String ocuDescripcion) {
        this.ocuDescripcion = ocuDescripcion;
    }
    
    public LocalDateTime getOcuUltModFecha() {
        return ocuUltModFecha;
    }

    public void setOcuUltModFecha(LocalDateTime ocuUltModFecha) {
        this.ocuUltModFecha = ocuUltModFecha;
    }

    public String getOcuUltModUsuario() {
        return ocuUltModUsuario;
    }

    public void setOcuUltModUsuario(String ocuUltModUsuario) {
        this.ocuUltModUsuario = ocuUltModUsuario;
    }

    public Integer getOcuVersion() {
        return ocuVersion;
    }

    public void setOcuVersion(Integer ocuVersion) {
        this.ocuVersion = ocuVersion;
    }

    
     public Boolean getOcuHabilitado() {
        return ocuHabilitado;
    }

    public void setOcuHabilitado(Boolean ocuHabilitado) {
        this.ocuHabilitado = ocuHabilitado;
    }

    public List<SgNivel> getOcuNiveles() {    
        return ocuNiveles;
    }

    public void setOcuNiveles(List<SgNivel> ocuNiveles) {
        this.ocuNiveles = ocuNiveles;
    }

    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ocuPk != null ? ocuPk.hashCode() : 0);
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
        final SgOrganizacionCurricular other = (SgOrganizacionCurricular) obj;
        if (!Objects.equals(this.ocuPk, other.ocuPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgOrganizacionCurricular{" + "ocuPk=" + ocuPk + ", ocuCodigo=" + ocuCodigo + ", ocuNombre=" + ocuNombre + ", ocuDescripcion=" + ocuDescripcion + ", ocuHabilitado=" + ocuHabilitado + ", ocuUltModFecha=" + ocuUltModFecha + ", ocuUltModUsuario=" + ocuUltModUsuario + ", ocuVersion=" + ocuVersion + ", ocuNiveles=" + ocuNiveles + '}';
    }


    
    
}
