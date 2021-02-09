/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import sv.gob.mined.siges.web.dto.catalogo.SgDefinicionTitulo;
import sv.gob.mined.siges.web.dto.catalogo.SgFormula;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "rgpPk", scope = SgRelGradoPlanEstudio.class)
public class SgRelGradoPlanEstudio implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long rgpPk;

    private SgGrado rgpGradoFk;

    private SgPlanEstudio rgpPlanEstudioFk;   

    private SgFormula rgpFormulaFk;

    private Boolean rgpHabilitado;
    
    private Boolean rgpPermiteCalificarSinMatValidada;
    
    private Boolean rgpPermiteCalificarConMatProvisional;

    private LocalDateTime rgpUltModFecha;

    private String rgpUltModUsuario;

    private Integer rgpVersion;
    
    private Boolean rgpConsiderarOrden;
    
    private Boolean rgpPermiteValidarMatriculaSinNIE;
    
    private Boolean rgpRequiereValidacionAcademica;
    
    private SgFormula rgpFormulaHabilitacionPP;

    private SgFormula rgpFormulaHabilitacionSP;

    private SgFormula rgpFormulaHabilitacionPS;
 
    private SgFormula rgpFormulaHabilitacionSS;
    
    private List<SgDefinicionTitulo> rgpDefinicionTitulo;
    
    private Boolean rgpAnual;
    
    public SgRelGradoPlanEstudio() {
        this.rgpHabilitado = Boolean.TRUE;
        this.rgpConsiderarOrden = Boolean.TRUE;
        this.rgpPermiteCalificarSinMatValidada = Boolean.TRUE;
        this.rgpPermiteCalificarConMatProvisional = Boolean.TRUE;
        this.rgpPermiteValidarMatriculaSinNIE = Boolean.TRUE;
        this.rgpRequiereValidacionAcademica = Boolean.TRUE;
        this.rgpAnual=Boolean.TRUE;
    }

    public Long getRgpPk() {
        return rgpPk;
    }

    public void setRgpPk(Long rgpPk) {
        this.rgpPk = rgpPk;
    }

    public SgGrado getRgpGradoFk() {
        return rgpGradoFk;
    }

    public void setRgpGradoFk(SgGrado rgpGradoFk) {
        this.rgpGradoFk = rgpGradoFk;
    }

    public SgPlanEstudio getRgpPlanEstudioFk() {
        return rgpPlanEstudioFk;
    }

    public void setRgpPlanEstudioFk(SgPlanEstudio rgpPlanEstudioFk) {
        this.rgpPlanEstudioFk = rgpPlanEstudioFk;
    }

    public SgFormula getRgpFormulaFk() {
        return rgpFormulaFk;
    }

    public void setRgpFormulaFk(SgFormula rgpFormulaFk) {
        this.rgpFormulaFk = rgpFormulaFk;
    }


    public LocalDateTime getRgpUltModFecha() {
        return rgpUltModFecha;
    }

    public void setRgpUltModFecha(LocalDateTime rgpUltModFecha) {
        this.rgpUltModFecha = rgpUltModFecha;
    }

    public String getRgpUltModUsuario() {
        return rgpUltModUsuario;
    }

    public void setRgpUltModUsuario(String rgpUltModUsuario) {
        this.rgpUltModUsuario = rgpUltModUsuario;
    }

    public Integer getRgpVersion() {
        return rgpVersion;
    }

    public void setRgpVersion(Integer rgpVersion) {
        this.rgpVersion = rgpVersion;
    }

    public Boolean getRgpHabilitado() {
        return rgpHabilitado;
    }

    public void setRgpHabilitado(Boolean rgpHabilitado) {
        this.rgpHabilitado = rgpHabilitado;
    }

    public Boolean getRgpConsiderarOrden() {
        return rgpConsiderarOrden;
    }

    public void setRgpConsiderarOrden(Boolean rgpConsiderarOrden) {
        this.rgpConsiderarOrden = rgpConsiderarOrden;
    }

    public Boolean getRgpPermiteCalificarSinMatValidada() {
        return rgpPermiteCalificarSinMatValidada;
    }

    public void setRgpPermiteCalificarSinMatValidada(Boolean rgpPermiteCalificarSinMatValidada) {
        this.rgpPermiteCalificarSinMatValidada = rgpPermiteCalificarSinMatValidada;
    }

    public Boolean getRgpPermiteCalificarConMatProvisional() {
        return rgpPermiteCalificarConMatProvisional;
    }

    public void setRgpPermiteCalificarConMatProvisional(Boolean rgpPermiteCalificarConMatProvisional) {
        this.rgpPermiteCalificarConMatProvisional = rgpPermiteCalificarConMatProvisional;
    }

    public Boolean getRgpPermiteValidarMatriculaSinNIE() {
        return rgpPermiteValidarMatriculaSinNIE;
    }

    public void setRgpPermiteValidarMatriculaSinNIE(Boolean rgpPermiteValidarMatriculaSinNIE) {
        this.rgpPermiteValidarMatriculaSinNIE = rgpPermiteValidarMatriculaSinNIE;
    }

    public Boolean getRgpRequiereValidacionAcademica() {
        return rgpRequiereValidacionAcademica;
    }

    public void setRgpRequiereValidacionAcademica(Boolean rgpRequiereValidacionAcademica) {
        this.rgpRequiereValidacionAcademica = rgpRequiereValidacionAcademica;
    }

    public SgFormula getRgpFormulaHabilitacionPP() {
        return rgpFormulaHabilitacionPP;
    }

    public void setRgpFormulaHabilitacionPP(SgFormula rgpFormulaHabilitacionPP) {
        this.rgpFormulaHabilitacionPP = rgpFormulaHabilitacionPP;
    }

    public SgFormula getRgpFormulaHabilitacionSP() {
        return rgpFormulaHabilitacionSP;
    }

    public void setRgpFormulaHabilitacionSP(SgFormula rgpFormulaHabilitacionSP) {
        this.rgpFormulaHabilitacionSP = rgpFormulaHabilitacionSP;
    }

    public SgFormula getRgpFormulaHabilitacionPS() {
        return rgpFormulaHabilitacionPS;
    }

    public void setRgpFormulaHabilitacionPS(SgFormula rgpFormulaHabilitacionPS) {
        this.rgpFormulaHabilitacionPS = rgpFormulaHabilitacionPS;
    }

    public SgFormula getRgpFormulaHabilitacionSS() {
        return rgpFormulaHabilitacionSS;
    }

    public void setRgpFormulaHabilitacionSS(SgFormula rgpFormulaHabilitacionSS) {
        this.rgpFormulaHabilitacionSS = rgpFormulaHabilitacionSS;
    }

    public List<SgDefinicionTitulo> getRgpDefinicionTitulo() {
        return rgpDefinicionTitulo;
    }

    public void setRgpDefinicionTitulo(List<SgDefinicionTitulo> rgpDefinicionTitulo) {
        this.rgpDefinicionTitulo = rgpDefinicionTitulo;
    }

    public Boolean getRgpAnual() {
        return rgpAnual;
    }

    public void setRgpAnual(Boolean rgpAnual) {
        this.rgpAnual = rgpAnual;
    }

   
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rgpPk != null ? rgpPk.hashCode() : 0);
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
        final SgRelGradoPlanEstudio other = (SgRelGradoPlanEstudio) obj;
        if (!Objects.equals(this.rgpPk, other.rgpPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgRelGradoPlanEstudio[ rgpPk=" + rgpPk + " ]";
    }
    
}
