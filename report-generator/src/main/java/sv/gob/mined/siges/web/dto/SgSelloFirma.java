/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "sfiPk", scope = SgSelloFirma.class)
public class SgSelloFirma implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long sfiPk;

    private String sfiComentario;

    private LocalDate sfiFechaDesde;

    private LocalDate sfiFechaHasta;

    private String sfiAclaracionFirma;

    private String sfiNumeroDocumento;

    private LocalDate sfiFechaDocumento;

    private String sfiInstitucion;

    private SgSede sfiSede;

    private SgPersona sfiPersona;
    
    private SgTipoRepresentante sfiTipoRepresentante;

    private SgArchivo sfiFirmaSello;

    private Boolean sfiHabilitado;

    private LocalDateTime sfiUltModFecha;

    private String sfiUltModUsuario;

    private Integer sfiVersion;
    
    
    public SgSelloFirma() {
        this.sfiHabilitado = Boolean.TRUE;
    }

    public Long getSfiPk() {
        return sfiPk;
    }

    public void setSfiPk(Long sfiPk) {
        this.sfiPk = sfiPk;
    }

    public String getSfiComentario() {
        return sfiComentario;
    }

    public void setSfiComentario(String sfiComentario) {
        this.sfiComentario = sfiComentario;
    }

    public LocalDate getSfiFechaDesde() {
        return sfiFechaDesde;
    }

    public void setSfiFechaDesde(LocalDate sfiFechaDesde) {
        this.sfiFechaDesde = sfiFechaDesde;
    }

    public LocalDate getSfiFechaHasta() {
        return sfiFechaHasta;
    }

    public void setSfiFechaHasta(LocalDate sfiFechaHasta) {
        this.sfiFechaHasta = sfiFechaHasta;
    }

    public String getSfiAclaracionFirma() {
        return sfiAclaracionFirma;
    }

    public void setSfiAclaracionFirma(String sfiAclaracionFirma) {
        this.sfiAclaracionFirma = sfiAclaracionFirma;
    }

    public String getSfiNumeroDocumento() {
        return sfiNumeroDocumento;
    }

    public void setSfiNumeroDocumento(String sfiNumeroDocumento) {
        this.sfiNumeroDocumento = sfiNumeroDocumento;
    }

    public LocalDate getSfiFechaDocumento() {
        return sfiFechaDocumento;
    }

    public void setSfiFechaDocumento(LocalDate sfiFechaDocumento) {
        this.sfiFechaDocumento = sfiFechaDocumento;
    }

    public String getSfiInstitucion() {
        return sfiInstitucion;
    }

    public void setSfiInstitucion(String sfiInstitucion) {
        this.sfiInstitucion = sfiInstitucion;
    }

    public SgSede getSfiSede() {
        return sfiSede;
    }

    public void setSfiSede(SgSede sfiSede) {
        this.sfiSede = sfiSede;
    }

    public SgPersona getSfiPersona() {
        return sfiPersona;
    }

    public void setSfiPersona(SgPersona sfiPersona) {
        this.sfiPersona = sfiPersona;
    }

    public SgArchivo getSfiFirmaSello() {
        return sfiFirmaSello;
    }

    public void setSfiFirmaSello(SgArchivo sfiFirmaSello) {
        this.sfiFirmaSello = sfiFirmaSello;
    }  

    public LocalDateTime getSfiUltModFecha() {
        return sfiUltModFecha;
    }

    public void setSfiUltModFecha(LocalDateTime sfiUltModFecha) {
        this.sfiUltModFecha = sfiUltModFecha;
    }

    public String getSfiUltModUsuario() {
        return sfiUltModUsuario;
    }

    public void setSfiUltModUsuario(String sfiUltModUsuario) {
        this.sfiUltModUsuario = sfiUltModUsuario;
    }

    public Integer getSfiVersion() {
        return sfiVersion;
    }

    public void setSfiVersion(Integer sfiVersion) {
        this.sfiVersion = sfiVersion;
    }

    
     public Boolean getSfiHabilitado() {
        return sfiHabilitado;
    }

    public void setSfiHabilitado(Boolean sfiHabilitado) {
        this.sfiHabilitado = sfiHabilitado;
    }

    public SgTipoRepresentante getSfiTipoRepresentante() {
        return sfiTipoRepresentante;
    }

    public void setSfiTipoRepresentante(SgTipoRepresentante sfiTipoRepresentante) {
        this.sfiTipoRepresentante = sfiTipoRepresentante;
    }

    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sfiPk != null ? sfiPk.hashCode() : 0);
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
        final SgSelloFirma other = (SgSelloFirma) obj;
        if (!Objects.equals(this.sfiPk, other.sfiPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgSelloFirma[ sfiPk=" + sfiPk + " ]";
    }
    
}
