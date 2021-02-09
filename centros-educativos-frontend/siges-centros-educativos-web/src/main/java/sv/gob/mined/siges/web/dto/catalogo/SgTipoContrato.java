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
import java.util.List;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import sv.gob.mined.siges.utils.SofisStringUtils;
import sv.gob.mined.siges.web.dto.SgDatoContratacion;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "tcoPk", scope = SgTipoContrato.class)
public class SgTipoContrato implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long tcoPk;

    private String tcoCodigo;

    private Boolean tcoHabilitado;

    private String tcoNombre;

    private String tcoNombreBusqueda;

    private LocalDateTime tcoUltModFecha;

    private String tcoUltModUsuario;

    private Integer tcoVersion;
    
    private Boolean tcoEsInterinato;
    
    private Boolean tcoRequiereFechaHasta;

    private List<SgDatoContratacion> tcoDatoContratacion;

    public SgTipoContrato() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.tcoNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.tcoNombre);
    }

    public SgTipoContrato(Long tcoPk) {
        this.tcoPk = tcoPk;
    }

    public Long getTcoPk() {
        return tcoPk;
    }

    public void setTcoPk(Long tcoPk) {
        this.tcoPk = tcoPk;
    }

    public String getTcoCodigo() {
        return tcoCodigo;
    }

    public void setTcoCodigo(String tcoCodigo) {
        this.tcoCodigo = tcoCodigo;
    }

    public Boolean getTcoHabilitado() {
        return tcoHabilitado;
    }

    public void setTcoHabilitado(Boolean tcoHabilitado) {
        this.tcoHabilitado = tcoHabilitado;
    }

    public String getTcoNombre() {
        return tcoNombre;
    }

    public void setTcoNombre(String tcoNombre) {
        this.tcoNombre = tcoNombre;
    }

    public String getTcoNombreBusqueda() {
        return tcoNombreBusqueda;
    }

    public void setTcoNombreBusqueda(String tcoNombreBusqueda) {
        this.tcoNombreBusqueda = tcoNombreBusqueda;
    }

    public LocalDateTime getTcoUltModFecha() {
        return tcoUltModFecha;
    }

    public void setTcoUltModFecha(LocalDateTime tcoUltModFecha) {
        this.tcoUltModFecha = tcoUltModFecha;
    }

    public String getTcoUltModUsuario() {
        return tcoUltModUsuario;
    }

    public void setTcoUltModUsuario(String tcoUltModUsuario) {
        this.tcoUltModUsuario = tcoUltModUsuario;
    }

    public Integer getTcoVersion() {
        return tcoVersion;
    }

    public void setTcoVersion(Integer tcoVersion) {
        this.tcoVersion = tcoVersion;
    }

    public List<SgDatoContratacion> getTcoDatoContratacion() {
        return tcoDatoContratacion;
    }

    public void setTcoDatoContratacion(List<SgDatoContratacion> tcoDatoContratacion) {
        this.tcoDatoContratacion = tcoDatoContratacion;
    }

    public Boolean getTcoEsInterinato() {
        return tcoEsInterinato;
    }

    public void setTcoEsInterinato(Boolean tcoEsInterinato) {
        this.tcoEsInterinato = tcoEsInterinato;
    }

    public Boolean getTcoRequiereFechaHasta() {
        return tcoRequiereFechaHasta;
    }

    public void setTcoRequiereFechaHasta(Boolean tcoRequiereFechaHasta) {
        this.tcoRequiereFechaHasta = tcoRequiereFechaHasta;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tcoPk != null ? tcoPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgTipoContrato)) {
            return false;
        }
        SgTipoContrato other = (SgTipoContrato) object;
        if ((this.tcoPk == null && other.tcoPk != null) || (this.tcoPk != null && !this.tcoPk.equals(other.tcoPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgTipoContrato[ tcoPk=" + tcoPk + " ]";
    }

}
