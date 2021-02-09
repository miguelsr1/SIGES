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

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "impPk", scope = SgImpresora.class)
public class SgImpresora implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long impPk;
    private String impCodigo;
    private Boolean impHabilitado;
    private String impNombre;
    private String impNombreBusqueda;
    private String impDescripcion;
    private LocalDateTime impUltModFecha;
    private String impUltModUsuario;
    private Integer impVersion;

    public SgImpresora() {
        this.impHabilitado = Boolean.TRUE;
    }

    public SgImpresora(Long mpePk) {
        this.impPk = mpePk;
    }

    public Long getImpPk() {
        return impPk;
    }

    public void setImpPk(Long impPk) {
        this.impPk = impPk;
    }

    public String getImpCodigo() {
        return impCodigo;
    }

    public void setImpCodigo(String impCodigo) {
        this.impCodigo = impCodigo;
    }

    public Boolean getImpHabilitado() {
        return impHabilitado;
    }

    public void setImpHabilitado(Boolean impHabilitado) {
        this.impHabilitado = impHabilitado;
    }

    public String getImpNombre() {
        return impNombre;
    }

    public void setImpNombre(String impNombre) {
        this.impNombre = impNombre;
    }

    public String getImpNombreBusqueda() {
        return impNombreBusqueda;
    }

    public void setImpNombreBusqueda(String impNombreBusqueda) {
        this.impNombreBusqueda = impNombreBusqueda;
    }

    public String getImpDescripcion() {
        return impDescripcion;
    }

    public void setImpDescripcion(String impDescripcion) {
        this.impDescripcion = impDescripcion;
    }

    public LocalDateTime getImpUltModFecha() {
        return impUltModFecha;
    }

    public void setImpUltModFecha(LocalDateTime impUltModFecha) {
        this.impUltModFecha = impUltModFecha;
    }

    public String getImpUltModUsuario() {
        return impUltModUsuario;
    }

    public void setImpUltModUsuario(String impUltModUsuario) {
        this.impUltModUsuario = impUltModUsuario;
    }

    public Integer getImpVersion() {
        return impVersion;
    }

    public void setImpVersion(Integer impVersion) {
        this.impVersion = impVersion;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.impPk);
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
        final SgImpresora other = (SgImpresora) obj;
        if (!Objects.equals(this.impPk, other.impPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgImpresora{" + "impPk=" + impPk + '}';
    }

    
}

