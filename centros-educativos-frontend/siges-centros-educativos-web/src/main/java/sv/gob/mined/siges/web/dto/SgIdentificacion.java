/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import sv.gob.mined.siges.web.dto.catalogo.SgPais;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoIdentificacion;
import sv.gob.mined.siges.web.utilidades.ViewDto;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "idePk", scope = SgIdentificacion.class)
public class SgIdentificacion implements Serializable, ViewDto {

    private static final long serialVersionUID = 1L;

    private Long idePk;

    private LocalDate ideFechaExpedicion;

    private LocalDate ideFechaVencimiento;

    private String ideNumeroDocumento;

    private String ideFolio;

    private String ideLibro;

    private LocalDateTime ideUltModFecha;

    private String ideUltModUsuario;

    private Integer ideVersion;

    private SgPersona idePersona;

    private SgTipoIdentificacion ideTipoDocumento;

    private SgPais idePaisEmisor;

    private Boolean idePkForView;

    public SgIdentificacion() {
    }

    public Long getIdePk() {
        return idePk;
    }

    public void setIdePk(Long idePk) {
        this.idePk = idePk;
    }

    public LocalDate getIdeFechaExpedicion() {
        return ideFechaExpedicion;
    }

    public void setIdeFechaExpedicion(LocalDate ideFechaExpedicion) {
        this.ideFechaExpedicion = ideFechaExpedicion;
    }

    public LocalDate getIdeFechaVencimiento() {
        return ideFechaVencimiento;
    }

    public void setIdeFechaVencimiento(LocalDate ideFechaVencimiento) {
        this.ideFechaVencimiento = ideFechaVencimiento;
    }

    public String getIdeNumeroDocumento() {
        return ideNumeroDocumento;
    }

    public void setIdeNumeroDocumento(String ideNumeroDocumento) {
        this.ideNumeroDocumento = ideNumeroDocumento;
    }

    public String getIdeFolio() {
        return ideFolio;
    }

    public void setIdeFolio(String ideFolio) {
        this.ideFolio = ideFolio;
    }

    public String getIdeLibro() {
        return ideLibro;
    }

    public void setIdeLibro(String ideLibro) {
        this.ideLibro = ideLibro;
    }

    public LocalDateTime getIdeUltModFecha() {
        return ideUltModFecha;
    }

    public void setIdeUltModFecha(LocalDateTime ideUltModFecha) {
        this.ideUltModFecha = ideUltModFecha;
    }

    public String getIdeUltModUsuario() {
        return ideUltModUsuario;
    }

    public void setIdeUltModUsuario(String ideUltModUsuario) {
        this.ideUltModUsuario = ideUltModUsuario;
    }

    public Integer getIdeVersion() {
        return ideVersion;
    }

    public void setIdeVersion(Integer ideVersion) {
        this.ideVersion = ideVersion;
    }

    public SgPersona getIdePersona() {
        return idePersona;
    }

    public void setIdePersona(SgPersona idePersona) {
        this.idePersona = idePersona;
    }

    public SgTipoIdentificacion getIdeTipoDocumento() {
        return ideTipoDocumento;
    }

    public void setIdeTipoDocumento(SgTipoIdentificacion ideTipoDocumento) {
        this.ideTipoDocumento = ideTipoDocumento;
    }

    public SgPais getIdePaisEmisor() {
        return idePaisEmisor;
    }

    public void setIdePaisEmisor(SgPais idePaisEmisor) {
        this.idePaisEmisor = idePaisEmisor;
    }

    @Override
    public Long getId() {
        return this.idePk;
    }

    @Override
    public Boolean getIdForView() {
        return this.idePkForView;
    }

    @Override
    public void setId(Long id) {
        this.idePk = id;
    }

    @Override
    public void setIdForView(Boolean valor) {
        this.idePkForView = valor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idePk != null ? idePk.hashCode() : 0);
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
        final SgIdentificacion other = (SgIdentificacion) obj;
        return this.idePk != null && this.idePk.equals(other.idePk);
    }

    public boolean equalsByDocumento(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SgIdentificacion other = (SgIdentificacion) obj;
        if (!Objects.equals(this.ideNumeroDocumento, other.ideNumeroDocumento)) {
            return false;
        }
        if (!Objects.equals(this.ideTipoDocumento, other.ideTipoDocumento)) {
            return false;
        }
        if (!Objects.equals(this.idePaisEmisor, other.idePaisEmisor)) {
            return false;
        }
        return true;
    }
    
    

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgIdentificaciones[ idePk=" + idePk + " ]";
    }

}
