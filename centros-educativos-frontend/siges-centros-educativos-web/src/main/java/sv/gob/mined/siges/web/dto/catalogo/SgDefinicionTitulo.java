/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "dtiPk", scope = SgDefinicionTitulo.class)
public class SgDefinicionTitulo implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long dtiPk;

    private String dtiCodigo;

    private String dtiNombre;

    private Boolean dtiHabilitado;
    
    private LocalDate dtiFechaDesde;
    
    private LocalDate dtiFechaHasta;

    private LocalDateTime dtiUltModFecha;

    private String dtiUltModUsuario;

    private Integer dtiVersion;
    
    private SgPlantilla dtiPlantilla;
    
    private String dtiNombreCertificado;
    
    private SgFormula dtiFormula;

    private String dtiEsTipoReposicion;
    
    
    public SgDefinicionTitulo() {
        this.dtiHabilitado = Boolean.TRUE;
    }

    public Long getDtiPk() {
        return dtiPk;
    }

    public void setDtiPk(Long dtiPk) {
        this.dtiPk = dtiPk;
    }

    public String getDtiCodigo() {
        return dtiCodigo;
    }

    public void setDtiCodigo(String dtiCodigo) {
        this.dtiCodigo = dtiCodigo;
    }

    public String getDtiNombre() {
        return dtiNombre;
    }

    public void setDtiNombre(String dtiNombre) {
        this.dtiNombre = dtiNombre;
    }

    public LocalDateTime getDtiUltModFecha() {
        return dtiUltModFecha;
    }

    public void setDtiUltModFecha(LocalDateTime dtiUltModFecha) {
        this.dtiUltModFecha = dtiUltModFecha;
    }

    public String getDtiUltModUsuario() {
        return dtiUltModUsuario;
    }

    public void setDtiUltModUsuario(String dtiUltModUsuario) {
        this.dtiUltModUsuario = dtiUltModUsuario;
    }

    public Integer getDtiVersion() {
        return dtiVersion;
    }

    public void setDtiVersion(Integer dtiVersion) {
        this.dtiVersion = dtiVersion;
    }

    
     public Boolean getDtiHabilitado() {
        return dtiHabilitado;
    }

    public void setDtiHabilitado(Boolean dtiHabilitado) {
        this.dtiHabilitado = dtiHabilitado;
    }

    public LocalDate getDtiFechaDesde() {
        return dtiFechaDesde;
    }

    public void setDtiFechaDesde(LocalDate dtiFechaDesde) {
        this.dtiFechaDesde = dtiFechaDesde;
    }

    public LocalDate getDtiFechaHasta() {
        return dtiFechaHasta;
    }

    public void setDtiFechaHasta(LocalDate dtiFechaHasta) {
        this.dtiFechaHasta = dtiFechaHasta;
    }

    public SgPlantilla getDtiPlantilla() {
        return dtiPlantilla;
    }

    public void setDtiPlantilla(SgPlantilla dtiPlantilla) {
        this.dtiPlantilla = dtiPlantilla;
    }

    public String getDtiNombreCertificado() {
        return dtiNombreCertificado;
    }

    public void setDtiNombreCertificado(String dtiNombreCertificado) {
        this.dtiNombreCertificado = dtiNombreCertificado;
    }

    public SgFormula getDtiFormula() {
        return dtiFormula;
    }

    public void setDtiFormula(SgFormula dtiFormula) {
        this.dtiFormula = dtiFormula;
    }

    public String getDtiEsTipoReposicion() {
        return dtiEsTipoReposicion;
    }

    public void setDtiEsTipoReposicion(String dtiEsTipoReposicion) {
        this.dtiEsTipoReposicion = dtiEsTipoReposicion;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dtiPk != null ? dtiPk.hashCode() : 0);
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
        final SgDefinicionTitulo other = (SgDefinicionTitulo) obj;
        if (!Objects.equals(this.dtiPk, other.dtiPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgDefinicionTitulo[ dtiPk=" + dtiPk + " ]";
    }
    
}
