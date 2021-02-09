/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import sv.gob.mined.siges.utils.SofisStringUtils;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "tcaPk", scope = SgTipoCapacitacion.class)
public class SgTipoCapacitacion implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long tcaPk;

    private String tcaCodigo;

    private Boolean tcaHabilitado;

    private String tcaNombre;

    private String tcaNombreBusqueda;

    private LocalDateTime tcaUltModFecha;

    private String tcaUltModUsuario;

    private Integer tcaVersion;

    public SgTipoCapacitacion() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.tcaNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.tcaNombre);
    }

    public SgTipoCapacitacion(Long tcaPk) {
        this.tcaPk = tcaPk;
    }

    public Long getTcaPk() {
        return tcaPk;
    }

    public void setTcaPk(Long tcaPk) {
        this.tcaPk = tcaPk;
    }

    public String getTcaCodigo() {
        return tcaCodigo;
    }

    public void setTcaCodigo(String tcaCodigo) {
        this.tcaCodigo = tcaCodigo;
    }

    public Boolean getTcaHabilitado() {
        return tcaHabilitado;
    }

    public void setTcaHabilitado(Boolean tcaHabilitado) {
        this.tcaHabilitado = tcaHabilitado;
    }

    public String getTcaNombre() {
        return tcaNombre;
    }

    public void setTcaNombre(String tcaNombre) {
        this.tcaNombre = tcaNombre;
    }

    public String getTcaNombreBusqueda() {
        return tcaNombreBusqueda;
    }

    public void setTcaNombreBusqueda(String tcaNombreBusqueda) {
        this.tcaNombreBusqueda = tcaNombreBusqueda;
    }

    public LocalDateTime getTcaUltModFecha() {
        return tcaUltModFecha;
    }

    public void setTcaUltModFecha(LocalDateTime tcaUltModFecha) {
        this.tcaUltModFecha = tcaUltModFecha;
    }

    public String getTcaUltModUsuario() {
        return tcaUltModUsuario;
    }

    public void setTcaUltModUsuario(String tcaUltModUsuario) {
        this.tcaUltModUsuario = tcaUltModUsuario;
    }

    public Integer getTcaVersion() {
        return tcaVersion;
    }

    public void setTcaVersion(Integer tcaVersion) {
        this.tcaVersion = tcaVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tcaPk != null ? tcaPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgTipoCapacitacion)) {
            return false;
        }
        SgTipoCapacitacion other = (SgTipoCapacitacion) object;
        if ((this.tcaPk == null && other.tcaPk != null) || (this.tcaPk != null && !this.tcaPk.equals(other.tcaPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgTipoCapacitacion[ tcaPk=" + tcaPk + " ]";
    }

}
